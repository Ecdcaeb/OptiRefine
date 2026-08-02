package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.common.utils.Checked;
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

@Checked
@Mixin(ScreenShotHelper.class)
public abstract class MixinScreenShotHelper {

    @WrapOperation(method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ScreenShotHelper;createScreenshot(IILnet/minecraft/client/shader/Framebuffer;)Ljava/awt/image/BufferedImage;"))
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
            Config.getGameSettings().guiScale = guiScale;
            mc.resize(width, height);
        }
        return var12;
    }

}
