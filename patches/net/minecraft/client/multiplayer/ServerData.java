package net.minecraft.client.multiplayer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class ServerData {
   public String serverName;
   public String serverIP;
   public String populationInfo;
   public String serverMOTD;
   public long pingToServer;
   public int version = 340;
   public String gameVersion = "1.12.2";
   public boolean pinged;
   public String playerList;
   private ServerData.ServerResourceMode resourceMode = ServerData.ServerResourceMode.PROMPT;
   private String serverIcon;
   private boolean lanServer;

   public ServerData(String var1, String var2, boolean var3) {
      this.serverName = ☃;
      this.serverIP = ☃;
      this.lanServer = ☃;
   }

   public NBTTagCompound getNBTCompound() {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setString("name", this.serverName);
      ☃.setString("ip", this.serverIP);
      if (this.serverIcon != null) {
         ☃.setString("icon", this.serverIcon);
      }

      if (this.resourceMode == ServerData.ServerResourceMode.ENABLED) {
         ☃.setBoolean("acceptTextures", true);
      } else if (this.resourceMode == ServerData.ServerResourceMode.DISABLED) {
         ☃.setBoolean("acceptTextures", false);
      }

      return ☃;
   }

   public ServerData.ServerResourceMode getResourceMode() {
      return this.resourceMode;
   }

   public void setResourceMode(ServerData.ServerResourceMode var1) {
      this.resourceMode = ☃;
   }

   public static ServerData getServerDataFromNBTCompound(NBTTagCompound var0) {
      ServerData ☃ = new ServerData(☃.getString("name"), ☃.getString("ip"), false);
      if (☃.hasKey("icon", 8)) {
         ☃.setBase64EncodedIconData(☃.getString("icon"));
      }

      if (☃.hasKey("acceptTextures", 1)) {
         if (☃.getBoolean("acceptTextures")) {
            ☃.setResourceMode(ServerData.ServerResourceMode.ENABLED);
         } else {
            ☃.setResourceMode(ServerData.ServerResourceMode.DISABLED);
         }
      } else {
         ☃.setResourceMode(ServerData.ServerResourceMode.PROMPT);
      }

      return ☃;
   }

   public String getBase64EncodedIconData() {
      return this.serverIcon;
   }

   public void setBase64EncodedIconData(String var1) {
      this.serverIcon = ☃;
   }

   public boolean isOnLAN() {
      return this.lanServer;
   }

   public void copyFrom(ServerData var1) {
      this.serverIP = ☃.serverIP;
      this.serverName = ☃.serverName;
      this.setResourceMode(☃.getResourceMode());
      this.serverIcon = ☃.serverIcon;
      this.lanServer = ☃.lanServer;
   }

   public static enum ServerResourceMode {
      ENABLED("enabled"),
      DISABLED("disabled"),
      PROMPT("prompt");

      private final ITextComponent motd;

      private ServerResourceMode(String var3) {
         this.motd = new TextComponentTranslation("addServer.resourcePack." + ☃);
      }

      public ITextComponent getMotd() {
         return this.motd;
      }
   }
}
