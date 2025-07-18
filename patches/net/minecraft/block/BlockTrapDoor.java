package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   public static final PropertyBool OPEN = PropertyBool.create("open");
   public static final PropertyEnum<BlockTrapDoor.DoorHalf> HALF = PropertyEnum.create("half", BlockTrapDoor.DoorHalf.class);
   protected static final AxisAlignedBB EAST_OPEN_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0);
   protected static final AxisAlignedBB WEST_OPEN_AABB = new AxisAlignedBB(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB SOUTH_OPEN_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875);
   protected static final AxisAlignedBB NORTH_OPEN_AABB = new AxisAlignedBB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0);
   protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.0, 0.8125, 0.0, 1.0, 1.0, 1.0);

   protected BlockTrapDoor(Material var1) {
      super(☃);
      this.setDefaultState(
         this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false).withProperty(HALF, BlockTrapDoor.DoorHalf.BOTTOM)
      );
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      AxisAlignedBB ☃;
      if (☃.getValue(OPEN)) {
         switch ((EnumFacing)☃.getValue(FACING)) {
            case NORTH:
            default:
               ☃ = NORTH_OPEN_AABB;
               break;
            case SOUTH:
               ☃ = SOUTH_OPEN_AABB;
               break;
            case WEST:
               ☃ = WEST_OPEN_AABB;
               break;
            case EAST:
               ☃ = EAST_OPEN_AABB;
         }
      } else if (☃.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP) {
         ☃ = TOP_AABB;
      } else {
         ☃ = BOTTOM_AABB;
      }

      return ☃;
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
   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return !☃.getBlockState(☃).getValue(OPEN);
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (this.material == Material.IRON) {
         return false;
      } else {
         ☃ = ☃.cycleProperty(OPEN);
         ☃.setBlockState(☃, ☃, 2);
         this.playSound(☃, ☃, ☃, ☃.getValue(OPEN));
         return true;
      }
   }

   protected void playSound(@Nullable EntityPlayer var1, World var2, BlockPos var3, boolean var4) {
      if (☃) {
         int ☃ = this.material == Material.IRON ? 1037 : 1007;
         ☃.playEvent(☃, ☃, ☃, 0);
      } else {
         int ☃ = this.material == Material.IRON ? 1036 : 1013;
         ☃.playEvent(☃, ☃, ☃, 0);
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote) {
         boolean ☃ = ☃.isBlockPowered(☃);
         if (☃ || ☃.getDefaultState().canProvidePower()) {
            boolean ☃x = ☃.getValue(OPEN);
            if (☃x != ☃) {
               ☃.setBlockState(☃, ☃.withProperty(OPEN, ☃), 2);
               this.playSound(null, ☃, ☃, ☃);
            }
         }
      }
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState ☃ = this.getDefaultState();
      if (☃.getAxis().isHorizontal()) {
         ☃ = ☃.withProperty(FACING, ☃).withProperty(OPEN, false);
         ☃ = ☃.withProperty(HALF, ☃ > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
      } else {
         ☃ = ☃.withProperty(FACING, ☃.getHorizontalFacing().getOpposite()).withProperty(OPEN, false);
         ☃ = ☃.withProperty(HALF, ☃ == EnumFacing.UP ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
      }

      if (☃.isBlockPowered(☃)) {
         ☃ = ☃.withProperty(OPEN, true);
      }

      return ☃;
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return true;
   }

   protected static EnumFacing getFacing(int var0) {
      switch (☃ & 3) {
         case 0:
            return EnumFacing.NORTH;
         case 1:
            return EnumFacing.SOUTH;
         case 2:
            return EnumFacing.WEST;
         case 3:
         default:
            return EnumFacing.EAST;
      }
   }

   protected static int getMetaForFacing(EnumFacing var0) {
      switch (☃) {
         case NORTH:
            return 0;
         case SOUTH:
            return 1;
         case WEST:
            return 2;
         case EAST:
         default:
            return 3;
      }
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState()
         .withProperty(FACING, getFacing(☃))
         .withProperty(OPEN, (☃ & 4) != 0)
         .withProperty(HALF, (☃ & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= getMetaForFacing(☃.getValue(FACING));
      if (☃.getValue(OPEN)) {
         ☃ |= 4;
      }

      if (☃.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP) {
         ☃ |= 8;
      }

      return ☃;
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withRotation(☃.toRotation(☃.getValue(FACING)));
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, OPEN, HALF);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return (☃ == EnumFacing.UP && ☃.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP || ☃ == EnumFacing.DOWN && ☃.getValue(HALF) == BlockTrapDoor.DoorHalf.BOTTOM)
            && !☃.getValue(OPEN)
         ? BlockFaceShape.SOLID
         : BlockFaceShape.UNDEFINED;
   }

   public static enum DoorHalf implements IStringSerializable {
      TOP("top"),
      BOTTOM("bottom");

      private final String name;

      private DoorHalf(String var3) {
         this.name = ☃;
      }

      @Override
      public String toString() {
         return this.name;
      }

      @Override
      public String getName() {
         return this.name;
      }
   }
}
