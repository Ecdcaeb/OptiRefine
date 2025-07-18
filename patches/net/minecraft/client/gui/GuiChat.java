package net.minecraft.client.gui;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiChat extends GuiScreen implements ITabCompleter {
   private static final Logger LOGGER = LogManager.getLogger();
   private String historyBuffer = "";
   private int sentHistoryCursor = -1;
   private TabCompleter tabCompleter;
   protected GuiTextField inputField;
   private String defaultInputFieldText = "";

   public GuiChat() {
   }

   public GuiChat(String var1) {
      this.defaultInputFieldText = ☃;
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      this.inputField = new GuiTextField(0, this.fontRenderer, 4, this.height - 12, this.width - 4, 12);
      this.inputField.setMaxStringLength(256);
      this.inputField.setEnableBackgroundDrawing(false);
      this.inputField.setFocused(true);
      this.inputField.setText(this.defaultInputFieldText);
      this.inputField.setCanLoseFocus(false);
      this.tabCompleter = new GuiChat.ChatTabCompleter(this.inputField);
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.ingameGUI.getChatGUI().resetScroll();
   }

   @Override
   public void updateScreen() {
      this.inputField.updateCursorCounter();
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      this.tabCompleter.resetRequested();
      if (☃ == 15) {
         this.tabCompleter.complete();
      } else {
         this.tabCompleter.resetDidComplete();
      }

      if (☃ == 1) {
         this.mc.displayGuiScreen(null);
      } else if (☃ == 28 || ☃ == 156) {
         String ☃ = this.inputField.getText().trim();
         if (!☃.isEmpty()) {
            this.sendChatMessage(☃);
         }

         this.mc.displayGuiScreen(null);
      } else if (☃ == 200) {
         this.getSentHistory(-1);
      } else if (☃ == 208) {
         this.getSentHistory(1);
      } else if (☃ == 201) {
         this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
      } else if (☃ == 209) {
         this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
      } else {
         this.inputField.textboxKeyTyped(☃, ☃);
      }
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      int ☃ = Mouse.getEventDWheel();
      if (☃ != 0) {
         if (☃ > 1) {
            ☃ = 1;
         }

         if (☃ < -1) {
            ☃ = -1;
         }

         if (!isShiftKeyDown()) {
            ☃ *= 7;
         }

         this.mc.ingameGUI.getChatGUI().scroll(☃);
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      if (☃ == 0) {
         ITextComponent ☃ = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
         if (☃ != null && this.handleComponentClick(☃)) {
            return;
         }
      }

      this.inputField.mouseClicked(☃, ☃, ☃);
      super.mouseClicked(☃, ☃, ☃);
   }

   @Override
   protected void setText(String var1, boolean var2) {
      if (☃) {
         this.inputField.setText(☃);
      } else {
         this.inputField.writeText(☃);
      }
   }

   public void getSentHistory(int var1) {
      int ☃ = this.sentHistoryCursor + ☃;
      int ☃x = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      ☃ = MathHelper.clamp(☃, 0, ☃x);
      if (☃ != this.sentHistoryCursor) {
         if (☃ == ☃x) {
            this.sentHistoryCursor = ☃x;
            this.inputField.setText(this.historyBuffer);
         } else {
            if (this.sentHistoryCursor == ☃x) {
               this.historyBuffer = this.inputField.getText();
            }

            this.inputField.setText(this.mc.ingameGUI.getChatGUI().getSentMessages().get(☃));
            this.sentHistoryCursor = ☃;
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
      this.inputField.drawTextBox();
      ITextComponent ☃ = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
      if (☃ != null && ☃.getStyle().getHoverEvent() != null) {
         this.handleComponentHover(☃, ☃, ☃);
      }

      super.drawScreen(☃, ☃, ☃);
   }

   @Override
   public boolean doesGuiPauseGame() {
      return false;
   }

   @Override
   public void setCompletions(String... var1) {
      this.tabCompleter.setCompletions(☃);
   }

   public static class ChatTabCompleter extends TabCompleter {
      private final Minecraft client = Minecraft.getMinecraft();

      public ChatTabCompleter(GuiTextField var1) {
         super(☃, false);
      }

      @Override
      public void complete() {
         super.complete();
         if (this.completions.size() > 1) {
            StringBuilder ☃ = new StringBuilder();

            for (String ☃x : this.completions) {
               if (☃.length() > 0) {
                  ☃.append(", ");
               }

               ☃.append(☃x);
            }

            this.client.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(☃.toString()), 1);
         }
      }

      @Nullable
      @Override
      public BlockPos getTargetBlockPos() {
         BlockPos ☃ = null;
         if (this.client.objectMouseOver != null && this.client.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            ☃ = this.client.objectMouseOver.getBlockPos();
         }

         return ☃;
      }
   }
}
