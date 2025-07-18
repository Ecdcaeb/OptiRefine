package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence extends Block {
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[]{
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.375, 0.625, 1.0, 0.625),
      new AxisAlignedBB(0.0, 0.0, 0.375, 0.625, 1.0, 1.0),
      new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.0, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.625, 1.0, 0.625),
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.625, 1.0, 1.0),
      new AxisAlignedBB(0.375, 0.0, 0.375, 1.0, 1.0, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.375, 1.0, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.0, 0.625),
      new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.0, 1.0),
      new AxisAlignedBB(0.375, 0.0, 0.0, 1.0, 1.0, 0.625),
      new AxisAlignedBB(0.375, 0.0, 0.0, 1.0, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.625),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
   };
   public static final AxisAlignedBB PILLAR_AABB = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.5, 0.625);
   public static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.375, 0.0, 0.625, 0.625, 1.5, 1.0);
   public static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0, 0.0, 0.375, 0.375, 1.5, 0.625);
   public static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.5, 0.375);
   public static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.625, 0.0, 0.375, 1.0, 1.5, 0.625);

   public BlockFence(Material var1, MapColor var2) {
      super(☃, ☃);
      this.setDefaultState(
         this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false)
      );
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      if (!☃) {
         ☃ = ☃.getActualState(☃, ☃);
      }

      addCollisionBoxToList(☃, ☃, ☃, PILLAR_AABB);
      if (☃.getValue(NORTH)) {
         addCollisionBoxToList(☃, ☃, ☃, NORTH_AABB);
      }

      if (☃.getValue(EAST)) {
         addCollisionBoxToList(☃, ☃, ☃, EAST_AABB);
      }

      if (☃.getValue(SOUTH)) {
         addCollisionBoxToList(☃, ☃, ☃, SOUTH_AABB);
      }

      if (☃.getValue(WEST)) {
         addCollisionBoxToList(☃, ☃, ☃, WEST_AABB);
      }
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      ☃ = this.getActualState(☃, ☃, ☃);
      return BOUNDING_BOXES[getBoundingBoxIdx(☃)];
   }

   private static int getBoundingBoxIdx(IBlockState var0) {
      int ☃ = 0;
      if (☃.getValue(NORTH)) {
         ☃ |= 1 << EnumFacing.NORTH.getHorizontalIndex();
      }

      if (☃.getValue(EAST)) {
         ☃ |= 1 << EnumFacing.EAST.getHorizontalIndex();
      }

      if (☃.getValue(SOUTH)) {
         ☃ |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
      }

      if (☃.getValue(WEST)) {
         ☃ |= 1 << EnumFacing.WEST.getHorizontalIndex();
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
      return false;
   }

   public boolean canConnectTo(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      IBlockState ☃ = ☃.getBlockState(☃);
      BlockFaceShape ☃x = ☃.getBlockFaceShape(☃, ☃, ☃);
      Block ☃xx = ☃.getBlock();
      boolean ☃xxx = ☃x == BlockFaceShape.MIDDLE_POLE && (☃.getMaterial() == this.material || ☃xx instanceof BlockFenceGate);
      return !isExcepBlockForAttachWithPiston(☃xx) && ☃x == BlockFaceShape.SOLID || ☃xxx;
   }

   protected static boolean isExcepBlockForAttachWithPiston(Block var0) {
      return Block.isExceptBlockForAttachWithPiston(☃) || ☃ == Blocks.BARRIER || ☃ == Blocks.MELON_BLOCK || ☃ == Blocks.PUMPKIN || ☃ == Blocks.LIT_PUMPKIN;
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return true;
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (!☃.isRemote) {
         return ItemLead.attachToFence(☃, ☃, ☃);
      } else {
         ItemStack ☃ = ☃.getHeldItem(☃);
         return ☃.getItem() == Items.LEAD || ☃.isEmpty();
      }
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return 0;
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃.withProperty(NORTH, this.canConnectTo(☃, ☃.north(), EnumFacing.SOUTH))
         .withProperty(EAST, this.canConnectTo(☃, ☃.east(), EnumFacing.WEST))
         .withProperty(SOUTH, this.canConnectTo(☃, ☃.south(), EnumFacing.NORTH))
         .withProperty(WEST, this.canConnectTo(☃, ☃.west(), EnumFacing.EAST));
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
      return ☃ != EnumFacing.UP && ☃ != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.CENTER;
   }
}
