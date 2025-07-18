package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFireball extends Item {
   public ItemFireball() {
      this.setCreativeTab(CreativeTabs.MISC);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      if (☃.isRemote) {
         return EnumActionResult.SUCCESS;
      } else {
         ☃ = ☃.offset(☃);
         ItemStack ☃ = ☃.getHeldItem(☃);
         if (!☃.canPlayerEdit(☃, ☃, ☃)) {
            return EnumActionResult.FAIL;
         } else {
            if (☃.getBlockState(☃).getMaterial() == Material.AIR) {
               ☃.playSound(null, ☃, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1.0F);
               ☃.setBlockState(☃, Blocks.FIRE.getDefaultState());
            }

            if (!☃.capabilities.isCreativeMode) {
               ☃.shrink(1);
            }

            return EnumActionResult.SUCCESS;
         }
      }
   }
}
