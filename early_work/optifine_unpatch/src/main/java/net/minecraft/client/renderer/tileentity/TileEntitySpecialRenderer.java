/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.entity.Entity
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public abstract class TileEntitySpecialRenderer<T extends TileEntity> {
    protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png")};
    protected TileEntityRendererDispatcher rendererDispatcher;

    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ITextComponent itextcomponent = te.getDisplayName();
        if (itextcomponent != null && this.rendererDispatcher.cameraHitResult != null && te.getPos().equals((Object)this.rendererDispatcher.cameraHitResult.getBlockPos())) {
            this.setLightmapDisabled(true);
            this.drawNameplate(te, itextcomponent.getFormattedText(), x, y, z, 12);
            this.setLightmapDisabled(false);
        }
    }

    protected void setLightmapDisabled(boolean disabled) {
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        if (disabled) {
            GlStateManager.disableTexture2D();
        } else {
            GlStateManager.enableTexture2D();
        }
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
    }

    protected void bindTexture(ResourceLocation location) {
        TextureManager texturemanager = this.rendererDispatcher.renderEngine;
        if (texturemanager != null) {
            texturemanager.bindTexture(location);
        }
    }

    protected World getWorld() {
        return this.rendererDispatcher.world;
    }

    public void setRendererDispatcher(TileEntityRendererDispatcher rendererDispatcherIn) {
        this.rendererDispatcher = rendererDispatcherIn;
    }

    public FontRenderer getFontRenderer() {
        return this.rendererDispatcher.getFontRenderer();
    }

    public boolean isGlobalRenderer(T te) {
        return false;
    }

    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
    }

    protected void drawNameplate(T te, String str, double x, double y, double z, int maxDistance) {
        Entity entity = this.rendererDispatcher.entity;
        double d0 = te.getDistanceSq(entity.posX, entity.posY, entity.posZ);
        if (d0 <= (double)(maxDistance * maxDistance)) {
            float f = this.rendererDispatcher.entityYaw;
            float f1 = this.rendererDispatcher.entityPitch;
            boolean flag = false;
            EntityRenderer.drawNameplate((FontRenderer)this.getFontRenderer(), (String)str, (float)((float)x + 0.5f), (float)((float)y + 1.5f), (float)((float)z + 0.5f), (int)0, (float)f, (float)f1, (boolean)false, (boolean)false);
        }
    }
}
