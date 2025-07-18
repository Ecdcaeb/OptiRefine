package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.World;

public class ItemAir extends Item {
   private final Block block;

   public ItemAir(Block var1) {
      this.block = ☃;
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      return this.block.getTranslationKey();
   }

   @Override
   public String getTranslationKey() {
      return this.block.getTranslationKey();
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      super.addInformation(☃, ☃, ☃, ☃);
      this.block.addInformation(☃, ☃, ☃, ☃);
   }
}
