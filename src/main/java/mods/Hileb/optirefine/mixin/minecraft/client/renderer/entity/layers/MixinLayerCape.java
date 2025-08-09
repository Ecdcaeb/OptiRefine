package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity.layers;

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
