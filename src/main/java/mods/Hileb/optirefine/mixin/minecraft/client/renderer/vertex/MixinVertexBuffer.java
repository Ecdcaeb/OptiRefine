package mods.Hileb.optirefine.mixin.minecraft.client.renderer.vertex;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.optifine.render.VboRange;
import net.optifine.render.VboRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.nio.ByteBuffer;

@Mixin(VertexBuffer.class)
public abstract class MixinVertexBuffer {
    @Unique
    private VboRegion vboRegion;
    @Unique
    private VboRange vboRange;
    @Unique
    private int drawMode;

    @WrapMethod(method = "bufferData")
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

        if (this.vboRegion != null) {
            this.vboRegion.drawArrays(mode, this.vboRange);
        } else {
            original.call(mode);
        }
    }

    @Shadow
    public abstract void deleteGlBuffers();

    @Unique
    public void setVboRegion(VboRegion vboRegion) {
        if (vboRegion != null) {
            this.deleteGlBuffers();
            this.vboRegion = vboRegion;
            this.vboRange = new VboRange();
        }
    }

    @Unique
    public VboRegion getVboRegion() {
        return this.vboRegion;
    }

    @Unique
    public VboRange getVboRange() {
        return this.vboRange;
    }

    @Unique
    public int getDrawMode() {
        return this.drawMode;
    }

    @Unique
    public void setDrawMode(int drawMode) {
        this.drawMode = drawMode;
    }

}
