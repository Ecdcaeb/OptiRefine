package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonRealmsProxy;
import net.minecraft.util.ResourceLocation;

public class RealmsButton {
   protected static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
   private final GuiButtonRealmsProxy proxy;

   public RealmsButton(int var1, int var2, int var3, String var4) {
      this.proxy = new GuiButtonRealmsProxy(this, ☃, ☃, ☃, ☃);
   }

   public RealmsButton(int var1, int var2, int var3, int var4, int var5, String var6) {
      this.proxy = new GuiButtonRealmsProxy(this, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public GuiButton getProxy() {
      return this.proxy;
   }

   public int id() {
      return this.proxy.getId();
   }

   public boolean active() {
      return this.proxy.getEnabled();
   }

   public void active(boolean var1) {
      this.proxy.setEnabled(☃);
   }

   public void msg(String var1) {
      this.proxy.setText(☃);
   }

   public int getWidth() {
      return this.proxy.getButtonWidth();
   }

   public int getHeight() {
      return this.proxy.getHeight();
   }

   public int y() {
      return this.proxy.getPositionY();
   }

   public void render(int var1, int var2, float var3) {
      this.proxy.drawButton(Minecraft.getMinecraft(), ☃, ☃, ☃);
   }

   public void clicked(int var1, int var2) {
   }

   public void released(int var1, int var2) {
   }

   public void blit(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.proxy.drawTexturedModalRect(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public void renderBg(int var1, int var2) {
   }

   public int getYImage(boolean var1) {
      return this.proxy.getYImage(☃);
   }
}
