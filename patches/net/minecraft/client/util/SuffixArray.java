package net.minecraft.client.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Swapper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SuffixArray<T> {
   private static final boolean DEBUG_PRINT_COMPARISONS = Boolean.parseBoolean(System.getProperty("SuffixArray.printComparisons", "false"));
   private static final boolean DEBUG_PRINT_ARRAY = Boolean.parseBoolean(System.getProperty("SuffixArray.printArray", "false"));
   private static final Logger LOGGER = LogManager.getLogger();
   protected final List<T> list = Lists.newArrayList();
   private final IntList chars = new IntArrayList();
   private final IntList wordStarts = new IntArrayList();
   private IntList suffixToT = new IntArrayList();
   private IntList offsets = new IntArrayList();
   private int maxStringLength;

   public void add(T var1, String var2) {
      this.maxStringLength = Math.max(this.maxStringLength, ☃.length());
      int ☃ = this.list.size();
      this.list.add(☃);
      this.wordStarts.add(this.chars.size());

      for (int ☃x = 0; ☃x < ☃.length(); ☃x++) {
         this.suffixToT.add(☃);
         this.offsets.add(☃x);
         this.chars.add(☃.charAt(☃x));
      }

      this.suffixToT.add(☃);
      this.offsets.add(☃.length());
      this.chars.add(-1);
   }

   public void generate() {
      int ☃ = this.chars.size();
      int[] ☃x = new int[☃];
      final int[] ☃xx = new int[☃];
      final int[] ☃xxx = new int[☃];
      int[] ☃xxxx = new int[☃];
      IntComparator ☃xxxxx = new IntComparator() {
         public int compare(int var1, int var2) {
            return ☃[☃] == ☃[☃] ? Integer.compare(☃[☃], ☃[☃]) : Integer.compare(☃[☃], ☃[☃]);
         }

         public int compare(Integer var1, Integer var2) {
            return this.compare(☃.intValue(), ☃.intValue());
         }
      };
      Swapper ☃xxxxxx = (var3x, var4x) -> {
         if (var3x != var4x) {
            int ☃xxxxxxx = ☃[var3x];
            ☃[var3x] = ☃[var4x];
            ☃[var4x] = ☃xxxxxxx;
            ☃xxxxxxx = ☃[var3x];
            ☃[var3x] = ☃[var4x];
            ☃[var4x] = ☃xxxxxxx;
            ☃xxxxxxx = ☃[var3x];
            ☃[var3x] = ☃[var4x];
            ☃[var4x] = ☃xxxxxxx;
         }
      };

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃; ☃xxxxxxx++) {
         ☃x[☃xxxxxxx] = this.chars.getInt(☃xxxxxxx);
      }

      int ☃xxxxxxx = 1;

      for (int ☃xxxxxxxx = Math.min(☃, this.maxStringLength); ☃xxxxxxx * 2 < ☃xxxxxxxx; ☃xxxxxxx *= 2) {
         for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃; ☃xxxx[☃xxxxxxxxx] = ☃xxxxxxxxx++) {
            ☃xx[☃xxxxxxxxx] = ☃x[☃xxxxxxxxx];
            ☃xxx[☃xxxxxxxxx] = ☃xxxxxxxxx + ☃xxxxxxx < ☃ ? ☃x[☃xxxxxxxxx + ☃xxxxxxx] : -2;
         }

         Arrays.quickSort(0, ☃, ☃xxxxx, ☃xxxxxx);

         for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃; ☃xxxxxxxxx++) {
            if (☃xxxxxxxxx > 0 && ☃xx[☃xxxxxxxxx] == ☃xx[☃xxxxxxxxx - 1] && ☃xxx[☃xxxxxxxxx] == ☃xxx[☃xxxxxxxxx - 1]) {
               ☃x[☃xxxx[☃xxxxxxxxx]] = ☃x[☃xxxx[☃xxxxxxxxx - 1]];
            } else {
               ☃x[☃xxxx[☃xxxxxxxxx]] = ☃xxxxxxxxx;
            }
         }
      }

      IntList ☃xxxxxxxx = this.suffixToT;
      IntList ☃xxxxxxxxxx = this.offsets;
      this.suffixToT = new IntArrayList(☃xxxxxxxx.size());
      this.offsets = new IntArrayList(☃xxxxxxxxxx.size());

      for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < ☃; ☃xxxxxxxxxxx++) {
         int ☃xxxxxxxxxxxx = ☃xxxx[☃xxxxxxxxxxx];
         this.suffixToT.add(☃xxxxxxxx.getInt(☃xxxxxxxxxxxx));
         this.offsets.add(☃xxxxxxxxxx.getInt(☃xxxxxxxxxxxx));
      }

      if (DEBUG_PRINT_ARRAY) {
         this.printArray();
      }
   }

   private void printArray() {
      for (int ☃ = 0; ☃ < this.suffixToT.size(); ☃++) {
         LOGGER.debug("{} {}", ☃, this.getString(☃));
      }

      LOGGER.debug("");
   }

   private String getString(int var1) {
      int ☃ = this.offsets.getInt(☃);
      int ☃x = this.wordStarts.getInt(this.suffixToT.getInt(☃));
      StringBuilder ☃xx = new StringBuilder();

      for (int ☃xxx = 0; ☃x + ☃xxx < this.chars.size(); ☃xxx++) {
         if (☃xxx == ☃) {
            ☃xx.append('^');
         }

         int ☃xxxx = (Integer)this.chars.get(☃x + ☃xxx);
         if (☃xxxx == -1) {
            break;
         }

         ☃xx.append((char)☃xxxx);
      }

      return ☃xx.toString();
   }

   private int compare(String var1, int var2) {
      int ☃ = this.wordStarts.getInt(this.suffixToT.getInt(☃));
      int ☃x = this.offsets.getInt(☃);

      for (int ☃xx = 0; ☃xx < ☃.length(); ☃xx++) {
         int ☃xxx = this.chars.getInt(☃ + ☃x + ☃xx);
         if (☃xxx == -1) {
            return 1;
         }

         char ☃xxxx = ☃.charAt(☃xx);
         char ☃xxxxx = (char)☃xxx;
         if (☃xxxx < ☃xxxxx) {
            return -1;
         }

         if (☃xxxx > ☃xxxxx) {
            return 1;
         }
      }

      return 0;
   }

   public List<T> search(String var1) {
      int ☃ = this.suffixToT.size();
      int ☃x = 0;
      int ☃xx = ☃;

      while (☃x < ☃xx) {
         int ☃xxx = ☃x + (☃xx - ☃x) / 2;
         int ☃xxxx = this.compare(☃, ☃xxx);
         if (DEBUG_PRINT_COMPARISONS) {
            LOGGER.debug("comparing lower \"{}\" with {} \"{}\": {}", ☃, ☃xxx, this.getString(☃xxx), ☃xxxx);
         }

         if (☃xxxx > 0) {
            ☃x = ☃xxx + 1;
         } else {
            ☃xx = ☃xxx;
         }
      }

      if (☃x >= 0 && ☃x < ☃) {
         int ☃xxxxx = ☃x;
         ☃xx = ☃;

         while (☃x < ☃xx) {
            int ☃xxxxxx = ☃x + (☃xx - ☃x) / 2;
            int ☃xxxxxxx = this.compare(☃, ☃xxxxxx);
            if (DEBUG_PRINT_COMPARISONS) {
               LOGGER.debug("comparing upper \"{}\" with {} \"{}\": {}", ☃, ☃xxxxxx, this.getString(☃xxxxxx), ☃xxxxxxx);
            }

            if (☃xxxxxxx >= 0) {
               ☃x = ☃xxxxxx + 1;
            } else {
               ☃xx = ☃xxxxxx;
            }
         }

         int ☃xxxxxxxx = ☃x;
         IntSet ☃xxxxxxxxx = new IntOpenHashSet();

         for (int ☃xxxxxxxxxx = ☃xxxxx; ☃xxxxxxxxxx < ☃xxxxxxxx; ☃xxxxxxxxxx++) {
            ☃xxxxxxxxx.add(this.suffixToT.getInt(☃xxxxxxxxxx));
         }

         int[] ☃xxxxxxxxxx = ☃xxxxxxxxx.toIntArray();
         java.util.Arrays.sort(☃xxxxxxxxxx);
         Set<T> ☃xxxxxxxxxxx = Sets.newLinkedHashSet();

         for (int ☃xxxxxxxxxxxx : ☃xxxxxxxxxx) {
            ☃xxxxxxxxxxx.add(this.list.get(☃xxxxxxxxxxxx));
         }

         return Lists.newArrayList(☃xxxxxxxxxxx);
      } else {
         return Collections.emptyList();
      }
   }
}
