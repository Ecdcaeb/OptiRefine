package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerInfo;

public class ServerSelectionList extends GuiListExtended {
   private final GuiMultiplayer owner;
   private final List<ServerListEntryNormal> serverListInternet = Lists.newArrayList();
   private final List<ServerListEntryLanDetected> serverListLan = Lists.newArrayList();
   private final GuiListExtended.IGuiListEntry lanScanEntry = new ServerListEntryLanScan();
   private int selectedSlotIndex = -1;

   public ServerSelectionList(GuiMultiplayer var1, Minecraft var2, int var3, int var4, int var5, int var6, int var7) {
      super(☃, ☃, ☃, ☃, ☃, ☃);
      this.owner = ☃;
   }

   @Override
   public GuiListExtended.IGuiListEntry getListEntry(int var1) {
      if (☃ < this.serverListInternet.size()) {
         return this.serverListInternet.get(☃);
      } else {
         ☃ -= this.serverListInternet.size();
         return ☃ == 0 ? this.lanScanEntry : this.serverListLan.get(--☃);
      }
   }

   @Override
   protected int getSize() {
      return this.serverListInternet.size() + 1 + this.serverListLan.size();
   }

   public void setSelectedSlotIndex(int var1) {
      this.selectedSlotIndex = ☃;
   }

   @Override
   protected boolean isSelected(int var1) {
      return ☃ == this.selectedSlotIndex;
   }

   public int getSelected() {
      return this.selectedSlotIndex;
   }

   public void updateOnlineServers(ServerList var1) {
      this.serverListInternet.clear();

      for (int ☃ = 0; ☃ < ☃.countServers(); ☃++) {
         this.serverListInternet.add(new ServerListEntryNormal(this.owner, ☃.getServerData(☃)));
      }
   }

   public void updateNetworkServers(List<LanServerInfo> var1) {
      this.serverListLan.clear();

      for (LanServerInfo ☃ : ☃) {
         this.serverListLan.add(new ServerListEntryLanDetected(this.owner, ☃));
      }
   }

   @Override
   protected int getScrollBarX() {
      return super.getScrollBarX() + 30;
   }

   @Override
   public int getListWidth() {
      return super.getListWidth() + 85;
   }
}
