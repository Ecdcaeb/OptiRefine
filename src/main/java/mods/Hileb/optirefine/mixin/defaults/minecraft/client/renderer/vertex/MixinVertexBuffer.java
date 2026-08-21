package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.vertex;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.optifine.render.VboRange;
import net.optifine.render.VboRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.nio.ByteBuffer;
@Mixin(VertexBuffer.class)
public abstract class MixinVertexBuffer {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added fields vboRegion/vboRange/drawMode (OF VertexBuffer:13-15)
    private VboRegion vboRegion;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private VboRange vboRange;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    private int drawMode;

    @WrapMethod(method = "bufferData")
    // [AUDIT-OK] bufferData baseline; vboRegion branch matches OF:26-29
    public void wrapbufferData(ByteBuffer p_181722_1_, Operation<Void> original){
        if (this.vboRegion != null) {
            this.vboRegion.bufferData(p_181722_1_, this.vboRange);
        } else {
            original.call(p_181722_1_);
        }
    }

    @WrapMethod(method = "drawArrays")
    public void wrapdrawArrays(int mode, Operation<Void> original){
        if (this.drawMode > 0) {
            mode = this.drawMode;
        }

        if (this.vboRegion != null && Config.isRenderRegions()) {
            this.vboRegion.drawArrays(mode, this.vboRange);
        } else {
            original.call(mode);
        }
    }

    @Shadow
    // [AUDIT-OK] baseline member deleteGlBuffers
    public abstract void deleteGlBuffers();

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    // [AUDIT-OK] OF-added members setVboRegion/getVboRegion/getVboRange/getDrawMode/setDrawMode (OF:60+)
    public void setVboRegion(VboRegion vboRegion) {
        if (vboRegion != null) {
            this.deleteGlBuffers();
            this.vboRegion = vboRegion;
            this.vboRange = new VboRange();
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    public VboRegion getVboRegion() {
        return this.vboRegion;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public VboRange getVboRange() {
        return this.vboRange;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    public int getDrawMode() {
        return this.drawMode;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public void setDrawMode(int drawMode) {
        this.drawMode = drawMode;
    }

}
