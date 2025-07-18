package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockSpecial extends Item {
   private final Block block;

   public ItemBlockSpecial(Block var1) {
      this.block = ☃;
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      IBlockState ☃ = ☃.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      if (☃x == Blocks.SNOW_LAYER && ☃.getValue(BlockSnow.LAYERS) < 1) {
         ☃ = EnumFacing.UP;
      } else if (!☃x.isReplaceable(☃, ☃)) {
         ☃ = ☃.offset(☃);
      }

      ItemStack ☃xx = ☃.getHeldItem(☃);
      if (!☃xx.isEmpty() && ☃.canPlayerEdit(☃, ☃, ☃xx) && ☃.mayPlace(this.block, ☃, false, ☃, null)) {
         IBlockState ☃xxx = this.block.getStateForPlacement(☃, ☃, ☃, ☃, ☃, ☃, 0, ☃);
         if (!☃.setBlockState(☃, ☃xxx, 11)) {
            return EnumActionResult.FAIL;
         } else {
            ☃xxx = ☃.getBlockState(☃);
            if (☃xxx.getBlock() == this.block) {
               ItemBlock.setTileEntityNBT(☃, ☃, ☃, ☃xx);
               ☃xxx.getBlock().onBlockPlacedBy(☃, ☃, ☃xxx, ☃, ☃xx);
               if (☃ instanceof EntityPlayerMP) {
                  CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃, ☃xx);
               }
            }

            SoundType ☃xxxx = this.block.getSoundType();
            ☃.playSound(☃, ☃, ☃xxxx.getPlaceSound(), SoundCategory.BLOCKS, (☃xxxx.getVolume() + 1.0F) / 2.0F, ☃xxxx.getPitch() * 0.8F);
            ☃xx.shrink(1);
            return EnumActionResult.SUCCESS;
         }
      } else {
         return EnumActionResult.FAIL;
      }
   }
}
