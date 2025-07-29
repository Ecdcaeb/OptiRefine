package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.CustomColors;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockFluidRenderer.class)
public abstract class MixinBlockFluidRenderer {

    @Shadow @Final
    private TextureAtlasSprite[] atlasSpritesLava;

    @Shadow @Final
    private TextureAtlasSprite[] atlasSpritesWater;

    @Shadow
    private TextureAtlasSprite atlasSpriteWaterOverlay;

    @Inject(method = "renderFluid", at = @At("HEAD"))
    public void inject_renderFluid_getRenderEnv(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn, CallbackInfoReturnable<Boolean> cir, @Share("renderEnv") LocalRef<RenderEnv> env) {
        env.set(BufferBuilder_getRenderEnv(worldRendererIn, blockStateIn, blockPosIn));
    }

    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder getRenderEnv (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)Lnet/optifine/render/RenderEnv;", deobf = true)
    private static RenderEnv BufferBuilder_getRenderEnv(BufferBuilder builder, IBlockState blockStateIn, BlockPos blockPosIn){
        throw new AbstractMethodError();
    }

    @Redirect(method = "renderFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/color/BlockColors;colorMultiplier(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;I)I"))
    public int getCustomColor(BlockColors instance, IBlockState blockStateIn, IBlockAccess blockAccess, BlockPos blockPosIn, int val, @Share("renderEnv") LocalRef<RenderEnv> env){
        return CustomColors.getFluidColor(blockAccess, blockStateIn, blockPosIn, env.get());
    }

    @Inject(method = "renderFluid", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/BlockFluidRenderer;atlasSpriteWaterOverlay:Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    public void onSet(IBlockAccess p_178270_1_, IBlockState p_178270_2_, BlockPos p_178270_3_, BufferBuilder p_178270_4_, CallbackInfoReturnable<Boolean> cir){
        BufferBuilder_setSprite(p_178270_4_, this.atlasSpriteWaterOverlay);
    }

    @SuppressWarnings("all")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder setSprite (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
    private static void BufferBuilder_setSprite(BufferBuilder builder, TextureAtlasSprite sprite){
        throw new AbstractMethodError();
    }


    @Inject(method = "renderFluid", at = @At("HEAD"))
    public void init_renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder bufferBuilderIn, CallbackInfoReturnable<Boolean> cir,
                                 @Share(namespace = "optirefine", value = "renderEnv") LocalRef<RenderEnv> renderEnv){
        renderEnv.set(BufferBuilder_getRenderEnv(bufferBuilderIn, blockStateIn, blockPosIn));
    }

    @Definition(id = "textureatlassprite", local = @Local(type = TextureAtlasSprite.class))
    @Expression("textureatlassprite = @(?)")
    @ModifyExpressionValue(method = "renderFluid", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    public TextureAtlasSprite hookAtSetTextureAtlasSprite_renderFluid(TextureAtlasSprite original, @Local(argsOnly = true) BufferBuilder builder) {
        BufferBuilder_setSprite(builder, original);
        return original;
    }

    @Definition(id = " atextureatlassprite", local = @Local(type = TextureAtlasSprite[].class))
    @Expression("@(atextureatlassprite[0]).?")
    @ModifyExpressionValue(method = "renderFluid", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public TextureAtlasSprite hookAtUseZeroTextureAtlasSprite_renderFluid(TextureAtlasSprite or,
                                                            @Local BufferBuilder builder,
                                                            @Share(namespace = "optirefine", value = "fbr") LocalRef<Float> fbr) {
        BufferBuilder_setSprite(builder, or);
        fbr.set(0.5F);
        return or;
    }

    @Redirect(method = "renderFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;color(FFFF)Lnet/minecraft/client/renderer/BufferBuilder;"))
    public BufferBuilder remapColor_renderFluid(BufferBuilder instance, float red, float green, float blue, float alpha,  @Share(namespace = "optirefine", value = "fbr") LocalRef<Float> fbr){
        if (fbr.get() == null) {
            return instance.color(red, green, blue, alpha);
        } else {
            float fbrf = fbr.get();
            return instance.color(red * fbrf, green * fbrf, blue * fbrf, alpha);
        }
    }


    @WrapMethod(method = "renderFluid")
    public boolean wrap_renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn, Operation<Boolean> original){
        boolean value;
        try {
            if (Config.isShaders()) {
                SVertexBuilder.pushEntity(blockStateIn, blockPosIn, blockAccess, worldRendererIn);
            }
            value = original.call(blockAccess, blockStateIn, blockPosIn, worldRendererIn);
        } finally {
            if (Config.isShaders()) {
                SVertexBuilder.popEntity(worldRendererIn);
            }
        }
        return value;
    }

}
