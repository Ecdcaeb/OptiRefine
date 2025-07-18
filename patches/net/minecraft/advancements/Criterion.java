package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class Criterion {
   private final ICriterionInstance criterionInstance;

   public Criterion(ICriterionInstance var1) {
      this.criterionInstance = ☃;
   }

   public Criterion() {
      this.criterionInstance = null;
   }

   public void serializeToNetwork(PacketBuffer var1) {
   }

   public static Criterion criterionFromJson(JsonObject var0, JsonDeserializationContext var1) {
      ResourceLocation ☃ = new ResourceLocation(JsonUtils.getString(☃, "trigger"));
      ICriterionTrigger<?> ☃x = CriteriaTriggers.get(☃);
      if (☃x == null) {
         throw new JsonSyntaxException("Invalid criterion trigger: " + ☃);
      } else {
         ICriterionInstance ☃xx = ☃x.deserializeInstance(JsonUtils.getJsonObject(☃, "conditions", new JsonObject()), ☃);
         return new Criterion(☃xx);
      }
   }

   public static Criterion criterionFromNetwork(PacketBuffer var0) {
      return new Criterion();
   }

   public static Map<String, Criterion> criteriaFromJson(JsonObject var0, JsonDeserializationContext var1) {
      Map<String, Criterion> ☃ = Maps.newHashMap();

      for (Entry<String, JsonElement> ☃x : ☃.entrySet()) {
         ☃.put(☃x.getKey(), criterionFromJson(JsonUtils.getJsonObject(☃x.getValue(), "criterion"), ☃));
      }

      return ☃;
   }

   public static Map<String, Criterion> criteriaFromNetwork(PacketBuffer var0) {
      Map<String, Criterion> ☃ = Maps.newHashMap();
      int ☃x = ☃.readVarInt();

      for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
         ☃.put(☃.readString(32767), criterionFromNetwork(☃));
      }

      return ☃;
   }

   public static void serializeToNetwork(Map<String, Criterion> var0, PacketBuffer var1) {
      ☃.writeVarInt(☃.size());

      for (Entry<String, Criterion> ☃ : ☃.entrySet()) {
         ☃.writeString(☃.getKey());
         ☃.getValue().serializeToNetwork(☃);
      }
   }

   @Nullable
   public ICriterionInstance getCriterionInstance() {
      return this.criterionInstance;
   }
}
