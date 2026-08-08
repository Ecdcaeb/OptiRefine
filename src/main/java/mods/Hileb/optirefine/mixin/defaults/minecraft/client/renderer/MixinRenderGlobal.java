package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.client.FMLClientHandler;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.optifine.CustomColors;
import net.optifine.CustomSky;
import net.optifine.DynamicLights;
import net.optifine.RandomEntities;
import net.optifine.render.CloudRenderer;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.gui.GuiShaderOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
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
// [AUDIT-OK] SRG func_184384_n + deobf=true double-matching (verified 2026-08-05)
    private static native boolean RenderGlobal_hasNoChunkUpdates(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.multiplayer.ChunkProviderClient field_73236_b Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;", deobf = true)
// [AUDIT-OK] SRG field_73236_b + deobf=true double-matching (verified 2026-08-05)
    private static native Long2ObjectMap ChunkProviderClient_loadedChunks_get(ChunkProviderClient chunkProviderClient);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.RenderChunk getChunk ()Lnet/minecraft/world/chunk/Chunk;")
// [AUDIT-OK] OF-added RenderChunk.getChunk(), not in baseline (MixinRenderChunk provides)
    private static native net.minecraft.world.chunk.Chunk RenderChunk_getChunk(RenderChunk renderChunk);
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Public
// [AUDIT-OK] OF-added field, not in baseline (@Public static)
    private static int renderEntitiesCounter = 0;

    @WrapMethod(method = "onEntityAdded")
    // [AUDIT-FIXED] 2026-08-05: OF registers dynamic-light entities on world events (OF:2626).
    public void optiRefine$onEntityAdded(Entity entityIn, Operation<Void> original) {
        original.call(entityIn);
        // [AUDIT-FIXED] 2026-08-05: OF:2624 also feeds RandomEntities (random mob skins, default on).
        RandomEntities.entityLoaded(entityIn, this.world);
        if (Config.isDynamicLights()) {
            DynamicLights.entityAdded(entityIn, (net.minecraft.client.renderer.RenderGlobal)(Object)this);
        }
    }

    @WrapMethod(method = "onEntityRemoved")
    // [AUDIT-FIXED] 2026-08-05: OF unregisters dynamic-light entities (OF:2633).
    public void optiRefine$onEntityRemoved(Entity entityIn, Operation<Void> original) {
        original.call(entityIn);
        // [AUDIT-FIXED] 2026-08-05: OF:2630 RandomEntities.entityUnloaded (random mob skins).
        RandomEntities.entityUnloaded(entityIn, this.world);
        if (Config.isDynamicLights()) {
            DynamicLights.entityRemoved(entityIn, (net.minecraft.client.renderer.RenderGlobal)(Object)this);
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    // [AUDIT-FIXED] 2026-08-05 crash: OF DynamicLights.updateMapDynamicLights calls
    // RenderGlobal.getWorld() (OF-added, OF:3073); missing provider -> NoSuchMethodError.
    public net.minecraft.client.multiplayer.WorldClient getWorld() {
        return this.world;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    // [AUDIT-FIXED] 2026-08-05 crash: GameSettings cloud-height slider calls RenderGlobal.resetClouds()
    // (OF:2998); missing provider -> NoSuchMethodError. OF field cloudRenderer + ctor init replicated.
    private net.optifine.render.CloudRenderer cloudRenderer;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    public void resetClouds() {
        this.cloudRenderer.reset();
    }

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

    @Inject(method = "setupTerrain", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ChunkRenderContainer;initialize(DDD)V"))
    // [AUDIT-FIXED] 2026-08-05: OF:912-915 DynamicLights.update(this) runs EVERY setupTerrain call,
    // outside the frustum-move if block; the previous INVOKE updateChunkPositions(AFTER) anchor only
    // fired on camera moves, so dynamic lights never recomputed while standing still.
    // [AUDIT-FIXED] 2026-08-08: anchor moved HEAD -> INVOKE ChunkRenderContainer.initialize BEFORE
    // (unconditional, right after the frustum-move block) to match OF:914 exactly.
    private void optiRefine$dynamicLightsUpdate(Entity entityIn, double partialTicks, ICamera p_174970_4_, int p_174970_5_, boolean p_174970_6_, CallbackInfo ci) {
        if (Config.isDynamicLights()) {
            DynamicLights.update((net.minecraft.client.renderer.RenderGlobal)(Object)this);
        }
    }

    @Inject(method = "setWorldAndLoadRenderers", at = @At("TAIL"))
    // [AUDIT-FIXED] 2026-08-05: OF clears dynamic lights when the world is swapped (OF:461).
    private void optiRefine$dynamicLightsClearWorld(net.minecraft.client.multiplayer.WorldClient worldIn, CallbackInfo ci) {
        if (Config.isDynamicLights()) {
            DynamicLights.clear();
        }
    }

    @Inject(method = "loadRenderers", at = @At("TAIL"))
    // [AUDIT-FIXED] OF RenderGlobal:539-542 sets firstWorldLoad when mc.player==null so onPlayerPositionSet re-runs loadRenderers on world join
    private void optiRefine$markFirstWorldLoad(CallbackInfo ci) {
        if (this.mc.player == null) {
            this.firstWorldLoad = true;
        }
        // [AUDIT-FIXED] 2026-08-05: OF clears dynamic lights in loadRenderers (OF:500).
        if (Config.isDynamicLights()) {
            DynamicLights.clear();
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

    // ===== sky / clouds / particles / selection box / debug-info shadows & bridges =====
    // [AUDIT-FIXED] 2026-08-09: three-way audit gaps 1-6 (sky, clouds, particles, selection box,
    // cloud hotkeys, debug info). Ported from patch/optifine RenderGlobal with cleanroom-native
    // Forge hooks (provider.getSkyRenderer / FMLClientHandler.renderClouds) replacing OF Reflector calls.

    @Shadow
// [AUDIT-OK] baseline member renderEngine (SRG field_72770_i)
    private TextureManager renderEngine;
    @Shadow
// [AUDIT-OK] baseline member skyVBO (SRG field_175012_t)
    private VertexBuffer skyVBO;
    @Shadow
// [AUDIT-OK] baseline member sky2VBO (SRG field_175011_u)
    private VertexBuffer sky2VBO;
    @Shadow
// [AUDIT-OK] baseline member starVBO (SRG field_175013_s)
    private VertexBuffer starVBO;
    @Shadow
// [AUDIT-OK] baseline member glSkyList (SRG field_72771_w)
    private int glSkyList;
    @Shadow
// [AUDIT-OK] baseline member glSkyList2 (SRG field_72781_x)
    private int glSkyList2;
    @Shadow
// [AUDIT-OK] baseline member starGLCallList (SRG field_72772_v)
    private int starGLCallList;
    @Shadow
// [AUDIT-OK] baseline member vboEnabled (SRG field_175005_X)
    private boolean vboEnabled;
    @Shadow
// [AUDIT-OK] baseline member cloudTickCounter (SRG field_72773_u)
    private int cloudTickCounter;
    @Shadow
// [AUDIT-OK] baseline member renderDistanceChunks (SRG field_72739_F)
    private int renderDistanceChunks;
    @Shadow
// [AUDIT-OK] baseline member setLightUpdates (SRG field_184387_ae)
    private java.util.Set<BlockPos> setLightUpdates;

    @Shadow
// [AUDIT-OK] baseline member SUN_TEXTURES (SRG field_110928_i)
    private static ResourceLocation SUN_TEXTURES;
    @Shadow
// [AUDIT-OK] baseline member MOON_PHASES_TEXTURES (SRG field_110927_h)
    private static ResourceLocation MOON_PHASES_TEXTURES;
    @Shadow
// [AUDIT-OK] baseline member CLOUDS_TEXTURES (SRG field_110925_j)
    private static ResourceLocation CLOUDS_TEXTURES;
    @Shadow
// [AUDIT-OK] baseline member END_SKY_TEXTURES (SRG field_110926_k)
    private static ResourceLocation END_SKY_TEXTURES;
    @Shadow
// [AUDIT-OK] baseline member FORCEFIELD_TEXTURES (SRG field_175006_g)
    private static ResourceLocation FORCEFIELD_TEXTURES;

    @Shadow
// [AUDIT-OK] baseline member renderSkyEnd()V (SRG func_180448_r) - private; wrapped with OF body
    protected abstract void renderSkyEnd();

    @Shadow
// [AUDIT-OK] baseline member renderCloudsFancy(FIDDD)V (SRG func_180445_c) - private; wrapped with OF body
    protected abstract void renderCloudsFancy(float partialTicks, int pass, double x, double y, double z);

    @Shadow
// [AUDIT-OK] baseline member calculateParticleLevel(Z)I (SRG func_190572_a)
    protected abstract int calculateParticleLevel(boolean alwaysShow);

    @Shadow
// [AUDIT-OK] baseline member cleanupDamagedBlocks(Ljava/util/Iterator;)V (SRG func_174965_a)
    protected abstract void cleanupDamagedBlocks(Iterator<DestroyBlockProgress> iterator);

    @Shadow
// [AUDIT-OK] baseline member markBlocksForUpdate(IIIIIIZ)V (SRG func_184385_a)
    protected abstract void markBlocksForUpdate(int x1, int y1, int z1, int x2, int y2, int z2, boolean needsUpdate);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.settings.GameSettings ofCloudsHeight F")
// [AUDIT-OK] OF member ofCloudsHeight, not in baseline (MixinGameSettings provides); local bridge per cross-mixin rule
    private static native float GameSettings_ofCloudsHeight_get(net.minecraft.client.settings.GameSettings gameSettings);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.EntityRenderer field_175080_Q F", deobf = true)
// [AUDIT-OK] vanilla SRG field_175080_Q = fogColorRed (AT-publicized in optirefine_at.cfg) + deobf=true
    private static native float EntityRenderer_fogColorRed_get(net.minecraft.client.renderer.EntityRenderer entityRenderer);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.EntityRenderer field_175082_R F", deobf = true)
// [AUDIT-OK] vanilla SRG field_175082_R = fogColorGreen (AT-publicized in optirefine_at.cfg) + deobf=true
    private static native float EntityRenderer_fogColorGreen_get(net.minecraft.client.renderer.EntityRenderer entityRenderer);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.EntityRenderer field_175081_S F", deobf = true)
// [AUDIT-OK] vanilla SRG field_175081_S = fogColorBlue (AT-publicized in optirefine_at.cfg) + deobf=true
    private static native float EntityRenderer_fogColorBlue_get(net.minecraft.client.renderer.EntityRenderer entityRenderer);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.Minecraft field_184129_aV Z", deobf = true)
// [AUDIT-OK] vanilla SRG field_184129_aV = actionKeyF3 (private, AT-publicized in optirefine_at.cfg); OF Reflector.Minecraft_actionKeyF3.setValue -> native PUTFIELD
    private static native void Minecraft_actionKeyF3_set(net.minecraft.client.Minecraft mc, boolean value);

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
// [AUDIT-FIXED] OF-added field renderEnv (OF:2573) - CustomColors.updateWaterFX needs it; init in <init> RETURN (cleanmix skips field initializers)
    private RenderEnv renderEnv;

    /**
     * @author OptiRefine
     * @reason OptiFine: custom sky colors, shaders sky hooks, sky/sun/moon/stars config gates, custom sky layers
     */
    @WrapMethod(method = "renderSky(FI)V")
    // [AUDIT-FIXED] OF RenderGlobal:1416-1600; Reflector.ForgeWorldProvider_getSkyRenderer -> cleanroom-native provider.getSkyRenderer()
    private void optiRefine$renderSky(float partialTicks, int pass, Operation<Void> original) {
        IRenderHandler renderer = this.mc.world.provider.getSkyRenderer();
        if (renderer != null) {
            renderer.render(partialTicks, this.world, this.mc);
            return;
        }
        if (this.mc.world.provider.getDimensionType() == DimensionType.THE_END) {
            this.renderSkyEnd();
        } else if (this.mc.world.provider.isSurfaceWorld()) {
            GlStateManager.disableTexture2D();
            boolean shaders = Config.isShaders();
            if (shaders) {
                Shaders.disableTexture2D();
            }
            Vec3d skyColor = this.world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
            skyColor = CustomColors.getSkyColor(skyColor, this.mc.world, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0, this.mc.getRenderViewEntity().posZ);
            if (shaders) {
                Shaders.setSkyColor(skyColor);
            }
            float f = (float) skyColor.x;
            float f1 = (float) skyColor.y;
            float f2 = (float) skyColor.z;
            if (pass != 2) {
                float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                f = f3;
                f1 = f4;
                f2 = f5;
            }
            GlStateManager.color(f, f1, f2);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.depthMask(false);
            GlStateManager.enableFog();
            if (shaders) {
                Shaders.enableFog();
            }
            GlStateManager.color(f, f1, f2);
            if (shaders) {
                Shaders.preSkyList();
            }
            if (Config.isSkyEnabled()) {
                if (this.vboEnabled) {
                    this.skyVBO.bindBuffer();
                    GlStateManager.glEnableClientState(32884);
                    GlStateManager.glVertexPointer(3, 5126, 12, 0);
                    this.skyVBO.drawArrays(7);
                    this.skyVBO.unbindBuffer();
                    GlStateManager.glDisableClientState(32884);
                } else {
                    GlStateManager.callList(this.glSkyList);
                }
            }
            GlStateManager.disableFog();
            if (shaders) {
                Shaders.disableFog();
            }
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.disableStandardItemLighting();
            float[] sunriseSunset = this.world.provider.calcSunriseSunsetColors(this.world.getCelestialAngle(partialTicks), partialTicks);
            if (sunriseSunset != null && Config.isSunMoonEnabled()) {
                GlStateManager.disableTexture2D();
                if (shaders) {
                    Shaders.disableTexture2D();
                }
                GlStateManager.shadeModel(7425);
                GlStateManager.pushMatrix();
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(MathHelper.sin(this.world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                float f6 = sunriseSunset[0];
                float f7 = sunriseSunset[1];
                float f8 = sunriseSunset[2];
                if (pass != 2) {
                    float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
                    float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
                    float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
                    f6 = f9;
                    f7 = f10;
                    f8 = f11;
                }
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder.pos(0.0, 100.0, 0.0).color(f6, f7, f8, sunriseSunset[3]).endVertex();
                byte b0 = 16;
                for (int i = 0; i <= 16; ++i) {
                    float f12 = i * ((float) Math.PI * 2.0F) / 16.0F;
                    float f13 = MathHelper.sin(f12);
                    float f14 = MathHelper.cos(f12);
                    bufferbuilder.pos(f13 * 120.0F, f14 * 120.0F, -f14 * 40.0F * sunriseSunset[3]).color(sunriseSunset[0], sunriseSunset[1], sunriseSunset[2], 0.0F).endVertex();
                }
                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.shadeModel(7424);
            }
            GlStateManager.enableTexture2D();
            if (shaders) {
                Shaders.enableTexture2D();
            }
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            float f15 = 1.0F - this.world.getRainStrength(partialTicks);
            GlStateManager.color(1.0F, 1.0F, 1.0F, f15);
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            CustomSky.renderSky(this.world, this.renderEngine, partialTicks);
            if (shaders) {
                Shaders.preCelestialRotate();
            }
            GlStateManager.rotate(this.world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
            if (shaders) {
                Shaders.postCelestialRotate();
            }
            float f16 = 30.0F;
            if (Config.isSunTexture()) {
                this.renderEngine.bindTexture(SUN_TEXTURES);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(-f16, 100.0, -f16).tex(0.0, 0.0).endVertex();
                bufferbuilder.pos(f16, 100.0, -f16).tex(1.0, 0.0).endVertex();
                bufferbuilder.pos(f16, 100.0, f16).tex(1.0, 1.0).endVertex();
                bufferbuilder.pos(-f16, 100.0, f16).tex(0.0, 1.0).endVertex();
                tessellator.draw();
            }
            f16 = 20.0F;
            if (Config.isMoonTexture()) {
                this.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
                int moonPhase = this.world.getMoonPhase();
                int i1 = moonPhase % 4;
                int j1 = moonPhase / 4 % 2;
                float f17 = (i1 + 0) / 4.0F;
                float f18 = (j1 + 0) / 2.0F;
                float f19 = (i1 + 1) / 4.0F;
                float f20 = (j1 + 1) / 2.0F;
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(-f16, -100.0, f16).tex(f19, f20).endVertex();
                bufferbuilder.pos(f16, -100.0, f16).tex(f17, f20).endVertex();
                bufferbuilder.pos(f16, -100.0, -f16).tex(f17, f18).endVertex();
                bufferbuilder.pos(-f16, -100.0, -f16).tex(f19, f18).endVertex();
                tessellator.draw();
            }
            GlStateManager.disableTexture2D();
            if (shaders) {
                Shaders.disableTexture2D();
            }
            float f21 = this.world.getStarBrightness(partialTicks) * f15;
            if (f21 > 0.0F && Config.isStarsEnabled() && !CustomSky.hasSkyLayers(this.world)) {
                GlStateManager.color(f21, f21, f21, f21);
                if (this.vboEnabled) {
                    this.starVBO.bindBuffer();
                    GlStateManager.glEnableClientState(32884);
                    GlStateManager.glVertexPointer(3, 5126, 12, 0);
                    this.starVBO.drawArrays(7);
                    this.starVBO.unbindBuffer();
                    GlStateManager.glDisableClientState(32884);
                } else {
                    GlStateManager.callList(this.starGLCallList);
                }
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableFog();
            if (shaders) {
                Shaders.enableFog();
            }
            GlStateManager.popMatrix();
            GlStateManager.disableTexture2D();
            if (shaders) {
                Shaders.disableTexture2D();
            }
            GlStateManager.color(0.0F, 0.0F, 0.0F);
            double d3 = this.mc.player.getPositionEyes(partialTicks).y - this.world.getHorizon();
            if (d3 < 0.0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, 12.0F, 0.0F);
                if (this.vboEnabled) {
                    this.sky2VBO.bindBuffer();
                    GlStateManager.glEnableClientState(32884);
                    GlStateManager.glVertexPointer(3, 5126, 12, 0);
                    this.sky2VBO.drawArrays(7);
                    this.sky2VBO.unbindBuffer();
                    GlStateManager.glDisableClientState(32884);
                } else {
                    GlStateManager.callList(this.glSkyList2);
                }
                GlStateManager.popMatrix();
                float f22 = 1.0F;
                float f23 = -((float) (d3 + 65.0));
                float f24 = -1.0F;
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder.pos(-1.0, f23, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, f23, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, f23, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, f23, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, f23, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, f23, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, f23, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, f23, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(-1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, 1.0).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos(1.0, -1.0, -1.0).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
            }
            if (this.world.provider.isSkyColored()) {
                GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
            } else {
                GlStateManager.color(f, f1, f2);
            }
            if (this.mc.gameSettings.renderDistanceChunks <= 4) {
                GlStateManager.color(EntityRenderer_fogColorRed_get(this.mc.entityRenderer), EntityRenderer_fogColorGreen_get(this.mc.entityRenderer), EntityRenderer_fogColorBlue_get(this.mc.entityRenderer));
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, -((float) (d3 - 16.0)), 0.0F);
            if (Config.isSkyEnabled()) {
                if (this.vboEnabled) {
                    this.sky2VBO.bindBuffer();
                    GlStateManager.glEnableClientState(32884);
                    GlStateManager.glVertexPointer(3, 5126, 12, 0);
                    this.sky2VBO.drawArrays(7);
                    this.sky2VBO.unbindBuffer();
                    GlStateManager.glDisableClientState(32884);
                } else {
                    GlStateManager.callList(this.glSkyList2);
                }
            }
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            if (shaders) {
                Shaders.enableTexture2D();
            }
            GlStateManager.depthMask(true);
        }
    }

    /**
     * @author OptiRefine
     * @reason OptiFine: THE_END sky gate + custom world sky color
     */
    @WrapMethod(method = "renderSkyEnd")
    // [AUDIT-FIXED] OF RenderGlobal:1350-1414: Config.isSkyEnabled() gate + CustomColors.getWorldSkyColor + trailing disableBlend
    private void optiRefine$renderSkyEnd(Operation<Void> original) {
        if (Config.isSkyEnabled()) {
            GlStateManager.disableFog();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.depthMask(false);
            this.renderEngine.bindTexture(END_SKY_TEXTURES);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            for (int i = 0; i < 6; ++i) {
                GlStateManager.pushMatrix();
                if (i == 1) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }
                if (i == 2) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }
                if (i == 3) {
                    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                }
                if (i == 4) {
                    GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                }
                if (i == 5) {
                    GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
                }
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int j = 40;
                int k = 40;
                int l = 40;
                if (Config.isCustomColors()) {
                    Vec3d vec3d = new Vec3d(j / 255.0, k / 255.0, l / 255.0);
                    vec3d = CustomColors.getWorldSkyColor(vec3d, this.world, this.mc.getRenderViewEntity(), 0.0F);
                    j = (int) (vec3d.x * 255.0);
                    k = (int) (vec3d.y * 255.0);
                    l = (int) (vec3d.z * 255.0);
                }
                bufferbuilder.pos(-100.0, -100.0, -100.0).tex(0.0, 0.0).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(-100.0, -100.0, 100.0).tex(0.0, 16.0).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(100.0, -100.0, 100.0).tex(16.0, 16.0).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(100.0, -100.0, -100.0).tex(16.0, 0.0).color(j, k, l, 255).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    /**
     * [AUDIT 2026-08-09] OF cloud rendering moved OUT of RenderGlobal.
     *
     * PROBLEM: OF's renderClouds override conflicted with the cleanroom Forge hook chain.
     * cleanroom renderClouds: FMLClientHandler.renderClouds() -> WorldProvider.getCloudRenderer()
     * (mod-set IRenderHandler) OR Forge's default CloudRenderer (net.minecraftforge.client.CloudRenderer,
     * always built while the VANILLA cloud setting is on). OF only honors WorldProvider.getCloudRenderer();
     * when null it continues with its own pipeline (fancy/fast, Shaders.beginClouds/endClouds,
     * ofCloudsHeight). Using the cleanroom hook from a wrap either suppressed the OF branch entirely
     * (Forge default CloudRenderer returns true) or double-rendered (two cloud passes in the frame).
     *
     * RESOLUTION: RenderGlobal.renderClouds stays vanilla (cleanroom hook chain intact); the OF cloud
     * deltas (ofClouds OFF gate, Shaders.beginClouds/endClouds, ofCloudsHeight Y offset) are rebuilt
     * on net.minecraftforge.client.CloudRenderer in MixinCloudRenderer (minecraftforge/client).
     * Fancy/fast split is dropped (Forge geometry is vanilla-fixed) — acceptable degradation.
     */


    /**
     * [AUDIT 2026-08-09] OF cloud rendering moved OUT of RenderGlobal.
     *
     * PROBLEM: OF's renderClouds override conflicted with the cleanroom Forge hook chain.
     * cleanroom renderClouds: FMLClientHandler.renderClouds() -> WorldProvider.getCloudRenderer()
     * (mod-set IRenderHandler) OR Forge's default CloudRenderer (net.minecraftforge.client.CloudRenderer,
     * always built while the VANILLA cloud setting is on). OF only honors WorldProvider.getCloudRenderer();
     * when null it continues with its own pipeline (fancy/fast, Shaders.beginClouds/endClouds,
     * ofCloudsHeight). Using the cleanroom hook from a wrap either suppressed the OF branch entirely
     * (Forge default CloudRenderer returns true) or double-rendered (two cloud passes in the frame).
     *
     * RESOLUTION: RenderGlobal.renderClouds stays vanilla (cleanroom hook chain intact); the OF cloud
     * deltas (ofClouds OFF gate, Shaders.beginClouds/endClouds, ofCloudsHeight Y offset) are rebuilt
     * on net.minecraftforge.client.CloudRenderer in MixinCloudRenderer (minecraftforge/client).
     * Fancy/fast split is dropped (Forge geometry is vanilla-fixed) — acceptable degradation.
     */


    /**
     * @author OptiRefine
     * @reason OptiFine: per-particle-type animation config gates + custom particle colors
     */
    @WrapMethod(method = "spawnParticle0(IZZDDDDDD[I)Lnet/minecraft/client/particle/Particle;")
    // [AUDIT-FIXED] OF RenderGlobal:2517-2609; the 2-boolean overload stays vanilla (delegates into this handler)
    private Particle optiRefine$spawnParticle0(int particleID, boolean ignoreRange, boolean alwaysShow, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int[] parameters, Operation<Particle> original) {
        Entity entity = this.mc.getRenderViewEntity();
        if (this.mc != null && entity != null && this.mc.effectRenderer != null) {
            int i = this.calculateParticleLevel(alwaysShow);
            double d0 = entity.posX - x;
            double d1 = entity.posY - y;
            double d2 = entity.posZ - z;
            if (particleID == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion()) {
                return null;
            } else if (particleID == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion()) {
                return null;
            } else if (particleID == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion()) {
                return null;
            } else if (particleID == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles()) {
                return null;
            } else if (particleID == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles()) {
                return null;
            } else if (particleID == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke()) {
                return null;
            } else if (particleID == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke()) {
                return null;
            } else if (particleID == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles()) {
                return null;
            } else if (particleID == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles()) {
                return null;
            } else if (particleID == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles()) {
                return null;
            } else if (particleID == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles()) {
                return null;
            } else if (particleID == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles()) {
                return null;
            } else if (particleID == EnumParticleTypes.PORTAL.getParticleID() && !Config.isPortalParticles()) {
                return null;
            } else if (particleID == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame()) {
                return null;
            } else if (particleID == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone()) {
                return null;
            } else if (particleID == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava()) {
                return null;
            } else if (particleID == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava()) {
                return null;
            } else if (particleID == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles()) {
                return null;
            } else {
                if (!ignoreRange) {
                    double d3 = 1024.0;
                    if (particleID == EnumParticleTypes.CRIT.getParticleID()) {
                        d3 = 38416.0;
                    }
                    if (d0 * d0 + d1 * d1 + d2 * d2 > d3) {
                        return null;
                    }
                    if (i > 1) {
                        return null;
                    }
                }
                Particle particle = this.mc.effectRenderer.spawnEffectParticle(particleID, x, y, z, xSpeed, ySpeed, zSpeed, parameters);
                if (particleID == EnumParticleTypes.WATER_BUBBLE.getParticleID()) {
                    CustomColors.updateWaterFX(particle, this.world, x, y, z, this.renderEnv);
                }
                if (particleID == EnumParticleTypes.WATER_SPLASH.getParticleID()) {
                    CustomColors.updateWaterFX(particle, this.world, x, y, z, this.renderEnv);
                }
                if (particleID == EnumParticleTypes.WATER_DROP.getParticleID()) {
                    CustomColors.updateWaterFX(particle, this.world, x, y, z, this.renderEnv);
                }
                if (particleID == EnumParticleTypes.TOWN_AURA.getParticleID()) {
                    CustomColors.updateMyceliumFX(particle);
                }
                if (particleID == EnumParticleTypes.PORTAL.getParticleID()) {
                    CustomColors.updatePortalFX(particle);
                }
                if (particleID == EnumParticleTypes.REDSTONE.getParticleID()) {
                    CustomColors.updateReddustFX(particle, this.world, x, y, z);
                }
                return particle;
            }
        } else {
            return null;
        }
    }

    /**
     * @author OptiRefine
     * @reason OptiFine: shaders program hooks around the outline selection box
     */
    @WrapMethod(method = "drawSelectionBox")
    // [AUDIT-FIXED] OF RenderGlobal:2310-2341: Shaders.disableTexture2D/enableTexture2D around the vanilla outline box
    private void optiRefine$drawSelectionBox(EntityPlayer player, RayTraceResult movingObjectPositionIn, int pass, float partialTicks, Operation<Void> original) {
        if (pass == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            if (Config.isShaders()) {
                Shaders.disableTexture2D();
            }
            GlStateManager.depthMask(false);
            BlockPos blockpos = movingObjectPositionIn.getBlockPos();
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            if (iblockstate.getMaterial() != Material.AIR && this.world.getWorldBorder().contains(blockpos)) {
                double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
                double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
                double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
                RenderGlobal.drawSelectionBoundingBox(iblockstate.getSelectedBoundingBox(this.world, blockpos).grow(0.002F).offset(-d0, -d1, -d2), 0.0F, 0.0F, 0.0F, 0.4F);
            }
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            if (Config.isShaders()) {
                Shaders.enableTexture2D();
            }
            GlStateManager.disableBlend();
        }
    }

    /**
     * @author OptiRefine
     * @reason OptiFine: shaders textured-lit program around the world border
     */
    @WrapMethod(method = "renderWorldBorder")
    // [AUDIT-FIXED] OF RenderGlobal:2106-2308: Shaders.pushProgram/useProgram(ProgramTexturedLit) at near-border branch start, popProgram at end
    private void optiRefine$renderWorldBorder(Entity entityIn, float partialTicks, Operation<Void> original) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        WorldBorder worldborder = this.world.getWorldBorder();
        double d0 = this.mc.gameSettings.renderDistanceChunks * 16;
        if (entityIn.posX >= worldborder.maxX() - d0 || entityIn.posX <= worldborder.minX() + d0 || entityIn.posZ >= worldborder.maxZ() - d0 || entityIn.posZ <= worldborder.minZ() + d0) {
            if (Config.isShaders()) {
                Shaders.pushProgram();
                Shaders.useProgram(Shaders.ProgramTexturedLit);
            }
            double d1 = 1.0 - worldborder.getClosestDistance(entityIn) / d0;
            d1 = Math.pow(d1, 4.0);
            double d2 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
            double d3 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
            double d4 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.renderEngine.bindTexture(FORCEFIELD_TEXTURES);
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            int i = worldborder.getStatus().getColor();
            float f = (i >> 16 & 255) / 255.0F;
            float f1 = (i >> 8 & 255) / 255.0F;
            float f2 = (i & 255) / 255.0F;
            GlStateManager.color(f, f1, f2, (float) d1);
            GlStateManager.doPolygonOffset(-3.0F, -3.0F);
            GlStateManager.enablePolygonOffset();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            float f3 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F;
            float f4 = 0.0F;
            float f5 = 0.0F;
            float f6 = 128.0F;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.setTranslation(-d2, -d3, -d4);
            double d5 = Math.max((double) MathHelper.floor(d4 - d0), worldborder.minZ());
            double d6 = Math.min((double) MathHelper.ceil(d4 + d0), worldborder.maxZ());
            if (d2 > worldborder.maxX() - d0) {
                float f7 = 0.0F;
                for (double d7 = d5; d7 < d6; f7 += 0.5F) {
                    double d8 = Math.min(1.0, d6 - d7);
                    float f8 = (float) d8 * 0.5F;
                    bufferbuilder.pos(worldborder.maxX(), 256.0, d7).tex(f3 + f7, f3 + 0.0F).endVertex();
                    bufferbuilder.pos(worldborder.maxX(), 256.0, d7 + d8).tex(f3 + f8 + f7, f3 + 0.0F).endVertex();
                    bufferbuilder.pos(worldborder.maxX(), 0.0, d7 + d8).tex(f3 + f8 + f7, f3 + 128.0F).endVertex();
                    bufferbuilder.pos(worldborder.maxX(), 0.0, d7).tex(f3 + f7, f3 + 128.0F).endVertex();
                    ++d7;
                }
            }
            if (d2 < worldborder.minX() + d0) {
                float f9 = 0.0F;
                for (double d9 = d5; d9 < d6; f9 += 0.5F) {
                    double d10 = Math.min(1.0, d6 - d9);
                    float f10 = (float) d10 * 0.5F;
                    bufferbuilder.pos(worldborder.minX(), 256.0, d9).tex(f3 + f9, f3 + 0.0F).endVertex();
                    bufferbuilder.pos(worldborder.minX(), 256.0, d9 + d10).tex(f3 + f10 + f9, f3 + 0.0F).endVertex();
                    bufferbuilder.pos(worldborder.minX(), 0.0, d9 + d10).tex(f3 + f10 + f9, f3 + 128.0F).endVertex();
                    bufferbuilder.pos(worldborder.minX(), 0.0, d9).tex(f3 + f9, f3 + 128.0F).endVertex();
                    ++d9;
                }
            }
            d5 = Math.max((double) MathHelper.floor(d2 - d0), worldborder.minX());
            d6 = Math.min((double) MathHelper.ceil(d2 + d0), worldborder.maxX());
            if (d4 > worldborder.maxZ() - d0) {
                float f11 = 0.0F;
                for (double d11 = d5; d11 < d6; f11 += 0.5F) {
                    double d12 = Math.min(1.0, d6 - d11);
                    float f12 = (float) d12 * 0.5F;
                    bufferbuilder.pos(d11, 256.0, worldborder.maxZ()).tex(f3 + f11, f3 + 0.0F).endVertex();
                    bufferbuilder.pos(d11 + d12, 256.0, worldborder.maxZ()).tex(f3 + f12 + f11, f3 + 0.0F).endVertex();
                    bufferbuilder.pos(d11 + d12, 0.0, worldborder.maxZ()).tex(f3 + f12 + f11, f3 + 128.0F).endVertex();
                    bufferbuilder.pos(d11, 0.0, worldborder.maxZ()).tex(f3 + f11, f3 + 128.0F).endVertex();
                    ++d11;
                }
            }
            if (d4 < worldborder.minZ() + d0) {
                float f13 = 0.0F;
                for (double d13 = d5; d13 < d6; f13 += 0.5F) {
                    double d14 = Math.min(1.0, d6 - d13);
                    float f14 = (float) d14 * 0.5F;
                    bufferbuilder.pos(d13, 256.0, worldborder.minZ()).tex(f3 + f13, f3 + 0.0F).endVertex();
                    bufferbuilder.pos(d13 + d14, 256.0, worldborder.minZ()).tex(f3 + f14 + f13, f3 + 0.0F).endVertex();
                    bufferbuilder.pos(d13 + d14, 0.0, worldborder.minZ()).tex(f3 + f14 + f13, f3 + 128.0F).endVertex();
                    bufferbuilder.pos(d13, 0.0, worldborder.minZ()).tex(f3 + f13, f3 + 128.0F).endVertex();
                    ++d13;
                }
            }
            tessellator.draw();
            bufferbuilder.setTranslation(0.0, 0.0, 0.0);
            GlStateManager.enableCull();
            GlStateManager.disableAlpha();
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.enableAlpha();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            if (Config.isShaders()) {
                Shaders.popProgram();
            }
        }
    }

    /**
     * @author OptiRefine
     * @reason OptiFine: F3+O shader options / F3+R shader reload hotkeys (1.12.2 OF; no cloud toggle in this version)
     */
    @WrapMethod(method = "updateClouds")
    // [AUDIT-FIXED] OF RenderGlobal:1317-1347: Keyboard F3+O (24) opens GuiShaderOptions, F3+R (19) reloads shader pack + re-opens debug screen
    private void optiRefine$updateClouds(Operation<Void> original) {
        if (Config.isShaders()) {
            if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(24)) {
                GuiShaderOptions guishaderoptions = new GuiShaderOptions(null, Config.getGameSettings());
                Config.getMinecraft().displayGuiScreen(guishaderoptions);
            }
            if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19)) {
                Shaders.uninit();
                Shaders.loadShaderPack();
                Minecraft_actionKeyF3_set(this.mc, true);
            }
        }
        this.cloudTickCounter++;
        if (this.cloudTickCounter % 20 == 0) {
            this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
        }
        if (!this.setLightUpdates.isEmpty() && !this.renderDispatcher.hasNoFreeRenderBuilders() && this.chunksToUpdate.isEmpty()) {
            Iterator<BlockPos> iterator = this.setLightUpdates.iterator();
            while (iterator.hasNext()) {
                BlockPos blockpos = iterator.next();
                iterator.remove();
                int i = blockpos.getX();
                int j = blockpos.getY();
                int k = blockpos.getZ();
                this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1, false);
            }
        }
    }

    @ModifyReturnValue(method = "getDebugInfoEntities", at = @At("RETURN"))
    // [AUDIT-FIXED] OF RenderGlobal:887-889: appends Config.getVersionDebug(); getDebugInfoRenders needs no delta (baseline already OF-identical)
    private String optiRefine$getDebugInfoEntities(String original) {
        return original + ", " + Config.getVersionDebug();
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.renderOverlayDamaged = false;
        this.renderOverlayEyes = false;
        this.firstWorldLoad = false;
        this.cloudRenderer = new CloudRenderer(this.mc);
        this.renderEnv = new RenderEnv(Blocks.AIR.getDefaultState(), new BlockPos(0, 0, 0));
    }
}
