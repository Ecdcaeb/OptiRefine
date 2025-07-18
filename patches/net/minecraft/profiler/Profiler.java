package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
   private static final Logger LOGGER = LogManager.getLogger();
   private final List<String> sectionList = Lists.newArrayList();
   private final List<Long> timestampList = Lists.newArrayList();
   public boolean profilingEnabled;
   private String profilingSection = "";
   private final Map<String, Long> profilingMap = Maps.newHashMap();

   public void clearProfiling() {
      this.profilingMap.clear();
      this.profilingSection = "";
      this.sectionList.clear();
   }

   public void startSection(String var1) {
      if (this.profilingEnabled) {
         if (!this.profilingSection.isEmpty()) {
            this.profilingSection = this.profilingSection + ".";
         }

         this.profilingSection = this.profilingSection + ☃;
         this.sectionList.add(this.profilingSection);
         this.timestampList.add(System.nanoTime());
      }
   }

   public void func_194340_a(Supplier<String> var1) {
      if (this.profilingEnabled) {
         this.startSection(☃.get());
      }
   }

   public void endSection() {
      if (this.profilingEnabled) {
         long ☃ = System.nanoTime();
         long ☃x = this.timestampList.remove(this.timestampList.size() - 1);
         this.sectionList.remove(this.sectionList.size() - 1);
         long ☃xx = ☃ - ☃x;
         if (this.profilingMap.containsKey(this.profilingSection)) {
            this.profilingMap.put(this.profilingSection, this.profilingMap.get(this.profilingSection) + ☃xx);
         } else {
            this.profilingMap.put(this.profilingSection, ☃xx);
         }

         if (☃xx > 100000000L) {
            LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", this.profilingSection, ☃xx / 1000000.0);
         }

         this.profilingSection = this.sectionList.isEmpty() ? "" : this.sectionList.get(this.sectionList.size() - 1);
      }
   }

   public List<Profiler.Result> getProfilingData(String var1) {
      if (!this.profilingEnabled) {
         return Collections.emptyList();
      } else {
         String ☃ = ☃;
         long ☃x = this.profilingMap.containsKey("root") ? this.profilingMap.get("root") : 0L;
         long ☃xx = this.profilingMap.containsKey(☃) ? this.profilingMap.get(☃) : -1L;
         List<Profiler.Result> ☃xxx = Lists.newArrayList();
         if (!☃.isEmpty()) {
            ☃ = ☃ + ".";
         }

         long ☃xxxx = 0L;

         for (String ☃xxxxx : this.profilingMap.keySet()) {
            if (☃xxxxx.length() > ☃.length() && ☃xxxxx.startsWith(☃) && ☃xxxxx.indexOf(".", ☃.length() + 1) < 0) {
               ☃xxxx += this.profilingMap.get(☃xxxxx);
            }
         }

         float ☃xxxxxx = (float)☃xxxx;
         if (☃xxxx < ☃xx) {
            ☃xxxx = ☃xx;
         }

         if (☃x < ☃xxxx) {
            ☃x = ☃xxxx;
         }

         for (String ☃xxxxxxx : this.profilingMap.keySet()) {
            if (☃xxxxxxx.length() > ☃.length() && ☃xxxxxxx.startsWith(☃) && ☃xxxxxxx.indexOf(".", ☃.length() + 1) < 0) {
               long ☃xxxxxxxx = this.profilingMap.get(☃xxxxxxx);
               double ☃xxxxxxxxx = ☃xxxxxxxx * 100.0 / ☃xxxx;
               double ☃xxxxxxxxxx = ☃xxxxxxxx * 100.0 / ☃x;
               String ☃xxxxxxxxxxx = ☃xxxxxxx.substring(☃.length());
               ☃xxx.add(new Profiler.Result(☃xxxxxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx));
            }
         }

         for (String ☃xxxxxxxx : this.profilingMap.keySet()) {
            this.profilingMap.put(☃xxxxxxxx, this.profilingMap.get(☃xxxxxxxx) * 999L / 1000L);
         }

         if ((float)☃xxxx > ☃xxxxxx) {
            ☃xxx.add(new Profiler.Result("unspecified", ((float)☃xxxx - ☃xxxxxx) * 100.0 / ☃xxxx, ((float)☃xxxx - ☃xxxxxx) * 100.0 / ☃x));
         }

         Collections.sort(☃xxx);
         ☃xxx.add(0, new Profiler.Result(☃, 100.0, ☃xxxx * 100.0 / ☃x));
         return ☃xxx;
      }
   }

   public void endStartSection(String var1) {
      this.endSection();
      this.startSection(☃);
   }

   public void func_194339_b(Supplier<String> var1) {
      this.endSection();
      this.func_194340_a(☃);
   }

   public String getNameOfLastSection() {
      return this.sectionList.isEmpty() ? "[UNKNOWN]" : this.sectionList.get(this.sectionList.size() - 1);
   }

   public static final class Result implements Comparable<Profiler.Result> {
      public double usePercentage;
      public double totalUsePercentage;
      public String profilerName;

      public Result(String var1, double var2, double var4) {
         this.profilerName = ☃;
         this.usePercentage = ☃;
         this.totalUsePercentage = ☃;
      }

      public int compareTo(Profiler.Result var1) {
         if (☃.usePercentage < this.usePercentage) {
            return -1;
         } else {
            return ☃.usePercentage > this.usePercentage ? 1 : ☃.profilerName.compareTo(this.profilerName);
         }
      }

      public int getColor() {
         return (this.profilerName.hashCode() & 11184810) + 4473924;
      }
   }
}
