package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLilyPad extends BlockBush {
   protected static final AxisAlignedBB LILY_PAD_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.09375, 0.9375);

   protected BlockLilyPad() {
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      if (!(☃ instanceof EntityBoat)) {
         addCollisionBoxToList(☃, ☃, ☃, LILY_PAD_AABB);
      }
   }

   @Override
   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      super.onEntityCollision(☃, ☃, ☃, ☃);
      if (☃ instanceof EntityBoat) {
         ☃.destroyBlock(new BlockPos(☃), true);
      }
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return LILY_PAD_AABB;
   }

   @Override
   protected boolean canSustainBush(IBlockState var1) {
      return ☃.getBlock() == Blocks.WATER || ☃.getMaterial() == Material.ICE;
   }

   @Override
   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      if (☃.getY() >= 0 && ☃.getY() < 256) {
         IBlockState ☃ = ☃.getBlockState(☃.down());
         Material ☃x = ☃.getMaterial();
         return ☃x == Material.WATER && ☃.getValue(BlockLiquid.LEVEL) == 0 || ☃x == Material.ICE;
      } else {
         return false;
      }
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return 0;
   }
}
