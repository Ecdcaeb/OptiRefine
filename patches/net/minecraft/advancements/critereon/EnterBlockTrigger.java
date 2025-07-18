package net.minecraft.advancements.critereon;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EnterBlockTrigger implements ICriterionTrigger<EnterBlockTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("enter_block");
   private final Map<PlayerAdvancements, EnterBlockTrigger.Listeners> listeners = Maps.newHashMap();

   @Override
   public ResourceLocation getId() {
      return ID;
   }

   @Override
   public void addListener(PlayerAdvancements var1, ICriterionTrigger.Listener<EnterBlockTrigger.Instance> var2) {
      EnterBlockTrigger.Listeners ☃ = this.listeners.get(☃);
      if (☃ == null) {
         ☃ = new EnterBlockTrigger.Listeners(☃);
         this.listeners.put(☃, ☃);
      }

      ☃.add(☃);
   }

   @Override
   public void removeListener(PlayerAdvancements var1, ICriterionTrigger.Listener<EnterBlockTrigger.Instance> var2) {
      EnterBlockTrigger.Listeners ☃ = this.listeners.get(☃);
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

   public EnterBlockTrigger.Instance deserializeInstance(JsonObject var1, JsonDeserializationContext var2) {
      Block ☃ = null;
      if (☃.has("block")) {
         ResourceLocation ☃x = new ResourceLocation(JsonUtils.getString(☃, "block"));
         if (!Block.REGISTRY.containsKey(☃x)) {
            throw new JsonSyntaxException("Unknown block type '" + ☃x + "'");
         }

         ☃ = Block.REGISTRY.getObject(☃x);
      }

      Map<IProperty<?>, Object> ☃x = null;
      if (☃.has("state")) {
         if (☃ == null) {
            throw new JsonSyntaxException("Can't define block state without a specific block type");
         }

         BlockStateContainer ☃xx = ☃.getBlockState();

         for (Entry<String, JsonElement> ☃xxx : JsonUtils.getJsonObject(☃, "state").entrySet()) {
            IProperty<?> ☃xxxx = ☃xx.getProperty(☃xxx.getKey());
            if (☃xxxx == null) {
               throw new JsonSyntaxException("Unknown block state property '" + ☃xxx.getKey() + "' for block '" + Block.REGISTRY.getNameForObject(☃) + "'");
            }

            String ☃xxxxx = JsonUtils.getString(☃xxx.getValue(), ☃xxx.getKey());
            Optional<?> ☃xxxxxx = ☃xxxx.parseValue(☃xxxxx);
            if (!☃xxxxxx.isPresent()) {
               throw new JsonSyntaxException(
                  "Invalid block state value '" + ☃xxxxx + "' for property '" + ☃xxx.getKey() + "' on block '" + Block.REGISTRY.getNameForObject(☃) + "'"
               );
            }

            if (☃x == null) {
               ☃x = Maps.newHashMap();
            }

            ☃x.put(☃xxxx, ☃xxxxxx.get());
         }
      }

      return new EnterBlockTrigger.Instance(☃, ☃x);
   }

   public void trigger(EntityPlayerMP var1, IBlockState var2) {
      EnterBlockTrigger.Listeners ☃ = this.listeners.get(☃.getAdvancements());
      if (☃ != null) {
         ☃.trigger(☃);
      }
   }

   public static class Instance extends AbstractCriterionInstance {
      private final Block block;
      private final Map<IProperty<?>, Object> properties;

      public Instance(@Nullable Block var1, @Nullable Map<IProperty<?>, Object> var2) {
         super(EnterBlockTrigger.ID);
         this.block = ☃;
         this.properties = ☃;
      }

      public boolean test(IBlockState var1) {
         if (this.block != null && ☃.getBlock() != this.block) {
            return false;
         } else {
            if (this.properties != null) {
               for (Entry<IProperty<?>, Object> ☃ : this.properties.entrySet()) {
                  if (☃.getValue(☃.getKey()) != ☃.getValue()) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   static class Listeners {
      private final PlayerAdvancements playerAdvancements;
      private final Set<ICriterionTrigger.Listener<EnterBlockTrigger.Instance>> listeners = Sets.newHashSet();

      public Listeners(PlayerAdvancements var1) {
         this.playerAdvancements = ☃;
      }

      public boolean isEmpty() {
         return this.listeners.isEmpty();
      }

      public void add(ICriterionTrigger.Listener<EnterBlockTrigger.Instance> var1) {
         this.listeners.add(☃);
      }

      public void remove(ICriterionTrigger.Listener<EnterBlockTrigger.Instance> var1) {
         this.listeners.remove(☃);
      }

      public void trigger(IBlockState var1) {
         List<ICriterionTrigger.Listener<EnterBlockTrigger.Instance>> ☃ = null;

         for (ICriterionTrigger.Listener<EnterBlockTrigger.Instance> ☃x : this.listeners) {
            if (☃x.getCriterionInstance().test(☃)) {
               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃x);
            }
         }

         if (☃ != null) {
            for (ICriterionTrigger.Listener<EnterBlockTrigger.Instance> ☃xx : ☃) {
               ☃xx.grantCriterion(this.playerAdvancements);
            }
         }
      }
   }
}
