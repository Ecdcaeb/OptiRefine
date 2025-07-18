package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons extends WorldGenerator {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation[] SPAWNERTYPES = new ResourceLocation[]{
      EntityList.getKey(EntitySkeleton.class),
      EntityList.getKey(EntityZombie.class),
      EntityList.getKey(EntityZombie.class),
      EntityList.getKey(EntitySpider.class)
   };

   @Override
   public boolean generate(World var1, Random var2, BlockPos var3) {
      int ☃ = 3;
      int ☃x = ☃.nextInt(2) + 2;
      int ☃xx = -☃x - 1;
      int ☃xxx = ☃x + 1;
      int ☃xxxx = -1;
      int ☃xxxxx = 4;
      int ☃xxxxxx = ☃.nextInt(2) + 2;
      int ☃xxxxxxx = -☃xxxxxx - 1;
      int ☃xxxxxxxx = ☃xxxxxx + 1;
      int ☃xxxxxxxxx = 0;

      for (int ☃xxxxxxxxxx = ☃xx; ☃xxxxxxxxxx <= ☃xxx; ☃xxxxxxxxxx++) {
         for (int ☃xxxxxxxxxxx = -1; ☃xxxxxxxxxxx <= 4; ☃xxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxx = ☃xxxxxxx; ☃xxxxxxxxxxxx <= ☃xxxxxxxx; ☃xxxxxxxxxxxx++) {
               BlockPos ☃xxxxxxxxxxxxx = ☃.add(☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
               Material ☃xxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxx).getMaterial();
               boolean ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.isSolid();
               if (☃xxxxxxxxxxx == -1 && !☃xxxxxxxxxxxxxxx) {
                  return false;
               }

               if (☃xxxxxxxxxxx == 4 && !☃xxxxxxxxxxxxxxx) {
                  return false;
               }

               if ((☃xxxxxxxxxx == ☃xx || ☃xxxxxxxxxx == ☃xxx || ☃xxxxxxxxxxxx == ☃xxxxxxx || ☃xxxxxxxxxxxx == ☃xxxxxxxx)
                  && ☃xxxxxxxxxxx == 0
                  && ☃.isAirBlock(☃xxxxxxxxxxxxx)
                  && ☃.isAirBlock(☃xxxxxxxxxxxxx.up())) {
                  ☃xxxxxxxxx++;
               }
            }
         }
      }

      if (☃xxxxxxxxx >= 1 && ☃xxxxxxxxx <= 5) {
         for (int ☃xxxxxxxxxx = ☃xx; ☃xxxxxxxxxx <= ☃xxx; ☃xxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxx = 3; ☃xxxxxxxxxxx >= -1; ☃xxxxxxxxxxx--) {
               for (int ☃xxxxxxxxxxxx = ☃xxxxxxx; ☃xxxxxxxxxxxx <= ☃xxxxxxxx; ☃xxxxxxxxxxxx++) {
                  BlockPos ☃xxxxxxxxxxxxxxxx = ☃.add(☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
                  if (☃xxxxxxxxxx != ☃xx
                     && ☃xxxxxxxxxxx != -1
                     && ☃xxxxxxxxxxxx != ☃xxxxxxx
                     && ☃xxxxxxxxxx != ☃xxx
                     && ☃xxxxxxxxxxx != 4
                     && ☃xxxxxxxxxxxx != ☃xxxxxxxx) {
                     if (☃.getBlockState(☃xxxxxxxxxxxxxxxx).getBlock() != Blocks.CHEST) {
                        ☃.setBlockToAir(☃xxxxxxxxxxxxxxxx);
                     }
                  } else if (☃xxxxxxxxxxxxxxxx.getY() >= 0 && !☃.getBlockState(☃xxxxxxxxxxxxxxxx.down()).getMaterial().isSolid()) {
                     ☃.setBlockToAir(☃xxxxxxxxxxxxxxxx);
                  } else if (☃.getBlockState(☃xxxxxxxxxxxxxxxx).getMaterial().isSolid() && ☃.getBlockState(☃xxxxxxxxxxxxxxxx).getBlock() != Blocks.CHEST) {
                     if (☃xxxxxxxxxxx == -1 && ☃.nextInt(4) != 0) {
                        ☃.setBlockState(☃xxxxxxxxxxxxxxxx, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 2);
                     } else {
                        ☃.setBlockState(☃xxxxxxxxxxxxxxxx, Blocks.COBBLESTONE.getDefaultState(), 2);
                     }
                  }
               }
            }
         }

         for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < 2; ☃xxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < 3; ☃xxxxxxxxxxx++) {
               int ☃xxxxxxxxxxxxxxxx = ☃.getX() + ☃.nextInt(☃x * 2 + 1) - ☃x;
               int ☃xxxxxxxxxxxxxxxxx = ☃.getY();
               int ☃xxxxxxxxxxxxxxxxxx = ☃.getZ() + ☃.nextInt(☃xxxxxx * 2 + 1) - ☃xxxxxx;
               BlockPos ☃xxxxxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx);
               if (☃.isAirBlock(☃xxxxxxxxxxxxxxxxxxx)) {
                  int ☃xxxxxxxxxxxxxxxxxxxx = 0;

                  for (EnumFacing ☃xxxxxxxxxxxxxxxxxxxxx : EnumFacing.Plane.HORIZONTAL) {
                     if (☃.getBlockState(☃xxxxxxxxxxxxxxxxxxx.offset(☃xxxxxxxxxxxxxxxxxxxxx)).getMaterial().isSolid()) {
                        ☃xxxxxxxxxxxxxxxxxxxx++;
                     }
                  }

                  if (☃xxxxxxxxxxxxxxxxxxxx == 1) {
                     ☃.setBlockState(☃xxxxxxxxxxxxxxxxxxx, Blocks.CHEST.correctFacing(☃, ☃xxxxxxxxxxxxxxxxxxx, Blocks.CHEST.getDefaultState()), 2);
                     TileEntity ☃xxxxxxxxxxxxxxxxxxxxxx = ☃.getTileEntity(☃xxxxxxxxxxxxxxxxxxx);
                     if (☃xxxxxxxxxxxxxxxxxxxxxx instanceof TileEntityChest) {
                        ((TileEntityChest)☃xxxxxxxxxxxxxxxxxxxxxx).setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, ☃.nextLong());
                     }
                     break;
                  }
               }
            }
         }

         ☃.setBlockState(☃, Blocks.MOB_SPAWNER.getDefaultState(), 2);
         TileEntity ☃xxxxxxxxxx = ☃.getTileEntity(☃);
         if (☃xxxxxxxxxx instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner)☃xxxxxxxxxx).getSpawnerBaseLogic().setEntityId(this.pickMobSpawner(☃));
         } else {
            LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", ☃.getX(), ☃.getY(), ☃.getZ());
         }

         return true;
      } else {
         return false;
      }
   }

   private ResourceLocation pickMobSpawner(Random var1) {
      return SPAWNERTYPES[☃.nextInt(SPAWNERTYPES.length)];
   }
}
