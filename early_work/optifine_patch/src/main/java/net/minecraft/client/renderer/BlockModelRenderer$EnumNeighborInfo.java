/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.BlockModelRenderer$Orientation
 *  net.minecraft.util.EnumFacing
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.util.EnumFacing;

public static enum BlockModelRenderer.EnumNeighborInfo {
    DOWN(new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.5f, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.SOUTH}),
    UP(new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH}, 1.0f, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.SOUTH}),
    NORTH(new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST}, 0.8f, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_WEST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_EAST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_EAST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_WEST}),
    SOUTH(new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP}, 0.8f, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.WEST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.WEST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.EAST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.EAST}),
    WEST(new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6f, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.SOUTH}),
    EAST(new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6f, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.SOUTH});

    private final EnumFacing[] corners;
    private final float shadeWeight;
    private final boolean doNonCubicWeight;
    private final BlockModelRenderer.Orientation[] vert0Weights;
    private final BlockModelRenderer.Orientation[] vert1Weights;
    private final BlockModelRenderer.Orientation[] vert2Weights;
    private final BlockModelRenderer.Orientation[] vert3Weights;
    private static final BlockModelRenderer.EnumNeighborInfo[] VALUES;

    private BlockModelRenderer.EnumNeighborInfo(EnumFacing[] p_i46236_3_, float p_i46236_4_, boolean p_i46236_5_, BlockModelRenderer.Orientation[] p_i46236_6_, BlockModelRenderer.Orientation[] p_i46236_7_, BlockModelRenderer.Orientation[] p_i46236_8_, BlockModelRenderer.Orientation[] p_i46236_9_) {
        this.corners = p_i46236_3_;
        this.shadeWeight = p_i46236_4_;
        this.doNonCubicWeight = p_i46236_5_;
        this.vert0Weights = p_i46236_6_;
        this.vert1Weights = p_i46236_7_;
        this.vert2Weights = p_i46236_8_;
        this.vert3Weights = p_i46236_9_;
    }

    public static BlockModelRenderer.EnumNeighborInfo getNeighbourInfo(EnumFacing p_178273_0_) {
        return VALUES[p_178273_0_.getIndex()];
    }

    static /* synthetic */ EnumFacing[] access$200(BlockModelRenderer.EnumNeighborInfo x0) {
        return x0.corners;
    }

    static /* synthetic */ boolean access$300(BlockModelRenderer.EnumNeighborInfo x0) {
        return x0.doNonCubicWeight;
    }

    static /* synthetic */ BlockModelRenderer.Orientation[] access$800(BlockModelRenderer.EnumNeighborInfo x0) {
        return x0.vert0Weights;
    }

    static /* synthetic */ BlockModelRenderer.Orientation[] access$1000(BlockModelRenderer.EnumNeighborInfo x0) {
        return x0.vert1Weights;
    }

    static /* synthetic */ BlockModelRenderer.Orientation[] access$1100(BlockModelRenderer.EnumNeighborInfo x0) {
        return x0.vert2Weights;
    }

    static /* synthetic */ BlockModelRenderer.Orientation[] access$1200(BlockModelRenderer.EnumNeighborInfo x0) {
        return x0.vert3Weights;
    }

    static {
        VALUES = new BlockModelRenderer.EnumNeighborInfo[6];
        BlockModelRenderer.EnumNeighborInfo.VALUES[EnumFacing.DOWN.getIndex()] = DOWN;
        BlockModelRenderer.EnumNeighborInfo.VALUES[EnumFacing.UP.getIndex()] = UP;
        BlockModelRenderer.EnumNeighborInfo.VALUES[EnumFacing.NORTH.getIndex()] = NORTH;
        BlockModelRenderer.EnumNeighborInfo.VALUES[EnumFacing.SOUTH.getIndex()] = SOUTH;
        BlockModelRenderer.EnumNeighborInfo.VALUES[EnumFacing.WEST.getIndex()] = WEST;
        BlockModelRenderer.EnumNeighborInfo.VALUES[EnumFacing.EAST.getIndex()] = EAST;
    }
}
