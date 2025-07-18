package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionsRowList extends GuiListExtended {
   private final List<GuiOptionsRowList.Row> options = Lists.newArrayList();

   public GuiOptionsRowList(Minecraft var1, int var2, int var3, int var4, int var5, int var6, GameSettings.Options... var7) {
      super(☃, ☃, ☃, ☃, ☃, ☃);
      this.centerListVertically = false;

      for (int ☃ = 0; ☃ < ☃.length; ☃ += 2) {
         GameSettings.Options ☃x = ☃[☃];
         GameSettings.Options ☃xx = ☃ < ☃.length - 1 ? ☃[☃ + 1] : null;
         GuiButton ☃xxx = this.createButton(☃, ☃ / 2 - 155, 0, ☃x);
         GuiButton ☃xxxx = this.createButton(☃, ☃ / 2 - 155 + 160, 0, ☃xx);
         this.options.add(new GuiOptionsRowList.Row(☃xxx, ☃xxxx));
      }
   }

   private GuiButton createButton(Minecraft var1, int var2, int var3, GameSettings.Options var4) {
      if (☃ == null) {
         return null;
      } else {
         int ☃ = ☃.getOrdinal();
         return (GuiButton)(☃.isFloat() ? new GuiOptionSlider(☃, ☃, ☃, ☃) : new GuiOptionButton(☃, ☃, ☃, ☃, ☃.gameSettings.getKeyBinding(☃)));
      }
   }

   public GuiOptionsRowList.Row getListEntry(int var1) {
      return this.options.get(☃);
   }

   @Override
   protected int getSize() {
      return this.options.size();
   }

   @Override
   public int getListWidth() {
      return 400;
   }

   @Override
   protected int getScrollBarX() {
      return super.getScrollBarX() + 32;
   }

   public static class Row implements GuiListExtended.IGuiListEntry {
      private final Minecraft client = Minecraft.getMinecraft();
      private final GuiButton buttonA;
      private final GuiButton buttonB;

      public Row(GuiButton var1, GuiButton var2) {
         this.buttonA = ☃;
         this.buttonB = ☃;
      }

      @Override
      public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
         if (this.buttonA != null) {
            this.buttonA.y = ☃;
            this.buttonA.drawButton(this.client, ☃, ☃, ☃);
         }

         if (this.buttonB != null) {
            this.buttonB.y = ☃;
            this.buttonB.drawButton(this.client, ☃, ☃, ☃);
         }
      }

      @Override
      public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
         if (this.buttonA.mousePressed(this.client, ☃, ☃)) {
            if (this.buttonA instanceof GuiOptionButton) {
               this.client.gameSettings.setOptionValue(((GuiOptionButton)this.buttonA).getOption(), 1);
               this.buttonA.displayString = this.client.gameSettings.getKeyBinding(GameSettings.Options.byOrdinal(this.buttonA.id));
            }

            return true;
         } else if (this.buttonB != null && this.buttonB.mousePressed(this.client, ☃, ☃)) {
            if (this.buttonB instanceof GuiOptionButton) {
               this.client.gameSettings.setOptionValue(((GuiOptionButton)this.buttonB).getOption(), 1);
               this.buttonB.displayString = this.client.gameSettings.getKeyBinding(GameSettings.Options.byOrdinal(this.buttonB.id));
            }

            return true;
         } else {
            return false;
         }
      }

      @Override
      public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
         if (this.buttonA != null) {
            this.buttonA.mouseReleased(☃, ☃);
         }

         if (this.buttonB != null) {
            this.buttonB.mouseReleased(☃, ☃);
         }
      }

      @Override
      public void updatePosition(int var1, int var2, int var3, float var4) {
      }
   }
}
