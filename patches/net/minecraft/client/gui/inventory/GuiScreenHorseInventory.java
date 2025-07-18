package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory extends GuiContainer {
   private static final ResourceLocation HORSE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/horse.png");
   private final IInventory playerInventory;
   private final IInventory horseInventory;
   private final AbstractHorse horseEntity;
   private float mousePosx;
   private float mousePosY;

   public GuiScreenHorseInventory(IInventory var1, IInventory var2, AbstractHorse var3) {
      super(new ContainerHorseInventory(☃, ☃, ☃, Minecraft.getMinecraft().player));
      this.playerInventory = ☃;
      this.horseInventory = ☃;
      this.horseEntity = ☃;
      this.allowUserInput = false;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRenderer.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
      this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(HORSE_GUI_TEXTURES);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
      if (this.horseEntity instanceof AbstractChestHorse) {
         AbstractChestHorse ☃xx = (AbstractChestHorse)this.horseEntity;
         if (☃xx.hasChest()) {
            this.drawTexturedModalRect(☃ + 79, ☃x + 17, 0, this.ySize, ☃xx.getInventoryColumns() * 18, 54);
         }
      }

      if (this.horseEntity.canBeSaddled()) {
         this.drawTexturedModalRect(☃ + 7, ☃x + 35 - 18, 18, this.ySize + 54, 18, 18);
      }

      if (this.horseEntity.wearsArmor()) {
         if (this.horseEntity instanceof EntityLlama) {
            this.drawTexturedModalRect(☃ + 7, ☃x + 35, 36, this.ySize + 54, 18, 18);
         } else {
            this.drawTexturedModalRect(☃ + 7, ☃x + 35, 0, this.ySize + 54, 18, 18);
         }
      }

      GuiInventory.drawEntityOnScreen(☃ + 51, ☃x + 60, 17, ☃ + 51 - this.mousePosx, ☃x + 75 - 50 - this.mousePosY, this.horseEntity);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.mousePosx = ☃;
      this.mousePosY = ☃;
      super.drawScreen(☃, ☃, ☃);
      this.renderHoveredToolTip(☃, ☃);
   }
}
