package net.minecraft.client.resources;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Sets;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.util.Util;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

public class FolderResourcePack extends AbstractResourcePack {
   private static final boolean ON_WINDOWS = Util.getOSType() == Util.EnumOS.WINDOWS;
   private static final CharMatcher BACKSLASH_MATCHER = CharMatcher.is('\\');

   public FolderResourcePack(File var1) {
      super(☃);
   }

   protected static boolean validatePath(File var0, String var1) throws IOException {
      String ☃ = ☃.getCanonicalPath();
      if (ON_WINDOWS) {
         ☃ = BACKSLASH_MATCHER.replaceFrom(☃, '/');
      }

      return ☃.endsWith(☃);
   }

   @Override
   protected InputStream getInputStreamByName(String var1) throws IOException {
      File ☃ = this.getFile(☃);
      if (☃ == null) {
         throw new ResourcePackFileNotFoundException(this.resourcePackFile, ☃);
      } else {
         return new BufferedInputStream(new FileInputStream(☃));
      }
   }

   @Override
   protected boolean hasResourceName(String var1) {
      return this.getFile(☃) != null;
   }

   @Nullable
   private File getFile(String var1) {
      try {
         File ☃ = new File(this.resourcePackFile, ☃);
         if (☃.isFile() && validatePath(☃, ☃)) {
            return ☃;
         }
      } catch (IOException var3) {
      }

      return null;
   }

   @Override
   public Set<String> getResourceDomains() {
      Set<String> ☃ = Sets.newHashSet();
      File ☃x = new File(this.resourcePackFile, "assets/");
      if (☃x.isDirectory()) {
         for (File ☃xx : ☃x.listFiles(DirectoryFileFilter.DIRECTORY)) {
            String ☃xxx = getRelativeName(☃x, ☃xx);
            if (☃xxx.equals(☃xxx.toLowerCase(java.util.Locale.ROOT))) {
               ☃.add(☃xxx.substring(0, ☃xxx.length() - 1));
            } else {
               this.logNameNotLowercase(☃xxx);
            }
         }
      }

      return ☃;
   }
}
