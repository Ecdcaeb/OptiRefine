package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate {
   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
   private final int maxWeight;

   protected BlockPressurePlateWeighted(Material var1, int var2) {
      this(☃, ☃, ☃.getMaterialMapColor());
   }

   protected BlockPressurePlateWeighted(Material var1, int var2, MapColor var3) {
      super(☃, ☃);
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
      this.maxWeight = ☃;
   }

   @Override
   protected int computeRedstoneStrength(World var1, BlockPos var2) {
      int ☃ = Math.min(☃.getEntitiesWithinAABB(Entity.class, PRESSURE_AABB.offset(☃)).size(), this.maxWeight);
      if (☃ > 0) {
         float ☃x = (float)Math.min(this.maxWeight, ☃) / this.maxWeight;
         return MathHelper.ceil(☃x * 15.0F);
      } else {
         return 0;
      }
   }

   @Override
   protected void playClickOnSound(World var1, BlockPos var2) {
      ☃.playSound(null, ☃, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.90000004F);
   }

   @Override
   protected void playClickOffSound(World var1, BlockPos var2) {
      ☃.playSound(null, ☃, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.75F);
   }

   @Override
   protected int getRedstoneStrength(IBlockState var1) {
      return ☃.getValue(POWER);
   }

   @Override
   protected IBlockState setRedstoneStrength(IBlockState var1, int var2) {
      return ☃.withProperty(POWER, ☃);
   }

   @Override
   public int tickRate(World var1) {
      return 10;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(POWER, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(POWER);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, POWER);
   }
}
