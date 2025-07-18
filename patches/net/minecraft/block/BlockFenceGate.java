package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockHorizontal {
   public static final PropertyBool OPEN = PropertyBool.create("open");
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");
   protected static final AxisAlignedBB AABB_HITBOX_ZAXIS = new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.0, 0.625);
   protected static final AxisAlignedBB AABB_HITBOX_XAXIS = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.0, 1.0);
   protected static final AxisAlignedBB AABB_HITBOX_ZAXIS_INWALL = new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 0.8125, 0.625);
   protected static final AxisAlignedBB AABB_HITBOX_XAXIS_INWALL = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 0.8125, 1.0);
   protected static final AxisAlignedBB AABB_COLLISION_BOX_ZAXIS = new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.5, 0.625);
   protected static final AxisAlignedBB AABB_COLLISION_BOX_XAXIS = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.5, 1.0);

   public BlockFenceGate(BlockPlanks.EnumType var1) {
      super(Material.WOOD, ☃.getMapColor());
      this.setDefaultState(this.blockState.getBaseState().withProperty(OPEN, false).withProperty(POWERED, false).withProperty(IN_WALL, false));
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      ☃ = this.getActualState(☃, ☃, ☃);
      if (☃.getValue(IN_WALL)) {
         return ☃.getValue(FACING).getAxis() == EnumFacing.Axis.X ? AABB_HITBOX_XAXIS_INWALL : AABB_HITBOX_ZAXIS_INWALL;
      } else {
         return ☃.getValue(FACING).getAxis() == EnumFacing.Axis.X ? AABB_HITBOX_XAXIS : AABB_HITBOX_ZAXIS;
      }
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      EnumFacing.Axis ☃ = ☃.getValue(FACING).getAxis();
      if (☃ == EnumFacing.Axis.Z
            && (☃.getBlockState(☃.west()).getBlock() == Blocks.COBBLESTONE_WALL || ☃.getBlockState(☃.east()).getBlock() == Blocks.COBBLESTONE_WALL)
         || ☃ == EnumFacing.Axis.X
            && (☃.getBlockState(☃.north()).getBlock() == Blocks.COBBLESTONE_WALL || ☃.getBlockState(☃.south()).getBlock() == Blocks.COBBLESTONE_WALL)) {
         ☃ = ☃.withProperty(IN_WALL, true);
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
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return ☃.getBlockState(☃.down()).getMaterial().isSolid() ? super.canPlaceBlockAt(☃, ☃) : false;
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (☃.getValue(OPEN)) {
         return NULL_AABB;
      } else {
         return ☃.getValue(FACING).getAxis() == EnumFacing.Axis.Z ? AABB_COLLISION_BOX_ZAXIS : AABB_COLLISION_BOX_XAXIS;
      }
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
      return ☃.getBlockState(☃).getValue(OPEN);
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      boolean ☃ = ☃.isBlockPowered(☃);
      return this.getDefaultState().withProperty(FACING, ☃.getHorizontalFacing()).withProperty(OPEN, ☃).withProperty(POWERED, ☃).withProperty(IN_WALL, false);
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.getValue(OPEN)) {
         ☃ = ☃.withProperty(OPEN, false);
         ☃.setBlockState(☃, ☃, 10);
      } else {
         EnumFacing ☃ = EnumFacing.fromAngle(☃.rotationYaw);
         if (☃.getValue(FACING) == ☃.getOpposite()) {
            ☃ = ☃.withProperty(FACING, ☃);
         }

         ☃ = ☃.withProperty(OPEN, true);
         ☃.setBlockState(☃, ☃, 10);
      }

      ☃.playEvent(☃, ☃.getValue(OPEN) ? 1008 : 1014, ☃, 0);
      return true;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote) {
         boolean ☃ = ☃.isBlockPowered(☃);
         if (☃.getValue(POWERED) != ☃) {
            ☃.setBlockState(☃, ☃.withProperty(POWERED, ☃).withProperty(OPEN, ☃), 2);
            if (☃.getValue(OPEN) != ☃) {
               ☃.playEvent(null, ☃ ? 1008 : 1014, ☃, 0);
            }
         }
      }
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return true;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(☃)).withProperty(OPEN, (☃ & 4) != 0).withProperty(POWERED, (☃ & 8) != 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getHorizontalIndex();
      if (☃.getValue(POWERED)) {
         ☃ |= 8;
      }

      if (☃.getValue(OPEN)) {
         ☃ |= 4;
      }

      return ☃;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, OPEN, POWERED, IN_WALL);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      if (☃ != EnumFacing.UP && ☃ != EnumFacing.DOWN) {
         return ☃.getValue(FACING).getAxis() == ☃.rotateY().getAxis() ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.UNDEFINED;
      } else {
         return BlockFaceShape.UNDEFINED;
      }
   }
}
