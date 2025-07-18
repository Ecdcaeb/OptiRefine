package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;

public class DamagePredicate {
   public static DamagePredicate ANY = new DamagePredicate();
   private final MinMaxBounds dealt;
   private final MinMaxBounds taken;
   private final EntityPredicate sourceEntity;
   private final Boolean blocked;
   private final DamageSourcePredicate type;

   public DamagePredicate() {
      this.dealt = MinMaxBounds.UNBOUNDED;
      this.taken = MinMaxBounds.UNBOUNDED;
      this.sourceEntity = EntityPredicate.ANY;
      this.blocked = null;
      this.type = DamageSourcePredicate.ANY;
   }

   public DamagePredicate(MinMaxBounds var1, MinMaxBounds var2, EntityPredicate var3, @Nullable Boolean var4, DamageSourcePredicate var5) {
      this.dealt = ☃;
      this.taken = ☃;
      this.sourceEntity = ☃;
      this.blocked = ☃;
      this.type = ☃;
   }

   public boolean test(EntityPlayerMP var1, DamageSource var2, float var3, float var4, boolean var5) {
      if (this == ANY) {
         return true;
      } else if (!this.dealt.test(☃)) {
         return false;
      } else if (!this.taken.test(☃)) {
         return false;
      } else if (!this.sourceEntity.test(☃, ☃.getTrueSource())) {
         return false;
      } else {
         return this.blocked != null && this.blocked != ☃ ? false : this.type.test(☃, ☃);
      }
   }

   public static DamagePredicate deserialize(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "damage");
         MinMaxBounds ☃x = MinMaxBounds.deserialize(☃.get("dealt"));
         MinMaxBounds ☃xx = MinMaxBounds.deserialize(☃.get("taken"));
         Boolean ☃xxx = ☃.has("blocked") ? JsonUtils.getBoolean(☃, "blocked") : null;
         EntityPredicate ☃xxxx = EntityPredicate.deserialize(☃.get("source_entity"));
         DamageSourcePredicate ☃xxxxx = DamageSourcePredicate.deserialize(☃.get("type"));
         return new DamagePredicate(☃x, ☃xx, ☃xxxx, ☃xxx, ☃xxxxx);
      } else {
         return ANY;
      }
   }
}
