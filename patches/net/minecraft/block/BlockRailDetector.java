package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailDetector extends BlockRailBase {
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

   public BlockRailDetector() {
      super(true);
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false).withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
      this.setTickRandomly(true);
   }

   @Override
   public int tickRate(World var1) {
      return 20;
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (!☃.isRemote) {
         if (!☃.getValue(POWERED)) {
            this.updatePoweredState(☃, ☃, ☃);
         }
      }
   }

   @Override
   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote && ☃.getValue(POWERED)) {
         this.updatePoweredState(☃, ☃, ☃);
      }
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃.getValue(POWERED) ? 15 : 0;
   }

   @Override
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (!☃.getValue(POWERED)) {
         return 0;
      } else {
         return ☃ == EnumFacing.UP ? 15 : 0;
      }
   }

   private void updatePoweredState(World var1, BlockPos var2, IBlockState var3) {
      boolean ☃ = ☃.getValue(POWERED);
      boolean ☃x = false;
      List<EntityMinecart> ☃xx = this.findMinecarts(☃, ☃, EntityMinecart.class);
      if (!☃xx.isEmpty()) {
         ☃x = true;
      }

      if (☃x && !☃) {
         ☃.setBlockState(☃, ☃.withProperty(POWERED, true), 3);
         this.updateConnectedRails(☃, ☃, ☃, true);
         ☃.notifyNeighborsOfStateChange(☃, this, false);
         ☃.notifyNeighborsOfStateChange(☃.down(), this, false);
         ☃.markBlockRangeForRenderUpdate(☃, ☃);
      }

      if (!☃x && ☃) {
         ☃.setBlockState(☃, ☃.withProperty(POWERED, false), 3);
         this.updateConnectedRails(☃, ☃, ☃, false);
         ☃.notifyNeighborsOfStateChange(☃, this, false);
         ☃.notifyNeighborsOfStateChange(☃.down(), this, false);
         ☃.markBlockRangeForRenderUpdate(☃, ☃);
      }

      if (☃x) {
         ☃.scheduleUpdate(new BlockPos(☃), this, this.tickRate(☃));
      }

      ☃.updateComparatorOutputLevel(☃, this);
   }

   protected void updateConnectedRails(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      BlockRailBase.Rail ☃ = new BlockRailBase.Rail(☃, ☃, ☃);

      for (BlockPos ☃x : ☃.getConnectedRails()) {
         IBlockState ☃xx = ☃.getBlockState(☃x);
         if (☃xx != null) {
            ☃xx.neighborChanged(☃, ☃x, ☃xx.getBlock(), ☃);
         }
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(☃, ☃, ☃);
      this.updatePoweredState(☃, ☃, ☃);
   }

   @Override
   public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
      return SHAPE;
   }

   @Override
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return true;
   }

   @Override
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      if (☃.getValue(POWERED)) {
         List<EntityMinecartCommandBlock> ☃ = this.findMinecarts(☃, ☃, EntityMinecartCommandBlock.class);
         if (!☃.isEmpty()) {
            return ☃.get(0).getCommandBlockLogic().getSuccessCount();
         }

         List<EntityMinecart> ☃x = this.findMinecarts(☃, ☃, EntityMinecart.class, EntitySelectors.HAS_INVENTORY);
         if (!☃x.isEmpty()) {
            return Container.calcRedstoneFromInventory((IInventory)☃x.get(0));
         }
      }

      return 0;
   }

   protected <T extends EntityMinecart> List<T> findMinecarts(World var1, BlockPos var2, Class<T> var3, Predicate<Entity>... var4) {
      AxisAlignedBB ☃ = this.getDectectionBox(☃);
      return ☃.length != 1 ? ☃.getEntitiesWithinAABB(☃, ☃) : ☃.getEntitiesWithinAABB(☃, ☃, ☃[0]);
   }

   private AxisAlignedBB getDectectionBox(BlockPos var1) {
      float ☃ = 0.2F;
      return new AxisAlignedBB(☃.getX() + 0.2F, ☃.getY(), ☃.getZ() + 0.2F, ☃.getX() + 1 - 0.2F, ☃.getY() + 1 - 0.2F, ☃.getZ() + 1 - 0.2F);
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
      return new BlockStateContainer(this, SHAPE, POWERED);
   }
}
