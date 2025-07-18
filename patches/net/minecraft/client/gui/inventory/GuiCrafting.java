package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCrafting extends GuiContainer implements IRecipeShownListener {
   private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");
   private GuiButtonImage recipeButton;
   private final GuiRecipeBook recipeBookGui = new GuiRecipeBook();
   private boolean widthTooNarrow;

   public GuiCrafting(InventoryPlayer var1, World var2) {
      this(☃, ☃, BlockPos.ORIGIN);
   }

   public GuiCrafting(InventoryPlayer var1, World var2, BlockPos var3) {
      super(new ContainerWorkbench(☃, ☃, ☃));
   }

   @Override
   public void initGui() {
      super.initGui();
      this.widthTooNarrow = this.width < 379;
      this.recipeBookGui.func_194303_a(this.width, this.height, this.mc, this.widthTooNarrow, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
      this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
      this.recipeButton = new GuiButtonImage(10, this.guiLeft + 5, this.height / 2 - 49, 20, 18, 0, 168, 19, CRAFTING_TABLE_GUI_TEXTURES);
      this.buttonList.add(this.recipeButton);
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.recipeBookGui.tick();
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
         this.drawGuiContainerBackgroundLayer(☃, ☃, ☃);
         this.recipeBookGui.render(☃, ☃, ☃);
      } else {
         this.recipeBookGui.render(☃, ☃, ☃);
         super.drawScreen(☃, ☃, ☃);
         this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, true, ☃);
      }

      this.renderHoveredToolTip(☃, ☃);
      this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, ☃, ☃);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRenderer.drawString(I18n.format("container.crafting"), 28, 6, 4210752);
      this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
      int ☃ = this.guiLeft;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
   }

   @Override
   protected boolean isPointInRegion(int var1, int var2, int var3, int var4, int var5, int var6) {
      return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isPointInRegion(☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      if (!this.recipeBookGui.mouseClicked(☃, ☃, ☃)) {
         if (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) {
            super.mouseClicked(☃, ☃, ☃);
         }
      }
   }

   @Override
   protected boolean hasClickedOutside(int var1, int var2, int var3, int var4) {
      boolean ☃ = ☃ < ☃ || ☃ < ☃ || ☃ >= ☃ + this.xSize || ☃ >= ☃ + this.ySize;
      return this.recipeBookGui.hasClickedOutside(☃, ☃, this.guiLeft, this.guiTop, this.xSize, this.ySize) && ☃;
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == 10) {
         this.recipeBookGui.initVisuals(this.widthTooNarrow, ((ContainerWorkbench)this.inventorySlots).craftMatrix);
         this.recipeBookGui.toggleVisibility();
         this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
         this.recipeButton.setPosition(this.guiLeft + 5, this.height / 2 - 49);
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (!this.recipeBookGui.keyPressed(☃, ☃)) {
         super.keyTyped(☃, ☃);
      }
   }

   @Override
   protected void handleMouseClick(Slot var1, int var2, int var3, ClickType var4) {
      super.handleMouseClick(☃, ☃, ☃, ☃);
      this.recipeBookGui.slotClicked(☃);
   }

   @Override
   public void recipesUpdated() {
      this.recipeBookGui.recipesUpdated();
   }

   @Override
   public void onGuiClosed() {
      this.recipeBookGui.removed();
      super.onGuiClosed();
   }

   @Override
   public GuiRecipeBook func_194310_f() {
      return this.recipeBookGui;
   }
}
