package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData {
   public final String mapName;
   private boolean dirty;

   public WorldSavedData(String var1) {
      this.mapName = ☃;
   }

   public abstract void readFromNBT(NBTTagCompound var1);

   public abstract NBTTagCompound writeToNBT(NBTTagCompound var1);

   public void markDirty() {
      this.setDirty(true);
   }

   public void setDirty(boolean var1) {
      this.dirty = ☃;
   }

   public boolean isDirty() {
      return this.dirty;
   }
}
