package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered extends BlockRailBase {
   public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create(
      "shape",
      BlockRailBase.EnumRailDirection.class,
      new Predicate<BlockRailBase.EnumRailDirection>() {
         public boolean apply(@Nullable BlockRailBase.EnumRailDirection var1) {
            return ☃ != BlockRailBase.EnumRailDirection.NORTH_EAST
               && ☃ != BlockRailBase.EnumRailDirection.NORTH_WEST
               && ☃ != BlockRailBase.EnumRailDirection.SOUTH_EAST
               && ☃ != BlockRailBase.EnumRailDirection.SOUTH_WEST;
         }
      }
   );
   public static final PropertyBool POWERED = PropertyBool.create("powered");

   protected BlockRailPowered() {
      super(true);
      this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, false));
   }

   protected boolean findPoweredRailSignal(World var1, BlockPos var2, IBlockState var3, boolean var4, int var5) {
      if (☃ >= 8) {
         return false;
      } else {
         int ☃ = ☃.getX();
         int ☃x = ☃.getY();
         int ☃xx = ☃.getZ();
         boolean ☃xxx = true;
         BlockRailBase.EnumRailDirection ☃xxxx = ☃.getValue(SHAPE);
         switch (☃xxxx) {
            case NORTH_SOUTH:
               if (☃) {
                  ☃xx++;
               } else {
                  ☃xx--;
               }
               break;
            case EAST_WEST:
               if (☃) {
                  ☃--;
               } else {
                  ☃++;
               }
               break;
            case ASCENDING_EAST:
               if (☃) {
                  ☃--;
               } else {
                  ☃++;
                  ☃x++;
                  ☃xxx = false;
               }

               ☃xxxx = BlockRailBase.EnumRailDirection.EAST_WEST;
               break;
            case ASCENDING_WEST:
               if (☃) {
                  ☃--;
                  ☃x++;
                  ☃xxx = false;
               } else {
                  ☃++;
               }

               ☃xxxx = BlockRailBase.EnumRailDirection.EAST_WEST;
               break;
            case ASCENDING_NORTH:
               if (☃) {
                  ☃xx++;
               } else {
                  ☃xx--;
                  ☃x++;
                  ☃xxx = false;
               }

               ☃xxxx = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
               break;
            case ASCENDING_SOUTH:
               if (☃) {
                  ☃xx++;
                  ☃x++;
                  ☃xxx = false;
               } else {
                  ☃xx--;
               }

               ☃xxxx = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         }

         return this.isSameRailWithPower(☃, new BlockPos(☃, ☃x, ☃xx), ☃, ☃, ☃xxxx)
            ? true
            : ☃xxx && this.isSameRailWithPower(☃, new BlockPos(☃, ☃x - 1, ☃xx), ☃, ☃, ☃xxxx);
      }
   }

   protected boolean isSameRailWithPower(World var1, BlockPos var2, boolean var3, int var4, BlockRailBase.EnumRailDirection var5) {
      IBlockState ☃ = ☃.getBlockState(☃);
      if (☃.getBlock() != this) {
         return false;
      } else {
         BlockRailBase.EnumRailDirection ☃x = ☃.getValue(SHAPE);
         if (☃ != BlockRailBase.EnumRailDirection.EAST_WEST
            || ☃x != BlockRailBase.EnumRailDirection.NORTH_SOUTH
               && ☃x != BlockRailBase.EnumRailDirection.ASCENDING_NORTH
               && ☃x != BlockRailBase.EnumRailDirection.ASCENDING_SOUTH) {
            if (☃ != BlockRailBase.EnumRailDirection.NORTH_SOUTH
               || ☃x != BlockRailBase.EnumRailDirection.EAST_WEST
                  && ☃x != BlockRailBase.EnumRailDirection.ASCENDING_EAST
                  && ☃x != BlockRailBase.EnumRailDirection.ASCENDING_WEST) {
               if (!☃.getValue(POWERED)) {
                  return false;
               } else {
                  return ☃.isBlockPowered(☃) ? true : this.findPoweredRailSignal(☃, ☃, ☃, ☃, ☃ + 1);
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   @Override
   protected void updateState(IBlockState var1, World var2, BlockPos var3, Block var4) {
      boolean ☃ = ☃.getValue(POWERED);
      boolean ☃x = ☃.isBlockPowered(☃) || this.findPoweredRailSignal(☃, ☃, ☃, true, 0) || this.findPoweredRailSignal(☃, ☃, ☃, false, 0);
      if (☃x != ☃) {
         ☃.setBlockState(☃, ☃.withProperty(POWERED, ☃x), 3);
         ☃.notifyNeighborsOfStateChange(☃.down(), this, false);
         if (☃.getValue(SHAPE).isAscending()) {
            ☃.notifyNeighborsOfStateChange(☃.up(), this, false);
         }
      }
   }

   @Override
   public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
      return SHAPE;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(☃ & 7)).withProperty(POWERED, (☃ & 8) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(SHAPE).getMetadata();
      if (☃.getValue(POWERED)) {
         ☃ |= 8;
      }

      return ☃;
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
               case NORTH_SOUTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
               case EAST_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
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
            }
         case CLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection)☃.getValue(SHAPE)) {
               case NORTH_SOUTH:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
               case EAST_WEST:
                  return ☃.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
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
      return new BlockStateContainer(this, SHAPE, POWERED);
   }
}
