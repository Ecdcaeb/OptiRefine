package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.nio.IntBuffer;
import mods.Hileb.optirefine.optifine.Config;
import net.optifine.render.VboRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * OF VboRegion.drawArrays records one (index,count) pair per RenderChunk into
 * capacity-sized IntBuffers (4096). A 256x256 region at world height 256 is
 * exactly 16*16*16 = 4096 chunks; one extra put overflows (Java 25
 * BufferOverflowException, VboRegion.java:310). Grow the index buffers like
 * expandVbo() already does for the GL buffer.
 */
@Mixin(targets = "net.optifine.render.VboRegion")
public abstract class MixinVboRegion {

    @Shadow(remap = false)
    @Mutable
    private IntBuffer bufferIndexVertex;

    @Shadow(remap = false)
    @Mutable
    private IntBuffer bufferCountVertex;

    @WrapMethod(method = "drawArrays", remap = false)
    private void optiRefine$growDrawBuffers(int drawMode, VboRange range, Operation<Void> original) {
        if (!this.bufferIndexVertex.hasRemaining()) {
            int used = this.bufferIndexVertex.position();
            int newCap = Math.max(used * 2, used + 256);
            IntBuffer nextIndex = Config.createDirectIntBuffer(newCap);
            IntBuffer nextCount = Config.createDirectIntBuffer(newCap);
            this.bufferIndexVertex.flip();
            this.bufferCountVertex.flip();
            nextIndex.put(this.bufferIndexVertex);
            nextCount.put(this.bufferCountVertex);
            this.bufferIndexVertex = nextIndex;
            this.bufferCountVertex = nextCount;
        }
        original.call(drawMode, range);
    }
}
