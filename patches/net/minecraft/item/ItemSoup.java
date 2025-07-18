package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemSoup extends ItemFood {
   public ItemSoup(int var1) {
      super(☃, false);
      this.setMaxStackSize(1);
   }

   @Override
   public ItemStack onItemUseFinish(ItemStack var1, World var2, EntityLivingBase var3) {
      super.onItemUseFinish(☃, ☃, ☃);
      return new ItemStack(Items.BOWL);
   }
}
