package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemSign extends Item {
   public ItemSign() {
      this.maxStackSize = 16;
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      IBlockState ☃ = ☃.getBlockState(☃);
      boolean ☃x = ☃.getBlock().isReplaceable(☃, ☃);
      if (☃ != EnumFacing.DOWN && (☃.getMaterial().isSolid() || ☃x) && (!☃x || ☃ == EnumFacing.UP)) {
         ☃ = ☃.offset(☃);
         ItemStack ☃xx = ☃.getHeldItem(☃);
         if (!☃.canPlayerEdit(☃, ☃, ☃xx) || !Blocks.STANDING_SIGN.canPlaceBlockAt(☃, ☃)) {
            return EnumActionResult.FAIL;
         } else if (☃.isRemote) {
            return EnumActionResult.SUCCESS;
         } else {
            ☃ = ☃x ? ☃.down() : ☃;
            if (☃ == EnumFacing.UP) {
               int ☃xxx = MathHelper.floor((☃.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5) & 15;
               ☃.setBlockState(☃, Blocks.STANDING_SIGN.getDefaultState().withProperty(BlockStandingSign.ROTATION, ☃xxx), 11);
            } else {
               ☃.setBlockState(☃, Blocks.WALL_SIGN.getDefaultState().withProperty(BlockWallSign.FACING, ☃), 11);
            }

            TileEntity ☃xxx = ☃.getTileEntity(☃);
            if (☃xxx instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(☃, ☃, ☃, ☃xx)) {
               ☃.openEditSign((TileEntitySign)☃xxx);
            }

            if (☃ instanceof EntityPlayerMP) {
               CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃, ☃xx);
            }

            ☃xx.shrink(1);
            return EnumActionResult.SUCCESS;
         }
      } else {
         return EnumActionResult.FAIL;
      }
   }
}
