package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class BlockSponge extends Block {
   public static final PropertyBool WET = PropertyBool.create("wet");

   protected BlockSponge() {
      super(Material.SPONGE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(WET, false));
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal(this.getTranslationKey() + ".dry.name");
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(WET) ? 1 : 0;
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.tryAbsorb(☃, ☃, ☃);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      this.tryAbsorb(☃, ☃, ☃);
      super.neighborChanged(☃, ☃, ☃, ☃, ☃);
   }

   protected void tryAbsorb(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.getValue(WET) && this.absorb(☃, ☃)) {
         ☃.setBlockState(☃, ☃.withProperty(WET, true), 2);
         ☃.playEvent(2001, ☃, Block.getIdFromBlock(Blocks.WATER));
      }
   }

   private boolean absorb(World var1, BlockPos var2) {
      Queue<Tuple<BlockPos, Integer>> ☃ = Lists.newLinkedList();
      List<BlockPos> ☃x = Lists.newArrayList();
      ☃.add(new Tuple<>(☃, 0));
      int ☃xx = 0;

      while (!☃.isEmpty()) {
         Tuple<BlockPos, Integer> ☃xxx = ☃.poll();
         BlockPos ☃xxxx = ☃xxx.getFirst();
         int ☃xxxxx = ☃xxx.getSecond();

         for (EnumFacing ☃xxxxxx : EnumFacing.values()) {
            BlockPos ☃xxxxxxx = ☃xxxx.offset(☃xxxxxx);
            if (☃.getBlockState(☃xxxxxxx).getMaterial() == Material.WATER) {
               ☃.setBlockState(☃xxxxxxx, Blocks.AIR.getDefaultState(), 2);
               ☃x.add(☃xxxxxxx);
               ☃xx++;
               if (☃xxxxx < 6) {
                  ☃.add(new Tuple<>(☃xxxxxxx, ☃xxxxx + 1));
               }
            }
         }

         if (☃xx > 64) {
            break;
         }
      }

      for (BlockPos ☃xxx : ☃x) {
         ☃.notifyNeighborsOfStateChange(☃xxx, Blocks.AIR, false);
      }

      return ☃xx > 0;
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      ☃.add(new ItemStack(this, 1, 0));
      ☃.add(new ItemStack(this, 1, 1));
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(WET, (☃ & 1) == 1);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(WET) ? 1 : 0;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, WET);
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      if (☃.getValue(WET)) {
         EnumFacing ☃ = EnumFacing.random(☃);
         if (☃ != EnumFacing.UP && !☃.getBlockState(☃.offset(☃)).isTopSolid()) {
            double ☃x = ☃.getX();
            double ☃xx = ☃.getY();
            double ☃xxx = ☃.getZ();
            if (☃ == EnumFacing.DOWN) {
               ☃xx -= 0.05;
               ☃x += ☃.nextDouble();
               ☃xxx += ☃.nextDouble();
            } else {
               ☃xx += ☃.nextDouble() * 0.8;
               if (☃.getAxis() == EnumFacing.Axis.X) {
                  ☃xxx += ☃.nextDouble();
                  if (☃ == EnumFacing.EAST) {
                     ☃x++;
                  } else {
                     ☃x += 0.05;
                  }
               } else {
                  ☃x += ☃.nextDouble();
                  if (☃ == EnumFacing.SOUTH) {
                     ☃xxx++;
                  } else {
                     ☃xxx += 0.05;
                  }
               }
            }

            ☃.spawnParticle(EnumParticleTypes.DRIP_WATER, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
         }
      }
   }
}
