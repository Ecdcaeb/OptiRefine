package net.minecraft.client.renderer;

import com.google.common.collect.Ordering;
import java.util.Collection;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public abstract class InventoryEffectRenderer extends GuiContainer {
   protected boolean hasActivePotionEffects;

   public InventoryEffectRenderer(Container var1) {
      super(☃);
   }

   @Override
   public void initGui() {
      super.initGui();
      this.updateActivePotionEffects();
   }

   protected void updateActivePotionEffects() {
      if (this.mc.player.getActivePotionEffects().isEmpty()) {
         this.guiLeft = (this.width - this.xSize) / 2;
         this.hasActivePotionEffects = false;
      } else {
         this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
         this.hasActivePotionEffects = true;
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      super.drawScreen(☃, ☃, ☃);
      if (this.hasActivePotionEffects) {
         this.drawActivePotionEffects();
      }
   }

   private void drawActivePotionEffects() {
      int ☃ = this.guiLeft - 124;
      int ☃x = this.guiTop;
      int ☃xx = 166;
      Collection<PotionEffect> ☃xxx = this.mc.player.getActivePotionEffects();
      if (!☃xxx.isEmpty()) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableLighting();
         int ☃xxxx = 33;
         if (☃xxx.size() > 5) {
            ☃xxxx = 132 / (☃xxx.size() - 1);
         }

         for (PotionEffect ☃xxxxx : Ordering.natural().sortedCopy(☃xxx)) {
            Potion ☃xxxxxx = ☃xxxxx.getPotion();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
            this.drawTexturedModalRect(☃, ☃x, 0, 166, 140, 32);
            if (☃xxxxxx.hasStatusIcon()) {
               int ☃xxxxxxx = ☃xxxxxx.getStatusIconIndex();
               this.drawTexturedModalRect(☃ + 6, ☃x + 7, 0 + ☃xxxxxxx % 8 * 18, 198 + ☃xxxxxxx / 8 * 18, 18, 18);
            }

            String ☃xxxxxxx = I18n.format(☃xxxxxx.getName());
            if (☃xxxxx.getAmplifier() == 1) {
               ☃xxxxxxx = ☃xxxxxxx + " " + I18n.format("enchantment.level.2");
            } else if (☃xxxxx.getAmplifier() == 2) {
               ☃xxxxxxx = ☃xxxxxxx + " " + I18n.format("enchantment.level.3");
            } else if (☃xxxxx.getAmplifier() == 3) {
               ☃xxxxxxx = ☃xxxxxxx + " " + I18n.format("enchantment.level.4");
            }

            this.fontRenderer.drawStringWithShadow(☃xxxxxxx, ☃ + 10 + 18, ☃x + 6, 16777215);
            String ☃xxxxxxxx = Potion.getPotionDurationString(☃xxxxx, 1.0F);
            this.fontRenderer.drawStringWithShadow(☃xxxxxxxx, ☃ + 10 + 18, ☃x + 6 + 10, 8355711);
            ☃x += ☃xxxx;
         }
      }
   }
}
