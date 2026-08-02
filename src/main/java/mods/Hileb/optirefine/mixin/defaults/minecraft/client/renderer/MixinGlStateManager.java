package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.optifine.SmartAnimations;
import net.optifine.render.GlAlphaState;
import net.optifine.render.GlBlendState;
import net.optifine.shaders.Shaders;
import net.optifine.util.LockCounter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;

/**
 * OptiFine additions to {@link GlStateManager}.
 *
 * <ul>
 *     <li>new fields: {@code clearEnabled}, {@code alphaLock}/{@code alphaLockState}, {@code blendLock}/{@code blendLockState}, {@code creatingDisplayList}</li>
 *     <li>alpha/blend operations honor the OptiFine lock/unlock mechanism (used by shaders)</li>
 *     <li>shaders notifications in {@code blendFunc}/{@code tryBlendFuncSeparate}/{@code setFog}/{@code setFogDensity}</li>
 *     <li>{@code deleteTexture} guards 0, resets to 0; {@code bindTexture} notifies {@link SmartAnimations}</li>
 *     <li>{@code clear} honors {@code clearEnabled}</li>
 *     <li>instance rendering in {@code glDrawArrays}/{@code callList}/{@code callLists}/{@code glMultiDrawArrays}</li>
 *     <li>{@code glNewList}/{@code glEndList} track {@code creatingDisplayList}</li>
 *     <li>new helpers: {@code getActiveTextureUnit}, {@code bindCurrentTexture}, {@code getBoundTexture},
 *     {@code checkBoundTexture}, {@code deleteTextures}, {@code isFogEnabled}, {@code setFogEnabled},
 *     {@code lockAlpha}/{@code unlockAlpha}/{@code getAlphaState}/{@code setAlphaState},
 *     {@code lockBlend}/{@code unlockBlend}/{@code getBlendState}/{@code setBlendState}</li>
 *     <li>texture unit array enlarged from 8 to 32</li>
 * </ul>
 */
@Mixin(GlStateManager.class)
public abstract class MixinGlStateManager {

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static boolean clearEnabled = true;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static LockCounter alphaLock = new LockCounter();
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static GlAlphaState alphaLockState = new GlAlphaState();
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static LockCounter blendLock = new LockCounter();
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static GlBlendState blendLockState = new GlBlendState();
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static boolean creatingDisplayList = false;

    @Shadow
    private static GlStateManager.AlphaState alphaState;
    @Shadow
    private static GlStateManager.BlendState blendState;
    @Shadow
    private static GlStateManager.FogState fogState;
    @Shadow
    private static int activeTextureUnit;
    @Shadow
    private static GlStateManager.TextureState[] textureState;
    @Shadow
    @Public
    private static native void alphaFunc(int func, float ref);

    @Shadow
    @Public
    private static native void deleteTexture(int texture);

    @Shadow
    @Public
    private static native void blendFunc(int srcFactor, int dstFactor);

    @Shadow
    @Public
    private static native void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha);    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.GlStateManager$BooleanState currentState Z")
    private static native boolean BooleanState_currentState_get(GlStateManager.BooleanState instance);

    // ===== alpha lock =====

    @WrapMethod(method = "disableAlpha")
    private static void optiRefine$disableAlpha(Operation<Void> original) {
        if (alphaLock.isLocked()) {
            alphaLockState.setDisabled();
        } else {
            original.call();
        }
    }

    @WrapMethod(method = "enableAlpha")
    private static void optiRefine$enableAlpha(Operation<Void> original) {
        if (alphaLock.isLocked()) {
            alphaLockState.setEnabled();
        } else {
            original.call();
        }
    }

    @WrapMethod(method = "alphaFunc")
    private static void optiRefine$alphaFunc(int func, float ref, Operation<Void> original) {
        if (alphaLock.isLocked()) {
            alphaLockState.setFuncRef(func, ref);
        } else {
            original.call(func, ref);
        }
    }

    // ===== blend lock =====

    @WrapMethod(method = "disableBlend")
    private static void optiRefine$disableBlend(Operation<Void> original) {
        if (blendLock.isLocked()) {
            blendLockState.setDisabled();
        } else {
            original.call();
        }
    }

    @WrapMethod(method = "enableBlend")
    private static void optiRefine$enableBlend(Operation<Void> original) {
        if (blendLock.isLocked()) {
            blendLockState.setEnabled();
        } else {
            original.call();
        }
    }

    @WrapMethod(method = "blendFunc(II)V")
    private static void optiRefine$blendFunc(int src, int dst, Operation<Void> original) {
        if (blendLock.isLocked()) {
            blendLockState.setFactors(src, dst);
        } else {
            if (src != blendState.srcFactor || dst != blendState.dstFactor || src != blendState.srcFactorAlpha || dst != blendState.dstFactorAlpha) {
                blendState.srcFactor = src;
                blendState.dstFactor = dst;
                blendState.srcFactorAlpha = src;
                blendState.dstFactorAlpha = dst;
                if (Config.isShaders()) {
                    Shaders.uniform_blendFunc.setValue(src, dst, src, dst);
                }
                org.lwjgl.opengl.GL11.glBlendFunc(src, dst);
            }
        }
    }

    @WrapMethod(method = "tryBlendFuncSeparate(IIII)V")
    private static void optiRefine$tryBlendFuncSeparate(int src, int dst, int srcAlpha, int dstAlpha, Operation<Void> original) {
        if (blendLock.isLocked()) {
            blendLockState.setFactors(src, dst, srcAlpha, dstAlpha);
        } else {
            if (src != blendState.srcFactor || dst != blendState.dstFactor || srcAlpha != blendState.srcFactorAlpha || dstAlpha != blendState.dstFactorAlpha) {
                blendState.srcFactor = src;
                blendState.dstFactor = dst;
                blendState.srcFactorAlpha = srcAlpha;
                blendState.dstFactorAlpha = dstAlpha;
                if (Config.isShaders()) {
                    Shaders.uniform_blendFunc.setValue(src, dst, srcAlpha, dstAlpha);
                }
                OpenGlHelper.glBlendFunc(src, dst, srcAlpha, dstAlpha);
            }
        }
    }

    // ===== fog =====

    @WrapMethod(method = "setFog(I)V")
    private static void optiRefine$setFog(int mode, Operation<Void> original) {
        if (mode != fogState.mode) {
            fogState.mode = mode;
            org.lwjgl.opengl.GL11.glFogi(2917, mode);
            if (Config.isShaders()) {
                Shaders.setFogMode(mode);
            }
        }
    }

    @WrapMethod(method = "setFogDensity")
    private static void optiRefine$setFogDensity(float density, Operation<Void> original) {
        if (density < 0.0F) {
            density = 0.0F;
        }
        if (density != fogState.density) {
            fogState.density = density;
            org.lwjgl.opengl.GL11.glFogf(2914, density);
            if (Config.isShaders()) {
                Shaders.setFogDensity(density);
            }
        }
    }

    // ===== texture =====

    @WrapMethod(method = "deleteTexture")
    private static void optiRefine$deleteTexture(int texture, Operation<Void> original) {
        if (texture != 0) {
            org.lwjgl.opengl.GL11.glDeleteTextures(texture);
            for (GlStateManager.TextureState state : textureState) {
                if (state.textureName == texture) {
                    state.textureName = 0;
                }
            }
        }
    }

    @WrapMethod(method = "bindTexture")
    private static void optiRefine$bindTexture(int texture, Operation<Void> original) {
        if (texture != textureState[activeTextureUnit].textureName) {
            textureState[activeTextureUnit].textureName = texture;
            org.lwjgl.opengl.GL11.glBindTexture(3553, texture);
            if (SmartAnimations.isActive()) {
                SmartAnimations.textureRendered(texture);
            }
        }
    }

    // ===== clear =====

    @WrapMethod(method = "clear")
    private static void optiRefine$clear(int mask, Operation<Void> original) {
        if (clearEnabled) {
            original.call(mask);
        }
    }

    // ===== instance rendering =====

    @Inject(method = "glDrawArrays", at = @At("TAIL"))
    private static void optiRefine$glDrawArrays(int mode, int first, int count, CallbackInfo ci) {
        if (Config.isShaders() && !creatingDisplayList) {
            int instances = Shaders.activeProgram.getCountInstances();
            if (instances > 1) {
                for (int i = 1; i < instances; i++) {
                    Shaders.uniform_instanceId.setValue(i);
                    org.lwjgl.opengl.GL11.glDrawArrays(mode, first, count);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }

    @Inject(method = "callList", at = @At("TAIL"))
    private static void optiRefine$callList(int list, CallbackInfo ci) {
        if (Config.isShaders() && !creatingDisplayList) {
            int instances = Shaders.activeProgram.getCountInstances();
            if (instances > 1) {
                for (int i = 1; i < instances; i++) {
                    Shaders.uniform_instanceId.setValue(i);
                    org.lwjgl.opengl.GL11.glCallList(list);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }

    @Inject(method = "glNewList", at = @At("TAIL"))
    private static void optiRefine$glNewList(int list, int mode, CallbackInfo ci) {
        creatingDisplayList = true;
    }

    @Inject(method = "glEndList", at = @At("TAIL"))
    private static void optiRefine$glEndList(CallbackInfo ci) {
        creatingDisplayList = false;
    }

    // ===== new methods =====

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void callLists(IntBuffer lists) {
        org.lwjgl.opengl.GL11.glCallLists(lists);
        if (Config.isShaders() && !creatingDisplayList) {
            int instances = Shaders.activeProgram.getCountInstances();
            if (instances > 1) {
                for (int i = 1; i < instances; i++) {
                    Shaders.uniform_instanceId.setValue(i);
                    org.lwjgl.opengl.GL11.glCallLists(lists);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void glMultiDrawArrays(int mode, IntBuffer firsts, IntBuffer counts) {
        org.lwjgl.opengl.GL14.glMultiDrawArrays(mode, firsts, counts);
        if (Config.isShaders() && !creatingDisplayList) {
            int instances = Shaders.activeProgram.getCountInstances();
            if (instances > 1) {
                for (int i = 1; i < instances; i++) {
                    Shaders.uniform_instanceId.setValue(i);
                    org.lwjgl.opengl.GL14.glMultiDrawArrays(mode, firsts, counts);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static int getActiveTextureUnit() {
        return OpenGlHelper.defaultTexUnit + activeTextureUnit;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void bindCurrentTexture() {
        org.lwjgl.opengl.GL11.glBindTexture(3553, textureState[activeTextureUnit].textureName);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static int getBoundTexture() {
        return textureState[activeTextureUnit].textureName;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void checkBoundTexture() {
        if (Config.isMinecraftThread()) {
            int glActive = org.lwjgl.opengl.GL11.glGetInteger(34016);
            int glTex = org.lwjgl.opengl.GL11.glGetInteger(32873);
            int active = getActiveTextureUnit();
            int bound = getBoundTexture();
            if (bound > 0) {
                if (glActive != active || glTex != bound) {
                    Config.dbg("checkTexture: act: " + active + ", glAct: " + glActive + ", tex: " + bound + ", glTex: " + glTex);
                }
            }
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void deleteTextures(IntBuffer textures) {
        textures.rewind();
        while (textures.position() < textures.limit()) {
            int texture = textures.get();
            deleteTexture(texture);
        }
        textures.rewind();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static boolean isFogEnabled() {
        return BooleanState_currentState_get(fogState.fog);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void setFogEnabled(boolean enabled) {
        fogState.fog.setState(enabled);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void lockAlpha(GlAlphaState state) {
        if (!alphaLock.isLocked()) {
            getAlphaState(alphaLockState);
            setAlphaState(state);
            alphaLock.lock();
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void unlockAlpha() {
        if (alphaLock.unlock()) {
            setAlphaState(alphaLockState);
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void getAlphaState(GlAlphaState state) {
        if (alphaLock.isLocked()) {
            state.setState(alphaLockState);
        } else {
            state.setState(BooleanState_currentState_get(alphaState.alphaTest), alphaState.func, alphaState.ref);
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void setAlphaState(GlAlphaState state) {
        if (alphaLock.isLocked()) {
            alphaLockState.setState(state);
        } else {
            alphaState.alphaTest.setState(state.isEnabled());
            alphaFunc(state.getFunc(), state.getRef());
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void lockBlend(GlBlendState state) {
        if (!blendLock.isLocked()) {
            getBlendState(blendLockState);
            setBlendState(state);
            blendLock.lock();
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void unlockBlend() {
        if (blendLock.unlock()) {
            setBlendState(blendLockState);
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void getBlendState(GlBlendState state) {
        if (blendLock.isLocked()) {
            state.setState(blendLockState);
        } else {
            state.setState(BooleanState_currentState_get(blendState.blend), blendState.srcFactor, blendState.dstFactor, blendState.srcFactorAlpha, blendState.dstFactorAlpha);
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void setBlendState(GlBlendState state) {
        if (blendLock.isLocked()) {
            blendLockState.setState(state);
        } else {
            blendState.blend.setState(state.isEnabled());
            if (!state.isSeparate()) {
                blendFunc(state.getSrcFactor(), state.getDstFactor());
            } else {
                tryBlendFuncSeparate(state.getSrcFactor(), state.getDstFactor(), state.getSrcFactorAlpha(), state.getDstFactorAlpha());
            }
        }
    }

    // ===== texture unit array 8 -> 32 =====

    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "[Lnet/minecraft/client/renderer/GlStateManager$TextureState;"))
    private static GlStateManager.TextureState[] optiRefine$largerTextureState(int size) {
        return new GlStateManager.TextureState[32];
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void optiRefine$initExtraTextureStates(CallbackInfo ci) {
        for (int i = 0; i < textureState.length; i++) {
            if (textureState[i] == null) {
                textureState[i] = new GlStateManager.TextureState();
            }
        }
    }
}
