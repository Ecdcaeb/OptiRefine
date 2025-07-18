package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

public class GuiDisconnected extends GuiScreen {
   private final String reason;
   private final ITextComponent message;
   private List<String> multilineMessage;
   private final GuiScreen parentScreen;
   private int textHeight;

   public GuiDisconnected(GuiScreen var1, String var2, ITextComponent var3) {
      this.parentScreen = ☃;
      this.reason = I18n.format(☃);
      this.message = ☃;
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      this.multilineMessage = this.fontRenderer.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
      this.textHeight = this.multilineMessage.size() * this.fontRenderer.FONT_HEIGHT;
      this.buttonList
         .add(
            new GuiButton(
               0,
               this.width / 2 - 100,
               Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT, this.height - 30),
               I18n.format("gui.toMenu")
            )
         );
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == 0) {
         this.mc.displayGuiScreen(this.parentScreen);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(
         this.fontRenderer, this.reason, this.width / 2, this.height / 2 - this.textHeight / 2 - this.fontRenderer.FONT_HEIGHT * 2, 11184810
      );
      int ☃ = this.height / 2 - this.textHeight / 2;
      if (this.multilineMessage != null) {
         for (String ☃x : this.multilineMessage) {
            this.drawCenteredString(this.fontRenderer, ☃x, this.width / 2, ☃, 16777215);
            ☃ += this.fontRenderer.FONT_HEIGHT;
         }
      }

      super.drawScreen(☃, ☃, ☃);
   }
}
