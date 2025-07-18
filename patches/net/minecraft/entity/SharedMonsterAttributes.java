package net.minecraft.entity;

import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SharedMonsterAttributes {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final IAttribute MAX_HEALTH = new RangedAttribute(null, "generic.maxHealth", 20.0, 0.0, 1024.0)
      .setDescription("Max Health")
      .setShouldWatch(true);
   public static final IAttribute FOLLOW_RANGE = new RangedAttribute(null, "generic.followRange", 32.0, 0.0, 2048.0).setDescription("Follow Range");
   public static final IAttribute KNOCKBACK_RESISTANCE = new RangedAttribute(null, "generic.knockbackResistance", 0.0, 0.0, 1.0)
      .setDescription("Knockback Resistance");
   public static final IAttribute MOVEMENT_SPEED = new RangedAttribute(null, "generic.movementSpeed", 0.7F, 0.0, 1024.0)
      .setDescription("Movement Speed")
      .setShouldWatch(true);
   public static final IAttribute FLYING_SPEED = new RangedAttribute(null, "generic.flyingSpeed", 0.4F, 0.0, 1024.0)
      .setDescription("Flying Speed")
      .setShouldWatch(true);
   public static final IAttribute ATTACK_DAMAGE = new RangedAttribute(null, "generic.attackDamage", 2.0, 0.0, 2048.0);
   public static final IAttribute ATTACK_SPEED = new RangedAttribute(null, "generic.attackSpeed", 4.0, 0.0, 1024.0).setShouldWatch(true);
   public static final IAttribute ARMOR = new RangedAttribute(null, "generic.armor", 0.0, 0.0, 30.0).setShouldWatch(true);
   public static final IAttribute ARMOR_TOUGHNESS = new RangedAttribute(null, "generic.armorToughness", 0.0, 0.0, 20.0).setShouldWatch(true);
   public static final IAttribute LUCK = new RangedAttribute(null, "generic.luck", 0.0, -1024.0, 1024.0).setShouldWatch(true);

   public static NBTTagList writeBaseAttributeMapToNBT(AbstractAttributeMap var0) {
      NBTTagList ☃ = new NBTTagList();

      for (IAttributeInstance ☃x : ☃.getAllAttributes()) {
         ☃.appendTag(writeAttributeInstanceToNBT(☃x));
      }

      return ☃;
   }

   private static NBTTagCompound writeAttributeInstanceToNBT(IAttributeInstance var0) {
      NBTTagCompound ☃ = new NBTTagCompound();
      IAttribute ☃x = ☃.getAttribute();
      ☃.setString("Name", ☃x.getName());
      ☃.setDouble("Base", ☃.getBaseValue());
      Collection<AttributeModifier> ☃xx = ☃.getModifiers();
      if (☃xx != null && !☃xx.isEmpty()) {
         NBTTagList ☃xxx = new NBTTagList();

         for (AttributeModifier ☃xxxx : ☃xx) {
            if (☃xxxx.isSaved()) {
               ☃xxx.appendTag(writeAttributeModifierToNBT(☃xxxx));
            }
         }

         ☃.setTag("Modifiers", ☃xxx);
      }

      return ☃;
   }

   public static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier var0) {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setString("Name", ☃.getName());
      ☃.setDouble("Amount", ☃.getAmount());
      ☃.setInteger("Operation", ☃.getOperation());
      ☃.setUniqueId("UUID", ☃.getID());
      return ☃;
   }

   public static void setAttributeModifiers(AbstractAttributeMap var0, NBTTagList var1) {
      for (int ☃ = 0; ☃ < ☃.tagCount(); ☃++) {
         NBTTagCompound ☃x = ☃.getCompoundTagAt(☃);
         IAttributeInstance ☃xx = ☃.getAttributeInstanceByName(☃x.getString("Name"));
         if (☃xx == null) {
            LOGGER.warn("Ignoring unknown attribute '{}'", ☃x.getString("Name"));
         } else {
            applyModifiersToAttributeInstance(☃xx, ☃x);
         }
      }
   }

   private static void applyModifiersToAttributeInstance(IAttributeInstance var0, NBTTagCompound var1) {
      ☃.setBaseValue(☃.getDouble("Base"));
      if (☃.hasKey("Modifiers", 9)) {
         NBTTagList ☃ = ☃.getTagList("Modifiers", 10);

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            AttributeModifier ☃xx = readAttributeModifierFromNBT(☃.getCompoundTagAt(☃x));
            if (☃xx != null) {
               AttributeModifier ☃xxx = ☃.getModifier(☃xx.getID());
               if (☃xxx != null) {
                  ☃.removeModifier(☃xxx);
               }

               ☃.applyModifier(☃xx);
            }
         }
      }
   }

   @Nullable
   public static AttributeModifier readAttributeModifierFromNBT(NBTTagCompound var0) {
      UUID ☃ = ☃.getUniqueId("UUID");

      try {
         return new AttributeModifier(☃, ☃.getString("Name"), ☃.getDouble("Amount"), ☃.getInteger("Operation"));
      } catch (Exception var3) {
         LOGGER.warn("Unable to create attribute: {}", var3.getMessage());
         return null;
      }
   }
}
