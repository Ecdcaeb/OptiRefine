package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiDispenser extends GuiContainer {
   private static final ResourceLocation DISPENSER_GUI_TEXTURES = new ResourceLocation("textures/gui/container/dispenser.png");
   private final InventoryPlayer playerInventory;
   public IInventory dispenserInventory;

   public GuiDispenser(InventoryPlayer var1, IInventory var2) {
      super(new ContainerDispenser(☃, ☃));
      this.playerInventory = ☃;
      this.dispenserInventory = ☃;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      super.drawScreen(☃, ☃, ☃);
      this.renderHoveredToolTip(☃, ☃);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      String ☃ = this.dispenserInventory.getDisplayName().getUnformattedText();
      this.fontRenderer.drawString(☃, this.xSize / 2 - this.fontRenderer.getStringWidth(☃) / 2, 6, 4210752);
      this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(DISPENSER_GUI_TEXTURES);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
   }
}
