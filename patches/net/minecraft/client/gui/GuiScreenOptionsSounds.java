package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class GuiScreenOptionsSounds extends GuiScreen {
   private final GuiScreen parent;
   private final GameSettings game_settings_4;
   protected String title = "Options";
   private String offDisplayString;

   public GuiScreenOptionsSounds(GuiScreen var1, GameSettings var2) {
      this.parent = ☃;
      this.game_settings_4 = ☃;
   }

   @Override
   public void initGui() {
      this.title = I18n.format("options.sounds.title");
      this.offDisplayString = I18n.format("options.off");
      int ☃ = 0;
      this.buttonList
         .add(
            new GuiScreenOptionsSounds.Button(
               SoundCategory.MASTER.ordinal(), this.width / 2 - 155 + ☃ % 2 * 160, this.height / 6 - 12 + 24 * (☃ >> 1), SoundCategory.MASTER, true
            )
         );
      ☃ += 2;

      for (SoundCategory ☃x : SoundCategory.values()) {
         if (☃x != SoundCategory.MASTER) {
            this.buttonList
               .add(new GuiScreenOptionsSounds.Button(☃x.ordinal(), this.width / 2 - 155 + ☃ % 2 * 160, this.height / 6 - 12 + 24 * (☃ >> 1), ☃x, false));
            ☃++;
         }
      }

      this.buttonList
         .add(
            new GuiOptionButton(
               201,
               this.width / 2 - 75,
               this.height / 6 - 12 + 24 * (++☃ >> 1),
               GameSettings.Options.SHOW_SUBTITLES,
               this.game_settings_4.getKeyBinding(GameSettings.Options.SHOW_SUBTITLES)
            )
         );
      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
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
            this.mc.displayGuiScreen(this.parent);
         } else if (☃.id == 201) {
            this.mc.gameSettings.setOptionValue(GameSettings.Options.SHOW_SUBTITLES, 1);
            ☃.displayString = this.mc.gameSettings.getKeyBinding(GameSettings.Options.SHOW_SUBTITLES);
            this.mc.gameSettings.saveOptions();
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
      super.drawScreen(☃, ☃, ☃);
   }

   protected String getDisplayString(SoundCategory var1) {
      float ☃ = this.game_settings_4.getSoundLevel(☃);
      return ☃ == 0.0F ? this.offDisplayString : (int)(☃ * 100.0F) + "%";
   }

   class Button extends GuiButton {
      private final SoundCategory category;
      private final String categoryName;
      public float volume = 1.0F;
      public boolean pressed;

      public Button(int var2, int var3, int var4, SoundCategory var5, boolean var6) {
         super(☃, ☃, ☃, ☃ ? 310 : 150, 20, "");
         this.category = ☃;
         this.categoryName = I18n.format("soundCategory." + ☃.getName());
         this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(☃);
         this.volume = GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(☃);
      }

      @Override
      protected int getHoverState(boolean var1) {
         return 0;
      }

      @Override
      protected void mouseDragged(Minecraft var1, int var2, int var3) {
         if (this.visible) {
            if (this.pressed) {
               this.volume = (float)(☃ - (this.x + 4)) / (this.width - 8);
               this.volume = MathHelper.clamp(this.volume, 0.0F, 1.0F);
               ☃.gameSettings.setSoundLevel(this.category, this.volume);
               ☃.gameSettings.saveOptions();
               this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(this.category);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.x + (int)(this.volume * (this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.x + (int)(this.volume * (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
         }
      }

      @Override
      public boolean mousePressed(Minecraft var1, int var2, int var3) {
         if (super.mousePressed(☃, ☃, ☃)) {
            this.volume = (float)(☃ - (this.x + 4)) / (this.width - 8);
            this.volume = MathHelper.clamp(this.volume, 0.0F, 1.0F);
            ☃.gameSettings.setSoundLevel(this.category, this.volume);
            ☃.gameSettings.saveOptions();
            this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(this.category);
            this.pressed = true;
            return true;
         } else {
            return false;
         }
      }

      @Override
      public void playPressSound(SoundHandler var1) {
      }

      @Override
      public void mouseReleased(int var1, int var2) {
         if (this.pressed) {
            GuiScreenOptionsSounds.this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }

         this.pressed = false;
      }
   }
}
