package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.Buffer;
import java.nio.ByteBuffer;

@Mixin(WorldVertexBufferUploader.class)
public abstract class MixinWorldVertexBufferUploader {
    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;getVertexFormat()Lnet/minecraft/client/renderer/vertex/VertexFormat;"))
    public void beforeDraw(BufferBuilder vertexBufferIn, CallbackInfo ci){
        if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            BufferBuilder_quadsToTriangles(vertexBufferIn);
        }
    }

    @WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;glDrawArrays(III)V"))
    public void openShader(int mode, int first, int count, Operation<Void> original, @Local(argsOnly = true) BufferBuilder vertexBufferIn) {
        boolean shaders = Config.isShaders();
        boolean shaderFormat = shaders && vertexBufferIn.getVertexFormat().getSize() == 56;
        try {
            if (shaderFormat) {
                // Multi-texture draws need the same client attributes as a single shader draw.
                ByteBuffer buffer = vertexBufferIn.getByteBuffer();
                ((Buffer) buffer).position(32);
                GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, GL11.GL_FLOAT, false, 56, buffer);
                ((Buffer) buffer).position(40);
                GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, GL11.GL_SHORT, false, 56, buffer);
                ((Buffer) buffer).position(48);
                GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, GL11.GL_SHORT, false, 56, buffer);
                ((Buffer) buffer).position(0);
                GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
                GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
                GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
            } else if (shaders) {
                // GUI formats cannot address attributes left over from a shader VBO draw.
                optiRefine$disableShaderArrays();
            }
            if (BufferBuilder_isMultiTexture(vertexBufferIn)) {
                BufferBuilder_drawMultiTexture(vertexBufferIn);
            } else {
                original.call(mode, first, count);
            }
        } finally {
            if (shaderFormat) {
                optiRefine$disableShaderArrays();
            }
        }
    }

    @Unique
    private static void optiRefine$disableShaderArrays() {
        GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
        GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
        GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder quadsToTriangles ()V")
// [AUDIT-OK] OF member BufferBuilder.quadsToTriangles
    private static native void BufferBuilder_quadsToTriangles(BufferBuilder builder) ;

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder isMultiTexture ()Z")
// [AUDIT-OK] OF member BufferBuilder.isMultiTexture
    private static native boolean BufferBuilder_isMultiTexture(BufferBuilder builder) ;

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder drawMultiTexture ()V")
// [AUDIT-OK] OF member BufferBuilder.drawMultiTexture
    private static native void BufferBuilder_drawMultiTexture(BufferBuilder builder) ;
}
