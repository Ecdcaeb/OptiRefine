package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import org.lwjgl.util.vector.Matrix4f;

public class Shader {
   private final ShaderManager manager;
   public final Framebuffer framebufferIn;
   public final Framebuffer framebufferOut;
   private final List<Object> listAuxFramebuffers = Lists.newArrayList();
   private final List<String> listAuxNames = Lists.newArrayList();
   private final List<Integer> listAuxWidths = Lists.newArrayList();
   private final List<Integer> listAuxHeights = Lists.newArrayList();
   private Matrix4f projectionMatrix;

   public Shader(IResourceManager var1, String var2, Framebuffer var3, Framebuffer var4) throws IOException {
      this.manager = new ShaderManager(☃, ☃);
      this.framebufferIn = ☃;
      this.framebufferOut = ☃;
   }

   public void deleteShader() {
      this.manager.deleteShader();
   }

   public void addAuxFramebuffer(String var1, Object var2, int var3, int var4) {
      this.listAuxNames.add(this.listAuxNames.size(), ☃);
      this.listAuxFramebuffers.add(this.listAuxFramebuffers.size(), ☃);
      this.listAuxWidths.add(this.listAuxWidths.size(), ☃);
      this.listAuxHeights.add(this.listAuxHeights.size(), ☃);
   }

   private void preRender() {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.disableDepth();
      GlStateManager.disableAlpha();
      GlStateManager.disableFog();
      GlStateManager.disableLighting();
      GlStateManager.disableColorMaterial();
      GlStateManager.enableTexture2D();
      GlStateManager.bindTexture(0);
   }

   public void setProjectionMatrix(Matrix4f var1) {
      this.projectionMatrix = ☃;
   }

   public void render(float var1) {
      this.preRender();
      this.framebufferIn.unbindFramebuffer();
      float ☃ = this.framebufferOut.framebufferTextureWidth;
      float ☃x = this.framebufferOut.framebufferTextureHeight;
      GlStateManager.viewport(0, 0, (int)☃, (int)☃x);
      this.manager.addSamplerTexture("DiffuseSampler", this.framebufferIn);

      for (int ☃xx = 0; ☃xx < this.listAuxFramebuffers.size(); ☃xx++) {
         this.manager.addSamplerTexture(this.listAuxNames.get(☃xx), this.listAuxFramebuffers.get(☃xx));
         this.manager.getShaderUniformOrDefault("AuxSize" + ☃xx).set(this.listAuxWidths.get(☃xx).intValue(), this.listAuxHeights.get(☃xx).intValue());
      }

      this.manager.getShaderUniformOrDefault("ProjMat").set(this.projectionMatrix);
      this.manager.getShaderUniformOrDefault("InSize").set(this.framebufferIn.framebufferTextureWidth, this.framebufferIn.framebufferTextureHeight);
      this.manager.getShaderUniformOrDefault("OutSize").set(☃, ☃x);
      this.manager.getShaderUniformOrDefault("Time").set(☃);
      Minecraft ☃xx = Minecraft.getMinecraft();
      this.manager.getShaderUniformOrDefault("ScreenSize").set(☃xx.displayWidth, ☃xx.displayHeight);
      this.manager.useShader();
      this.framebufferOut.framebufferClear();
      this.framebufferOut.bindFramebuffer(false);
      GlStateManager.depthMask(false);
      GlStateManager.colorMask(true, true, true, true);
      Tessellator ☃xxx = Tessellator.getInstance();
      BufferBuilder ☃xxxx = ☃xxx.getBuffer();
      ☃xxxx.begin(7, DefaultVertexFormats.POSITION_COLOR);
      ☃xxxx.pos(0.0, ☃x, 500.0).color(255, 255, 255, 255).endVertex();
      ☃xxxx.pos(☃, ☃x, 500.0).color(255, 255, 255, 255).endVertex();
      ☃xxxx.pos(☃, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
      ☃xxxx.pos(0.0, 0.0, 500.0).color(255, 255, 255, 255).endVertex();
      ☃xxx.draw();
      GlStateManager.depthMask(true);
      GlStateManager.colorMask(true, true, true, true);
      this.manager.endShader();
      this.framebufferOut.unbindFramebuffer();
      this.framebufferIn.unbindFramebufferTexture();

      for (Object ☃xxxxx : this.listAuxFramebuffers) {
         if (☃xxxxx instanceof Framebuffer) {
            ((Framebuffer)☃xxxxx).unbindFramebufferTexture();
         }
      }
   }

   public ShaderManager getShaderManager() {
      return this.manager;
   }
}
