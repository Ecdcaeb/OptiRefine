package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public class KilledByPlayer implements LootCondition {
   private final boolean inverse;

   public KilledByPlayer(boolean var1) {
      this.inverse = ☃;
   }

   @Override
   public boolean testCondition(Random var1, LootContext var2) {
      boolean ☃ = ☃.getKillerPlayer() != null;
      return ☃ == !this.inverse;
   }

   public static class Serializer extends LootCondition.Serializer<KilledByPlayer> {
      protected Serializer() {
         super(new ResourceLocation("killed_by_player"), KilledByPlayer.class);
      }

      public void serialize(JsonObject var1, KilledByPlayer var2, JsonSerializationContext var3) {
         ☃.addProperty("inverse", ☃.inverse);
      }

      public KilledByPlayer deserialize(JsonObject var1, JsonDeserializationContext var2) {
         return new KilledByPlayer(JsonUtils.getBoolean(☃, "inverse", false));
      }
   }
}
