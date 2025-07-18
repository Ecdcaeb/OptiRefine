package net.minecraft.block.state.pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.state.BlockWorldState;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class FactoryBlockPattern {
   private static final Joiner COMMA_JOIN = Joiner.on(",");
   private final List<String[]> depth = Lists.newArrayList();
   private final Map<Character, Predicate<BlockWorldState>> symbolMap = Maps.newHashMap();
   private int aisleHeight;
   private int rowWidth;

   private FactoryBlockPattern() {
      this.symbolMap.put(' ', Predicates.alwaysTrue());
   }

   public FactoryBlockPattern aisle(String... var1) {
      if (!ArrayUtils.isEmpty(☃) && !StringUtils.isEmpty(☃[0])) {
         if (this.depth.isEmpty()) {
            this.aisleHeight = ☃.length;
            this.rowWidth = ☃[0].length();
         }

         if (☃.length != this.aisleHeight) {
            throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + ☃.length + ")");
         } else {
            for (String ☃ : ☃) {
               if (☃.length() != this.rowWidth) {
                  throw new IllegalArgumentException(
                     "Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + ☃.length() + ")"
                  );
               }

               for (char ☃x : ☃.toCharArray()) {
                  if (!this.symbolMap.containsKey(☃x)) {
                     this.symbolMap.put(☃x, null);
                  }
               }
            }

            this.depth.add(☃);
            return this;
         }
      } else {
         throw new IllegalArgumentException("Empty pattern for aisle");
      }
   }

   public static FactoryBlockPattern start() {
      return new FactoryBlockPattern();
   }

   public FactoryBlockPattern where(char var1, Predicate<BlockWorldState> var2) {
      this.symbolMap.put(☃, ☃);
      return this;
   }

   public BlockPattern build() {
      return new BlockPattern(this.makePredicateArray());
   }

   private Predicate<BlockWorldState>[][][] makePredicateArray() {
      this.checkMissingPredicates();
      Predicate<BlockWorldState>[][][] ☃ = (Predicate<BlockWorldState>[][][])Array.newInstance(
         Predicate.class, this.depth.size(), this.aisleHeight, this.rowWidth
      );

      for (int ☃x = 0; ☃x < this.depth.size(); ☃x++) {
         for (int ☃xx = 0; ☃xx < this.aisleHeight; ☃xx++) {
            for (int ☃xxx = 0; ☃xxx < this.rowWidth; ☃xxx++) {
               ☃[☃x][☃xx][☃xxx] = this.symbolMap.get(this.depth.get(☃x)[☃xx].charAt(☃xxx));
            }
         }
      }

      return ☃;
   }

   private void checkMissingPredicates() {
      List<Character> ☃ = Lists.newArrayList();

      for (Entry<Character, Predicate<BlockWorldState>> ☃x : this.symbolMap.entrySet()) {
         if (☃x.getValue() == null) {
            ☃.add(☃x.getKey());
         }
      }

      if (!☃.isEmpty()) {
         throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join(☃) + " are missing");
      }
   }
}
