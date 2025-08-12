package mods.Hileb.optirefine.mixin.defaults.minecraft.util.math;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChunkPos.class)
public abstract class MixinChunkPos {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private int cachedHashCode = 0;

    @WrapMethod(method = "hashCode", remap = false)
    public int injectHashCode(Operation<Integer> original){
        if (cachedHashCode != 0) {
            return cachedHashCode;
        } else {
            return cachedHashCode = original.call();
        }
    }
}
