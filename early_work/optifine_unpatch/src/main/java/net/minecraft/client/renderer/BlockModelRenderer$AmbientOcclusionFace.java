/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.BitSet
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.BlockModelRenderer
 *  net.minecraft.client.renderer.BlockModelRenderer$EnumNeighborInfo
 *  net.minecraft.client.renderer.BlockModelRenderer$VertexTranslations
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$PooledMutableBlockPos
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer;

import java.util.BitSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Exception performing whole class analysis ignored.
 */
@SideOnly(value=Side.CLIENT)
class BlockModelRenderer.AmbientOcclusionFace {
    private final float[] vertexColorMultiplier = new float[4];
    private final int[] vertexBrightness = new int[4];

    BlockModelRenderer.AmbientOcclusionFace(BlockModelRenderer this$0) {
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
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        BlockModelRenderer.EnumNeighborInfo blockmodelrenderer$enumneighborinfo = BlockModelRenderer.EnumNeighborInfo.getNeighbourInfo((EnumFacing)direction);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.retain((Vec3i)blockpos).move(blockmodelrenderer$enumneighborinfo.corners[0]);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain((Vec3i)blockpos).move(blockmodelrenderer$enumneighborinfo.corners[1]);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos3 = BlockPos.PooledMutableBlockPos.retain((Vec3i)blockpos).move(blockmodelrenderer$enumneighborinfo.corners[2]);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos4 = BlockPos.PooledMutableBlockPos.retain((Vec3i)blockpos).move(blockmodelrenderer$enumneighborinfo.corners[3]);
        int i = state.b(worldIn, (BlockPos)blockpos$pooledmutableblockpos1);
        int j = state.b(worldIn, (BlockPos)blockpos$pooledmutableblockpos2);
        int k = state.b(worldIn, (BlockPos)blockpos$pooledmutableblockpos3);
        int l = state.b(worldIn, (BlockPos)blockpos$pooledmutableblockpos4);
        float f = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos1).j();
        float f1 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos2).j();
        float f2 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos3).j();
        float f3 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos4).j();
        boolean flag = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos1).move(direction)).e();
        boolean flag1 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos2).move(direction)).e();
        boolean flag2 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos3).move(direction)).e();
        boolean flag3 = worldIn.getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos4).move(direction)).e();
        if (!flag2 && !flag) {
            f4 = f;
            i1 = i;
        } else {
            BlockPos.PooledMutableBlockPos blockpos1 = blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos1).move(blockmodelrenderer$enumneighborinfo.corners[2]);
            f4 = worldIn.getBlockState((BlockPos)blockpos1).j();
            i1 = state.b(worldIn, (BlockPos)blockpos1);
        }
        if (!flag3 && !flag) {
            f5 = f;
            j1 = i;
        } else {
            BlockPos.PooledMutableBlockPos blockpos2 = blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos1).move(blockmodelrenderer$enumneighborinfo.corners[3]);
            f5 = worldIn.getBlockState((BlockPos)blockpos2).j();
            j1 = state.b(worldIn, (BlockPos)blockpos2);
        }
        if (!flag2 && !flag1) {
            f6 = f1;
            k1 = j;
        } else {
            BlockPos.PooledMutableBlockPos blockpos3 = blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos2).move(blockmodelrenderer$enumneighborinfo.corners[2]);
            f6 = worldIn.getBlockState((BlockPos)blockpos3).j();
            k1 = state.b(worldIn, (BlockPos)blockpos3);
        }
        if (!flag3 && !flag1) {
            f7 = f1;
            l1 = j;
        } else {
            BlockPos.PooledMutableBlockPos blockpos4 = blockpos$pooledmutableblockpos.setPos((Vec3i)blockpos$pooledmutableblockpos2).move(blockmodelrenderer$enumneighborinfo.corners[3]);
            f7 = worldIn.getBlockState((BlockPos)blockpos4).j();
            l1 = state.b(worldIn, (BlockPos)blockpos4);
        }
        int i3 = state.b(worldIn, centerPos);
        if (shapeState.get(0) || !worldIn.getBlockState(centerPos.offset(direction)).p()) {
            i3 = state.b(worldIn, centerPos.offset(direction));
        }
        float f8 = shapeState.get(0) ? worldIn.getBlockState(blockpos).j() : worldIn.getBlockState(centerPos).j();
        BlockModelRenderer.VertexTranslations blockmodelrenderer$vertextranslations = BlockModelRenderer.VertexTranslations.getVertexTranslations((EnumFacing)direction);
        blockpos$pooledmutableblockpos.release();
        blockpos$pooledmutableblockpos1.release();
        blockpos$pooledmutableblockpos2.release();
        blockpos$pooledmutableblockpos3.release();
        blockpos$pooledmutableblockpos4.release();
        if (shapeState.get(1) && blockmodelrenderer$enumneighborinfo.doNonCubicWeight) {
            float f29 = (f3 + f + f5 + f8) * 0.25f;
            float f30 = (f2 + f + f4 + f8) * 0.25f;
            float f31 = (f2 + f1 + f6 + f8) * 0.25f;
            float f32 = (f3 + f1 + f7 + f8) * 0.25f;
            float f13 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[0].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[1].shape];
            float f14 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[2].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[3].shape];
            float f15 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[4].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[5].shape];
            float f16 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[6].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[7].shape];
            float f17 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[0].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[1].shape];
            float f18 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[2].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[3].shape];
            float f19 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[4].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[5].shape];
            float f20 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[6].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[7].shape];
            float f21 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[0].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[1].shape];
            float f22 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[2].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[3].shape];
            float f23 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[4].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[5].shape];
            float f24 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[6].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[7].shape];
            float f25 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[0].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[1].shape];
            float f26 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[2].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[3].shape];
            float f27 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[4].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[5].shape];
            float f28 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[6].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[7].shape];
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert0] = f29 * f13 + f30 * f14 + f31 * f15 + f32 * f16;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert1] = f29 * f17 + f30 * f18 + f31 * f19 + f32 * f20;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert2] = f29 * f21 + f30 * f22 + f31 * f23 + f32 * f24;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert3] = f29 * f25 + f30 * f26 + f31 * f27 + f32 * f28;
            int i2 = this.getAoBrightness(l, i, j1, i3);
            int j2 = this.getAoBrightness(k, i, i1, i3);
            int k2 = this.getAoBrightness(k, j, k1, i3);
            int l2 = this.getAoBrightness(l, j, l1, i3);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert0] = this.getVertexBrightness(i2, j2, k2, l2, f13, f14, f15, f16);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert1] = this.getVertexBrightness(i2, j2, k2, l2, f17, f18, f19, f20);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert2] = this.getVertexBrightness(i2, j2, k2, l2, f21, f22, f23, f24);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert3] = this.getVertexBrightness(i2, j2, k2, l2, f25, f26, f27, f28);
        } else {
            float f9 = (f3 + f + f5 + f8) * 0.25f;
            float f10 = (f2 + f + f4 + f8) * 0.25f;
            float f11 = (f2 + f1 + f6 + f8) * 0.25f;
            float f12 = (f3 + f1 + f7 + f8) * 0.25f;
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert0] = this.getAoBrightness(l, i, j1, i3);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert1] = this.getAoBrightness(k, i, i1, i3);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert2] = this.getAoBrightness(k, j, k1, i3);
            this.vertexBrightness[blockmodelrenderer$vertextranslations.vert3] = this.getAoBrightness(l, j, l1, i3);
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert0] = f9;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert1] = f10;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert2] = f11;
            this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert3] = f12;
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
}
