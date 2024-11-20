/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Queues
 *  com.google.common.util.concurrent.Futures
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.google.common.util.concurrent.ListenableFutureTask
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  java.lang.InterruptedException
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.Runtime
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.List
 *  java.util.Queue
 *  java.util.concurrent.BlockingQueue
 *  java.util.concurrent.PriorityBlockingQueue
 *  java.util.concurrent.ThreadFactory
 *  java.util.concurrent.TimeUnit
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RegionRenderCacheBuilder
 *  net.minecraft.client.renderer.VertexBufferUploader
 *  net.minecraft.client.renderer.WorldVertexBufferUploader
 *  net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator
 *  net.minecraft.client.renderer.chunk.ChunkRenderDispatcher$PendingUpload
 *  net.minecraft.client.renderer.chunk.ChunkRenderWorker
 *  net.minecraft.client.renderer.chunk.CompiledChunk
 *  net.minecraft.client.renderer.chunk.ListedRenderChunk
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.math.MathHelper
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.Collection;
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
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.ChunkRenderWorker;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Exception performing whole class analysis ignored.
 */
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
    private final Queue<PendingUpload> queueChunkUploads = Queues.newPriorityQueue();
    private final ChunkRenderWorker renderWorker;
    private List<RegionRenderCacheBuilder> listPausedBuilders = new ArrayList();

    public ChunkRenderDispatcher() {
        this(-1);
    }

    public ChunkRenderDispatcher(int countRenderBuilders) {
        int i = Math.max((int)1, (int)((int)((double)Runtime.getRuntime().maxMemory() * 0.3) / 0xA00000));
        int j = Math.max((int)1, (int)MathHelper.clamp((int)(Runtime.getRuntime().availableProcessors() - 2), (int)1, (int)(i / 5)));
        this.countRenderBuilders = countRenderBuilders < 0 ? MathHelper.clamp((int)(j * 8), (int)1, (int)i) : countRenderBuilders;
        if (j > 1) {
            for (int k = 0; k < j; ++k) {
                ChunkRenderWorker chunkrenderworker = new ChunkRenderWorker(this);
                Thread thread = THREAD_FACTORY.newThread((Runnable)chunkrenderworker);
                thread.start();
                this.listThreadedWorkers.add((Object)chunkrenderworker);
                this.listWorkerThreads.add((Object)thread);
            }
        }
        this.queueFreeRenderBuilders = Queues.newArrayBlockingQueue((int)this.countRenderBuilders);
        for (int l = 0; l < this.countRenderBuilders; ++l) {
            this.queueFreeRenderBuilders.add((Object)new RegionRenderCacheBuilder());
        }
        this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
    }

    public String getDebugInfo() {
        return this.listWorkerThreads.isEmpty() ? String.format((String)"pC: %03d, single-threaded", (Object[])new Object[]{this.queueChunkUpdates.size()}) : String.format((String)"pC: %03d, pU: %1d, aB: %1d", (Object[])new Object[]{this.queueChunkUpdates.size(), this.queueChunkUploads.size(), this.queueFreeRenderBuilders.size()});
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean runChunkUploads(long finishTimeNano) {
        boolean flag1;
        boolean flag = false;
        do {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator;
            flag1 = false;
            if (this.listWorkerThreads.isEmpty() && (chunkcompiletaskgenerator = (ChunkCompileTaskGenerator)this.queueChunkUpdates.poll()) != null) {
                try {
                    this.renderWorker.processTask(chunkcompiletaskgenerator);
                    flag1 = true;
                }
                catch (InterruptedException var8) {
                    LOGGER.warn("Skipped task due to interrupt");
                }
            }
            PendingUpload pendingUpload = null;
            Queue<PendingUpload> queue = this.queueChunkUploads;
            synchronized (queue) {
                pendingUpload = (PendingUpload)this.queueChunkUploads.poll();
            }
            if (pendingUpload == null) continue;
            PendingUpload.access$000((PendingUpload)pendingUpload).run();
            flag1 = true;
            flag = true;
        } while (finishTimeNano != 0L && flag1 && finishTimeNano >= System.nanoTime());
        return flag;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateChunkLater(RenderChunk chunkRenderer) {
        boolean flag1;
        chunkRenderer.getLockCompileTask().lock();
        try {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
            chunkcompiletaskgenerator.addFinishRunnable((Runnable)new /* Unavailable Anonymous Inner Class!! */);
            boolean flag = this.queueChunkUpdates.offer((Object)chunkcompiletaskgenerator);
            if (!flag) {
                chunkcompiletaskgenerator.finish();
            }
            flag1 = flag;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        return flag1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateChunkNow(RenderChunk chunkRenderer) {
        boolean flag;
        chunkRenderer.getLockCompileTask().lock();
        try {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskChunk();
            try {
                this.renderWorker.processTask(chunkcompiletaskgenerator);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            flag = true;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        return flag;
    }

    public void stopChunkUpdates() {
        this.clearChunkUpdates();
        ArrayList list = Lists.newArrayList();
        while (list.size() != this.countRenderBuilders) {
            this.runChunkUploads(Long.MAX_VALUE);
            try {
                list.add((Object)this.allocateRenderBuilder());
            }
            catch (InterruptedException interruptedException) {}
        }
        this.queueFreeRenderBuilders.addAll((Collection)list);
    }

    public void freeRenderBuilder(RegionRenderCacheBuilder p_178512_1_) {
        this.queueFreeRenderBuilders.add((Object)p_178512_1_);
    }

    public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException {
        return (RegionRenderCacheBuilder)this.queueFreeRenderBuilders.take();
    }

    public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException {
        return (ChunkCompileTaskGenerator)this.queueChunkUpdates.take();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean updateTransparencyLater(RenderChunk chunkRenderer) {
        boolean flag;
        chunkRenderer.getLockCompileTask().lock();
        try {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = chunkRenderer.makeCompileTaskTransparency();
            if (chunkcompiletaskgenerator == null) {
                boolean flag2;
                boolean bl = flag2 = true;
                return bl;
            }
            chunkcompiletaskgenerator.addFinishRunnable((Runnable)new /* Unavailable Anonymous Inner Class!! */);
            flag = this.queueChunkUpdates.offer((Object)chunkcompiletaskgenerator);
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        return flag;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ListenableFuture<Object> uploadChunk(BlockRenderLayer p_188245_1_, BufferBuilder p_188245_2_, RenderChunk p_188245_3_, CompiledChunk p_188245_4_, double p_188245_5_) {
        if (Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
            if (OpenGlHelper.useVbo()) {
                this.uploadVertexBuffer(p_188245_2_, p_188245_3_.getVertexBufferByLayer(p_188245_1_.ordinal()));
            } else {
                this.uploadDisplayList(p_188245_2_, ((ListedRenderChunk)p_188245_3_).getDisplayList(p_188245_1_, p_188245_4_), p_188245_3_);
            }
            p_188245_2_.setTranslation(0.0, 0.0, 0.0);
            return Futures.immediateFuture(null);
        }
        ListenableFutureTask listenablefuturetask = ListenableFutureTask.create((Runnable)new /* Unavailable Anonymous Inner Class!! */, null);
        Queue<PendingUpload> queue = this.queueChunkUploads;
        synchronized (queue) {
            this.queueChunkUploads.add((Object)new PendingUpload(this, listenablefuturetask, p_188245_5_));
            return listenablefuturetask;
        }
    }

    private void uploadDisplayList(BufferBuilder vertexBufferIn, int list, RenderChunk chunkRenderer) {
        GlStateManager.glNewList((int)list, (int)4864);
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
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = (ChunkCompileTaskGenerator)this.queueChunkUpdates.poll();
            if (chunkcompiletaskgenerator == null) continue;
            chunkcompiletaskgenerator.finish();
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
            }
            catch (InterruptedException interruptedexception) {
                LOGGER.warn("Interrupted whilst waiting for worker to die", (Throwable)interruptedexception);
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
                RegionRenderCacheBuilder builder = (RegionRenderCacheBuilder)this.queueFreeRenderBuilders.poll(100L, TimeUnit.MILLISECONDS);
                if (builder == null) continue;
                this.listPausedBuilders.add((Object)builder);
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    public void resumeChunkUpdates() {
        this.queueFreeRenderBuilders.addAll(this.listPausedBuilders);
        this.listPausedBuilders.clear();
    }

    static /* synthetic */ PriorityBlockingQueue access$100(ChunkRenderDispatcher x0) {
        return x0.queueChunkUpdates;
    }
}
