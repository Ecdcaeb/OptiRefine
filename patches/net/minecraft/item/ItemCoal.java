package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.NonNullList;

public class ItemCoal extends Item {
   public ItemCoal() {
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(CreativeTabs.MATERIALS);
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      return ☃.getMetadata() == 1 ? "item.charcoal" : "item.coal";
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         ☃.add(new ItemStack(this, 1, 0));
         ☃.add(new ItemStack(this, 1, 1));
      }
   }
}
