package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class PotionWater implements IFixableData {
   @Override
   public int getFixVersion() {
      return 806;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      String ☃ = ☃.getString("id");
      if ("minecraft:potion".equals(☃) || "minecraft:splash_potion".equals(☃) || "minecraft:lingering_potion".equals(☃) || "minecraft:tipped_arrow".equals(☃)) {
         NBTTagCompound ☃x = ☃.getCompoundTag("tag");
         if (!☃x.hasKey("Potion", 8)) {
            ☃x.setString("Potion", "minecraft:water");
         }

         if (!☃.hasKey("tag", 10)) {
            ☃.setTag("tag", ☃x);
         }
      }

      return ☃;
   }
}
