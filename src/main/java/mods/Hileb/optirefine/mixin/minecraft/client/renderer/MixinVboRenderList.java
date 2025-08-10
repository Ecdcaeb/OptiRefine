package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

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
    @Unique
    private double optiRefine$viewEntityX;
    @Unique
    private double optiRefine$viewEntityY;
    @Unique
    private double optiRefine$viewEntityZ;

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.vertex.VertexBuffer getVboRegion ()Lnet.optifine.render.VboRegion;")
    private static native VboRegion VertexBuffer_getVboRegion(VertexBuffer vertexBuffer);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionX I")
    private static native int RenderChunk_regionX(RenderChunk renderChunk);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionZ I")
    private static native int RenderChunk_regionZ(RenderChunk renderChunk);

    @ModifyExpressionValue(method = "renderChunkLayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/VboRenderList;initialized:Z"))
    public boolean shouldRender(boolean original, @Local(argsOnly = true) BlockRenderLayer layer){
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
            }
        }
        return false;
    }

    @Inject(method = "renderChunkLayer", at = @At("TAIL"))
    public void afterRender(BlockRenderLayer layer, CallbackInfo ci){
        if (this.initialized) {
            OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }

    @WrapMethod(method = "setupArrayPointers")
    public void setupArrayPointersForShaders(Operation<Void> original){
        if (Config.isShaders()) {
            ShadersRender.setupArrayPointersVbo();
        } else {
            original.call();
        }
    }

    @Override
    public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
        this.optiRefine$viewEntityX = viewEntityXIn;
        this.optiRefine$viewEntityY = viewEntityYIn;
        this.optiRefine$viewEntityZ = viewEntityZIn;
        super.initialize(viewEntityXIn, viewEntityYIn, viewEntityZIn);
    }

    @Unique
    private void optiRefine$drawRegion(int regionX, int regionZ, VboRegion vboRegion) {
        GlStateManager.pushMatrix();
        this.optiRefine$preRenderRegion(regionX, 0, regionZ);
        vboRegion.finishDraw((VboRenderList)(Object)this);
        GlStateManager.popMatrix();
    }

    @Unique
    public void optiRefine$preRenderRegion(int x, int y, int z) {
        GlStateManager.translate((float)(x - this.optiRefine$viewEntityX), (float)(y - this.optiRefine$viewEntityY), (float)(z - this.optiRefine$viewEntityZ));
    }
}
