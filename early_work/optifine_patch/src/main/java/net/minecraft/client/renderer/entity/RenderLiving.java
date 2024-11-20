/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
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
 *  net.optifine.shaders.Shaders
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
import net.optifine.shaders.Shaders;

public abstract class RenderLiving<T extends EntityLiving>
extends RenderLivingBase<T> {
    public RenderLiving(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    protected boolean canRenderName(T entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.n_() && entity == this.renderManager.pointedEntity);
    }

    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (livingEntity.getLeashed() && livingEntity.getLeashHolder() != null) {
            Entity entity = livingEntity.getLeashHolder();
            return camera.isBoundingBoxInFrustum(entity.getRenderBoundingBox());
        }
        return false;
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (!this.renderOutlines) {
            this.renderLeash(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    public void setLightmap(T entityLivingIn) {
        int i = entityLivingIn.av();
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
    }

    private double interpolateValue(double start, double end, double pct) {
        return start + (end - start) * pct;
    }

    protected void renderLeash(T entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks) {
        if (Config.isShaders() && Shaders.isShadowPass) {
            return;
        }
        Entity entity = entityLivingIn.getLeashHolder();
        if (entity != null) {
            y -= (1.6 - (double)((EntityLiving)entityLivingIn).H) * 0.5;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            double d0 = this.interpolateValue(entity.prevRotationYaw, entity.rotationYaw, partialTicks * 0.5f) * 0.01745329238474369;
            double d1 = this.interpolateValue(entity.prevRotationPitch, entity.rotationPitch, partialTicks * 0.5f) * 0.01745329238474369;
            double d2 = Math.cos((double)d0);
            double d3 = Math.sin((double)d0);
            double d4 = Math.sin((double)d1);
            if (entity instanceof EntityHanging) {
                d2 = 0.0;
                d3 = 0.0;
                d4 = -1.0;
            }
            double d5 = Math.cos((double)d1);
            double d6 = this.interpolateValue(entity.prevPosX, entity.posX, partialTicks) - d2 * 0.7 - d3 * 0.5 * d5;
            double d7 = this.interpolateValue(entity.prevPosY + (double)entity.getEyeHeight() * 0.7, entity.posY + (double)entity.getEyeHeight() * 0.7, partialTicks) - d4 * 0.5 - 0.25;
            double d8 = this.interpolateValue(entity.prevPosZ, entity.posZ, partialTicks) - d3 * 0.7 + d2 * 0.5 * d5;
            double d9 = this.interpolateValue(((EntityLiving)entityLivingIn).prevRenderYawOffset, ((EntityLiving)entityLivingIn).renderYawOffset, partialTicks) * 0.01745329238474369 + 1.5707963267948966;
            d2 = Math.cos((double)d9) * (double)((EntityLiving)entityLivingIn).G * 0.4;
            d3 = Math.sin((double)d9) * (double)((EntityLiving)entityLivingIn).G * 0.4;
            double d10 = this.interpolateValue(((EntityLiving)entityLivingIn).m, ((EntityLiving)entityLivingIn).p, partialTicks) + d2;
            double d11 = this.interpolateValue(((EntityLiving)entityLivingIn).n, ((EntityLiving)entityLivingIn).q, partialTicks);
            double d12 = this.interpolateValue(((EntityLiving)entityLivingIn).o, ((EntityLiving)entityLivingIn).r, partialTicks) + d3;
            x += d2;
            z += d3;
            double d13 = (float)(d6 - d10);
            double d14 = (float)(d7 - d11);
            double d15 = (float)(d8 - d12);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            if (Config.isShaders()) {
                Shaders.beginLeash();
            }
            int i = 24;
            double d16 = 0.025;
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
            for (int j = 0; j <= 24; ++j) {
                float f = 0.5f;
                float f1 = 0.4f;
                float f2 = 0.3f;
                if (j % 2 == 0) {
                    f *= 0.7f;
                    f1 *= 0.7f;
                    f2 *= 0.7f;
                }
                float f3 = (float)j / 24.0f;
                bufferbuilder.pos(x + d13 * (double)f3 + 0.0, y + d14 * (double)(f3 * f3 + f3) * 0.5 + (double)((24.0f - (float)j) / 18.0f + 0.125f), z + d15 * (double)f3).color(f, f1, f2, 1.0f).endVertex();
                bufferbuilder.pos(x + d13 * (double)f3 + 0.025, y + d14 * (double)(f3 * f3 + f3) * 0.5 + (double)((24.0f - (float)j) / 18.0f + 0.125f) + 0.025, z + d15 * (double)f3).color(f, f1, f2, 1.0f).endVertex();
            }
            tessellator.draw();
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
            for (int k = 0; k <= 24; ++k) {
                float f4 = 0.5f;
                float f5 = 0.4f;
                float f6 = 0.3f;
                if (k % 2 == 0) {
                    f4 *= 0.7f;
                    f5 *= 0.7f;
                    f6 *= 0.7f;
                }
                float f7 = (float)k / 24.0f;
                bufferbuilder.pos(x + d13 * (double)f7 + 0.0, y + d14 * (double)(f7 * f7 + f7) * 0.5 + (double)((24.0f - (float)k) / 18.0f + 0.125f) + 0.025, z + d15 * (double)f7).color(f4, f5, f6, 1.0f).endVertex();
                bufferbuilder.pos(x + d13 * (double)f7 + 0.025, y + d14 * (double)(f7 * f7 + f7) * 0.5 + (double)((24.0f - (float)k) / 18.0f + 0.125f), z + d15 * (double)f7 + 0.025).color(f4, f5, f6, 1.0f).endVertex();
            }
            tessellator.draw();
            if (Config.isShaders()) {
                Shaders.endLeash();
            }
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
        }
    }
}
