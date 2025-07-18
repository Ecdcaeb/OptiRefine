package net.minecraft.entity.ai.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.util.LowerStringMap;

public abstract class AbstractAttributeMap {
   protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();
   protected final Map<String, IAttributeInstance> attributesByName = new LowerStringMap<>();
   protected final Multimap<IAttribute, IAttribute> descendantsByParent = HashMultimap.create();

   public IAttributeInstance getAttributeInstance(IAttribute var1) {
      return this.attributes.get(☃);
   }

   @Nullable
   public IAttributeInstance getAttributeInstanceByName(String var1) {
      return this.attributesByName.get(☃);
   }

   public IAttributeInstance registerAttribute(IAttribute var1) {
      if (this.attributesByName.containsKey(☃.getName())) {
         throw new IllegalArgumentException("Attribute is already registered!");
      } else {
         IAttributeInstance ☃ = this.createInstance(☃);
         this.attributesByName.put(☃.getName(), ☃);
         this.attributes.put(☃, ☃);

         for (IAttribute ☃x = ☃.getParent(); ☃x != null; ☃x = ☃x.getParent()) {
            this.descendantsByParent.put(☃x, ☃);
         }

         return ☃;
      }
   }

   protected abstract IAttributeInstance createInstance(IAttribute var1);

   public Collection<IAttributeInstance> getAllAttributes() {
      return this.attributesByName.values();
   }

   public void onAttributeModified(IAttributeInstance var1) {
   }

   public void removeAttributeModifiers(Multimap<String, AttributeModifier> var1) {
      for (Entry<String, AttributeModifier> ☃ : ☃.entries()) {
         IAttributeInstance ☃x = this.getAttributeInstanceByName(☃.getKey());
         if (☃x != null) {
            ☃x.removeModifier(☃.getValue());
         }
      }
   }

   public void applyAttributeModifiers(Multimap<String, AttributeModifier> var1) {
      for (Entry<String, AttributeModifier> ☃ : ☃.entries()) {
         IAttributeInstance ☃x = this.getAttributeInstanceByName(☃.getKey());
         if (☃x != null) {
            ☃x.removeModifier(☃.getValue());
            ☃x.applyModifier(☃.getValue());
         }
      }
   }
}
