/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityHanging
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public abstract class RenderLiving<T extends EntityLiving>
extends RenderLivingBase<T> {
    public RenderLiving(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
    }

    protected boolean canRenderName(T t) {
        return super.canRenderName(t) && (t.getAlwaysRenderNameTagForRender() || t.n_() && t == this.renderManager.pointedEntity);
    }

    public boolean shouldRender(T t, ICamera iCamera, double d, double d2, double d3) {
        if (super.shouldRender(t, iCamera, d, d2, d3)) {
            return true;
        }
        if (t.getLeashed() && t.getLeashHolder() != null) {
            Entity entity = t.getLeashHolder();
            return iCamera.isBoundingBoxInFrustum(entity.getRenderBoundingBox());
        }
        return false;
    }

    public void doRender(T t, double d, double d2, double d3, float f, float f2) {
        super.doRender(t, d, d2, d3, f, f2);
        if (!this.renderOutlines) {
            this.renderLeash(t, d, d2, d3, f, f2);
        }
    }

    public void setLightmap(T t) {
        int n = t.av();
        \u2603 = n % 65536;
        \u2603 = n / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)\u2603, (float)\u2603);
    }

    private double interpolateValue(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }

    protected void renderLeash(T t, double d, double d2, double d3, float f, float f2) {
        float f3;
        int n;
        Entity entity = t.getLeashHolder();
        if (entity == null) {
            return;
        }
        d2 -= (1.6 - (double)((EntityLiving)t).H) * 0.5;
        Tessellator \u26032 = Tessellator.getInstance();
        BufferBuilder \u26033 = \u26032.getBuffer();
        double \u26034 = this.interpolateValue(entity.prevRotationYaw, entity.rotationYaw, f2 * 0.5f) * 0.01745329238474369;
        double \u26035 = this.interpolateValue(entity.prevRotationPitch, entity.rotationPitch, f2 * 0.5f) * 0.01745329238474369;
        double \u26036 = Math.cos((double)\u26034);
        double \u26037 = Math.sin((double)\u26034);
        double \u26038 = Math.sin((double)\u26035);
        if (entity instanceof EntityHanging) {
            \u26036 = 0.0;
            \u26037 = 0.0;
            \u26038 = -1.0;
        }
        double \u26039 = Math.cos((double)\u26035);
        double \u260310 = this.interpolateValue(entity.prevPosX, entity.posX, f2) - \u26036 * 0.7 - \u26037 * 0.5 * \u26039;
        double \u260311 = this.interpolateValue(entity.prevPosY + (double)entity.getEyeHeight() * 0.7, entity.posY + (double)entity.getEyeHeight() * 0.7, f2) - \u26038 * 0.5 - 0.25;
        double \u260312 = this.interpolateValue(entity.prevPosZ, entity.posZ, f2) - \u26037 * 0.7 + \u26036 * 0.5 * \u26039;
        double \u260313 = this.interpolateValue(((EntityLiving)t).prevRenderYawOffset, ((EntityLiving)t).renderYawOffset, f2) * 0.01745329238474369 + 1.5707963267948966;
        \u26036 = Math.cos((double)\u260313) * (double)((EntityLiving)t).G * 0.4;
        \u26037 = Math.sin((double)\u260313) * (double)((EntityLiving)t).G * 0.4;
        double \u260314 = this.interpolateValue(((EntityLiving)t).m, ((EntityLiving)t).p, f2) + \u26036;
        double \u260315 = this.interpolateValue(((EntityLiving)t).n, ((EntityLiving)t).q, f2);
        double \u260316 = this.interpolateValue(((EntityLiving)t).o, ((EntityLiving)t).r, f2) + \u26037;
        d += \u26036;
        d3 += \u26037;
        double \u260317 = (float)(\u260310 - \u260314);
        double \u260318 = (float)(\u260311 - \u260315);
        double \u260319 = (float)(\u260312 - \u260316);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        int \u260320 = 24;
        double \u260321 = 0.025;
        \u26033.begin(5, DefaultVertexFormats.POSITION_COLOR);
        for (n = 0; n <= 24; ++n) {
            f3 = 0.5f;
            \u2603 = 0.4f;
            \u2603 = 0.3f;
            if (n % 2 == 0) {
                f3 *= 0.7f;
                \u2603 *= 0.7f;
                \u2603 *= 0.7f;
            }
            \u2603 = (float)n / 24.0f;
            \u26033.pos(d + \u260317 * (double)\u2603 + 0.0, d2 + \u260318 * (double)(\u2603 * \u2603 + \u2603) * 0.5 + (double)((24.0f - (float)n) / 18.0f + 0.125f), d3 + \u260319 * (double)\u2603).color(f3, \u2603, \u2603, 1.0f).endVertex();
            \u26033.pos(d + \u260317 * (double)\u2603 + 0.025, d2 + \u260318 * (double)(\u2603 * \u2603 + \u2603) * 0.5 + (double)((24.0f - (float)n) / 18.0f + 0.125f) + 0.025, d3 + \u260319 * (double)\u2603).color(f3, \u2603, \u2603, 1.0f).endVertex();
        }
        \u26032.draw();
        \u26033.begin(5, DefaultVertexFormats.POSITION_COLOR);
        for (n = 0; n <= 24; ++n) {
            f3 = 0.5f;
            \u2603 = 0.4f;
            \u2603 = 0.3f;
            if (n % 2 == 0) {
                f3 *= 0.7f;
                \u2603 *= 0.7f;
                \u2603 *= 0.7f;
            }
            \u2603 = (float)n / 24.0f;
            \u26033.pos(d + \u260317 * (double)\u2603 + 0.0, d2 + \u260318 * (double)(\u2603 * \u2603 + \u2603) * 0.5 + (double)((24.0f - (float)n) / 18.0f + 0.125f) + 0.025, d3 + \u260319 * (double)\u2603).color(f3, \u2603, \u2603, 1.0f).endVertex();
            \u26033.pos(d + \u260317 * (double)\u2603 + 0.025, d2 + \u260318 * (double)(\u2603 * \u2603 + \u2603) * 0.5 + (double)((24.0f - (float)n) / 18.0f + 0.125f), d3 + \u260319 * (double)\u2603 + 0.025).color(f3, \u2603, \u2603, 1.0f).endVertex();
        }
        \u26032.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
    }

    protected /* synthetic */ boolean canRenderName(EntityLivingBase entityLivingBase) {
        return this.canRenderName((EntityLiving)entityLivingBase);
    }
}
