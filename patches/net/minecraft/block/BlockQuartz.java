package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQuartz extends Block {
   public static final PropertyEnum<BlockQuartz.EnumType> VARIANT = PropertyEnum.create("variant", BlockQuartz.EnumType.class);

   public BlockQuartz() {
      super(Material.ROCK);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT));
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      if (☃ == BlockQuartz.EnumType.LINES_Y.getMetadata()) {
         switch (☃.getAxis()) {
            case Z:
               return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Z);
            case X:
               return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_X);
            case Y:
               return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.LINES_Y);
         }
      }

      return ☃ == BlockQuartz.EnumType.CHISELED.getMetadata()
         ? this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.CHISELED)
         : this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.DEFAULT);
   }

   @Override
   public int damageDropped(IBlockState var1) {
      BlockQuartz.EnumType ☃ = ☃.getValue(VARIANT);
      return ☃ != BlockQuartz.EnumType.LINES_X && ☃ != BlockQuartz.EnumType.LINES_Z ? ☃.getMetadata() : BlockQuartz.EnumType.LINES_Y.getMetadata();
   }

   @Override
   protected ItemStack getSilkTouchDrop(IBlockState var1) {
      BlockQuartz.EnumType ☃ = ☃.getValue(VARIANT);
      return ☃ != BlockQuartz.EnumType.LINES_X && ☃ != BlockQuartz.EnumType.LINES_Z
         ? super.getSilkTouchDrop(☃)
         : new ItemStack(Item.getItemFromBlock(this), 1, BlockQuartz.EnumType.LINES_Y.getMetadata());
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      ☃.add(new ItemStack(this, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()));
      ☃.add(new ItemStack(this, 1, BlockQuartz.EnumType.CHISELED.getMetadata()));
      ☃.add(new ItemStack(this, 1, BlockQuartz.EnumType.LINES_Y.getMetadata()));
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.QUARTZ;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockQuartz.EnumType.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case COUNTERCLOCKWISE_90:
         case CLOCKWISE_90:
            switch ((BlockQuartz.EnumType)☃.getValue(VARIANT)) {
               case LINES_X:
                  return ☃.withProperty(VARIANT, BlockQuartz.EnumType.LINES_Z);
               case LINES_Z:
                  return ☃.withProperty(VARIANT, BlockQuartz.EnumType.LINES_X);
               default:
                  return ☃;
            }
         default:
            return ☃;
      }
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, VARIANT);
   }

   public static enum EnumType implements IStringSerializable {
      DEFAULT(0, "default", "default"),
      CHISELED(1, "chiseled", "chiseled"),
      LINES_Y(2, "lines_y", "lines"),
      LINES_X(3, "lines_x", "lines"),
      LINES_Z(4, "lines_z", "lines");

      private static final BlockQuartz.EnumType[] META_LOOKUP = new BlockQuartz.EnumType[values().length];
      private final int meta;
      private final String serializedName;
      private final String translationKey;

      private EnumType(int var3, String var4, String var5) {
         this.meta = ☃;
         this.serializedName = ☃;
         this.translationKey = ☃;
      }

      public int getMetadata() {
         return this.meta;
      }

      @Override
      public String toString() {
         return this.translationKey;
      }

      public static BlockQuartz.EnumType byMetadata(int var0) {
         if (☃ < 0 || ☃ >= META_LOOKUP.length) {
            ☃ = 0;
         }

         return META_LOOKUP[☃];
      }

      @Override
      public String getName() {
         return this.serializedName;
      }

      static {
         for (BlockQuartz.EnumType ☃ : values()) {
            META_LOOKUP[☃.getMetadata()] = ☃;
         }
      }
   }
}
