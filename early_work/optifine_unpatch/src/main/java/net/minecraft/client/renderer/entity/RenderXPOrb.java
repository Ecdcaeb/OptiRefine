/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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

public class RenderXPOrb
extends Render<EntityXPOrb> {
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

    public RenderXPOrb(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }

    public void doRender(EntityXPOrb entityXPOrb, double d, double d2, double d3, float f, float f2) {
        if (this.renderOutlines) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)((float)d), (float)((float)d2), (float)((float)d3));
        this.bindEntityTexture((Entity)entityXPOrb);
        RenderHelper.enableStandardItemLighting();
        int n = entityXPOrb.getTextureByXP();
        float \u26032 = (float)(n % 4 * 16 + 0) / 64.0f;
        float \u26033 = (float)(n % 4 * 16 + 16) / 64.0f;
        float \u26034 = (float)(n / 4 * 16 + 0) / 64.0f;
        float \u26035 = (float)(n / 4 * 16 + 16) / 64.0f;
        float \u26036 = 1.0f;
        float \u26037 = 0.5f;
        float \u26038 = 0.25f;
        \u2603 = entityXPOrb.getBrightnessForRender();
        \u2603 = \u2603 % 65536;
        \u2603 = \u2603 / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)\u2603, (float)\u2603);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float \u26039 = 255.0f;
        float \u260310 = ((float)entityXPOrb.xpColor + f2) / 2.0f;
        \u2603 = (int)((MathHelper.sin((float)(\u260310 + 0.0f)) + 1.0f) * 0.5f * 255.0f);
        \u2603 = 255;
        \u2603 = (int)((MathHelper.sin((float)(\u260310 + 4.1887903f)) + 1.0f) * 0.1f * 255.0f);
        GlStateManager.translate((float)0.0f, (float)0.1f, (float)0.0f);
        GlStateManager.rotate((float)(180.0f - this.renderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX), (float)1.0f, (float)0.0f, (float)0.0f);
        float \u260311 = 0.3f;
        GlStateManager.scale((float)0.3f, (float)0.3f, (float)0.3f);
        Tessellator \u260312 = Tessellator.getInstance();
        BufferBuilder \u260313 = \u260312.getBuffer();
        \u260313.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        \u260313.pos(-0.5, -0.25, 0.0).tex((double)\u26032, (double)\u26035).color(\u2603, 255, \u2603, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        \u260313.pos(0.5, -0.25, 0.0).tex((double)\u26033, (double)\u26035).color(\u2603, 255, \u2603, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        \u260313.pos(0.5, 0.75, 0.0).tex((double)\u26033, (double)\u26034).color(\u2603, 255, \u2603, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        \u260313.pos(-0.5, 0.75, 0.0).tex((double)\u26032, (double)\u26034).color(\u2603, 255, \u2603, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        \u260312.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender((Entity)entityXPOrb, d, d2, d3, f, f2);
    }

    protected ResourceLocation getEntityTexture(EntityXPOrb entityXPOrb) {
        return EXPERIENCE_ORB_TEXTURES;
    }
}
