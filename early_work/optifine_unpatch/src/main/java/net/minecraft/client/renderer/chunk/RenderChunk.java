/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.nio.FloatBuffer
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.Set
 *  java.util.concurrent.locks.ReentrantLock
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.BlockRendererDispatcher
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator
 *  net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator$Status
 *  net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator$Type
 *  net.minecraft.client.renderer.chunk.CompiledChunk
 *  net.minecraft.client.renderer.chunk.VisGraph
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
 *  net.minecraft.world.chunk.Chunk$EnumCreateEntityType
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.client.MinecraftForgeClient
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Sets;
import java.nio.FloatBuffer;
import java.util.Collection;
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
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
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
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
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
    private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer((int)16);
    private final VertexBuffer[] vertexBuffers = new VertexBuffer[BlockRenderLayer.values().length];
    public AxisAlignedBB boundingBox;
    private int frameIndex = -1;
    private boolean needsUpdate = true;
    private final BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos(-1, -1, -1);
    private final BlockPos.MutableBlockPos[] mapEnumFacing = new BlockPos.MutableBlockPos[6];
    private boolean needsImmediateUpdate;
    private ChunkCache worldView;

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
            this.boundingBox = new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 16), (double)(y + 16), (double)(z + 16));
            for (EnumFacing enumfacing : EnumFacing.values()) {
                this.mapEnumFacing[enumfacing.ordinal()].setPos((Vec3i)this.position).move(enumfacing, 16);
            }
            this.initModelviewMatrix();
        }
    }

    public void resortTransparency(float x, float y, float z, ChunkCompileTaskGenerator generator) {
        CompiledChunk compiledchunk = generator.getCompiledChunk();
        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(BlockRenderLayer.TRANSLUCENT)) {
            this.preRenderBlocks(generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT), (BlockPos)this.position);
            generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT).setVertexState(compiledchunk.getState());
            this.postRenderBlocks(BlockRenderLayer.TRANSLUCENT, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT), compiledchunk);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator) {
        CompiledChunk compiledchunk = new CompiledChunk();
        boolean i = true;
        BlockPos.MutableBlockPos blockpos = this.position;
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
        if (!this.worldView.isEmpty()) {
            ++renderChunksUpdated;
            boolean[] aboolean = new boolean[BlockRenderLayer.values().length];
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable((BlockPos)blockpos, (BlockPos)blockpos1)) {
                TileEntitySpecialRenderer tileentityspecialrenderer;
                TileEntity tileentity;
                IBlockState iblockstate = this.worldView.getBlockState((BlockPos)blockpos$mutableblockpos);
                Block block = iblockstate.getBlock();
                if (iblockstate.p()) {
                    lvt_9_1_.setOpaqueCube((BlockPos)blockpos$mutableblockpos);
                }
                if (block.hasTileEntity(iblockstate) && (tileentity = this.worldView.getTileEntity((BlockPos)blockpos$mutableblockpos, Chunk.EnumCreateEntityType.CHECK)) != null && (tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getRenderer(tileentity)) != null) {
                    if (tileentityspecialrenderer.isGlobalRenderer(tileentity)) {
                        lvt_10_1_.add((Object)tileentity);
                    } else {
                        compiledchunk.addTileEntity(tileentity);
                    }
                }
                for (BlockRenderLayer blockrenderlayer1 : BlockRenderLayer.values()) {
                    if (!block.canRenderInLayer(iblockstate, blockrenderlayer1)) continue;
                    ForgeHooksClient.setRenderLayer((BlockRenderLayer)blockrenderlayer1);
                    int j = blockrenderlayer1.ordinal();
                    if (block.getDefaultState().i() == EnumBlockRenderType.INVISIBLE) continue;
                    BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(j);
                    if (!compiledchunk.isLayerStarted(blockrenderlayer1)) {
                        compiledchunk.setLayerStarted(blockrenderlayer1);
                        this.preRenderBlocks(bufferbuilder, (BlockPos)blockpos);
                    }
                    int n = j;
                    aboolean[n] = aboolean[n] | blockrendererdispatcher.renderBlock(iblockstate, (BlockPos)blockpos$mutableblockpos, (IBlockAccess)this.worldView, bufferbuilder);
                }
                ForgeHooksClient.setRenderLayer(null);
            }
            for (BlockRenderLayer blockrenderlayer : BlockRenderLayer.values()) {
                if (aboolean[blockrenderlayer.ordinal()]) {
                    compiledchunk.setLayerUsed(blockrenderlayer);
                }
                if (!compiledchunk.isLayerStarted(blockrenderlayer)) continue;
                this.postRenderBlocks(blockrenderlayer, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), compiledchunk);
            }
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
        ChunkCache cache = this.createRegionRenderCache(this.world, this.position.add(-1, -1, -1), this.position.add(16, 16, 16), 1);
        MinecraftForgeClient.onRebuildChunk((World)this.world, (BlockPos)this.position, (ChunkCache)cache);
        this.worldView = cache;
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

    private void preRenderBlocks(BufferBuilder bufferBuilderIn, BlockPos pos) {
        bufferBuilderIn.begin(7, DefaultVertexFormats.BLOCK);
        bufferBuilderIn.setTranslation((double)(-pos.p()), (double)(-pos.q()), (double)(-pos.r()));
    }

    private void postRenderBlocks(BlockRenderLayer layer, float x, float y, float z, BufferBuilder bufferBuilderIn, CompiledChunk compiledChunkIn) {
        if (layer == BlockRenderLayer.TRANSLUCENT && !compiledChunkIn.isLayerEmpty(layer)) {
            bufferBuilderIn.sortVertexData(x, y, z);
            compiledChunkIn.setState(bufferBuilderIn.getVertexState());
        }
        bufferBuilderIn.finishDrawing();
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
        this.world = null;
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

    protected ChunkCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract) {
        return new ChunkCache(world, from, to, subtract);
    }

    public BlockPos getBlockPosOffset16(EnumFacing facing) {
        return this.mapEnumFacing[facing.ordinal()];
    }

    public World getWorld() {
        return this.world;
    }
}
