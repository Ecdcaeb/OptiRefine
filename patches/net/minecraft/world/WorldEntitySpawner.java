package net.minecraft.world;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public final class WorldEntitySpawner {
   private static final int MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);
   private final Set<ChunkPos> eligibleChunksForSpawning = Sets.newHashSet();

   public int findChunksForSpawning(WorldServer var1, boolean var2, boolean var3, boolean var4) {
      if (!☃ && !☃) {
         return 0;
      } else {
         this.eligibleChunksForSpawning.clear();
         int ☃ = 0;

         for (EntityPlayer ☃x : ☃.playerEntities) {
            if (!☃x.isSpectator()) {
               int ☃xx = MathHelper.floor(☃x.posX / 16.0);
               int ☃xxx = MathHelper.floor(☃x.posZ / 16.0);
               int ☃xxxx = 8;

               for (int ☃xxxxx = -8; ☃xxxxx <= 8; ☃xxxxx++) {
                  for (int ☃xxxxxx = -8; ☃xxxxxx <= 8; ☃xxxxxx++) {
                     boolean ☃xxxxxxx = ☃xxxxx == -8 || ☃xxxxx == 8 || ☃xxxxxx == -8 || ☃xxxxxx == 8;
                     ChunkPos ☃xxxxxxxx = new ChunkPos(☃xxxxx + ☃xx, ☃xxxxxx + ☃xxx);
                     if (!this.eligibleChunksForSpawning.contains(☃xxxxxxxx)) {
                        ☃++;
                        if (!☃xxxxxxx && ☃.getWorldBorder().contains(☃xxxxxxxx)) {
                           PlayerChunkMapEntry ☃xxxxxxxxx = ☃.getPlayerChunkMap().getEntry(☃xxxxxxxx.x, ☃xxxxxxxx.z);
                           if (☃xxxxxxxxx != null && ☃xxxxxxxxx.isSentToPlayers()) {
                              this.eligibleChunksForSpawning.add(☃xxxxxxxx);
                           }
                        }
                     }
                  }
               }
            }
         }

         int ☃xx = 0;
         BlockPos ☃xxx = ☃.getSpawnPoint();

         for (EnumCreatureType ☃xxxx : EnumCreatureType.values()) {
            if ((!☃xxxx.getPeacefulCreature() || ☃) && (☃xxxx.getPeacefulCreature() || ☃) && (!☃xxxx.getAnimal() || ☃)) {
               int ☃xxxxx = ☃.countEntities(☃xxxx.getCreatureClass());
               int ☃xxxxxxx = ☃xxxx.getMaxNumberOfCreature() * ☃ / MOB_COUNT_DIV;
               if (☃xxxxx <= ☃xxxxxxx) {
                  BlockPos.MutableBlockPos ☃xxxxxxxx = new BlockPos.MutableBlockPos();

                  label134:
                  for (ChunkPos ☃xxxxxxxxx : this.eligibleChunksForSpawning) {
                     BlockPos ☃xxxxxxxxxx = getRandomChunkPosition(☃, ☃xxxxxxxxx.x, ☃xxxxxxxxx.z);
                     int ☃xxxxxxxxxxx = ☃xxxxxxxxxx.getX();
                     int ☃xxxxxxxxxxxx = ☃xxxxxxxxxx.getY();
                     int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx.getZ();
                     IBlockState ☃xxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxx);
                     if (!☃xxxxxxxxxxxxxx.isNormalCube()) {
                        int ☃xxxxxxxxxxxxxxx = 0;

                        for (int ☃xxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxx < 3; ☃xxxxxxxxxxxxxxxx++) {
                           int ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx;
                           int ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx;
                           int ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx;
                           int ☃xxxxxxxxxxxxxxxxxxxx = 6;
                           Biome.SpawnListEntry ☃xxxxxxxxxxxxxxxxxxxxx = null;
                           IEntityLivingData ☃xxxxxxxxxxxxxxxxxxxxxx = null;
                           int ☃xxxxxxxxxxxxxxxxxxxxxxx = MathHelper.ceil(Math.random() * 4.0);

                           for (int ☃xxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxxx++) {
                              ☃xxxxxxxxxxxxxxxxx += ☃.rand.nextInt(6) - ☃.rand.nextInt(6);
                              ☃xxxxxxxxxxxxxxxxxx += ☃.rand.nextInt(1) - ☃.rand.nextInt(1);
                              ☃xxxxxxxxxxxxxxxxxxx += ☃.rand.nextInt(6) - ☃.rand.nextInt(6);
                              ☃xxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx);
                              float ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx + 0.5F;
                              float ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx + 0.5F;
                              if (!☃.isAnyPlayerWithinRangeAt(☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, 24.0)
                                 && !(☃xxx.distanceSq(☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx) < 576.0)) {
                                 if (☃xxxxxxxxxxxxxxxxxxxxx == null) {
                                    ☃xxxxxxxxxxxxxxxxxxxxx = ☃.getSpawnListEntryForTypeAt(☃xxxx, ☃xxxxxxxx);
                                    if (☃xxxxxxxxxxxxxxxxxxxxx == null) {
                                       break;
                                    }
                                 }

                                 if (☃.canCreatureTypeSpawnHere(☃xxxx, ☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxx)
                                    && canCreatureTypeSpawnAtLocation(
                                       EntitySpawnPlacementRegistry.getPlacementForEntity(☃xxxxxxxxxxxxxxxxxxxxx.entityClass), ☃, ☃xxxxxxxx
                                    )) {
                                    EntityLiving ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx;
                                    try {
                                       ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx.entityClass.getConstructor(World.class).newInstance(☃);
                                    } catch (Exception var36) {
                                       var36.printStackTrace();
                                       return ☃xx;
                                    }

                                    ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx.setLocationAndAngles(
                                       ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃.rand.nextFloat() * 360.0F, 0.0F
                                    );
                                    if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxx.getCanSpawnHere() && ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx.isNotColliding()) {
                                       ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx.onInitialSpawn(
                                          ☃.getDifficultyForLocation(new BlockPos(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx)), ☃xxxxxxxxxxxxxxxxxxxxxx
                                       );
                                       if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxx.isNotColliding()) {
                                          ☃xxxxxxxxxxxxxxx++;
                                          ☃.spawnEntity(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx);
                                       } else {
                                          ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx.setDead();
                                       }

                                       if (☃xxxxxxxxxxxxxxx >= ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx.getMaxSpawnedInChunk()) {
                                          continue label134;
                                       }
                                    }

                                    ☃xx += ☃xxxxxxxxxxxxxxx;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return ☃xx;
      }
   }

   private static BlockPos getRandomChunkPosition(World var0, int var1, int var2) {
      Chunk ☃ = ☃.getChunk(☃, ☃);
      int ☃x = ☃ * 16 + ☃.rand.nextInt(16);
      int ☃xx = ☃ * 16 + ☃.rand.nextInt(16);
      int ☃xxx = MathHelper.roundUp(☃.getHeight(new BlockPos(☃x, 0, ☃xx)) + 1, 16);
      int ☃xxxx = ☃.rand.nextInt(☃xxx > 0 ? ☃xxx : ☃.getTopFilledSegment() + 16 - 1);
      return new BlockPos(☃x, ☃xxxx, ☃xx);
   }

   public static boolean isValidEmptySpawnBlock(IBlockState var0) {
      if (☃.isBlockNormalCube()) {
         return false;
      } else if (☃.canProvidePower()) {
         return false;
      } else {
         return ☃.getMaterial().isLiquid() ? false : !BlockRailBase.isRailBlock(☃);
      }
   }

   public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType var0, World var1, BlockPos var2) {
      if (!☃.getWorldBorder().contains(☃)) {
         return false;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃);
         if (☃ == EntityLiving.SpawnPlacementType.IN_WATER) {
            return ☃.getMaterial() == Material.WATER && ☃.getBlockState(☃.down()).getMaterial() == Material.WATER && !☃.getBlockState(☃.up()).isNormalCube();
         } else {
            BlockPos ☃x = ☃.down();
            if (!☃.getBlockState(☃x).isTopSolid()) {
               return false;
            } else {
               Block ☃xx = ☃.getBlockState(☃x).getBlock();
               boolean ☃xxx = ☃xx != Blocks.BEDROCK && ☃xx != Blocks.BARRIER;
               return ☃xxx && isValidEmptySpawnBlock(☃) && isValidEmptySpawnBlock(☃.getBlockState(☃.up()));
            }
         }
      }
   }

   public static void performWorldGenSpawning(World var0, Biome var1, int var2, int var3, int var4, int var5, Random var6) {
      List<Biome.SpawnListEntry> ☃ = ☃.getSpawnableList(EnumCreatureType.CREATURE);
      if (!☃.isEmpty()) {
         while (☃.nextFloat() < ☃.getSpawningChance()) {
            Biome.SpawnListEntry ☃x = WeightedRandom.getRandomItem(☃.rand, ☃);
            int ☃xx = ☃x.minGroupCount + ☃.nextInt(1 + ☃x.maxGroupCount - ☃x.minGroupCount);
            IEntityLivingData ☃xxx = null;
            int ☃xxxx = ☃ + ☃.nextInt(☃);
            int ☃xxxxx = ☃ + ☃.nextInt(☃);
            int ☃xxxxxx = ☃xxxx;
            int ☃xxxxxxx = ☃xxxxx;

            for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xx; ☃xxxxxxxx++) {
               boolean ☃xxxxxxxxx = false;

               for (int ☃xxxxxxxxxx = 0; !☃xxxxxxxxx && ☃xxxxxxxxxx < 4; ☃xxxxxxxxxx++) {
                  BlockPos ☃xxxxxxxxxxx = ☃.getTopSolidOrLiquidBlock(new BlockPos(☃xxxx, 0, ☃xxxxx));
                  if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, ☃, ☃xxxxxxxxxxx)) {
                     EntityLiving ☃xxxxxxxxxxxx;
                     try {
                        ☃xxxxxxxxxxxx = ☃x.entityClass.getConstructor(World.class).newInstance(☃);
                     } catch (Exception var21) {
                        var21.printStackTrace();
                        continue;
                     }

                     ☃xxxxxxxxxxxx.setLocationAndAngles(☃xxxx + 0.5F, ☃xxxxxxxxxxx.getY(), ☃xxxxx + 0.5F, ☃.nextFloat() * 360.0F, 0.0F);
                     ☃.spawnEntity(☃xxxxxxxxxxxx);
                     ☃xxx = ☃xxxxxxxxxxxx.onInitialSpawn(☃.getDifficultyForLocation(new BlockPos(☃xxxxxxxxxxxx)), ☃xxx);
                     ☃xxxxxxxxx = true;
                  }

                  ☃xxxx += ☃.nextInt(5) - ☃.nextInt(5);

                  for (☃xxxxx += ☃.nextInt(5) - ☃.nextInt(5);
                     ☃xxxx < ☃ || ☃xxxx >= ☃ + ☃ || ☃xxxxx < ☃ || ☃xxxxx >= ☃ + ☃;
                     ☃xxxxx = ☃xxxxxxx + ☃.nextInt(5) - ☃.nextInt(5)
                  ) {
                     ☃xxxx = ☃xxxxxx + ☃.nextInt(5) - ☃.nextInt(5);
                  }
               }
            }
         }
      }
   }
}
