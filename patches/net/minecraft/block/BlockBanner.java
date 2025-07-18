package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBanner extends BlockContainer {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
   protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);

   protected BlockBanner() {
      super(Material.WOOD);
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal("item.banner.white.name");
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return true;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean canSpawnInBlock() {
      return true;
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityBanner();
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.BANNER;
   }

   private ItemStack getTileDataItemStack(World var1, BlockPos var2) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      return ☃ instanceof TileEntityBanner ? ((TileEntityBanner)☃).getItem() : ItemStack.EMPTY;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      ItemStack ☃ = this.getTileDataItemStack(☃, ☃);
      return ☃.isEmpty() ? new ItemStack(Items.BANNER) : ☃;
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      ItemStack ☃ = this.getTileDataItemStack(☃, ☃);
      if (☃.isEmpty()) {
         super.dropBlockAsItemWithChance(☃, ☃, ☃, ☃, ☃);
      } else {
         spawnAsEntity(☃, ☃, ☃);
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return !this.hasInvalidNeighbor(☃, ☃) && super.canPlaceBlockAt(☃, ☃);
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      if (☃ instanceof TileEntityBanner) {
         TileEntityBanner ☃ = (TileEntityBanner)☃;
         ItemStack ☃x = ☃.getItem();
         spawnAsEntity(☃, ☃, ☃x);
      } else {
         super.harvestBlock(☃, ☃, ☃, ☃, null, ☃);
      }
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }

   public static class BlockBannerHanging extends BlockBanner {
      protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.875, 1.0, 0.78125, 1.0);
      protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.78125, 0.125);
      protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.875, 0.0, 0.0, 1.0, 0.78125, 1.0);
      protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.125, 0.78125, 1.0);

      public BlockBannerHanging() {
         this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
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
      public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
         switch ((EnumFacing)☃.getValue(FACING)) {
            case NORTH:
            default:
               return NORTH_AABB;
            case SOUTH:
               return SOUTH_AABB;
            case WEST:
               return WEST_AABB;
            case EAST:
               return EAST_AABB;
         }
      }

      @Override
      public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
         EnumFacing ☃ = ☃.getValue(FACING);
         if (!☃.getBlockState(☃.offset(☃.getOpposite())).getMaterial().isSolid()) {
            this.dropBlockAsItem(☃, ☃, ☃, 0);
            ☃.setBlockToAir(☃);
         }

         super.neighborChanged(☃, ☃, ☃, ☃, ☃);
      }

      @Override
      public IBlockState getStateFromMeta(int var1) {
         EnumFacing ☃ = EnumFacing.byIndex(☃);
         if (☃.getAxis() == EnumFacing.Axis.Y) {
            ☃ = EnumFacing.NORTH;
         }

         return this.getDefaultState().withProperty(FACING, ☃);
      }

      @Override
      public int getMetaFromState(IBlockState var1) {
         return ☃.getValue(FACING).getIndex();
      }

      @Override
      protected BlockStateContainer createBlockState() {
         return new BlockStateContainer(this, FACING);
      }
   }

   public static class BlockBannerStanding extends BlockBanner {
      public BlockBannerStanding() {
         this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, 0));
      }

      @Override
      public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
         return STANDING_AABB;
      }

      @Override
      public IBlockState withRotation(IBlockState var1, Rotation var2) {
         return ☃.withProperty(ROTATION, ☃.rotate(☃.getValue(ROTATION), 16));
      }

      @Override
      public IBlockState withMirror(IBlockState var1, Mirror var2) {
         return ☃.withProperty(ROTATION, ☃.mirrorRotation(☃.getValue(ROTATION), 16));
      }

      @Override
      public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
         if (!☃.getBlockState(☃.down()).getMaterial().isSolid()) {
            this.dropBlockAsItem(☃, ☃, ☃, 0);
            ☃.setBlockToAir(☃);
         }

         super.neighborChanged(☃, ☃, ☃, ☃, ☃);
      }

      @Override
      public IBlockState getStateFromMeta(int var1) {
         return this.getDefaultState().withProperty(ROTATION, ☃);
      }

      @Override
      public int getMetaFromState(IBlockState var1) {
         return ☃.getValue(ROTATION);
      }

      @Override
      protected BlockStateContainer createBlockState() {
         return new BlockStateContainer(this, ROTATION);
      }
   }
}
