package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball {
   public EntitySmallFireball(World var1) {
      super(☃);
      this.setSize(0.3125F, 0.3125F);
   }

   public EntitySmallFireball(World var1, EntityLivingBase var2, double var3, double var5, double var7) {
      super(☃, ☃, ☃, ☃, ☃);
      this.setSize(0.3125F, 0.3125F);
   }

   public EntitySmallFireball(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      this.setSize(0.3125F, 0.3125F);
   }

   public static void registerFixesSmallFireball(DataFixer var0) {
      EntityFireball.registerFixesFireball(☃, "SmallFireball");
   }

   @Override
   protected void onImpact(RayTraceResult var1) {
      if (!this.world.isRemote) {
         if (☃.entityHit != null) {
            if (!☃.entityHit.isImmuneToFire()) {
               boolean ☃ = ☃.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);
               if (☃) {
                  this.applyEnchantments(this.shootingEntity, ☃.entityHit);
                  ☃.entityHit.setFire(5);
               }
            }
         } else {
            boolean ☃ = true;
            if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving) {
               ☃ = this.world.getGameRules().getBoolean("mobGriefing");
            }

            if (☃) {
               BlockPos ☃x = ☃.getBlockPos().offset(☃.sideHit);
               if (this.world.isAirBlock(☃x)) {
                  this.world.setBlockState(☃x, Blocks.FIRE.getDefaultState());
               }
            }
         }

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
}
