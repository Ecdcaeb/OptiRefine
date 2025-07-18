package net.minecraft.world.storage;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerFileData {
   void writePlayerData(EntityPlayer var1);

   @Nullable
   NBTTagCompound readPlayerData(EntityPlayer var1);

   String[] getAvailablePlayerDat();
}
