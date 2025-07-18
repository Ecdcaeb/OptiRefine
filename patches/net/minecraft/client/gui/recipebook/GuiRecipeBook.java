package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class GuiRecipeBook extends Gui implements IRecipeUpdateListener {
   protected static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
   private int xOffset;
   private int width;
   private int height;
   private final GhostRecipe ghostRecipe = new GhostRecipe();
   private final List<GuiButtonRecipeTab> recipeTabs = Lists.newArrayList(
      new GuiButtonRecipeTab[]{
         new GuiButtonRecipeTab(0, CreativeTabs.SEARCH),
         new GuiButtonRecipeTab(0, CreativeTabs.TOOLS),
         new GuiButtonRecipeTab(0, CreativeTabs.BUILDING_BLOCKS),
         new GuiButtonRecipeTab(0, CreativeTabs.MISC),
         new GuiButtonRecipeTab(0, CreativeTabs.REDSTONE)
      }
   );
   private GuiButtonRecipeTab currentTab;
   private GuiButtonToggle toggleRecipesBtn;
   private InventoryCrafting craftingSlots;
   private Minecraft mc;
   private GuiTextField searchBar;
   private String lastSearch = "";
   private RecipeBook recipeBook;
   private final RecipeBookPage recipeBookPage = new RecipeBookPage();
   private RecipeItemHelper stackedContents = new RecipeItemHelper();
   private int timesInventoryChanged;

   public void func_194303_a(int var1, int var2, Minecraft var3, boolean var4, InventoryCrafting var5) {
      this.mc = ☃;
      this.width = ☃;
      this.height = ☃;
      this.craftingSlots = ☃;
      this.recipeBook = ☃.player.getRecipeBook();
      this.timesInventoryChanged = ☃.player.inventory.getTimesChanged();
      this.currentTab = this.recipeTabs.get(0);
      this.currentTab.setStateTriggered(true);
      if (this.isVisible()) {
         this.initVisuals(☃, ☃);
      }

      Keyboard.enableRepeatEvents(true);
   }

   public void initVisuals(boolean var1, InventoryCrafting var2) {
      this.xOffset = ☃ ? 0 : 86;
      int ☃ = (this.width - 147) / 2 - this.xOffset;
      int ☃x = (this.height - 166) / 2;
      this.stackedContents.clear();
      this.mc.player.inventory.fillStackedContents(this.stackedContents, false);
      ☃.fillStackedContents(this.stackedContents);
      this.searchBar = new GuiTextField(0, this.mc.fontRenderer, ☃ + 25, ☃x + 14, 80, this.mc.fontRenderer.FONT_HEIGHT + 5);
      this.searchBar.setMaxStringLength(50);
      this.searchBar.setEnableBackgroundDrawing(false);
      this.searchBar.setVisible(true);
      this.searchBar.setTextColor(16777215);
      this.recipeBookPage.init(this.mc, ☃, ☃x);
      this.recipeBookPage.addListener(this);
      this.toggleRecipesBtn = new GuiButtonToggle(0, ☃ + 110, ☃x + 12, 26, 16, this.recipeBook.isFilteringCraftable());
      this.toggleRecipesBtn.initTextureValues(152, 41, 28, 18, RECIPE_BOOK);
      this.updateCollections(false);
      this.updateTabs();
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public int updateScreenPosition(boolean var1, int var2, int var3) {
      int ☃;
      if (this.isVisible() && !☃) {
         ☃ = 177 + (☃ - ☃ - 200) / 2;
      } else {
         ☃ = (☃ - ☃) / 2;
      }

      return ☃;
   }

   public void toggleVisibility() {
      this.setVisible(!this.isVisible());
   }

   public boolean isVisible() {
      return this.recipeBook.isGuiOpen();
   }

   private void setVisible(boolean var1) {
      this.recipeBook.setGuiOpen(☃);
      if (!☃) {
         this.recipeBookPage.setInvisible();
      }

      this.sendUpdateSettings();
   }

   public void slotClicked(@Nullable Slot var1) {
      if (☃ != null && ☃.slotNumber <= 9) {
         this.ghostRecipe.clear();
         if (this.isVisible()) {
            this.updateStackedContents();
         }
      }
   }

   private void updateCollections(boolean var1) {
      List<RecipeList> ☃ = RecipeBookClient.RECIPES_BY_TAB.get(this.currentTab.getCategory());
      ☃.forEach(var1x -> var1x.canCraft(this.stackedContents, this.craftingSlots.getWidth(), this.craftingSlots.getHeight(), this.recipeBook));
      List<RecipeList> ☃x = Lists.newArrayList(☃);
      ☃x.removeIf(var0 -> !var0.isNotEmpty());
      ☃x.removeIf(var0 -> !var0.containsValidRecipes());
      String ☃xx = this.searchBar.getText();
      if (!☃xx.isEmpty()) {
         ObjectSet<RecipeList> ☃xxx = new ObjectLinkedOpenHashSet(this.mc.getSearchTree(SearchTreeManager.RECIPES).search(☃xx.toLowerCase(Locale.ROOT)));
         ☃x.removeIf(var1x -> !☃.contains(var1x));
      }

      if (this.recipeBook.isFilteringCraftable()) {
         ☃x.removeIf(var0 -> !var0.containsCraftableRecipes());
      }

      this.recipeBookPage.updateLists(☃x, ☃);
   }

   private void updateTabs() {
      int ☃ = (this.width - 147) / 2 - this.xOffset - 30;
      int ☃x = (this.height - 166) / 2 + 3;
      int ☃xx = 27;
      int ☃xxx = 0;

      for (GuiButtonRecipeTab ☃xxxx : this.recipeTabs) {
         CreativeTabs ☃xxxxx = ☃xxxx.getCategory();
         if (☃xxxxx == CreativeTabs.SEARCH) {
            ☃xxxx.visible = true;
            ☃xxxx.setPosition(☃, ☃x + 27 * ☃xxx++);
         } else if (☃xxxx.updateVisibility()) {
            ☃xxxx.setPosition(☃, ☃x + 27 * ☃xxx++);
            ☃xxxx.startAnimation(this.mc);
         }
      }
   }

   public void tick() {
      if (this.isVisible()) {
         if (this.timesInventoryChanged != this.mc.player.inventory.getTimesChanged()) {
            this.updateStackedContents();
            this.timesInventoryChanged = this.mc.player.inventory.getTimesChanged();
         }
      }
   }

   private void updateStackedContents() {
      this.stackedContents.clear();
      this.mc.player.inventory.fillStackedContents(this.stackedContents, false);
      this.craftingSlots.fillStackedContents(this.stackedContents);
      this.updateCollections(false);
   }

   public void render(int var1, int var2, float var3) {
      if (this.isVisible()) {
         RenderHelper.enableGUIStandardItemLighting();
         GlStateManager.disableLighting();
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, 0.0F, 100.0F);
         this.mc.getTextureManager().bindTexture(RECIPE_BOOK);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         int ☃ = (this.width - 147) / 2 - this.xOffset;
         int ☃x = (this.height - 166) / 2;
         this.drawTexturedModalRect(☃, ☃x, 1, 1, 147, 166);
         this.searchBar.drawTextBox();
         RenderHelper.disableStandardItemLighting();

         for (GuiButtonRecipeTab ☃xx : this.recipeTabs) {
            ☃xx.drawButton(this.mc, ☃, ☃, ☃);
         }

         this.toggleRecipesBtn.drawButton(this.mc, ☃, ☃, ☃);
         this.recipeBookPage.render(☃, ☃x, ☃, ☃, ☃);
         GlStateManager.popMatrix();
      }
   }

   public void renderTooltip(int var1, int var2, int var3, int var4) {
      if (this.isVisible()) {
         this.recipeBookPage.renderTooltip(☃, ☃);
         if (this.toggleRecipesBtn.isMouseOver()) {
            String ☃ = I18n.format(this.toggleRecipesBtn.isStateTriggered() ? "gui.recipebook.toggleRecipes.craftable" : "gui.recipebook.toggleRecipes.all");
            if (this.mc.currentScreen != null) {
               this.mc.currentScreen.drawHoveringText(☃, ☃, ☃);
            }
         }

         this.renderGhostRecipeTooltip(☃, ☃, ☃, ☃);
      }
   }

   private void renderGhostRecipeTooltip(int var1, int var2, int var3, int var4) {
      ItemStack ☃ = null;

      for (int ☃x = 0; ☃x < this.ghostRecipe.size(); ☃x++) {
         GhostRecipe.GhostIngredient ☃xx = this.ghostRecipe.get(☃x);
         int ☃xxx = ☃xx.getX() + ☃;
         int ☃xxxx = ☃xx.getY() + ☃;
         if (☃ >= ☃xxx && ☃ >= ☃xxxx && ☃ < ☃xxx + 16 && ☃ < ☃xxxx + 16) {
            ☃ = ☃xx.getItem();
         }
      }

      if (☃ != null && this.mc.currentScreen != null) {
         this.mc.currentScreen.drawHoveringText(this.mc.currentScreen.getItemToolTip(☃), ☃, ☃);
      }
   }

   public void renderGhostRecipe(int var1, int var2, boolean var3, float var4) {
      this.ghostRecipe.render(this.mc, ☃, ☃, ☃, ☃);
   }

   public boolean mouseClicked(int var1, int var2, int var3) {
      if (this.isVisible() && !this.mc.player.isSpectator()) {
         if (this.recipeBookPage.mouseClicked(☃, ☃, ☃, (this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166)) {
            IRecipe ☃ = this.recipeBookPage.getLastClickedRecipe();
            RecipeList ☃x = this.recipeBookPage.getLastClickedRecipeList();
            if (☃ != null && ☃x != null) {
               if (!☃x.isCraftable(☃) && this.ghostRecipe.getRecipe() == ☃) {
                  return false;
               }

               this.ghostRecipe.clear();
               this.mc.playerController.func_194338_a(this.mc.player.openContainer.windowId, ☃, GuiScreen.isShiftKeyDown(), this.mc.player);
               if (!this.isOffsetNextToMainGUI() && ☃ == 0) {
                  this.setVisible(false);
               }
            }

            return true;
         } else if (☃ != 0) {
            return false;
         } else if (this.searchBar.mouseClicked(☃, ☃, ☃)) {
            return true;
         } else if (this.toggleRecipesBtn.mousePressed(this.mc, ☃, ☃)) {
            boolean ☃ = !this.recipeBook.isFilteringCraftable();
            this.recipeBook.setFilteringCraftable(☃);
            this.toggleRecipesBtn.setStateTriggered(☃);
            this.toggleRecipesBtn.playPressSound(this.mc.getSoundHandler());
            this.sendUpdateSettings();
            this.updateCollections(false);
            return true;
         } else {
            for (GuiButtonRecipeTab ☃ : this.recipeTabs) {
               if (☃.mousePressed(this.mc, ☃, ☃)) {
                  if (this.currentTab != ☃) {
                     ☃.playPressSound(this.mc.getSoundHandler());
                     this.currentTab.setStateTriggered(false);
                     this.currentTab = ☃;
                     this.currentTab.setStateTriggered(true);
                     this.updateCollections(true);
                  }

                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean hasClickedOutside(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (!this.isVisible()) {
         return true;
      } else {
         boolean ☃ = ☃ < ☃ || ☃ < ☃ || ☃ >= ☃ + ☃ || ☃ >= ☃ + ☃;
         boolean ☃x = ☃ - 147 < ☃ && ☃ < ☃ && ☃ < ☃ && ☃ < ☃ + ☃;
         return ☃ && !☃x && !this.currentTab.mousePressed(this.mc, ☃, ☃);
      }
   }

   public boolean keyPressed(char var1, int var2) {
      if (!this.isVisible() || this.mc.player.isSpectator()) {
         return false;
      } else if (☃ == 1 && !this.isOffsetNextToMainGUI()) {
         this.setVisible(false);
         return true;
      } else {
         if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat) && !this.searchBar.isFocused()) {
            this.searchBar.setFocused(true);
         } else if (this.searchBar.textboxKeyTyped(☃, ☃)) {
            String ☃ = this.searchBar.getText().toLowerCase(Locale.ROOT);
            this.pirateRecipe(☃);
            if (!☃.equals(this.lastSearch)) {
               this.updateCollections(false);
               this.lastSearch = ☃;
            }

            return true;
         }

         return false;
      }
   }

   private void pirateRecipe(String var1) {
      if ("excitedze".equals(☃)) {
         LanguageManager ☃ = this.mc.getLanguageManager();
         Language ☃x = ☃.getLanguage("en_pt");
         if (☃.getCurrentLanguage().compareTo(☃x) == 0) {
            return;
         }

         ☃.setCurrentLanguage(☃x);
         this.mc.gameSettings.language = ☃x.getLanguageCode();
         this.mc.refreshResources();
         this.mc.fontRenderer.setUnicodeFlag(this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.mc.gameSettings.forceUnicodeFont);
         this.mc.fontRenderer.setBidiFlag(☃.isCurrentLanguageBidirectional());
         this.mc.gameSettings.saveOptions();
      }
   }

   private boolean isOffsetNextToMainGUI() {
      return this.xOffset == 86;
   }

   public void recipesUpdated() {
      this.updateTabs();
      if (this.isVisible()) {
         this.updateCollections(false);
      }
   }

   @Override
   public void recipesShown(List<IRecipe> var1) {
      for (IRecipe ☃ : ☃) {
         this.mc.player.removeRecipeHighlight(☃);
      }
   }

   public void setupGhostRecipe(IRecipe var1, List<Slot> var2) {
      ItemStack ☃ = ☃.getRecipeOutput();
      this.ghostRecipe.setRecipe(☃);
      this.ghostRecipe.addIngredient(Ingredient.fromStacks(☃), ☃.get(0).xPos, ☃.get(0).yPos);
      int ☃x = this.craftingSlots.getWidth();
      int ☃xx = this.craftingSlots.getHeight();
      int ☃xxx = ☃ instanceof ShapedRecipes ? ((ShapedRecipes)☃).getWidth() : ☃x;
      int ☃xxxx = 1;
      Iterator<Ingredient> ☃xxxxx = ☃.getIngredients().iterator();

      for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xx; ☃xxxxxx++) {
         for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxx; ☃xxxxxxx++) {
            if (!☃xxxxx.hasNext()) {
               return;
            }

            Ingredient ☃xxxxxxxx = ☃xxxxx.next();
            if (☃xxxxxxxx != Ingredient.EMPTY) {
               Slot ☃xxxxxxxxx = ☃.get(☃xxxx);
               this.ghostRecipe.addIngredient(☃xxxxxxxx, ☃xxxxxxxxx.xPos, ☃xxxxxxxxx.yPos);
            }

            ☃xxxx++;
         }

         if (☃xxx < ☃x) {
            ☃xxxx += ☃x - ☃xxx;
         }
      }
   }

   private void sendUpdateSettings() {
      if (this.mc.getConnection() != null) {
         this.mc.getConnection().sendPacket(new CPacketRecipeInfo(this.isVisible(), this.recipeBook.isFilteringCraftable()));
      }
   }
}
