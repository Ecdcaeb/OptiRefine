package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.util.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.BitSet;

@Mixin(CompiledChunk.class)
public abstract class MixinCompiledChunk {

    @Unique
    private BitSet[] optiRefine$animatedSprites = new BitSet[BlockRenderLayer.values().length];

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public BitSet getAnimatedSprites(BlockRenderLayer layer) {
        return this.optiRefine$animatedSprites[layer.ordinal()];
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void setAnimatedSprites(BlockRenderLayer layer, BitSet animatedSprites) {
        this.optiRefine$animatedSprites[layer.ordinal()] = animatedSprites;
    }


}
