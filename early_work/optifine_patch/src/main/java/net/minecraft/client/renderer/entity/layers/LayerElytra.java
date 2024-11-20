/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
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
 *  net.optifine.CustomItems
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
import net.optifine.CustomItems;

public class LayerElytra
implements LayerRenderer<EntityLivingBase> {
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    protected final RenderLivingBase<?> renderPlayer;
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerElytra(RenderLivingBase<?> p_i47185_1_) {
        this.renderPlayer = p_i47185_1_;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (itemstack.getItem() == Items.ELYTRA) {
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            if (entitylivingbaseIn instanceof AbstractClientPlayer) {
                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entitylivingbaseIn;
                if (abstractclientplayer.isPlayerInfoSet() && abstractclientplayer.getLocationElytra() != null) {
                    this.renderPlayer.bindTexture(abstractclientplayer.getLocationElytra());
                } else if (abstractclientplayer.hasElytraCape() && abstractclientplayer.hasPlayerInfo() && abstractclientplayer.getLocationCape() != null && abstractclientplayer.isWearing(EnumPlayerModelParts.CAPE)) {
                    this.renderPlayer.bindTexture(abstractclientplayer.getLocationCape());
                } else {
                    ResourceLocation locElytra = TEXTURE_ELYTRA;
                    if (Config.isCustomItems()) {
                        locElytra = CustomItems.getCustomElytraTexture((ItemStack)itemstack, (ResourceLocation)locElytra);
                    }
                    this.renderPlayer.bindTexture(locElytra);
                }
            } else {
                ResourceLocation locElytra = TEXTURE_ELYTRA;
                if (Config.isCustomItems()) {
                    locElytra = CustomItems.getCustomElytraTexture((ItemStack)itemstack, (ResourceLocation)locElytra);
                }
                this.renderPlayer.bindTexture(locElytra);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.125f);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, (Entity)entitylivingbaseIn);
            this.modelElytra.render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (itemstack.isItemEnchanted()) {
                LayerArmorBase.renderEnchantedGlint(this.renderPlayer, (EntityLivingBase)entitylivingbaseIn, (ModelBase)this.modelElytra, (float)limbSwing, (float)limbSwingAmount, (float)partialTicks, (float)ageInTicks, (float)netHeadYaw, (float)headPitch, (float)scale);
            }
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
