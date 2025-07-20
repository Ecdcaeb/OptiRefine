package mods.Hileb.optirefine.mixin.minecraft.client.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {
    @Shadow
    @Final
    private ArrayDeque<Particle>[][] fxLayers;

    @Shadow
    @Final
    private Queue<Particle> queue;

    @WrapWithCondition(method = "addEffect", at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z"))
    public boolean injectAddEffect(Queue<?> instance, Object effect) {
        return !(effect instanceof ParticleFirework.Spark) || Config.isFireworkParticles();
    }

    @Shadow
    protected abstract void tickParticle(Particle particle);

    /**
     * @author HILEB
     * @reason TODO
     */
    @Overwrite
    private void tickParticleList(Queue<Particle> p_187240_1_) {
        if (!p_187240_1_.isEmpty()) {
            long timeStartMs = System.currentTimeMillis();
            int countLeft = p_187240_1_.size();
            Iterator<Particle> iterator = p_187240_1_.iterator();

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

                for (Iterator<Particle> it = p_187240_1_.iterator(); it.hasNext() && countToRemove > 0; countToRemove--) {
                    Particle particlex = it.next();
                    particlex.setExpired();
                    it.remove();
                }
            }
        }
    }

    @Inject(method = "updateEffects", at = @At("RETURN"))
    public void injectUpdateEffects(CallbackInfo ci){
        if (!this.queue.isEmpty()) {
            for (Particle particle = this.queue.poll(); particle != null; particle = this.queue.poll()) {
                int j = particle.getFXLayer();
                int k = particle.shouldDisableDepth() ? 0 : 1;
                if (this.fxLayers[j][k].size() >= 16384) {
                    this.fxLayers[j][k].removeFirst();
                }

                if (!(particle instanceof Barrier) || !this.reuseBarrierParticle(particle, this.fxLayers[j][k])) {
                    this.fxLayers[j][k].add(particle);
                }
            }
        }
    }

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
