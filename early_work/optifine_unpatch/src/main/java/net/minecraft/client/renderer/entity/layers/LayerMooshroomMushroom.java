/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BlockRendererDispatcher
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$CullFace
 *  net.minecraft.client.renderer.entity.RenderMooshroom
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.entity.passive.EntityMooshroom
 *  net.minecraft.init.Blocks
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Blocks;

public class LayerMooshroomMushroom
implements LayerRenderer<EntityMooshroom> {
    private final RenderMooshroom mooshroomRenderer;

    public LayerMooshroomMushroom(RenderMooshroom renderMooshroom) {
        this.mooshroomRenderer = renderMooshroom;
    }

    public void doRenderLayer(EntityMooshroom entityMooshroom, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (entityMooshroom.l_() || entityMooshroom.aX()) {
            return;
        }
        BlockRendererDispatcher blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        this.mooshroomRenderer.a(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.enableCull();
        GlStateManager.cullFace((GlStateManager.CullFace)GlStateManager.CullFace.FRONT);
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)1.0f, (float)-1.0f, (float)1.0f);
        GlStateManager.translate((float)0.2f, (float)0.35f, (float)0.5f);
        GlStateManager.rotate((float)42.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)0.5f);
        blockRendererDispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.t(), 1.0f);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)0.1f, (float)0.0f, (float)-0.6f);
        GlStateManager.rotate((float)42.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)0.5f);
        blockRendererDispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.t(), 1.0f);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.mooshroomRenderer.getMainModel().a.postRender(0.0625f);
        GlStateManager.scale((float)1.0f, (float)-1.0f, (float)1.0f);
        GlStateManager.translate((float)0.0f, (float)0.7f, (float)-0.2f);
        GlStateManager.rotate((float)12.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)0.5f);
        blockRendererDispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.t(), 1.0f);
        GlStateManager.popMatrix();
        GlStateManager.cullFace((GlStateManager.CullFace)GlStateManager.CullFace.BACK);
        GlStateManager.disableCull();
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
