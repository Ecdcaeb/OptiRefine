package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockNewLog extends BlockLog {
   public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create(
      "variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
         public boolean apply(@Nullable BlockPlanks.EnumType var1) {
            return ☃.getMetadata() >= 4;
         }
      }
   );

   public BlockNewLog() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      BlockPlanks.EnumType ☃ = ☃.getValue(VARIANT);
      switch ((BlockLog.EnumAxis)☃.getValue(LOG_AXIS)) {
         case X:
         case Z:
         case NONE:
         default:
            switch (☃) {
               case ACACIA:
               default:
                  return MapColor.STONE;
               case DARK_OAK:
                  return BlockPlanks.EnumType.DARK_OAK.getMapColor();
            }
         case Y:
            return ☃.getMapColor();
      }
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      ☃.add(new ItemStack(this, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
      ☃.add(new ItemStack(this, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      IBlockState ☃ = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((☃ & 3) + 4));
      switch (☃ & 12) {
         case 0:
            ☃ = ☃.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
            break;
         case 4:
            ☃ = ☃.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
            break;
         case 8:
            ☃ = ☃.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
            break;
         default:
            ☃ = ☃.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
      }

      return ☃;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(VARIANT).getMetadata() - 4;
      switch ((BlockLog.EnumAxis)☃.getValue(LOG_AXIS)) {
         case X:
            ☃ |= 4;
            break;
         case Z:
            ☃ |= 8;
            break;
         case NONE:
            ☃ |= 12;
      }

      return ☃;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, VARIANT, LOG_AXIS);
   }

   @Override
   protected ItemStack getSilkTouchDrop(IBlockState var1) {
      return new ItemStack(Item.getItemFromBlock(this), 1, ☃.getValue(VARIANT).getMetadata() - 4);
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata() - 4;
   }
}
