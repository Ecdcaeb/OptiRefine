package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class ObjectIntIdentityMap<T> implements IObjectIntIterable<T> {
   private final IdentityHashMap<T, Integer> identityMap;
   private final List<T> objectList;

   public ObjectIntIdentityMap() {
      this(512);
   }

   public ObjectIntIdentityMap(int var1) {
      this.objectList = Lists.newArrayListWithExpectedSize(☃);
      this.identityMap = new IdentityHashMap<>(☃);
   }

   public void put(T var1, int var2) {
      this.identityMap.put(☃, ☃);

      while (this.objectList.size() <= ☃) {
         this.objectList.add(null);
      }

      this.objectList.set(☃, ☃);
   }

   public int get(T var1) {
      Integer ☃ = this.identityMap.get(☃);
      return ☃ == null ? -1 : ☃;
   }

   @Nullable
   public final T getByValue(int var1) {
      return ☃ >= 0 && ☃ < this.objectList.size() ? this.objectList.get(☃) : null;
   }

   @Override
   public Iterator<T> iterator() {
      return Iterators.filter(this.objectList.iterator(), Predicates.notNull());
   }

   public int size() {
      return this.identityMap.size();
   }
}
