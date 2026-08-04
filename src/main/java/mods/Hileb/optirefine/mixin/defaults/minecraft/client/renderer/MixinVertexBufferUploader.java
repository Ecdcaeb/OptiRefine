package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.VertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(VertexBufferUploader.class)
public abstract class MixinVertexBufferUploader {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 1
    @Shadow
// [AUDIT-OK] baseline member vertexBuffer (SRG field_178179_a)
    private VertexBuffer vertexBuffer;

    @WrapMethod(method = "draw")
    // [AUDIT-FIXED] three-way audit (P6): Forge baseline draw = reset(); bufferData(getByteBuffer()) —
    // the leading reset() zeroes vertexCount so quadsToTriangles converted nothing yet set
    // modeTriangles=true (empty triangle buffer). Rewrite in OF order (OF VertexBufferUploader:9-15):
    // quadsToTriangles -> setDrawMode -> bufferData -> reset. reset() at the end also clears
    // modeTriangles via the reset TAIL hook, so subsequent draws read the quad-layout buffer again.
    public void optiRefine$draw(BufferBuilder vertexBufferIn, Operation<Void> original) {
        if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            BufferBuilder_quadsToTriangles(vertexBufferIn);
            VertexBuffer_setDrawMode(vertexBuffer, vertexBufferIn.getDrawMode());
        }
        VertexBuffer_bufferData(vertexBuffer, vertexBufferIn.getByteBuffer());
        vertexBufferIn.reset();
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder quadsToTriangles ()V")
// [AUDIT-OK] OF member BufferBuilder.quadsToTriangles (MixinBufferBuilder @Unique), not in baseline
    private static native void BufferBuilder_quadsToTriangles(BufferBuilder builder) ;

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.vertex.VertexBuffer setDrawMode (I)V")
// [AUDIT-OK] OF member VertexBuffer.setDrawMode (MixinVertexBuffer @Unique public), not in baseline
    private static native void VertexBuffer_setDrawMode(VertexBuffer builder, int arg1) ;

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.vertex.VertexBuffer bufferData (Ljava.nio.ByteBuffer;)V")
// [AUDIT-OK] baseline member VertexBuffer.bufferData (SRG func_181722_a); goes through MixinVertexBuffer vboRegion wrap
    private static native void VertexBuffer_bufferData(VertexBuffer builder, java.nio.ByteBuffer data) ;

}
