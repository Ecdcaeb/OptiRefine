package net.minecraft.client.gui.chat;

import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class NarratorChatListener implements IChatListener {
   public static final NarratorChatListener INSTANCE = new NarratorChatListener();
   private final Narrator narrator = Narrator.getNarrator();

   @Override
   public void say(ChatType var1, ITextComponent var2) {
      int ☃ = Minecraft.getMinecraft().gameSettings.narrator;
      if (☃ != 0 && this.narrator.active()) {
         if (☃ == 1 || ☃ == 2 && ☃ == ChatType.CHAT || ☃ == 3 && ☃ == ChatType.SYSTEM) {
            if (☃ instanceof TextComponentTranslation && "chat.type.text".equals(((TextComponentTranslation)☃).getKey())) {
               this.narrator.say(new TextComponentTranslation("chat.type.text.narrate", ((TextComponentTranslation)☃).getFormatArgs()).getUnformattedText());
            } else {
               this.narrator.say(☃.getUnformattedText());
            }
         }
      }
   }

   public void announceMode(int var1) {
      this.narrator.clear();
      this.narrator
         .say(
            new TextComponentTranslation("options.narrator").getUnformattedText()
               + " : "
               + new TextComponentTranslation(GameSettings.NARRATOR_MODES[☃]).getUnformattedText()
         );
      GuiToast ☃ = Minecraft.getMinecraft().getToastGui();
      if (this.narrator.active()) {
         if (☃ == 0) {
            SystemToast.addOrUpdate(☃, SystemToast.Type.NARRATOR_TOGGLE, new TextComponentTranslation("narrator.toast.disabled"), null);
         } else {
            SystemToast.addOrUpdate(
               ☃,
               SystemToast.Type.NARRATOR_TOGGLE,
               new TextComponentTranslation("narrator.toast.enabled"),
               new TextComponentTranslation(GameSettings.NARRATOR_MODES[☃])
            );
         }
      } else {
         SystemToast.addOrUpdate(
            ☃,
            SystemToast.Type.NARRATOR_TOGGLE,
            new TextComponentTranslation("narrator.toast.disabled"),
            new TextComponentTranslation("options.narrator.notavailable")
         );
      }
   }

   public boolean isActive() {
      return this.narrator.active();
   }

   public void clear() {
      this.narrator.clear();
   }
}
