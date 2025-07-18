package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;

public class MapStorage {
   private final ISaveHandler saveHandler;
   protected Map<String, WorldSavedData> loadedDataMap = Maps.newHashMap();
   private final List<WorldSavedData> loadedDataList = Lists.newArrayList();
   private final Map<String, Short> idCounts = Maps.newHashMap();

   public MapStorage(ISaveHandler var1) {
      this.saveHandler = ☃;
      this.loadIdCounts();
   }

   @Nullable
   public WorldSavedData getOrLoadData(Class<? extends WorldSavedData> var1, String var2) {
      WorldSavedData ☃ = this.loadedDataMap.get(☃);
      if (☃ != null) {
         return ☃;
      } else {
         if (this.saveHandler != null) {
            try {
               File ☃x = this.saveHandler.getMapFileFromName(☃);
               if (☃x != null && ☃x.exists()) {
                  try {
                     ☃ = ☃.getConstructor(String.class).newInstance(☃);
                  } catch (Exception var7) {
                     throw new RuntimeException("Failed to instantiate " + ☃, var7);
                  }

                  FileInputStream ☃xx = new FileInputStream(☃x);
                  NBTTagCompound ☃xxx = CompressedStreamTools.readCompressed(☃xx);
                  ☃xx.close();
                  ☃.readFromNBT(☃xxx.getCompoundTag("data"));
               }
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }

         if (☃ != null) {
            this.loadedDataMap.put(☃, ☃);
            this.loadedDataList.add(☃);
         }

         return ☃;
      }
   }

   public void setData(String var1, WorldSavedData var2) {
      if (this.loadedDataMap.containsKey(☃)) {
         this.loadedDataList.remove(this.loadedDataMap.remove(☃));
      }

      this.loadedDataMap.put(☃, ☃);
      this.loadedDataList.add(☃);
   }

   public void saveAllData() {
      for (int ☃ = 0; ☃ < this.loadedDataList.size(); ☃++) {
         WorldSavedData ☃x = this.loadedDataList.get(☃);
         if (☃x.isDirty()) {
            this.saveData(☃x);
            ☃x.setDirty(false);
         }
      }
   }

   private void saveData(WorldSavedData var1) {
      if (this.saveHandler != null) {
         try {
            File ☃ = this.saveHandler.getMapFileFromName(☃.mapName);
            if (☃ != null) {
               NBTTagCompound ☃x = new NBTTagCompound();
               ☃x.setTag("data", ☃.writeToNBT(new NBTTagCompound()));
               FileOutputStream ☃xx = new FileOutputStream(☃);
               CompressedStreamTools.writeCompressed(☃x, ☃xx);
               ☃xx.close();
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }
   }

   private void loadIdCounts() {
      try {
         this.idCounts.clear();
         if (this.saveHandler == null) {
            return;
         }

         File ☃ = this.saveHandler.getMapFileFromName("idcounts");
         if (☃ != null && ☃.exists()) {
            DataInputStream ☃x = new DataInputStream(new FileInputStream(☃));
            NBTTagCompound ☃xx = CompressedStreamTools.read(☃x);
            ☃x.close();

            for (String ☃xxx : ☃xx.getKeySet()) {
               NBTBase ☃xxxx = ☃xx.getTag(☃xxx);
               if (☃xxxx instanceof NBTTagShort) {
                  NBTTagShort ☃xxxxx = (NBTTagShort)☃xxxx;
                  short ☃xxxxxx = ☃xxxxx.getShort();
                  this.idCounts.put(☃xxx, ☃xxxxxx);
               }
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }
   }

   public int getUniqueDataId(String var1) {
      Short ☃ = this.idCounts.get(☃);
      if (☃ == null) {
         ☃ = (short)0;
      } else {
         ☃ = (short)(☃ + 1);
      }

      this.idCounts.put(☃, ☃);
      if (this.saveHandler == null) {
         return ☃;
      } else {
         try {
            File ☃x = this.saveHandler.getMapFileFromName("idcounts");
            if (☃x != null) {
               NBTTagCompound ☃xx = new NBTTagCompound();

               for (String ☃xxx : this.idCounts.keySet()) {
                  ☃xx.setShort(☃xxx, this.idCounts.get(☃xxx));
               }

               DataOutputStream ☃xxx = new DataOutputStream(new FileOutputStream(☃x));
               CompressedStreamTools.write(☃xx, ☃xxx);
               ☃xxx.close();
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         return ☃;
      }
   }
}
