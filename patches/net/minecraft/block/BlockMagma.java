package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockMagma extends Block {
   public BlockMagma() {
      super(Material.ROCK);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
      this.setLightLevel(0.2F);
      this.setTickRandomly(true);
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.NETHERRACK;
   }

   @Override
   public void onEntityWalk(World var1, BlockPos var2, Entity var3) {
      if (!☃.isImmuneToFire() && ☃ instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase)☃)) {
         ☃.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
      }

      super.onEntityWalk(☃, ☃, ☃);
   }

   @Override
   public int getPackedLightmapCoords(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return 15728880;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      BlockPos ☃ = ☃.up();
      IBlockState ☃x = ☃.getBlockState(☃);
      if (☃x.getBlock() == Blocks.WATER || ☃x.getBlock() == Blocks.FLOWING_WATER) {
         ☃.setBlockToAir(☃);
         ☃.playSound(null, ☃, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (☃.rand.nextFloat() - ☃.rand.nextFloat()) * 0.8F);
         if (☃ instanceof WorldServer) {
            ((WorldServer)☃).spawnParticle(EnumParticleTypes.SMOKE_LARGE, ☃.getX() + 0.5, ☃.getY() + 0.25, ☃.getZ() + 0.5, 8, 0.5, 0.25, 0.5, 0.0);
         }
      }
   }

   @Override
   public boolean canEntitySpawn(IBlockState var1, Entity var2) {
      return ☃.isImmuneToFire();
   }
}
