package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import java.util.BitSet;
import java.util.List;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.optifine.SmartAnimations;

public abstract class ChunkRenderContainer {
   private double viewEntityX;
   private double viewEntityY;
   private double viewEntityZ;
   protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity(17424);
   protected boolean initialized;
   private BitSet animatedSpritesRendered;
   private final BitSet animatedSpritesCached = new BitSet();

   public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
      this.initialized = true;
      this.renderChunks.clear();
      this.viewEntityX = viewEntityXIn;
      this.viewEntityY = viewEntityYIn;
      this.viewEntityZ = viewEntityZIn;
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

   public void preRenderChunk(RenderChunk renderChunkIn) {
      BlockPos blockpos = renderChunkIn.getPosition();
      GlStateManager.translate(
         (float)(blockpos.getX() - this.viewEntityX), (float)(blockpos.getY() - this.viewEntityY), (float)(blockpos.getZ() - this.viewEntityZ)
      );
   }

   public void addRenderChunk(RenderChunk renderChunkIn, BlockRenderLayer layer) {
      this.renderChunks.add(renderChunkIn);
      if (this.animatedSpritesRendered != null) {
         BitSet animatedSprites = renderChunkIn.compiledChunk.getAnimatedSprites(layer);
         if (animatedSprites != null) {
            this.animatedSpritesRendered.or(animatedSprites);
         }
      }
   }

   public abstract void renderChunkLayer(BlockRenderLayer var1);
}
