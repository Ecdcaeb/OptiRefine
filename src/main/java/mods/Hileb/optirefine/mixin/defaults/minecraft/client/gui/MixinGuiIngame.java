package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {
    @WrapOperation(method = "renderExpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    public int redirectDrawString(FontRenderer instance, String text, int x, int y, int color, Operation<Integer> original){
        if (color == 0) {
            return original.call(instance, text, x, y, color);
        } else {
            int col = color;
            if (Config.isCustomColors()) {
                 col = CustomColors.getExpBarTextColor(color);
            }
            return original.call(instance, text, x, y, col);
        }
    }

    @WrapMethod(method = "renderVignette")
    public void injectRenderVignette(float p_180480_1_, ScaledResolution p_180480_2_, Operation<Void> original){
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        } else original.call(p_180480_1_, p_180480_2_);
    }
}
