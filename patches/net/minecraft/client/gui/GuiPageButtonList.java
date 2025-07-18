package net.minecraft.client.gui;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IntHashMap;

public class GuiPageButtonList extends GuiListExtended {
   private final List<GuiPageButtonList.GuiEntry> entries = Lists.newArrayList();
   private final IntHashMap<Gui> componentMap = new IntHashMap<>();
   private final List<GuiTextField> editBoxes = Lists.newArrayList();
   private final GuiPageButtonList.GuiListEntry[][] pages;
   private int page;
   private final GuiPageButtonList.GuiResponder responder;
   private Gui focusedControl;

   public GuiPageButtonList(
      Minecraft var1, int var2, int var3, int var4, int var5, int var6, GuiPageButtonList.GuiResponder var7, GuiPageButtonList.GuiListEntry[]... var8
   ) {
      super(☃, ☃, ☃, ☃, ☃, ☃);
      this.responder = ☃;
      this.pages = ☃;
      this.centerListVertically = false;
      this.populateComponents();
      this.populateEntries();
   }

   private void populateComponents() {
      for (GuiPageButtonList.GuiListEntry[] ☃ : this.pages) {
         for (int ☃x = 0; ☃x < ☃.length; ☃x += 2) {
            GuiPageButtonList.GuiListEntry ☃xx = ☃[☃x];
            GuiPageButtonList.GuiListEntry ☃xxx = ☃x < ☃.length - 1 ? ☃[☃x + 1] : null;
            Gui ☃xxxx = this.createEntry(☃xx, 0, ☃xxx == null);
            Gui ☃xxxxx = this.createEntry(☃xxx, 160, ☃xx == null);
            GuiPageButtonList.GuiEntry ☃xxxxxx = new GuiPageButtonList.GuiEntry(☃xxxx, ☃xxxxx);
            this.entries.add(☃xxxxxx);
            if (☃xx != null && ☃xxxx != null) {
               this.componentMap.addKey(☃xx.getId(), ☃xxxx);
               if (☃xxxx instanceof GuiTextField) {
                  this.editBoxes.add((GuiTextField)☃xxxx);
               }
            }

            if (☃xxx != null && ☃xxxxx != null) {
               this.componentMap.addKey(☃xxx.getId(), ☃xxxxx);
               if (☃xxxxx instanceof GuiTextField) {
                  this.editBoxes.add((GuiTextField)☃xxxxx);
               }
            }
         }
      }
   }

   private void populateEntries() {
      this.entries.clear();

      for (int ☃ = 0; ☃ < this.pages[this.page].length; ☃ += 2) {
         GuiPageButtonList.GuiListEntry ☃x = this.pages[this.page][☃];
         GuiPageButtonList.GuiListEntry ☃xx = ☃ < this.pages[this.page].length - 1 ? this.pages[this.page][☃ + 1] : null;
         Gui ☃xxx = this.componentMap.lookup(☃x.getId());
         Gui ☃xxxx = ☃xx != null ? this.componentMap.lookup(☃xx.getId()) : null;
         GuiPageButtonList.GuiEntry ☃xxxxx = new GuiPageButtonList.GuiEntry(☃xxx, ☃xxxx);
         this.entries.add(☃xxxxx);
      }
   }

   public void setPage(int var1) {
      if (☃ != this.page) {
         int ☃ = this.page;
         this.page = ☃;
         this.populateEntries();
         this.markVisibility(☃, ☃);
         this.amountScrolled = 0.0F;
      }
   }

   public int getPage() {
      return this.page;
   }

   public int getPageCount() {
      return this.pages.length;
   }

   public Gui getFocusedControl() {
      return this.focusedControl;
   }

   public void previousPage() {
      if (this.page > 0) {
         this.setPage(this.page - 1);
      }
   }

   public void nextPage() {
      if (this.page < this.pages.length - 1) {
         this.setPage(this.page + 1);
      }
   }

   public Gui getComponent(int var1) {
      return this.componentMap.lookup(☃);
   }

   private void markVisibility(int var1, int var2) {
      for (GuiPageButtonList.GuiListEntry ☃ : this.pages[☃]) {
         if (☃ != null) {
            this.setComponentVisibility(this.componentMap.lookup(☃.getId()), false);
         }
      }

      for (GuiPageButtonList.GuiListEntry ☃x : this.pages[☃]) {
         if (☃x != null) {
            this.setComponentVisibility(this.componentMap.lookup(☃x.getId()), true);
         }
      }
   }

   private void setComponentVisibility(Gui var1, boolean var2) {
      if (☃ instanceof GuiButton) {
         ((GuiButton)☃).visible = ☃;
      } else if (☃ instanceof GuiTextField) {
         ((GuiTextField)☃).setVisible(☃);
      } else if (☃ instanceof GuiLabel) {
         ((GuiLabel)☃).visible = ☃;
      }
   }

   @Nullable
   private Gui createEntry(@Nullable GuiPageButtonList.GuiListEntry var1, int var2, boolean var3) {
      if (☃ instanceof GuiPageButtonList.GuiSlideEntry) {
         return this.createSlider(this.width / 2 - 155 + ☃, 0, (GuiPageButtonList.GuiSlideEntry)☃);
      } else if (☃ instanceof GuiPageButtonList.GuiButtonEntry) {
         return this.createButton(this.width / 2 - 155 + ☃, 0, (GuiPageButtonList.GuiButtonEntry)☃);
      } else if (☃ instanceof GuiPageButtonList.EditBoxEntry) {
         return this.createTextField(this.width / 2 - 155 + ☃, 0, (GuiPageButtonList.EditBoxEntry)☃);
      } else {
         return ☃ instanceof GuiPageButtonList.GuiLabelEntry ? this.createLabel(this.width / 2 - 155 + ☃, 0, (GuiPageButtonList.GuiLabelEntry)☃, ☃) : null;
      }
   }

   public void setActive(boolean var1) {
      for (GuiPageButtonList.GuiEntry ☃ : this.entries) {
         if (☃.component1 instanceof GuiButton) {
            ((GuiButton)☃.component1).enabled = ☃;
         }

         if (☃.component2 instanceof GuiButton) {
            ((GuiButton)☃.component2).enabled = ☃;
         }
      }
   }

   @Override
   public boolean mouseClicked(int var1, int var2, int var3) {
      boolean ☃ = super.mouseClicked(☃, ☃, ☃);
      int ☃x = this.getSlotIndexFromScreenCoords(☃, ☃);
      if (☃x >= 0) {
         GuiPageButtonList.GuiEntry ☃xx = this.getListEntry(☃x);
         if (this.focusedControl != ☃xx.focusedControl && this.focusedControl != null && this.focusedControl instanceof GuiTextField) {
            ((GuiTextField)this.focusedControl).setFocused(false);
         }

         this.focusedControl = ☃xx.focusedControl;
      }

      return ☃;
   }

   private GuiSlider createSlider(int var1, int var2, GuiPageButtonList.GuiSlideEntry var3) {
      GuiSlider ☃ = new GuiSlider(this.responder, ☃.getId(), ☃, ☃, ☃.getCaption(), ☃.getMinValue(), ☃.getMaxValue(), ☃.getInitalValue(), ☃.getFormatter());
      ☃.visible = ☃.shouldStartVisible();
      return ☃;
   }

   private GuiListButton createButton(int var1, int var2, GuiPageButtonList.GuiButtonEntry var3) {
      GuiListButton ☃ = new GuiListButton(this.responder, ☃.getId(), ☃, ☃, ☃.getCaption(), ☃.getInitialValue());
      ☃.visible = ☃.shouldStartVisible();
      return ☃;
   }

   private GuiTextField createTextField(int var1, int var2, GuiPageButtonList.EditBoxEntry var3) {
      GuiTextField ☃ = new GuiTextField(☃.getId(), this.mc.fontRenderer, ☃, ☃, 150, 20);
      ☃.setText(☃.getCaption());
      ☃.setGuiResponder(this.responder);
      ☃.setVisible(☃.shouldStartVisible());
      ☃.setValidator(☃.getFilter());
      return ☃;
   }

   private GuiLabel createLabel(int var1, int var2, GuiPageButtonList.GuiLabelEntry var3, boolean var4) {
      GuiLabel ☃;
      if (☃) {
         ☃ = new GuiLabel(this.mc.fontRenderer, ☃.getId(), ☃, ☃, this.width - ☃ * 2, 20, -1);
      } else {
         ☃ = new GuiLabel(this.mc.fontRenderer, ☃.getId(), ☃, ☃, 150, 20, -1);
      }

      ☃.visible = ☃.shouldStartVisible();
      ☃.addLine(☃.getCaption());
      ☃.setCentered();
      return ☃;
   }

   public void onKeyPressed(char var1, int var2) {
      if (this.focusedControl instanceof GuiTextField) {
         GuiTextField ☃ = (GuiTextField)this.focusedControl;
         if (!GuiScreen.isKeyComboCtrlV(☃)) {
            if (☃ == 15) {
               ☃.setFocused(false);
               int ☃x = this.editBoxes.indexOf(this.focusedControl);
               if (GuiScreen.isShiftKeyDown()) {
                  if (☃x == 0) {
                     ☃x = this.editBoxes.size() - 1;
                  } else {
                     ☃x--;
                  }
               } else if (☃x == this.editBoxes.size() - 1) {
                  ☃x = 0;
               } else {
                  ☃x++;
               }

               this.focusedControl = this.editBoxes.get(☃x);
               ☃ = (GuiTextField)this.focusedControl;
               ☃.setFocused(true);
               int ☃xx = ☃.y + this.slotHeight;
               int ☃xxx = ☃.y;
               if (☃xx > this.bottom) {
                  this.amountScrolled = this.amountScrolled + (☃xx - this.bottom);
               } else if (☃xxx < this.top) {
                  this.amountScrolled = ☃xxx;
               }
            } else {
               ☃.textboxKeyTyped(☃, ☃);
            }
         } else {
            String ☃xx = GuiScreen.getClipboardString();
            String[] ☃xxx = ☃xx.split(";");
            int ☃xxxx = this.editBoxes.indexOf(this.focusedControl);
            int ☃xxxxx = ☃xxxx;

            for (String ☃xxxxxx : ☃xxx) {
               GuiTextField ☃xxxxxxx = this.editBoxes.get(☃xxxxx);
               ☃xxxxxxx.setText(☃xxxxxx);
               ☃xxxxxxx.setResponderEntryValue(☃xxxxxxx.getId(), ☃xxxxxx);
               if (☃xxxxx == this.editBoxes.size() - 1) {
                  ☃xxxxx = 0;
               } else {
                  ☃xxxxx++;
               }

               if (☃xxxxx == ☃xxxx) {
                  break;
               }
            }
         }
      }
   }

   public GuiPageButtonList.GuiEntry getListEntry(int var1) {
      return this.entries.get(☃);
   }

   @Override
   public int getSize() {
      return this.entries.size();
   }

   @Override
   public int getListWidth() {
      return 400;
   }

   @Override
   protected int getScrollBarX() {
      return super.getScrollBarX() + 32;
   }

   public static class EditBoxEntry extends GuiPageButtonList.GuiListEntry {
      private final Predicate<String> filter;

      public EditBoxEntry(int var1, String var2, boolean var3, Predicate<String> var4) {
         super(☃, ☃, ☃);
         this.filter = (Predicate<String>)MoreObjects.firstNonNull(☃, Predicates.alwaysTrue());
      }

      public Predicate<String> getFilter() {
         return this.filter;
      }
   }

   public static class GuiButtonEntry extends GuiPageButtonList.GuiListEntry {
      private final boolean initialValue;

      public GuiButtonEntry(int var1, String var2, boolean var3, boolean var4) {
         super(☃, ☃, ☃);
         this.initialValue = ☃;
      }

      public boolean getInitialValue() {
         return this.initialValue;
      }
   }

   public static class GuiEntry implements GuiListExtended.IGuiListEntry {
      private final Minecraft client = Minecraft.getMinecraft();
      private final Gui component1;
      private final Gui component2;
      private Gui focusedControl;

      public GuiEntry(@Nullable Gui var1, @Nullable Gui var2) {
         this.component1 = ☃;
         this.component2 = ☃;
      }

      public Gui getComponent1() {
         return this.component1;
      }

      public Gui getComponent2() {
         return this.component2;
      }

      @Override
      public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
         this.renderComponent(this.component1, ☃, ☃, ☃, false, ☃);
         this.renderComponent(this.component2, ☃, ☃, ☃, false, ☃);
      }

      private void renderComponent(Gui var1, int var2, int var3, int var4, boolean var5, float var6) {
         if (☃ != null) {
            if (☃ instanceof GuiButton) {
               this.renderButton((GuiButton)☃, ☃, ☃, ☃, ☃, ☃);
            } else if (☃ instanceof GuiTextField) {
               this.renderTextField((GuiTextField)☃, ☃, ☃);
            } else if (☃ instanceof GuiLabel) {
               this.renderLabel((GuiLabel)☃, ☃, ☃, ☃, ☃);
            }
         }
      }

      private void renderButton(GuiButton var1, int var2, int var3, int var4, boolean var5, float var6) {
         ☃.y = ☃;
         if (!☃) {
            ☃.drawButton(this.client, ☃, ☃, ☃);
         }
      }

      private void renderTextField(GuiTextField var1, int var2, boolean var3) {
         ☃.y = ☃;
         if (!☃) {
            ☃.drawTextBox();
         }
      }

      private void renderLabel(GuiLabel var1, int var2, int var3, int var4, boolean var5) {
         ☃.y = ☃;
         if (!☃) {
            ☃.drawLabel(this.client, ☃, ☃);
         }
      }

      @Override
      public void updatePosition(int var1, int var2, int var3, float var4) {
         this.renderComponent(this.component1, ☃, 0, 0, true, ☃);
         this.renderComponent(this.component2, ☃, 0, 0, true, ☃);
      }

      @Override
      public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
         boolean ☃ = this.clickComponent(this.component1, ☃, ☃, ☃);
         boolean ☃x = this.clickComponent(this.component2, ☃, ☃, ☃);
         return ☃ || ☃x;
      }

      private boolean clickComponent(Gui var1, int var2, int var3, int var4) {
         if (☃ == null) {
            return false;
         } else if (☃ instanceof GuiButton) {
            return this.clickButton((GuiButton)☃, ☃, ☃, ☃);
         } else {
            if (☃ instanceof GuiTextField) {
               this.clickTextField((GuiTextField)☃, ☃, ☃, ☃);
            }

            return false;
         }
      }

      private boolean clickButton(GuiButton var1, int var2, int var3, int var4) {
         boolean ☃ = ☃.mousePressed(this.client, ☃, ☃);
         if (☃) {
            this.focusedControl = ☃;
         }

         return ☃;
      }

      private void clickTextField(GuiTextField var1, int var2, int var3, int var4) {
         ☃.mouseClicked(☃, ☃, ☃);
         if (☃.isFocused()) {
            this.focusedControl = ☃;
         }
      }

      @Override
      public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
         this.releaseComponent(this.component1, ☃, ☃, ☃);
         this.releaseComponent(this.component2, ☃, ☃, ☃);
      }

      private void releaseComponent(Gui var1, int var2, int var3, int var4) {
         if (☃ != null) {
            if (☃ instanceof GuiButton) {
               this.releaseButton((GuiButton)☃, ☃, ☃, ☃);
            }
         }
      }

      private void releaseButton(GuiButton var1, int var2, int var3, int var4) {
         ☃.mouseReleased(☃, ☃);
      }
   }

   public static class GuiLabelEntry extends GuiPageButtonList.GuiListEntry {
      public GuiLabelEntry(int var1, String var2, boolean var3) {
         super(☃, ☃, ☃);
      }
   }

   public static class GuiListEntry {
      private final int id;
      private final String caption;
      private final boolean startVisible;

      public GuiListEntry(int var1, String var2, boolean var3) {
         this.id = ☃;
         this.caption = ☃;
         this.startVisible = ☃;
      }

      public int getId() {
         return this.id;
      }

      public String getCaption() {
         return this.caption;
      }

      public boolean shouldStartVisible() {
         return this.startVisible;
      }
   }

   public interface GuiResponder {
      void setEntryValue(int var1, boolean var2);

      void setEntryValue(int var1, float var2);

      void setEntryValue(int var1, String var2);
   }

   public static class GuiSlideEntry extends GuiPageButtonList.GuiListEntry {
      private final GuiSlider.FormatHelper formatter;
      private final float minValue;
      private final float maxValue;
      private final float initialValue;

      public GuiSlideEntry(int var1, String var2, boolean var3, GuiSlider.FormatHelper var4, float var5, float var6, float var7) {
         super(☃, ☃, ☃);
         this.formatter = ☃;
         this.minValue = ☃;
         this.maxValue = ☃;
         this.initialValue = ☃;
      }

      public GuiSlider.FormatHelper getFormatter() {
         return this.formatter;
      }

      public float getMinValue() {
         return this.minValue;
      }

      public float getMaxValue() {
         return this.maxValue;
      }

      public float getInitalValue() {
         return this.initialValue;
      }
   }
}
