package net.minecraft.world.storage;

import javax.annotation.Nullable;

public class SaveDataMemoryStorage extends MapStorage {
   public SaveDataMemoryStorage() {
      super(null);
   }

   @Nullable
   @Override
   public WorldSavedData getOrLoadData(Class<? extends WorldSavedData> var1, String var2) {
      return this.loadedDataMap.get(☃);
   }

   @Override
   public void setData(String var1, WorldSavedData var2) {
      this.loadedDataMap.put(☃, ☃);
   }

   @Override
   public void saveAllData() {
   }

   @Override
   public int getUniqueDataId(String var1) {
      return 0;
   }
}
