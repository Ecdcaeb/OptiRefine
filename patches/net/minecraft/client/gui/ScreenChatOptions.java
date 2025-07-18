package net.minecraft.client.gui;

import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class ScreenChatOptions extends GuiScreen {
   private static final GameSettings.Options[] CHAT_OPTIONS = new GameSettings.Options[]{
      GameSettings.Options.CHAT_VISIBILITY,
      GameSettings.Options.CHAT_COLOR,
      GameSettings.Options.CHAT_LINKS,
      GameSettings.Options.CHAT_OPACITY,
      GameSettings.Options.CHAT_LINKS_PROMPT,
      GameSettings.Options.CHAT_SCALE,
      GameSettings.Options.CHAT_HEIGHT_FOCUSED,
      GameSettings.Options.CHAT_HEIGHT_UNFOCUSED,
      GameSettings.Options.CHAT_WIDTH,
      GameSettings.Options.REDUCED_DEBUG_INFO,
      GameSettings.Options.NARRATOR
   };
   private final GuiScreen parentScreen;
   private final GameSettings game_settings;
   private String chatTitle;
   private GuiOptionButton narratorButton;

   public ScreenChatOptions(GuiScreen var1, GameSettings var2) {
      this.parentScreen = ☃;
      this.game_settings = ☃;
   }

   @Override
   public void initGui() {
      this.chatTitle = I18n.format("options.chat.title");
      int ☃ = 0;

      for (GameSettings.Options ☃x : CHAT_OPTIONS) {
         if (☃x.isFloat()) {
            this.buttonList.add(new GuiOptionSlider(☃x.getOrdinal(), this.width / 2 - 155 + ☃ % 2 * 160, this.height / 6 + 24 * (☃ >> 1), ☃x));
         } else {
            GuiOptionButton ☃xx = new GuiOptionButton(
               ☃x.getOrdinal(), this.width / 2 - 155 + ☃ % 2 * 160, this.height / 6 + 24 * (☃ >> 1), ☃x, this.game_settings.getKeyBinding(☃x)
            );
            this.buttonList.add(☃xx);
            if (☃x == GameSettings.Options.NARRATOR) {
               this.narratorButton = ☃xx;
               ☃xx.enabled = NarratorChatListener.INSTANCE.isActive();
            }
         }

         ☃++;
      }

      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 144, I18n.format("gui.done")));
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (☃ == 1) {
         this.mc.gameSettings.saveOptions();
      }

      super.keyTyped(☃, ☃);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id < 100 && ☃ instanceof GuiOptionButton) {
            this.game_settings.setOptionValue(((GuiOptionButton)☃).getOption(), 1);
            ☃.displayString = this.game_settings.getKeyBinding(GameSettings.Options.byOrdinal(☃.id));
         }

         if (☃.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentScreen);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, this.chatTitle, this.width / 2, 20, 16777215);
      super.drawScreen(☃, ☃, ☃);
   }

   public void updateNarratorButton() {
      this.narratorButton.displayString = this.game_settings.getKeyBinding(GameSettings.Options.byOrdinal(this.narratorButton.id));
   }
}
