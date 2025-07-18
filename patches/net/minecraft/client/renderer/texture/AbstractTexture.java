package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;

public abstract class AbstractTexture implements ITextureObject {
   protected int glTextureId = -1;
   protected boolean blur;
   protected boolean mipmap;
   protected boolean blurLast;
   protected boolean mipmapLast;

   public void setBlurMipmapDirect(boolean var1, boolean var2) {
      this.blur = ☃;
      this.mipmap = ☃;
      int ☃;
      int ☃x;
      if (☃) {
         ☃ = ☃ ? 9987 : 9729;
         ☃x = 9729;
      } else {
         ☃ = ☃ ? 9986 : 9728;
         ☃x = 9728;
      }

      GlStateManager.glTexParameteri(3553, 10241, ☃);
      GlStateManager.glTexParameteri(3553, 10240, ☃x);
   }

   @Override
   public void setBlurMipmap(boolean var1, boolean var2) {
      this.blurLast = this.blur;
      this.mipmapLast = this.mipmap;
      this.setBlurMipmapDirect(☃, ☃);
   }

   @Override
   public void restoreLastBlurMipmap() {
      this.setBlurMipmapDirect(this.blurLast, this.mipmapLast);
   }

   @Override
   public int getGlTextureId() {
      if (this.glTextureId == -1) {
         this.glTextureId = TextureUtil.glGenTextures();
      }

      return this.glTextureId;
   }

   public void deleteGlTexture() {
      if (this.glTextureId != -1) {
         TextureUtil.deleteTexture(this.glTextureId);
         this.glTextureId = -1;
      }
   }
}
