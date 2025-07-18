package net.minecraft.client.particle;

import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Particle {
   private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
   protected World world;
   protected double prevPosX;
   protected double prevPosY;
   protected double prevPosZ;
   protected double posX;
   protected double posY;
   protected double posZ;
   protected double motionX;
   protected double motionY;
   protected double motionZ;
   private AxisAlignedBB boundingBox = EMPTY_AABB;
   protected boolean onGround;
   protected boolean canCollide;
   protected boolean isExpired;
   protected float width = 0.6F;
   protected float height = 1.8F;
   protected Random rand = new Random();
   protected int particleTextureIndexX;
   protected int particleTextureIndexY;
   protected float particleTextureJitterX;
   protected float particleTextureJitterY;
   protected int particleAge;
   protected int particleMaxAge;
   protected float particleScale;
   protected float particleGravity;
   protected float particleRed;
   protected float particleGreen;
   protected float particleBlue;
   protected float particleAlpha = 1.0F;
   protected TextureAtlasSprite particleTexture;
   protected float particleAngle;
   protected float prevParticleAngle;
   public static double interpPosX;
   public static double interpPosY;
   public static double interpPosZ;
   public static Vec3d cameraViewDir;

   protected Particle(World var1, double var2, double var4, double var6) {
      this.world = ☃;
      this.setSize(0.2F, 0.2F);
      this.setPosition(☃, ☃, ☃);
      this.prevPosX = ☃;
      this.prevPosY = ☃;
      this.prevPosZ = ☃;
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
      this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
      this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
      this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
      this.particleAge = 0;
      this.canCollide = true;
   }

   public Particle(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      this(☃, ☃, ☃, ☃);
      this.motionX = ☃ + (Math.random() * 2.0 - 1.0) * 0.4F;
      this.motionY = ☃ + (Math.random() * 2.0 - 1.0) * 0.4F;
      this.motionZ = ☃ + (Math.random() * 2.0 - 1.0) * 0.4F;
      float ☃ = (float)(Math.random() + Math.random() + 1.0) * 0.15F;
      float ☃x = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
      this.motionX = this.motionX / ☃x * ☃ * 0.4F;
      this.motionY = this.motionY / ☃x * ☃ * 0.4F + 0.1F;
      this.motionZ = this.motionZ / ☃x * ☃ * 0.4F;
   }

   public Particle multiplyVelocity(float var1) {
      this.motionX *= ☃;
      this.motionY = (this.motionY - 0.1F) * ☃ + 0.1F;
      this.motionZ *= ☃;
      return this;
   }

   public Particle multipleParticleScaleBy(float var1) {
      this.setSize(0.2F * ☃, 0.2F * ☃);
      this.particleScale *= ☃;
      return this;
   }

   public void setRBGColorF(float var1, float var2, float var3) {
      this.particleRed = ☃;
      this.particleGreen = ☃;
      this.particleBlue = ☃;
   }

   public void setAlphaF(float var1) {
      this.particleAlpha = ☃;
   }

   public boolean shouldDisableDepth() {
      return false;
   }

   public float getRedColorF() {
      return this.particleRed;
   }

   public float getGreenColorF() {
      return this.particleGreen;
   }

   public float getBlueColorF() {
      return this.particleBlue;
   }

   public void setMaxAge(int var1) {
      this.particleMaxAge = ☃;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setExpired();
      }

      this.motionY = this.motionY - 0.04 * this.particleGravity;
      this.move(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }

   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = this.particleTextureIndexX / 16.0F;
      float ☃x = ☃ + 0.0624375F;
      float ☃xx = this.particleTextureIndexY / 16.0F;
      float ☃xxx = ☃xx + 0.0624375F;
      float ☃xxxx = 0.1F * this.particleScale;
      if (this.particleTexture != null) {
         ☃ = this.particleTexture.getMinU();
         ☃x = this.particleTexture.getMaxU();
         ☃xx = this.particleTexture.getMinV();
         ☃xxx = this.particleTexture.getMaxV();
      }

      float ☃xxxxx = (float)(this.prevPosX + (this.posX - this.prevPosX) * ☃ - interpPosX);
      float ☃xxxxxx = (float)(this.prevPosY + (this.posY - this.prevPosY) * ☃ - interpPosY);
      float ☃xxxxxxx = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * ☃ - interpPosZ);
      int ☃xxxxxxxx = this.getBrightnessForRender(☃);
      int ☃xxxxxxxxx = ☃xxxxxxxx >> 16 & 65535;
      int ☃xxxxxxxxxx = ☃xxxxxxxx & 65535;
      Vec3d[] ☃xxxxxxxxxxx = new Vec3d[]{
         new Vec3d(-☃ * ☃xxxx - ☃ * ☃xxxx, -☃ * ☃xxxx, -☃ * ☃xxxx - ☃ * ☃xxxx),
         new Vec3d(-☃ * ☃xxxx + ☃ * ☃xxxx, ☃ * ☃xxxx, -☃ * ☃xxxx + ☃ * ☃xxxx),
         new Vec3d(☃ * ☃xxxx + ☃ * ☃xxxx, ☃ * ☃xxxx, ☃ * ☃xxxx + ☃ * ☃xxxx),
         new Vec3d(☃ * ☃xxxx - ☃ * ☃xxxx, -☃ * ☃xxxx, ☃ * ☃xxxx - ☃ * ☃xxxx)
      };
      if (this.particleAngle != 0.0F) {
         float ☃xxxxxxxxxxxx = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * ☃;
         float ☃xxxxxxxxxxxxx = MathHelper.cos(☃xxxxxxxxxxxx * 0.5F);
         float ☃xxxxxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxxxx * 0.5F) * (float)cameraViewDir.x;
         float ☃xxxxxxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxxxx * 0.5F) * (float)cameraViewDir.y;
         float ☃xxxxxxxxxxxxxxxx = MathHelper.sin(☃xxxxxxxxxxxx * 0.5F) * (float)cameraViewDir.z;
         Vec3d ☃xxxxxxxxxxxxxxxxx = new Vec3d(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx);

         for (int ☃xxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxxxxxxx++) {
            ☃xxxxxxxxxxx[☃xxxxxxxxxxxxxxxxxx] = ☃xxxxxxxxxxxxxxxxx.scale(2.0 * ☃xxxxxxxxxxx[☃xxxxxxxxxxxxxxxxxx].dotProduct(☃xxxxxxxxxxxxxxxxx))
               .add(☃xxxxxxxxxxx[☃xxxxxxxxxxxxxxxxxx].scale(☃xxxxxxxxxxxxx * ☃xxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxx.dotProduct(☃xxxxxxxxxxxxxxxxx)))
               .add(☃xxxxxxxxxxxxxxxxx.crossProduct(☃xxxxxxxxxxx[☃xxxxxxxxxxxxxxxxxx]).scale(2.0F * ☃xxxxxxxxxxxxx));
         }
      }

      ☃.pos(☃xxxxx + ☃xxxxxxxxxxx[0].x, ☃xxxxxx + ☃xxxxxxxxxxx[0].y, ☃xxxxxxx + ☃xxxxxxxxxxx[0].z)
         .tex(☃x, ☃xxx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
      ☃.pos(☃xxxxx + ☃xxxxxxxxxxx[1].x, ☃xxxxxx + ☃xxxxxxxxxxx[1].y, ☃xxxxxxx + ☃xxxxxxxxxxx[1].z)
         .tex(☃x, ☃xx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
      ☃.pos(☃xxxxx + ☃xxxxxxxxxxx[2].x, ☃xxxxxx + ☃xxxxxxxxxxx[2].y, ☃xxxxxxx + ☃xxxxxxxxxxx[2].z)
         .tex(☃, ☃xx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
      ☃.pos(☃xxxxx + ☃xxxxxxxxxxx[3].x, ☃xxxxxx + ☃xxxxxxxxxxx[3].y, ☃xxxxxxx + ☃xxxxxxxxxxx[3].z)
         .tex(☃, ☃xxx)
         .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
         .lightmap(☃xxxxxxxxx, ☃xxxxxxxxxx)
         .endVertex();
   }

   public int getFXLayer() {
      return 0;
   }

   public void setParticleTexture(TextureAtlasSprite var1) {
      int ☃ = this.getFXLayer();
      if (☃ == 1) {
         this.particleTexture = ☃;
      } else {
         throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
      }
   }

   public void setParticleTextureIndex(int var1) {
      if (this.getFXLayer() != 0) {
         throw new RuntimeException("Invalid call to Particle.setMiscTex");
      } else {
         this.particleTextureIndexX = ☃ % 16;
         this.particleTextureIndexY = ☃ / 16;
      }
   }

   public void nextTextureIndexX() {
      this.particleTextureIndexX++;
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName()
         + ", Pos ("
         + this.posX
         + ","
         + this.posY
         + ","
         + this.posZ
         + "), RGBA ("
         + this.particleRed
         + ","
         + this.particleGreen
         + ","
         + this.particleBlue
         + ","
         + this.particleAlpha
         + "), Age "
         + this.particleAge;
   }

   public void setExpired() {
      this.isExpired = true;
   }

   protected void setSize(float var1, float var2) {
      if (☃ != this.width || ☃ != this.height) {
         this.width = ☃;
         this.height = ☃;
         AxisAlignedBB ☃ = this.getBoundingBox();
         this.setBoundingBox(new AxisAlignedBB(☃.minX, ☃.minY, ☃.minZ, ☃.minX + this.width, ☃.minY + this.height, ☃.minZ + this.width));
      }
   }

   public void setPosition(double var1, double var3, double var5) {
      this.posX = ☃;
      this.posY = ☃;
      this.posZ = ☃;
      float ☃ = this.width / 2.0F;
      float ☃x = this.height;
      this.setBoundingBox(new AxisAlignedBB(☃ - ☃, ☃, ☃ - ☃, ☃ + ☃, ☃ + ☃x, ☃ + ☃));
   }

   public void move(double var1, double var3, double var5) {
      double ☃ = ☃;
      double ☃x = ☃;
      double ☃xx = ☃;
      if (this.canCollide) {
         List<AxisAlignedBB> ☃xxx = this.world.getCollisionBoxes(null, this.getBoundingBox().expand(☃, ☃, ☃));

         for (AxisAlignedBB ☃xxxx : ☃xxx) {
            ☃ = ☃xxxx.calculateYOffset(this.getBoundingBox(), ☃);
         }

         this.setBoundingBox(this.getBoundingBox().offset(0.0, ☃, 0.0));

         for (AxisAlignedBB ☃xxxx : ☃xxx) {
            ☃ = ☃xxxx.calculateXOffset(this.getBoundingBox(), ☃);
         }

         this.setBoundingBox(this.getBoundingBox().offset(☃, 0.0, 0.0));

         for (AxisAlignedBB ☃xxxx : ☃xxx) {
            ☃ = ☃xxxx.calculateZOffset(this.getBoundingBox(), ☃);
         }

         this.setBoundingBox(this.getBoundingBox().offset(0.0, 0.0, ☃));
      } else {
         this.setBoundingBox(this.getBoundingBox().offset(☃, ☃, ☃));
      }

      this.resetPositionToBB();
      this.onGround = ☃x != ☃ && ☃x < 0.0;
      if (☃ != ☃) {
         this.motionX = 0.0;
      }

      if (☃xx != ☃) {
         this.motionZ = 0.0;
      }
   }

   protected void resetPositionToBB() {
      AxisAlignedBB ☃ = this.getBoundingBox();
      this.posX = (☃.minX + ☃.maxX) / 2.0;
      this.posY = ☃.minY;
      this.posZ = (☃.minZ + ☃.maxZ) / 2.0;
   }

   public int getBrightnessForRender(float var1) {
      BlockPos ☃ = new BlockPos(this.posX, this.posY, this.posZ);
      return this.world.isBlockLoaded(☃) ? this.world.getCombinedLight(☃, 0) : 0;
   }

   public boolean isAlive() {
      return !this.isExpired;
   }

   public AxisAlignedBB getBoundingBox() {
      return this.boundingBox;
   }

   public void setBoundingBox(AxisAlignedBB var1) {
      this.boundingBox = ☃;
   }
}
