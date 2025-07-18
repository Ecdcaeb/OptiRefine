package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class RealmsEditBox {
   private final GuiTextField editBox;

   public RealmsEditBox(int var1, int var2, int var3, int var4, int var5) {
      this.editBox = new GuiTextField(☃, Minecraft.getMinecraft().fontRenderer, ☃, ☃, ☃, ☃);
   }

   public String getValue() {
      return this.editBox.getText();
   }

   public void tick() {
      this.editBox.updateCursorCounter();
   }

   public void setFocus(boolean var1) {
      this.editBox.setFocused(☃);
   }

   public void setValue(String var1) {
      this.editBox.setText(☃);
   }

   public void keyPressed(char var1, int var2) {
      this.editBox.textboxKeyTyped(☃, ☃);
   }

   public boolean isFocused() {
      return this.editBox.isFocused();
   }

   public void mouseClicked(int var1, int var2, int var3) {
      this.editBox.mouseClicked(☃, ☃, ☃);
   }

   public void render() {
      this.editBox.drawTextBox();
   }

   public void setMaxLength(int var1) {
      this.editBox.setMaxStringLength(☃);
   }

   public void setIsEditable(boolean var1) {
      this.editBox.setEnabled(☃);
   }
}
