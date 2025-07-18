package net.minecraft.util.datafix.fixes;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class BedItemColor implements IFixableData {
   @Override
   public int getFixVersion() {
      return 1125;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("minecraft:bed".equals(☃.getString("id")) && ☃.getShort("Damage") == 0) {
         ☃.setShort("Damage", (short)EnumDyeColor.RED.getMetadata());
      }

      return ☃;
   }
}
