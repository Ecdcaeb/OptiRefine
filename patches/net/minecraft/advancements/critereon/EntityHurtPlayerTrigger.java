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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class EntityHurtPlayerTrigger implements ICriterionTrigger<EntityHurtPlayerTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("entity_hurt_player");
   private final Map<PlayerAdvancements, EntityHurtPlayerTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> var2) {
      EntityHurtPlayerTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new EntityHurtPlayerTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> var2) {
      EntityHurtPlayerTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public EntityHurtPlayerTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      DamagePredicate ☃ = DamagePredicate.deserialize(☃.get("damage"));
      return new EntityHurtPlayerTrigger.Instance(☃);
   }

   public void trigger(EntityPlayerMP var1, DamageSource var2, float var3, float var4, boolean var5) {
      EntityHurtPlayerTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃, ☃, ☃, ☃, ☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final DamagePredicate damage;

      public Instance(DamagePredicate var1) {
         super(EntityHurtPlayerTrigger.ID);
         this.damage = ☃;
      }

      public boolean test(EntityPlayerMP var1, DamageSource var2, float var3, float var4, boolean var5) {
         return this.damage.test(☃, ☃, ☃, ☃, ☃);
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(EntityPlayerMP var1, DamageSource var2, float var3, float var4, boolean var5) {
         List<ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃, ☃, ☃, ☃, ☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
