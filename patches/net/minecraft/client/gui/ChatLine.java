package net.minecraft.client.gui;

import net.minecraft.util.text.ITextComponent;

public class ChatLine {
   private final int updateCounterCreated;
   private final ITextComponent lineString;
   private final int chatLineID;

   public ChatLine(int var1, ITextComponent var2, int var3) {
      this.lineString = ☃;
      this.updateCounterCreated = ☃;
      this.chatLineID = ☃;
   }

   public ITextComponent getChatComponent() {
      return this.lineString;
   }

   public int getUpdatedCounter() {
      return this.updateCounterCreated;
   }

   public int getChatLineID() {
      return this.chatLineID;
   }
}
