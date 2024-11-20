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
 *  net.minecraft.client.renderer.entity.RenderSpider
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.monster.EntitySpider
 *  net.minecraft.util.ResourceLocation
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class LayerSpiderEyes<T extends EntitySpider>
implements LayerRenderer<T> {
    private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");
    private final RenderSpider<T> spiderRenderer;

    public LayerSpiderEyes(RenderSpider<T> renderSpider) {
        this.spiderRenderer = renderSpider;
    }

    public void doRenderLayer(T t, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.spiderRenderer.a(SPIDER_EYES);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
        if (t.aX()) {
            GlStateManager.depthMask((boolean)false);
        } else {
            GlStateManager.depthMask((boolean)true);
        }
        int n = 61680;
        \u2603 = n % 65536;
        \u2603 = n / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)\u2603, (float)\u2603);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.spiderRenderer.b().render(t, f, f2, f4, f5, f6, f7);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        n = t.av();
        \u2603 = n % 65536;
        \u2603 = n / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)\u2603, (float)\u2603);
        this.spiderRenderer.c(t);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
