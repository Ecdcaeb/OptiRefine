package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.optifine.shaders.ShaderVertexData;
import net.optifine.shaders.SVertexBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.nio.IntBuffer;

@Mixin(value = SVertexBuilder.class, remap = false)
public abstract class MixinSVertexBuilder implements ShaderVertexData {
    @Shadow int vertexSize;
    @Shadow long[] entityData;
    @Shadow int entityDataIndex;

    @Unique
    @Override
    public void optiRefine$writeEntityData(IntBuffer destination, int start, int length) {
        if (this.vertexSize != 14) return;
        long entity = this.entityData[this.entityDataIndex];
        for (int offset = 12; offset + 1 < length; offset += this.vertexSize) {
            destination.put(start + offset, (int)entity);
            destination.put(start + offset + 1, (int)(entity >>> 32));
        }
    }
}
