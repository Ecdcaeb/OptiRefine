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

import java.awt.image.BufferedImage;
@Mixin(ScreenShotHelper.class)
public abstract class MixinScreenShotHelper {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1


    @WrapOperation(method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ScreenShotHelper;createScreenshot(IILnet/minecraft/client/shader/Framebuffer;)Ljava/awt/image/BufferedImage;"))
// [AUDIT-OK] resize/capture/restore flow around createScreenshot matches OF saveScreenshot (guiScale*guiScale-mul, clear 16640, updateCameraAndRender, restore)
// [AUDIT-ISSUE] guiScale is restored from sr.getScaleFactor() instead of the saved guiScaleOld (declared unused) - with guiScale=0 (Auto) the setting permanently becomes a fixed scale after the first screenshot. Fix: Config.getGameSettings().guiScale = guiScaleOld;
    private static BufferedImage beforecreateScreenshot(int width, int height, Framebuffer framebufferIn, Operation<BufferedImage> original){
        Minecraft mc = Minecraft.getMinecraft();
        //noinspection unused
        int guiScaleOld = Config.getGameSettings().guiScale;
        ScaledResolution sr = new ScaledResolution(mc);
        int guiScale = sr.getScaleFactor();
        int mul = Config.getScreenshotSize();
        boolean resize =OpenGlHelper.isFramebufferEnabled() && mul > 1;
        if (resize) {
            Config.getGameSettings().guiScale = guiScale * mul;
            Minecraft.getMinecraft().resize(width * mul, height * mul);
            GlStateManager.pushMatrix();
            GlStateManager.clear(16640);
            mc.getFramebuffer().bindFramebuffer(true);
            mc.entityRenderer.updateCameraAndRender(mc.getRenderPartialTicks(), System.nanoTime());
        }
        BufferedImage var12 = original.call(width, height, framebufferIn);
        if (resize) {
            mc.getFramebuffer().unbindFramebuffer();
            GlStateManager.popMatrix();
            // [AUDIT-FIXED] restore original setting (guiScale=0 Auto must not become a fixed scale)
            Config.getGameSettings().guiScale = guiScaleOld;
            mc.resize(width, height);
        }
        return var12;
    }

}
