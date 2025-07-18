package net.minecraft.util.text;

public class TextComponentString extends TextComponentBase {
   private final String text;

   public TextComponentString(String var1) {
      this.text = ☃;
   }

   public String getText() {
      return this.text;
   }

   @Override
   public String getUnformattedComponentText() {
      return this.text;
   }

   public TextComponentString createCopy() {
      TextComponentString ☃ = new TextComponentString(this.text);
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
      } else if (!(☃ instanceof TextComponentString)) {
         return false;
      } else {
         TextComponentString ☃ = (TextComponentString)☃;
         return this.text.equals(☃.getText()) && super.equals(☃);
      }
   }

   @Override
   public String toString() {
      return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
   }
}
