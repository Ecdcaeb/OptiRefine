package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ArmorStandSilent implements IFixableData {
   @Override
   public int getFixVersion() {
      return 147;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("ArmorStand".equals(☃.getString("id")) && ☃.getBoolean("Silent") && !☃.getBoolean("Marker")) {
         ☃.removeTag("Silent");
      }

      return ☃;
   }
}
