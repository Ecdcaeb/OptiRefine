package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockSlab extends Block {
   public static final PropertyEnum<BlockSlab.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockSlab.EnumBlockHalf.class);
   protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
   protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);

   public BlockSlab(Material var1) {
      this(☃, ☃.getMaterialMapColor());
   }

   public BlockSlab(Material var1, MapColor var2) {
      super(☃, ☃);
      this.fullBlock = this.isDouble();
      this.setLightOpacity(255);
   }

   @Override
   protected boolean canSilkHarvest() {
      return false;
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (this.isDouble()) {
         return FULL_BLOCK_AABB;
      } else {
         return ☃.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP ? AABB_TOP_HALF : AABB_BOTTOM_HALF;
      }
   }

   @Override
   public boolean isTopSolid(IBlockState var1) {
      return ((BlockSlab)☃.getBlock()).isDouble() || ☃.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      if (((BlockSlab)☃.getBlock()).isDouble()) {
         return BlockFaceShape.SOLID;
      } else if (☃ == EnumFacing.UP && ☃.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
         return BlockFaceShape.SOLID;
      } else {
         return ☃ == EnumFacing.DOWN && ☃.getValue(HALF) == BlockSlab.EnumBlockHalf.BOTTOM ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
      }
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return this.isDouble();
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      IBlockState ☃ = super.getStateForPlacement(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃).withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
      if (this.isDouble()) {
         return ☃;
      } else {
         return ☃ != EnumFacing.DOWN && (☃ == EnumFacing.UP || !(☃ > 0.5)) ? ☃ : ☃.withProperty(HALF, BlockSlab.EnumBlockHalf.TOP);
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      return this.isDouble() ? 2 : 1;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return this.isDouble();
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (this.isDouble()) {
         return super.shouldSideBeRendered(☃, ☃, ☃, ☃);
      } else if (☃ != EnumFacing.UP && ☃ != EnumFacing.DOWN && !super.shouldSideBeRendered(☃, ☃, ☃, ☃)) {
         return false;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃.offset(☃));
         boolean ☃x = isHalfSlab(☃) && ☃.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP;
         boolean ☃xx = isHalfSlab(☃) && ☃.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP;
         if (☃xx) {
            if (☃ == EnumFacing.DOWN) {
               return true;
            } else {
               return ☃ == EnumFacing.UP && super.shouldSideBeRendered(☃, ☃, ☃, ☃) ? true : !isHalfSlab(☃) || !☃x;
            }
         } else if (☃ == EnumFacing.UP) {
            return true;
         } else {
            return ☃ == EnumFacing.DOWN && super.shouldSideBeRendered(☃, ☃, ☃, ☃) ? true : !isHalfSlab(☃) || ☃x;
         }
      }
   }

   protected static boolean isHalfSlab(IBlockState var0) {
      Block ☃ = ☃.getBlock();
      return ☃ == Blocks.STONE_SLAB || ☃ == Blocks.WOODEN_SLAB || ☃ == Blocks.STONE_SLAB2 || ☃ == Blocks.PURPUR_SLAB;
   }

   public abstract String getTranslationKey(int var1);

   public abstract boolean isDouble();

   public abstract IProperty<?> getVariantProperty();

   public abstract Comparable<?> getTypeForItem(ItemStack var1);

   public static enum EnumBlockHalf implements IStringSerializable {
      TOP("top"),
      BOTTOM("bottom");

      private final String name;

      private EnumBlockHalf(String var3) {
         this.name = ☃;
      }

      @Override
      public String toString() {
         return this.name;
      }

      @Override
      public String getName() {
         return this.name;
      }
   }
}
