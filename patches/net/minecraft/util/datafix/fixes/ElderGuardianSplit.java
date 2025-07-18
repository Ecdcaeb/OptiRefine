package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ElderGuardianSplit implements IFixableData {
   @Override
   public int getFixVersion() {
      return 700;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("Guardian".equals(☃.getString("id"))) {
         if (☃.getBoolean("Elder")) {
            ☃.setString("id", "ElderGuardian");
         }

         ☃.removeTag("Elder");
      }

      return ☃;
   }
}
