package net.minecraft.util.datafix.fixes;

import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ZombieProfToType implements IFixableData {
   private static final Random RANDOM = new Random();

   @Override
   public int getFixVersion() {
      return 502;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("Zombie".equals(☃.getString("id")) && ☃.getBoolean("IsVillager")) {
         if (!☃.hasKey("ZombieType", 99)) {
            int ☃ = -1;
            if (☃.hasKey("VillagerProfession", 99)) {
               try {
                  ☃ = this.getVillagerProfession(☃.getInteger("VillagerProfession"));
               } catch (RuntimeException var4) {
               }
            }

            if (☃ == -1) {
               ☃ = this.getVillagerProfession(RANDOM.nextInt(6));
            }

            ☃.setInteger("ZombieType", ☃);
         }

         ☃.removeTag("IsVillager");
      }

      return ☃;
   }

   private int getVillagerProfession(int var1) {
      return ☃ >= 0 && ☃ < 6 ? ☃ : -1;
   }
}
