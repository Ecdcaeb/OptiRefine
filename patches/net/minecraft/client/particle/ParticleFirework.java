package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemDye;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleFirework {
   public static class Factory implements IParticleFactory {
      @Override
      public Particle createParticle(int var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
         ParticleFirework.Spark ☃ = new ParticleFirework.Spark(☃, ☃, ☃, ☃, ☃, ☃, ☃, Minecraft.getMinecraft().effectRenderer);
         ☃.setAlphaF(0.99F);
         return ☃;
      }
   }

   public static class Overlay extends Particle {
      protected Overlay(World var1, double var2, double var4, double var6) {
         super(☃, ☃, ☃, ☃);
         this.particleMaxAge = 4;
      }

      @Override
      public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
         float ☃ = 0.25F;
         float ☃x = 0.5F;
         float ☃xx = 0.125F;
         float ☃xxx = 0.375F;
         float ☃xxxx = 7.1F * MathHelper.sin((this.particleAge + ☃ - 1.0F) * 0.25F * (float) Math.PI);
         this.setAlphaF(0.6F - (this.particleAge + ☃ - 1.0F) * 0.25F * 0.5F);
         float ☃xxxxx = (float)(this.prevPosX + (this.posX - this.prevPosX) * ☃ - interpPosX);
         float ☃xxxxxx = (float)(this.prevPosY + (this.posY - this.prevPosY) * ☃ - interpPosY);
         float ☃xxxxxxx = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * ☃ - interpPosZ);
         int ☃xxxxxxxx = this.getBrightnessForRender(☃);
         int ☃xxxxxxxxx = ☃xxxxxxxx >> 16 & 65535;
         int ☃xxxxxxxxxx = ☃xxxxxxxx & 65535;
         ☃.pos(☃xxxxx - ☃ * ☃xxxx - ☃ * ☃xxxx, ☃xxxxxx - ☃ * ☃xxxx, ☃xxxxxxx - ☃ * ☃xxxx - ☃ * ☃xxxx)
            .tex(0.5, 0.375)
            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
            .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
            .endVertex();
         ☃.pos(☃xxxxx - ☃ * ☃xxxx + ☃ * ☃xxxx, ☃xxxxxx + ☃ * ☃xxxx, ☃xxxxxxx - ☃ * ☃xxxx + ☃ * ☃xxxx)
            .tex(0.5, 0.125)
            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
            .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
            .endVertex();
         ☃.pos(☃xxxxx + ☃ * ☃xxxx + ☃ * ☃xxxx, ☃xxxxxx + ☃ * ☃xxxx, ☃xxxxxxx + ☃ * ☃xxxx + ☃ * ☃xxxx)
            .tex(0.25, 0.125)
            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
            .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
            .endVertex();
         ☃.pos(☃xxxxx + ☃ * ☃xxxx - ☃ * ☃xxxx, ☃xxxxxx - ☃ * ☃xxxx, ☃xxxxxxx + ☃ * ☃xxxx - ☃ * ☃xxxx)
            .tex(0.25, 0.375)
            .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
            .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
            .endVertex();
      }
   }

   public static class Spark extends ParticleSimpleAnimated {
      private boolean trail;
      private boolean twinkle;
      private final ParticleManager effectRenderer;
      private float fadeColourRed;
      private float fadeColourGreen;
      private float fadeColourBlue;
      private boolean hasFadeColour;

      public Spark(World var1, double var2, double var4, double var6, double var8, double var10, double var12, ParticleManager var14) {
         super(☃, ☃, ☃, ☃, 160, 8, -0.004F);
         this.motionX = ☃;
         this.motionY = ☃;
         this.motionZ = ☃;
         this.effectRenderer = ☃;
         this.particleScale *= 0.75F;
         this.particleMaxAge = 48 + this.rand.nextInt(12);
      }

      public void setTrail(boolean var1) {
         this.trail = ☃;
      }

      public void setTwinkle(boolean var1) {
         this.twinkle = ☃;
      }

      @Override
      public boolean shouldDisableDepth() {
         return true;
      }

      @Override
      public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
         if (!this.twinkle || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0) {
            super.renderParticle(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
         }
      }

      @Override
      public void onUpdate() {
         super.onUpdate();
         if (this.trail && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0) {
            ParticleFirework.Spark ☃ = new ParticleFirework.Spark(this.world, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, this.effectRenderer);
            ☃.setAlphaF(0.99F);
            ☃.setRBGColorF(this.particleRed, this.particleGreen, this.particleBlue);
            ☃.particleAge = ☃.particleMaxAge / 2;
            if (this.hasFadeColour) {
               ☃.hasFadeColour = true;
               ☃.fadeColourRed = this.fadeColourRed;
               ☃.fadeColourGreen = this.fadeColourGreen;
               ☃.fadeColourBlue = this.fadeColourBlue;
            }

            ☃.twinkle = this.twinkle;
            this.effectRenderer.addEffect(☃);
         }
      }
   }

   public static class Starter extends Particle {
      private int fireworkAge;
      private final ParticleManager manager;
      private NBTTagList fireworkExplosions;
      boolean twinkle;

      public Starter(
         World var1, double var2, double var4, double var6, double var8, double var10, double var12, ParticleManager var14, @Nullable NBTTagCompound var15
      ) {
         super(☃, ☃, ☃, ☃, 0.0, 0.0, 0.0);
         this.motionX = ☃;
         this.motionY = ☃;
         this.motionZ = ☃;
         this.manager = ☃;
         this.particleMaxAge = 8;
         if (☃ != null) {
            this.fireworkExplosions = ☃.getTagList("Explosions", 10);
            if (this.fireworkExplosions.isEmpty()) {
               this.fireworkExplosions = null;
            } else {
               this.particleMaxAge = this.fireworkExplosions.tagCount() * 2 - 1;

               for (int ☃ = 0; ☃ < this.fireworkExplosions.tagCount(); ☃++) {
                  NBTTagCompound ☃x = this.fireworkExplosions.getCompoundTagAt(☃);
                  if (☃x.getBoolean("Flicker")) {
                     this.twinkle = true;
                     this.particleMaxAge += 15;
                     break;
                  }
               }
            }
         }
      }

      @Override
      public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      }

      @Override
      public void onUpdate() {
         if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
            boolean ☃ = this.isFarFromCamera();
            boolean ☃x = false;
            if (this.fireworkExplosions.tagCount() >= 3) {
               ☃x = true;
            } else {
               for (int ☃xx = 0; ☃xx < this.fireworkExplosions.tagCount(); ☃xx++) {
                  NBTTagCompound ☃xxx = this.fireworkExplosions.getCompoundTagAt(☃xx);
                  if (☃xxx.getByte("Type") == 1) {
                     ☃x = true;
                     break;
                  }
               }
            }

            SoundEvent ☃xxx;
            if (☃x) {
               ☃xxx = ☃ ? SoundEvents.ENTITY_FIREWORK_LARGE_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_LARGE_BLAST;
            } else {
               ☃xxx = ☃ ? SoundEvents.ENTITY_FIREWORK_BLAST_FAR : SoundEvents.ENTITY_FIREWORK_BLAST;
            }

            this.world.playSound(this.posX, this.posY, this.posZ, ☃xxx, SoundCategory.AMBIENT, 20.0F, 0.95F + this.rand.nextFloat() * 0.1F, true);
         }

         if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.tagCount()) {
            int ☃xxx = this.fireworkAge / 2;
            NBTTagCompound ☃xxxx = this.fireworkExplosions.getCompoundTagAt(☃xxx);
            int ☃xxxxx = ☃xxxx.getByte("Type");
            boolean ☃xxxxxx = ☃xxxx.getBoolean("Trail");
            boolean ☃xxxxxxx = ☃xxxx.getBoolean("Flicker");
            int[] ☃xxxxxxxx = ☃xxxx.getIntArray("Colors");
            int[] ☃xxxxxxxxx = ☃xxxx.getIntArray("FadeColors");
            if (☃xxxxxxxx.length == 0) {
               ☃xxxxxxxx = new int[]{ItemDye.DYE_COLORS[0]};
            }

            if (☃xxxxx == 1) {
               this.createBall(0.5, 4, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxx, ☃xxxxxxx);
            } else if (☃xxxxx == 2) {
               this.createShaped(
                  0.5,
                  new double[][]{
                     {0.0, 1.0},
                     {0.3455, 0.309},
                     {0.9511, 0.309},
                     {0.3795918367346939, -0.12653061224489795},
                     {0.6122448979591837, -0.8040816326530612},
                     {0.0, -0.35918367346938773}
                  },
                  ☃xxxxxxxx,
                  ☃xxxxxxxxx,
                  ☃xxxxxx,
                  ☃xxxxxxx,
                  false
               );
            } else if (☃xxxxx == 3) {
               this.createShaped(
                  0.5,
                  new double[][]{
                     {0.0, 0.2},
                     {0.2, 0.2},
                     {0.2, 0.6},
                     {0.6, 0.6},
                     {0.6, 0.2},
                     {0.2, 0.2},
                     {0.2, 0.0},
                     {0.4, 0.0},
                     {0.4, -0.6},
                     {0.2, -0.6},
                     {0.2, -0.4},
                     {0.0, -0.4}
                  },
                  ☃xxxxxxxx,
                  ☃xxxxxxxxx,
                  ☃xxxxxx,
                  ☃xxxxxxx,
                  true
               );
            } else if (☃xxxxx == 4) {
               this.createBurst(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxx, ☃xxxxxxx);
            } else {
               this.createBall(0.25, 2, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxx, ☃xxxxxxx);
            }

            int ☃xxxxxxxxxx = ☃xxxxxxxx[0];
            float ☃xxxxxxxxxxx = ((☃xxxxxxxxxx & 0xFF0000) >> 16) / 255.0F;
            float ☃xxxxxxxxxxxx = ((☃xxxxxxxxxx & 0xFF00) >> 8) / 255.0F;
            float ☃xxxxxxxxxxxxx = ((☃xxxxxxxxxx & 0xFF) >> 0) / 255.0F;
            ParticleFirework.Overlay ☃xxxxxxxxxxxxxx = new ParticleFirework.Overlay(this.world, this.posX, this.posY, this.posZ);
            ☃xxxxxxxxxxxxxx.setRBGColorF(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx);
            this.manager.addEffect(☃xxxxxxxxxxxxxx);
         }

         this.fireworkAge++;
         if (this.fireworkAge > this.particleMaxAge) {
            if (this.twinkle) {
               boolean ☃xxxxxxxxxx = this.isFarFromCamera();
               SoundEvent ☃xxxxxxxxxxx = ☃xxxxxxxxxx ? SoundEvents.ENTITY_FIREWORK_TWINKLE_FAR : SoundEvents.ENTITY_FIREWORK_TWINKLE;
               this.world.playSound(this.posX, this.posY, this.posZ, ☃xxxxxxxxxxx, SoundCategory.AMBIENT, 20.0F, 0.9F + this.rand.nextFloat() * 0.15F, true);
            }

            this.setExpired();
         }
      }

      private boolean isFarFromCamera() {
         Minecraft ☃ = Minecraft.getMinecraft();
         return ☃ == null || ☃.getRenderViewEntity() == null || !(☃.getRenderViewEntity().getDistanceSq(this.posX, this.posY, this.posZ) < 256.0);
      }

      private void createParticle(
         double var1, double var3, double var5, double var7, double var9, double var11, int[] var13, int[] var14, boolean var15, boolean var16
      ) {
         ParticleFirework.Spark ☃ = new ParticleFirework.Spark(this.world, ☃, ☃, ☃, ☃, ☃, ☃, this.manager);
         ☃.setAlphaF(0.99F);
         ☃.setTrail(☃);
         ☃.setTwinkle(☃);
         int ☃x = this.rand.nextInt(☃.length);
         ☃.setColor(☃[☃x]);
         if (☃ != null && ☃.length > 0) {
            ☃.setColorFade(☃[this.rand.nextInt(☃.length)]);
         }

         this.manager.addEffect(☃);
      }

      private void createBall(double var1, int var3, int[] var4, int[] var5, boolean var6, boolean var7) {
         double ☃ = this.posX;
         double ☃x = this.posY;
         double ☃xx = this.posZ;

         for (int ☃xxx = -☃; ☃xxx <= ☃; ☃xxx++) {
            for (int ☃xxxx = -☃; ☃xxxx <= ☃; ☃xxxx++) {
               for (int ☃xxxxx = -☃; ☃xxxxx <= ☃; ☃xxxxx++) {
                  double ☃xxxxxx = ☃xxxx + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                  double ☃xxxxxxx = ☃xxx + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                  double ☃xxxxxxxx = ☃xxxxx + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5;
                  double ☃xxxxxxxxx = MathHelper.sqrt(☃xxxxxx * ☃xxxxxx + ☃xxxxxxx * ☃xxxxxxx + ☃xxxxxxxx * ☃xxxxxxxx) / ☃ + this.rand.nextGaussian() * 0.05;
                  this.createParticle(☃, ☃x, ☃xx, ☃xxxxxx / ☃xxxxxxxxx, ☃xxxxxxx / ☃xxxxxxxxx, ☃xxxxxxxx / ☃xxxxxxxxx, ☃, ☃, ☃, ☃);
                  if (☃xxx != -☃ && ☃xxx != ☃ && ☃xxxx != -☃ && ☃xxxx != ☃) {
                     ☃xxxxx += ☃ * 2 - 1;
                  }
               }
            }
         }
      }

      private void createShaped(double var1, double[][] var3, int[] var4, int[] var5, boolean var6, boolean var7, boolean var8) {
         double ☃ = ☃[0][0];
         double ☃x = ☃[0][1];
         this.createParticle(this.posX, this.posY, this.posZ, ☃ * ☃, ☃x * ☃, 0.0, ☃, ☃, ☃, ☃);
         float ☃xx = this.rand.nextFloat() * (float) Math.PI;
         double ☃xxx = ☃ ? 0.034 : 0.34;

         for (int ☃xxxx = 0; ☃xxxx < 3; ☃xxxx++) {
            double ☃xxxxx = ☃xx + ☃xxxx * (float) Math.PI * ☃xxx;
            double ☃xxxxxx = ☃;
            double ☃xxxxxxx = ☃x;

            for (int ☃xxxxxxxx = 1; ☃xxxxxxxx < ☃.length; ☃xxxxxxxx++) {
               double ☃xxxxxxxxx = ☃[☃xxxxxxxx][0];
               double ☃xxxxxxxxxx = ☃[☃xxxxxxxx][1];

               for (double ☃xxxxxxxxxxx = 0.25; ☃xxxxxxxxxxx <= 1.0; ☃xxxxxxxxxxx += 0.25) {
                  double ☃xxxxxxxxxxxx = (☃xxxxxx + (☃xxxxxxxxx - ☃xxxxxx) * ☃xxxxxxxxxxx) * ☃;
                  double ☃xxxxxxxxxxxxx = (☃xxxxxxx + (☃xxxxxxxxxx - ☃xxxxxxx) * ☃xxxxxxxxxxx) * ☃;
                  double ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxx * Math.sin(☃xxxxx);
                  ☃xxxxxxxxxxxx *= Math.cos(☃xxxxx);

                  for (double ☃xxxxxxxxxxxxxxx = -1.0; ☃xxxxxxxxxxxxxxx <= 1.0; ☃xxxxxxxxxxxxxxx += 2.0) {
                     this.createParticle(
                        this.posX, this.posY, this.posZ, ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx, ☃, ☃, ☃, ☃
                     );
                  }
               }

               ☃xxxxxx = ☃xxxxxxxxx;
               ☃xxxxxxx = ☃xxxxxxxxxx;
            }
         }
      }

      private void createBurst(int[] var1, int[] var2, boolean var3, boolean var4) {
         double ☃ = this.rand.nextGaussian() * 0.05;
         double ☃x = this.rand.nextGaussian() * 0.05;

         for (int ☃xx = 0; ☃xx < 70; ☃xx++) {
            double ☃xxx = this.motionX * 0.5 + this.rand.nextGaussian() * 0.15 + ☃;
            double ☃xxxx = this.motionZ * 0.5 + this.rand.nextGaussian() * 0.15 + ☃x;
            double ☃xxxxx = this.motionY * 0.5 + this.rand.nextDouble() * 0.5;
            this.createParticle(this.posX, this.posY, this.posZ, ☃xxx, ☃xxxxx, ☃xxxx, ☃, ☃, ☃, ☃);
         }
      }

      @Override
      public int getFXLayer() {
         return 0;
      }
   }
}
