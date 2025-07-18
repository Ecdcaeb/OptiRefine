package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class RegionFileCache {
   private static final Map<File, RegionFile> REGIONS_BY_FILE = Maps.newHashMap();

   public static synchronized RegionFile createOrLoadRegionFile(File var0, int var1, int var2) {
      File ☃ = new File(☃, "region");
      File ☃x = new File(☃, "r." + (☃ >> 5) + "." + (☃ >> 5) + ".mca");
      RegionFile ☃xx = REGIONS_BY_FILE.get(☃x);
      if (☃xx != null) {
         return ☃xx;
      } else {
         if (!☃.exists()) {
            ☃.mkdirs();
         }

         if (REGIONS_BY_FILE.size() >= 256) {
            clearRegionFileReferences();
         }

         RegionFile ☃xxx = new RegionFile(☃x);
         REGIONS_BY_FILE.put(☃x, ☃xxx);
         return ☃xxx;
      }
   }

   public static synchronized RegionFile getRegionFileIfExists(File var0, int var1, int var2) {
      File ☃ = new File(☃, "region");
      File ☃x = new File(☃, "r." + (☃ >> 5) + "." + (☃ >> 5) + ".mca");
      RegionFile ☃xx = REGIONS_BY_FILE.get(☃x);
      if (☃xx != null) {
         return ☃xx;
      } else if (☃.exists() && ☃x.exists()) {
         if (REGIONS_BY_FILE.size() >= 256) {
            clearRegionFileReferences();
         }

         RegionFile ☃xxx = new RegionFile(☃x);
         REGIONS_BY_FILE.put(☃x, ☃xxx);
         return ☃xxx;
      } else {
         return null;
      }
   }

   public static synchronized void clearRegionFileReferences() {
      for (RegionFile ☃ : REGIONS_BY_FILE.values()) {
         try {
            if (☃ != null) {
               ☃.close();
            }
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      REGIONS_BY_FILE.clear();
   }

   public static DataInputStream getChunkInputStream(File var0, int var1, int var2) {
      RegionFile ☃ = createOrLoadRegionFile(☃, ☃, ☃);
      return ☃.getChunkDataInputStream(☃ & 31, ☃ & 31);
   }

   public static DataOutputStream getChunkOutputStream(File var0, int var1, int var2) {
      RegionFile ☃ = createOrLoadRegionFile(☃, ☃, ☃);
      return ☃.getChunkDataOutputStream(☃ & 31, ☃ & 31);
   }

   public static boolean chunkExists(File var0, int var1, int var2) {
      RegionFile ☃ = getRegionFileIfExists(☃, ☃, ☃);
      return ☃ != null ? ☃.isChunkSaved(☃ & 31, ☃ & 31) : false;
   }
}
