package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxEntityColor implements IFixableData {
   @Override
   public int getFixVersion() {
      return 808;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("minecraft:shulker".equals(☃.getString("id")) && !☃.hasKey("Color", 99)) {
         ☃.setByte("Color", (byte)10);
      }

      return ☃;
   }
}
