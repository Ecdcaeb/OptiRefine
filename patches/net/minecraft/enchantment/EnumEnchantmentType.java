package net.minecraft.enchantment;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public enum EnumEnchantmentType {
   ALL {
      @Override
      public boolean canEnchantItem(Item var1) {
         for (EnumEnchantmentType ☃ : EnumEnchantmentType.values()) {
            if (☃ != EnumEnchantmentType.ALL && ☃.canEnchantItem(☃)) {
               return true;
            }
         }

         return false;
      }
   },
   ARMOR {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃ instanceof ItemArmor;
      }
   },
   ARMOR_FEET {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃ instanceof ItemArmor && ((ItemArmor)☃).armorType == EntityEquipmentSlot.FEET;
      }
   },
   ARMOR_LEGS {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃ instanceof ItemArmor && ((ItemArmor)☃).armorType == EntityEquipmentSlot.LEGS;
      }
   },
   ARMOR_CHEST {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃ instanceof ItemArmor && ((ItemArmor)☃).armorType == EntityEquipmentSlot.CHEST;
      }
   },
   ARMOR_HEAD {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃ instanceof ItemArmor && ((ItemArmor)☃).armorType == EntityEquipmentSlot.HEAD;
      }
   },
   WEAPON {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃ instanceof ItemSword;
      }
   },
   DIGGER {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃ instanceof ItemTool;
      }
   },
   FISHING_ROD {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃ instanceof ItemFishingRod;
      }
   },
   BREAKABLE {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃.isDamageable();
      }
   },
   BOW {
      @Override
      public boolean canEnchantItem(Item var1) {
         return ☃ instanceof ItemBow;
      }
   },
   WEARABLE {
      @Override
      public boolean canEnchantItem(Item var1) {
         boolean ☃ = ☃ instanceof ItemBlock && ((ItemBlock)☃).getBlock() instanceof BlockPumpkin;
         return ☃ instanceof ItemArmor || ☃ instanceof ItemElytra || ☃ instanceof ItemSkull || ☃;
      }
   };

   private EnumEnchantmentType() {
   }

   public abstract boolean canEnchantItem(Item var1);
}
