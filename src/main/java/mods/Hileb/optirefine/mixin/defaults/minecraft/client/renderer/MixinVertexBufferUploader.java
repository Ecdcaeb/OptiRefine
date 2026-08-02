package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.VertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(VertexBufferUploader.class)
public abstract class MixinVertexBufferUploader {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 1
    @Shadow
// [AUDIT-OK] baseline member vertexBuffer (SRG field_178179_a)
    private VertexBuffer vertexBuffer;

    @Inject(method = "draw", at = @At("HEAD"))
    public void before_draw(BufferBuilder vertexBufferIn, CallbackInfo ci){
// [AUDIT-ISSUE] target draw(Lnet/minecraft/client/renderer/BufferBuilder;)V (SRG func_181679_a) exists, but inject runs BEFORE vanilla guards (!isDrawing && vertexBuffer!=null) -> quadsToTriangles/setDrawMode may NPE on null/not-finished buffer; add the guards (OF has them)
        if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            BufferBuilder_quadsToTriangles(vertexBufferIn);
            VertexBuffer_setDrawMode(vertexBuffer, vertexBufferIn.getDrawMode());
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder quadsToTriangles ()V")
// [AUDIT-OK] OF member BufferBuilder.quadsToTriangles (MixinBufferBuilder @Unique), not in baseline
    private static native void BufferBuilder_quadsToTriangles(BufferBuilder builder) ;

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.vertex.VertexBuffer setDrawMode (I)V")
// [AUDIT-OK] OF member VertexBuffer.setDrawMode (MixinVertexBuffer @Unique public), not in baseline
    private static native void VertexBuffer_setDrawMode(VertexBuffer builder, int arg1) ;

}
