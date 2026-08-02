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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 4
    @Public
    // [AUDIT-OK] OF-added static final (OF:70 public); @Public private static correct
    private static final BlockRenderLayer[] ENUM_WORLD_BLOCK_LAYERS = BlockRenderLayer.values();
    @Unique
    // [AUDIT-OK] OF-added fields blockLayersSingle/isMipmaps/fixBlockLayer (OF:71-73)
    private final BlockRenderLayer[] blockLayersSingle = new BlockRenderLayer[1];
    @Unique
    private final boolean isMipmaps = Config.isMipmaps();
    @Unique
    private final boolean fixBlockLayer = !Reflector.BetterFoliageClient.exists();
    @Unique
    // [AUDIT-OK] OF-added field (OF:74)
    private boolean playerUpdate = false;
    @Unique
    // [AUDIT-OK] OF-added public fields regionX/regionZ (OF:75-76)
    public int regionX;
    @Unique
    public int regionZ;
    @Unique
    // [AUDIT-OK] OF-added fields renderChunksOfset16/renderChunksOffset16Updated/chunk/renderChunkNeighbours/renderChunkNeighboursValid/renderChunkNeighboursUpated (OF:77-82)
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
    // [AUDIT-OK] OF-added field (OF:83, named renderInfo); init via NEW helper matches OF's field initializer
    // [AUDIT-OK] NEW AccessibleOperation + Construction sentinel pairing correct; 3-arg ctor (RenderChunk,EnumFacing,I) verified in deobf RenderGlobal:2533-2540, OF RenderGlobal:3086+, and cleanroom RenderGlobal.java.patch — private ctor/class widened by optirefine_at.cfg
    private Object renderInfo_RenderGlobal_ContainerLocalRenderInformation = new_RenderGlobal_ContainerLocalRenderInformation(AccessibleOperation.Construction.construction(), this.renderGlobal, (RenderChunk)(Object)this, null, 0);
    @Unique
    public AabbFrame boundingBoxParent;

    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation (Lnet/minecraft/client/renderer/RenderGlobal;Lnet/minecraft/client/renderer/chunk/RenderChunk;Lnet/minecraft/util/EnumFacing;I)V")
    private static native Object new_RenderGlobal_ContainerLocalRenderInformation(AccessibleOperation.Construction construction, RenderGlobal renderGlobal, RenderChunk p_i46248_2, @Nullable EnumFacing p_i46248_3, int p_i46248_4);


    @Redirect(method = "setPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing;values()[Lnet/minecraft/util/EnumFacing;"))
    // [AUDIT-OK] setPosition values() INVOKE unique; VALUES matches OF:121
    public EnumFacing[] setPosition$EnumValues(){
        return EnumFacing.VALUES;
    }

    @Shadow
    // [AUDIT-OK] baseline member getBlockPosOffset16 (deobf:430)
    public abstract BlockPos getBlockPosOffset16(EnumFacing facing);

    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk renderChunkNeighboursUpated Z")
    private static native void RenderChunk_renderChunkNeighboursUpated_set(RenderChunk renderChunk, boolean val);

    @WrapOperation(method = "setPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;initModelviewMatrix()V"))
    // [AUDIT-OK] initModelviewMatrix INVOKE unique in setPosition; region/neighbour/chunk/boundingBoxParent resets match OF:119-139
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
    // [AUDIT-OK] baseline private members preRenderBlocks/postRenderBlocks (deobf:325/331)
    private native void preRenderBlocks(BufferBuilder bufferBuilderIn, BlockPos pos);

    @Shadow
    private native void postRenderBlocks(BlockRenderLayer layer, float x, float y, float z, BufferBuilder bufferBuilderIn, CompiledChunk compiledChunkIn);

    @WrapMethod(method = "resortTransparency")
    // [AUDIT-OK] baseline (deobf:112); body matches OF:143-152
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
    // [AUDIT-OK] BlockRenderLayer.values() in rebuildChunk (2 sites incl. cleanroom canRenderInLayer loop) -> ENUM_WORLD_BLOCK_LAYERS, matches OF:204
    public BlockRenderLayer[] rebuildChunk$BlockRenderLayer$values(){
        return ENUM_WORLD_BLOCK_LAYERS;
    }

    @WrapOperation(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RegionRenderCacheBuilder;getWorldRendererByLayerId(I)Lnet/minecraft/client/renderer/BufferBuilder;"))
    // [AUDIT-OK] getWorldRendererByLayerId INVOKE; getRenderEnv + setRegionRenderCacheBuilder match OF:225-227
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
    // [AUDIT-OK] renderBlock INVOKE; overlays post-render matches OF:231-236
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
    // [AUDIT-OK] postRenderBlocks INVOKE; calcNormalChunkLayer + animatedSprites clone match OF:253-261
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
    // [AUDIT-OK] isLayerStarted ordinal 1 (post-loop); null-reset matches OF:264
    public boolean ifisNotLayerStartedWeReloadAnimi(CompiledChunk instance, BlockRenderLayer layer, Operation<Boolean> original){
        if (original.call(instance, layer)) {
            return true;
        } else {
            CompiledChunk_setAnimatedSprites(instance, layer, null);
            return false;
        }
    }


    @WrapMethod(method = "rebuildWorldView")
    // [AUDIT-OK] rebuildWorldView baseline (deobf:278); OF stubs it out (OF:320) — empty wrap equivalent
    private void $rebuildWorldView(Operation<Void> original) {
    }

    @WrapOperation(method = "preRenderBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;setTranslation(DDD)V"))
    // [AUDIT-OK] setTranslation wrap with region offsets matches OF preRenderBlocks
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
    // [AUDIT-ISSUE] hooks needsUpdate() TAIL, but OF sets playerUpdate inside setNeedsUpdate (OF:431-440); needsUpdate() is polled every frame so the flag goes true for ALL chunks during player-update ticks (false positives) — move the hook to setNeedsUpdate
    public void postNeedsUpdate(CallbackInfoReturnable<Boolean> cir){
        if (this.isWorldPlayerUpdate()) {
            this.playerUpdate = true;
        }
    }

    @Inject(method = "clearNeedsUpdate", at = @At("TAIL"))
    // [AUDIT-OK] clearNeedsUpdate baseline; playerUpdate=false matches OF:445
    public void post$clearNeedsUpdate(CallbackInfo ci){
        this.playerUpdate = false;
    }

    @Unique
    // [AUDIT-OK] matches OF:464-470
    private boolean isWorldPlayerUpdate() {
        if (this.world instanceof WorldClient worldClient) {
            // [AUDIT-OK] WorldClient.isPlayerUpdate is OF-added (OF WorldClient:490) — MCP name, no deobf correct; provider in MixinWorldClient
            return WorldClient_isPlayerUpdate(worldClient);
        } else {
            return false;
        }
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.multiplayer.WorldClient isPlayerUpdate ()Z")
    private static native boolean WorldClient_isPlayerUpdate(WorldClient world);

    @Unique
    // [AUDIT-OK] OF-added member (OF RenderChunk)
    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }

    @Unique
    // [AUDIT-ISSUE] defined but NEVER invoked; OF applies fixBlockLayer in rebuildChunk (OF:223) — CustomBlockLayers + mipmap CUTOUT fix not applied at runtime (needs-verification: may be intentional)
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
    // [AUDIT-OK] matches OF postRenderOverlays; BufferBuilder.isDrawing is OF-added (OF BufferBuilder:767, provided by MixinBufferBuilder)
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
    // [AUDIT-OK] ChunkCacheOF swap + renderStart/renderFinish match OF rebuildChunk:292-310
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

    @SuppressWarnings("AddedMixinMembersNamePattern")

    @Unique

    // [AUDIT-OK] same-name protected method already added by cleanroom patch (RenderChunk.java.patch FORGE START) — the @Unique copy is silently discarded; identical behavior (new ChunkCache), no runtime impact
    protected ChunkCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract) {

        return new ChunkCache(world, from, to, subtract);

    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.ViewFrustum func_178161_a (Lnet.minecraft.util.math.BlockPos;)Lnet.minecraft.client.renderer.chunk.RenderChunk;")
    // [AUDIT-ISSUE] SRG func_178161_a (=ViewFrustum.getRenderChunk, tsrg) correct for SRG runtime but missing deobf=true — MCP (devrun) runtime would fail; add deobf=true
    private static native RenderChunk ViewFrustum_getRenderChunk(ViewFrustum viewFrustum, BlockPos b);
    @Unique
    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added member (OF:534); matches OF:534-544
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
    // [AUDIT-OK] OF-added members getChunk/isChunkRegionEmpty (OF RenderChunk)
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
    // [AUDIT-OK] OF-added members setRenderChunkNeighbour/getRenderChunkNeighbour (OF:573/578)
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
    // [AUDIT-OK] OF-added member (OF:586); @SignatureFix supplies ContainerLocalRenderInformation return desc
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
    // [AUDIT-ISSUE] RenderGlobal.getRenderChunk(BlockPos) is OF-added (OF RenderGlobal:3040, delegates to viewFrustum) but NO mixin provides it — NoSuchMethodError when getBoundingBoxParent() invokes it; add @Unique @Public getRenderChunk to MixinRenderGlobal
    private static native RenderChunk RenderGrobal_getRenderChunk(RenderGlobal renderGlobal, BlockPos blockPos);

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.RenderChunk getBoundingBoxParent ()Lnet.optifine.render.AabbFrame;")
    private static native AabbFrame RenderChunk_getBoundingBoxParent(RenderChunk renderChunk);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] matches OF:608-633; RenderChunk.getBoundingBoxParent() accessor is OF-added (OF member) — MCP name correct
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
