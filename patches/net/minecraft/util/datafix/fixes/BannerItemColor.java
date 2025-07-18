package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class BannerItemColor implements IFixableData {
   @Override
   public int getFixVersion() {
      return 804;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if ("minecraft:banner".equals(☃.getString("id")) && ☃.hasKey("tag", 10)) {
         NBTTagCompound ☃ = ☃.getCompoundTag("tag");
         if (☃.hasKey("BlockEntityTag", 10)) {
            NBTTagCompound ☃x = ☃.getCompoundTag("BlockEntityTag");
            if (☃x.hasKey("Base", 99)) {
               ☃.setShort("Damage", (short)(☃x.getShort("Base") & 15));
               if (☃.hasKey("display", 10)) {
                  NBTTagCompound ☃xx = ☃.getCompoundTag("display");
                  if (☃xx.hasKey("Lore", 9)) {
                     NBTTagList ☃xxx = ☃xx.getTagList("Lore", 8);
                     if (☃xxx.tagCount() == 1 && "(+NBT)".equals(☃xxx.getStringTagAt(0))) {
                        return ☃;
                     }
                  }
               }

               ☃x.removeTag("Base");
               if (☃x.isEmpty()) {
                  ☃.removeTag("BlockEntityTag");
               }

               if (☃.isEmpty()) {
                  ☃.removeTag("tag");
               }
            }
         }
      }

      return ☃;
   }
}
