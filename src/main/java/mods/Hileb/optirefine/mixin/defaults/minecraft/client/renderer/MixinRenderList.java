package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderList;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.Buffer;
import java.nio.IntBuffer;

@Mixin(RenderList.class)
public abstract class MixinRenderList extends ChunkRenderContainer {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0
// [AUDIT-OK] OF field viewEntityX equivalent (prefixed @Unique private, internal only)
    @Unique
    private double optiRefine$viewEntityX;
    @Unique
// [AUDIT-OK] OF field viewEntityY equivalent
    private double optiRefine$viewEntityY;
// [AUDIT-OK] OF field viewEntityZ equivalent
    private double optiRefine$viewEntityZ;

// [AUDIT-OK] OF-added field (OF: package-private IntBuffer bufferLists), not in baseline
    @Unique
    IntBuffer bufferLists;

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionX I")
// [AUDIT-OK] OF field RenderChunk.regionX (MixinRenderChunk @Unique public), not in baseline
    private static native int optiRefine$getRenderChunkRegionX(RenderChunk renderChunk);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionZ I")
// [AUDIT-OK] OF field RenderChunk.regionZ, not in baseline
    private static native int optiRefine$getRenderChunkRegionZ(RenderChunk renderChunk);

    @WrapMethod(method = "renderChunkLayer")
    private void optiRefine$renderChunkLayer(BlockRenderLayer layer, Operation<Void> original){
// [AUDIT-OK] target renderChunkLayer(Lnet/minecraft/util/BlockRenderLayer;)V (SRG func_178001_a) matches baseline; body == OF RenderList.renderChunkLayer (region + multiTexture bindCurrentTexture)
        if (this.initialized) {
            if (!Config.isRenderRegions()) {
                for (RenderChunk renderChunk : this.renderChunks) {
                    ListedRenderChunk listedRenderChunk = (ListedRenderChunk) renderChunk;
                    GlStateManager.pushMatrix();
                    this.preRenderChunk(renderChunk);
                    GlStateManager.callList(listedRenderChunk.getDisplayList(layer, listedRenderChunk.getCompiledChunk()));
                    GlStateManager.popMatrix();
                }
            } else {
                int var2 = Integer.MIN_VALUE;
                int var3 = Integer.MIN_VALUE;
                for (RenderChunk var5 : this.renderChunks) {
                    ListedRenderChunk var6 = (ListedRenderChunk)var5;
                    if (var2 != optiRefine$getRenderChunkRegionX(var5) || var3 != optiRefine$getRenderChunkRegionZ(var5)) {
                        if (this.bufferLists.position() > 0) {
                            this.optiRefine$drawRegion(var2, var3, this.bufferLists);
                        }
                        var2 = optiRefine$getRenderChunkRegionX(var5);
                        var3 = optiRefine$getRenderChunkRegionZ(var5);
                    }
                    if (this.bufferLists.position() >= this.bufferLists.capacity()) {
                        IntBuffer var7 = GLAllocation.createDirectIntBuffer(this.bufferLists.capacity() * 2);
                        ((Buffer)this.bufferLists).flip();
                        var7.put(this.bufferLists);
                        this.bufferLists = var7;
                    }
                    this.bufferLists.put(var6.getDisplayList(layer, var6.getCompiledChunk()));
                }
                if (this.bufferLists.position() > 0) {
                    this.optiRefine$drawRegion(var2, var3, this.bufferLists);
                }
            } if (Config.isMultiTexture()) {
// [AUDIT-OK] OF member GlStateManager.bindCurrentTexture (MixinGlStateManager @Public), not in baseline
                optiRefine$bindGlStateCurrentTexture();
            }


            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }

    @Override
    public void initialize(double viewEntityX, double viewEntityY, double viewEntityZ) {
// [AUDIT-OK] overrides inherited ChunkRenderContainer.initialize (SRG func_178004_a), matches OF; super call valid
        this.optiRefine$viewEntityX = viewEntityX;
        this.optiRefine$viewEntityY = viewEntityY;
        this.optiRefine$viewEntityZ = viewEntityZ;
        super.initialize(viewEntityX, viewEntityY, viewEntityZ);
    }

    @Unique
// [AUDIT-OK] OF-added method (OF: private drawRegion), not in baseline
    private void optiRefine$drawRegion(int regionX, int regionZ, IntBuffer displayLists) {
        GlStateManager.pushMatrix();
        this.preRenderRegion(regionX, 0, regionZ);
        ((Buffer)displayLists).flip();
        optiRefine$callGlStateLists(displayLists);
        ((Buffer)displayLists).clear();
        GlStateManager.popMatrix();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added method (OF: public preRenderRegion), not in baseline
    public void preRenderRegion(int regionX, int regionY, int regionZ) {
        GlStateManager.translate((float)(regionX - this.optiRefine$viewEntityX), (float)(regionY - this.optiRefine$viewEntityY), (float)(regionZ - this.optiRefine$viewEntityZ));
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation.Reference(GlStateManager.class)
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager bindCurrentTexture ()V")
    private static native void optiRefine$bindGlStateCurrentTexture();

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation.Reference(GlStateManager.class)
    @AccessibleOperation.Reference(IntBuffer.class)
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager callLists (Ljava.nio.IntBuffer;)V")
// [AUDIT-OK] OF member GlStateManager.callLists (MixinGlStateManager @Public), not in baseline
    private static native void optiRefine$callGlStateLists(IntBuffer intBuffer);

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initializeRenderListBuffer(CallbackInfo ci) {
        this.bufferLists = GLAllocation.createDirectIntBuffer(16);
    }
}
