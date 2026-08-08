package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.CustomColors;
import net.optifine.TextureAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

// [AUDIT-OK] target updateTick()V declared in baseline (deobf:1164); OF updateTick starts with
// if (this.mc.world == null) TextureAnimations.updateAnimations(); mc inherited -> use Minecraft.getMinecraft()
    @Inject(method = "updateTick", at = @At("HEAD"))
    private void optiRefine$updateTickAnimations(CallbackInfo ci) {
        if (Minecraft.getMinecraft().world == null) {
            TextureAnimations.updateAnimations();
        }
    }

// [AUDIT-OK] target renderExpBar(Lnet/minecraft/client/gui/ScaledResolution;I)V matches baseline; drawString(String,III)I invokes present; handler params match
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

// [AUDIT-OK] target renderVignette(F,Lnet/minecraft/client/gui/ScaledResolution;)V matches baseline; replicates OF call-site guard
    @WrapMethod(method = "renderVignette")
    public void injectRenderVignette(float p_180480_1_, ScaledResolution p_180480_2_, Operation<Void> original){
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        } else original.call(p_180480_1_, p_180480_2_);
    }
}
