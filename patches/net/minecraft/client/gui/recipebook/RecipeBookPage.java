package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class RecipeBookPage {
   private List<GuiButtonRecipe> buttons = Lists.newArrayListWithCapacity(20);
   private GuiButtonRecipe hoveredButton;
   private GuiRecipeOverlay overlay = new GuiRecipeOverlay();
   private Minecraft minecraft;
   private List<IRecipeUpdateListener> listeners = Lists.newArrayList();
   private List<RecipeList> recipeLists;
   private GuiButtonToggle forwardButton;
   private GuiButtonToggle backButton;
   private int totalPages;
   private int currentPage;
   private RecipeBook recipeBook;
   private IRecipe lastClickedRecipe;
   private RecipeList lastClickedRecipeList;

   public RecipeBookPage() {
      for (int ☃ = 0; ☃ < 20; ☃++) {
         this.buttons.add(new GuiButtonRecipe());
      }
   }

   public void init(Minecraft var1, int var2, int var3) {
      this.minecraft = ☃;
      this.recipeBook = ☃.player.getRecipeBook();

      for (int ☃ = 0; ☃ < this.buttons.size(); ☃++) {
         this.buttons.get(☃).setPosition(☃ + 11 + 25 * (☃ % 5), ☃ + 31 + 25 * (☃ / 5));
      }

      this.forwardButton = new GuiButtonToggle(0, ☃ + 93, ☃ + 137, 12, 17, false);
      this.forwardButton.initTextureValues(1, 208, 13, 18, GuiRecipeBook.RECIPE_BOOK);
      this.backButton = new GuiButtonToggle(0, ☃ + 38, ☃ + 137, 12, 17, true);
      this.backButton.initTextureValues(1, 208, 13, 18, GuiRecipeBook.RECIPE_BOOK);
   }

   public void addListener(GuiRecipeBook var1) {
      this.listeners.remove(☃);
      this.listeners.add(☃);
   }

   public void updateLists(List<RecipeList> var1, boolean var2) {
      this.recipeLists = ☃;
      this.totalPages = (int)Math.ceil(☃.size() / 20.0);
      if (this.totalPages <= this.currentPage || ☃) {
         this.currentPage = 0;
      }

      this.updateButtonsForPage();
   }

   private void updateButtonsForPage() {
      int ☃ = 20 * this.currentPage;

      for (int ☃x = 0; ☃x < this.buttons.size(); ☃x++) {
         GuiButtonRecipe ☃xx = this.buttons.get(☃x);
         if (☃ + ☃x < this.recipeLists.size()) {
            RecipeList ☃xxx = this.recipeLists.get(☃ + ☃x);
            ☃xx.init(☃xxx, this, this.recipeBook);
            ☃xx.visible = true;
         } else {
            ☃xx.visible = false;
         }
      }

      this.updateArrowButtons();
   }

   private void updateArrowButtons() {
      this.forwardButton.visible = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
      this.backButton.visible = this.totalPages > 1 && this.currentPage > 0;
   }

   public void render(int var1, int var2, int var3, int var4, float var5) {
      if (this.totalPages > 1) {
         String ☃ = this.currentPage + 1 + "/" + this.totalPages;
         int ☃x = this.minecraft.fontRenderer.getStringWidth(☃);
         this.minecraft.fontRenderer.drawString(☃, ☃ - ☃x / 2 + 73, ☃ + 141, -1);
      }

      RenderHelper.disableStandardItemLighting();
      this.hoveredButton = null;

      for (GuiButtonRecipe ☃ : this.buttons) {
         ☃.drawButton(this.minecraft, ☃, ☃, ☃);
         if (☃.visible && ☃.isMouseOver()) {
            this.hoveredButton = ☃;
         }
      }

      this.backButton.drawButton(this.minecraft, ☃, ☃, ☃);
      this.forwardButton.drawButton(this.minecraft, ☃, ☃, ☃);
      this.overlay.render(☃, ☃, ☃);
   }

   public void renderTooltip(int var1, int var2) {
      if (this.minecraft.currentScreen != null && this.hoveredButton != null && !this.overlay.isVisible()) {
         this.minecraft.currentScreen.drawHoveringText(this.hoveredButton.getToolTipText(this.minecraft.currentScreen), ☃, ☃);
      }
   }

   @Nullable
   public IRecipe getLastClickedRecipe() {
      return this.lastClickedRecipe;
   }

   @Nullable
   public RecipeList getLastClickedRecipeList() {
      return this.lastClickedRecipeList;
   }

   public void setInvisible() {
      this.overlay.setVisible(false);
   }

   public boolean mouseClicked(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.lastClickedRecipe = null;
      this.lastClickedRecipeList = null;
      if (this.overlay.isVisible()) {
         if (this.overlay.buttonClicked(☃, ☃, ☃)) {
            this.lastClickedRecipe = this.overlay.getLastRecipeClicked();
            this.lastClickedRecipeList = this.overlay.getRecipeList();
         } else {
            this.overlay.setVisible(false);
         }

         return true;
      } else if (this.forwardButton.mousePressed(this.minecraft, ☃, ☃) && ☃ == 0) {
         this.forwardButton.playPressSound(this.minecraft.getSoundHandler());
         this.currentPage++;
         this.updateButtonsForPage();
         return true;
      } else if (this.backButton.mousePressed(this.minecraft, ☃, ☃) && ☃ == 0) {
         this.backButton.playPressSound(this.minecraft.getSoundHandler());
         this.currentPage--;
         this.updateButtonsForPage();
         return true;
      } else {
         for (GuiButtonRecipe ☃ : this.buttons) {
            if (☃.mousePressed(this.minecraft, ☃, ☃)) {
               ☃.playPressSound(this.minecraft.getSoundHandler());
               if (☃ == 0) {
                  this.lastClickedRecipe = ☃.getRecipe();
                  this.lastClickedRecipeList = ☃.getList();
               } else if (!this.overlay.isVisible() && !☃.isOnlyOption()) {
                  this.overlay.init(this.minecraft, ☃.getList(), ☃.x, ☃.y, ☃ + ☃ / 2, ☃ + 13 + ☃ / 2, ☃.getButtonWidth(), this.recipeBook);
               }

               return true;
            }
         }

         return false;
      }
   }

   public void recipesShown(List<IRecipe> var1) {
      for (IRecipeUpdateListener ☃ : this.listeners) {
         ☃.recipesShown(☃);
      }
   }
}
