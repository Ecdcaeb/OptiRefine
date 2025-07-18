package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension extends BlockDirectional {
   public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = PropertyEnum.create("type", BlockPistonExtension.EnumPistonType.class);
   public static final PropertyBool SHORT = PropertyBool.create("short");
   protected static final AxisAlignedBB PISTON_EXTENSION_EAST_AABB = new AxisAlignedBB(0.75, 0.0, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB PISTON_EXTENSION_WEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.25, 1.0, 1.0);
   protected static final AxisAlignedBB PISTON_EXTENSION_SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.75, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB PISTON_EXTENSION_NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.25);
   protected static final AxisAlignedBB PISTON_EXTENSION_UP_AABB = new AxisAlignedBB(0.0, 0.75, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB PISTON_EXTENSION_DOWN_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);
   protected static final AxisAlignedBB UP_ARM_AABB = new AxisAlignedBB(0.375, -0.25, 0.375, 0.625, 0.75, 0.625);
   protected static final AxisAlignedBB DOWN_ARM_AABB = new AxisAlignedBB(0.375, 0.25, 0.375, 0.625, 1.25, 0.625);
   protected static final AxisAlignedBB SOUTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, -0.25, 0.625, 0.625, 0.75);
   protected static final AxisAlignedBB NORTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, 0.25, 0.625, 0.625, 1.25);
   protected static final AxisAlignedBB EAST_ARM_AABB = new AxisAlignedBB(-0.25, 0.375, 0.375, 0.75, 0.625, 0.625);
   protected static final AxisAlignedBB WEST_ARM_AABB = new AxisAlignedBB(0.25, 0.375, 0.375, 1.25, 0.625, 0.625);
   protected static final AxisAlignedBB SHORT_UP_ARM_AABB = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.75, 0.625);
   protected static final AxisAlignedBB SHORT_DOWN_ARM_AABB = new AxisAlignedBB(0.375, 0.25, 0.375, 0.625, 1.0, 0.625);
   protected static final AxisAlignedBB SHORT_SOUTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, 0.0, 0.625, 0.625, 0.75);
   protected static final AxisAlignedBB SHORT_NORTH_ARM_AABB = new AxisAlignedBB(0.375, 0.375, 0.25, 0.625, 0.625, 1.0);
   protected static final AxisAlignedBB SHORT_EAST_ARM_AABB = new AxisAlignedBB(0.0, 0.375, 0.375, 0.75, 0.625, 0.625);
   protected static final AxisAlignedBB SHORT_WEST_ARM_AABB = new AxisAlignedBB(0.25, 0.375, 0.375, 1.0, 0.625, 0.625);

   public BlockPistonExtension() {
      super(Material.PISTON);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT)
            .withProperty(SHORT, false)
      );
      this.setSoundType(SoundType.STONE);
      this.setHardness(0.5F);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      switch ((EnumFacing)☃.getValue(FACING)) {
         case DOWN:
         default:
            return PISTON_EXTENSION_DOWN_AABB;
         case UP:
            return PISTON_EXTENSION_UP_AABB;
         case NORTH:
            return PISTON_EXTENSION_NORTH_AABB;
         case SOUTH:
            return PISTON_EXTENSION_SOUTH_AABB;
         case WEST:
            return PISTON_EXTENSION_WEST_AABB;
         case EAST:
            return PISTON_EXTENSION_EAST_AABB;
      }
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      addCollisionBoxToList(☃, ☃, ☃, ☃.getBoundingBox(☃, ☃));
      addCollisionBoxToList(☃, ☃, ☃, this.getArmShape(☃));
   }

   private AxisAlignedBB getArmShape(IBlockState var1) {
      boolean ☃ = ☃.getValue(SHORT);
      switch ((EnumFacing)☃.getValue(FACING)) {
         case DOWN:
         default:
            return ☃ ? SHORT_DOWN_ARM_AABB : DOWN_ARM_AABB;
         case UP:
            return ☃ ? SHORT_UP_ARM_AABB : UP_ARM_AABB;
         case NORTH:
            return ☃ ? SHORT_NORTH_ARM_AABB : NORTH_ARM_AABB;
         case SOUTH:
            return ☃ ? SHORT_SOUTH_ARM_AABB : SOUTH_ARM_AABB;
         case WEST:
            return ☃ ? SHORT_WEST_ARM_AABB : WEST_ARM_AABB;
         case EAST:
            return ☃ ? SHORT_EAST_ARM_AABB : EAST_ARM_AABB;
      }
   }

   @Override
   public boolean isTopSolid(IBlockState var1) {
      return ☃.getValue(FACING) == EnumFacing.UP;
   }

   @Override
   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      if (☃.capabilities.isCreativeMode) {
         BlockPos ☃ = ☃.offset(☃.getValue(FACING).getOpposite());
         Block ☃x = ☃.getBlockState(☃).getBlock();
         if (☃x == Blocks.PISTON || ☃x == Blocks.STICKY_PISTON) {
            ☃.setBlockToAir(☃);
         }
      }

      super.onBlockHarvested(☃, ☃, ☃, ☃);
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(☃, ☃, ☃);
      EnumFacing ☃ = ☃.getValue(FACING).getOpposite();
      ☃ = ☃.offset(☃);
      IBlockState ☃x = ☃.getBlockState(☃);
      if ((☃x.getBlock() == Blocks.PISTON || ☃x.getBlock() == Blocks.STICKY_PISTON) && ☃x.getValue(BlockPistonBase.EXTENDED)) {
         ☃x.getBlock().dropBlockAsItem(☃, ☃, ☃x, 0);
         ☃.setBlockToAir(☃);
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
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return false;
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return false;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      EnumFacing ☃ = ☃.getValue(FACING);
      BlockPos ☃x = ☃.offset(☃.getOpposite());
      IBlockState ☃xx = ☃.getBlockState(☃x);
      if (☃xx.getBlock() != Blocks.PISTON && ☃xx.getBlock() != Blocks.STICKY_PISTON) {
         ☃.setBlockToAir(☃);
      } else {
         ☃xx.neighborChanged(☃, ☃x, ☃, ☃);
      }
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return true;
   }

   @Nullable
   public static EnumFacing getFacing(int var0) {
      int ☃ = ☃ & 7;
      return ☃ > 5 ? null : EnumFacing.byIndex(☃);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(☃.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY ? Blocks.STICKY_PISTON : Blocks.PISTON);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState()
         .withProperty(FACING, getFacing(☃))
         .withProperty(TYPE, (☃ & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getIndex();
      if (☃.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
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
      return new BlockStateContainer(this, FACING, TYPE, SHORT);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return ☃ == ☃.getValue(FACING) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }

   public static enum EnumPistonType implements IStringSerializable {
      DEFAULT("normal"),
      STICKY("sticky");

      private final String VARIANT;

      private EnumPistonType(String var3) {
         this.VARIANT = ☃;
      }

      @Override
      public String toString() {
         return this.VARIANT;
      }

      @Override
      public String getName() {
         return this.VARIANT;
      }
   }
}
