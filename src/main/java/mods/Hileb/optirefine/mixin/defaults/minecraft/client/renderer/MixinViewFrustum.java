package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.optifine.render.VboRegion;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(ViewFrustum.class)
public abstract class MixinViewFrustum {

    @Shadow
    public RenderChunk[] renderChunks;

    @Unique
    private Map<ChunkPos, VboRegion[]> optiRefine$mapVboRegions = new HashMap<>();

    @WrapOperation(method = "createRenderChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;setPosition(III)V"))
    public void updateMapVboRegions(RenderChunk instance, int i, int x, int y, Operation<Void> original){
        original.call(instance, i, x, y);
        if (Config.isVbo() && Config.isRenderRegions()) {
            this.optiRefine$updateVboRegion(instance);
        }
    }

    @Inject(method = "createRenderChunks", at = @At("TAIL"))
    public void postUpdateVboRegion(IRenderChunkFactory renderChunkFactory, CallbackInfo ci){
        for (RenderChunk renderChunk : this.renderChunks) {
            for (int l = 0; l < EnumFacing.VALUES.length; l++) {
                EnumFacing facing = EnumFacing.VALUES[l];
                BlockPos posOffset16 = renderChunk.getBlockPosOffset16(facing);
                RenderChunk neighbour = this.getRenderChunk(posOffset16);
                RenderChunk_setRenderChunkNeighbour(renderChunk, facing, neighbour);
            }
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.RenderChunk setRenderChunkNeighbour (Lnet.minecraft.util.EnumFacing;Lnet.minecraft.client.renderer.chunk.RenderChunk;)V")
    private static native void RenderChunk_setRenderChunkNeighbour(RenderChunk renderChunk, EnumFacing enumFacing, RenderChunk neighbour);

    @Shadow
    protected abstract RenderChunk getRenderChunk(BlockPos pos);

    @Inject(method = "deleteGlResources", at = @At("TAIL"))
    public void extraDeleteVboRegions(CallbackInfo ci){
        this.deleteVboRegions();
    }

    @Unique
    private void optiRefine$updateVboRegion(RenderChunk renderChunk) {
        BlockPos pos = renderChunk.getPosition();
        int rx = pos.getX() >> 8 << 8;
        int rz = pos.getZ() >> 8 << 8;
        ChunkPos cp = new ChunkPos(rx, rz);
        BlockRenderLayer[] layers = BlockRenderLayer.values();
        VboRegion[] regions = this.optiRefine$mapVboRegions.get(cp);
        if (regions == null) {
            regions = new VboRegion[layers.length];

            for (int ix = 0; ix < layers.length; ix++) {
                regions[ix] = new VboRegion(layers[ix]);
            }

            this.optiRefine$mapVboRegions.put(cp, regions);
        }

        for (int ix = 0; ix < layers.length; ix++) {
            VboRegion vr = regions[ix];
            if (vr != null) {
                VertexBuffer_setVboRegion( renderChunk.getVertexBufferByLayer(ix), vr);
            }
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.vertex.VertexBuffer setVboRegion (Lnet.optifine.render.VboRegion;)V")
    private static native void VertexBuffer_setVboRegion(VertexBuffer vertexBuffer, VboRegion vboRegion);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void deleteVboRegions() {
        for (ChunkPos cp : this.optiRefine$mapVboRegions.keySet()) {
            VboRegion[] vboRegions = this.optiRefine$mapVboRegions.get(cp);

            for (int i = 0; i < vboRegions.length; i++) {
                VboRegion vboRegion = vboRegions[i];
                if (vboRegion != null) {
                    vboRegion.deleteGlBuffers();
                }

                vboRegions[i] = null;
            }
        }

        this.optiRefine$mapVboRegions.clear();
    }
}
