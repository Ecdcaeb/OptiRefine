/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderPlayer
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.MathHelper
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class LayerCape
implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public LayerCape(RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
    }

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack itemstack;
        if (entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.aX() && entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE) && entitylivingbaseIn.getLocationCape() != null && (itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST)).getItem() != Items.ELYTRA) {
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.playerRenderer.a(entitylivingbaseIn.getLocationCape());
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.125f);
            double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double)partialTicks - (entitylivingbaseIn.m + (entitylivingbaseIn.p - entitylivingbaseIn.m) * (double)partialTicks);
            double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double)partialTicks - (entitylivingbaseIn.n + (entitylivingbaseIn.q - entitylivingbaseIn.n) * (double)partialTicks);
            double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double)partialTicks - (entitylivingbaseIn.o + (entitylivingbaseIn.r - entitylivingbaseIn.o) * (double)partialTicks);
            float f = entitylivingbaseIn.aO + (entitylivingbaseIn.aN - entitylivingbaseIn.aO) * partialTicks;
            double d3 = MathHelper.sin((float)(f * ((float)Math.PI / 180)));
            double d4 = -MathHelper.cos((float)(f * ((float)Math.PI / 180)));
            float f1 = (float)d1 * 10.0f;
            f1 = MathHelper.clamp((float)f1, (float)-6.0f, (float)32.0f);
            float f2 = (float)(d0 * d3 + d2 * d4) * 100.0f;
            float f3 = (float)(d0 * d4 - d2 * d3) * 100.0f;
            if (f2 < 0.0f) {
                f2 = 0.0f;
            }
            if (f2 > 165.0f) {
                f2 = 165.0f;
            }
            if (f1 < -5.0f) {
                f1 = -5.0f;
            }
            float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
            f1 += MathHelper.sin((float)((entitylivingbaseIn.I + (entitylivingbaseIn.J - entitylivingbaseIn.I) * partialTicks) * 6.0f)) * 32.0f * f4;
            if (entitylivingbaseIn.aU()) {
                f1 += 25.0f;
                GlStateManager.translate((float)0.0f, (float)0.142f, (float)-0.0178f);
            }
            GlStateManager.rotate((float)(6.0f + f2 / 2.0f + f1), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)(f3 / 2.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.rotate((float)(-f3 / 2.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            this.playerRenderer.getMainModel().renderCape(0.0625f);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
