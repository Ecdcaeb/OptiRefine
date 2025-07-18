package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class SkeletonSplit implements IFixableData {
   @Override
   public int getFixVersion() {
      return 701;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      String ☃ = ☃.getString("id");
      if ("Skeleton".equals(☃)) {
         int ☃x = ☃.getInteger("SkeletonType");
         if (☃x == 1) {
            ☃.setString("id", "WitherSkeleton");
         } else if (☃x == 2) {
            ☃.setString("id", "Stray");
         }

         ☃.removeTag("SkeletonType");
      }

      return ☃;
   }
}
