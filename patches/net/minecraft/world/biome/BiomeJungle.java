package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeJungle extends Biome {
   private final boolean isEdge;
   private static final IBlockState JUNGLE_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
   private static final IBlockState JUNGLE_LEAF = Blocks.LEAVES
      .getDefaultState()
      .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)
      .withProperty(BlockLeaves.CHECK_DECAY, false);
   private static final IBlockState OAK_LEAF = Blocks.LEAVES
      .getDefaultState()
      .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
      .withProperty(BlockLeaves.CHECK_DECAY, false);

   public BiomeJungle(boolean var1, Biome.BiomeProperties var2) {
      super(☃);
      this.isEdge = ☃;
      if (☃) {
         this.decorator.treesPerChunk = 2;
      } else {
         this.decorator.treesPerChunk = 50;
      }

      this.decorator.grassPerChunk = 25;
      this.decorator.flowersPerChunk = 4;
      if (!☃) {
         this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityOcelot.class, 2, 1, 1));
      }

      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityParrot.class, 40, 1, 2));
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      if (☃.nextInt(10) == 0) {
         return BIG_TREE_FEATURE;
      } else if (☃.nextInt(2) == 0) {
         return new WorldGenShrub(JUNGLE_LOG, OAK_LEAF);
      } else {
         return (WorldGenAbstractTree)(!this.isEdge && ☃.nextInt(3) == 0
            ? new WorldGenMegaJungle(false, 10, 20, JUNGLE_LOG, JUNGLE_LEAF)
            : new WorldGenTrees(false, 4 + ☃.nextInt(7), JUNGLE_LOG, JUNGLE_LEAF, true));
      }
   }

   @Override
   public WorldGenerator getRandomWorldGenForGrass(Random var1) {
      return ☃.nextInt(4) == 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      super.decorate(☃, ☃, ☃);
      int ☃ = ☃.nextInt(16) + 8;
      int ☃x = ☃.nextInt(16) + 8;
      int ☃xx = ☃.nextInt(☃.getHeight(☃.add(☃, 0, ☃x)).getY() * 2);
      new WorldGenMelon().generate(☃, ☃, ☃.add(☃, ☃xx, ☃x));
      WorldGenVines ☃xxx = new WorldGenVines();

      for (int ☃xxxx = 0; ☃xxxx < 50; ☃xxxx++) {
         ☃xx = ☃.nextInt(16) + 8;
         int ☃xxxxx = 128;
         int ☃xxxxxx = ☃.nextInt(16) + 8;
         ☃xxx.generate(☃, ☃, ☃.add(☃xx, 128, ☃xxxxxx));
      }
   }
}
