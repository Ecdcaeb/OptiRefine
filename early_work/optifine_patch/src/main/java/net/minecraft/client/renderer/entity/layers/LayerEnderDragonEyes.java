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
 *  net.minecraft.client.renderer.entity.RenderDragon
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.boss.EntityDragon
 *  net.minecraft.util.ResourceLocation
 *  net.optifine.shaders.Shaders
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.Shaders;

public class LayerEnderDragonEyes
implements LayerRenderer<EntityDragon> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
    private final RenderDragon dragonRenderer;

    public LayerEnderDragonEyes(RenderDragon dragonRendererIn) {
        this.dragonRenderer = dragonRendererIn;
    }

    public void doRenderLayer(EntityDragon entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.dragonRenderer.a(TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthFunc((int)514);
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
        this.dragonRenderer.b().render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Config.getRenderGlobal().renderOverlayEyes = false;
        if (Config.isShaders()) {
            Shaders.endSpiderEyes();
        }
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.dragonRenderer.c((EntityLiving)entitylivingbaseIn);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.depthFunc((int)515);
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
