/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.BlockSlab$EnumBlockHalf
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.block.model.FaceBakery
 *  net.minecraft.client.renderer.color.BlockColors
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.IBlockAccess
 *  net.optifine.CustomColors
 *  net.optifine.render.RenderEnv
 *  net.optifine.shaders.SVertexBuilder
 */
package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.optifine.CustomColors;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;

public class BlockFluidRenderer {
    private final BlockColors blockColors;
    private final TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
    private final TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];
    private TextureAtlasSprite atlasSpriteWaterOverlay;

    public BlockFluidRenderer(BlockColors blockColorsIn) {
        this.blockColors = blockColorsIn;
        this.initAtlasSprites();
    }

    protected void initAtlasSprites() {
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        this.atlasSpritesLava[0] = texturemap.getAtlasSprite("minecraft:blocks/lava_still");
        this.atlasSpritesLava[1] = texturemap.getAtlasSprite("minecraft:blocks/lava_flow");
        this.atlasSpritesWater[0] = texturemap.getAtlasSprite("minecraft:blocks/water_still");
        this.atlasSpritesWater[1] = texturemap.getAtlasSprite("minecraft:blocks/water_flow");
        this.atlasSpriteWaterOverlay = texturemap.getAtlasSprite("minecraft:blocks/water_overlay");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn) {
        try {
            if (Config.isShaders()) {
                SVertexBuilder.pushEntity((IBlockState)blockStateIn, (BlockPos)blockPosIn, (IBlockAccess)blockAccess, (BufferBuilder)worldRendererIn);
            }
            BlockLiquid blockliquid = (BlockLiquid)blockStateIn.getBlock();
            boolean flag = blockStateIn.a() == Material.LAVA;
            TextureAtlasSprite[] atextureatlassprite = flag ? this.atlasSpritesLava : this.atlasSpritesWater;
            RenderEnv renderEnv = worldRendererIn.getRenderEnv(blockStateIn, blockPosIn);
            int i = CustomColors.getFluidColor((IBlockAccess)blockAccess, (IBlockState)blockStateIn, (BlockPos)blockPosIn, (RenderEnv)renderEnv);
            float f = (float)(i >> 16 & 0xFF) / 255.0f;
            float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
            float f2 = (float)(i & 0xFF) / 255.0f;
            boolean flag1 = blockStateIn.c(blockAccess, blockPosIn, EnumFacing.UP);
            boolean flag2 = blockStateIn.c(blockAccess, blockPosIn, EnumFacing.DOWN);
            boolean[] aboolean = renderEnv.getBorderFlags();
            aboolean[0] = blockStateIn.c(blockAccess, blockPosIn, EnumFacing.NORTH);
            aboolean[1] = blockStateIn.c(blockAccess, blockPosIn, EnumFacing.SOUTH);
            aboolean[2] = blockStateIn.c(blockAccess, blockPosIn, EnumFacing.WEST);
            aboolean[3] = blockStateIn.c(blockAccess, blockPosIn, EnumFacing.EAST);
            if (!(flag1 || flag2 || aboolean[0] || aboolean[1] || aboolean[2] || aboolean[3])) {
                boolean bl = false;
                return bl;
            }
            boolean flag3 = false;
            float f3 = 0.5f;
            float f4 = 1.0f;
            float f5 = 0.8f;
            float f6 = 0.6f;
            Material material = blockStateIn.a();
            float f7 = this.getFluidHeight(blockAccess, blockPosIn, material);
            float f8 = this.getFluidHeight(blockAccess, blockPosIn.south(), material);
            float f9 = this.getFluidHeight(blockAccess, blockPosIn.east().south(), material);
            float f10 = this.getFluidHeight(blockAccess, blockPosIn.east(), material);
            double d0 = blockPosIn.p();
            double d1 = blockPosIn.q();
            double d2 = blockPosIn.r();
            float f11 = 0.001f;
            if (flag1) {
                float f20;
                float f16;
                float f19;
                float f15;
                float f18;
                float f14;
                float f17;
                float f13;
                flag3 = true;
                float f12 = BlockLiquid.getSlopeAngle((IBlockAccess)blockAccess, (BlockPos)blockPosIn, (Material)material, (IBlockState)blockStateIn);
                TextureAtlasSprite textureatlassprite = f12 > -999.0f ? atextureatlassprite[1] : atextureatlassprite[0];
                worldRendererIn.setSprite(textureatlassprite);
                f7 -= 0.001f;
                f8 -= 0.001f;
                f9 -= 0.001f;
                f10 -= 0.001f;
                if (f12 < -999.0f) {
                    f13 = textureatlassprite.getInterpolatedU(0.0);
                    f17 = textureatlassprite.getInterpolatedV(0.0);
                    f14 = f13;
                    f18 = textureatlassprite.getInterpolatedV(16.0);
                    f15 = textureatlassprite.getInterpolatedU(16.0);
                    f19 = f18;
                    f16 = f15;
                    f20 = f17;
                } else {
                    float f21 = MathHelper.sin((float)f12) * 0.25f;
                    float f22 = MathHelper.cos((float)f12) * 0.25f;
                    float f23 = 8.0f;
                    f13 = textureatlassprite.getInterpolatedU((double)(8.0f + (-f22 - f21) * 16.0f));
                    f17 = textureatlassprite.getInterpolatedV((double)(8.0f + (-f22 + f21) * 16.0f));
                    f14 = textureatlassprite.getInterpolatedU((double)(8.0f + (-f22 + f21) * 16.0f));
                    f18 = textureatlassprite.getInterpolatedV((double)(8.0f + (f22 + f21) * 16.0f));
                    f15 = textureatlassprite.getInterpolatedU((double)(8.0f + (f22 + f21) * 16.0f));
                    f19 = textureatlassprite.getInterpolatedV((double)(8.0f + (f22 - f21) * 16.0f));
                    f16 = textureatlassprite.getInterpolatedU((double)(8.0f + (f22 - f21) * 16.0f));
                    f20 = textureatlassprite.getInterpolatedV((double)(8.0f + (-f22 - f21) * 16.0f));
                }
                int k2 = blockStateIn.b(blockAccess, blockPosIn);
                int l2 = k2 >> 16 & 0xFFFF;
                int i3 = k2 & 0xFFFF;
                float f24 = 1.0f * f;
                float f25 = 1.0f * f1;
                float f26 = 1.0f * f2;
                worldRendererIn.pos(d0 + 0.0, d1 + (double)f7, d2 + 0.0).color(f24, f25, f26, 1.0f).tex((double)f13, (double)f17).lightmap(l2, i3).endVertex();
                worldRendererIn.pos(d0 + 0.0, d1 + (double)f8, d2 + 1.0).color(f24, f25, f26, 1.0f).tex((double)f14, (double)f18).lightmap(l2, i3).endVertex();
                worldRendererIn.pos(d0 + 1.0, d1 + (double)f9, d2 + 1.0).color(f24, f25, f26, 1.0f).tex((double)f15, (double)f19).lightmap(l2, i3).endVertex();
                worldRendererIn.pos(d0 + 1.0, d1 + (double)f10, d2 + 0.0).color(f24, f25, f26, 1.0f).tex((double)f16, (double)f20).lightmap(l2, i3).endVertex();
                if (blockliquid.shouldRenderSides(blockAccess, blockPosIn.up())) {
                    worldRendererIn.pos(d0 + 0.0, d1 + (double)f7, d2 + 0.0).color(f24, f25, f26, 1.0f).tex((double)f13, (double)f17).lightmap(l2, i3).endVertex();
                    worldRendererIn.pos(d0 + 1.0, d1 + (double)f10, d2 + 0.0).color(f24, f25, f26, 1.0f).tex((double)f16, (double)f20).lightmap(l2, i3).endVertex();
                    worldRendererIn.pos(d0 + 1.0, d1 + (double)f9, d2 + 1.0).color(f24, f25, f26, 1.0f).tex((double)f15, (double)f19).lightmap(l2, i3).endVertex();
                    worldRendererIn.pos(d0 + 0.0, d1 + (double)f8, d2 + 1.0).color(f24, f25, f26, 1.0f).tex((double)f14, (double)f18).lightmap(l2, i3).endVertex();
                }
            }
            if (flag2) {
                worldRendererIn.setSprite(atextureatlassprite[0]);
                float f35 = atextureatlassprite[0].getMinU();
                float f36 = atextureatlassprite[0].getMaxU();
                float f37 = atextureatlassprite[0].getMinV();
                float f38 = atextureatlassprite[0].getMaxV();
                int l1 = blockStateIn.b(blockAccess, blockPosIn.down());
                int i2 = l1 >> 16 & 0xFFFF;
                int j2 = l1 & 0xFFFF;
                float fbr = FaceBakery.getFaceBrightness((EnumFacing)EnumFacing.DOWN);
                worldRendererIn.pos(d0, d1, d2 + 1.0).color(f * fbr, f1 * fbr, f2 * fbr, 1.0f).tex((double)f35, (double)f38).lightmap(i2, j2).endVertex();
                worldRendererIn.pos(d0, d1, d2).color(f * fbr, f1 * fbr, f2 * fbr, 1.0f).tex((double)f35, (double)f37).lightmap(i2, j2).endVertex();
                worldRendererIn.pos(d0 + 1.0, d1, d2).color(f * fbr, f1 * fbr, f2 * fbr, 1.0f).tex((double)f36, (double)f37).lightmap(i2, j2).endVertex();
                worldRendererIn.pos(d0 + 1.0, d1, d2 + 1.0).color(f * fbr, f1 * fbr, f2 * fbr, 1.0f).tex((double)f36, (double)f38).lightmap(i2, j2).endVertex();
                flag3 = true;
            }
            for (int i1 = 0; i1 < 4; ++i1) {
                double d6;
                double d4;
                double d5;
                double d3;
                float f40;
                float f39;
                int j1 = 0;
                int k1 = 0;
                if (i1 == 0) {
                    --k1;
                }
                if (i1 == 1) {
                    ++k1;
                }
                if (i1 == 2) {
                    --j1;
                }
                if (i1 == 3) {
                    ++j1;
                }
                BlockPos blockpos = blockPosIn.add(j1, 0, k1);
                TextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];
                worldRendererIn.setSprite(textureatlassprite1);
                float yMin1 = 0.0f;
                float yMin2 = 0.0f;
                if (!flag) {
                    BlockSlab blockSlab;
                    IBlockState blockState = blockAccess.getBlockState(blockpos);
                    Block block = blockState.getBlock();
                    if (block == Blocks.GLASS || block == Blocks.STAINED_GLASS || block == Blocks.BEACON || block == Blocks.SLIME_BLOCK) {
                        textureatlassprite1 = this.atlasSpriteWaterOverlay;
                        worldRendererIn.setSprite(textureatlassprite1);
                    }
                    if (block == Blocks.FARMLAND || block == Blocks.GRASS_PATH) {
                        yMin1 = 0.9375f;
                        yMin2 = 0.9375f;
                    }
                    if (block instanceof BlockSlab && !(blockSlab = (BlockSlab)block).isDouble() && blockState.getValue((IProperty)BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM) {
                        yMin1 = 0.5f;
                        yMin2 = 0.5f;
                    }
                }
                if (!aboolean[i1]) continue;
                if (i1 == 0) {
                    f39 = f7;
                    f40 = f10;
                    d3 = d0;
                    d5 = d0 + 1.0;
                    d4 = d2 + (double)0.001f;
                    d6 = d2 + (double)0.001f;
                } else if (i1 == 1) {
                    f39 = f9;
                    f40 = f8;
                    d3 = d0 + 1.0;
                    d5 = d0;
                    d4 = d2 + 1.0 - (double)0.001f;
                    d6 = d2 + 1.0 - (double)0.001f;
                } else if (i1 == 2) {
                    f39 = f8;
                    f40 = f7;
                    d3 = d0 + (double)0.001f;
                    d5 = d0 + (double)0.001f;
                    d4 = d2 + 1.0;
                    d6 = d2;
                } else {
                    f39 = f10;
                    f40 = f9;
                    d3 = d0 + 1.0 - (double)0.001f;
                    d5 = d0 + 1.0 - (double)0.001f;
                    d4 = d2;
                    d6 = d2 + 1.0;
                }
                if (f39 <= yMin1 && f40 <= yMin2) continue;
                yMin1 = Math.min((float)yMin1, (float)f39);
                yMin2 = Math.min((float)yMin2, (float)f40);
                if (yMin1 > f11) {
                    yMin1 -= f11;
                }
                if (yMin2 > f11) {
                    yMin2 -= f11;
                }
                flag3 = true;
                float f41 = textureatlassprite1.getInterpolatedU(0.0);
                float f27 = textureatlassprite1.getInterpolatedU(8.0);
                float f28 = textureatlassprite1.getInterpolatedV((double)((1.0f - f39) * 16.0f * 0.5f));
                float f29 = textureatlassprite1.getInterpolatedV((double)((1.0f - f40) * 16.0f * 0.5f));
                float f30 = textureatlassprite1.getInterpolatedV(8.0);
                float vMin1 = textureatlassprite1.getInterpolatedV((double)((1.0f - yMin1) * 16.0f * 0.5f));
                float vMin2 = textureatlassprite1.getInterpolatedV((double)((1.0f - yMin2) * 16.0f * 0.5f));
                int j = blockStateIn.b(blockAccess, blockpos);
                int k = j >> 16 & 0xFFFF;
                int l = j & 0xFFFF;
                float f31 = i1 < 2 ? FaceBakery.getFaceBrightness((EnumFacing)EnumFacing.NORTH) : FaceBakery.getFaceBrightness((EnumFacing)EnumFacing.WEST);
                float f32 = 1.0f * f31 * f;
                float f33 = 1.0f * f31 * f1;
                float f34 = 1.0f * f31 * f2;
                worldRendererIn.pos(d3, d1 + (double)f39, d4).color(f32, f33, f34, 1.0f).tex((double)f41, (double)f28).lightmap(k, l).endVertex();
                worldRendererIn.pos(d5, d1 + (double)f40, d6).color(f32, f33, f34, 1.0f).tex((double)f27, (double)f29).lightmap(k, l).endVertex();
                worldRendererIn.pos(d5, d1 + (double)yMin2, d6).color(f32, f33, f34, 1.0f).tex((double)f27, (double)vMin2).lightmap(k, l).endVertex();
                worldRendererIn.pos(d3, d1 + (double)yMin1, d4).color(f32, f33, f34, 1.0f).tex((double)f41, (double)vMin1).lightmap(k, l).endVertex();
                if (textureatlassprite1 == this.atlasSpriteWaterOverlay) continue;
                worldRendererIn.pos(d3, d1 + (double)yMin1, d4).color(f32, f33, f34, 1.0f).tex((double)f41, (double)vMin1).lightmap(k, l).endVertex();
                worldRendererIn.pos(d5, d1 + (double)yMin2, d6).color(f32, f33, f34, 1.0f).tex((double)f27, (double)vMin2).lightmap(k, l).endVertex();
                worldRendererIn.pos(d5, d1 + (double)f40, d6).color(f32, f33, f34, 1.0f).tex((double)f27, (double)f29).lightmap(k, l).endVertex();
                worldRendererIn.pos(d3, d1 + (double)f39, d4).color(f32, f33, f34, 1.0f).tex((double)f41, (double)f28).lightmap(k, l).endVertex();
            }
            worldRendererIn.setSprite(null);
            boolean bl = flag3;
            return bl;
        }
        finally {
            if (Config.isShaders()) {
                SVertexBuilder.popEntity((BufferBuilder)worldRendererIn);
            }
        }
    }

    private float getFluidHeight(IBlockAccess blockAccess, BlockPos blockPosIn, Material blockMaterial) {
        int i = 0;
        float f = 0.0f;
        for (int j = 0; j < 4; ++j) {
            BlockPos blockpos = blockPosIn.add(-(j & 1), 0, -(j >> 1 & 1));
            if (blockAccess.getBlockState(blockpos.up()).a() == blockMaterial) {
                return 1.0f;
            }
            IBlockState iblockstate = blockAccess.getBlockState(blockpos);
            Material material = iblockstate.a();
            if (material != blockMaterial) {
                if (material.isSolid()) continue;
                f += 1.0f;
                ++i;
                continue;
            }
            int k = (Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL);
            if (k >= 8 || k == 0) {
                f += BlockLiquid.getLiquidHeightPercent((int)k) * 10.0f;
                i += 10;
            }
            f += BlockLiquid.getLiquidHeightPercent((int)k);
            ++i;
        }
        return 1.0f - f / (float)i;
    }
}
