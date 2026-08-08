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
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(WorldVertexBufferUploader.class)
public abstract class MixinWorldVertexBufferUploader {
// [DIAG-TEMP] 2026-08-08: remove with the draw-stream probe
    @Unique
    private static long optiRefine$diagLastLog = 0L;
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0
    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;getVertexFormat()Lnet/minecraft/client/renderer/vertex/VertexFormat;"))
    public void beforeDraw(BufferBuilder vertexBufferIn, CallbackInfo ci){
// [AUDIT-OK] target draw(Lnet/minecraft/client/renderer/BufferBuilder;)V (SRG func_181679_a) matches baseline; runs after isDrawing/vertexCount guards (getVertexFormat INVOKE) so quadsToTriangles safe
        if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            BufferBuilder_quadsToTriangles(vertexBufferIn);
        }
    }

    @WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;glDrawArrays(III)V"))
    public void openShader(int mode, int first, int count, Operation<Void> original, @Local(argsOnly = true) BufferBuilder vertexBufferIn){
// [AUDIT-OK] target GlStateManager.glDrawArrays(III)V in draw matches baseline; multiTexture/shaders/vanilla branches == OF
// [DIAG-TEMP] 2026-08-08: draw-stream probe for the hand-item corruption; remove after diagnosis
        if (Config.isShaders() && System.currentTimeMillis() > optiRefine$diagLastLog + 2000L) {
            optiRefine$diagLastLog = System.currentTimeMillis();
            String caller = "?";
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            for (StackTraceElement e : st) {
                String cn = e.getClassName();
                if (cn.startsWith("net.minecraft") && !cn.contains("optirefine") && !cn.contains("Tessellator") && !cn.contains("WorldVertexBufferUploader")) {
                    caller = cn.substring(cn.lastIndexOf('.') + 1) + "." + e.getMethodName();
                    break;
                }
            }
            String prog = "none";
            try {
                prog = net.optifine.shaders.Shaders.activeProgram.getName();
            } catch (Throwable t) {
                prog = "err:" + t.getMessage();
            }
            mods.Hileb.optirefine.core.OptiRefineLog.log.info("[DIAG] draw fmt={} mode={} count={} multi={} prog={} caller={}", vertexBufferIn.getVertexFormat().getSize(), mode, count, BufferBuilder_isMultiTexture(vertexBufferIn), prog, caller);
        }
        if (BufferBuilder_isMultiTexture(vertexBufferIn)) {
            BufferBuilder_drawMultiTexture(vertexBufferIn);
        } else if (Config.isShaders()) {
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
