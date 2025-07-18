package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Sets;
import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RenderChunk {
   private World world;
   private final RenderGlobal renderGlobal;
   public static int renderChunksUpdated;
   public CompiledChunk compiledChunk = CompiledChunk.DUMMY;
   private final ReentrantLock lockCompileTask = new ReentrantLock();
   private final ReentrantLock lockCompiledChunk = new ReentrantLock();
   private ChunkCompileTaskGenerator compileTask;
   private final Set<TileEntity> setTileEntities = Sets.newHashSet();
   private final int index;
   private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
   private final VertexBuffer[] vertexBuffers = new VertexBuffer[BlockRenderLayer.values().length];
   public AxisAlignedBB boundingBox;
   private int frameIndex = -1;
   private boolean needsUpdate = true;
   private final BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos(-1, -1, -1);
   private final BlockPos.MutableBlockPos[] mapEnumFacing = new BlockPos.MutableBlockPos[6];
   private boolean needsImmediateUpdate;
   private ChunkCache worldView;

   public RenderChunk(World var1, RenderGlobal var2, int var3) {
      for (int ☃ = 0; ☃ < this.mapEnumFacing.length; ☃++) {
         this.mapEnumFacing[☃] = new BlockPos.MutableBlockPos();
      }

      this.world = ☃;
      this.renderGlobal = ☃;
      this.index = ☃;
      if (OpenGlHelper.useVbo()) {
         for (int ☃ = 0; ☃ < BlockRenderLayer.values().length; ☃++) {
            this.vertexBuffers[☃] = new VertexBuffer(DefaultVertexFormats.BLOCK);
         }
      }
   }

   public boolean setFrameIndex(int var1) {
      if (this.frameIndex == ☃) {
         return false;
      } else {
         this.frameIndex = ☃;
         return true;
      }
   }

   public VertexBuffer getVertexBufferByLayer(int var1) {
      return this.vertexBuffers[☃];
   }

   public void setPosition(int var1, int var2, int var3) {
      if (☃ != this.position.getX() || ☃ != this.position.getY() || ☃ != this.position.getZ()) {
         this.stopCompileTask();
         this.position.setPos(☃, ☃, ☃);
         this.boundingBox = new AxisAlignedBB(☃, ☃, ☃, ☃ + 16, ☃ + 16, ☃ + 16);

         for (EnumFacing ☃ : EnumFacing.values()) {
            this.mapEnumFacing[☃.ordinal()].setPos(this.position).move(☃, 16);
         }

         this.initModelviewMatrix();
      }
   }

   public void resortTransparency(float var1, float var2, float var3, ChunkCompileTaskGenerator var4) {
      CompiledChunk ☃ = ☃.getCompiledChunk();
      if (☃.getState() != null && !☃.isLayerEmpty(BlockRenderLayer.TRANSLUCENT)) {
         this.preRenderBlocks(☃.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT), this.position);
         ☃.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT).setVertexState(☃.getState());
         this.postRenderBlocks(BlockRenderLayer.TRANSLUCENT, ☃, ☃, ☃, ☃.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT), ☃);
      }
   }

   public void rebuildChunk(float var1, float var2, float var3, ChunkCompileTaskGenerator var4) {
      CompiledChunk ☃ = new CompiledChunk();
      int ☃x = 1;
      BlockPos ☃xx = this.position;
      BlockPos ☃xxx = ☃xx.add(15, 15, 15);
      ☃.getLock().lock();

      try {
         if (☃.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
            return;
         }

         ☃.setCompiledChunk(☃);
      } finally {
         ☃.getLock().unlock();
      }

      VisGraph var9 = new VisGraph();
      HashSet var10 = Sets.newHashSet();
      if (!this.worldView.isEmpty()) {
         renderChunksUpdated++;
         boolean[] ☃xxxx = new boolean[BlockRenderLayer.values().length];
         BlockRendererDispatcher ☃xxxxx = Minecraft.getMinecraft().getBlockRendererDispatcher();

         for (BlockPos.MutableBlockPos ☃xxxxxx : BlockPos.getAllInBoxMutable(☃xx, ☃xxx)) {
            IBlockState ☃xxxxxxx = this.worldView.getBlockState(☃xxxxxx);
            Block ☃xxxxxxxx = ☃xxxxxxx.getBlock();
            if (☃xxxxxxx.isOpaqueCube()) {
               var9.setOpaqueCube(☃xxxxxx);
            }

            if (☃xxxxxxxx.hasTileEntity()) {
               TileEntity ☃xxxxxxxxx = this.worldView.getTileEntity(☃xxxxxx, Chunk.EnumCreateEntityType.CHECK);
               if (☃xxxxxxxxx != null) {
                  TileEntitySpecialRenderer<TileEntity> ☃xxxxxxxxxx = TileEntityRendererDispatcher.instance.getRenderer(☃xxxxxxxxx);
                  if (☃xxxxxxxxxx != null) {
                     ☃.addTileEntity(☃xxxxxxxxx);
                     if (☃xxxxxxxxxx.isGlobalRenderer(☃xxxxxxxxx)) {
                        var10.add(☃xxxxxxxxx);
                     }
                  }
               }
            }

            BlockRenderLayer ☃xxxxxxxxx = ☃xxxxxxxx.getRenderLayer();
            int ☃xxxxxxxxxx = ☃xxxxxxxxx.ordinal();
            if (☃xxxxxxxx.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE) {
               BufferBuilder ☃xxxxxxxxxxx = ☃.getRegionRenderCacheBuilder().getWorldRendererByLayerId(☃xxxxxxxxxx);
               if (!☃.isLayerStarted(☃xxxxxxxxx)) {
                  ☃.setLayerStarted(☃xxxxxxxxx);
                  this.preRenderBlocks(☃xxxxxxxxxxx, ☃xx);
               }

               ☃xxxx[☃xxxxxxxxxx] |= ☃xxxxx.renderBlock(☃xxxxxxx, ☃xxxxxx, this.worldView, ☃xxxxxxxxxxx);
            }
         }

         for (BlockRenderLayer ☃xxxxxx : BlockRenderLayer.values()) {
            if (☃xxxx[☃xxxxxx.ordinal()]) {
               ☃.setLayerUsed(☃xxxxxx);
            }

            if (☃.isLayerStarted(☃xxxxxx)) {
               this.postRenderBlocks(☃xxxxxx, ☃, ☃, ☃, ☃.getRegionRenderCacheBuilder().getWorldRendererByLayer(☃xxxxxx), ☃);
            }
         }
      }

      ☃.setVisibility(var9.computeVisibility());
      this.lockCompileTask.lock();

      try {
         Set<TileEntity> ☃xxxx = Sets.newHashSet(var10);
         Set<TileEntity> ☃xxxxx = Sets.newHashSet(this.setTileEntities);
         ☃xxxx.removeAll(this.setTileEntities);
         ☃xxxxx.removeAll(var10);
         this.setTileEntities.clear();
         this.setTileEntities.addAll(var10);
         this.renderGlobal.updateTileEntities(☃xxxxx, ☃xxxx);
      } finally {
         this.lockCompileTask.unlock();
      }
   }

   protected void finishCompileTask() {
      this.lockCompileTask.lock();

      try {
         if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
            this.compileTask.finish();
            this.compileTask = null;
         }
      } finally {
         this.lockCompileTask.unlock();
      }
   }

   public ReentrantLock getLockCompileTask() {
      return this.lockCompileTask;
   }

   public ChunkCompileTaskGenerator makeCompileTaskChunk() {
      this.lockCompileTask.lock();

      ChunkCompileTaskGenerator var1;
      try {
         this.finishCompileTask();
         this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK, this.getDistanceSq());
         this.rebuildWorldView();
         var1 = this.compileTask;
      } finally {
         this.lockCompileTask.unlock();
      }

      return var1;
   }

   private void rebuildWorldView() {
      int ☃ = 1;
      this.worldView = new ChunkCache(this.world, this.position.add(-1, -1, -1), this.position.add(16, 16, 16), 1);
   }

   @Nullable
   public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
      this.lockCompileTask.lock();

      Object var1;
      try {
         if (this.compileTask == null || this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
               this.compileTask.finish();
               this.compileTask = null;
            }

            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY, this.getDistanceSq());
            this.compileTask.setCompiledChunk(this.compiledChunk);
            return this.compileTask;
         }

         var1 = null;
      } finally {
         this.lockCompileTask.unlock();
      }

      return (ChunkCompileTaskGenerator)var1;
   }

   protected double getDistanceSq() {
      EntityPlayerSP ☃ = Minecraft.getMinecraft().player;
      double ☃x = this.boundingBox.minX + 8.0 - ☃.posX;
      double ☃xx = this.boundingBox.minY + 8.0 - ☃.posY;
      double ☃xxx = this.boundingBox.minZ + 8.0 - ☃.posZ;
      return ☃x * ☃x + ☃xx * ☃xx + ☃xxx * ☃xxx;
   }

   private void preRenderBlocks(BufferBuilder var1, BlockPos var2) {
      ☃.begin(7, DefaultVertexFormats.BLOCK);
      ☃.setTranslation(-☃.getX(), -☃.getY(), -☃.getZ());
   }

   private void postRenderBlocks(BlockRenderLayer var1, float var2, float var3, float var4, BufferBuilder var5, CompiledChunk var6) {
      if (☃ == BlockRenderLayer.TRANSLUCENT && !☃.isLayerEmpty(☃)) {
         ☃.sortVertexData(☃, ☃, ☃);
         ☃.setState(☃.getVertexState());
      }

      ☃.finishDrawing();
   }

   private void initModelviewMatrix() {
      GlStateManager.pushMatrix();
      GlStateManager.loadIdentity();
      float ☃ = 1.000001F;
      GlStateManager.translate(-8.0F, -8.0F, -8.0F);
      GlStateManager.scale(1.000001F, 1.000001F, 1.000001F);
      GlStateManager.translate(8.0F, 8.0F, 8.0F);
      GlStateManager.getFloat(2982, this.modelviewMatrix);
      GlStateManager.popMatrix();
   }

   public void multModelviewMatrix() {
      GlStateManager.multMatrix(this.modelviewMatrix);
   }

   public CompiledChunk getCompiledChunk() {
      return this.compiledChunk;
   }

   public void setCompiledChunk(CompiledChunk var1) {
      this.lockCompiledChunk.lock();

      try {
         this.compiledChunk = ☃;
      } finally {
         this.lockCompiledChunk.unlock();
      }
   }

   public void stopCompileTask() {
      this.finishCompileTask();
      this.compiledChunk = CompiledChunk.DUMMY;
   }

   public void deleteGlResources() {
      this.stopCompileTask();
      this.world = null;

      for (int ☃ = 0; ☃ < BlockRenderLayer.values().length; ☃++) {
         if (this.vertexBuffers[☃] != null) {
            this.vertexBuffers[☃].deleteGlBuffers();
         }
      }
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public void setNeedsUpdate(boolean var1) {
      if (this.needsUpdate) {
         ☃ |= this.needsImmediateUpdate;
      }

      this.needsUpdate = true;
      this.needsImmediateUpdate = ☃;
   }

   public void clearNeedsUpdate() {
      this.needsUpdate = false;
      this.needsImmediateUpdate = false;
   }

   public boolean needsUpdate() {
      return this.needsUpdate;
   }

   public boolean needsImmediateUpdate() {
      return this.needsUpdate && this.needsImmediateUpdate;
   }

   public BlockPos getBlockPosOffset16(EnumFacing var1) {
      return this.mapEnumFacing[☃.ordinal()];
   }

   public World getWorld() {
      return this.world;
   }
}
