package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;


import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.util.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;
@Mixin(CompiledChunk.class)
public abstract class MixinCompiledChunk {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-FIXED] three-way audit (P10): cleanmix does not inject instance-field initializers ->
    // `= new BitSet[...]` left the field null; set/getAnimatedSprites callers (MixinRenderChunk:197/213,
    // MixinChunkRenderContainer:49) would NPE. Init moved to <init>* RETURN per convention.
    // Array length BlockRenderLayer.values().length == ENUM_WORLD_BLOCK_LAYERS.length (4)
    private BitSet[] optiRefine$animatedSprites;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void optiRefine$initFields(CallbackInfo ci) {
        this.optiRefine$animatedSprites = new BitSet[BlockRenderLayer.values().length];
    }

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
