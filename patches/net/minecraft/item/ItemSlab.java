package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
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

public class ItemSlab extends ItemBlock {
   private final BlockSlab singleSlab;
   private final BlockSlab doubleSlab;

   public ItemSlab(Block var1, BlockSlab var2, BlockSlab var3) {
      super(☃);
      this.singleSlab = ☃;
      this.doubleSlab = ☃;
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @Override
   public int getMetadata(int var1) {
      return ☃;
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      return this.singleSlab.getTranslationKey(☃.getMetadata());
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.isEmpty() && ☃.canPlayerEdit(☃.offset(☃), ☃, ☃)) {
         Comparable<?> ☃x = this.singleSlab.getTypeForItem(☃);
         IBlockState ☃xx = ☃.getBlockState(☃);
         if (☃xx.getBlock() == this.singleSlab) {
            IProperty<?> ☃xxx = this.singleSlab.getVariantProperty();
            Comparable<?> ☃xxxx = ☃xx.getValue((IProperty<Comparable<?>>)☃xxx);
            BlockSlab.EnumBlockHalf ☃xxxxx = ☃xx.getValue(BlockSlab.HALF);
            if ((☃ == EnumFacing.UP && ☃xxxxx == BlockSlab.EnumBlockHalf.BOTTOM || ☃ == EnumFacing.DOWN && ☃xxxxx == BlockSlab.EnumBlockHalf.TOP)
               && ☃xxxx == ☃x) {
               IBlockState ☃xxxxxx = this.makeState(☃xxx, ☃xxxx);
               AxisAlignedBB ☃xxxxxxx = ☃xxxxxx.getCollisionBoundingBox(☃, ☃);
               if (☃xxxxxxx != Block.NULL_AABB && ☃.checkNoEntityCollision(☃xxxxxxx.offset(☃)) && ☃.setBlockState(☃, ☃xxxxxx, 11)) {
                  SoundType ☃xxxxxxxx = this.doubleSlab.getSoundType();
                  ☃.playSound(☃, ☃, ☃xxxxxxxx.getPlaceSound(), SoundCategory.BLOCKS, (☃xxxxxxxx.getVolume() + 1.0F) / 2.0F, ☃xxxxxxxx.getPitch() * 0.8F);
                  ☃.shrink(1);
                  if (☃ instanceof EntityPlayerMP) {
                     CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃, ☃);
                  }
               }

               return EnumActionResult.SUCCESS;
            }
         }

         return this.tryPlace(☃, ☃, ☃, ☃.offset(☃), ☃x) ? EnumActionResult.SUCCESS : super.onItemUse(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      } else {
         return EnumActionResult.FAIL;
      }
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3, EntityPlayer var4, ItemStack var5) {
      IProperty<?> ☃ = this.singleSlab.getVariantProperty();
      Comparable<?> ☃x = this.singleSlab.getTypeForItem(☃);
      IBlockState ☃xx = ☃.getBlockState(☃);
      if (☃xx.getBlock() == this.singleSlab) {
         boolean ☃xxx = ☃xx.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
         if ((☃ == EnumFacing.UP && !☃xxx || ☃ == EnumFacing.DOWN && ☃xxx) && ☃x == ☃xx.getValue(☃)) {
            return true;
         }
      }

      BlockPos var11 = ☃.offset(☃);
      IBlockState ☃xxx = ☃.getBlockState(var11);
      return ☃xxx.getBlock() == this.singleSlab && ☃x == ☃xxx.getValue(☃) ? true : super.canPlaceBlockOnSide(☃, ☃, ☃, ☃, ☃);
   }

   private boolean tryPlace(EntityPlayer var1, ItemStack var2, World var3, BlockPos var4, Object var5) {
      IBlockState ☃ = ☃.getBlockState(☃);
      if (☃.getBlock() == this.singleSlab) {
         Comparable<?> ☃x = ☃.getValue((IProperty<Comparable<?>>)this.singleSlab.getVariantProperty());
         if (☃x == ☃) {
            IBlockState ☃xx = this.makeState(this.singleSlab.getVariantProperty(), ☃x);
            AxisAlignedBB ☃xxx = ☃xx.getCollisionBoundingBox(☃, ☃);
            if (☃xxx != Block.NULL_AABB && ☃.checkNoEntityCollision(☃xxx.offset(☃)) && ☃.setBlockState(☃, ☃xx, 11)) {
               SoundType ☃xxxx = this.doubleSlab.getSoundType();
               ☃.playSound(☃, ☃, ☃xxxx.getPlaceSound(), SoundCategory.BLOCKS, (☃xxxx.getVolume() + 1.0F) / 2.0F, ☃xxxx.getPitch() * 0.8F);
               ☃.shrink(1);
            }

            return true;
         }
      }

      return false;
   }

   protected <T extends Comparable<T>> IBlockState makeState(IProperty<T> var1, Comparable<?> var2) {
      return this.doubleSlab.getDefaultState().withProperty(☃, ☃);
   }
}
