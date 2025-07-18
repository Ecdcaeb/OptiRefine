package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.util.math.MathHelper;

public class IntIdentityHashBiMap<K> implements IObjectIntIterable<K> {
   private static final Object EMPTY = null;
   private K[] values;
   private int[] intKeys;
   private K[] byId;
   private int nextFreeIndex;
   private int mapSize;

   public IntIdentityHashBiMap(int var1) {
      ☃ = (int)(☃ / 0.8F);
      this.values = (K[])(new Object[☃]);
      this.intKeys = new int[☃];
      this.byId = (K[])(new Object[☃]);
   }

   public int getId(@Nullable K var1) {
      return this.getValue(this.getIndex(☃, this.hashObject(☃)));
   }

   @Nullable
   public K get(int var1) {
      return ☃ >= 0 && ☃ < this.byId.length ? this.byId[☃] : null;
   }

   private int getValue(int var1) {
      return ☃ == -1 ? -1 : this.intKeys[☃];
   }

   public int add(K var1) {
      int ☃ = this.nextId();
      this.put(☃, ☃);
      return ☃;
   }

   private int nextId() {
      while (this.nextFreeIndex < this.byId.length && this.byId[this.nextFreeIndex] != null) {
         this.nextFreeIndex++;
      }

      return this.nextFreeIndex;
   }

   private void grow(int var1) {
      K[] ☃ = this.values;
      int[] ☃x = this.intKeys;
      this.values = (K[])(new Object[☃]);
      this.intKeys = new int[☃];
      this.byId = (K[])(new Object[☃]);
      this.nextFreeIndex = 0;
      this.mapSize = 0;

      for (int ☃xx = 0; ☃xx < ☃.length; ☃xx++) {
         if (☃[☃xx] != null) {
            this.put(☃[☃xx], ☃x[☃xx]);
         }
      }
   }

   public void put(K var1, int var2) {
      int ☃ = Math.max(☃, this.mapSize + 1);
      if (☃ >= this.values.length * 0.8F) {
         int ☃x = this.values.length << 1;

         while (☃x < ☃) {
            ☃x <<= 1;
         }

         this.grow(☃x);
      }

      int ☃x = this.findEmpty(this.hashObject(☃));
      this.values[☃x] = ☃;
      this.intKeys[☃x] = ☃;
      this.byId[☃] = ☃;
      this.mapSize++;
      if (☃ == this.nextFreeIndex) {
         this.nextFreeIndex++;
      }
   }

   private int hashObject(@Nullable K var1) {
      return (MathHelper.hash(System.identityHashCode(☃)) & 2147483647) % this.values.length;
   }

   private int getIndex(@Nullable K var1, int var2) {
      for (int ☃ = ☃; ☃ < this.values.length; ☃++) {
         if (this.values[☃] == ☃) {
            return ☃;
         }

         if (this.values[☃] == EMPTY) {
            return -1;
         }
      }

      for (int ☃ = 0; ☃ < ☃; ☃++) {
         if (this.values[☃] == ☃) {
            return ☃;
         }

         if (this.values[☃] == EMPTY) {
            return -1;
         }
      }

      return -1;
   }

   private int findEmpty(int var1) {
      for (int ☃ = ☃; ☃ < this.values.length; ☃++) {
         if (this.values[☃] == EMPTY) {
            return ☃;
         }
      }

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         if (this.values[☃x] == EMPTY) {
            return ☃x;
         }
      }

      throw new RuntimeException("Overflowed :(");
   }

   @Override
   public Iterator<K> iterator() {
      return Iterators.filter(Iterators.forArray(this.byId), Predicates.notNull());
   }

   public void clear() {
      Arrays.fill(this.values, null);
      Arrays.fill(this.byId, null);
      this.nextFreeIndex = 0;
      this.mapSize = 0;
   }

   public int size() {
      return this.mapSize;
   }
}
