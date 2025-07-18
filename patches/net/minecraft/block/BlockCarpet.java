package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpet extends Block {
   public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
   protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);

   protected BlockCarpet() {
      super(Material.CARPET);
      this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return CARPET_AABB;
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.getBlockColor(☃.getValue(COLOR));
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
      return super.canPlaceBlockAt(☃, ☃) && this.canBlockStay(☃, ☃);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      this.checkForDrop(☃, ☃, ☃);
   }

   private boolean checkForDrop(World var1, BlockPos var2, IBlockState var3) {
      if (!this.canBlockStay(☃, ☃)) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
         return false;
      } else {
         return true;
      }
   }

   private boolean canBlockStay(World var1, BlockPos var2) {
      return !☃.isAirBlock(☃.down());
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (☃ == EnumFacing.UP) {
         return true;
      } else {
         return ☃.getBlockState(☃.offset(☃)).getBlock() == this ? true : super.shouldSideBeRendered(☃, ☃, ☃, ☃);
      }
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(COLOR).getMetadata();
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (int ☃ = 0; ☃ < 16; ☃++) {
         ☃.add(new ItemStack(this, 1, ☃));
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(COLOR).getMetadata();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, COLOR);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
