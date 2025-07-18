package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerElytra implements LayerRenderer<EntityLivingBase> {
   private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
   protected final RenderLivingBase<?> renderPlayer;
   private final ModelElytra modelElytra = new ModelElytra();

   public LayerElytra(RenderLivingBase<?> var1) {
      this.renderPlayer = ☃;
   }

   @Override
   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
      if (☃.getItem() == Items.ELYTRA) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
         if (☃ instanceof AbstractClientPlayer) {
            AbstractClientPlayer ☃x = (AbstractClientPlayer)☃;
            if (☃x.isPlayerInfoSet() && ☃x.getLocationElytra() != null) {
               this.renderPlayer.bindTexture(☃x.getLocationElytra());
            } else if (☃x.hasPlayerInfo() && ☃x.getLocationCape() != null && ☃x.isWearing(EnumPlayerModelParts.CAPE)) {
               this.renderPlayer.bindTexture(☃x.getLocationCape());
            } else {
               this.renderPlayer.bindTexture(TEXTURE_ELYTRA);
            }
         } else {
            this.renderPlayer.bindTexture(TEXTURE_ELYTRA);
         }

         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, 0.0F, 0.125F);
         this.modelElytra.setRotationAngles(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         this.modelElytra.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         if (☃.isItemEnchanted()) {
            LayerArmorBase.renderEnchantedGlint(this.renderPlayer, ☃, this.modelElytra, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
         }

         GlStateManager.disableBlend();
         GlStateManager.popMatrix();
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
