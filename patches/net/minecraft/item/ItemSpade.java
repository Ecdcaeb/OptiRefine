package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpade extends ItemTool {
   private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(
      new Block[]{
         Blocks.CLAY,
         Blocks.DIRT,
         Blocks.FARMLAND,
         Blocks.GRASS,
         Blocks.GRAVEL,
         Blocks.MYCELIUM,
         Blocks.SAND,
         Blocks.SNOW,
         Blocks.SNOW_LAYER,
         Blocks.SOUL_SAND,
         Blocks.GRASS_PATH,
         Blocks.CONCRETE_POWDER
      }
   );

   public ItemSpade(Item.ToolMaterial var1) {
      super(1.5F, -3.0F, ☃, EFFECTIVE_ON);
   }

   @Override
   public boolean canHarvestBlock(IBlockState var1) {
      Block ☃ = ☃.getBlock();
      return ☃ == Blocks.SNOW_LAYER ? true : ☃ == Blocks.SNOW;
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.canPlayerEdit(☃.offset(☃), ☃, ☃)) {
         return EnumActionResult.FAIL;
      } else {
         IBlockState ☃x = ☃.getBlockState(☃);
         Block ☃xx = ☃x.getBlock();
         if (☃ != EnumFacing.DOWN && ☃.getBlockState(☃.up()).getMaterial() == Material.AIR && ☃xx == Blocks.GRASS) {
            IBlockState ☃xxx = Blocks.GRASS_PATH.getDefaultState();
            ☃.playSound(☃, ☃, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!☃.isRemote) {
               ☃.setBlockState(☃, ☃xxx, 11);
               ☃.damageItem(1, ☃);
            }

            return EnumActionResult.SUCCESS;
         } else {
            return EnumActionResult.PASS;
         }
      }
   }
}
