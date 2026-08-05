package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;


import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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

@SuppressWarnings("unused")
@Mixin(BlockFluidRenderer.class)
public abstract class MixinBlockFluidRenderer {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0

    @Shadow @Final
// [AUDIT-OK] baseline member atlasSpritesLava (SRG field_178272_a)
    private TextureAtlasSprite[] atlasSpritesLava;

    @Shadow @Final
// [AUDIT-OK] baseline member atlasSpritesWater (SRG field_178271_b)
    private TextureAtlasSprite[] atlasSpritesWater;

    @Shadow
// [AUDIT-OK] baseline member atlasSpriteWaterOverlay (SRG field_187501_d)
    private TextureAtlasSprite atlasSpriteWaterOverlay;

    @Inject(method = "renderFluid", at = @At("HEAD"))
    public void inject_renderFluid_getRenderEnv(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn, CallbackInfoReturnable<Boolean> cir, @Share("renderEnv") LocalRef<RenderEnv> env) {
// [AUDIT-OK] target renderFluid(LIBlockAccess;LIBlockState;LBlockPos;LBufferBuilder;)Z (SRG func_178270_a) matches baseline
        env.set(BufferBuilder_getRenderEnv(worldRendererIn, blockStateIn, blockPosIn));
    }

    
    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder getRenderEnv (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)Lnet/optifine/render/RenderEnv;", deobf = true)
// [AUDIT-OK] OF member BufferBuilder.getRenderEnv; deobf=true no-op for OF name
    private static RenderEnv BufferBuilder_getRenderEnv(BufferBuilder builder, IBlockState blockStateIn, BlockPos blockPosIn){
        throw new AbstractMethodError();
    }

    @Redirect(method = "renderFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/color/BlockColors;colorMultiplier(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;I)I"))
    public int getCustomColor(BlockColors instance, IBlockState blockStateIn, IBlockAccess blockAccess, BlockPos blockPosIn, int val, @Share("renderEnv") LocalRef<RenderEnv> env){
// [AUDIT-OK] target BlockColors.colorMultiplier in renderFluid matches baseline (line 48)
        return CustomColors.getFluidColor(blockAccess, blockStateIn, blockPosIn, env.get());
    }

    @Inject(method = "renderFluid", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/BlockFluidRenderer;atlasSpriteWaterOverlay:Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    public void onSet(IBlockAccess p_178270_1_, IBlockState p_178270_2_, BlockPos p_178270_3_, BufferBuilder p_178270_4_, CallbackInfoReturnable<Boolean> cir){
// [AUDIT-OK] target GETFIELD atlasSpriteWaterOverlay in renderFluid matches baseline (line 191); setSprite(overlay) matches OF
        BufferBuilder_setSprite(p_178270_4_, this.atlasSpriteWaterOverlay);
    }

    
    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder setSprite (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
// [AUDIT-OK] OF member BufferBuilder.setSprite
    private static void BufferBuilder_setSprite(BufferBuilder builder, TextureAtlasSprite sprite){
        throw new AbstractMethodError();
    }


    @Definition(id = "textureatlassprite", local = @Local(type = TextureAtlasSprite.class))
    @Expression("textureatlassprite = @(?)")
    @ModifyExpressionValue(method = "renderFluid", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    public TextureAtlasSprite hookAtSetTextureAtlasSprite_renderFluid(TextureAtlasSprite original, @Local(argsOnly = true) BufferBuilder builder) {
// [AUDIT-OK] expression hook; setSprite on overlay sprite assignment matches OF
        BufferBuilder_setSprite(builder, original);
        return original;
    }

    @Definition(id = " atextureatlassprite", local = @Local(type = TextureAtlasSprite[].class))
    @Expression("@(atextureatlassprite[0]).?")
    @ModifyExpressionValue(method = "renderFluid", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public TextureAtlasSprite hookAtUseZeroTextureAtlasSprite_renderFluid(TextureAtlasSprite or,
// [AUDIT-FIXED] setSprite(bottom sprite) kept; fbr=0.5 removed (vanilla color() already carries face brightness)
                                                            @Local(argsOnly = true) BufferBuilder builder,
                                                            @Share(namespace = "optirefine", value = "fbr") LocalRef<Float> fbr) {
        BufferBuilder_setSprite(builder, or);
        return or;
    }

    @Redirect(method = "renderFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;color(FFFF)Lnet/minecraft/client/renderer/BufferBuilder;"))
    // [AUDIT-FIXED] pass-through: vanilla color() args already include per-face brightness (0.5 bottom/0.8/0.6 sides);
    // the previous fbr=0.5 scaling double-darkened bottom+side faces (OF uses FaceBakery.getFaceBrightness per face, same values)
    public BufferBuilder remapColor_renderFluid(BufferBuilder instance, float red, float green, float blue, float alpha,  @Share(namespace = "optirefine", value = "fbr") LocalRef<Float> fbr){
        return instance.color(red, green, blue, alpha);
    }


    @WrapMethod(method = "renderFluid")
    public boolean wrap_renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn, Operation<Boolean> original){
// [AUDIT-OK] target renderFluid matches baseline; SVertexBuilder push/popEntity matches OF
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

    @Inject(method = "renderFluid", at = @At("RETURN"))
    // [AUDIT-FIXED] 2026-08-05: OF:290 setSprite(null) before return (success path); without it the
    // next consumer of the BufferBuilder inherits the fluid sprite (multi-texture state leakage).
    public void resetSprite_renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValue()) {
            BufferBuilder_setSprite(worldRendererIn, null);
        }
    }

}
