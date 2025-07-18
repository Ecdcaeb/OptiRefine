package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class GuiOptions extends GuiScreen {
   private static final GameSettings.Options[] SCREEN_OPTIONS = new GameSettings.Options[]{GameSettings.Options.FOV};
   private final GuiScreen lastScreen;
   private final GameSettings settings;
   private GuiButton difficultyButton;
   private GuiLockIconButton lockButton;
   protected String title = "Options";

   public GuiOptions(GuiScreen var1, GameSettings var2) {
      this.lastScreen = ☃;
      this.settings = ☃;
   }

   @Override
   public void initGui() {
      this.title = I18n.format("options.title");
      int ☃ = 0;

      for (GameSettings.Options ☃x : SCREEN_OPTIONS) {
         if (☃x.isFloat()) {
            this.buttonList.add(new GuiOptionSlider(☃x.getOrdinal(), this.width / 2 - 155 + ☃ % 2 * 160, this.height / 6 - 12 + 24 * (☃ >> 1), ☃x));
         } else {
            GuiOptionButton ☃xx = new GuiOptionButton(
               ☃x.getOrdinal(), this.width / 2 - 155 + ☃ % 2 * 160, this.height / 6 - 12 + 24 * (☃ >> 1), ☃x, this.settings.getKeyBinding(☃x)
            );
            this.buttonList.add(☃xx);
         }

         ☃++;
      }

      if (this.mc.world != null) {
         EnumDifficulty ☃x = this.mc.world.getDifficulty();
         this.difficultyButton = new GuiButton(
            108, this.width / 2 - 155 + ☃ % 2 * 160, this.height / 6 - 12 + 24 * (☃ >> 1), 150, 20, this.getDifficultyText(☃x)
         );
         this.buttonList.add(this.difficultyButton);
         if (this.mc.isSingleplayer() && !this.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
            this.difficultyButton.setWidth(this.difficultyButton.getButtonWidth() - 20);
            this.lockButton = new GuiLockIconButton(109, this.difficultyButton.x + this.difficultyButton.getButtonWidth(), this.difficultyButton.y);
            this.buttonList.add(this.lockButton);
            this.lockButton.setLocked(this.mc.world.getWorldInfo().isDifficultyLocked());
            this.lockButton.enabled = !this.lockButton.isLocked();
            this.difficultyButton.enabled = !this.lockButton.isLocked();
         } else {
            this.difficultyButton.enabled = false;
         }
      } else {
         this.buttonList
            .add(
               new GuiOptionButton(
                  GameSettings.Options.REALMS_NOTIFICATIONS.getOrdinal(),
                  this.width / 2 - 155 + ☃ % 2 * 160,
                  this.height / 6 - 12 + 24 * (☃ >> 1),
                  GameSettings.Options.REALMS_NOTIFICATIONS,
                  this.settings.getKeyBinding(GameSettings.Options.REALMS_NOTIFICATIONS)
               )
            );
      }

      this.buttonList.add(new GuiButton(110, this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation")));
      this.buttonList.add(new GuiButton(106, this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, I18n.format("options.sounds")));
      this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20, I18n.format("options.video")));
      this.buttonList.add(new GuiButton(100, this.width / 2 + 5, this.height / 6 + 72 - 6, 150, 20, I18n.format("options.controls")));
      this.buttonList.add(new GuiButton(102, this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20, I18n.format("options.language")));
      this.buttonList.add(new GuiButton(103, this.width / 2 + 5, this.height / 6 + 96 - 6, 150, 20, I18n.format("options.chat.title")));
      this.buttonList.add(new GuiButton(105, this.width / 2 - 155, this.height / 6 + 120 - 6, 150, 20, I18n.format("options.resourcepack")));
      this.buttonList.add(new GuiButton(104, this.width / 2 + 5, this.height / 6 + 120 - 6, 150, 20, I18n.format("options.snooper.view")));
      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
   }

   public String getDifficultyText(EnumDifficulty var1) {
      ITextComponent ☃ = new TextComponentString("");
      ☃.appendSibling(new TextComponentTranslation("options.difficulty"));
      ☃.appendText(": ");
      ☃.appendSibling(new TextComponentTranslation(☃.getTranslationKey()));
      return ☃.getFormattedText();
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      this.mc.displayGuiScreen(this);
      if (☃ == 109 && ☃ && this.mc.world != null) {
         this.mc.world.getWorldInfo().setDifficultyLocked(true);
         this.lockButton.setLocked(true);
         this.lockButton.enabled = false;
         this.difficultyButton.enabled = false;
      }
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
            GameSettings.Options ☃ = ((GuiOptionButton)☃).getOption();
            this.settings.setOptionValue(☃, 1);
            ☃.displayString = this.settings.getKeyBinding(GameSettings.Options.byOrdinal(☃.id));
         }

         if (☃.id == 108) {
            this.mc.world.getWorldInfo().setDifficulty(EnumDifficulty.byId(this.mc.world.getDifficulty().getId() + 1));
            this.difficultyButton.displayString = this.getDifficultyText(this.mc.world.getDifficulty());
         }

         if (☃.id == 109) {
            this.mc
               .displayGuiScreen(
                  new GuiYesNo(
                     this,
                     new TextComponentTranslation("difficulty.lock.title").getFormattedText(),
                     new TextComponentTranslation(
                           "difficulty.lock.question", new TextComponentTranslation(this.mc.world.getWorldInfo().getDifficulty().getTranslationKey())
                        )
                        .getFormattedText(),
                     109
                  )
               );
         }

         if (☃.id == 110) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiCustomizeSkin(this));
         }

         if (☃.id == 101) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiVideoSettings(this, this.settings));
         }

         if (☃.id == 100) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiControls(this, this.settings));
         }

         if (☃.id == 102) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiLanguage(this, this.settings, this.mc.getLanguageManager()));
         }

         if (☃.id == 103) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new ScreenChatOptions(this, this.settings));
         }

         if (☃.id == 104) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiSnooper(this, this.settings));
         }

         if (☃.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.lastScreen);
         }

         if (☃.id == 105) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
         }

         if (☃.id == 106) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.settings));
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
      super.drawScreen(☃, ☃, ☃);
   }
}
