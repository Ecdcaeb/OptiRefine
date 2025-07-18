package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenFossils;

public class BiomeSwamp extends Biome {
   protected static final IBlockState WATER_LILY = Blocks.WATERLILY.getDefaultState();

   protected BiomeSwamp(Biome.BiomeProperties var1) {
      super(☃);
      this.decorator.treesPerChunk = 2;
      this.decorator.flowersPerChunk = 1;
      this.decorator.deadBushPerChunk = 1;
      this.decorator.mushroomsPerChunk = 8;
      this.decorator.reedsPerChunk = 10;
      this.decorator.clayPerChunk = 1;
      this.decorator.waterlilyPerChunk = 4;
      this.decorator.sandPatchesPerChunk = 0;
      this.decorator.gravelPatchesPerChunk = 0;
      this.decorator.grassPerChunk = 5;
      this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySlime.class, 1, 1, 1));
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      return SWAMP_FEATURE;
   }

   @Override
   public int getGrassColorAtPos(BlockPos var1) {
      double ☃ = GRASS_COLOR_NOISE.getValue(☃.getX() * 0.0225, ☃.getZ() * 0.0225);
      return ☃ < -0.1 ? 5011004 : 6975545;
   }

   @Override
   public int getFoliageColorAtPos(BlockPos var1) {
      return 6975545;
   }

   @Override
   public BlockFlower.EnumFlowerType pickRandomFlower(Random var1, BlockPos var2) {
      return BlockFlower.EnumFlowerType.BLUE_ORCHID;
   }

   @Override
   public void genTerrainBlocks(World var1, Random var2, ChunkPrimer var3, int var4, int var5, double var6) {
      double ☃ = GRASS_COLOR_NOISE.getValue(☃ * 0.25, ☃ * 0.25);
      if (☃ > 0.0) {
         int ☃x = ☃ & 15;
         int ☃xx = ☃ & 15;

         for (int ☃xxx = 255; ☃xxx >= 0; ☃xxx--) {
            if (☃.getBlockState(☃xx, ☃xxx, ☃x).getMaterial() != Material.AIR) {
               if (☃xxx == 62 && ☃.getBlockState(☃xx, ☃xxx, ☃x).getBlock() != Blocks.WATER) {
                  ☃.setBlockState(☃xx, ☃xxx, ☃x, WATER);
                  if (☃ < 0.12) {
                     ☃.setBlockState(☃xx, ☃xxx + 1, ☃x, WATER_LILY);
                  }
               }
               break;
            }
         }
      }

      this.generateBiomeTerrain(☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      super.decorate(☃, ☃, ☃);
      if (☃.nextInt(64) == 0) {
         new WorldGenFossils().generate(☃, ☃, ☃);
      }
   }
}
