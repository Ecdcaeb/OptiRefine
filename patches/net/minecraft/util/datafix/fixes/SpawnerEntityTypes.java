package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class SpawnerEntityTypes implements IFixableData {
   @Override
   public int getFixVersion() {
      return 107;
   }

   @Override
   public NBTTagCompound fixTagCompound(NBTTagCompound var1) {
      if (!"MobSpawner".equals(☃.getString("id"))) {
         return ☃;
      } else {
         if (☃.hasKey("EntityId", 8)) {
            String ☃ = ☃.getString("EntityId");
            NBTTagCompound ☃x = ☃.getCompoundTag("SpawnData");
            ☃x.setString("id", ☃.isEmpty() ? "Pig" : ☃);
            ☃.setTag("SpawnData", ☃x);
            ☃.removeTag("EntityId");
         }

         if (☃.hasKey("SpawnPotentials", 9)) {
            NBTTagList ☃ = ☃.getTagList("SpawnPotentials", 10);

            for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
               NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
               if (☃xx.hasKey("Type", 8)) {
                  NBTTagCompound ☃xxx = ☃xx.getCompoundTag("Properties");
                  ☃xxx.setString("id", ☃xx.getString("Type"));
                  ☃xx.setTag("Entity", ☃xxx);
                  ☃xx.removeTag("Type");
                  ☃xx.removeTag("Properties");
               }
            }
         }

         return ☃;
      }
   }
}
