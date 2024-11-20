/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.util.EnumFacing
 */
package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

static enum BlockModelRenderer.VertexTranslations {
    DOWN(0, 1, 2, 3),
    UP(2, 3, 0, 1),
    NORTH(3, 0, 1, 2),
    SOUTH(0, 1, 2, 3),
    WEST(3, 0, 1, 2),
    EAST(1, 2, 3, 0);

    private final int vert0;
    private final int vert1;
    private final int vert2;
    private final int vert3;
    private static final BlockModelRenderer.VertexTranslations[] VALUES;

    private BlockModelRenderer.VertexTranslations(int p_i46234_3_, int p_i46234_4_, int p_i46234_5_, int p_i46234_6_) {
        this.vert0 = p_i46234_3_;
        this.vert1 = p_i46234_4_;
        this.vert2 = p_i46234_5_;
        this.vert3 = p_i46234_6_;
    }

    public static BlockModelRenderer.VertexTranslations getVertexTranslations(EnumFacing p_178184_0_) {
        return VALUES[p_178184_0_.getIndex()];
    }

    static /* synthetic */ int access$400(BlockModelRenderer.VertexTranslations x0) {
        return x0.vert0;
    }

    static /* synthetic */ int access$500(BlockModelRenderer.VertexTranslations x0) {
        return x0.vert1;
    }

    static /* synthetic */ int access$600(BlockModelRenderer.VertexTranslations x0) {
        return x0.vert2;
    }

    static /* synthetic */ int access$700(BlockModelRenderer.VertexTranslations x0) {
        return x0.vert3;
    }

    static {
        VALUES = new BlockModelRenderer.VertexTranslations[6];
        BlockModelRenderer.VertexTranslations.VALUES[EnumFacing.DOWN.getIndex()] = DOWN;
        BlockModelRenderer.VertexTranslations.VALUES[EnumFacing.UP.getIndex()] = UP;
        BlockModelRenderer.VertexTranslations.VALUES[EnumFacing.NORTH.getIndex()] = NORTH;
        BlockModelRenderer.VertexTranslations.VALUES[EnumFacing.SOUTH.getIndex()] = SOUTH;
        BlockModelRenderer.VertexTranslations.VALUES[EnumFacing.WEST.getIndex()] = WEST;
        BlockModelRenderer.VertexTranslations.VALUES[EnumFacing.EAST.getIndex()] = EAST;
    }
}
