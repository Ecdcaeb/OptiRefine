package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

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
public class MixinVertexBufferUploader {
    @Shadow
    private VertexBuffer vertexBuffer;

    @Inject(method = "draw", at = @At("HEAD"))
    public void before_draw(BufferBuilder vertexBufferIn, CallbackInfo ci){
        if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            BufferBuilder_quadsToTriangles(vertexBufferIn);
            VertexBuffer_setDrawMode(vertexBuffer, vertexBufferIn.getDrawMode());
        }
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder quadsToTriangles ()V")
    private static native void BufferBuilder_quadsToTriangles(BufferBuilder builder) ;

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.vertex.VertexBuffer setDrawMode (I)V")
    private static native void VertexBuffer_setDrawMode(VertexBuffer builder, int arg1) ;

}
