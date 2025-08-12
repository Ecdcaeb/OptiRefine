package mods.Hileb.optirefine.mixin.minecraft.client.renderer.chunk;

import net.minecraft.util.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.BitSet;

@Mixin(targets = "net.minecraft.client.renderer.chunk.CompiledChunk$1")
public abstract class MixinCompiledChunk_1 {
    @Unique
    public void setAnimatedSprites(BlockRenderLayer layer, BitSet animatedSprites) {
        throw new UnsupportedOperationException();
    }
}
