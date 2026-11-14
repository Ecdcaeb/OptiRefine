package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;


import net.minecraft.util.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;

import java.util.BitSet;
@Mixin(targets = "net.minecraft.client.renderer.chunk.CompiledChunk$1")
public abstract class MixinCompiledChunk_1 {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings("AddedMixinMembersNamePattern")
    public void setAnimatedSprites(BlockRenderLayer layer, BitSet animatedSprites) {
        throw new UnsupportedOperationException();
    }
}
