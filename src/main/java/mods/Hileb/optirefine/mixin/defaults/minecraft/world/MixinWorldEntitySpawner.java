package mods.Hileb.optirefine.mixin.defaults.minecraft.world;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.chunk.Chunk;
import net.optifine.BlockPosM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(WorldEntitySpawner.class)
public abstract class MixinWorldEntitySpawner {
//    @Unique
//    private final Map<Class<?>, EntityLiving> mapSampleEntitiesByClass = new HashMap<>();
//    @Unique
//    private int lastPlayerChunkX = Integer.MAX_VALUE;
//    @Unique
//    private int lastPlayerChunkZ = Integer.MAX_VALUE;
//    @Unique
//    private int countChunkPos;
//
//
//    /**
//     * @author Hileb
//     * @reason TODO
//     */
//    @Overwrite
//    public int findChunksForSpawning(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate) {
//        if (!spawnHostileMobs && !spawnPeacefulMobs) {
//            return 0;
//        }
//        boolean updateEligibleChunks = true;
//        EntityPlayer player = null;
//        if (worldServerIn.playerEntities.size() == 1) {
//            player = worldServerIn.playerEntities.get(0);
//            if (this.eligibleChunksForSpawning.size() > 0 && player != null && player.chunkCoordX == this.lastPlayerChunkX && player.chunkCoordZ == this.lastPlayerChunkZ) {
//                updateEligibleChunks = false;
//            }
//        }
//        if (updateEligibleChunks) {
//            this.eligibleChunksForSpawning.clear();
//            int i = 0;
//            for (EntityPlayer entityplayer : worldServerIn.playerEntities) {
//                if (entityplayer.isSpectator()) continue;
//                int j = MathHelper.floor(entityplayer.posX / 16.0);
//                int k = MathHelper.floor(entityplayer.posZ / 16.0);
//                int l = 8;
//                for (int i1 = -8; i1 <= 8; ++i1) {
//                    for (int j1 = -8; j1 <= 8; ++j1) {
//                        PlayerChunkMapEntry playerchunkmapentry;
//                        boolean flag = i1 == -8 || i1 == 8 || j1 == -8 || j1 == 8;
//                        ChunkPos chunkpos = new ChunkPos(i1 + j, j1 + k);
//                        if (this.eligibleChunksForSpawning.contains(chunkpos)) continue;
//                        ++i;
//                        if (flag || !worldServerIn.getWorldBorder().contains(chunkpos) || (playerchunkmapentry = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.x, chunkpos.z)) == null || !playerchunkmapentry.isSentToPlayers()) continue;
//                        this.eligibleChunksForSpawning.add(chunkpos);
//                    }
//                }
//            }
//            this.countChunkPos = i;
//            if (player != null) {
//                this.lastPlayerChunkX = player.chunkCoordX;
//                this.lastPlayerChunkZ = player.chunkCoordZ;
//            }
//        }
//        int j4 = 0;
//        BlockPos blockpos1 = worldServerIn.getSpawnPoint();
//        BlockPosM blockPosM = new BlockPosM(0, 0, 0);
//        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
//        for (EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
//            int l4;
//            int k4;
//            if (enumcreaturetype.getPeacefulCreature() && !spawnPeacefulMobs || !enumcreaturetype.getPeacefulCreature() && !spawnHostileMobs || enumcreaturetype.getAnimal() && !spawnOnSetTickRate || (k4 = Reflector.ForgeWorld_countEntities.exists() ? Reflector.callInt(worldServerIn, Reflector.ForgeWorld_countEntities, enumcreaturetype, true) : worldServerIn.countEntities(enumcreaturetype.getCreatureClass())) > (l4 = enumcreaturetype.getMaxNumberOfCreature() * this.countChunkPos / MOB_COUNT_DIV)) continue;
//            Collection<ChunkPos> chunksForSpawning = this.eligibleChunksForSpawning;
//            if (Reflector.ForgeHooksClient.exists()) {
//                ArrayList<ChunkPos> shuffled = Lists.newArrayList(chunksForSpawning);
//                Collections.shuffle(shuffled);
//                chunksForSpawning = shuffled;
//            }
//            for (ChunkPos chunkpos1 : chunksForSpawning) {
//                BlockPosM blockpos = WorldEntitySpawner.getRandomChunkPosition((World)worldServerIn, chunkpos1.x, chunkpos1.z, blockPosM);
//                int k1 = blockpos.getX();
//                int l1 = blockpos.getY();
//                int i2 = blockpos.getZ();
//                IBlockState iblockstate = worldServerIn.getBlockState(blockpos);
//                if (iblockstate.l()) continue;
//                int j2 = 0;
//                for (int k2 = 0; k2 < 3; ++k2) {
//                    int l2 = k1;
//                    int i3 = l1;
//                    int j3 = i2;
//                    int k3 = 6;
//                    Biome.SpawnListEntry biome$spawnlistentry = null;
//                    IEntityLivingData ientitylivingdata = null;
//                    int l3 = MathHelper.ceil(Math.random() * 4.0);
//                    for (int i4 = 0; i4 < l3; ++i4) {
//                        boolean canSpawn;
//                        EntityLiving entityliving;
//                        blockpos$mutableblockpos.setPos(l2 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6), i3 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1), j3 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6));
//                        float f = (float)l2 + 0.5f;
//                        float f1 = (float)j3 + 0.5f;
//                        if (worldServerIn.isAnyPlayerWithinRangeAt(f, i3, f1, 24.0) || !(blockpos1.distanceSq(f, i3, f1) >= 576.0)) continue;
//                        if (biome$spawnlistentry == null && (biome$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos$mutableblockpos)) == null) continue block7;
//                        if (!worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biome$spawnlistentry, (BlockPos)blockpos$mutableblockpos) || !WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity((Class)biome$spawnlistentry.entityClass), (World)worldServerIn, (BlockPos)blockpos$mutableblockpos)) continue;
//                        try {
//                            entityliving = this.mapSampleEntitiesByClass.get(biome$spawnlistentry.entityClass);
//                            if (entityliving == null) {
//                                entityliving = Reflector.ForgeBiomeSpawnListEntry_newInstance.exists() ? (EntityLiving)((Object)Reflector.call(biome$spawnlistentry, Reflector.ForgeBiomeSpawnListEntry_newInstance, worldServerIn)) : (EntityLiving)((Object)biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldServerIn));
//                                this.mapSampleEntitiesByClass.put(biome$spawnlistentry.entityClass, entityliving);
//                            }
//                        }
//                        catch (Exception exception) {
//                            exception.printStackTrace();
//                            return j4;
//                        }
//                        entityliving.setLocationAndAngles(f, i3, f1, worldServerIn.rand.nextFloat() * 360.0f, 0.0f);
//                        boolean bl = Reflector.ForgeEventFactory_canEntitySpawn.exists() ? ReflectorForge.canEntitySpawn(entityliving, (World)worldServerIn, f, i3, f1) : (canSpawn = entityliving.getCanSpawnHere() && entityliving.isNotColliding());
//                        if (canSpawn) {
//                            int maxSpawnedInChunk;
//                            this.mapSampleEntitiesByClass.remove(biome$spawnlistentry.entityClass);
//                            if (!ReflectorForge.doSpecialSpawn(entityliving, (World)worldServerIn, f, i3, f1)) {
//                                ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos((Entity)entityliving)), ientitylivingdata);
//                            }
//                            if (entityliving.isNotColliding()) {
//                                ++j2;
//                                worldServerIn.spawnEntity(entityliving);
//                            } else {
//                                entityliving.setDead();
//                            }
//                            int n = maxSpawnedInChunk = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists() ? Reflector.callInt(Reflector.ForgeEventFactory_getMaxSpawnPackSize, new Object[]{entityliving}) : entityliving.getMaxSpawnedInChunk();
//                            if (j2 >= maxSpawnedInChunk) continue block6;
//                        }
//                        j4 += j2;
//                    }
//                }
//            }
//        }
//        return j4;
//    }
//
//    @Unique
//    private static BlockPosM getRandomChunkPosition(World worldIn, int x, int z, BlockPosM blockPosM) {
//        Chunk chunk = worldIn.getChunk(x, z);
//        int px = x * 16 + worldIn.rand.nextInt(16);
//        int pz = z * 16 + worldIn.rand.nextInt(16);
//        int k = MathHelper.roundUp(chunk.getHeightValue(px & 0xF, pz & 0xF) + 1, 16);
//        int py = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
//        blockPosM.setXyz(px, py, pz);
//        return blockPosM;
//    }
    @Unique
    private Map<Class<?>, EntityLiving> mapSampleEntitiesByClass = new HashMap<>();
    @Unique
    private int lastPlayerChunkX = Integer.MAX_VALUE;
    @Unique
    private int lastPlayerChunkZ = Integer.MAX_VALUE;
    @Unique
    private int countChunkPos;

    @Unique
    private static BlockPosM getRandomChunkPosition(World var0, int var1, int var2, BlockPosM var3) {
        Chunk var4 = var0.getChunk(var1, var2);
        int var5 = var1 * 16 + var0.rand.nextInt(16);
        int var6 = var2 * 16 + var0.rand.nextInt(16);
        int var7 = MathHelper.roundUp(var4.getHeightValue(var5 & 15, var6 & 15) + 1, 16);
        int var8 = var0.rand.nextInt(var7 > 0 ? var7 : var4.getTopFilledSegment() + 16 - 1);
        var3.setXyz(var5, var8, var6);
        return var3;
    }


}
/*
--- net/minecraft/world/WorldEntitySpawner.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/world/WorldEntitySpawner.java	Tue Aug 19 14:59:58 2025
@@ -1,10 +1,15 @@
 package net.minecraft.world;

+import com.google.common.collect.Lists;
 import com.google.common.collect.Sets;
+import java.util.ArrayList;
+import java.util.Collections;
+import java.util.HashMap;
 import java.util.List;
+import java.util.Map;
 import java.util.Random;
 import java.util.Set;
 import net.minecraft.block.Block;
 import net.minecraft.block.BlockRailBase;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.state.IBlockState;
@@ -16,180 +21,253 @@
 import net.minecraft.init.Blocks;
 import net.minecraft.server.management.PlayerChunkMapEntry;
 import net.minecraft.util.WeightedRandom;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.ChunkPos;
 import net.minecraft.util.math.MathHelper;
+import net.minecraft.util.math.BlockPos.MutableBlockPos;
 import net.minecraft.world.biome.Biome;
+import net.minecraft.world.biome.Biome.SpawnListEntry;
 import net.minecraft.world.chunk.Chunk;
+import net.optifine.BlockPosM;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;

 public final class WorldEntitySpawner {
    private static final int MOB_COUNT_DIV = (int)Math.pow(17.0, 2.0);
    private final Set<ChunkPos> eligibleChunksForSpawning = Sets.newHashSet();
+   private Map<Class, EntityLiving> mapSampleEntitiesByClass = new HashMap<>();
+   private int lastPlayerChunkX = Integer.MAX_VALUE;
+   private int lastPlayerChunkZ = Integer.MAX_VALUE;
+   private int countChunkPos;

    public int findChunksForSpawning(WorldServer var1, boolean var2, boolean var3, boolean var4) {
       if (!var2 && !var3) {
          return 0;
       } else {
-         this.eligibleChunksForSpawning.clear();
-         int var5 = 0;
+         boolean var5 = true;
+         EntityPlayer var6 = null;
+         if (var1.playerEntities.size() == 1) {
+            var6 = (EntityPlayer)var1.playerEntities.get(0);
+            if (this.eligibleChunksForSpawning.size() > 0
+               && var6 != null
+               && var6.chunkCoordX == this.lastPlayerChunkX
+               && var6.chunkCoordZ == this.lastPlayerChunkZ) {
+               var5 = false;
+            }
+         }

-         for (EntityPlayer var7 : var1.playerEntities) {
-            if (!var7.isSpectator()) {
-               int var8 = MathHelper.floor(var7.posX / 16.0);
-               int var9 = MathHelper.floor(var7.posZ / 16.0);
-               byte var10 = 8;
-
-               for (int var11 = -8; var11 <= 8; var11++) {
-                  for (int var12 = -8; var12 <= 8; var12++) {
-                     boolean var13 = var11 == -8 || var11 == 8 || var12 == -8 || var12 == 8;
-                     ChunkPos var14 = new ChunkPos(var11 + var8, var12 + var9);
-                     if (!this.eligibleChunksForSpawning.contains(var14)) {
-                        var5++;
-                        if (!var13 && var1.getWorldBorder().contains(var14)) {
-                           PlayerChunkMapEntry var15 = var1.getPlayerChunkMap().getEntry(var14.x, var14.z);
-                           if (var15 != null && var15.isSentToPlayers()) {
-                              this.eligibleChunksForSpawning.add(var14);
+         if (var5) {
+            this.eligibleChunksForSpawning.clear();
+            int var7 = 0;
+
+            for (EntityPlayer var9 : var1.playerEntities) {
+               if (!var9.isSpectator()) {
+                  int var10 = MathHelper.floor(var9.posX / 16.0);
+                  int var11 = MathHelper.floor(var9.posZ / 16.0);
+                  byte var12 = 8;
+
+                  for (int var13 = -8; var13 <= 8; var13++) {
+                     for (int var14 = -8; var14 <= 8; var14++) {
+                        boolean var15 = var13 == -8 || var13 == 8 || var14 == -8 || var14 == 8;
+                        ChunkPos var16 = new ChunkPos(var13 + var10, var14 + var11);
+                        if (!this.eligibleChunksForSpawning.contains(var16)) {
+                           var7++;
+                           if (!var15 && var1.getWorldBorder().contains(var16)) {
+                              PlayerChunkMapEntry var17 = var1.getPlayerChunkMap().getEntry(var16.x, var16.z);
+                              if (var17 != null && var17.isSentToPlayers()) {
+                                 this.eligibleChunksForSpawning.add(var16);
+                              }
                            }
                         }
                      }
                   }
                }
             }
+
+            this.countChunkPos = var7;
+            if (var6 != null) {
+               this.lastPlayerChunkX = var6.chunkCoordX;
+               this.lastPlayerChunkZ = var6.chunkCoordZ;
+            }
          }

-         int var37 = 0;
-         BlockPos var38 = var1.getSpawnPoint();
+         int var41 = 0;
+         BlockPos var42 = var1.getSpawnPoint();
+         BlockPosM var43 = new BlockPosM(0, 0, 0);
+         MutableBlockPos var44 = new MutableBlockPos();
+
+         for (EnumCreatureType var48 : EnumCreatureType.values()) {
+            if ((!var48.getPeacefulCreature() || var3) && (var48.getPeacefulCreature() || var2) && (!var48.getAnimal() || var4)) {
+               int var49 = Reflector.ForgeWorld_countEntities.exists()
+                  ? Reflector.callInt(var1, Reflector.ForgeWorld_countEntities, new Object[]{var48, true})
+                  : var1.countEntities(var48.getCreatureClass());
+               int var50 = var48.getMaxNumberOfCreature() * this.countChunkPos / MOB_COUNT_DIV;
+               if (var49 <= var50) {
+                  Object var51 = this.eligibleChunksForSpawning;
+                  if (Reflector.ForgeHooksClient.exists()) {
+                     ArrayList var18 = Lists.newArrayList((Iterable)var51);
+                     Collections.shuffle(var18);
+                     var51 = var18;
+                  }

-         for (EnumCreatureType var42 : EnumCreatureType.values()) {
-            if ((!var42.getPeacefulCreature() || var3) && (var42.getPeacefulCreature() || var2) && (!var42.getAnimal() || var4)) {
-               int var43 = var1.countEntities(var42.getCreatureClass());
-               int var44 = var42.getMaxNumberOfCreature() * var5 / MOB_COUNT_DIV;
-               if (var43 <= var44) {
-                  BlockPos.MutableBlockPos var45 = new BlockPos.MutableBlockPos();
-
-                  label134:
-                  for (ChunkPos var16 : this.eligibleChunksForSpawning) {
-                     BlockPos var17 = getRandomChunkPosition(var1, var16.x, var16.z);
-                     int var18 = var17.getX();
-                     int var19 = var17.getY();
-                     int var20 = var17.getZ();
-                     IBlockState var21 = var1.getBlockState(var17);
-                     if (!var21.isNormalCube()) {
-                        int var22 = 0;
-
-                        for (int var23 = 0; var23 < 3; var23++) {
-                           int var24 = var18;
-                           int var25 = var19;
-                           int var26 = var20;
-                           byte var27 = 6;
-                           Biome.SpawnListEntry var28 = null;
-                           IEntityLivingData var29 = null;
-                           int var30 = MathHelper.ceil(Math.random() * 4.0);
-
-                           for (int var31 = 0; var31 < var30; var31++) {
-                              var24 += var1.rand.nextInt(6) - var1.rand.nextInt(6);
-                              var25 += var1.rand.nextInt(1) - var1.rand.nextInt(1);
-                              var26 += var1.rand.nextInt(6) - var1.rand.nextInt(6);
-                              var45.setPos(var24, var25, var26);
-                              float var32 = var24 + 0.5F;
-                              float var33 = var26 + 0.5F;
-                              if (!var1.isAnyPlayerWithinRangeAt(var32, var25, var33, 24.0) && !(var38.distanceSq(var32, var25, var33) < 576.0)) {
-                                 if (var28 == null) {
-                                    var28 = var1.getSpawnListEntryForTypeAt(var42, var45);
-                                    if (var28 == null) {
+                  label176:
+                  for (ChunkPos var19 : var51) {
+                     BlockPosM var20 = getRandomChunkPosition(var1, var19.x, var19.z, var43);
+                     int var21 = var20.getX();
+                     int var22 = var20.getY();
+                     int var23 = var20.getZ();
+                     IBlockState var24 = var1.getBlockState(var20);
+                     if (!var24.l()) {
+                        int var25 = 0;
+
+                        for (int var26 = 0; var26 < 3; var26++) {
+                           int var27 = var21;
+                           int var28 = var22;
+                           int var29 = var23;
+                           byte var30 = 6;
+                           SpawnListEntry var31 = null;
+                           IEntityLivingData var32 = null;
+                           int var33 = MathHelper.ceil(Math.random() * 4.0);
+
+                           for (int var34 = 0; var34 < var33; var34++) {
+                              var27 += var1.rand.nextInt(6) - var1.rand.nextInt(6);
+                              var28 += var1.rand.nextInt(1) - var1.rand.nextInt(1);
+                              var29 += var1.rand.nextInt(6) - var1.rand.nextInt(6);
+                              var44.setPos(var27, var28, var29);
+                              float var35 = var27 + 0.5F;
+                              float var36 = var29 + 0.5F;
+                              if (!var1.isAnyPlayerWithinRangeAt(var35, var28, var36, 24.0) && var42.distanceSq(var35, var28, var36) >= 576.0) {
+                                 if (var31 == null) {
+                                    var31 = var1.getSpawnListEntryForTypeAt(var48, var44);
+                                    if (var31 == null) {
                                        break;
                                     }
                                  }

-                                 if (var1.canCreatureTypeSpawnHere(var42, var28, var45)
-                                    && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(var28.entityClass), var1, var45)) {
-                                    EntityLiving var34;
+                                 if (var1.canCreatureTypeSpawnHere(var48, var31, var44)
+                                    && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(var31.entityClass), var1, var44)) {
+                                    EntityLiving var37;
                                     try {
-                                       var34 = var28.entityClass.getConstructor(World.class).newInstance(var1);
-                                    } catch (Exception var36) {
-                                       var36.printStackTrace();
-                                       return var37;
+                                       var37 = this.mapSampleEntitiesByClass.get(var31.entityClass);
+                                       if (var37 == null) {
+                                          if (Reflector.ForgeBiomeSpawnListEntry_newInstance.exists()) {
+                                             var37 = (EntityLiving)Reflector.call(var31, Reflector.ForgeBiomeSpawnListEntry_newInstance, new Object[]{var1});
+                                          } else {
+                                             var37 = (EntityLiving)var31.entityClass.getConstructor(World.class).newInstance(var1);
+                                          }
+
+                                          this.mapSampleEntitiesByClass.put(var31.entityClass, var37);
+                                       }
+                                    } catch (Exception var40) {
+                                       var40.printStackTrace();
+                                       return var41;
                                     }

-                                    var34.setLocationAndAngles(var32, var25, var33, var1.rand.nextFloat() * 360.0F, 0.0F);
-                                    if (var34.getCanSpawnHere() && var34.isNotColliding()) {
-                                       var29 = var34.onInitialSpawn(var1.getDifficultyForLocation(new BlockPos(var34)), var29);
-                                       if (var34.isNotColliding()) {
-                                          var22++;
-                                          var1.spawnEntity(var34);
+                                    var37.setLocationAndAngles(var35, var28, var36, var1.rand.nextFloat() * 360.0F, 0.0F);
+                                    boolean var38 = Reflector.ForgeEventFactory_canEntitySpawn.exists()
+                                       ? ReflectorForge.canEntitySpawn(var37, var1, var35, var28, var36)
+                                       : var37.getCanSpawnHere() && var37.isNotColliding();
+                                    if (var38) {
+                                       this.mapSampleEntitiesByClass.remove(var31.entityClass);
+                                       if (!ReflectorForge.doSpecialSpawn(var37, var1, var35, var28, var36)) {
+                                          var32 = var37.onInitialSpawn(var1.getDifficultyForLocation(new BlockPos(var37)), var32);
+                                       }
+
+                                       if (var37.isNotColliding()) {
+                                          var25++;
+                                          var1.spawnEntity(var37);
                                        } else {
-                                          var34.setDead();
+                                          var37.setDead();
                                        }

-                                       if (var22 >= var34.getMaxSpawnedInChunk()) {
-                                          continue label134;
+                                       int var39 = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists()
+                                          ? Reflector.callInt(Reflector.ForgeEventFactory_getMaxSpawnPackSize, new Object[]{var37})
+                                          : var37.getMaxSpawnedInChunk();
+                                       if (var25 >= var39) {
+                                          continue label176;
                                        }
                                     }

-                                    var37 += var22;
+                                    var41 += var25;
                                  }
                               }
                            }
                         }
                      }
                   }
                }
             }
          }

-         return var37;
+         return var41;
       }
    }

    private static BlockPos getRandomChunkPosition(World var0, int var1, int var2) {
       Chunk var3 = var0.getChunk(var1, var2);
       int var4 = var1 * 16 + var0.rand.nextInt(16);
       int var5 = var2 * 16 + var0.rand.nextInt(16);
       int var6 = MathHelper.roundUp(var3.getHeight(new BlockPos(var4, 0, var5)) + 1, 16);
       int var7 = var0.rand.nextInt(var6 > 0 ? var6 : var3.getTopFilledSegment() + 16 - 1);
       return new BlockPos(var4, var7, var5);
    }

+   private static BlockPosM getRandomChunkPosition(World var0, int var1, int var2, BlockPosM var3) {
+      Chunk var4 = var0.getChunk(var1, var2);
+      int var5 = var1 * 16 + var0.rand.nextInt(16);
+      int var6 = var2 * 16 + var0.rand.nextInt(16);
+      int var7 = MathHelper.roundUp(var4.getHeightValue(var5 & 15, var6 & 15) + 1, 16);
+      int var8 = var0.rand.nextInt(var7 > 0 ? var7 : var4.getTopFilledSegment() + 16 - 1);
+      var3.setXyz(var5, var8, var6);
+      return var3;
+   }
+
    public static boolean isValidEmptySpawnBlock(IBlockState var0) {
-      if (var0.isBlockNormalCube()) {
+      if (var0.k()) {
          return false;
-      } else if (var0.canProvidePower()) {
+      } else if (var0.m()) {
          return false;
       } else {
-         return var0.getMaterial().isLiquid() ? false : !BlockRailBase.isRailBlock(var0);
+         return var0.a().isLiquid() ? false : !BlockRailBase.isRailBlock(var0);
       }
    }

    public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType var0, World var1, BlockPos var2) {
       if (!var1.getWorldBorder().contains(var2)) {
          return false;
       } else {
-         IBlockState var3 = var1.getBlockState(var2);
-         if (var0 == EntityLiving.SpawnPlacementType.IN_WATER) {
-            return var3.getMaterial() == Material.WATER
-               && var1.getBlockState(var2.down()).getMaterial() == Material.WATER
-               && !var1.getBlockState(var2.up()).isNormalCube();
+         return var0 == null ? false : var0.canSpawnAt(var1, var2);
+      }
+   }
+
+   public static boolean canCreatureTypeSpawnBody(EntityLiving.SpawnPlacementType var0, World var1, BlockPos var2) {
+      IBlockState var3 = var1.getBlockState(var2);
+      if (var0 == EntityLiving.SpawnPlacementType.IN_WATER) {
+         return var3.a() == Material.WATER && var1.getBlockState(var2.down()).a() == Material.WATER && !var1.getBlockState(var2.up()).l();
+      } else {
+         BlockPos var4 = var2.down();
+         IBlockState var5 = var1.getBlockState(var4);
+         boolean var6 = Reflector.ForgeBlock_canCreatureSpawn.exists()
+            ? Reflector.callBoolean(var5.getBlock(), Reflector.ForgeBlock_canCreatureSpawn, new Object[]{var5, var1, var4, var0})
+            : var5.q();
+         if (!var6) {
+            return false;
          } else {
-            BlockPos var4 = var2.down();
-            if (!var1.getBlockState(var4).isTopSolid()) {
-               return false;
-            } else {
-               Block var5 = var1.getBlockState(var4).getBlock();
-               boolean var6 = var5 != Blocks.BEDROCK && var5 != Blocks.BARRIER;
-               return var6 && isValidEmptySpawnBlock(var3) && isValidEmptySpawnBlock(var1.getBlockState(var2.up()));
-            }
+            Block var7 = var1.getBlockState(var4).getBlock();
+            boolean var8 = var7 != Blocks.BEDROCK && var7 != Blocks.BARRIER;
+            return var8 && isValidEmptySpawnBlock(var3) && isValidEmptySpawnBlock(var1.getBlockState(var2.up()));
          }
       }
    }

    public static void performWorldGenSpawning(World var0, Biome var1, int var2, int var3, int var4, int var5, Random var6) {
       List var7 = var1.getSpawnableList(EnumCreatureType.CREATURE);
       if (!var7.isEmpty()) {
          while (var6.nextFloat() < var1.getSpawningChance()) {
-            Biome.SpawnListEntry var8 = WeightedRandom.getRandomItem(var0.rand, var7);
+            SpawnListEntry var8 = (SpawnListEntry)WeightedRandom.getRandomItem(var0.rand, var7);
             int var9 = var8.minGroupCount + var6.nextInt(1 + var8.maxGroupCount - var8.minGroupCount);
             IEntityLivingData var10 = null;
             int var11 = var2 + var6.nextInt(var4);
             int var12 = var3 + var6.nextInt(var5);
             int var13 = var11;
             int var14 = var12;
@@ -199,16 +277,29 @@

                for (int var17 = 0; !var16 && var17 < 4; var17++) {
                   BlockPos var18 = var0.getTopSolidOrLiquidBlock(new BlockPos(var11, 0, var12));
                   if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, var0, var18)) {
                      EntityLiving var19;
                      try {
-                        var19 = var8.entityClass.getConstructor(World.class).newInstance(var0);
+                        if (Reflector.ForgeBiomeSpawnListEntry_newInstance.exists()) {
+                           var19 = (EntityLiving)Reflector.call(var8, Reflector.ForgeBiomeSpawnListEntry_newInstance, new Object[]{var0});
+                        } else {
+                           var19 = (EntityLiving)var8.entityClass.getConstructor(World.class).newInstance(var0);
+                        }
                      } catch (Exception var21) {
                         var21.printStackTrace();
                         continue;
+                     }
+
+                     if (Reflector.ForgeEventFactory_canEntitySpawn.exists()) {
+                        Object var20 = Reflector.call(
+                           Reflector.ForgeEventFactory_canEntitySpawn, new Object[]{var19, var0, var11 + 0.5F, var18.getY(), var12 + 0.5F, false}
+                        );
+                        if (var20 == ReflectorForge.EVENT_RESULT_DENY) {
+                           continue;
+                        }
                      }

                      var19.setLocationAndAngles(var11 + 0.5F, var18.getY(), var12 + 0.5F, var6.nextFloat() * 360.0F, 0.0F);
                      var0.spawnEntity(var19);
                      var10 = var19.onInitialSpawn(var0.getDifficultyForLocation(new BlockPos(var19)), var10);
                      var16 = true;
 */
