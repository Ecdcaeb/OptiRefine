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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @Expression("? = ? + 25.0")
    @Inject(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    // [AUDIT-OK] '? = ? + 25.0' matches f1 += 25.0F sneaking branch (deobf:57, unique); translate matches OF sneaking block
    public void transferSneaking(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci){
        GlStateManager.translate(0.0F, 0.142F, -0.0178F);
    }

    @Expression("? = @(? * 100.0)")
    @ModifyExpressionValue(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    // [AUDIT-OK] '(? * 100.0)' ordinal 0 = f2; [0,165] clamp equivalent to OF:135-138
    public float fixf2(float f2){
        if (f2 < 0.0F) {
            return  0.0F;
        }

        return Math.min(f2, 165.0F);
    }

    @ModifyExpressionValue(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal = 0))
    // [AUDIT-OK] MathHelper.clamp(FFF) ordinal 0 unique (deobf:43); -5 lower bound equivalent to OF:139 (applied to pre-sin value, same as OF)
    public float fixf1(float f1){
        return f1 < -5.0F ? -5.0F : f1;
    }

}
