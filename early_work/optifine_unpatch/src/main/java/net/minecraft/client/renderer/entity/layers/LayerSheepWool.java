/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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

public class LayerSheepWool
implements LayerRenderer<EntitySheep> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final RenderSheep sheepRenderer;
    private final ModelSheep1 sheepModel = new ModelSheep1();

    public LayerSheepWool(RenderSheep renderSheep) {
        this.sheepRenderer = renderSheep;
    }

    public void doRenderLayer(EntitySheep entitySheep, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (entitySheep.getSheared() || entitySheep.aX()) {
            return;
        }
        this.sheepRenderer.a(TEXTURE);
        if (entitySheep.n_() && "jeb_".equals((Object)entitySheep.bq())) {
            int n = 25;
            \u2603 = entitySheep.T / 25 + entitySheep.S();
            \u2603 = EnumDyeColor.values().length;
            \u2603 = \u2603 % \u2603;
            \u2603 = (\u2603 + 1) % \u2603;
            float \u26032 = ((float)(entitySheep.T % 25) + f3) / 25.0f;
            float[] \u26033 = EntitySheep.getDyeRgb((EnumDyeColor)EnumDyeColor.byMetadata((int)\u2603));
            float[] \u26034 = EntitySheep.getDyeRgb((EnumDyeColor)EnumDyeColor.byMetadata((int)\u2603));
            GlStateManager.color((float)(\u26033[0] * (1.0f - \u26032) + \u26034[0] * \u26032), (float)(\u26033[1] * (1.0f - \u26032) + \u26034[1] * \u26032), (float)(\u26033[2] * (1.0f - \u26032) + \u26034[2] * \u26032));
        } else {
            float[] \u26035 = EntitySheep.getDyeRgb((EnumDyeColor)entitySheep.getFleeceColor());
            GlStateManager.color((float)\u26035[0], (float)\u26035[1], (float)\u26035[2]);
        }
        this.sheepModel.a(this.sheepRenderer.b());
        this.sheepModel.setLivingAnimations((EntityLivingBase)entitySheep, f, f2, f3);
        this.sheepModel.a((Entity)entitySheep, f, f2, f4, f5, f6, f7);
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
