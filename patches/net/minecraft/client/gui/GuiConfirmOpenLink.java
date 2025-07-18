package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;

public class GuiConfirmOpenLink extends GuiYesNo {
   private final String openLinkWarning;
   private final String copyLinkButtonText;
   private final String linkText;
   private boolean showSecurityWarning = true;

   public GuiConfirmOpenLink(GuiYesNoCallback var1, String var2, int var3, boolean var4) {
      super(☃, I18n.format(☃ ? "chat.link.confirmTrusted" : "chat.link.confirm"), ☃, ☃);
      this.confirmButtonText = I18n.format(☃ ? "chat.link.open" : "gui.yes");
      this.cancelButtonText = I18n.format(☃ ? "gui.cancel" : "gui.no");
      this.copyLinkButtonText = I18n.format("chat.copy");
      this.openLinkWarning = I18n.format("chat.link.warning");
      this.linkText = ☃;
   }

   @Override
   public void initGui() {
      super.initGui();
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 50 - 105, this.height / 6 + 96, 100, 20, this.confirmButtonText));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height / 6 + 96, 100, 20, this.copyLinkButtonText));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 50 + 105, this.height / 6 + 96, 100, 20, this.cancelButtonText));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == 2) {
         this.copyLinkToClipboard();
      }

      this.parentScreen.confirmClicked(☃.id == 0, this.parentButtonClickedId);
   }

   public void copyLinkToClipboard() {
      setClipboardString(this.linkText);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      super.drawScreen(☃, ☃, ☃);
      if (this.showSecurityWarning) {
         this.drawCenteredString(this.fontRenderer, this.openLinkWarning, this.width / 2, 110, 16764108);
      }
   }

   public void disableSecurityWarning() {
      this.showSecurityWarning = false;
   }
}
