package net.minecraft.client.gui;

import net.minecraft.client.settings.GameSettings;

public class GuiOptionButton extends GuiButton {
   private final GameSettings.Options enumOptions;

   public GuiOptionButton(int var1, int var2, int var3, String var4) {
      this(☃, ☃, ☃, null, ☃);
   }

   public GuiOptionButton(int var1, int var2, int var3, GameSettings.Options var4, String var5) {
      super(☃, ☃, ☃, 150, 20, ☃);
      this.enumOptions = ☃;
   }

   public GameSettings.Options getOption() {
      return this.enumOptions;
   }
}
