package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;

public abstract class BaseAttribute implements IAttribute {
   private final IAttribute parent;
   private final String translationKey;
   private final double defaultValue;
   private boolean shouldWatch;

   protected BaseAttribute(@Nullable IAttribute var1, String var2, double var3) {
      this.parent = ☃;
      this.translationKey = ☃;
      this.defaultValue = ☃;
      if (☃ == null) {
         throw new IllegalArgumentException("Name cannot be null!");
      }
   }

   @Override
   public String getName() {
      return this.translationKey;
   }

   @Override
   public double getDefaultValue() {
      return this.defaultValue;
   }

   @Override
   public boolean getShouldWatch() {
      return this.shouldWatch;
   }

   public BaseAttribute setShouldWatch(boolean var1) {
      this.shouldWatch = ☃;
      return this;
   }

   @Nullable
   @Override
   public IAttribute getParent() {
      return this.parent;
   }

   @Override
   public int hashCode() {
      return this.translationKey.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return ☃ instanceof IAttribute && this.translationKey.equals(((IAttribute)☃).getName());
   }
}
