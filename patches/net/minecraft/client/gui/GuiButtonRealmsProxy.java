package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsButton;

public class GuiButtonRealmsProxy extends GuiButton {
   private final RealmsButton realmsButton;

   public GuiButtonRealmsProxy(RealmsButton var1, int var2, int var3, int var4, String var5) {
      super(☃, ☃, ☃, ☃);
      this.realmsButton = ☃;
   }

   public GuiButtonRealmsProxy(RealmsButton var1, int var2, int var3, int var4, String var5, int var6, int var7) {
      super(☃, ☃, ☃, ☃, ☃, ☃);
      this.realmsButton = ☃;
   }

   public int getId() {
      return this.id;
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = ☃;
   }

   public void setText(String var1) {
      super.displayString = ☃;
   }

   @Override
   public int getButtonWidth() {
      return super.getButtonWidth();
   }

   public int getPositionY() {
      return this.y;
   }

   @Override
   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      if (super.mousePressed(☃, ☃, ☃)) {
         this.realmsButton.clicked(☃, ☃);
      }

      return super.mousePressed(☃, ☃, ☃);
   }

   @Override
   public void mouseReleased(int var1, int var2) {
      this.realmsButton.released(☃, ☃);
   }

   @Override
   public void mouseDragged(Minecraft var1, int var2, int var3) {
      this.realmsButton.renderBg(☃, ☃);
   }

   public RealmsButton getRealmsButton() {
      return this.realmsButton;
   }

   @Override
   public int getHoverState(boolean var1) {
      return this.realmsButton.getYImage(☃);
   }

   public int getYImage(boolean var1) {
      return super.getHoverState(☃);
   }

   public int getHeight() {
      return this.height;
   }
}
