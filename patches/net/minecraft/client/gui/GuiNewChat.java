package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiNewChat extends Gui {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Minecraft mc;
   private final List<String> sentMessages = Lists.newArrayList();
   private final List<ChatLine> chatLines = Lists.newArrayList();
   private final List<ChatLine> drawnChatLines = Lists.newArrayList();
   private int scrollPos;
   private boolean isScrolled;

   public GuiNewChat(Minecraft var1) {
      this.mc = ☃;
   }

   public void drawChat(int var1) {
      if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
         int ☃ = this.getLineCount();
         int ☃x = this.drawnChatLines.size();
         float ☃xx = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
         if (☃x > 0) {
            boolean ☃xxx = false;
            if (this.getChatOpen()) {
               ☃xxx = true;
            }

            float ☃xxxx = this.getChatScale();
            int ☃xxxxx = MathHelper.ceil(this.getChatWidth() / ☃xxxx);
            GlStateManager.pushMatrix();
            GlStateManager.translate(2.0F, 8.0F, 0.0F);
            GlStateManager.scale(☃xxxx, ☃xxxx, 1.0F);
            int ☃xxxxxx = 0;

            for (int ☃xxxxxxx = 0; ☃xxxxxxx + this.scrollPos < this.drawnChatLines.size() && ☃xxxxxxx < ☃; ☃xxxxxxx++) {
               ChatLine ☃xxxxxxxx = this.drawnChatLines.get(☃xxxxxxx + this.scrollPos);
               if (☃xxxxxxxx != null) {
                  int ☃xxxxxxxxx = ☃ - ☃xxxxxxxx.getUpdatedCounter();
                  if (☃xxxxxxxxx < 200 || ☃xxx) {
                     double ☃xxxxxxxxxx = ☃xxxxxxxxx / 200.0;
                     ☃xxxxxxxxxx = 1.0 - ☃xxxxxxxxxx;
                     ☃xxxxxxxxxx *= 10.0;
                     ☃xxxxxxxxxx = MathHelper.clamp(☃xxxxxxxxxx, 0.0, 1.0);
                     ☃xxxxxxxxxx *= ☃xxxxxxxxxx;
                     int ☃xxxxxxxxxxx = (int)(255.0 * ☃xxxxxxxxxx);
                     if (☃xxx) {
                        ☃xxxxxxxxxxx = 255;
                     }

                     ☃xxxxxxxxxxx = (int)(☃xxxxxxxxxxx * ☃xx);
                     ☃xxxxxx++;
                     if (☃xxxxxxxxxxx > 3) {
                        int ☃xxxxxxxxxxxx = 0;
                        int ☃xxxxxxxxxxxxx = -☃xxxxxxx * 9;
                        drawRect(-2, ☃xxxxxxxxxxxxx - 9, 0 + ☃xxxxx + 4, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxx / 2 << 24);
                        String ☃xxxxxxxxxxxxxx = ☃xxxxxxxx.getChatComponent().getFormattedText();
                        GlStateManager.enableBlend();
                        this.mc.fontRenderer.drawStringWithShadow(☃xxxxxxxxxxxxxx, 0.0F, ☃xxxxxxxxxxxxx - 8, 16777215 + (☃xxxxxxxxxxx << 24));
                        GlStateManager.disableAlpha();
                        GlStateManager.disableBlend();
                     }
                  }
               }
            }

            if (☃xxx) {
               int ☃xxxxxxxx = this.mc.fontRenderer.FONT_HEIGHT;
               GlStateManager.translate(-3.0F, 0.0F, 0.0F);
               int ☃xxxxxxxxx = ☃x * ☃xxxxxxxx + ☃x;
               int ☃xxxxxxxxxxxx = ☃xxxxxx * ☃xxxxxxxx + ☃xxxxxx;
               int ☃xxxxxxxxxxxxx = this.scrollPos * ☃xxxxxxxxxxxx / ☃x;
               int ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx / ☃xxxxxxxxx;
               if (☃xxxxxxxxx != ☃xxxxxxxxxxxx) {
                  int ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx > 0 ? 170 : 96;
                  int ☃xxxxxxxxxxxxxxxx = this.isScrolled ? 13382451 : 3355562;
                  drawRect(0, -☃xxxxxxxxxxxxx, 2, -☃xxxxxxxxxxxxx - ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx + (☃xxxxxxxxxxxxxxx << 24));
                  drawRect(2, -☃xxxxxxxxxxxxx, 1, -☃xxxxxxxxxxxxx - ☃xxxxxxxxxxxxxx, 13421772 + (☃xxxxxxxxxxxxxxx << 24));
               }
            }

            GlStateManager.popMatrix();
         }
      }
   }

   public void clearChatMessages(boolean var1) {
      this.drawnChatLines.clear();
      this.chatLines.clear();
      if (☃) {
         this.sentMessages.clear();
      }
   }

   public void printChatMessage(ITextComponent var1) {
      this.printChatMessageWithOptionalDeletion(☃, 0);
   }

   public void printChatMessageWithOptionalDeletion(ITextComponent var1, int var2) {
      this.setChatLine(☃, ☃, this.mc.ingameGUI.getUpdateCounter(), false);
      LOGGER.info("[CHAT] {}", ☃.getUnformattedText().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
   }

   private void setChatLine(ITextComponent var1, int var2, int var3, boolean var4) {
      if (☃ != 0) {
         this.deleteChatLine(☃);
      }

      int ☃ = MathHelper.floor(this.getChatWidth() / this.getChatScale());
      List<ITextComponent> ☃x = GuiUtilRenderComponents.splitText(☃, ☃, this.mc.fontRenderer, false, false);
      boolean ☃xx = this.getChatOpen();

      for (ITextComponent ☃xxx : ☃x) {
         if (☃xx && this.scrollPos > 0) {
            this.isScrolled = true;
            this.scroll(1);
         }

         this.drawnChatLines.add(0, new ChatLine(☃, ☃xxx, ☃));
      }

      while (this.drawnChatLines.size() > 100) {
         this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
      }

      if (!☃) {
         this.chatLines.add(0, new ChatLine(☃, ☃, ☃));

         while (this.chatLines.size() > 100) {
            this.chatLines.remove(this.chatLines.size() - 1);
         }
      }
   }

   public void refreshChat() {
      this.drawnChatLines.clear();
      this.resetScroll();

      for (int ☃ = this.chatLines.size() - 1; ☃ >= 0; ☃--) {
         ChatLine ☃x = this.chatLines.get(☃);
         this.setChatLine(☃x.getChatComponent(), ☃x.getChatLineID(), ☃x.getUpdatedCounter(), true);
      }
   }

   public List<String> getSentMessages() {
      return this.sentMessages;
   }

   public void addToSentMessages(String var1) {
      if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(☃)) {
         this.sentMessages.add(☃);
      }
   }

   public void resetScroll() {
      this.scrollPos = 0;
      this.isScrolled = false;
   }

   public void scroll(int var1) {
      this.scrollPos += ☃;
      int ☃ = this.drawnChatLines.size();
      if (this.scrollPos > ☃ - this.getLineCount()) {
         this.scrollPos = ☃ - this.getLineCount();
      }

      if (this.scrollPos <= 0) {
         this.scrollPos = 0;
         this.isScrolled = false;
      }
   }

   @Nullable
   public ITextComponent getChatComponent(int var1, int var2) {
      if (!this.getChatOpen()) {
         return null;
      } else {
         ScaledResolution ☃ = new ScaledResolution(this.mc);
         int ☃x = ☃.getScaleFactor();
         float ☃xx = this.getChatScale();
         int ☃xxx = ☃ / ☃x - 2;
         int ☃xxxx = ☃ / ☃x - 40;
         ☃xxx = MathHelper.floor(☃xxx / ☃xx);
         ☃xxxx = MathHelper.floor(☃xxxx / ☃xx);
         if (☃xxx >= 0 && ☃xxxx >= 0) {
            int ☃xxxxx = Math.min(this.getLineCount(), this.drawnChatLines.size());
            if (☃xxx <= MathHelper.floor(this.getChatWidth() / this.getChatScale()) && ☃xxxx < this.mc.fontRenderer.FONT_HEIGHT * ☃xxxxx + ☃xxxxx) {
               int ☃xxxxxx = ☃xxxx / this.mc.fontRenderer.FONT_HEIGHT + this.scrollPos;
               if (☃xxxxxx >= 0 && ☃xxxxxx < this.drawnChatLines.size()) {
                  ChatLine ☃xxxxxxx = this.drawnChatLines.get(☃xxxxxx);
                  int ☃xxxxxxxx = 0;

                  for (ITextComponent ☃xxxxxxxxx : ☃xxxxxxx.getChatComponent()) {
                     if (☃xxxxxxxxx instanceof TextComponentString) {
                        ☃xxxxxxxx += this.mc
                           .fontRenderer
                           .getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponentString)☃xxxxxxxxx).getText(), false));
                        if (☃xxxxxxxx > ☃xxx) {
                           return ☃xxxxxxxxx;
                        }
                     }
                  }
               }

               return null;
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public boolean getChatOpen() {
      return this.mc.currentScreen instanceof GuiChat;
   }

   public void deleteChatLine(int var1) {
      Iterator<ChatLine> ☃ = this.drawnChatLines.iterator();

      while (☃.hasNext()) {
         ChatLine ☃x = ☃.next();
         if (☃x.getChatLineID() == ☃) {
            ☃.remove();
         }
      }

      ☃ = this.chatLines.iterator();

      while (☃.hasNext()) {
         ChatLine ☃x = ☃.next();
         if (☃x.getChatLineID() == ☃) {
            ☃.remove();
            break;
         }
      }
   }

   public int getChatWidth() {
      return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
   }

   public int getChatHeight() {
      return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
   }

   public float getChatScale() {
      return this.mc.gameSettings.chatScale;
   }

   public static int calculateChatboxWidth(float var0) {
      int ☃ = 320;
      int ☃x = 40;
      return MathHelper.floor(☃ * 280.0F + 40.0F);
   }

   public static int calculateChatboxHeight(float var0) {
      int ☃ = 180;
      int ☃x = 20;
      return MathHelper.floor(☃ * 160.0F + 20.0F);
   }

   public int getLineCount() {
      return this.getChatHeight() / 9;
   }
}
