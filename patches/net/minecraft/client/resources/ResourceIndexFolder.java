package net.minecraft.client.resources;

import java.io.File;
import net.minecraft.util.ResourceLocation;

public class ResourceIndexFolder extends ResourceIndex {
   private final File baseDir;

   public ResourceIndexFolder(File var1) {
      this.baseDir = ☃;
   }

   @Override
   public File getFile(ResourceLocation var1) {
      return new File(this.baseDir, ☃.toString().replace(':', '/'));
   }

   @Override
   public File getPackMcmeta() {
      return new File(this.baseDir, "pack.mcmeta");
   }
}
