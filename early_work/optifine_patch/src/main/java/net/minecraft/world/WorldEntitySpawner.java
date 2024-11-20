/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Random
 *  java.util.Set
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockRailBase
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLiving$SpawnPlacementType
 *  net.minecraft.entity.EntitySpawnPlacementRegistry
 *  net.minecraft.entity.EnumCreatureType
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.server.management.PlayerChunkMapEntry
 *  net.minecraft.util.WeightedRandom
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 *  net.minecraft.util.math.ChunkPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldServer
 *  net.minecraft.world.biome.Biome
 *  net.minecraft.world.biome.Biome$SpawnListEntry
 *  net.minecraft.world.chunk.Chunk
 *  net.optifine.BlockPosM
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorForge
 *  net.optifine.reflect.ReflectorMethod
 */
package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
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
import net.minecraft.entity.Entity;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.optifine.BlockPosM;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.reflect.ReflectorMethod;

public final class WorldEntitySpawner {
    private static final int MOB_COUNT_DIV = (int)Math.pow((double)17.0, (double)2.0);
    private final Set<ChunkPos> eligibleChunksForSpawning = Sets.newHashSet();
    private Map<Class, EntityLiving> mapSampleEntitiesByClass = new HashMap();
    private int lastPlayerChunkX = Integer.MAX_VALUE;
    private int lastPlayerChunkZ = Integer.MAX_VALUE;
    private int countChunkPos;

    public int findChunksForSpawning(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate) {
        if (!spawnHostileMobs && !spawnPeacefulMobs) {
            return 0;
        }
        boolean updateEligibleChunks = true;
        EntityPlayer player = null;
        if (worldServerIn.i.size() == 1) {
            player = (EntityPlayer)worldServerIn.i.get(0);
            if (this.eligibleChunksForSpawning.size() > 0 && player != null && player.ab == this.lastPlayerChunkX && player.ad == this.lastPlayerChunkZ) {
                updateEligibleChunks = false;
            }
        }
        if (updateEligibleChunks) {
            this.eligibleChunksForSpawning.clear();
            int i = 0;
            for (EntityPlayer entityplayer : worldServerIn.i) {
                if (entityplayer.isSpectator()) continue;
                int j = MathHelper.floor((double)(entityplayer.p / 16.0));
                int k = MathHelper.floor((double)(entityplayer.r / 16.0));
                int l = 8;
                for (int i1 = -8; i1 <= 8; ++i1) {
                    for (int j1 = -8; j1 <= 8; ++j1) {
                        PlayerChunkMapEntry playerchunkmapentry;
                        boolean flag = i1 == -8 || i1 == 8 || j1 == -8 || j1 == 8;
                        ChunkPos chunkpos = new ChunkPos(i1 + j, j1 + k);
                        if (this.eligibleChunksForSpawning.contains((Object)chunkpos)) continue;
                        ++i;
                        if (flag || !worldServerIn.al().contains(chunkpos) || (playerchunkmapentry = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.x, chunkpos.z)) == null || !playerchunkmapentry.isSentToPlayers()) continue;
                        this.eligibleChunksForSpawning.add((Object)chunkpos);
                    }
                }
            }
            this.countChunkPos = i;
            if (player != null) {
                this.lastPlayerChunkX = player.ab;
                this.lastPlayerChunkZ = player.ad;
            }
        }
        int j4 = 0;
        BlockPos blockpos1 = worldServerIn.T();
        BlockPosM blockPosM = new BlockPosM(0, 0, 0);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
            int l4;
            int k4;
            if (enumcreaturetype.getPeacefulCreature() && !spawnPeacefulMobs || !enumcreaturetype.getPeacefulCreature() && !spawnHostileMobs || enumcreaturetype.getAnimal() && !spawnOnSetTickRate || (k4 = Reflector.ForgeWorld_countEntities.exists() ? Reflector.callInt((Object)worldServerIn, (ReflectorMethod)Reflector.ForgeWorld_countEntities, (Object[])new Object[]{enumcreaturetype, true}) : worldServerIn.a(enumcreaturetype.getCreatureClass())) > (l4 = enumcreaturetype.getMaxNumberOfCreature() * this.countChunkPos / MOB_COUNT_DIV)) continue;
            ArrayList chunksForSpawning = this.eligibleChunksForSpawning;
            if (Reflector.ForgeHooksClient.exists()) {
                ArrayList shuffled = Lists.newArrayList(chunksForSpawning);
                Collections.shuffle((List)shuffled);
                chunksForSpawning = shuffled;
            }
            block6: for (ChunkPos chunkpos1 : chunksForSpawning) {
                BlockPosM blockpos = WorldEntitySpawner.getRandomChunkPosition((World)worldServerIn, chunkpos1.x, chunkpos1.z, blockPosM);
                int k1 = blockpos.p();
                int l1 = blockpos.q();
                int i2 = blockpos.r();
                IBlockState iblockstate = worldServerIn.o((BlockPos)blockpos);
                if (iblockstate.l()) continue;
                int j2 = 0;
                block7: for (int k2 = 0; k2 < 3; ++k2) {
                    int l2 = k1;
                    int i3 = l1;
                    int j3 = i2;
                    int k3 = 6;
                    Biome.SpawnListEntry biome$spawnlistentry = null;
                    IEntityLivingData ientitylivingdata = null;
                    int l3 = MathHelper.ceil((double)(Math.random() * 4.0));
                    for (int i4 = 0; i4 < l3; ++i4) {
                        boolean canSpawn;
                        EntityLiving entityliving;
                        blockpos$mutableblockpos.setPos(l2 += worldServerIn.r.nextInt(6) - worldServerIn.r.nextInt(6), i3 += worldServerIn.r.nextInt(1) - worldServerIn.r.nextInt(1), j3 += worldServerIn.r.nextInt(6) - worldServerIn.r.nextInt(6));
                        float f = (float)l2 + 0.5f;
                        float f1 = (float)j3 + 0.5f;
                        if (worldServerIn.a((double)f, (double)i3, (double)f1, 24.0) || !(blockpos1.f((double)f, (double)i3, (double)f1) >= 576.0)) continue;
                        if (biome$spawnlistentry == null && (biome$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, (BlockPos)blockpos$mutableblockpos)) == null) continue block7;
                        if (!worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biome$spawnlistentry, (BlockPos)blockpos$mutableblockpos) || !WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity((Class)biome$spawnlistentry.entityClass), (World)worldServerIn, (BlockPos)blockpos$mutableblockpos)) continue;
                        try {
                            entityliving = (EntityLiving)this.mapSampleEntitiesByClass.get((Object)biome$spawnlistentry.entityClass);
                            if (entityliving == null) {
                                entityliving = Reflector.ForgeBiomeSpawnListEntry_newInstance.exists() ? (EntityLiving)Reflector.call((Object)biome$spawnlistentry, (ReflectorMethod)Reflector.ForgeBiomeSpawnListEntry_newInstance, (Object[])new Object[]{worldServerIn}) : (EntityLiving)biome$spawnlistentry.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{worldServerIn});
                                this.mapSampleEntitiesByClass.put((Object)biome$spawnlistentry.entityClass, (Object)entityliving);
                            }
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                            return j4;
                        }
                        entityliving.b((double)f, (double)i3, (double)f1, worldServerIn.r.nextFloat() * 360.0f, 0.0f);
                        boolean bl = Reflector.ForgeEventFactory_canEntitySpawn.exists() ? ReflectorForge.canEntitySpawn((EntityLiving)entityliving, (World)worldServerIn, (float)f, (float)i3, (float)f1) : (canSpawn = entityliving.getCanSpawnHere() && entityliving.isNotColliding());
                        if (canSpawn) {
                            int maxSpawnedInChunk;
                            this.mapSampleEntitiesByClass.remove((Object)biome$spawnlistentry.entityClass);
                            if (!ReflectorForge.doSpecialSpawn((EntityLiving)entityliving, (World)worldServerIn, (float)f, (int)i3, (float)f1)) {
                                ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.D(new BlockPos((Entity)entityliving)), ientitylivingdata);
                            }
                            if (entityliving.isNotColliding()) {
                                ++j2;
                                worldServerIn.spawnEntity((Entity)entityliving);
                            } else {
                                entityliving.X();
                            }
                            int n = maxSpawnedInChunk = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists() ? Reflector.callInt((ReflectorMethod)Reflector.ForgeEventFactory_getMaxSpawnPackSize, (Object[])new Object[]{entityliving}) : entityliving.getMaxSpawnedInChunk();
                            if (j2 >= maxSpawnedInChunk) continue block6;
                        }
                        j4 += j2;
                    }
                }
            }
        }
        return j4;
    }

    private static BlockPos getRandomChunkPosition(World worldIn, int x, int z) {
        Chunk chunk = worldIn.getChunk(x, z);
        int i = x * 16 + worldIn.rand.nextInt(16);
        int j = z * 16 + worldIn.rand.nextInt(16);
        int k = MathHelper.roundUp((int)(chunk.getHeight(new BlockPos(i, 0, j)) + 1), (int)16);
        int l = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
        return new BlockPos(i, l, j);
    }

    private static BlockPosM getRandomChunkPosition(World worldIn, int x, int z, BlockPosM blockPosM) {
        Chunk chunk = worldIn.getChunk(x, z);
        int px = x * 16 + worldIn.rand.nextInt(16);
        int pz = z * 16 + worldIn.rand.nextInt(16);
        int k = MathHelper.roundUp((int)(chunk.getHeightValue(px & 0xF, pz & 0xF) + 1), (int)16);
        int py = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
        blockPosM.setXyz(px, py, pz);
        return blockPosM;
    }

    public static boolean isValidEmptySpawnBlock(IBlockState state) {
        if (state.k()) {
            return false;
        }
        if (state.m()) {
            return false;
        }
        if (state.a().isLiquid()) {
            return false;
        }
        return !BlockRailBase.isRailBlock((IBlockState)state);
    }

    public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos) {
        if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        if (spawnPlacementTypeIn == null) {
            return false;
        }
        return spawnPlacementTypeIn.canSpawnAt(worldIn, pos);
    }

    public static boolean canCreatureTypeSpawnBody(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos) {
        boolean canSpawn;
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (spawnPlacementTypeIn == EntityLiving.SpawnPlacementType.IN_WATER) {
            return iblockstate.a() == Material.WATER && worldIn.getBlockState(pos.down()).a() == Material.WATER && !worldIn.getBlockState(pos.up()).l();
        }
        BlockPos blockpos = pos.down();
        IBlockState state = worldIn.getBlockState(blockpos);
        boolean bl = canSpawn = Reflector.ForgeBlock_canCreatureSpawn.exists() ? Reflector.callBoolean((Object)state.getBlock(), (ReflectorMethod)Reflector.ForgeBlock_canCreatureSpawn, (Object[])new Object[]{state, worldIn, blockpos, spawnPlacementTypeIn}) : state.q();
        if (!canSpawn) {
            return false;
        }
        Block block = worldIn.getBlockState(blockpos).getBlock();
        boolean flag = block != Blocks.BEDROCK && block != Blocks.BARRIER;
        return flag && WorldEntitySpawner.isValidEmptySpawnBlock(iblockstate) && WorldEntitySpawner.isValidEmptySpawnBlock(worldIn.getBlockState(pos.up()));
    }

    public static void performWorldGenSpawning(World worldIn, Biome biomeIn, int p_77191_2_, int p_77191_3_, int p_77191_4_, int p_77191_5_, Random randomIn) {
        List list = biomeIn.getSpawnableList(EnumCreatureType.CREATURE);
        if (!list.isEmpty()) {
            while (randomIn.nextFloat() < biomeIn.getSpawningChance()) {
                Biome.SpawnListEntry biome$spawnlistentry = (Biome.SpawnListEntry)WeightedRandom.getRandomItem((Random)worldIn.rand, (List)list);
                int i = biome$spawnlistentry.minGroupCount + randomIn.nextInt(1 + biome$spawnlistentry.maxGroupCount - biome$spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j = p_77191_2_ + randomIn.nextInt(p_77191_4_);
                int k = p_77191_3_ + randomIn.nextInt(p_77191_5_);
                int l = j;
                int i1 = k;
                for (int j1 = 0; j1 < i; ++j1) {
                    boolean flag = false;
                    for (int k1 = 0; !flag && k1 < 4; ++k1) {
                        BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                        if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                            Object canSpawn;
                            EntityLiving entityliving;
                            try {
                                entityliving = Reflector.ForgeBiomeSpawnListEntry_newInstance.exists() ? (EntityLiving)Reflector.call((Object)biome$spawnlistentry, (ReflectorMethod)Reflector.ForgeBiomeSpawnListEntry_newInstance, (Object[])new Object[]{worldIn}) : (EntityLiving)biome$spawnlistentry.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{worldIn});
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                                continue;
                            }
                            if (Reflector.ForgeEventFactory_canEntitySpawn.exists() && (canSpawn = Reflector.call((ReflectorMethod)Reflector.ForgeEventFactory_canEntitySpawn, (Object[])new Object[]{entityliving, worldIn, Float.valueOf((float)((float)j + 0.5f)), blockpos.q(), Float.valueOf((float)((float)k + 0.5f)), false})) == ReflectorForge.EVENT_RESULT_DENY) continue;
                            entityliving.b((double)((float)j + 0.5f), (double)blockpos.q(), (double)((float)k + 0.5f), randomIn.nextFloat() * 360.0f, 0.0f);
                            worldIn.spawnEntity((Entity)entityliving);
                            ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos((Entity)entityliving)), ientitylivingdata);
                            flag = true;
                        }
                        j += randomIn.nextInt(5) - randomIn.nextInt(5);
                        k += randomIn.nextInt(5) - randomIn.nextInt(5);
                        while (j < p_77191_2_ || j >= p_77191_2_ + p_77191_4_ || k < p_77191_3_ || k >= p_77191_3_ + p_77191_4_) {
                            j = l + randomIn.nextInt(5) - randomIn.nextInt(5);
                            k = i1 + randomIn.nextInt(5) - randomIn.nextInt(5);
                        }
                    }
                }
            }
        }
    }
}
