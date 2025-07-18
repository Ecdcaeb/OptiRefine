package net.minecraft.client.gui;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.ArrayUtils;

public class GuiKeyBindingList extends GuiListExtended {
   private final GuiControls controlsScreen;
   private final Minecraft mc;
   private final GuiListExtended.IGuiListEntry[] listEntries;
   private int maxListLabelWidth;

   public GuiKeyBindingList(GuiControls var1, Minecraft var2) {
      super(☃, ☃.width + 45, ☃.height, 63, ☃.height - 32, 20);
      this.controlsScreen = ☃;
      this.mc = ☃;
      KeyBinding[] ☃ = (KeyBinding[])ArrayUtils.clone(☃.gameSettings.keyBindings);
      this.listEntries = new GuiListExtended.IGuiListEntry[☃.length + KeyBinding.getKeybinds().size()];
      Arrays.sort((Object[])☃);
      int ☃x = 0;
      String ☃xx = null;

      for (KeyBinding ☃xxx : ☃) {
         String ☃xxxx = ☃xxx.getKeyCategory();
         if (!☃xxxx.equals(☃xx)) {
            ☃xx = ☃xxxx;
            this.listEntries[☃x++] = new GuiKeyBindingList.CategoryEntry(☃xxxx);
         }

         int ☃xxxxx = ☃.fontRenderer.getStringWidth(I18n.format(☃xxx.getKeyDescription()));
         if (☃xxxxx > this.maxListLabelWidth) {
            this.maxListLabelWidth = ☃xxxxx;
         }

         this.listEntries[☃x++] = new GuiKeyBindingList.KeyEntry(☃xxx);
      }
   }

   @Override
   protected int getSize() {
      return this.listEntries.length;
   }

   @Override
   public GuiListExtended.IGuiListEntry getListEntry(int var1) {
      return this.listEntries[☃];
   }

   @Override
   protected int getScrollBarX() {
      return super.getScrollBarX() + 15;
   }

   @Override
   public int getListWidth() {
      return super.getListWidth() + 32;
   }

   public class CategoryEntry implements GuiListExtended.IGuiListEntry {
      private final String labelText;
      private final int labelWidth;

      public CategoryEntry(String var2) {
         this.labelText = I18n.format(☃);
         this.labelWidth = GuiKeyBindingList.this.mc.fontRenderer.getStringWidth(this.labelText);
      }

      @Override
      public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
         GuiKeyBindingList.this.mc
            .fontRenderer
            .drawString(
               this.labelText,
               GuiKeyBindingList.this.mc.currentScreen.width / 2 - this.labelWidth / 2,
               ☃ + ☃ - GuiKeyBindingList.this.mc.fontRenderer.FONT_HEIGHT - 1,
               16777215
            );
      }

      @Override
      public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
         return false;
      }

      @Override
      public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
      }

      @Override
      public void updatePosition(int var1, int var2, int var3, float var4) {
      }
   }

   public class KeyEntry implements GuiListExtended.IGuiListEntry {
      private final KeyBinding keybinding;
      private final String keyDesc;
      private final GuiButton btnChangeKeyBinding;
      private final GuiButton btnReset;

      private KeyEntry(KeyBinding var2) {
         this.keybinding = ☃;
         this.keyDesc = I18n.format(☃.getKeyDescription());
         this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.format(☃.getKeyDescription()));
         this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset"));
      }

      @Override
      public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
         boolean ☃ = GuiKeyBindingList.this.controlsScreen.buttonId == this.keybinding;
         GuiKeyBindingList.this.mc
            .fontRenderer
            .drawString(
               this.keyDesc, ☃ + 90 - GuiKeyBindingList.this.maxListLabelWidth, ☃ + ☃ / 2 - GuiKeyBindingList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215
            );
         this.btnReset.x = ☃ + 190;
         this.btnReset.y = ☃;
         this.btnReset.enabled = this.keybinding.getKeyCode() != this.keybinding.getKeyCodeDefault();
         this.btnReset.drawButton(GuiKeyBindingList.this.mc, ☃, ☃, ☃);
         this.btnChangeKeyBinding.x = ☃ + 105;
         this.btnChangeKeyBinding.y = ☃;
         this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.getKeyCode());
         boolean ☃x = false;
         if (this.keybinding.getKeyCode() != 0) {
            for (KeyBinding ☃xx : GuiKeyBindingList.this.mc.gameSettings.keyBindings) {
               if (☃xx != this.keybinding && ☃xx.getKeyCode() == this.keybinding.getKeyCode()) {
                  ☃x = true;
                  break;
               }
            }
         }

         if (☃) {
            this.btnChangeKeyBinding.displayString = TextFormatting.WHITE
               + "> "
               + TextFormatting.YELLOW
               + this.btnChangeKeyBinding.displayString
               + TextFormatting.WHITE
               + " <";
         } else if (☃x) {
            this.btnChangeKeyBinding.displayString = TextFormatting.RED + this.btnChangeKeyBinding.displayString;
         }

         this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, ☃, ☃, ☃);
      }

      @Override
      public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
         if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, ☃, ☃)) {
            GuiKeyBindingList.this.controlsScreen.buttonId = this.keybinding;
            return true;
         } else if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, ☃, ☃)) {
            GuiKeyBindingList.this.mc.gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
            KeyBinding.resetKeyBindingArrayAndHash();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
         this.btnChangeKeyBinding.mouseReleased(☃, ☃);
         this.btnReset.mouseReleased(☃, ☃);
      }

      @Override
      public void updatePosition(int var1, int var2, int var3, float var4) {
      }
   }
}
