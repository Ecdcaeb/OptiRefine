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
 *  net.minecraft.client.renderer.entity.RenderSpider
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.monster.EntitySpider
 *  net.minecraft.util.ResourceLocation
 *  net.optifine.shaders.Shaders
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.Shaders;

public class LayerSpiderEyes<T extends EntitySpider>
implements LayerRenderer<T> {
    private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");
    private final RenderSpider<T> spiderRenderer;

    public LayerSpiderEyes(RenderSpider<T> spiderRendererIn) {
        this.spiderRenderer = spiderRendererIn;
    }

    public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.spiderRenderer.a(SPIDER_EYES);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        if (entitylivingbaseIn.aX()) {
            GlStateManager.depthMask((boolean)false);
        } else {
            GlStateManager.depthMask((boolean)true);
        }
        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        if (Config.isShaders()) {
            Shaders.beginSpiderEyes();
        }
        Config.getRenderGlobal().renderOverlayEyes = true;
        this.spiderRenderer.b().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Config.getRenderGlobal().renderOverlayEyes = false;
        if (Config.isShaders()) {
            Shaders.endSpiderEyes();
        }
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        i = entitylivingbaseIn.av();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        this.spiderRenderer.c(entitylivingbaseIn);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
