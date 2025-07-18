package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class CrashReportCategory {
   private final CrashReport crashReport;
   private final String name;
   private final List<CrashReportCategory.Entry> children = Lists.newArrayList();
   private StackTraceElement[] stackTrace = new StackTraceElement[0];

   public CrashReportCategory(CrashReport var1, String var2) {
      this.crashReport = ☃;
      this.name = ☃;
   }

   public static String getCoordinateInfo(double var0, double var2, double var4) {
      return String.format("%.2f,%.2f,%.2f - %s", ☃, ☃, ☃, getCoordinateInfo(new BlockPos(☃, ☃, ☃)));
   }

   public static String getCoordinateInfo(BlockPos var0) {
      return getCoordinateInfo(☃.getX(), ☃.getY(), ☃.getZ());
   }

   public static String getCoordinateInfo(int var0, int var1, int var2) {
      StringBuilder ☃ = new StringBuilder();

      try {
         ☃.append(String.format("World: (%d,%d,%d)", ☃, ☃, ☃));
      } catch (Throwable var16) {
         ☃.append("(Error finding world loc)");
      }

      ☃.append(", ");

      try {
         int ☃x = ☃ >> 4;
         int ☃xx = ☃ >> 4;
         int ☃xxx = ☃ & 15;
         int ☃xxxx = ☃ >> 4;
         int ☃xxxxx = ☃ & 15;
         int ☃xxxxxx = ☃x << 4;
         int ☃xxxxxxx = ☃xx << 4;
         int ☃xxxxxxxx = (☃x + 1 << 4) - 1;
         int ☃xxxxxxxxx = (☃xx + 1 << 4) - 1;
         ☃.append(
            String.format(
               "Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", ☃xxx, ☃xxxx, ☃xxxxx, ☃x, ☃xx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx
            )
         );
      } catch (Throwable var15) {
         ☃.append("(Error finding chunk loc)");
      }

      ☃.append(", ");

      try {
         int ☃x = ☃ >> 9;
         int ☃xx = ☃ >> 9;
         int ☃xxx = ☃x << 5;
         int ☃xxxx = ☃xx << 5;
         int ☃xxxxx = (☃x + 1 << 5) - 1;
         int ☃xxxxxx = (☃xx + 1 << 5) - 1;
         int ☃xxxxxxx = ☃x << 9;
         int ☃xxxxxxxx = ☃xx << 9;
         int ☃xxxxxxxxx = (☃x + 1 << 9) - 1;
         int ☃xxxxxxxxxx = (☃xx + 1 << 9) - 1;
         ☃.append(
            String.format(
               "Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)",
               ☃x,
               ☃xx,
               ☃xxx,
               ☃xxxx,
               ☃xxxxx,
               ☃xxxxxx,
               ☃xxxxxxx,
               ☃xxxxxxxx,
               ☃xxxxxxxxx,
               ☃xxxxxxxxxx
            )
         );
      } catch (Throwable var14) {
         ☃.append("(Error finding world loc)");
      }

      return ☃.toString();
   }

   public void addDetail(String var1, ICrashReportDetail<String> var2) {
      try {
         this.addCrashSection(☃, ☃.call());
      } catch (Throwable var4) {
         this.addCrashSectionThrowable(☃, var4);
      }
   }

   public void addCrashSection(String var1, Object var2) {
      this.children.add(new CrashReportCategory.Entry(☃, ☃));
   }

   public void addCrashSectionThrowable(String var1, Throwable var2) {
      this.addCrashSection(☃, ☃);
   }

   public int getPrunedStackTrace(int var1) {
      StackTraceElement[] ☃ = Thread.currentThread().getStackTrace();
      if (☃.length <= 0) {
         return 0;
      } else {
         this.stackTrace = new StackTraceElement[☃.length - 3 - ☃];
         System.arraycopy(☃, 3 + ☃, this.stackTrace, 0, this.stackTrace.length);
         return this.stackTrace.length;
      }
   }

   public boolean firstTwoElementsOfStackTraceMatch(StackTraceElement var1, StackTraceElement var2) {
      if (this.stackTrace.length != 0 && ☃ != null) {
         StackTraceElement ☃ = this.stackTrace[0];
         if (☃.isNativeMethod() == ☃.isNativeMethod()
            && ☃.getClassName().equals(☃.getClassName())
            && ☃.getFileName().equals(☃.getFileName())
            && ☃.getMethodName().equals(☃.getMethodName())) {
            if (☃ != null != this.stackTrace.length > 1) {
               return false;
            } else if (☃ != null && !this.stackTrace[1].equals(☃)) {
               return false;
            } else {
               this.stackTrace[0] = ☃;
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void trimStackTraceEntriesFromBottom(int var1) {
      StackTraceElement[] ☃ = new StackTraceElement[this.stackTrace.length - ☃];
      System.arraycopy(this.stackTrace, 0, ☃, 0, ☃.length);
      this.stackTrace = ☃;
   }

   public void appendToStringBuilder(StringBuilder var1) {
      ☃.append("-- ").append(this.name).append(" --\n");
      ☃.append("Details:");

      for (CrashReportCategory.Entry ☃ : this.children) {
         ☃.append("\n\t");
         ☃.append(☃.getKey());
         ☃.append(": ");
         ☃.append(☃.getValue());
      }

      if (this.stackTrace != null && this.stackTrace.length > 0) {
         ☃.append("\nStacktrace:");

         for (StackTraceElement ☃ : this.stackTrace) {
            ☃.append("\n\tat ");
            ☃.append(☃);
         }
      }
   }

   public StackTraceElement[] getStackTrace() {
      return this.stackTrace;
   }

   public static void addBlockInfo(CrashReportCategory var0, final BlockPos var1, final Block var2, final int var3) {
      final int ☃ = Block.getIdFromBlock(☃);
      ☃.addDetail("Block type", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            try {
               return String.format("ID #%d (%s // %s)", ☃, ☃.getTranslationKey(), ☃.getClass().getCanonicalName());
            } catch (Throwable var2x) {
               return "ID #" + ☃;
            }
         }
      });
      ☃.addDetail("Block data value", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            if (☃ < 0) {
               return "Unknown? (Got " + ☃ + ")";
            } else {
               String ☃ = String.format("%4s", Integer.toBinaryString(☃)).replace(" ", "0");
               return String.format("%1$d / 0x%1$X / 0b%2$s", ☃, ☃);
            }
         }
      });
      ☃.addDetail("Block location", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo(☃);
         }
      });
   }

   public static void addBlockInfo(CrashReportCategory var0, final BlockPos var1, final IBlockState var2) {
      ☃.addDetail("Block", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return ☃.toString();
         }
      });
      ☃.addDetail("Block location", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return CrashReportCategory.getCoordinateInfo(☃);
         }
      });
   }

   static class Entry {
      private final String key;
      private final String value;

      public Entry(String var1, Object var2) {
         this.key = ☃;
         if (☃ == null) {
            this.value = "~~NULL~~";
         } else if (☃ instanceof Throwable) {
            Throwable ☃ = (Throwable)☃;
            this.value = "~~ERROR~~ " + ☃.getClass().getSimpleName() + ": " + ☃.getMessage();
         } else {
            this.value = ☃.toString();
         }
      }

      public String getKey() {
         return this.key;
      }

      public String getValue() {
         return this.value;
      }
   }
}
