/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.function.BiPredicate
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldEntitySpawner
 */
package net.minecraft.entity;

import java.util.function.BiPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;

public static enum EntityLiving.SpawnPlacementType {
    ON_GROUND,
    IN_AIR,
    IN_WATER;

    private final BiPredicate<IBlockAccess, BlockPos> spawnPredicate;

    private EntityLiving.SpawnPlacementType() {
        this.spawnPredicate = null;
    }

    private EntityLiving.SpawnPlacementType(BiPredicate<IBlockAccess, BlockPos> spawnPredicate) {
        this.spawnPredicate = spawnPredicate;
    }

    public boolean canSpawnAt(World world, BlockPos pos) {
        return this.spawnPredicate != null ? this.spawnPredicate.test((Object)world, (Object)pos) : WorldEntitySpawner.canCreatureTypeSpawnBody((EntityLiving.SpawnPlacementType)this, (World)world, (BlockPos)pos);
    }
}
