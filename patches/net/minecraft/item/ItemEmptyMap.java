package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEmptyMap extends ItemMapBase {
   protected ItemEmptyMap() {
      this.setCreativeTab(CreativeTabs.MISC);
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ItemMap.setupNewMap(☃, ☃.posX, ☃.posZ, (byte)0, true, false);
      ItemStack ☃x = ☃.getHeldItem(☃);
      ☃x.shrink(1);
      if (☃x.isEmpty()) {
         return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
      } else {
         if (!☃.inventory.addItemStackToInventory(☃.copy())) {
            ☃.dropItem(☃, false);
         }

         ☃.addStat(StatList.getObjectUseStats(this));
         return new ActionResult<>(EnumActionResult.SUCCESS, ☃x);
      }
   }
}
