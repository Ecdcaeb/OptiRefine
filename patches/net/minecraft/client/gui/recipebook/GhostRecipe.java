package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;

public class GhostRecipe {
   private IRecipe recipe;
   private final List<GhostRecipe.GhostIngredient> ingredients = Lists.newArrayList();
   private float time;

   public void clear() {
      this.recipe = null;
      this.ingredients.clear();
      this.time = 0.0F;
   }

   public void addIngredient(Ingredient var1, int var2, int var3) {
      this.ingredients.add(new GhostRecipe.GhostIngredient(☃, ☃, ☃));
   }

   public GhostRecipe.GhostIngredient get(int var1) {
      return this.ingredients.get(☃);
   }

   public int size() {
      return this.ingredients.size();
   }

   @Nullable
   public IRecipe getRecipe() {
      return this.recipe;
   }

   public void setRecipe(IRecipe var1) {
      this.recipe = ☃;
   }

   public void render(Minecraft var1, int var2, int var3, boolean var4, float var5) {
      if (!GuiScreen.isCtrlKeyDown()) {
         this.time += ☃;
      }

      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.disableLighting();

      for (int ☃ = 0; ☃ < this.ingredients.size(); ☃++) {
         GhostRecipe.GhostIngredient ☃x = this.ingredients.get(☃);
         int ☃xx = ☃x.getX() + ☃;
         int ☃xxx = ☃x.getY() + ☃;
         if (☃ == 0 && ☃) {
            Gui.drawRect(☃xx - 4, ☃xxx - 4, ☃xx + 20, ☃xxx + 20, 822018048);
         } else {
            Gui.drawRect(☃xx, ☃xxx, ☃xx + 16, ☃xxx + 16, 822018048);
         }

         GlStateManager.disableLighting();
         ItemStack ☃xxxx = ☃x.getItem();
         RenderItem ☃xxxxx = ☃.getRenderItem();
         ☃xxxxx.renderItemAndEffectIntoGUI(☃.player, ☃xxxx, ☃xx, ☃xxx);
         GlStateManager.depthFunc(516);
         Gui.drawRect(☃xx, ☃xxx, ☃xx + 16, ☃xxx + 16, 822083583);
         GlStateManager.depthFunc(515);
         if (☃ == 0) {
            ☃xxxxx.renderItemOverlays(☃.fontRenderer, ☃xxxx, ☃xx, ☃xxx);
         }

         GlStateManager.enableLighting();
      }

      RenderHelper.disableStandardItemLighting();
   }

   public class GhostIngredient {
      private final Ingredient ingredient;
      private final int x;
      private final int y;

      public GhostIngredient(Ingredient var2, int var3, int var4) {
         this.ingredient = ☃;
         this.x = ☃;
         this.y = ☃;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public ItemStack getItem() {
         ItemStack[] ☃ = this.ingredient.getMatchingStacks();
         return ☃[MathHelper.floor(GhostRecipe.this.time / 30.0F) % ☃.length];
      }
   }
}
