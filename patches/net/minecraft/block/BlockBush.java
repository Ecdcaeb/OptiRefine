package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBush extends Block {
   protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.3F, 0.0, 0.3F, 0.7F, 0.6F, 0.7F);

   protected BlockBush() {
      this(Material.PLANTS);
   }

   protected BlockBush(Material var1) {
      this(☃, ☃.getMaterialMapColor());
   }

   protected BlockBush(Material var1, MapColor var2) {
      super(☃, ☃);
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(☃, ☃) && this.canSustainBush(☃.getBlockState(☃.down()));
   }

   protected boolean canSustainBush(IBlockState var1) {
      return ☃.getBlock() == Blocks.GRASS || ☃.getBlock() == Blocks.DIRT || ☃.getBlock() == Blocks.FARMLAND;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      super.neighborChanged(☃, ☃, ☃, ☃, ☃);
      this.checkAndDropBlock(☃, ☃, ☃);
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      this.checkAndDropBlock(☃, ☃, ☃);
   }

   protected void checkAndDropBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!this.canBlockStay(☃, ☃, ☃)) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockState(☃, Blocks.AIR.getDefaultState(), 3);
      }
   }

   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      return this.canSustainBush(☃.getBlockState(☃.down()));
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return BUSH_AABB;
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
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
