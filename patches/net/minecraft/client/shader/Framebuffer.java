package net.minecraft.client.shader;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Framebuffer {
   public int framebufferTextureWidth;
   public int framebufferTextureHeight;
   public int framebufferWidth;
   public int framebufferHeight;
   public boolean useDepth;
   public int framebufferObject;
   public int framebufferTexture;
   public int depthBuffer;
   public float[] framebufferColor;
   public int framebufferFilter;

   public Framebuffer(int var1, int var2, boolean var3) {
      this.useDepth = ☃;
      this.framebufferObject = -1;
      this.framebufferTexture = -1;
      this.depthBuffer = -1;
      this.framebufferColor = new float[4];
      this.framebufferColor[0] = 1.0F;
      this.framebufferColor[1] = 1.0F;
      this.framebufferColor[2] = 1.0F;
      this.framebufferColor[3] = 0.0F;
      this.createBindFramebuffer(☃, ☃);
   }

   public void createBindFramebuffer(int var1, int var2) {
      if (!OpenGlHelper.isFramebufferEnabled()) {
         this.framebufferWidth = ☃;
         this.framebufferHeight = ☃;
      } else {
         GlStateManager.enableDepth();
         if (this.framebufferObject >= 0) {
            this.deleteFramebuffer();
         }

         this.createFramebuffer(☃, ☃);
         this.checkFramebufferComplete();
         OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
      }
   }

   public void deleteFramebuffer() {
      if (OpenGlHelper.isFramebufferEnabled()) {
         this.unbindFramebufferTexture();
         this.unbindFramebuffer();
         if (this.depthBuffer > -1) {
            OpenGlHelper.glDeleteRenderbuffers(this.depthBuffer);
            this.depthBuffer = -1;
         }

         if (this.framebufferTexture > -1) {
            TextureUtil.deleteTexture(this.framebufferTexture);
            this.framebufferTexture = -1;
         }

         if (this.framebufferObject > -1) {
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
            OpenGlHelper.glDeleteFramebuffers(this.framebufferObject);
            this.framebufferObject = -1;
         }
      }
   }

   public void createFramebuffer(int var1, int var2) {
      this.framebufferWidth = ☃;
      this.framebufferHeight = ☃;
      this.framebufferTextureWidth = ☃;
      this.framebufferTextureHeight = ☃;
      if (!OpenGlHelper.isFramebufferEnabled()) {
         this.framebufferClear();
      } else {
         this.framebufferObject = OpenGlHelper.glGenFramebuffers();
         this.framebufferTexture = TextureUtil.glGenTextures();
         if (this.useDepth) {
            this.depthBuffer = OpenGlHelper.glGenRenderbuffers();
         }

         this.setFramebufferFilter(9728);
         GlStateManager.bindTexture(this.framebufferTexture);
         GlStateManager.glTexImage2D(3553, 0, 32856, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, 6408, 5121, null);
         OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
         OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, this.framebufferTexture, 0);
         if (this.useDepth) {
            OpenGlHelper.glBindRenderbuffer(OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
            OpenGlHelper.glRenderbufferStorage(OpenGlHelper.GL_RENDERBUFFER, 33190, this.framebufferTextureWidth, this.framebufferTextureHeight);
            OpenGlHelper.glFramebufferRenderbuffer(
               OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer
            );
         }

         this.framebufferClear();
         this.unbindFramebufferTexture();
      }
   }

   public void setFramebufferFilter(int var1) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         this.framebufferFilter = ☃;
         GlStateManager.bindTexture(this.framebufferTexture);
         GlStateManager.glTexParameteri(3553, 10241, ☃);
         GlStateManager.glTexParameteri(3553, 10240, ☃);
         GlStateManager.glTexParameteri(3553, 10242, 10496);
         GlStateManager.glTexParameteri(3553, 10243, 10496);
         GlStateManager.bindTexture(0);
      }
   }

   public void checkFramebufferComplete() {
      int ☃ = OpenGlHelper.glCheckFramebufferStatus(OpenGlHelper.GL_FRAMEBUFFER);
      if (☃ != OpenGlHelper.GL_FRAMEBUFFER_COMPLETE) {
         if (☃ == OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
         } else if (☃ == OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
         } else if (☃ == OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
         } else if (☃ == OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
         } else {
            throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + ☃);
         }
      }
   }

   public void bindFramebufferTexture() {
      if (OpenGlHelper.isFramebufferEnabled()) {
         GlStateManager.bindTexture(this.framebufferTexture);
      }
   }

   public void unbindFramebufferTexture() {
      if (OpenGlHelper.isFramebufferEnabled()) {
         GlStateManager.bindTexture(0);
      }
   }

   public void bindFramebuffer(boolean var1) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
         if (☃) {
            GlStateManager.viewport(0, 0, this.framebufferWidth, this.framebufferHeight);
         }
      }
   }

   public void unbindFramebuffer() {
      if (OpenGlHelper.isFramebufferEnabled()) {
         OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, 0);
      }
   }

   public void setFramebufferColor(float var1, float var2, float var3, float var4) {
      this.framebufferColor[0] = ☃;
      this.framebufferColor[1] = ☃;
      this.framebufferColor[2] = ☃;
      this.framebufferColor[3] = ☃;
   }

   public void framebufferRender(int var1, int var2) {
      this.framebufferRenderExt(☃, ☃, true);
   }

   public void framebufferRenderExt(int var1, int var2, boolean var3) {
      if (OpenGlHelper.isFramebufferEnabled()) {
         GlStateManager.colorMask(true, true, true, false);
         GlStateManager.disableDepth();
         GlStateManager.depthMask(false);
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         GlStateManager.ortho(0.0, ☃, ☃, 0.0, 1000.0, 3000.0);
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.0F, 0.0F, -2000.0F);
         GlStateManager.viewport(0, 0, ☃, ☃);
         GlStateManager.enableTexture2D();
         GlStateManager.disableLighting();
         GlStateManager.disableAlpha();
         if (☃) {
            GlStateManager.disableBlend();
            GlStateManager.enableColorMaterial();
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.bindFramebufferTexture();
         float ☃ = ☃;
         float ☃x = ☃;
         float ☃xx = (float)this.framebufferWidth / this.framebufferTextureWidth;
         float ☃xxx = (float)this.framebufferHeight / this.framebufferTextureHeight;
         Tessellator ☃xxxx = Tessellator.getInstance();
         BufferBuilder ☃xxxxx = ☃xxxx.getBuffer();
         ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
         ☃xxxxx.pos(0.0, ☃x, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
         ☃xxxxx.pos(☃, ☃x, 0.0).tex(☃xx, 0.0).color(255, 255, 255, 255).endVertex();
         ☃xxxxx.pos(☃, 0.0, 0.0).tex(☃xx, ☃xxx).color(255, 255, 255, 255).endVertex();
         ☃xxxxx.pos(0.0, 0.0, 0.0).tex(0.0, ☃xxx).color(255, 255, 255, 255).endVertex();
         ☃xxxx.draw();
         this.unbindFramebufferTexture();
         GlStateManager.depthMask(true);
         GlStateManager.colorMask(true, true, true, true);
      }
   }

   public void framebufferClear() {
      this.bindFramebuffer(true);
      GlStateManager.clearColor(this.framebufferColor[0], this.framebufferColor[1], this.framebufferColor[2], this.framebufferColor[3]);
      int ☃ = 16384;
      if (this.useDepth) {
         GlStateManager.clearDepth(1.0);
         ☃ |= 256;
      }

      GlStateManager.clear(☃);
      this.unbindFramebuffer();
   }
}
