package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEgg extends EntityThrowable {
   public EntityEgg(World var1) {
      super(☃);
   }

   public EntityEgg(World var1, EntityLivingBase var2) {
      super(☃, ☃);
   }

   public EntityEgg(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesEgg(DataFixer var0) {
      EntityThrowable.registerFixesThrowable(☃, "ThrownEgg");
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 3) {
         double ☃ = 0.08;

         for (int ☃x = 0; ☃x < 8; ☃x++) {
            this.world
               .spawnParticle(
                  EnumParticleTypes.ITEM_CRACK,
                  this.posX,
                  this.posY,
                  this.posZ,
                  (this.rand.nextFloat() - 0.5) * 0.08,
                  (this.rand.nextFloat() - 0.5) * 0.08,
                  (this.rand.nextFloat() - 0.5) * 0.08,
                  Item.getIdFromItem(Items.EGG)
               );
         }
      }
   }

   @Override
   protected void onImpact(RayTraceResult var1) {
      if (☃.entityHit != null) {
         ☃.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
      }

      if (!this.world.isRemote) {
         if (this.rand.nextInt(8) == 0) {
            int ☃ = 1;
            if (this.rand.nextInt(32) == 0) {
               ☃ = 4;
            }

            for (int ☃x = 0; ☃x < ☃; ☃x++) {
               EntityChicken ☃xx = new EntityChicken(this.world);
               ☃xx.setGrowingAge(-24000);
               ☃xx.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
               this.world.spawnEntity(☃xx);
            }
         }

         this.world.setEntityState(this, (byte)3);
         this.setDead();
      }
   }
}
