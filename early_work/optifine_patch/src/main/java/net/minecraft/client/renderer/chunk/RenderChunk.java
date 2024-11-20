/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  com.google.common.collect.Sets
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.FloatBuffer
 *  java.util.BitSet
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.Set
 *  java.util.concurrent.locks.ReentrantLock
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockCactus
 *  net.minecraft.block.BlockRedstoneWire
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.BlockRendererDispatcher
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RegionRenderCacheBuilder
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation
 *  net.minecraft.client.renderer.ViewFrustum
 *  net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator
 *  net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator$Status
 *  net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator$Type
 *  net.minecraft.client.renderer.chunk.CompiledChunk
 *  net.minecraft.client.renderer.chunk.VisGraph
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.EnumBlockRenderType
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.ChunkCache
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraft.world.chunk.Chunk$EnumCreateEntityType
 *  net.optifine.BlockPosM
 *  net.optifine.CustomBlockLayers
 *  net.optifine.override.ChunkCacheOF
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorForge
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.render.AabbFrame
 *  net.optifine.render.RenderEnv
 *  net.optifine.shaders.SVertexBuilder
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Sets;
import java.nio.FloatBuffer;
import java.util.BitSet;
import java.util.Collection;
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
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
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
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.optifine.BlockPosM;
import net.optifine.CustomBlockLayers;
import net.optifine.override.ChunkCacheOF;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.reflect.ReflectorMethod;
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
    private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer((int)16);
    private final VertexBuffer[] vertexBuffers = new VertexBuffer[BlockRenderLayer.values().length];
    public AxisAlignedBB boundingBox;
    private int frameIndex = -1;
    private boolean needsUpdate = true;
    private final BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos(-1, -1, -1);
    private final BlockPos.MutableBlockPos[] mapEnumFacing = new BlockPos.MutableBlockPos[6];
    private boolean needsImmediateUpdate;
    public static final BlockRenderLayer[] ENUM_WORLD_BLOCK_LAYERS;
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
        for (int i = 0; i < this.mapEnumFacing.length; ++i) {
            this.mapEnumFacing[i] = new BlockPos.MutableBlockPos();
        }
        this.world = worldIn;
        this.renderGlobal = renderGlobalIn;
        this.index = indexIn;
        if (OpenGlHelper.useVbo()) {
            for (int j = 0; j < BlockRenderLayer.values().length; ++j) {
                this.vertexBuffers[j] = new VertexBuffer(DefaultVertexFormats.BLOCK);
            }
        }
    }

    public boolean setFrameIndex(int frameIndexIn) {
        if (this.frameIndex == frameIndexIn) {
            return false;
        }
        this.frameIndex = frameIndexIn;
        return true;
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
            this.boundingBox = new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 16), (double)(y + 16), (double)(z + 16));
            for (EnumFacing enumfacing : EnumFacing.VALUES) {
                this.mapEnumFacing[enumfacing.ordinal()].setPos((Vec3i)this.position).move(enumfacing, 16);
            }
            this.renderChunksOffset16Updated = false;
            this.renderChunkNeighboursUpated = false;
            for (int i = 0; i < this.renderChunkNeighbours.length; ++i) {
                RenderChunk neighbour = this.renderChunkNeighbours[i];
                if (neighbour == null) continue;
                neighbour.renderChunkNeighboursUpated = false;
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
            this.preRenderBlocks(bufferTranslucent, (BlockPos)this.position);
            bufferTranslucent.setVertexState(compiledchunk.getState());
            this.postRenderBlocks(BlockRenderLayer.TRANSLUCENT, x, y, z, bufferTranslucent, compiledchunk);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator) {
        CompiledChunk compiledchunk = new CompiledChunk();
        boolean i = true;
        BlockPos blockpos = new BlockPos((Vec3i)this.position);
        BlockPos blockpos1 = blockpos.add(15, 15, 15);
        generator.getLock().lock();
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }
            generator.setCompiledChunk(compiledchunk);
        }
        finally {
            generator.getLock().unlock();
        }
        VisGraph lvt_9_1_ = new VisGraph();
        HashSet lvt_10_1_ = Sets.newHashSet();
        if (!this.isChunkRegionEmpty(blockpos)) {
            ++renderChunksUpdated;
            ChunkCacheOF blockAccess = this.makeChunkCacheOF(blockpos);
            blockAccess.renderStart();
            boolean[] aboolean = new boolean[ENUM_WORLD_BLOCK_LAYERS.length];
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            boolean forgeBlockCanRenderInLayerExists = Reflector.ForgeBlock_canRenderInLayer.exists();
            boolean forgeHooksSetRenderLayerExists = Reflector.ForgeHooksClient_setRenderLayer.exists();
            for (BlockPosM blockpos$mutableblockpos : BlockPosM.getAllInBoxMutable((BlockPos)blockpos, (BlockPos)blockpos1)) {
                BlockRenderLayer[] blockLayers;
                TileEntitySpecialRenderer tileentityspecialrenderer;
                TileEntity tileentity;
                IBlockState iblockstate = blockAccess.o((BlockPos)blockpos$mutableblockpos);
                Block block = iblockstate.getBlock();
                if (iblockstate.p()) {
                    lvt_9_1_.setOpaqueCube((BlockPos)blockpos$mutableblockpos);
                }
                if (ReflectorForge.blockHasTileEntity((IBlockState)iblockstate) && (tileentity = blockAccess.getTileEntity((BlockPos)blockpos$mutableblockpos, Chunk.EnumCreateEntityType.CHECK)) != null && (tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getRenderer(tileentity)) != null) {
                    if (tileentityspecialrenderer.isGlobalRenderer(tileentity)) {
                        lvt_10_1_.add((Object)tileentity);
                    } else {
                        compiledchunk.addTileEntity(tileentity);
                    }
                }
                if (forgeBlockCanRenderInLayerExists) {
                    blockLayers = ENUM_WORLD_BLOCK_LAYERS;
                } else {
                    blockLayers = this.blockLayersSingle;
                    blockLayers[0] = block.getRenderLayer();
                }
                for (int ix = 0; ix < blockLayers.length; ++ix) {
                    boolean canRenderInLayer;
                    BlockRenderLayer blockrenderlayer1 = blockLayers[ix];
                    if (forgeBlockCanRenderInLayerExists && !(canRenderInLayer = Reflector.callBoolean((Object)block, (ReflectorMethod)Reflector.ForgeBlock_canRenderInLayer, (Object[])new Object[]{iblockstate, blockrenderlayer1}))) continue;
                    if (forgeHooksSetRenderLayerExists) {
                        Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_setRenderLayer, (Object[])new Object[]{blockrenderlayer1});
                    }
                    blockrenderlayer1 = this.fixBlockLayer(iblockstate, blockrenderlayer1);
                    int j = blockrenderlayer1.ordinal();
                    if (block.getDefaultState().i() == EnumBlockRenderType.INVISIBLE) continue;
                    BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j);
                    bufferbuilder.setBlockLayer(blockrenderlayer1);
                    RenderEnv renderEnv = bufferbuilder.getRenderEnv(iblockstate, (BlockPos)blockpos$mutableblockpos);
                    renderEnv.setRegionRenderCacheBuilder(generator.getRegionRenderCacheBuilder());
                    if (!compiledchunk.isLayerStarted(blockrenderlayer1)) {
                        compiledchunk.setLayerStarted(blockrenderlayer1);
                        this.preRenderBlocks(bufferbuilder, blockpos);
                    }
                    int n = j;
                    aboolean[n] = aboolean[n] | blockrendererdispatcher.renderBlock(iblockstate, (BlockPos)blockpos$mutableblockpos, (IBlockAccess)blockAccess, bufferbuilder);
                    if (!renderEnv.isOverlaysRendered()) continue;
                    this.postRenderOverlays(generator.getRegionRenderCacheBuilder(), compiledchunk, aboolean);
                    renderEnv.setOverlaysRendered(false);
                }
                if (!forgeHooksSetRenderLayerExists) continue;
                Reflector.callVoid((ReflectorMethod)Reflector.ForgeHooksClient_setRenderLayer, (Object[])new Object[]{null});
            }
            for (BlockRenderLayer blockrenderlayer : ENUM_WORLD_BLOCK_LAYERS) {
                if (aboolean[blockrenderlayer.ordinal()]) {
                    compiledchunk.setLayerUsed(blockrenderlayer);
                }
                if (compiledchunk.isLayerStarted(blockrenderlayer)) {
                    if (Config.isShaders()) {
                        SVertexBuilder.calcNormalChunkLayer((BufferBuilder)generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer));
                    }
                    BufferBuilder bufferBuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer);
                    this.postRenderBlocks(blockrenderlayer, x, y, z, bufferBuilder, compiledchunk);
                    if (bufferBuilder.animatedSprites == null) continue;
                    compiledchunk.setAnimatedSprites(blockrenderlayer, (BitSet)bufferBuilder.animatedSprites.clone());
                    continue;
                }
                compiledchunk.setAnimatedSprites(blockrenderlayer, null);
            }
            blockAccess.renderFinish();
        }
        compiledchunk.setVisibility(lvt_9_1_.computeVisibility());
        this.lockCompileTask.lock();
        try {
            HashSet set = Sets.newHashSet((Iterable)lvt_10_1_);
            HashSet set1 = Sets.newHashSet(this.setTileEntities);
            set.removeAll(this.setTileEntities);
            set1.removeAll((Collection)lvt_10_1_);
            this.setTileEntities.clear();
            this.setTileEntities.addAll((Collection)lvt_10_1_);
            this.renderGlobal.updateTileEntities((Collection)set1, (Collection)set);
        }
        finally {
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
        }
        finally {
            this.lockCompileTask.unlock();
        }
    }

    public ReentrantLock getLockCompileTask() {
        return this.lockCompileTask;
    }

    public ChunkCompileTaskGenerator makeCompileTaskChunk() {
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;
        this.lockCompileTask.lock();
        try {
            this.finishCompileTask();
            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK, this.getDistanceSq());
            this.rebuildWorldView();
            chunkcompiletaskgenerator = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        return chunkcompiletaskgenerator;
    }

    private void rebuildWorldView() {
        boolean i = true;
    }

    @Nullable
    public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;
        this.lockCompileTask.lock();
        try {
            if (this.compileTask == null || this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
                ChunkCompileTaskGenerator chunkcompiletaskgenerator2;
                if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
                    this.compileTask.finish();
                    this.compileTask = null;
                }
                this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY, this.getDistanceSq());
                this.compileTask.setCompiledChunk(this.compiledChunk);
                ChunkCompileTaskGenerator chunkCompileTaskGenerator = chunkcompiletaskgenerator2 = this.compileTask;
                return chunkCompileTaskGenerator;
            }
            chunkcompiletaskgenerator = null;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        return chunkcompiletaskgenerator;
    }

    protected double getDistanceSq() {
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
        double d0 = this.boundingBox.minX + 8.0 - entityplayersp.p;
        double d1 = this.boundingBox.minY + 8.0 - entityplayersp.q;
        double d2 = this.boundingBox.minZ + 8.0 - entityplayersp.r;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    private void preRenderBlocks(BufferBuilder worldRendererIn, BlockPos pos) {
        worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
        if (Config.isRenderRegions()) {
            int bits = 8;
            int dx = pos.p() >> bits << bits;
            int dy = pos.q() >> bits << bits;
            int dz = pos.r() >> bits << bits;
            dx = this.regionX;
            dz = this.regionZ;
            worldRendererIn.setTranslation((double)(-dx), (double)(-dy), (double)(-dz));
        } else {
            worldRendererIn.setTranslation((double)(-pos.p()), (double)(-pos.q()), (double)(-pos.r()));
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
        float f = 1.000001f;
        GlStateManager.translate((float)-8.0f, (float)-8.0f, (float)-8.0f);
        GlStateManager.scale((float)1.000001f, (float)1.000001f, (float)1.000001f);
        GlStateManager.translate((float)8.0f, (float)8.0f, (float)8.0f);
        GlStateManager.getFloat((int)2982, (FloatBuffer)this.modelviewMatrix);
        GlStateManager.popMatrix();
    }

    public void multModelviewMatrix() {
        GlStateManager.multMatrix((FloatBuffer)this.modelviewMatrix);
    }

    public CompiledChunk getCompiledChunk() {
        return this.compiledChunk;
    }

    public void setCompiledChunk(CompiledChunk compiledChunkIn) {
        this.lockCompiledChunk.lock();
        try {
            this.compiledChunk = compiledChunkIn;
        }
        finally {
            this.lockCompiledChunk.unlock();
        }
    }

    public void stopCompileTask() {
        this.finishCompileTask();
        this.compiledChunk = CompiledChunk.DUMMY;
    }

    public void deleteGlResources() {
        this.stopCompileTask();
        for (int i = 0; i < BlockRenderLayer.values().length; ++i) {
            if (this.vertexBuffers[i] == null) continue;
            this.vertexBuffers[i].deleteGlBuffers();
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
        }
        return false;
    }

    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }

    private BlockRenderLayer fixBlockLayer(IBlockState blockState, BlockRenderLayer layer) {
        BlockRenderLayer layerCustom;
        if (CustomBlockLayers.isActive() && (layerCustom = CustomBlockLayers.getRenderLayer((IBlockState)blockState)) != null) {
            return layerCustom;
        }
        if (!this.fixBlockLayer) {
            return layer;
        }
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
            Reflector.call((ReflectorMethod)Reflector.MinecraftForgeClient_onRebuildChunk, (Object[])new Object[]{this.world, posIn, chunkCache});
        }
        ChunkCacheOF chunkCacheOF = new ChunkCacheOF(chunkCache, posFrom, posTo, 1);
        return chunkCacheOF;
    }

    public RenderChunk getRenderChunkOffset16(ViewFrustum viewFrustum, EnumFacing facing) {
        if (!this.renderChunksOffset16Updated) {
            for (int i = 0; i < EnumFacing.VALUES.length; ++i) {
                EnumFacing ef = EnumFacing.VALUES[i];
                BlockPos posOffset16 = this.getBlockPosOffset16(ef);
                this.renderChunksOfset16[i] = viewFrustum.getRenderChunk(posOffset16);
            }
            this.renderChunksOffset16Updated = true;
        }
        return this.renderChunksOfset16[facing.ordinal()];
    }

    public Chunk getChunk() {
        return this.getChunk((BlockPos)this.position);
    }

    private Chunk getChunk(BlockPos posIn) {
        Chunk chunkLocal = this.chunk;
        if (chunkLocal != null && chunkLocal.isLoaded()) {
            return chunkLocal;
        }
        this.chunk = chunkLocal = this.world.getChunk(posIn);
        return chunkLocal;
    }

    public boolean isChunkRegionEmpty() {
        return this.isChunkRegionEmpty((BlockPos)this.position);
    }

    private boolean isChunkRegionEmpty(BlockPos posIn) {
        int yStart = posIn.q();
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
        int x = this.getPosition().p();
        int z = this.getPosition().r();
        int north = EnumFacing.NORTH.ordinal();
        int south = EnumFacing.SOUTH.ordinal();
        int west = EnumFacing.WEST.ordinal();
        int east = EnumFacing.EAST.ordinal();
        this.renderChunkNeighboursValid[north] = this.renderChunkNeighbours[north].getPosition().r() == z - 16 ? this.renderChunkNeighbours[north] : null;
        this.renderChunkNeighboursValid[south] = this.renderChunkNeighbours[south].getPosition().r() == z + 16 ? this.renderChunkNeighbours[south] : null;
        this.renderChunkNeighboursValid[west] = this.renderChunkNeighbours[west].getPosition().p() == x - 16 ? this.renderChunkNeighbours[west] : null;
        this.renderChunkNeighboursValid[east] = this.renderChunkNeighbours[east].getPosition().p() == x + 16 ? this.renderChunkNeighbours[east] : null;
        this.renderChunkNeighboursUpated = true;
    }

    public boolean isBoundingBoxInFrustum(ICamera camera, int frameCount) {
        if (this.getBoundingBoxParent().isBoundingBoxInFrustumFully(camera, frameCount)) {
            return true;
        }
        return camera.isBoundingBoxInFrustum(this.boundingBox);
    }

    public AabbFrame getBoundingBoxParent() {
        if (this.boundingBoxParent == null) {
            AabbFrame bbp;
            BlockPos pos = this.getPosition();
            int x = pos.p();
            int y = pos.q();
            int z = pos.r();
            int bits = 5;
            int xp = x >> bits << bits;
            int yp = y >> bits << bits;
            int zp = z >> bits << bits;
            if ((xp != x || yp != y || zp != z) && (bbp = this.renderGlobal.getRenderChunk(new BlockPos(xp, yp, zp)).getBoundingBoxParent()) != null && bbp.a == (double)xp && bbp.b == (double)yp && bbp.c == (double)zp) {
                this.boundingBoxParent = bbp;
            }
            if (this.boundingBoxParent == null) {
                int delta = 1 << bits;
                this.boundingBoxParent = new AabbFrame((double)xp, (double)yp, (double)zp, (double)(xp + delta), (double)(yp + delta), (double)(zp + delta));
            }
        }
        return this.boundingBoxParent;
    }

    public String toString() {
        return "pos: " + this.getPosition() + ", frameIndex: " + this.frameIndex;
    }

    protected ChunkCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract) {
        return new ChunkCache(world, from, to, subtract);
    }

    static {
        ENUM_WORLD_BLOCK_LAYERS = BlockRenderLayer.values();
    }
}
