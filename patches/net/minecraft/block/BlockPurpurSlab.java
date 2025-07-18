package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockPurpurSlab extends BlockSlab {
   public static final PropertyEnum<BlockPurpurSlab.Variant> VARIANT = PropertyEnum.create("variant", BlockPurpurSlab.Variant.class);

   public BlockPurpurSlab() {
      super(Material.ROCK, MapColor.MAGENTA);
      IBlockState ☃ = this.blockState.getBaseState();
      if (!this.isDouble()) {
         ☃ = ☃.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
      }

      this.setDefaultState(☃.withProperty(VARIANT, BlockPurpurSlab.Variant.DEFAULT));
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.PURPUR_SLAB);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Blocks.PURPUR_SLAB);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      IBlockState ☃ = this.getDefaultState().withProperty(VARIANT, BlockPurpurSlab.Variant.DEFAULT);
      if (!this.isDouble()) {
         ☃ = ☃.withProperty(HALF, (☃ & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
      }

      return ☃;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      if (!this.isDouble() && ☃.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
         ☃ |= 8;
      }

      return ☃;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
   }

   @Override
   public String getTranslationKey(int var1) {
      return super.getTranslationKey();
   }

   @Override
   public IProperty<?> getVariantProperty() {
      return VARIANT;
   }

   @Override
   public Comparable<?> getTypeForItem(ItemStack var1) {
      return BlockPurpurSlab.Variant.DEFAULT;
   }

   public static class Double extends BlockPurpurSlab {
      @Override
      public boolean isDouble() {
         return true;
      }
   }

   public static class Half extends BlockPurpurSlab {
      @Override
      public boolean isDouble() {
         return false;
      }
   }

   public static enum Variant implements IStringSerializable {
      DEFAULT;

      @Override
      public String getName() {
         return "default";
      }
   }
}
