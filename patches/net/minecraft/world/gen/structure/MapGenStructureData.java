package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class MapGenStructureData extends WorldSavedData {
   private NBTTagCompound tagCompound = new NBTTagCompound();

   public MapGenStructureData(String var1) {
      super(☃);
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      this.tagCompound = ☃.getCompoundTag("Features");
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      ☃.setTag("Features", this.tagCompound);
      return ☃;
   }

   public void writeInstance(NBTTagCompound var1, int var2, int var3) {
      this.tagCompound.setTag(formatChunkCoords(☃, ☃), ☃);
   }

   public static String formatChunkCoords(int var0, int var1) {
      return "[" + ☃ + "," + ☃ + "]";
   }

   public NBTTagCompound getTagCompound() {
      return this.tagCompound;
   }
}
