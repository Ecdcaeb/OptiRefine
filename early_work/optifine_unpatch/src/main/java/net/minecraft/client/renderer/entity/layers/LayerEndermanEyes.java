/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.RenderEnderman
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.monster.EntityEnderman
 *  net.minecraft.util.ResourceLocation
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;

public class LayerEndermanEyes
implements LayerRenderer<EntityEnderman> {
    private static final ResourceLocation RES_ENDERMAN_EYES = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
    private final RenderEnderman endermanRenderer;

    public LayerEndermanEyes(RenderEnderman renderEnderman) {
        this.endermanRenderer = renderEnderman;
    }

    public void doRenderLayer(EntityEnderman entityEnderman, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.endermanRenderer.a(RES_ENDERMAN_EYES);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask((!entityEnderman.aX() ? 1 : 0) != 0);
        int n = 61680;
        \u2603 = 61680;
        boolean \u26032 = false;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)61680.0f, (float)0.0f);
        GlStateManager.enableLighting();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.endermanRenderer.getMainModel().a((Entity)entityEnderman, f, f2, f4, f5, f6, f7);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.endermanRenderer.c((EntityLiving)entityEnderman);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
