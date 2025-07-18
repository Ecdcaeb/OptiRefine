package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

public class BlockStoneBrick extends Block {
   public static final PropertyEnum<BlockStoneBrick.EnumType> VARIANT = PropertyEnum.create("variant", BlockStoneBrick.EnumType.class);
   public static final int DEFAULT_META = BlockStoneBrick.EnumType.DEFAULT.getMetadata();
   public static final int MOSSY_META = BlockStoneBrick.EnumType.MOSSY.getMetadata();
   public static final int CRACKED_META = BlockStoneBrick.EnumType.CRACKED.getMetadata();
   public static final int CHISELED_META = BlockStoneBrick.EnumType.CHISELED.getMetadata();

   public BlockStoneBrick() {
      super(Material.ROCK);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockStoneBrick.EnumType.DEFAULT));
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (BlockStoneBrick.EnumType ☃ : BlockStoneBrick.EnumType.values()) {
         ☃.add(new ItemStack(this, 1, ☃.getMetadata()));
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockStoneBrick.EnumType.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, VARIANT);
   }

   public static enum EnumType implements IStringSerializable {
      DEFAULT(0, "stonebrick", "default"),
      MOSSY(1, "mossy_stonebrick", "mossy"),
      CRACKED(2, "cracked_stonebrick", "cracked"),
      CHISELED(3, "chiseled_stonebrick", "chiseled");

      private static final BlockStoneBrick.EnumType[] META_LOOKUP = new BlockStoneBrick.EnumType[values().length];
      private final int meta;
      private final String name;
      private final String translationKey;

      private EnumType(int var3, String var4, String var5) {
         this.meta = ☃;
         this.name = ☃;
         this.translationKey = ☃;
      }

      public int getMetadata() {
         return this.meta;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockStoneBrick.EnumType byMetadata(int var0) {
         if (☃ < 0 || ☃ >= META_LOOKUP.length) {
            ☃ = 0;
         }

         return META_LOOKUP[☃];
      }

      @Override
      public String getName() {
         return this.name;
      }

      public String getTranslationKey() {
         return this.translationKey;
      }

      static {
         for (BlockStoneBrick.EnumType ☃ : values()) {
            META_LOOKUP[☃.getMetadata()] = ☃;
         }
      }
   }
}
