package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class RedundantChanceTags implements IFixableData {
   @Override
   public int getFixVersion() {
      return 113;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if (☃.hasKey("HandDropChances", 9)) {
         NBTTagList ☃ = ☃.getTagList("HandDropChances", 5);
         if (☃.tagCount() == 2 && ☃.getFloatAt(0) == 0.0F && ☃.getFloatAt(1) == 0.0F) {
            ☃.removeTag("HandDropChances");
         }
      }

      if (☃.hasKey("ArmorDropChances", 9)) {
         NBTTagList ☃ = ☃.getTagList("ArmorDropChances", 5);
         if (☃.tagCount() == 4 && ☃.getFloatAt(0) == 0.0F && ☃.getFloatAt(1) == 0.0F && ☃.getFloatAt(2) == 0.0F && ☃.getFloatAt(3) == 0.0F) {
            ☃.removeTag("ArmorDropChances");
         }
      }

      return ☃;
   }
}
