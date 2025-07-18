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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockWoodSlab extends BlockSlab {
   public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);

   public BlockWoodSlab() {
      super(Material.WOOD);
      IBlockState ☃ = this.blockState.getBaseState();
      if (!this.isDouble()) {
         ☃ = ☃.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
      }

      this.setDefaultState(☃.withProperty(VARIANT, BlockPlanks.EnumType.OAK));
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃.getValue(VARIANT).getMapColor();
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.WOODEN_SLAB);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Blocks.WOODEN_SLAB, 1, ☃.getValue(VARIANT).getMetadata());
   }

   @Override
   public String getTranslationKey(int var1) {
      return super.getTranslationKey() + "." + BlockPlanks.EnumType.byMetadata(☃).getTranslationKey();
   }

   @Override
   public IProperty<?> getVariantProperty() {
      return VARIANT;
   }

   @Override
   public Comparable<?> getTypeForItem(ItemStack var1) {
      return BlockPlanks.EnumType.byMetadata(☃.getMetadata() & 7);
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (BlockPlanks.EnumType ☃ : BlockPlanks.EnumType.values()) {
         ☃.add(new ItemStack(this, 1, ☃.getMetadata()));
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      IBlockState ☃ = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(☃ & 7));
      if (!this.isDouble()) {
         ☃ = ☃.withProperty(HALF, (☃ & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
      }

      return ☃;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(VARIANT).getMetadata();
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
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }
}
