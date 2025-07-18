package net.minecraft.util.text;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public abstract class TextComponentBase implements ITextComponent {
   protected List<ITextComponent> siblings = Lists.newArrayList();
   private Style style;

   @Override
   public ITextComponent appendSibling(ITextComponent var1) {
      ☃.getStyle().setParentStyle(this.getStyle());
      this.siblings.add(☃);
      return this;
   }

   @Override
   public List<ITextComponent> getSiblings() {
      return this.siblings;
   }

   @Override
   public ITextComponent appendText(String var1) {
      return this.appendSibling(new TextComponentString(☃));
   }

   @Override
   public ITextComponent setStyle(Style var1) {
      this.style = ☃;

      for (ITextComponent ☃ : this.siblings) {
         ☃.getStyle().setParentStyle(this.getStyle());
      }

      return this;
   }

   @Override
   public Style getStyle() {
      if (this.style == null) {
         this.style = new Style();

         for (ITextComponent ☃ : this.siblings) {
            ☃.getStyle().setParentStyle(this.style);
         }
      }

      return this.style;
   }

   @Override
   public Iterator<ITextComponent> iterator() {
      return Iterators.concat(Iterators.forArray(new TextComponentBase[]{this}), createDeepCopyIterator(this.siblings));
   }

   @Override
   public final String getUnformattedText() {
      StringBuilder ☃ = new StringBuilder();

      for (ITextComponent ☃x : this) {
         ☃.append(☃x.getUnformattedComponentText());
      }

      return ☃.toString();
   }

   @Override
   public final String getFormattedText() {
      StringBuilder ☃ = new StringBuilder();

      for (ITextComponent ☃x : this) {
         String ☃xx = ☃x.getUnformattedComponentText();
         if (!☃xx.isEmpty()) {
            ☃.append(☃x.getStyle().getFormattingCode());
            ☃.append(☃xx);
            ☃.append(TextFormatting.RESET);
         }
      }

      return ☃.toString();
   }

   public static Iterator<ITextComponent> createDeepCopyIterator(Iterable<ITextComponent> var0) {
      Iterator<ITextComponent> ☃ = Iterators.concat(Iterators.transform(☃.iterator(), new Function<ITextComponent, Iterator<ITextComponent>>() {
         public Iterator<ITextComponent> apply(@Nullable ITextComponent var1) {
            return ☃.iterator();
         }
      }));
      return Iterators.transform(☃, new Function<ITextComponent, ITextComponent>() {
         public ITextComponent apply(@Nullable ITextComponent var1) {
            ITextComponent ☃x = ☃.createCopy();
            ☃x.setStyle(☃x.getStyle().createDeepCopy());
            return ☃x;
         }
      });
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof TextComponentBase)) {
         return false;
      } else {
         TextComponentBase ☃ = (TextComponentBase)☃;
         return this.siblings.equals(☃.siblings) && this.getStyle().equals(☃.getStyle());
      }
   }

   @Override
   public int hashCode() {
      return 31 * this.style.hashCode() + this.siblings.hashCode();
   }

   @Override
   public String toString() {
      return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
   }
}
