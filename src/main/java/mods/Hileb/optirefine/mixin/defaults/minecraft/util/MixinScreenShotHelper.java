package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
@Mixin(ScreenShotHelper.class)
public abstract class MixinScreenShotHelper {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1


    @WrapOperation(method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ScreenShotHelper;createScreenshot(IILnet/minecraft/client/shader/Framebuffer;)Ljava/awt/image/BufferedImage;"))
    // Restore the display and matrix stack even when rendering or pixel capture fails.
    private static BufferedImage optiRefine$captureScaledScreenshot(int width, int height, Framebuffer framebufferIn, Operation<BufferedImage> original){
        Minecraft mc = Minecraft.getMinecraft();
        int guiScaleOld = Config.getGameSettings().guiScale;
        ScaledResolution sr = new ScaledResolution(mc);
        int guiScale = sr.getScaleFactor();
        int mul = Config.getScreenshotSize();
        boolean resize = OpenGlHelper.isFramebufferEnabled() && mul > 1;
        if (!resize) {
            return original.call(width, height, framebufferIn);
        }
        boolean matrixPushed = false;
        int matrixMode = GL11.glGetInteger(GL11.GL_MATRIX_MODE);
        Config.getGameSettings().guiScale = guiScale * mul;
        try {
            mc.resize(width * mul, height * mul);
            GlStateManager.matrixMode(matrixMode);
            GlStateManager.pushMatrix();
            matrixPushed = true;
            GlStateManager.clear(16640);
            mc.getFramebuffer().bindFramebuffer(true);
            mc.entityRenderer.updateCameraAndRender(mc.getRenderPartialTicks(), System.nanoTime());
            return original.call(width, height, framebufferIn);
        } finally {
            try {
                if (matrixPushed) {
                    try {
                        mc.getFramebuffer().unbindFramebuffer();
                    } finally {
                        GlStateManager.matrixMode(matrixMode);
                        GlStateManager.popMatrix();
                    }
                }
            } finally {
                Config.getGameSettings().guiScale = guiScaleOld;
                mc.resize(width, height);
            }
        }
    }

}
