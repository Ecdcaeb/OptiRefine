package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;

public class DamageSourcePredicate {
   public static DamageSourcePredicate ANY = new DamageSourcePredicate();
   private final Boolean isProjectile;
   private final Boolean isExplosion;
   private final Boolean bypassesArmor;
   private final Boolean bypassesInvulnerability;
   private final Boolean bypassesMagic;
   private final Boolean isFire;
   private final Boolean isMagic;
   private final EntityPredicate directEntity;
   private final EntityPredicate sourceEntity;

   public DamageSourcePredicate() {
      this.isProjectile = null;
      this.isExplosion = null;
      this.bypassesArmor = null;
      this.bypassesInvulnerability = null;
      this.bypassesMagic = null;
      this.isFire = null;
      this.isMagic = null;
      this.directEntity = EntityPredicate.ANY;
      this.sourceEntity = EntityPredicate.ANY;
   }

   public DamageSourcePredicate(
      @Nullable Boolean var1,
      @Nullable Boolean var2,
      @Nullable Boolean var3,
      @Nullable Boolean var4,
      @Nullable Boolean var5,
      @Nullable Boolean var6,
      @Nullable Boolean var7,
      EntityPredicate var8,
      EntityPredicate var9
   ) {
      this.isProjectile = ☃;
      this.isExplosion = ☃;
      this.bypassesArmor = ☃;
      this.bypassesInvulnerability = ☃;
      this.bypassesMagic = ☃;
      this.isFire = ☃;
      this.isMagic = ☃;
      this.directEntity = ☃;
      this.sourceEntity = ☃;
   }

   public boolean test(EntityPlayerMP var1, DamageSource var2) {
      if (this == ANY) {
         return true;
      } else if (this.isProjectile != null && this.isProjectile != ☃.isProjectile()) {
         return false;
      } else if (this.isExplosion != null && this.isExplosion != ☃.isExplosion()) {
         return false;
      } else if (this.bypassesArmor != null && this.bypassesArmor != ☃.isUnblockable()) {
         return false;
      } else if (this.bypassesInvulnerability != null && this.bypassesInvulnerability != ☃.canHarmInCreative()) {
         return false;
      } else if (this.bypassesMagic != null && this.bypassesMagic != ☃.isDamageAbsolute()) {
         return false;
      } else if (this.isFire != null && this.isFire != ☃.isFireDamage()) {
         return false;
      } else if (this.isMagic != null && this.isMagic != ☃.isMagicDamage()) {
         return false;
      } else {
         return !this.directEntity.test(☃, ☃.getImmediateSource()) ? false : this.sourceEntity.test(☃, ☃.getTrueSource());
      }
   }

   public static DamageSourcePredicate deserialize(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "damage type");
         Boolean ☃x = optionalBoolean(☃, "is_projectile");
         Boolean ☃xx = optionalBoolean(☃, "is_explosion");
         Boolean ☃xxx = optionalBoolean(☃, "bypasses_armor");
         Boolean ☃xxxx = optionalBoolean(☃, "bypasses_invulnerability");
         Boolean ☃xxxxx = optionalBoolean(☃, "bypasses_magic");
         Boolean ☃xxxxxx = optionalBoolean(☃, "is_fire");
         Boolean ☃xxxxxxx = optionalBoolean(☃, "is_magic");
         EntityPredicate ☃xxxxxxxx = EntityPredicate.deserialize(☃.get("direct_entity"));
         EntityPredicate ☃xxxxxxxxx = EntityPredicate.deserialize(☃.get("source_entity"));
         return new DamageSourcePredicate(☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx);
      } else {
         return ANY;
      }
   }

   @Nullable
   private static Boolean optionalBoolean(JsonObject var0, String var1) {
      return ☃.has(☃) ? JsonUtils.getBoolean(☃, ☃) : null;
   }
}
