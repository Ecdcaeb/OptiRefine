package net.minecraft.entity.ai;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget {
   protected final Class<T> targetClass;
   private final int targetChance;
   protected final EntityAINearestAttackableTarget.Sorter sorter;
   protected final Predicate<? super T> targetEntitySelector;
   protected T targetEntity;

   public EntityAINearestAttackableTarget(EntityCreature var1, Class<T> var2, boolean var3) {
      this(☃, ☃, ☃, false);
   }

   public EntityAINearestAttackableTarget(EntityCreature var1, Class<T> var2, boolean var3, boolean var4) {
      this(☃, ☃, 10, ☃, ☃, null);
   }

   public EntityAINearestAttackableTarget(EntityCreature var1, Class<T> var2, int var3, boolean var4, boolean var5, @Nullable final Predicate<? super T> var6) {
      super(☃, ☃, ☃);
      this.targetClass = ☃;
      this.targetChance = ☃;
      this.sorter = new EntityAINearestAttackableTarget.Sorter(☃);
      this.setMutexBits(1);
      this.targetEntitySelector = new Predicate<T>() {
         public boolean apply(@Nullable T var1) {
            if (☃ == null) {
               return false;
            } else if (☃ != null && !☃.apply(☃)) {
               return false;
            } else {
               return !EntitySelectors.NOT_SPECTATING.apply(☃) ? false : EntityAINearestAttackableTarget.this.isSuitableTarget(☃, false);
            }
         }
      };
   }

   @Override
   public boolean shouldExecute() {
      if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
         return false;
      } else if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class) {
         List<T> ☃ = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
         if (☃.isEmpty()) {
            return false;
         } else {
            Collections.sort(☃, this.sorter);
            this.targetEntity = ☃.get(0);
            return true;
         }
      } else {
         this.targetEntity = (T)this.taskOwner
            .world
            .getNearestAttackablePlayer(
               this.taskOwner.posX,
               this.taskOwner.posY + this.taskOwner.getEyeHeight(),
               this.taskOwner.posZ,
               this.getTargetDistance(),
               this.getTargetDistance(),
               new Function<EntityPlayer, Double>() {
                  @Nullable
                  public Double apply(@Nullable EntityPlayer var1) {
                     ItemStack ☃ = ☃.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                     if (☃.getItem() == Items.SKULL) {
                        int ☃x = ☃.getItemDamage();
                        boolean ☃xx = EntityAINearestAttackableTarget.this.taskOwner instanceof EntitySkeleton && ☃x == 0;
                        boolean ☃xxx = EntityAINearestAttackableTarget.this.taskOwner instanceof EntityZombie && ☃x == 2;
                        boolean ☃xxxx = EntityAINearestAttackableTarget.this.taskOwner instanceof EntityCreeper && ☃x == 4;
                        if (☃xx || ☃xxx || ☃xxxx) {
                           return 0.5;
                        }
                     }

                     return 1.0;
                  }
               },
               this.targetEntitySelector
            );
         return this.targetEntity != null;
      }
   }

   protected AxisAlignedBB getTargetableArea(double var1) {
      return this.taskOwner.getEntityBoundingBox().grow(☃, 4.0, ☃);
   }

   @Override
   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.targetEntity);
      super.startExecuting();
   }

   public static class Sorter implements Comparator<Entity> {
      private final Entity entity;

      public Sorter(Entity var1) {
         this.entity = ☃;
      }

      public int compare(Entity var1, Entity var2) {
         double ☃ = this.entity.getDistanceSq(☃);
         double ☃x = this.entity.getDistanceSq(☃);
         if (☃ < ☃x) {
            return -1;
         } else {
            return ☃ > ☃x ? 1 : 0;
         }
      }
   }
}
