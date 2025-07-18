package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSplit implements IFixableData {
   @Override
   public int getFixVersion() {
      return 703;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("EntityHorse".equals(☃.getString("id"))) {
         int ☃ = ☃.getInteger("Type");
         switch (☃) {
            case 0:
            default:
               ☃.setString("id", "Horse");
               break;
            case 1:
               ☃.setString("id", "Donkey");
               break;
            case 2:
               ☃.setString("id", "Mule");
               break;
            case 3:
               ☃.setString("id", "ZombieHorse");
               break;
            case 4:
               ☃.setString("id", "SkeletonHorse");
         }

         ☃.removeTag("Type");
      }

      return ☃;
   }
}
