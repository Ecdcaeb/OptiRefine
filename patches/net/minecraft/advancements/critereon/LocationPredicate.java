package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;

public class LocationPredicate {
   public static LocationPredicate ANY = new LocationPredicate(MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, null, null, null);
   private final MinMaxBounds x;
   private final MinMaxBounds y;
   private final MinMaxBounds z;
   @Nullable
   final Biome biome;
   @Nullable
   private final String feature;
   @Nullable
   private final DimensionType dimension;

   public LocationPredicate(MinMaxBounds var1, MinMaxBounds var2, MinMaxBounds var3, @Nullable Biome var4, @Nullable String var5, @Nullable DimensionType var6) {
      this.x = ☃;
      this.y = ☃;
      this.z = ☃;
      this.biome = ☃;
      this.feature = ☃;
      this.dimension = ☃;
   }

   public boolean test(WorldServer var1, double var2, double var4, double var6) {
      return this.test(☃, (float)☃, (float)☃, (float)☃);
   }

   public boolean test(WorldServer var1, float var2, float var3, float var4) {
      if (!this.x.test(☃)) {
         return false;
      } else if (!this.y.test(☃)) {
         return false;
      } else if (!this.z.test(☃)) {
         return false;
      } else if (this.dimension != null && this.dimension != ☃.provider.getDimensionType()) {
         return false;
      } else {
         BlockPos ☃ = new BlockPos((double)☃, (double)☃, (double)☃);
         return this.biome != null && this.biome != ☃.getBiome(☃) ? false : this.feature == null || ☃.getChunkProvider().isInsideStructure(☃, this.feature, ☃);
      }
   }

   public static LocationPredicate deserialize(@Nullable JsonElement var0) {
      if (☃ != null && !☃.isJsonNull()) {
         JsonObject ☃ = JsonUtils.getJsonObject(☃, "location");
         JsonObject ☃x = JsonUtils.getJsonObject(☃, "position", new JsonObject());
         MinMaxBounds ☃xx = MinMaxBounds.deserialize(☃x.get("x"));
         MinMaxBounds ☃xxx = MinMaxBounds.deserialize(☃x.get("y"));
         MinMaxBounds ☃xxxx = MinMaxBounds.deserialize(☃x.get("z"));
         DimensionType ☃xxxxx = ☃.has("dimension") ? DimensionType.byName(JsonUtils.getString(☃, "dimension")) : null;
         String ☃xxxxxx = ☃.has("feature") ? JsonUtils.getString(☃, "feature") : null;
         Biome ☃xxxxxxx = null;
         if (☃.has("biome")) {
            ResourceLocation ☃xxxxxxxx = new ResourceLocation(JsonUtils.getString(☃, "biome"));
            ☃xxxxxxx = Biome.REGISTRY.getObject(☃xxxxxxxx);
            if (☃xxxxxxx == null) {
               throw new JsonSyntaxException("Unknown biome '" + ☃xxxxxxxx + "'");
            }
         }

         return new LocationPredicate(☃xx, ☃xxx, ☃xxxx, ☃xxxxxxx, ☃xxxxxx, ☃xxxxx);
      } else {
         return ANY;
      }
   }
}
