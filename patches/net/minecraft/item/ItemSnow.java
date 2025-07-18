package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSnow extends ItemBlock {
   public ItemSnow(Block var1) {
      super(☃);
      this.setMaxDamage(0);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.isEmpty() && ☃.canPlayerEdit(☃, ☃, ☃)) {
         IBlockState ☃x = ☃.getBlockState(☃);
         Block ☃xx = ☃x.getBlock();
         BlockPos ☃xxx = ☃;
         if ((☃ != EnumFacing.UP || ☃xx != this.block) && !☃xx.isReplaceable(☃, ☃)) {
            ☃xxx = ☃.offset(☃);
            ☃x = ☃.getBlockState(☃xxx);
            ☃xx = ☃x.getBlock();
         }

         if (☃xx == this.block) {
            int ☃xxxx = ☃x.getValue(BlockSnow.LAYERS);
            if (☃xxxx < 8) {
               IBlockState ☃xxxxx = ☃x.withProperty(BlockSnow.LAYERS, ☃xxxx + 1);
               AxisAlignedBB ☃xxxxxx = ☃xxxxx.getCollisionBoundingBox(☃, ☃xxx);
               if (☃xxxxxx != Block.NULL_AABB && ☃.checkNoEntityCollision(☃xxxxxx.offset(☃xxx)) && ☃.setBlockState(☃xxx, ☃xxxxx, 10)) {
                  SoundType ☃xxxxxxx = this.block.getSoundType();
                  ☃.playSound(☃, ☃xxx, ☃xxxxxxx.getPlaceSound(), SoundCategory.BLOCKS, (☃xxxxxxx.getVolume() + 1.0F) / 2.0F, ☃xxxxxxx.getPitch() * 0.8F);
                  if (☃ instanceof EntityPlayerMP) {
                     CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃, ☃);
                  }

                  ☃.shrink(1);
                  return EnumActionResult.SUCCESS;
               }
            }
         }

         return super.onItemUse(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else {
         return EnumActionResult.FAIL;
      }
   }

   @Override
   public int getMetadata(int var1) {
      return ☃;
   }
}
