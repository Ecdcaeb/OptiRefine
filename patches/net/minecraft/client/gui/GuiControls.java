package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class GuiControls extends GuiScreen {
   private static final GameSettings.Options[] OPTIONS_ARR = new GameSettings.Options[]{
      GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN, GameSettings.Options.AUTO_JUMP
   };
   private final GuiScreen parentScreen;
   protected String screenTitle = "Controls";
   private final GameSettings options;
   public KeyBinding buttonId;
   public long time;
   private GuiKeyBindingList keyBindingList;
   private GuiButton buttonReset;

   public GuiControls(GuiScreen var1, GameSettings var2) {
      this.parentScreen = ☃;
      this.options = ☃;
   }

   @Override
   public void initGui() {
      this.keyBindingList = new GuiKeyBindingList(this, this.mc);
      this.buttonList.add(new GuiButton(200, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.done")));
      this.buttonReset = this.addButton(new GuiButton(201, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("controls.resetAll")));
      this.screenTitle = I18n.format("controls.title");
      int ☃ = 0;

      for (GameSettings.Options ☃x : OPTIONS_ARR) {
         if (☃x.isFloat()) {
            this.buttonList.add(new GuiOptionSlider(☃x.getOrdinal(), this.width / 2 - 155 + ☃ % 2 * 160, 18 + 24 * (☃ >> 1), ☃x));
         } else {
            this.buttonList
               .add(new GuiOptionButton(☃x.getOrdinal(), this.width / 2 - 155 + ☃ % 2 * 160, 18 + 24 * (☃ >> 1), ☃x, this.options.getKeyBinding(☃x)));
         }

         ☃++;
      }
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      this.keyBindingList.handleMouseInput();
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == 200) {
         this.mc.displayGuiScreen(this.parentScreen);
      } else if (☃.id == 201) {
         for (KeyBinding ☃ : this.mc.gameSettings.keyBindings) {
            ☃.setKeyCode(☃.getKeyCodeDefault());
         }

         KeyBinding.resetKeyBindingArrayAndHash();
      } else if (☃.id < 100 && ☃ instanceof GuiOptionButton) {
         this.options.setOptionValue(((GuiOptionButton)☃).getOption(), 1);
         ☃.displayString = this.options.getKeyBinding(GameSettings.Options.byOrdinal(☃.id));
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      if (this.buttonId != null) {
         this.options.setOptionKeyBinding(this.buttonId, -100 + ☃);
         this.buttonId = null;
         KeyBinding.resetKeyBindingArrayAndHash();
      } else if (☃ != 0 || !this.keyBindingList.mouseClicked(☃, ☃, ☃)) {
         super.mouseClicked(☃, ☃, ☃);
      }
   }

   @Override
   protected void mouseReleased(int var1, int var2, int var3) {
      if (☃ != 0 || !this.keyBindingList.mouseReleased(☃, ☃, ☃)) {
         super.mouseReleased(☃, ☃, ☃);
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (this.buttonId != null) {
         if (☃ == 1) {
            this.options.setOptionKeyBinding(this.buttonId, 0);
         } else if (☃ != 0) {
            this.options.setOptionKeyBinding(this.buttonId, ☃);
         } else if (☃ > 0) {
            this.options.setOptionKeyBinding(this.buttonId, ☃ + 256);
         }

         this.buttonId = null;
         this.time = Minecraft.getSystemTime();
         KeyBinding.resetKeyBindingArrayAndHash();
      } else {
         super.keyTyped(☃, ☃);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.keyBindingList.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);
      boolean ☃ = false;

      for (KeyBinding ☃x : this.options.keyBindings) {
         if (☃x.getKeyCode() != ☃x.getKeyCodeDefault()) {
            ☃ = true;
            break;
         }
      }

      this.buttonReset.enabled = ☃;
      super.drawScreen(☃, ☃, ☃);
   }
}
