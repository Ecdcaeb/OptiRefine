package net.minecraft.client.gui;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiVideoSettings extends GuiScreen {
   private final GuiScreen parentGuiScreen;
   protected String screenTitle = "Video Settings";
   private final GameSettings guiGameSettings;
   private GuiListExtended optionsRowList;
   private static final GameSettings.Options[] VIDEO_OPTIONS = new GameSettings.Options[]{
      GameSettings.Options.GRAPHICS,
      GameSettings.Options.RENDER_DISTANCE,
      GameSettings.Options.AMBIENT_OCCLUSION,
      GameSettings.Options.FRAMERATE_LIMIT,
      GameSettings.Options.ANAGLYPH,
      GameSettings.Options.VIEW_BOBBING,
      GameSettings.Options.GUI_SCALE,
      GameSettings.Options.ATTACK_INDICATOR,
      GameSettings.Options.GAMMA,
      GameSettings.Options.RENDER_CLOUDS,
      GameSettings.Options.PARTICLES,
      GameSettings.Options.USE_FULLSCREEN,
      GameSettings.Options.ENABLE_VSYNC,
      GameSettings.Options.MIPMAP_LEVELS,
      GameSettings.Options.USE_VBO,
      GameSettings.Options.ENTITY_SHADOWS
   };

   public GuiVideoSettings(GuiScreen var1, GameSettings var2) {
      this.parentGuiScreen = ☃;
      this.guiGameSettings = ☃;
   }

   @Override
   public void initGui() {
      this.screenTitle = I18n.format("options.videoTitle");
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 27, I18n.format("gui.done")));
      if (OpenGlHelper.vboSupported) {
         this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, VIDEO_OPTIONS);
      } else {
         GameSettings.Options[] ☃ = new GameSettings.Options[VIDEO_OPTIONS.length - 1];
         int ☃x = 0;

         for (GameSettings.Options ☃xx : VIDEO_OPTIONS) {
            if (☃xx == GameSettings.Options.USE_VBO) {
               break;
            }

            ☃[☃x] = ☃xx;
            ☃x++;
         }

         this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, ☃);
      }
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      this.optionsRowList.handleMouseInput();
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
            this.mc.displayGuiScreen(this.parentGuiScreen);
         }
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      int ☃ = this.guiGameSettings.guiScale;
      super.mouseClicked(☃, ☃, ☃);
      this.optionsRowList.mouseClicked(☃, ☃, ☃);
      if (this.guiGameSettings.guiScale != ☃) {
         ScaledResolution ☃x = new ScaledResolution(this.mc);
         int ☃xx = ☃x.getScaledWidth();
         int ☃xxx = ☃x.getScaledHeight();
         this.setWorldAndResolution(this.mc, ☃xx, ☃xxx);
      }
   }

   @Override
   protected void mouseReleased(int var1, int var2, int var3) {
      int ☃ = this.guiGameSettings.guiScale;
      super.mouseReleased(☃, ☃, ☃);
      this.optionsRowList.mouseReleased(☃, ☃, ☃);
      if (this.guiGameSettings.guiScale != ☃) {
         ScaledResolution ☃x = new ScaledResolution(this.mc);
         int ☃xx = ☃x.getScaledWidth();
         int ☃xxx = ☃x.getScaledHeight();
         this.setWorldAndResolution(this.mc, ☃xx, ☃xxx);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.optionsRowList.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 5, 16777215);
      super.drawScreen(☃, ☃, ☃);
   }
}
