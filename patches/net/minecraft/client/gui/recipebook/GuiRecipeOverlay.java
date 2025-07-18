package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiRecipeOverlay extends Gui {
   private static final ResourceLocation RECIPE_BOOK_TEXTURE = new ResourceLocation("textures/gui/recipe_book.png");
   private final List<GuiRecipeOverlay.Button> buttonList = Lists.newArrayList();
   private boolean visible;
   private int x;
   private int y;
   private Minecraft mc;
   private RecipeList recipeList;
   private IRecipe lastRecipeClicked;
   private float time;

   public void init(Minecraft var1, RecipeList var2, int var3, int var4, int var5, int var6, float var7, RecipeBook var8) {
      this.mc = ☃;
      this.recipeList = ☃;
      boolean ☃ = ☃.isFilteringCraftable();
      List<IRecipe> ☃x = ☃.getDisplayRecipes(true);
      List<IRecipe> ☃xx = ☃ ? Collections.emptyList() : ☃.getDisplayRecipes(false);
      int ☃xxx = ☃x.size();
      int ☃xxxx = ☃xxx + ☃xx.size();
      int ☃xxxxx = ☃xxxx <= 16 ? 4 : 5;
      int ☃xxxxxx = (int)Math.ceil((float)☃xxxx / ☃xxxxx);
      this.x = ☃;
      this.y = ☃;
      int ☃xxxxxxx = 25;
      float ☃xxxxxxxx = this.x + Math.min(☃xxxx, ☃xxxxx) * 25;
      float ☃xxxxxxxxx = ☃ + 50;
      if (☃xxxxxxxx > ☃xxxxxxxxx) {
         this.x = (int)(this.x - ☃ * (int)((☃xxxxxxxx - ☃xxxxxxxxx) / ☃));
      }

      float ☃xxxxxxxxxx = this.y + ☃xxxxxx * 25;
      float ☃xxxxxxxxxxx = ☃ + 50;
      if (☃xxxxxxxxxx > ☃xxxxxxxxxxx) {
         this.y = (int)(this.y - ☃ * MathHelper.ceil((☃xxxxxxxxxx - ☃xxxxxxxxxxx) / ☃));
      }

      float ☃xxxxxxxxxxxx = this.y;
      float ☃xxxxxxxxxxxxx = ☃ - 100;
      if (☃xxxxxxxxxxxx < ☃xxxxxxxxxxxxx) {
         this.y = (int)(this.y - ☃ * MathHelper.ceil((☃xxxxxxxxxxxx - ☃xxxxxxxxxxxxx) / ☃));
      }

      this.visible = true;
      this.buttonList.clear();

      for (int ☃xxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxx < ☃xxxx; ☃xxxxxxxxxxxxxx++) {
         boolean ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx < ☃xxx;
         this.buttonList
            .add(
               new GuiRecipeOverlay.Button(
                  this.x + 4 + 25 * (☃xxxxxxxxxxxxxx % ☃xxxxx),
                  this.y + 5 + 25 * (☃xxxxxxxxxxxxxx / ☃xxxxx),
                  ☃xxxxxxxxxxxxxxx ? ☃x.get(☃xxxxxxxxxxxxxx) : ☃xx.get(☃xxxxxxxxxxxxxx - ☃xxx),
                  ☃xxxxxxxxxxxxxxx
               )
            );
      }

      this.lastRecipeClicked = null;
   }

   public RecipeList getRecipeList() {
      return this.recipeList;
   }

   public IRecipe getLastRecipeClicked() {
      return this.lastRecipeClicked;
   }

   public boolean buttonClicked(int var1, int var2, int var3) {
      if (☃ != 0) {
         return false;
      } else {
         for (GuiRecipeOverlay.Button ☃ : this.buttonList) {
            if (☃.mousePressed(this.mc, ☃, ☃)) {
               this.lastRecipeClicked = ☃.recipe;
               return true;
            }
         }

         return false;
      }
   }

   public void render(int var1, int var2, float var3) {
      if (this.visible) {
         this.time += ☃;
         RenderHelper.enableGUIStandardItemLighting();
         GlStateManager.enableBlend();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(RECIPE_BOOK_TEXTURE);
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, 0.0F, 170.0F);
         int ☃ = this.buttonList.size() <= 16 ? 4 : 5;
         int ☃x = Math.min(this.buttonList.size(), ☃);
         int ☃xx = MathHelper.ceil((float)this.buttonList.size() / ☃);
         int ☃xxx = 24;
         int ☃xxxx = 4;
         int ☃xxxxx = 82;
         int ☃xxxxxx = 208;
         this.nineInchSprite(☃x, ☃xx, 24, 4, 82, 208);
         GlStateManager.disableBlend();
         RenderHelper.disableStandardItemLighting();

         for (GuiRecipeOverlay.Button ☃xxxxxxx : this.buttonList) {
            ☃xxxxxxx.drawButton(this.mc, ☃, ☃, ☃);
         }

         GlStateManager.popMatrix();
      }
   }

   private void nineInchSprite(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.drawTexturedModalRect(this.x, this.y, ☃, ☃, ☃, ☃);
      this.drawTexturedModalRect(this.x + ☃ * 2 + ☃ * ☃, this.y, ☃ + ☃ + ☃, ☃, ☃, ☃);
      this.drawTexturedModalRect(this.x, this.y + ☃ * 2 + ☃ * ☃, ☃, ☃ + ☃ + ☃, ☃, ☃);
      this.drawTexturedModalRect(this.x + ☃ * 2 + ☃ * ☃, this.y + ☃ * 2 + ☃ * ☃, ☃ + ☃ + ☃, ☃ + ☃ + ☃, ☃, ☃);

      for (int ☃ = 0; ☃ < ☃; ☃++) {
         this.drawTexturedModalRect(this.x + ☃ + ☃ * ☃, this.y, ☃ + ☃, ☃, ☃, ☃);
         this.drawTexturedModalRect(this.x + ☃ + (☃ + 1) * ☃, this.y, ☃ + ☃, ☃, ☃, ☃);

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            if (☃ == 0) {
               this.drawTexturedModalRect(this.x, this.y + ☃ + ☃x * ☃, ☃, ☃ + ☃, ☃, ☃);
               this.drawTexturedModalRect(this.x, this.y + ☃ + (☃x + 1) * ☃, ☃, ☃ + ☃, ☃, ☃);
            }

            this.drawTexturedModalRect(this.x + ☃ + ☃ * ☃, this.y + ☃ + ☃x * ☃, ☃ + ☃, ☃ + ☃, ☃, ☃);
            this.drawTexturedModalRect(this.x + ☃ + (☃ + 1) * ☃, this.y + ☃ + ☃x * ☃, ☃ + ☃, ☃ + ☃, ☃, ☃);
            this.drawTexturedModalRect(this.x + ☃ + ☃ * ☃, this.y + ☃ + (☃x + 1) * ☃, ☃ + ☃, ☃ + ☃, ☃, ☃);
            this.drawTexturedModalRect(this.x + ☃ + (☃ + 1) * ☃ - 1, this.y + ☃ + (☃x + 1) * ☃ - 1, ☃ + ☃, ☃ + ☃, ☃ + 1, ☃ + 1);
            if (☃ == ☃ - 1) {
               this.drawTexturedModalRect(this.x + ☃ * 2 + ☃ * ☃, this.y + ☃ + ☃x * ☃, ☃ + ☃ + ☃, ☃ + ☃, ☃, ☃);
               this.drawTexturedModalRect(this.x + ☃ * 2 + ☃ * ☃, this.y + ☃ + (☃x + 1) * ☃, ☃ + ☃ + ☃, ☃ + ☃, ☃, ☃);
            }
         }

         this.drawTexturedModalRect(this.x + ☃ + ☃ * ☃, this.y + ☃ * 2 + ☃ * ☃, ☃ + ☃, ☃ + ☃ + ☃, ☃, ☃);
         this.drawTexturedModalRect(this.x + ☃ + (☃ + 1) * ☃, this.y + ☃ * 2 + ☃ * ☃, ☃ + ☃, ☃ + ☃ + ☃, ☃, ☃);
      }
   }

   public void setVisible(boolean var1) {
      this.visible = ☃;
   }

   public boolean isVisible() {
      return this.visible;
   }

   class Button extends GuiButton {
      private final IRecipe recipe;
      private final boolean isCraftable;

      public Button(int var2, int var3, IRecipe var4, boolean var5) {
         super(0, ☃, ☃, "");
         this.width = 24;
         this.height = 24;
         this.recipe = ☃;
         this.isCraftable = ☃;
      }

      @Override
      public void drawButton(Minecraft var1, int var2, int var3, float var4) {
         RenderHelper.enableGUIStandardItemLighting();
         GlStateManager.enableAlpha();
         ☃.getTextureManager().bindTexture(GuiRecipeOverlay.RECIPE_BOOK_TEXTURE);
         this.hovered = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
         int ☃ = 152;
         if (!this.isCraftable) {
            ☃ += 26;
         }

         int ☃x = 78;
         if (this.hovered) {
            ☃x += 26;
         }

         this.drawTexturedModalRect(this.x, this.y, ☃, ☃x, this.width, this.height);
         int ☃xx = 3;
         int ☃xxx = 3;
         if (this.recipe instanceof ShapedRecipes) {
            ShapedRecipes ☃xxxx = (ShapedRecipes)this.recipe;
            ☃xx = ☃xxxx.getWidth();
            ☃xxx = ☃xxxx.getHeight();
         }

         Iterator<Ingredient> ☃xxxx = this.recipe.getIngredients().iterator();

         for (int ☃xxxxx = 0; ☃xxxxx < ☃xxx; ☃xxxxx++) {
            int ☃xxxxxx = 3 + ☃xxxxx * 7;

            for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xx; ☃xxxxxxx++) {
               if (☃xxxx.hasNext()) {
                  ItemStack[] ☃xxxxxxxx = ☃xxxx.next().getMatchingStacks();
                  if (☃xxxxxxxx.length != 0) {
                     int ☃xxxxxxxxx = 3 + ☃xxxxxxx * 7;
                     GlStateManager.pushMatrix();
                     float ☃xxxxxxxxxx = 0.42F;
                     int ☃xxxxxxxxxxx = (int)((this.x + ☃xxxxxxxxx) / 0.42F - 3.0F);
                     int ☃xxxxxxxxxxxx = (int)((this.y + ☃xxxxxx) / 0.42F - 3.0F);
                     GlStateManager.scale(0.42F, 0.42F, 1.0F);
                     GlStateManager.enableLighting();
                     ☃.getRenderItem()
                        .renderItemAndEffectIntoGUI(
                           ☃xxxxxxxx[MathHelper.floor(GuiRecipeOverlay.this.time / 30.0F) % ☃xxxxxxxx.length], ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx
                        );
                     GlStateManager.disableLighting();
                     GlStateManager.popMatrix();
                  }
               }
            }
         }

         GlStateManager.disableAlpha();
         RenderHelper.disableStandardItemLighting();
      }
   }
}
