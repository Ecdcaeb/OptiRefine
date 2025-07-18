package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockIce extends BlockBreakable {
   public BlockIce() {
      super(Material.ICE, false);
      this.slipperiness = 0.98F;
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      ☃.addStat(StatList.getBlockStats(this));
      ☃.addExhaustion(0.005F);
      if (this.canSilkHarvest() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, ☃) > 0) {
         spawnAsEntity(☃, ☃, this.getSilkTouchDrop(☃));
      } else {
         if (☃.provider.doesWaterVaporize()) {
            ☃.setBlockToAir(☃);
            return;
         }

         int ☃ = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, ☃);
         this.dropBlockAsItem(☃, ☃, ☃, ☃);
         Material ☃x = ☃.getBlockState(☃.down()).getMaterial();
         if (☃x.blocksMovement() || ☃x.isLiquid()) {
            ☃.setBlockState(☃, Blocks.FLOWING_WATER.getDefaultState());
         }
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (☃.getLightFor(EnumSkyBlock.BLOCK, ☃) > 11 - this.getDefaultState().getLightOpacity()) {
         this.turnIntoWater(☃, ☃);
      }
   }

   protected void turnIntoWater(World var1, BlockPos var2) {
      if (☃.provider.doesWaterVaporize()) {
         ☃.setBlockToAir(☃);
      } else {
         this.dropBlockAsItem(☃, ☃, ☃.getBlockState(☃), 0);
         ☃.setBlockState(☃, Blocks.WATER.getDefaultState());
         ☃.neighborChanged(☃, Blocks.WATER, ☃);
      }
   }

   @Override
   public EnumPushReaction getPushReaction(IBlockState var1) {
      return EnumPushReaction.NORMAL;
   }
}
