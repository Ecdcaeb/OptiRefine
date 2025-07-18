package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFlintAndSteel extends Item {
   public ItemFlintAndSteel() {
      this.maxStackSize = 1;
      this.setMaxDamage(64);
      this.setCreativeTab(CreativeTabs.TOOLS);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ☃ = ☃.offset(☃);
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.canPlayerEdit(☃, ☃, ☃)) {
         return EnumActionResult.FAIL;
      } else {
         if (☃.getBlockState(☃).getMaterial() == Material.AIR) {
            ☃.playSound(☃, ☃, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
            ☃.setBlockState(☃, Blocks.FIRE.getDefaultState(), 11);
         }

         if (☃ instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃, ☃);
         }

         ☃.damageItem(1, ☃);
         return EnumActionResult.SUCCESS;
      }
   }
}
