package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.optifine.Config;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * OptiFine rewrites {@code GL11.glEnable}/{@code glDisable} inside
 * {@code GlStateManager.BooleanState.setState} to {@code Shaders.glEnableWrapper}/
 * {@code glDisableWrapper}. Without this, {@code GuiSlot.drawSelectionBox}'s
 * {@code disableTexture2D} never switches the shader off {@code ProgramTexturedLit}
 * into {@code ProgramBasic}, and the following GUI 3D item render stays on a
 * textured-lit program with GL_TEXTURE_2D disabled — selected-row icons go black.
 */
@Mixin(targets = "net.minecraft.client.renderer.GlStateManager$BooleanState")
public abstract class MixinGlStateManager$BooleanState {

    @Redirect(method = "setState", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V"))
    private void optiRefine$glEnableWrapper(int cap) {
        if (Config.isShaders()) {
            Shaders.glEnableWrapper(cap);
        } else {
            GL11.glEnable(cap);
        }
    }

    @Redirect(method = "setState", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V"))
    private void optiRefine$glDisableWrapper(int cap) {
        if (Config.isShaders()) {
            Shaders.glDisableWrapper(cap);
        } else {
            GL11.glDisable(cap);
        }
    }
}
