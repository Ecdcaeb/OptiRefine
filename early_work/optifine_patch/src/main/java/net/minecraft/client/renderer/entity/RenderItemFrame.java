/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.BlockRendererDispatcher
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.block.model.ModelManager
 *  net.minecraft.client.renderer.block.model.ModelResourceLocation
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemMap
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  net.minecraft.world.storage.MapData
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorConstructor
 *  net.optifine.reflect.ReflectorForge
 *  net.optifine.shaders.Shaders
 */
package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorConstructor;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.Shaders;

public class RenderItemFrame
extends Render<EntityItemFrame> {
    private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
    private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
    private final RenderItem itemRenderer;
    private static double itemRenderDistanceSq = 4096.0;

    public RenderItemFrame(RenderManager renderManagerIn, RenderItem itemRendererIn) {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
    }

    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        BlockPos blockpos = entity.q();
        double d0 = (double)blockpos.p() - entity.p + x;
        double d1 = (double)blockpos.q() - entity.q + y;
        double d2 = (double)blockpos.r() - entity.r + z;
        GlStateManager.translate((double)(d0 + 0.5), (double)(d1 + 0.5), (double)(d2 + 0.5));
        GlStateManager.rotate((float)(180.0f - entity.v), (float)0.0f, (float)1.0f, (float)0.0f);
        this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        IBakedModel ibakedmodel = entity.getDisplayedItem().getItem() instanceof ItemMap ? modelmanager.getModel(this.mapModel) : modelmanager.getModel(this.itemFrameModel);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)-0.5f);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode((int)this.getTeamColor((Entity)entity));
        }
        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0f, 1.0f, 1.0f, 1.0f);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.4375f);
        this.renderItem(entity);
        GlStateManager.popMatrix();
        this.renderName(entity, x + (double)((float)entity.b.getXOffset() * 0.3f), y - 0.25, z + (double)((float)entity.b.getZOffset() * 0.3f));
    }

    @Nullable
    protected ResourceLocation getEntityTexture(EntityItemFrame entity) {
        return null;
    }

    private void renderItem(EntityItemFrame itemFrame) {
        ItemStack itemstack = itemFrame.getDisplayedItem();
        if (!itemstack.isEmpty()) {
            if (!this.isRenderItem(itemFrame)) {
                return;
            }
            if (!Config.zoomMode) {
                EntityPlayerSP player = this.mc.player;
                double distSq = itemFrame.d(player.posX, player.posY, player.posZ);
                if (distSq > 4096.0) {
                    return;
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            boolean flag = itemstack.getItem() instanceof ItemMap;
            int i = flag ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
            GlStateManager.rotate((float)((float)i * 360.0f / 8.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            if (!Reflector.postForgeBusEvent((ReflectorConstructor)Reflector.RenderItemInFrameEvent_Constructor, (Object[])new Object[]{itemFrame, this})) {
                if (flag) {
                    this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
                    GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                    float f = 0.0078125f;
                    GlStateManager.scale((float)0.0078125f, (float)0.0078125f, (float)0.0078125f);
                    GlStateManager.translate((float)-64.0f, (float)-64.0f, (float)0.0f);
                    MapData mapdata = ReflectorForge.getMapData((ItemMap)Items.FILLED_MAP, (ItemStack)itemstack, (World)itemFrame.l);
                    GlStateManager.translate((float)0.0f, (float)0.0f, (float)-1.0f);
                    if (mapdata != null) {
                        this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
                    }
                } else {
                    GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
                    GlStateManager.pushAttrib();
                    RenderHelper.enableStandardItemLighting();
                    this.itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.popAttrib();
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    protected void renderName(EntityItemFrame entity, double x, double y, double z) {
        if (Minecraft.isGuiEnabled() && !entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity) {
            float f;
            double d0 = entity.h(this.renderManager.renderViewEntity);
            float f2 = f = entity.aU() ? 32.0f : 64.0f;
            if (d0 < (double)(f * f)) {
                String s = entity.getDisplayedItem().getDisplayName();
                this.renderLivingLabel((Entity)entity, s, x, y, z, 64);
            }
        }
    }

    private boolean isRenderItem(EntityItemFrame itemFrame) {
        if (Shaders.isShadowPass) {
            return false;
        }
        if (!Config.zoomMode) {
            Entity viewEntity = this.mc.getRenderViewEntity();
            double distSq = itemFrame.d(viewEntity.posX, viewEntity.posY, viewEntity.posZ);
            if (distSq > itemRenderDistanceSq) {
                return false;
            }
        }
        return true;
    }

    public static void updateItemRenderDistance() {
        Minecraft mc = Config.getMinecraft();
        double fov = Config.limit((float)mc.gameSettings.fovSetting, (float)1.0f, (float)120.0f);
        double itemRenderDistance = Math.max((double)(6.0 * (double)mc.displayHeight / fov), (double)16.0);
        itemRenderDistanceSq = itemRenderDistance * itemRenderDistance;
    }
}
