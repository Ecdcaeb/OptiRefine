package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndGateway extends BlockContainer {
   protected BlockEndGateway(Material var1) {
      super(☃);
      this.setLightLevel(1.0F);
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityEndGateway();
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      IBlockState ☃ = ☃.getBlockState(☃.offset(☃));
      Block ☃x = ☃.getBlock();
      return !☃.isOpaqueCube() && ☃x != Blocks.END_GATEWAY;
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof TileEntityEndGateway) {
         int ☃x = ((TileEntityEndGateway)☃).getParticleAmount();

         for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
            double ☃xxx = ☃.getX() + ☃.nextFloat();
            double ☃xxxx = ☃.getY() + ☃.nextFloat();
            double ☃xxxxx = ☃.getZ() + ☃.nextFloat();
            double ☃xxxxxx = (☃.nextFloat() - 0.5) * 0.5;
            double ☃xxxxxxx = (☃.nextFloat() - 0.5) * 0.5;
            double ☃xxxxxxxx = (☃.nextFloat() - 0.5) * 0.5;
            int ☃xxxxxxxxx = ☃.nextInt(2) * 2 - 1;
            if (☃.nextBoolean()) {
               ☃xxxxx = ☃.getZ() + 0.5 + 0.25 * ☃xxxxxxxxx;
               ☃xxxxxxxx = ☃.nextFloat() * 2.0F * ☃xxxxxxxxx;
            } else {
               ☃xxx = ☃.getX() + 0.5 + 0.25 * ☃xxxxxxxxx;
               ☃xxxxxx = ☃.nextFloat() * 2.0F * ☃xxxxxxxxx;
            }

            ☃.spawnParticle(EnumParticleTypes.PORTAL, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx);
         }
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return ItemStack.EMPTY;
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.BLACK;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
