package net.minecraft.client.gui.toasts;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class RecipeToast implements IToast {
   private final List<ItemStack> recipesOutputs = Lists.newArrayList();
   private long firstDrawTime;
   private boolean hasNewOutputs;

   public RecipeToast(ItemStack var1) {
      this.recipesOutputs.add(☃);
   }

   @Override
   public IToast.Visibility draw(GuiToast var1, long var2) {
      if (this.hasNewOutputs) {
         this.firstDrawTime = ☃;
         this.hasNewOutputs = false;
      }

      if (this.recipesOutputs.isEmpty()) {
         return IToast.Visibility.HIDE;
      } else {
         ☃.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
         GlStateManager.color(1.0F, 1.0F, 1.0F);
         ☃.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
         ☃.getMinecraft().fontRenderer.drawString(I18n.format("recipe.toast.title"), 30, 7, -11534256);
         ☃.getMinecraft().fontRenderer.drawString(I18n.format("recipe.toast.description"), 30, 18, -16777216);
         RenderHelper.enableGUIStandardItemLighting();
         ☃.getMinecraft()
            .getRenderItem()
            .renderItemAndEffectIntoGUI(null, this.recipesOutputs.get((int)(☃ / (5000L / this.recipesOutputs.size()) % this.recipesOutputs.size())), 8, 8);
         return ☃ - this.firstDrawTime >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
      }
   }

   public void addRecipeOutput(ItemStack var1) {
      if (this.recipesOutputs.add(☃)) {
         this.hasNewOutputs = true;
      }
   }

   public static void addOrUpdate(GuiToast var0, IRecipe var1) {
      RecipeToast ☃ = ☃.getToast(RecipeToast.class, NO_TOKEN);
      if (☃ == null) {
         ☃.add(new RecipeToast(☃.getRecipeOutput()));
      } else {
         ☃.addRecipeOutput(☃.getRecipeOutput());
      }
   }
}
