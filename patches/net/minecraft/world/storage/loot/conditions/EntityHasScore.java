package net.minecraft.world.storage.loot.conditions;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;

public class EntityHasScore implements LootCondition {
   private final Map<String, RandomValueRange> scores;
   private final LootContext.EntityTarget target;

   public EntityHasScore(Map<String, RandomValueRange> var1, LootContext.EntityTarget var2) {
      this.scores = ☃;
      this.target = ☃;
   }

   @Override
   public boolean testCondition(Random var1, LootContext var2) {
      Entity ☃ = ☃.getEntity(this.target);
      if (☃ == null) {
         return false;
      } else {
         Scoreboard ☃x = ☃.world.getScoreboard();

         for (Entry<String, RandomValueRange> ☃xx : this.scores.entrySet()) {
            if (!this.entityScoreMatch(☃, ☃x, ☃xx.getKey(), ☃xx.getValue())) {
               return false;
            }
         }

         return true;
      }
   }

   protected boolean entityScoreMatch(Entity var1, Scoreboard var2, String var3, RandomValueRange var4) {
      ScoreObjective ☃ = ☃.getObjective(☃);
      if (☃ == null) {
         return false;
      } else {
         String ☃x = ☃ instanceof EntityPlayerMP ? ☃.getName() : ☃.getCachedUniqueIdString();
         return !☃.entityHasObjective(☃x, ☃) ? false : ☃.isInRange(☃.getOrCreateScore(☃x, ☃).getScorePoints());
      }
   }

   public static class Serializer extends LootCondition.Serializer<EntityHasScore> {
      protected Serializer() {
         super(new ResourceLocation("entity_scores"), EntityHasScore.class);
      }

      public void serialize(JsonObject var1, EntityHasScore var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();

         for (Entry<String, RandomValueRange> ☃x : ☃.scores.entrySet()) {
            ☃.add(☃x.getKey(), ☃.serialize(☃x.getValue()));
         }

         ☃.add("scores", ☃);
         ☃.add("entity", ☃.serialize(☃.target));
      }

      public EntityHasScore deserialize(JsonObject var1, JsonDeserializationContext var2) {
         Set<Entry<String, JsonElement>> ☃ = JsonUtils.getJsonObject(☃, "scores").entrySet();
         Map<String, RandomValueRange> ☃x = Maps.newLinkedHashMap();

         for (Entry<String, JsonElement> ☃xx : ☃) {
            ☃x.put(☃xx.getKey(), JsonUtils.deserializeClass(☃xx.getValue(), "score", ☃, RandomValueRange.class));
         }

         return new EntityHasScore(☃x, JsonUtils.deserializeClass(☃, "entity", ☃, LootContext.EntityTarget.class));
      }
   }
}
