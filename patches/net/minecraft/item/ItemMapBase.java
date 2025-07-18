package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class ItemMapBase extends Item {
   protected ItemMapBase() {
   }

   @Override
   public boolean isMap() {
      return true;
   }

   @Nullable
   public Packet<?> createMapDataPacket(ItemStack var1, World var2, EntityPlayer var3) {
      return null;
   }
}
