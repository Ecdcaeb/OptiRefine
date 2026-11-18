package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.shaders.modern.ModernShaderPipeline;
import mods.Hileb.optirefine.optifine.shaders.modern.ShaderPassProperties;
import net.minecraft.client.Minecraft;
import net.optifine.shaders.ICustomTexture;
import net.optifine.shaders.Program;
import net.optifine.shaders.Programs;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.ShaderOption;
import net.optifine.shaders.uniform.ShaderUniforms;
import net.optifine.shaders.FlipTextures;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Properties;

@Mixin(targets = "net.optifine.shaders.Shaders", remap = false)
public abstract class MixinModernShaderPipeline {
    @Shadow private static Programs programs;
    @Shadow private static ShaderUniforms shaderUniforms;
    @Shadow private static boolean hasDeferredPrograms;
    @Shadow public static boolean isShaderPackInitialized;
    @Shadow static int usedColorBuffers;
    @Shadow static int usedDrawBuffers;
    @Shadow static int usedDepthBuffers;
    @Shadow static int usedShadowColorBuffers;
    @Shadow static int usedShadowDepthBuffers;
    @Shadow static boolean centerDepthSmoothEnabled;
    @Shadow static int dfb;
    @Shadow static int sfb;
    @Shadow static int shadowMapWidth;
    @Shadow static int shadowMapHeight;
    @Shadow @Final static FlipTextures dfbColorTexturesFlip;
    @Shadow @Final static IntBuffer dfbDepthTextures;
    @Shadow @Final static IntBuffer sfbColorTextures;
    @Shadow @Final static IntBuffer sfbDepthTextures;
    @Shadow @Final static IntBuffer sfbDrawBuffers;
    @Shadow private static int[] gbuffersFormat;
    @Shadow public static boolean[] gbuffersClear;
    @Shadow public static Vector4f[] gbuffersClearColor;
    @Shadow private static ICustomTexture[] customTexturesGbuffers;
    @Unique private static Properties optiRefine$stageProperties;

    @Shadow
    private static ICustomTexture[] loadCustomTextures(Properties properties, int stage) {
        throw new AssertionError();
    }

    @Shadow private static native int getTextureFormatFromString(String name);
    @Shadow private static native int getPixelFormat(int internalFormat);

    @Inject(method = "loadShaderPackProperties", at = @At("HEAD"))
    private static void optiRefine$clearStageProperties(CallbackInfo ci) {
        optiRefine$stageProperties = null;
        ShaderPassProperties.reset();
    }

    @WrapOperation(method = "loadShaderPackProperties", at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/Shaders;loadCustomTextures(Ljava/util/Properties;I)[Lnet/optifine/shaders/ICustomTexture;"))
    private static ICustomTexture[] optiRefine$captureStageProperties(Properties properties, int stage, Operation<ICustomTexture[]> original) {
        optiRefine$stageProperties = properties;
        if (stage == 0) ShaderPassProperties.load(properties);
        return original.call(properties, stage);
    }

    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/Programs;getCount()I"))
    private static void optiRefine$registerStages(CallbackInfo ci) {
        ModernShaderPipeline.register(programs);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void optiRefine$registerImages(CallbackInfo ci) {
        ModernShaderPipeline.initializeUniforms(shaderUniforms);
    }

    @WrapMethod(method = "init")
    private static void optiRefine$loadModernStages(Operation<Void> original) {
        if (isShaderPackInitialized) { original.call(); return; }
        ModernShaderPipeline.beginPack();
        try {
            ModernShaderPipeline.colorProperties(optiRefine$stageProperties, programs);
            ICustomTexture[] prepare = optiRefine$stageProperties == null ? null : loadCustomTextures(ModernShaderPipeline.stageTextureProperties(optiRefine$stageProperties, "prepare"), 1);
            ModernShaderPipeline.stageTextures(prepare, null, customTexturesGbuffers);
            ICustomTexture[] shadowcomp = optiRefine$stageProperties == null ? null : loadCustomTextures(ModernShaderPipeline.stageTextureProperties(optiRefine$stageProperties, "shadowcomp"), 1);
            ModernShaderPipeline.stageTextures(prepare, shadowcomp, customTexturesGbuffers);
            original.call();
            ModernShaderPipeline.completePack();
            for (Program deferred : Shaders.ProgramsDeferred) if (ModernShaderPipeline.hasComputes(deferred)) {
                hasDeferredPrograms = true;
                break;
            }
        } catch (ModernShaderPipeline.LoadException exception) {
            // G5 only aborts beginRender for this exact exception message. The detailed
            // filename/GL/compiler error has already been printed by the sidecar.
            isShaderPackInitialized = true;
            Shaders.uninit();
            throw new IllegalStateException("Function is not supported", exception);
        }
    }

    @Inject(method = "loadShaderPack", at = @At("HEAD"))
    private static void optiRefine$releasePreviousPack(CallbackInfo ci) {
        if (isShaderPackInitialized || ModernShaderPipeline.isReady()) Shaders.uninit();
        else ModernShaderPipeline.close();
    }

    @Inject(method = "uninit", at = @At("HEAD"))
    private static void optiRefine$releaseModernObjects(CallbackInfo ci) {
        ModernShaderPipeline.close();
    }

    @Inject(method = "setupProgram", at = @At("HEAD"))
    private static void optiRefine$loadComputes(Program program, String vertex, String geometry, String fragment, CallbackInfo ci) {
        ModernShaderPipeline.loadComputes(program, vertex);
        optiRefine$applyRequirements();
    }

    @Inject(method = "setupProgram", at = @At("RETURN"))
    private static void optiRefine$finishProgram(Program program, String vertex, String geometry, String fragment, CallbackInfo ci) {
        ModernShaderPipeline.requireShadowComposite(program);
        optiRefine$applyRequirements();
        ModernShaderPipeline.checkLoadError();
    }

    @WrapOperation(method = "setupProgram", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/ARBShaderObjects;glLinkProgramARB(I)V"))
    private static void optiRefine$linkFragmentOutputs(int id, Operation<Void> original, @Local(argsOnly = true) Program program) {
        ModernShaderPipeline.bindFragmentOutputs(program, id);
        original.call(id);
    }

    @ModifyExpressionValue(method = "createFragShader", at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/config/ShaderParser;isValidDrawBuffers(Ljava/lang/String;)Z"))
    private static boolean optiRefine$allowExtendedDrawTargets(boolean valid) {
        return valid || ModernShaderPipeline.acceptsExtendedDrawBuffers();
    }

    @Inject(method = {"createVertShader", "createGeomShader", "createFragShader"}, at = @At("HEAD"))
    private static void optiRefine$beginSource(Program program, String path, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Integer> cir) {
        ModernShaderPipeline.beginSource(path);
    }

    @WrapOperation(method = {"createVertShader", "createGeomShader", "createFragShader"}, at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/Shaders;applyOptions(Ljava/lang/String;[Lnet/optifine/shaders/config/ShaderOption;)Ljava/lang/String;"))
    private static String optiRefine$scanSource(String line, ShaderOption[] options, Operation<String> original) {
        String result = original.call(line, options);
        ModernShaderPipeline.sourceLine(result);
        return result;
    }

    @Inject(method = {"createVertShader", "createGeomShader", "createFragShader"}, at = @At("RETURN"))
    private static void optiRefine$endSource(Program program, String path, CallbackInfoReturnable<Integer> cir) {
        ModernShaderPipeline.endSource(program, MixinModernShaderPipeline::getTextureFormatFromString);
    }

    @WrapOperation(method = {"createVertShader", "createGeomShader", "createFragShader"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL20;glGetShaderi(II)I"))
    private static int optiRefine$graphicsCompileStatus(int shader, int parameter, Operation<Integer> original, @Local(argsOnly = true) String path) {
        int status = original.call(shader, parameter);
        if (parameter == GL20.GL_COMPILE_STATUS && status == 0) ModernShaderPipeline.graphicsError(path + ": graphics compilation failed\n" + GL20.glGetShaderInfoLog(shader, 32768));
        return status;
    }

    @WrapOperation(method = "setupProgram", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL20;glGetProgrami(II)I"))
    private static int optiRefine$graphicsLinkStatus(int programId, int parameter, Operation<Integer> original, @Local(argsOnly = true) Program program) {
        int status = original.call(programId, parameter);
        if ((parameter == GL20.GL_LINK_STATUS || parameter == GL20.GL_VALIDATE_STATUS) && status == 0) ModernShaderPipeline.graphicsError(program.getName() + ": graphics link/validation failed\n" + GL20.glGetProgramInfoLog(programId, 32768));
        return status;
    }

    @Unique
    private static void optiRefine$applyRequirements() {
        usedColorBuffers = Math.max(usedColorBuffers, ModernShaderPipeline.requiredColors());
        usedDepthBuffers = Math.max(usedDepthBuffers, ModernShaderPipeline.requiredDepths());
        usedShadowColorBuffers = Math.max(usedShadowColorBuffers, ModernShaderPipeline.requiredShadowColors());
        usedShadowDepthBuffers = Math.max(usedShadowDepthBuffers, ModernShaderPipeline.requiredShadowDepths());
        centerDepthSmoothEnabled |= ModernShaderPipeline.requiresCenterDepthSmooth();
    }

    @Inject(method = "initDrawBuffers", at = @At("HEAD"), cancellable = true)
    private static void optiRefine$configureDrawBuffers(Program program, CallbackInfo ci) {
        int outputs = ModernShaderPipeline.configureColorOutputs(program);
        if (outputs != 0) {
            usedDrawBuffers = Math.max(usedDrawBuffers, outputs);
            ci.cancel();
            return;
        }
        if (!ModernShaderPipeline.isShadowComposite(program)) return;
        boolean[] toggles = program.getToggleColorTextures();
        Arrays.fill(toggles, false);
        if (program.getId() == 0) { program.setDrawBuffers(sfbDrawBuffers); ci.cancel(); return; }
        ModernShaderPipeline.requireShadowComposite(program);
        optiRefine$applyRequirements();
        String settings = program.getDrawBufSettings();
        if (settings == null) {
            program.setDrawBuffers(sfbDrawBuffers);
            Arrays.fill(toggles, 0, usedShadowColorBuffers, true);
        } else {
            IntBuffer buffers = program.getDrawBuffersBuffer();
            int count = Math.min(settings.length(), GL11.glGetInteger(34852));
            buffers.clear().limit(count);
            for (int i = 0; i < count; i++) {
                int attachment = settings.charAt(i) - '0';
                buffers.put(i, attachment >= 0 && attachment < 8 ? 36064 + attachment : 0);
                if (attachment >= 0 && attachment < 8) toggles[attachment] = true;
            }
            program.setDrawBuffers(buffers);
        }
        ci.cancel();
    }

    @ModifyArg(method = {"setupFrameBuffer", "setupShadowFrameBuffer"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTexImage2D(IIIIIIIILjava/nio/ByteBuffer;)V"), index = 2)
    private static int optiRefine$sizedImageStorage(int format) {
        return ModernShaderPipeline.imageStorageFormat(format);
    }

    @WrapMethod(method = {"setupFrameBuffer", "setupShadowFrameBuffer"})
    private static void optiRefine$releaseFailedResize(Operation<Void> original) {
        try {
            original.call();
        } catch (RuntimeException exception) {
            isShaderPackInitialized = true;
            Shaders.uninit();
            throw exception;
        }
    }

    @WrapMethod(method = "beginRender")
    private static void optiRefine$recoverFailedModernResize(Minecraft minecraft, float partialTicks, long finishTimeNano, Operation<Void> original) {
        // Other renderers can use image units between world frames.
        ModernShaderPipeline.invalidateImageBindings();
        try {
            original.call(minecraft, partialTicks, finishTimeNano);
        } catch (ModernShaderPipeline.LoadException exception) {
            minecraft.profiler.endSection();
            Shaders.setShaderPack("OFF");
            minecraft.getFramebuffer().bindFramebuffer(true);
        }
    }

    @Inject(method = "setupFrameBuffer", at = @At("RETURN"))
    private static void optiRefine$mainImages(CallbackInfo ci) {
        ModernShaderPipeline.mainTargets(dfbColorTexturesFlip, dfbDepthTextures, usedColorBuffers, usedDepthBuffers, dfb,
                gbuffersFormat, gbuffersClear, gbuffersClearColor, MixinModernShaderPipeline::getPixelFormat);
    }

    @Inject(method = "setupShadowFrameBuffer", at = @At("HEAD"))
    private static void optiRefine$deleteShadowAlternates(CallbackInfo ci) {
        ModernShaderPipeline.deleteShadowAlternates();
    }

    @Inject(method = "setupShadowFrameBuffer", at = @At("RETURN"))
    private static void optiRefine$shadowImages(CallbackInfo ci) {
        ModernShaderPipeline.shadowTargets(sfbColorTextures, sfbDepthTextures, usedShadowColorBuffers, usedShadowDepthBuffers, shadowMapWidth, shadowMapHeight, sfb);
    }

    @Inject(method = "useProgram", at = @At("RETURN"))
    private static void optiRefine$imageUniforms(Program program, CallbackInfo ci) {
        ModernShaderPipeline.onUseProgram();
    }

    @ModifyExpressionValue(method = "renderComposites", at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/Program;getId()I"))
    private static int optiRefine$dispatchBeforeGraphics(int id, @Local Program program) {
        ModernShaderPipeline.dispatch(program);
        return id;
    }

    @Inject(method = "renderFinal", at = @At("HEAD"))
    private static void optiRefine$finalCompute(CallbackInfo ci) {
        ModernShaderPipeline.dispatch(Shaders.ProgramFinal);
    }

    @Inject(method = "setCamera", at = @At("RETURN"))
    private static void optiRefine$prepare(float partialTicks, CallbackInfo ci) {
        if (!Shaders.isShadowPass) ModernShaderPipeline.renderPrepare();
    }

    @Inject(method = "getTextureIndex", at = @At("RETURN"), cancellable = true)
    private static void optiRefine$extendedCustomTextureUnit(int stage, String name, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValueI() < 0) {
            int unit = ModernShaderPipeline.extendedTextureUnit(name);
            if (unit >= 0) cir.setReturnValue(unit);
        }
    }

    @Inject(method = "updateToggleBuffers", at = @At("RETURN"))
    private static void optiRefine$keepOwnedFlipsOutOfG5(Program program, CallbackInfo ci) {
        ModernShaderPipeline.suppressOwnedTextureFlips(program);
    }

    @Inject(method = "setDrawBuffers", at = @At("HEAD"))
    private static void optiRefine$routePackColorOutputs(IntBuffer buffers, CallbackInfo ci) {
        ModernShaderPipeline.routeColorOutputs(buffers);
    }

    @Inject(method = "clearRenderBuffer", at = @At("RETURN"))
    private static void optiRefine$clearPackColors(CallbackInfo ci) {
        ModernShaderPipeline.clearColorTargets();
    }

    @WrapOperation(method = {"bindGbuffersTextures", "renderComposites"}, at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/Shaders;bindCustomTextures([Lnet/optifine/shaders/ICustomTexture;)V"))
    private static void optiRefine$bindPackColors(ICustomTexture[] textures, Operation<Void> original) {
        ModernShaderPipeline.bindPackColorSamplers();
        original.call((Object) textures);
    }

    @WrapMethod(method = "renderComposites")
    private static void optiRefine$compositeColorOutputs(Program[] programs, boolean renderFinal, Operation<Void> original) {
        boolean previous = ModernShaderPipeline.setCompositeColorOutputs(true);
        try {
            original.call(programs, renderFinal);
        } finally {
            ModernShaderPipeline.setCompositeColorOutputs(previous);
        }
    }

    @WrapOperation(method = "renderComposites", at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/Shaders;drawComposite()V"))
    private static void optiRefine$drawPackColorOutputs(Operation<Void> original, @Local Program program) {
        ModernShaderPipeline.drawCompositeWithColorTargets(program, original::call);
        ModernShaderPipeline.flipCompositeColorTargets(program);
    }

    @WrapOperation(method = {"renderDeferred", "renderCompositeFinal"}, at = @At(value = "INVOKE", target = "Lnet/optifine/shaders/Shaders;checkBufferFlip(Lnet/optifine/shaders/Program;)Z"))
    private static boolean optiRefine$flipPreColorTargets(Program program, Operation<Boolean> original) {
        boolean flipped = original.call(program);
        return ModernShaderPipeline.flipPreColorTargets(program) || flipped;
    }

    @Inject(method = "genCompositeMipmap", at = @At("RETURN"))
    private static void optiRefine$generatePackColorMipmaps(CallbackInfo ci) {
        ModernShaderPipeline.mipmapPackColors();
    }

    @Inject(method = "drawComposite", at = @At("RETURN"))
    private static void optiRefine$graphicsImageBarrier(CallbackInfo ci) {
        // Image stores made by graphics must be visible to subsequent samplers and images.
        ModernShaderPipeline.graphicsBarrier();
    }
}
