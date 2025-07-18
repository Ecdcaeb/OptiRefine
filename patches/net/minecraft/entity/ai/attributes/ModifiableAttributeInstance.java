package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public class ModifiableAttributeInstance implements IAttributeInstance {
   private final AbstractAttributeMap attributeMap;
   private final IAttribute genericAttribute;
   private final Map<Integer, Set<AttributeModifier>> mapByOperation = Maps.newHashMap();
   private final Map<String, Set<AttributeModifier>> mapByName = Maps.newHashMap();
   private final Map<UUID, AttributeModifier> mapByUUID = Maps.newHashMap();
   private double baseValue;
   private boolean needsUpdate = true;
   private double cachedValue;

   public ModifiableAttributeInstance(AbstractAttributeMap var1, IAttribute var2) {
      this.attributeMap = ☃;
      this.genericAttribute = ☃;
      this.baseValue = ☃.getDefaultValue();

      for (int ☃ = 0; ☃ < 3; ☃++) {
         this.mapByOperation.put(☃, Sets.newHashSet());
      }
   }

   @Override
   public IAttribute getAttribute() {
      return this.genericAttribute;
   }

   @Override
   public double getBaseValue() {
      return this.baseValue;
   }

   @Override
   public void setBaseValue(double var1) {
      if (☃ != this.getBaseValue()) {
         this.baseValue = ☃;
         this.flagForUpdate();
      }
   }

   @Override
   public Collection<AttributeModifier> getModifiersByOperation(int var1) {
      return this.mapByOperation.get(☃);
   }

   @Override
   public Collection<AttributeModifier> getModifiers() {
      Set<AttributeModifier> ☃ = Sets.newHashSet();

      for (int ☃x = 0; ☃x < 3; ☃x++) {
         ☃.addAll(this.getModifiersByOperation(☃x));
      }

      return ☃;
   }

   @Nullable
   @Override
   public AttributeModifier getModifier(UUID var1) {
      return this.mapByUUID.get(☃);
   }

   @Override
   public boolean hasModifier(AttributeModifier var1) {
      return this.mapByUUID.get(☃.getID()) != null;
   }

   @Override
   public void applyModifier(AttributeModifier var1) {
      if (this.getModifier(☃.getID()) != null) {
         throw new IllegalArgumentException("Modifier is already applied on this attribute!");
      } else {
         Set<AttributeModifier> ☃ = this.mapByName.get(☃.getName());
         if (☃ == null) {
            ☃ = Sets.newHashSet();
            this.mapByName.put(☃.getName(), ☃);
         }

         this.mapByOperation.get(☃.getOperation()).add(☃);
         ☃.add(☃);
         this.mapByUUID.put(☃.getID(), ☃);
         this.flagForUpdate();
      }
   }

   protected void flagForUpdate() {
      this.needsUpdate = true;
      this.attributeMap.onAttributeModified(this);
   }

   @Override
   public void removeModifier(AttributeModifier var1) {
      for (int ☃ = 0; ☃ < 3; ☃++) {
         Set<AttributeModifier> ☃x = this.mapByOperation.get(☃);
         ☃x.remove(☃);
      }

      Set<AttributeModifier> ☃ = this.mapByName.get(☃.getName());
      if (☃ != null) {
         ☃.remove(☃);
         if (☃.isEmpty()) {
            this.mapByName.remove(☃.getName());
         }
      }

      this.mapByUUID.remove(☃.getID());
      this.flagForUpdate();
   }

   @Override
   public void removeModifier(UUID var1) {
      AttributeModifier ☃ = this.getModifier(☃);
      if (☃ != null) {
         this.removeModifier(☃);
      }
   }

   @Override
   public void removeAllModifiers() {
      Collection<AttributeModifier> ☃ = this.getModifiers();
      if (☃ != null) {
         for (AttributeModifier ☃x : Lists.newArrayList(☃)) {
            this.removeModifier(☃x);
         }
      }
   }

   @Override
   public double getAttributeValue() {
      if (this.needsUpdate) {
         this.cachedValue = this.computeValue();
         this.needsUpdate = false;
      }

      return this.cachedValue;
   }

   private double computeValue() {
      double ☃ = this.getBaseValue();

      for (AttributeModifier ☃x : this.getAppliedModifiers(0)) {
         ☃ += ☃x.getAmount();
      }

      double ☃x = ☃;

      for (AttributeModifier ☃xx : this.getAppliedModifiers(1)) {
         ☃x += ☃ * ☃xx.getAmount();
      }

      for (AttributeModifier ☃xx : this.getAppliedModifiers(2)) {
         ☃x *= 1.0 + ☃xx.getAmount();
      }

      return this.genericAttribute.clampValue(☃x);
   }

   private Collection<AttributeModifier> getAppliedModifiers(int var1) {
      Set<AttributeModifier> ☃ = Sets.newHashSet(this.getModifiersByOperation(☃));

      for (IAttribute ☃x = this.genericAttribute.getParent(); ☃x != null; ☃x = ☃x.getParent()) {
         IAttributeInstance ☃xx = this.attributeMap.getAttributeInstance(☃x);
         if (☃xx != null) {
            ☃.addAll(☃xx.getModifiersByOperation(☃));
         }
      }

      return ☃;
   }
}
