package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.optifine.render.VboRegion;
import net.optifine.shaders.ShadersRender;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(VboRenderList.class)
public abstract class MixinVboRenderList extends ChunkRenderContainer {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0
    @Unique
// [AUDIT-OK] OF field viewEntityX equivalent (prefixed @Unique private, internal only)
    private double optiRefine$viewEntityX;
    @Unique
// [AUDIT-OK] OF field viewEntityY equivalent
    private double optiRefine$viewEntityY;
    @Unique
// [AUDIT-OK] OF field viewEntityZ equivalent
    private double optiRefine$viewEntityZ;

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.vertex.VertexBuffer getVboRegion ()Lnet.optifine.render.VboRegion;")
// [AUDIT-OK] OF member VertexBuffer.getVboRegion (MixinVertexBuffer @Unique public), not in baseline
    private static native VboRegion VertexBuffer_getVboRegion(VertexBuffer vertexBuffer);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionX I")
// [AUDIT-OK] OF field RenderChunk.regionX, not in baseline
    private static native int RenderChunk_regionX(RenderChunk renderChunk);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionZ I")
// [AUDIT-OK] OF field RenderChunk.regionZ, not in baseline
    private static native int RenderChunk_regionZ(RenderChunk renderChunk);

    @ModifyExpressionValue(method = "renderChunkLayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/VboRenderList;initialized:Z"))
    public boolean shouldRender(boolean original, @Local(argsOnly = true) BlockRenderLayer layer){
// [AUDIT-OK] target renderChunkLayer (SRG func_178001_a) GETFIELD initialized matches baseline; region loop == OF VboRenderList.renderChunkLayer
        if (original) {
            if (Config.isRenderRegions()){
                int regionX = Integer.MIN_VALUE;
                int regionZ = Integer.MIN_VALUE;
                VboRegion lastVboRegion = null;

                for (RenderChunk renderchunk : this.renderChunks) {
                    VertexBuffer vertexbuffer = renderchunk.getVertexBufferByLayer(layer.ordinal());
                    VboRegion vboRegion = VertexBuffer_getVboRegion(vertexbuffer);
                    if (vboRegion != lastVboRegion || regionX != RenderChunk_regionX(renderchunk) || regionZ != RenderChunk_regionZ(renderchunk)) {
                        if (lastVboRegion != null) {
                            this.optiRefine$drawRegion(regionX, regionZ, lastVboRegion);
                        }

                        regionX = RenderChunk_regionX(renderchunk);
                        regionZ = RenderChunk_regionZ(renderchunk);
                        lastVboRegion = vboRegion;
                    }

                    vertexbuffer.drawArrays(7);
                }

                if (lastVboRegion != null) {
                    this.optiRefine$drawRegion(regionX, regionZ, lastVboRegion);
                }
                return false;
            }
            return true;
        }
        return false;
    }

    @Inject(method = "renderChunkLayer", at = @At("TAIL"))
    public void afterRender(BlockRenderLayer layer, CallbackInfo ci){
// [AUDIT-OK] target renderChunkLayer matches baseline; tail (glBindBuffer/resetColor/clear) matches OF
        if (this.initialized) {
            OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }

    @WrapMethod(method = "setupArrayPointers")
    public void setupArrayPointersForShaders(Operation<Void> original){
// [AUDIT-OK] target setupArrayPointers()V (SRG func_178010_a) matches baseline; shaders branch matches OF
        if (Config.isShaders()) {
            ShadersRender.setupArrayPointersVbo();
        } else {
            original.call();
        }
    }

    @Override
    public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
// [AUDIT-OK] overrides inherited ChunkRenderContainer.initialize (SRG func_178004_a), matches OF
        this.optiRefine$viewEntityX = viewEntityXIn;
        this.optiRefine$viewEntityY = viewEntityYIn;
        this.optiRefine$viewEntityZ = viewEntityZIn;
        super.initialize(viewEntityXIn, viewEntityYIn, viewEntityZIn);
    }

    @Unique
// [AUDIT-OK] OF-added method (OF: private drawRegion), not in baseline
    private void optiRefine$drawRegion(int regionX, int regionZ, VboRegion vboRegion) {
        GlStateManager.pushMatrix();
        this.optiRefine$preRenderRegion(regionX, 0, regionZ);
        vboRegion.finishDraw((VboRenderList)(Object)this);
        GlStateManager.popMatrix();
    }

    @Unique
// [AUDIT-OK] OF-added method (OF: public preRenderRegion), not in baseline
    public void optiRefine$preRenderRegion(int x, int y, int z) {
        GlStateManager.translate((float)(x - this.optiRefine$viewEntityX), (float)(y - this.optiRefine$viewEntityY), (float)(z - this.optiRefine$viewEntityZ));
    }
}
