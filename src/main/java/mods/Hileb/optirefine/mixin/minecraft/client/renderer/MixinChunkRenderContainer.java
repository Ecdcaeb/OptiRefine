package mods.Hileb.optirefine.mixin.minecraft.client.renderer;

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
    @Unique
    private BitSet animatedSpritesRendered;
    @Unique
    private final BitSet animatedSpritesCached = new BitSet();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void logicOfinit(CallbackInfo ci){
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
        if (this.animatedSpritesRendered != null) {
            BitSet animatedSprites = CompiledChunk_getAnimatedSprites(renderChunkIn.compiledChunk, layer);
            if (animatedSprites != null) {
                this.animatedSpritesRendered.or(animatedSprites);
            }
        }
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.CompiledChunk getAnimatedSprites (Lnet.minecraft.util.BlockRenderLayer;)Ljava.util.BitSet;")
    private native static BitSet CompiledChunk_getAnimatedSprites(CompiledChunk instance, BlockRenderLayer arg0) ;
}
