package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.primitives.Doubles;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
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
   private List<RegionRenderCacheBuilder> listPausedBuilders = new ArrayList<>();

   public ChunkRenderDispatcher() {
      this(-1);
   }

   public ChunkRenderDispatcher(int countRenderBuilders) {
      int i = Math.max(1, (int)(Runtime.getRuntime().maxMemory() * 0.3) / 10485760);
      int j = Math.max(1, MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 2, 1, i / 5));
      if (countRenderBuilders < 0) {
         this.countRenderBuilders = MathHelper.clamp(j * 8, 1, i);
      } else {
         this.countRenderBuilders = countRenderBuilders;
      }

      if (j > 1) {
         for (int k = 0; k < j; k++) {
            ChunkRenderWorker chunkrenderworker = new ChunkRenderWorker(this);
            Thread thread = THREAD_FACTORY.newThread(chunkrenderworker);
            thread.start();
            this.listThreadedWorkers.add(chunkrenderworker);
            this.listWorkerThreads.add(thread);
         }
      }

      this.queueFreeRenderBuilders = Queues.newArrayBlockingQueue(this.countRenderBuilders);

      for (int l = 0; l < this.countRenderBuilders; l++) {
         this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
      }

      this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
   }

   public String getDebugInfo() {
      return this.listWorkerThreads.isEmpty()
         ? String.format("pC: %03d, single-threaded", this.queueChunkUpdates.size())
         : String.format("pC: %03d, pU: %1d, aB: %1d", this.queueChunkUpdates.size(), this.queueChunkUploads.size(), this.queueFreeRenderBuilders.size());
   }

   public boolean runChunkUploads(long finishTimeNano) {
      boolean flag = false;

      boolean flag1;
      do {
         flag1 = false;
         if (this.listWorkerThreads.isEmpty()) {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = this.queueChunkUpdates.poll();
            if (chunkcompiletaskgenerator != null) {
               try {
                  this.renderWorker.processTask(chunkcompiletaskgenerator);
                  flag1 = true;
               } catch (InterruptedException var9) {
                  LOGGER.warn("Skipped task due to interrupt");
               }
            }
         }

         ChunkRenderDispatcher.PendingUpload pendingUpload = null;
         synchronized (this.queueChunkUploads) {
            pendingUpload = this.queueChunkUploads.poll();
         }

         if (pendingUpload != null) {
            pendingUpload.uploadTask.run();
            flag1 = true;
            flag = true;
         }
      } while (finishTimeNano != 0L && flag1 && finishTimeNano >= System.nanoTime());

      return flag;
   }

   public boolean updateChunkLater(RenderChunk chunkRenderer) {
      chunkRenderer.getLockCompileTask().lock();

      boolean flag1;
      try {
         final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
         chunkcompiletaskgenerator.addFinishRunnable(new Runnable() {
            @Override
            public void run() {
               ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkcompiletaskgenerator);
            }
         });
         boolean flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
         if (!flag) {
            chunkcompiletaskgenerator.finish();
         }

         flag1 = flag;
      } finally {
         chunkRenderer.getLockCompileTask().unlock();
      }

      return flag1;
   }

   public boolean updateChunkNow(RenderChunk chunkRenderer) {
      chunkRenderer.getLockCompileTask().lock();

      boolean flag;
      try {
         ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();

         try {
            this.renderWorker.processTask(chunkcompiletaskgenerator);
         } catch (InterruptedException var8) {
         }

         flag = true;
      } finally {
         chunkRenderer.getLockCompileTask().unlock();
      }

      return flag;
   }

   public void stopChunkUpdates() {
      this.clearChunkUpdates();
      List<RegionRenderCacheBuilder> list = Lists.newArrayList();

      while (list.size() != this.countRenderBuilders) {
         this.runChunkUploads(Long.MAX_VALUE);

         try {
            list.add(this.allocateRenderBuilder());
         } catch (InterruptedException var3) {
         }
      }

      this.queueFreeRenderBuilders.addAll(list);
   }

   public void freeRenderBuilder(RegionRenderCacheBuilder p_178512_1_) {
      this.queueFreeRenderBuilders.add(p_178512_1_);
   }

   public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException {
      return this.queueFreeRenderBuilders.take();
   }

   public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException {
      return this.queueChunkUpdates.take();
   }

   public boolean updateTransparencyLater(RenderChunk chunkRenderer) {
      chunkRenderer.getLockCompileTask().lock();

      boolean var4;
      try {
         final ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskTransparency();
         if (chunkcompiletaskgenerator != null) {
            chunkcompiletaskgenerator.addFinishRunnable(new Runnable() {
               @Override
               public void run() {
                  ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkcompiletaskgenerator);
               }
            });
            return this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
         }

         boolean flag = true;
         var4 = flag;
      } finally {
         chunkRenderer.getLockCompileTask().unlock();
      }

      return var4;
   }

   public ListenableFuture<Object> uploadChunk(
      final BlockRenderLayer p_188245_1_,
      final BufferBuilder p_188245_2_,
      final RenderChunk p_188245_3_,
      final CompiledChunk p_188245_4_,
      final double p_188245_5_
   ) {
      if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
         if (OpenGlHelper.useVbo()) {
            this.uploadVertexBuffer(p_188245_2_, p_188245_3_.getVertexBufferByLayer(p_188245_1_.ordinal()));
         } else {
            this.uploadDisplayList(p_188245_2_, ((ListedRenderChunk)p_188245_3_).getDisplayList(p_188245_1_, p_188245_4_), p_188245_3_);
         }

         p_188245_2_.setTranslation(0.0, 0.0, 0.0);
         return Futures.immediateFuture(null);
      } else {
         ListenableFutureTask<Object> listenablefuturetask = ListenableFutureTask.create(new Runnable() {
            @Override
            public void run() {
               ChunkRenderDispatcher.this.uploadChunk(p_188245_1_, p_188245_2_, p_188245_3_, p_188245_4_, p_188245_5_);
            }
         }, null);
         synchronized (this.queueChunkUploads) {
            this.queueChunkUploads.add(new ChunkRenderDispatcher.PendingUpload(listenablefuturetask, p_188245_5_));
            return listenablefuturetask;
         }
      }
   }

   private void uploadDisplayList(BufferBuilder vertexBufferIn, int list, RenderChunk chunkRenderer) {
      GlStateManager.glNewList(list, 4864);
      GlStateManager.pushMatrix();
      chunkRenderer.multModelviewMatrix();
      this.worldVertexUploader.draw(vertexBufferIn);
      GlStateManager.popMatrix();
      GlStateManager.glEndList();
   }

   private void uploadVertexBuffer(BufferBuilder p_178506_1_, VertexBuffer vertexBufferIn) {
      this.vertexUploader.setVertexBuffer(vertexBufferIn);
      this.vertexUploader.draw(p_178506_1_);
   }

   public void clearChunkUpdates() {
      while (!this.queueChunkUpdates.isEmpty()) {
         ChunkCompileTaskGenerator chunkcompiletaskgenerator = this.queueChunkUpdates.poll();
         if (chunkcompiletaskgenerator != null) {
            chunkcompiletaskgenerator.finish();
         }
      }
   }

   public boolean hasNoChunkUpdates() {
      return this.queueChunkUpdates.isEmpty() && this.queueChunkUploads.isEmpty();
   }

   public void stopWorkerThreads() {
      this.clearChunkUpdates();

      for (ChunkRenderWorker chunkrenderworker : this.listThreadedWorkers) {
         chunkrenderworker.notifyToStop();
      }

      for (Thread thread : this.listWorkerThreads) {
         try {
            thread.interrupt();
            thread.join();
         } catch (InterruptedException var4) {
            LOGGER.warn("Interrupted whilst waiting for worker to die", var4);
         }
      }

      this.queueFreeRenderBuilders.clear();
   }

   public boolean hasNoFreeRenderBuilders() {
      return this.queueFreeRenderBuilders.isEmpty();
   }

   public void pauseChunkUpdates() {
      while (this.listPausedBuilders.size() != this.countRenderBuilders) {
         try {
            this.runChunkUploads(Long.MAX_VALUE);
            RegionRenderCacheBuilder builder = this.queueFreeRenderBuilders.poll(100L, TimeUnit.MILLISECONDS);
            if (builder != null) {
               this.listPausedBuilders.add(builder);
            }
         } catch (InterruptedException var2) {
         }
      }
   }

   public void resumeChunkUpdates() {
      this.queueFreeRenderBuilders.addAll(this.listPausedBuilders);
      this.listPausedBuilders.clear();
   }

   class PendingUpload implements Comparable<ChunkRenderDispatcher.PendingUpload> {
      private final ListenableFutureTask<Object> uploadTask;
      private final double distanceSq;

      public PendingUpload(ListenableFutureTask<Object> uploadTaskIn, double distanceSqIn) {
         this.uploadTask = uploadTaskIn;
         this.distanceSq = distanceSqIn;
      }

      public int compareTo(ChunkRenderDispatcher.PendingUpload p_compareTo_1_) {
         return Doubles.compare(this.distanceSq, p_compareTo_1_.distanceSq);
      }
   }
}
