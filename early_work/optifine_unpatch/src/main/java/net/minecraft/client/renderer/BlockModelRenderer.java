/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Float
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.BitSet
 *  java.util.List
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BlockModelRenderer$1
 *  net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.color.BlockColors
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 *  net.minecraftforge.client.model.pipeline.LightUtil
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BlockModelRenderer {
    private final BlockColors blockColors;

    public BlockModelRenderer(BlockColors blockColorsIn) {
        this.blockColors = blockColorsIn;
    }

    public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder buffer, boolean checkSides) {
        return this.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, buffer, checkSides, MathHelper.getPositionRandom((Vec3i)blockPosIn));
    }

    public boolean renderModel(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand) {
        boolean flag = Minecraft.isAmbientOcclusionEnabled() && stateIn.getLightValue(worldIn, posIn) == 0 && modelIn.isAmbientOcclusion(stateIn);
        try {
            return flag ? this.renderModelSmooth(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand) : this.renderModelFlat(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand);
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo((CrashReportCategory)crashreportcategory, (BlockPos)posIn, (IBlockState)stateIn);
            crashreportcategory.addCrashSection("Using AO", (Object)flag);
            throw new ReportedException(crashreport);
        }
    }

    public boolean renderModelSmooth(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand) {
        boolean flag = false;
        float[] afloat = new float[EnumFacing.values().length * 2];
        BitSet bitset = new BitSet(3);
        AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = new AmbientOcclusionFace(this);
        for (EnumFacing enumfacing : EnumFacing.values()) {
            List list = modelIn.getQuads(stateIn, enumfacing, rand);
            if (list.isEmpty() || checkSides && !stateIn.c(worldIn, posIn, enumfacing)) continue;
            this.renderQuadsSmooth(worldIn, stateIn, posIn, buffer, (List<BakedQuad>)list, afloat, bitset, blockmodelrenderer$ambientocclusionface);
            flag = true;
        }
        List list1 = modelIn.getQuads(stateIn, (EnumFacing)null, rand);
        if (!list1.isEmpty()) {
            this.renderQuadsSmooth(worldIn, stateIn, posIn, buffer, (List<BakedQuad>)list1, afloat, bitset, blockmodelrenderer$ambientocclusionface);
            flag = true;
        }
        return flag;
    }

    public boolean renderModelFlat(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand) {
        boolean flag = false;
        BitSet bitset = new BitSet(3);
        for (EnumFacing enumfacing : EnumFacing.values()) {
            List list = modelIn.getQuads(stateIn, enumfacing, rand);
            if (list.isEmpty() || checkSides && !stateIn.c(worldIn, posIn, enumfacing)) continue;
            int i = stateIn.b(worldIn, posIn.offset(enumfacing));
            this.renderQuadsFlat(worldIn, stateIn, posIn, i, false, buffer, (List<BakedQuad>)list, bitset);
            flag = true;
        }
        List list1 = modelIn.getQuads(stateIn, (EnumFacing)null, rand);
        if (!list1.isEmpty()) {
            this.renderQuadsFlat(worldIn, stateIn, posIn, -1, true, buffer, (List<BakedQuad>)list1, bitset);
            flag = true;
        }
        return flag;
    }

    private void renderQuadsSmooth(IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, List<BakedQuad> list, float[] quadBounds, BitSet bitSet, AmbientOcclusionFace aoFace) {
        Vec3d vec3d = stateIn.f(blockAccessIn, posIn);
        double d0 = (double)posIn.p() + vec3d.x;
        double d1 = (double)posIn.q() + vec3d.y;
        double d2 = (double)posIn.r() + vec3d.z;
        int j = list.size();
        for (int i = 0; i < j; ++i) {
            BakedQuad bakedquad = (BakedQuad)list.get(i);
            this.fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), quadBounds, bitSet);
            aoFace.updateVertexBrightness(blockAccessIn, stateIn, posIn, bakedquad.getFace(), quadBounds, bitSet);
            buffer.addVertexData(bakedquad.getVertexData());
            buffer.putBrightness4(aoFace.vertexBrightness[0], aoFace.vertexBrightness[1], aoFace.vertexBrightness[2], aoFace.vertexBrightness[3]);
            if (bakedquad.shouldApplyDiffuseLighting()) {
                float diffuse = LightUtil.diffuseLight((EnumFacing)bakedquad.getFace());
                aoFace.vertexColorMultiplier[0] = aoFace.vertexColorMultiplier[0] * diffuse;
                aoFace.vertexColorMultiplier[1] = aoFace.vertexColorMultiplier[1] * diffuse;
                aoFace.vertexColorMultiplier[2] = aoFace.vertexColorMultiplier[2] * diffuse;
                aoFace.vertexColorMultiplier[3] = aoFace.vertexColorMultiplier[3] * diffuse;
            }
            if (bakedquad.hasTintIndex()) {
                int k = this.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor((int)k);
                }
                float f = (float)(k >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(k >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(k & 0xFF) / 255.0f;
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[0] * f, aoFace.vertexColorMultiplier[0] * f1, aoFace.vertexColorMultiplier[0] * f2, 4);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[1] * f, aoFace.vertexColorMultiplier[1] * f1, aoFace.vertexColorMultiplier[1] * f2, 3);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[2] * f, aoFace.vertexColorMultiplier[2] * f1, aoFace.vertexColorMultiplier[2] * f2, 2);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[3] * f, aoFace.vertexColorMultiplier[3] * f1, aoFace.vertexColorMultiplier[3] * f2, 1);
            } else {
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[0], aoFace.vertexColorMultiplier[0], aoFace.vertexColorMultiplier[0], 4);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[1], aoFace.vertexColorMultiplier[1], aoFace.vertexColorMultiplier[1], 3);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[2], aoFace.vertexColorMultiplier[2], aoFace.vertexColorMultiplier[2], 2);
                buffer.putColorMultiplier(aoFace.vertexColorMultiplier[3], aoFace.vertexColorMultiplier[3], aoFace.vertexColorMultiplier[3], 1);
            }
            buffer.putPosition(d0, d1, d2);
        }
    }

    private void fillQuadBounds(IBlockState stateIn, int[] vertexData, EnumFacing face, @Nullable float[] quadBounds, BitSet boundsFlags) {
        float f = 32.0f;
        float f1 = 32.0f;
        float f2 = 32.0f;
        float f3 = -32.0f;
        float f4 = -32.0f;
        float f5 = -32.0f;
        for (int i = 0; i < 4; ++i) {
            float f6 = Float.intBitsToFloat((int)vertexData[i * 7]);
            float f7 = Float.intBitsToFloat((int)vertexData[i * 7 + 1]);
            float f8 = Float.intBitsToFloat((int)vertexData[i * 7 + 2]);
            f = Math.min((float)f, (float)f6);
            f1 = Math.min((float)f1, (float)f7);
            f2 = Math.min((float)f2, (float)f8);
            f3 = Math.max((float)f3, (float)f6);
            f4 = Math.max((float)f4, (float)f7);
            f5 = Math.max((float)f5, (float)f8);
        }
        if (quadBounds != null) {
            quadBounds[EnumFacing.WEST.getIndex()] = f;
            quadBounds[EnumFacing.EAST.getIndex()] = f3;
            quadBounds[EnumFacing.DOWN.getIndex()] = f1;
            quadBounds[EnumFacing.UP.getIndex()] = f4;
            quadBounds[EnumFacing.NORTH.getIndex()] = f2;
            quadBounds[EnumFacing.SOUTH.getIndex()] = f5;
            int j = EnumFacing.values().length;
            quadBounds[EnumFacing.WEST.getIndex() + j] = 1.0f - f;
            quadBounds[EnumFacing.EAST.getIndex() + j] = 1.0f - f3;
            quadBounds[EnumFacing.DOWN.getIndex() + j] = 1.0f - f1;
            quadBounds[EnumFacing.UP.getIndex() + j] = 1.0f - f4;
            quadBounds[EnumFacing.NORTH.getIndex() + j] = 1.0f - f2;
            quadBounds[EnumFacing.SOUTH.getIndex() + j] = 1.0f - f5;
        }
        float f9 = 1.0E-4f;
        float f10 = 0.9999f;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[face.ordinal()]) {
            case 1: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f3 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f1 < 1.0E-4f || stateIn.g()) && f1 == f4);
                break;
            }
            case 2: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f3 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f4 > 0.9999f || stateIn.g()) && f1 == f4);
                break;
            }
            case 3: {
                boundsFlags.set(1, f >= 1.0E-4f || f1 >= 1.0E-4f || f3 <= 0.9999f || f4 <= 0.9999f);
                boundsFlags.set(0, (f2 < 1.0E-4f || stateIn.g()) && f2 == f5);
                break;
            }
            case 4: {
                boundsFlags.set(1, f >= 1.0E-4f || f1 >= 1.0E-4f || f3 <= 0.9999f || f4 <= 0.9999f);
                boundsFlags.set(0, (f5 > 0.9999f || stateIn.g()) && f2 == f5);
                break;
            }
            case 5: {
                boundsFlags.set(1, f1 >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f < 1.0E-4f || stateIn.g()) && f == f3);
                break;
            }
            case 6: {
                boundsFlags.set(1, f1 >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f3 > 0.9999f || stateIn.g()) && f == f3);
            }
        }
    }

    private void renderQuadsFlat(IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, int brightnessIn, boolean ownBrightness, BufferBuilder buffer, List<BakedQuad> list, BitSet bitSet) {
        Vec3d vec3d = stateIn.f(blockAccessIn, posIn);
        double d0 = (double)posIn.p() + vec3d.x;
        double d1 = (double)posIn.q() + vec3d.y;
        double d2 = (double)posIn.r() + vec3d.z;
        int j = list.size();
        for (int i = 0; i < j; ++i) {
            BakedQuad bakedquad = (BakedQuad)list.get(i);
            if (ownBrightness) {
                this.fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), null, bitSet);
                BlockPos blockpos = bitSet.get(0) ? posIn.offset(bakedquad.getFace()) : posIn;
                brightnessIn = stateIn.b(blockAccessIn, blockpos);
            }
            buffer.addVertexData(bakedquad.getVertexData());
            buffer.putBrightness4(brightnessIn, brightnessIn, brightnessIn, brightnessIn);
            if (bakedquad.hasTintIndex()) {
                int k = this.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor((int)k);
                }
                float f = (float)(k >> 16 & 0xFF) / 255.0f;
                float f1 = (float)(k >> 8 & 0xFF) / 255.0f;
                float f2 = (float)(k & 0xFF) / 255.0f;
                if (bakedquad.shouldApplyDiffuseLighting()) {
                    float diffuse = LightUtil.diffuseLight((EnumFacing)bakedquad.getFace());
                    f *= diffuse;
                    f1 *= diffuse;
                    f2 *= diffuse;
                }
                buffer.putColorMultiplier(f, f1, f2, 4);
                buffer.putColorMultiplier(f, f1, f2, 3);
                buffer.putColorMultiplier(f, f1, f2, 2);
                buffer.putColorMultiplier(f, f1, f2, 1);
            } else if (bakedquad.shouldApplyDiffuseLighting()) {
                float diffuse = LightUtil.diffuseLight((EnumFacing)bakedquad.getFace());
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 4);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 3);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 2);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 1);
            }
            buffer.putPosition(d0, d1, d2);
        }
    }

    public void renderModelBrightnessColor(IBakedModel bakedModel, float p_178262_2_, float red, float green, float blue) {
        this.renderModelBrightnessColor(null, bakedModel, p_178262_2_, red, green, blue);
    }

    public void renderModelBrightnessColor(IBlockState state, IBakedModel p_187495_2_, float p_187495_3_, float p_187495_4_, float p_187495_5_, float p_187495_6_) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            this.renderModelBrightnessColorQuads(p_187495_3_, p_187495_4_, p_187495_5_, p_187495_6_, (List<BakedQuad>)p_187495_2_.getQuads(state, enumfacing, 0L));
        }
        this.renderModelBrightnessColorQuads(p_187495_3_, p_187495_4_, p_187495_5_, p_187495_6_, (List<BakedQuad>)p_187495_2_.getQuads(state, (EnumFacing)null, 0L));
    }

    public void renderModelBrightness(IBakedModel model, IBlockState state, float brightness, boolean p_178266_4_) {
        Block block = state.getBlock();
        GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        int i = this.blockColors.colorMultiplier(state, (IBlockAccess)null, (BlockPos)null, 0);
        if (EntityRenderer.anaglyphEnable) {
            i = TextureUtil.anaglyphColor((int)i);
        }
        float f = (float)(i >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(i & 0xFF) / 255.0f;
        if (!p_178266_4_) {
            GlStateManager.color((float)brightness, (float)brightness, (float)brightness, (float)1.0f);
        }
        this.renderModelBrightnessColor(state, model, brightness, f, f1, f2);
    }

    private void renderModelBrightnessColorQuads(float brightness, float red, float green, float blue, List<BakedQuad> listQuads) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        int j = listQuads.size();
        for (int i = 0; i < j; ++i) {
            BakedQuad bakedquad = (BakedQuad)listQuads.get(i);
            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
            bufferbuilder.addVertexData(bakedquad.getVertexData());
            if (bakedquad.hasTintIndex()) {
                bufferbuilder.putColorRGB_F4(red * brightness, green * brightness, blue * brightness);
            } else {
                bufferbuilder.putColorRGB_F4(brightness, brightness, brightness);
            }
            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            bufferbuilder.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            tessellator.draw();
        }
    }
}
