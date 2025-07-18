package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiHopper extends GuiContainer {
   private static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
   private final IInventory playerInventory;
   private final IInventory hopperInventory;

   public GuiHopper(InventoryPlayer var1, IInventory var2) {
      super(new ContainerHopper(☃, ☃, Minecraft.getMinecraft().player));
      this.playerInventory = ☃;
      this.hopperInventory = ☃;
      this.allowUserInput = false;
      this.ySize = 133;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      super.drawScreen(☃, ☃, ☃);
      this.renderHoveredToolTip(☃, ☃);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRenderer.drawString(this.hopperInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
      this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(HOPPER_GUI_TEXTURE);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
   }
}
