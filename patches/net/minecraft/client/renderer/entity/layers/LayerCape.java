package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class LayerCape implements LayerRenderer<AbstractClientPlayer> {
   private final RenderPlayer playerRenderer;

   public LayerCape(RenderPlayer var1) {
      this.playerRenderer = ☃;
   }

   public void doRenderLayer(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (☃.hasPlayerInfo() && !☃.isInvisible() && ☃.isWearing(EnumPlayerModelParts.CAPE) && ☃.getLocationCape() != null) {
         ItemStack ☃ = ☃.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
         if (☃.getItem() != Items.ELYTRA) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.playerRenderer.bindTexture(☃.getLocationCape());
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            double ☃x = ☃.prevChasingPosX + (☃.chasingPosX - ☃.prevChasingPosX) * ☃ - (☃.prevPosX + (☃.posX - ☃.prevPosX) * ☃);
            double ☃xx = ☃.prevChasingPosY + (☃.chasingPosY - ☃.prevChasingPosY) * ☃ - (☃.prevPosY + (☃.posY - ☃.prevPosY) * ☃);
            double ☃xxx = ☃.prevChasingPosZ + (☃.chasingPosZ - ☃.prevChasingPosZ) * ☃ - (☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * ☃);
            float ☃xxxx = ☃.prevRenderYawOffset + (☃.renderYawOffset - ☃.prevRenderYawOffset) * ☃;
            double ☃xxxxx = MathHelper.sin(☃xxxx * (float) (Math.PI / 180.0));
            double ☃xxxxxx = -MathHelper.cos(☃xxxx * (float) (Math.PI / 180.0));
            float ☃xxxxxxx = (float)☃xx * 10.0F;
            ☃xxxxxxx = MathHelper.clamp(☃xxxxxxx, -6.0F, 32.0F);
            float ☃xxxxxxxx = (float)(☃x * ☃xxxxx + ☃xxx * ☃xxxxxx) * 100.0F;
            float ☃xxxxxxxxx = (float)(☃x * ☃xxxxxx - ☃xxx * ☃xxxxx) * 100.0F;
            if (☃xxxxxxxx < 0.0F) {
               ☃xxxxxxxx = 0.0F;
            }

            float ☃xxxxxxxxxx = ☃.prevCameraYaw + (☃.cameraYaw - ☃.prevCameraYaw) * ☃;
            ☃xxxxxxx += MathHelper.sin((☃.prevDistanceWalkedModified + (☃.distanceWalkedModified - ☃.prevDistanceWalkedModified) * ☃) * 6.0F)
               * 32.0F
               * ☃xxxxxxxxxx;
            if (☃.isSneaking()) {
               ☃xxxxxxx += 25.0F;
            }

            GlStateManager.rotate(6.0F + ☃xxxxxxxx / 2.0F + ☃xxxxxxx, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(☃xxxxxxxxx / 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-☃xxxxxxxxx / 2.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            this.playerRenderer.getMainModel().renderCape(0.0625F);
            GlStateManager.popMatrix();
         }
      }
   }

   @Override
   public boolean shouldCombineTextures() {
      return false;
   }
}
