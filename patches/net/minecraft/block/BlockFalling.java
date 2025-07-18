package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFalling extends Block {
   public static boolean fallInstantly;

   public BlockFalling() {
      super(Material.SAND);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   public BlockFalling(Material var1) {
      super(☃);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      ☃.scheduleUpdate(☃, this, this.tickRate(☃));
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      ☃.scheduleUpdate(☃, this, this.tickRate(☃));
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         this.checkFallable(☃, ☃);
      }
   }

   private void checkFallable(World var1, BlockPos var2) {
      if (canFallThrough(☃.getBlockState(☃.down())) && ☃.getY() >= 0) {
         int ☃ = 32;
         if (fallInstantly || !☃.isAreaLoaded(☃.add(-32, -32, -32), ☃.add(32, 32, 32))) {
            ☃.setBlockToAir(☃);
            BlockPos ☃x = ☃.down();

            while (canFallThrough(☃.getBlockState(☃x)) && ☃x.getY() > 0) {
               ☃x = ☃x.down();
            }

            if (☃x.getY() > 0) {
               ☃.setBlockState(☃x.up(), this.getDefaultState());
            }
         } else if (!☃.isRemote) {
            EntityFallingBlock ☃x = new EntityFallingBlock(☃, ☃.getX() + 0.5, ☃.getY(), ☃.getZ() + 0.5, ☃.getBlockState(☃));
            this.onStartFalling(☃x);
            ☃.spawnEntity(☃x);
         }
      }
   }

   protected void onStartFalling(EntityFallingBlock var1) {
   }

   @Override
   public int tickRate(World var1) {
      return 2;
   }

   public static boolean canFallThrough(IBlockState var0) {
      Block ☃ = ☃.getBlock();
      Material ☃x = ☃.getMaterial();
      return ☃ == Blocks.FIRE || ☃x == Material.AIR || ☃x == Material.WATER || ☃x == Material.LAVA;
   }

   public void onEndFalling(World var1, BlockPos var2, IBlockState var3, IBlockState var4) {
   }

   public void onBroken(World var1, BlockPos var2) {
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      if (☃.nextInt(16) == 0) {
         BlockPos ☃ = ☃.down();
         if (canFallThrough(☃.getBlockState(☃))) {
            double ☃x = ☃.getX() + ☃.nextFloat();
            double ☃xx = ☃.getY() - 0.05;
            double ☃xxx = ☃.getZ() + ☃.nextFloat();
            ☃.spawnParticle(EnumParticleTypes.FALLING_DUST, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0, Block.getStateId(☃));
         }
      }
   }

   public int getDustColor(IBlockState var1) {
      return -16777216;
   }
}
