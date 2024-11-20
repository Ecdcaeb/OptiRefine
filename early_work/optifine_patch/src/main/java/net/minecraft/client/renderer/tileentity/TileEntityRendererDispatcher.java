/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Map
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.model.ModelShulker
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityBedRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityChestRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityEndGatewayRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityPistonRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityShulkerBoxRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntitySignRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityStructureRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityBanner
 *  net.minecraft.tileentity.TileEntityBeacon
 *  net.minecraft.tileentity.TileEntityBed
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityEnchantmentTable
 *  net.minecraft.tileentity.TileEntityEndGateway
 *  net.minecraft.tileentity.TileEntityEndPortal
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  net.minecraft.tileentity.TileEntityMobSpawner
 *  net.minecraft.tileentity.TileEntityPiston
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraft.tileentity.TileEntitySign
 *  net.minecraft.tileentity.TileEntitySkull
 *  net.minecraft.tileentity.TileEntityStructure
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.optifine.EmissiveTextures
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 */
package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityBedRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEndGatewayRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityPistonRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityShulkerBoxRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityStructureRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.optifine.EmissiveTextures;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;

public class TileEntityRendererDispatcher {
    public final Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> renderers = Maps.newHashMap();
    public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
    public FontRenderer fontRenderer;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World world;
    public Entity entity;
    public float entityYaw;
    public float entityPitch;
    public RayTraceResult cameraHitResult;
    public double entityX;
    public double entityY;
    public double entityZ;
    public TileEntity tileEntityRendered;
    private Tessellator batchBuffer = new Tessellator(0x200000);
    private boolean drawingBatch = false;

    private TileEntityRendererDispatcher() {
        this.renderers.put(TileEntitySign.class, (Object)new TileEntitySignRenderer());
        this.renderers.put(TileEntityMobSpawner.class, (Object)new TileEntityMobSpawnerRenderer());
        this.renderers.put(TileEntityPiston.class, (Object)new TileEntityPistonRenderer());
        this.renderers.put(TileEntityChest.class, (Object)new TileEntityChestRenderer());
        this.renderers.put(TileEntityEnderChest.class, (Object)new TileEntityEnderChestRenderer());
        this.renderers.put(TileEntityEnchantmentTable.class, (Object)new TileEntityEnchantmentTableRenderer());
        this.renderers.put(TileEntityEndPortal.class, (Object)new TileEntityEndPortalRenderer());
        this.renderers.put(TileEntityEndGateway.class, (Object)new TileEntityEndGatewayRenderer());
        this.renderers.put(TileEntityBeacon.class, (Object)new TileEntityBeaconRenderer());
        this.renderers.put(TileEntitySkull.class, (Object)new TileEntitySkullRenderer());
        this.renderers.put(TileEntityBanner.class, (Object)new TileEntityBannerRenderer());
        this.renderers.put(TileEntityStructure.class, (Object)new TileEntityStructureRenderer());
        this.renderers.put(TileEntityShulkerBox.class, (Object)new TileEntityShulkerBoxRenderer(new ModelShulker()));
        this.renderers.put(TileEntityBed.class, (Object)new TileEntityBedRenderer());
        for (TileEntitySpecialRenderer tileentityspecialrenderer : this.renderers.values()) {
            tileentityspecialrenderer.setRendererDispatcher(this);
        }
    }

    public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer(Class<? extends TileEntity> teClass) {
        TileEntitySpecialRenderer<T> tileentityspecialrenderer = (TileEntitySpecialRenderer<T>)this.renderers.get(teClass);
        if (tileentityspecialrenderer == null && teClass != TileEntity.class) {
            tileentityspecialrenderer = this.getRenderer((Class<? extends TileEntity>)teClass.getSuperclass());
            this.renderers.put(teClass, tileentityspecialrenderer);
        }
        return tileentityspecialrenderer;
    }

    @Nullable
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer(@Nullable TileEntity tileEntityIn) {
        return tileEntityIn == null || tileEntityIn.isInvalid() ? null : this.getRenderer((Class<? extends TileEntity>)tileEntityIn.getClass());
    }

    public void prepare(World worldIn, TextureManager renderEngineIn, FontRenderer fontRendererIn, Entity entityIn, RayTraceResult cameraHitResultIn, float p_190056_6_) {
        if (this.world != worldIn) {
            this.setWorld(worldIn);
        }
        this.renderEngine = renderEngineIn;
        this.entity = entityIn;
        this.fontRenderer = fontRendererIn;
        this.cameraHitResult = cameraHitResultIn;
        this.entityYaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * p_190056_6_;
        this.entityPitch = entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * p_190056_6_;
        this.entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)p_190056_6_;
        this.entityY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)p_190056_6_;
        this.entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)p_190056_6_;
    }

    public void render(TileEntity tileentityIn, float partialTicks, int destroyStage) {
        if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn.getMaxRenderDistanceSquared()) {
            BlockPos blockpos;
            boolean setLightmap = true;
            if (Reflector.ForgeTileEntity_hasFastRenderer.exists()) {
                boolean bl = setLightmap = !this.drawingBatch || !Reflector.callBoolean((Object)tileentityIn, (ReflectorMethod)Reflector.ForgeTileEntity_hasFastRenderer, (Object[])new Object[0]);
            }
            if (setLightmap) {
                RenderHelper.enableStandardItemLighting();
                int i = this.world.getCombinedLight(tileentityIn.getPos(), 0);
                int j = i % 65536;
                int k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            }
            if (!this.world.isBlockLoaded(blockpos = tileentityIn.getPos(), false)) {
                return;
            }
            if (EmissiveTextures.isActive()) {
                EmissiveTextures.beginRender();
            }
            this.render(tileentityIn, (double)blockpos.p() - staticPlayerX, (double)blockpos.q() - staticPlayerY, (double)blockpos.r() - staticPlayerZ, partialTicks, destroyStage, 1.0f);
            if (EmissiveTextures.isActive()) {
                if (EmissiveTextures.hasEmissive()) {
                    EmissiveTextures.beginRenderEmissive();
                    this.render(tileentityIn, (double)blockpos.p() - staticPlayerX, (double)blockpos.q() - staticPlayerY, (double)blockpos.r() - staticPlayerZ, partialTicks, destroyStage, 1.0f);
                    EmissiveTextures.endRenderEmissive();
                }
                EmissiveTextures.endRender();
            }
        }
    }

    public void render(TileEntity tileEntityIn, double x, double y, double z, float partialTicks) {
        this.render(tileEntityIn, x, y, z, partialTicks, 1.0f);
    }

    public void render(TileEntity p_192855_1_, double p_192855_2_, double p_192855_4_, double p_192855_6_, float p_192855_8_, float p_192855_9_) {
        this.render(p_192855_1_, p_192855_2_, p_192855_4_, p_192855_6_, p_192855_8_, -1, p_192855_9_);
    }

    public void render(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, float p_192854_10_) {
        TileEntitySpecialRenderer tileentityspecialrenderer = this.getRenderer(tileEntityIn);
        if (tileentityspecialrenderer != null) {
            try {
                this.tileEntityRendered = tileEntityIn;
                if (this.drawingBatch && Reflector.callBoolean((Object)tileEntityIn, (ReflectorMethod)Reflector.ForgeTileEntity_hasFastRenderer, (Object[])new Object[0])) {
                    tileentityspecialrenderer.renderTileEntityFast(tileEntityIn, x, y, z, partialTicks, destroyStage, p_192854_10_, this.batchBuffer.getBuffer());
                } else {
                    tileentityspecialrenderer.render(tileEntityIn, x, y, z, partialTicks, destroyStage, p_192854_10_);
                }
                this.tileEntityRendered = null;
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Rendering Block Entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
                tileEntityIn.addInfoToCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void setWorld(@Nullable World worldIn) {
        this.world = worldIn;
        if (worldIn == null) {
            this.entity = null;
        }
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public void preDrawBatch() {
        this.batchBuffer.getBuffer().begin(7, DefaultVertexFormats.BLOCK);
        this.drawingBatch = true;
    }

    public void drawBatch(int pass) {
        this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel((int)7425);
        } else {
            GlStateManager.shadeModel((int)7424);
        }
        if (pass > 0) {
            Vec3d cameraPos = (Vec3d)Reflector.call((ReflectorMethod)Reflector.ActiveRenderInfo_getCameraPosition, (Object[])new Object[0]);
            if (cameraPos != null) {
                this.batchBuffer.getBuffer().sortVertexData((float)cameraPos.x, (float)cameraPos.y, (float)cameraPos.z);
            } else {
                this.batchBuffer.getBuffer().sortVertexData(0.0f, 0.0f, 0.0f);
            }
        }
        this.batchBuffer.draw();
        RenderHelper.enableStandardItemLighting();
        this.drawingBatch = false;
    }
}
