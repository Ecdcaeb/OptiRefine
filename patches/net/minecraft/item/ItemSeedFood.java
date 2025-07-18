package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSeedFood extends ItemFood {
   private final Block crops;
   private final Block soilId;

   public ItemSeedFood(int var1, float var2, Block var3, Block var4) {
      super(☃, ☃, false);
      this.crops = ☃;
      this.soilId = ☃;
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃ == EnumFacing.UP && ☃.canPlayerEdit(☃.offset(☃), ☃, ☃) && ☃.getBlockState(☃).getBlock() == this.soilId && ☃.isAirBlock(☃.up())) {
         ☃.setBlockState(☃.up(), this.crops.getDefaultState(), 11);
         ☃.shrink(1);
         return EnumActionResult.SUCCESS;
      } else {
         return EnumActionResult.FAIL;
      }
   }
}
