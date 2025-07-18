package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFireworkCharge extends Item {
   public static NBTBase getExplosionTag(ItemStack var0, String var1) {
      if (☃.hasTagCompound()) {
         NBTTagCompound ☃ = ☃.getTagCompound().getCompoundTag("Explosion");
         if (☃ != null) {
            return ☃.getTag(☃);
         }
      }

      return null;
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      if (☃.hasTagCompound()) {
         NBTTagCompound ☃ = ☃.getTagCompound().getCompoundTag("Explosion");
         if (☃ != null) {
            addExplosionInfo(☃, ☃);
         }
      }
   }

   public static void addExplosionInfo(NBTTagCompound var0, List<String> var1) {
      byte ☃ = ☃.getByte("Type");
      if (☃ >= 0 && ☃ <= 4) {
         ☃.add(I18n.translateToLocal("item.fireworksCharge.type." + ☃).trim());
      } else {
         ☃.add(I18n.translateToLocal("item.fireworksCharge.type").trim());
      }

      int[] ☃x = ☃.getIntArray("Colors");
      if (☃x.length > 0) {
         boolean ☃xx = true;
         String ☃xxx = "";

         for (int ☃xxxx : ☃x) {
            if (!☃xx) {
               ☃xxx = ☃xxx + ", ";
            }

            ☃xx = false;
            boolean ☃xxxxx = false;

            for (int ☃xxxxxx = 0; ☃xxxxxx < ItemDye.DYE_COLORS.length; ☃xxxxxx++) {
               if (☃xxxx == ItemDye.DYE_COLORS[☃xxxxxx]) {
                  ☃xxxxx = true;
                  ☃xxx = ☃xxx + I18n.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(☃xxxxxx).getTranslationKey());
                  break;
               }
            }

            if (!☃xxxxx) {
               ☃xxx = ☃xxx + I18n.translateToLocal("item.fireworksCharge.customColor");
            }
         }

         ☃.add(☃xxx);
      }

      int[] ☃xx = ☃.getIntArray("FadeColors");
      if (☃xx.length > 0) {
         boolean ☃xxx = true;
         String ☃xxxx = I18n.translateToLocal("item.fireworksCharge.fadeTo") + " ";

         for (int ☃xxxxx : ☃xx) {
            if (!☃xxx) {
               ☃xxxx = ☃xxxx + ", ";
            }

            ☃xxx = false;
            boolean ☃xxxxxxx = false;

            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 16; ☃xxxxxxxx++) {
               if (☃xxxxx == ItemDye.DYE_COLORS[☃xxxxxxxx]) {
                  ☃xxxxxxx = true;
                  ☃xxxx = ☃xxxx + I18n.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(☃xxxxxxxx).getTranslationKey());
                  break;
               }
            }

            if (!☃xxxxxxx) {
               ☃xxxx = ☃xxxx + I18n.translateToLocal("item.fireworksCharge.customColor");
            }
         }

         ☃.add(☃xxxx);
      }

      boolean ☃xxx = ☃.getBoolean("Trail");
      if (☃xxx) {
         ☃.add(I18n.translateToLocal("item.fireworksCharge.trail"));
      }

      boolean ☃xxxx = ☃.getBoolean("Flicker");
      if (☃xxxx) {
         ☃.add(I18n.translateToLocal("item.fireworksCharge.flicker"));
      }
   }
}
