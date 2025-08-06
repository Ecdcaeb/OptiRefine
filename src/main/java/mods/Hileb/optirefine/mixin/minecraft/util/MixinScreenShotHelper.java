package mods.Hileb.optirefine.mixin.minecraft.util;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import jakarta.annotation.Nullable;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(ScreenShotHelper.class) //TODO
public abstract class MixinScreenShotHelper {

    @Inject(method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ScreenShotHelper;createScreenshot(IILnet/minecraft/client/shader/Framebuffer;)Ljava/awt/image/BufferedImage;"))
    private static void beforecreateScreenshot(File gameDirectory, @Nullable String screenshotName, int width, int height, Framebuffer buffer, CallbackInfoReturnable<ITextComponent> cir, @Share("resize") LocalBooleanRef resize){
        Minecraft mc = Minecraft.getMinecraft();
        //noinspection unused
        int guiScaleOld = Config.getGameSettings().guiScale;
        ScaledResolution sr = new ScaledResolution(mc);
        int guiScale = sr.getScaleFactor();
        int mul = Config.getScreenshotSize();
        resize.set(OpenGlHelper.isFramebufferEnabled() && mul > 1);
        if (resize.get()) {
            Config.getGameSettings().guiScale = guiScale * mul;
            resize(width * mul, height * mul);
            GlStateManager.pushMatrix();
            GlStateManager.clear(16640);
            mc.getFramebuffer().bindFramebuffer(true);
            mc.entityRenderer.updateCameraAndRender(mc.getRenderPartialTicks(), System.nanoTime());
        }
    }

    @Unique
    private static void resize(int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.displayWidth = Math.max(1, width);
        mc.displayHeight = Math.max(1, height);
        if (mc.currentScreen != null) {
            ScaledResolution sr = new ScaledResolution(mc);
            mc.currentScreen.onResize(mc, sr.getScaledWidth(), sr.getScaledHeight());
        }

        updateFramebufferSize();
    }

    @Unique
    private static void updateFramebufferSize() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getFramebuffer().createBindFramebuffer(mc.displayWidth, mc.displayHeight);
        if (mc.entityRenderer != null) {
            mc.entityRenderer.updateShaderGroupSize(mc.displayWidth, mc.displayHeight);
        }
    }
}
