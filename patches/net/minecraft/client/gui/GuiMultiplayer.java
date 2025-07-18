package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer extends GuiScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private final ServerPinger oldServerPinger = new ServerPinger();
   private final GuiScreen parentScreen;
   private ServerSelectionList serverListSelector;
   private ServerList savedServerList;
   private GuiButton btnEditServer;
   private GuiButton btnSelectServer;
   private GuiButton btnDeleteServer;
   private boolean deletingServer;
   private boolean addingServer;
   private boolean editingServer;
   private boolean directConnect;
   private String hoveringText;
   private ServerData selectedServer;
   private LanServerDetector.LanServerList lanServerList;
   private LanServerDetector.ThreadLanServerFind lanServerDetector;
   private boolean initialized;

   public GuiMultiplayer(GuiScreen var1) {
      this.parentScreen = ☃;
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      if (this.initialized) {
         this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
      } else {
         this.initialized = true;
         this.savedServerList = new ServerList(this.mc);
         this.savedServerList.loadServerList();
         this.lanServerList = new LanServerDetector.LanServerList();

         try {
            this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
            this.lanServerDetector.start();
         } catch (Exception var2) {
            LOGGER.warn("Unable to start LAN server detection: {}", var2.getMessage());
         }

         this.serverListSelector = new ServerSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
         this.serverListSelector.updateOnlineServers(this.savedServerList);
      }

      this.createButtons();
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      this.serverListSelector.handleMouseInput();
   }

   public void createButtons() {
      this.btnEditServer = this.addButton(new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit")));
      this.btnDeleteServer = this.addButton(new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete")));
      this.btnSelectServer = this.addButton(new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("selectServer.select")));
      this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("selectServer.direct")));
      this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("selectServer.add")));
      this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh")));
      this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel")));
      this.selectServer(this.serverListSelector.getSelected());
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      if (this.lanServerList.getWasUpdated()) {
         List<LanServerInfo> ☃ = this.lanServerList.getLanServers();
         this.lanServerList.setWasNotUpdated();
         this.serverListSelector.updateNetworkServers(☃);
      }

      this.oldServerPinger.pingPendingNetworks();
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      if (this.lanServerDetector != null) {
         this.lanServerDetector.interrupt();
         this.lanServerDetector = null;
      }

      this.oldServerPinger.clearPendingNetworks();
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         GuiListExtended.IGuiListEntry ☃ = this.serverListSelector.getSelected() < 0
            ? null
            : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
         if (☃.id == 2 && ☃ instanceof ServerListEntryNormal) {
            String ☃x = ((ServerListEntryNormal)☃).getServerData().serverName;
            if (☃x != null) {
               this.deletingServer = true;
               String ☃xx = I18n.format("selectServer.deleteQuestion");
               String ☃xxx = "'" + ☃x + "' " + I18n.format("selectServer.deleteWarning");
               String ☃xxxx = I18n.format("selectServer.deleteButton");
               String ☃xxxxx = I18n.format("gui.cancel");
               GuiYesNo ☃xxxxxx = new GuiYesNo(this, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx, this.serverListSelector.getSelected());
               this.mc.displayGuiScreen(☃xxxxxx);
            }
         } else if (☃.id == 1) {
            this.connectToSelected();
         } else if (☃.id == 4) {
            this.directConnect = true;
            this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
            this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer));
         } else if (☃.id == 3) {
            this.addingServer = true;
            this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
            this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
         } else if (☃.id == 7 && ☃ instanceof ServerListEntryNormal) {
            this.editingServer = true;
            ServerData ☃x = ((ServerListEntryNormal)☃).getServerData();
            this.selectedServer = new ServerData(☃x.serverName, ☃x.serverIP, false);
            this.selectedServer.copyFrom(☃x);
            this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
         } else if (☃.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (☃.id == 8) {
            this.refreshServerList();
         }
      }
   }

   private void refreshServerList() {
      this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      GuiListExtended.IGuiListEntry ☃ = this.serverListSelector.getSelected() < 0
         ? null
         : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
      if (this.deletingServer) {
         this.deletingServer = false;
         if (☃ && ☃ instanceof ServerListEntryNormal) {
            this.savedServerList.removeServerData(this.serverListSelector.getSelected());
            this.savedServerList.saveServerList();
            this.serverListSelector.setSelectedSlotIndex(-1);
            this.serverListSelector.updateOnlineServers(this.savedServerList);
         }

         this.mc.displayGuiScreen(this);
      } else if (this.directConnect) {
         this.directConnect = false;
         if (☃) {
            this.connectToServer(this.selectedServer);
         } else {
            this.mc.displayGuiScreen(this);
         }
      } else if (this.addingServer) {
         this.addingServer = false;
         if (☃) {
            this.savedServerList.addServerData(this.selectedServer);
            this.savedServerList.saveServerList();
            this.serverListSelector.setSelectedSlotIndex(-1);
            this.serverListSelector.updateOnlineServers(this.savedServerList);
         }

         this.mc.displayGuiScreen(this);
      } else if (this.editingServer) {
         this.editingServer = false;
         if (☃ && ☃ instanceof ServerListEntryNormal) {
            ServerData ☃x = ((ServerListEntryNormal)☃).getServerData();
            ☃x.serverName = this.selectedServer.serverName;
            ☃x.serverIP = this.selectedServer.serverIP;
            ☃x.copyFrom(this.selectedServer);
            this.savedServerList.saveServerList();
            this.serverListSelector.updateOnlineServers(this.savedServerList);
         }

         this.mc.displayGuiScreen(this);
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      int ☃ = this.serverListSelector.getSelected();
      GuiListExtended.IGuiListEntry ☃x = ☃ < 0 ? null : this.serverListSelector.getListEntry(☃);
      if (☃ == 63) {
         this.refreshServerList();
      } else {
         if (☃ >= 0) {
            if (☃ == 200) {
               if (isShiftKeyDown()) {
                  if (☃ > 0 && ☃x instanceof ServerListEntryNormal) {
                     this.savedServerList.swapServers(☃, ☃ - 1);
                     this.selectServer(this.serverListSelector.getSelected() - 1);
                     this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                     this.serverListSelector.updateOnlineServers(this.savedServerList);
                  }
               } else if (☃ > 0) {
                  this.selectServer(this.serverListSelector.getSelected() - 1);
                  this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                  if (this.serverListSelector.getListEntry(this.serverListSelector.getSelected()) instanceof ServerListEntryLanScan) {
                     if (this.serverListSelector.getSelected() > 0) {
                        this.selectServer(this.serverListSelector.getSize() - 1);
                        this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                     } else {
                        this.selectServer(-1);
                     }
                  }
               } else {
                  this.selectServer(-1);
               }
            } else if (☃ == 208) {
               if (isShiftKeyDown()) {
                  if (☃ < this.savedServerList.countServers() - 1) {
                     this.savedServerList.swapServers(☃, ☃ + 1);
                     this.selectServer(☃ + 1);
                     this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                     this.serverListSelector.updateOnlineServers(this.savedServerList);
                  }
               } else if (☃ < this.serverListSelector.getSize()) {
                  this.selectServer(this.serverListSelector.getSelected() + 1);
                  this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                  if (this.serverListSelector.getListEntry(this.serverListSelector.getSelected()) instanceof ServerListEntryLanScan) {
                     if (this.serverListSelector.getSelected() < this.serverListSelector.getSize() - 1) {
                        this.selectServer(this.serverListSelector.getSize() + 1);
                        this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                     } else {
                        this.selectServer(-1);
                     }
                  }
               } else {
                  this.selectServer(-1);
               }
            } else if (☃ != 28 && ☃ != 156) {
               super.keyTyped(☃, ☃);
            } else {
               this.actionPerformed(this.buttonList.get(2));
            }
         } else {
            super.keyTyped(☃, ☃);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.hoveringText = null;
      this.drawDefaultBackground();
      this.serverListSelector.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, I18n.format("multiplayer.title"), this.width / 2, 20, 16777215);
      super.drawScreen(☃, ☃, ☃);
      if (this.hoveringText != null) {
         this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), ☃, ☃);
      }
   }

   public void connectToSelected() {
      GuiListExtended.IGuiListEntry ☃ = this.serverListSelector.getSelected() < 0
         ? null
         : this.serverListSelector.getListEntry(this.serverListSelector.getSelected());
      if (☃ instanceof ServerListEntryNormal) {
         this.connectToServer(((ServerListEntryNormal)☃).getServerData());
      } else if (☃ instanceof ServerListEntryLanDetected) {
         LanServerInfo ☃x = ((ServerListEntryLanDetected)☃).getServerData();
         this.connectToServer(new ServerData(☃x.getServerMotd(), ☃x.getServerIpPort(), true));
      }
   }

   private void connectToServer(ServerData var1) {
      this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, ☃));
   }

   public void selectServer(int var1) {
      this.serverListSelector.setSelectedSlotIndex(☃);
      GuiListExtended.IGuiListEntry ☃ = ☃ < 0 ? null : this.serverListSelector.getListEntry(☃);
      this.btnSelectServer.enabled = false;
      this.btnEditServer.enabled = false;
      this.btnDeleteServer.enabled = false;
      if (☃ != null && !(☃ instanceof ServerListEntryLanScan)) {
         this.btnSelectServer.enabled = true;
         if (☃ instanceof ServerListEntryNormal) {
            this.btnEditServer.enabled = true;
            this.btnDeleteServer.enabled = true;
         }
      }
   }

   public ServerPinger getOldServerPinger() {
      return this.oldServerPinger;
   }

   public void setHoveringText(String var1) {
      this.hoveringText = ☃;
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      this.serverListSelector.mouseClicked(☃, ☃, ☃);
   }

   @Override
   protected void mouseReleased(int var1, int var2, int var3) {
      super.mouseReleased(☃, ☃, ☃);
      this.serverListSelector.mouseReleased(☃, ☃, ☃);
   }

   public ServerList getServerList() {
      return this.savedServerList;
   }

   public boolean canMoveUp(ServerListEntryNormal var1, int var2) {
      return ☃ > 0;
   }

   public boolean canMoveDown(ServerListEntryNormal var1, int var2) {
      return ☃ < this.savedServerList.countServers() - 1;
   }

   public void moveServerUp(ServerListEntryNormal var1, int var2, boolean var3) {
      int ☃ = ☃ ? 0 : ☃ - 1;
      this.savedServerList.swapServers(☃, ☃);
      if (this.serverListSelector.getSelected() == ☃) {
         this.selectServer(☃);
      }

      this.serverListSelector.updateOnlineServers(this.savedServerList);
   }

   public void moveServerDown(ServerListEntryNormal var1, int var2, boolean var3) {
      int ☃ = ☃ ? this.savedServerList.countServers() - 1 : ☃ + 1;
      this.savedServerList.swapServers(☃, ☃);
      if (this.serverListSelector.getSelected() == ☃) {
         this.selectServer(☃);
      }

      this.serverListSelector.updateOnlineServers(this.savedServerList);
   }
}
