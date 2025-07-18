package net.minecraft.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumDifficulty;

public class FoodStats {
   private int foodLevel = 20;
   private float foodSaturationLevel;
   private float foodExhaustionLevel;
   private int foodTimer;
   private int prevFoodLevel = 20;

   public FoodStats() {
      this.foodSaturationLevel = 5.0F;
   }

   public void addStats(int var1, float var2) {
      this.foodLevel = Math.min(☃ + this.foodLevel, 20);
      this.foodSaturationLevel = Math.min(this.foodSaturationLevel + ☃ * ☃ * 2.0F, (float)this.foodLevel);
   }

   public void addStats(ItemFood var1, ItemStack var2) {
      this.addStats(☃.getHealAmount(☃), ☃.getSaturationModifier(☃));
   }

   public void onUpdate(EntityPlayer var1) {
      EnumDifficulty ☃ = ☃.world.getDifficulty();
      this.prevFoodLevel = this.foodLevel;
      if (this.foodExhaustionLevel > 4.0F) {
         this.foodExhaustionLevel -= 4.0F;
         if (this.foodSaturationLevel > 0.0F) {
            this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
         } else if (☃ != EnumDifficulty.PEACEFUL) {
            this.foodLevel = Math.max(this.foodLevel - 1, 0);
         }
      }

      boolean ☃x = ☃.world.getGameRules().getBoolean("naturalRegeneration");
      if (☃x && this.foodSaturationLevel > 0.0F && ☃.shouldHeal() && this.foodLevel >= 20) {
         this.foodTimer++;
         if (this.foodTimer >= 10) {
            float ☃xx = Math.min(this.foodSaturationLevel, 6.0F);
            ☃.heal(☃xx / 6.0F);
            this.addExhaustion(☃xx);
            this.foodTimer = 0;
         }
      } else if (☃x && this.foodLevel >= 18 && ☃.shouldHeal()) {
         this.foodTimer++;
         if (this.foodTimer >= 80) {
            ☃.heal(1.0F);
            this.addExhaustion(6.0F);
            this.foodTimer = 0;
         }
      } else if (this.foodLevel <= 0) {
         this.foodTimer++;
         if (this.foodTimer >= 80) {
            if (☃.getHealth() > 10.0F || ☃ == EnumDifficulty.HARD || ☃.getHealth() > 1.0F && ☃ == EnumDifficulty.NORMAL) {
               ☃.attackEntityFrom(DamageSource.STARVE, 1.0F);
            }

            this.foodTimer = 0;
         }
      } else {
         this.foodTimer = 0;
      }
   }

   public void readNBT(NBTTagCompound var1) {
      if (☃.hasKey("foodLevel", 99)) {
         this.foodLevel = ☃.getInteger("foodLevel");
         this.foodTimer = ☃.getInteger("foodTickTimer");
         this.foodSaturationLevel = ☃.getFloat("foodSaturationLevel");
         this.foodExhaustionLevel = ☃.getFloat("foodExhaustionLevel");
      }
   }

   public void writeNBT(NBTTagCompound var1) {
      ☃.setInteger("foodLevel", this.foodLevel);
      ☃.setInteger("foodTickTimer", this.foodTimer);
      ☃.setFloat("foodSaturationLevel", this.foodSaturationLevel);
      ☃.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
   }

   public int getFoodLevel() {
      return this.foodLevel;
   }

   public boolean needFood() {
      return this.foodLevel < 20;
   }

   public void addExhaustion(float var1) {
      this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + ☃, 40.0F);
   }

   public float getSaturationLevel() {
      return this.foodSaturationLevel;
   }

   public void setFoodLevel(int var1) {
      this.foodLevel = ☃;
   }

   public void setFoodSaturationLevel(float var1) {
      this.foodSaturationLevel = ☃;
   }
}
