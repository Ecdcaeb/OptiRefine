package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.primitives.Doubles;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.VertexBufferUploader;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkRenderDispatcher {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("Chunk Batcher %d").setDaemon(true).build();
   private final int countRenderBuilders;
   private final List<Thread> listWorkerThreads = Lists.newArrayList();
   private final List<ChunkRenderWorker> listThreadedWorkers = Lists.newArrayList();
   private final PriorityBlockingQueue<ChunkCompileTaskGenerator> queueChunkUpdates = Queues.newPriorityBlockingQueue();
   private final BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders;
   private final WorldVertexBufferUploader worldVertexUploader = new WorldVertexBufferUploader();
   private final VertexBufferUploader vertexUploader = new VertexBufferUploader();
   private final Queue<ChunkRenderDispatcher.PendingUpload> queueChunkUploads = Queues.newPriorityQueue();
   private final ChunkRenderWorker renderWorker;

   public ChunkRenderDispatcher() {
      int ☃ = Math.max(1, (int)(Runtime.getRuntime().maxMemory() * 0.3) / 10485760);
      int ☃x = Math.max(1, MathHelper.clamp(Runtime.getRuntime().availableProcessors(), 1, ☃ / 5));
      this.countRenderBuilders = MathHelper.clamp(☃x * 10, 1, ☃);
      if (☃x > 1) {
         for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
            ChunkRenderWorker ☃xxx = new ChunkRenderWorker(this);
            Thread ☃xxxx = THREAD_FACTORY.newThread(☃xxx);
            ☃xxxx.start();
            this.listThreadedWorkers.add(☃xxx);
            this.listWorkerThreads.add(☃xxxx);
         }
      }

      this.queueFreeRenderBuilders = Queues.newArrayBlockingQueue(this.countRenderBuilders);

      for (int ☃xx = 0; ☃xx < this.countRenderBuilders; ☃xx++) {
         this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
      }

      this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
   }

   public String getDebugInfo() {
      return this.listWorkerThreads.isEmpty()
         ? String.format("pC: %03d, single-threaded", this.queueChunkUpdates.size())
         : String.format("pC: %03d, pU: %1d, aB: %1d", this.queueChunkUpdates.size(), this.queueChunkUploads.size(), this.queueFreeRenderBuilders.size());
   }

   public boolean runChunkUploads(long var1) {
      boolean ☃ = false;

      boolean ☃x;
      do {
         ☃x = false;
         if (this.listWorkerThreads.isEmpty()) {
            ChunkCompileTaskGenerator ☃xx = this.queueChunkUpdates.poll();
            if (☃xx != null) {
               try {
                  this.renderWorker.processTask(☃xx);
                  ☃x = true;
               } catch (InterruptedException var8) {
                  LOGGER.warn("Skipped task due to interrupt");
               }
            }
         }

         synchronized (this.queueChunkUploads) {
            if (!this.queueChunkUploads.isEmpty()) {
               this.queueChunkUploads.poll().uploadTask.run();
               ☃x = true;
               ☃ = true;
            }
         }
      } while (☃ != 0L && ☃x && ☃ >= System.nanoTime());

      return ☃;
   }

   public boolean updateChunkLater(RenderChunk var1) {
      ☃.getLockCompileTask().lock();

      boolean var4;
      try {
         final ChunkCompileTaskGenerator ☃ = ☃.makeCompileTaskChunk();
         ☃.addFinishRunnable(new Runnable() {
            @Override
            public void run() {
               ChunkRenderDispatcher.this.queueChunkUpdates.remove(☃);
            }
         });
         boolean ☃x = this.queueChunkUpdates.offer(☃);
         if (!☃x) {
            ☃.finish();
         }

         var4 = ☃x;
      } finally {
         ☃.getLockCompileTask().unlock();
      }

      return var4;
   }

   public boolean updateChunkNow(RenderChunk var1) {
      ☃.getLockCompileTask().lock();

      boolean var3;
      try {
         ChunkCompileTaskGenerator ☃ = ☃.makeCompileTaskChunk();

         try {
            this.renderWorker.processTask(☃);
         } catch (InterruptedException var7) {
         }

         var3 = true;
      } finally {
         ☃.getLockCompileTask().unlock();
      }

      return var3;
   }

   public void stopChunkUpdates() {
      this.clearChunkUpdates();
      List<RegionRenderCacheBuilder> ☃ = Lists.newArrayList();

      while (☃.size() != this.countRenderBuilders) {
         this.runChunkUploads(Long.MAX_VALUE);

         try {
            ☃.add(this.allocateRenderBuilder());
         } catch (InterruptedException var3) {
         }
      }

      this.queueFreeRenderBuilders.addAll(☃);
   }

   public void freeRenderBuilder(RegionRenderCacheBuilder var1) {
      this.queueFreeRenderBuilders.add(☃);
   }

   public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException {
      return this.queueFreeRenderBuilders.take();
   }

   public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException {
      return this.queueChunkUpdates.take();
   }

   public boolean updateTransparencyLater(RenderChunk var1) {
      ☃.getLockCompileTask().lock();

      boolean var3;
      try {
         final ChunkCompileTaskGenerator ☃ = ☃.makeCompileTaskTransparency();
         if (☃ == null) {
            return true;
         }

         ☃.addFinishRunnable(new Runnable() {
            @Override
            public void run() {
               ChunkRenderDispatcher.this.queueChunkUpdates.remove(☃);
            }
         });
         var3 = this.queueChunkUpdates.offer(☃);
      } finally {
         ☃.getLockCompileTask().unlock();
      }

      return var3;
   }

   public ListenableFuture<Object> uploadChunk(
      final BlockRenderLayer var1, final BufferBuilder var2, final RenderChunk var3, final CompiledChunk var4, final double var5
   ) {
      if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
         if (OpenGlHelper.useVbo()) {
            this.uploadVertexBuffer(☃, ☃.getVertexBufferByLayer(☃.ordinal()));
         } else {
            this.uploadDisplayList(☃, ((ListedRenderChunk)☃).getDisplayList(☃, ☃), ☃);
         }

         ☃.setTranslation(0.0, 0.0, 0.0);
         return Futures.immediateFuture(null);
      } else {
         ListenableFutureTask<Object> ☃ = ListenableFutureTask.create(new Runnable() {
            @Override
            public void run() {
               ChunkRenderDispatcher.this.uploadChunk(☃, ☃, ☃, ☃, ☃);
            }
         }, null);
         synchronized (this.queueChunkUploads) {
            this.queueChunkUploads.add(new ChunkRenderDispatcher.PendingUpload(☃, ☃));
            return ☃;
         }
      }
   }

   private void uploadDisplayList(BufferBuilder var1, int var2, RenderChunk var3) {
      GlStateManager.glNewList(☃, 4864);
      GlStateManager.pushMatrix();
      ☃.multModelviewMatrix();
      this.worldVertexUploader.draw(☃);
      GlStateManager.popMatrix();
      GlStateManager.glEndList();
   }

   private void uploadVertexBuffer(BufferBuilder var1, VertexBuffer var2) {
      this.vertexUploader.setVertexBuffer(☃);
      this.vertexUploader.draw(☃);
   }

   public void clearChunkUpdates() {
      while (!this.queueChunkUpdates.isEmpty()) {
         ChunkCompileTaskGenerator ☃ = this.queueChunkUpdates.poll();
         if (☃ != null) {
            ☃.finish();
         }
      }
   }

   public boolean hasNoChunkUpdates() {
      return this.queueChunkUpdates.isEmpty() && this.queueChunkUploads.isEmpty();
   }

   public void stopWorkerThreads() {
      this.clearChunkUpdates();

      for (ChunkRenderWorker ☃ : this.listThreadedWorkers) {
         ☃.notifyToStop();
      }

      for (Thread ☃ : this.listWorkerThreads) {
         try {
            ☃.interrupt();
            ☃.join();
         } catch (InterruptedException var4) {
            LOGGER.warn("Interrupted whilst waiting for worker to die", var4);
         }
      }

      this.queueFreeRenderBuilders.clear();
   }

   public boolean hasNoFreeRenderBuilders() {
      return this.queueFreeRenderBuilders.isEmpty();
   }

   class PendingUpload implements Comparable<ChunkRenderDispatcher.PendingUpload> {
      private final ListenableFutureTask<Object> uploadTask;
      private final double distanceSq;

      public PendingUpload(ListenableFutureTask<Object> var2, double var3) {
         this.uploadTask = ☃;
         this.distanceSq = ☃;
      }

      public int compareTo(ChunkRenderDispatcher.PendingUpload var1) {
         return Doubles.compare(this.distanceSq, ☃.distanceSq);
      }
   }
}
