package net.minecraft.util.datafix.fixes;

import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class StringToUUID implements IFixableData {
   @Override
   public int getFixVersion() {
      return 108;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if (☃.hasKey("UUID", 8)) {
         ☃.setUniqueId("UUID", UUID.fromString(☃.getString("UUID")));
      }

      return ☃;
   }
}
