package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleFootStep extends Particle {
   private static final ResourceLocation FOOTPRINT_TEXTURE = new ResourceLocation("textures/particle/footprint.png");
   private int footstepAge;
   private final int footstepMaxAge;
   private final TextureManager currentFootSteps;

   protected ParticleFootStep(TextureManager var1, World var2, double var3, double var5, double var7) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.currentFootSteps = ☃;
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.footstepMaxAge = 200;
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = (this.footstepAge + ☃) / this.footstepMaxAge;
      ☃ *= ☃;
      float ☃x = 2.0F - ☃ * 2.0F;
      if (☃x > 1.0F) {
         ☃x = 1.0F;
      }

      ☃x *= 0.2F;
      GlStateManager.disableLighting();
      float ☃xx = 0.125F;
      float ☃xxx = (float)(this.posX - interpPosX);
      float ☃xxxx = (float)(this.posY - interpPosY);
      float ☃xxxxx = (float)(this.posZ - interpPosZ);
      float ☃xxxxxx = this.world.getLightBrightness(new BlockPos(this.posX, this.posY, this.posZ));
      this.currentFootSteps.bindTexture(FOOTPRINT_TEXTURE);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      ☃.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      ☃.pos(☃xxx - 0.125F, ☃xxxx, ☃xxxxx + 0.125F).tex(0.0, 1.0).color(☃xxxxxx, ☃xxxxxx, ☃xxxxxx, ☃x).endVertex();
      ☃.pos(☃xxx + 0.125F, ☃xxxx, ☃xxxxx + 0.125F).tex(1.0, 1.0).color(☃xxxxxx, ☃xxxxxx, ☃xxxxxx, ☃x).endVertex();
      ☃.pos(☃xxx + 0.125F, ☃xxxx, ☃xxxxx - 0.125F).tex(1.0, 0.0).color(☃xxxxxx, ☃xxxxxx, ☃xxxxxx, ☃x).endVertex();
      ☃.pos(☃xxx - 0.125F, ☃xxxx, ☃xxxxx - 0.125F).tex(0.0, 0.0).color(☃xxxxxx, ☃xxxxxx, ☃xxxxxx, ☃x).endVertex();
      Tessellator.getInstance().draw();
      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
   }

   @Override
   public void onUpdate() {
      this.footstepAge++;
      if (this.footstepAge == this.footstepMaxAge) {
         this.setExpired();
      }
   }

   @Override
   public int getFXLayer() {
      return 3;
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleFootStep(Minecraft.getMinecraft().getTextureManager(), ☃, ☃, ☃, ☃);
      }
   }
}
