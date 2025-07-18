package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.IFixableData;

public class PaintingDirection implements IFixableData {
   @Override
   public int getFixVersion() {
      return 111;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      String ☃ = ☃.getString("id");
      boolean ☃x = "Painting".equals(☃);
      boolean ☃xx = "ItemFrame".equals(☃);
      if ((☃x || ☃xx) && !☃.hasKey("Facing", 99)) {
         EnumFacing ☃xxx;
         if (☃.hasKey("Direction", 99)) {
            ☃xxx = EnumFacing.byHorizontalIndex(☃.getByte("Direction"));
            ☃.setInteger("TileX", ☃.getInteger("TileX") + ☃xxx.getXOffset());
            ☃.setInteger("TileY", ☃.getInteger("TileY") + ☃xxx.getYOffset());
            ☃.setInteger("TileZ", ☃.getInteger("TileZ") + ☃xxx.getZOffset());
            ☃.removeTag("Direction");
            if (☃xx && ☃.hasKey("ItemRotation", 99)) {
               ☃.setByte("ItemRotation", (byte)(☃.getByte("ItemRotation") * 2));
            }
         } else {
            ☃xxx = EnumFacing.byHorizontalIndex(☃.getByte("Dir"));
            ☃.removeTag("Dir");
         }

         ☃.setByte("Facing", (byte)☃xxx.getHorizontalIndex());
      }

      return ☃;
   }
}
