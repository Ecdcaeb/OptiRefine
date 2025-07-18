package net.minecraft.util;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class Util {
   public static Util.EnumOS getOSType() {
      String ☃ = System.getProperty("os.name").toLowerCase(Locale.ROOT);
      if (☃.contains("win")) {
         return Util.EnumOS.WINDOWS;
      } else if (☃.contains("mac")) {
         return Util.EnumOS.OSX;
      } else if (☃.contains("solaris")) {
         return Util.EnumOS.SOLARIS;
      } else if (☃.contains("sunos")) {
         return Util.EnumOS.SOLARIS;
      } else if (☃.contains("linux")) {
         return Util.EnumOS.LINUX;
      } else {
         return ☃.contains("unix") ? Util.EnumOS.LINUX : Util.EnumOS.UNKNOWN;
      }
   }

   @Nullable
   public static <V> V runTask(FutureTask<V> var0, Logger var1) {
      try {
         ☃.run();
         return ☃.get();
      } catch (ExecutionException var3) {
         ☃.fatal("Error executing task", var3);
      } catch (InterruptedException var4) {
         ☃.fatal("Error executing task", var4);
      }

      return null;
   }

   public static <T> T getLastElement(List<T> var0) {
      return ☃.get(☃.size() - 1);
   }

   public static enum EnumOS {
      LINUX,
      SOLARIS,
      WINDOWS,
      OSX,
      UNKNOWN;
   }
}
