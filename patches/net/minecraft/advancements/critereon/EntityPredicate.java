package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EntityPredicate {
   public static final EntityPredicate ANY = new EntityPredicate(null, DistancePredicate.ANY, LocationPredicate.ANY, MobEffectsPredicate.ANY, NBTPredicate.ANY);
   private final ResourceLocation type;
   private final DistancePredicate distance;
   private final LocationPredicate location;
   private final MobEffectsPredicate effects;
   private final NBTPredicate nbt;

   public EntityPredicate(@Nullable ResourceLocation var1, DistancePredicate var2, LocationPredicate var3, MobEffectsPredicate var4, NBTPredicate var5) {
      this.type = ☃;
      this.distance = ☃;
      this.location = ☃;
      this.effects = ☃;
      this.nbt = ☃;
   }

   public boolean test(EntityPlayerMP var1, @Nullable Entity var2) {
      if (this == ANY) {
         return true;
      } else if (☃ == null) {
         return false;
      } else if (this.type != null && !EntityList.isMatchingName(☃, this.type)) {
         return false;
      } else if (!this.distance.test(☃.posX, ☃.posY, ☃.posZ, ☃.posX, ☃.posY, ☃.posZ)) {
         return false;
      } else if (!this.location.test(☃.getServerWorld(), ☃.posX, ☃.posY, ☃.posZ)) {
         return false;
      } else {
         return !this.effects.test(☃) ? false : this.nbt.test(☃);
      }
   }

   public static EntityPredicate deserialize(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "entity");
         ResourceLocation ☃x = null;
         if (☃.has("type")) {
            ☃x = new ResourceLocation(JsonUtils.getString(☃, "type"));
            if (!EntityList.isRegistered(☃x)) {
               throw new JsonSyntaxException("Unknown entity type '" + ☃x + "', valid types are: " + EntityList.getValidTypeNames());
            }
         }

         DistancePredicate ☃xx = DistancePredicate.deserialize(☃.get("distance"));
         LocationPredicate ☃xxx = LocationPredicate.deserialize(☃.get("location"));
         MobEffectsPredicate ☃xxxx = MobEffectsPredicate.deserialize(☃.get("effects"));
         NBTPredicate ☃xxxxx = NBTPredicate.deserialize(☃.get("nbt"));
         return new EntityPredicate(☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
      } else {
         return ANY;
      }
   }
}
