package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeDecorator {
   protected boolean decorating;
   protected BlockPos chunkPos;
   protected ChunkGeneratorSettings chunkProviderSettings;
   protected WorldGenerator clayGen = new WorldGenClay(4);
   protected WorldGenerator sandGen = new WorldGenSand(Blocks.SAND, 7);
   protected WorldGenerator gravelGen = new WorldGenSand(Blocks.GRAVEL, 6);
   protected WorldGenerator dirtGen;
   protected WorldGenerator gravelOreGen;
   protected WorldGenerator graniteGen;
   protected WorldGenerator dioriteGen;
   protected WorldGenerator andesiteGen;
   protected WorldGenerator coalGen;
   protected WorldGenerator ironGen;
   protected WorldGenerator goldGen;
   protected WorldGenerator redstoneGen;
   protected WorldGenerator diamondGen;
   protected WorldGenerator lapisGen;
   protected WorldGenFlowers flowerGen = new WorldGenFlowers(Blocks.YELLOW_FLOWER, BlockFlower.EnumFlowerType.DANDELION);
   protected WorldGenerator mushroomBrownGen = new WorldGenBush(Blocks.BROWN_MUSHROOM);
   protected WorldGenerator mushroomRedGen = new WorldGenBush(Blocks.RED_MUSHROOM);
   protected WorldGenerator bigMushroomGen = new WorldGenBigMushroom();
   protected WorldGenerator reedGen = new WorldGenReed();
   protected WorldGenerator cactusGen = new WorldGenCactus();
   protected WorldGenerator waterlilyGen = new WorldGenWaterlily();
   protected int waterlilyPerChunk;
   protected int treesPerChunk;
   protected float extraTreeChance = 0.1F;
   protected int flowersPerChunk = 2;
   protected int grassPerChunk = 1;
   protected int deadBushPerChunk;
   protected int mushroomsPerChunk;
   protected int reedsPerChunk;
   protected int cactiPerChunk;
   protected int gravelPatchesPerChunk = 1;
   protected int sandPatchesPerChunk = 3;
   protected int clayPerChunk = 1;
   protected int bigMushroomsPerChunk;
   public boolean generateFalls = true;

   public void decorate(World var1, Random var2, Biome var3, BlockPos var4) {
      if (this.decorating) {
         throw new RuntimeException("Already decorating");
      } else {
         this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(☃.getWorldInfo().getGeneratorOptions()).build();
         this.chunkPos = ☃;
         this.dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), this.chunkProviderSettings.dirtSize);
         this.gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), this.chunkProviderSettings.gravelSize);
         this.graniteGen = new WorldGenMinable(
            Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize
         );
         this.dioriteGen = new WorldGenMinable(
            Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize
         );
         this.andesiteGen = new WorldGenMinable(
            Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize
         );
         this.coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), this.chunkProviderSettings.coalSize);
         this.ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), this.chunkProviderSettings.ironSize);
         this.goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), this.chunkProviderSettings.goldSize);
         this.redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), this.chunkProviderSettings.redstoneSize);
         this.diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), this.chunkProviderSettings.diamondSize);
         this.lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), this.chunkProviderSettings.lapisSize);
         this.genDecorations(☃, ☃, ☃);
         this.decorating = false;
      }
   }

   protected void genDecorations(Biome var1, World var2, Random var3) {
      this.generateOres(☃, ☃);

      for (int ☃ = 0; ☃ < this.sandPatchesPerChunk; ☃++) {
         int ☃x = ☃.nextInt(16) + 8;
         int ☃xx = ☃.nextInt(16) + 8;
         this.sandGen.generate(☃, ☃, ☃.getTopSolidOrLiquidBlock(this.chunkPos.add(☃x, 0, ☃xx)));
      }

      for (int ☃ = 0; ☃ < this.clayPerChunk; ☃++) {
         int ☃x = ☃.nextInt(16) + 8;
         int ☃xx = ☃.nextInt(16) + 8;
         this.clayGen.generate(☃, ☃, ☃.getTopSolidOrLiquidBlock(this.chunkPos.add(☃x, 0, ☃xx)));
      }

      for (int ☃ = 0; ☃ < this.gravelPatchesPerChunk; ☃++) {
         int ☃x = ☃.nextInt(16) + 8;
         int ☃xx = ☃.nextInt(16) + 8;
         this.gravelGen.generate(☃, ☃, ☃.getTopSolidOrLiquidBlock(this.chunkPos.add(☃x, 0, ☃xx)));
      }

      int ☃ = this.treesPerChunk;
      if (☃.nextFloat() < this.extraTreeChance) {
         ☃++;
      }

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         int ☃xx = ☃.nextInt(16) + 8;
         int ☃xxx = ☃.nextInt(16) + 8;
         WorldGenAbstractTree ☃xxxx = ☃.getRandomTreeFeature(☃);
         ☃xxxx.setDecorationDefaults();
         BlockPos ☃xxxxx = ☃.getHeight(this.chunkPos.add(☃xx, 0, ☃xxx));
         if (☃xxxx.generate(☃, ☃, ☃xxxxx)) {
            ☃xxxx.generateSaplings(☃, ☃, ☃xxxxx);
         }
      }

      for (int ☃xx = 0; ☃xx < this.bigMushroomsPerChunk; ☃xx++) {
         int ☃xxx = ☃.nextInt(16) + 8;
         int ☃xxxx = ☃.nextInt(16) + 8;
         this.bigMushroomGen.generate(☃, ☃, ☃.getHeight(this.chunkPos.add(☃xxx, 0, ☃xxxx)));
      }

      for (int ☃xx = 0; ☃xx < this.flowersPerChunk; ☃xx++) {
         int ☃xxx = ☃.nextInt(16) + 8;
         int ☃xxxx = ☃.nextInt(16) + 8;
         int ☃xxxxx = ☃.getHeight(this.chunkPos.add(☃xxx, 0, ☃xxxx)).getY() + 32;
         if (☃xxxxx > 0) {
            int ☃xxxxxx = ☃.nextInt(☃xxxxx);
            BlockPos ☃xxxxxxx = this.chunkPos.add(☃xxx, ☃xxxxxx, ☃xxxx);
            BlockFlower.EnumFlowerType ☃xxxxxxxx = ☃.pickRandomFlower(☃, ☃xxxxxxx);
            BlockFlower ☃xxxxxxxxx = ☃xxxxxxxx.getBlockType().getBlock();
            if (☃xxxxxxxxx.getDefaultState().getMaterial() != Material.AIR) {
               this.flowerGen.setGeneratedBlock(☃xxxxxxxxx, ☃xxxxxxxx);
               this.flowerGen.generate(☃, ☃, ☃xxxxxxx);
            }
         }
      }

      for (int ☃xxx = 0; ☃xxx < this.grassPerChunk; ☃xxx++) {
         int ☃xxxx = ☃.nextInt(16) + 8;
         int ☃xxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxx, 0, ☃xxxxx)).getY() * 2;
         if (☃xxxxxx > 0) {
            int ☃xxxxxxx = ☃.nextInt(☃xxxxxx);
            ☃.getRandomWorldGenForGrass(☃).generate(☃, ☃, this.chunkPos.add(☃xxxx, ☃xxxxxxx, ☃xxxxx));
         }
      }

      for (int ☃xxxx = 0; ☃xxxx < this.deadBushPerChunk; ☃xxxx++) {
         int ☃xxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxx, 0, ☃xxxxxx)).getY() * 2;
         if (☃xxxxxxx > 0) {
            int ☃xxxxxxxx = ☃.nextInt(☃xxxxxxx);
            new WorldGenDeadBush().generate(☃, ☃, this.chunkPos.add(☃xxxxx, ☃xxxxxxxx, ☃xxxxxx));
         }
      }

      for (int ☃xxxxx = 0; ☃xxxxx < this.waterlilyPerChunk; ☃xxxxx++) {
         int ☃xxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxxx, 0, ☃xxxxxxx)).getY() * 2;
         if (☃xxxxxxxx > 0) {
            int ☃xxxxxxxxx = ☃.nextInt(☃xxxxxxxx);
            BlockPos ☃xxxxxxxxxx = this.chunkPos.add(☃xxxxxx, ☃xxxxxxxxx, ☃xxxxxxx);

            while (☃xxxxxxxxxx.getY() > 0) {
               BlockPos ☃xxxxxxxxxxx = ☃xxxxxxxxxx.down();
               if (!☃.isAirBlock(☃xxxxxxxxxxx)) {
                  break;
               }

               ☃xxxxxxxxxx = ☃xxxxxxxxxxx;
            }

            this.waterlilyGen.generate(☃, ☃, ☃xxxxxxxxxx);
         }
      }

      for (int ☃xxxxxx = 0; ☃xxxxxx < this.mushroomsPerChunk; ☃xxxxxx++) {
         if (☃.nextInt(4) == 0) {
            int ☃xxxxxxx = ☃.nextInt(16) + 8;
            int ☃xxxxxxxx = ☃.nextInt(16) + 8;
            BlockPos ☃xxxxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxxxx, 0, ☃xxxxxxxx));
            this.mushroomBrownGen.generate(☃, ☃, ☃xxxxxxxxx);
         }

         if (☃.nextInt(8) == 0) {
            int ☃xxxxxxx = ☃.nextInt(16) + 8;
            int ☃xxxxxxxx = ☃.nextInt(16) + 8;
            int ☃xxxxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxxxx, 0, ☃xxxxxxxx)).getY() * 2;
            if (☃xxxxxxxxx > 0) {
               int ☃xxxxxxxxxx = ☃.nextInt(☃xxxxxxxxx);
               BlockPos ☃xxxxxxxxxxx = this.chunkPos.add(☃xxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxx);
               this.mushroomRedGen.generate(☃, ☃, ☃xxxxxxxxxxx);
            }
         }
      }

      if (☃.nextInt(4) == 0) {
         int ☃xxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxxx, 0, ☃xxxxxxx)).getY() * 2;
         if (☃xxxxxxxx > 0) {
            int ☃xxxxxxxxx = ☃.nextInt(☃xxxxxxxx);
            this.mushroomBrownGen.generate(☃, ☃, this.chunkPos.add(☃xxxxxx, ☃xxxxxxxxx, ☃xxxxxxx));
         }
      }

      if (☃.nextInt(8) == 0) {
         int ☃xxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxxx, 0, ☃xxxxxxx)).getY() * 2;
         if (☃xxxxxxxx > 0) {
            int ☃xxxxxxxxx = ☃.nextInt(☃xxxxxxxx);
            this.mushroomRedGen.generate(☃, ☃, this.chunkPos.add(☃xxxxxx, ☃xxxxxxxxx, ☃xxxxxxx));
         }
      }

      for (int ☃xxxxxx = 0; ☃xxxxxx < this.reedsPerChunk; ☃xxxxxx++) {
         int ☃xxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxxxx, 0, ☃xxxxxxxx)).getY() * 2;
         if (☃xxxxxxxxx > 0) {
            int ☃xxxxxxxxxx = ☃.nextInt(☃xxxxxxxxx);
            this.reedGen.generate(☃, ☃, this.chunkPos.add(☃xxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxx));
         }
      }

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < 10; ☃xxxxxxx++) {
         int ☃xxxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxxxxx, 0, ☃xxxxxxxxx)).getY() * 2;
         if (☃xxxxxxxxxx > 0) {
            int ☃xxxxxxxxxxx = ☃.nextInt(☃xxxxxxxxxx);
            this.reedGen.generate(☃, ☃, this.chunkPos.add(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx));
         }
      }

      if (☃.nextInt(32) == 0) {
         int ☃xxxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxxxxx, 0, ☃xxxxxxxxx)).getY() * 2;
         if (☃xxxxxxxxxx > 0) {
            int ☃xxxxxxxxxxx = ☃.nextInt(☃xxxxxxxxxx);
            new WorldGenPumpkin().generate(☃, ☃, this.chunkPos.add(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx));
         }
      }

      for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < this.cactiPerChunk; ☃xxxxxxxx++) {
         int ☃xxxxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxxxx = ☃.nextInt(16) + 8;
         int ☃xxxxxxxxxxx = ☃.getHeight(this.chunkPos.add(☃xxxxxxxxx, 0, ☃xxxxxxxxxx)).getY() * 2;
         if (☃xxxxxxxxxxx > 0) {
            int ☃xxxxxxxxxxxx = ☃.nextInt(☃xxxxxxxxxxx);
            this.cactusGen.generate(☃, ☃, this.chunkPos.add(☃xxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxx));
         }
      }

      if (this.generateFalls) {
         for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 50; ☃xxxxxxxxx++) {
            int ☃xxxxxxxxxx = ☃.nextInt(16) + 8;
            int ☃xxxxxxxxxxx = ☃.nextInt(16) + 8;
            int ☃xxxxxxxxxxxx = ☃.nextInt(248) + 8;
            if (☃xxxxxxxxxxxx > 0) {
               int ☃xxxxxxxxxxxxx = ☃.nextInt(☃xxxxxxxxxxxx);
               BlockPos ☃xxxxxxxxxxxxxx = this.chunkPos.add(☃xxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxx);
               new WorldGenLiquids(Blocks.FLOWING_WATER).generate(☃, ☃, ☃xxxxxxxxxxxxxx);
            }
         }

         for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < 20; ☃xxxxxxxxxx++) {
            int ☃xxxxxxxxxxx = ☃.nextInt(16) + 8;
            int ☃xxxxxxxxxxxx = ☃.nextInt(16) + 8;
            int ☃xxxxxxxxxxxxx = ☃.nextInt(☃.nextInt(☃.nextInt(240) + 8) + 8);
            BlockPos ☃xxxxxxxxxxxxxx = this.chunkPos.add(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxx);
            new WorldGenLiquids(Blocks.FLOWING_LAVA).generate(☃, ☃, ☃xxxxxxxxxxxxxx);
         }
      }
   }

   protected void generateOres(World var1, Random var2) {
      this.genStandardOre1(
         ☃, ☃, this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight
      );
      this.genStandardOre1(
         ☃,
         ☃,
         this.chunkProviderSettings.gravelCount,
         this.gravelOreGen,
         this.chunkProviderSettings.gravelMinHeight,
         this.chunkProviderSettings.gravelMaxHeight
      );
      this.genStandardOre1(
         ☃,
         ☃,
         this.chunkProviderSettings.dioriteCount,
         this.dioriteGen,
         this.chunkProviderSettings.dioriteMinHeight,
         this.chunkProviderSettings.dioriteMaxHeight
      );
      this.genStandardOre1(
         ☃,
         ☃,
         this.chunkProviderSettings.graniteCount,
         this.graniteGen,
         this.chunkProviderSettings.graniteMinHeight,
         this.chunkProviderSettings.graniteMaxHeight
      );
      this.genStandardOre1(
         ☃,
         ☃,
         this.chunkProviderSettings.andesiteCount,
         this.andesiteGen,
         this.chunkProviderSettings.andesiteMinHeight,
         this.chunkProviderSettings.andesiteMaxHeight
      );
      this.genStandardOre1(
         ☃, ☃, this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight
      );
      this.genStandardOre1(
         ☃, ☃, this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight
      );
      this.genStandardOre1(
         ☃, ☃, this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight
      );
      this.genStandardOre1(
         ☃,
         ☃,
         this.chunkProviderSettings.redstoneCount,
         this.redstoneGen,
         this.chunkProviderSettings.redstoneMinHeight,
         this.chunkProviderSettings.redstoneMaxHeight
      );
      this.genStandardOre1(
         ☃,
         ☃,
         this.chunkProviderSettings.diamondCount,
         this.diamondGen,
         this.chunkProviderSettings.diamondMinHeight,
         this.chunkProviderSettings.diamondMaxHeight
      );
      this.genStandardOre2(
         ☃, ☃, this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread
      );
   }

   protected void genStandardOre1(World var1, Random var2, int var3, WorldGenerator var4, int var5, int var6) {
      if (☃ < ☃) {
         int ☃ = ☃;
         ☃ = ☃;
         ☃ = ☃;
      } else if (☃ == ☃) {
         if (☃ < 255) {
            ☃++;
         } else {
            ☃--;
         }
      }

      for (int ☃ = 0; ☃ < ☃; ☃++) {
         BlockPos ☃x = this.chunkPos.add(☃.nextInt(16), ☃.nextInt(☃ - ☃) + ☃, ☃.nextInt(16));
         ☃.generate(☃, ☃, ☃x);
      }
   }

   protected void genStandardOre2(World var1, Random var2, int var3, WorldGenerator var4, int var5, int var6) {
      for (int ☃ = 0; ☃ < ☃; ☃++) {
         BlockPos ☃x = this.chunkPos.add(☃.nextInt(16), ☃.nextInt(☃) + ☃.nextInt(☃) + ☃ - ☃, ☃.nextInt(16));
         ☃.generate(☃, ☃, ☃x);
      }
   }
}
