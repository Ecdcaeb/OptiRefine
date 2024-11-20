/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderWolf
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.item.EnumDyeColor
 *  net.minecraft.util.ResourceLocation
 *  net.optifine.CustomColors
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;

public class LayerWolfCollar
implements LayerRenderer<EntityWolf> {
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    private final RenderWolf wolfRenderer;

    public LayerWolfCollar(RenderWolf wolfRendererIn) {
        this.wolfRenderer = wolfRendererIn;
    }

    public void doRenderLayer(EntityWolf entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn.dl() && !entitylivingbaseIn.aX()) {
            this.wolfRenderer.a(WOLF_COLLAR);
            float[] afloat = entitylivingbaseIn.getCollarColor().getColorComponentValues();
            if (Config.isCustomColors()) {
                afloat = CustomColors.getWolfCollarColors((EnumDyeColor)entitylivingbaseIn.getCollarColor(), (float[])afloat);
            }
            GlStateManager.color((float)afloat[0], (float)afloat[1], (float)afloat[2]);
            this.wolfRenderer.b().render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
