package net.minecraft.client.gui.toasts;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;

public class SystemToast implements IToast {
   private final SystemToast.Type type;
   private String title;
   private String subtitle;
   private long firstDrawTime;
   private boolean newDisplay;

   public SystemToast(SystemToast.Type var1, ITextComponent var2, @Nullable ITextComponent var3) {
      this.type = ☃;
      this.title = ☃.getUnformattedText();
      this.subtitle = ☃ == null ? null : ☃.getUnformattedText();
   }

   @Override
   public IToast.Visibility draw(GuiToast var1, long var2) {
      if (this.newDisplay) {
         this.firstDrawTime = ☃;
         this.newDisplay = false;
      }

      ☃.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      ☃.drawTexturedModalRect(0, 0, 0, 64, 160, 32);
      if (this.subtitle == null) {
         ☃.getMinecraft().fontRenderer.drawString(this.title, 18, 12, -256);
      } else {
         ☃.getMinecraft().fontRenderer.drawString(this.title, 18, 7, -256);
         ☃.getMinecraft().fontRenderer.drawString(this.subtitle, 18, 18, -1);
      }

      return ☃ - this.firstDrawTime < 5000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
   }

   public void setDisplayedText(ITextComponent var1, @Nullable ITextComponent var2) {
      this.title = ☃.getUnformattedText();
      this.subtitle = ☃ == null ? null : ☃.getUnformattedText();
      this.newDisplay = true;
   }

   public SystemToast.Type getType() {
      return this.type;
   }

   public static void addOrUpdate(GuiToast var0, SystemToast.Type var1, ITextComponent var2, @Nullable ITextComponent var3) {
      SystemToast ☃ = ☃.getToast(SystemToast.class, ☃);
      if (☃ == null) {
         ☃.add(new SystemToast(☃, ☃, ☃));
      } else {
         ☃.setDisplayedText(☃, ☃);
      }
   }

   public static enum Type {
      TUTORIAL_HINT,
      NARRATOR_TOGGLE;
   }
}
