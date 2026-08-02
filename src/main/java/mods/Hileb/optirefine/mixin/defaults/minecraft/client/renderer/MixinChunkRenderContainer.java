package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.optifine.SmartAnimations;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;
@Mixin(ChunkRenderContainer.class)
public abstract class MixinChunkRenderContainer {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 1
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field, not in baseline
    private BitSet animatedSpritesRendered;
    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field, not in baseline
    @Unique
    private final BitSet animatedSpritesCached = new BitSet();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void logicOfinit(CallbackInfo ci){
// [AUDIT-ISSUE] OF places this SmartAnimations block in initialize() (per-frame); mixin injects into <init> (once) -> per-frame spritesRendered() reporting/clear never happens; move the inject to initialize()
        if (SmartAnimations.isActive()) {
            if (this.animatedSpritesRendered != null) {
                SmartAnimations.spritesRendered(this.animatedSpritesRendered);
            } else {
                this.animatedSpritesRendered = this.animatedSpritesCached;
            }

            this.animatedSpritesRendered.clear();
        } else if (this.animatedSpritesRendered != null) {
            SmartAnimations.spritesRendered(this.animatedSpritesRendered);
            this.animatedSpritesRendered = null;
        }
    }

    @Inject(method = "addRenderChunk", at = @At("RETURN"))
    public void afterAddRenderChunk(RenderChunk renderChunkIn, BlockRenderLayer layer, CallbackInfo ci){
// [AUDIT-OK] target addRenderChunk(Lnet/minecraft/client/renderer/chunk/RenderChunk;Lnet/minecraft/util/BlockRenderLayer;)V (SRG func_178002_a) matches baseline; OR logic matches OF
        if (this.animatedSpritesRendered != null) {
            BitSet animatedSprites = CompiledChunk_getAnimatedSprites(renderChunkIn.compiledChunk, layer);
            if (animatedSprites != null) {
                this.animatedSpritesRendered.or(animatedSprites);
            }
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.CompiledChunk getAnimatedSprites (Lnet.minecraft.util.BlockRenderLayer;)Ljava.util.BitSet;")
// [AUDIT-OK] OF member CompiledChunk.getAnimatedSprites, not in baseline (MixinCompiledChunk @Unique provides); dot-desc converted by processor
    private native static BitSet CompiledChunk_getAnimatedSprites(CompiledChunk instance, BlockRenderLayer arg0) ;
}
