package net.minecraft.util.text;

import java.util.function.Function;
import java.util.function.Supplier;

public class TextComponentKeybind extends TextComponentBase {
   public static Function<String, Supplier<String>> displaySupplierFunction = var0 -> () -> var0;
   private final String keybind;
   private Supplier<String> displaySupplier;

   public TextComponentKeybind(String var1) {
      this.keybind = ☃;
   }

   @Override
   public String getUnformattedComponentText() {
      if (this.displaySupplier == null) {
         this.displaySupplier = displaySupplierFunction.apply(this.keybind);
      }

      return this.displaySupplier.get();
   }

   public TextComponentKeybind createCopy() {
      TextComponentKeybind ☃ = new TextComponentKeybind(this.keybind);
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
      } else if (!(☃ instanceof TextComponentKeybind)) {
         return false;
      } else {
         TextComponentKeybind ☃ = (TextComponentKeybind)☃;
         return this.keybind.equals(☃.keybind) && super.equals(☃);
      }
   }

   @Override
   public String toString() {
      return "KeybindComponent{keybind='" + this.keybind + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
   }

   public String getKeybind() {
      return this.keybind;
   }
}
