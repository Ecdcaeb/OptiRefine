package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemLilyPad extends ItemColored {
   public ItemLilyPad(Block var1) {
      super(☃, false);
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      RayTraceResult ☃x = this.rayTrace(☃, ☃, true);
      if (☃x == null) {
         return new ActionResult<>(EnumActionResult.PASS, ☃);
      } else {
         if (☃x.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos ☃xx = ☃x.getBlockPos();
            if (!☃.isBlockModifiable(☃, ☃xx) || !☃.canPlayerEdit(☃xx.offset(☃x.sideHit), ☃x.sideHit, ☃)) {
               return new ActionResult<>(EnumActionResult.FAIL, ☃);
            }

            BlockPos ☃xxx = ☃xx.up();
            IBlockState ☃xxxx = ☃.getBlockState(☃xx);
            if (☃xxxx.getMaterial() == Material.WATER && ☃xxxx.getValue(BlockLiquid.LEVEL) == 0 && ☃.isAirBlock(☃xxx)) {
               ☃.setBlockState(☃xxx, Blocks.WATERLILY.getDefaultState(), 11);
               if (☃ instanceof EntityPlayerMP) {
                  CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃xxx, ☃);
               }

               if (!☃.capabilities.isCreativeMode) {
                  ☃.shrink(1);
               }

               ☃.addStat(StatList.getObjectUseStats(this));
               ☃.playSound(☃, ☃xx, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
               return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
            }
         }

         return new ActionResult<>(EnumActionResult.FAIL, ☃);
      }
   }
}
