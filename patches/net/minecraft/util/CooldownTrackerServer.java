package net.minecraft.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketCooldown;

public class CooldownTrackerServer extends CooldownTracker {
   private final EntityPlayerMP player;

   public CooldownTrackerServer(EntityPlayerMP var1) {
      this.player = ☃;
   }

   @Override
   protected void notifyOnSet(Item var1, int var2) {
      super.notifyOnSet(☃, ☃);
      this.player.connection.sendPacket(new SPacketCooldown(☃, ☃));
   }

   @Override
   protected void notifyOnRemove(Item var1) {
      super.notifyOnRemove(☃);
      this.player.connection.sendPacket(new SPacketCooldown(☃, 0));
   }
}
