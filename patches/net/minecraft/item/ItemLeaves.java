package net.minecraft.item;

import net.minecraft.block.BlockLeaves;

public class ItemLeaves extends ItemBlock {
   private final BlockLeaves leaves;

   public ItemLeaves(BlockLeaves var1) {
      super(☃);
      this.leaves = ☃;
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @Override
   public int getMetadata(int var1) {
      return ☃ | 4;
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      return super.getTranslationKey() + "." + this.leaves.getWoodType(☃.getMetadata()).getTranslationKey();
   }
}
