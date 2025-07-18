package net.minecraft.client.multiplayer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiConnecting extends GuiScreen {
   private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
   private static final Logger LOGGER = LogManager.getLogger();
   private NetworkManager networkManager;
   private boolean cancel;
   private final GuiScreen previousGuiScreen;

   public GuiConnecting(GuiScreen var1, Minecraft var2, ServerData var3) {
      this.mc = ☃;
      this.previousGuiScreen = ☃;
      ServerAddress ☃ = ServerAddress.fromString(☃.serverIP);
      ☃.loadWorld(null);
      ☃.setServerData(☃);
      this.connect(☃.getIP(), ☃.getPort());
   }

   public GuiConnecting(GuiScreen var1, Minecraft var2, String var3, int var4) {
      this.mc = ☃;
      this.previousGuiScreen = ☃;
      ☃.loadWorld(null);
      this.connect(☃, ☃);
   }

   private void connect(final String var1, final int var2) {
      LOGGER.info("Connecting to {}, {}", ☃, ☃);
      (new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet()) {
            @Override
            public void run() {
               InetAddress ☃ = null;

               try {
                  if (GuiConnecting.this.cancel) {
                     return;
                  }

                  ☃ = InetAddress.getByName(☃);
                  GuiConnecting.this.networkManager = NetworkManager.createNetworkManagerAndConnect(
                     ☃, ☃, GuiConnecting.this.mc.gameSettings.isUsingNativeTransport()
                  );
                  GuiConnecting.this.networkManager
                     .setNetHandler(new NetHandlerLoginClient(GuiConnecting.this.networkManager, GuiConnecting.this.mc, GuiConnecting.this.previousGuiScreen));
                  GuiConnecting.this.networkManager.sendPacket(new C00Handshake(☃, ☃, EnumConnectionState.LOGIN));
                  GuiConnecting.this.networkManager.sendPacket(new CPacketLoginStart(GuiConnecting.this.mc.getSession().getProfile()));
               } catch (UnknownHostException var5) {
                  if (GuiConnecting.this.cancel) {
                     return;
                  }

                  GuiConnecting.LOGGER.error("Couldn't connect to server", var5);
                  GuiConnecting.this.mc
                     .displayGuiScreen(
                        new GuiDisconnected(
                           GuiConnecting.this.previousGuiScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", "Unknown host")
                        )
                     );
               } catch (Exception var6) {
                  if (GuiConnecting.this.cancel) {
                     return;
                  }

                  GuiConnecting.LOGGER.error("Couldn't connect to server", var6);
                  String ☃x = var6.toString();
                  if (☃ != null) {
                     String ☃xx = ☃ + ":" + ☃;
                     ☃x = ☃x.replaceAll(☃xx, "");
                  }

                  GuiConnecting.this.mc
                     .displayGuiScreen(
                        new GuiDisconnected(
                           GuiConnecting.this.previousGuiScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", ☃x)
                        )
                     );
               }
            }
         })
         .start();
   }

   @Override
   public void updateScreen() {
      if (this.networkManager != null) {
         if (this.networkManager.isChannelOpen()) {
            this.networkManager.processReceivedPackets();
         } else {
            this.networkManager.handleDisconnection();
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == 0) {
         this.cancel = true;
         if (this.networkManager != null) {
            this.networkManager.closeChannel(new TextComponentString("Aborted"));
         }

         this.mc.displayGuiScreen(this.previousGuiScreen);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      if (this.networkManager == null) {
         this.drawCenteredString(this.fontRenderer, I18n.format("connect.connecting"), this.width / 2, this.height / 2 - 50, 16777215);
      } else {
         this.drawCenteredString(this.fontRenderer, I18n.format("connect.authorizing"), this.width / 2, this.height / 2 - 50, 16777215);
      }

      super.drawScreen(☃, ☃, ☃);
   }
}
