/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
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
 *  net.minecraft.item.ItemMap
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.storage.MapData
 *  net.minecraftforge.client.event.RenderItemInFrameEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
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
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class RenderItemFrame
extends Render<EntityItemFrame> {
    private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
    private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
    private final RenderItem itemRenderer;

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
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            boolean flag = itemstack.getItem() instanceof ItemMap;
            int i = flag ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
            GlStateManager.rotate((float)((float)i * 360.0f / 8.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            RenderItemInFrameEvent event = new RenderItemInFrameEvent(itemFrame, this);
            if (!MinecraftForge.EVENT_BUS.post((Event)event)) {
                if (flag) {
                    this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
                    GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                    float f = 0.0078125f;
                    GlStateManager.scale((float)0.0078125f, (float)0.0078125f, (float)0.0078125f);
                    GlStateManager.translate((float)-64.0f, (float)-64.0f, (float)0.0f);
                    MapData mapdata = ((ItemMap)itemstack.getItem()).getMapData(itemstack, itemFrame.l);
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
}
