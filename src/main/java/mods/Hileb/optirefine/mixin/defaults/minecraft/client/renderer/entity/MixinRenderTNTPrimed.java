package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderTNTPrimed.class)
public abstract class MixinRenderTNTPrimed extends Render<EntityTNTPrimed> {
    protected MixinRenderTNTPrimed(RenderManager manager) {
        super(manager);
    }

    @WrapOperation(method = "doRender(Lnet/minecraft/entity/item/EntityTNTPrimed;DDDFF)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderBlockBrightness(Lnet/minecraft/block/state/IBlockState;F)V", ordinal = 0))
    private void optiRefine$shaderTntFlash(BlockRendererDispatcher renderer, IBlockState state, float brightness,
                                         Operation<Void> draw, @Local(argsOnly = true) EntityTNTPrimed tnt,
                                         @Local(ordinal = 2) float flashAlpha, @Share("shaderFlash") LocalBooleanRef shaderFlash) {
        if (!this.renderOutlines && tnt.getFuse() / 5 % 2 == 0 && Config.isShaders() && Shaders.isRenderingWorld
                && !Shaders.isShadowPass && Shaders.uniform_entityColor.isDefined()) {
            float[] color = Shaders.uniform_entityColor.getValue();
            boolean initialized = color[0] != -Float.MAX_VALUE;
            float red = initialized ? color[0] : 0.0F;
            float green = initialized ? color[1] : 0.0F;
            float blue = initialized ? color[2] : 0.0F;
            float alpha = initialized ? color[3] : 0.0F;
            Shaders.setEntityColor(1.0F, 1.0F, 1.0F, flashAlpha);
            try {
                draw.call(renderer, state, brightness);
                shaderFlash.set(true);
            } finally {
                Shaders.setEntityColor(red, green, blue, alpha);
            }
        } else {
            draw.call(renderer, state, brightness);
        }
    }

    @WrapWithCondition(method = "doRender(Lnet/minecraft/entity/item/EntityTNTPrimed;DDDFF)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderBlockBrightness(Lnet/minecraft/block/state/IBlockState;F)V", ordinal = 2))
    private boolean optiRefine$useVanillaTntFlash(BlockRendererDispatcher renderer, IBlockState state, float brightness,
                                                @Share("shaderFlash") LocalBooleanRef shaderFlash) {
        return !shaderFlash.get();
    }
}
