package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public class RandomChanceWithLooting implements LootCondition {
   private final float chance;
   private final float lootingMultiplier;

   public RandomChanceWithLooting(float var1, float var2) {
      this.chance = ☃;
      this.lootingMultiplier = ☃;
   }

   @Override
   public boolean testCondition(Random var1, LootContext var2) {
      int ☃ = 0;
      if (☃.getKiller() instanceof EntityLivingBase) {
         ☃ = EnchantmentHelper.getLootingModifier((EntityLivingBase)☃.getKiller());
      }

      return ☃.nextFloat() < this.chance + ☃ * this.lootingMultiplier;
   }

   public static class Serializer extends LootCondition.Serializer<RandomChanceWithLooting> {
      protected Serializer() {
         super(new ResourceLocation("random_chance_with_looting"), RandomChanceWithLooting.class);
      }

      public void serialize(JsonObject var1, RandomChanceWithLooting var2, JsonSerializationContext var3) {
         ☃.addProperty("chance", ☃.chance);
         ☃.addProperty("looting_multiplier", ☃.lootingMultiplier);
      }

      public RandomChanceWithLooting deserialize(JsonObject var1, JsonDeserializationContext var2) {
         return new RandomChanceWithLooting(JsonUtils.getFloat(☃, "chance"), JsonUtils.getFloat(☃, "looting_multiplier"));
      }
   }
}
