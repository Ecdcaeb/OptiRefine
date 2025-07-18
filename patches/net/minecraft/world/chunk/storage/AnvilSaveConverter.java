package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilSaveConverter extends SaveFormatOld {
   private static final Logger LOGGER = LogManager.getLogger();

   public AnvilSaveConverter(File var1, DataFixer var2) {
      super(☃, ☃);
   }

   @Override
   public String getName() {
      return "Anvil";
   }

   @Override
   public List<WorldSummary> getSaveList() throws AnvilConverterException {
      if (this.savesDirectory != null && this.savesDirectory.exists() && this.savesDirectory.isDirectory()) {
         List<WorldSummary> ☃ = Lists.newArrayList();
         File[] ☃x = this.savesDirectory.listFiles();

         for (File ☃xx : ☃x) {
            if (☃xx.isDirectory()) {
               String ☃xxx = ☃xx.getName();
               WorldInfo ☃xxxx = this.getWorldInfo(☃xxx);
               if (☃xxxx != null && (☃xxxx.getSaveVersion() == 19132 || ☃xxxx.getSaveVersion() == 19133)) {
                  boolean ☃xxxxx = ☃xxxx.getSaveVersion() != this.getSaveVersion();
                  String ☃xxxxxx = ☃xxxx.getWorldName();
                  if (StringUtils.isEmpty(☃xxxxxx)) {
                     ☃xxxxxx = ☃xxx;
                  }

                  long ☃xxxxxxx = 0L;
                  ☃.add(new WorldSummary(☃xxxx, ☃xxx, ☃xxxxxx, 0L, ☃xxxxx));
               }
            }
         }

         return ☃;
      } else {
         throw new AnvilConverterException(I18n.translateToLocal("selectWorld.load_folder_access"));
      }
   }

   protected int getSaveVersion() {
      return 19133;
   }

   @Override
   public void flushCache() {
      RegionFileCache.clearRegionFileReferences();
   }

   @Override
   public ISaveHandler getSaveLoader(String var1, boolean var2) {
      return new AnvilSaveHandler(this.savesDirectory, ☃, ☃, this.dataFixer);
   }

   @Override
   public boolean isConvertible(String var1) {
      WorldInfo ☃ = this.getWorldInfo(☃);
      return ☃ != null && ☃.getSaveVersion() == 19132;
   }

   @Override
   public boolean isOldMapFormat(String var1) {
      WorldInfo ☃ = this.getWorldInfo(☃);
      return ☃ != null && ☃.getSaveVersion() != this.getSaveVersion();
   }

   @Override
   public boolean convertMapFormat(String var1, IProgressUpdate var2) {
      ☃.setLoadingProgress(0);
      List<File> ☃ = Lists.newArrayList();
      List<File> ☃x = Lists.newArrayList();
      List<File> ☃xx = Lists.newArrayList();
      File ☃xxx = new File(this.savesDirectory, ☃);
      File ☃xxxx = new File(☃xxx, "DIM-1");
      File ☃xxxxx = new File(☃xxx, "DIM1");
      LOGGER.info("Scanning folders...");
      this.addRegionFilesToCollection(☃xxx, ☃);
      if (☃xxxx.exists()) {
         this.addRegionFilesToCollection(☃xxxx, ☃x);
      }

      if (☃xxxxx.exists()) {
         this.addRegionFilesToCollection(☃xxxxx, ☃xx);
      }

      int ☃xxxxxx = ☃.size() + ☃x.size() + ☃xx.size();
      LOGGER.info("Total conversion count is {}", ☃xxxxxx);
      WorldInfo ☃xxxxxxx = this.getWorldInfo(☃);
      BiomeProvider ☃xxxxxxxx;
      if (☃xxxxxxx != null && ☃xxxxxxx.getTerrainType() == WorldType.FLAT) {
         ☃xxxxxxxx = new BiomeProviderSingle(Biomes.PLAINS);
      } else {
         ☃xxxxxxxx = new BiomeProvider(☃xxxxxxx);
      }

      this.convertFile(new File(☃xxx, "region"), ☃, ☃xxxxxxxx, 0, ☃xxxxxx, ☃);
      this.convertFile(new File(☃xxxx, "region"), ☃x, new BiomeProviderSingle(Biomes.HELL), ☃.size(), ☃xxxxxx, ☃);
      this.convertFile(new File(☃xxxxx, "region"), ☃xx, new BiomeProviderSingle(Biomes.SKY), ☃.size() + ☃x.size(), ☃xxxxxx, ☃);
      ☃xxxxxxx.setSaveVersion(19133);
      if (☃xxxxxxx.getTerrainType() == WorldType.DEFAULT_1_1) {
         ☃xxxxxxx.setTerrainType(WorldType.DEFAULT);
      }

      this.createFile(☃);
      ISaveHandler ☃xxxxxxxxx = this.getSaveLoader(☃, false);
      ☃xxxxxxxxx.saveWorldInfo(☃xxxxxxx);
      return true;
   }

   private void createFile(String var1) {
      File ☃ = new File(this.savesDirectory, ☃);
      if (!☃.exists()) {
         LOGGER.warn("Unable to create level.dat_mcr backup");
      } else {
         File ☃x = new File(☃, "level.dat");
         if (!☃x.exists()) {
            LOGGER.warn("Unable to create level.dat_mcr backup");
         } else {
            File ☃xx = new File(☃, "level.dat_mcr");
            if (!☃x.renameTo(☃xx)) {
               LOGGER.warn("Unable to create level.dat_mcr backup");
            }
         }
      }
   }

   private void convertFile(File var1, Iterable<File> var2, BiomeProvider var3, int var4, int var5, IProgressUpdate var6) {
      for (File ☃ : ☃) {
         this.convertChunks(☃, ☃, ☃, ☃, ☃, ☃);
         ☃++;
         int ☃x = (int)Math.round(100.0 * ☃ / ☃);
         ☃.setLoadingProgress(☃x);
      }
   }

   private void convertChunks(File var1, File var2, BiomeProvider var3, int var4, int var5, IProgressUpdate var6) {
      try {
         String ☃ = ☃.getName();
         RegionFile ☃x = new RegionFile(☃);
         RegionFile ☃xx = new RegionFile(new File(☃, ☃.substring(0, ☃.length() - ".mcr".length()) + ".mca"));

         for (int ☃xxx = 0; ☃xxx < 32; ☃xxx++) {
            for (int ☃xxxx = 0; ☃xxxx < 32; ☃xxxx++) {
               if (☃x.isChunkSaved(☃xxx, ☃xxxx) && !☃xx.isChunkSaved(☃xxx, ☃xxxx)) {
                  DataInputStream ☃xxxxx = ☃x.getChunkDataInputStream(☃xxx, ☃xxxx);
                  if (☃xxxxx == null) {
                     LOGGER.warn("Failed to fetch input stream");
                  } else {
                     NBTTagCompound ☃xxxxxx = CompressedStreamTools.read(☃xxxxx);
                     ☃xxxxx.close();
                     NBTTagCompound ☃xxxxxxx = ☃xxxxxx.getCompoundTag("Level");
                     ChunkLoader.AnvilConverterData ☃xxxxxxxx = ChunkLoader.load(☃xxxxxxx);
                     NBTTagCompound ☃xxxxxxxxx = new NBTTagCompound();
                     NBTTagCompound ☃xxxxxxxxxx = new NBTTagCompound();
                     ☃xxxxxxxxx.setTag("Level", ☃xxxxxxxxxx);
                     ChunkLoader.convertToAnvilFormat(☃xxxxxxxx, ☃xxxxxxxxxx, ☃);
                     DataOutputStream ☃xxxxxxxxxxx = ☃xx.getChunkDataOutputStream(☃xxx, ☃xxxx);
                     CompressedStreamTools.write(☃xxxxxxxxx, ☃xxxxxxxxxxx);
                     ☃xxxxxxxxxxx.close();
                  }
               }
            }

            int ☃xxxxx = (int)Math.round(100.0 * (☃ * 1024) / (☃ * 1024));
            int ☃xxxxxx = (int)Math.round(100.0 * ((☃xxx + 1) * 32 + ☃ * 1024) / (☃ * 1024));
            if (☃xxxxxx > ☃xxxxx) {
               ☃.setLoadingProgress(☃xxxxxx);
            }
         }

         ☃x.close();
         ☃xx.close();
      } catch (IOException var19) {
         var19.printStackTrace();
      }
   }

   private void addRegionFilesToCollection(File var1, Collection<File> var2) {
      File ☃ = new File(☃, "region");
      File[] ☃x = ☃.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(File var1, String var2x) {
            return var2x.endsWith(".mcr");
         }
      });
      if (☃x != null) {
         Collections.addAll(☃, ☃x);
      }
   }
}
