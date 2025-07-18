package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class RidingToPassengers implements IFixableData {
   @Override
   public int getFixVersion() {
      return 135;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      while (☃.hasKey("Riding", 10)) {
         NBTTagCompound ☃ = this.extractVehicle(☃);
         this.addPassengerToVehicle(☃, ☃);
         ☃ = ☃;
      }

      return ☃;
   }

   protected void addPassengerToVehicle(NBTTagCompound var1, NBTTagCompound var2) {
      NBTTagList ☃ = new NBTTagList();
      ☃.appendTag(☃);
      ☃.setTag("Passengers", ☃);
   }

   protected NBTTagCompound extractVehicle(NBTTagCompound var1) {
      NBTTagCompound ☃ = ☃.getCompoundTag("Riding");
      ☃.removeTag("Riding");
      return ☃;
   }
}
