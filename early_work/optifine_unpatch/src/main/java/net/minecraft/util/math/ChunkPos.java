/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 */
package net.minecraft.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class ChunkPos {
    public final int x;
    public final int z;

    public ChunkPos(int n, int n2) {
        this.x = n;
        this.z = n2;
    }

    public ChunkPos(BlockPos blockPos) {
        this.x = blockPos.p() >> 4;
        this.z = blockPos.r() >> 4;
    }

    public static long asLong(int n, int n2) {
        return (long)n & 0xFFFFFFFFL | ((long)n2 & 0xFFFFFFFFL) << 32;
    }

    public int hashCode() {
        int n = 1664525 * this.x + 1013904223;
        \u2603 = 1664525 * (this.z ^ 0xDEADBEEF) + 1013904223;
        return n ^ \u2603;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ChunkPos) {
            ChunkPos chunkPos = (ChunkPos)object;
            return this.x == chunkPos.x && this.z == chunkPos.z;
        }
        return false;
    }

    public double getDistanceSq(Entity entity) {
        double d = this.x * 16 + 8;
        \u2603 = this.z * 16 + 8;
        \u2603 = d - entity.posX;
        \u2603 = \u2603 - entity.posZ;
        return \u2603 * \u2603 + \u2603 * \u2603;
    }

    public int getXStart() {
        return this.x << 4;
    }

    public int getZStart() {
        return this.z << 4;
    }

    public int getXEnd() {
        return (this.x << 4) + 15;
    }

    public int getZEnd() {
        return (this.z << 4) + 15;
    }

    public BlockPos getBlock(int n, int n2, int n3) {
        return new BlockPos((this.x << 4) + n, n2, (this.z << 4) + n3);
    }

    public String toString() {
        return "[" + this.x + ", " + this.z + "]";
    }
}
