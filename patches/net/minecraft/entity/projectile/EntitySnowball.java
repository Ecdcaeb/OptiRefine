package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySnowball extends EntityThrowable {
   public EntitySnowball(World var1) {
      super(☃);
   }

   public EntitySnowball(World var1, EntityLivingBase var2) {
      super(☃, ☃);
   }

   public EntitySnowball(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesSnowball(DataFixer var0) {
      EntityThrowable.registerFixesThrowable(☃, "Snowball");
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 3) {
         for (int ☃ = 0; ☃ < 8; ☃++) {
            this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0);
         }
      }
   }

   @Override
   protected void onImpact(RayTraceResult var1) {
      if (☃.entityHit != null) {
         int ☃ = 0;
         if (☃.entityHit instanceof EntityBlaze) {
            ☃ = 3;
         }

         ☃.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), ☃);
      }

      if (!this.world.isRemote) {
         this.world.setEntityState(this, (byte)3);
         this.setDead();
      }
   }
}
