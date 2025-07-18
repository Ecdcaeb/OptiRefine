package net.minecraft.stats;

import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;

public class StatCrafting extends StatBase {
   private final Item item;

   public StatCrafting(String var1, String var2, ITextComponent var3, Item var4) {
      super(☃ + ☃, ☃);
      this.item = ☃;
   }

   public Item getItem() {
      return this.item;
   }
}
