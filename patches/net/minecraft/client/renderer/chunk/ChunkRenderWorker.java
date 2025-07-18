package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorker implements Runnable {
   private static final Logger LOGGER = LogManager.getLogger();
   private final ChunkRenderDispatcher chunkRenderDispatcher;
   private final RegionRenderCacheBuilder regionRenderCacheBuilder;
   private boolean shouldRun = true;

   public ChunkRenderWorker(ChunkRenderDispatcher var1) {
      this(☃, null);
   }

   public ChunkRenderWorker(ChunkRenderDispatcher var1, @Nullable RegionRenderCacheBuilder var2) {
      this.chunkRenderDispatcher = ☃;
      this.regionRenderCacheBuilder = ☃;
   }

   @Override
   public void run() {
      while (this.shouldRun) {
         try {
            this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
         } catch (InterruptedException var3) {
            LOGGER.debug("Stopping chunk worker due to interrupt");
            return;
         } catch (Throwable var4) {
            CrashReport ☃ = CrashReport.makeCrashReport(var4, "Batching chunks");
            Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(☃));
            return;
         }
      }
   }

   protected void processTask(final ChunkCompileTaskGenerator var1) throws InterruptedException {
      ☃.getLock().lock();

      try {
         if (☃.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
            if (!☃.isFinished()) {
               LOGGER.warn("Chunk render task was {} when I expected it to be pending; ignoring task", ☃.getStatus());
            }

            return;
         }

         BlockPos ☃ = new BlockPos(Minecraft.getMinecraft().player);
         BlockPos ☃x = ☃.getRenderChunk().getPosition();
         int ☃xx = 16;
         int ☃xxx = 8;
         int ☃xxxx = 24;
         if (☃x.add(8, 8, 8).distanceSq(☃) > 576.0) {
            World ☃xxxxx = ☃.getRenderChunk().getWorld();
            BlockPos.MutableBlockPos ☃xxxxxx = new BlockPos.MutableBlockPos(☃x);
            if (!this.isChunkExisting(☃xxxxxx.setPos(☃x).move(EnumFacing.WEST, 16), ☃xxxxx)
               || !this.isChunkExisting(☃xxxxxx.setPos(☃x).move(EnumFacing.NORTH, 16), ☃xxxxx)
               || !this.isChunkExisting(☃xxxxxx.setPos(☃x).move(EnumFacing.EAST, 16), ☃xxxxx)
               || !this.isChunkExisting(☃xxxxxx.setPos(☃x).move(EnumFacing.SOUTH, 16), ☃xxxxx)) {
               return;
            }
         }

         ☃.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
      } finally {
         ☃.getLock().unlock();
      }

      Entity ☃ = Minecraft.getMinecraft().getRenderViewEntity();
      if (☃ == null) {
         ☃.finish();
      } else {
         ☃.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
         float ☃x = (float)☃.posX;
         float ☃xx = (float)☃.posY + ☃.getEyeHeight();
         float ☃xxx = (float)☃.posZ;
         ChunkCompileTaskGenerator.Type ☃xxxx = ☃.getType();
         if (☃xxxx == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
            ☃.getRenderChunk().rebuildChunk(☃x, ☃xx, ☃xxx, ☃);
         } else if (☃xxxx == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
            ☃.getRenderChunk().resortTransparency(☃x, ☃xx, ☃xxx, ☃);
         }

         ☃.getLock().lock();

         try {
            if (☃.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
               if (!☃.isFinished()) {
                  LOGGER.warn("Chunk render task was {} when I expected it to be compiling; aborting task", ☃.getStatus());
               }

               this.freeRenderBuilder(☃);
               return;
            }

            ☃.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
         } finally {
            ☃.getLock().unlock();
         }

         final CompiledChunk var24 = ☃.getCompiledChunk();
         ArrayList var25 = Lists.newArrayList();
         if (☃xxxx == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
            for (BlockRenderLayer ☃xxxxx : BlockRenderLayer.values()) {
               if (var24.isLayerStarted(☃xxxxx)) {
                  var25.add(
                     this.chunkRenderDispatcher
                        .uploadChunk(☃xxxxx, ☃.getRegionRenderCacheBuilder().getWorldRendererByLayer(☃xxxxx), ☃.getRenderChunk(), var24, ☃.getDistanceSq())
                  );
               }
            }
         } else if (☃xxxx == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
            var25.add(
               this.chunkRenderDispatcher
                  .uploadChunk(
                     BlockRenderLayer.TRANSLUCENT,
                     ☃.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT),
                     ☃.getRenderChunk(),
                     var24,
                     ☃.getDistanceSq()
                  )
            );
         }

         final ListenableFuture<List<Object>> ☃xxxxxx = Futures.allAsList(var25);
         ☃.addFinishRunnable(new Runnable() {
            @Override
            public void run() {
               ☃.cancel(false);
            }
         });
         Futures.addCallback(☃xxxxxx, new FutureCallback<List<Object>>() {
            public void onSuccess(@Nullable List<Object> var1x) {
               ChunkRenderWorker.this.freeRenderBuilder(☃);
               ☃.getLock().lock();

               label43: {
                  try {
                     if (☃.getStatus() == ChunkCompileTaskGenerator.Status.UPLOADING) {
                        ☃.setStatus(ChunkCompileTaskGenerator.Status.DONE);
                        break label43;
                     }

                     if (!☃.isFinished()) {
                        ChunkRenderWorker.LOGGER.warn("Chunk render task was {} when I expected it to be uploading; aborting task", ☃.getStatus());
                     }
                  } finally {
                     ☃.getLock().unlock();
                  }

                  return;
               }

               ☃.getRenderChunk().setCompiledChunk(var24);
            }

            public void onFailure(Throwable var1x) {
               ChunkRenderWorker.this.freeRenderBuilder(☃);
               if (!(☃ instanceof CancellationException) && !(☃ instanceof InterruptedException)) {
                  Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(☃, "Rendering chunk"));
               }
            }
         });
      }
   }

   private boolean isChunkExisting(BlockPos var1, World var2) {
      return !☃.getChunk(☃.getX() >> 4, ☃.getZ() >> 4).isEmpty();
   }

   private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException {
      return this.regionRenderCacheBuilder != null ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
   }

   private void freeRenderBuilder(ChunkCompileTaskGenerator var1) {
      if (this.regionRenderCacheBuilder == null) {
         this.chunkRenderDispatcher.freeRenderBuilder(☃.getRegionRenderCacheBuilder());
      }
   }

   public void notifyToStop() {
      this.shouldRun = false;
   }
}
