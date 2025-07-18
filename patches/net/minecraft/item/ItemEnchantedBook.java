package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemEnchantedBook extends Item {
   @Override
   public boolean hasEffect(ItemStack var1) {
      return true;
   }

   @Override
   public boolean isEnchantable(ItemStack var1) {
      return false;
   }

   @Override
   public EnumRarity getRarity(ItemStack var1) {
      return getEnchantments(☃).isEmpty() ? super.getRarity(☃) : EnumRarity.UNCOMMON;
   }

   public static NBTTagList getEnchantments(ItemStack var0) {
      NBTTagCompound ☃ = ☃.getTagCompound();
      return ☃ != null ? ☃.getTagList("StoredEnchantments", 10) : new NBTTagList();
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      super.addInformation(☃, ☃, ☃, ☃);
      NBTTagList ☃ = getEnchantments(☃);

      for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
         NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
         int ☃xxx = ☃xx.getShort("id");
         Enchantment ☃xxxx = Enchantment.getEnchantmentByID(☃xxx);
         if (☃xxxx != null) {
            ☃.add(☃xxxx.getTranslatedName(☃xx.getShort("lvl")));
         }
      }
   }

   public static void addEnchantment(ItemStack var0, EnchantmentData var1) {
      NBTTagList ☃ = getEnchantments(☃);
      boolean ☃x = true;

      for (int ☃xx = 0; ☃xx < ☃.tagCount(); ☃xx++) {
         NBTTagCompound ☃xxx = ☃.getCompoundTagAt(☃xx);
         if (Enchantment.getEnchantmentByID(☃xxx.getShort("id")) == ☃.enchantment) {
            if (☃xxx.getShort("lvl") < ☃.enchantmentLevel) {
               ☃xxx.setShort("lvl", (short)☃.enchantmentLevel);
            }

            ☃x = false;
            break;
         }
      }

      if (☃x) {
         NBTTagCompound ☃xxx = new NBTTagCompound();
         ☃xxx.setShort("id", (short)Enchantment.getEnchantmentID(☃.enchantment));
         ☃xxx.setShort("lvl", (short)☃.enchantmentLevel);
         ☃.appendTag(☃xxx);
      }

      if (!☃.hasTagCompound()) {
         ☃.setTagCompound(new NBTTagCompound());
      }

      ☃.getTagCompound().setTag("StoredEnchantments", ☃);
   }

   public static ItemStack getEnchantedItemStack(EnchantmentData var0) {
      ItemStack ☃ = new ItemStack(Items.ENCHANTED_BOOK);
      addEnchantment(☃, ☃);
      return ☃;
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (☃ == CreativeTabs.SEARCH) {
         for (Enchantment ☃ : Enchantment.REGISTRY) {
            if (☃.type != null) {
               for (int ☃x = ☃.getMinLevel(); ☃x <= ☃.getMaxLevel(); ☃x++) {
                  ☃.add(getEnchantedItemStack(new EnchantmentData(☃, ☃x)));
               }
            }
         }
      } else if (☃.getRelevantEnchantmentTypes().length != 0) {
         for (Enchantment ☃x : Enchantment.REGISTRY) {
            if (☃.hasRelevantEnchantmentType(☃x.type)) {
               ☃.add(getEnchantedItemStack(new EnchantmentData(☃x, ☃x.getMaxLevel())));
            }
         }
      }
   }
}
