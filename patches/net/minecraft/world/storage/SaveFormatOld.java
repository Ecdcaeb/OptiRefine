package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat {
   private static final Logger LOGGER = LogManager.getLogger();
   protected final File savesDirectory;
   protected final DataFixer dataFixer;

   public SaveFormatOld(File var1, DataFixer var2) {
      this.dataFixer = ☃;
      if (!☃.exists()) {
         ☃.mkdirs();
      }

      this.savesDirectory = ☃;
   }

   @Override
   public String getName() {
      return "Old Format";
   }

   @Override
   public List<WorldSummary> getSaveList() throws AnvilConverterException {
      List<WorldSummary> ☃ = Lists.newArrayList();

      for (int ☃x = 0; ☃x < 5; ☃x++) {
         String ☃xx = "World" + (☃x + 1);
         WorldInfo ☃xxx = this.getWorldInfo(☃xx);
         if (☃xxx != null) {
            ☃.add(new WorldSummary(☃xxx, ☃xx, "", ☃xxx.getSizeOnDisk(), false));
         }
      }

      return ☃;
   }

   @Override
   public void flushCache() {
   }

   @Nullable
   @Override
   public WorldInfo getWorldInfo(String var1) {
      File ☃ = new File(this.savesDirectory, ☃);
      if (!☃.exists()) {
         return null;
      } else {
         File ☃x = new File(☃, "level.dat");
         if (☃x.exists()) {
            WorldInfo ☃xx = getWorldData(☃x, this.dataFixer);
            if (☃xx != null) {
               return ☃xx;
            }
         }

         ☃x = new File(☃, "level.dat_old");
         return ☃x.exists() ? getWorldData(☃x, this.dataFixer) : null;
      }
   }

   @Nullable
   public static WorldInfo getWorldData(File var0, DataFixer var1) {
      try {
         NBTTagCompound ☃ = CompressedStreamTools.readCompressed(new FileInputStream(☃));
         NBTTagCompound ☃x = ☃.getCompoundTag("Data");
         return new WorldInfo(☃.process(FixTypes.LEVEL, ☃x));
      } catch (Exception var4) {
         LOGGER.error("Exception reading {}", ☃, var4);
         return null;
      }
   }

   @Override
   public void renameWorld(String var1, String var2) {
      File ☃ = new File(this.savesDirectory, ☃);
      if (☃.exists()) {
         File ☃x = new File(☃, "level.dat");
         if (☃x.exists()) {
            try {
               NBTTagCompound ☃xx = CompressedStreamTools.readCompressed(new FileInputStream(☃x));
               NBTTagCompound ☃xxx = ☃xx.getCompoundTag("Data");
               ☃xxx.setString("LevelName", ☃);
               CompressedStreamTools.writeCompressed(☃xx, new FileOutputStream(☃x));
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }
   }

   @Override
   public boolean isNewLevelIdAcceptable(String var1) {
      File ☃ = new File(this.savesDirectory, ☃);
      if (☃.exists()) {
         return false;
      } else {
         try {
            ☃.mkdir();
            ☃.delete();
            return true;
         } catch (Throwable var4) {
            LOGGER.warn("Couldn't make new level", var4);
            return false;
         }
      }
   }

   @Override
   public boolean deleteWorldDirectory(String var1) {
      File ☃ = new File(this.savesDirectory, ☃);
      if (!☃.exists()) {
         return true;
      } else {
         LOGGER.info("Deleting level {}", ☃);

         for (int ☃x = 1; ☃x <= 5; ☃x++) {
            LOGGER.info("Attempt {}...", ☃x);
            if (deleteFiles(☃.listFiles())) {
               break;
            }

            LOGGER.warn("Unsuccessful in deleting contents.");
            if (☃x < 5) {
               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var5) {
               }
            }
         }

         return ☃.delete();
      }
   }

   protected static boolean deleteFiles(File[] var0) {
      for (File ☃ : ☃) {
         LOGGER.debug("Deleting {}", ☃);
         if (☃.isDirectory() && !deleteFiles(☃.listFiles())) {
            LOGGER.warn("Couldn't delete directory {}", ☃);
            return false;
         }

         if (!☃.delete()) {
            LOGGER.warn("Couldn't delete file {}", ☃);
            return false;
         }
      }

      return true;
   }

   @Override
   public ISaveHandler getSaveLoader(String var1, boolean var2) {
      return new SaveHandler(this.savesDirectory, ☃, ☃, this.dataFixer);
   }

   @Override
   public boolean isConvertible(String var1) {
      return false;
   }

   @Override
   public boolean isOldMapFormat(String var1) {
      return false;
   }

   @Override
   public boolean convertMapFormat(String var1, IProgressUpdate var2) {
      return false;
   }

   @Override
   public boolean canLoadWorld(String var1) {
      File ☃ = new File(this.savesDirectory, ☃);
      return ☃.isDirectory();
   }

   @Override
   public File getFile(String var1, String var2) {
      return new File(new File(this.savesDirectory, ☃), ☃);
   }
}
