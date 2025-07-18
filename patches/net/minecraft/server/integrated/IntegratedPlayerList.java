package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerList;

public class IntegratedPlayerList extends PlayerList {
   private NBTTagCompound hostPlayerData;

   public IntegratedPlayerList(IntegratedServer var1) {
      super(☃);
      this.setViewDistance(10);
   }

   @Override
   protected void writePlayerData(EntityPlayerMP var1) {
      if (☃.getName().equals(this.getServerInstance().getServerOwner())) {
         this.hostPlayerData = ☃.writeToNBT(new NBTTagCompound());
      }

      super.writePlayerData(☃);
   }

   @Override
   public String allowUserToConnect(SocketAddress var1, GameProfile var2) {
      return ☃.getName().equalsIgnoreCase(this.getServerInstance().getServerOwner()) && this.getPlayerByUsername(☃.getName()) != null
         ? "That name is already taken."
         : super.allowUserToConnect(☃, ☃);
   }

   public IntegratedServer getServerInstance() {
      return (IntegratedServer)super.getServerInstance();
   }

   @Override
   public NBTTagCompound getHostPlayerData() {
      return this.hostPlayerData;
   }
}
