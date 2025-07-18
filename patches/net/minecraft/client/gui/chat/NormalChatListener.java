package net.minecraft.client.gui.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

public class NormalChatListener implements IChatListener {
   private final Minecraft mc;

   public NormalChatListener(Minecraft var1) {
      this.mc = ☃;
   }

   @Override
   public void say(ChatType var1, ITextComponent var2) {
      this.mc.ingameGUI.getChatGUI().printChatMessage(☃);
   }
}
