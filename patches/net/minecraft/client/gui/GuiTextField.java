package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;

public class GuiTextField extends Gui {
   private final int id;
   private final FontRenderer fontRenderer;
   public int x;
   public int y;
   private final int width;
   private final int height;
   private String text = "";
   private int maxStringLength = 32;
   private int cursorCounter;
   private boolean enableBackgroundDrawing = true;
   private boolean canLoseFocus = true;
   private boolean isFocused;
   private boolean isEnabled = true;
   private int lineScrollOffset;
   private int cursorPosition;
   private int selectionEnd;
   private int enabledColor = 14737632;
   private int disabledColor = 7368816;
   private boolean visible = true;
   private GuiPageButtonList.GuiResponder guiResponder;
   private Predicate<String> validator = Predicates.alwaysTrue();

   public GuiTextField(int var1, FontRenderer var2, int var3, int var4, int var5, int var6) {
      this.id = ☃;
      this.fontRenderer = ☃;
      this.x = ☃;
      this.y = ☃;
      this.width = ☃;
      this.height = ☃;
   }

   public void setGuiResponder(GuiPageButtonList.GuiResponder var1) {
      this.guiResponder = ☃;
   }

   public void updateCursorCounter() {
      this.cursorCounter++;
   }

   public void setText(String var1) {
      if (this.validator.apply(☃)) {
         if (☃.length() > this.maxStringLength) {
            this.text = ☃.substring(0, this.maxStringLength);
         } else {
            this.text = ☃;
         }

         this.setCursorPositionEnd();
      }
   }

   public String getText() {
      return this.text;
   }

   public String getSelectedText() {
      int ☃ = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
      int ☃x = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
      return this.text.substring(☃, ☃x);
   }

   public void setValidator(Predicate<String> var1) {
      this.validator = ☃;
   }

   public void writeText(String var1) {
      String ☃ = "";
      String ☃x = ChatAllowedCharacters.filterAllowedCharacters(☃);
      int ☃xx = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
      int ☃xxx = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
      int ☃xxxx = this.maxStringLength - this.text.length() - (☃xx - ☃xxx);
      if (!this.text.isEmpty()) {
         ☃ = ☃ + this.text.substring(0, ☃xx);
      }

      int ☃xxxxx;
      if (☃xxxx < ☃x.length()) {
         ☃ = ☃ + ☃x.substring(0, ☃xxxx);
         ☃xxxxx = ☃xxxx;
      } else {
         ☃ = ☃ + ☃x;
         ☃xxxxx = ☃x.length();
      }

      if (!this.text.isEmpty() && ☃xxx < this.text.length()) {
         ☃ = ☃ + this.text.substring(☃xxx);
      }

      if (this.validator.apply(☃)) {
         this.text = ☃;
         this.moveCursorBy(☃xx - this.selectionEnd + ☃xxxxx);
         this.setResponderEntryValue(this.id, this.text);
      }
   }

   public void setResponderEntryValue(int var1, String var2) {
      if (this.guiResponder != null) {
         this.guiResponder.setEntryValue(☃, ☃);
      }
   }

   public void deleteWords(int var1) {
      if (!this.text.isEmpty()) {
         if (this.selectionEnd != this.cursorPosition) {
            this.writeText("");
         } else {
            this.deleteFromCursor(this.getNthWordFromCursor(☃) - this.cursorPosition);
         }
      }
   }

   public void deleteFromCursor(int var1) {
      if (!this.text.isEmpty()) {
         if (this.selectionEnd != this.cursorPosition) {
            this.writeText("");
         } else {
            boolean ☃ = ☃ < 0;
            int ☃x = ☃ ? this.cursorPosition + ☃ : this.cursorPosition;
            int ☃xx = ☃ ? this.cursorPosition : this.cursorPosition + ☃;
            String ☃xxx = "";
            if (☃x >= 0) {
               ☃xxx = this.text.substring(0, ☃x);
            }

            if (☃xx < this.text.length()) {
               ☃xxx = ☃xxx + this.text.substring(☃xx);
            }

            if (this.validator.apply(☃xxx)) {
               this.text = ☃xxx;
               if (☃) {
                  this.moveCursorBy(☃);
               }

               this.setResponderEntryValue(this.id, this.text);
            }
         }
      }
   }

   public int getId() {
      return this.id;
   }

   public int getNthWordFromCursor(int var1) {
      return this.getNthWordFromPos(☃, this.getCursorPosition());
   }

   public int getNthWordFromPos(int var1, int var2) {
      return this.getNthWordFromPosWS(☃, ☃, true);
   }

   public int getNthWordFromPosWS(int var1, int var2, boolean var3) {
      int ☃ = ☃;
      boolean ☃x = ☃ < 0;
      int ☃xx = Math.abs(☃);

      for (int ☃xxx = 0; ☃xxx < ☃xx; ☃xxx++) {
         if (!☃x) {
            int ☃xxxx = this.text.length();
            ☃ = this.text.indexOf(32, ☃);
            if (☃ == -1) {
               ☃ = ☃xxxx;
            } else {
               while (☃ && ☃ < ☃xxxx && this.text.charAt(☃) == ' ') {
                  ☃++;
               }
            }
         } else {
            while (☃ && ☃ > 0 && this.text.charAt(☃ - 1) == ' ') {
               ☃--;
            }

            while (☃ > 0 && this.text.charAt(☃ - 1) != ' ') {
               ☃--;
            }
         }
      }

      return ☃;
   }

   public void moveCursorBy(int var1) {
      this.setCursorPosition(this.selectionEnd + ☃);
   }

   public void setCursorPosition(int var1) {
      this.cursorPosition = ☃;
      int ☃ = this.text.length();
      this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, ☃);
      this.setSelectionPos(this.cursorPosition);
   }

   public void setCursorPositionZero() {
      this.setCursorPosition(0);
   }

   public void setCursorPositionEnd() {
      this.setCursorPosition(this.text.length());
   }

   public boolean textboxKeyTyped(char var1, int var2) {
      if (!this.isFocused) {
         return false;
      } else if (GuiScreen.isKeyComboCtrlA(☃)) {
         this.setCursorPositionEnd();
         this.setSelectionPos(0);
         return true;
      } else if (GuiScreen.isKeyComboCtrlC(☃)) {
         GuiScreen.setClipboardString(this.getSelectedText());
         return true;
      } else if (GuiScreen.isKeyComboCtrlV(☃)) {
         if (this.isEnabled) {
            this.writeText(GuiScreen.getClipboardString());
         }

         return true;
      } else if (GuiScreen.isKeyComboCtrlX(☃)) {
         GuiScreen.setClipboardString(this.getSelectedText());
         if (this.isEnabled) {
            this.writeText("");
         }

         return true;
      } else {
         switch (☃) {
            case 14:
               if (GuiScreen.isCtrlKeyDown()) {
                  if (this.isEnabled) {
                     this.deleteWords(-1);
                  }
               } else if (this.isEnabled) {
                  this.deleteFromCursor(-1);
               }

               return true;
            case 199:
               if (GuiScreen.isShiftKeyDown()) {
                  this.setSelectionPos(0);
               } else {
                  this.setCursorPositionZero();
               }

               return true;
            case 203:
               if (GuiScreen.isShiftKeyDown()) {
                  if (GuiScreen.isCtrlKeyDown()) {
                     this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                  } else {
                     this.setSelectionPos(this.getSelectionEnd() - 1);
                  }
               } else if (GuiScreen.isCtrlKeyDown()) {
                  this.setCursorPosition(this.getNthWordFromCursor(-1));
               } else {
                  this.moveCursorBy(-1);
               }

               return true;
            case 205:
               if (GuiScreen.isShiftKeyDown()) {
                  if (GuiScreen.isCtrlKeyDown()) {
                     this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                  } else {
                     this.setSelectionPos(this.getSelectionEnd() + 1);
                  }
               } else if (GuiScreen.isCtrlKeyDown()) {
                  this.setCursorPosition(this.getNthWordFromCursor(1));
               } else {
                  this.moveCursorBy(1);
               }

               return true;
            case 207:
               if (GuiScreen.isShiftKeyDown()) {
                  this.setSelectionPos(this.text.length());
               } else {
                  this.setCursorPositionEnd();
               }

               return true;
            case 211:
               if (GuiScreen.isCtrlKeyDown()) {
                  if (this.isEnabled) {
                     this.deleteWords(1);
                  }
               } else if (this.isEnabled) {
                  this.deleteFromCursor(1);
               }

               return true;
            default:
               if (ChatAllowedCharacters.isAllowedCharacter(☃)) {
                  if (this.isEnabled) {
                     this.writeText(Character.toString(☃));
                  }

                  return true;
               } else {
                  return false;
               }
         }
      }
   }

   public boolean mouseClicked(int var1, int var2, int var3) {
      boolean ☃ = ☃ >= this.x && ☃ < this.x + this.width && ☃ >= this.y && ☃ < this.y + this.height;
      if (this.canLoseFocus) {
         this.setFocused(☃);
      }

      if (this.isFocused && ☃ && ☃ == 0) {
         int ☃x = ☃ - this.x;
         if (this.enableBackgroundDrawing) {
            ☃x -= 4;
         }

         String ☃xx = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
         this.setCursorPosition(this.fontRenderer.trimStringToWidth(☃xx, ☃x).length() + this.lineScrollOffset);
         return true;
      } else {
         return false;
      }
   }

   public void drawTextBox() {
      if (this.getVisible()) {
         if (this.getEnableBackgroundDrawing()) {
            drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
            drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
         }

         int ☃ = this.isEnabled ? this.enabledColor : this.disabledColor;
         int ☃x = this.cursorPosition - this.lineScrollOffset;
         int ☃xx = this.selectionEnd - this.lineScrollOffset;
         String ☃xxx = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
         boolean ☃xxxx = ☃x >= 0 && ☃x <= ☃xxx.length();
         boolean ☃xxxxx = this.isFocused && this.cursorCounter / 6 % 2 == 0 && ☃xxxx;
         int ☃xxxxxx = this.enableBackgroundDrawing ? this.x + 4 : this.x;
         int ☃xxxxxxx = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
         int ☃xxxxxxxx = ☃xxxxxx;
         if (☃xx > ☃xxx.length()) {
            ☃xx = ☃xxx.length();
         }

         if (!☃xxx.isEmpty()) {
            String ☃xxxxxxxxx = ☃xxxx ? ☃xxx.substring(0, ☃x) : ☃xxx;
            ☃xxxxxxxx = this.fontRenderer.drawStringWithShadow(☃xxxxxxxxx, ☃xxxxxx, ☃xxxxxxx, ☃);
         }

         boolean ☃xxxxxxxxx = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
         int ☃xxxxxxxxxx = ☃xxxxxxxx;
         if (!☃xxxx) {
            ☃xxxxxxxxxx = ☃x > 0 ? ☃xxxxxx + this.width : ☃xxxxxx;
         } else if (☃xxxxxxxxx) {
            ☃xxxxxxxxxx = ☃xxxxxxxx - 1;
            ☃xxxxxxxx--;
         }

         if (!☃xxx.isEmpty() && ☃xxxx && ☃x < ☃xxx.length()) {
            ☃xxxxxxxx = this.fontRenderer.drawStringWithShadow(☃xxx.substring(☃x), ☃xxxxxxxx, ☃xxxxxxx, ☃);
         }

         if (☃xxxxx) {
            if (☃xxxxxxxxx) {
               Gui.drawRect(☃xxxxxxxxxx, ☃xxxxxxx - 1, ☃xxxxxxxxxx + 1, ☃xxxxxxx + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
            } else {
               this.fontRenderer.drawStringWithShadow("_", ☃xxxxxxxxxx, ☃xxxxxxx, ☃);
            }
         }

         if (☃xx != ☃x) {
            int ☃xxxxxxxxxxx = ☃xxxxxx + this.fontRenderer.getStringWidth(☃xxx.substring(0, ☃xx));
            this.drawSelectionBox(☃xxxxxxxxxx, ☃xxxxxxx - 1, ☃xxxxxxxxxxx - 1, ☃xxxxxxx + 1 + this.fontRenderer.FONT_HEIGHT);
         }
      }
   }

   private void drawSelectionBox(int var1, int var2, int var3, int var4) {
      if (☃ < ☃) {
         int ☃ = ☃;
         ☃ = ☃;
         ☃ = ☃;
      }

      if (☃ < ☃) {
         int ☃ = ☃;
         ☃ = ☃;
         ☃ = ☃;
      }

      if (☃ > this.x + this.width) {
         ☃ = this.x + this.width;
      }

      if (☃ > this.x + this.width) {
         ☃ = this.x + this.width;
      }

      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.enableColorLogic();
      GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
      ☃x.begin(7, DefaultVertexFormats.POSITION);
      ☃x.pos(☃, ☃, 0.0).endVertex();
      ☃x.pos(☃, ☃, 0.0).endVertex();
      ☃x.pos(☃, ☃, 0.0).endVertex();
      ☃x.pos(☃, ☃, 0.0).endVertex();
      ☃.draw();
      GlStateManager.disableColorLogic();
      GlStateManager.enableTexture2D();
   }

   public void setMaxStringLength(int var1) {
      this.maxStringLength = ☃;
      if (this.text.length() > ☃) {
         this.text = this.text.substring(0, ☃);
      }
   }

   public int getMaxStringLength() {
      return this.maxStringLength;
   }

   public int getCursorPosition() {
      return this.cursorPosition;
   }

   public boolean getEnableBackgroundDrawing() {
      return this.enableBackgroundDrawing;
   }

   public void setEnableBackgroundDrawing(boolean var1) {
      this.enableBackgroundDrawing = ☃;
   }

   public void setTextColor(int var1) {
      this.enabledColor = ☃;
   }

   public void setDisabledTextColour(int var1) {
      this.disabledColor = ☃;
   }

   public void setFocused(boolean var1) {
      if (☃ && !this.isFocused) {
         this.cursorCounter = 0;
      }

      this.isFocused = ☃;
      if (Minecraft.getMinecraft().currentScreen != null) {
         Minecraft.getMinecraft().currentScreen.setFocused(☃);
      }
   }

   public boolean isFocused() {
      return this.isFocused;
   }

   public void setEnabled(boolean var1) {
      this.isEnabled = ☃;
   }

   public int getSelectionEnd() {
      return this.selectionEnd;
   }

   public int getWidth() {
      return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
   }

   public void setSelectionPos(int var1) {
      int ☃ = this.text.length();
      if (☃ > ☃) {
         ☃ = ☃;
      }

      if (☃ < 0) {
         ☃ = 0;
      }

      this.selectionEnd = ☃;
      if (this.fontRenderer != null) {
         if (this.lineScrollOffset > ☃) {
            this.lineScrollOffset = ☃;
         }

         int ☃x = this.getWidth();
         String ☃xx = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), ☃x);
         int ☃xxx = ☃xx.length() + this.lineScrollOffset;
         if (☃ == this.lineScrollOffset) {
            this.lineScrollOffset = this.lineScrollOffset - this.fontRenderer.trimStringToWidth(this.text, ☃x, true).length();
         }

         if (☃ > ☃xxx) {
            this.lineScrollOffset += ☃ - ☃xxx;
         } else if (☃ <= this.lineScrollOffset) {
            this.lineScrollOffset = this.lineScrollOffset - (this.lineScrollOffset - ☃);
         }

         this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, ☃);
      }
   }

   public void setCanLoseFocus(boolean var1) {
      this.canLoseFocus = ☃;
   }

   public boolean getVisible() {
      return this.visible;
   }

   public void setVisible(boolean var1) {
      this.visible = ☃;
   }
}
