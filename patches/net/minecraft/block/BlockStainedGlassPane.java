package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStainedGlassPane extends BlockPane {
   public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

   public BlockStainedGlassPane() {
      super(Material.GLASS, false);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(NORTH, false)
            .withProperty(EAST, false)
            .withProperty(SOUTH, false)
            .withProperty(WEST, false)
            .withProperty(COLOR, EnumDyeColor.WHITE)
      );
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(COLOR).getMetadata();
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (int ☃ = 0; ☃ < EnumDyeColor.values().length; ☃++) {
         ☃.add(new ItemStack(this, 1, ☃));
      }
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.getBlockColor(☃.getValue(COLOR));
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(COLOR).getMetadata();
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      switch (☃) {
         case CLOCKWISE_180:
            return ☃.withProperty(NORTH, ☃.getValue(SOUTH))
               .withProperty(EAST, ☃.getValue(WEST))
               .withProperty(SOUTH, ☃.getValue(NORTH))
               .withProperty(WEST, ☃.getValue(EAST));
         case COUNTERCLOCKWISE_90:
            return ☃.withProperty(NORTH, ☃.getValue(EAST))
               .withProperty(EAST, ☃.getValue(SOUTH))
               .withProperty(SOUTH, ☃.getValue(WEST))
               .withProperty(WEST, ☃.getValue(NORTH));
         case CLOCKWISE_90:
            return ☃.withProperty(NORTH, ☃.getValue(WEST))
               .withProperty(EAST, ☃.getValue(NORTH))
               .withProperty(SOUTH, ☃.getValue(EAST))
               .withProperty(WEST, ☃.getValue(SOUTH));
         default:
            return ☃;
      }
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      switch (☃) {
         case LEFT_RIGHT:
            return ☃.withProperty(NORTH, ☃.getValue(SOUTH)).withProperty(SOUTH, ☃.getValue(NORTH));
         case FRONT_BACK:
            return ☃.withProperty(EAST, ☃.getValue(WEST)).withProperty(WEST, ☃.getValue(EAST));
         default:
            return super.withMirror(☃, ☃);
      }
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, COLOR);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         BlockBeacon.updateColorAsync(☃, ☃);
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         BlockBeacon.updateColorAsync(☃, ☃);
      }
   }
}
