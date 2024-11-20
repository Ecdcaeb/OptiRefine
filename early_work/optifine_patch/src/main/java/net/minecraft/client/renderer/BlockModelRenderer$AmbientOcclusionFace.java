/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.BitSet
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.BlockModelRenderer
 *  net.minecraft.client.renderer.BlockModelRenderer$EnumNeighborInfo
 *  net.minecraft.client.renderer.BlockModelRenderer$Orientation
 *  net.minecraft.client.renderer.BlockModelRenderer$VertexTranslations
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 */
package net.minecraft.client.renderer;

import java.util.BitSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;

/*
 * Exception performing whole class analysis ignored.
 */
public static class BlockModelRenderer.AmbientOcclusionFace {
    private final float[] vertexColorMultiplier = new float[4];
    private final int[] vertexBrightness = new int[4];
    private BlockPos.MutableBlockPos[] blockPosArr = new BlockPos.MutableBlockPos[5];

    public BlockModelRenderer.AmbientOcclusionFace() {
        this(null);
    }

    public BlockModelRenderer.AmbientOcclusionFace(BlockModelRenderer bmr) {
        for (int i = 0; i < this.blockPosArr.length; ++i) {
            this.blockPosArr[i] = new BlockPos.MutableBlockPos();
        }
    }

    public void setMaxBlockLight() {
        int maxBlockLight = 240;
        this.vertexBrightness[0] = this.vertexBrightness[0] | maxBlockLight;
        this.vertexBrightness[1] = this.vertexBrightness[1] | maxBlockLight;
        this.vertexBrightness[2] = this.vertexBrightness[2] | maxBlockLight;
        this.vertexBrightness[3] = this.vertexBrightness[3] | maxBlockLight;
        this.vertexColorMultiplier[0] = 1.0f;
        this.vertexColorMultiplier[1] = 1.0f;
        this.vertexColorMultiplier[2] = 1.0f;
        this.vertexColorMultiplier[3] = 1.0f;
    }

    public void updateVertexBrightness(IBlockAccess worldIn, IBlockState state, BlockPos centerPos, EnumFacing direction, float[] faceShape, BitSet shapeState) {
        int l1;
        float f7;
        int k1;
        float f6;
        int j1;
        float f5;
        int i1;
        float f4;
        BlockPos blockpos = shapeState.get(0) ? centerPos.offset(direction) : centerPos;
        BlockPos.MutableBlockPos blockpos$pooledmutableblockpos = this.blockPosArr[0].setPos(0, 0, 0);
        BlockModelRenderer.EnumNeighborInfo blockmodelrenderer$enumneighborinfo = BlockModelRenderer.EnumNeighborInfo.getNeighbourInfo((EnumFacing)direction);
        BlockPos.MutableBlockPos blockpos$pooledmutableblockpos1 = this.blockPosArr[1].setPos((Vec3i)blockpos).move(BlockModelRenderer.EnumNeighborInfo.access$200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[0]);
        BlockPos.MutableBlockPos blockpos$pooledmutableblockpos2 = this.blockPosArr[2].setPos((Vec3i)blockpos).move(BlockModelRenderer.EnumNeighborInfo.access$200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[1]);
        BlockPos.MutableBlockPos blockpos$pooledmutableblockpos3 = this.blockPosArr[3].setPos((Vec3i)blockpos).move(BlockModelRenderer.EnumNeighborInfo.access$200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[2]);
        BlockPos.MutableBlockPos blockpos$pooledmutableblockpos4 = this.blockPosArr[4].setPos((Vec3i)blockpos).move(BlockModelRenderer.EnumNeighborInfo.access$200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[3]);
        int i = state.b(worldIn, (BlockPos)blockpos$pooledmutableblockpos1);
        int j = state.b(worldIn, (BlockPos)blockpos$pooledmutableblockpos2);
        int k = state.b(worldIn, (BlockPos)blockpos$pooledmutableblockpos3);
        int l = state.b(worldIn, (BlockPos)blockpos$pooledmutableblockpos4);
        float f = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos1).j();
        float f1 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos2).j();
        float f2 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos3).j();
        float f3 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos4).j();
        f = BlockModelRenderer.fixAoLightValue((float)f);
        f1 = BlockModelRenderer.fixAoLightValue((float)f1);
        f2 = BlockModelRenderer.fixAoLightValue((float)f2);
        f3 = BlockModelRenderer.fixAoLightValue((float)f3);
        boolean flag = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos1).move(direction)).e();
        boolean flag1 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos2).move(direction)).e();
        boolean flag2 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos3).move(direction)).e();
        boolean flag3 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos4).move(direction)).e();
        if (!flag2 && !flag) {
            f4 = f;
            i1 = i;
        } else {
            BlockPos.MutableBlockPos blockpos1 = blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos1).move(BlockModelRenderer.EnumNeighborInfo.access$200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[2]);
            f4 = worldIn.getBlockState((BlockPos)blockpos1).j();
            f4 = BlockModelRenderer.fixAoLightValue((float)f4);
            i1 = state.b(worldIn, (BlockPos)blockpos1);
        }
        if (!flag3 && !flag) {
            f5 = f;
            j1 = i;
        } else {
            BlockPos.MutableBlockPos blockpos2 = blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos1).move(BlockModelRenderer.EnumNeighborInfo.access$200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[3]);
            f5 = worldIn.getBlockState((BlockPos)blockpos2).j();
            f5 = BlockModelRenderer.fixAoLightValue((float)f5);
            j1 = state.b(worldIn, (BlockPos)blockpos2);
        }
        if (!flag2 && !flag1) {
            f6 = f1;
            k1 = j;
        } else {
            BlockPos.MutableBlockPos blockpos3 = blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos2).move(BlockModelRenderer.EnumNeighborInfo.access$200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[2]);
            f6 = worldIn.getBlockState((BlockPos)blockpos3).j();
            f6 = BlockModelRenderer.fixAoLightValue((float)f6);
            k1 = state.b(worldIn, (BlockPos)blockpos3);
        }
        if (!flag3 && !flag1) {
            f7 = f1;
            l1 = j;
        } else {
            BlockPos.MutableBlockPos blockpos4 = blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos2).move(BlockModelRenderer.EnumNeighborInfo.access$200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[3]);
            f7 = worldIn.getBlockState((BlockPos)blockpos4).j();
            f7 = BlockModelRenderer.fixAoLightValue((float)f7);
            l1 = state.b(worldIn, (BlockPos)blockpos4);
        }
        int i3 = state.b(worldIn, centerPos);
        if (shapeState.get(0) || !worldIn.getBlockState(centerPos.offset(direction)).p()) {
            i3 = state.b(worldIn, centerPos.offset(direction));
        }
        float f8 = shapeState.get(0) ? worldIn.getBlockState(blockpos).j() : worldIn.getBlockState(centerPos).j();
        f8 = BlockModelRenderer.fixAoLightValue((float)f8);
        BlockModelRenderer.VertexTranslations blockmodelrenderer$vertextranslations = BlockModelRenderer.VertexTranslations.getVertexTranslations((EnumFacing)direction);
        if (shapeState.get(1) && BlockModelRenderer.EnumNeighborInfo.access$300((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)) {
            float f29 = (f3 + f + f5 + f8) * 0.25f;
            float f30 = (f2 + f + f4 + f8) * 0.25f;
            float f31 = (f2 + f1 + f6 + f8) * 0.25f;
            float f32 = (f3 + f1 + f7 + f8) * 0.25f;
            float f13 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$800((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[0])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$800((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[1])];
            float f14 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$800((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[2])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$800((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[3])];
            float f15 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$800((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[4])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$800((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[5])];
            float f16 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$800((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[6])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$800((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[7])];
            float f17 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1000((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[0])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1000((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[1])];
            float f18 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1000((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[2])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1000((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[3])];
            float f19 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1000((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[4])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1000((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[5])];
            float f20 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1000((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[6])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1000((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[7])];
            float f21 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1100((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[0])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1100((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[1])];
            float f22 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1100((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[2])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1100((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[3])];
            float f23 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1100((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[4])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1100((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[5])];
            float f24 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1100((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[6])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1100((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[7])];
            float f25 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[0])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[1])];
            float f26 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[2])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[3])];
            float f27 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[4])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[5])];
            float f28 = faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[6])] * faceShape[BlockModelRenderer.Orientation.access$900((BlockModelRenderer.Orientation)BlockModelRenderer.EnumNeighborInfo.access$1200((BlockModelRenderer.EnumNeighborInfo)blockmodelrenderer$enumneighborinfo)[7])];
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$400((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f29 * f13 + f30 * f14 + f31 * f15 + f32 * f16;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$500((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f29 * f17 + f30 * f18 + f31 * f19 + f32 * f20;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$600((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f29 * f21 + f30 * f22 + f31 * f23 + f32 * f24;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$700((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f29 * f25 + f30 * f26 + f31 * f27 + f32 * f28;
            int i2 = this.getAoBrightness(l, i, j1, i3);
            int j2 = this.getAoBrightness(k, i, i1, i3);
            int k2 = this.getAoBrightness(k, j, k1, i3);
            int l2 = this.getAoBrightness(l, j, l1, i3);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$400((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getVertexBrightness(i2, j2, k2, l2, f13, f14, f15, f16);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$500((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getVertexBrightness(i2, j2, k2, l2, f17, f18, f19, f20);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$600((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getVertexBrightness(i2, j2, k2, l2, f21, f22, f23, f24);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$700((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getVertexBrightness(i2, j2, k2, l2, f25, f26, f27, f28);
        } else {
            float f9 = (f3 + f + f5 + f8) * 0.25f;
            float f10 = (f2 + f + f4 + f8) * 0.25f;
            float f11 = (f2 + f1 + f6 + f8) * 0.25f;
            float f12 = (f3 + f1 + f7 + f8) * 0.25f;
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$400((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getAoBrightness(l, i, j1, i3);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$500((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getAoBrightness(k, i, i1, i3);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$600((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getAoBrightness(k, j, k1, i3);
            this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$700((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = this.getAoBrightness(l, j, l1, i3);
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$400((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f9;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$500((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f10;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$600((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f11;
            this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$700((BlockModelRenderer.VertexTranslations)blockmodelrenderer$vertextranslations)] = f12;
        }
    }

    private int getAoBrightness(int br1, int br2, int br3, int br4) {
        if (br1 == 0) {
            br1 = br4;
        }
        if (br2 == 0) {
            br2 = br4;
        }
        if (br3 == 0) {
            br3 = br4;
        }
        return br1 + br2 + br3 + br4 >> 2 & 0xFF00FF;
    }

    private int getVertexBrightness(int p_178203_1_, int p_178203_2_, int p_178203_3_, int p_178203_4_, float p_178203_5_, float p_178203_6_, float p_178203_7_, float p_178203_8_) {
        int i = (int)((float)(p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (float)(p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (float)(p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (float)(p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
        int j = (int)((float)(p_178203_1_ & 0xFF) * p_178203_5_ + (float)(p_178203_2_ & 0xFF) * p_178203_6_ + (float)(p_178203_3_ & 0xFF) * p_178203_7_ + (float)(p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
        return i << 16 | j;
    }

    static /* synthetic */ int[] access$000(BlockModelRenderer.AmbientOcclusionFace x0) {
        return x0.vertexBrightness;
    }

    static /* synthetic */ float[] access$100(BlockModelRenderer.AmbientOcclusionFace x0) {
        return x0.vertexColorMultiplier;
    }
}
