package mods.Hileb.optirefine.mixin.defaults.minecraft.client.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.common.utils.Caster;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleSuspend;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

// [AUDIT-OK] baseline member world (World), declared in ParticleManager
    @Shadow
    protected World world;

// [AUDIT-FIXED] OF renderParticles computes the camera-in-water flag once at method start
// (ActiveRenderInfo.getBlockStateAtEntityViewpoint(...).getMaterial() == Material.WATER)
// and gates the render call with "if (var9 || !(var16 instanceof ParticleSuspend))"; vanilla has no flag.
    @Inject(method = "renderParticles", at = @At("HEAD"))
    public void optiRefine$computeCameraInWater(Entity entityIn, float partialTicks, CallbackInfo ci,
                                                @Share(namespace = "optirefine", value = "cameraInWater") LocalRef<Boolean> cameraInWater) {
        cameraInWater.set(ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.world, entityIn, partialTicks).getMaterial() == Material.WATER);
    }

    @WrapOperation(method = "renderParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;renderParticle(Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFFFF)V"))
// [AUDIT-FIXED] OF renderParticles:312-313 skips ParticleSuspend when the camera block is WATER
    public void optiRefine$skipSuspendInWater(BufferBuilder bufferbuilder, Entity entityIn, float partialTicks, float f1, float f2, float f3, float f4, float f5, Operation<Void> original,
                                              @Local Particle particle,
                                              @Share(namespace = "optirefine", value = "cameraInWater") LocalRef<Boolean> cameraInWater) {
        Boolean inWater = cameraInWater.get();
        if (inWater == null || !inWater || !(particle instanceof ParticleSuspend)) {
            original.call(bufferbuilder, entityIn, partialTicks, f1, f2, f3, f4, f5);
        }
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
