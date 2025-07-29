package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import mods.Hileb.optirefine.optifine.OptifineHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeModContainer;
import net.optifine.BetterSnow;
import net.optifine.CustomColors;
import net.optifine.model.BlockModelCustomizer;
import net.optifine.model.ListQuadsOverlay;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.List;

@Mixin(BlockModelRenderer.class)
public class MixinBlockModelRenderer {
    @Unique
    private static float aoLightValueOpaque = 0.2F;
    @Unique
    private static boolean separateAoLightValue = false;
    @Unique
    private static final BlockRenderLayer[] OVERLAY_LAYERS = new BlockRenderLayer[]{
            BlockRenderLayer.CUTOUT, BlockRenderLayer.CUTOUT_MIPPED, BlockRenderLayer.TRANSLUCENT
    };

    @Inject(method = "<init>", at = @At("RETURN"))
    public void closeForgeLightPipelineAtConstructor(BlockColors p_i46575_1, CallbackInfo ci) {
        ForgeModContainer.forgeLightPipelineEnabled = false;
    }

    @WrapMethod(method = "renderModel(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;ZJ)Z")
    public boolean renderModel(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand, Operation<Boolean> original) {
        boolean flag = Minecraft.isAmbientOcclusionEnabled() && stateIn.getLightValue(worldIn, posIn) == 0 && modelIn.isAmbientOcclusion(stateIn);

        try {
            if (Config.isShaders()) {
                SVertexBuilder.pushEntity(stateIn, posIn, worldIn, buffer);
            }
            if (!Config.isAlternateBlocks()) {
                rand = 0L;
            }
            RenderEnv renderEnv = BufferBuilder_getRenderEnv(buffer, stateIn, posIn);
            modelIn = BlockModelCustomizer.getRenderModel(modelIn, stateIn, renderEnv);
            boolean rendered = flag
                    ? this.renderModelSmooth(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand)
                    : this.renderModelFlat(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand);
            if (rendered) {
                this.renderOverlayModels(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand, renderEnv, flag);
            }

            if (Config.isShaders()) {
                SVertexBuilder.popEntity(buffer);
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, posIn, stateIn);
            crashreportcategory.addCrashSection("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder getRenderEnv (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)Lnet/optifine/render/RenderEnv;", deobf = true)
    private static RenderEnv BufferBuilder_getRenderEnv(BufferBuilder builder, IBlockState blockStateIn, BlockPos blockPosIn) {
        throw new AbstractMethodError();
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder getBlockLayer ()Lnet.minecraft.util.BlockRenderLayer;", deobf = true)
    private static BlockRenderLayer BufferBuilder_getBlockLayer(BufferBuilder builder) {
        throw new AbstractMethodError();
    }

    @Inject(method = "renderModelSmooth", at = @At("HEAD"))
    public void initRenderModelSmooth(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand, CallbackInfoReturnable<Boolean> cir,
                                      @Share(namespace = "optirefine", value = "renderEnv") LocalRef<RenderEnv> envLocalRef,
                                      @Share(namespace = "optirefine", value = "layer") LocalRef<BlockRenderLayer> layerLocalRef
    ) {

        envLocalRef.set(BufferBuilder_getRenderEnv(buffer, stateIn, posIn));
        layerLocalRef.set(BufferBuilder_getBlockLayer(buffer));
    }

    @Redirect(method = "renderModelSmooth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelRenderer;renderQuadsSmooth(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;Ljava/util/List;[FLjava/util/BitSet;Lnet/minecraft/client/renderer/BlockModelRenderer$AmbientOcclusionFace;)V"))
    public void makeCustomQuadsRenderModelSmooth(BlockModelRenderer instance, IBlockAccess k, IBlockState f, BlockPos f1, BufferBuilder f2, List<BakedQuad> bakedquad, float[] j, BitSet bitSet, BlockModelRenderer.AmbientOcclusionFace blockAccessIn,
                                                 @Share(namespace = "optirefine", value = "renderEnv") LocalRef<RenderEnv> envLocalRef,
                                                 @Share(namespace = "optirefine", value = "layer") LocalRef<BlockRenderLayer> layerLocalRef, @Local(argsOnly = true) long rand) {
        bakedquad = BlockModelCustomizer.getRenderQuads(bakedquad, k, f, f1, null, layerLocalRef.get(), rand, envLocalRef.get());
        BlockModelRenderer_renderQuadsSmooth(instance, k, f, f1, f2, bakedquad, envLocalRef.get());
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BlockModelRenderer renderQuadsSmooth (Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;Ljava/util/List;Lnet.optifine.render.RenderEnv;)V", deobf = true)
    private static void BlockModelRenderer_renderQuadsSmooth(BlockModelRenderer instance, IBlockAccess k, IBlockState f, BlockPos f1, BufferBuilder f2, List<BakedQuad> bakedquad, RenderEnv env) {
        throw new AbstractMethodError();
    }


    @Inject(method = "renderModelFlat", at = @At("HEAD"))
    public void initRenderModelFlat(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, boolean checkSides, long rand, CallbackInfoReturnable<Boolean> cir,
                                    @Share(namespace = "optirefine", value = "renderEnv") LocalRef<RenderEnv> envLocalRef,
                                    @Share(namespace = "optirefine", value = "layer") LocalRef<BlockRenderLayer> layerLocalRef
    ) {

        envLocalRef.set(BufferBuilder_getRenderEnv(buffer, stateIn, posIn));
        layerLocalRef.set(BufferBuilder_getBlockLayer(buffer));
    }

    @Redirect(method = "renderModelFlat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockModelRenderer;renderQuadsFlat(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;IZLnet/minecraft/client/renderer/BufferBuilder;Ljava/util/List;Ljava/util/BitSet;)V"))
    public void makeCustomQuadsRenderModelFlat(BlockModelRenderer instance, IBlockAccess diffuse, IBlockState k, BlockPos f, int f1, boolean f2, BufferBuilder
            builder, List<BakedQuad> bakedquad, BitSet j, @Share(namespace = "optirefine", value = "renderEnv") LocalRef<RenderEnv> envLocalRef, @Share(namespace = "optirefine", value = "layer") LocalRef<BlockRenderLayer> layerLocalRef, @Local(argsOnly = true) long rand) {
        bakedquad = BlockModelCustomizer.getRenderQuads(bakedquad, k, f, f1, null, layerLocalRef.get(), rand, envLocalRef.get());
        BlockModelRenderer_renderQuadsFlat(instance, k, f, f1, f2, bakedquad, envLocalRef.get());
    }

    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder isMultiTexture ()Z")
    private static native boolean BufferBuilder_isMultiTexture(BufferBuilder builder);

    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, deobf = true, desc = "net/minecraft/client/renderer/BufferBuilder putColorMultiplierRgba (FFFFI)V")
    private static native void BufferBuilder_putColorMultiplierRgba(BufferBuilder builder, float red, float green, float blue, float alpha, int vertexIndex);

    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, deobf = true, desc = "net/minecraft/client/renderer/BufferBuilder putSprite (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
    private static native void BufferBuilder_putSprite(BufferBuilder builder, TextureAtlasSprite sprite);
    
    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, deobf = true, desc = "net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace field_178206_b [F")
    private static native float[] AmbientOcclusionFace_vertexColorMultiplier(Object instance);

    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.GETFIELD, deobf = true, desc = "net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace field_178207_c [I")
    private static native int[] AmbientOcclusionFace_vertexBrightness(Object instance);

    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, deobf = true, desc = "net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace setMaxBlockLight ()V")
    private static native void AmbientOcclusionFace_setMaxBlockLight(Object instance);

    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, deobf = true, desc = "net.minecraft.client.renderer.BlockModelRenderer$AmbientOcclusionFace func_187491_a (Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;[FLjava/util/BitSet;)V")
    private static native void AmbientOcclusionFace_updateVertexBrightness(Object instance, IBlockAccess worldIn, IBlockState state, BlockPos centerPos, EnumFacing direction, float[] faceShape, BitSet shapeState);


    @Shadow @Final
    private BlockColors blockColors;

    @Unique
    private void renderQuadsSmooth(IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, List<BakedQuad> list, RenderEnv renderEnv) {
        float[] quadBounds = renderEnv.getQuadBounds();
        BitSet bitSet = renderEnv.getBoundsFlags();
        var aoFace = renderEnv.getAoFace();
        Vec3d vec3d = stateIn.f(blockAccessIn, posIn);
        double d0 = posIn.getX() + vec3d.x;
        double d1 = posIn.getY() + vec3d.y;
        double d2 = posIn.getZ() + vec3d.z;
        int i = 0;

        for (int j = list.size(); i < j; i++) {
            BakedQuad bakedquad = list.get(i);
            this.fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), quadBounds, bitSet);
            AmbientOcclusionFace_updateVertexBrightness(aoFace, blockAccessIn, stateIn, posIn, bakedquad.getFace(), quadBounds, bitSet);
            if (bakedquad.getSprite().isEmissive) {
                AmbientOcclusionFace_setMaxBlockLight(aoFace);
            }
            if (BufferBuilder_isMultiTexture(buffer)) {
                buffer.addVertexData(bakedquad.getVertexDataSingle());
            } else {
                buffer.addVertexData(bakedquad.getVertexData());
            }
        }
        BufferBuilder_putSprite(buffer, bakedquad.getSprite());
        buffer.putBrightness4(AmbientOcclusionFace_vertexBrightness(aoFace)[0], AmbientOcclusionFace_vertexBrightness(aoFace)[1], AmbientOcclusionFace_vertexBrightness(aoFace)[2], AmbientOcclusionFace_vertexBrightness(aoFace)[3]);
        if (bakedquad.shouldApplyDiffuseLighting()) {
            float diffuse = OptifineHelper.getFaceBrightness(bakedquad.getFace());
            AmbientOcclusionFace_vertexColorMultiplier(aoFace)[0] *= diffuse;
            AmbientOcclusionFace_vertexColorMultiplier(aoFace)[1] *= diffuse;
            AmbientOcclusionFace_vertexColorMultiplier(aoFace)[2] *= diffuse;
            AmbientOcclusionFace_vertexColorMultiplier(aoFace)[3] *= diffuse;
        }
        int colorMultiplier = CustomColors.getColorMultiplier(bakedquad, stateIn, blockAccessIn, posIn, renderEnv);
        if (bakedquad.hasTintIndex() || colorMultiplier != -1) {
            int k = colorMultiplier;
            if (colorMultiplier == -1) {
                k = this.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());
            }
            if (EntityRenderer.anaglyphEnable) {
                k = TextureUtil.anaglyphColor(k);
            }
            float f = (k >> 16 & 0xFF) / 255.0F;
            float f1 = (k >> 8 & 0xFF) / 255.0F;
            float f2 = (k & 0xFF) / 255.0F;
            if (separateAoLightValue) {
                BufferBuilder_putColorMultiplierRgba(buffer, f, f1, f2, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[0], 4);
                BufferBuilder_putColorMultiplierRgba(buffer, f, f1, f2, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[1], 3);
                BufferBuilder_putColorMultiplierRgba(buffer, f, f1, f2, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[2], 2);
                BufferBuilder_putColorMultiplierRgba(buffer, f, f1, f2, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[3], 1);
            } else {
                buffer.putColorMultiplier(AmbientOcclusionFace_vertexColorMultiplier(aoFace)[0] * f, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[0] * f1, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[0] * f2, 4);
                buffer.putColorMultiplier(AmbientOcclusionFace_vertexColorMultiplier(aoFace)[1] * f, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[1] * f1, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[1] * f2, 3);
                buffer.putColorMultiplier(AmbientOcclusionFace_vertexColorMultiplier(aoFace)[2] * f, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[2] * f1, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[2] * f2, 2);
                buffer.putColorMultiplier(AmbientOcclusionFace_vertexColorMultiplier(aoFace)[3] * f, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[3] * f1, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[3] * f2, 1);
            }
        } else if (separateAoLightValue) {
            BufferBuilder_putColorMultiplierRgba(buffer, 1.0F, 1.0F, 1.0F, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[0], 4);
            BufferBuilder_putColorMultiplierRgba(buffer, 1.0F, 1.0F, 1.0F, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[1], 3);
            BufferBuilder_putColorMultiplierRgba(buffer, 1.0F, 1.0F, 1.0F, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[2], 2);
            BufferBuilder_putColorMultiplierRgba(buffer, 1.0F, 1.0F, 1.0F, AmbientOcclusionFace_vertexColorMultiplier(aoFace)[3], 1);
        } else {
            buffer.putColorMultiplier(AmbientOcclusionFace_vertexColorMultiplier(aoFace)[0], AmbientOcclusionFace_vertexColorMultiplier(aoFace)[0], AmbientOcclusionFace_vertexColorMultiplier(aoFace)[0], 4);
            buffer.putColorMultiplier(AmbientOcclusionFace_vertexColorMultiplier(aoFace)[1], AmbientOcclusionFace_vertexColorMultiplier(aoFace)[1], AmbientOcclusionFace_vertexColorMultiplier(aoFace)[1], 3);
            buffer.putColorMultiplier(AmbientOcclusionFace_vertexColorMultiplier(aoFace)[2], AmbientOcclusionFace_vertexColorMultiplier(aoFace)[2], AmbientOcclusionFace_vertexColorMultiplier(aoFace)[2], 2);
            buffer.putColorMultiplier(AmbientOcclusionFace_vertexColorMultiplier(aoFace)[3], AmbientOcclusionFace_vertexColorMultiplier(aoFace)[3], AmbientOcclusionFace_vertexColorMultiplier(aoFace)[3], 1);
        }
        buffer.putPosition(d0, d1, d2);
    }

    @Unique
    private void fillQuadBounds(IBlockState stateIn, int[] vertexData, EnumFacing face, @Nullable float[] quadBounds, BitSet boundsFlags) {
        float f = 32.0F;
        float f1 = 32.0F;
        float f2 = 32.0F;
        float f3 = -32.0F;
        float f4 = -32.0F;
        float f5 = -32.0F;
        int step = vertexData.length / 4;
        
        for (int i = 0; i < 4; i++) {
            float f6 = Float.intBitsToFloat(vertexData[i * step]);
            float f7 = Float.intBitsToFloat(vertexData[i * step + 1]);
            float f8 = Float.intBitsToFloat(vertexData[i * step + 2]);
            f = Math.min(f, f6);
            f1 = Math.min(f1, f7);
            f2 = Math.min(f2, f8);
            f3 = Math.max(f3, f6);
            f4 = Math.max(f4, f7);
            f5 = Math.max(f5, f8);
        }

        if (quadBounds != null) {
            quadBounds[4] = f;  //WEST
            quadBounds[5] = f3; //EAST
            quadBounds[0] = f1; //DOWN
            quadBounds[1] = f4; //UP
            quadBounds[2] = f2; //NORTH
            quadBounds[3] = f5; //SOUTH
            final int j = 6; //EnumFacing.VALUES.length
            quadBounds[4 + j] = 1.0F - f; //WEST2
            quadBounds[5 + j] = 1.0F - f3; //EAST2
            quadBounds[0 + j] = 1.0F - f1; //DOWN2
            quadBounds[1 + j] = 1.0F - f4; //UP2
            quadBounds[52+ j] = 1.0F - f2; //NORTH2
            quadBounds[3 + j] = 1.0F - f5; //SOUTH2
        }

        final float f9 = 1.0E-4F;
        float f10 = 0.9999F;
        switch (face) {
            case DOWN:
                boundsFlags.set(1, f >= f9 || f2 >= f9 || f3 <= f10 || f5 <= f10);
                boundsFlags.set(0, (f1 < f9 || stateIn.g()) && f1 == f4);
                break;
            case UP:
                boundsFlags.set(1, f >= f9 || f2 >= f9 || f3 <= f10 || f5 <= f10);
                boundsFlags.set(0, (f4 > f10 || stateIn.g()) && f1 == f4);
                break;
            case NORTH:
                boundsFlags.set(1, f >= f9 || f1 >= f9 || f3 <= f10 || f4 <= f10);
                boundsFlags.set(0, (f2 < f9 || stateIn.g()) && f2 == f5);
                break;
            case SOUTH:
                boundsFlags.set(1, f >= f9 || f1 >= f9 || f3 <= f10 || f4 <= f10);
                boundsFlags.set(0, (f5 > f10 || stateIn.g()) && f2 == f5);
                break;
            case WEST:
                boundsFlags.set(1, f1 >= f9 || f2 >= f9 || f4 <= f10 || f5 <= f10);
                boundsFlags.set(0, (f < f9 || stateIn.g()) && f == f3);
                break;
            case EAST:
                boundsFlags.set(1, f1 >= f9 || f2 >= f9 || f4 <= f10 || f5 <= f10);
                boundsFlags.set(0, (f3 > f10 || stateIn.g()) && f == f3);
        }
    }

    @Unique
    private void renderQuadsFlat(
            IBlockAccess blockAccessIn,
            IBlockState stateIn,
            BlockPos posIn,
            int brightnessIn,
            boolean ownBrightness,
            BufferBuilder buffer,
            List<BakedQuad> list,
            RenderEnv renderEnv
    ) {
        BitSet bitSet = renderEnv.getBoundsFlags();
        Vec3d vec3d = stateIn.f(blockAccessIn, posIn);
        double d0 = posIn.getX() + vec3d.x;
        double d1 = posIn.getY() + vec3d.y;
        double d2 = posIn.getZ() + vec3d.z;
        int i = 0;

        for (int j = list.size(); i < j; i++) {
            BakedQuad bakedquad = list.get(i);
            if (ownBrightness) {
                this.fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), null, bitSet);
                BlockPos blockpos = bitSet.get(0) ? posIn.offset(bakedquad.getFace()) : posIn;
                brightnessIn = stateIn.getLightValue(blockAccessIn, blockpos);
            }

            if (bakedquad.getSprite().isEmissive) {
                brightnessIn |= 240;
            }

            if (buffer.isMultiTexture()) {
                buffer.addVertexData(bakedquad.getVertexDataSingle());
            } else {
                buffer.addVertexData(bakedquad.getVertexData());
            }
            
            BufferBuilder_putSprite(buffer, bakedquad.getSprite());
            buffer.putBrightness4(brightnessIn, brightnessIn, brightnessIn, brightnessIn);
            int colorMultiplier = CustomColors.getColorMultiplier(bakedquad, stateIn, blockAccessIn, posIn, renderEnv);
            if (bakedquad.hasTintIndex() || colorMultiplier != -1) {
                int k = colorMultiplier;
                if (colorMultiplier == -1) {
                    k = this.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());
                }

                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }

                float f = (k >> 16 & 0xFF) / 255.0F;
                float f1 = (k >> 8 & 0xFF) / 255.0F;
                float f2 = (k & 0xFF) / 255.0F;
                if (bakedquad.shouldApplyDiffuseLighting()) {
                    float diffuse = OptifineHelper.getFaceBrightness(bakedquad.getFace());
                    f *= diffuse;
                    f1 *= diffuse;
                    f2 *= diffuse;
                }

                buffer.putColorMultiplier(f, f1, f2, 4);
                buffer.putColorMultiplier(f, f1, f2, 3);
                buffer.putColorMultiplier(f, f1, f2, 2);
                buffer.putColorMultiplier(f, f1, f2, 1);
            } else if (bakedquad.shouldApplyDiffuseLighting()) {
                float diffuse = FaceBakery.getFaceBrightness(bakedquad.getFace());
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 4);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 3);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 2);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 1);
            }

            buffer.putPosition(d0, d1, d2);
        }
    }

    @Unique @Public
    private static float fixAoLightValue(float val) {
        return val == 0.2F ? aoLightValueOpaque : val;
    }

    @Unique @Public
    private static void updateAoLightValue() {
        aoLightValueOpaque = 1.0F - Config.getAmbientOcclusionLevel() * 0.8F;
        separateAoLightValue = Config.isShaders() && Shaders.isSeparateAo();
    }

    @Unique
    private void renderOverlayModels(
            IBlockAccess worldIn,
            IBakedModel modelIn,
            IBlockState stateIn,
            BlockPos posIn,
            BufferBuilder buffer,
            boolean checkSides,
            long rand,
            RenderEnv renderEnv,
            boolean smooth
    ) {
        if (renderEnv.isOverlaysRendered()) {
            for (int l = 0; l < OVERLAY_LAYERS.length; l++) {
                BlockRenderLayer layer = OVERLAY_LAYERS[l];
                ListQuadsOverlay listQuadsOverlay = renderEnv.getListQuadsOverlay(layer);
                if (listQuadsOverlay.size() > 0) {
                    RegionRenderCacheBuilder rrcb = renderEnv.getRegionRenderCacheBuilder();
                    if (rrcb != null) {
                        BufferBuilder overlayBuffer = rrcb.getWorldRendererByLayer(layer);
                        if (!overlayBuffer.isDrawing()) {
                            overlayBuffer.begin(7, DefaultVertexFormats.BLOCK);
                            overlayBuffer.setTranslation(buffer.getXOffset(), buffer.getYOffset(), buffer.getZOffset());
                        }

                        for (int q = 0; q < listQuadsOverlay.size(); q++) {
                            BakedQuad quad = listQuadsOverlay.getQuad(q);
                            List<BakedQuad> listQuadSingle = listQuadsOverlay.getListQuadsSingle(quad);
                            IBlockState quadBlockState = listQuadsOverlay.getBlockState(q);
                            if (quad.getQuadEmissive() != null) {
                                listQuadsOverlay.addQuad(quad.getQuadEmissive(), quadBlockState);
                            }

                            renderEnv.reset(quadBlockState, posIn);
                            if (smooth) {
                                this.renderQuadsSmooth(worldIn, quadBlockState, posIn, overlayBuffer, listQuadSingle, renderEnv);
                            } else {
                                int col = quadBlockState.b(worldIn, posIn.offset(quad.getFace()));
                                this.renderQuadsFlat(worldIn, quadBlockState, posIn, col, false, overlayBuffer, listQuadSingle, renderEnv);
                            }
                        }
                    }

                    listQuadsOverlay.clear();
                }
            }
        }

        if (Config.isBetterSnow() && !renderEnv.isBreakingAnimation() && BetterSnow.shouldRender(worldIn, stateIn, posIn)) {
            IBakedModel modelSnow = BetterSnow.getModelSnowLayer();
            IBlockState stateSnow = BetterSnow.getStateSnowLayer();
            ((BlockModelRenderer)(Object)this).renderModel(worldIn, modelSnow, stateSnow, posIn, buffer, checkSides, rand);
        }
    }
}