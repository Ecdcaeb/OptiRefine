package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;

public class DynamicTexture extends AbstractTexture {
   private final int[] dynamicTextureData;
   private final int width;
   private final int height;

   public DynamicTexture(BufferedImage var1) {
      this(☃.getWidth(), ☃.getHeight());
      ☃.getRGB(0, 0, ☃.getWidth(), ☃.getHeight(), this.dynamicTextureData, 0, ☃.getWidth());
      this.updateDynamicTexture();
   }

   public DynamicTexture(int var1, int var2) {
      this.width = ☃;
      this.height = ☃;
      this.dynamicTextureData = new int[☃ * ☃];
      TextureUtil.allocateTexture(this.getGlTextureId(), ☃, ☃);
   }

   @Override
   public void loadTexture(IResourceManager var1) throws IOException {
   }

   public void updateDynamicTexture() {
      TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
   }

   public int[] getTextureData() {
      return this.dynamicTextureData;
   }
}
