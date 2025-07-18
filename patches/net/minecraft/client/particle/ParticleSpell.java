package net.minecraft.client.particle;

import java.util.Random;
import net.minecraft.world.World;

public class ParticleSpell extends Particle {
   private static final Random RANDOM = new Random();
   private int baseSpellTextureIndex = 128;

   protected ParticleSpell(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, 0.5 - RANDOM.nextDouble(), ☃, 0.5 - RANDOM.nextDouble());
      this.motionY *= 0.2F;
      if (☃ == 0.0 && ☃ == 0.0) {
         this.motionX *= 0.1F;
         this.motionZ *= 0.1F;
      }

      this.particleScale *= 0.75F;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
   }

   @Override
   public boolean shouldDisableDepth() {
      return true;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setExpired();
      }

      this.setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
      this.motionY += 0.004;
      this.move(this.motionX, this.motionY, this.motionZ);
      if (this.posY == this.prevPosY) {
         this.motionX *= 1.1;
         this.motionZ *= 1.1;
      }

      this.motionX *= 0.96F;
      this.motionY *= 0.96F;
      this.motionZ *= 0.96F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }

   public void setBaseSpellTextureIndex(int var1) {
      this.baseSpellTextureIndex = ☃;
   }

   public static class AmbientMobFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         Particle ☃ = new ParticleSpell(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         ☃.setAlphaF(0.15F);
         ☃.setRBGColorF((float)☃, (float)☃, (float)☃);
         return ☃;
      }
   }

   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         return new ParticleSpell(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public static class InstantFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         Particle ☃ = new ParticleSpell(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         ((ParticleSpell)☃).setBaseSpellTextureIndex(144);
         return ☃;
      }
   }

   public static class MobFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         Particle ☃ = new ParticleSpell(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         ☃.setRBGColorF((float)☃, (float)☃, (float)☃);
         return ☃;
      }
   }

   public static class WitchFactory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         Particle ☃ = new ParticleSpell(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         ((ParticleSpell)☃).setBaseSpellTextureIndex(144);
         float ☃x = ☃.rand.nextFloat() * 0.5F + 0.35F;
         ☃.setRBGColorF(1.0F * ☃x, 0.0F * ☃x, 1.0F * ☃x);
         return ☃;
      }
   }
}
