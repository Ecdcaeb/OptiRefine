package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHugeMushroom extends Block {
   public static final PropertyEnum<BlockHugeMushroom.EnumType> VARIANT = PropertyEnum.create("variant", BlockHugeMushroom.EnumType.class);
   private final Block smallBlock;

   public BlockHugeMushroom(Material var1, MapColor var2, Block var3) {
      super(☃, ☃);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE));
      this.smallBlock = ☃;
   }

   @Override
   public int quantityDropped(Random var1) {
      return Math.max(0, ☃.nextInt(10) - 7);
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      switch ((BlockHugeMushroom.EnumType)☃.getValue(VARIANT)) {
         case ALL_STEM:
            return MapColor.CLOTH;
         case ALL_INSIDE:
            return MapColor.SAND;
         case STEM:
            return MapColor.SAND;
         default:
            return super.getMapColor(☃, ☃, ☃);
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(this.smallBlock);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(this.smallBlock);
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState();
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockHugeMushroom.EnumType.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case CLOCKWISE_180:
            switch ((BlockHugeMushroom.EnumType)☃.getValue(VARIANT)) {
               case STEM:
                  break;
               case NORTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
               case NORTH:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH);
               case NORTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
               case WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.EAST);
               case EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.WEST);
               case SOUTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
               case SOUTH:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH);
               case SOUTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
               default:
                  return ☃;
            }
         case COUNTERCLOCKWISE_90:
            switch ((BlockHugeMushroom.EnumType)☃.getValue(VARIANT)) {
               case STEM:
                  break;
               case NORTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
               case NORTH:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.WEST);
               case NORTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
               case WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH);
               case EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH);
               case SOUTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
               case SOUTH:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.EAST);
               case SOUTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
               default:
                  return ☃;
            }
         case CLOCKWISE_90:
            switch ((BlockHugeMushroom.EnumType)☃.getValue(VARIANT)) {
               case STEM:
                  break;
               case NORTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
               case NORTH:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.EAST);
               case NORTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
               case WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH);
               case EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH);
               case SOUTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
               case SOUTH:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.WEST);
               case SOUTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
               default:
                  return ☃;
            }
         default:
            return ☃;
      }
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      BlockHugeMushroom.EnumType ☃ = ☃.getValue(VARIANT);
      switch (☃) {
         case LEFT_RIGHT:
            switch (☃) {
               case NORTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
               case NORTH:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH);
               case NORTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
               case WEST:
               case EAST:
               default:
                  return super.withMirror(☃, ☃);
               case SOUTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
               case SOUTH:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH);
               case SOUTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
            }
         case FRONT_BACK:
            switch (☃) {
               case NORTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
               case NORTH:
               case SOUTH:
               default:
                  break;
               case NORTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);
               case WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.EAST);
               case EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.WEST);
               case SOUTH_WEST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);
               case SOUTH_EAST:
                  return ☃.withProperty(VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
            }
      }

      return super.withMirror(☃, ☃);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, VARIANT);
   }

   public static enum EnumType implements IStringSerializable {
      NORTH_WEST(1, "north_west"),
      NORTH(2, "north"),
      NORTH_EAST(3, "north_east"),
      WEST(4, "west"),
      CENTER(5, "center"),
      EAST(6, "east"),
      SOUTH_WEST(7, "south_west"),
      SOUTH(8, "south"),
      SOUTH_EAST(9, "south_east"),
      STEM(10, "stem"),
      ALL_INSIDE(0, "all_inside"),
      ALL_OUTSIDE(14, "all_outside"),
      ALL_STEM(15, "all_stem");

      private static final BlockHugeMushroom.EnumType[] META_LOOKUP = new BlockHugeMushroom.EnumType[16];
      private final int meta;
      private final String name;

      private EnumType(int var3, String var4) {
         this.meta = ☃;
         this.name = ☃;
      }

      public int getMetadata() {
         return this.meta;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockHugeMushroom.EnumType byMetadata(int var0) {
         if (☃ < 0 || ☃ >= META_LOOKUP.length) {
            ☃ = 0;
         }

         BlockHugeMushroom.EnumType ☃ = META_LOOKUP[☃];
         return ☃ == null ? META_LOOKUP[0] : ☃;
      }

      @Override
      public String getName() {
         return this.name;
      }

      static {
         for (BlockHugeMushroom.EnumType ☃ : values()) {
            META_LOOKUP[☃.getMetadata()] = ☃;
         }
      }
   }
}
