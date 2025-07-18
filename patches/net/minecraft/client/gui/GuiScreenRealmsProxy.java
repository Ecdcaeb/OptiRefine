package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class GuiScreenRealmsProxy extends GuiScreen {
   private final RealmsScreen proxy;

   public GuiScreenRealmsProxy(RealmsScreen var1) {
      this.proxy = ☃;
      this.buttonList = Collections.synchronizedList(Lists.newArrayList());
   }

   public RealmsScreen getProxy() {
      return this.proxy;
   }

   @Override
   public void initGui() {
      this.proxy.init();
      super.initGui();
   }

   public void drawCenteredString(String var1, int var2, int var3, int var4) {
      super.drawCenteredString(this.fontRenderer, ☃, ☃, ☃, ☃);
   }

   public void drawString(String var1, int var2, int var3, int var4, boolean var5) {
      if (☃) {
         super.drawString(this.fontRenderer, ☃, ☃, ☃, ☃);
      } else {
         this.fontRenderer.drawString(☃, ☃, ☃, ☃);
      }
   }

   @Override
   public void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.proxy.blit(☃, ☃, ☃, ☃, ☃, ☃);
      super.drawTexturedModalRect(☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public void drawGradientRect(int var1, int var2, int var3, int var4, int var5, int var6) {
      super.drawGradientRect(☃, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public void drawDefaultBackground() {
      super.drawDefaultBackground();
   }

   @Override
   public boolean doesGuiPauseGame() {
      return super.doesGuiPauseGame();
   }

   @Override
   public void drawWorldBackground(int var1) {
      super.drawWorldBackground(☃);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.proxy.render(☃, ☃, ☃);
   }

   @Override
   public void renderToolTip(ItemStack var1, int var2, int var3) {
      super.renderToolTip(☃, ☃, ☃);
   }

   @Override
   public void drawHoveringText(String var1, int var2, int var3) {
      super.drawHoveringText(☃, ☃, ☃);
   }

   @Override
   public void drawHoveringText(List<String> var1, int var2, int var3) {
      super.drawHoveringText(☃, ☃, ☃);
   }

   @Override
   public void updateScreen() {
      this.proxy.tick();
      super.updateScreen();
   }

   public int getFontHeight() {
      return this.fontRenderer.FONT_HEIGHT;
   }

   public int getStringWidth(String var1) {
      return this.fontRenderer.getStringWidth(☃);
   }

   public void fontDrawShadow(String var1, int var2, int var3, int var4) {
      this.fontRenderer.drawStringWithShadow(☃, ☃, ☃, ☃);
   }

   public List<String> fontSplit(String var1, int var2) {
      return this.fontRenderer.listFormattedStringToWidth(☃, ☃);
   }

   @Override
   public final void actionPerformed(GuiButton var1) {
      this.proxy.buttonClicked(((GuiButtonRealmsProxy)☃).getRealmsButton());
   }

   public void buttonsClear() {
      this.buttonList.clear();
   }

   public void buttonsAdd(RealmsButton var1) {
      this.buttonList.add(☃.getProxy());
   }

   public List<RealmsButton> buttons() {
      List<RealmsButton> ☃ = Lists.newArrayListWithExpectedSize(this.buttonList.size());

      for (GuiButton ☃x : this.buttonList) {
         ☃.add(((GuiButtonRealmsProxy)☃x).getRealmsButton());
      }

      return ☃;
   }

   public void buttonsRemove(RealmsButton var1) {
      this.buttonList.remove(☃.getProxy());
   }

   @Override
   public void mouseClicked(int var1, int var2, int var3) {
      this.proxy.mouseClicked(☃, ☃, ☃);
      super.mouseClicked(☃, ☃, ☃);
   }

   @Override
   public void handleMouseInput() {
      this.proxy.mouseEvent();
      super.handleMouseInput();
   }

   @Override
   public void handleKeyboardInput() {
      this.proxy.keyboardEvent();
      super.handleKeyboardInput();
   }

   @Override
   public void mouseReleased(int var1, int var2, int var3) {
      this.proxy.mouseReleased(☃, ☃, ☃);
   }

   @Override
   public void mouseClickMove(int var1, int var2, int var3, long var4) {
      this.proxy.mouseDragged(☃, ☃, ☃, ☃);
   }

   @Override
   public void keyTyped(char var1, int var2) {
      this.proxy.keyPressed(☃, ☃);
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      this.proxy.confirmResult(☃, ☃);
   }

   @Override
   public void onGuiClosed() {
      this.proxy.removed();
      super.onGuiClosed();
   }
}
