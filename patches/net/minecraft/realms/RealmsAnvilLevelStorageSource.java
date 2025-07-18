package net.minecraft.realms;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;

public class RealmsAnvilLevelStorageSource {
   private final ISaveFormat levelStorageSource;

   public RealmsAnvilLevelStorageSource(ISaveFormat var1) {
      this.levelStorageSource = ☃;
   }

   public String getName() {
      return this.levelStorageSource.getName();
   }

   public boolean levelExists(String var1) {
      return this.levelStorageSource.canLoadWorld(☃);
   }

   public boolean convertLevel(String var1, IProgressUpdate var2) {
      return this.levelStorageSource.convertMapFormat(☃, ☃);
   }

   public boolean requiresConversion(String var1) {
      return this.levelStorageSource.isOldMapFormat(☃);
   }

   public boolean isNewLevelIdAcceptable(String var1) {
      return this.levelStorageSource.isNewLevelIdAcceptable(☃);
   }

   public boolean deleteLevel(String var1) {
      return this.levelStorageSource.deleteWorldDirectory(☃);
   }

   public boolean isConvertible(String var1) {
      return this.levelStorageSource.isConvertible(☃);
   }

   public void renameLevel(String var1, String var2) {
      this.levelStorageSource.renameWorld(☃, ☃);
   }

   public void clearAll() {
      this.levelStorageSource.flushCache();
   }

   public List<RealmsLevelSummary> getLevelList() throws AnvilConverterException {
      List<RealmsLevelSummary> ☃ = Lists.newArrayList();

      for (WorldSummary ☃x : this.levelStorageSource.getSaveList()) {
         ☃.add(new RealmsLevelSummary(☃x));
      }

      return ☃;
   }
}
