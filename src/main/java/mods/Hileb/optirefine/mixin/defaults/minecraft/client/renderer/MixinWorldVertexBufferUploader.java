package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.optifine.shaders.SVertexBuilder;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL20;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(WorldVertexBufferUploader.class)
public abstract class MixinWorldVertexBufferUploader {
    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;getVertexFormat()Lnet/minecraft/client/renderer/vertex/VertexFormat;"))
    public void beforeDraw(BufferBuilder vertexBufferIn, CallbackInfo ci){
        if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            BufferBuilder_quadsToTriangles(vertexBufferIn);
        }
    }

    @WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;glDrawArrays(III)V"))
    public void openShader(int mode, int first, int count, Operation<Void> original, @Local(argsOnly = true) BufferBuilder vertexBufferIn){
        if (BufferBuilder_isMultiTexture(vertexBufferIn)) {
            BufferBuilder_drawMultiTexture(vertexBufferIn);
        } else if (Config.isShaders()) {
            // SVertexBuilder.drawArrays only enable/disable midTex/tangent/entity attribs when the
            // vertex is 56 bytes. Loading-screen / GUI draws (28-byte POSITION_TEX_COLOR) then
            // glDrawArrays with leftover VBO attrib arrays still enabled -> Intel ig9icd64 AV
            // (hs_err: LoadingScreenRenderer.setLoadingProgress after loadWorld(null)).
            if (vertexBufferIn.getVertexFormat().getSize() != 56) {
                GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
                GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
                GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
            }
            SVertexBuilder.drawArrays(vertexBufferIn.getDrawMode(), 0, vertexBufferIn.getVertexCount(), vertexBufferIn);
        } else {
            GlStateManager.glDrawArrays(vertexBufferIn.getDrawMode(), 0, vertexBufferIn.getVertexCount());
        }
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
