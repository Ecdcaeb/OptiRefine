package net.minecraft.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class GuiCustomizeSkin extends GuiScreen {
   private final GuiScreen parentScreen;
   private String title;

   public GuiCustomizeSkin(GuiScreen var1) {
      this.parentScreen = ☃;
   }

   @Override
   public void initGui() {
      int ☃ = 0;
      this.title = I18n.format("options.skinCustomisation.title");

      for (EnumPlayerModelParts ☃x : EnumPlayerModelParts.values()) {
         this.buttonList.add(new GuiCustomizeSkin.ButtonPart(☃x.getPartId(), this.width / 2 - 155 + ☃ % 2 * 160, this.height / 6 + 24 * (☃ >> 1), 150, 20, ☃x));
         ☃++;
      }

      this.buttonList
         .add(
            new GuiOptionButton(
               199,
               this.width / 2 - 155 + ☃ % 2 * 160,
               this.height / 6 + 24 * (☃ >> 1),
               GameSettings.Options.MAIN_HAND,
               this.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND)
            )
         );
      if (++☃ % 2 == 1) {
         ☃++;
      }

      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 24 * (☃ >> 1), I18n.format("gui.done")));
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
         if (☃.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (☃.id == 199) {
            this.mc.gameSettings.setOptionValue(GameSettings.Options.MAIN_HAND, 1);
            ☃.displayString = this.mc.gameSettings.getKeyBinding(GameSettings.Options.MAIN_HAND);
            this.mc.gameSettings.sendSettingsToServer();
         } else if (☃ instanceof GuiCustomizeSkin.ButtonPart) {
            EnumPlayerModelParts ☃ = ((GuiCustomizeSkin.ButtonPart)☃).playerModelParts;
            this.mc.gameSettings.switchModelPartEnabled(☃);
            ☃.displayString = this.getMessage(☃);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
      super.drawScreen(☃, ☃, ☃);
   }

   private String getMessage(EnumPlayerModelParts var1) {
      String ☃;
      if (this.mc.gameSettings.getModelParts().contains(☃)) {
         ☃ = I18n.format("options.on");
      } else {
         ☃ = I18n.format("options.off");
      }

      return ☃.getName().getFormattedText() + ": " + ☃;
   }

   class ButtonPart extends GuiButton {
      private final EnumPlayerModelParts playerModelParts;

      private ButtonPart(int var2, int var3, int var4, int var5, int var6, EnumPlayerModelParts var7) {
         super(☃, ☃, ☃, ☃, ☃, GuiCustomizeSkin.this.getMessage(☃));
         this.playerModelParts = ☃;
      }
   }
}
