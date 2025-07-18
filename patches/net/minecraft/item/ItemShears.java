package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShears extends Item {
   public ItemShears() {
      this.setMaxStackSize(1);
      this.setMaxDamage(238);
      this.setCreativeTab(CreativeTabs.TOOLS);
   }

   @Override
   public boolean onBlockDestroyed(ItemStack var1, World var2, IBlockState var3, BlockPos var4, EntityLivingBase var5) {
      if (!☃.isRemote) {
         ☃.damageItem(1, ☃);
      }

      Block ☃ = ☃.getBlock();
      return ☃.getMaterial() != Material.LEAVES && ☃ != Blocks.WEB && ☃ != Blocks.TALLGRASS && ☃ != Blocks.VINE && ☃ != Blocks.TRIPWIRE && ☃ != Blocks.WOOL
         ? super.onBlockDestroyed(☃, ☃, ☃, ☃, ☃)
         : true;
   }

   @Override
   public boolean canHarvestBlock(IBlockState var1) {
      Block ☃ = ☃.getBlock();
      return ☃ == Blocks.WEB || ☃ == Blocks.REDSTONE_WIRE || ☃ == Blocks.TRIPWIRE;
   }

   @Override
   public float getDestroySpeed(ItemStack var1, IBlockState var2) {
      Block ☃ = ☃.getBlock();
      if (☃ == Blocks.WEB || ☃.getMaterial() == Material.LEAVES) {
         return 15.0F;
      } else {
         return ☃ == Blocks.WOOL ? 5.0F : super.getDestroySpeed(☃, ☃);
      }
   }
}
