package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemMultiTexture extends ItemBlock {
   protected final Block unused;
   protected final ItemMultiTexture.Mapper nameFunction;

   public ItemMultiTexture(Block var1, Block var2, ItemMultiTexture.Mapper var3) {
      super(☃);
      this.unused = ☃;
      this.nameFunction = ☃;
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public ItemMultiTexture(Block var1, Block var2, final String[] var3) {
      this(☃, ☃, new ItemMultiTexture.Mapper() {
         @Override
         public String apply(ItemStack var1) {
            int ☃ = ☃.getMetadata();
            if (☃ < 0 || ☃ >= ☃.length) {
               ☃ = 0;
            }

            return ☃[☃];
         }
      });
   }

   @Override
   public int getMetadata(int var1) {
      return ☃;
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      return super.getTranslationKey() + "." + this.nameFunction.apply(☃);
   }

   public interface Mapper {
      String apply(ItemStack var1);
   }
}
