package net.minecraft.block;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGlazedTerracotta extends BlockHorizontal {
   public BlockGlazedTerracotta(EnumDyeColor var1) {
      super(Material.ROCK, MapColor.getBlockColor(☃));
      this.setHardness(1.4F);
      this.setSoundType(SoundType.STONE);
      String ☃ = ☃.getTranslationKey();
      if (☃.length() > 1) {
         String ☃x = ☃.substring(0, 1).toUpperCase() + ☃.substring(1, ☃.length());
         this.setTranslationKey("glazedTerracotta" + ☃x);
      }

      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING);
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withRotation(☃.toRotation(☃.getValue(FACING)));
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, ☃.getHorizontalFacing().getOpposite());
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      return ☃ | ☃.getValue(FACING).getHorizontalIndex();
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(☃));
   }

   @Override
   public EnumPushReaction getPushReaction(IBlockState var1) {
      return EnumPushReaction.PUSH_ONLY;
   }
}
