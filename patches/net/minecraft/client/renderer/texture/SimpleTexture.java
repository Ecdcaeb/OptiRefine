package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleTexture extends AbstractTexture {
   private static final Logger LOGGER = LogManager.getLogger();
   protected final ResourceLocation textureLocation;

   public SimpleTexture(ResourceLocation var1) {
      this.textureLocation = ☃;
   }

   @Override
   public void loadTexture(IResourceManager var1) throws IOException {
      this.deleteGlTexture();
      IResource ☃ = null;

      try {
         ☃ = ☃.getResource(this.textureLocation);
         BufferedImage ☃x = TextureUtil.readBufferedImage(☃.getInputStream());
         boolean ☃xx = false;
         boolean ☃xxx = false;
         if (☃.hasMetadata()) {
            try {
               TextureMetadataSection ☃xxxx = ☃.getMetadata("texture");
               if (☃xxxx != null) {
                  ☃xx = ☃xxxx.getTextureBlur();
                  ☃xxx = ☃xxxx.getTextureClamp();
               }
            } catch (RuntimeException var10) {
               LOGGER.warn("Failed reading metadata of: {}", this.textureLocation, var10);
            }
         }

         TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), ☃x, ☃xx, ☃xxx);
      } finally {
         IOUtils.closeQuietly(☃);
      }
   }
}
