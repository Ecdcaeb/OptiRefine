package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleDragonBreath extends Particle {
   private final float oSize;
   private boolean hasHitGround;

   protected ParticleDragonBreath(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
      this.particleRed = MathHelper.nextFloat(this.rand, 0.7176471F, 0.8745098F);
      this.particleGreen = MathHelper.nextFloat(this.rand, 0.0F, 0.0F);
      this.particleBlue = MathHelper.nextFloat(this.rand, 0.8235294F, 0.9764706F);
      this.particleScale *= 0.75F;
      this.oSize = this.particleScale;
      this.particleMaxAge = (int)(20.0 / (this.rand.nextFloat() * 0.8 + 0.2));
      this.hasHitGround = false;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setExpired();
      } else {
         this.setParticleTextureIndex(3 * this.particleAge / this.particleMaxAge + 5);
         if (this.onGround) {
            this.motionY = 0.0;
            this.hasHitGround = true;
         }

         if (this.hasHitGround) {
            this.motionY += 0.002;
         }

         this.move(this.motionX, this.motionY, this.motionZ);
         if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
         }

         this.motionX *= 0.96F;
         this.motionZ *= 0.96F;
         if (this.hasHitGround) {
            this.motionY *= 0.96F;
         }
      }
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.particleScale = this.oSize * MathHelper.clamp((this.particleAge + ☃) / this.particleMaxAge * 32.0F, 0.0F, 1.0F);
      super.renderParticle(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleDragonBreath(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
