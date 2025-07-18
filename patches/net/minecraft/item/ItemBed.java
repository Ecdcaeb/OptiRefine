package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemBed extends Item {
   public ItemBed() {
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      if (☃.isRemote) {
         return EnumActionResult.SUCCESS;
      } else if (☃ != EnumFacing.UP) {
         return EnumActionResult.FAIL;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃);
         Block ☃x = ☃.getBlock();
         boolean ☃xx = ☃x.isReplaceable(☃, ☃);
         if (!☃xx) {
            ☃ = ☃.up();
         }

         int ☃xxx = MathHelper.floor(☃.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
         EnumFacing ☃xxxx = EnumFacing.byHorizontalIndex(☃xxx);
         BlockPos ☃xxxxx = ☃.offset(☃xxxx);
         ItemStack ☃xxxxxx = ☃.getHeldItem(☃);
         if (☃.canPlayerEdit(☃, ☃, ☃xxxxxx) && ☃.canPlayerEdit(☃xxxxx, ☃, ☃xxxxxx)) {
            IBlockState ☃xxxxxxx = ☃.getBlockState(☃xxxxx);
            boolean ☃xxxxxxxx = ☃xxxxxxx.getBlock().isReplaceable(☃, ☃xxxxx);
            boolean ☃xxxxxxxxx = ☃xx || ☃.isAirBlock(☃);
            boolean ☃xxxxxxxxxx = ☃xxxxxxxx || ☃.isAirBlock(☃xxxxx);
            if (☃xxxxxxxxx && ☃xxxxxxxxxx && ☃.getBlockState(☃.down()).isTopSolid() && ☃.getBlockState(☃xxxxx.down()).isTopSolid()) {
               IBlockState ☃xxxxxxxxxxx = Blocks.BED
                  .getDefaultState()
                  .withProperty(BlockBed.OCCUPIED, false)
                  .withProperty(BlockBed.FACING, ☃xxxx)
                  .withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
               ☃.setBlockState(☃, ☃xxxxxxxxxxx, 10);
               ☃.setBlockState(☃xxxxx, ☃xxxxxxxxxxx.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
               SoundType ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx.getBlock().getSoundType();
               ☃.playSound(
                  null, ☃, ☃xxxxxxxxxxxx.getPlaceSound(), SoundCategory.BLOCKS, (☃xxxxxxxxxxxx.getVolume() + 1.0F) / 2.0F, ☃xxxxxxxxxxxx.getPitch() * 0.8F
               );
               TileEntity ☃xxxxxxxxxxxxx = ☃.getTileEntity(☃xxxxx);
               if (☃xxxxxxxxxxxxx instanceof TileEntityBed) {
                  ((TileEntityBed)☃xxxxxxxxxxxxx).setItemValues(☃xxxxxx);
               }

               TileEntity ☃xxxxxxxxxxxxxx = ☃.getTileEntity(☃);
               if (☃xxxxxxxxxxxxxx instanceof TileEntityBed) {
                  ((TileEntityBed)☃xxxxxxxxxxxxxx).setItemValues(☃xxxxxx);
               }

               ☃.notifyNeighborsRespectDebug(☃, ☃x, false);
               ☃.notifyNeighborsRespectDebug(☃xxxxx, ☃xxxxxxx.getBlock(), false);
               if (☃ instanceof EntityPlayerMP) {
                  CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃, ☃xxxxxx);
               }

               ☃xxxxxx.shrink(1);
               return EnumActionResult.SUCCESS;
            } else {
               return EnumActionResult.FAIL;
            }
         } else {
            return EnumActionResult.FAIL;
         }
      }
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      return super.getTranslationKey() + "." + EnumDyeColor.byMetadata(☃.getMetadata()).getTranslationKey();
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         for (int ☃ = 0; ☃ < 16; ☃++) {
            ☃.add(new ItemStack(this, 1, ☃));
         }
      }
   }
}
