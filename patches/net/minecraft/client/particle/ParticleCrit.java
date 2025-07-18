package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleCrit extends Particle {
   float oSize;

   protected ParticleCrit(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      this(☃, ☃, ☃, ☃, ☃, ☃, ☃, 1.0F);
   }

   protected ParticleCrit(World var1, double var2, double var4, double var6, double var8, double var10, double var12, float var14) {
      super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
      this.motionX *= 0.1F;
      this.motionY *= 0.1F;
      this.motionZ *= 0.1F;
      this.motionX += ☃ * 0.4;
      this.motionY += ☃ * 0.4;
      this.motionZ += ☃ * 0.4;
      float ☃ = (float)(Math.random() * 0.3F + 0.6F);
      this.particleRed = ☃;
      this.particleGreen = ☃;
      this.particleBlue = ☃;
      this.particleScale *= 0.75F;
      this.particleScale *= ☃;
      this.oSize = this.particleScale;
      this.particleMaxAge = (int)(6.0 / (Math.random() * 0.8 + 0.6));
      this.particleMaxAge = (int)(this.particleMaxAge * ☃);
      this.setParticleTextureIndex(65);
      this.onUpdate();
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = (this.particleAge + ☃) / this.particleMaxAge * 32.0F;
      ☃ = MathHelper.clamp(☃, 0.0F, 1.0F);
      this.particleScale = this.oSize * ☃;
      super.renderParticle(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setExpired();
      }

      this.move(this.motionX, this.motionY, this.motionZ);
      this.particleGreen = (float)(this.particleGreen * 0.96);
      this.particleBlue = (float)(this.particleBlue * 0.9);
      this.motionX *= 0.7F;
      this.motionY *= 0.7F;
      this.motionZ *= 0.7F;
      this.motionY -= 0.02F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }

   public static class DamageIndicatorFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         Particle ☃ = new ParticleCrit(☃, ☃, ☃, ☃, ☃, ☃ + 1.0, ☃, 1.0F);
         ☃.setMaxAge(20);
         ☃.setParticleTextureIndex(67);
         return ☃;
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleCrit(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public static class MagicFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         Particle ☃ = new ParticleCrit(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         ☃.setRBGColorF(☃.getRedColorF() * 0.3F, ☃.getGreenColorF() * 0.8F, ☃.getBlueColorF());
         ☃.nextTextureIndexX();
         return ☃;
      }
   }
}
