package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;

public final class EntitySelectors {
   public static final Predicate<Entity> IS_ALIVE = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return ☃.isEntityAlive();
      }
   };
   public static final Predicate<Entity> IS_STANDALONE = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return ☃.isEntityAlive() && !☃.isBeingRidden() && !☃.isRiding();
      }
   };
   public static final Predicate<Entity> HAS_INVENTORY = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return ☃ instanceof IInventory && ☃.isEntityAlive();
      }
   };
   public static final Predicate<Entity> CAN_AI_TARGET = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return !(☃ instanceof EntityPlayer) || !((EntityPlayer)☃).isSpectator() && !((EntityPlayer)☃).isCreative();
      }
   };
   public static final Predicate<Entity> NOT_SPECTATING = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity var1) {
         return !(☃ instanceof EntityPlayer) || !((EntityPlayer)☃).isSpectator();
      }
   };

   public static <T extends Entity> Predicate<T> withinRange(final double var0, final double var2, final double var4, double var6) {
      final double ☃ = ☃ * ☃;
      return new Predicate<T>() {
         public boolean apply(@Nullable T var1) {
            return ☃ != null && ☃.getDistanceSq(☃, ☃, ☃) <= ☃;
         }
      };
   }

   public static <T extends Entity> Predicate<T> getTeamCollisionPredicate(final Entity var0) {
      final Team ☃ = ☃.getTeam();
      final Team.CollisionRule ☃x = ☃ == null ? Team.CollisionRule.ALWAYS : ☃.getCollisionRule();
      return ☃x == Team.CollisionRule.NEVER
         ? Predicates.alwaysFalse()
         : Predicates.and(
            NOT_SPECTATING,
            new Predicate<Entity>() {
               public boolean apply(@Nullable Entity var1x) {
                  if (!☃.canBePushed()) {
                     return false;
                  } else if (!☃.world.isRemote || ☃ instanceof EntityPlayer && ((EntityPlayer)☃).isUser()) {
                     Team ☃xx = ☃.getTeam();
                     Team.CollisionRule ☃x = ☃xx == null ? Team.CollisionRule.ALWAYS : ☃xx.getCollisionRule();
                     if (☃x == Team.CollisionRule.NEVER) {
                        return false;
                     } else {
                        boolean ☃xx = ☃ != null && ☃.isSameTeam(☃xx);
                        return (☃ == Team.CollisionRule.HIDE_FOR_OWN_TEAM || ☃x == Team.CollisionRule.HIDE_FOR_OWN_TEAM) && ☃xx
                           ? false
                           : ☃ != Team.CollisionRule.HIDE_FOR_OTHER_TEAMS && ☃x != Team.CollisionRule.HIDE_FOR_OTHER_TEAMS || ☃xx;
                     }
                  } else {
                     return false;
                  }
               }
            }
         );
   }

   public static Predicate<Entity> notRiding(final Entity var0) {
      return new Predicate<Entity>() {
         public boolean apply(@Nullable Entity var1) {
            while (☃.isRiding()) {
               ☃ = ☃.getRidingEntity();
               if (☃ == ☃) {
                  return false;
               }
            }

            return true;
         }
      };
   }

   public static class ArmoredMob implements Predicate<Entity> {
      private final ItemStack armor;

      public ArmoredMob(ItemStack var1) {
         this.armor = ☃;
      }

      public boolean apply(@Nullable Entity var1) {
         if (!☃.isEntityAlive()) {
            return false;
         } else if (!(☃ instanceof EntityLivingBase)) {
            return false;
         } else {
            EntityLivingBase ☃ = (EntityLivingBase)☃;
            if (!☃.getItemStackFromSlot(EntityLiving.getSlotForItemStack(this.armor)).isEmpty()) {
               return false;
            } else if (☃ instanceof EntityLiving) {
               return ((EntityLiving)☃).canPickUpLoot();
            } else {
               return ☃ instanceof EntityArmorStand ? true : ☃ instanceof EntityPlayer;
            }
         }
      }
   }
}
