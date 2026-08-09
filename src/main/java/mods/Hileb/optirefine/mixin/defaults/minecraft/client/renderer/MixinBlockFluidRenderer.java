package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;


import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.optifine.CustomColors;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;

@SuppressWarnings("unused")
@Mixin(BlockFluidRenderer.class)
public abstract class MixinBlockFluidRenderer {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0

    // ===== shader-aware face brightness (OF FaceBakery.getFaceBrightness is public static) =====
    // [AUDIT-FIXED 2026-08-09] three-way runtime audit: the port used OptifineHelper fixed constants;
    // OF delegates to FaceBakery.getFaceBrightness (Shaders.blockLightLevel05/08/06 via MixinFaceBakery).

    @Unique
    private static FaceBakery optiRefine$faceBakery;

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/block/model/FaceBakery func_178412_b (Lnet/minecraft/util/EnumFacing;)F", deobf = true)
    private static native float FaceBakery_getFaceBrightness(FaceBakery instance, EnumFacing facing);

    @Unique
    private static float optiRefine$faceBrightness(EnumFacing facing) {
        if (optiRefine$faceBakery == null) {
            optiRefine$faceBakery = new FaceBakery();
        }
        return FaceBakery_getFaceBrightness(optiRefine$faceBakery, facing);
    }

    @Shadow @Final
// [AUDIT-OK] baseline member atlasSpritesLava (SRG field_178272_a)
    private TextureAtlasSprite[] atlasSpritesLava;

    @Shadow @Final
// [AUDIT-OK] baseline member atlasSpritesWater (SRG field_178271_b)
    private TextureAtlasSprite[] atlasSpritesWater;

    @Shadow
// [AUDIT-OK] baseline member atlasSpriteWaterOverlay (SRG field_187501_d)
    private TextureAtlasSprite atlasSpriteWaterOverlay;

    @Shadow
// [AUDIT-OK] baseline member getFluidHeight (SRG func_178269_b), declared in BlockFluidRenderer
    protected abstract float getFluidHeight(IBlockAccess blockAccessIn, BlockPos posIn, Material materialIn);

    
    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder getRenderEnv (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)Lnet/optifine/render/RenderEnv;", deobf = true)
// [AUDIT-OK] OF member BufferBuilder.getRenderEnv; deobf=true no-op for OF name
    private static RenderEnv BufferBuilder_getRenderEnv(BufferBuilder builder, IBlockState blockStateIn, BlockPos blockPosIn){
        throw new AbstractMethodError();
    }

    
    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder setSprite (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
// [AUDIT-OK] OF member BufferBuilder.setSprite
    private static void BufferBuilder_setSprite(BufferBuilder builder, TextureAtlasSprite sprite){
        throw new AbstractMethodError();
    }

    @WrapMethod(method = "renderFluid")
// [AUDIT-FIXED] full OF renderFluid port (patch/optifine BlockFluidRenderer):
// - per-face side setSprite (textureatlassprite1 = atextureatlassprite[1]) inside the 4-face loop
// - farmland/GRASS_PATH neighbor -> wall bottom 0.9375F, bottom-half slab -> 0.5F
// - wall skipped when both edge heights sit at/below the neighbor surface (guard !(h1<=surf1)||!(h2<=surf2)),
//   bottoms at min(surf, edge) - 0.001 (guarded epsilon)
// - bottom face tinted with CustomColors.getFluidColor * FaceBakery.getFaceBrightness(DOWN)
//   (via OptifineHelper; compile-time vanilla FaceBakery is private instance) instead of fixed 0.5 gray
// - lava skips every adjustment (Material.LAVA gate)
// - overlay keeps cleanroom's getBlockFaceShape==SOLID check (superset of OF's BEACON/SLIME_BLOCK)
    public boolean wrap_renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn, Operation<Boolean> original) {
        boolean rendered;

        try {
            if (Config.isShaders()) {
                SVertexBuilder.pushEntity(blockStateIn, blockPosIn, blockAccess, worldRendererIn);
            }

            BlockLiquid blockliquid = (BlockLiquid) blockStateIn.getBlock();
            boolean isLava = blockStateIn.getMaterial() == Material.LAVA;
            TextureAtlasSprite[] atlasSprites = isLava ? this.atlasSpritesLava : this.atlasSpritesWater;
            RenderEnv renderEnv = BufferBuilder_getRenderEnv(worldRendererIn, blockStateIn, blockPosIn);
            int fluidColor = CustomColors.getFluidColor(blockAccess, blockStateIn, blockPosIn, renderEnv);
            float colorRed = (float) (fluidColor >> 16 & 255) / 255.0F;
            float colorGreen = (float) (fluidColor >> 8 & 255) / 255.0F;
            float colorBlue = (float) (fluidColor & 255) / 255.0F;
            boolean flagUp = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.UP);
            boolean flagDown = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.DOWN);
            boolean[] borderFlags = renderEnv.getBorderFlags();
            borderFlags[0] = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.NORTH);
            borderFlags[1] = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.SOUTH);
            borderFlags[2] = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.WEST);
            borderFlags[3] = blockStateIn.shouldSideBeRendered(blockAccess, blockPosIn, EnumFacing.EAST);

            if (flagUp || flagDown || borderFlags[0] || borderFlags[1] || borderFlags[2] || borderFlags[3]) {
                rendered = false;
                Material material = blockStateIn.getMaterial();
                float f7 = this.getFluidHeight(blockAccess, blockPosIn, material);
                float f8 = this.getFluidHeight(blockAccess, blockPosIn.south(), material);
                float f9 = this.getFluidHeight(blockAccess, blockPosIn.east().south(), material);
                float f10 = this.getFluidHeight(blockAccess, blockPosIn.east(), material);
                double d0 = (double) blockPosIn.getX();
                double d1 = (double) blockPosIn.getY();
                double d2 = (double) blockPosIn.getZ();
                float f11 = 0.001F;

                if (flagUp) {
                    rendered = true;
                    float f12 = BlockLiquid.getSlopeAngle(blockAccess, blockPosIn, material, blockStateIn);
                    TextureAtlasSprite textureatlassprite = f12 > -999.0F ? atlasSprites[1] : atlasSprites[0];
                    BufferBuilder_setSprite(worldRendererIn, textureatlassprite);
                    f7 -= 0.001F;
                    f8 -= 0.001F;
                    f9 -= 0.001F;
                    f10 -= 0.001F;
                    float f13;
                    float f14;
                    float f15;
                    float f16;
                    float f17;
                    float f18;
                    float f19;
                    float f20;

                    if (f12 < -999.0F) {
                        f13 = textureatlassprite.getInterpolatedU(0.0D);
                        f17 = textureatlassprite.getInterpolatedV(0.0D);
                        f14 = f13;
                        f18 = textureatlassprite.getInterpolatedV(16.0D);
                        f15 = textureatlassprite.getInterpolatedU(16.0D);
                        f19 = f18;
                        f16 = f15;
                        f20 = f17;
                    } else {
                        float f21 = MathHelper.sin(f12) * 0.25F;
                        float f22 = MathHelper.cos(f12) * 0.25F;
                        f13 = textureatlassprite.getInterpolatedU((double) (8.0F + (-f22 - f21) * 16.0F));
                        f17 = textureatlassprite.getInterpolatedV((double) (8.0F + (-f22 + f21) * 16.0F));
                        f14 = textureatlassprite.getInterpolatedU((double) (8.0F + (-f22 + f21) * 16.0F));
                        f18 = textureatlassprite.getInterpolatedV((double) (8.0F + (f22 + f21) * 16.0F));
                        f15 = textureatlassprite.getInterpolatedU((double) (8.0F + (f22 + f21) * 16.0F));
                        f19 = textureatlassprite.getInterpolatedV((double) (8.0F + (f22 - f21) * 16.0F));
                        f16 = textureatlassprite.getInterpolatedU((double) (8.0F + (f22 - f21) * 16.0F));
                        f20 = textureatlassprite.getInterpolatedV((double) (8.0F + (-f22 - f21) * 16.0F));
                    }

                    int k2 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn);
                    int l2 = k2 >> 16 & 65535;
                    int i3 = k2 & 65535;
                    float f24 = 1.0F * colorRed;
                    float f25 = 1.0F * colorGreen;
                    float f26 = 1.0F * colorBlue;
                    worldRendererIn.pos(d0 + 0.0D, d1 + (double) f7, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double) f13, (double) f17).lightmap(l2, i3).endVertex();
                    worldRendererIn.pos(d0 + 0.0D, d1 + (double) f8, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double) f14, (double) f18).lightmap(l2, i3).endVertex();
                    worldRendererIn.pos(d0 + 1.0D, d1 + (double) f9, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double) f15, (double) f19).lightmap(l2, i3).endVertex();
                    worldRendererIn.pos(d0 + 1.0D, d1 + (double) f10, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double) f16, (double) f20).lightmap(l2, i3).endVertex();

                    if (blockliquid.shouldRenderSides(blockAccess, blockPosIn.up())) {
                        worldRendererIn.pos(d0 + 0.0D, d1 + (double) f7, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double) f13, (double) f17).lightmap(l2, i3).endVertex();
                        worldRendererIn.pos(d0 + 1.0D, d1 + (double) f10, d2 + 0.0D).color(f24, f25, f26, 1.0F).tex((double) f16, (double) f20).lightmap(l2, i3).endVertex();
                        worldRendererIn.pos(d0 + 1.0D, d1 + (double) f9, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double) f15, (double) f19).lightmap(l2, i3).endVertex();
                        worldRendererIn.pos(d0 + 0.0D, d1 + (double) f8, d2 + 1.0D).color(f24, f25, f26, 1.0F).tex((double) f14, (double) f18).lightmap(l2, i3).endVertex();
                    }
                }

                if (flagDown) {
                    BufferBuilder_setSprite(worldRendererIn, atlasSprites[0]);
                    float f35 = atlasSprites[0].getMinU();
                    float f36 = atlasSprites[0].getMaxU();
                    float f37 = atlasSprites[0].getMinV();
                    float f38 = atlasSprites[0].getMaxV();
                    int l1 = blockStateIn.getPackedLightmapCoords(blockAccess, blockPosIn.down());
                    int i2 = l1 >> 16 & 65535;
                    int j2 = l1 & 65535;
                    float faceBrightnessDown = optiRefine$faceBrightness(EnumFacing.DOWN);
                    worldRendererIn.pos(d0, d1, d2 + 1.0D).color(colorRed * faceBrightnessDown, colorGreen * faceBrightnessDown, colorBlue * faceBrightnessDown, 1.0F).tex((double) f35, (double) f38).lightmap(i2, j2).endVertex();
                    worldRendererIn.pos(d0, d1, d2).color(colorRed * faceBrightnessDown, colorGreen * faceBrightnessDown, colorBlue * faceBrightnessDown, 1.0F).tex((double) f35, (double) f37).lightmap(i2, j2).endVertex();
                    worldRendererIn.pos(d0 + 1.0D, d1, d2).color(colorRed * faceBrightnessDown, colorGreen * faceBrightnessDown, colorBlue * faceBrightnessDown, 1.0F).tex((double) f36, (double) f37).lightmap(i2, j2).endVertex();
                    worldRendererIn.pos(d0 + 1.0D, d1, d2 + 1.0D).color(colorRed * faceBrightnessDown, colorGreen * faceBrightnessDown, colorBlue * faceBrightnessDown, 1.0F).tex((double) f36, (double) f38).lightmap(i2, j2).endVertex();
                    rendered = true;
                }

                for (int i1 = 0; i1 < 4; i1++) {
                    int j1 = 0;
                    int k1 = 0;

                    if (i1 == 0) {
                        k1--;
                    }

                    if (i1 == 1) {
                        k1++;
                    }

                    if (i1 == 2) {
                        j1--;
                    }

                    if (i1 == 3) {
                        j1++;
                    }

                    BlockPos blockpos = blockPosIn.add(j1, 0, k1);
                    TextureAtlasSprite textureatlassprite1 = atlasSprites[1];
                    BufferBuilder_setSprite(worldRendererIn, textureatlassprite1);
                    float f40 = 0.0F;
                    float f41 = 0.0F;

                    if (!isLava) {
                        IBlockState iblockstate = blockAccess.getBlockState(blockpos);
                        Block block = iblockstate.getBlock();

                        if (iblockstate.getBlockFaceShape(blockAccess, blockpos, EnumFacing.VALUES[i1 + 2].getOpposite()) == BlockFaceShape.SOLID) {
                            textureatlassprite1 = this.atlasSpriteWaterOverlay;
                            BufferBuilder_setSprite(worldRendererIn, textureatlassprite1);
                        }

                        if (block == Blocks.FARMLAND || block == Blocks.GRASS_PATH) {
                            f40 = 0.9375F;
                            f41 = 0.9375F;
                        }

                        if (block instanceof BlockSlab) {
                            BlockSlab blockslab = (BlockSlab) block;

                            if (!blockslab.isDouble() && iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM) {
                                f40 = 0.5F;
                                f41 = 0.5F;
                            }
                        }
                    }

                    if (borderFlags[i1]) {
                        float f42;
                        float f43;
                        double d3;
                        double d4;
                        double d5;
                        double d6;

                        if (i1 == 0) {
                            f42 = f7;
                            f43 = f10;
                            d3 = d0;
                            d5 = d0 + 1.0D;
                            d4 = d2 + 0.0010000000474974513D;
                            d6 = d2 + 0.0010000000474974513D;
                        } else if (i1 == 1) {
                            f42 = f9;
                            f43 = f8;
                            d3 = d0 + 1.0D;
                            d5 = d0;
                            d4 = d2 + 1.0D - 0.0010000000474974513D;
                            d6 = d2 + 1.0D - 0.0010000000474974513D;
                        } else if (i1 == 2) {
                            f42 = f8;
                            f43 = f7;
                            d3 = d0 + 0.0010000000474974513D;
                            d5 = d0 + 0.0010000000474974513D;
                            d4 = d2 + 1.0D;
                            d6 = d2;
                        } else {
                            f42 = f10;
                            f43 = f9;
                            d3 = d0 + 1.0D - 0.0010000000474974513D;
                            d5 = d0 + 1.0D - 0.0010000000474974513D;
                            d4 = d2;
                            d6 = d2 + 1.0D;
                        }

                        if (!(f42 <= f40) || !(f43 <= f41)) {
                            f40 = Math.min(f40, f42);
                            f41 = Math.min(f41, f43);

                            if (f40 > f11) {
                                f40 -= f11;
                            }

                            if (f41 > f11) {
                                f41 -= f11;
                            }

                            rendered = true;
                            float f44 = textureatlassprite1.getInterpolatedU(0.0D);
                            float f27 = textureatlassprite1.getInterpolatedU(8.0D);
                            float f28 = textureatlassprite1.getInterpolatedV((double) ((1.0F - f42) * 16.0F * 0.5F));
                            float f29 = textureatlassprite1.getInterpolatedV((double) ((1.0F - f43) * 16.0F * 0.5F));
                            float f30 = textureatlassprite1.getInterpolatedV((double) ((1.0F - f40) * 16.0F * 0.5F));
                            float f31 = textureatlassprite1.getInterpolatedV((double) ((1.0F - f41) * 16.0F * 0.5F));
                            int j = blockStateIn.getPackedLightmapCoords(blockAccess, blockpos);
                            int k = j >> 16 & 65535;
                            int l = j & 65535;
                            float f32 = i1 < 2 ? optiRefine$faceBrightness(EnumFacing.NORTH) : optiRefine$faceBrightness(EnumFacing.WEST);
                            float f33 = 1.0F * f32 * colorRed;
                            float f34 = 1.0F * f32 * colorGreen;
                            float f35 = 1.0F * f32 * colorBlue;
                            worldRendererIn.pos(d3, d1 + (double) f42, d4).color(f33, f34, f35, 1.0F).tex((double) f44, (double) f28).lightmap(k, l).endVertex();
                            worldRendererIn.pos(d5, d1 + (double) f43, d6).color(f33, f34, f35, 1.0F).tex((double) f27, (double) f29).lightmap(k, l).endVertex();
                            worldRendererIn.pos(d5, d1 + (double) f41, d6).color(f33, f34, f35, 1.0F).tex((double) f27, (double) f31).lightmap(k, l).endVertex();
                            worldRendererIn.pos(d3, d1 + (double) f40, d4).color(f33, f34, f35, 1.0F).tex((double) f44, (double) f30).lightmap(k, l).endVertex();

                            if (textureatlassprite1 != this.atlasSpriteWaterOverlay) {
                                worldRendererIn.pos(d3, d1 + (double) f40, d4).color(f33, f34, f35, 1.0F).tex((double) f44, (double) f30).lightmap(k, l).endVertex();
                                worldRendererIn.pos(d5, d1 + (double) f41, d6).color(f33, f34, f35, 1.0F).tex((double) f27, (double) f31).lightmap(k, l).endVertex();
                                worldRendererIn.pos(d5, d1 + (double) f43, d6).color(f33, f34, f35, 1.0F).tex((double) f27, (double) f29).lightmap(k, l).endVertex();
                                worldRendererIn.pos(d3, d1 + (double) f42, d4).color(f33, f34, f35, 1.0F).tex((double) f44, (double) f28).lightmap(k, l).endVertex();
                            }
                        }
                    }
                }

                BufferBuilder_setSprite(worldRendererIn, null);
                return rendered;
            }

            rendered = false;
        } finally {
            if (Config.isShaders()) {
                SVertexBuilder.popEntity(worldRendererIn);
            }
        }

        return rendered;
    }

}
