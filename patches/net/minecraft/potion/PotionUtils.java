package net.minecraft.potion;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class PotionUtils {
   public static List<PotionEffect> getEffectsFromStack(ItemStack var0) {
      return getEffectsFromTag(☃.getTagCompound());
   }

   public static List<PotionEffect> mergeEffects(PotionType var0, Collection<PotionEffect> var1) {
      List<PotionEffect> ☃ = Lists.newArrayList();
      ☃.addAll(☃.getEffects());
      ☃.addAll(☃);
      return ☃;
   }

   public static List<PotionEffect> getEffectsFromTag(@Nullable NBTTagCompound var0) {
      List<PotionEffect> ☃ = Lists.newArrayList();
      ☃.addAll(getPotionTypeFromNBT(☃).getEffects());
      addCustomPotionEffectToList(☃, ☃);
      return ☃;
   }

   public static List<PotionEffect> getFullEffectsFromItem(ItemStack var0) {
      return getFullEffectsFromTag(☃.getTagCompound());
   }

   public static List<PotionEffect> getFullEffectsFromTag(@Nullable NBTTagCompound var0) {
      List<PotionEffect> ☃ = Lists.newArrayList();
      addCustomPotionEffectToList(☃, ☃);
      return ☃;
   }

   public static void addCustomPotionEffectToList(@Nullable NBTTagCompound var0, List<PotionEffect> var1) {
      if (☃ != null && ☃.hasKey("CustomPotionEffects", 9)) {
         NBTTagList ☃ = ☃.getTagList("CustomPotionEffects", 10);

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
            PotionEffect ☃xxx = PotionEffect.readCustomPotionEffectFromNBT(☃xx);
            if (☃xxx != null) {
               ☃.add(☃xxx);
            }
         }
      }
   }

   public static int getColor(ItemStack var0) {
      NBTTagCompound ☃ = ☃.getTagCompound();
      if (☃ != null && ☃.hasKey("CustomPotionColor", 99)) {
         return ☃.getInteger("CustomPotionColor");
      } else {
         return getPotionFromItem(☃) == PotionTypes.EMPTY ? 16253176 : getPotionColorFromEffectList(getEffectsFromStack(☃));
      }
   }

   public static int getPotionColor(PotionType var0) {
      return ☃ == PotionTypes.EMPTY ? 16253176 : getPotionColorFromEffectList(☃.getEffects());
   }

   public static int getPotionColorFromEffectList(Collection<PotionEffect> var0) {
      int ☃ = 3694022;
      if (☃.isEmpty()) {
         return 3694022;
      } else {
         float ☃x = 0.0F;
         float ☃xx = 0.0F;
         float ☃xxx = 0.0F;
         int ☃xxxx = 0;

         for (PotionEffect ☃xxxxx : ☃) {
            if (☃xxxxx.doesShowParticles()) {
               int ☃xxxxxx = ☃xxxxx.getPotion().getLiquidColor();
               int ☃xxxxxxx = ☃xxxxx.getAmplifier() + 1;
               ☃x += ☃xxxxxxx * (☃xxxxxx >> 16 & 0xFF) / 255.0F;
               ☃xx += ☃xxxxxxx * (☃xxxxxx >> 8 & 0xFF) / 255.0F;
               ☃xxx += ☃xxxxxxx * (☃xxxxxx >> 0 & 0xFF) / 255.0F;
               ☃xxxx += ☃xxxxxxx;
            }
         }

         if (☃xxxx == 0) {
            return 0;
         } else {
            ☃x = ☃x / ☃xxxx * 255.0F;
            ☃xx = ☃xx / ☃xxxx * 255.0F;
            ☃xxx = ☃xxx / ☃xxxx * 255.0F;
            return (int)☃x << 16 | (int)☃xx << 8 | (int)☃xxx;
         }
      }
   }

   public static PotionType getPotionFromItem(ItemStack var0) {
      return getPotionTypeFromNBT(☃.getTagCompound());
   }

   public static PotionType getPotionTypeFromNBT(@Nullable NBTTagCompound var0) {
      return ☃ == null ? PotionTypes.EMPTY : PotionType.getPotionTypeForName(☃.getString("Potion"));
   }

   public static ItemStack addPotionToItemStack(ItemStack var0, PotionType var1) {
      ResourceLocation ☃ = PotionType.REGISTRY.getNameForObject(☃);
      if (☃ == PotionTypes.EMPTY) {
         if (☃.hasTagCompound()) {
            NBTTagCompound ☃x = ☃.getTagCompound();
            ☃x.removeTag("Potion");
            if (☃x.isEmpty()) {
               ☃.setTagCompound(null);
            }
         }
      } else {
         NBTTagCompound ☃x = ☃.hasTagCompound() ? ☃.getTagCompound() : new NBTTagCompound();
         ☃x.setString("Potion", ☃.toString());
         ☃.setTagCompound(☃x);
      }

      return ☃;
   }

   public static ItemStack appendEffects(ItemStack var0, Collection<PotionEffect> var1) {
      if (☃.isEmpty()) {
         return ☃;
      } else {
         NBTTagCompound ☃ = (NBTTagCompound)MoreObjects.firstNonNull(☃.getTagCompound(), new NBTTagCompound());
         NBTTagList ☃x = ☃.getTagList("CustomPotionEffects", 9);

         for (PotionEffect ☃xx : ☃) {
            ☃x.appendTag(☃xx.writeCustomPotionEffectToNBT(new NBTTagCompound()));
         }

         ☃.setTag("CustomPotionEffects", ☃x);
         ☃.setTagCompound(☃);
         return ☃;
      }
   }

   public static void addPotionTooltip(ItemStack var0, List<String> var1, float var2) {
      List<PotionEffect> ☃ = getEffectsFromStack(☃);
      List<Tuple<String, AttributeModifier>> ☃x = Lists.newArrayList();
      if (☃.isEmpty()) {
         String ☃xx = I18n.translateToLocal("effect.none").trim();
         ☃.add(TextFormatting.GRAY + ☃xx);
      } else {
         for (PotionEffect ☃xx : ☃) {
            String ☃xxx = I18n.translateToLocal(☃xx.getEffectName()).trim();
            Potion ☃xxxx = ☃xx.getPotion();
            Map<IAttribute, AttributeModifier> ☃xxxxx = ☃xxxx.getAttributeModifierMap();
            if (!☃xxxxx.isEmpty()) {
               for (Entry<IAttribute, AttributeModifier> ☃xxxxxx : ☃xxxxx.entrySet()) {
                  AttributeModifier ☃xxxxxxx = ☃xxxxxx.getValue();
                  AttributeModifier ☃xxxxxxxx = new AttributeModifier(
                     ☃xxxxxxx.getName(), ☃xxxx.getAttributeModifierAmount(☃xx.getAmplifier(), ☃xxxxxxx), ☃xxxxxxx.getOperation()
                  );
                  ☃x.add(new Tuple<>(☃xxxxxx.getKey().getName(), ☃xxxxxxxx));
               }
            }

            if (☃xx.getAmplifier() > 0) {
               ☃xxx = ☃xxx + " " + I18n.translateToLocal("potion.potency." + ☃xx.getAmplifier()).trim();
            }

            if (☃xx.getDuration() > 20) {
               ☃xxx = ☃xxx + " (" + Potion.getPotionDurationString(☃xx, ☃) + ")";
            }

            if (☃xxxx.isBadEffect()) {
               ☃.add(TextFormatting.RED + ☃xxx);
            } else {
               ☃.add(TextFormatting.BLUE + ☃xxx);
            }
         }
      }

      if (!☃x.isEmpty()) {
         ☃.add("");
         ☃.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("potion.whenDrank"));

         for (Tuple<String, AttributeModifier> ☃xx : ☃x) {
            AttributeModifier ☃xxxxxx = ☃xx.getSecond();
            double ☃xxxxxxx = ☃xxxxxx.getAmount();
            double ☃xxxxxxxx;
            if (☃xxxxxx.getOperation() != 1 && ☃xxxxxx.getOperation() != 2) {
               ☃xxxxxxxx = ☃xxxxxx.getAmount();
            } else {
               ☃xxxxxxxx = ☃xxxxxx.getAmount() * 100.0;
            }

            if (☃xxxxxxx > 0.0) {
               ☃.add(
                  TextFormatting.BLUE
                     + I18n.translateToLocalFormatted(
                        "attribute.modifier.plus." + ☃xxxxxx.getOperation(),
                        ItemStack.DECIMALFORMAT.format(☃xxxxxxxx),
                        I18n.translateToLocal("attribute.name." + ☃xx.getFirst())
                     )
               );
            } else if (☃xxxxxxx < 0.0) {
               ☃xxxxxxxx *= -1.0;
               ☃.add(
                  TextFormatting.RED
                     + I18n.translateToLocalFormatted(
                        "attribute.modifier.take." + ☃xxxxxx.getOperation(),
                        ItemStack.DECIMALFORMAT.format(☃xxxxxxxx),
                        I18n.translateToLocal("attribute.name." + ☃xx.getFirst())
                     )
               );
            }
         }
      }
   }
}
