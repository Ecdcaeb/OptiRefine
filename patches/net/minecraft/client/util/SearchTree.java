package net.minecraft.client.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import net.minecraft.util.ResourceLocation;

public class SearchTree<T> implements ISearchTree<T> {
   protected SuffixArray<T> byName = new SuffixArray<>();
   protected SuffixArray<T> byId = new SuffixArray<>();
   private final Function<T, Iterable<String>> nameFunc;
   private final Function<T, Iterable<ResourceLocation>> idFunc;
   private final List<T> contents = Lists.newArrayList();
   private Object2IntMap<T> numericContents = new Object2IntOpenHashMap();

   public SearchTree(Function<T, Iterable<String>> var1, Function<T, Iterable<ResourceLocation>> var2) {
      this.nameFunc = ☃;
      this.idFunc = ☃;
   }

   public void recalculate() {
      this.byName = new SuffixArray<>();
      this.byId = new SuffixArray<>();

      for (T ☃ : this.contents) {
         this.index(☃);
      }

      this.byName.generate();
      this.byId.generate();
   }

   public void add(T var1) {
      this.numericContents.put(☃, this.contents.size());
      this.contents.add(☃);
      this.index(☃);
   }

   private void index(T var1) {
      this.idFunc.apply(☃).forEach(var2 -> this.byId.add(☃, var2.toString().toLowerCase(Locale.ROOT)));
      this.nameFunc.apply(☃).forEach(var2 -> this.byName.add(☃, var2.toLowerCase(Locale.ROOT)));
   }

   @Override
   public List<T> search(String var1) {
      List<T> ☃ = this.byName.search(☃);
      if (☃.indexOf(58) < 0) {
         return ☃;
      } else {
         List<T> ☃x = this.byId.search(☃);
         return (List<T>)(☃x.isEmpty() ? ☃ : Lists.newArrayList(new SearchTree.MergingIterator<T>(☃.iterator(), ☃x.iterator(), this.numericContents)));
      }
   }

   static class MergingIterator<T> extends AbstractIterator<T> {
      private final Iterator<T> leftItr;
      private final Iterator<T> rightItr;
      private final Object2IntMap<T> numbers;
      private T left;
      private T right;

      public MergingIterator(Iterator<T> var1, Iterator<T> var2, Object2IntMap<T> var3) {
         this.leftItr = ☃;
         this.rightItr = ☃;
         this.numbers = ☃;
         this.left = ☃.hasNext() ? ☃.next() : null;
         this.right = ☃.hasNext() ? ☃.next() : null;
      }

      protected T computeNext() {
         if (this.left == null && this.right == null) {
            return (T)this.endOfData();
         } else {
            int ☃;
            if (this.left == this.right) {
               ☃ = 0;
            } else if (this.left == null) {
               ☃ = 1;
            } else if (this.right == null) {
               ☃ = -1;
            } else {
               ☃ = Integer.compare(this.numbers.getInt(this.left), this.numbers.getInt(this.right));
            }

            T ☃x = ☃ <= 0 ? this.left : this.right;
            if (☃ <= 0) {
               this.left = this.leftItr.hasNext() ? this.leftItr.next() : null;
            }

            if (☃ >= 0) {
               this.right = this.rightItr.hasNext() ? this.rightItr.next() : null;
            }

            return ☃x;
         }
      }
   }
}
