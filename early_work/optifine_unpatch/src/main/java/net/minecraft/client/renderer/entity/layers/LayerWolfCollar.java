/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderWolf
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.util.ResourceLocation
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class LayerWolfCollar
implements LayerRenderer<EntityWolf> {
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    private final RenderWolf wolfRenderer;

    public LayerWolfCollar(RenderWolf renderWolf) {
        this.wolfRenderer = renderWolf;
    }

    public void doRenderLayer(EntityWolf entityWolf, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (!entityWolf.dl() || entityWolf.aX()) {
            return;
        }
        this.wolfRenderer.a(WOLF_COLLAR);
        float[] fArray = entityWolf.getCollarColor().getColorComponentValues();
        GlStateManager.color((float)fArray[0], (float)fArray[1], (float)fArray[2]);
        this.wolfRenderer.b().render((Entity)entityWolf, f, f2, f4, f5, f6, f7);
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
