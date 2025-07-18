package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Sets;
import java.nio.FloatBuffer;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator.Status;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator.Type;
import net.minecraft.client.renderer.culling.ICamera;
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
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
import net.optifine.BlockPosM;
import net.optifine.CustomBlockLayers;
import net.optifine.override.ChunkCacheOF;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.render.AabbFrame;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;

public class RenderChunk {
   private final World world;
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
   private final MutableBlockPos position = new MutableBlockPos(-1, -1, -1);
   private final MutableBlockPos[] mapEnumFacing = new MutableBlockPos[6];
   private boolean needsImmediateUpdate;
   public static final BlockRenderLayer[] ENUM_WORLD_BLOCK_LAYERS = BlockRenderLayer.values();
   private final BlockRenderLayer[] blockLayersSingle = new BlockRenderLayer[1];
   private final boolean isMipmaps = Config.isMipmaps();
   private final boolean fixBlockLayer = !Reflector.BetterFoliageClient.exists();
   private boolean playerUpdate = false;
   public int regionX;
   public int regionZ;
   private final RenderChunk[] renderChunksOfset16 = new RenderChunk[6];
   private boolean renderChunksOffset16Updated = false;
   private Chunk chunk;
   private RenderChunk[] renderChunkNeighbours = new RenderChunk[EnumFacing.VALUES.length];
   private RenderChunk[] renderChunkNeighboursValid = new RenderChunk[EnumFacing.VALUES.length];
   private boolean renderChunkNeighboursUpated = false;
   private RenderGlobal.ContainerLocalRenderInformation renderInfo = new RenderGlobal.ContainerLocalRenderInformation(this, null, 0);
   public AabbFrame boundingBoxParent;

   public RenderChunk(World worldIn, RenderGlobal renderGlobalIn, int indexIn) {
      for (int i = 0; i < this.mapEnumFacing.length; i++) {
         this.mapEnumFacing[i] = new MutableBlockPos();
      }

      this.world = worldIn;
      this.renderGlobal = renderGlobalIn;
      this.index = indexIn;
      if (OpenGlHelper.useVbo()) {
         for (int j = 0; j < BlockRenderLayer.values().length; j++) {
            this.vertexBuffers[j] = new VertexBuffer(DefaultVertexFormats.BLOCK);
         }
      }
   }

   public boolean setFrameIndex(int frameIndexIn) {
      if (this.frameIndex == frameIndexIn) {
         return false;
      } else {
         this.frameIndex = frameIndexIn;
         return true;
      }
   }

   public VertexBuffer getVertexBufferByLayer(int layer) {
      return this.vertexBuffers[layer];
   }

   public void setPosition(int x, int y, int z) {
      if (x != this.position.getX() || y != this.position.getY() || z != this.position.getZ()) {
         this.stopCompileTask();
         this.position.setPos(x, y, z);
         int bits = 8;
         this.regionX = x >> bits << bits;
         this.regionZ = z >> bits << bits;
         this.boundingBox = new AxisAlignedBB(x, y, z, x + 16, y + 16, z + 16);

         for (EnumFacing enumfacing : EnumFacing.VALUES) {
            this.mapEnumFacing[enumfacing.ordinal()].setPos(this.position).move(enumfacing, 16);
         }

         this.renderChunksOffset16Updated = false;
         this.renderChunkNeighboursUpated = false;

         for (int i = 0; i < this.renderChunkNeighbours.length; i++) {
            RenderChunk neighbour = this.renderChunkNeighbours[i];
            if (neighbour != null) {
               neighbour.renderChunkNeighboursUpated = false;
            }
         }

         this.chunk = null;
         this.boundingBoxParent = null;
         this.initModelviewMatrix();
      }
   }

   public void resortTransparency(float x, float y, float z, ChunkCompileTaskGenerator generator) {
      CompiledChunk compiledchunk = generator.getCompiledChunk();
      if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(BlockRenderLayer.TRANSLUCENT)) {
         BufferBuilder bufferTranslucent = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT);
         this.preRenderBlocks(bufferTranslucent, this.position);
         bufferTranslucent.setVertexState(compiledchunk.getState());
         this.postRenderBlocks(BlockRenderLayer.TRANSLUCENT, x, y, z, bufferTranslucent, compiledchunk);
      }
   }

   public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator) {
      CompiledChunk compiledchunk = new CompiledChunk();
      int i = 1;
      BlockPos blockpos = new BlockPos(this.position);
      BlockPos blockpos1 = blockpos.add(15, 15, 15);
      generator.getLock().lock();

      try {
         if (generator.getStatus() != Status.COMPILING) {
            return;
         }

         generator.setCompiledChunk(compiledchunk);
      } finally {
         generator.getLock().unlock();
      }

      VisGraph lvt_9_1_ = new VisGraph();
      HashSet lvt_10_1_ = Sets.newHashSet();
      if (!this.isChunkRegionEmpty(blockpos)) {
         renderChunksUpdated++;
         ChunkCacheOF blockAccess = this.makeChunkCacheOF(blockpos);
         blockAccess.renderStart();
         boolean[] aboolean = new boolean[ENUM_WORLD_BLOCK_LAYERS.length];
         BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
         boolean forgeBlockCanRenderInLayerExists = Reflector.ForgeBlock_canRenderInLayer.exists();
         boolean forgeHooksSetRenderLayerExists = Reflector.ForgeHooksClient_setRenderLayer.exists();

         for (BlockPosM blockpos$mutableblockpos : BlockPosM.getAllInBoxMutable(blockpos, blockpos1)) {
            IBlockState iblockstate = blockAccess.getBlockState(blockpos$mutableblockpos);
            Block block = iblockstate.getBlock();
            if (iblockstate.p()) {
               lvt_9_1_.setOpaqueCube(blockpos$mutableblockpos);
            }

            if (ReflectorForge.blockHasTileEntity(iblockstate)) {
               TileEntity tileentity = blockAccess.getTileEntity(blockpos$mutableblockpos, EnumCreateEntityType.CHECK);
               if (tileentity != null) {
                  TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getRenderer(tileentity);
                  if (tileentityspecialrenderer != null) {
                     if (tileentityspecialrenderer.isGlobalRenderer(tileentity)) {
                        lvt_10_1_.add(tileentity);
                     } else {
                        compiledchunk.addTileEntity(tileentity);
                     }
                  }
               }
            }

            BlockRenderLayer[] blockLayers;
            if (forgeBlockCanRenderInLayerExists) {
               blockLayers = ENUM_WORLD_BLOCK_LAYERS;
            } else {
               blockLayers = this.blockLayersSingle;
               blockLayers[0] = block.getRenderLayer();
            }

            for (int ix = 0; ix < blockLayers.length; ix++) {
               BlockRenderLayer blockrenderlayer1 = blockLayers[ix];
               if (forgeBlockCanRenderInLayerExists) {
                  boolean canRenderInLayer = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInLayer, new Object[]{iblockstate, blockrenderlayer1});
                  if (!canRenderInLayer) {
                     continue;
                  }
               }

               if (forgeHooksSetRenderLayerExists) {
                  Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[]{blockrenderlayer1});
               }

               blockrenderlayer1 = this.fixBlockLayer(iblockstate, blockrenderlayer1);
               int j = blockrenderlayer1.ordinal();
               if (block.getDefaultState().i() != EnumBlockRenderType.INVISIBLE) {
                  BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j);
                  bufferbuilder.setBlockLayer(blockrenderlayer1);
                  RenderEnv renderEnv = bufferbuilder.getRenderEnv(iblockstate, blockpos$mutableblockpos);
                  renderEnv.setRegionRenderCacheBuilder(generator.getRegionRenderCacheBuilder());
                  if (!compiledchunk.isLayerStarted(blockrenderlayer1)) {
                     compiledchunk.setLayerStarted(blockrenderlayer1);
                     this.preRenderBlocks(bufferbuilder, blockpos);
                  }

                  aboolean[j] |= blockrendererdispatcher.renderBlock(iblockstate, blockpos$mutableblockpos, blockAccess, bufferbuilder);
                  if (renderEnv.isOverlaysRendered()) {
                     this.postRenderOverlays(generator.getRegionRenderCacheBuilder(), compiledchunk, aboolean);
                     renderEnv.setOverlaysRendered(false);
                  }
               }
            }

            if (forgeHooksSetRenderLayerExists) {
               Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[]{null});
            }
         }

         for (BlockRenderLayer blockrenderlayer : ENUM_WORLD_BLOCK_LAYERS) {
            if (aboolean[blockrenderlayer.ordinal()]) {
               compiledchunk.setLayerUsed(blockrenderlayer);
            }

            if (compiledchunk.isLayerStarted(blockrenderlayer)) {
               if (Config.isShaders()) {
                  SVertexBuilder.calcNormalChunkLayer(generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer));
               }

               BufferBuilder bufferBuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer);
               this.postRenderBlocks(blockrenderlayer, x, y, z, bufferBuilder, compiledchunk);
               if (bufferBuilder.animatedSprites != null) {
                  compiledchunk.setAnimatedSprites(blockrenderlayer, (BitSet)bufferBuilder.animatedSprites.clone());
               }
            } else {
               compiledchunk.setAnimatedSprites(blockrenderlayer, null);
            }
         }

         blockAccess.renderFinish();
      }

      compiledchunk.setVisibility(lvt_9_1_.computeVisibility());
      this.lockCompileTask.lock();

      try {
         Set<TileEntity> set = Sets.newHashSet(lvt_10_1_);
         Set<TileEntity> set1 = Sets.newHashSet(this.setTileEntities);
         set.removeAll(this.setTileEntities);
         set1.removeAll(lvt_10_1_);
         this.setTileEntities.clear();
         this.setTileEntities.addAll(lvt_10_1_);
         this.renderGlobal.updateTileEntities(set1, set);
      } finally {
         this.lockCompileTask.unlock();
      }
   }

   protected void finishCompileTask() {
      this.lockCompileTask.lock();

      try {
         if (this.compileTask != null && this.compileTask.getStatus() != Status.DONE) {
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

      ChunkCompileTaskGenerator chunkcompiletaskgenerator;
      try {
         this.finishCompileTask();
         this.compileTask = new ChunkCompileTaskGenerator(this, Type.REBUILD_CHUNK, this.getDistanceSq());
         this.rebuildWorldView();
         chunkcompiletaskgenerator = this.compileTask;
      } finally {
         this.lockCompileTask.unlock();
      }

      return chunkcompiletaskgenerator;
   }

   private void rebuildWorldView() {
      int i = 1;
   }

   @Nullable
   public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
      this.lockCompileTask.lock();

      ChunkCompileTaskGenerator var2;
      try {
         if (this.compileTask != null && this.compileTask.getStatus() == Status.PENDING) {
            return null;
         }

         if (this.compileTask != null && this.compileTask.getStatus() != Status.DONE) {
            this.compileTask.finish();
            this.compileTask = null;
         }

         this.compileTask = new ChunkCompileTaskGenerator(this, Type.RESORT_TRANSPARENCY, this.getDistanceSq());
         this.compileTask.setCompiledChunk(this.compiledChunk);
         ChunkCompileTaskGenerator chunkcompiletaskgenerator = this.compileTask;
         var2 = chunkcompiletaskgenerator;
      } finally {
         this.lockCompileTask.unlock();
      }

      return var2;
   }

   protected double getDistanceSq() {
      EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
      double d0 = this.boundingBox.minX + 8.0 - entityplayersp.posX;
      double d1 = this.boundingBox.minY + 8.0 - entityplayersp.posY;
      double d2 = this.boundingBox.minZ + 8.0 - entityplayersp.posZ;
      return d0 * d0 + d1 * d1 + d2 * d2;
   }

   private void preRenderBlocks(BufferBuilder worldRendererIn, BlockPos pos) {
      worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
      if (Config.isRenderRegions()) {
         int bits = 8;
         int dx = pos.getX() >> bits << bits;
         int dy = pos.getY() >> bits << bits;
         int dz = pos.getZ() >> bits << bits;
         dx = this.regionX;
         dz = this.regionZ;
         worldRendererIn.setTranslation(-dx, -dy, -dz);
      } else {
         worldRendererIn.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
      }
   }

   private void postRenderBlocks(BlockRenderLayer layer, float x, float y, float z, BufferBuilder worldRendererIn, CompiledChunk compiledChunkIn) {
      if (layer == BlockRenderLayer.TRANSLUCENT && !compiledChunkIn.isLayerEmpty(layer)) {
         worldRendererIn.sortVertexData(x, y, z);
         compiledChunkIn.setState(worldRendererIn.getVertexState());
      }

      worldRendererIn.finishDrawing();
   }

   private void initModelviewMatrix() {
      GlStateManager.pushMatrix();
      GlStateManager.loadIdentity();
      float f = 1.000001F;
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

   public void setCompiledChunk(CompiledChunk compiledChunkIn) {
      this.lockCompiledChunk.lock();

      try {
         this.compiledChunk = compiledChunkIn;
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

      for (int i = 0; i < BlockRenderLayer.values().length; i++) {
         if (this.vertexBuffers[i] != null) {
            this.vertexBuffers[i].deleteGlBuffers();
         }
      }
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public void setNeedsUpdate(boolean immediate) {
      if (this.needsUpdate) {
         immediate |= this.needsImmediateUpdate;
      }

      this.needsUpdate = true;
      this.needsImmediateUpdate = immediate;
      if (this.isWorldPlayerUpdate()) {
         this.playerUpdate = true;
      }
   }

   public void clearNeedsUpdate() {
      this.needsUpdate = false;
      this.needsImmediateUpdate = false;
      this.playerUpdate = false;
   }

   public boolean needsUpdate() {
      return this.needsUpdate;
   }

   public boolean needsImmediateUpdate() {
      return this.needsUpdate && this.needsImmediateUpdate;
   }

   public BlockPos getBlockPosOffset16(EnumFacing facing) {
      return this.mapEnumFacing[facing.ordinal()];
   }

   public World getWorld() {
      return this.world;
   }

   private boolean isWorldPlayerUpdate() {
      if (this.world instanceof WorldClient) {
         WorldClient worldClient = (WorldClient)this.world;
         return worldClient.isPlayerUpdate();
      } else {
         return false;
      }
   }

   public boolean isPlayerUpdate() {
      return this.playerUpdate;
   }

   private BlockRenderLayer fixBlockLayer(IBlockState blockState, BlockRenderLayer layer) {
      if (CustomBlockLayers.isActive()) {
         BlockRenderLayer layerCustom = CustomBlockLayers.getRenderLayer(blockState);
         if (layerCustom != null) {
            return layerCustom;
         }
      }

      if (!this.fixBlockLayer) {
         return layer;
      } else {
         if (this.isMipmaps) {
            if (layer == BlockRenderLayer.CUTOUT) {
               Block block = blockState.getBlock();
               if (block instanceof BlockRedstoneWire) {
                  return layer;
               }

               if (block instanceof BlockCactus) {
                  return layer;
               }

               return BlockRenderLayer.CUTOUT_MIPPED;
            }
         } else if (layer == BlockRenderLayer.CUTOUT_MIPPED) {
            return BlockRenderLayer.CUTOUT;
         }

         return layer;
      }
   }

   private void postRenderOverlays(RegionRenderCacheBuilder regionRenderCacheBuilder, CompiledChunk compiledChunk, boolean[] layerFlags) {
      this.postRenderOverlay(BlockRenderLayer.CUTOUT, regionRenderCacheBuilder, compiledChunk, layerFlags);
      this.postRenderOverlay(BlockRenderLayer.CUTOUT_MIPPED, regionRenderCacheBuilder, compiledChunk, layerFlags);
      this.postRenderOverlay(BlockRenderLayer.TRANSLUCENT, regionRenderCacheBuilder, compiledChunk, layerFlags);
   }

   private void postRenderOverlay(BlockRenderLayer layer, RegionRenderCacheBuilder regionRenderCacheBuilder, CompiledChunk compiledchunk, boolean[] layerFlags) {
      BufferBuilder bufferOverlay = regionRenderCacheBuilder.getWorldRendererByLayer(layer);
      if (bufferOverlay.isDrawing()) {
         compiledchunk.setLayerStarted(layer);
         layerFlags[layer.ordinal()] = true;
      }
   }

   private ChunkCacheOF makeChunkCacheOF(BlockPos posIn) {
      BlockPos posFrom = posIn.add(-1, -1, -1);
      BlockPos posTo = posIn.add(16, 16, 16);
      ChunkCache chunkCache = this.createRegionRenderCache(this.world, posFrom, posTo, 1);
      if (Reflector.MinecraftForgeClient_onRebuildChunk.exists()) {
         Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, new Object[]{this.world, posIn, chunkCache});
      }

      return new ChunkCacheOF(chunkCache, posFrom, posTo, 1);
   }

   public RenderChunk getRenderChunkOffset16(ViewFrustum viewFrustum, EnumFacing facing) {
      if (!this.renderChunksOffset16Updated) {
         for (int i = 0; i < EnumFacing.VALUES.length; i++) {
            EnumFacing ef = EnumFacing.VALUES[i];
            BlockPos posOffset16 = this.getBlockPosOffset16(ef);
            this.renderChunksOfset16[i] = viewFrustum.getRenderChunk(posOffset16);
         }

         this.renderChunksOffset16Updated = true;
      }

      return this.renderChunksOfset16[facing.ordinal()];
   }

   public Chunk getChunk() {
      return this.getChunk(this.position);
   }

   private Chunk getChunk(BlockPos posIn) {
      Chunk chunkLocal = this.chunk;
      if (chunkLocal != null && chunkLocal.isLoaded()) {
         return chunkLocal;
      } else {
         chunkLocal = this.world.getChunk(posIn);
         this.chunk = chunkLocal;
         return chunkLocal;
      }
   }

   public boolean isChunkRegionEmpty() {
      return this.isChunkRegionEmpty(this.position);
   }

   private boolean isChunkRegionEmpty(BlockPos posIn) {
      int yStart = posIn.getY();
      int yEnd = yStart + 15;
      return this.getChunk(posIn).isEmptyBetween(yStart, yEnd);
   }

   public void setRenderChunkNeighbour(EnumFacing facing, RenderChunk neighbour) {
      this.renderChunkNeighbours[facing.ordinal()] = neighbour;
      this.renderChunkNeighboursValid[facing.ordinal()] = neighbour;
   }

   public RenderChunk getRenderChunkNeighbour(EnumFacing facing) {
      if (!this.renderChunkNeighboursUpated) {
         this.updateRenderChunkNeighboursValid();
      }

      return this.renderChunkNeighboursValid[facing.ordinal()];
   }

   public RenderGlobal.ContainerLocalRenderInformation getRenderInfo() {
      return this.renderInfo;
   }

   private void updateRenderChunkNeighboursValid() {
      int x = this.getPosition().getX();
      int z = this.getPosition().getZ();
      int north = EnumFacing.NORTH.ordinal();
      int south = EnumFacing.SOUTH.ordinal();
      int west = EnumFacing.WEST.ordinal();
      int east = EnumFacing.EAST.ordinal();
      this.renderChunkNeighboursValid[north] = this.renderChunkNeighbours[north].getPosition().getZ() == z - 16 ? this.renderChunkNeighbours[north] : null;
      this.renderChunkNeighboursValid[south] = this.renderChunkNeighbours[south].getPosition().getZ() == z + 16 ? this.renderChunkNeighbours[south] : null;
      this.renderChunkNeighboursValid[west] = this.renderChunkNeighbours[west].getPosition().getX() == x - 16 ? this.renderChunkNeighbours[west] : null;
      this.renderChunkNeighboursValid[east] = this.renderChunkNeighbours[east].getPosition().getX() == x + 16 ? this.renderChunkNeighbours[east] : null;
      this.renderChunkNeighboursUpated = true;
   }

   public boolean isBoundingBoxInFrustum(ICamera camera, int frameCount) {
      return this.getBoundingBoxParent().isBoundingBoxInFrustumFully(camera, frameCount) ? true : camera.isBoundingBoxInFrustum(this.boundingBox);
   }

   public AabbFrame getBoundingBoxParent() {
      if (this.boundingBoxParent == null) {
         BlockPos pos = this.getPosition();
         int x = pos.getX();
         int y = pos.getY();
         int z = pos.getZ();
         int bits = 5;
         int xp = x >> bits << bits;
         int yp = y >> bits << bits;
         int zp = z >> bits << bits;
         if (xp != x || yp != y || zp != z) {
            AabbFrame bbp = this.renderGlobal.getRenderChunk(new BlockPos(xp, yp, zp)).getBoundingBoxParent();
            if (bbp != null && bbp.minX == xp && bbp.minY == yp && bbp.minZ == zp) {
               this.boundingBoxParent = bbp;
            }
         }

         if (this.boundingBoxParent == null) {
            int delta = 1 << bits;
            this.boundingBoxParent = new AabbFrame(xp, yp, zp, xp + delta, yp + delta, zp + delta);
         }
      }

      return this.boundingBoxParent;
   }

   @Override
   public String toString() {
      return "pos: " + this.getPosition() + ", frameIndex: " + this.frameIndex;
   }

   protected ChunkCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract) {
      return new ChunkCache(world, from, to, subtract);
   }
}
