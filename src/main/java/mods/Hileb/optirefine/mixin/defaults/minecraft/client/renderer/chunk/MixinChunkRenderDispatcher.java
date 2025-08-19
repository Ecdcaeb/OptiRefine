package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Mixin(ChunkRenderDispatcher.class)
public abstract class MixinChunkRenderDispatcher {

    @Unique
    private List<RegionRenderCacheBuilder> listPausedBuilders = new ArrayList<>();

    @Shadow @Final
    private int countRenderBuilders;

    @Shadow
    public abstract boolean runChunkUploads(long finishTimeNano);

    @Shadow @Final
    private BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void pauseChunkUpdates() {
        while (this.listPausedBuilders.size() != this.countRenderBuilders) {
            try {
                this.runChunkUploads(Long.MAX_VALUE);
                RegionRenderCacheBuilder builder = this.queueFreeRenderBuilders.poll(100L, TimeUnit.MILLISECONDS);
                if (builder != null) {
                    this.listPausedBuilders.add(builder);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void resumeChunkUpdates() {
        this.queueFreeRenderBuilders.addAll(this.listPausedBuilders);
        this.listPausedBuilders.clear();
    }

}
/*
+++ net/minecraft/client/renderer/chunk/ChunkRenderDispatcher.java	Tue Aug 19 14:59:58 2025
@@ -10,12 +10,13 @@
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Queue;
 import java.util.concurrent.BlockingQueue;
 import java.util.concurrent.PriorityBlockingQueue;
 import java.util.concurrent.ThreadFactory;
+import java.util.concurrent.TimeUnit;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.RegionRenderCacheBuilder;
 import net.minecraft.client.renderer.VertexBufferUploader;
@@ -35,30 +36,40 @@
    private final PriorityBlockingQueue<ChunkCompileTaskGenerator> queueChunkUpdates = Queues.newPriorityBlockingQueue();
    private final BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders;
    private final WorldVertexBufferUploader worldVertexUploader = new WorldVertexBufferUploader();
    private final VertexBufferUploader vertexUploader = new VertexBufferUploader();
    private final Queue<ChunkRenderDispatcher.PendingUpload> queueChunkUploads = Queues.newPriorityQueue();
    private final ChunkRenderWorker renderWorker;
+   private List<RegionRenderCacheBuilder> listPausedBuilders = new ArrayList<>();

    public ChunkRenderDispatcher() {
-      int var1 = Math.max(1, (int)(Runtime.getRuntime().maxMemory() * 0.3) / 10485760);
-      int var2 = Math.max(1, MathHelper.clamp(Runtime.getRuntime().availableProcessors(), 1, var1 / 5));
-      this.countRenderBuilders = MathHelper.clamp(var2 * 10, 1, var1);
-      if (var2 > 1) {
-         for (int var3 = 0; var3 < var2; var3++) {
-            ChunkRenderWorker var4 = new ChunkRenderWorker(this);
-            Thread var5 = THREAD_FACTORY.newThread(var4);
-            var5.start();
-            this.listThreadedWorkers.add(var4);
-            this.listWorkerThreads.add(var5);
+      this(-1);
+   }
+
+   public ChunkRenderDispatcher(int var1) {
+      int var2 = Math.max(1, (int)(Runtime.getRuntime().maxMemory() * 0.3) / 10485760);
+      int var3 = Math.max(1, MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 2, 1, var2 / 5));
+      if (var1 < 0) {
+         this.countRenderBuilders = MathHelper.clamp(var3 * 8, 1, var2);
+      } else {
+         this.countRenderBuilders = var1;
+      }
+
+      if (var3 > 1) {
+         for (int var4 = 0; var4 < var3; var4++) {
+            ChunkRenderWorker var5 = new ChunkRenderWorker(this);
+            Thread var6 = THREAD_FACTORY.newThread(var5);
+            var6.start();
+            this.listThreadedWorkers.add(var5);
+            this.listWorkerThreads.add(var6);
          }
       }

       this.queueFreeRenderBuilders = Queues.newArrayBlockingQueue(this.countRenderBuilders);

-      for (int var6 = 0; var6 < this.countRenderBuilders; var6++) {
+      for (int var7 = 0; var7 < this.countRenderBuilders; var7++) {
          this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
       }

       this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
    }

@@ -77,72 +88,75 @@
          if (this.listWorkerThreads.isEmpty()) {
             ChunkCompileTaskGenerator var5 = this.queueChunkUpdates.poll();
             if (var5 != null) {
                try {
                   this.renderWorker.processTask(var5);
                   var4 = true;
-               } catch (InterruptedException var8) {
+               } catch (InterruptedException var9) {
                   LOGGER.warn("Skipped task due to interrupt");
                }
             }
          }

+         Object var10 = null;
          synchronized (this.queueChunkUploads) {
-            if (!this.queueChunkUploads.isEmpty()) {
-               this.queueChunkUploads.poll().uploadTask.run();
-               var4 = true;
-               var3 = true;
-            }
+            var10 = this.queueChunkUploads.poll();
+         }
+
+         if (var10 != null) {
+            ((ChunkRenderDispatcher.PendingUpload)var10).uploadTask.run();
+            var4 = true;
+            var3 = true;
          }
       } while (var1 != 0L && var4 && var1 >= System.nanoTime());

       return var3;
    }

    public boolean updateChunkLater(RenderChunk var1) {
       var1.getLockCompileTask().lock();

-      boolean var4;
+      boolean var2;
       try {
-         final ChunkCompileTaskGenerator var2 = var1.makeCompileTaskChunk();
-         var2.addFinishRunnable(new Runnable() {
+         final ChunkCompileTaskGenerator var3 = var1.makeCompileTaskChunk();
+         var3.addFinishRunnable(new Runnable() {
             public void run() {
-               ChunkRenderDispatcher.this.queueChunkUpdates.remove(var2);
+               ChunkRenderDispatcher.this.queueChunkUpdates.remove(var3);
             }
          });
-         boolean var3 = this.queueChunkUpdates.offer(var2);
-         if (!var3) {
-            var2.finish();
+         boolean var4 = this.queueChunkUpdates.offer(var3);
+         if (!var4) {
+            var3.finish();
          }

-         var4 = var3;
+         var2 = var4;
       } finally {
          var1.getLockCompileTask().unlock();
       }

-      return var4;
+      return var2;
    }

    public boolean updateChunkNow(RenderChunk var1) {
       var1.getLockCompileTask().lock();

-      boolean var3;
+      boolean var2;
       try {
-         ChunkCompileTaskGenerator var2 = var1.makeCompileTaskChunk();
+         ChunkCompileTaskGenerator var3 = var1.makeCompileTaskChunk();

          try {
-            this.renderWorker.processTask(var2);
-         } catch (InterruptedException var7) {
+            this.renderWorker.processTask(var3);
+         } catch (InterruptedException var8) {
          }

-         var3 = true;
+         var2 = true;
       } finally {
          var1.getLockCompileTask().unlock();
       }

-      return var3;
+      return var2;
    }

    public void stopChunkUpdates() {
       this.clearChunkUpdates();
       ArrayList var1 = Lists.newArrayList();

@@ -170,30 +184,31 @@
       return this.queueChunkUpdates.take();
    }

    public boolean updateTransparencyLater(RenderChunk var1) {
       var1.getLockCompileTask().lock();

-      boolean var3;
+      boolean var4;
       try {
-         final ChunkCompileTaskGenerator var2 = var1.makeCompileTaskTransparency();
-         if (var2 == null) {
-            return true;
+         final ChunkCompileTaskGenerator var3 = var1.makeCompileTaskTransparency();
+         if (var3 != null) {
+            var3.addFinishRunnable(new Runnable() {
+               public void run() {
+                  ChunkRenderDispatcher.this.queueChunkUpdates.remove(var3);
+               }
+            });
+            return this.queueChunkUpdates.offer(var3);
          }

-         var2.addFinishRunnable(new Runnable() {
-            public void run() {
-               ChunkRenderDispatcher.this.queueChunkUpdates.remove(var2);
-            }
-         });
-         var3 = this.queueChunkUpdates.offer(var2);
+         boolean var2 = true;
+         var4 = var2;
       } finally {
          var1.getLockCompileTask().unlock();
       }

-      return var3;
+      return var4;
    }

    public ListenableFuture<Object> uploadChunk(
       final BlockRenderLayer var1, final BufferBuilder var2, final RenderChunk var3, final CompiledChunk var4, final double var5
    ) {
       if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
@@ -263,12 +278,30 @@

       this.queueFreeRenderBuilders.clear();
    }

    public boolean hasNoFreeRenderBuilders() {
       return this.queueFreeRenderBuilders.isEmpty();
+   }
+
+   public void pauseChunkUpdates() {
+      while (this.listPausedBuilders.size() != this.countRenderBuilders) {
+         try {
+            this.runChunkUploads(Long.MAX_VALUE);
+            RegionRenderCacheBuilder var1 = this.queueFreeRenderBuilders.poll(100L, TimeUnit.MILLISECONDS);
+            if (var1 != null) {
+               this.listPausedBuilders.add(var1);
+            }
+         } catch (InterruptedException var2) {
+         }
+      }
+   }
+
+   public void resumeChunkUpdates() {
+      this.queueFreeRenderBuilders.addAll(this.listPausedBuilders);
+      this.listPausedBuilders.clear();
    }

    class PendingUpload implements Comparable<ChunkRenderDispatcher.PendingUpload> {
       private final ListenableFutureTask<Object> uploadTask;
       private final double distanceSq;
 */
