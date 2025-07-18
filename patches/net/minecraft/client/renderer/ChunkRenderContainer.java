package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;

public abstract class ChunkRenderContainer {
   private double viewEntityX;
   private double viewEntityY;
   private double viewEntityZ;
   protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity(17424);
   protected boolean initialized;

   public void initialize(double var1, double var3, double var5) {
      this.initialized = true;
      this.renderChunks.clear();
      this.viewEntityX = ☃;
      this.viewEntityY = ☃;
      this.viewEntityZ = ☃;
   }

   public void preRenderChunk(RenderChunk var1) {
      BlockPos ☃ = ☃.getPosition();
      GlStateManager.translate((float)(☃.getX() - this.viewEntityX), (float)(☃.getY() - this.viewEntityY), (float)(☃.getZ() - this.viewEntityZ));
   }

   public void addRenderChunk(RenderChunk var1, BlockRenderLayer var2) {
      this.renderChunks.add(☃);
   }

   public abstract void renderChunkLayer(BlockRenderLayer var1);
}
