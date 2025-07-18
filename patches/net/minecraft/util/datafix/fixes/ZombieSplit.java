package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ZombieSplit implements IFixableData {
   @Override
   public int getFixVersion() {
      return 702;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("Zombie".equals(☃.getString("id"))) {
         int ☃ = ☃.getInteger("ZombieType");
         switch (☃) {
            case 0:
            default:
               break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
               ☃.setString("id", "ZombieVillager");
               ☃.setInteger("Profession", ☃ - 1);
               break;
            case 6:
               ☃.setString("id", "Husk");
         }

         ☃.removeTag("ZombieType");
      }

      return ☃;
   }
}
