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
 *  net.minecraft.client.renderer.entity.RenderDragon
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.boss.EntityDragon
 *  net.minecraft.util.ResourceLocation
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

public class LayerEnderDragonEyes
implements LayerRenderer<EntityDragon> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
    private final RenderDragon dragonRenderer;

    public LayerEnderDragonEyes(RenderDragon renderDragon) {
        this.dragonRenderer = renderDragon;
    }

    public void doRenderLayer(EntityDragon entityDragon, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.dragonRenderer.a(TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthFunc((int)514);
        int n = 61680;
        \u2603 = 61680;
        boolean \u26032 = false;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)61680.0f, (float)0.0f);
        GlStateManager.enableLighting();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.dragonRenderer.b().render((Entity)entityDragon, f, f2, f4, f5, f6, f7);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.dragonRenderer.c((EntityLiving)entityDragon);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.depthFunc((int)515);
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
