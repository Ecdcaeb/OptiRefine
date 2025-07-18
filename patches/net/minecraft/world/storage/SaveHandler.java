package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveHandler implements ISaveHandler, IPlayerFileData {
   private static final Logger LOGGER = LogManager.getLogger();
   private final File worldDirectory;
   private final File playersDirectory;
   private final File mapDataDir;
   private final long initializationTime = MinecraftServer.getCurrentTimeMillis();
   private final String saveDirectoryName;
   private final TemplateManager structureTemplateManager;
   protected final DataFixer dataFixer;

   public SaveHandler(File var1, String var2, boolean var3, DataFixer var4) {
      this.dataFixer = ☃;
      this.worldDirectory = new File(☃, ☃);
      this.worldDirectory.mkdirs();
      this.playersDirectory = new File(this.worldDirectory, "playerdata");
      this.mapDataDir = new File(this.worldDirectory, "data");
      this.mapDataDir.mkdirs();
      this.saveDirectoryName = ☃;
      if (☃) {
         this.playersDirectory.mkdirs();
         this.structureTemplateManager = new TemplateManager(new File(this.worldDirectory, "structures").toString(), ☃);
      } else {
         this.structureTemplateManager = null;
      }

      this.setSessionLock();
   }

   private void setSessionLock() {
      try {
         File ☃ = new File(this.worldDirectory, "session.lock");
         DataOutputStream ☃x = new DataOutputStream(new FileOutputStream(☃));

         try {
            ☃x.writeLong(this.initializationTime);
         } finally {
            ☃x.close();
         }
      } catch (IOException var7) {
         var7.printStackTrace();
         throw new RuntimeException("Failed to check session lock, aborting");
      }
   }

   @Override
   public File getWorldDirectory() {
      return this.worldDirectory;
   }

   @Override
   public void checkSessionLock() throws MinecraftException {
      try {
         File ☃ = new File(this.worldDirectory, "session.lock");
         DataInputStream ☃x = new DataInputStream(new FileInputStream(☃));

         try {
            if (☃x.readLong() != this.initializationTime) {
               throw new MinecraftException("The save is being accessed from another location, aborting");
            }
         } finally {
            ☃x.close();
         }
      } catch (IOException var7) {
         throw new MinecraftException("Failed to check session lock, aborting");
      }
   }

   @Override
   public IChunkLoader getChunkLoader(WorldProvider var1) {
      throw new RuntimeException("Old Chunk Storage is no longer supported.");
   }

   @Nullable
   @Override
   public WorldInfo loadWorldInfo() {
      File ☃ = new File(this.worldDirectory, "level.dat");
      if (☃.exists()) {
         WorldInfo ☃x = SaveFormatOld.getWorldData(☃, this.dataFixer);
         if (☃x != null) {
            return ☃x;
         }
      }

      ☃ = new File(this.worldDirectory, "level.dat_old");
      return ☃.exists() ? SaveFormatOld.getWorldData(☃, this.dataFixer) : null;
   }

   @Override
   public void saveWorldInfoWithPlayer(WorldInfo var1, @Nullable NBTTagCompound var2) {
      NBTTagCompound ☃ = ☃.cloneNBTCompound(☃);
      NBTTagCompound ☃x = new NBTTagCompound();
      ☃x.setTag("Data", ☃);

      try {
         File ☃xx = new File(this.worldDirectory, "level.dat_new");
         File ☃xxx = new File(this.worldDirectory, "level.dat_old");
         File ☃xxxx = new File(this.worldDirectory, "level.dat");
         CompressedStreamTools.writeCompressed(☃x, new FileOutputStream(☃xx));
         if (☃xxx.exists()) {
            ☃xxx.delete();
         }

         ☃xxxx.renameTo(☃xxx);
         if (☃xxxx.exists()) {
            ☃xxxx.delete();
         }

         ☃xx.renameTo(☃xxxx);
         if (☃xx.exists()) {
            ☃xx.delete();
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }
   }

   @Override
   public void saveWorldInfo(WorldInfo var1) {
      this.saveWorldInfoWithPlayer(☃, null);
   }

   @Override
   public void writePlayerData(EntityPlayer var1) {
      try {
         NBTTagCompound ☃ = ☃.writeToNBT(new NBTTagCompound());
         File ☃x = new File(this.playersDirectory, ☃.getCachedUniqueIdString() + ".dat.tmp");
         File ☃xx = new File(this.playersDirectory, ☃.getCachedUniqueIdString() + ".dat");
         CompressedStreamTools.writeCompressed(☃, new FileOutputStream(☃x));
         if (☃xx.exists()) {
            ☃xx.delete();
         }

         ☃x.renameTo(☃xx);
      } catch (Exception var5) {
         LOGGER.warn("Failed to save player data for {}", ☃.getName());
      }
   }

   @Nullable
   @Override
   public NBTTagCompound readPlayerData(EntityPlayer var1) {
      NBTTagCompound ☃ = null;

      try {
         File ☃x = new File(this.playersDirectory, ☃.getCachedUniqueIdString() + ".dat");
         if (☃x.exists() && ☃x.isFile()) {
            ☃ = CompressedStreamTools.readCompressed(new FileInputStream(☃x));
         }
      } catch (Exception var4) {
         LOGGER.warn("Failed to load player data for {}", ☃.getName());
      }

      if (☃ != null) {
         ☃.readFromNBT(this.dataFixer.process(FixTypes.PLAYER, ☃));
      }

      return ☃;
   }

   @Override
   public IPlayerFileData getPlayerNBTManager() {
      return this;
   }

   @Override
   public String[] getAvailablePlayerDat() {
      String[] ☃ = this.playersDirectory.list();
      if (☃ == null) {
         ☃ = new String[0];
      }

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         if (☃[☃x].endsWith(".dat")) {
            ☃[☃x] = ☃[☃x].substring(0, ☃[☃x].length() - 4);
         }
      }

      return ☃;
   }

   @Override
   public void flush() {
   }

   @Override
   public File getMapFileFromName(String var1) {
      return new File(this.mapDataDir, ☃ + ".dat");
   }

   @Override
   public TemplateManager getStructureTemplateManager() {
      return this.structureTemplateManager;
   }
}
