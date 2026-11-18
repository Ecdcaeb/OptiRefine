package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.BlockPos;
import net.optifine.override.ChunkCacheOF;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkCacheOF.class)
public abstract class MixinChunkCacheOF {
    @Shadow(remap = false)
    private int getPositionIndex(BlockPos pos) {
        throw new AssertionError();
    }

    // G5 rounds both endpoints out to 16-block sections, then converts its origin and sizes back.
    // Keep the original padded bounds in block units. getPositionIndex already handles misses.
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 4), require = 12, allow = 12)
    private int optiRefine$keepBlockCoordinates(int sectionShift) {
        return 0;
    }

    @Redirect(method = "getCombinedLight", at = @At(value = "INVOKE",
            target = "Lnet/optifine/override/ChunkCacheOF;getPositionIndex(Lnet/minecraft/util/math/BlockPos;)I", remap = false))
    private int optiRefine$cacheDefaultLight(ChunkCacheOF cache, BlockPos pos, @Local(argsOnly = true) int minimumBlockLight) {
        // The cache has one entry per position. A different light floor must use the raw path.
        return minimumBlockLight == 0 ? this.getPositionIndex(pos) : -1;
    }
}
