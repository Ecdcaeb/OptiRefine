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

import java.nio.Buffer;
import java.nio.IntBuffer;

@Mixin(RenderList.class)
public abstract class MixinRenderList extends ChunkRenderContainer {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0
// [AUDIT-OK] OF field viewEntityX equivalent (prefixed @Unique private, internal only)
    private double optiRefine$viewEntityX;
    @Unique
// [AUDIT-OK] OF field viewEntityY equivalent
    private double optiRefine$viewEntityY;
// [AUDIT-OK] OF field viewEntityZ equivalent
    private double optiRefine$viewEntityZ;

// [AUDIT-OK] OF-added field (OF: package-private IntBuffer bufferLists), not in baseline
    @Unique
    IntBuffer bufferLists = GLAllocation.createDirectIntBuffer(16);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionX I")
// [AUDIT-OK] OF field RenderChunk.regionX (MixinRenderChunk @Unique public), not in baseline
    private static native int RenderChunk_regionX(RenderChunk r);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionZ I")
// [AUDIT-OK] OF field RenderChunk.regionZ, not in baseline
    private static native int RenderChunk_regionZ(RenderChunk r);

    @WrapMethod(method = "renderChunkLayer")
    private void addElseForRender(BlockRenderLayer layer, Operation<Void> original){
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
                    if (var2 != RenderChunk_regionX(var5) || var3 != RenderChunk_regionZ(var5)) {
                        if (this.bufferLists.position() > 0) {
                            this.optiRefine$drawRegion(var2, var3, this.bufferLists);
                        }
                        var2 = RenderChunk_regionX(var5);
                        var3 = RenderChunk_regionZ(var5);
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
                GlStateManager_bindCurrentTexture();
            }


            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }

    @Override
    public void initialize(double var1, double var3, double var5) {
// [AUDIT-OK] overrides inherited ChunkRenderContainer.initialize (SRG func_178004_a), matches OF; super call valid
        this.optiRefine$viewEntityX = var1;
        this.optiRefine$viewEntityY = var3;
        this.optiRefine$viewEntityZ = var5;
        super.initialize(var1, var3, var5);
    }

    @Unique
// [AUDIT-OK] OF-added method (OF: private drawRegion), not in baseline
    private void optiRefine$drawRegion(int var1, int var2, IntBuffer var3) {
        GlStateManager.pushMatrix();
        this.preRenderRegion(var1, 0, var2);
        ((Buffer)var3).flip();
        GlStateManager_callLists(var3);
        ((Buffer)var3).clear();
        GlStateManager.popMatrix();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
// [AUDIT-OK] OF-added method (OF: public preRenderRegion), not in baseline
    public void preRenderRegion(int var1, int var2, int var3) {
        GlStateManager.translate((float)(var1 - this.optiRefine$viewEntityX), (float)(var2 - this.optiRefine$viewEntityY), (float)(var3 - this.optiRefine$viewEntityZ));
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation.Reference(GlStateManager.class)
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager bindCurrentTexture ()V")
    private static native void GlStateManager_bindCurrentTexture();

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation.Reference(GlStateManager.class)
    @AccessibleOperation.Reference(IntBuffer.class)
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager callLists (Ljava.nio.IntBuffer;)V")
// [AUDIT-OK] OF member GlStateManager.callLists (MixinGlStateManager @Public), not in baseline
    private static native void GlStateManager_callLists(IntBuffer intBuffer);
}
