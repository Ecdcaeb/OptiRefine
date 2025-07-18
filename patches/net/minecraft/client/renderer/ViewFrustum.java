package net.minecraft.client.renderer;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ViewFrustum {
   protected final RenderGlobal renderGlobal;
   protected final World world;
   protected int countChunksY;
   protected int countChunksX;
   protected int countChunksZ;
   public RenderChunk[] renderChunks;

   public ViewFrustum(World var1, int var2, RenderGlobal var3, IRenderChunkFactory var4) {
      this.renderGlobal = ☃;
      this.world = ☃;
      this.setCountChunksXYZ(☃);
      this.createRenderChunks(☃);
   }

   protected void createRenderChunks(IRenderChunkFactory var1) {
      int ☃ = this.countChunksX * this.countChunksY * this.countChunksZ;
      this.renderChunks = new RenderChunk[☃];
      int ☃x = 0;

      for (int ☃xx = 0; ☃xx < this.countChunksX; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < this.countChunksY; ☃xxx++) {
            for (int ☃xxxx = 0; ☃xxxx < this.countChunksZ; ☃xxxx++) {
               int ☃xxxxx = (☃xxxx * this.countChunksY + ☃xxx) * this.countChunksX + ☃xx;
               this.renderChunks[☃xxxxx] = ☃.create(this.world, this.renderGlobal, ☃x++);
               this.renderChunks[☃xxxxx].setPosition(☃xx * 16, ☃xxx * 16, ☃xxxx * 16);
            }
         }
      }
   }

   public void deleteGlResources() {
      for (RenderChunk ☃ : this.renderChunks) {
         ☃.deleteGlResources();
      }
   }

   protected void setCountChunksXYZ(int var1) {
      int ☃ = ☃ * 2 + 1;
      this.countChunksX = ☃;
      this.countChunksY = 16;
      this.countChunksZ = ☃;
   }

   public void updateChunkPositions(double var1, double var3) {
      int ☃ = MathHelper.floor(☃) - 8;
      int ☃x = MathHelper.floor(☃) - 8;
      int ☃xx = this.countChunksX * 16;

      for (int ☃xxx = 0; ☃xxx < this.countChunksX; ☃xxx++) {
         int ☃xxxx = this.getBaseCoordinate(☃, ☃xx, ☃xxx);

         for (int ☃xxxxx = 0; ☃xxxxx < this.countChunksZ; ☃xxxxx++) {
            int ☃xxxxxx = this.getBaseCoordinate(☃x, ☃xx, ☃xxxxx);

            for (int ☃xxxxxxx = 0; ☃xxxxxxx < this.countChunksY; ☃xxxxxxx++) {
               int ☃xxxxxxxx = ☃xxxxxxx * 16;
               RenderChunk ☃xxxxxxxxx = this.renderChunks[(☃xxxxx * this.countChunksY + ☃xxxxxxx) * this.countChunksX + ☃xxx];
               ☃xxxxxxxxx.setPosition(☃xxxx, ☃xxxxxxxx, ☃xxxxxx);
            }
         }
      }
   }

   private int getBaseCoordinate(int var1, int var2, int var3) {
      int ☃ = ☃ * 16;
      int ☃x = ☃ - ☃ + ☃ / 2;
      if (☃x < 0) {
         ☃x -= ☃ - 1;
      }

      return ☃ - ☃x / ☃ * ☃;
   }

   public void markBlocksForUpdate(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      int ☃ = MathHelper.intFloorDiv(☃, 16);
      int ☃x = MathHelper.intFloorDiv(☃, 16);
      int ☃xx = MathHelper.intFloorDiv(☃, 16);
      int ☃xxx = MathHelper.intFloorDiv(☃, 16);
      int ☃xxxx = MathHelper.intFloorDiv(☃, 16);
      int ☃xxxxx = MathHelper.intFloorDiv(☃, 16);

      for (int ☃xxxxxx = ☃; ☃xxxxxx <= ☃xxx; ☃xxxxxx++) {
         int ☃xxxxxxx = ☃xxxxxx % this.countChunksX;
         if (☃xxxxxxx < 0) {
            ☃xxxxxxx += this.countChunksX;
         }

         for (int ☃xxxxxxxx = ☃x; ☃xxxxxxxx <= ☃xxxx; ☃xxxxxxxx++) {
            int ☃xxxxxxxxx = ☃xxxxxxxx % this.countChunksY;
            if (☃xxxxxxxxx < 0) {
               ☃xxxxxxxxx += this.countChunksY;
            }

            for (int ☃xxxxxxxxxx = ☃xx; ☃xxxxxxxxxx <= ☃xxxxx; ☃xxxxxxxxxx++) {
               int ☃xxxxxxxxxxx = ☃xxxxxxxxxx % this.countChunksZ;
               if (☃xxxxxxxxxxx < 0) {
                  ☃xxxxxxxxxxx += this.countChunksZ;
               }

               int ☃xxxxxxxxxxxx = (☃xxxxxxxxxxx * this.countChunksY + ☃xxxxxxxxx) * this.countChunksX + ☃xxxxxxx;
               RenderChunk ☃xxxxxxxxxxxxx = this.renderChunks[☃xxxxxxxxxxxx];
               ☃xxxxxxxxxxxxx.setNeedsUpdate(☃);
            }
         }
      }
   }

   @Nullable
   protected RenderChunk getRenderChunk(BlockPos var1) {
      int ☃ = MathHelper.intFloorDiv(☃.getX(), 16);
      int ☃x = MathHelper.intFloorDiv(☃.getY(), 16);
      int ☃xx = MathHelper.intFloorDiv(☃.getZ(), 16);
      if (☃x >= 0 && ☃x < this.countChunksY) {
         ☃ %= this.countChunksX;
         if (☃ < 0) {
            ☃ += this.countChunksX;
         }

         ☃xx %= this.countChunksZ;
         if (☃xx < 0) {
            ☃xx += this.countChunksZ;
         }

         int ☃xxx = (☃xx * this.countChunksY + ☃x) * this.countChunksX + ☃;
         return this.renderChunks[☃xxx];
      } else {
         return null;
      }
   }
}
