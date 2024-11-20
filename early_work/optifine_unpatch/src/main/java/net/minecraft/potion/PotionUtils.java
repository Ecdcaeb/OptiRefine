/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  com.google.common.collect.Lists
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  javax.annotation.Nullable
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 *  net.minecraft.entity.ai.attributes.IAttribute
 *  net.minecraft.init.PotionTypes
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.potion.PotionType
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Tuple
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.util.text.translation.I18n
 */
package net.minecraft.potion;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class PotionUtils {
    public static List<PotionEffect> getEffectsFromStack(ItemStack itemStack) {
        return PotionUtils.getEffectsFromTag(itemStack.getTagCompound());
    }

    public static List<PotionEffect> mergeEffects(PotionType potionType, Collection<PotionEffect> collection) {
        ArrayList arrayList = Lists.newArrayList();
        arrayList.addAll((Collection)potionType.getEffects());
        arrayList.addAll(collection);
        return arrayList;
    }

    public static List<PotionEffect> getEffectsFromTag(@Nullable NBTTagCompound nBTTagCompound) {
        ArrayList arrayList = Lists.newArrayList();
        arrayList.addAll((Collection)PotionUtils.getPotionTypeFromNBT(nBTTagCompound).getEffects());
        PotionUtils.addCustomPotionEffectToList(nBTTagCompound, (List<PotionEffect>)arrayList);
        return arrayList;
    }

    public static List<PotionEffect> getFullEffectsFromItem(ItemStack itemStack) {
        return PotionUtils.getFullEffectsFromTag(itemStack.getTagCompound());
    }

    public static List<PotionEffect> getFullEffectsFromTag(@Nullable NBTTagCompound nBTTagCompound) {
        ArrayList arrayList = Lists.newArrayList();
        PotionUtils.addCustomPotionEffectToList(nBTTagCompound, (List<PotionEffect>)arrayList);
        return arrayList;
    }

    public static void addCustomPotionEffectToList(@Nullable NBTTagCompound nBTTagCompound, List<PotionEffect> list) {
        if (nBTTagCompound != null && nBTTagCompound.hasKey("CustomPotionEffects", 9)) {
            NBTTagList nBTTagList = nBTTagCompound.getTagList("CustomPotionEffects", 10);
            for (int i = 0; i < nBTTagList.tagCount(); ++i) {
                NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(i);
                PotionEffect \u26032 = PotionEffect.readCustomPotionEffectFromNBT((NBTTagCompound)nBTTagCompound2);
                if (\u26032 == null) continue;
                list.add((Object)\u26032);
            }
        }
    }

    public static int getColor(ItemStack itemStack) {
        NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
        if (nBTTagCompound != null && nBTTagCompound.hasKey("CustomPotionColor", 99)) {
            return nBTTagCompound.getInteger("CustomPotionColor");
        }
        return PotionUtils.getPotionFromItem(itemStack) == PotionTypes.EMPTY ? 0xF800F8 : PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromStack(itemStack));
    }

    public static int getPotionColor(PotionType potionType) {
        return potionType == PotionTypes.EMPTY ? 0xF800F8 : PotionUtils.getPotionColorFromEffectList((Collection<PotionEffect>)potionType.getEffects());
    }

    public static int getPotionColorFromEffectList(Collection<PotionEffect> collection) {
        int n;
        int n2 = 3694022;
        if (collection.isEmpty()) {
            return 3694022;
        }
        float \u26032 = 0.0f;
        float \u26033 = 0.0f;
        float \u26034 = 0.0f;
        n = 0;
        for (PotionEffect potionEffect : collection) {
            if (!potionEffect.doesShowParticles()) continue;
            int n3 = potionEffect.getPotion().getLiquidColor();
            \u2603 = potionEffect.getAmplifier() + 1;
            \u26032 += (float)(\u2603 * (n3 >> 16 & 0xFF)) / 255.0f;
            \u26033 += (float)(\u2603 * (n3 >> 8 & 0xFF)) / 255.0f;
            \u26034 += (float)(\u2603 * (n3 >> 0 & 0xFF)) / 255.0f;
            n += \u2603;
        }
        if (n == 0) {
            return 0;
        }
        \u26032 = \u26032 / (float)n * 255.0f;
        \u26033 = \u26033 / (float)n * 255.0f;
        \u26034 = \u26034 / (float)n * 255.0f;
        return (int)\u26032 << 16 | (int)\u26033 << 8 | (int)\u26034;
    }

    public static PotionType getPotionFromItem(ItemStack itemStack) {
        return PotionUtils.getPotionTypeFromNBT(itemStack.getTagCompound());
    }

    public static PotionType getPotionTypeFromNBT(@Nullable NBTTagCompound nBTTagCompound) {
        if (nBTTagCompound == null) {
            return PotionTypes.EMPTY;
        }
        return PotionType.getPotionTypeForName((String)nBTTagCompound.getString("Potion"));
    }

    public static ItemStack addPotionToItemStack(ItemStack itemStack2, PotionType potionType) {
        ResourceLocation resourceLocation = (ResourceLocation)PotionType.REGISTRY.getNameForObject((Object)potionType);
        if (potionType == PotionTypes.EMPTY) {
            if (itemStack2.hasTagCompound()) {
                NBTTagCompound nBTTagCompound = itemStack2.getTagCompound();
                nBTTagCompound.removeTag("Potion");
                if (nBTTagCompound.isEmpty()) {
                    itemStack2.setTagCompound(null);
                }
            }
        } else {
            ItemStack itemStack2;
            NBTTagCompound \u26032 = itemStack2.hasTagCompound() ? itemStack2.getTagCompound() : new NBTTagCompound();
            \u26032.setString("Potion", resourceLocation.toString());
            itemStack2.setTagCompound(\u26032);
        }
        return itemStack2;
    }

    public static ItemStack appendEffects(ItemStack itemStack, Collection<PotionEffect> collection) {
        if (collection.isEmpty()) {
            return itemStack;
        }
        NBTTagCompound nBTTagCompound = (NBTTagCompound)MoreObjects.firstNonNull((Object)itemStack.getTagCompound(), (Object)new NBTTagCompound());
        NBTTagList \u26032 = nBTTagCompound.getTagList("CustomPotionEffects", 9);
        for (PotionEffect potionEffect : collection) {
            \u26032.appendTag((NBTBase)potionEffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
        }
        nBTTagCompound.setTag("CustomPotionEffects", (NBTBase)\u26032);
        itemStack.setTagCompound(nBTTagCompound);
        return itemStack;
    }

    public static void addPotionTooltip(ItemStack itemStack, List<String> list2, float f) {
        List<String> list2;
        String string;
        List<PotionEffect> list3 = PotionUtils.getEffectsFromStack(itemStack);
        ArrayList \u26032 = Lists.newArrayList();
        if (list3.isEmpty()) {
            String string2 = I18n.translateToLocal((String)"effect.none").trim();
            list2.add((Object)(TextFormatting.GRAY + string2));
        } else {
            for (PotionEffect \u26033 : list3) {
                string = I18n.translateToLocal((String)\u26033.getEffectName()).trim();
                Potion \u26034 = \u26033.getPotion();
                Map \u26035 = \u26034.getAttributeModifierMap();
                if (!\u26035.isEmpty()) {
                    for (Map.Entry entry : \u26035.entrySet()) {
                        AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
                        \u2603 = new AttributeModifier(attributeModifier.getName(), \u26034.getAttributeModifierAmount(\u26033.getAmplifier(), attributeModifier), attributeModifier.getOperation());
                        \u26032.add((Object)new Tuple((Object)((IAttribute)entry.getKey()).getName(), (Object)\u2603));
                    }
                }
                if (\u26033.getAmplifier() > 0) {
                    string = string + " " + I18n.translateToLocal((String)("potion.potency." + \u26033.getAmplifier())).trim();
                }
                if (\u26033.getDuration() > 20) {
                    string = string + " (" + Potion.getPotionDurationString((PotionEffect)\u26033, (float)f) + ")";
                }
                if (\u26034.isBadEffect()) {
                    list2.add((Object)(TextFormatting.RED + string));
                    continue;
                }
                list2.add((Object)(TextFormatting.BLUE + string));
            }
        }
        if (!\u26032.isEmpty()) {
            list2.add((Object)"");
            list2.add((Object)(TextFormatting.DARK_PURPLE + I18n.translateToLocal((String)"potion.whenDrank")));
            for (PotionEffect \u26033 : \u26032) {
                string = (AttributeModifier)\u26033.getSecond();
                double \u26036 = string.getAmount();
                double \u26037 = string.getOperation() == 1 || string.getOperation() == 2 ? string.getAmount() * 100.0 : string.getAmount();
                if (\u26036 > 0.0) {
                    list2.add((Object)(TextFormatting.BLUE + I18n.translateToLocalFormatted((String)("attribute.modifier.plus." + string.getOperation()), (Object[])new Object[]{ItemStack.DECIMALFORMAT.format(\u26037), I18n.translateToLocal((String)("attribute.name." + (String)\u26033.getFirst()))})));
                    continue;
                }
                if (!(\u26036 < 0.0)) continue;
                list2.add((Object)(TextFormatting.RED + I18n.translateToLocalFormatted((String)("attribute.modifier.take." + string.getOperation()), (Object[])new Object[]{ItemStack.DECIMALFORMAT.format(\u26037 *= -1.0), I18n.translateToLocal((String)("attribute.name." + (String)\u26033.getFirst()))})));
            }
        }
    }
}
