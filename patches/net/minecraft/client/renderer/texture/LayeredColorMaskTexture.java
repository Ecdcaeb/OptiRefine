package net.minecraft.client.renderer.texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredColorMaskTexture extends AbstractTexture {
   private static final Logger LOGGER = LogManager.getLogger();
   private final ResourceLocation textureLocation;
   private final List<String> listTextures;
   private final List<EnumDyeColor> listDyeColors;

   public LayeredColorMaskTexture(ResourceLocation var1, List<String> var2, List<EnumDyeColor> var3) {
      this.textureLocation = ☃;
      this.listTextures = ☃;
      this.listDyeColors = ☃;
   }

   @Override
   public void loadTexture(IResourceManager var1) throws IOException {
      this.deleteGlTexture();
      IResource ☃ = null;

      BufferedImage ☃x;
      label198: {
         try {
            ☃ = ☃.getResource(this.textureLocation);
            BufferedImage ☃xx = TextureUtil.readBufferedImage(☃.getInputStream());
            int ☃xxx = ☃xx.getType();
            if (☃xxx == 0) {
               ☃xxx = 6;
            }

            ☃x = new BufferedImage(☃xx.getWidth(), ☃xx.getHeight(), ☃xxx);
            Graphics ☃xxxx = ☃x.getGraphics();
            ☃xxxx.drawImage(☃xx, 0, 0, null);
            int ☃xxxxx = 0;

            while (true) {
               if (☃xxxxx >= 17 || ☃xxxxx >= this.listTextures.size() || ☃xxxxx >= this.listDyeColors.size()) {
                  break label198;
               }

               IResource ☃xxxxxx = null;

               try {
                  String ☃xxxxxxx = this.listTextures.get(☃xxxxx);
                  int ☃xxxxxxxx = this.listDyeColors.get(☃xxxxx).getColorValue();
                  if (☃xxxxxxx != null) {
                     ☃xxxxxx = ☃.getResource(new ResourceLocation(☃xxxxxxx));
                     BufferedImage ☃xxxxxxxxx = TextureUtil.readBufferedImage(☃xxxxxx.getInputStream());
                     if (☃xxxxxxxxx.getWidth() == ☃x.getWidth() && ☃xxxxxxxxx.getHeight() == ☃x.getHeight() && ☃xxxxxxxxx.getType() == 6) {
                        for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < ☃xxxxxxxxx.getHeight(); ☃xxxxxxxxxx++) {
                           for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < ☃xxxxxxxxx.getWidth(); ☃xxxxxxxxxxx++) {
                              int ☃xxxxxxxxxxxx = ☃xxxxxxxxx.getRGB(☃xxxxxxxxxxx, ☃xxxxxxxxxx);
                              if ((☃xxxxxxxxxxxx & 0xFF000000) != 0) {
                                 int ☃xxxxxxxxxxxxx = (☃xxxxxxxxxxxx & 0xFF0000) << 8 & 0xFF000000;
                                 int ☃xxxxxxxxxxxxxx = ☃xx.getRGB(☃xxxxxxxxxxx, ☃xxxxxxxxxx);
                                 int ☃xxxxxxxxxxxxxxx = MathHelper.multiplyColor(☃xxxxxxxxxxxxxx, ☃xxxxxxxx) & 16777215;
                                 ☃xxxxxxxxx.setRGB(☃xxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxx | ☃xxxxxxxxxxxxxxx);
                              }
                           }
                        }

                        ☃x.getGraphics().drawImage(☃xxxxxxxxx, 0, 0, null);
                     }
                  }
               } finally {
                  IOUtils.closeQuietly(☃xxxxxx);
               }

               ☃xxxxx++;
            }
         } catch (IOException var27) {
            LOGGER.error("Couldn't load layered image", var27);
         } finally {
            IOUtils.closeQuietly(☃);
         }

         return;
      }

      TextureUtil.uploadTextureImage(this.getGlTextureId(), ☃x);
   }
}
