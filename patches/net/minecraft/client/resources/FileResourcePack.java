package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileResourcePack extends AbstractResourcePack implements Closeable {
   public static final Splitter ENTRY_NAME_SPLITTER = Splitter.on('/').omitEmptyStrings().limit(3);
   private ZipFile resourcePackZipFile;

   public FileResourcePack(File var1) {
      super(☃);
   }

   private ZipFile getResourcePackZipFile() throws IOException {
      if (this.resourcePackZipFile == null) {
         this.resourcePackZipFile = new ZipFile(this.resourcePackFile);
      }

      return this.resourcePackZipFile;
   }

   @Override
   protected InputStream getInputStreamByName(String var1) throws IOException {
      ZipFile ☃ = this.getResourcePackZipFile();
      ZipEntry ☃x = ☃.getEntry(☃);
      if (☃x == null) {
         throw new ResourcePackFileNotFoundException(this.resourcePackFile, ☃);
      } else {
         return ☃.getInputStream(☃x);
      }
   }

   @Override
   public boolean hasResourceName(String var1) {
      try {
         return this.getResourcePackZipFile().getEntry(☃) != null;
      } catch (IOException var3) {
         return false;
      }
   }

   @Override
   public Set<String> getResourceDomains() {
      ZipFile ☃;
      try {
         ☃ = this.getResourcePackZipFile();
      } catch (IOException var8) {
         return Collections.emptySet();
      }

      Enumeration<? extends ZipEntry> ☃x = ☃.entries();
      Set<String> ☃xx = Sets.newHashSet();

      while (☃x.hasMoreElements()) {
         ZipEntry ☃xxx = ☃x.nextElement();
         String ☃xxxx = ☃xxx.getName();
         if (☃xxxx.startsWith("assets/")) {
            List<String> ☃xxxxx = Lists.newArrayList(ENTRY_NAME_SPLITTER.split(☃xxxx));
            if (☃xxxxx.size() > 1) {
               String ☃xxxxxx = ☃xxxxx.get(1);
               if (☃xxxxxx.equals(☃xxxxxx.toLowerCase(java.util.Locale.ROOT))) {
                  ☃xx.add(☃xxxxxx);
               } else {
                  this.logNameNotLowercase(☃xxxxxx);
               }
            }
         }
      }

      return ☃xx;
   }

   @Override
   protected void finalize() throws Throwable {
      this.close();
      super.finalize();
   }

   @Override
   public void close() throws IOException {
      if (this.resourcePackZipFile != null) {
         this.resourcePackZipFile.close();
         this.resourcePackZipFile = null;
      }
   }
}
