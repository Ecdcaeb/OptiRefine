package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiBrewingStand extends GuiContainer {
   private static final ResourceLocation BREWING_STAND_GUI_TEXTURES = new ResourceLocation("textures/gui/container/brewing_stand.png");
   private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};
   private final InventoryPlayer playerInventory;
   private final IInventory tileBrewingStand;

   public GuiBrewingStand(InventoryPlayer var1, IInventory var2) {
      super(new ContainerBrewingStand(☃, ☃));
      this.playerInventory = ☃;
      this.tileBrewingStand = ☃;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      super.drawScreen(☃, ☃, ☃);
      this.renderHoveredToolTip(☃, ☃);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      String ☃ = this.tileBrewingStand.getDisplayName().getUnformattedText();
      this.fontRenderer.drawString(☃, this.xSize / 2 - this.fontRenderer.getStringWidth(☃) / 2, 6, 4210752);
      this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(BREWING_STAND_GUI_TEXTURES);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
      int ☃xx = this.tileBrewingStand.getField(1);
      int ☃xxx = MathHelper.clamp((18 * ☃xx + 20 - 1) / 20, 0, 18);
      if (☃xxx > 0) {
         this.drawTexturedModalRect(☃ + 60, ☃x + 44, 176, 29, ☃xxx, 4);
      }

      int ☃xxxx = this.tileBrewingStand.getField(0);
      if (☃xxxx > 0) {
         int ☃xxxxx = (int)(28.0F * (1.0F - ☃xxxx / 400.0F));
         if (☃xxxxx > 0) {
            this.drawTexturedModalRect(☃ + 97, ☃x + 16, 176, 0, 9, ☃xxxxx);
         }

         ☃xxxxx = BUBBLELENGTHS[☃xxxx / 2 % 7];
         if (☃xxxxx > 0) {
            this.drawTexturedModalRect(☃ + 63, ☃x + 14 + 29 - ☃xxxxx, 185, 29 - ☃xxxxx, 12, ☃xxxxx);
         }
      }
   }
}
