package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRotatedPillar extends Block {
   public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

   protected BlockRotatedPillar(Material var1) {
      super(☃, ☃.getMaterialMapColor());
   }

   protected BlockRotatedPillar(Material var1, MapColor var2) {
      super(☃, ☃);
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case COUNTERCLOCKWISE_90:
         case CLOCKWISE_90:
            switch ((EnumFacing.Axis)☃.getValue(AXIS)) {
               case X:
                  return ☃.withProperty(AXIS, EnumFacing.Axis.Z);
               case Z:
                  return ☃.withProperty(AXIS, EnumFacing.Axis.X);
               default:
                  return ☃;
            }
         default:
            return ☃;
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      EnumFacing.Axis ☃ = EnumFacing.Axis.Y;
      int ☃x = ☃ & 12;
      if (☃x == 4) {
         ☃ = EnumFacing.Axis.X;
      } else if (☃x == 8) {
         ☃ = EnumFacing.Axis.Z;
      }

      return this.getDefaultState().withProperty(AXIS, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      EnumFacing.Axis ☃x = ☃.getValue(AXIS);
      if (☃x == EnumFacing.Axis.X) {
         ☃ |= 4;
      } else if (☃x == EnumFacing.Axis.Z) {
         ☃ |= 8;
      }

      return ☃;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, AXIS);
   }

   @Override
   protected ItemStack getSilkTouchDrop(IBlockState var1) {
      return new ItemStack(Item.getItemFromBlock(this));
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return super.getStateForPlacement(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃).withProperty(AXIS, ☃.getAxis());
   }
}
