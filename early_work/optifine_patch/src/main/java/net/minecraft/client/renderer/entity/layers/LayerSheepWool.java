/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Object
 *  net.minecraft.client.model.ModelSheep1
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderSheep
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.passive.EntitySheep
 *  net.minecraft.item.EnumDyeColor
 *  net.minecraft.util.ResourceLocation
 *  net.optifine.CustomColors
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;

public class LayerSheepWool
implements LayerRenderer<EntitySheep> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final RenderSheep sheepRenderer;
    public ModelSheep1 sheepModel = new ModelSheep1();

    public LayerSheepWool(RenderSheep sheepRendererIn) {
        this.sheepRenderer = sheepRendererIn;
    }

    public void doRenderLayer(EntitySheep entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entitylivingbaseIn.getSheared() && !entitylivingbaseIn.aX()) {
            this.sheepRenderer.a(TEXTURE);
            if (entitylivingbaseIn.n_() && "jeb_".equals((Object)entitylivingbaseIn.bq())) {
                int i1 = 25;
                int i = entitylivingbaseIn.T / 25 + entitylivingbaseIn.S();
                int j = EnumDyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f = ((float)(entitylivingbaseIn.T % 25) + partialTicks) / 25.0f;
                float[] afloat1 = EntitySheep.getDyeRgb((EnumDyeColor)EnumDyeColor.byMetadata((int)k));
                float[] afloat2 = EntitySheep.getDyeRgb((EnumDyeColor)EnumDyeColor.byMetadata((int)l));
                if (Config.isCustomColors()) {
                    afloat1 = CustomColors.getSheepColors((EnumDyeColor)EnumDyeColor.byMetadata((int)k), (float[])afloat1);
                    afloat2 = CustomColors.getSheepColors((EnumDyeColor)EnumDyeColor.byMetadata((int)l), (float[])afloat2);
                }
                GlStateManager.color((float)(afloat1[0] * (1.0f - f) + afloat2[0] * f), (float)(afloat1[1] * (1.0f - f) + afloat2[1] * f), (float)(afloat1[2] * (1.0f - f) + afloat2[2] * f));
            } else {
                float[] afloat = EntitySheep.getDyeRgb((EnumDyeColor)entitylivingbaseIn.getFleeceColor());
                if (Config.isCustomColors()) {
                    afloat = CustomColors.getSheepColors((EnumDyeColor)entitylivingbaseIn.getFleeceColor(), (float[])afloat);
                }
                GlStateManager.color((float)afloat[0], (float)afloat[1], (float)afloat[2]);
            }
            this.sheepModel.a(this.sheepRenderer.b());
            this.sheepModel.setLivingAnimations((EntityLivingBase)entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.sheepModel.a((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
