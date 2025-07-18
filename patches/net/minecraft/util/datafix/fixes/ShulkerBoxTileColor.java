package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxTileColor implements IFixableData {
   @Override
   public int getFixVersion() {
      return 813;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("minecraft:shulker".equals(☃.getString("id"))) {
         ☃.removeTag("Color");
      }

      return ☃;
   }
}
