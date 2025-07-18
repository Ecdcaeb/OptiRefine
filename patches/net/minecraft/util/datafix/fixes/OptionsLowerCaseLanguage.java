package net.minecraft.util.datafix.fixes;

import java.util.Locale;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class OptionsLowerCaseLanguage implements IFixableData {
   @Override
   public int getFixVersion() {
      return 816;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if (☃.hasKey("lang", 8)) {
         ☃.setString("lang", ☃.getString("lang").toLowerCase(Locale.ROOT));
      }

      return ☃;
   }
}
