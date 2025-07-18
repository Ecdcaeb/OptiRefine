package net.minecraft.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityWitherSkull extends EntityFireball {
   private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.createKey(EntityWitherSkull.class, DataSerializers.BOOLEAN);

   public EntityWitherSkull(World var1) {
      super(☃);
      this.setSize(0.3125F, 0.3125F);
   }

   public EntityWitherSkull(World var1, EntityLivingBase var2, double var3, double var5, double var7) {
      super(☃, ☃, ☃, ☃, ☃);
      this.setSize(0.3125F, 0.3125F);
   }

   public static void registerFixesWitherSkull(DataFixer var0) {
      EntityFireball.registerFixesFireball(☃, "WitherSkull");
   }

   @Override
   protected float getMotionFactor() {
      return this.isInvulnerable() ? 0.73F : super.getMotionFactor();
   }

   public EntityWitherSkull(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.setSize(0.3125F, 0.3125F);
   }

   @Override
   public boolean isBurning() {
      return false;
   }

   @Override
   public float getExplosionResistance(Explosion var1, World var2, BlockPos var3, IBlockState var4) {
      float ☃ = super.getExplosionResistance(☃, ☃, ☃, ☃);
      Block ☃x = ☃.getBlock();
      if (this.isInvulnerable() && EntityWither.canDestroyBlock(☃x)) {
         ☃ = Math.min(0.8F, ☃);
      }

      return ☃;
   }

   @Override
   protected void onImpact(RayTraceResult var1) {
      if (!this.world.isRemote) {
         if (☃.entityHit != null) {
            if (this.shootingEntity != null) {
               if (☃.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F)) {
                  if (☃.entityHit.isEntityAlive()) {
                     this.applyEnchantments(this.shootingEntity, ☃.entityHit);
                  } else {
                     this.shootingEntity.heal(5.0F);
                  }
               }
            } else {
               ☃.entityHit.attackEntityFrom(DamageSource.MAGIC, 5.0F);
            }

            if (☃.entityHit instanceof EntityLivingBase) {
               int ☃ = 0;
               if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                  ☃ = 10;
               } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                  ☃ = 40;
               }

               if (☃ > 0) {
                  ((EntityLivingBase)☃.entityHit).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * ☃, 1));
               }
            }
         }

         this.world.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
         this.setDead();
      }
   }

   @Override
   public boolean canBeCollidedWith() {
      return false;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return false;
   }

   @Override
   protected void entityInit() {
      this.dataManager.register(INVULNERABLE, false);
   }

   public boolean isInvulnerable() {
      return this.dataManager.get(INVULNERABLE);
   }

   public void setInvulnerable(boolean var1) {
      this.dataManager.set(INVULNERABLE, ☃);
   }

   @Override
   protected boolean isFireballFiery() {
      return false;
   }
}
