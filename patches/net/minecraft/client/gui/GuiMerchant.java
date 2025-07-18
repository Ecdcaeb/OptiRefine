package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiMerchant extends GuiContainer {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
   private final IMerchant merchant;
   private GuiMerchant.MerchantButton nextButton;
   private GuiMerchant.MerchantButton previousButton;
   private int selectedMerchantRecipe;
   private final ITextComponent chatComponent;

   public GuiMerchant(InventoryPlayer var1, IMerchant var2, World var3) {
      super(new ContainerMerchant(☃, ☃, ☃));
      this.merchant = ☃;
      this.chatComponent = ☃.getDisplayName();
   }

   @Override
   public void initGui() {
      super.initGui();
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.nextButton = this.addButton(new GuiMerchant.MerchantButton(1, ☃ + 120 + 27, ☃x + 24 - 1, true));
      this.previousButton = this.addButton(new GuiMerchant.MerchantButton(2, ☃ + 36 - 19, ☃x + 24 - 1, false));
      this.nextButton.enabled = false;
      this.previousButton.enabled = false;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      String ☃ = this.chatComponent.getUnformattedText();
      this.fontRenderer.drawString(☃, this.xSize / 2 - this.fontRenderer.getStringWidth(☃) / 2, 6, 4210752);
      this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      MerchantRecipeList ☃ = this.merchant.getRecipes(this.mc.player);
      if (☃ != null) {
         this.nextButton.enabled = this.selectedMerchantRecipe < ☃.size() - 1;
         this.previousButton.enabled = this.selectedMerchantRecipe > 0;
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      boolean ☃ = false;
      if (☃ == this.nextButton) {
         this.selectedMerchantRecipe++;
         MerchantRecipeList ☃x = this.merchant.getRecipes(this.mc.player);
         if (☃x != null && this.selectedMerchantRecipe >= ☃x.size()) {
            this.selectedMerchantRecipe = ☃x.size() - 1;
         }

         ☃ = true;
      } else if (☃ == this.previousButton) {
         this.selectedMerchantRecipe--;
         if (this.selectedMerchantRecipe < 0) {
            this.selectedMerchantRecipe = 0;
         }

         ☃ = true;
      }

      if (☃) {
         ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
         PacketBuffer ☃x = new PacketBuffer(Unpooled.buffer());
         ☃x.writeInt(this.selectedMerchantRecipe);
         this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|TrSel", ☃x));
      }
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
      MerchantRecipeList ☃xx = this.merchant.getRecipes(this.mc.player);
      if (☃xx != null && !☃xx.isEmpty()) {
         int ☃xxx = this.selectedMerchantRecipe;
         if (☃xxx < 0 || ☃xxx >= ☃xx.size()) {
            return;
         }

         MerchantRecipe ☃xxxx = ☃xx.get(☃xxx);
         if (☃xxxx.isRecipeDisabled()) {
            this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
            this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      super.drawScreen(☃, ☃, ☃);
      MerchantRecipeList ☃ = this.merchant.getRecipes(this.mc.player);
      if (☃ != null && !☃.isEmpty()) {
         int ☃x = (this.width - this.xSize) / 2;
         int ☃xx = (this.height - this.ySize) / 2;
         int ☃xxx = this.selectedMerchantRecipe;
         MerchantRecipe ☃xxxx = ☃.get(☃xxx);
         ItemStack ☃xxxxx = ☃xxxx.getItemToBuy();
         ItemStack ☃xxxxxx = ☃xxxx.getSecondItemToBuy();
         ItemStack ☃xxxxxxx = ☃xxxx.getItemToSell();
         GlStateManager.pushMatrix();
         RenderHelper.enableGUIStandardItemLighting();
         GlStateManager.disableLighting();
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableColorMaterial();
         GlStateManager.enableLighting();
         this.itemRender.zLevel = 100.0F;
         this.itemRender.renderItemAndEffectIntoGUI(☃xxxxx, ☃x + 36, ☃xx + 24);
         this.itemRender.renderItemOverlays(this.fontRenderer, ☃xxxxx, ☃x + 36, ☃xx + 24);
         if (!☃xxxxxx.isEmpty()) {
            this.itemRender.renderItemAndEffectIntoGUI(☃xxxxxx, ☃x + 62, ☃xx + 24);
            this.itemRender.renderItemOverlays(this.fontRenderer, ☃xxxxxx, ☃x + 62, ☃xx + 24);
         }

         this.itemRender.renderItemAndEffectIntoGUI(☃xxxxxxx, ☃x + 120, ☃xx + 24);
         this.itemRender.renderItemOverlays(this.fontRenderer, ☃xxxxxxx, ☃x + 120, ☃xx + 24);
         this.itemRender.zLevel = 0.0F;
         GlStateManager.disableLighting();
         if (this.isPointInRegion(36, 24, 16, 16, ☃, ☃) && !☃xxxxx.isEmpty()) {
            this.renderToolTip(☃xxxxx, ☃, ☃);
         } else if (!☃xxxxxx.isEmpty() && this.isPointInRegion(62, 24, 16, 16, ☃, ☃) && !☃xxxxxx.isEmpty()) {
            this.renderToolTip(☃xxxxxx, ☃, ☃);
         } else if (!☃xxxxxxx.isEmpty() && this.isPointInRegion(120, 24, 16, 16, ☃, ☃) && !☃xxxxxxx.isEmpty()) {
            this.renderToolTip(☃xxxxxxx, ☃, ☃);
         } else if (☃xxxx.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, ☃, ☃) || this.isPointInRegion(83, 51, 28, 21, ☃, ☃))) {
            this.drawHoveringText(I18n.format("merchant.deprecated"), ☃, ☃);
         }

         GlStateManager.popMatrix();
         GlStateManager.enableLighting();
         GlStateManager.enableDepth();
         RenderHelper.enableStandardItemLighting();
      }

      this.renderHoveredToolTip(☃, ☃);
   }

   public IMerchant getMerchant() {
      return this.merchant;
   }

   static class MerchantButton extends GuiButton {
      private final boolean forward;

      public MerchantButton(int var1, int var2, int var3, boolean var4) {
         super(☃, ☃, ☃, 12, 19, "");
         this.forward = ☃;
      }

      @Override
      public void drawButton(Minecraft var1, int var2, int var3, float var4) {
         if (this.visible) {
            ☃.getTextureManager().bindTexture(GuiMerchant.MERCHANT_GUI_TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean ☃ = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
            int ☃x = 0;
            int ☃xx = 176;
            if (!this.enabled) {
               ☃xx += this.width * 2;
            } else if (☃) {
               ☃xx += this.width;
            }

            if (!this.forward) {
               ☃x += this.height;
            }

            this.drawTexturedModalRect(this.x, this.y, ☃xx, ☃x, this.width, this.height);
         }
      }
   }
}
