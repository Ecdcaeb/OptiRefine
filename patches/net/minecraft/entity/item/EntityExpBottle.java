package net.minecraft.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityExpBottle extends EntityThrowable {
   public EntityExpBottle(World var1) {
      super(☃);
   }

   public EntityExpBottle(World var1, EntityLivingBase var2) {
      super(☃, ☃);
   }

   public EntityExpBottle(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesExpBottle(DataFixer var0) {
      EntityThrowable.registerFixesThrowable(☃, "ThrowableExpBottle");
   }

   @Override
   protected float getGravityVelocity() {
      return 0.07F;
   }

   @Override
   protected void onImpact(RayTraceResult var1) {
      if (!this.world.isRemote) {
         this.world.playEvent(2002, new BlockPos(this), PotionUtils.getPotionColor(PotionTypes.WATER));
         int ☃ = 3 + this.world.rand.nextInt(5) + this.world.rand.nextInt(5);

         while (☃ > 0) {
            int ☃x = EntityXPOrb.getXPSplit(☃);
            ☃ -= ☃x;
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, ☃x));
         }

         this.setDead();
      }
   }
}
