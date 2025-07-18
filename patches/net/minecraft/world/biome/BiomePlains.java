package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomePlains extends Biome {
   protected boolean sunflowers;

   protected BiomePlains(boolean var1, Biome.BiomeProperties var2) {
      super(☃);
      this.sunflowers = ☃;
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityHorse.class, 5, 2, 6));
      this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityDonkey.class, 1, 1, 3));
      this.decorator.treesPerChunk = 0;
      this.decorator.extraTreeChance = 0.05F;
      this.decorator.flowersPerChunk = 4;
      this.decorator.grassPerChunk = 10;
   }

   @Override
   public BlockFlower.EnumFlowerType pickRandomFlower(Random var1, BlockPos var2) {
      double ☃ = GRASS_COLOR_NOISE.getValue(☃.getX() / 200.0, ☃.getZ() / 200.0);
      if (☃ < -0.8) {
         int ☃x = ☃.nextInt(4);
         switch (☃x) {
            case 0:
               return BlockFlower.EnumFlowerType.ORANGE_TULIP;
            case 1:
               return BlockFlower.EnumFlowerType.RED_TULIP;
            case 2:
               return BlockFlower.EnumFlowerType.PINK_TULIP;
            case 3:
            default:
               return BlockFlower.EnumFlowerType.WHITE_TULIP;
         }
      } else if (☃.nextInt(3) > 0) {
         int ☃x = ☃.nextInt(3);
         if (☃x == 0) {
            return BlockFlower.EnumFlowerType.POPPY;
         } else {
            return ☃x == 1 ? BlockFlower.EnumFlowerType.HOUSTONIA : BlockFlower.EnumFlowerType.OXEYE_DAISY;
         }
      } else {
         return BlockFlower.EnumFlowerType.DANDELION;
      }
   }

   @Override
   public void decorate(World var1, Random var2, BlockPos var3) {
      double ☃ = GRASS_COLOR_NOISE.getValue((☃.getX() + 8) / 200.0, (☃.getZ() + 8) / 200.0);
      if (☃ < -0.8) {
         this.decorator.flowersPerChunk = 15;
         this.decorator.grassPerChunk = 5;
      } else {
         this.decorator.flowersPerChunk = 4;
         this.decorator.grassPerChunk = 10;
         DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);

         for (int ☃x = 0; ☃x < 7; ☃x++) {
            int ☃xx = ☃.nextInt(16) + 8;
            int ☃xxx = ☃.nextInt(16) + 8;
            int ☃xxxx = ☃.nextInt(☃.getHeight(☃.add(☃xx, 0, ☃xxx)).getY() + 32);
            DOUBLE_PLANT_GENERATOR.generate(☃, ☃, ☃.add(☃xx, ☃xxxx, ☃xxx));
         }
      }

      if (this.sunflowers) {
         DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SUNFLOWER);

         for (int ☃x = 0; ☃x < 10; ☃x++) {
            int ☃xx = ☃.nextInt(16) + 8;
            int ☃xxx = ☃.nextInt(16) + 8;
            int ☃xxxx = ☃.nextInt(☃.getHeight(☃.add(☃xx, 0, ☃xxx)).getY() + 32);
            DOUBLE_PLANT_GENERATOR.generate(☃, ☃, ☃.add(☃xx, ☃xxxx, ☃xxx));
         }
      }

      super.decorate(☃, ☃, ☃);
   }

   @Override
   public WorldGenAbstractTree getRandomTreeFeature(Random var1) {
      return (WorldGenAbstractTree)(☃.nextInt(3) == 0 ? BIG_TREE_FEATURE : TREE_FEATURE);
   }
}
