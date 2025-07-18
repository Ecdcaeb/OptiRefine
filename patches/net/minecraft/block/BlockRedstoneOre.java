package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRedstoneOre extends Block {
   private final boolean isOn;

   public BlockRedstoneOre(boolean var1) {
      super(Material.ROCK);
      if (☃) {
         this.setTickRandomly(true);
      }

      this.isOn = ☃;
   }

   @Override
   public int tickRate(World var1) {
      return 30;
   }

   @Override
   public void onBlockClicked(World var1, BlockPos var2, EntityPlayer var3) {
      this.activate(☃, ☃);
      super.onBlockClicked(☃, ☃, ☃);
   }

   @Override
   public void onEntityWalk(World var1, BlockPos var2, Entity var3) {
      this.activate(☃, ☃);
      super.onEntityWalk(☃, ☃, ☃);
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      this.activate(☃, ☃);
      return super.onBlockActivated(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   private void activate(World var1, BlockPos var2) {
      this.spawnParticles(☃, ☃);
      if (this == Blocks.REDSTONE_ORE) {
         ☃.setBlockState(☃, Blocks.LIT_REDSTONE_ORE.getDefaultState());
      }
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (this == Blocks.LIT_REDSTONE_ORE) {
         ☃.setBlockState(☃, Blocks.REDSTONE_ORE.getDefaultState());
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.REDSTONE;
   }

   @Override
   public int quantityDroppedWithBonus(int var1, Random var2) {
      return this.quantityDropped(☃) + ☃.nextInt(☃ + 1);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 4 + ☃.nextInt(2);
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      super.dropBlockAsItemWithChance(☃, ☃, ☃, ☃, ☃);
      if (this.getItemDropped(☃, ☃.rand, ☃) != Item.getItemFromBlock(this)) {
         int ☃ = 1 + ☃.rand.nextInt(5);
         this.dropXpOnBlockBreak(☃, ☃, ☃);
      }
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      if (this.isOn) {
         this.spawnParticles(☃, ☃);
      }
   }

   private void spawnParticles(World var1, BlockPos var2) {
      Random ☃ = ☃.rand;
      double ☃x = 0.0625;

      for (int ☃xx = 0; ☃xx < 6; ☃xx++) {
         double ☃xxx = ☃.getX() + ☃.nextFloat();
         double ☃xxxx = ☃.getY() + ☃.nextFloat();
         double ☃xxxxx = ☃.getZ() + ☃.nextFloat();
         if (☃xx == 0 && !☃.getBlockState(☃.up()).isOpaqueCube()) {
            ☃xxxx = ☃.getY() + 0.0625 + 1.0;
         }

         if (☃xx == 1 && !☃.getBlockState(☃.down()).isOpaqueCube()) {
            ☃xxxx = ☃.getY() - 0.0625;
         }

         if (☃xx == 2 && !☃.getBlockState(☃.south()).isOpaqueCube()) {
            ☃xxxxx = ☃.getZ() + 0.0625 + 1.0;
         }

         if (☃xx == 3 && !☃.getBlockState(☃.north()).isOpaqueCube()) {
            ☃xxxxx = ☃.getZ() - 0.0625;
         }

         if (☃xx == 4 && !☃.getBlockState(☃.east()).isOpaqueCube()) {
            ☃xxx = ☃.getX() + 0.0625 + 1.0;
         }

         if (☃xx == 5 && !☃.getBlockState(☃.west()).isOpaqueCube()) {
            ☃xxx = ☃.getX() - 0.0625;
         }

         if (☃xxx < ☃.getX() || ☃xxx > ☃.getX() + 1 || ☃xxxx < 0.0 || ☃xxxx > ☃.getY() + 1 || ☃xxxxx < ☃.getZ() || ☃xxxxx > ☃.getZ() + 1) {
            ☃.spawnParticle(EnumParticleTypes.REDSTONE, ☃xxx, ☃xxxx, ☃xxxxx, 0.0, 0.0, 0.0);
         }
      }
   }

   @Override
   protected ItemStack getSilkTouchDrop(IBlockState var1) {
      return new ItemStack(Blocks.REDSTONE_ORE);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Item.getItemFromBlock(Blocks.REDSTONE_ORE), 1, this.damageDropped(☃));
   }
}
