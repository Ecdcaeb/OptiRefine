package net.minecraft.util;

import javax.annotation.Nullable;

public class IntHashMap<V> {
   private transient IntHashMap.Entry<V>[] slots;
   private transient int count;
   private int threshold;
   private final float growFactor = 0.75F;

   public IntHashMap() {
      this.threshold = 12;
      this.slots = new IntHashMap.Entry[16];
   }

   private static int computeHash(int var0) {
      ☃ ^= ☃ >>> 20 ^ ☃ >>> 12;
      return ☃ ^ ☃ >>> 7 ^ ☃ >>> 4;
   }

   private static int getSlotIndex(int var0, int var1) {
      return ☃ & ☃ - 1;
   }

   @Nullable
   public V lookup(int var1) {
      int ☃ = computeHash(☃);

      for (IntHashMap.Entry<V> ☃x = this.slots[getSlotIndex(☃, this.slots.length)]; ☃x != null; ☃x = ☃x.nextEntry) {
         if (☃x.hashEntry == ☃) {
            return ☃x.valueEntry;
         }
      }

      return null;
   }

   public boolean containsItem(int var1) {
      return this.lookupEntry(☃) != null;
   }

   @Nullable
   final IntHashMap.Entry<V> lookupEntry(int var1) {
      int ☃ = computeHash(☃);

      for (IntHashMap.Entry<V> ☃x = this.slots[getSlotIndex(☃, this.slots.length)]; ☃x != null; ☃x = ☃x.nextEntry) {
         if (☃x.hashEntry == ☃) {
            return ☃x;
         }
      }

      return null;
   }

   public void addKey(int var1, V var2) {
      int ☃ = computeHash(☃);
      int ☃x = getSlotIndex(☃, this.slots.length);

      for (IntHashMap.Entry<V> ☃xx = this.slots[☃x]; ☃xx != null; ☃xx = ☃xx.nextEntry) {
         if (☃xx.hashEntry == ☃) {
            ☃xx.valueEntry = ☃;
            return;
         }
      }

      this.insert(☃, ☃, ☃, ☃x);
   }

   private void grow(int var1) {
      IntHashMap.Entry<V>[] ☃ = this.slots;
      int ☃x = ☃.length;
      if (☃x == 1073741824) {
         this.threshold = Integer.MAX_VALUE;
      } else {
         IntHashMap.Entry<V>[] ☃xx = new IntHashMap.Entry[☃];
         this.copyTo(☃xx);
         this.slots = ☃xx;
         this.threshold = (int)(☃ * this.growFactor);
      }
   }

   private void copyTo(IntHashMap.Entry<V>[] var1) {
      IntHashMap.Entry<V>[] ☃ = this.slots;
      int ☃x = ☃.length;

      for (int ☃xx = 0; ☃xx < ☃.length; ☃xx++) {
         IntHashMap.Entry<V> ☃xxx = ☃[☃xx];
         if (☃xxx != null) {
            ☃[☃xx] = null;

            while (true) {
               IntHashMap.Entry<V> ☃xxxx = ☃xxx.nextEntry;
               int ☃xxxxx = getSlotIndex(☃xxx.slotHash, ☃x);
               ☃xxx.nextEntry = ☃[☃xxxxx];
               ☃[☃xxxxx] = ☃xxx;
               ☃xxx = ☃xxxx;
               if (☃xxxx == null) {
                  break;
               }
            }
         }
      }
   }

   @Nullable
   public V removeObject(int var1) {
      IntHashMap.Entry<V> ☃ = this.removeEntry(☃);
      return ☃ == null ? null : ☃.valueEntry;
   }

   @Nullable
   final IntHashMap.Entry<V> removeEntry(int var1) {
      int ☃ = computeHash(☃);
      int ☃x = getSlotIndex(☃, this.slots.length);
      IntHashMap.Entry<V> ☃xx = this.slots[☃x];
      IntHashMap.Entry<V> ☃xxx = ☃xx;

      while (☃xxx != null) {
         IntHashMap.Entry<V> ☃xxxx = ☃xxx.nextEntry;
         if (☃xxx.hashEntry == ☃) {
            this.count--;
            if (☃xx == ☃xxx) {
               this.slots[☃x] = ☃xxxx;
            } else {
               ☃xx.nextEntry = ☃xxxx;
            }

            return ☃xxx;
         }

         ☃xx = ☃xxx;
         ☃xxx = ☃xxxx;
      }

      return ☃xxx;
   }

   public void clearMap() {
      IntHashMap.Entry<V>[] ☃ = this.slots;

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         ☃[☃x] = null;
      }

      this.count = 0;
   }

   private void insert(int var1, int var2, V var3, int var4) {
      IntHashMap.Entry<V> ☃ = this.slots[☃];
      this.slots[☃] = new IntHashMap.Entry<>(☃, ☃, ☃, ☃);
      if (this.count++ >= this.threshold) {
         this.grow(2 * this.slots.length);
      }
   }

   static class Entry<V> {
      final int hashEntry;
      V valueEntry;
      IntHashMap.Entry<V> nextEntry;
      final int slotHash;

      Entry(int var1, int var2, V var3, IntHashMap.Entry<V> var4) {
         this.valueEntry = ☃;
         this.nextEntry = ☃;
         this.hashEntry = ☃;
         this.slotHash = ☃;
      }

      public final int getHash() {
         return this.hashEntry;
      }

      public final V getValue() {
         return this.valueEntry;
      }

      @Override
      public final boolean equals(Object var1) {
         if (!(☃ instanceof IntHashMap.Entry)) {
            return false;
         } else {
            IntHashMap.Entry<V> ☃ = (IntHashMap.Entry<V>)☃;
            if (this.hashEntry == ☃.hashEntry) {
               Object ☃x = this.getValue();
               Object ☃xx = ☃.getValue();
               if (☃x == ☃xx || ☃x != null && ☃x.equals(☃xx)) {
                  return true;
               }
            }

            return false;
         }
      }

      @Override
      public final int hashCode() {
         return IntHashMap.computeHash(this.hashEntry);
      }

      @Override
      public final String toString() {
         return this.getHash() + "=" + this.getValue();
      }
   }
}
