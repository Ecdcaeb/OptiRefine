package net.minecraft.util.text;

public class TextComponentSelector extends TextComponentBase {
   private final String selector;

   public TextComponentSelector(String var1) {
      this.selector = ☃;
   }

   public String getSelector() {
      return this.selector;
   }

   @Override
   public String getUnformattedComponentText() {
      return this.selector;
   }

   public TextComponentSelector createCopy() {
      TextComponentSelector ☃ = new TextComponentSelector(this.selector);
      ☃.setStyle(this.getStyle().createShallowCopy());

      for (ITextComponent ☃x : this.getSiblings()) {
         ☃.appendSibling(☃x.createCopy());
      }

      return ☃;
   }

   @Override
   public boolean equals(Object var1) {
      if (this == ☃) {
         return true;
      } else if (!(☃ instanceof TextComponentSelector)) {
         return false;
      } else {
         TextComponentSelector ☃ = (TextComponentSelector)☃;
         return this.selector.equals(☃.selector) && super.equals(☃);
      }
   }

   @Override
   public String toString() {
      return "SelectorComponent{pattern='" + this.selector + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
   }
}
