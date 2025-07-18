package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSnow extends Block {
   public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);
   protected static final AxisAlignedBB[] SNOW_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.375, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.625, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.75, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.875, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
   };

   protected BlockSnow() {
      super(Material.SNOW);
      this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS, 1));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return SNOW_AABB[☃.getValue(LAYERS)];
   }

   @Override
   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return ☃.getBlockState(☃).getValue(LAYERS) < 5;
   }

   @Override
   public boolean isTopSolid(IBlockState var1) {
      return ☃.getValue(LAYERS) == 8;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      int ☃ = ☃.getValue(LAYERS) - 1;
      float ☃x = 0.125F;
      AxisAlignedBB ☃xx = ☃.getBoundingBox(☃, ☃);
      return new AxisAlignedBB(☃xx.minX, ☃xx.minY, ☃xx.minZ, ☃xx.maxX, ☃ * 0.125F, ☃xx.maxZ);
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
      IBlockState ☃ = ☃.getBlockState(☃.down());
      Block ☃x = ☃.getBlock();
      if (☃x != Blocks.ICE && ☃x != Blocks.PACKED_ICE && ☃x != Blocks.BARRIER) {
         BlockFaceShape ☃xx = ☃.getBlockFaceShape(☃, ☃.down(), EnumFacing.UP);
         return ☃xx == BlockFaceShape.SOLID || ☃.getMaterial() == Material.LEAVES || ☃x == this && ☃.getValue(LAYERS) == 8;
      } else {
         return false;
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      this.checkAndDropBlock(☃, ☃, ☃);
   }

   private boolean checkAndDropBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!this.canPlaceBlockAt(☃, ☃)) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
         return false;
      } else {
         return true;
      }
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      spawnAsEntity(☃, ☃, new ItemStack(Items.SNOWBALL, ☃.getValue(LAYERS) + 1, 0));
      ☃.setBlockToAir(☃);
      ☃.addStat(StatList.getBlockStats(this));
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.SNOWBALL;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (☃.getLightFor(EnumSkyBlock.BLOCK, ☃) > 11) {
         this.dropBlockAsItem(☃, ☃, ☃.getBlockState(☃), 0);
         ☃.setBlockToAir(☃);
      }
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (☃ == EnumFacing.UP) {
         return true;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃.offset(☃));
         return ☃.getBlock() == this && ☃.getValue(LAYERS) >= ☃.getValue(LAYERS) ? false : super.shouldSideBeRendered(☃, ☃, ☃, ☃);
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(LAYERS, (☃ & 7) + 1);
   }

   @Override
   public boolean isReplaceable(IBlockAccess var1, BlockPos var2) {
      return ☃.getBlockState(☃).getValue(LAYERS) == 1;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(LAYERS) - 1;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, LAYERS);
   }
}
