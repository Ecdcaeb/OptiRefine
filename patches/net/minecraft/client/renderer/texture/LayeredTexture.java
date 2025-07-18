package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LayeredTexture extends AbstractTexture {
   private static final Logger LOGGER = LogManager.getLogger();
   public final List<String> layeredTextureNames;

   public LayeredTexture(String... var1) {
      this.layeredTextureNames = Lists.newArrayList(☃);
   }

   @Override
   public void loadTexture(IResourceManager var1) throws IOException {
      this.deleteGlTexture();
      BufferedImage ☃ = null;

      for (String ☃x : this.layeredTextureNames) {
         IResource ☃xx = null;

         try {
            if (☃x != null) {
               ☃xx = ☃.getResource(new ResourceLocation(☃x));
               BufferedImage ☃xxx = TextureUtil.readBufferedImage(☃xx.getInputStream());
               if (☃ == null) {
                  ☃ = new BufferedImage(☃xxx.getWidth(), ☃xxx.getHeight(), 2);
               }

               ☃.getGraphics().drawImage(☃xxx, 0, 0, null);
            }
            continue;
         } catch (IOException var10) {
            LOGGER.error("Couldn't load layered image", var10);
         } finally {
            IOUtils.closeQuietly(☃xx);
         }

         return;
      }

      TextureUtil.uploadTextureImage(this.getGlTextureId(), ☃);
   }
}
