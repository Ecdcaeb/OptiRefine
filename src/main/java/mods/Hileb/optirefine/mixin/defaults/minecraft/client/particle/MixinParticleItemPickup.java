package mods.Hileb.optirefine.mixin.defaults.minecraft.client.particle;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.particle.ParticleItemPickup;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ParticleItemPickup.class)
public abstract class MixinParticleItemPickup {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

// [AUDIT-OK] baseline member item exists in target class
    @Shadow
    @Final
    private Entity item;

// [AUDIT-OK] target renderParticle(Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFFF)V matches baseline; handler params match
    @Inject(method = "renderParticle", at = @At("HEAD"))
    public void preInjectRenderParticle(BufferBuilder buffer,
                                        Entity entityIn,
                                        float partialTicks,
                                        float rotationX, float rotationZ, float rotationYZ,
                                        float rotationXY, float rotationXZ,
                                        CallbackInfo ci, @Share("oldShadersProgram")LocalRef<Program> oldShadersProgram){
        oldShadersProgram.set(null);
        if (Config.isShaders()) {
            oldShadersProgram.set(Shaders.activeProgram);
            Shaders.nextEntity(this.item);
        }

    }

// [AUDIT-OK] target renderParticle matches baseline; RETURN restores shader program
    @Inject(method = "renderParticle", at = @At("RETURN"))
    public void postInjectRenderParticle(BufferBuilder buffer,
                                        Entity entityIn,
                                        float partialTicks,
                                        float rotationX, float rotationZ, float rotationYZ,
                                        float rotationXY, float rotationXZ,
                                        CallbackInfo ci, @Share("oldShadersProgram")LocalRef<Program> oldShadersProgram){
        if (Config.isShaders()) {
            Shaders.setEntityId(null);
            Shaders.useProgram(oldShadersProgram.get());
        }
    }

}
