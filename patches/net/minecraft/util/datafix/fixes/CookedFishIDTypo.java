package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;

public class CookedFishIDTypo implements IFixableData {
   private static final ResourceLocation WRONG = new ResourceLocation("cooked_fished");

   @Override
   public int getFixVersion() {
      return 502;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if (☃.hasKey("id", 8) && WRONG.equals(new ResourceLocation(☃.getString("id")))) {
         ☃.setString("id", "minecraft:cooked_fish");
      }

      return ☃;
   }
}
