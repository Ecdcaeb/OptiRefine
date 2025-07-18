package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetMetadata extends LootFunction {
   private static final Logger LOGGER = LogManager.getLogger();
   private final RandomValueRange metaRange;

   public SetMetadata(LootCondition[] var1, RandomValueRange var2) {
      super(☃);
      this.metaRange = ☃;
   }

   @Override
   public ItemStack apply(ItemStack var1, Random var2, LootContext var3) {
      if (☃.isItemStackDamageable()) {
         LOGGER.warn("Couldn't set data of loot item {}", ☃);
      } else {
         ☃.setItemDamage(this.metaRange.generateInt(☃));
      }

      return ☃;
   }

   public static class Serializer extends LootFunction.Serializer<SetMetadata> {
      protected Serializer() {
         super(new ResourceLocation("set_data"), SetMetadata.class);
      }

      public void serialize(JsonObject var1, SetMetadata var2, JsonSerializationContext var3) {
         ☃.add("data", ☃.serialize(☃.metaRange));
      }

      public SetMetadata deserialize(JsonObject var1, JsonDeserializationContext var2, LootCondition[] var3) {
         return new SetMetadata(☃, JsonUtils.deserializeClass(☃, "data", ☃, RandomValueRange.class));
      }
   }
}
