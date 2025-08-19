package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerCape.class)
public abstract class MixinLayerCape {
    @Expression("? = ? + 25.0")
    @Inject(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    public void transferSneaking(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci){
        GlStateManager.translate(0.0F, 0.142F, -0.0178F);
    }

    @Expression("? = @(? * 100.0)")
    @ModifyExpressionValue(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public float fixf2(float f2){
        if (f2 < 0.0F) {
            return  0.0F;
        }

        return Math.min(f2, 165.0F);
    }

}
/*
+++ net/minecraft/client/renderer/entity/layers/LayerCape.java	Tue Aug 19 14:59:58 2025
@@ -35,18 +35,27 @@
             float var22 = (float)(var10 * var17 + var14 * var19) * 100.0F;
             float var23 = (float)(var10 * var19 - var14 * var17) * 100.0F;
             if (var22 < 0.0F) {
                var22 = 0.0F;
             }

+            if (var22 > 165.0F) {
+               var22 = 165.0F;
+            }
+
+            if (var21 < -5.0F) {
+               var21 = -5.0F;
+            }
+
             float var24 = var1.prevCameraYaw + (var1.cameraYaw - var1.prevCameraYaw) * var4;
             var21 += MathHelper.sin((var1.prevDistanceWalkedModified + (var1.distanceWalkedModified - var1.prevDistanceWalkedModified) * var4) * 6.0F)
                * 32.0F
                * var24;
             if (var1.isSneaking()) {
                var21 += 25.0F;
+               GlStateManager.translate(0.0F, 0.142F, -0.0178F);
             }

             GlStateManager.rotate(6.0F + var22 / 2.0F + var21, 1.0F, 0.0F, 0.0F);
             GlStateManager.rotate(var23 / 2.0F, 0.0F, 0.0F, 1.0F);
             GlStateManager.rotate(-var23 / 2.0F, 0.0F, 1.0F, 0.0F);
             GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
 */
