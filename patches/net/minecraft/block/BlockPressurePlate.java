package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate {
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   private final BlockPressurePlate.Sensitivity sensitivity;

   protected BlockPressurePlate(Material var1, BlockPressurePlate.Sensitivity var2) {
      super(☃);
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
      this.sensitivity = ☃;
   }

   @Override
   protected int getRedstoneStrength(IBlockState var1) {
      return ☃.getValue(POWERED) ? 15 : 0;
   }

   @Override
   protected IBlockState setRedstoneStrength(IBlockState var1, int var2) {
      return ☃.withProperty(POWERED, ☃ > 0);
   }

   @Override
   protected void playClickOnSound(World var1, BlockPos var2) {
      if (this.material == Material.WOOD) {
         ☃.playSound(null, ☃, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.8F);
      } else {
         ☃.playSound(null, ☃, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
      }
   }

   @Override
   protected void playClickOffSound(World var1, BlockPos var2) {
      if (this.material == Material.WOOD) {
         ☃.playSound(null, ☃, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.7F);
      } else {
         ☃.playSound(null, ☃, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
      }
   }

   @Override
   protected int computeRedstoneStrength(World var1, BlockPos var2) {
      AxisAlignedBB ☃ = PRESSURE_AABB.offset(☃);
      List<? extends Entity> ☃x;
      switch (this.sensitivity) {
         case EVERYTHING:
            ☃x = ☃.getEntitiesWithinAABBExcludingEntity(null, ☃);
            break;
         case MOBS:
            ☃x = ☃.getEntitiesWithinAABB(EntityLivingBase.class, ☃);
            break;
         default:
            return 0;
      }

      if (!☃x.isEmpty()) {
         for (Entity ☃ : ☃x) {
            if (!☃.doesEntityNotTriggerPressurePlate()) {
               return 15;
            }
         }
      }

      return 0;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(POWERED, ☃ == 1);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(POWERED) ? 1 : 0;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, POWERED);
   }

   public static enum Sensitivity {
      EVERYTHING,
      MOBS;
   }
}
