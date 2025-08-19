package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

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

    @SuppressWarnings("AddedMixinMembersNamePattern")
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static void updateFramebufferSize() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getFramebuffer().createBindFramebuffer(mc.displayWidth, mc.displayHeight);
        if (mc.entityRenderer != null) {
            mc.entityRenderer.updateShaderGroupSize(mc.displayWidth, mc.displayHeight);
        }
    }
}
/*
--- net/minecraft/util/ScreenShotHelper.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/util/ScreenShotHelper.java	Tue Aug 19 14:59:58 2025
@@ -6,54 +6,98 @@
 import java.nio.IntBuffer;
 import java.text.DateFormat;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import javax.annotation.Nullable;
 import javax.imageio.ImageIO;
+import net.minecraft.client.Minecraft;
+import net.minecraft.client.gui.ScaledResolution;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.texture.TextureUtil;
 import net.minecraft.client.shader.Framebuffer;
 import net.minecraft.util.text.ITextComponent;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraft.util.text.TextComponentTranslation;
 import net.minecraft.util.text.event.ClickEvent;
+import net.minecraft.util.text.event.ClickEvent.Action;
+import net.optifine.reflect.Reflector;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 import org.lwjgl.BufferUtils;

 public class ScreenShotHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    public static ITextComponent saveScreenshot(File var0, int var1, int var2, Framebuffer var3) {
-      return saveScreenshot(var0, null, var1, var2, var3);
+      return saveScreenshot(var0, (String)null, var1, var2, var3);
    }

    public static ITextComponent saveScreenshot(File var0, @Nullable String var1, int var2, int var3, Framebuffer var4) {
       try {
          File var5 = new File(var0, "screenshots");
          var5.mkdir();
-         BufferedImage var6 = createScreenshot(var2, var3, var4);
-         File var7;
+         Minecraft var6 = Minecraft.getMinecraft();
+         int var7 = Config.getGameSettings().guiScale;
+         ScaledResolution var8 = new ScaledResolution(var6);
+         int var9 = var8.getScaleFactor();
+         int var10 = Config.getScreenshotSize();
+         boolean var11 = OpenGlHelper.isFramebufferEnabled() && var10 > 1;
+         if (var11) {
+            Config.getGameSettings().guiScale = var9 * var10;
+            resize(var2 * var10, var3 * var10);
+            GlStateManager.pushMatrix();
+            GlStateManager.clear(16640);
+            var6.getFramebuffer().bindFramebuffer(true);
+            var6.entityRenderer.updateCameraAndRender(var6.getRenderPartialTicks(), System.nanoTime());
+         }
+
+         BufferedImage var12 = createScreenshot(var2, var3, var4);
+         if (var11) {
+            var6.getFramebuffer().unbindFramebuffer();
+            GlStateManager.popMatrix();
+            Config.getGameSettings().guiScale = var7;
+            resize(var2, var3);
+         }
+
+         File var13;
          if (var1 == null) {
-            var7 = getTimestampedPNGFileForDirectory(var5);
+            var13 = getTimestampedPNGFileForDirectory(var5);
          } else {
-            var7 = new File(var5, var1);
+            var13 = new File(var5, var1);
+         }
+
+         var13 = var13.getCanonicalFile();
+         Object var14 = null;
+         if (Reflector.ForgeHooksClient_onScreenshot.exists()) {
+            var14 = Reflector.call(Reflector.ForgeHooksClient_onScreenshot, new Object[]{var12, var13});
+            if (Reflector.callBoolean(var14, Reflector.Event_isCanceled, new Object[0])) {
+               return (ITextComponent)Reflector.call(var14, Reflector.ScreenshotEvent_getCancelMessage, new Object[0]);
+            }
+
+            var13 = (File)Reflector.call(var14, Reflector.ScreenshotEvent_getScreenshotFile, new Object[0]);
+         }
+
+         ImageIO.write(var12, "png", var13);
+         TextComponentString var15 = new TextComponentString(var13.getName());
+         var15.getStyle().setClickEvent(new ClickEvent(Action.OPEN_FILE, var13.getAbsolutePath()));
+         var15.getStyle().setUnderlined(true);
+         if (var14 != null) {
+            ITextComponent var16 = (ITextComponent)Reflector.call(var14, Reflector.ScreenshotEvent_getResultMessage, new Object[0]);
+            if (var16 != null) {
+               return var16;
+            }
          }

-         ImageIO.write(var6, "png", var7);
-         TextComponentString var8 = new TextComponentString(var7.getName());
-         var8.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, var7.getAbsolutePath()));
-         var8.getStyle().setUnderlined(true);
-         return new TextComponentTranslation("screenshot.success", var8);
-      } catch (Exception var9) {
-         LOGGER.warn("Couldn't save screenshot", var9);
-         return new TextComponentTranslation("screenshot.failure", var9.getMessage());
+         return new TextComponentTranslation("screenshot.success", new Object[]{var15});
+      } catch (Exception var17) {
+         LOGGER.warn("Couldn't save screenshot", var17);
+         return new TextComponentTranslation("screenshot.failure", new Object[]{var17.getMessage()});
       }
    }

    public static BufferedImage createScreenshot(int var0, int var1, Framebuffer var2) {
       if (OpenGlHelper.isFramebufferEnabled()) {
          var0 = var2.framebufferTextureWidth;
@@ -81,19 +125,39 @@
       BufferedImage var4 = new BufferedImage(var0, var1, 1);
       var4.setRGB(0, 0, var0, var1, pixelValues, 0, var0);
       return var4;
    }

    private static File getTimestampedPNGFileForDirectory(File var0) {
-      String var2 = DATE_FORMAT.format(new Date()).toString();
-      int var3 = 1;
+      String var1 = DATE_FORMAT.format(new Date()).toString();
+      int var2 = 1;

       while (true) {
-         File var1 = new File(var0, var2 + (var3 == 1 ? "" : "_" + var3) + ".png");
-         if (!var1.exists()) {
-            return var1;
+         File var3 = new File(var0, var1 + (var2 == 1 ? "" : "_" + var2) + ".png");
+         if (!var3.exists()) {
+            return var3;
          }

-         var3++;
+         var2++;
+      }
+   }
+
+   private static void resize(int var0, int var1) {
+      Minecraft var2 = Minecraft.getMinecraft();
+      var2.displayWidth = Math.max(1, var0);
+      var2.displayHeight = Math.max(1, var1);
+      if (var2.currentScreen != null) {
+         ScaledResolution var3 = new ScaledResolution(var2);
+         var2.currentScreen.onResize(var2, var3.getScaledWidth(), var3.getScaledHeight());
+      }
+
+      updateFramebufferSize();
+   }
+
+   private static void updateFramebufferSize() {
+      Minecraft var0 = Minecraft.getMinecraft();
+      var0.getFramebuffer().createBindFramebuffer(var0.displayWidth, var0.displayHeight);
+      if (var0.entityRenderer != null) {
+         var0.entityRenderer.updateShaderGroupSize(var0.displayWidth, var0.displayHeight);
       }
    }
 }
 */
