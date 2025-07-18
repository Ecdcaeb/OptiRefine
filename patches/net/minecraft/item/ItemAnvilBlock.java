package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAnvilBlock extends ItemMultiTexture {
   public ItemAnvilBlock(Block var1) {
      super(☃, ☃, new String[]{"intact", "slightlyDamaged", "veryDamaged"});
   }

   @Override
   public int getMetadata(int var1) {
      return ☃ << 2;
   }
}
