package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChorusPlant extends Block {
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool DOWN = PropertyBool.create("down");

   protected BlockChorusPlant() {
      super(Material.PLANTS, MapColor.PURPLE);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(NORTH, false)
            .withProperty(EAST, false)
            .withProperty(SOUTH, false)
            .withProperty(WEST, false)
            .withProperty(UP, false)
            .withProperty(DOWN, false)
      );
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      Block ☃ = ☃.getBlockState(☃.down()).getBlock();
      Block ☃x = ☃.getBlockState(☃.up()).getBlock();
      Block ☃xx = ☃.getBlockState(☃.north()).getBlock();
      Block ☃xxx = ☃.getBlockState(☃.east()).getBlock();
      Block ☃xxxx = ☃.getBlockState(☃.south()).getBlock();
      Block ☃xxxxx = ☃.getBlockState(☃.west()).getBlock();
      return ☃.withProperty(DOWN, ☃ == this || ☃ == Blocks.CHORUS_FLOWER || ☃ == Blocks.END_STONE)
         .withProperty(UP, ☃x == this || ☃x == Blocks.CHORUS_FLOWER)
         .withProperty(NORTH, ☃xx == this || ☃xx == Blocks.CHORUS_FLOWER)
         .withProperty(EAST, ☃xxx == this || ☃xxx == Blocks.CHORUS_FLOWER)
         .withProperty(SOUTH, ☃xxxx == this || ☃xxxx == Blocks.CHORUS_FLOWER)
         .withProperty(WEST, ☃xxxxx == this || ☃xxxxx == Blocks.CHORUS_FLOWER);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      ☃ = ☃.getActualState(☃, ☃);
      float ☃ = 0.1875F;
      float ☃x = ☃.getValue(WEST) ? 0.0F : 0.1875F;
      float ☃xx = ☃.getValue(DOWN) ? 0.0F : 0.1875F;
      float ☃xxx = ☃.getValue(NORTH) ? 0.0F : 0.1875F;
      float ☃xxxx = ☃.getValue(EAST) ? 1.0F : 0.8125F;
      float ☃xxxxx = ☃.getValue(UP) ? 1.0F : 0.8125F;
      float ☃xxxxxx = ☃.getValue(SOUTH) ? 1.0F : 0.8125F;
      return new AxisAlignedBB(☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx);
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      if (!☃) {
         ☃ = ☃.getActualState(☃, ☃);
      }

      float ☃ = 0.1875F;
      float ☃x = 0.8125F;
      addCollisionBoxToList(☃, ☃, ☃, new AxisAlignedBB(0.1875, 0.1875, 0.1875, 0.8125, 0.8125, 0.8125));
      if (☃.getValue(WEST)) {
         addCollisionBoxToList(☃, ☃, ☃, new AxisAlignedBB(0.0, 0.1875, 0.1875, 0.1875, 0.8125, 0.8125));
      }

      if (☃.getValue(EAST)) {
         addCollisionBoxToList(☃, ☃, ☃, new AxisAlignedBB(0.8125, 0.1875, 0.1875, 1.0, 0.8125, 0.8125));
      }

      if (☃.getValue(UP)) {
         addCollisionBoxToList(☃, ☃, ☃, new AxisAlignedBB(0.1875, 0.8125, 0.1875, 0.8125, 1.0, 0.8125));
      }

      if (☃.getValue(DOWN)) {
         addCollisionBoxToList(☃, ☃, ☃, new AxisAlignedBB(0.1875, 0.0, 0.1875, 0.8125, 0.1875, 0.8125));
      }

      if (☃.getValue(NORTH)) {
         addCollisionBoxToList(☃, ☃, ☃, new AxisAlignedBB(0.1875, 0.1875, 0.0, 0.8125, 0.8125, 0.1875));
      }

      if (☃.getValue(SOUTH)) {
         addCollisionBoxToList(☃, ☃, ☃, new AxisAlignedBB(0.1875, 0.1875, 0.8125, 0.8125, 0.8125, 1.0));
      }
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return 0;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!this.canSurviveAt(☃, ☃)) {
         ☃.destroyBlock(☃, true);
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.CHORUS_FRUIT;
   }

   @Override
   public int quantityDropped(Random var1) {
      return ☃.nextInt(2);
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(☃, ☃) ? this.canSurviveAt(☃, ☃) : false;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!this.canSurviveAt(☃, ☃)) {
         ☃.scheduleUpdate(☃, this, 1);
      }
   }

   public boolean canSurviveAt(World var1, BlockPos var2) {
      boolean ☃ = ☃.isAirBlock(☃.up());
      boolean ☃x = ☃.isAirBlock(☃.down());

      for (EnumFacing ☃xx : EnumFacing.Plane.HORIZONTAL) {
         BlockPos ☃xxx = ☃.offset(☃xx);
         Block ☃xxxx = ☃.getBlockState(☃xxx).getBlock();
         if (☃xxxx == this) {
            if (!☃ && !☃x) {
               return false;
            }

            Block ☃xxxxx = ☃.getBlockState(☃xxx.down()).getBlock();
            if (☃xxxxx == this || ☃xxxxx == Blocks.END_STONE) {
               return true;
            }
         }
      }

      Block ☃xxx = ☃.getBlockState(☃.down()).getBlock();
      return ☃xxx == this || ☃xxx == Blocks.END_STONE;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      Block ☃ = ☃.getBlockState(☃.offset(☃)).getBlock();
      return ☃ != this && ☃ != Blocks.CHORUS_FLOWER && (☃ != EnumFacing.DOWN || ☃ != Blocks.END_STONE);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, UP, DOWN);
   }

   @Override
   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return false;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
