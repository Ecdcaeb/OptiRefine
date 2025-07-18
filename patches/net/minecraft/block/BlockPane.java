package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane extends Block {
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[]{
      new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 1.0, 0.5625),
      new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.4375, 0.5625, 1.0, 0.5625),
      new AxisAlignedBB(0.0, 0.0, 0.4375, 0.5625, 1.0, 1.0),
      new AxisAlignedBB(0.4375, 0.0, 0.0, 0.5625, 1.0, 0.5625),
      new AxisAlignedBB(0.4375, 0.0, 0.0, 0.5625, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.5625, 1.0, 0.5625),
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.5625, 1.0, 1.0),
      new AxisAlignedBB(0.4375, 0.0, 0.4375, 1.0, 1.0, 0.5625),
      new AxisAlignedBB(0.4375, 0.0, 0.4375, 1.0, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.4375, 1.0, 1.0, 0.5625),
      new AxisAlignedBB(0.0, 0.0, 0.4375, 1.0, 1.0, 1.0),
      new AxisAlignedBB(0.4375, 0.0, 0.0, 1.0, 1.0, 0.5625),
      new AxisAlignedBB(0.4375, 0.0, 0.0, 1.0, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.5625),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
   };
   private final boolean canDrop;

   protected BlockPane(Material var1, boolean var2) {
      super(☃);
      this.setDefaultState(
         this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false)
      );
      this.canDrop = ☃;
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      if (!☃) {
         ☃ = this.getActualState(☃, ☃, ☃);
      }

      addCollisionBoxToList(☃, ☃, ☃, AABB_BY_INDEX[0]);
      if (☃.getValue(NORTH)) {
         addCollisionBoxToList(☃, ☃, ☃, AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.NORTH)]);
      }

      if (☃.getValue(SOUTH)) {
         addCollisionBoxToList(☃, ☃, ☃, AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.SOUTH)]);
      }

      if (☃.getValue(EAST)) {
         addCollisionBoxToList(☃, ☃, ☃, AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.EAST)]);
      }

      if (☃.getValue(WEST)) {
         addCollisionBoxToList(☃, ☃, ☃, AABB_BY_INDEX[getBoundingBoxIndex(EnumFacing.WEST)]);
      }
   }

   private static int getBoundingBoxIndex(EnumFacing var0) {
      return 1 << ☃.getHorizontalIndex();
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      ☃ = this.getActualState(☃, ☃, ☃);
      return AABB_BY_INDEX[getBoundingBoxIndex(☃)];
   }

   private static int getBoundingBoxIndex(IBlockState var0) {
      int ☃ = 0;
      if (☃.getValue(NORTH)) {
         ☃ |= getBoundingBoxIndex(EnumFacing.NORTH);
      }

      if (☃.getValue(EAST)) {
         ☃ |= getBoundingBoxIndex(EnumFacing.EAST);
      }

      if (☃.getValue(SOUTH)) {
         ☃ |= getBoundingBoxIndex(EnumFacing.SOUTH);
      }

      if (☃.getValue(WEST)) {
         ☃ |= getBoundingBoxIndex(EnumFacing.WEST);
      }

      return ☃;
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃.withProperty(NORTH, this.attachesTo(☃, ☃.getBlockState(☃.north()), ☃.north(), EnumFacing.SOUTH))
         .withProperty(SOUTH, this.attachesTo(☃, ☃.getBlockState(☃.south()), ☃.south(), EnumFacing.NORTH))
         .withProperty(WEST, this.attachesTo(☃, ☃.getBlockState(☃.west()), ☃.west(), EnumFacing.EAST))
         .withProperty(EAST, this.attachesTo(☃, ☃.getBlockState(☃.east()), ☃.east(), EnumFacing.WEST));
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return !this.canDrop ? Items.AIR : super.getItemDropped(☃, ☃, ☃);
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
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃.getBlockState(☃.offset(☃)).getBlock() == this ? false : super.shouldSideBeRendered(☃, ☃, ☃, ☃);
   }

   public final boolean attachesTo(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      Block ☃ = ☃.getBlock();
      BlockFaceShape ☃x = ☃.getBlockFaceShape(☃, ☃, ☃);
      return !isExcepBlockForAttachWithPiston(☃) && ☃x == BlockFaceShape.SOLID || ☃x == BlockFaceShape.MIDDLE_POLE_THIN;
   }

   protected static boolean isExcepBlockForAttachWithPiston(Block var0) {
      return ☃ instanceof BlockShulkerBox
         || ☃ instanceof BlockLeaves
         || ☃ == Blocks.BEACON
         || ☃ == Blocks.CAULDRON
         || ☃ == Blocks.GLOWSTONE
         || ☃ == Blocks.ICE
         || ☃ == Blocks.SEA_LANTERN
         || ☃ == Blocks.PISTON
         || ☃ == Blocks.STICKY_PISTON
         || ☃ == Blocks.PISTON_HEAD
         || ☃ == Blocks.MELON_BLOCK
         || ☃ == Blocks.PUMPKIN
         || ☃ == Blocks.LIT_PUMPKIN
         || ☃ == Blocks.BARRIER;
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT_MIPPED;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return 0;
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case CLOCKWISE_180:
            return ☃.withProperty(NORTH, ☃.getValue(SOUTH))
               .withProperty(EAST, ☃.getValue(WEST))
               .withProperty(SOUTH, ☃.getValue(NORTH))
               .withProperty(WEST, ☃.getValue(EAST));
         case COUNTERCLOCKWISE_90:
            return ☃.withProperty(NORTH, ☃.getValue(EAST))
               .withProperty(EAST, ☃.getValue(SOUTH))
               .withProperty(SOUTH, ☃.getValue(WEST))
               .withProperty(WEST, ☃.getValue(NORTH));
         case CLOCKWISE_90:
            return ☃.withProperty(NORTH, ☃.getValue(WEST))
               .withProperty(EAST, ☃.getValue(NORTH))
               .withProperty(SOUTH, ☃.getValue(EAST))
               .withProperty(WEST, ☃.getValue(SOUTH));
         default:
            return ☃;
      }
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      switch (☃) {
         case LEFT_RIGHT:
            return ☃.withProperty(NORTH, ☃.getValue(SOUTH)).withProperty(SOUTH, ☃.getValue(NORTH));
         case FRONT_BACK:
            return ☃.withProperty(EAST, ☃.getValue(WEST)).withProperty(WEST, ☃.getValue(EAST));
         default:
            return super.withMirror(☃, ☃);
      }
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, NORTH, EAST, WEST, SOUTH);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return ☃ != EnumFacing.UP && ☃ != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THIN : BlockFaceShape.CENTER_SMALL;
   }
}
