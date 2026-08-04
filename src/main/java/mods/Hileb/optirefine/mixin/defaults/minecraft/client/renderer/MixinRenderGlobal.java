package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.chunk.RenderChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.MinecraftForgeClient;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

/**
 * OptiFine additions to {@link RenderGlobal}.
 *
 * <p>Implements the core OptiFine render hooks: the shaders-wrapped entity/tile-entity
 * rendering loop, fog override, per-entity render distance updates, overlay state flags
 * and animation toggles. The rendering-pass system, {@code shouldRenderInPass} checks,
 * {@code preDrawBatch}/{@code drawBatch} and custom sky/cloud renderers are already
 * provided by the Cleanroom patches.</p>
 */
@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 2

    // ===== new fields =====

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public)
    @Unique
    private boolean renderOverlayDamaged;
    // ===== cross-class private access =====

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETSTATIC, desc = "net.minecraft.client.renderer.RenderGlobal renderEntitiesCounter I")
// [AUDIT-OK] OF member renderEntitiesCounter (static, mixin-provided @Public below)
    private static native int RenderGlobal_renderEntitiesCounter_get();

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTSTATIC, desc = "net.minecraft.client.renderer.RenderGlobal renderEntitiesCounter I")
// [AUDIT-OK] OF member renderEntitiesCounter, not in baseline
    private static native void RenderGlobal_renderEntitiesCounter_set(int value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.EntityRenderer fogStandard Z")
// [AUDIT-OK] OF member fogStandard, not in baseline (MixinEntityRenderer @Unique provides)
    private static native boolean EntityRenderer_fogStandard_get(net.minecraft.client.renderer.EntityRenderer entityRenderer);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.entity.RenderItemFrame updateItemRenderDistance ()V")
// [AUDIT-OK] OF member updateItemRenderDistance()V, not in baseline (MixinRenderItemFrame provides)
    private static native void RenderItemFrame_updateItemRenderDistance();

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.tileentity.TileEntitySignRenderer updateTextRenderDistance ()V")
// [AUDIT-OK] OF member updateTextRenderDistance()V, not in baseline (MixinTileEntitySignRenderer provides)
    private static native void TileEntitySignRenderer_updateTextRenderDistance();

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal getCountLoadedChunks ()I")
// [AUDIT-OK] OF member getCountLoadedChunks()I, not in baseline (mixin-provided @Public)
    private static native int RenderGlobal_getCountLoadedChunks(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal getCountChunksToUpdate ()I")
// [AUDIT-OK] OF member getCountChunksToUpdate()I, not in baseline (mixin-provided @Public)
    private static native int RenderGlobal_getCountChunksToUpdate(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal func_184384_n ()Z", deobf = true)
// [AUDIT-ISSUE] vanilla SRG func_184384_n = hasNoChunkUpdates verified, but deobf=true missing -> devrun (MCP runtime) breaks; SRG runtime OK
    private static native boolean RenderGlobal_hasNoChunkUpdates(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.multiplayer.ChunkProviderClient field_73236_b Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;", deobf = true)
// [AUDIT-ISSUE] vanilla SRG field_73236_b = loadedChunks verified, but deobf=true missing -> devrun breaks; SRG runtime OK
    private static native Long2ObjectMap ChunkProviderClient_loadedChunks_get(ChunkProviderClient chunkProviderClient);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.RenderChunk getChunk ()Lnet/minecraft/world/chunk/Chunk;")
// [AUDIT-OK] OF-added RenderChunk.getChunk(), not in baseline (MixinRenderChunk provides)
    private static native net.minecraft.world.chunk.Chunk RenderChunk_getChunk(RenderChunk renderChunk);
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public static)
    private static int renderEntitiesCounter = 0;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    // [AUDIT-FIXED] OF RenderGlobal:3002-3016 - Config.drawFps calls these; missing -> NoSuchMethodError
    private int getCountRenderers() {
        return this.viewFrustum.renderChunks.length;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private int getCountActiveRenderers() {
        return this.renderInfos.size();
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private int getCountEntitiesRendered() {
        return this.countEntitiesRendered;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private int getCountTileEntitiesRendered() {
        return this.countTileEntitiesRendered;
    }

    public int getCountLoadedChunks() {
        // [AUDIT-FIXED] null-guard provider/map like OF RenderGlobal:3018-3033
        if (this.world == null || this.world.getChunkProvider() == null) {
            return 0;
        }
        it.unimi.dsi.fastutil.longs.Long2ObjectMap loaded = ChunkProviderClient_loadedChunks_get(this.world.getChunkProvider());
        return loaded == null ? 0 : loaded.size();
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added method, not in baseline
    public int getCountChunksToUpdate() {
        return this.chunksToUpdate.size();
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public)
    @Unique
    private boolean renderOverlayEyes;

    // ===== shadowed fields =====

    @Shadow
// [AUDIT-OK] baseline member mc (SRG field_72777_q)
    private Minecraft mc;
    @Shadow
// [AUDIT-OK] baseline member world (SRG field_72769_h)
    private WorldClient world;
    @Shadow
// [AUDIT-OK] baseline member renderManager (SRG field_175010_j)
    private RenderManager renderManager;
    @Shadow
// [AUDIT-OK] baseline member renderEntitiesStartupCounter (SRG field_72740_G)
    private int renderEntitiesStartupCounter;
    @Shadow
// [AUDIT-OK] baseline member countEntitiesTotal (SRG field_72748_H)
    private int countEntitiesTotal;
    @Shadow
// [AUDIT-OK] baseline member countEntitiesRendered (SRG field_72749_I)
    private int countEntitiesRendered;
    @Shadow
// [AUDIT-OK] baseline member countEntitiesHidden (SRG field_72750_J)
    private int countEntitiesHidden;
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public lifts visibility)
    private int countTileEntitiesRendered;
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public)
    private Entity renderedEntity;
    @Shadow
// [AUDIT-OK] baseline member entityOutlinesRendered (SRG field_184386_ad)
    private boolean entityOutlinesRendered;
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public; OF keeps private)
    @Shadow
// [AUDIT-OK] baseline member renderInfos (SRG field_72755_R) - filled by baseline setupTerrain
    public java.util.List<RenderGlobal.ContainerLocalRenderInformation> renderInfos;

    private List<RenderGlobal.ContainerLocalRenderInformation> renderInfosEntities = new java.util.ArrayList<>(1024);
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public; OF keeps private)
    private List<RenderGlobal.ContainerLocalRenderInformation> renderInfosTileEntities = new java.util.ArrayList<>(1024);
    @Shadow
// [AUDIT-OK] baseline member setTileEntities (SRG field_181024_n)
    private Set<TileEntity> setTileEntities;
    @Shadow
// [AUDIT-OK] baseline member damagedBlocks (SRG field_72738_E)
    private java.util.Map<Integer, net.minecraft.client.renderer.DestroyBlockProgress> damagedBlocks;

    @Shadow
// [AUDIT-OK] baseline member chunksToUpdate (SRG field_175009_l)
    private java.util.Set<net.minecraft.client.renderer.chunk.RenderChunk> chunksToUpdate;

    @Shadow
// [AUDIT-OK] baseline member renderDispatcher (SRG field_174995_M)
    private net.minecraft.client.renderer.chunk.ChunkRenderDispatcher renderDispatcher;

    @Shadow
    public net.minecraft.client.renderer.ViewFrustum viewFrustum;

    @Shadow
// [AUDIT-OK] baseline member loadRenderers (func_72712_a)
    public abstract void loadRenderers();

    @Inject(method = "loadRenderers", at = @At("TAIL"))
    // [AUDIT-FIXED] OF RenderGlobal:539-542 sets firstWorldLoad when mc.player==null so onPlayerPositionSet re-runs loadRenderers on world join
    private void optiRefine$markFirstWorldLoad(CallbackInfo ci) {
        if (this.mc.player == null) {
            this.firstWorldLoad = true;
        }
    }

    @Shadow
// [AUDIT-OK] baseline member isRenderEntityOutlines()Z (SRG func_174985_d)
    protected abstract boolean isRenderEntityOutlines();
    @Shadow
// [AUDIT-OK] baseline member preRenderDamagedBlocks()V (SRG func_180443_s)
    protected abstract void preRenderDamagedBlocks();

    @WrapOperation(method = "renderBlockLayer(Lnet/minecraft/util/BlockRenderLayer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ChunkRenderContainer;renderChunkLayer(Lnet/minecraft/util/BlockRenderLayer;)V"))
    // [AUDIT-FIXED] shaders hooks around renderChunkLayer (OF RenderGlobal:1275-1282): pre/postRenderChunkLayer
    // enable GL_NORMAL_ARRAY + midTexCoord/tangent/entity attrib arrays and flip backface culling.
    // Missing -> terrain programs read default attributes -> blocks render black/blank under shaderpacks.
    public void optiRefine$renderChunkLayer(net.minecraft.client.renderer.ChunkRenderContainer instance, net.minecraft.util.BlockRenderLayer layer, Operation<Void> original) {
        if (Config.isShaders()) {
            net.optifine.shaders.ShadersRender.preRenderChunkLayer(layer);
        }
        original.call(instance, layer);
        if (Config.isShaders()) {
            net.optifine.shaders.ShadersRender.postRenderChunkLayer(layer);
        }
    }
    @Shadow
// [AUDIT-OK] baseline member postRenderDamagedBlocks()V (SRG func_174969_t)
    protected abstract void postRenderDamagedBlocks();

    /**
     * @author OptiRefine
     * @reason OptiFine: shaders entity/tile-entity rendering + fog override + render distance updates
     */
    @WrapMethod(method = "renderEntities")
    private void optiRefine$renderEntities(Entity entityIn, ICamera camera, float partialTicks, Operation<Void> original) {
// [AUDIT-OK] target renderEntities(LEntity;LICamera;F)V = func_180446_a; OF body incl. Forge pass hooks (pass/shouldRenderInPass/preDrawBatch/drawBatch) preserved
        int pass = MinecraftForgeClient.getRenderPass();
        if (this.renderEntitiesStartupCounter > 0) {
            if (pass > 0) {
                return;
            }
            --this.renderEntitiesStartupCounter;
        } else {
            double d0 = entityIn.prevPosX + (entityIn.posX - entityIn.prevPosX) * partialTicks;
            double d1 = entityIn.prevPosY + (entityIn.posY - entityIn.prevPosY) * partialTicks;
            double d2 = entityIn.prevPosZ + (entityIn.posZ - entityIn.prevPosZ) * partialTicks;
            this.world.profiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.prepare(this.world, this.mc.getTextureManager(), this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.objectMouseOver, partialTicks);
            this.renderManager.cacheActiveRenderInfo(this.world, this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
            RenderGlobal_renderEntitiesCounter_set(RenderGlobal_renderEntitiesCounter_get() + 1);
            if (pass == 0) {
                this.countEntitiesTotal = 0;
                this.countEntitiesRendered = 0;
                this.countEntitiesHidden = 0;
                this.countTileEntitiesRendered = 0;
            }
            Entity entity = this.mc.getRenderViewEntity();
            double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
            double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
            double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
            TileEntityRendererDispatcher.staticPlayerX = d3;
            TileEntityRendererDispatcher.staticPlayerY = d4;
            TileEntityRendererDispatcher.staticPlayerZ = d5;
            this.renderManager.setRenderPosition(d3, d4, d5);
            this.mc.entityRenderer.enableLightmap();
            this.world.profiler.endStartSection("global");
            List<Entity> list = this.world.getLoadedEntityList();
            if (pass == 0) {
                this.countEntitiesTotal = list.size();
            }
            if (Config.isFogOff() && EntityRenderer_fogStandard_get(this.mc.entityRenderer)) {
                GlStateManager.disableFog();
            }
            for (int i = 0; i < this.world.weatherEffects.size(); ++i) {
                Entity entity1 = this.world.weatherEffects.get(i);
                if (!entity1.shouldRenderInPass(pass)) {
                    continue;
                }
                ++this.countEntitiesRendered;
                if (entity1.isInRangeToRender3d(d0, d1, d2)) {
                    this.renderManager.renderEntityStatic(entity1, partialTicks, false);
                }
            }
            this.world.profiler.endStartSection("entities");
            boolean flag = Config.isShaders();
            if (flag) {
                Shaders.beginEntities();
            }
            RenderItemFrame_updateItemRenderDistance();
            List<Entity> list1 = com.google.common.collect.Lists.newArrayList();
            List<Entity> list2 = com.google.common.collect.Lists.newArrayList();
            PooledMutableBlockPos pooledmutableblockpos = PooledMutableBlockPos.retain();
            boolean flag1 = Shaders.isShadowPass && !this.mc.player.isSpectator();
            // [AUDIT-FIXED] iterate baseline renderInfos (field_72755_R, filled by baseline setupTerrain);
            // mixin-added renderInfosEntities was never populated -> entities/TEs never rendered
            for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
                Chunk chunk = RenderChunk_getChunk(renderglobal$containerlocalrenderinformation.renderChunk);
                ClassInheritanceMultiMap<Entity> classinheritancemultimap = chunk.getEntityLists()[renderglobal$containerlocalrenderinformation.renderChunk.getPosition().getY() / 16];
                if (!classinheritancemultimap.isEmpty()) {
                    for (Entity entity2 : classinheritancemultimap) {
                        if (!entity2.shouldRenderInPass(pass)) {
                            continue;
                        }
                        boolean flag2 = this.renderManager.shouldRender(entity2, camera, d0, d1, d2) || entity2.isRidingOrBeingRiddenBy(this.mc.player);
                        if (flag2) {
                            boolean flag3 = this.mc.getRenderViewEntity() instanceof EntityLivingBase ? ((EntityLivingBase) this.mc.getRenderViewEntity()).isPlayerSleeping() : false;
                            if ((entity2 != this.mc.getRenderViewEntity() || flag1 || this.mc.gameSettings.thirdPersonView != 0 || flag3) && (entity2.posY < 0.0D || entity2.posY >= 256.0D || this.world.isBlockLoaded(pooledmutableblockpos.setPos(entity2)))) {
                                ++this.countEntitiesRendered;
                                this.renderedEntity = entity2;
                                if (flag) {
                                    Shaders.nextEntity(entity2);
                                }
                                this.renderManager.renderEntityStatic(entity2, partialTicks, false);
                                this.renderedEntity = null;
                                if (this.isOutlineActive(entity2, entity, camera)) {
                                    list1.add(entity2);
                                }
                                if (this.renderManager.isRenderMultipass(entity2)) {
                                    list2.add(entity2);
                                }
                            }
                        }
                    }
                }
            }
            pooledmutableblockpos.release();
            if (!list2.isEmpty()) {
                for (Entity entity3 : list2) {
                    if (!entity3.shouldRenderInPass(pass)) {
                        continue;
                    }
                    if (flag) {
                        Shaders.nextEntity(entity3);
                    }
                    this.renderManager.renderMultipass(entity3, partialTicks);
                }
            }
            if (pass == 0 && this.isRenderEntityOutlines() && (!list1.isEmpty() || this.entityOutlinesRendered)) {
                this.world.profiler.endStartSection("entityOutlines");
                this.entityOutlineFramebuffer.framebufferClear();
                this.entityOutlinesRendered = !list1.isEmpty();
                if (!list1.isEmpty()) {
                    GlStateManager.depthFunc(519);
                    GlStateManager.disableFog();
                    this.entityOutlineFramebuffer.bindFramebuffer(false);
                    net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                    this.renderManager.setRenderOutlines(true);
                    for (int j = 0; j < list1.size(); ++j) {
                        Entity entity4 = list1.get(j);
                        if (!entity4.shouldRenderInPass(pass)) {
                            continue;
                        }
                        if (flag) {
                            Shaders.nextEntity(entity4);
                        }
                        this.renderManager.renderEntityStatic(entity4, partialTicks, false);
                    }
                    this.renderManager.setRenderOutlines(false);
                    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
                    GlStateManager.depthMask(false);
                    this.entityOutlineShader.render(partialTicks);
                    GlStateManager.enableLighting();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableFog();
                    GlStateManager.enableBlend();
                    GlStateManager.enableColorMaterial();
                    GlStateManager.depthFunc(515);
                    GlStateManager.enableDepth();
                    GlStateManager.enableAlpha();
                }
                this.mc.getFramebuffer().bindFramebuffer(false);
            }
            if (!this.isRenderEntityOutlines() && (!list1.isEmpty() || this.entityOutlinesRendered)) {
                this.world.profiler.endStartSection("entityOutlines");
                this.entityOutlinesRendered = !list1.isEmpty();
                if (!list1.isEmpty()) {
                    if (flag) {
                        Shaders.beginEntitiesGlowing();
                    }
                    GlStateManager.disableFog();
                    GlStateManager.disableDepth();
                    this.mc.entityRenderer.disableLightmap();
                    net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                    this.renderManager.setRenderOutlines(true);
                    for (int k = 0; k < list1.size(); ++k) {
                        Entity entity5 = list1.get(k);
                        if (!entity5.shouldRenderInPass(pass)) {
                            continue;
                        }
                        if (flag) {
                            Shaders.nextEntity(entity5);
                        }
                        this.renderManager.renderEntityStatic(entity5, partialTicks, false);
                    }
                    this.renderManager.setRenderOutlines(false);
                    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
                    this.mc.entityRenderer.enableLightmap();
                    GlStateManager.enableDepth();
                    GlStateManager.enableFog();
                    if (flag) {
                        Shaders.endEntitiesGlowing();
                    }
                }
            }
            if (flag) {
                Shaders.endEntities();
                Shaders.beginBlockEntities();
            }
            this.world.profiler.endStartSection("blockentities");
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
            TileEntityRendererDispatcher.instance.preDrawBatch();
            TileEntitySignRenderer_updateTextRenderDistance();
            for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 : this.renderInfos) {
                List<TileEntity> list3 = renderglobal$containerlocalrenderinformation1.renderChunk.getCompiledChunk().getTileEntities();
                if (!list3.isEmpty()) {
                    for (TileEntity tileentity : list3) {
                        if (!tileentity.shouldRenderInPass(pass)) {
                            continue;
                        }
                        AxisAlignedBB axisalignedbb = tileentity.getRenderBoundingBox();
                        if (axisalignedbb == null || camera.isBoundingBoxInFrustum(axisalignedbb)) {
                            if (flag) {
                                Shaders.nextBlockEntity(tileentity);
                            }
                            TileEntityRendererDispatcher.instance.render(tileentity, partialTicks, -1);
                            ++this.countTileEntitiesRendered;
                        }
                    }
                }
            }
            synchronized (this.setTileEntities) {
                for (TileEntity tileentity1 : this.setTileEntities) {
                    if (!tileentity1.shouldRenderInPass(pass)) {
                        continue;
                    }
                    if (flag) {
                        Shaders.nextBlockEntity(tileentity1);
                    }
                    TileEntityRendererDispatcher.instance.render(tileentity1, partialTicks, -1);
                }
            }
            TileEntityRendererDispatcher.instance.drawBatch(pass);
            this.renderOverlayDamaged = true;
            this.preRenderDamagedBlocks();
            for (net.minecraft.client.renderer.DestroyBlockProgress destroyblockprogress : this.damagedBlocks.values()) {
                BlockPos blockpos = destroyblockprogress.getPosition();
                if (this.world.getBlockState(blockpos).getBlock().hasTileEntity()) {
                    TileEntity tileentity2 = this.world.getTileEntity(blockpos);
                    if (tileentity2 instanceof TileEntityChest) {
                        TileEntityChest tileentitychest = (TileEntityChest) tileentity2;
                        if (tileentitychest.adjacentChestXNeg != null) {
                            blockpos = blockpos.offset(EnumFacing.WEST);
                            tileentity2 = this.world.getTileEntity(blockpos);
                        } else if (tileentitychest.adjacentChestZNeg != null) {
                            blockpos = blockpos.offset(EnumFacing.NORTH);
                            tileentity2 = this.world.getTileEntity(blockpos);
                        }
                    }
                    IBlockState iblockstate = this.world.getBlockState(blockpos);
                    if (tileentity2 != null && iblockstate.getBlock().hasTileEntity(iblockstate)) {
                        if (flag) {
                            Shaders.nextBlockEntity(tileentity2);
                        }
                        TileEntityRendererDispatcher.instance.render(tileentity2, partialTicks, destroyblockprogress.getPartialBlockDamage());
                    }
                }
            }
            this.postRenderDamagedBlocks();
            this.renderOverlayDamaged = false;
            if (flag) {
                Shaders.endBlockEntities();
            }
            RenderGlobal_renderEntitiesCounter_set(RenderGlobal_renderEntitiesCounter_get() - 1);
            this.mc.entityRenderer.disableLightmap();
            this.mc.profiler.endSection();
        }
    }

    @Shadow
// [AUDIT-OK] baseline member isOutlineActive(LEntity;LEntity;LICamera;)Z (SRG func_184383_a)
    protected abstract boolean isOutlineActive(Entity entity, Entity viewEntity, ICamera camera);

    @Shadow
// [AUDIT-OK] baseline member entityOutlineFramebuffer (SRG field_175015_z)
    private net.minecraft.client.shader.Framebuffer entityOutlineFramebuffer;
    @Shadow
// [AUDIT-OK] baseline member entityOutlineShader (SRG field_174991_A)
    private net.minecraft.client.shader.ShaderGroup entityOutlineShader;

    // [AUDIT-FIXED] vanilla private fields cross-class accessed (EntityRenderer helpers): publicize via cursed AT
    @AccessTransformer(name = "field_72738_E", deobf = true, access = org.objectweb.asm.Opcodes.ACC_PUBLIC)
    private java.util.Map acc_damagedBlocks;

    @AccessTransformer(name = "field_147595_R", deobf = true, access = org.objectweb.asm.Opcodes.ACC_PUBLIC)
    private boolean acc_displayListEntitiesDirty;

    @Unique
    private boolean firstWorldLoad;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-OK] OF-added method (patch/optifine RenderGlobal:3060), invoked by MixinPacketThreadUtil
    public void onPlayerPositionSet() {
        if (this.firstWorldLoad) {
            this.loadRenderers();
            this.firstWorldLoad = false;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-FIXED] three-way audit (P9): OF-added getRenderChunk(BlockPos) (OF:3040-3042); no mixin
// provided it -> MixinRenderChunk.getBoundingBoxParent chain NoSuchMethodError on parent lookup.
    public RenderChunk getRenderChunk(net.minecraft.util.math.BlockPos pos) {
        return ViewFrustum_getRenderChunk(this.viewFrustum, pos);
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.ViewFrustum func_178161_a (Lnet.minecraft.util.math.BlockPos;)Lnet.minecraft.client.renderer.chunk.RenderChunk;", deobf = true)
    private static native RenderChunk ViewFrustum_getRenderChunk(ViewFrustum viewFrustum, net.minecraft.util.math.BlockPos pos);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
// [AUDIT-OK] OF-added method, not in baseline
    public void pauseChunkUpdates() {
        if (this.renderDispatcher != null) {
            ChunkRenderDispatcher_pauseChunkUpdates(this.renderDispatcher);
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-OK] OF-added method, not in baseline
    public void resumeChunkUpdates() {
        if (this.renderDispatcher != null) {
            ChunkRenderDispatcher_resumeChunkUpdates(this.renderDispatcher);
        }
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher pauseChunkUpdates ()V")
// [AUDIT-OK] OF member pauseChunkUpdates()V, not in baseline (MixinChunkRenderDispatcher provides)
    private static native void ChunkRenderDispatcher_pauseChunkUpdates(net.minecraft.client.renderer.chunk.ChunkRenderDispatcher dispatcher);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher resumeChunkUpdates ()V")
// [AUDIT-OK] OF member resumeChunkUpdates()V, not in baseline
    private static native void ChunkRenderDispatcher_resumeChunkUpdates(net.minecraft.client.renderer.chunk.ChunkRenderDispatcher dispatcher);


    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.renderOverlayDamaged = false;
        this.renderOverlayEyes = false;
        this.firstWorldLoad = false;
    }
}
