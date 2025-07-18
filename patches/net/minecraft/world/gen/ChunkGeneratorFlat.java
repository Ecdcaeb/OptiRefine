package net.minecraft.world.gen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

public class ChunkGeneratorFlat implements IChunkGenerator {
   private final World world;
   private final Random random;
   private final IBlockState[] cachedBlockIDs = new IBlockState[256];
   private final FlatGeneratorInfo flatWorldGenInfo;
   private final Map<String, MapGenStructure> structureGenerators = new HashMap<>();
   private final boolean hasDecoration;
   private final boolean hasDungeons;
   private WorldGenLakes waterLakeGenerator;
   private WorldGenLakes lavaLakeGenerator;

   public ChunkGeneratorFlat(World var1, long var2, boolean var4, String var5) {
      this.world = ☃;
      this.random = new Random(☃);
      this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(☃);
      if (☃) {
         Map<String, Map<String, String>> ☃ = this.flatWorldGenInfo.getWorldFeatures();
         if (☃.containsKey("village")) {
            Map<String, String> ☃x = ☃.get("village");
            if (!☃x.containsKey("size")) {
               ☃x.put("size", "1");
            }

            this.structureGenerators.put("Village", new MapGenVillage(☃x));
         }

         if (☃.containsKey("biome_1")) {
            this.structureGenerators.put("Temple", new MapGenScatteredFeature(☃.get("biome_1")));
         }

         if (☃.containsKey("mineshaft")) {
            this.structureGenerators.put("Mineshaft", new MapGenMineshaft(☃.get("mineshaft")));
         }

         if (☃.containsKey("stronghold")) {
            this.structureGenerators.put("Stronghold", new MapGenStronghold(☃.get("stronghold")));
         }

         if (☃.containsKey("oceanmonument")) {
            this.structureGenerators.put("Monument", new StructureOceanMonument(☃.get("oceanmonument")));
         }
      }

      if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
         this.waterLakeGenerator = new WorldGenLakes(Blocks.WATER);
      }

      if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
         this.lavaLakeGenerator = new WorldGenLakes(Blocks.LAVA);
      }

      this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
      int ☃x = 0;
      int ☃xx = 0;
      boolean ☃xxx = true;

      for (FlatLayerInfo ☃xxxx : this.flatWorldGenInfo.getFlatLayers()) {
         for (int ☃xxxxx = ☃xxxx.getMinY(); ☃xxxxx < ☃xxxx.getMinY() + ☃xxxx.getLayerCount(); ☃xxxxx++) {
            IBlockState ☃xxxxxx = ☃xxxx.getLayerMaterial();
            if (☃xxxxxx.getBlock() != Blocks.AIR) {
               ☃xxx = false;
               this.cachedBlockIDs[☃xxxxx] = ☃xxxxxx;
            }
         }

         if (☃xxxx.getLayerMaterial().getBlock() == Blocks.AIR) {
            ☃xx += ☃xxxx.getLayerCount();
         } else {
            ☃x += ☃xxxx.getLayerCount() + ☃xx;
            ☃xx = 0;
         }
      }

      ☃.setSeaLevel(☃x);
      this.hasDecoration = ☃xxx && this.flatWorldGenInfo.getBiome() != Biome.getIdForBiome(Biomes.VOID)
         ? false
         : this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration");
   }

   @Override
   public Chunk generateChunk(int var1, int var2) {
      ChunkPrimer ☃ = new ChunkPrimer();

      for (int ☃x = 0; ☃x < this.cachedBlockIDs.length; ☃x++) {
         IBlockState ☃xx = this.cachedBlockIDs[☃x];
         if (☃xx != null) {
            for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
               for (int ☃xxxx = 0; ☃xxxx < 16; ☃xxxx++) {
                  ☃.setBlockState(☃xxx, ☃x, ☃xxxx, ☃xx);
               }
            }
         }
      }

      for (MapGenBase ☃xx : this.structureGenerators.values()) {
         ☃xx.generate(this.world, ☃, ☃, ☃);
      }

      Chunk ☃xx = new Chunk(this.world, ☃, ☃, ☃);
      Biome[] ☃xxx = this.world.getBiomeProvider().getBiomes(null, ☃ * 16, ☃ * 16, 16, 16);
      byte[] ☃xxxx = ☃xx.getBiomeArray();

      for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.length; ☃xxxxx++) {
         ☃xxxx[☃xxxxx] = (byte)Biome.getIdForBiome(☃xxx[☃xxxxx]);
      }

      ☃xx.generateSkylightMap();
      return ☃xx;
   }

   @Override
   public void populate(int var1, int var2) {
      int ☃ = ☃ * 16;
      int ☃x = ☃ * 16;
      BlockPos ☃xx = new BlockPos(☃, 0, ☃x);
      Biome ☃xxx = this.world.getBiome(new BlockPos(☃ + 16, 0, ☃x + 16));
      boolean ☃xxxx = false;
      this.random.setSeed(this.world.getSeed());
      long ☃xxxxx = this.random.nextLong() / 2L * 2L + 1L;
      long ☃xxxxxx = this.random.nextLong() / 2L * 2L + 1L;
      this.random.setSeed(☃ * ☃xxxxx + ☃ * ☃xxxxxx ^ this.world.getSeed());
      ChunkPos ☃xxxxxxx = new ChunkPos(☃, ☃);

      for (MapGenStructure ☃xxxxxxxx : this.structureGenerators.values()) {
         boolean ☃xxxxxxxxx = ☃xxxxxxxx.generateStructure(this.world, this.random, ☃xxxxxxx);
         if (☃xxxxxxxx instanceof MapGenVillage) {
            ☃xxxx |= ☃xxxxxxxxx;
         }
      }

      if (this.waterLakeGenerator != null && !☃xxxx && this.random.nextInt(4) == 0) {
         this.waterLakeGenerator.generate(this.world, this.random, ☃xx.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
      }

      if (this.lavaLakeGenerator != null && !☃xxxx && this.random.nextInt(8) == 0) {
         BlockPos ☃xxxxxxxxx = ☃xx.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);
         if (☃xxxxxxxxx.getY() < this.world.getSeaLevel() || this.random.nextInt(10) == 0) {
            this.lavaLakeGenerator.generate(this.world, this.random, ☃xxxxxxxxx);
         }
      }

      if (this.hasDungeons) {
         for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 8; ☃xxxxxxxxx++) {
            new WorldGenDungeons()
               .generate(this.world, this.random, ☃xx.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
         }
      }

      if (this.hasDecoration) {
         ☃xxx.decorate(this.world, this.random, ☃xx);
      }
   }

   @Override
   public boolean generateStructures(Chunk var1, int var2, int var3) {
      return false;
   }

   @Override
   public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType var1, BlockPos var2) {
      Biome ☃ = this.world.getBiome(☃);
      return ☃.getSpawnableList(☃);
   }

   @Nullable
   @Override
   public BlockPos getNearestStructurePos(World var1, String var2, BlockPos var3, boolean var4) {
      MapGenStructure ☃ = this.structureGenerators.get(☃);
      return ☃ != null ? ☃.getNearestStructurePos(☃, ☃, ☃) : null;
   }

   @Override
   public boolean isInsideStructure(World var1, String var2, BlockPos var3) {
      MapGenStructure ☃ = this.structureGenerators.get(☃);
      return ☃ != null ? ☃.isInsideStructure(☃) : false;
   }

   @Override
   public void recreateStructures(Chunk var1, int var2, int var3) {
      for (MapGenStructure ☃ : this.structureGenerators.values()) {
         ☃.generate(this.world, ☃, ☃, null);
      }
   }
}
