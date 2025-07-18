package net.minecraft.world.gen;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.init.Biomes;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.biome.Biome;

public class ChunkGeneratorSettings {
   public final float coordinateScale;
   public final float heightScale;
   public final float upperLimitScale;
   public final float lowerLimitScale;
   public final float depthNoiseScaleX;
   public final float depthNoiseScaleZ;
   public final float depthNoiseScaleExponent;
   public final float mainNoiseScaleX;
   public final float mainNoiseScaleY;
   public final float mainNoiseScaleZ;
   public final float baseSize;
   public final float stretchY;
   public final float biomeDepthWeight;
   public final float biomeDepthOffSet;
   public final float biomeScaleWeight;
   public final float biomeScaleOffset;
   public final int seaLevel;
   public final boolean useCaves;
   public final boolean useDungeons;
   public final int dungeonChance;
   public final boolean useStrongholds;
   public final boolean useVillages;
   public final boolean useMineShafts;
   public final boolean useTemples;
   public final boolean useMonuments;
   public final boolean useMansions;
   public final boolean useRavines;
   public final boolean useWaterLakes;
   public final int waterLakeChance;
   public final boolean useLavaLakes;
   public final int lavaLakeChance;
   public final boolean useLavaOceans;
   public final int fixedBiome;
   public final int biomeSize;
   public final int riverSize;
   public final int dirtSize;
   public final int dirtCount;
   public final int dirtMinHeight;
   public final int dirtMaxHeight;
   public final int gravelSize;
   public final int gravelCount;
   public final int gravelMinHeight;
   public final int gravelMaxHeight;
   public final int graniteSize;
   public final int graniteCount;
   public final int graniteMinHeight;
   public final int graniteMaxHeight;
   public final int dioriteSize;
   public final int dioriteCount;
   public final int dioriteMinHeight;
   public final int dioriteMaxHeight;
   public final int andesiteSize;
   public final int andesiteCount;
   public final int andesiteMinHeight;
   public final int andesiteMaxHeight;
   public final int coalSize;
   public final int coalCount;
   public final int coalMinHeight;
   public final int coalMaxHeight;
   public final int ironSize;
   public final int ironCount;
   public final int ironMinHeight;
   public final int ironMaxHeight;
   public final int goldSize;
   public final int goldCount;
   public final int goldMinHeight;
   public final int goldMaxHeight;
   public final int redstoneSize;
   public final int redstoneCount;
   public final int redstoneMinHeight;
   public final int redstoneMaxHeight;
   public final int diamondSize;
   public final int diamondCount;
   public final int diamondMinHeight;
   public final int diamondMaxHeight;
   public final int lapisSize;
   public final int lapisCount;
   public final int lapisCenterHeight;
   public final int lapisSpread;

   private ChunkGeneratorSettings(ChunkGeneratorSettings.Factory var1) {
      this.coordinateScale = ☃.coordinateScale;
      this.heightScale = ☃.heightScale;
      this.upperLimitScale = ☃.upperLimitScale;
      this.lowerLimitScale = ☃.lowerLimitScale;
      this.depthNoiseScaleX = ☃.depthNoiseScaleX;
      this.depthNoiseScaleZ = ☃.depthNoiseScaleZ;
      this.depthNoiseScaleExponent = ☃.depthNoiseScaleExponent;
      this.mainNoiseScaleX = ☃.mainNoiseScaleX;
      this.mainNoiseScaleY = ☃.mainNoiseScaleY;
      this.mainNoiseScaleZ = ☃.mainNoiseScaleZ;
      this.baseSize = ☃.baseSize;
      this.stretchY = ☃.stretchY;
      this.biomeDepthWeight = ☃.biomeDepthWeight;
      this.biomeDepthOffSet = ☃.biomeDepthOffset;
      this.biomeScaleWeight = ☃.biomeScaleWeight;
      this.biomeScaleOffset = ☃.biomeScaleOffset;
      this.seaLevel = ☃.seaLevel;
      this.useCaves = ☃.useCaves;
      this.useDungeons = ☃.useDungeons;
      this.dungeonChance = ☃.dungeonChance;
      this.useStrongholds = ☃.useStrongholds;
      this.useVillages = ☃.useVillages;
      this.useMineShafts = ☃.useMineShafts;
      this.useTemples = ☃.useTemples;
      this.useMonuments = ☃.useMonuments;
      this.useMansions = ☃.useMansions;
      this.useRavines = ☃.useRavines;
      this.useWaterLakes = ☃.useWaterLakes;
      this.waterLakeChance = ☃.waterLakeChance;
      this.useLavaLakes = ☃.useLavaLakes;
      this.lavaLakeChance = ☃.lavaLakeChance;
      this.useLavaOceans = ☃.useLavaOceans;
      this.fixedBiome = ☃.fixedBiome;
      this.biomeSize = ☃.biomeSize;
      this.riverSize = ☃.riverSize;
      this.dirtSize = ☃.dirtSize;
      this.dirtCount = ☃.dirtCount;
      this.dirtMinHeight = ☃.dirtMinHeight;
      this.dirtMaxHeight = ☃.dirtMaxHeight;
      this.gravelSize = ☃.gravelSize;
      this.gravelCount = ☃.gravelCount;
      this.gravelMinHeight = ☃.gravelMinHeight;
      this.gravelMaxHeight = ☃.gravelMaxHeight;
      this.graniteSize = ☃.graniteSize;
      this.graniteCount = ☃.graniteCount;
      this.graniteMinHeight = ☃.graniteMinHeight;
      this.graniteMaxHeight = ☃.graniteMaxHeight;
      this.dioriteSize = ☃.dioriteSize;
      this.dioriteCount = ☃.dioriteCount;
      this.dioriteMinHeight = ☃.dioriteMinHeight;
      this.dioriteMaxHeight = ☃.dioriteMaxHeight;
      this.andesiteSize = ☃.andesiteSize;
      this.andesiteCount = ☃.andesiteCount;
      this.andesiteMinHeight = ☃.andesiteMinHeight;
      this.andesiteMaxHeight = ☃.andesiteMaxHeight;
      this.coalSize = ☃.coalSize;
      this.coalCount = ☃.coalCount;
      this.coalMinHeight = ☃.coalMinHeight;
      this.coalMaxHeight = ☃.coalMaxHeight;
      this.ironSize = ☃.ironSize;
      this.ironCount = ☃.ironCount;
      this.ironMinHeight = ☃.ironMinHeight;
      this.ironMaxHeight = ☃.ironMaxHeight;
      this.goldSize = ☃.goldSize;
      this.goldCount = ☃.goldCount;
      this.goldMinHeight = ☃.goldMinHeight;
      this.goldMaxHeight = ☃.goldMaxHeight;
      this.redstoneSize = ☃.redstoneSize;
      this.redstoneCount = ☃.redstoneCount;
      this.redstoneMinHeight = ☃.redstoneMinHeight;
      this.redstoneMaxHeight = ☃.redstoneMaxHeight;
      this.diamondSize = ☃.diamondSize;
      this.diamondCount = ☃.diamondCount;
      this.diamondMinHeight = ☃.diamondMinHeight;
      this.diamondMaxHeight = ☃.diamondMaxHeight;
      this.lapisSize = ☃.lapisSize;
      this.lapisCount = ☃.lapisCount;
      this.lapisCenterHeight = ☃.lapisCenterHeight;
      this.lapisSpread = ☃.lapisSpread;
   }

   public static class Factory {
      @VisibleForTesting
      static final Gson JSON_ADAPTER = new GsonBuilder()
         .registerTypeAdapter(ChunkGeneratorSettings.Factory.class, new ChunkGeneratorSettings.Serializer())
         .create();
      public float coordinateScale = 684.412F;
      public float heightScale = 684.412F;
      public float upperLimitScale = 512.0F;
      public float lowerLimitScale = 512.0F;
      public float depthNoiseScaleX = 200.0F;
      public float depthNoiseScaleZ = 200.0F;
      public float depthNoiseScaleExponent = 0.5F;
      public float mainNoiseScaleX = 80.0F;
      public float mainNoiseScaleY = 160.0F;
      public float mainNoiseScaleZ = 80.0F;
      public float baseSize = 8.5F;
      public float stretchY = 12.0F;
      public float biomeDepthWeight = 1.0F;
      public float biomeDepthOffset;
      public float biomeScaleWeight = 1.0F;
      public float biomeScaleOffset;
      public int seaLevel = 63;
      public boolean useCaves = true;
      public boolean useDungeons = true;
      public int dungeonChance = 8;
      public boolean useStrongholds = true;
      public boolean useVillages = true;
      public boolean useMineShafts = true;
      public boolean useTemples = true;
      public boolean useMonuments = true;
      public boolean useMansions = true;
      public boolean useRavines = true;
      public boolean useWaterLakes = true;
      public int waterLakeChance = 4;
      public boolean useLavaLakes = true;
      public int lavaLakeChance = 80;
      public boolean useLavaOceans;
      public int fixedBiome = -1;
      public int biomeSize = 4;
      public int riverSize = 4;
      public int dirtSize = 33;
      public int dirtCount = 10;
      public int dirtMinHeight;
      public int dirtMaxHeight = 256;
      public int gravelSize = 33;
      public int gravelCount = 8;
      public int gravelMinHeight;
      public int gravelMaxHeight = 256;
      public int graniteSize = 33;
      public int graniteCount = 10;
      public int graniteMinHeight;
      public int graniteMaxHeight = 80;
      public int dioriteSize = 33;
      public int dioriteCount = 10;
      public int dioriteMinHeight;
      public int dioriteMaxHeight = 80;
      public int andesiteSize = 33;
      public int andesiteCount = 10;
      public int andesiteMinHeight;
      public int andesiteMaxHeight = 80;
      public int coalSize = 17;
      public int coalCount = 20;
      public int coalMinHeight;
      public int coalMaxHeight = 128;
      public int ironSize = 9;
      public int ironCount = 20;
      public int ironMinHeight;
      public int ironMaxHeight = 64;
      public int goldSize = 9;
      public int goldCount = 2;
      public int goldMinHeight;
      public int goldMaxHeight = 32;
      public int redstoneSize = 8;
      public int redstoneCount = 8;
      public int redstoneMinHeight;
      public int redstoneMaxHeight = 16;
      public int diamondSize = 8;
      public int diamondCount = 1;
      public int diamondMinHeight;
      public int diamondMaxHeight = 16;
      public int lapisSize = 7;
      public int lapisCount = 1;
      public int lapisCenterHeight = 16;
      public int lapisSpread = 16;

      public static ChunkGeneratorSettings.Factory jsonToFactory(String var0) {
         if (☃.isEmpty()) {
            return new ChunkGeneratorSettings.Factory();
         } else {
            try {
               return JsonUtils.gsonDeserialize(JSON_ADAPTER, ☃, ChunkGeneratorSettings.Factory.class);
            } catch (Exception var2) {
               return new ChunkGeneratorSettings.Factory();
            }
         }
      }

      @Override
      public String toString() {
         return JSON_ADAPTER.toJson(this);
      }

      public Factory() {
         this.setDefaults();
      }

      public void setDefaults() {
         this.coordinateScale = 684.412F;
         this.heightScale = 684.412F;
         this.upperLimitScale = 512.0F;
         this.lowerLimitScale = 512.0F;
         this.depthNoiseScaleX = 200.0F;
         this.depthNoiseScaleZ = 200.0F;
         this.depthNoiseScaleExponent = 0.5F;
         this.mainNoiseScaleX = 80.0F;
         this.mainNoiseScaleY = 160.0F;
         this.mainNoiseScaleZ = 80.0F;
         this.baseSize = 8.5F;
         this.stretchY = 12.0F;
         this.biomeDepthWeight = 1.0F;
         this.biomeDepthOffset = 0.0F;
         this.biomeScaleWeight = 1.0F;
         this.biomeScaleOffset = 0.0F;
         this.seaLevel = 63;
         this.useCaves = true;
         this.useDungeons = true;
         this.dungeonChance = 8;
         this.useStrongholds = true;
         this.useVillages = true;
         this.useMineShafts = true;
         this.useTemples = true;
         this.useMonuments = true;
         this.useMansions = true;
         this.useRavines = true;
         this.useWaterLakes = true;
         this.waterLakeChance = 4;
         this.useLavaLakes = true;
         this.lavaLakeChance = 80;
         this.useLavaOceans = false;
         this.fixedBiome = -1;
         this.biomeSize = 4;
         this.riverSize = 4;
         this.dirtSize = 33;
         this.dirtCount = 10;
         this.dirtMinHeight = 0;
         this.dirtMaxHeight = 256;
         this.gravelSize = 33;
         this.gravelCount = 8;
         this.gravelMinHeight = 0;
         this.gravelMaxHeight = 256;
         this.graniteSize = 33;
         this.graniteCount = 10;
         this.graniteMinHeight = 0;
         this.graniteMaxHeight = 80;
         this.dioriteSize = 33;
         this.dioriteCount = 10;
         this.dioriteMinHeight = 0;
         this.dioriteMaxHeight = 80;
         this.andesiteSize = 33;
         this.andesiteCount = 10;
         this.andesiteMinHeight = 0;
         this.andesiteMaxHeight = 80;
         this.coalSize = 17;
         this.coalCount = 20;
         this.coalMinHeight = 0;
         this.coalMaxHeight = 128;
         this.ironSize = 9;
         this.ironCount = 20;
         this.ironMinHeight = 0;
         this.ironMaxHeight = 64;
         this.goldSize = 9;
         this.goldCount = 2;
         this.goldMinHeight = 0;
         this.goldMaxHeight = 32;
         this.redstoneSize = 8;
         this.redstoneCount = 8;
         this.redstoneMinHeight = 0;
         this.redstoneMaxHeight = 16;
         this.diamondSize = 8;
         this.diamondCount = 1;
         this.diamondMinHeight = 0;
         this.diamondMaxHeight = 16;
         this.lapisSize = 7;
         this.lapisCount = 1;
         this.lapisCenterHeight = 16;
         this.lapisSpread = 16;
      }

      @Override
      public boolean equals(Object var1) {
         if (this == ☃) {
            return true;
         } else if (☃ != null && this.getClass() == ☃.getClass()) {
            ChunkGeneratorSettings.Factory ☃ = (ChunkGeneratorSettings.Factory)☃;
            if (this.andesiteCount != ☃.andesiteCount) {
               return false;
            } else if (this.andesiteMaxHeight != ☃.andesiteMaxHeight) {
               return false;
            } else if (this.andesiteMinHeight != ☃.andesiteMinHeight) {
               return false;
            } else if (this.andesiteSize != ☃.andesiteSize) {
               return false;
            } else if (Float.compare(☃.baseSize, this.baseSize) != 0) {
               return false;
            } else if (Float.compare(☃.biomeDepthOffset, this.biomeDepthOffset) != 0) {
               return false;
            } else if (Float.compare(☃.biomeDepthWeight, this.biomeDepthWeight) != 0) {
               return false;
            } else if (Float.compare(☃.biomeScaleOffset, this.biomeScaleOffset) != 0) {
               return false;
            } else if (Float.compare(☃.biomeScaleWeight, this.biomeScaleWeight) != 0) {
               return false;
            } else if (this.biomeSize != ☃.biomeSize) {
               return false;
            } else if (this.coalCount != ☃.coalCount) {
               return false;
            } else if (this.coalMaxHeight != ☃.coalMaxHeight) {
               return false;
            } else if (this.coalMinHeight != ☃.coalMinHeight) {
               return false;
            } else if (this.coalSize != ☃.coalSize) {
               return false;
            } else if (Float.compare(☃.coordinateScale, this.coordinateScale) != 0) {
               return false;
            } else if (Float.compare(☃.depthNoiseScaleExponent, this.depthNoiseScaleExponent) != 0) {
               return false;
            } else if (Float.compare(☃.depthNoiseScaleX, this.depthNoiseScaleX) != 0) {
               return false;
            } else if (Float.compare(☃.depthNoiseScaleZ, this.depthNoiseScaleZ) != 0) {
               return false;
            } else if (this.diamondCount != ☃.diamondCount) {
               return false;
            } else if (this.diamondMaxHeight != ☃.diamondMaxHeight) {
               return false;
            } else if (this.diamondMinHeight != ☃.diamondMinHeight) {
               return false;
            } else if (this.diamondSize != ☃.diamondSize) {
               return false;
            } else if (this.dioriteCount != ☃.dioriteCount) {
               return false;
            } else if (this.dioriteMaxHeight != ☃.dioriteMaxHeight) {
               return false;
            } else if (this.dioriteMinHeight != ☃.dioriteMinHeight) {
               return false;
            } else if (this.dioriteSize != ☃.dioriteSize) {
               return false;
            } else if (this.dirtCount != ☃.dirtCount) {
               return false;
            } else if (this.dirtMaxHeight != ☃.dirtMaxHeight) {
               return false;
            } else if (this.dirtMinHeight != ☃.dirtMinHeight) {
               return false;
            } else if (this.dirtSize != ☃.dirtSize) {
               return false;
            } else if (this.dungeonChance != ☃.dungeonChance) {
               return false;
            } else if (this.fixedBiome != ☃.fixedBiome) {
               return false;
            } else if (this.goldCount != ☃.goldCount) {
               return false;
            } else if (this.goldMaxHeight != ☃.goldMaxHeight) {
               return false;
            } else if (this.goldMinHeight != ☃.goldMinHeight) {
               return false;
            } else if (this.goldSize != ☃.goldSize) {
               return false;
            } else if (this.graniteCount != ☃.graniteCount) {
               return false;
            } else if (this.graniteMaxHeight != ☃.graniteMaxHeight) {
               return false;
            } else if (this.graniteMinHeight != ☃.graniteMinHeight) {
               return false;
            } else if (this.graniteSize != ☃.graniteSize) {
               return false;
            } else if (this.gravelCount != ☃.gravelCount) {
               return false;
            } else if (this.gravelMaxHeight != ☃.gravelMaxHeight) {
               return false;
            } else if (this.gravelMinHeight != ☃.gravelMinHeight) {
               return false;
            } else if (this.gravelSize != ☃.gravelSize) {
               return false;
            } else if (Float.compare(☃.heightScale, this.heightScale) != 0) {
               return false;
            } else if (this.ironCount != ☃.ironCount) {
               return false;
            } else if (this.ironMaxHeight != ☃.ironMaxHeight) {
               return false;
            } else if (this.ironMinHeight != ☃.ironMinHeight) {
               return false;
            } else if (this.ironSize != ☃.ironSize) {
               return false;
            } else if (this.lapisCenterHeight != ☃.lapisCenterHeight) {
               return false;
            } else if (this.lapisCount != ☃.lapisCount) {
               return false;
            } else if (this.lapisSize != ☃.lapisSize) {
               return false;
            } else if (this.lapisSpread != ☃.lapisSpread) {
               return false;
            } else if (this.lavaLakeChance != ☃.lavaLakeChance) {
               return false;
            } else if (Float.compare(☃.lowerLimitScale, this.lowerLimitScale) != 0) {
               return false;
            } else if (Float.compare(☃.mainNoiseScaleX, this.mainNoiseScaleX) != 0) {
               return false;
            } else if (Float.compare(☃.mainNoiseScaleY, this.mainNoiseScaleY) != 0) {
               return false;
            } else if (Float.compare(☃.mainNoiseScaleZ, this.mainNoiseScaleZ) != 0) {
               return false;
            } else if (this.redstoneCount != ☃.redstoneCount) {
               return false;
            } else if (this.redstoneMaxHeight != ☃.redstoneMaxHeight) {
               return false;
            } else if (this.redstoneMinHeight != ☃.redstoneMinHeight) {
               return false;
            } else if (this.redstoneSize != ☃.redstoneSize) {
               return false;
            } else if (this.riverSize != ☃.riverSize) {
               return false;
            } else if (this.seaLevel != ☃.seaLevel) {
               return false;
            } else if (Float.compare(☃.stretchY, this.stretchY) != 0) {
               return false;
            } else if (Float.compare(☃.upperLimitScale, this.upperLimitScale) != 0) {
               return false;
            } else if (this.useCaves != ☃.useCaves) {
               return false;
            } else if (this.useDungeons != ☃.useDungeons) {
               return false;
            } else if (this.useLavaLakes != ☃.useLavaLakes) {
               return false;
            } else if (this.useLavaOceans != ☃.useLavaOceans) {
               return false;
            } else if (this.useMineShafts != ☃.useMineShafts) {
               return false;
            } else if (this.useRavines != ☃.useRavines) {
               return false;
            } else if (this.useStrongholds != ☃.useStrongholds) {
               return false;
            } else if (this.useTemples != ☃.useTemples) {
               return false;
            } else if (this.useMonuments != ☃.useMonuments) {
               return false;
            } else if (this.useMansions != ☃.useMansions) {
               return false;
            } else if (this.useVillages != ☃.useVillages) {
               return false;
            } else {
               return this.useWaterLakes != ☃.useWaterLakes ? false : this.waterLakeChance == ☃.waterLakeChance;
            }
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         int ☃ = this.coordinateScale == 0.0F ? 0 : Float.floatToIntBits(this.coordinateScale);
         ☃ = 31 * ☃ + (this.heightScale == 0.0F ? 0 : Float.floatToIntBits(this.heightScale));
         ☃ = 31 * ☃ + (this.upperLimitScale == 0.0F ? 0 : Float.floatToIntBits(this.upperLimitScale));
         ☃ = 31 * ☃ + (this.lowerLimitScale == 0.0F ? 0 : Float.floatToIntBits(this.lowerLimitScale));
         ☃ = 31 * ☃ + (this.depthNoiseScaleX == 0.0F ? 0 : Float.floatToIntBits(this.depthNoiseScaleX));
         ☃ = 31 * ☃ + (this.depthNoiseScaleZ == 0.0F ? 0 : Float.floatToIntBits(this.depthNoiseScaleZ));
         ☃ = 31 * ☃ + (this.depthNoiseScaleExponent == 0.0F ? 0 : Float.floatToIntBits(this.depthNoiseScaleExponent));
         ☃ = 31 * ☃ + (this.mainNoiseScaleX == 0.0F ? 0 : Float.floatToIntBits(this.mainNoiseScaleX));
         ☃ = 31 * ☃ + (this.mainNoiseScaleY == 0.0F ? 0 : Float.floatToIntBits(this.mainNoiseScaleY));
         ☃ = 31 * ☃ + (this.mainNoiseScaleZ == 0.0F ? 0 : Float.floatToIntBits(this.mainNoiseScaleZ));
         ☃ = 31 * ☃ + (this.baseSize == 0.0F ? 0 : Float.floatToIntBits(this.baseSize));
         ☃ = 31 * ☃ + (this.stretchY == 0.0F ? 0 : Float.floatToIntBits(this.stretchY));
         ☃ = 31 * ☃ + (this.biomeDepthWeight == 0.0F ? 0 : Float.floatToIntBits(this.biomeDepthWeight));
         ☃ = 31 * ☃ + (this.biomeDepthOffset == 0.0F ? 0 : Float.floatToIntBits(this.biomeDepthOffset));
         ☃ = 31 * ☃ + (this.biomeScaleWeight == 0.0F ? 0 : Float.floatToIntBits(this.biomeScaleWeight));
         ☃ = 31 * ☃ + (this.biomeScaleOffset == 0.0F ? 0 : Float.floatToIntBits(this.biomeScaleOffset));
         ☃ = 31 * ☃ + this.seaLevel;
         ☃ = 31 * ☃ + (this.useCaves ? 1 : 0);
         ☃ = 31 * ☃ + (this.useDungeons ? 1 : 0);
         ☃ = 31 * ☃ + this.dungeonChance;
         ☃ = 31 * ☃ + (this.useStrongholds ? 1 : 0);
         ☃ = 31 * ☃ + (this.useVillages ? 1 : 0);
         ☃ = 31 * ☃ + (this.useMineShafts ? 1 : 0);
         ☃ = 31 * ☃ + (this.useTemples ? 1 : 0);
         ☃ = 31 * ☃ + (this.useMonuments ? 1 : 0);
         ☃ = 31 * ☃ + (this.useMansions ? 1 : 0);
         ☃ = 31 * ☃ + (this.useRavines ? 1 : 0);
         ☃ = 31 * ☃ + (this.useWaterLakes ? 1 : 0);
         ☃ = 31 * ☃ + this.waterLakeChance;
         ☃ = 31 * ☃ + (this.useLavaLakes ? 1 : 0);
         ☃ = 31 * ☃ + this.lavaLakeChance;
         ☃ = 31 * ☃ + (this.useLavaOceans ? 1 : 0);
         ☃ = 31 * ☃ + this.fixedBiome;
         ☃ = 31 * ☃ + this.biomeSize;
         ☃ = 31 * ☃ + this.riverSize;
         ☃ = 31 * ☃ + this.dirtSize;
         ☃ = 31 * ☃ + this.dirtCount;
         ☃ = 31 * ☃ + this.dirtMinHeight;
         ☃ = 31 * ☃ + this.dirtMaxHeight;
         ☃ = 31 * ☃ + this.gravelSize;
         ☃ = 31 * ☃ + this.gravelCount;
         ☃ = 31 * ☃ + this.gravelMinHeight;
         ☃ = 31 * ☃ + this.gravelMaxHeight;
         ☃ = 31 * ☃ + this.graniteSize;
         ☃ = 31 * ☃ + this.graniteCount;
         ☃ = 31 * ☃ + this.graniteMinHeight;
         ☃ = 31 * ☃ + this.graniteMaxHeight;
         ☃ = 31 * ☃ + this.dioriteSize;
         ☃ = 31 * ☃ + this.dioriteCount;
         ☃ = 31 * ☃ + this.dioriteMinHeight;
         ☃ = 31 * ☃ + this.dioriteMaxHeight;
         ☃ = 31 * ☃ + this.andesiteSize;
         ☃ = 31 * ☃ + this.andesiteCount;
         ☃ = 31 * ☃ + this.andesiteMinHeight;
         ☃ = 31 * ☃ + this.andesiteMaxHeight;
         ☃ = 31 * ☃ + this.coalSize;
         ☃ = 31 * ☃ + this.coalCount;
         ☃ = 31 * ☃ + this.coalMinHeight;
         ☃ = 31 * ☃ + this.coalMaxHeight;
         ☃ = 31 * ☃ + this.ironSize;
         ☃ = 31 * ☃ + this.ironCount;
         ☃ = 31 * ☃ + this.ironMinHeight;
         ☃ = 31 * ☃ + this.ironMaxHeight;
         ☃ = 31 * ☃ + this.goldSize;
         ☃ = 31 * ☃ + this.goldCount;
         ☃ = 31 * ☃ + this.goldMinHeight;
         ☃ = 31 * ☃ + this.goldMaxHeight;
         ☃ = 31 * ☃ + this.redstoneSize;
         ☃ = 31 * ☃ + this.redstoneCount;
         ☃ = 31 * ☃ + this.redstoneMinHeight;
         ☃ = 31 * ☃ + this.redstoneMaxHeight;
         ☃ = 31 * ☃ + this.diamondSize;
         ☃ = 31 * ☃ + this.diamondCount;
         ☃ = 31 * ☃ + this.diamondMinHeight;
         ☃ = 31 * ☃ + this.diamondMaxHeight;
         ☃ = 31 * ☃ + this.lapisSize;
         ☃ = 31 * ☃ + this.lapisCount;
         ☃ = 31 * ☃ + this.lapisCenterHeight;
         return 31 * ☃ + this.lapisSpread;
      }

      public ChunkGeneratorSettings build() {
         return new ChunkGeneratorSettings(this);
      }
   }

   public static class Serializer implements JsonDeserializer<ChunkGeneratorSettings.Factory>, JsonSerializer<ChunkGeneratorSettings.Factory> {
      public ChunkGeneratorSettings.Factory deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         JsonObject ☃ = ☃.getAsJsonObject();
         ChunkGeneratorSettings.Factory ☃x = new ChunkGeneratorSettings.Factory();

         try {
            ☃x.coordinateScale = JsonUtils.getFloat(☃, "coordinateScale", ☃x.coordinateScale);
            ☃x.heightScale = JsonUtils.getFloat(☃, "heightScale", ☃x.heightScale);
            ☃x.lowerLimitScale = JsonUtils.getFloat(☃, "lowerLimitScale", ☃x.lowerLimitScale);
            ☃x.upperLimitScale = JsonUtils.getFloat(☃, "upperLimitScale", ☃x.upperLimitScale);
            ☃x.depthNoiseScaleX = JsonUtils.getFloat(☃, "depthNoiseScaleX", ☃x.depthNoiseScaleX);
            ☃x.depthNoiseScaleZ = JsonUtils.getFloat(☃, "depthNoiseScaleZ", ☃x.depthNoiseScaleZ);
            ☃x.depthNoiseScaleExponent = JsonUtils.getFloat(☃, "depthNoiseScaleExponent", ☃x.depthNoiseScaleExponent);
            ☃x.mainNoiseScaleX = JsonUtils.getFloat(☃, "mainNoiseScaleX", ☃x.mainNoiseScaleX);
            ☃x.mainNoiseScaleY = JsonUtils.getFloat(☃, "mainNoiseScaleY", ☃x.mainNoiseScaleY);
            ☃x.mainNoiseScaleZ = JsonUtils.getFloat(☃, "mainNoiseScaleZ", ☃x.mainNoiseScaleZ);
            ☃x.baseSize = JsonUtils.getFloat(☃, "baseSize", ☃x.baseSize);
            ☃x.stretchY = JsonUtils.getFloat(☃, "stretchY", ☃x.stretchY);
            ☃x.biomeDepthWeight = JsonUtils.getFloat(☃, "biomeDepthWeight", ☃x.biomeDepthWeight);
            ☃x.biomeDepthOffset = JsonUtils.getFloat(☃, "biomeDepthOffset", ☃x.biomeDepthOffset);
            ☃x.biomeScaleWeight = JsonUtils.getFloat(☃, "biomeScaleWeight", ☃x.biomeScaleWeight);
            ☃x.biomeScaleOffset = JsonUtils.getFloat(☃, "biomeScaleOffset", ☃x.biomeScaleOffset);
            ☃x.seaLevel = JsonUtils.getInt(☃, "seaLevel", ☃x.seaLevel);
            ☃x.useCaves = JsonUtils.getBoolean(☃, "useCaves", ☃x.useCaves);
            ☃x.useDungeons = JsonUtils.getBoolean(☃, "useDungeons", ☃x.useDungeons);
            ☃x.dungeonChance = JsonUtils.getInt(☃, "dungeonChance", ☃x.dungeonChance);
            ☃x.useStrongholds = JsonUtils.getBoolean(☃, "useStrongholds", ☃x.useStrongholds);
            ☃x.useVillages = JsonUtils.getBoolean(☃, "useVillages", ☃x.useVillages);
            ☃x.useMineShafts = JsonUtils.getBoolean(☃, "useMineShafts", ☃x.useMineShafts);
            ☃x.useTemples = JsonUtils.getBoolean(☃, "useTemples", ☃x.useTemples);
            ☃x.useMonuments = JsonUtils.getBoolean(☃, "useMonuments", ☃x.useMonuments);
            ☃x.useMansions = JsonUtils.getBoolean(☃, "useMansions", ☃x.useMansions);
            ☃x.useRavines = JsonUtils.getBoolean(☃, "useRavines", ☃x.useRavines);
            ☃x.useWaterLakes = JsonUtils.getBoolean(☃, "useWaterLakes", ☃x.useWaterLakes);
            ☃x.waterLakeChance = JsonUtils.getInt(☃, "waterLakeChance", ☃x.waterLakeChance);
            ☃x.useLavaLakes = JsonUtils.getBoolean(☃, "useLavaLakes", ☃x.useLavaLakes);
            ☃x.lavaLakeChance = JsonUtils.getInt(☃, "lavaLakeChance", ☃x.lavaLakeChance);
            ☃x.useLavaOceans = JsonUtils.getBoolean(☃, "useLavaOceans", ☃x.useLavaOceans);
            ☃x.fixedBiome = JsonUtils.getInt(☃, "fixedBiome", ☃x.fixedBiome);
            if (☃x.fixedBiome < 38 && ☃x.fixedBiome >= -1) {
               if (☃x.fixedBiome >= Biome.getIdForBiome(Biomes.HELL)) {
                  ☃x.fixedBiome += 2;
               }
            } else {
               ☃x.fixedBiome = -1;
            }

            ☃x.biomeSize = JsonUtils.getInt(☃, "biomeSize", ☃x.biomeSize);
            ☃x.riverSize = JsonUtils.getInt(☃, "riverSize", ☃x.riverSize);
            ☃x.dirtSize = JsonUtils.getInt(☃, "dirtSize", ☃x.dirtSize);
            ☃x.dirtCount = JsonUtils.getInt(☃, "dirtCount", ☃x.dirtCount);
            ☃x.dirtMinHeight = JsonUtils.getInt(☃, "dirtMinHeight", ☃x.dirtMinHeight);
            ☃x.dirtMaxHeight = JsonUtils.getInt(☃, "dirtMaxHeight", ☃x.dirtMaxHeight);
            ☃x.gravelSize = JsonUtils.getInt(☃, "gravelSize", ☃x.gravelSize);
            ☃x.gravelCount = JsonUtils.getInt(☃, "gravelCount", ☃x.gravelCount);
            ☃x.gravelMinHeight = JsonUtils.getInt(☃, "gravelMinHeight", ☃x.gravelMinHeight);
            ☃x.gravelMaxHeight = JsonUtils.getInt(☃, "gravelMaxHeight", ☃x.gravelMaxHeight);
            ☃x.graniteSize = JsonUtils.getInt(☃, "graniteSize", ☃x.graniteSize);
            ☃x.graniteCount = JsonUtils.getInt(☃, "graniteCount", ☃x.graniteCount);
            ☃x.graniteMinHeight = JsonUtils.getInt(☃, "graniteMinHeight", ☃x.graniteMinHeight);
            ☃x.graniteMaxHeight = JsonUtils.getInt(☃, "graniteMaxHeight", ☃x.graniteMaxHeight);
            ☃x.dioriteSize = JsonUtils.getInt(☃, "dioriteSize", ☃x.dioriteSize);
            ☃x.dioriteCount = JsonUtils.getInt(☃, "dioriteCount", ☃x.dioriteCount);
            ☃x.dioriteMinHeight = JsonUtils.getInt(☃, "dioriteMinHeight", ☃x.dioriteMinHeight);
            ☃x.dioriteMaxHeight = JsonUtils.getInt(☃, "dioriteMaxHeight", ☃x.dioriteMaxHeight);
            ☃x.andesiteSize = JsonUtils.getInt(☃, "andesiteSize", ☃x.andesiteSize);
            ☃x.andesiteCount = JsonUtils.getInt(☃, "andesiteCount", ☃x.andesiteCount);
            ☃x.andesiteMinHeight = JsonUtils.getInt(☃, "andesiteMinHeight", ☃x.andesiteMinHeight);
            ☃x.andesiteMaxHeight = JsonUtils.getInt(☃, "andesiteMaxHeight", ☃x.andesiteMaxHeight);
            ☃x.coalSize = JsonUtils.getInt(☃, "coalSize", ☃x.coalSize);
            ☃x.coalCount = JsonUtils.getInt(☃, "coalCount", ☃x.coalCount);
            ☃x.coalMinHeight = JsonUtils.getInt(☃, "coalMinHeight", ☃x.coalMinHeight);
            ☃x.coalMaxHeight = JsonUtils.getInt(☃, "coalMaxHeight", ☃x.coalMaxHeight);
            ☃x.ironSize = JsonUtils.getInt(☃, "ironSize", ☃x.ironSize);
            ☃x.ironCount = JsonUtils.getInt(☃, "ironCount", ☃x.ironCount);
            ☃x.ironMinHeight = JsonUtils.getInt(☃, "ironMinHeight", ☃x.ironMinHeight);
            ☃x.ironMaxHeight = JsonUtils.getInt(☃, "ironMaxHeight", ☃x.ironMaxHeight);
            ☃x.goldSize = JsonUtils.getInt(☃, "goldSize", ☃x.goldSize);
            ☃x.goldCount = JsonUtils.getInt(☃, "goldCount", ☃x.goldCount);
            ☃x.goldMinHeight = JsonUtils.getInt(☃, "goldMinHeight", ☃x.goldMinHeight);
            ☃x.goldMaxHeight = JsonUtils.getInt(☃, "goldMaxHeight", ☃x.goldMaxHeight);
            ☃x.redstoneSize = JsonUtils.getInt(☃, "redstoneSize", ☃x.redstoneSize);
            ☃x.redstoneCount = JsonUtils.getInt(☃, "redstoneCount", ☃x.redstoneCount);
            ☃x.redstoneMinHeight = JsonUtils.getInt(☃, "redstoneMinHeight", ☃x.redstoneMinHeight);
            ☃x.redstoneMaxHeight = JsonUtils.getInt(☃, "redstoneMaxHeight", ☃x.redstoneMaxHeight);
            ☃x.diamondSize = JsonUtils.getInt(☃, "diamondSize", ☃x.diamondSize);
            ☃x.diamondCount = JsonUtils.getInt(☃, "diamondCount", ☃x.diamondCount);
            ☃x.diamondMinHeight = JsonUtils.getInt(☃, "diamondMinHeight", ☃x.diamondMinHeight);
            ☃x.diamondMaxHeight = JsonUtils.getInt(☃, "diamondMaxHeight", ☃x.diamondMaxHeight);
            ☃x.lapisSize = JsonUtils.getInt(☃, "lapisSize", ☃x.lapisSize);
            ☃x.lapisCount = JsonUtils.getInt(☃, "lapisCount", ☃x.lapisCount);
            ☃x.lapisCenterHeight = JsonUtils.getInt(☃, "lapisCenterHeight", ☃x.lapisCenterHeight);
            ☃x.lapisSpread = JsonUtils.getInt(☃, "lapisSpread", ☃x.lapisSpread);
         } catch (Exception var7) {
         }

         return ☃x;
      }

      public JsonElement serialize(ChunkGeneratorSettings.Factory var1, Type var2, JsonSerializationContext var3) {
         JsonObject ☃ = new JsonObject();
         ☃.addProperty("coordinateScale", ☃.coordinateScale);
         ☃.addProperty("heightScale", ☃.heightScale);
         ☃.addProperty("lowerLimitScale", ☃.lowerLimitScale);
         ☃.addProperty("upperLimitScale", ☃.upperLimitScale);
         ☃.addProperty("depthNoiseScaleX", ☃.depthNoiseScaleX);
         ☃.addProperty("depthNoiseScaleZ", ☃.depthNoiseScaleZ);
         ☃.addProperty("depthNoiseScaleExponent", ☃.depthNoiseScaleExponent);
         ☃.addProperty("mainNoiseScaleX", ☃.mainNoiseScaleX);
         ☃.addProperty("mainNoiseScaleY", ☃.mainNoiseScaleY);
         ☃.addProperty("mainNoiseScaleZ", ☃.mainNoiseScaleZ);
         ☃.addProperty("baseSize", ☃.baseSize);
         ☃.addProperty("stretchY", ☃.stretchY);
         ☃.addProperty("biomeDepthWeight", ☃.biomeDepthWeight);
         ☃.addProperty("biomeDepthOffset", ☃.biomeDepthOffset);
         ☃.addProperty("biomeScaleWeight", ☃.biomeScaleWeight);
         ☃.addProperty("biomeScaleOffset", ☃.biomeScaleOffset);
         ☃.addProperty("seaLevel", ☃.seaLevel);
         ☃.addProperty("useCaves", ☃.useCaves);
         ☃.addProperty("useDungeons", ☃.useDungeons);
         ☃.addProperty("dungeonChance", ☃.dungeonChance);
         ☃.addProperty("useStrongholds", ☃.useStrongholds);
         ☃.addProperty("useVillages", ☃.useVillages);
         ☃.addProperty("useMineShafts", ☃.useMineShafts);
         ☃.addProperty("useTemples", ☃.useTemples);
         ☃.addProperty("useMonuments", ☃.useMonuments);
         ☃.addProperty("useMansions", ☃.useMansions);
         ☃.addProperty("useRavines", ☃.useRavines);
         ☃.addProperty("useWaterLakes", ☃.useWaterLakes);
         ☃.addProperty("waterLakeChance", ☃.waterLakeChance);
         ☃.addProperty("useLavaLakes", ☃.useLavaLakes);
         ☃.addProperty("lavaLakeChance", ☃.lavaLakeChance);
         ☃.addProperty("useLavaOceans", ☃.useLavaOceans);
         ☃.addProperty("fixedBiome", ☃.fixedBiome);
         ☃.addProperty("biomeSize", ☃.biomeSize);
         ☃.addProperty("riverSize", ☃.riverSize);
         ☃.addProperty("dirtSize", ☃.dirtSize);
         ☃.addProperty("dirtCount", ☃.dirtCount);
         ☃.addProperty("dirtMinHeight", ☃.dirtMinHeight);
         ☃.addProperty("dirtMaxHeight", ☃.dirtMaxHeight);
         ☃.addProperty("gravelSize", ☃.gravelSize);
         ☃.addProperty("gravelCount", ☃.gravelCount);
         ☃.addProperty("gravelMinHeight", ☃.gravelMinHeight);
         ☃.addProperty("gravelMaxHeight", ☃.gravelMaxHeight);
         ☃.addProperty("graniteSize", ☃.graniteSize);
         ☃.addProperty("graniteCount", ☃.graniteCount);
         ☃.addProperty("graniteMinHeight", ☃.graniteMinHeight);
         ☃.addProperty("graniteMaxHeight", ☃.graniteMaxHeight);
         ☃.addProperty("dioriteSize", ☃.dioriteSize);
         ☃.addProperty("dioriteCount", ☃.dioriteCount);
         ☃.addProperty("dioriteMinHeight", ☃.dioriteMinHeight);
         ☃.addProperty("dioriteMaxHeight", ☃.dioriteMaxHeight);
         ☃.addProperty("andesiteSize", ☃.andesiteSize);
         ☃.addProperty("andesiteCount", ☃.andesiteCount);
         ☃.addProperty("andesiteMinHeight", ☃.andesiteMinHeight);
         ☃.addProperty("andesiteMaxHeight", ☃.andesiteMaxHeight);
         ☃.addProperty("coalSize", ☃.coalSize);
         ☃.addProperty("coalCount", ☃.coalCount);
         ☃.addProperty("coalMinHeight", ☃.coalMinHeight);
         ☃.addProperty("coalMaxHeight", ☃.coalMaxHeight);
         ☃.addProperty("ironSize", ☃.ironSize);
         ☃.addProperty("ironCount", ☃.ironCount);
         ☃.addProperty("ironMinHeight", ☃.ironMinHeight);
         ☃.addProperty("ironMaxHeight", ☃.ironMaxHeight);
         ☃.addProperty("goldSize", ☃.goldSize);
         ☃.addProperty("goldCount", ☃.goldCount);
         ☃.addProperty("goldMinHeight", ☃.goldMinHeight);
         ☃.addProperty("goldMaxHeight", ☃.goldMaxHeight);
         ☃.addProperty("redstoneSize", ☃.redstoneSize);
         ☃.addProperty("redstoneCount", ☃.redstoneCount);
         ☃.addProperty("redstoneMinHeight", ☃.redstoneMinHeight);
         ☃.addProperty("redstoneMaxHeight", ☃.redstoneMaxHeight);
         ☃.addProperty("diamondSize", ☃.diamondSize);
         ☃.addProperty("diamondCount", ☃.diamondCount);
         ☃.addProperty("diamondMinHeight", ☃.diamondMinHeight);
         ☃.addProperty("diamondMaxHeight", ☃.diamondMaxHeight);
         ☃.addProperty("lapisSize", ☃.lapisSize);
         ☃.addProperty("lapisCount", ☃.lapisCount);
         ☃.addProperty("lapisCenterHeight", ☃.lapisCenterHeight);
         ☃.addProperty("lapisSpread", ☃.lapisSpread);
         return ☃;
      }
   }
}
