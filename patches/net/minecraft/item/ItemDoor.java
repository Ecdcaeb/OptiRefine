package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDoor extends Item {
   private final Block block;

   public ItemDoor(Block var1) {
      this.block = ☃;
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      if (☃ != EnumFacing.UP) {
         return EnumActionResult.FAIL;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃);
         Block ☃x = ☃.getBlock();
         if (!☃x.isReplaceable(☃, ☃)) {
            ☃ = ☃.offset(☃);
         }

         ItemStack ☃xx = ☃.getHeldItem(☃);
         if (☃.canPlayerEdit(☃, ☃, ☃xx) && this.block.canPlaceBlockAt(☃, ☃)) {
            EnumFacing ☃xxx = EnumFacing.fromAngle(☃.rotationYaw);
            int ☃xxxx = ☃xxx.getXOffset();
            int ☃xxxxx = ☃xxx.getZOffset();
            boolean ☃xxxxxx = ☃xxxx < 0 && ☃ < 0.5F || ☃xxxx > 0 && ☃ > 0.5F || ☃xxxxx < 0 && ☃ > 0.5F || ☃xxxxx > 0 && ☃ < 0.5F;
            placeDoor(☃, ☃, ☃xxx, this.block, ☃xxxxxx);
            SoundType ☃xxxxxxx = this.block.getSoundType();
            ☃.playSound(☃, ☃, ☃xxxxxxx.getPlaceSound(), SoundCategory.BLOCKS, (☃xxxxxxx.getVolume() + 1.0F) / 2.0F, ☃xxxxxxx.getPitch() * 0.8F);
            ☃xx.shrink(1);
            return EnumActionResult.SUCCESS;
         } else {
            return EnumActionResult.FAIL;
         }
      }
   }

   public static void placeDoor(World var0, BlockPos var1, EnumFacing var2, Block var3, boolean var4) {
      BlockPos ☃ = ☃.offset(☃.rotateY());
      BlockPos ☃x = ☃.offset(☃.rotateYCCW());
      int ☃xx = (☃.getBlockState(☃x).isNormalCube() ? 1 : 0) + (☃.getBlockState(☃x.up()).isNormalCube() ? 1 : 0);
      int ☃xxx = (☃.getBlockState(☃).isNormalCube() ? 1 : 0) + (☃.getBlockState(☃.up()).isNormalCube() ? 1 : 0);
      boolean ☃xxxx = ☃.getBlockState(☃x).getBlock() == ☃ || ☃.getBlockState(☃x.up()).getBlock() == ☃;
      boolean ☃xxxxx = ☃.getBlockState(☃).getBlock() == ☃ || ☃.getBlockState(☃.up()).getBlock() == ☃;
      if ((!☃xxxx || ☃xxxxx) && ☃xxx <= ☃xx) {
         if (☃xxxxx && !☃xxxx || ☃xxx < ☃xx) {
            ☃ = false;
         }
      } else {
         ☃ = true;
      }

      BlockPos ☃xxxxxx = ☃.up();
      boolean ☃xxxxxxx = ☃.isBlockPowered(☃) || ☃.isBlockPowered(☃xxxxxx);
      IBlockState ☃xxxxxxxx = ☃.getDefaultState()
         .withProperty(BlockDoor.FACING, ☃)
         .withProperty(BlockDoor.HINGE, ☃ ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT)
         .withProperty(BlockDoor.POWERED, ☃xxxxxxx)
         .withProperty(BlockDoor.OPEN, ☃xxxxxxx);
      ☃.setBlockState(☃, ☃xxxxxxxx.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
      ☃.setBlockState(☃xxxxxx, ☃xxxxxxxx.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
      ☃.notifyNeighborsOfStateChange(☃, ☃, false);
      ☃.notifyNeighborsOfStateChange(☃xxxxxx, ☃, false);
   }
}
