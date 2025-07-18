package net.minecraft.world.chunk.storage;

import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.storage.WorldInfo;

public class AnvilSaveHandler extends SaveHandler {
   public AnvilSaveHandler(File var1, String var2, boolean var3, DataFixer var4) {
      super(☃, ☃, ☃, ☃);
   }

   @Override
   public IChunkLoader getChunkLoader(WorldProvider var1) {
      File ☃ = this.getWorldDirectory();
      if (☃ instanceof WorldProviderHell) {
         File ☃x = new File(☃, "DIM-1");
         ☃x.mkdirs();
         return new AnvilChunkLoader(☃x, this.dataFixer);
      } else if (☃ instanceof WorldProviderEnd) {
         File ☃x = new File(☃, "DIM1");
         ☃x.mkdirs();
         return new AnvilChunkLoader(☃x, this.dataFixer);
      } else {
         return new AnvilChunkLoader(☃, this.dataFixer);
      }
   }

   @Override
   public void saveWorldInfoWithPlayer(WorldInfo var1, @Nullable NBTTagCompound var2) {
      ☃.setSaveVersion(19133);
      super.saveWorldInfoWithPlayer(☃, ☃);
   }

   @Override
   public void flush() {
      try {
         ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

      RegionFileCache.clearRegionFileReferences();
   }
}
