package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLiving.class)
public abstract class MixinRenderLiving {
    @WrapMethod(method = "renderLeash")
    public void blockRenderLeashForConfig(EntityLiving entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks, Operation<Void> original){
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            original.call(entityLivingIn, x, y, z, entityYaw, partialTicks);
        }
    }

    @Inject(method = "renderLeash", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableCull()V"))
    public void beforeRenderLeashForConfig(EntityLiving entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci){
        if (Config.isShaders()) {
            Shaders.beginLeash();
        }
    }


    @WrapOperation(method = "renderLeash", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableLighting()V"))
    public void afterRenderLeashForConfig(Operation<Void> original){
        if (Config.isShaders()) {
            Shaders.endLeash();
        }
        original.call();
    }
}
