package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;

public class GuiPlayerTabOverlay extends Gui {
   private static final Ordering<NetworkPlayerInfo> ENTRY_ORDERING = Ordering.from(new GuiPlayerTabOverlay.PlayerComparator());
   private final Minecraft mc;
   private final GuiIngame guiIngame;
   private ITextComponent footer;
   private ITextComponent header;
   private long lastTimeOpened;
   private boolean isBeingRendered;

   public GuiPlayerTabOverlay(Minecraft var1, GuiIngame var2) {
      this.mc = ☃;
      this.guiIngame = ☃;
   }

   public String getPlayerName(NetworkPlayerInfo var1) {
      return ☃.getDisplayName() != null
         ? ☃.getDisplayName().getFormattedText()
         : ScorePlayerTeam.formatPlayerName(☃.getPlayerTeam(), ☃.getGameProfile().getName());
   }

   public void updatePlayerList(boolean var1) {
      if (☃ && !this.isBeingRendered) {
         this.lastTimeOpened = Minecraft.getSystemTime();
      }

      this.isBeingRendered = ☃;
   }

   public void renderPlayerlist(int var1, Scoreboard var2, @Nullable ScoreObjective var3) {
      NetHandlerPlayClient ☃ = this.mc.player.connection;
      List<NetworkPlayerInfo> ☃x = ENTRY_ORDERING.sortedCopy(☃.getPlayerInfoMap());
      int ☃xx = 0;
      int ☃xxx = 0;

      for (NetworkPlayerInfo ☃xxxx : ☃x) {
         int ☃xxxxx = this.mc.fontRenderer.getStringWidth(this.getPlayerName(☃xxxx));
         ☃xx = Math.max(☃xx, ☃xxxxx);
         if (☃ != null && ☃.getRenderType() != IScoreCriteria.EnumRenderType.HEARTS) {
            ☃xxxxx = this.mc.fontRenderer.getStringWidth(" " + ☃.getOrCreateScore(☃xxxx.getGameProfile().getName(), ☃).getScorePoints());
            ☃xxx = Math.max(☃xxx, ☃xxxxx);
         }
      }

      ☃x = ☃x.subList(0, Math.min(☃x.size(), 80));
      int ☃xxxxx = ☃x.size();
      int ☃xxxxxx = ☃xxxxx;

      int ☃xxxxxxx;
      for (☃xxxxxxx = 1; ☃xxxxxx > 20; ☃xxxxxx = (☃xxxxx + ☃xxxxxxx - 1) / ☃xxxxxxx) {
         ☃xxxxxxx++;
      }

      boolean ☃xxxxxxxx = this.mc.isIntegratedServerRunning() || this.mc.getConnection().getNetworkManager().isEncrypted();
      int ☃xxxxxxxxx;
      if (☃ != null) {
         if (☃.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) {
            ☃xxxxxxxxx = 90;
         } else {
            ☃xxxxxxxxx = ☃xxx;
         }
      } else {
         ☃xxxxxxxxx = 0;
      }

      int ☃xxxxxxxxxx = Math.min(☃xxxxxxx * ((☃xxxxxxxx ? 9 : 0) + ☃xx + ☃xxxxxxxxx + 13), ☃ - 50) / ☃xxxxxxx;
      int ☃xxxxxxxxxxx = ☃ / 2 - (☃xxxxxxxxxx * ☃xxxxxxx + (☃xxxxxxx - 1) * 5) / 2;
      int ☃xxxxxxxxxxxx = 10;
      int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx * ☃xxxxxxx + (☃xxxxxxx - 1) * 5;
      List<String> ☃xxxxxxxxxxxxxx = null;
      if (this.header != null) {
         ☃xxxxxxxxxxxxxx = this.mc.fontRenderer.listFormattedStringToWidth(this.header.getFormattedText(), ☃ - 50);

         for (String ☃xxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxx) {
            ☃xxxxxxxxxxxxx = Math.max(☃xxxxxxxxxxxxx, this.mc.fontRenderer.getStringWidth(☃xxxxxxxxxxxxxxx));
         }
      }

      List<String> ☃xxxxxxxxxxxxxxx = null;
      if (this.footer != null) {
         ☃xxxxxxxxxxxxxxx = this.mc.fontRenderer.listFormattedStringToWidth(this.footer.getFormattedText(), ☃ - 50);

         for (String ☃xxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxx) {
            ☃xxxxxxxxxxxxx = Math.max(☃xxxxxxxxxxxxx, this.mc.fontRenderer.getStringWidth(☃xxxxxxxxxxxxxxxx));
         }
      }

      if (☃xxxxxxxxxxxxxx != null) {
         drawRect(
            ☃ / 2 - ☃xxxxxxxxxxxxx / 2 - 1,
            ☃xxxxxxxxxxxx - 1,
            ☃ / 2 + ☃xxxxxxxxxxxxx / 2 + 1,
            ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxx.size() * this.mc.fontRenderer.FONT_HEIGHT,
            Integer.MIN_VALUE
         );

         for (String ☃xxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxx) {
            int ☃xxxxxxxxxxxxxxxxx = this.mc.fontRenderer.getStringWidth(☃xxxxxxxxxxxxxxxx);
            this.mc.fontRenderer.drawStringWithShadow(☃xxxxxxxxxxxxxxxx, ☃ / 2 - ☃xxxxxxxxxxxxxxxxx / 2, ☃xxxxxxxxxxxx, -1);
            ☃xxxxxxxxxxxx += this.mc.fontRenderer.FONT_HEIGHT;
         }

         ☃xxxxxxxxxxxx++;
      }

      drawRect(☃ / 2 - ☃xxxxxxxxxxxxx / 2 - 1, ☃xxxxxxxxxxxx - 1, ☃ / 2 + ☃xxxxxxxxxxxxx / 2 + 1, ☃xxxxxxxxxxxx + ☃xxxxxx * 9, Integer.MIN_VALUE);

      for (int ☃xxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxx < ☃xxxxx; ☃xxxxxxxxxxxxxxxx++) {
         int ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx / ☃xxxxxx;
         int ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx % ☃xxxxxx;
         int ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxx + ☃xxxxxxxxxxxxxxxxx * 5;
         int ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx * 9;
         drawRect(☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx + 8, 553648127);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.enableAlpha();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         if (☃xxxxxxxxxxxxxxxx < ☃x.size()) {
            NetworkPlayerInfo ☃xxxxxxxxxxxxxxxxxxxxx = ☃x.get(☃xxxxxxxxxxxxxxxx);
            GameProfile ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx.getGameProfile();
            if (☃xxxxxxxx) {
               EntityPlayer ☃xxxxxxxxxxxxxxxxxxxxxxx = this.mc.world.getPlayerEntityByUUID(☃xxxxxxxxxxxxxxxxxxxxxx.getId());
               boolean ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxx != null
                  && ☃xxxxxxxxxxxxxxxxxxxxxxx.isWearing(EnumPlayerModelParts.CAPE)
                  && ("Dinnerbone".equals(☃xxxxxxxxxxxxxxxxxxxxxx.getName()) || "Grumm".equals(☃xxxxxxxxxxxxxxxxxxxxxx.getName()));
               this.mc.getTextureManager().bindTexture(☃xxxxxxxxxxxxxxxxxxxxx.getLocationSkin());
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = 8 + (☃xxxxxxxxxxxxxxxxxxxxxxxx ? 8 : 0);
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = 8 * (☃xxxxxxxxxxxxxxxxxxxxxxxx ? -1 : 1);
               Gui.drawScaledCustomSizeModalRect(
                  ☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, 8.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxx, 8, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, 8, 8, 64.0F, 64.0F
               );
               if (☃xxxxxxxxxxxxxxxxxxxxxxx != null && ☃xxxxxxxxxxxxxxxxxxxxxxx.isWearing(EnumPlayerModelParts.HAT)) {
                  int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = 8 + (☃xxxxxxxxxxxxxxxxxxxxxxxx ? 8 : 0);
                  int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = 8 * (☃xxxxxxxxxxxxxxxxxxxxxxxx ? -1 : 1);
                  Gui.drawScaledCustomSizeModalRect(
                     ☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, 40.0F, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, 8, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx, 8, 8, 64.0F, 64.0F
                  );
               }

               ☃xxxxxxxxxxxxxxxxxxx += 9;
            }

            String ☃xxxxxxxxxxxxxxxxxxxxxxx = this.getPlayerName(☃xxxxxxxxxxxxxxxxxxxxx);
            if (☃xxxxxxxxxxxxxxxxxxxxx.getGameType() == GameType.SPECTATOR) {
               this.mc
                  .fontRenderer
                  .drawStringWithShadow(TextFormatting.ITALIC + ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, -1862270977);
            } else {
               this.mc.fontRenderer.drawStringWithShadow(☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, -1);
            }

            if (☃ != null && ☃xxxxxxxxxxxxxxxxxxxxx.getGameType() != GameType.SPECTATOR) {
               int ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx + ☃xx + 1;
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxx;
               if (☃xxxxxxxxxxxxxxxxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxx > 5) {
                  this.drawScoreboardValues(
                     ☃, ☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx.getName(), ☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx
                  );
               }
            }

            this.drawPing(☃xxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx - (☃xxxxxxxx ? 9 : 0), ☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx);
         }
      }

      if (☃xxxxxxxxxxxxxxx != null) {
         ☃xxxxxxxxxxxx += ☃xxxxxx * 9 + 1;
         drawRect(
            ☃ / 2 - ☃xxxxxxxxxxxxx / 2 - 1,
            ☃xxxxxxxxxxxx - 1,
            ☃ / 2 + ☃xxxxxxxxxxxxx / 2 + 1,
            ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxx.size() * this.mc.fontRenderer.FONT_HEIGHT,
            Integer.MIN_VALUE
         );

         for (String ☃xxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxxx) {
            int ☃xxxxxxxxxxxxxxxxxx = this.mc.fontRenderer.getStringWidth(☃xxxxxxxxxxxxxxxxx);
            this.mc.fontRenderer.drawStringWithShadow(☃xxxxxxxxxxxxxxxxx, ☃ / 2 - ☃xxxxxxxxxxxxxxxxxx / 2, ☃xxxxxxxxxxxx, -1);
            ☃xxxxxxxxxxxx += this.mc.fontRenderer.FONT_HEIGHT;
         }
      }
   }

   protected void drawPing(int var1, int var2, int var3, NetworkPlayerInfo var4) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ICONS);
      int ☃ = 0;
      int ☃x;
      if (☃.getResponseTime() < 0) {
         ☃x = 5;
      } else if (☃.getResponseTime() < 150) {
         ☃x = 0;
      } else if (☃.getResponseTime() < 300) {
         ☃x = 1;
      } else if (☃.getResponseTime() < 600) {
         ☃x = 2;
      } else if (☃.getResponseTime() < 1000) {
         ☃x = 3;
      } else {
         ☃x = 4;
      }

      this.zLevel += 100.0F;
      this.drawTexturedModalRect(☃ + ☃ - 11, ☃, 0, 176 + ☃x * 8, 10, 8);
      this.zLevel -= 100.0F;
   }

   private void drawScoreboardValues(ScoreObjective var1, int var2, String var3, int var4, int var5, NetworkPlayerInfo var6) {
      int ☃ = ☃.getScoreboard().getOrCreateScore(☃, ☃).getScorePoints();
      if (☃.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) {
         this.mc.getTextureManager().bindTexture(ICONS);
         if (this.lastTimeOpened == ☃.getRenderVisibilityId()) {
            if (☃ < ☃.getLastHealth()) {
               ☃.setLastHealthTime(Minecraft.getSystemTime());
               ☃.setHealthBlinkTime(this.guiIngame.getUpdateCounter() + 20);
            } else if (☃ > ☃.getLastHealth()) {
               ☃.setLastHealthTime(Minecraft.getSystemTime());
               ☃.setHealthBlinkTime(this.guiIngame.getUpdateCounter() + 10);
            }
         }

         if (Minecraft.getSystemTime() - ☃.getLastHealthTime() > 1000L || this.lastTimeOpened != ☃.getRenderVisibilityId()) {
            ☃.setLastHealth(☃);
            ☃.setDisplayHealth(☃);
            ☃.setLastHealthTime(Minecraft.getSystemTime());
         }

         ☃.setRenderVisibilityId(this.lastTimeOpened);
         ☃.setLastHealth(☃);
         int ☃x = MathHelper.ceil(Math.max(☃, ☃.getDisplayHealth()) / 2.0F);
         int ☃xx = Math.max(MathHelper.ceil((float)(☃ / 2)), Math.max(MathHelper.ceil((float)(☃.getDisplayHealth() / 2)), 10));
         boolean ☃xxx = ☃.getHealthBlinkTime() > this.guiIngame.getUpdateCounter()
            && (☃.getHealthBlinkTime() - this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;
         if (☃x > 0) {
            float ☃xxxx = Math.min((float)(☃ - ☃ - 4) / ☃xx, 9.0F);
            if (☃xxxx > 3.0F) {
               for (int ☃xxxxx = ☃x; ☃xxxxx < ☃xx; ☃xxxxx++) {
                  this.drawTexturedModalRect(☃ + ☃xxxxx * ☃xxxx, ☃, ☃xxx ? 25 : 16, 0, 9, 9);
               }

               for (int ☃xxxxx = 0; ☃xxxxx < ☃x; ☃xxxxx++) {
                  this.drawTexturedModalRect(☃ + ☃xxxxx * ☃xxxx, ☃, ☃xxx ? 25 : 16, 0, 9, 9);
                  if (☃xxx) {
                     if (☃xxxxx * 2 + 1 < ☃.getDisplayHealth()) {
                        this.drawTexturedModalRect(☃ + ☃xxxxx * ☃xxxx, ☃, 70, 0, 9, 9);
                     }

                     if (☃xxxxx * 2 + 1 == ☃.getDisplayHealth()) {
                        this.drawTexturedModalRect(☃ + ☃xxxxx * ☃xxxx, ☃, 79, 0, 9, 9);
                     }
                  }

                  if (☃xxxxx * 2 + 1 < ☃) {
                     this.drawTexturedModalRect(☃ + ☃xxxxx * ☃xxxx, ☃, ☃xxxxx >= 10 ? 160 : 52, 0, 9, 9);
                  }

                  if (☃xxxxx * 2 + 1 == ☃) {
                     this.drawTexturedModalRect(☃ + ☃xxxxx * ☃xxxx, ☃, ☃xxxxx >= 10 ? 169 : 61, 0, 9, 9);
                  }
               }
            } else {
               float ☃xxxxx = MathHelper.clamp(☃ / 20.0F, 0.0F, 1.0F);
               int ☃xxxxxx = (int)((1.0F - ☃xxxxx) * 255.0F) << 16 | (int)(☃xxxxx * 255.0F) << 8;
               String ☃xxxxxxx = "" + ☃ / 2.0F;
               if (☃ - this.mc.fontRenderer.getStringWidth(☃xxxxxxx + "hp") >= ☃) {
                  ☃xxxxxxx = ☃xxxxxxx + "hp";
               }

               this.mc.fontRenderer.drawStringWithShadow(☃xxxxxxx, (☃ + ☃) / 2 - this.mc.fontRenderer.getStringWidth(☃xxxxxxx) / 2, ☃, ☃xxxxxx);
            }
         }
      } else {
         String ☃x = TextFormatting.YELLOW + "" + ☃;
         this.mc.fontRenderer.drawStringWithShadow(☃x, ☃ - this.mc.fontRenderer.getStringWidth(☃x), ☃, 16777215);
      }
   }

   public void setFooter(@Nullable ITextComponent var1) {
      this.footer = ☃;
   }

   public void setHeader(@Nullable ITextComponent var1) {
      this.header = ☃;
   }

   public void resetFooterHeader() {
      this.header = null;
      this.footer = null;
   }

   static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
      private PlayerComparator() {
      }

      public int compare(NetworkPlayerInfo var1, NetworkPlayerInfo var2) {
         ScorePlayerTeam ☃ = ☃.getPlayerTeam();
         ScorePlayerTeam ☃x = ☃.getPlayerTeam();
         return ComparisonChain.start()
            .compareTrueFirst(☃.getGameType() != GameType.SPECTATOR, ☃.getGameType() != GameType.SPECTATOR)
            .compare(☃ != null ? ☃.getName() : "", ☃x != null ? ☃x.getName() : "")
            .compare(☃.getGameProfile().getName(), ☃.getGameProfile().getName())
            .result();
      }
   }
}
