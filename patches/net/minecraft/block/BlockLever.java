package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block {
   public static final PropertyEnum<BlockLever.EnumOrientation> FACING = PropertyEnum.create("facing", BlockLever.EnumOrientation.class);
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   protected static final AxisAlignedBB LEVER_NORTH_AABB = new AxisAlignedBB(0.3125, 0.2F, 0.625, 0.6875, 0.8F, 1.0);
   protected static final AxisAlignedBB LEVER_SOUTH_AABB = new AxisAlignedBB(0.3125, 0.2F, 0.0, 0.6875, 0.8F, 0.375);
   protected static final AxisAlignedBB LEVER_WEST_AABB = new AxisAlignedBB(0.625, 0.2F, 0.3125, 1.0, 0.8F, 0.6875);
   protected static final AxisAlignedBB LEVER_EAST_AABB = new AxisAlignedBB(0.0, 0.2F, 0.3125, 0.375, 0.8F, 0.6875);
   protected static final AxisAlignedBB LEVER_UP_AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 0.6F, 0.75);
   protected static final AxisAlignedBB LEVER_DOWN_AABB = new AxisAlignedBB(0.25, 0.4F, 0.25, 0.75, 1.0, 0.75);

   protected BlockLever() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, BlockLever.EnumOrientation.NORTH).withProperty(POWERED, false));
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return canAttachTo(☃, ☃, ☃);
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      for (EnumFacing ☃ : EnumFacing.values()) {
         if (canAttachTo(☃, ☃, ☃)) {
            return true;
         }
      }

      return false;
   }

   protected static boolean canAttachTo(World var0, BlockPos var1, EnumFacing var2) {
      return BlockButton.canPlaceBlock(☃, ☃, ☃);
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState ☃ = this.getDefaultState().withProperty(POWERED, false);
      if (canAttachTo(☃, ☃, ☃)) {
         return ☃.withProperty(FACING, BlockLever.EnumOrientation.forFacings(☃, ☃.getHorizontalFacing()));
      } else {
         for (EnumFacing ☃x : EnumFacing.Plane.HORIZONTAL) {
            if (☃x != ☃ && canAttachTo(☃, ☃, ☃x)) {
               return ☃.withProperty(FACING, BlockLever.EnumOrientation.forFacings(☃x, ☃.getHorizontalFacing()));
            }
         }

         return ☃.getBlockState(☃.down()).isTopSolid()
            ? ☃.withProperty(FACING, BlockLever.EnumOrientation.forFacings(EnumFacing.UP, ☃.getHorizontalFacing()))
            : ☃;
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (this.checkCanSurvive(☃, ☃, ☃) && !canAttachTo(☃, ☃, ☃.getValue(FACING).getFacing())) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
      }
   }

   private boolean checkCanSurvive(World var1, BlockPos var2, IBlockState var3) {
      if (this.canPlaceBlockAt(☃, ☃)) {
         return true;
      } else {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
         return false;
      }
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      switch ((BlockLever.EnumOrientation)☃.getValue(FACING)) {
         case EAST:
         default:
            return LEVER_EAST_AABB;
         case WEST:
            return LEVER_WEST_AABB;
         case SOUTH:
            return LEVER_SOUTH_AABB;
         case NORTH:
            return LEVER_NORTH_AABB;
         case UP_Z:
         case UP_X:
            return LEVER_UP_AABB;
         case DOWN_X:
         case DOWN_Z:
            return LEVER_DOWN_AABB;
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isRemote) {
         return true;
      } else {
         ☃ = ☃.cycleProperty(POWERED);
         ☃.setBlockState(☃, ☃, 3);
         float ☃ = ☃.getValue(POWERED) ? 0.6F : 0.5F;
         ☃.playSound(null, ☃, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, ☃);
         ☃.notifyNeighborsOfStateChange(☃, this, false);
         EnumFacing ☃x = ☃.getValue(FACING).getFacing();
         ☃.notifyNeighborsOfStateChange(☃.offset(☃x.getOpposite()), this, false);
         return true;
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (☃.getValue(POWERED)) {
         ☃.notifyNeighborsOfStateChange(☃, this, false);
         EnumFacing ☃ = ☃.getValue(FACING).getFacing();
         ☃.notifyNeighborsOfStateChange(☃.offset(☃.getOpposite()), this, false);
      }

      super.breakBlock(☃, ☃, ☃);
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
         return ☃.getValue(FACING).getFacing() == ☃ ? 15 : 0;
      }
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, BlockLever.EnumOrientation.byMetadata(☃ & 7)).withProperty(POWERED, (☃ & 8) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getMetadata();
      if (☃.getValue(POWERED)) {
         ☃ |= 8;
      }

      return ☃;
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case CLOCKWISE_180:
            switch ((BlockLever.EnumOrientation)☃.getValue(FACING)) {
               case EAST:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.WEST);
               case WEST:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.EAST);
               case SOUTH:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.NORTH);
               case NORTH:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.SOUTH);
               default:
                  return ☃;
            }
         case COUNTERCLOCKWISE_90:
            switch ((BlockLever.EnumOrientation)☃.getValue(FACING)) {
               case EAST:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.NORTH);
               case WEST:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.SOUTH);
               case SOUTH:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.EAST);
               case NORTH:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.WEST);
               case UP_Z:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.UP_X);
               case UP_X:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.UP_Z);
               case DOWN_X:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.DOWN_Z);
               case DOWN_Z:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.DOWN_X);
            }
         case CLOCKWISE_90:
            switch ((BlockLever.EnumOrientation)☃.getValue(FACING)) {
               case EAST:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.SOUTH);
               case WEST:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.NORTH);
               case SOUTH:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.WEST);
               case NORTH:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.EAST);
               case UP_Z:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.UP_X);
               case UP_X:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.UP_Z);
               case DOWN_X:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.DOWN_Z);
               case DOWN_Z:
                  return ☃.withProperty(FACING, BlockLever.EnumOrientation.DOWN_X);
            }
         default:
            return ☃;
      }
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withRotation(☃.toRotation(☃.getValue(FACING).getFacing()));
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, POWERED);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }

   public static enum EnumOrientation implements IStringSerializable {
      DOWN_X(0, "down_x", EnumFacing.DOWN),
      EAST(1, "east", EnumFacing.EAST),
      WEST(2, "west", EnumFacing.WEST),
      SOUTH(3, "south", EnumFacing.SOUTH),
      NORTH(4, "north", EnumFacing.NORTH),
      UP_Z(5, "up_z", EnumFacing.UP),
      UP_X(6, "up_x", EnumFacing.UP),
      DOWN_Z(7, "down_z", EnumFacing.DOWN);

      private static final BlockLever.EnumOrientation[] META_LOOKUP = new BlockLever.EnumOrientation[values().length];
      private final int meta;
      private final String name;
      private final EnumFacing facing;

      private EnumOrientation(int var3, String var4, EnumFacing var5) {
         this.meta = ☃;
         this.name = ☃;
         this.facing = ☃;
      }

      public int getMetadata() {
         return this.meta;
      }

      public EnumFacing getFacing() {
         return this.facing;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockLever.EnumOrientation byMetadata(int var0) {
         if (☃ < 0 || ☃ >= META_LOOKUP.length) {
            ☃ = 0;
         }

         return META_LOOKUP[☃];
      }

      public static BlockLever.EnumOrientation forFacings(EnumFacing var0, EnumFacing var1) {
         switch (☃) {
            case DOWN:
               switch (☃.getAxis()) {
                  case X:
                     return DOWN_X;
                  case Z:
                     return DOWN_Z;
                  default:
                     throw new IllegalArgumentException("Invalid entityFacing " + ☃ + " for facing " + ☃);
               }
            case UP:
               switch (☃.getAxis()) {
                  case X:
                     return UP_X;
                  case Z:
                     return UP_Z;
                  default:
                     throw new IllegalArgumentException("Invalid entityFacing " + ☃ + " for facing " + ☃);
               }
            case NORTH:
               return NORTH;
            case SOUTH:
               return SOUTH;
            case WEST:
               return WEST;
            case EAST:
               return EAST;
            default:
               throw new IllegalArgumentException("Invalid facing: " + ☃);
         }
      }

      @Override
      public String getName() {
         return this.name;
      }

      static {
         for (BlockLever.EnumOrientation ☃ : values()) {
            META_LOOKUP[☃.getMetadata()] = ☃;
         }
      }
   }
}
