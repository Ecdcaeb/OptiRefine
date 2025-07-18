package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneTorch extends BlockTorch {
   private static final Map<World, List<BlockRedstoneTorch.Toggle>> toggles = Maps.newHashMap();
   private final boolean isOn;

   private boolean isBurnedOut(World var1, BlockPos var2, boolean var3) {
      if (!toggles.containsKey(☃)) {
         toggles.put(☃, Lists.newArrayList());
      }

      List<BlockRedstoneTorch.Toggle> ☃ = toggles.get(☃);
      if (☃) {
         ☃.add(new BlockRedstoneTorch.Toggle(☃, ☃.getTotalWorldTime()));
      }

      int ☃x = 0;

      for (int ☃xx = 0; ☃xx < ☃.size(); ☃xx++) {
         BlockRedstoneTorch.Toggle ☃xxx = ☃.get(☃xx);
         if (☃xxx.pos.equals(☃)) {
            if (++☃x >= 8) {
               return true;
            }
         }
      }

      return false;
   }

   protected BlockRedstoneTorch(boolean var1) {
      this.isOn = ☃;
      this.setTickRandomly(true);
      this.setCreativeTab(null);
   }

   @Override
   public int tickRate(World var1) {
      return 2;
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (this.isOn) {
         for (EnumFacing ☃ : EnumFacing.values()) {
            ☃.notifyNeighborsOfStateChange(☃.offset(☃), this, false);
         }
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (this.isOn) {
         for (EnumFacing ☃ : EnumFacing.values()) {
            ☃.notifyNeighborsOfStateChange(☃.offset(☃), this, false);
         }
      }
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return this.isOn && ☃.getValue(FACING) != ☃ ? 15 : 0;
   }

   private boolean shouldBeOff(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing ☃ = ☃.getValue(FACING).getOpposite();
      return ☃.isSidePowered(☃.offset(☃), ☃);
   }

   @Override
   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      boolean ☃ = this.shouldBeOff(☃, ☃, ☃);
      List<BlockRedstoneTorch.Toggle> ☃x = toggles.get(☃);

      while (☃x != null && !☃x.isEmpty() && ☃.getTotalWorldTime() - ☃x.get(0).time > 60L) {
         ☃x.remove(0);
      }

      if (this.isOn) {
         if (☃) {
            ☃.setBlockState(☃, Blocks.UNLIT_REDSTONE_TORCH.getDefaultState().withProperty(FACING, ☃.getValue(FACING)), 3);
            if (this.isBurnedOut(☃, ☃, true)) {
               ☃.playSound(
                  null, ☃, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.5F, 2.6F + (☃.rand.nextFloat() - ☃.rand.nextFloat()) * 0.8F
               );

               for (int ☃xx = 0; ☃xx < 5; ☃xx++) {
                  double ☃xxx = ☃.getX() + ☃.nextDouble() * 0.6 + 0.2;
                  double ☃xxxx = ☃.getY() + ☃.nextDouble() * 0.6 + 0.2;
                  double ☃xxxxx = ☃.getZ() + ☃.nextDouble() * 0.6 + 0.2;
                  ☃.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃xxx, ☃xxxx, ☃xxxxx, 0.0, 0.0, 0.0);
               }

               ☃.scheduleUpdate(☃, ☃.getBlockState(☃).getBlock(), 160);
            }
         }
      } else if (!☃ && !this.isBurnedOut(☃, ☃, false)) {
         ☃.setBlockState(☃, Blocks.REDSTONE_TORCH.getDefaultState().withProperty(FACING, ☃.getValue(FACING)), 3);
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!this.onNeighborChangeInternal(☃, ☃, ☃)) {
         if (this.isOn == this.shouldBeOff(☃, ☃, ☃)) {
            ☃.scheduleUpdate(☃, this, this.tickRate(☃));
         }
      }
   }

   @Override
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.DOWN ? ☃.getWeakPower(☃, ☃, ☃) : 0;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.REDSTONE_TORCH);
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      if (this.isOn) {
         double ☃ = ☃.getX() + 0.5 + (☃.nextDouble() - 0.5) * 0.2;
         double ☃x = ☃.getY() + 0.7 + (☃.nextDouble() - 0.5) * 0.2;
         double ☃xx = ☃.getZ() + 0.5 + (☃.nextDouble() - 0.5) * 0.2;
         EnumFacing ☃xxx = ☃.getValue(FACING);
         if (☃xxx.getAxis().isHorizontal()) {
            EnumFacing ☃xxxx = ☃xxx.getOpposite();
            double ☃xxxxx = 0.27;
            ☃ += 0.27 * ☃xxxx.getXOffset();
            ☃x += 0.22;
            ☃xx += 0.27 * ☃xxxx.getZOffset();
         }

         ☃.spawnParticle(EnumParticleTypes.REDSTONE, ☃, ☃x, ☃xx, 0.0, 0.0, 0.0);
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Blocks.REDSTONE_TORCH);
   }

   @Override
   public boolean isAssociatedBlock(Block var1) {
      return ☃ == Blocks.UNLIT_REDSTONE_TORCH || ☃ == Blocks.REDSTONE_TORCH;
   }

   static class Toggle {
      BlockPos pos;
      long time;

      public Toggle(BlockPos var1, long var2) {
         this.pos = ☃;
         this.time = ☃;
      }
   }
}
