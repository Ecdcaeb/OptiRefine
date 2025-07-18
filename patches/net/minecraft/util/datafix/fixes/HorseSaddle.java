package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSaddle implements IFixableData {
   @Override
   public int getFixVersion() {
      return 110;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("EntityHorse".equals(☃.getString("id")) && !☃.hasKey("SaddleItem", 10) && ☃.getBoolean("Saddle")) {
         NBTTagCompound ☃ = new NBTTagCompound();
         ☃.setString("id", "minecraft:saddle");
         ☃.setByte("Count", (byte)1);
         ☃.setShort("Damage", (short)0);
         ☃.setTag("SaddleItem", ☃);
         ☃.removeTag("Saddle");
      }

      return ☃;
   }
}
