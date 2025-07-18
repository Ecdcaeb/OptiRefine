package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleMobAppearance extends Particle {
   private EntityLivingBase entity;

   protected ParticleMobAppearance(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.particleGravity = 0.0F;
      this.particleMaxAge = 30;
   }

   @Override
   public int getFXLayer() {
      return 3;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.entity == null) {
         EntityElderGuardian ☃ = new EntityElderGuardian(this.world);
         ☃.setGhost();
         this.entity = ☃;
      }
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (this.entity != null) {
         RenderManager ☃ = Minecraft.getMinecraft().getRenderManager();
         ☃.setRenderPosition(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);
         float ☃x = 0.42553192F;
         float ☃xx = (this.particleAge + ☃) / this.particleMaxAge;
         GlStateManager.depthMask(true);
         GlStateManager.enableBlend();
         GlStateManager.enableDepth();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         float ☃xxx = 240.0F;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
         GlStateManager.pushMatrix();
         float ☃xxxx = 0.05F + 0.5F * MathHelper.sin(☃xx * (float) Math.PI);
         GlStateManager.color(1.0F, 1.0F, 1.0F, ☃xxxx);
         GlStateManager.translate(0.0F, 1.8F, 0.0F);
         GlStateManager.rotate(180.0F - ☃.rotationYaw, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(60.0F - 150.0F * ☃xx - ☃.rotationPitch, 1.0F, 0.0F, 0.0F);
         GlStateManager.translate(0.0F, -0.4F, -1.5F);
         GlStateManager.scale(0.42553192F, 0.42553192F, 0.42553192F);
         this.entity.rotationYaw = 0.0F;
         this.entity.rotationYawHead = 0.0F;
         this.entity.prevRotationYaw = 0.0F;
         this.entity.prevRotationYawHead = 0.0F;
         ☃.renderEntity(this.entity, 0.0, 0.0, 0.0, 0.0F, ☃, false);
         GlStateManager.popMatrix();
         GlStateManager.enableDepth();
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleMobAppearance(☃, ☃, ☃, ☃);
      }
   }
}
