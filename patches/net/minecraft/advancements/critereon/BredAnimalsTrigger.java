package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class BredAnimalsTrigger implements ICriterionTrigger<BredAnimalsTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("bred_animals");
   private final Map<PlayerAdvancements, BredAnimalsTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> var2) {
      BredAnimalsTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new BredAnimalsTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> var2) {
      BredAnimalsTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ != null) {
         ☃.remove(☃);
         if (☃.isEmpty()) {
            this.listeners.remove(☃);
         }
      }
   }

   @Override
   public void removeAllListeners(PlayerAdvancements var1) {
      this.listeners.remove(☃);
   }

   public BredAnimalsTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      EntityPredicate ☃ = EntityPredicate.deserialize(☃.get("parent"));
      EntityPredicate ☃x = EntityPredicate.deserialize(☃.get("partner"));
      EntityPredicate ☃xx = EntityPredicate.deserialize(☃.get("child"));
      return new BredAnimalsTrigger.Instance(☃, ☃x, ☃xx);
   }

   public void trigger(EntityPlayerMP var1, EntityAnimal var2, EntityAnimal var3, EntityAgeable var4) {
      BredAnimalsTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃, ☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final EntityPredicate parent;
      private final EntityPredicate partner;
      private final EntityPredicate child;

      public Instance(EntityPredicate var1, EntityPredicate var2, EntityPredicate var3) {
         super(BredAnimalsTrigger.ID);
         this.parent = ☃;
         this.partner = ☃;
         this.child = ☃;
      }

      public boolean test(EntityPlayerMP var1, EntityAnimal var2, EntityAnimal var3, EntityAgeable var4) {
         return !this.child.test(☃, ☃) ? false : this.parent.test(☃, ☃) && this.partner.test(☃, ☃) || this.parent.test(☃, ☃) && this.partner.test(☃, ☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<BredAnimalsTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(EntityPlayerMP var1, EntityAnimal var2, EntityAnimal var3, EntityAgeable var4) {
         List<ICriterionTrigger.Listener<BredAnimalsTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃, ☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
