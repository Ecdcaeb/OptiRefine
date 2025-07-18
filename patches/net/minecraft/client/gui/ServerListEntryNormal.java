package net.minecraft.client.gui;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(
      5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).build()
   );
   private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
   private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
   private final GuiMultiplayer owner;
   private final Minecraft mc;
   private final ServerData server;
   private final ResourceLocation serverIcon;
   private String lastIconB64;
   private DynamicTexture icon;
   private long lastClickTime;

   protected ServerListEntryNormal(GuiMultiplayer var1, ServerData var2) {
      this.owner = ☃;
      this.server = ☃;
      this.mc = Minecraft.getMinecraft();
      this.serverIcon = new ResourceLocation("servers/" + ☃.serverIP + "/icon");
      this.icon = (DynamicTexture)this.mc.getTextureManager().getTexture(this.serverIcon);
   }

   @Override
   public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
      if (!this.server.pinged) {
         this.server.pinged = true;
         this.server.pingToServer = -2L;
         this.server.serverMOTD = "";
         this.server.populationInfo = "";
         EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
               try {
                  ServerListEntryNormal.this.owner.getOldServerPinger().ping(ServerListEntryNormal.this.server);
               } catch (UnknownHostException var2x) {
                  ServerListEntryNormal.this.server.pingToServer = -1L;
                  ServerListEntryNormal.this.server.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_resolve");
               } catch (Exception var3x) {
                  ServerListEntryNormal.this.server.pingToServer = -1L;
                  ServerListEntryNormal.this.server.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_connect");
               }
            }
         });
      }

      boolean ☃ = this.server.version > 340;
      boolean ☃x = this.server.version < 340;
      boolean ☃xx = ☃ || ☃x;
      this.mc.fontRenderer.drawString(this.server.serverName, ☃ + 32 + 3, ☃ + 1, 16777215);
      List<String> ☃xxx = this.mc.fontRenderer.listFormattedStringToWidth(this.server.serverMOTD, ☃ - 32 - 2);

      for (int ☃xxxx = 0; ☃xxxx < Math.min(☃xxx.size(), 2); ☃xxxx++) {
         this.mc.fontRenderer.drawString(☃xxx.get(☃xxxx), ☃ + 32 + 3, ☃ + 12 + this.mc.fontRenderer.FONT_HEIGHT * ☃xxxx, 8421504);
      }

      String ☃xxxx = ☃xx ? TextFormatting.DARK_RED + this.server.gameVersion : this.server.populationInfo;
      int ☃xxxxx = this.mc.fontRenderer.getStringWidth(☃xxxx);
      this.mc.fontRenderer.drawString(☃xxxx, ☃ + ☃ - ☃xxxxx - 15 - 2, ☃ + 1, 8421504);
      int ☃xxxxxx = 0;
      String ☃xxxxxxx = null;
      int ☃xxxxxxxx;
      String ☃xxxxxxxxx;
      if (☃xx) {
         ☃xxxxxxxx = 5;
         ☃xxxxxxxxx = I18n.format(☃ ? "multiplayer.status.client_out_of_date" : "multiplayer.status.server_out_of_date");
         ☃xxxxxxx = this.server.playerList;
      } else if (this.server.pinged && this.server.pingToServer != -2L) {
         if (this.server.pingToServer < 0L) {
            ☃xxxxxxxx = 5;
         } else if (this.server.pingToServer < 150L) {
            ☃xxxxxxxx = 0;
         } else if (this.server.pingToServer < 300L) {
            ☃xxxxxxxx = 1;
         } else if (this.server.pingToServer < 600L) {
            ☃xxxxxxxx = 2;
         } else if (this.server.pingToServer < 1000L) {
            ☃xxxxxxxx = 3;
         } else {
            ☃xxxxxxxx = 4;
         }

         if (this.server.pingToServer < 0L) {
            ☃xxxxxxxxx = I18n.format("multiplayer.status.no_connection");
         } else {
            ☃xxxxxxxxx = this.server.pingToServer + "ms";
            ☃xxxxxxx = this.server.playerList;
         }
      } else {
         ☃xxxxxx = 1;
         ☃xxxxxxxx = (int)(Minecraft.getSystemTime() / 100L + ☃ * 2 & 7L);
         if (☃xxxxxxxx > 4) {
            ☃xxxxxxxx = 8 - ☃xxxxxxxx;
         }

         ☃xxxxxxxxx = I18n.format("multiplayer.status.pinging");
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(Gui.ICONS);
      Gui.drawModalRectWithCustomSizedTexture(☃ + ☃ - 15, ☃, ☃xxxxxx * 10, 176 + ☃xxxxxxxx * 8, 10, 8, 256.0F, 256.0F);
      if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.lastIconB64)) {
         this.lastIconB64 = this.server.getBase64EncodedIconData();
         this.prepareServerIcon();
         this.owner.getServerList().saveServerList();
      }

      if (this.icon != null) {
         this.drawTextureAt(☃, ☃, this.serverIcon);
      } else {
         this.drawTextureAt(☃, ☃, UNKNOWN_SERVER);
      }

      int ☃xxxxxxxxxx = ☃ - ☃;
      int ☃xxxxxxxxxxx = ☃ - ☃;
      if (☃xxxxxxxxxx >= ☃ - 15 && ☃xxxxxxxxxx <= ☃ - 5 && ☃xxxxxxxxxxx >= 0 && ☃xxxxxxxxxxx <= 8) {
         this.owner.setHoveringText(☃xxxxxxxxx);
      } else if (☃xxxxxxxxxx >= ☃ - ☃xxxxx - 15 - 2 && ☃xxxxxxxxxx <= ☃ - 15 - 2 && ☃xxxxxxxxxxx >= 0 && ☃xxxxxxxxxxx <= 8) {
         this.owner.setHoveringText(☃xxxxxxx);
      }

      if (this.mc.gameSettings.touchscreen || ☃) {
         this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
         Gui.drawRect(☃, ☃, ☃ + 32, ☃ + 32, -1601138544);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         int ☃xxxxxxxxxxxx = ☃ - ☃;
         int ☃xxxxxxxxxxxxx = ☃ - ☃;
         if (this.canJoin()) {
            if (☃xxxxxxxxxxxx < 32 && ☃xxxxxxxxxxxx > 16) {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
            } else {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
            }
         }

         if (this.owner.canMoveUp(this, ☃)) {
            if (☃xxxxxxxxxxxx < 16 && ☃xxxxxxxxxxxxx < 16) {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
            } else {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
            }
         }

         if (this.owner.canMoveDown(this, ☃)) {
            if (☃xxxxxxxxxxxx < 16 && ☃xxxxxxxxxxxxx > 16) {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
            } else {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
            }
         }
      }
   }

   protected void drawTextureAt(int var1, int var2, ResourceLocation var3) {
      this.mc.getTextureManager().bindTexture(☃);
      GlStateManager.enableBlend();
      Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
      GlStateManager.disableBlend();
   }

   private boolean canJoin() {
      return true;
   }

   private void prepareServerIcon() {
      if (this.server.getBase64EncodedIconData() == null) {
         this.mc.getTextureManager().deleteTexture(this.serverIcon);
         this.icon = null;
      } else {
         ByteBuf ☃ = Unpooled.copiedBuffer(this.server.getBase64EncodedIconData(), StandardCharsets.UTF_8);
         ByteBuf ☃x = null;

         BufferedImage ☃xx;
         label80: {
            try {
               ☃x = Base64.decode(☃);
               ☃xx = TextureUtil.readBufferedImage(new ByteBufInputStream(☃x));
               Validate.validState(☃xx.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
               Validate.validState(☃xx.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
               break label80;
            } catch (Throwable var8) {
               LOGGER.error("Invalid icon for server {} ({})", this.server.serverName, this.server.serverIP, var8);
               this.server.setBase64EncodedIconData(null);
            } finally {
               ☃.release();
               if (☃x != null) {
                  ☃x.release();
               }
            }

            return;
         }

         if (this.icon == null) {
            this.icon = new DynamicTexture(☃xx.getWidth(), ☃xx.getHeight());
            this.mc.getTextureManager().loadTexture(this.serverIcon, this.icon);
         }

         ☃xx.getRGB(0, 0, ☃xx.getWidth(), ☃xx.getHeight(), this.icon.getTextureData(), 0, ☃xx.getWidth());
         this.icon.updateDynamicTexture();
      }
   }

   @Override
   public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (☃ <= 32) {
         if (☃ < 32 && ☃ > 16 && this.canJoin()) {
            this.owner.selectServer(☃);
            this.owner.connectToSelected();
            return true;
         }

         if (☃ < 16 && ☃ < 16 && this.owner.canMoveUp(this, ☃)) {
            this.owner.moveServerUp(this, ☃, GuiScreen.isShiftKeyDown());
            return true;
         }

         if (☃ < 16 && ☃ > 16 && this.owner.canMoveDown(this, ☃)) {
            this.owner.moveServerDown(this, ☃, GuiScreen.isShiftKeyDown());
            return true;
         }
      }

      this.owner.selectServer(☃);
      if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
         this.owner.connectToSelected();
      }

      this.lastClickTime = Minecraft.getSystemTime();
      return false;
   }

   @Override
   public void updatePosition(int var1, int var2, int var3, float var4) {
   }

   @Override
   public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   public ServerData getServerData() {
      return this.server;
   }
}
