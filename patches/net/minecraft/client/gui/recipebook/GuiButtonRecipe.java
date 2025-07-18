package net.minecraft.client.gui.recipebook;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiButtonRecipe extends GuiButton {
   private static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
   private RecipeBook book;
   private RecipeList list;
   private float time;
   private float animationTime;
   private int currentIndex;

   public GuiButtonRecipe() {
      super(0, 0, 0, 25, 25, "");
   }

   public void init(RecipeList var1, RecipeBookPage var2, RecipeBook var3) {
      this.list = ☃;
      this.book = ☃;
      List<IRecipe> ☃ = ☃.getRecipes(☃.isFilteringCraftable());

      for (IRecipe ☃x : ☃) {
         if (☃.isNew(☃x)) {
            ☃.recipesShown(☃);
            this.animationTime = 15.0F;
            break;
         }
      }
   }

   public RecipeList getList() {
      return this.list;
   }

   public void setPosition(int var1, int var2) {
      this.x = ☃;
      this.y = ☃;
   }

   @Override
   public void drawButton(Minecraft var1, int var2, int var3, float var4) {
      if (this.visible) {
         if (!GuiScreen.isCtrlKeyDown()) {
            this.time += ☃;
         }

         this.hovered = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
         RenderHelper.enableGUIStandardItemLighting();
         ☃.getTextureManager().bindTexture(RECIPE_BOOK);
         GlStateManager.disableLighting();
         int ☃ = 29;
         if (!this.list.containsCraftableRecipes()) {
            ☃ += 25;
         }

         int ☃x = 206;
         if (this.list.getRecipes(this.book.isFilteringCraftable()).size() > 1) {
            ☃x += 25;
         }

         boolean ☃xx = this.animationTime > 0.0F;
         if (☃xx) {
            float ☃xxx = 1.0F + 0.1F * (float)Math.sin(this.animationTime / 15.0F * (float) Math.PI);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(this.x + 8), (float)(this.y + 12), 0.0F);
            GlStateManager.scale(☃xxx, ☃xxx, 1.0F);
            GlStateManager.translate((float)(-(this.x + 8)), (float)(-(this.y + 12)), 0.0F);
            this.animationTime -= ☃;
         }

         this.drawTexturedModalRect(this.x, this.y, ☃, ☃x, this.width, this.height);
         List<IRecipe> ☃xxx = this.getOrderedRecipes();
         this.currentIndex = MathHelper.floor(this.time / 30.0F) % ☃xxx.size();
         ItemStack ☃xxxx = ☃xxx.get(this.currentIndex).getRecipeOutput();
         int ☃xxxxx = 4;
         if (this.list.hasSingleResultItem() && this.getOrderedRecipes().size() > 1) {
            ☃.getRenderItem().renderItemAndEffectIntoGUI(☃xxxx, this.x + ☃xxxxx + 1, this.y + ☃xxxxx + 1);
            ☃xxxxx--;
         }

         ☃.getRenderItem().renderItemAndEffectIntoGUI(☃xxxx, this.x + ☃xxxxx, this.y + ☃xxxxx);
         if (☃xx) {
            GlStateManager.popMatrix();
         }

         GlStateManager.enableLighting();
         RenderHelper.disableStandardItemLighting();
      }
   }

   private List<IRecipe> getOrderedRecipes() {
      List<IRecipe> ☃ = this.list.getDisplayRecipes(true);
      if (!this.book.isFilteringCraftable()) {
         ☃.addAll(this.list.getDisplayRecipes(false));
      }

      return ☃;
   }

   public boolean isOnlyOption() {
      return this.getOrderedRecipes().size() == 1;
   }

   public IRecipe getRecipe() {
      List<IRecipe> ☃ = this.getOrderedRecipes();
      return ☃.get(this.currentIndex);
   }

   public List<String> getToolTipText(GuiScreen var1) {
      ItemStack ☃ = this.getOrderedRecipes().get(this.currentIndex).getRecipeOutput();
      List<String> ☃x = ☃.getItemToolTip(☃);
      if (this.list.getRecipes(this.book.isFilteringCraftable()).size() > 1) {
         ☃x.add(I18n.format("gui.recipebook.moreRecipes"));
      }

      return ☃x;
   }

   @Override
   public int getButtonWidth() {
      return 25;
   }
}
