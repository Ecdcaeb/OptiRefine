package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRedstone extends Item {
   public ItemRedstone() {
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      boolean ☃ = ☃.getBlockState(☃).getBlock().isReplaceable(☃, ☃);
      BlockPos ☃x = ☃ ? ☃ : ☃.offset(☃);
      ItemStack ☃xx = ☃.getHeldItem(☃);
      if (☃.canPlayerEdit(☃x, ☃, ☃xx) && ☃.mayPlace(☃.getBlockState(☃x).getBlock(), ☃x, false, ☃, null) && Blocks.REDSTONE_WIRE.canPlaceBlockAt(☃, ☃x)) {
         ☃.setBlockState(☃x, Blocks.REDSTONE_WIRE.getDefaultState());
         if (☃ instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃x, ☃xx);
         }

         ☃xx.shrink(1);
         return EnumActionResult.SUCCESS;
      } else {
         return EnumActionResult.FAIL;
      }
   }
}
