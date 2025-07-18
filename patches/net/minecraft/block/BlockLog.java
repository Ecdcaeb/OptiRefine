package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar {
   public static final PropertyEnum<BlockLog.EnumAxis> LOG_AXIS = PropertyEnum.create("axis", BlockLog.EnumAxis.class);

   public BlockLog() {
      super(Material.WOOD);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
      this.setHardness(2.0F);
      this.setSoundType(SoundType.WOOD);
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      int ☃ = 4;
      int ☃x = 5;
      if (☃.isAreaLoaded(☃.add(-5, -5, -5), ☃.add(5, 5, 5))) {
         for (BlockPos ☃xx : BlockPos.getAllInBox(☃.add(-4, -4, -4), ☃.add(4, 4, 4))) {
            IBlockState ☃xxx = ☃.getBlockState(☃xx);
            if (☃xxx.getMaterial() == Material.LEAVES && !☃xxx.getValue(BlockLeaves.CHECK_DECAY)) {
               ☃.setBlockState(☃xx, ☃xxx.withProperty(BlockLeaves.CHECK_DECAY, true), 4);
            }
         }
      }
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getStateFromMeta(☃).withProperty(LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(☃.getAxis()));
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case COUNTERCLOCKWISE_90:
         case CLOCKWISE_90:
            switch ((BlockLog.EnumAxis)☃.getValue(LOG_AXIS)) {
               case X:
                  return ☃.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
               case Z:
                  return ☃.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
               default:
                  return ☃;
            }
         default:
            return ☃;
      }
   }

   public static enum EnumAxis implements IStringSerializable {
      X("x"),
      Y("y"),
      Z("z"),
      NONE("none");

      private final String name;

      private EnumAxis(String var3) {
         this.name = ☃;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockLog.EnumAxis fromFacingAxis(EnumFacing.Axis var0) {
         switch (☃) {
            case X:
               return X;
            case Y:
               return Y;
            case Z:
               return Z;
            default:
               return NONE;
         }
      }

      @Override
      public String getName() {
         return this.name;
      }
   }
}
