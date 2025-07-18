package net.minecraft.client.settings;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreativeSettings {
   private static final Logger LOGGER = LogManager.getLogger();
   protected Minecraft minecraft;
   private final File dataFile;
   private final HotbarSnapshot[] hotbarSnapshots = new HotbarSnapshot[9];

   public CreativeSettings(Minecraft var1, File var2) {
      this.minecraft = ☃;
      this.dataFile = new File(☃, "hotbar.nbt");

      for (int ☃ = 0; ☃ < 9; ☃++) {
         this.hotbarSnapshots[☃] = new HotbarSnapshot();
      }

      this.read();
   }

   public void read() {
      try {
         NBTTagCompound ☃ = CompressedStreamTools.read(this.dataFile);
         if (☃ == null) {
            return;
         }

         for (int ☃x = 0; ☃x < 9; ☃x++) {
            this.hotbarSnapshots[☃x].fromTag(☃.getTagList(String.valueOf(☃x), 10));
         }
      } catch (Exception var3) {
         LOGGER.error("Failed to load creative mode options", var3);
      }
   }

   public void write() {
      try {
         NBTTagCompound ☃ = new NBTTagCompound();

         for (int ☃x = 0; ☃x < 9; ☃x++) {
            ☃.setTag(String.valueOf(☃x), this.hotbarSnapshots[☃x].createTag());
         }

         CompressedStreamTools.write(☃, this.dataFile);
      } catch (Exception var3) {
         LOGGER.error("Failed to save creative mode options", var3);
      }
   }

   public HotbarSnapshot getHotbarSnapshot(int var1) {
      return this.hotbarSnapshots[☃];
   }
}
