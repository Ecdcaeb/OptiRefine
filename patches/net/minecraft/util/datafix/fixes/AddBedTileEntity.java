package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddBedTileEntity implements IFixableData {
   private static final Logger LOGGER = LogManager.getLogger();

   @Override
   public int getFixVersion() {
      return 1125;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      int ☃ = 416;

      try {
         NBTTagCompound ☃x = ☃.getCompoundTag("Level");
         int ☃xx = ☃x.getInteger("xPos");
         int ☃xxx = ☃x.getInteger("zPos");
         NBTTagList ☃xxxx = ☃x.getTagList("TileEntities", 10);
         NBTTagList ☃xxxxx = ☃x.getTagList("Sections", 10);

         for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxxx.tagCount(); ☃xxxxxx++) {
            NBTTagCompound ☃xxxxxxx = ☃xxxxx.getCompoundTagAt(☃xxxxxx);
            int ☃xxxxxxxx = ☃xxxxxxx.getByte("Y");
            byte[] ☃xxxxxxxxx = ☃xxxxxxx.getByteArray("Blocks");

            for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < ☃xxxxxxxxx.length; ☃xxxxxxxxxx++) {
               if (416 == (☃xxxxxxxxx[☃xxxxxxxxxx] & 255) << 4) {
                  int ☃xxxxxxxxxxx = ☃xxxxxxxxxx & 15;
                  int ☃xxxxxxxxxxxx = ☃xxxxxxxxxx >> 8 & 15;
                  int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx >> 4 & 15;
                  NBTTagCompound ☃xxxxxxxxxxxxxx = new NBTTagCompound();
                  ☃xxxxxxxxxxxxxx.setString("id", "bed");
                  ☃xxxxxxxxxxxxxx.setInteger("x", ☃xxxxxxxxxxx + (☃xx << 4));
                  ☃xxxxxxxxxxxxxx.setInteger("y", ☃xxxxxxxxxxxx + (☃xxxxxxxx << 4));
                  ☃xxxxxxxxxxxxxx.setInteger("z", ☃xxxxxxxxxxxxx + (☃xxx << 4));
                  ☃xxxx.appendTag(☃xxxxxxxxxxxxxx);
               }
            }
         }
      } catch (Exception var17) {
         LOGGER.warn("Unable to datafix Bed blocks, level format may be missing tags.");
      }

      return ☃;
   }
}
