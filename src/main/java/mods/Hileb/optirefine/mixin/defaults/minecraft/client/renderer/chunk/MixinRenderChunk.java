package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.SignatureFix;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.MinecraftForgeClient;
import net.optifine.CustomBlockLayers;
import net.optifine.override.ChunkCacheOF;
import net.optifine.reflect.Reflector;
import net.optifine.render.AabbFrame;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.BitSet;

@Mixin(RenderChunk.class)
public abstract class MixinRenderChunk {
    @Unique @Public
    private static final BlockRenderLayer[] ENUM_WORLD_BLOCK_LAYERS = BlockRenderLayer.values();
    @Unique
    private final BlockRenderLayer[] blockLayersSingle = new BlockRenderLayer[1];
    @Unique
    private final boolean isMipmaps = Config.isMipmaps();
    @Unique
    private final boolean fixBlockLayer = !Reflector.BetterFoliageClient.exists();
    @Unique
    private boolean playerUpdate = false;
    @Unique
    public int regionX;
    @Unique
    public int regionZ;
    @Unique
    private final RenderChunk[] renderChunksOfset16 = new RenderChunk[6];
    @Unique
    private boolean renderChunksOffset16Updated = false;
    @Unique
    private Chunk chunk;
    @Unique
    private RenderChunk[] renderChunkNeighbours = new RenderChunk[EnumFacing.VALUES.length];
    @Unique
    private RenderChunk[] renderChunkNeighboursValid = new RenderChunk[EnumFacing.VALUES.length];
    @Unique
    private boolean renderChunkNeighboursUpated = false;
    @Unique
    private Object renderInfo_RenderGlobal_ContainerLocalRenderInformation = new_RenderGlobal_ContainerLocalRenderInformation(AccessibleOperation.Construction.construction(),(RenderChunk)(Object)this, null, 0);
    @Unique
    public AabbFrame boundingBoxParent;

    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation (Lnet/minecraft/client/renderer/RenderGlobal;Lnet/minecraft/client/renderer/chunk/RenderChunk;Lnet/minecraft/util/EnumFacing;I)V")
    private static native Object new_RenderGlobal_ContainerLocalRenderInformation(AccessibleOperation.Construction construction, RenderChunk p_i46248_2, @Nullable EnumFacing p_i46248_3, int p_i46248_4);


    @Redirect(method = "setPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing;values()[Lnet/minecraft/util/EnumFacing;"))
    public EnumFacing[] setPosition$EnumValues(){
        return EnumFacing.VALUES;
    }

    @Shadow
    public abstract BlockPos getBlockPosOffset16(EnumFacing facing);

    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk renderChunkNeighboursUpated Z")
    private static native void RenderChunk_renderChunkNeighboursUpated_set(RenderChunk renderChunk, boolean val);

    @WrapOperation(method = "setPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;initModelviewMatrix()V"))
    public void beforeInitModelviewMatrix(RenderChunk instance, Operation<Void> original, @Local(argsOnly = true, ordinal = 0) int x, @Local(argsOnly = true, ordinal = 2) int z){
        int bits = 8;
        this.regionX = x >> bits << bits;
        this.regionZ = z >> bits << bits;
        this.renderChunksOffset16Updated = false;
        this.renderChunkNeighboursUpated = false;

        for (RenderChunk neighbour : this.renderChunkNeighbours) {
            if (neighbour != null) {
                RenderChunk_renderChunkNeighboursUpated_set(neighbour, false);
            }
        }

        this.chunk = null;
        this.boundingBoxParent = null;
        original.call(instance);
    }

    @Shadow
    private native void preRenderBlocks(BufferBuilder bufferBuilderIn, BlockPos pos);

    @Shadow
    private native void postRenderBlocks(BlockRenderLayer layer, float x, float y, float z, BufferBuilder bufferBuilderIn, CompiledChunk compiledChunkIn);

    @WrapMethod(method = "resortTransparency")
    public void resortTransparency(float x, float y, float z, ChunkCompileTaskGenerator generator, Operation<Void> original) {
        CompiledChunk compiledchunk = generator.getCompiledChunk();
        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(BlockRenderLayer.TRANSLUCENT)) {
            BufferBuilder bufferTranslucent = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT);
            this.preRenderBlocks(bufferTranslucent, this.position);
            bufferTranslucent.setVertexState(compiledchunk.getState());
            this.postRenderBlocks(BlockRenderLayer.TRANSLUCENT, x, y, z, bufferTranslucent, compiledchunk);
        }
    }

    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/BlockRenderLayer;values()[Lnet/minecraft/util/BlockRenderLayer;"))
    public BlockRenderLayer[] rebuildChunk$BlockRenderLayer$values(){
        return ENUM_WORLD_BLOCK_LAYERS;
    }

    @WrapOperation(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RegionRenderCacheBuilder;getWorldRendererByLayerId(I)Lnet/minecraft/client/renderer/BufferBuilder;"))
    public BufferBuilder processPreBlockRender(RegionRenderCacheBuilder instance, int id, Operation<BufferBuilder> original, @Local IBlockState iblockstate, @Local BlockPos.MutableBlockPos blockpos$mutableblockpos, @Local(argsOnly = true) ChunkCompileTaskGenerator generator,
                                               @Share(namespace = "optifine", value = "renderEnv")LocalRef<RenderEnv> renderEnvLocalRef){
        BufferBuilder bufferBuilder = original.call(instance, id);
        RenderEnv renderEnv = BufferBuilder_getRenderEnv(bufferBuilder, iblockstate, blockpos$mutableblockpos);
        renderEnv.setRegionRenderCacheBuilder(generator.getRegionRenderCacheBuilder());
        renderEnvLocalRef.set(renderEnv);
        return bufferBuilder;
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder getRenderEnv (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)Lnet/optifine/render/RenderEnv;")
    private static native RenderEnv BufferBuilder_getRenderEnv(BufferBuilder builder, IBlockState blockStateIn, BlockPos blockPosIn);

    @WrapOperation(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderBlock(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z"))
    public boolean rebuildChunk$renderBlock(BlockRendererDispatcher instance, IBlockState enumblockrendertype, BlockPos crashreport, IBlockAccess crashreportcategory, BufferBuilder throwable, Operation<Boolean> original, @Share(namespace = "optifine", value = "renderEnv")LocalRef<RenderEnv> renderEnvLocalRef, @Local BlockRenderLayer blockRenderLayer, @Local boolean[] aboolean, @Local(argsOnly = true) ChunkCompileTaskGenerator generator, @Local CompiledChunk compiledchunk){
        boolean b;
        aboolean[blockRenderLayer.ordinal()] = b = original.call(instance, enumblockrendertype, crashreport, crashreportcategory, throwable);
        RenderEnv renderEnv = renderEnvLocalRef.get();
        if (renderEnv != null && renderEnv.isOverlaysRendered()) {
            this.optiRefine$postRenderOverlays(generator.getRegionRenderCacheBuilder(), compiledchunk, aboolean);
            renderEnv.setOverlaysRendered(false);
        }
        return b;
    }

    @WrapOperation(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;postRenderBlocks(Lnet/minecraft/util/BlockRenderLayer;FFFLnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/client/renderer/chunk/CompiledChunk;)V"))
    public void wrappostRenderBlocks(RenderChunk instance, BlockRenderLayer layer, float x, float y, float z, BufferBuilder bufferBuilderIn, CompiledChunk compiledChunkIn, Operation<Void> original, @Local(argsOnly = true) ChunkCompileTaskGenerator generator){
        if (Config.isShaders()) {
            SVertexBuilder.calcNormalChunkLayer(generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(layer));
        }

        BufferBuilder bufferBuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(layer);
        original.call(instance, layer, x, y, z, bufferBuilder, compiledChunkIn);
        if (BufferBuilder_animatedSprites_get(bufferBuilder) != null) {
            CompiledChunk_setAnimatedSprites(compiledChunkIn, layer, (BitSet)BufferBuilder_animatedSprites_get(bufferBuilder).clone());
        }
    }

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.BufferBuilder animatedSprites Ljava.util.BitSet;")
    private static native BitSet BufferBuilder_animatedSprites_get(BufferBuilder builder);

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.CompiledChunk setAnimatedSprites (Lnet.minecraft.util.BlockRenderLayer;Ljava.util.BitSet;)V")
    private static native void CompiledChunk_setAnimatedSprites(CompiledChunk compiledChunk, BlockRenderLayer layer, BitSet animatedSprites);

    @WrapOperation(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/CompiledChunk;isLayerStarted(Lnet/minecraft/util/BlockRenderLayer;)Z", ordinal = 1))
    public boolean ifisNotLayerStartedWeReloadAnimi(CompiledChunk instance, BlockRenderLayer layer, Operation<Boolean> original){
        if (original.call(instance, layer)) {
            return true;
        } else {
            CompiledChunk_setAnimatedSprites(instance, layer, null);
            return false;
        }
    }


    @WrapMethod(method = "rebuildWorldView")
    private void $rebuildWorldView(Operation<Void> original) {
    }

    @WrapOperation(method = "preRenderBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;setTranslation(DDD)V"))
    private void preRenderBlocks$setTranslationRegion(BufferBuilder instance, double x, double y, double z, Operation<Void> original, @Local(argsOnly = true) BlockPos pos){
        if (Config.isRenderRegions()) {
            int bits = 8;
            int dx = this.regionX;
            int dy = pos.getY() >> bits << bits;
            int dz =  this.regionZ;
            original.call(instance, (double)-dx, (double)-dy, (double)-dz);
        } else {
            original.call(instance, x, y, z);
        }
    }

    @Inject(method = "needsUpdate", at = @At("TAIL"))
    public void postNeedsUpdate(CallbackInfoReturnable<Boolean> cir){
        if (this.isWorldPlayerUpdate()) {
            this.playerUpdate = true;
        }
    }

    @Inject(method = "clearNeedsUpdate", at = @At("TAIL"))
    public void post$clearNeedsUpdate(CallbackInfo ci){
        this.playerUpdate = false;
    }

    @Unique
    private boolean isWorldPlayerUpdate() {
        if (this.world instanceof WorldClient worldClient) {
            return WorldClient_isPlayerUpdate(worldClient);
        } else {
            return false;
        }
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.multiplayer.WorldClient isPlayerUpdate ()Z")
    private static native boolean WorldClient_isPlayerUpdate(WorldClient world);

    @Unique
    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }

    @Unique
    private BlockRenderLayer optiRefine$fixBlockLayer(IBlockState blockState, BlockRenderLayer layer) {
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

    @Unique
    private void optiRefine$postRenderOverlays(RegionRenderCacheBuilder regionRenderCacheBuilder, CompiledChunk compiledChunk, boolean[] layerFlags) {
        this.optiRefine$postRenderOverlay(BlockRenderLayer.CUTOUT, regionRenderCacheBuilder, compiledChunk, layerFlags);
        this.optiRefine$postRenderOverlay(BlockRenderLayer.CUTOUT_MIPPED, regionRenderCacheBuilder, compiledChunk, layerFlags);
        this.optiRefine$postRenderOverlay(BlockRenderLayer.TRANSLUCENT, regionRenderCacheBuilder, compiledChunk, layerFlags);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder isDrawing ()Z")
    private static native boolean BufferBuilder_isDrawing(BufferBuilder builder);

    @Unique
    private void optiRefine$postRenderOverlay(BlockRenderLayer layer, RegionRenderCacheBuilder regionRenderCacheBuilder, CompiledChunk compiledchunk, boolean[] layerFlags) {
        BufferBuilder bufferOverlay = regionRenderCacheBuilder.getWorldRendererByLayer(layer);
        if (BufferBuilder_isDrawing(bufferOverlay)) {
            compiledchunk.setLayerStarted(layer);
            layerFlags[layer.ordinal()] = true;
        }
    }

    //@Deprecated worldView
    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ChunkCache;isEmpty()Z"))
    public boolean ifWorldViewEmpty(ChunkCache instance, @Share(namespace = "optifine", value = "chunkCacheOF") LocalRef<IBlockAccess> chunkCacheOF){
        if (this.isChunkRegionEmpty()) {
            return true;
        } else {
            ChunkCacheOF chunkCacheOF_ = optiRefine$makeChunkCacheOF(this.position);
            chunkCacheOF_.renderStart();
            chunkCacheOF.set(chunkCacheOF_);
            return false;
        }
    }

    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ChunkCache;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState worldView$getBlockState(ChunkCache instance, BlockPos i, @Share(namespace = "optifine", value = "chunkCacheOF") LocalRef<IBlockAccess> chunkCacheOF){
        return chunkCacheOF.get().getBlockState(i);
    }

    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ChunkCache;getTileEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/chunk/Chunk$EnumCreateEntityType;)Lnet/minecraft/tileentity/TileEntity;"))
    public TileEntity worldView$getTileEntity(ChunkCache instance, BlockPos pos, Chunk.EnumCreateEntityType createType, @Share(namespace = "optifine", value = "chunkCacheOF") LocalRef<IBlockAccess> chunkCacheOF){
        return ((ChunkCacheOF)chunkCacheOF.get()).getTileEntity(pos, createType);
    }

    @WrapOperation(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderBlock(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z"))
    public boolean useOFCacheForRender(BlockRendererDispatcher instance, IBlockState enumblockrendertype, BlockPos crashreport, IBlockAccess crashreportcategory, BufferBuilder throwable, Operation<Boolean> original, @Share(namespace = "optifine", value = "chunkCacheOF") LocalRef<IBlockAccess> chunkCacheOF){
        return original.call(instance, enumblockrendertype, crashreport, chunkCacheOF.get(), throwable);
    }

    @WrapOperation(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/CompiledChunk;setVisibility(Lnet/minecraft/client/renderer/chunk/SetVisibility;)V"))
    public void renderFinished(CompiledChunk instance, SetVisibility visibility, Operation<Void> original, @Share(namespace = "optifine", value = "chunkCacheOF") LocalRef<IBlockAccess> chunkCacheOF){
        ((ChunkCacheOF)chunkCacheOF.get()).renderFinish();
        original.call(instance, visibility);
    }

    @Unique
    private ChunkCacheOF optiRefine$makeChunkCacheOF(BlockPos posIn) {
        BlockPos posFrom = posIn.add(-1, -1, -1);
        BlockPos posTo = posIn.add(16, 16, 16);
        ChunkCache chunkCache = this.createRegionRenderCache(this.world, posFrom, posTo, 1);
        MinecraftForgeClient.onRebuildChunk(this.world, posIn, chunkCache);

        return new ChunkCacheOF(chunkCache, posFrom, posTo, 1);
    }

    @Shadow
    protected abstract ChunkCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract);

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.ViewFrustum getRenderChunk (Lnet.minecraft.util.math.BlockPos;)Lnet.minecraft.client.renderer.chunk.RenderChunk;")
    private static native RenderChunk ViewFrustum_getRenderChunk(ViewFrustum viewFrustum, BlockPos b);
    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public RenderChunk getRenderChunkOffset16(ViewFrustum viewFrustum, EnumFacing facing) {
        if (!this.renderChunksOffset16Updated) {
            for (int i = 0; i < EnumFacing.VALUES.length; i++) {
                EnumFacing ef = EnumFacing.VALUES[i];
                BlockPos posOffset16 = this.getBlockPosOffset16(ef);
                this.renderChunksOfset16[i] = ViewFrustum_getRenderChunk(viewFrustum, posOffset16);
            }

            this.renderChunksOffset16Updated = true;
        }

        return this.renderChunksOfset16[facing.ordinal()];
    }

    @Shadow @Final
    private BlockPos.MutableBlockPos position;

    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public Chunk getChunk() {
        return this.getChunk(this.position);
    }

    @Shadow
    private World world;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean isChunkRegionEmpty() {
        return this.optiRefine$isChunkRegionEmpty(this.position);
    }

    @Unique
    private boolean optiRefine$isChunkRegionEmpty(BlockPos posIn) {
        int yStart = posIn.getY();
        int yEnd = yStart + 15;
        return this.getChunk(posIn).isEmptyBetween(yStart, yEnd);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void setRenderChunkNeighbour(EnumFacing facing, RenderChunk neighbour) {
        this.renderChunkNeighbours[facing.ordinal()] = neighbour;
        this.renderChunkNeighboursValid[facing.ordinal()] = neighbour;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public RenderChunk getRenderChunkNeighbour(EnumFacing facing) {
        if (!this.renderChunkNeighboursUpated) {
            this.optiRefine$updateRenderChunkNeighboursValid();
        }

        return this.renderChunkNeighboursValid[facing.ordinal()];
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @SignatureFix("()Lnet.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation;")
    public Object getRenderInfo() {
        return this.renderInfo_RenderGlobal_ContainerLocalRenderInformation;
    }

    @Shadow
    public abstract BlockPos getPosition();

    @Shadow @Final private RenderGlobal renderGlobal;

    @Shadow public abstract void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator);

    @Unique
    private void optiRefine$updateRenderChunkNeighboursValid() {
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

    @Shadow
    public AxisAlignedBB boundingBox;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public boolean isBoundingBoxInFrustum(ICamera camera, int frameCount) {
        return this.getBoundingBoxParent().isBoundingBoxInFrustumFully(camera, frameCount) || camera.isBoundingBoxInFrustum(this.boundingBox);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal getRenderChunk (Lnet.minecraft.util.math.BlockPos;)Lnet.minecraft.client.renderer.chunk.RenderChunk;")
    private static native RenderChunk RenderGrobal_getRenderChunk(RenderGlobal renderGlobal, BlockPos blockPos);

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.RenderChunk getBoundingBoxParent ()Lnet.optifine.render.AabbFrame;")
    private static native AabbFrame RenderChunk_getBoundingBoxParent(RenderChunk renderChunk);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
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
                AabbFrame bbp = RenderChunk_getBoundingBoxParent(RenderGrobal_getRenderChunk(this.renderGlobal, new BlockPos(xp, yp, zp)));
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

}

/*
+++ net/minecraft/client/renderer/chunk/RenderChunk.java	Tue Aug 19 14:59:58 2025
@@ -1,40 +1,59 @@
 package net.minecraft.client.renderer.chunk;

 import com.google.common.collect.Sets;
 import java.nio.FloatBuffer;
+import java.util.BitSet;
 import java.util.HashSet;
 import java.util.Set;
 import java.util.concurrent.locks.ReentrantLock;
 import javax.annotation.Nullable;
 import net.minecraft.block.Block;
+import net.minecraft.block.BlockCactus;
+import net.minecraft.block.BlockRedstoneWire;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
+import net.minecraft.client.multiplayer.WorldClient;
 import net.minecraft.client.renderer.BlockRendererDispatcher;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GLAllocation;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
+import net.minecraft.client.renderer.RegionRenderCacheBuilder;
 import net.minecraft.client.renderer.RenderGlobal;
+import net.minecraft.client.renderer.ViewFrustum;
+import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator.Status;
+import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator.Type;
+import net.minecraft.client.renderer.culling.ICamera;
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
+import net.minecraft.util.math.BlockPos.MutableBlockPos;
 import net.minecraft.world.ChunkCache;
 import net.minecraft.world.World;
 import net.minecraft.world.chunk.Chunk;
+import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
+import net.optifine.BlockPosM;
+import net.optifine.CustomBlockLayers;
+import net.optifine.override.ChunkCacheOF;
+import net.optifine.reflect.Reflector;
+import net.optifine.reflect.ReflectorForge;
+import net.optifine.render.AabbFrame;
+import net.optifine.render.RenderEnv;
+import net.optifine.shaders.SVertexBuilder;

 public class RenderChunk {
-   private World world;
+   private final World world;
    private final RenderGlobal renderGlobal;
    public static int renderChunksUpdated;
    public CompiledChunk compiledChunk = CompiledChunk.DUMMY;
    private final ReentrantLock lockCompileTask = new ReentrantLock();
    private final ReentrantLock lockCompiledChunk = new ReentrantLock();
    private ChunkCompileTaskGenerator compileTask;
@@ -42,20 +61,34 @@
    private final int index;
    private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
    private final VertexBuffer[] vertexBuffers = new VertexBuffer[BlockRenderLayer.values().length];
    public AxisAlignedBB boundingBox;
    private int frameIndex = -1;
    private boolean needsUpdate = true;
-   private final BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos(-1, -1, -1);
-   private final BlockPos.MutableBlockPos[] mapEnumFacing = new BlockPos.MutableBlockPos[6];
+   private final MutableBlockPos position = new MutableBlockPos(-1, -1, -1);
+   private final MutableBlockPos[] mapEnumFacing = new MutableBlockPos[6];
    private boolean needsImmediateUpdate;
-   private ChunkCache worldView;
+   public static final BlockRenderLayer[] ENUM_WORLD_BLOCK_LAYERS = BlockRenderLayer.values();
+   private final BlockRenderLayer[] blockLayersSingle = new BlockRenderLayer[1];
+   private final boolean isMipmaps = Config.isMipmaps();
+   private final boolean fixBlockLayer = !Reflector.BetterFoliageClient.exists();
+   private boolean playerUpdate = false;
+   public int regionX;
+   public int regionZ;
+   private final RenderChunk[] renderChunksOfset16 = new RenderChunk[6];
+   private boolean renderChunksOffset16Updated = false;
+   private Chunk chunk;
+   private RenderChunk[] renderChunkNeighbours = new RenderChunk[EnumFacing.VALUES.length];
+   private RenderChunk[] renderChunkNeighboursValid = new RenderChunk[EnumFacing.VALUES.length];
+   private boolean renderChunkNeighboursUpated = false;
+   private RenderGlobal.ContainerLocalRenderInformation renderInfo = new RenderGlobal.ContainerLocalRenderInformation(this, null, 0);
+   public AabbFrame boundingBoxParent;

    public RenderChunk(World var1, RenderGlobal var2, int var3) {
       for (int var4 = 0; var4 < this.mapEnumFacing.length; var4++) {
-         this.mapEnumFacing[var4] = new BlockPos.MutableBlockPos();
+         this.mapEnumFacing[var4] = new MutableBlockPos();
       }

       this.world = var1;
       this.renderGlobal = var2;
       this.index = var3;
       if (OpenGlHelper.useVbo()) {
@@ -79,122 +112,186 @@
    }

    public void setPosition(int var1, int var2, int var3) {
       if (var1 != this.position.getX() || var2 != this.position.getY() || var3 != this.position.getZ()) {
          this.stopCompileTask();
          this.position.setPos(var1, var2, var3);
+         byte var4 = 8;
+         this.regionX = var1 >> var4 << var4;
+         this.regionZ = var3 >> var4 << var4;
          this.boundingBox = new AxisAlignedBB(var1, var2, var3, var1 + 16, var2 + 16, var3 + 16);

-         for (EnumFacing var7 : EnumFacing.values()) {
-            this.mapEnumFacing[var7.ordinal()].setPos(this.position).move(var7, 16);
+         for (EnumFacing var8 : EnumFacing.VALUES) {
+            this.mapEnumFacing[var8.ordinal()].setPos(this.position).move(var8, 16);
          }

+         this.renderChunksOffset16Updated = false;
+         this.renderChunkNeighboursUpated = false;
+
+         for (int var9 = 0; var9 < this.renderChunkNeighbours.length; var9++) {
+            RenderChunk var10 = this.renderChunkNeighbours[var9];
+            if (var10 != null) {
+               var10.renderChunkNeighboursUpated = false;
+            }
+         }
+
+         this.chunk = null;
+         this.boundingBoxParent = null;
          this.initModelviewMatrix();
       }
    }

    public void resortTransparency(float var1, float var2, float var3, ChunkCompileTaskGenerator var4) {
       CompiledChunk var5 = var4.getCompiledChunk();
       if (var5.getState() != null && !var5.isLayerEmpty(BlockRenderLayer.TRANSLUCENT)) {
-         this.preRenderBlocks(var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT), this.position);
-         var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT).setVertexState(var5.getState());
-         this.postRenderBlocks(
-            BlockRenderLayer.TRANSLUCENT, var1, var2, var3, var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT), var5
-         );
+         BufferBuilder var6 = var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT);
+         this.preRenderBlocks(var6, this.position);
+         var6.setVertexState(var5.getState());
+         this.postRenderBlocks(BlockRenderLayer.TRANSLUCENT, var1, var2, var3, var6, var5);
       }
    }

    public void rebuildChunk(float var1, float var2, float var3, ChunkCompileTaskGenerator var4) {
       CompiledChunk var5 = new CompiledChunk();
       boolean var6 = true;
-      BlockPos.MutableBlockPos var7 = this.position;
+      BlockPos var7 = new BlockPos(this.position);
       BlockPos var8 = var7.add(15, 15, 15);
       var4.getLock().lock();

       try {
-         if (var4.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
+         if (var4.getStatus() != Status.COMPILING) {
             return;
          }

          var4.setCompiledChunk(var5);
       } finally {
          var4.getLock().unlock();
       }

       VisGraph var9 = new VisGraph();
       HashSet var10 = Sets.newHashSet();
-      if (!this.worldView.isEmpty()) {
+      if (!this.isChunkRegionEmpty(var7)) {
          renderChunksUpdated++;
-         boolean[] var11 = new boolean[BlockRenderLayer.values().length];
-         BlockRendererDispatcher var12 = Minecraft.getMinecraft().getBlockRendererDispatcher();
-
-         for (BlockPos.MutableBlockPos var14 : BlockPos.getAllInBoxMutable(var7, var8)) {
-            IBlockState var15 = this.worldView.getBlockState(var14);
-            Block var16 = var15.getBlock();
-            if (var15.isOpaqueCube()) {
-               var9.setOpaqueCube(var14);
+         ChunkCacheOF var11 = this.makeChunkCacheOF(var7);
+         var11.renderStart();
+         boolean[] var12 = new boolean[ENUM_WORLD_BLOCK_LAYERS.length];
+         BlockRendererDispatcher var13 = Minecraft.getMinecraft().getBlockRendererDispatcher();
+         boolean var14 = Reflector.ForgeBlock_canRenderInLayer.exists();
+         boolean var15 = Reflector.ForgeHooksClient_setRenderLayer.exists();
+
+         for (BlockPosM var17 : BlockPosM.getAllInBoxMutable(var7, var8)) {
+            IBlockState var18 = var11.getBlockState(var17);
+            Block var19 = var18.getBlock();
+            if (var18.p()) {
+               var9.setOpaqueCube(var17);
             }

-            if (var16.hasTileEntity()) {
-               TileEntity var17 = this.worldView.getTileEntity(var14, Chunk.EnumCreateEntityType.CHECK);
-               if (var17 != null) {
-                  TileEntitySpecialRenderer var18 = TileEntityRendererDispatcher.instance.getRenderer(var17);
-                  if (var18 != null) {
-                     var5.addTileEntity(var17);
-                     if (var18.isGlobalRenderer(var17)) {
-                        var10.add(var17);
+            if (ReflectorForge.blockHasTileEntity(var18)) {
+               TileEntity var20 = var11.getTileEntity(var17, EnumCreateEntityType.CHECK);
+               if (var20 != null) {
+                  TileEntitySpecialRenderer var21 = TileEntityRendererDispatcher.instance.getRenderer(var20);
+                  if (var21 != null) {
+                     if (var21.isGlobalRenderer(var20)) {
+                        var10.add(var20);
+                     } else {
+                        var5.addTileEntity(var20);
                      }
                   }
                }
             }

-            BlockRenderLayer var33 = var16.getRenderLayer();
-            int var34 = var33.ordinal();
-            if (var16.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE) {
-               BufferBuilder var19 = var4.getRegionRenderCacheBuilder().getWorldRendererByLayerId(var34);
-               if (!var5.isLayerStarted(var33)) {
-                  var5.setLayerStarted(var33);
-                  this.preRenderBlocks(var19, var7);
+            BlockRenderLayer[] var38;
+            if (var14) {
+               var38 = ENUM_WORLD_BLOCK_LAYERS;
+            } else {
+               var38 = this.blockLayersSingle;
+               var38[0] = var19.getRenderLayer();
+            }
+
+            for (int var40 = 0; var40 < var38.length; var40++) {
+               BlockRenderLayer var22 = var38[var40];
+               if (var14) {
+                  boolean var23 = Reflector.callBoolean(var19, Reflector.ForgeBlock_canRenderInLayer, new Object[]{var18, var22});
+                  if (!var23) {
+                     continue;
+                  }
+               }
+
+               if (var15) {
+                  Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[]{var22});
                }

-               var11[var34] |= var12.renderBlock(var15, var14, this.worldView, var19);
+               var22 = this.fixBlockLayer(var18, var22);
+               int var43 = var22.ordinal();
+               if (var19.getDefaultState().i() != EnumBlockRenderType.INVISIBLE) {
+                  BufferBuilder var24 = var4.getRegionRenderCacheBuilder().getWorldRendererByLayerId(var43);
+                  var24.setBlockLayer(var22);
+                  RenderEnv var25 = var24.getRenderEnv(var18, var17);
+                  var25.setRegionRenderCacheBuilder(var4.getRegionRenderCacheBuilder());
+                  if (!var5.isLayerStarted(var22)) {
+                     var5.setLayerStarted(var22);
+                     this.preRenderBlocks(var24, var7);
+                  }
+
+                  var12[var43] |= var13.renderBlock(var18, var17, var11, var24);
+                  if (var25.isOverlaysRendered()) {
+                     this.postRenderOverlays(var4.getRegionRenderCacheBuilder(), var5, var12);
+                     var25.setOverlaysRendered(false);
+                  }
+               }
+            }
+
+            if (var15) {
+               Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[]{null});
             }
          }

-         for (BlockRenderLayer var32 : BlockRenderLayer.values()) {
-            if (var11[var32.ordinal()]) {
-               var5.setLayerUsed(var32);
+         for (BlockRenderLayer var39 : ENUM_WORLD_BLOCK_LAYERS) {
+            if (var12[var39.ordinal()]) {
+               var5.setLayerUsed(var39);
             }

-            if (var5.isLayerStarted(var32)) {
-               this.postRenderBlocks(var32, var1, var2, var3, var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(var32), var5);
+            if (var5.isLayerStarted(var39)) {
+               if (Config.isShaders()) {
+                  SVertexBuilder.calcNormalChunkLayer(var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(var39));
+               }
+
+               BufferBuilder var41 = var4.getRegionRenderCacheBuilder().getWorldRendererByLayer(var39);
+               this.postRenderBlocks(var39, var1, var2, var3, var41, var5);
+               if (var41.animatedSprites != null) {
+                  var5.setAnimatedSprites(var39, (BitSet)var41.animatedSprites.clone());
+               }
+            } else {
+               var5.setAnimatedSprites(var39, null);
             }
          }
+
+         var11.renderFinish();
       }

       var5.setVisibility(var9.computeVisibility());
       this.lockCompileTask.lock();

       try {
-         HashSet var27 = Sets.newHashSet(var10);
-         HashSet var28 = Sets.newHashSet(this.setTileEntities);
-         var27.removeAll(this.setTileEntities);
-         var28.removeAll(var10);
+         HashSet var33 = Sets.newHashSet(var10);
+         HashSet var34 = Sets.newHashSet(this.setTileEntities);
+         var33.removeAll(this.setTileEntities);
+         var34.removeAll(var10);
          this.setTileEntities.clear();
          this.setTileEntities.addAll(var10);
-         this.renderGlobal.updateTileEntities(var28, var27);
+         this.renderGlobal.updateTileEntities(var34, var33);
       } finally {
          this.lockCompileTask.unlock();
       }
    }

    protected void finishCompileTask() {
       this.lockCompileTask.lock();

       try {
-         if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
+         if (this.compileTask != null && this.compileTask.getStatus() != Status.DONE) {
             this.compileTask.finish();
             this.compileTask = null;
          }
       } finally {
          this.lockCompileTask.unlock();
       }
@@ -207,63 +304,73 @@
    public ChunkCompileTaskGenerator makeCompileTaskChunk() {
       this.lockCompileTask.lock();

       ChunkCompileTaskGenerator var1;
       try {
          this.finishCompileTask();
-         this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK, this.getDistanceSq());
+         this.compileTask = new ChunkCompileTaskGenerator(this, Type.REBUILD_CHUNK, this.getDistanceSq());
          this.rebuildWorldView();
          var1 = this.compileTask;
       } finally {
          this.lockCompileTask.unlock();
       }

       return var1;
    }

    private void rebuildWorldView() {
       boolean var1 = true;
-      this.worldView = new ChunkCache(this.world, this.position.add(-1, -1, -1), this.position.add(16, 16, 16), 1);
    }

    @Nullable
    public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
       this.lockCompileTask.lock();

-      Object var1;
+      ChunkCompileTaskGenerator var2;
       try {
-         if (this.compileTask == null || this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
-            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
-               this.compileTask.finish();
-               this.compileTask = null;
-            }
+         if (this.compileTask != null && this.compileTask.getStatus() == Status.PENDING) {
+            return null;
+         }

-            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY, this.getDistanceSq());
-            this.compileTask.setCompiledChunk(this.compiledChunk);
-            return this.compileTask;
+         if (this.compileTask != null && this.compileTask.getStatus() != Status.DONE) {
+            this.compileTask.finish();
+            this.compileTask = null;
          }

-         var1 = null;
+         this.compileTask = new ChunkCompileTaskGenerator(this, Type.RESORT_TRANSPARENCY, this.getDistanceSq());
+         this.compileTask.setCompiledChunk(this.compiledChunk);
+         ChunkCompileTaskGenerator var1 = this.compileTask;
+         var2 = var1;
       } finally {
          this.lockCompileTask.unlock();
       }

-      return (ChunkCompileTaskGenerator)var1;
+      return var2;
    }

    protected double getDistanceSq() {
       EntityPlayerSP var1 = Minecraft.getMinecraft().player;
       double var2 = this.boundingBox.minX + 8.0 - var1.posX;
       double var4 = this.boundingBox.minY + 8.0 - var1.posY;
       double var6 = this.boundingBox.minZ + 8.0 - var1.posZ;
       return var2 * var2 + var4 * var4 + var6 * var6;
    }

    private void preRenderBlocks(BufferBuilder var1, BlockPos var2) {
       var1.begin(7, DefaultVertexFormats.BLOCK);
-      var1.setTranslation(-var2.getX(), -var2.getY(), -var2.getZ());
+      if (Config.isRenderRegions()) {
+         byte var3 = 8;
+         int var4 = var2.getX() >> var3 << var3;
+         int var5 = var2.getY() >> var3 << var3;
+         int var6 = var2.getZ() >> var3 << var3;
+         var4 = this.regionX;
+         var6 = this.regionZ;
+         var1.setTranslation(-var4, -var5, -var6);
+      } else {
+         var1.setTranslation(-var2.getX(), -var2.getY(), -var2.getZ());
+      }
    }

    private void postRenderBlocks(BlockRenderLayer var1, float var2, float var3, float var4, BufferBuilder var5, CompiledChunk var6) {
       if (var1 == BlockRenderLayer.TRANSLUCENT && !var6.isLayerEmpty(var1)) {
          var5.sortVertexData(var2, var3, var4);
          var6.setState(var5.getVertexState());
@@ -305,13 +412,12 @@
       this.finishCompileTask();
       this.compiledChunk = CompiledChunk.DUMMY;
    }

    public void deleteGlResources() {
       this.stopCompileTask();
-      this.world = null;

       for (int var1 = 0; var1 < BlockRenderLayer.values().length; var1++) {
          if (this.vertexBuffers[var1] != null) {
             this.vertexBuffers[var1].deleteGlBuffers();
          }
       }
@@ -325,17 +431,21 @@
       if (this.needsUpdate) {
          var1 |= this.needsImmediateUpdate;
       }

       this.needsUpdate = true;
       this.needsImmediateUpdate = var1;
+      if (this.isWorldPlayerUpdate()) {
+         this.playerUpdate = true;
+      }
    }

    public void clearNeedsUpdate() {
       this.needsUpdate = false;
       this.needsImmediateUpdate = false;
+      this.playerUpdate = false;
    }

    public boolean needsUpdate() {
       return this.needsUpdate;
    }

@@ -346,8 +456,186 @@
    public BlockPos getBlockPosOffset16(EnumFacing var1) {
       return this.mapEnumFacing[var1.ordinal()];
    }

    public World getWorld() {
       return this.world;
+   }
+
+   private boolean isWorldPlayerUpdate() {
+      if (this.world instanceof WorldClient) {
+         WorldClient var1 = (WorldClient)this.world;
+         return var1.isPlayerUpdate();
+      } else {
+         return false;
+      }
+   }
+
+   public boolean isPlayerUpdate() {
+      return this.playerUpdate;
+   }
+
+   private BlockRenderLayer fixBlockLayer(IBlockState var1, BlockRenderLayer var2) {
+      if (CustomBlockLayers.isActive()) {
+         BlockRenderLayer var3 = CustomBlockLayers.getRenderLayer(var1);
+         if (var3 != null) {
+            return var3;
+         }
+      }
+
+      if (!this.fixBlockLayer) {
+         return var2;
+      } else {
+         if (this.isMipmaps) {
+            if (var2 == BlockRenderLayer.CUTOUT) {
+               Block var4 = var1.getBlock();
+               if (var4 instanceof BlockRedstoneWire) {
+                  return var2;
+               }
+
+               if (var4 instanceof BlockCactus) {
+                  return var2;
+               }
+
+               return BlockRenderLayer.CUTOUT_MIPPED;
+            }
+         } else if (var2 == BlockRenderLayer.CUTOUT_MIPPED) {
+            return BlockRenderLayer.CUTOUT;
+         }
+
+         return var2;
+      }
+   }
+
+   private void postRenderOverlays(RegionRenderCacheBuilder var1, CompiledChunk var2, boolean[] var3) {
+      this.postRenderOverlay(BlockRenderLayer.CUTOUT, var1, var2, var3);
+      this.postRenderOverlay(BlockRenderLayer.CUTOUT_MIPPED, var1, var2, var3);
+      this.postRenderOverlay(BlockRenderLayer.TRANSLUCENT, var1, var2, var3);
+   }
+
+   private void postRenderOverlay(BlockRenderLayer var1, RegionRenderCacheBuilder var2, CompiledChunk var3, boolean[] var4) {
+      BufferBuilder var5 = var2.getWorldRendererByLayer(var1);
+      if (var5.isDrawing()) {
+         var3.setLayerStarted(var1);
+         var4[var1.ordinal()] = true;
+      }
+   }
+
+   private ChunkCacheOF makeChunkCacheOF(BlockPos var1) {
+      BlockPos var2 = var1.add(-1, -1, -1);
+      BlockPos var3 = var1.add(16, 16, 16);
+      ChunkCache var4 = this.createRegionRenderCache(this.world, var2, var3, 1);
+      if (Reflector.MinecraftForgeClient_onRebuildChunk.exists()) {
+         Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, new Object[]{this.world, var1, var4});
+      }
+
+      return new ChunkCacheOF(var4, var2, var3, 1);
+   }
+
+   public RenderChunk getRenderChunkOffset16(ViewFrustum var1, EnumFacing var2) {
+      if (!this.renderChunksOffset16Updated) {
+         for (int var3 = 0; var3 < EnumFacing.VALUES.length; var3++) {
+            EnumFacing var4 = EnumFacing.VALUES[var3];
+            BlockPos var5 = this.getBlockPosOffset16(var4);
+            this.renderChunksOfset16[var3] = var1.getRenderChunk(var5);
+         }
+
+         this.renderChunksOffset16Updated = true;
+      }
+
+      return this.renderChunksOfset16[var2.ordinal()];
+   }
+
+   public Chunk getChunk() {
+      return this.getChunk(this.position);
+   }
+
+   private Chunk getChunk(BlockPos var1) {
+      Chunk var2 = this.chunk;
+      if (var2 != null && var2.isLoaded()) {
+         return var2;
+      } else {
+         var2 = this.world.getChunk(var1);
+         this.chunk = var2;
+         return var2;
+      }
+   }
+
+   public boolean isChunkRegionEmpty() {
+      return this.isChunkRegionEmpty(this.position);
+   }
+
+   private boolean isChunkRegionEmpty(BlockPos var1) {
+      int var2 = var1.getY();
+      int var3 = var2 + 15;
+      return this.getChunk(var1).isEmptyBetween(var2, var3);
+   }
+
+   public void setRenderChunkNeighbour(EnumFacing var1, RenderChunk var2) {
+      this.renderChunkNeighbours[var1.ordinal()] = var2;
+      this.renderChunkNeighboursValid[var1.ordinal()] = var2;
+   }
+
+   public RenderChunk getRenderChunkNeighbour(EnumFacing var1) {
+      if (!this.renderChunkNeighboursUpated) {
+         this.updateRenderChunkNeighboursValid();
+      }
+
+      return this.renderChunkNeighboursValid[var1.ordinal()];
+   }
+
+   public RenderGlobal.ContainerLocalRenderInformation getRenderInfo() {
+      return this.renderInfo;
+   }
+
+   private void updateRenderChunkNeighboursValid() {
+      int var1 = this.getPosition().getX();
+      int var2 = this.getPosition().getZ();
+      int var3 = EnumFacing.NORTH.ordinal();
+      int var4 = EnumFacing.SOUTH.ordinal();
+      int var5 = EnumFacing.WEST.ordinal();
+      int var6 = EnumFacing.EAST.ordinal();
+      this.renderChunkNeighboursValid[var3] = this.renderChunkNeighbours[var3].getPosition().getZ() == var2 - 16 ? this.renderChunkNeighbours[var3] : null;
+      this.renderChunkNeighboursValid[var4] = this.renderChunkNeighbours[var4].getPosition().getZ() == var2 + 16 ? this.renderChunkNeighbours[var4] : null;
+      this.renderChunkNeighboursValid[var5] = this.renderChunkNeighbours[var5].getPosition().getX() == var1 - 16 ? this.renderChunkNeighbours[var5] : null;
+      this.renderChunkNeighboursValid[var6] = this.renderChunkNeighbours[var6].getPosition().getX() == var1 + 16 ? this.renderChunkNeighbours[var6] : null;
+      this.renderChunkNeighboursUpated = true;
+   }
+
+   public boolean isBoundingBoxInFrustum(ICamera var1, int var2) {
+      return this.getBoundingBoxParent().isBoundingBoxInFrustumFully(var1, var2) ? true : var1.isBoundingBoxInFrustum(this.boundingBox);
+   }
+
+   public AabbFrame getBoundingBoxParent() {
+      if (this.boundingBoxParent == null) {
+         BlockPos var1 = this.getPosition();
+         int var2 = var1.getX();
+         int var3 = var1.getY();
+         int var4 = var1.getZ();
+         byte var5 = 5;
+         int var6 = var2 >> var5 << var5;
+         int var7 = var3 >> var5 << var5;
+         int var8 = var4 >> var5 << var5;
+         if (var6 != var2 || var7 != var3 || var8 != var4) {
+            AabbFrame var9 = this.renderGlobal.getRenderChunk(new BlockPos(var6, var7, var8)).getBoundingBoxParent();
+            if (var9 != null && var9.minX == var6 && var9.minY == var7 && var9.minZ == var8) {
+               this.boundingBoxParent = var9;
+            }
+         }
+
+         if (this.boundingBoxParent == null) {
+            int var10 = 1 << var5;
+            this.boundingBoxParent = new AabbFrame(var6, var7, var8, var6 + var10, var7 + var10, var8 + var10);
+         }
+      }
+
+      return this.boundingBoxParent;
+   }
+
+   public String toString() {
+      return "pos: " + this.getPosition() + ", frameIndex: " + this.frameIndex;
+   }
+
+   protected ChunkCache createRegionRenderCache(World var1, BlockPos var2, BlockPos var3, int var4) {
+      return new ChunkCache(var1, var2, var3, var4);
    }
 }
 */
