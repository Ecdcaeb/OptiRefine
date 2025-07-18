package net.minecraft.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public enum EnumHandSide {
   LEFT(new TextComponentTranslation("options.mainHand.left")),
   RIGHT(new TextComponentTranslation("options.mainHand.right"));

   private final ITextComponent handName;

   private EnumHandSide(ITextComponent var3) {
      this.handName = ☃;
   }

   public EnumHandSide opposite() {
      return this == LEFT ? RIGHT : LEFT;
   }

   @Override
   public String toString() {
      return this.handName.getUnformattedText();
   }
}
