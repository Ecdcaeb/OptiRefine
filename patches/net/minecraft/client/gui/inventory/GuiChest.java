package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiChest extends GuiContainer {
   private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
   private final IInventory upperChestInventory;
   private final IInventory lowerChestInventory;
   private final int inventoryRows;

   public GuiChest(IInventory var1, IInventory var2) {
      super(new ContainerChest(☃, ☃, Minecraft.getMinecraft().player));
      this.upperChestInventory = ☃;
      this.lowerChestInventory = ☃;
      this.allowUserInput = false;
      int ☃ = 222;
      int ☃x = 114;
      this.inventoryRows = ☃.getSizeInventory() / 9;
      this.ySize = 114 + this.inventoryRows * 18;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      super.drawScreen(☃, ☃, ☃);
      this.renderHoveredToolTip(☃, ☃);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRenderer.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
      this.fontRenderer.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
      this.drawTexturedModalRect(☃, ☃x + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
   }
}
