package net.minecraft.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.optifine.reflect.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

public class ScreenShotHelper {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
   private static IntBuffer pixelBuffer;
   private static int[] pixelValues;

   public static ITextComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer) {
      return saveScreenshot(gameDirectory, (String)null, width, height, buffer);
   }

   public static ITextComponent saveScreenshot(File gameDirectory, @Nullable String screenshotName, int width, int height, Framebuffer buffer) {
      try {
         File file1 = new File(gameDirectory, "screenshots");
         file1.mkdir();
         Minecraft mc = Minecraft.getMinecraft();
         int guiScaleOld = Config.getGameSettings().guiScale;
         ScaledResolution sr = new ScaledResolution(mc);
         int guiScale = sr.getScaleFactor();
         int mul = Config.getScreenshotSize();
         boolean resize = OpenGlHelper.isFramebufferEnabled() && mul > 1;
         if (resize) {
            Config.getGameSettings().guiScale = guiScale * mul;
            resize(width * mul, height * mul);
            GlStateManager.pushMatrix();
            GlStateManager.clear(16640);
            mc.getFramebuffer().bindFramebuffer(true);
            mc.entityRenderer.updateCameraAndRender(mc.getRenderPartialTicks(), System.nanoTime());
         }

         BufferedImage bufferedimage = createScreenshot(width, height, buffer);
         if (resize) {
            mc.getFramebuffer().unbindFramebuffer();
            GlStateManager.popMatrix();
            Config.getGameSettings().guiScale = guiScaleOld;
            resize(width, height);
         }

         File file2;
         if (screenshotName == null) {
            file2 = getTimestampedPNGFileForDirectory(file1);
         } else {
            file2 = new File(file1, screenshotName);
         }

         file2 = file2.getCanonicalFile();
         Object event = null;
         if (Reflector.ForgeHooksClient_onScreenshot.exists()) {
            event = Reflector.call(Reflector.ForgeHooksClient_onScreenshot, new Object[]{bufferedimage, file2});
            if (Reflector.callBoolean(event, Reflector.Event_isCanceled, new Object[0])) {
               return (ITextComponent)Reflector.call(event, Reflector.ScreenshotEvent_getCancelMessage, new Object[0]);
            }

            file2 = (File)Reflector.call(event, Reflector.ScreenshotEvent_getScreenshotFile, new Object[0]);
         }

         ImageIO.write(bufferedimage, "png", file2);
         ITextComponent itextcomponent = new TextComponentString(file2.getName());
         itextcomponent.getStyle().setClickEvent(new ClickEvent(Action.OPEN_FILE, file2.getAbsolutePath()));
         itextcomponent.getStyle().setUnderlined(true);
         if (event != null) {
            ITextComponent resultMessage = (ITextComponent)Reflector.call(event, Reflector.ScreenshotEvent_getResultMessage, new Object[0]);
            if (resultMessage != null) {
               return resultMessage;
            }
         }

         return new TextComponentTranslation("screenshot.success", new Object[]{itextcomponent});
      } catch (Exception var17) {
         LOGGER.warn("Couldn't save screenshot", var17);
         return new TextComponentTranslation("screenshot.failure", new Object[]{var17.getMessage()});
      }
   }

   public static BufferedImage createScreenshot(int width, int height, Framebuffer framebufferIn) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         width = framebufferIn.framebufferTextureWidth;
         height = framebufferIn.framebufferTextureHeight;
      }

      int i = width * height;
      if (pixelBuffer == null || pixelBuffer.capacity() < i) {
         pixelBuffer = BufferUtils.createIntBuffer(i);
         pixelValues = new int[i];
      }

      GlStateManager.glPixelStorei(3333, 1);
      GlStateManager.glPixelStorei(3317, 1);
      ((Buffer)pixelBuffer).clear();
      if (OpenGlHelper.isFramebufferEnabled()) {
         GlStateManager.bindTexture(framebufferIn.framebufferTexture);
         GlStateManager.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
      } else {
         GlStateManager.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
      }

      pixelBuffer.get(pixelValues);
      TextureUtil.processPixelValues(pixelValues, width, height);
      BufferedImage bufferedimage = new BufferedImage(width, height, 1);
      bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
      return bufferedimage;
   }

   private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
      String s = DATE_FORMAT.format(new Date()).toString();
      int i = 1;

      while (true) {
         File file1 = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");
         if (!file1.exists()) {
            return file1;
         }

         i++;
      }
   }

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

   private static void updateFramebufferSize() {
      Minecraft mc = Minecraft.getMinecraft();
      mc.getFramebuffer().createBindFramebuffer(mc.displayWidth, mc.displayHeight);
      if (mc.entityRenderer != null) {
         mc.entityRenderer.updateShaderGroupSize(mc.displayWidth, mc.displayHeight);
      }
   }
}
