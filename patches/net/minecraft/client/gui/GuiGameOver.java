package net.minecraft.client.gui;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiGameOver extends GuiScreen {
   private int enableButtonsTimer;
   private final ITextComponent causeOfDeath;

   public GuiGameOver(@Nullable ITextComponent var1) {
      this.causeOfDeath = ☃;
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      this.enableButtonsTimer = 0;
      if (this.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
         this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.spectate")));
         this.buttonList
            .add(
               new GuiButton(
                  1,
                  this.width / 2 - 100,
                  this.height / 4 + 96,
                  I18n.format("deathScreen." + (this.mc.isIntegratedServerRunning() ? "deleteWorld" : "leaveServer"))
               )
            );
      } else {
         this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn")));
         this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen")));
         if (this.mc.getSession() == null) {
            this.buttonList.get(1).enabled = false;
         }
      }

      for (GuiButton ☃ : this.buttonList) {
         ☃.enabled = false;
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      switch (☃.id) {
         case 0:
            this.mc.player.respawnPlayer();
            this.mc.displayGuiScreen(null);
            break;
         case 1:
            if (this.mc.world.getWorldInfo().isHardcoreModeEnabled()) {
               this.mc.displayGuiScreen(new GuiMainMenu());
            } else {
               GuiYesNo ☃ = new GuiYesNo(
                  this, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0
               );
               this.mc.displayGuiScreen(☃);
               ☃.setButtonDelay(20);
            }
      }
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      if (☃) {
         if (this.mc.world != null) {
            this.mc.world.sendQuittingDisconnectingPacket();
         }

         this.mc.loadWorld(null);
         this.mc.displayGuiScreen(new GuiMainMenu());
      } else {
         this.mc.player.respawnPlayer();
         this.mc.displayGuiScreen(null);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      boolean ☃ = this.mc.world.getWorldInfo().isHardcoreModeEnabled();
      this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
      GlStateManager.pushMatrix();
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      this.drawCenteredString(this.fontRenderer, I18n.format(☃ ? "deathScreen.title.hardcore" : "deathScreen.title"), this.width / 2 / 2, 30, 16777215);
      GlStateManager.popMatrix();
      if (this.causeOfDeath != null) {
         this.drawCenteredString(this.fontRenderer, this.causeOfDeath.getFormattedText(), this.width / 2, 85, 16777215);
      }

      this.drawCenteredString(
         this.fontRenderer, I18n.format("deathScreen.score") + ": " + TextFormatting.YELLOW + this.mc.player.getScore(), this.width / 2, 100, 16777215
      );
      if (this.causeOfDeath != null && ☃ > 85 && ☃ < 85 + this.fontRenderer.FONT_HEIGHT) {
         ITextComponent ☃x = this.getClickedComponentAt(☃);
         if (☃x != null && ☃x.getStyle().getHoverEvent() != null) {
            this.handleComponentHover(☃x, ☃, ☃);
         }
      }

      super.drawScreen(☃, ☃, ☃);
   }

   @Nullable
   public ITextComponent getClickedComponentAt(int var1) {
      if (this.causeOfDeath == null) {
         return null;
      } else {
         int ☃ = this.mc.fontRenderer.getStringWidth(this.causeOfDeath.getFormattedText());
         int ☃x = this.width / 2 - ☃ / 2;
         int ☃xx = this.width / 2 + ☃ / 2;
         int ☃xxx = ☃x;
         if (☃ >= ☃x && ☃ <= ☃xx) {
            for (ITextComponent ☃xxxx : this.causeOfDeath) {
               ☃xxx += this.mc.fontRenderer.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(☃xxxx.getUnformattedComponentText(), false));
               if (☃xxx > ☃) {
                  return ☃xxxx;
               }
            }

            return null;
         } else {
            return null;
         }
      }
   }

   @Override
   public boolean doesGuiPauseGame() {
      return false;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.enableButtonsTimer++;
      if (this.enableButtonsTimer == 20) {
         for (GuiButton ☃ : this.buttonList) {
            ☃.enabled = true;
         }
      }
   }
}
