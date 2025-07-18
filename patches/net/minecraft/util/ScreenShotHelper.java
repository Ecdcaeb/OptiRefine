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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

public class ScreenShotHelper {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
   private static IntBuffer pixelBuffer;
   private static int[] pixelValues;

   public static ITextComponent saveScreenshot(File var0, int var1, int var2, Framebuffer var3) {
      return saveScreenshot(☃, null, ☃, ☃, ☃);
   }

   public static ITextComponent saveScreenshot(File var0, @Nullable String var1, int var2, int var3, Framebuffer var4) {
      try {
         File ☃ = new File(☃, "screenshots");
         ☃.mkdir();
         BufferedImage ☃x = createScreenshot(☃, ☃, ☃);
         File ☃xx;
         if (☃ == null) {
            ☃xx = getTimestampedPNGFileForDirectory(☃);
         } else {
            ☃xx = new File(☃, ☃);
         }

         ImageIO.write(☃x, "png", ☃xx);
         ITextComponent ☃xxx = new TextComponentString(☃xx.getName());
         ☃xxx.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, ☃xx.getAbsolutePath()));
         ☃xxx.getStyle().setUnderlined(true);
         return new TextComponentTranslation("screenshot.success", ☃xxx);
      } catch (Exception var9) {
         LOGGER.warn("Couldn't save screenshot", var9);
         return new TextComponentTranslation("screenshot.failure", var9.getMessage());
      }
   }

   public static BufferedImage createScreenshot(int var0, int var1, Framebuffer var2) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         ☃ = ☃.framebufferTextureWidth;
         ☃ = ☃.framebufferTextureHeight;
      }

      int ☃ = ☃ * ☃;
      if (pixelBuffer == null || pixelBuffer.capacity() < ☃) {
         pixelBuffer = BufferUtils.createIntBuffer(☃);
         pixelValues = new int[☃];
      }

      GlStateManager.glPixelStorei(3333, 1);
      GlStateManager.glPixelStorei(3317, 1);
      ((Buffer)pixelBuffer).clear();
      if (OpenGlHelper.isFramebufferEnabled()) {
         GlStateManager.bindTexture(☃.framebufferTexture);
         GlStateManager.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
      } else {
         GlStateManager.glReadPixels(0, 0, ☃, ☃, 32993, 33639, pixelBuffer);
      }

      pixelBuffer.get(pixelValues);
      TextureUtil.processPixelValues(pixelValues, ☃, ☃);
      BufferedImage ☃x = new BufferedImage(☃, ☃, 1);
      ☃x.setRGB(0, 0, ☃, ☃, pixelValues, 0, ☃);
      return ☃x;
   }

   private static File getTimestampedPNGFileForDirectory(File var0) {
      String ☃ = DATE_FORMAT.format(new Date()).toString();
      int ☃x = 1;

      while (true) {
         File ☃xx = new File(☃, ☃ + (☃x == 1 ? "" : "_" + ☃x) + ".png");
         if (!☃xx.exists()) {
            return ☃xx;
         }

         ☃x++;
      }
   }
}
