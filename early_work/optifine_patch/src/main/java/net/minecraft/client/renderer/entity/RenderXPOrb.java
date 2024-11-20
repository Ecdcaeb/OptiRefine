/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Object
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  net.optifine.CustomColors
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.optifine.CustomColors;

public class RenderXPOrb
extends Render<EntityXPOrb> {
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

    public RenderXPOrb(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }

    public void doRender(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.renderOutlines) {
            int col;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
            this.bindEntityTexture((Entity)entity);
            RenderHelper.enableStandardItemLighting();
            int i = entity.getTextureByXP();
            float f = (float)(i % 4 * 16 + 0) / 64.0f;
            float f1 = (float)(i % 4 * 16 + 16) / 64.0f;
            float f2 = (float)(i / 4 * 16 + 0) / 64.0f;
            float f3 = (float)(i / 4 * 16 + 16) / 64.0f;
            float f4 = 1.0f;
            float f5 = 0.5f;
            float f6 = 0.25f;
            int j = entity.getBrightnessForRender();
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)k, (float)l);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            float f8 = 255.0f;
            float f9 = ((float)entity.xpColor + partialTicks) / 2.0f;
            if (Config.isCustomColors()) {
                f9 = CustomColors.getXpOrbTimer((float)f9);
            }
            l = (int)((MathHelper.sin((float)(f9 + 0.0f)) + 1.0f) * 0.5f * 255.0f);
            int i1 = 255;
            int j1 = (int)((MathHelper.sin((float)(f9 + 4.1887903f)) + 1.0f) * 0.1f * 255.0f);
            GlStateManager.translate((float)0.0f, (float)0.1f, (float)0.0f);
            GlStateManager.rotate((float)(180.0f - this.renderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX), (float)1.0f, (float)0.0f, (float)0.0f);
            float f7 = 0.3f;
            GlStateManager.scale((float)0.3f, (float)0.3f, (float)0.3f);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            int red = l;
            int green = 255;
            int blue = j1;
            if (Config.isCustomColors() && (col = CustomColors.getXpOrbColor((float)f9)) >= 0) {
                red = col >> 16 & 0xFF;
                green = col >> 8 & 0xFF;
                blue = col >> 0 & 0xFF;
            }
            bufferbuilder.pos(-0.5, -0.25, 0.0).tex((double)f, (double)f3).color(red, green, blue, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
            bufferbuilder.pos(0.5, -0.25, 0.0).tex((double)f1, (double)f3).color(red, green, blue, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
            bufferbuilder.pos(0.5, 0.75, 0.0).tex((double)f1, (double)f2).color(red, green, blue, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
            bufferbuilder.pos(-0.5, 0.75, 0.0).tex((double)f, (double)f2).color(red, green, blue, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
        }
    }

    protected ResourceLocation getEntityTexture(EntityXPOrb entity) {
        return EXPERIENCE_ORB_TEXTURES;
    }
}
