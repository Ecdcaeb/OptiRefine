package net.minecraft.client.gui;

import java.net.URI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiScreenDemo extends GuiScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation DEMO_BACKGROUND_LOCATION = new ResourceLocation("textures/gui/demo_background.png");

   @Override
   public void initGui() {
      this.buttonList.clear();
      int ☃ = -16;
      this.buttonList.add(new GuiButton(1, this.width / 2 - 116, this.height / 2 + 62 + -16, 114, 20, I18n.format("demo.help.buy")));
      this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height / 2 + 62 + -16, 114, 20, I18n.format("demo.help.later")));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      switch (☃.id) {
         case 1:
            ☃.enabled = false;

            try {
               Class<?> ☃ = Class.forName("java.awt.Desktop");
               Object ☃x = ☃.getMethod("getDesktop").invoke(null);
               ☃.getMethod("browse", URI.class).invoke(☃x, new URI("http://www.minecraft.net/store?source=demo"));
            } catch (Throwable var4) {
               LOGGER.error("Couldn't open link", var4);
            }
            break;
         case 2:
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
      }
   }

   @Override
   public void drawDefaultBackground() {
      super.drawDefaultBackground();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(DEMO_BACKGROUND_LOCATION);
      int ☃ = (this.width - 248) / 2;
      int ☃x = (this.height - 166) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, 248, 166);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      int ☃ = (this.width - 248) / 2 + 10;
      int ☃x = (this.height - 166) / 2 + 8;
      this.fontRenderer.drawString(I18n.format("demo.help.title"), ☃, ☃x, 2039583);
      ☃x += 12;
      GameSettings ☃xx = this.mc.gameSettings;
      this.fontRenderer
         .drawString(
            I18n.format(
               "demo.help.movementShort",
               GameSettings.getKeyDisplayString(☃xx.keyBindForward.getKeyCode()),
               GameSettings.getKeyDisplayString(☃xx.keyBindLeft.getKeyCode()),
               GameSettings.getKeyDisplayString(☃xx.keyBindBack.getKeyCode()),
               GameSettings.getKeyDisplayString(☃xx.keyBindRight.getKeyCode())
            ),
            ☃,
            ☃x,
            5197647
         );
      this.fontRenderer.drawString(I18n.format("demo.help.movementMouse"), ☃, ☃x + 12, 5197647);
      this.fontRenderer.drawString(I18n.format("demo.help.jump", GameSettings.getKeyDisplayString(☃xx.keyBindJump.getKeyCode())), ☃, ☃x + 24, 5197647);
      this.fontRenderer
         .drawString(I18n.format("demo.help.inventory", GameSettings.getKeyDisplayString(☃xx.keyBindInventory.getKeyCode())), ☃, ☃x + 36, 5197647);
      this.fontRenderer.drawSplitString(I18n.format("demo.help.fullWrapped"), ☃, ☃x + 68, 218, 2039583);
      super.drawScreen(☃, ☃, ☃);
   }
}
