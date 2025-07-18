package net.minecraft.client.gui.toasts;

import javax.annotation.Nullable;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class TutorialToast implements IToast {
   private final TutorialToast.Icons icon;
   private final String title;
   private final String subtitle;
   private IToast.Visibility visibility = IToast.Visibility.SHOW;
   private long lastDelta;
   private float displayedProgress;
   private float currentProgress;
   private final boolean hasProgressBar;

   public TutorialToast(TutorialToast.Icons var1, ITextComponent var2, @Nullable ITextComponent var3, boolean var4) {
      this.icon = ☃;
      this.title = ☃.getFormattedText();
      this.subtitle = ☃ == null ? null : ☃.getFormattedText();
      this.hasProgressBar = ☃;
   }

   @Override
   public IToast.Visibility draw(GuiToast var1, long var2) {
      ☃.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      ☃.drawTexturedModalRect(0, 0, 0, 96, 160, 32);
      this.icon.draw(☃, 6, 6);
      if (this.subtitle == null) {
         ☃.getMinecraft().fontRenderer.drawString(this.title, 30, 12, -11534256);
      } else {
         ☃.getMinecraft().fontRenderer.drawString(this.title, 30, 7, -11534256);
         ☃.getMinecraft().fontRenderer.drawString(this.subtitle, 30, 18, -16777216);
      }

      if (this.hasProgressBar) {
         Gui.drawRect(3, 28, 157, 29, -1);
         float ☃ = (float)MathHelper.clampedLerp(this.displayedProgress, this.currentProgress, (float)(☃ - this.lastDelta) / 100.0F);
         int ☃x;
         if (this.currentProgress >= this.displayedProgress) {
            ☃x = -16755456;
         } else {
            ☃x = -11206656;
         }

         Gui.drawRect(3, 28, (int)(3.0F + 154.0F * ☃), 29, ☃x);
         this.displayedProgress = ☃;
         this.lastDelta = ☃;
      }

      return this.visibility;
   }

   public void hide() {
      this.visibility = IToast.Visibility.HIDE;
   }

   public void setProgress(float var1) {
      this.currentProgress = ☃;
   }

   public static enum Icons {
      MOVEMENT_KEYS(0, 0),
      MOUSE(1, 0),
      TREE(2, 0),
      RECIPE_BOOK(0, 1),
      WOODEN_PLANKS(1, 1);

      private final int column;
      private final int row;

      private Icons(int var3, int var4) {
         this.column = ☃;
         this.row = ☃;
      }

      public void draw(Gui var1, int var2, int var3) {
         GlStateManager.enableBlend();
         ☃.drawTexturedModalRect(☃, ☃, 176 + this.column * 20, this.row * 20, 20, 20);
         GlStateManager.enableBlend();
      }
   }
}
