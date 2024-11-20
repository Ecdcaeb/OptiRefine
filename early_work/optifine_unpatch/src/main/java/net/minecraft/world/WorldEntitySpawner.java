/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Math
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.List
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
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldServer
 *  net.minecraft.world.biome.Biome
 *  net.minecraft.world.biome.Biome$SpawnListEntry
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraftforge.event.ForgeEventFactory
 *  net.minecraftforge.fml.common.eventhandler.Event$Result
 */
package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class WorldEntitySpawner {
    private static final int MOB_COUNT_DIV = (int)Math.pow((double)17.0, (double)2.0);
    private final Set<ChunkPos> eligibleChunksForSpawning = Sets.newHashSet();

    public int findChunksForSpawning(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate) {
        if (!spawnHostileMobs && !spawnPeacefulMobs) {
            return 0;
        }
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
        int j4 = 0;
        BlockPos blockpos1 = worldServerIn.T();
        for (EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
            int l4;
            int k4;
            if (enumcreaturetype.getPeacefulCreature() && !spawnPeacefulMobs || !enumcreaturetype.getPeacefulCreature() && !spawnHostileMobs || enumcreaturetype.getAnimal() && !spawnOnSetTickRate || (k4 = worldServerIn.countEntities(enumcreaturetype, true)) > (l4 = enumcreaturetype.getMaxNumberOfCreature() * i / MOB_COUNT_DIV)) continue;
            ArrayList shuffled = Lists.newArrayList(this.eligibleChunksForSpawning);
            Collections.shuffle((List)shuffled);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            block6: for (ChunkPos chunkpos1 : shuffled) {
                BlockPos blockpos = WorldEntitySpawner.getRandomChunkPosition((World)worldServerIn, chunkpos1.x, chunkpos1.z);
                int k1 = blockpos.p();
                int l1 = blockpos.q();
                int i2 = blockpos.r();
                IBlockState iblockstate = worldServerIn.o(blockpos);
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
                        EntityLiving entityliving;
                        blockpos$mutableblockpos.setPos(l2 += worldServerIn.r.nextInt(6) - worldServerIn.r.nextInt(6), i3 += worldServerIn.r.nextInt(1) - worldServerIn.r.nextInt(1), j3 += worldServerIn.r.nextInt(6) - worldServerIn.r.nextInt(6));
                        float f = (float)l2 + 0.5f;
                        float f1 = (float)j3 + 0.5f;
                        if (worldServerIn.a((double)f, (double)i3, (double)f1, 24.0) || !(blockpos1.f((double)f, (double)i3, (double)f1) >= 576.0)) continue;
                        if (biome$spawnlistentry == null && (biome$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, (BlockPos)blockpos$mutableblockpos)) == null) continue block7;
                        if (!worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biome$spawnlistentry, (BlockPos)blockpos$mutableblockpos) || !WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity((Class)biome$spawnlistentry.entityClass), (World)worldServerIn, (BlockPos)blockpos$mutableblockpos)) continue;
                        try {
                            entityliving = biome$spawnlistentry.newInstance((World)worldServerIn);
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                            return j4;
                        }
                        entityliving.b((double)f, (double)i3, (double)f1, worldServerIn.r.nextFloat() * 360.0f, 0.0f);
                        Event.Result canSpawn = ForgeEventFactory.canEntitySpawn((EntityLiving)entityliving, (World)worldServerIn, (float)f, (float)i3, (float)f1, (boolean)false);
                        if (canSpawn == Event.Result.ALLOW || canSpawn == Event.Result.DEFAULT && entityliving.getCanSpawnHere() && entityliving.isNotColliding()) {
                            if (!ForgeEventFactory.doSpecialSpawn((EntityLiving)entityliving, (World)worldServerIn, (float)f, (float)i3, (float)f1)) {
                                ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.D(new BlockPos((Entity)entityliving)), ientitylivingdata);
                            }
                            if (entityliving.isNotColliding()) {
                                ++j2;
                                worldServerIn.spawnEntity((Entity)entityliving);
                            } else {
                                entityliving.X();
                            }
                            if (j2 >= ForgeEventFactory.getMaxSpawnPackSize((EntityLiving)entityliving)) continue block6;
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
        return spawnPlacementTypeIn.canSpawnAt(worldIn, pos);
    }

    public static boolean canCreatureTypeSpawnBody(EntityLiving.SpawnPlacementType spawnPlacementTypeIn, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (spawnPlacementTypeIn == EntityLiving.SpawnPlacementType.IN_WATER) {
            return iblockstate.a() == Material.WATER && worldIn.getBlockState(pos.down()).a() == Material.WATER && !worldIn.getBlockState(pos.up()).l();
        }
        BlockPos blockpos = pos.down();
        IBlockState state = worldIn.getBlockState(blockpos);
        if (!state.getBlock().canCreatureSpawn(state, (IBlockAccess)worldIn, blockpos, spawnPlacementTypeIn)) {
            return false;
        }
        Block block = worldIn.getBlockState(blockpos).getBlock();
        boolean flag = block != Blocks.BEDROCK && block != Blocks.BARRIER;
        return flag && WorldEntitySpawner.isValidEmptySpawnBlock(iblockstate) && WorldEntitySpawner.isValidEmptySpawnBlock(worldIn.getBlockState(pos.up()));
    }

    public static void performWorldGenSpawning(World worldIn, Biome biomeIn, int centerX, int centerZ, int diameterX, int diameterZ, Random randomIn) {
        List list = biomeIn.getSpawnableList(EnumCreatureType.CREATURE);
        if (!list.isEmpty()) {
            while (randomIn.nextFloat() < biomeIn.getSpawningChance()) {
                Biome.SpawnListEntry biome$spawnlistentry = (Biome.SpawnListEntry)WeightedRandom.getRandomItem((Random)worldIn.rand, (List)list);
                int i = biome$spawnlistentry.minGroupCount + randomIn.nextInt(1 + biome$spawnlistentry.maxGroupCount - biome$spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j = centerX + randomIn.nextInt(diameterX);
                int k = centerZ + randomIn.nextInt(diameterZ);
                int l = j;
                int i1 = k;
                for (int j1 = 0; j1 < i; ++j1) {
                    boolean flag = false;
                    for (int k1 = 0; !flag && k1 < 4; ++k1) {
                        BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k));
                        if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos)) {
                            EntityLiving entityliving;
                            try {
                                entityliving = biome$spawnlistentry.newInstance(worldIn);
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                                continue;
                            }
                            if (ForgeEventFactory.canEntitySpawn((EntityLiving)entityliving, (World)worldIn, (float)((float)j + 0.5f), (float)blockpos.q(), (float)((float)k + 0.5f), (boolean)false) == Event.Result.DENY) continue;
                            entityliving.b((double)((float)j + 0.5f), (double)blockpos.q(), (double)((float)k + 0.5f), randomIn.nextFloat() * 360.0f, 0.0f);
                            worldIn.spawnEntity((Entity)entityliving);
                            ientitylivingdata = entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos((Entity)entityliving)), ientitylivingdata);
                            flag = true;
                        }
                        j += randomIn.nextInt(5) - randomIn.nextInt(5);
                        k += randomIn.nextInt(5) - randomIn.nextInt(5);
                        while (j < centerX || j >= centerX + diameterX || k < centerZ || k >= centerZ + diameterX) {
                            j = l + randomIn.nextInt(5) - randomIn.nextInt(5);
                            k = i1 + randomIn.nextInt(5) - randomIn.nextInt(5);
                        }
                    }
                }
            }
        }
    }
}
