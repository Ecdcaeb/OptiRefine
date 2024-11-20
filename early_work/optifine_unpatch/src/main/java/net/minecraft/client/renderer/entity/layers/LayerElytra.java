/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelElytra
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.layers.LayerArmorBase
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerElytra
implements LayerRenderer<EntityLivingBase> {
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    protected final RenderLivingBase<?> renderPlayer;
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerElytra(RenderLivingBase<?> renderLivingBase) {
        this.renderPlayer = renderLivingBase;
    }

    public void doRenderLayer(EntityLivingBase entityLivingBase, float f8, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8;
        ItemStack itemStack = entityLivingBase.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (itemStack.getItem() != Items.ELYTRA) {
            return;
        }
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        if (entityLivingBase instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer)entityLivingBase;
            if (abstractClientPlayer.isPlayerInfoSet() && abstractClientPlayer.getLocationElytra() != null) {
                this.renderPlayer.bindTexture(abstractClientPlayer.getLocationElytra());
            } else if (abstractClientPlayer.hasPlayerInfo() && abstractClientPlayer.getLocationCape() != null && abstractClientPlayer.isWearing(EnumPlayerModelParts.CAPE)) {
                this.renderPlayer.bindTexture(abstractClientPlayer.getLocationCape());
            } else {
                this.renderPlayer.bindTexture(TEXTURE_ELYTRA);
            }
        } else {
            this.renderPlayer.bindTexture(TEXTURE_ELYTRA);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.125f);
        this.modelElytra.setRotationAngles(f8, f2, f4, f5, f6, f7, (Entity)entityLivingBase);
        this.modelElytra.render((Entity)entityLivingBase, f8, f2, f4, f5, f6, f7);
        if (itemStack.isItemEnchanted()) {
            LayerArmorBase.renderEnchantedGlint(this.renderPlayer, (EntityLivingBase)entityLivingBase, (ModelBase)this.modelElytra, (float)f8, (float)f2, (float)f3, (float)f4, (float)f5, (float)f6, (float)f7);
        }
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
