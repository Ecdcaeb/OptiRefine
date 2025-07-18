package net.minecraft.realms;

import java.util.List;
import net.minecraft.util.text.ITextComponent;

public class DisconnectedRealmsScreen extends RealmsScreen {
   private final String title;
   private final ITextComponent reason;
   private List<String> lines;
   private final RealmsScreen parent;
   private int textHeight;

   public DisconnectedRealmsScreen(RealmsScreen var1, String var2, ITextComponent var3) {
      this.parent = ☃;
      this.title = getLocalizedString(☃);
      this.reason = ☃;
   }

   @Override
   public void init() {
      Realms.setConnectedToRealms(false);
      Realms.clearResourcePack();
      this.buttonsClear();
      this.lines = this.fontSplit(this.reason.getFormattedText(), this.width() - 50);
      this.textHeight = this.lines.size() * this.fontLineHeight();
      this.buttonsAdd(newButton(0, this.width() / 2 - 100, this.height() / 2 + this.textHeight / 2 + this.fontLineHeight(), getLocalizedString("gui.back")));
   }

   @Override
   public void keyPressed(char var1, int var2) {
      if (☃ == 1) {
         Realms.setScreen(this.parent);
      }
   }

   @Override
   public void buttonClicked(RealmsButton var1) {
      if (☃.id() == 0) {
         Realms.setScreen(this.parent);
      }
   }

   @Override
   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(this.title, this.width() / 2, this.height() / 2 - this.textHeight / 2 - this.fontLineHeight() * 2, 11184810);
      int ☃ = this.height() / 2 - this.textHeight / 2;
      if (this.lines != null) {
         for (String ☃x : this.lines) {
            this.drawCenteredString(☃x, this.width() / 2, ☃, 16777215);
            ☃ += this.fontLineHeight();
         }
      }

      super.render(☃, ☃, ☃);
   }
}
