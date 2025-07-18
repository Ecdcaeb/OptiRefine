package net.minecraft.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class NonNullList<E> extends AbstractList<E> {
   private final List<E> delegate;
   private final E defaultElement;

   public static <E> NonNullList<E> create() {
      return new NonNullList<>();
   }

   public static <E> NonNullList<E> withSize(int var0, E var1) {
      Validate.notNull(☃);
      Object[] ☃ = new Object[☃];
      Arrays.fill(☃, ☃);
      return new NonNullList<>(Arrays.asList((E[])☃), ☃);
   }

   public static <E> NonNullList<E> from(E var0, E... var1) {
      return new NonNullList<>(Arrays.asList(☃), ☃);
   }

   protected NonNullList() {
      this(new ArrayList<>(), null);
   }

   protected NonNullList(List<E> var1, @Nullable E var2) {
      this.delegate = ☃;
      this.defaultElement = ☃;
   }

   @Nonnull
   @Override
   public E get(int var1) {
      return this.delegate.get(☃);
   }

   @Override
   public E set(int var1, E var2) {
      Validate.notNull(☃);
      return this.delegate.set(☃, ☃);
   }

   @Override
   public void add(int var1, E var2) {
      Validate.notNull(☃);
      this.delegate.add(☃, ☃);
   }

   @Override
   public E remove(int var1) {
      return this.delegate.remove(☃);
   }

   @Override
   public int size() {
      return this.delegate.size();
   }

   @Override
   public void clear() {
      if (this.defaultElement == null) {
         super.clear();
      } else {
         for (int ☃ = 0; ☃ < this.size(); ☃++) {
            this.set(☃, this.defaultElement);
         }
      }
   }
}
