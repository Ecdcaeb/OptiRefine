package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSeeds extends Item {
   private final Block crops;
   private final Block soilBlockID;

   public ItemSeeds(Block var1, Block var2) {
      this.crops = ☃;
      this.soilBlockID = ☃;
      this.setCreativeTab(CreativeTabs.MATERIALS);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃ == EnumFacing.UP && ☃.canPlayerEdit(☃.offset(☃), ☃, ☃) && ☃.getBlockState(☃).getBlock() == this.soilBlockID && ☃.isAirBlock(☃.up())) {
         ☃.setBlockState(☃.up(), this.crops.getDefaultState());
         if (☃ instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃.up(), ☃);
         }

         ☃.shrink(1);
         return EnumActionResult.SUCCESS;
      } else {
         return EnumActionResult.FAIL;
      }
   }
}
