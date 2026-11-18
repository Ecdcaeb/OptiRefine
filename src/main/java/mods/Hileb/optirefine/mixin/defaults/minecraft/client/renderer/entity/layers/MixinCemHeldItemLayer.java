package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import mods.Hileb.optirefine.optifine.cem.CemHandAttachments;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerHeldItem.class)
public abstract class MixinCemHeldItemLayer {
    @Shadow @Final protected RenderLivingBase<?> livingEntityRenderer;

    @WrapOperation(method = "renderHeldItem", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/layers/LayerHeldItem;translateToHand(Lnet/minecraft/util/EnumHandSide;)V"))
    private void optiRefine$translateToAnchor(LayerHeldItem layer, EnumHandSide side, Operation<Void> original,
                                             @Share("cemAnchor") LocalBooleanRef anchored) {
        if (CemHandAttachments.apply(this.livingEntityRenderer.getMainModel(), side, 0.0625F)) {
            anchored.set(true);
        } else {
            original.call(layer, side);
        }
    }

    @WrapOperation(method = "renderHeldItem", at = @At(value = "INVOKE", ordinal = 1,
            target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"))
    private void optiRefine$keepVanillaHandOffset(float x, float y, float z, Operation<Void> original,
                                                 @Share("cemAnchor") LocalBooleanRef anchored) {
        if (!anchored.get()) {
            original.call(x, y, z);
        }
    }
}
