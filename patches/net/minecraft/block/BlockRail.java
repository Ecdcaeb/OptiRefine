package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRail extends BlockRailBase {
   public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class);

   protected BlockRail() {
      super(false);
      this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
   }

   @Override
   protected void updateState(IBlockState var1, World var2, BlockPos var3, Block var4) {
      if (☃.getDefaultState().canProvidePower() && new BlockRailBase.Rail(☃, ☃, ☃).countAdjacentRails() == 3) {
         this.updateDir(☃, ☃, ☃, false);
      }
   }

   @Override
   public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
      return SHAPE;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(SHAPE).getMetadata();
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case CLOCKWISE_180:
            switch ((BlockRailBase.EnumRailDirection)☃.getValue(SHAPE)) {
               case ASCENDING_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
               case ASCENDING_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
               case ASCENDING_NORTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
               case ASCENDING_SOUTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
               case SOUTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
               case SOUTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
               case NORTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
               case NORTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
            }
         case COUNTERCLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection)☃.getValue(SHAPE)) {
               case ASCENDING_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
               case ASCENDING_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
               case ASCENDING_NORTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
               case ASCENDING_SOUTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
               case SOUTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
               case SOUTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
               case NORTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
               case NORTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
               case NORTH_SOUTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
               case EAST_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }
         case CLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection)☃.getValue(SHAPE)) {
               case ASCENDING_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
               case ASCENDING_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
               case ASCENDING_NORTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
               case ASCENDING_SOUTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
               case SOUTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
               case SOUTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
               case NORTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
               case NORTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
               case NORTH_SOUTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
               case EAST_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }
         default:
            return ☃;
      }
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      BlockRailBase.EnumRailDirection ☃ = ☃.getValue(SHAPE);
      switch (☃) {
         case LEFT_RIGHT:
            switch (☃) {
               case ASCENDING_NORTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
               case ASCENDING_SOUTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
               case SOUTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
               case SOUTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
               case NORTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
               case NORTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
               default:
                  return super.withMirror(☃, ☃);
            }
         case FRONT_BACK:
            switch (☃) {
               case ASCENDING_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
               case ASCENDING_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
               case ASCENDING_NORTH:
               case ASCENDING_SOUTH:
               default:
                  break;
               case SOUTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
               case SOUTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
               case NORTH_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
               case NORTH_EAST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }
      }

      return super.withMirror(☃, ☃);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, SHAPE);
   }
}
