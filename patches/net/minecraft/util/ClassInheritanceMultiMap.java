package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassInheritanceMultiMap<T> extends AbstractSet<T> {
   private static final Set<Class<?>> ALL_KNOWN = Sets.newHashSet();
   private final Map<Class<?>, List<T>> map = Maps.newHashMap();
   private final Set<Class<?>> knownKeys = Sets.newIdentityHashSet();
   private final Class<T> baseClass;
   private final List<T> values = Lists.newArrayList();

   public ClassInheritanceMultiMap(Class<T> var1) {
      this.baseClass = ☃;
      this.knownKeys.add(☃);
      this.map.put(☃, this.values);

      for (Class<?> ☃ : ALL_KNOWN) {
         this.createLookup(☃);
      }
   }

   protected void createLookup(Class<?> var1) {
      ALL_KNOWN.add(☃);

      for (T ☃ : this.values) {
         if (☃.isAssignableFrom(☃.getClass())) {
            this.addForClass(☃, ☃);
         }
      }

      this.knownKeys.add(☃);
   }

   protected Class<?> initializeClassLookup(Class<?> var1) {
      if (this.baseClass.isAssignableFrom(☃)) {
         if (!this.knownKeys.contains(☃)) {
            this.createLookup(☃);
         }

         return ☃;
      } else {
         throw new IllegalArgumentException("Don't know how to search for " + ☃);
      }
   }

   @Override
   public boolean add(T var1) {
      for (Class<?> ☃ : this.knownKeys) {
         if (☃.isAssignableFrom(☃.getClass())) {
            this.addForClass(☃, ☃);
         }
      }

      return true;
   }

   private void addForClass(T var1, Class<?> var2) {
      List<T> ☃ = this.map.get(☃);
      if (☃ == null) {
         this.map.put(☃, Lists.newArrayList(new Object[]{☃}));
      } else {
         ☃.add(☃);
      }
   }

   @Override
   public boolean remove(Object var1) {
      T ☃ = (T)☃;
      boolean ☃x = false;

      for (Class<?> ☃xx : this.knownKeys) {
         if (☃xx.isAssignableFrom(☃.getClass())) {
            List<T> ☃xxx = this.map.get(☃xx);
            if (☃xxx != null && ☃xxx.remove(☃)) {
               ☃x = true;
            }
         }
      }

      return ☃x;
   }

   @Override
   public boolean contains(Object var1) {
      return Iterators.contains(this.getByClass(☃.getClass()).iterator(), ☃);
   }

   public <S> Iterable<S> getByClass(final Class<S> var1) {
      return new Iterable<S>() {
         @Override
         public Iterator<S> iterator() {
            List<T> ☃ = ClassInheritanceMultiMap.this.map.get(ClassInheritanceMultiMap.this.initializeClassLookup(☃));
            if (☃ == null) {
               return Collections.emptyIterator();
            } else {
               Iterator<T> ☃x = ☃.iterator();
               return Iterators.filter(☃x, ☃);
            }
         }
      };
   }

   @Override
   public Iterator<T> iterator() {
      return (Iterator<T>)(this.values.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator(this.values.iterator()));
   }

   @Override
   public int size() {
      return this.values.size();
   }
}
