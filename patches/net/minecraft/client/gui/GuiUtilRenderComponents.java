package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class GuiUtilRenderComponents {
   public static String removeTextColorsIfConfigured(String var0, boolean var1) {
      return !☃ && !Minecraft.getMinecraft().gameSettings.chatColours ? TextFormatting.getTextWithoutFormattingCodes(☃) : ☃;
   }

   public static List<ITextComponent> splitText(ITextComponent var0, int var1, FontRenderer var2, boolean var3, boolean var4) {
      int ☃ = 0;
      ITextComponent ☃x = new TextComponentString("");
      List<ITextComponent> ☃xx = Lists.newArrayList();
      List<ITextComponent> ☃xxx = Lists.newArrayList(☃);

      for (int ☃xxxx = 0; ☃xxxx < ☃xxx.size(); ☃xxxx++) {
         ITextComponent ☃xxxxx = ☃xxx.get(☃xxxx);
         String ☃xxxxxx = ☃xxxxx.getUnformattedComponentText();
         boolean ☃xxxxxxx = false;
         if (☃xxxxxx.contains("\n")) {
            int ☃xxxxxxxx = ☃xxxxxx.indexOf(10);
            String ☃xxxxxxxxx = ☃xxxxxx.substring(☃xxxxxxxx + 1);
            ☃xxxxxx = ☃xxxxxx.substring(0, ☃xxxxxxxx + 1);
            ITextComponent ☃xxxxxxxxxx = new TextComponentString(☃xxxxxxxxx);
            ☃xxxxxxxxxx.setStyle(☃xxxxx.getStyle().createShallowCopy());
            ☃xxx.add(☃xxxx + 1, ☃xxxxxxxxxx);
            ☃xxxxxxx = true;
         }

         String ☃xxxxxxxx = removeTextColorsIfConfigured(☃xxxxx.getStyle().getFormattingCode() + ☃xxxxxx, ☃);
         String ☃xxxxxxxxx = ☃xxxxxxxx.endsWith("\n") ? ☃xxxxxxxx.substring(0, ☃xxxxxxxx.length() - 1) : ☃xxxxxxxx;
         int ☃xxxxxxxxxx = ☃.getStringWidth(☃xxxxxxxxx);
         TextComponentString ☃xxxxxxxxxxx = new TextComponentString(☃xxxxxxxxx);
         ☃xxxxxxxxxxx.setStyle(☃xxxxx.getStyle().createShallowCopy());
         if (☃ + ☃xxxxxxxxxx > ☃) {
            String ☃xxxxxxxxxxxx = ☃.trimStringToWidth(☃xxxxxxxx, ☃ - ☃, false);
            String ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx.length() < ☃xxxxxxxx.length() ? ☃xxxxxxxx.substring(☃xxxxxxxxxxxx.length()) : null;
            if (☃xxxxxxxxxxxxx != null && !☃xxxxxxxxxxxxx.isEmpty()) {
               int ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxx.lastIndexOf(32);
               if (☃xxxxxxxxxxxxxx >= 0 && ☃.getStringWidth(☃xxxxxxxx.substring(0, ☃xxxxxxxxxxxxxx)) > 0) {
                  ☃xxxxxxxxxxxx = ☃xxxxxxxx.substring(0, ☃xxxxxxxxxxxxxx);
                  if (☃) {
                     ☃xxxxxxxxxxxxxx++;
                  }

                  ☃xxxxxxxxxxxxx = ☃xxxxxxxx.substring(☃xxxxxxxxxxxxxx);
               } else if (☃ > 0 && !☃xxxxxxxx.contains(" ")) {
                  ☃xxxxxxxxxxxx = "";
                  ☃xxxxxxxxxxxxx = ☃xxxxxxxx;
               }

               TextComponentString ☃xxxxxxxxxxxxxxx = new TextComponentString(☃xxxxxxxxxxxxx);
               ☃xxxxxxxxxxxxxxx.setStyle(☃xxxxx.getStyle().createShallowCopy());
               ☃xxx.add(☃xxxx + 1, ☃xxxxxxxxxxxxxxx);
            }

            ☃xxxxxxxxxx = ☃.getStringWidth(☃xxxxxxxxxxxx);
            ☃xxxxxxxxxxx = new TextComponentString(☃xxxxxxxxxxxx);
            ☃xxxxxxxxxxx.setStyle(☃xxxxx.getStyle().createShallowCopy());
            ☃xxxxxxx = true;
         }

         if (☃ + ☃xxxxxxxxxx <= ☃) {
            ☃ += ☃xxxxxxxxxx;
            ☃x.appendSibling(☃xxxxxxxxxxx);
         } else {
            ☃xxxxxxx = true;
         }

         if (☃xxxxxxx) {
            ☃xx.add(☃x);
            ☃ = 0;
            ☃x = new TextComponentString("");
         }
      }

      ☃xx.add(☃x);
      return ☃xx;
   }
}
