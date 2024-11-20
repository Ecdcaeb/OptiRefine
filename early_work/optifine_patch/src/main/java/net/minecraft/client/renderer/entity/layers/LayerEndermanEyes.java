/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
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
 *  net.optifine.shaders.Shaders
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
import net.optifine.shaders.Shaders;

public class LayerEndermanEyes
implements LayerRenderer<EntityEnderman> {
    private static final ResourceLocation RES_ENDERMAN_EYES = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
    private final RenderEnderman endermanRenderer;

    public LayerEndermanEyes(RenderEnderman endermanRendererIn) {
        this.endermanRenderer = endermanRendererIn;
    }

    public void doRenderLayer(EntityEnderman entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.endermanRenderer.a(RES_ENDERMAN_EYES);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask((!entitylivingbaseIn.aX() ? 1 : 0) != 0);
        int i = 61680;
        int j = 61680;
        boolean k = false;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)61680.0f, (float)0.0f);
        GlStateManager.enableLighting();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        if (Config.isShaders()) {
            Shaders.beginSpiderEyes();
        }
        Config.getRenderGlobal().renderOverlayEyes = true;
        this.endermanRenderer.getMainModel().a((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Config.getRenderGlobal().renderOverlayEyes = false;
        if (Config.isShaders()) {
            Shaders.endSpiderEyes();
        }
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.endermanRenderer.c((EntityLiving)entitylivingbaseIn);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
