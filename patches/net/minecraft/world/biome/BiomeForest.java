package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;

public class BiomeForest extends Biome {
   protected static final WorldGenBirchTree SUPER_BIRCH_TREE = new WorldGenBirchTree(false, true);
   protected static final WorldGenBirchTree BIRCH_TREE = new WorldGenBirchTree(false, false);
   protected static final WorldGenCanopyTree ROOF_TREE = new WorldGenCanopyTree(false);
   private final BiomeForest.Type type;

   public BiomeForest(BiomeForest.Type var1, Biome.BiomeProperties var2) {
      super(☃);
      this.type = ☃;
      this.decorator.treesPerChunk = 10;
      this.decorator.grassPerChunk = 2;
      if (this.type == BiomeForest.Type.FLOWER) {
         this.decorator.treesPerChunk = 6;
         this.decorator.flowersPerChunk = 100;
         this.decorator.grassPerChunk = 1;
         this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
      }

      if (this.type == BiomeForest.Type.NORMAL) {
         this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityWolf.class, 5, 4, 4));
      }

      if (this.type == BiomeForest.Type.ROOFED) {
         this.decorator.treesPerChunk = -999;
      }
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      if (this.type == BiomeForest.Type.ROOFED && ☃.nextInt(3) > 0) {
         return ROOF_TREE;
      } else if (this.type == BiomeForest.Type.BIRCH || ☃.nextInt(5) == 0) {
         return BIRCH_TREE;
      } else {
         return (WorldGenAbstractTree)(☃.nextInt(10) == 0 ? BIG_TREE_FEATURE : TREE_FEATURE);
      }
   }

   @Override
   public BlockFlower.EnumFlowerType pickRandomFlower(Random var1, BlockPos var2) {
      if (this.type == BiomeForest.Type.FLOWER) {
         double ☃ = MathHelper.clamp((1.0 + GRASS_COLOR_NOISE.getValue(☃.getX() / 48.0, ☃.getZ() / 48.0)) / 2.0, 0.0, 0.9999);
         BlockFlower.EnumFlowerType ☃x = BlockFlower.EnumFlowerType.values()[(int)(☃ * BlockFlower.EnumFlowerType.values().length)];
         return ☃x == BlockFlower.EnumFlowerType.BLUE_ORCHID ? BlockFlower.EnumFlowerType.POPPY : ☃x;
      } else {
         return super.pickRandomFlower(☃, ☃);
      }
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      if (this.type == BiomeForest.Type.ROOFED) {
         this.addMushrooms(☃, ☃, ☃);
      }

      int ☃ = ☃.nextInt(5) - 3;
      if (this.type == BiomeForest.Type.FLOWER) {
         ☃ += 2;
      }

      this.addDoublePlants(☃, ☃, ☃, ☃);
      super.decorate(☃, ☃, ☃);
   }

   protected void addMushrooms(World var1, Random var2, BlockPos var3) {
      for (int ☃ = 0; ☃ < 4; ☃++) {
         for (int ☃x = 0; ☃x < 4; ☃x++) {
            int ☃xx = ☃ * 4 + 1 + 8 + ☃.nextInt(3);
            int ☃xxx = ☃x * 4 + 1 + 8 + ☃.nextInt(3);
            BlockPos ☃xxxx = ☃.getHeight(☃.add(☃xx, 0, ☃xxx));
            if (☃.nextInt(20) == 0) {
               WorldGenBigMushroom ☃xxxxx = new WorldGenBigMushroom();
               ☃xxxxx.generate(☃, ☃, ☃xxxx);
            } else {
               WorldGenAbstractTree ☃xxxxx = this.getRandomTreeFeature(☃);
               ☃xxxxx.setDecorationDefaults();
               if (☃xxxxx.generate(☃, ☃, ☃xxxx)) {
                  ☃xxxxx.generateSaplings(☃, ☃, ☃xxxx);
               }
            }
         }
      }
   }

   protected void addDoublePlants(World var1, Random var2, BlockPos var3, int var4) {
      for (int ☃ = 0; ☃ < ☃; ☃++) {
         int ☃x = ☃.nextInt(3);
         if (☃x == 0) {
            DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SYRINGA);
         } else if (☃x == 1) {
            DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.ROSE);
         } else if (☃x == 2) {
            DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.PAEONIA);
         }

         for (int ☃xx = 0; ☃xx < 5; ☃xx++) {
            int ☃xxx = ☃.nextInt(16) + 8;
            int ☃xxxx = ☃.nextInt(16) + 8;
            int ☃xxxxx = ☃.nextInt(☃.getHeight(☃.add(☃xxx, 0, ☃xxxx)).getY() + 32);
            if (DOUBLE_PLANT_GENERATOR.generate(☃, ☃, new BlockPos(☃.getX() + ☃xxx, ☃xxxxx, ☃.getZ() + ☃xxxx))) {
               break;
            }
         }
      }
   }

   @Override
   public Class<? extends Biome> getBiomeClass() {
      return BiomeForest.class;
   }

   @Override
   public int getGrassColorAtPos(BlockPos var1) {
      int ☃ = super.getGrassColorAtPos(☃);
      return this.type == BiomeForest.Type.ROOFED ? (☃ & 16711422) + 2634762 >> 1 : ☃;
   }

   public static enum Type {
      NORMAL,
      FLOWER,
      BIRCH,
      ROOFED;
   }
}
