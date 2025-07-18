package net.minecraft.util;

import java.util.List;
import java.util.Random;

public class WeightedRandom {
   public static int getTotalWeight(List<? extends WeightedRandom.Item> var0) {
      int ☃ = 0;
      int ☃x = 0;

      for (int ☃xx = ☃.size(); ☃x < ☃xx; ☃x++) {
         WeightedRandom.Item ☃xxx = ☃.get(☃x);
         ☃ += ☃xxx.itemWeight;
      }

      return ☃;
   }

   public static <T extends WeightedRandom.Item> T getRandomItem(Random var0, List<T> var1, int var2) {
      if (☃ <= 0) {
         throw new IllegalArgumentException();
      } else {
         int ☃ = ☃.nextInt(☃);
         return getRandomItem(☃, ☃);
      }
   }

   public static <T extends WeightedRandom.Item> T getRandomItem(List<T> var0, int var1) {
      int ☃ = 0;

      for (int ☃x = ☃.size(); ☃ < ☃x; ☃++) {
         T ☃xx = (T)☃.get(☃);
         ☃ -= ☃xx.itemWeight;
         if (☃ < 0) {
            return ☃xx;
         }
      }

      return null;
   }

   public static <T extends WeightedRandom.Item> T getRandomItem(Random var0, List<T> var1) {
      return getRandomItem(☃, ☃, getTotalWeight(☃));
   }

   public static class Item {
      protected int itemWeight;

      public Item(int var1) {
         this.itemWeight = ☃;
      }
   }
}
