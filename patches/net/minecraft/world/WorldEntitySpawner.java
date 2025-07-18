package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.optifine.BlockPosM;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;

public final class WorldEntitySpawner {
   private static final int MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);
   private final Set<ChunkPos> eligibleChunksForSpawning = Sets.newHashSet();
   private Map<Class, EntityLiving> mapSampleEntitiesByClass = new HashMap<>();
   private int lastPlayerChunkX = Integer.MAX_VALUE;
   private int lastPlayerChunkZ = Integer.MAX_VALUE;
   private int countChunkPos;

   public int findChunksForSpawning(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate) {
      if (!spawnHostileMobs && !spawnPeacefulMobs) {
         return 0;
      } else {
         boolean updateEligibleChunks = true;
         EntityPlayer player = null;
         if (worldServerIn.playerEntities.size() == 1) {
            player = (EntityPlayer)worldServerIn.playerEntities.get(0);
            if (this.eligibleChunksForSpawning.size() > 0
               && player != null
               && player.chunkCoordX == this.lastPlayerChunkX
               && player.chunkCoordZ == this.lastPlayerChunkZ) {
               updateEligibleChunks = false;
            }
         }

         if (updateEligibleChunks) {
            this.eligibleChunksForSpawning.clear();
            int i = 0;

            for (EntityPlayer entityplayer : worldServerIn.playerEntities) {
               if (!entityplayer.isSpectator()) {
                  int j = MathHelper.floor(entityplayer.posX / 16.0);
                  int k = MathHelper.floor(entityplayer.posZ / 16.0);
                  int l = 8;

                  for (int i1 = -8; i1 <= 8; i1++) {
                     for (int j1 = -8; j1 <= 8; j1++) {
                        boolean flag = i1 == -8 || i1 == 8 || j1 == -8 || j1 == 8;
                        ChunkPos chunkpos = new ChunkPos(i1 + j, j1 + k);
                        if (!this.eligibleChunksForSpawning.contains(chunkpos)) {
                           i++;
                           if (!flag && worldServerIn.getWorldBorder().contains(chunkpos)) {
                              PlayerChunkMapEntry playerchunkmapentry = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.x, chunkpos.z);
                              if (playerchunkmapentry != null && playerchunkmapentry.isSentToPlayers()) {
                                 this.eligibleChunksForSpawning.add(chunkpos);
                              }
                           }
                        }
                     }
                  }
               }
            }

            this.countChunkPos = i;
            if (player != null) {
               this.lastPlayerChunkX = player.chunkCoordX;
               this.lastPlayerChunkZ = player.chunkCoordZ;
            }
         }

         int j4 = 0;
         BlockPos blockpos1 = worldServerIn.getSpawnPoint();
         BlockPosM blockPosM = new BlockPosM(0, 0, 0);
         MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();

         for (EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
            if ((!enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs)
               && (enumcreaturetype.getPeacefulCreature() || spawnHostileMobs)
               && (!enumcreaturetype.getAnimal() || spawnOnSetTickRate)) {
               int k4 = Reflector.ForgeWorld_countEntities.exists()
                  ? Reflector.callInt(worldServerIn, Reflector.ForgeWorld_countEntities, new Object[]{enumcreaturetype, true})
                  : worldServerIn.countEntities(enumcreaturetype.getCreatureClass());
               int l4 = enumcreaturetype.getMaxNumberOfCreature() * this.countChunkPos / MOB_COUNT_DIV;
               if (k4 <= l4) {
                  Collection<ChunkPos> chunksForSpawning = this.eligibleChunksForSpawning;
                  if (Reflector.ForgeHooksClient.exists()) {
                     ArrayList<ChunkPos> shuffled = Lists.newArrayList(chunksForSpawning);
                     Collections.shuffle(shuffled);
                     chunksForSpawning = shuffled;
                  }

                  label176:
                  for (ChunkPos chunkpos1 : chunksForSpawning) {
                     BlockPos blockpos = getRandomChunkPosition(worldServerIn, chunkpos1.x, chunkpos1.z, blockPosM);
                     int k1 = blockpos.getX();
                     int l1 = blockpos.getY();
                     int i2 = blockpos.getZ();
                     IBlockState iblockstate = worldServerIn.getBlockState(blockpos);
                     if (!iblockstate.l()) {
                        int j2 = 0;

                        for (int k2 = 0; k2 < 3; k2++) {
                           int l2 = k1;
                           int i3 = l1;
                           int j3 = i2;
                           int k3 = 6;
                           SpawnListEntry biome$spawnlistentry = null;
                           IEntityLivingData ientitylivingdata = null;
                           int l3 = MathHelper.ceil(Math.random() * 4.0);

                           for (int i4 = 0; i4 < l3; i4++) {
                              l2 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6);
                              i3 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1);
                              j3 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6);
                              blockpos$mutableblockpos.setPos(l2, i3, j3);
                              float f = l2 + 0.5F;
                              float f1 = j3 + 0.5F;
                              if (!worldServerIn.isAnyPlayerWithinRangeAt(f, i3, f1, 24.0) && blockpos1.distanceSq(f, i3, f1) >= 576.0) {
                                 if (biome$spawnlistentry == null) {
                                    biome$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos$mutableblockpos);
                                    if (biome$spawnlistentry == null) {
                                       break;
                                    }
                                 }

                                 if (worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biome$spawnlistentry, blockpos$mutableblockpos)
                                    && canCreatureTypeSpawnAtLocation(
                                       EntitySpawnPlacementRegistry.getPlacementForEntity(biome$spawnlistentry.entityClass),
                                       worldServerIn,
                                       blockpos$mutableblockpos
                                    )) {
                                    EntityLiving entityliving;
                                    try {
                                       entityliving = this.mapSampleEntitiesByClass.get(biome$spawnlistentry.entityClass);
                                       if (entityliving == null) {
                                          if (Reflector.ForgeBiomeSpawnListEntry_newInstance.exists()) {
                                             entityliving = (EntityLiving)Reflector.call(
                                                biome$spawnlistentry, Reflector.ForgeBiomeSpawnListEntry_newInstance, new Object[]{worldServerIn}
                                             );
                                          } else {
                                             entityliving = (EntityLiving)biome$spawnlistentry.entityClass
                                                .getConstructor(World.class)
                                                .newInstance(worldServerIn);
                                          }

                                          this.mapSampleEntitiesByClass.put(biome$spawnlistentry.entityClass, entityliving);
                                       }
                                    } catch (Exception var40) {
                                       var40.printStackTrace();
                                       return j4;
                                    }

                                    entityliving.setLocationAndAngles(f, i3, f1, worldServerIn.rand.nextFloat() * 360.0F, 0.0F);
                                    boolean canSpawn = Reflector.ForgeEventFactory_canEntitySpawn.exists()
                                       ? ReflectorForge.canEntitySpawn(entityliving, worldServerIn, f, i3, f1)
                                       : entityliving.getCanSpawnHere() && entityliving.isNotColliding();
                                    if (canSpawn) {
                                       this.mapSampleEntitiesByClass.remove(biome$spawnlistentry.entityClass);
                                       if (!ReflectorForge.doSpecialSpawn(entityliving, worldServerIn, f, i3, f1)) {
                                          ientitylivingdata = entityliving.onInitialSpawn(
                                             worldServerIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata
                                          );
                                       }

                                       if (entityliving.isNotColliding()) {
                                          j2++;
                                          worldServerIn.spawnEntity(entityliving);
                                       } else {
                                          entityliving.setDead();
                                       }

                                       int maxSpawnedInChunk = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists()
                                          ? Reflector.callInt(Reflector.ForgeEventFactory_getMaxSpawnPackSize, new Object[]{entityliving})
                                          : entityliving.getMaxSpawnedInChunk();
                                       if (j2 >= maxSpawnedInChunk) {
                                          continue label176;
                                       }
                                    }

                                    j4 += j2;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return j4;
      }
   }

   private static BlockPos getRandomChunkPosition(World worldIn, int x, int z) {
      Chunk chunk = worldIn.getChunk(x, z);
      int i = x * 16 + worldIn.rand.nextInt(16);
      int j = z * 16 + worldIn.rand.nextInt(16);
      int k = MathHelper.roundUp(chunk.getHeight(new BlockPos(i, 0, j)) + 1, 16);
      int l = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
      return new BlockPos(i, l, j);
   }

   private static BlockPosM getRandomChunkPosition(World worldIn, int x, int z, BlockPosM blockPosM) {
      Chunk chunk = worldIn.getChunk(x, z);
      int px = x * 16 + worldIn.rand.nextInt(16);
      int pz = z * 16 + worldIn.rand.nextInt(16);
      int k = MathHelper.roundUp(chunk.getHeightValue(px & 15, pz & 15) + 1, 16);
      int py = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
      blockPosM.setXyz(px, py, pz);
      return blockPosM;
   }

   public static boolean isValidEmptySpawnBlock(IBlockState state) {
      if (state.k()) {
         return false;
      } else if (state.m()) {
         return false;
      } else {
         return state.a().isLiquid() ? false : !BlockRailBase.isRailBlock(state);
      }
   }

   public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos) {
      if (!worldIn.getWorldBorder().contains(pos)) {
         return false;
      } else {
         return spawnPlacementTypeIn == null ? false : spawnPlacementTypeIn.canSpawnAt(worldIn, pos);
      }
   }

   public static boolean canCreatureTypeSpawnBody(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      if (spawnPlacementTypeIn == EntityLiving.SpawnPlacementType.IN_WATER) {
         return iblockstate.a() == Material.WATER && worldIn.getBlockState(pos.down()).a() == Material.WATER && !worldIn.getBlockState(pos.up()).l();
      } else {
         BlockPos blockpos = pos.down();
         IBlockState state = worldIn.getBlockState(blockpos);
         boolean canSpawn = Reflector.ForgeBlock_canCreatureSpawn.exists()
            ? Reflector.callBoolean(state.getBlock(), Reflector.ForgeBlock_canCreatureSpawn, new Object[]{state, worldIn, blockpos, spawnPlacementTypeIn})
            : state.q();
         if (!canSpawn) {
            return false;
         } else {
            Block block = worldIn.getBlockState(blockpos).getBlock();
            boolean flag = block != Blocks.BEDROCK && block != Blocks.BARRIER;
            return flag && isValidEmptySpawnBlock(iblockstate) && isValidEmptySpawnBlock(worldIn.getBlockState(pos.up()));
         }
      }
   }

   public static void performWorldGenSpawning(World worldIn, Biome biomeIn, int p_77191_2_, int p_77191_3_, int p_77191_4_, int p_77191_5_, Random randomIn) {
      List<SpawnListEntry> list = biomeIn.getSpawnableList(EnumCreatureType.CREATURE);
      if (!list.isEmpty()) {
         while (randomIn.nextFloat() < biomeIn.getSpawningChance()) {
            SpawnListEntry biome$spawnlistentry = (SpawnListEntry)WeightedRandom.getRandomItem(worldIn.rand, list);
            int i = biome$spawnlistentry.minGroupCount + randomIn.nextInt(1 + biome$spawnlistentry.maxGroupCount - biome$spawnlistentry.minGroupCount);
            IEntityLivingData ientitylivingdata = null;
            int j = p_77191_2_ + randomIn.nextInt(p_77191_4_);
            int k = p_77191_3_ + randomIn.nextInt(p_77191_5_);
            int l = j;
            int i1 = k;

            for (int j1 = 0; j1 < i; j1++) {
               boolean flag = false;

               for (int k1 = 0; !flag && k1 < 4; k1++) {
                  BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                  if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                     EntityLiving entityliving;
                     try {
                        if (Reflector.ForgeBiomeSpawnListEntry_newInstance.exists()) {
                           entityliving = (EntityLiving)Reflector.call(
                              biome$spawnlistentry, Reflector.ForgeBiomeSpawnListEntry_newInstance, new Object[]{worldIn}
                           );
                        } else {
                           entityliving = (EntityLiving)biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldIn);
                        }
                     } catch (Exception var21) {
                        var21.printStackTrace();
                        continue;
                     }

                     if (Reflector.ForgeEventFactory_canEntitySpawn.exists()) {
                        Object canSpawn = Reflector.call(
                           Reflector.ForgeEventFactory_canEntitySpawn, new Object[]{entityliving, worldIn, j + 0.5F, blockpos.getY(), k + 0.5F, false}
                        );
                        if (canSpawn == ReflectorForge.EVENT_RESULT_DENY) {
                           continue;
                        }
                     }

                     entityliving.setLocationAndAngles(j + 0.5F, blockpos.getY(), k + 0.5F, randomIn.nextFloat() * 360.0F, 0.0F);
                     worldIn.spawnEntity(entityliving);
                     ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                     flag = true;
                  }

                  j += randomIn.nextInt(5) - randomIn.nextInt(5);

                  for (k += randomIn.nextInt(5) - randomIn.nextInt(5);
                     j < p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_;
                     k = i1 + randomIn.nextInt(5) - randomIn.nextInt(5)
                  ) {
                     j = l + randomIn.nextInt(5) - randomIn.nextInt(5);
                  }
               }
            }
         }
      }
   }
}
