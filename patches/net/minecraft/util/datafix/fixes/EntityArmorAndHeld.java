package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class EntityArmorAndHeld implements IFixableData {
   @Override
   public int getFixVersion() {
      return 100;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      NBTTagList ☃ = ☃.getTagList("Equipment", 10);
      if (!☃.isEmpty() && !☃.hasKey("HandItems", 10)) {
         NBTTagList ☃x = new NBTTagList();
         ☃x.appendTag(☃.get(0));
         ☃x.appendTag(new NBTTagCompound());
         ☃.setTag("HandItems", ☃x);
      }

      if (☃.tagCount() > 1 && !☃.hasKey("ArmorItem", 10)) {
         NBTTagList ☃x = new NBTTagList();
         ☃x.appendTag(☃.getCompoundTagAt(1));
         ☃x.appendTag(☃.getCompoundTagAt(2));
         ☃x.appendTag(☃.getCompoundTagAt(3));
         ☃x.appendTag(☃.getCompoundTagAt(4));
         ☃.setTag("ArmorItems", ☃x);
      }

      ☃.removeTag("Equipment");
      if (☃.hasKey("DropChances", 9)) {
         NBTTagList ☃x = ☃.getTagList("DropChances", 5);
         if (!☃.hasKey("HandDropChances", 10)) {
            NBTTagList ☃xx = new NBTTagList();
            ☃xx.appendTag(new NBTTagFloat(☃x.getFloatAt(0)));
            ☃xx.appendTag(new NBTTagFloat(0.0F));
            ☃.setTag("HandDropChances", ☃xx);
         }

         if (!☃.hasKey("ArmorDropChances", 10)) {
            NBTTagList ☃xx = new NBTTagList();
            ☃xx.appendTag(new NBTTagFloat(☃x.getFloatAt(1)));
            ☃xx.appendTag(new NBTTagFloat(☃x.getFloatAt(2)));
            ☃xx.appendTag(new NBTTagFloat(☃x.getFloatAt(3)));
            ☃xx.appendTag(new NBTTagFloat(☃x.getFloatAt(4)));
            ☃.setTag("ArmorDropChances", ☃xx);
         }

         ☃.removeTag("DropChances");
      }

      return ☃;
   }
}
