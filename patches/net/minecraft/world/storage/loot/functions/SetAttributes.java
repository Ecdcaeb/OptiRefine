package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetAttributes extends LootFunction {
   private static final Logger LOGGER = LogManager.getLogger();
   private final SetAttributes.Modifier[] modifiers;

   public SetAttributes(LootCondition[] var1, SetAttributes.Modifier[] var2) {
      super(☃);
      this.modifiers = ☃;
   }

   @Override
   public ItemStack apply(ItemStack var1, Random var2, LootContext var3) {
      for (SetAttributes.Modifier ☃ : this.modifiers) {
         UUID ☃x = ☃.uuid;
         if (☃x == null) {
            ☃x = UUID.randomUUID();
         }

         EntityEquipmentSlot ☃xx = ☃.slots[☃.nextInt(☃.slots.length)];
         ☃.addAttributeModifier(☃.attributeName, new AttributeModifier(☃x, ☃.modifierName, ☃.amount.generateFloat(☃), ☃.operation), ☃xx);
      }

      return ☃;
   }

   static class Modifier {
      private final String modifierName;
      private final String attributeName;
      private final int operation;
      private final RandomValueRange amount;
      @Nullable
      private final UUID uuid;
      private final EntityEquipmentSlot[] slots;

      private Modifier(String var1, String var2, int var3, RandomValueRange var4, EntityEquipmentSlot[] var5, @Nullable UUID var6) {
         this.modifierName = ☃;
         this.attributeName = ☃;
         this.operation = ☃;
         this.amount = ☃;
         this.uuid = ☃;
         this.slots = ☃;
      }

      public JsonObject serialize(JsonSerializationContext var1) {
         JsonObject ☃ = new JsonObject();
         ☃.addProperty("name", this.modifierName);
         ☃.addProperty("attribute", this.attributeName);
         ☃.addProperty("operation", getOperationFromStr(this.operation));
         ☃.add("amount", ☃.serialize(this.amount));
         if (this.uuid != null) {
            ☃.addProperty("id", this.uuid.toString());
         }

         if (this.slots.length == 1) {
            ☃.addProperty("slot", this.slots[0].getName());
         } else {
            JsonArray ☃x = new JsonArray();

            for (EntityEquipmentSlot ☃xx : this.slots) {
               ☃x.add(new JsonPrimitive(☃xx.getName()));
            }

            ☃.add("slot", ☃x);
         }

         return ☃;
      }

      public static SetAttributes.Modifier deserialize(JsonObject var0, JsonDeserializationContext var1) {
         String ☃ = JsonUtils.getString(☃, "name");
         String ☃x = JsonUtils.getString(☃, "attribute");
         int ☃xx = getOperationFromInt(JsonUtils.getString(☃, "operation"));
         RandomValueRange ☃xxx = JsonUtils.deserializeClass(☃, "amount", ☃, RandomValueRange.class);
         UUID ☃xxxx = null;
         EntityEquipmentSlot[] ☃xxxxx;
         if (JsonUtils.isString(☃, "slot")) {
            ☃xxxxx = new EntityEquipmentSlot[]{EntityEquipmentSlot.fromString(JsonUtils.getString(☃, "slot"))};
         } else {
            if (!JsonUtils.isJsonArray(☃, "slot")) {
               throw new JsonSyntaxException("Invalid or missing attribute modifier slot; must be either string or array of strings.");
            }

            JsonArray ☃xxxxxx = JsonUtils.getJsonArray(☃, "slot");
            ☃xxxxx = new EntityEquipmentSlot[☃xxxxxx.size()];
            int ☃xxxxxxx = 0;

            for (JsonElement ☃xxxxxxxx : ☃xxxxxx) {
               ☃xxxxx[☃xxxxxxx++] = EntityEquipmentSlot.fromString(JsonUtils.getString(☃xxxxxxxx, "slot"));
            }

            if (☃xxxxx.length == 0) {
               throw new JsonSyntaxException("Invalid attribute modifier slot; must contain at least one entry.");
            }
         }

         if (☃.has("id")) {
            String ☃xxxxxx = JsonUtils.getString(☃, "id");

            try {
               ☃xxxx = UUID.fromString(☃xxxxxx);
            } catch (IllegalArgumentException var12) {
               throw new JsonSyntaxException("Invalid attribute modifier id '" + ☃xxxxxx + "' (must be UUID format, with dashes)");
            }
         }

         return new SetAttributes.Modifier(☃, ☃x, ☃xx, ☃xxx, ☃xxxxx, ☃xxxx);
      }

      private static String getOperationFromStr(int var0) {
         switch (☃) {
            case 0:
               return "addition";
            case 1:
               return "multiply_base";
            case 2:
               return "multiply_total";
            default:
               throw new IllegalArgumentException("Unknown operation " + ☃);
         }
      }

      private static int getOperationFromInt(String var0) {
         if ("addition".equals(☃)) {
            return 0;
         } else if ("multiply_base".equals(☃)) {
            return 1;
         } else if ("multiply_total".equals(☃)) {
            return 2;
         } else {
            throw new JsonSyntaxException("Unknown attribute modifier operation " + ☃);
         }
      }
   }

   public static class Serializer extends LootFunction.Serializer<SetAttributes> {
      public Serializer() {
         super(new ResourceLocation("set_attributes"), SetAttributes.class);
      }

      public void serialize(JsonObject var1, SetAttributes var2, JsonSerializationContext var3) {
         JsonArray ☃ = new JsonArray();

         for (SetAttributes.Modifier ☃x : ☃.modifiers) {
            ☃.add(☃x.serialize(☃));
         }

         ☃.add("modifiers", ☃);
      }

      public SetAttributes deserialize(JsonObject var1, JsonDeserializationContext var2, LootCondition[] var3) {
         JsonArray ☃ = JsonUtils.getJsonArray(☃, "modifiers");
         SetAttributes.Modifier[] ☃x = new SetAttributes.Modifier[☃.size()];
         int ☃xx = 0;

         for (JsonElement ☃xxx : ☃) {
            ☃x[☃xx++] = SetAttributes.Modifier.deserialize(JsonUtils.getJsonObject(☃xxx, "modifier"), ☃);
         }

         if (☃x.length == 0) {
            throw new JsonSyntaxException("Invalid attribute modifiers array; cannot be empty");
         } else {
            return new SetAttributes(☃, ☃x);
         }
      }
   }
}
