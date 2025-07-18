package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCake extends Block {
   public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
   protected static final AxisAlignedBB[] CAKE_AABB = new AxisAlignedBB[]{
      new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
      new AxisAlignedBB(0.1875, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
      new AxisAlignedBB(0.3125, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
      new AxisAlignedBB(0.4375, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
      new AxisAlignedBB(0.5625, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
      new AxisAlignedBB(0.6875, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
      new AxisAlignedBB(0.8125, 0.0, 0.0625, 0.9375, 0.5, 0.9375)
   };

   protected BlockCake() {
      super(Material.CAKE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, 0));
      this.setTickRandomly(true);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return CAKE_AABB[☃.getValue(BITES)];
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
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (!☃.isRemote) {
         return this.eatCake(☃, ☃, ☃, ☃);
      } else {
         ItemStack ☃ = ☃.getHeldItem(☃);
         return this.eatCake(☃, ☃, ☃, ☃) || ☃.isEmpty();
      }
   }

   private boolean eatCake(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      if (!☃.canEat(false)) {
         return false;
      } else {
         ☃.addStat(StatList.CAKE_SLICES_EATEN);
         ☃.getFoodStats().addStats(2, 0.1F);
         int ☃ = ☃.getValue(BITES);
         if (☃ < 6) {
            ☃.setBlockState(☃, ☃.withProperty(BITES, ☃ + 1), 3);
         } else {
            ☃.setBlockToAir(☃);
         }

         return true;
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(☃, ☃) ? this.canBlockStay(☃, ☃) : false;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!this.canBlockStay(☃, ☃)) {
         ☃.setBlockToAir(☃);
      }
   }

   private boolean canBlockStay(World var1, BlockPos var2) {
      return ☃.getBlockState(☃.down()).getMaterial().isSolid();
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.AIR;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.CAKE);
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(BITES, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(BITES);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, BITES);
   }

   @Override
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      return (7 - ☃.getValue(BITES)) * 2;
   }

   @Override
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return true;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
