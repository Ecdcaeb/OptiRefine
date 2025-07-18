package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;

public class GuiFurnace extends GuiContainer {
   private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/furnace.png");
   private final InventoryPlayer playerInventory;
   private final IInventory tileFurnace;

   public GuiFurnace(InventoryPlayer var1, IInventory var2) {
      super(new ContainerFurnace(☃, ☃));
      this.playerInventory = ☃;
      this.tileFurnace = ☃;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      super.drawScreen(☃, ☃, ☃);
      this.renderHoveredToolTip(☃, ☃);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      String ☃ = this.tileFurnace.getDisplayName().getUnformattedText();
      this.fontRenderer.drawString(☃, this.xSize / 2 - this.fontRenderer.getStringWidth(☃) / 2, 6, 4210752);
      this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
      if (TileEntityFurnace.isBurning(this.tileFurnace)) {
         int ☃xx = this.getBurnLeftScaled(13);
         this.drawTexturedModalRect(☃ + 56, ☃x + 36 + 12 - ☃xx, 176, 12 - ☃xx, 14, ☃xx + 1);
      }

      int ☃xx = this.getCookProgressScaled(24);
      this.drawTexturedModalRect(☃ + 79, ☃x + 34, 176, 14, ☃xx + 1, 16);
   }

   private int getCookProgressScaled(int var1) {
      int ☃ = this.tileFurnace.getField(2);
      int ☃x = this.tileFurnace.getField(3);
      return ☃x != 0 && ☃ != 0 ? ☃ * ☃ / ☃x : 0;
   }

   private int getBurnLeftScaled(int var1) {
      int ☃ = this.tileFurnace.getField(1);
      if (☃ == 0) {
         ☃ = 200;
      }

      return this.tileFurnace.getField(0) * ☃ / ☃;
   }
}
