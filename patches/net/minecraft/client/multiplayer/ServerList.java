package net.minecraft.client.multiplayer;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerList {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Minecraft mc;
   private final List<ServerData> servers = Lists.newArrayList();

   public ServerList(Minecraft var1) {
      this.mc = ☃;
      this.loadServerList();
   }

   public void loadServerList() {
      try {
         this.servers.clear();
         NBTTagCompound ☃ = CompressedStreamTools.read(new File(this.mc.gameDir, "servers.dat"));
         if (☃ == null) {
            return;
         }

         NBTTagList ☃x = ☃.getTagList("servers", 10);

         for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
            this.servers.add(ServerData.getServerDataFromNBTCompound(☃x.getCompoundTagAt(☃xx)));
         }
      } catch (Exception var4) {
         LOGGER.error("Couldn't load server list", var4);
      }
   }

   public void saveServerList() {
      try {
         NBTTagList ☃ = new NBTTagList();

         for (ServerData ☃x : this.servers) {
            ☃.appendTag(☃x.getNBTCompound());
         }

         NBTTagCompound ☃x = new NBTTagCompound();
         ☃x.setTag("servers", ☃);
         CompressedStreamTools.safeWrite(☃x, new File(this.mc.gameDir, "servers.dat"));
      } catch (Exception var4) {
         LOGGER.error("Couldn't save server list", var4);
      }
   }

   public ServerData getServerData(int var1) {
      return this.servers.get(☃);
   }

   public void removeServerData(int var1) {
      this.servers.remove(☃);
   }

   public void addServerData(ServerData var1) {
      this.servers.add(☃);
   }

   public int countServers() {
      return this.servers.size();
   }

   public void swapServers(int var1, int var2) {
      ServerData ☃ = this.getServerData(☃);
      this.servers.set(☃, this.getServerData(☃));
      this.servers.set(☃, ☃);
      this.saveServerList();
   }

   public void set(int var1, ServerData var2) {
      this.servers.set(☃, ☃);
   }

   public static void saveSingleServer(ServerData var0) {
      ServerList ☃ = new ServerList(Minecraft.getMinecraft());
      ☃.loadServerList();

      for (int ☃x = 0; ☃x < ☃.countServers(); ☃x++) {
         ServerData ☃xx = ☃.getServerData(☃x);
         if (☃xx.serverName.equals(☃.serverName) && ☃xx.serverIP.equals(☃.serverIP)) {
            ☃.set(☃x, ☃);
            break;
         }
      }

      ☃.saveServerList();
   }
}
