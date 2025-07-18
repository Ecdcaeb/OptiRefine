package net.minecraft.util;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class Util {
   public static Util.EnumOS getOSType() {
      String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
      if (s.contains("win")) {
         return Util.EnumOS.WINDOWS;
      } else if (s.contains("mac")) {
         return Util.EnumOS.OSX;
      } else if (s.contains("solaris")) {
         return Util.EnumOS.SOLARIS;
      } else if (s.contains("sunos")) {
         return Util.EnumOS.SOLARIS;
      } else if (s.contains("linux")) {
         return Util.EnumOS.LINUX;
      } else {
         return s.contains("unix") ? Util.EnumOS.LINUX : Util.EnumOS.UNKNOWN;
      }
   }

   @Nullable
   public static <V> V runTask(FutureTask<V> task, Logger logger) {
      try {
         task.run();
         return task.get();
      } catch (ExecutionException var4) {
         logger.fatal("Error executing task", var4);
         if (var4.getCause() instanceof OutOfMemoryError) {
            OutOfMemoryError oome = (OutOfMemoryError)var4.getCause();
            throw oome;
         }
      } catch (InterruptedException var5) {
         logger.fatal("Error executing task", var5);
      }

      return null;
   }

   public static <T> T getLastElement(List<T> list) {
      return list.get(list.size() - 1);
   }

   public static enum EnumOS {
      LINUX,
      SOLARIS,
      WINDOWS,
      OSX,
      UNKNOWN;
   }
}
