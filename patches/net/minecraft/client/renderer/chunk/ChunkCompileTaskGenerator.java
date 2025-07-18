package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;

public class ChunkCompileTaskGenerator implements Comparable<ChunkCompileTaskGenerator> {
   private final RenderChunk renderChunk;
   private final ReentrantLock lock = new ReentrantLock();
   private final List<Runnable> listFinishRunnables = Lists.newArrayList();
   private final ChunkCompileTaskGenerator.Type type;
   private final double distanceSq;
   private RegionRenderCacheBuilder regionRenderCacheBuilder;
   private CompiledChunk compiledChunk;
   private ChunkCompileTaskGenerator.Status status = ChunkCompileTaskGenerator.Status.PENDING;
   private boolean finished;

   public ChunkCompileTaskGenerator(RenderChunk var1, ChunkCompileTaskGenerator.Type var2, double var3) {
      this.renderChunk = ☃;
      this.type = ☃;
      this.distanceSq = ☃;
   }

   public ChunkCompileTaskGenerator.Status getStatus() {
      return this.status;
   }

   public RenderChunk getRenderChunk() {
      return this.renderChunk;
   }

   public CompiledChunk getCompiledChunk() {
      return this.compiledChunk;
   }

   public void setCompiledChunk(CompiledChunk var1) {
      this.compiledChunk = ☃;
   }

   public RegionRenderCacheBuilder getRegionRenderCacheBuilder() {
      return this.regionRenderCacheBuilder;
   }

   public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder var1) {
      this.regionRenderCacheBuilder = ☃;
   }

   public void setStatus(ChunkCompileTaskGenerator.Status var1) {
      this.lock.lock();

      try {
         this.status = ☃;
      } finally {
         this.lock.unlock();
      }
   }

   public void finish() {
      this.lock.lock();

      try {
         if (this.type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK && this.status != ChunkCompileTaskGenerator.Status.DONE) {
            this.renderChunk.setNeedsUpdate(false);
         }

         this.finished = true;
         this.status = ChunkCompileTaskGenerator.Status.DONE;

         for (Runnable ☃ : this.listFinishRunnables) {
            ☃.run();
         }
      } finally {
         this.lock.unlock();
      }
   }

   public void addFinishRunnable(Runnable var1) {
      this.lock.lock();

      try {
         this.listFinishRunnables.add(☃);
         if (this.finished) {
            ☃.run();
         }
      } finally {
         this.lock.unlock();
      }
   }

   public ReentrantLock getLock() {
      return this.lock;
   }

   public ChunkCompileTaskGenerator.Type getType() {
      return this.type;
   }

   public boolean isFinished() {
      return this.finished;
   }

   public int compareTo(ChunkCompileTaskGenerator var1) {
      return Doubles.compare(this.distanceSq, ☃.distanceSq);
   }

   public double getDistanceSq() {
      return this.distanceSq;
   }

   public static enum Status {
      PENDING,
      COMPILING,
      UPLOADING,
      DONE;
   }

   public static enum Type {
      REBUILD_CHUNK,
      RESORT_TRANSPARENCY;
   }
}
