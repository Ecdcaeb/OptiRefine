package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt extends Block {
   public static final PropertyEnum<BlockDirt.DirtType> VARIANT = PropertyEnum.create("variant", BlockDirt.DirtType.class);
   public static final PropertyBool SNOWY = PropertyBool.create("snowy");

   protected BlockDirt() {
      super(Material.GROUND);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockDirt.DirtType.DIRT).withProperty(SNOWY, false));
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃.getValue(VARIANT).getColor();
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (☃.getValue(VARIANT) == BlockDirt.DirtType.PODZOL) {
         Block ☃ = ☃.getBlockState(☃.up()).getBlock();
         ☃ = ☃.withProperty(SNOWY, ☃ == Blocks.SNOW || ☃ == Blocks.SNOW_LAYER);
      }

      return ☃;
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      ☃.add(new ItemStack(this, 1, BlockDirt.DirtType.DIRT.getMetadata()));
      ☃.add(new ItemStack(this, 1, BlockDirt.DirtType.COARSE_DIRT.getMetadata()));
      ☃.add(new ItemStack(this, 1, BlockDirt.DirtType.PODZOL.getMetadata()));
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(this, 1, ☃.getValue(VARIANT).getMetadata());
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockDirt.DirtType.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, VARIANT, SNOWY);
   }

   @Override
   public int damageDropped(IBlockState var1) {
      BlockDirt.DirtType ☃ = ☃.getValue(VARIANT);
      if (☃ == BlockDirt.DirtType.PODZOL) {
         ☃ = BlockDirt.DirtType.DIRT;
      }

      return ☃.getMetadata();
   }

   public static enum DirtType implements IStringSerializable {
      DIRT(0, "dirt", "default", MapColor.DIRT),
      COARSE_DIRT(1, "coarse_dirt", "coarse", MapColor.DIRT),
      PODZOL(2, "podzol", MapColor.OBSIDIAN);

      private static final BlockDirt.DirtType[] METADATA_LOOKUP = new BlockDirt.DirtType[values().length];
      private final int metadata;
      private final String name;
      private final String translationKey;
      private final MapColor color;

      private DirtType(int var3, String var4, MapColor var5) {
         this(☃, ☃, ☃, ☃);
      }

      private DirtType(int var3, String var4, String var5, MapColor var6) {
         this.metadata = ☃;
         this.name = ☃;
         this.translationKey = ☃;
         this.color = ☃;
      }

      public int getMetadata() {
         return this.metadata;
      }

      public String getTranslationKey() {
         return this.translationKey;
      }

      public MapColor getColor() {
         return this.color;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockDirt.DirtType byMetadata(int var0) {
         if (☃ < 0 || ☃ >= METADATA_LOOKUP.length) {
            ☃ = 0;
         }

         return METADATA_LOOKUP[☃];
      }

      @Override
      public String getName() {
         return this.name;
      }

      static {
         for (BlockDirt.DirtType ☃ : values()) {
            METADATA_LOOKUP[☃.getMetadata()] = ☃;
         }
      }
   }
}
