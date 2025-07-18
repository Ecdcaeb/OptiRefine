package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class WorldGeneratorBonusChest extends WorldGenerator {
   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for (IBlockState ☃ = ☃.getBlockState(☃); (☃.getMaterial() == Material.AIR || ☃.getMaterial() == Material.LEAVES) && ☃.getY() > 1; ☃ = ☃.getBlockState(☃)) {
         ☃ = ☃.down();
      }

      if (☃.getY() < 1) {
         return false;
      } else {
         ☃ = ☃.up();

         for (int ☃ = 0; ☃ < 4; ☃++) {
            BlockPos ☃x = ☃.add(☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(3) - ☃.nextInt(3), ☃.nextInt(4) - ☃.nextInt(4));
            if (☃.isAirBlock(☃x) && ☃.getBlockState(☃x.down()).isTopSolid()) {
               ☃.setBlockState(☃x, Blocks.CHEST.getDefaultState(), 2);
               TileEntity ☃xx = ☃.getTileEntity(☃x);
               if (☃xx instanceof TileEntityChest) {
                  ((TileEntityChest)☃xx).setLootTable(LootTableList.CHESTS_SPAWN_BONUS_CHEST, ☃.nextLong());
               }

               BlockPos ☃xxx = ☃x.east();
               BlockPos ☃xxxx = ☃x.west();
               BlockPos ☃xxxxx = ☃x.north();
               BlockPos ☃xxxxxx = ☃x.south();
               if (☃.isAirBlock(☃xxxx) && ☃.getBlockState(☃xxxx.down()).isTopSolid()) {
                  ☃.setBlockState(☃xxxx, Blocks.TORCH.getDefaultState(), 2);
               }

               if (☃.isAirBlock(☃xxx) && ☃.getBlockState(☃xxx.down()).isTopSolid()) {
                  ☃.setBlockState(☃xxx, Blocks.TORCH.getDefaultState(), 2);
               }

               if (☃.isAirBlock(☃xxxxx) && ☃.getBlockState(☃xxxxx.down()).isTopSolid()) {
                  ☃.setBlockState(☃xxxxx, Blocks.TORCH.getDefaultState(), 2);
               }

               if (☃.isAirBlock(☃xxxxxx) && ☃.getBlockState(☃xxxxxx.down()).isTopSolid()) {
                  ☃.setBlockState(☃xxxxxx, Blocks.TORCH.getDefaultState(), 2);
               }

               return true;
            }
         }

         return false;
      }
   }
}
