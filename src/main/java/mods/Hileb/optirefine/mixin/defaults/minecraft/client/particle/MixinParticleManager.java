package mods.Hileb.optirefine.mixin.defaults.minecraft.client.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.common.utils.Caster;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] target addEffect(Lnet/minecraft/client/particle/Particle;)V matches baseline; queue.add invoke present; handler params match
    @WrapWithCondition(method = "addEffect", at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z"))
    public boolean injectAddEffect(Queue<?> instance, Object effect) {
        return !(effect instanceof ParticleFirework.Spark) || Config.isFireworkParticles();
    }

// [AUDIT-OK] baseline method tickParticle(Lnet/minecraft/client/particle/Particle;)V declared in ParticleManager
    @Shadow
    protected abstract void tickParticle(Particle particle);

// [AUDIT-OK] target tickParticleList(Ljava/util/Queue;)V declared in baseline; handler params match
    @WrapMethod(method = "tickParticleList")
    private void tickParticleList(Queue<Particle> particlesToTick, Operation<Void> original) {
        if (!particlesToTick.isEmpty()) {
            long timeStartMs = System.currentTimeMillis();
            int countLeft = particlesToTick.size();
            Iterator<Particle> iterator = particlesToTick.iterator();

            while (iterator.hasNext()) {
                Particle particle = iterator.next();
                this.tickParticle(particle);
                if (!particle.isAlive()) {
                    iterator.remove();
                }

                countLeft--;
                if (System.currentTimeMillis() > timeStartMs + 20L) {
                    break;
                }
            }

            if (countLeft > 0) {
                int countToRemove = countLeft;

                for (Iterator<Particle> it = particlesToTick.iterator(); it.hasNext() && countToRemove > 0; countToRemove--) {
                    Particle particlex = it.next();
                    particlex.setExpired();
                    it.remove();
                }
            }
        }
    }

// [AUDIT-OK] target updateEffects()V matches baseline; fxLayers ArrayDeque.add invoke present; handler params match
    @WrapOperation(method = "updateEffects", at = @At(value = "INVOKE", target = "Ljava/util/ArrayDeque;add(Ljava/lang/Object;)Z"))
    public boolean fixLayers(ArrayDeque<Object> fxLayersJK, Object particle, Operation<Boolean> original){
        if (!(particle instanceof Barrier) || !this.reuseBarrierParticle((Particle) particle, Caster.cast(fxLayersJK))) {
            fxLayersJK.add(particle);
        }

        return false;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added helper, not in baseline
    @Unique
    private boolean reuseBarrierParticle(Particle entityfx, ArrayDeque<Particle> deque) {
        for (Particle efx : deque) {
            if (efx instanceof Barrier && entityfx.prevPosX == efx.prevPosX && entityfx.prevPosY == efx.prevPosY && entityfx.prevPosZ == efx.prevPosZ) {
                efx.particleAge = 0;
                return true;
            }
        }

        return false;
    }

}
