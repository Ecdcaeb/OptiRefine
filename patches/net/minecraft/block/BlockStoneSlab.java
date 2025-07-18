package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockStoneSlab extends BlockSlab {
   public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
   public static final PropertyEnum<BlockStoneSlab.EnumType> VARIANT = PropertyEnum.create("variant", BlockStoneSlab.EnumType.class);

   public BlockStoneSlab() {
      super(Material.ROCK);
      IBlockState ☃ = this.blockState.getBaseState();
      if (this.isDouble()) {
         ☃ = ☃.withProperty(SEAMLESS, false);
      } else {
         ☃ = ☃.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
      }

      this.setDefaultState(☃.withProperty(VARIANT, BlockStoneSlab.EnumType.STONE));
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.STONE_SLAB);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Blocks.STONE_SLAB, 1, ☃.getValue(VARIANT).getMetadata());
   }

   @Override
   public String getTranslationKey(int var1) {
      return super.getTranslationKey() + "." + BlockStoneSlab.EnumType.byMetadata(☃).getTranslationKey();
   }

   @Override
   public IProperty<?> getVariantProperty() {
      return VARIANT;
   }

   @Override
   public Comparable<?> getTypeForItem(ItemStack var1) {
      return BlockStoneSlab.EnumType.byMetadata(☃.getMetadata() & 7);
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (BlockStoneSlab.EnumType ☃ : BlockStoneSlab.EnumType.values()) {
         if (☃ != BlockStoneSlab.EnumType.WOOD) {
            ☃.add(new ItemStack(this, 1, ☃.getMetadata()));
         }
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      IBlockState ☃ = this.getDefaultState().withProperty(VARIANT, BlockStoneSlab.EnumType.byMetadata(☃ & 7));
      if (this.isDouble()) {
         ☃ = ☃.withProperty(SEAMLESS, (☃ & 8) != 0);
      } else {
         ☃ = ☃.withProperty(HALF, (☃ & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
      }

      return ☃;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(VARIANT).getMetadata();
      if (this.isDouble()) {
         if (☃.getValue(SEAMLESS)) {
            ☃ |= 8;
         }
      } else if (☃.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
         ☃ |= 8;
      }

      return ☃;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return this.isDouble() ? new BlockStateContainer(this, SEAMLESS, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃.getValue(VARIANT).getMapColor();
   }

   public static enum EnumType implements IStringSerializable {
      STONE(0, MapColor.STONE, "stone"),
      SAND(1, MapColor.SAND, "sandstone", "sand"),
      WOOD(2, MapColor.WOOD, "wood_old", "wood"),
      COBBLESTONE(3, MapColor.STONE, "cobblestone", "cobble"),
      BRICK(4, MapColor.RED, "brick"),
      SMOOTHBRICK(5, MapColor.STONE, "stone_brick", "smoothStoneBrick"),
      NETHERBRICK(6, MapColor.NETHERRACK, "nether_brick", "netherBrick"),
      QUARTZ(7, MapColor.QUARTZ, "quartz");

      private static final BlockStoneSlab.EnumType[] META_LOOKUP = new BlockStoneSlab.EnumType[values().length];
      private final int meta;
      private final MapColor mapColor;
      private final String name;
      private final String translationKey;

      private EnumType(int var3, MapColor var4, String var5) {
         this(☃, ☃, ☃, ☃);
      }

      private EnumType(int var3, MapColor var4, String var5, String var6) {
         this.meta = ☃;
         this.mapColor = ☃;
         this.name = ☃;
         this.translationKey = ☃;
      }

      public int getMetadata() {
         return this.meta;
      }

      public MapColor getMapColor() {
         return this.mapColor;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockStoneSlab.EnumType byMetadata(int var0) {
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
         for (BlockStoneSlab.EnumType ☃ : values()) {
            META_LOOKUP[☃.getMetadata()] = ☃;
         }
      }
   }
}
