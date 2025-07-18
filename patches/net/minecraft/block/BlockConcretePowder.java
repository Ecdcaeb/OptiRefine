package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConcretePowder extends BlockFalling {
   public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

   public BlockConcretePowder() {
      super(Material.SAND);
      this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public void onEndFalling(World var1, BlockPos var2, IBlockState var3, IBlockState var4) {
      if (☃.getMaterial().isLiquid()) {
         ☃.setBlockState(☃, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, ☃.getValue(COLOR)), 3);
      }
   }

   protected boolean tryTouchWater(World var1, BlockPos var2, IBlockState var3) {
      boolean ☃ = false;

      for (EnumFacing ☃x : EnumFacing.values()) {
         if (☃x != EnumFacing.DOWN) {
            BlockPos ☃xx = ☃.offset(☃x);
            if (☃.getBlockState(☃xx).getMaterial() == Material.WATER) {
               ☃ = true;
               break;
            }
         }
      }

      if (☃) {
         ☃.setBlockState(☃, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, ☃.getValue(COLOR)), 3);
      }

      return ☃;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!this.tryTouchWater(☃, ☃, ☃)) {
         super.neighborChanged(☃, ☃, ☃, ☃, ☃);
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!this.tryTouchWater(☃, ☃, ☃)) {
         super.onBlockAdded(☃, ☃, ☃);
      }
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(COLOR).getMetadata();
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (EnumDyeColor ☃ : EnumDyeColor.values()) {
         ☃.add(new ItemStack(this, 1, ☃.getMetadata()));
      }
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.getBlockColor(☃.getValue(COLOR));
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
}
