package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeSavannaMutated extends BiomeSavanna {
   public BiomeSavannaMutated(Biome.BiomeProperties var1) {
      super(☃);
      this.decorator.treesPerChunk = 2;
      this.decorator.flowersPerChunk = 2;
      this.decorator.grassPerChunk = 5;
   }

   @Override
   public void genTerrainBlocks(World var1, Random var2, ChunkPrimer var3, int var4, int var5, double var6) {
      this.topBlock = Blocks.GRASS.getDefaultState();
      this.fillerBlock = Blocks.DIRT.getDefaultState();
      if (☃ > 1.75) {
         this.topBlock = Blocks.STONE.getDefaultState();
         this.fillerBlock = Blocks.STONE.getDefaultState();
      } else if (☃ > -0.5) {
         this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
      }

      this.generateBiomeTerrain(☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      this.decorator.decorate(☃, ☃, this, ☃);
   }
}
