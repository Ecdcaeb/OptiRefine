package net.minecraft.util.math;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

public class Cartesian {
   public static <T> Iterable<T[]> cartesianProduct(Class<T> var0, Iterable<? extends Iterable<? extends T>> var1) {
      return new Cartesian.Product<>(☃, toArray(Iterable.class, ☃));
   }

   public static <T> Iterable<List<T>> cartesianProduct(Iterable<? extends Iterable<? extends T>> var0) {
      return arraysAsLists(cartesianProduct(Object.class, ☃));
   }

   private static <T> Iterable<List<T>> arraysAsLists(Iterable<Object[]> var0) {
      return Iterables.transform(☃, new Cartesian.GetList());
   }

   private static <T> T[] toArray(Class<? super T> var0, Iterable<? extends T> var1) {
      List<T> ☃ = Lists.newArrayList();

      for (T ☃x : ☃) {
         ☃.add(☃x);
      }

      return (T[])☃.toArray(createArray(☃, ☃.size()));
   }

   private static <T> T[] createArray(Class<? super T> var0, int var1) {
      return (T[])((Object[])Array.newInstance(☃, ☃));
   }

   static class GetList<T> implements Function<Object[], List<T>> {
      private GetList() {
      }

      public List<T> apply(@Nullable Object[] var1) {
         return Arrays.asList((T[])☃);
      }
   }

   static class Product<T> implements Iterable<T[]> {
      private final Class<T> clazz;
      private final Iterable<? extends T>[] iterables;

      private Product(Class<T> var1, Iterable<? extends T>[] var2) {
         this.clazz = ☃;
         this.iterables = ☃;
      }

      @Override
      public Iterator<T[]> iterator() {
         return (Iterator<T[]>)(this.iterables.length <= 0
            ? Collections.singletonList(Cartesian.createArray(this.clazz, 0)).iterator()
            : new Cartesian.Product.ProductIterator(this.clazz, this.iterables));
      }

      static class ProductIterator<T> extends UnmodifiableIterator<T[]> {
         private int index = -2;
         private final Iterable<? extends T>[] iterables;
         private final Iterator<? extends T>[] iterators;
         private final T[] results;

         private ProductIterator(Class<T> var1, Iterable<? extends T>[] var2) {
            this.iterables = ☃;
            this.iterators = Cartesian.createArray(Iterator.class, this.iterables.length);

            for (int ☃ = 0; ☃ < this.iterables.length; ☃++) {
               this.iterators[☃] = ☃[☃].iterator();
            }

            this.results = Cartesian.createArray(☃, this.iterators.length);
         }

         private void endOfData() {
            this.index = -1;
            Arrays.fill(this.iterators, null);
            Arrays.fill(this.results, null);
         }

         public boolean hasNext() {
            if (this.index == -2) {
               this.index = 0;

               for (Iterator<? extends T> ☃ : this.iterators) {
                  if (!☃.hasNext()) {
                     this.endOfData();
                     break;
                  }
               }

               return true;
            } else {
               if (this.index >= this.iterators.length) {
                  for (this.index = this.iterators.length - 1; this.index >= 0; this.index--) {
                     Iterator<? extends T> ☃x = this.iterators[this.index];
                     if (☃x.hasNext()) {
                        break;
                     }

                     if (this.index == 0) {
                        this.endOfData();
                        break;
                     }

                     ☃x = this.iterables[this.index].iterator();
                     this.iterators[this.index] = ☃x;
                     if (!☃x.hasNext()) {
                        this.endOfData();
                        break;
                     }
                  }
               }

               return this.index >= 0;
            }
         }

         public T[] next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               while (this.index < this.iterators.length) {
                  this.results[this.index] = (T)this.iterators[this.index].next();
                  this.index++;
               }

               return (T[])((Object[])this.results.clone());
            }
         }
      }
   }
}
