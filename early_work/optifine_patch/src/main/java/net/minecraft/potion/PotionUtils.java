/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
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
 *  net.optifine.CustomColors
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
import net.optifine.CustomColors;

public class PotionUtils {
    public static List<PotionEffect> getEffectsFromStack(ItemStack stack) {
        return PotionUtils.getEffectsFromTag(stack.getTagCompound());
    }

    public static List<PotionEffect> mergeEffects(PotionType potionIn, Collection<PotionEffect> effects) {
        ArrayList list = Lists.newArrayList();
        list.addAll((Collection)potionIn.getEffects());
        list.addAll(effects);
        return list;
    }

    public static List<PotionEffect> getEffectsFromTag(@Nullable NBTTagCompound tag) {
        ArrayList list = Lists.newArrayList();
        list.addAll((Collection)PotionUtils.getPotionTypeFromNBT(tag).getEffects());
        PotionUtils.addCustomPotionEffectToList(tag, (List<PotionEffect>)list);
        return list;
    }

    public static List<PotionEffect> getFullEffectsFromItem(ItemStack itemIn) {
        return PotionUtils.getFullEffectsFromTag(itemIn.getTagCompound());
    }

    public static List<PotionEffect> getFullEffectsFromTag(@Nullable NBTTagCompound tag) {
        ArrayList list = Lists.newArrayList();
        PotionUtils.addCustomPotionEffectToList(tag, (List<PotionEffect>)list);
        return list;
    }

    public static void addCustomPotionEffectToList(@Nullable NBTTagCompound tag, List<PotionEffect> effectList) {
        if (tag != null && tag.hasKey("CustomPotionEffects", 9)) {
            NBTTagList nbttaglist = tag.getTagList("CustomPotionEffects", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT((NBTTagCompound)nbttagcompound);
                if (potioneffect == null) continue;
                effectList.add((Object)potioneffect);
            }
        }
    }

    public static int getColor(ItemStack p_190932_0_) {
        NBTTagCompound nbttagcompound = p_190932_0_.getTagCompound();
        if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99)) {
            return nbttagcompound.getInteger("CustomPotionColor");
        }
        return PotionUtils.getPotionFromItem(p_190932_0_) == PotionTypes.EMPTY ? 0xF800F8 : PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromStack(p_190932_0_));
    }

    public static int getPotionColor(PotionType potionIn) {
        return potionIn == PotionTypes.EMPTY ? 0xF800F8 : PotionUtils.getPotionColorFromEffectList((Collection<PotionEffect>)potionIn.getEffects());
    }

    public static int getPotionColorFromEffectList(Collection<PotionEffect> effects) {
        int i = 3694022;
        if (effects.isEmpty()) {
            if (Config.isCustomColors()) {
                return CustomColors.getPotionColor(null, (int)i);
            }
            return 3694022;
        }
        float f = 0.0f;
        float f1 = 0.0f;
        float f2 = 0.0f;
        int j = 0;
        for (PotionEffect potioneffect : effects) {
            if (!potioneffect.doesShowParticles()) continue;
            int k = potioneffect.getPotion().getLiquidColor();
            if (Config.isCustomColors()) {
                k = CustomColors.getPotionColor((Potion)potioneffect.getPotion(), (int)k);
            }
            int l = potioneffect.getAmplifier() + 1;
            f += (float)(l * (k >> 16 & 0xFF)) / 255.0f;
            f1 += (float)(l * (k >> 8 & 0xFF)) / 255.0f;
            f2 += (float)(l * (k >> 0 & 0xFF)) / 255.0f;
            j += l;
        }
        if (j == 0) {
            return 0;
        }
        f = f / (float)j * 255.0f;
        f1 = f1 / (float)j * 255.0f;
        f2 = f2 / (float)j * 255.0f;
        return (int)f << 16 | (int)f1 << 8 | (int)f2;
    }

    public static PotionType getPotionFromItem(ItemStack itemIn) {
        return PotionUtils.getPotionTypeFromNBT(itemIn.getTagCompound());
    }

    public static PotionType getPotionTypeFromNBT(@Nullable NBTTagCompound tag) {
        return tag == null ? PotionTypes.EMPTY : PotionType.getPotionTypeForName((String)tag.getString("Potion"));
    }

    public static ItemStack addPotionToItemStack(ItemStack itemIn, PotionType potionIn) {
        ResourceLocation resourcelocation = (ResourceLocation)PotionType.REGISTRY.getNameForObject((Object)potionIn);
        if (potionIn == PotionTypes.EMPTY) {
            if (itemIn.hasTagCompound()) {
                NBTTagCompound nbttagcompound = itemIn.getTagCompound();
                nbttagcompound.removeTag("Potion");
                if (nbttagcompound.isEmpty()) {
                    itemIn.setTagCompound((NBTTagCompound)null);
                }
            }
        } else {
            NBTTagCompound nbttagcompound1 = itemIn.hasTagCompound() ? itemIn.getTagCompound() : new NBTTagCompound();
            nbttagcompound1.setString("Potion", resourcelocation.toString());
            itemIn.setTagCompound(nbttagcompound1);
        }
        return itemIn;
    }

    public static ItemStack appendEffects(ItemStack itemIn, Collection<PotionEffect> effects) {
        if (effects.isEmpty()) {
            return itemIn;
        }
        NBTTagCompound nbttagcompound = (NBTTagCompound)MoreObjects.firstNonNull((Object)itemIn.getTagCompound(), (Object)new NBTTagCompound());
        NBTTagList nbttaglist = nbttagcompound.getTagList("CustomPotionEffects", 9);
        for (PotionEffect potioneffect : effects) {
            nbttaglist.appendTag((NBTBase)potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
        }
        nbttagcompound.setTag("CustomPotionEffects", (NBTBase)nbttaglist);
        itemIn.setTagCompound(nbttagcompound);
        return itemIn;
    }

    public static void addPotionTooltip(ItemStack itemIn, List<String> lores, float durationFactor) {
        List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemIn);
        ArrayList list1 = Lists.newArrayList();
        if (list.isEmpty()) {
            String s = I18n.translateToLocal((String)"effect.none").trim();
            lores.add((Object)(TextFormatting.GRAY + s));
        } else {
            for (PotionEffect potioneffect : list) {
                String s1 = I18n.translateToLocal((String)potioneffect.getEffectName()).trim();
                Potion potion = potioneffect.getPotion();
                Map map = potion.getAttributeModifierMap();
                if (!map.isEmpty()) {
                    for (Map.Entry entry : map.entrySet()) {
                        AttributeModifier attributemodifier = (AttributeModifier)entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        list1.add((Object)new Tuple((Object)((IAttribute)entry.getKey()).getName(), (Object)attributemodifier1));
                    }
                }
                if (potioneffect.getAmplifier() > 0) {
                    s1 = s1 + " " + I18n.translateToLocal((String)("potion.potency." + potioneffect.getAmplifier())).trim();
                }
                if (potioneffect.getDuration() > 20) {
                    s1 = s1 + " (" + Potion.getPotionDurationString((PotionEffect)potioneffect, (float)durationFactor) + ")";
                }
                if (potion.isBadEffect()) {
                    lores.add((Object)(TextFormatting.RED + s1));
                    continue;
                }
                lores.add((Object)(TextFormatting.BLUE + s1));
            }
        }
        if (!list1.isEmpty()) {
            lores.add((Object)"");
            lores.add((Object)(TextFormatting.DARK_PURPLE + I18n.translateToLocal((String)"potion.whenDrank")));
            for (Tuple tuple : list1) {
                AttributeModifier attributemodifier2 = (AttributeModifier)tuple.getSecond();
                double d0 = attributemodifier2.getAmount();
                double d1 = attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2 ? attributemodifier2.getAmount() : attributemodifier2.getAmount() * 100.0;
                if (d0 > 0.0) {
                    lores.add((Object)(TextFormatting.BLUE + I18n.translateToLocalFormatted((String)("attribute.modifier.plus." + attributemodifier2.getOperation()), (Object[])new Object[]{ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal((String)("attribute.name." + (String)tuple.getFirst()))})));
                    continue;
                }
                if (!(d0 < 0.0)) continue;
                lores.add((Object)(TextFormatting.RED + I18n.translateToLocalFormatted((String)("attribute.modifier.take." + attributemodifier2.getOperation()), (Object[])new Object[]{ItemStack.DECIMALFORMAT.format(d1 *= -1.0), I18n.translateToLocal((String)("attribute.name." + (String)tuple.getFirst()))})));
            }
        }
    }
}
