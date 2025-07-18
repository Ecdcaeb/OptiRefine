package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDragonEgg extends Block {
   protected static final AxisAlignedBB DRAGON_EGG_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);

   public BlockDragonEgg() {
      super(Material.DRAGON_EGG, MapColor.BLACK);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return DRAGON_EGG_AABB;
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
      this.checkFall(☃, ☃);
   }

   private void checkFall(World var1, BlockPos var2) {
      if (BlockFalling.canFallThrough(☃.getBlockState(☃.down())) && ☃.getY() >= 0) {
         int ☃ = 32;
         if (!BlockFalling.fallInstantly && ☃.isAreaLoaded(☃.add(-32, -32, -32), ☃.add(32, 32, 32))) {
            ☃.spawnEntity(new EntityFallingBlock(☃, ☃.getX() + 0.5F, ☃.getY(), ☃.getZ() + 0.5F, this.getDefaultState()));
         } else {
            ☃.setBlockToAir(☃);
            BlockPos ☃x = ☃;

            while (BlockFalling.canFallThrough(☃.getBlockState(☃x)) && ☃x.getY() > 0) {
               ☃x = ☃x.down();
            }

            if (☃x.getY() > 0) {
               ☃.setBlockState(☃x, this.getDefaultState(), 2);
            }
         }
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      this.teleport(☃, ☃);
      return true;
   }

   @Override
   public void onBlockClicked(World var1, BlockPos var2, EntityPlayer var3) {
      this.teleport(☃, ☃);
   }

   private void teleport(World var1, BlockPos var2) {
      IBlockState ☃ = ☃.getBlockState(☃);
      if (☃.getBlock() == this) {
         for (int ☃x = 0; ☃x < 1000; ☃x++) {
            BlockPos ☃xx = ☃.add(☃.rand.nextInt(16) - ☃.rand.nextInt(16), ☃.rand.nextInt(8) - ☃.rand.nextInt(8), ☃.rand.nextInt(16) - ☃.rand.nextInt(16));
            if (☃.getBlockState(☃xx).getBlock().material == Material.AIR) {
               if (☃.isRemote) {
                  for (int ☃xxx = 0; ☃xxx < 128; ☃xxx++) {
                     double ☃xxxx = ☃.rand.nextDouble();
                     float ☃xxxxx = (☃.rand.nextFloat() - 0.5F) * 0.2F;
                     float ☃xxxxxx = (☃.rand.nextFloat() - 0.5F) * 0.2F;
                     float ☃xxxxxxx = (☃.rand.nextFloat() - 0.5F) * 0.2F;
                     double ☃xxxxxxxx = ☃xx.getX() + (☃.getX() - ☃xx.getX()) * ☃xxxx + (☃.rand.nextDouble() - 0.5) + 0.5;
                     double ☃xxxxxxxxx = ☃xx.getY() + (☃.getY() - ☃xx.getY()) * ☃xxxx + ☃.rand.nextDouble() - 0.5;
                     double ☃xxxxxxxxxx = ☃xx.getZ() + (☃.getZ() - ☃xx.getZ()) * ☃xxxx + (☃.rand.nextDouble() - 0.5) + 0.5;
                     ☃.spawnParticle(EnumParticleTypes.PORTAL, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
                  }
               } else {
                  ☃.setBlockState(☃xx, ☃, 2);
                  ☃.setBlockToAir(☃);
               }

               return;
            }
         }
      }
   }

   @Override
   public int tickRate(World var1) {
      return 5;
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
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return true;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
