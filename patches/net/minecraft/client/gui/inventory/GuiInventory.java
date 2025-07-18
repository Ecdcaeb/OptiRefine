package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;

public class GuiInventory extends InventoryEffectRenderer implements IRecipeShownListener {
   private float oldMouseX;
   private float oldMouseY;
   private GuiButtonImage recipeButton;
   private final GuiRecipeBook recipeBookGui = new GuiRecipeBook();
   private boolean widthTooNarrow;
   private boolean buttonClicked;

   public GuiInventory(EntityPlayer var1) {
      super(☃.inventoryContainer);
      this.allowUserInput = true;
   }

   @Override
   public void updateScreen() {
      if (this.mc.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.player));
      }

      this.recipeBookGui.tick();
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      if (this.mc.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.player));
      } else {
         super.initGui();
      }

      this.widthTooNarrow = this.width < 379;
      this.recipeBookGui.func_194303_a(this.width, this.height, this.mc, this.widthTooNarrow, ((ContainerPlayer)this.inventorySlots).craftMatrix);
      this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
      this.recipeButton = new GuiButtonImage(10, this.guiLeft + 104, this.height / 2 - 22, 20, 18, 178, 0, 19, INVENTORY_BACKGROUND);
      this.buttonList.add(this.recipeButton);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRenderer.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.hasActivePotionEffects = !this.recipeBookGui.isVisible();
      if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
         this.drawGuiContainerBackgroundLayer(☃, ☃, ☃);
         this.recipeBookGui.render(☃, ☃, ☃);
      } else {
         this.recipeBookGui.render(☃, ☃, ☃);
         super.drawScreen(☃, ☃, ☃);
         this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, false, ☃);
      }

      this.renderHoveredToolTip(☃, ☃);
      this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, ☃, ☃);
      this.oldMouseX = ☃;
      this.oldMouseY = ☃;
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
      int ☃ = this.guiLeft;
      int ☃x = this.guiTop;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
      drawEntityOnScreen(☃ + 51, ☃x + 75, 30, ☃ + 51 - this.oldMouseX, ☃x + 75 - 50 - this.oldMouseY, this.mc.player);
   }

   public static void drawEntityOnScreen(int var0, int var1, int var2, float var3, float var4, EntityLivingBase var5) {
      GlStateManager.enableColorMaterial();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)☃, (float)☃, 50.0F);
      GlStateManager.scale((float)(-☃), (float)☃, (float)☃);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      float ☃ = ☃.renderYawOffset;
      float ☃x = ☃.rotationYaw;
      float ☃xx = ☃.rotationPitch;
      float ☃xxx = ☃.prevRotationYawHead;
      float ☃xxxx = ☃.rotationYawHead;
      GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-((float)Math.atan(☃ / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
      ☃.renderYawOffset = (float)Math.atan(☃ / 40.0F) * 20.0F;
      ☃.rotationYaw = (float)Math.atan(☃ / 40.0F) * 40.0F;
      ☃.rotationPitch = -((float)Math.atan(☃ / 40.0F)) * 20.0F;
      ☃.rotationYawHead = ☃.rotationYaw;
      ☃.prevRotationYawHead = ☃.rotationYaw;
      GlStateManager.translate(0.0F, 0.0F, 0.0F);
      RenderManager ☃xxxxx = Minecraft.getMinecraft().getRenderManager();
      ☃xxxxx.setPlayerViewY(180.0F);
      ☃xxxxx.setRenderShadow(false);
      ☃xxxxx.renderEntity(☃, 0.0, 0.0, 0.0, 0.0F, 1.0F, false);
      ☃xxxxx.setRenderShadow(true);
      ☃.renderYawOffset = ☃;
      ☃.rotationYaw = ☃x;
      ☃.rotationPitch = ☃xx;
      ☃.prevRotationYawHead = ☃xxx;
      ☃.rotationYawHead = ☃xxxx;
      GlStateManager.popMatrix();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
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
   protected void mouseReleased(int var1, int var2, int var3) {
      if (this.buttonClicked) {
         this.buttonClicked = false;
      } else {
         super.mouseReleased(☃, ☃, ☃);
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
         this.recipeBookGui.initVisuals(this.widthTooNarrow, ((ContainerPlayer)this.inventorySlots).craftMatrix);
         this.recipeBookGui.toggleVisibility();
         this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
         this.recipeButton.setPosition(this.guiLeft + 104, this.height / 2 - 22);
         this.buttonClicked = true;
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
