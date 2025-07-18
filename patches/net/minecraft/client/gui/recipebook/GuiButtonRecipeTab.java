package net.minecraft.client.gui.recipebook;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class GuiButtonRecipeTab extends GuiButtonToggle {
   private final CreativeTabs category;
   private float animationTime;

   public GuiButtonRecipeTab(int var1, CreativeTabs var2) {
      super(☃, 0, 0, 35, 27, false);
      this.category = ☃;
      this.initTextureValues(153, 2, 35, 0, GuiRecipeBook.RECIPE_BOOK);
   }

   public void startAnimation(Minecraft var1) {
      RecipeBook ☃ = ☃.player.getRecipeBook();

      for (RecipeList ☃x : RecipeBookClient.RECIPES_BY_TAB.get(this.category)) {
         for (IRecipe ☃xx : ☃x.getRecipes(☃.isFilteringCraftable())) {
            if (☃.isNew(☃xx)) {
               this.animationTime = 15.0F;
               return;
            }
         }
      }
   }

   @Override
   public void drawButton(Minecraft var1, int var2, int var3, float var4) {
      if (this.visible) {
         if (this.animationTime > 0.0F) {
            float ☃ = 1.0F + 0.1F * (float)Math.sin(this.animationTime / 15.0F * (float) Math.PI);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(this.x + 8), (float)(this.y + 12), 0.0F);
            GlStateManager.scale(1.0F, ☃, 1.0F);
            GlStateManager.translate((float)(-(this.x + 8)), (float)(-(this.y + 12)), 0.0F);
         }

         this.hovered = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
         ☃.getTextureManager().bindTexture(this.resourceLocation);
         GlStateManager.disableDepth();
         int ☃ = this.xTexStart;
         int ☃x = this.yTexStart;
         if (this.stateTriggered) {
            ☃ += this.xDiffTex;
         }

         if (this.hovered) {
            ☃x += this.yDiffTex;
         }

         int ☃xx = this.x;
         if (this.stateTriggered) {
            ☃xx -= 2;
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.drawTexturedModalRect(☃xx, this.y, ☃, ☃x, this.width, this.height);
         GlStateManager.enableDepth();
         RenderHelper.enableGUIStandardItemLighting();
         GlStateManager.disableLighting();
         this.renderIcon(☃.getRenderItem());
         GlStateManager.enableLighting();
         RenderHelper.disableStandardItemLighting();
         if (this.animationTime > 0.0F) {
            GlStateManager.popMatrix();
            this.animationTime -= ☃;
         }
      }
   }

   private void renderIcon(RenderItem var1) {
      ItemStack ☃ = this.category.getIcon();
      if (this.category == CreativeTabs.TOOLS) {
         ☃.renderItemAndEffectIntoGUI(☃, this.x + 3, this.y + 5);
         ☃.renderItemAndEffectIntoGUI(CreativeTabs.COMBAT.getIcon(), this.x + 14, this.y + 5);
      } else if (this.category == CreativeTabs.MISC) {
         ☃.renderItemAndEffectIntoGUI(☃, this.x + 3, this.y + 5);
         ☃.renderItemAndEffectIntoGUI(CreativeTabs.FOOD.getIcon(), this.x + 14, this.y + 5);
      } else {
         ☃.renderItemAndEffectIntoGUI(☃, this.x + 9, this.y + 5);
      }
   }

   public CreativeTabs getCategory() {
      return this.category;
   }

   public boolean updateVisibility() {
      List<RecipeList> ☃ = RecipeBookClient.RECIPES_BY_TAB.get(this.category);
      this.visible = false;

      for (RecipeList ☃x : ☃) {
         if (☃x.isNotEmpty() && ☃x.containsValidRecipes()) {
            this.visible = true;
            break;
         }
      }

      return this.visible;
   }
}
