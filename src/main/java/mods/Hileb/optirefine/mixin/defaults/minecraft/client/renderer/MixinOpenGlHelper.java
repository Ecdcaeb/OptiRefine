package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.ARBCopyBuffer;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GLContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * OptiFine additions to {@link OpenGlHelper}.
 *
 * <ul>
 *     <li>new public fields: lastBrightnessX/Y, openGL31, vboRegions, GL_COPY_READ/WRITE_BUFFER, GL_QUADS, GL_TRIANGLES</li>
 *     <li>{@code initializeTextures()} now calls {@code Config.initDisplay()} and detects GL 1.3/ARB_copy_buffer (vboRegions)</li>
 *     <li>{@code useVbo()} disabled by multi-texture / render-regions</li>
 *     <li>{@code setLightmapTextureCoords()} records last brightness</li>
 *     <li>{@code isFramebufferEnabled()} disabled by fast-render / antialiasing</li>
 *     <li>new glBufferData / glBufferSubData / glCopyBufferSubData helpers</li>
 * </ul>
 */
@Mixin(OpenGlHelper.class)
public abstract class MixinOpenGlHelper {

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    private static float lastBrightnessX = 0.0F;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    private static float lastBrightnessY = 0.0F;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    private static boolean openGL31;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    private static boolean vboRegions;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    private static int GL_COPY_READ_BUFFER;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    private static int GL_COPY_WRITE_BUFFER;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private static final int GL_QUADS = 7;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private static final int GL_TRIANGLES = 4;

    @Shadow
    private static String logText;
    @Shadow
    @Public
    private static boolean vboSupported;
    @Shadow
    private static boolean arbVbo;
    @Shadow
    @Public
    private static int lightmapTexUnit;
    @Shadow
    @Public
    private static boolean framebufferSupported;

    @Inject(method = "initializeTextures", at = @At("HEAD"))
    private static void optiRefine$initializeTextures(CallbackInfo ci) {
        Config.initDisplay();
        ContextCapabilities capabilities = GLContext.getCapabilities();
        openGL31 = capabilities.OpenGL31;
        GL_COPY_READ_BUFFER = 36662;
        GL_COPY_WRITE_BUFFER = 36663;

        boolean flag = openGL31 || capabilities.GL_ARB_copy_buffer;
        boolean flag1 = capabilities.OpenGL14;
        vboRegions = flag && flag1;
        if (!vboRegions) {
            ArrayList<String> list = new ArrayList<>();
            if (!flag) {
                list.add("OpenGL 1.3, ARB_copy_buffer");
            }
            if (!flag1) {
                list.add("OpenGL 1.4");
            }
            String s = "VboRegions not supported, missing: " + Config.listToString(list);
            Config.dbg(s);
            logText = logText + s + "\n";
        }
    }

    @ModifyReturnValue(method = "useVbo", at = @At("RETURN"))
    private static boolean optiRefine$useVbo(boolean original) {
        if (Config.isMultiTexture()) {
            return false;
        }
        return Config.isRenderRegions() && !vboRegions ? false : original;
    }

    @Inject(method = "setLightmapTextureCoords", at = @At("TAIL"))
    private static void optiRefine$setLightmapTextureCoords(int texUnit, float brightnessX, float brightnessY, CallbackInfo ci) {
        if (texUnit == lightmapTexUnit) {
            lastBrightnessX = brightnessX;
            lastBrightnessY = brightnessY;
        }
    }

    @ModifyReturnValue(method = "isFramebufferEnabled", at = @At("RETURN"))
    private static boolean optiRefine$isFramebufferEnabled(boolean original) {
        if (Config.isFastRender()) {
            return false;
        }
        return Config.isAntialiasing() ? false : original;
    }

    @Public
    private static void glBufferData(int target, long size, int usage) {
        if (arbVbo) {
            ARBVertexBufferObject.glBufferDataARB(target, size, usage);
        } else {
            GL15.glBufferData(target, size, usage);
        }
    }

    @Public
    private static void glBufferSubData(int target, long offset, ByteBuffer data) {
        if (arbVbo) {
            ARBVertexBufferObject.glBufferSubDataARB(target, offset, data);
        } else {
            GL15.glBufferSubData(target, offset, data);
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
    private static void glCopyBufferSubData(int readTarget, int writeTarget, long readOffset, long writeOffset, long size) {
        if (openGL31) {
            GL31.glCopyBufferSubData(readTarget, writeTarget, readOffset, writeOffset, size);
        } else {
            ARBCopyBuffer.glCopyBufferSubData(readTarget, writeTarget, readOffset, writeOffset, size);
        }
    }
}
