package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.chunk.RenderChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
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

    // ===== new fields =====

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public boolean renderOverlayDamaged = false;
    // ===== cross-class private access =====

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETSTATIC, desc = "net.minecraft.client.renderer.RenderGlobal renderEntitiesCounter I")
    private static native int RenderGlobal_renderEntitiesCounter_get();

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.PUTSTATIC, desc = "net.minecraft.client.renderer.RenderGlobal renderEntitiesCounter I")
    private static native void RenderGlobal_renderEntitiesCounter_set(int value);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.EntityRenderer fogStandard Z")
    private static native boolean EntityRenderer_fogStandard_get(net.minecraft.client.renderer.EntityRenderer entityRenderer);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.entity.RenderItemFrame updateItemRenderDistance ()V")
    private static native void RenderItemFrame_updateItemRenderDistance();

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.tileentity.TileEntitySignRenderer updateTextRenderDistance ()V")
    private static native void TileEntitySignRenderer_updateTextRenderDistance();

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal getCountLoadedChunks ()I")
    private static native int RenderGlobal_getCountLoadedChunks(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal getCountChunksToUpdate ()I")
    private static native int RenderGlobal_getCountChunksToUpdate(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.RenderGlobal hasNoChunkUpdates ()Z")
    private static native boolean RenderGlobal_hasNoChunkUpdates(RenderGlobal renderGlobal);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.multiplayer.ChunkProviderClient loadedChunks Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;")
    private static native Long2ObjectMap ChunkProviderClient_loadedChunks_get(ChunkProviderClient chunkProviderClient);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.RenderChunk getChunk ()Lnet/minecraft/world/chunk/Chunk;")
    private static native net.minecraft.world.chunk.Chunk RenderChunk_getChunk(RenderChunk renderChunk);
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public static int renderEntitiesCounter = 0;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    public int getCountLoadedChunks() {
        return this.world == null ? 0 : ChunkProviderClient_loadedChunks_get(this.world.getChunkProvider()).size();
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    public int getCountChunksToUpdate() {
        return this.chunksToUpdate.size();
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public boolean renderOverlayEyes = false;

    // ===== shadowed fields =====

    @Shadow
    private Minecraft mc;
    @Shadow
    private WorldClient world;
    @Shadow
    private RenderManager renderManager;
    @Shadow
    private int renderEntitiesStartupCounter;
    @Shadow
    private int countEntitiesTotal;
    @Shadow
    private int countEntitiesRendered;
    @Shadow
    private int countEntitiesHidden;
    @Unique @Public
    private int countTileEntitiesRendered;
    @Unique @Public
    private Entity renderedEntity;
    @Shadow
    private boolean entityOutlinesRendered;
    @Unique @Public
    private List<RenderGlobal.ContainerLocalRenderInformation> renderInfosEntities;
    @Unique @Public
    private List<RenderGlobal.ContainerLocalRenderInformation> renderInfosTileEntities;
    @Shadow
    private Set<TileEntity> setTileEntities;
    @Shadow
    private java.util.Map<Integer, net.minecraft.client.renderer.DestroyBlockProgress> damagedBlocks;

    @Shadow
    private java.util.Set<net.minecraft.client.renderer.chunk.RenderChunk> chunksToUpdate;

    @Shadow
    private net.minecraft.client.renderer.chunk.ChunkRenderDispatcher renderDispatcher;

    @Shadow
    protected abstract boolean isRenderEntityOutlines();
    @Shadow
    protected abstract void preRenderDamagedBlocks();
    @Shadow
    protected abstract void postRenderDamagedBlocks();

    /**
     * @author OptiRefine
     * @reason OptiFine: shaders entity/tile-entity rendering + fog override + render distance updates
     */
    @WrapMethod(method = "renderEntities")
    private void optiRefine$renderEntities(Entity entityIn, ICamera camera, float partialTicks, Operation<Void> original) {
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
            for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfosEntities) {
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
            for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 : this.renderInfosTileEntities) {
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
    protected abstract boolean isOutlineActive(Entity entity, Entity viewEntity, ICamera camera);

    @Shadow
    private net.minecraft.client.shader.Framebuffer entityOutlineFramebuffer;
    @Shadow
    private net.minecraft.client.shader.ShaderGroup entityOutlineShader;

}
