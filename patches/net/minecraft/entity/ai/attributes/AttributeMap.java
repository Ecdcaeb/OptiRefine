package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.minecraft.util.LowerStringMap;

public class AttributeMap extends AbstractAttributeMap {
   private final Set<IAttributeInstance> dirtyInstances = Sets.newHashSet();
   protected final Map<String, IAttributeInstance> instancesByName = new LowerStringMap<>();

   public ModifiableAttributeInstance getAttributeInstance(IAttribute var1) {
      return (ModifiableAttributeInstance)super.getAttributeInstance(☃);
   }

   public ModifiableAttributeInstance getAttributeInstanceByName(String var1) {
      IAttributeInstance ☃ = super.getAttributeInstanceByName(☃);
      if (☃ == null) {
         ☃ = this.instancesByName.get(☃);
      }

      return (ModifiableAttributeInstance)☃;
   }

   @Override
   public IAttributeInstance registerAttribute(IAttribute var1) {
      IAttributeInstance ☃ = super.registerAttribute(☃);
      if (☃ instanceof RangedAttribute && ((RangedAttribute)☃).getDescription() != null) {
         this.instancesByName.put(((RangedAttribute)☃).getDescription(), ☃);
      }

      return ☃;
   }

   @Override
   protected IAttributeInstance createInstance(IAttribute var1) {
      return new ModifiableAttributeInstance(this, ☃);
   }

   @Override
   public void onAttributeModified(IAttributeInstance var1) {
      if (☃.getAttribute().getShouldWatch()) {
         this.dirtyInstances.add(☃);
      }

      for (IAttribute ☃ : this.descendantsByParent.get(☃.getAttribute())) {
         ModifiableAttributeInstance ☃x = this.getAttributeInstance(☃);
         if (☃x != null) {
            ☃x.flagForUpdate();
         }
      }
   }

   public Set<IAttributeInstance> getDirtyInstances() {
      return this.dirtyInstances;
   }

   public Collection<IAttributeInstance> getWatchedAttributes() {
      Set<IAttributeInstance> ☃ = Sets.newHashSet();

      for (IAttributeInstance ☃x : this.getAllAttributes()) {
         if (☃x.getAttribute().getShouldWatch()) {
            ☃.add(☃x);
         }
      }

      return ☃;
   }
}
