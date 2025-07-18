package net.minecraft.enchantment;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EnchantmentFrostWalker extends Enchantment {
   public EnchantmentFrostWalker(Enchantment.Rarity var1, EntityEquipmentSlot... var2) {
      super(☃, EnumEnchantmentType.ARMOR_FEET, ☃);
      this.setName("frostWalker");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return ☃ * 10;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(☃) + 15;
   }

   @Override
   public boolean isTreasureEnchantment() {
      return true;
   }

   @Override
   public int getMaxLevel() {
      return 2;
   }

   public static void freezeNearby(EntityLivingBase var0, World var1, BlockPos var2, int var3) {
      if (☃.onGround) {
         float ☃ = Math.min(16, 2 + ☃);
         BlockPos.MutableBlockPos ☃x = new BlockPos.MutableBlockPos(0, 0, 0);

         for (BlockPos.MutableBlockPos ☃xx : BlockPos.getAllInBoxMutable(☃.add((double)(-☃), -1.0, (double)(-☃)), ☃.add((double)☃, -1.0, (double)☃))) {
            if (☃xx.distanceSqToCenter(☃.posX, ☃.posY, ☃.posZ) <= ☃ * ☃) {
               ☃x.setPos(☃xx.getX(), ☃xx.getY() + 1, ☃xx.getZ());
               IBlockState ☃xxx = ☃.getBlockState(☃x);
               if (☃xxx.getMaterial() == Material.AIR) {
                  IBlockState ☃xxxx = ☃.getBlockState(☃xx);
                  if (☃xxxx.getMaterial() == Material.WATER
                     && ☃xxxx.getValue(BlockLiquid.LEVEL) == 0
                     && ☃.mayPlace(Blocks.FROSTED_ICE, ☃xx, false, EnumFacing.DOWN, null)) {
                     ☃.setBlockState(☃xx, Blocks.FROSTED_ICE.getDefaultState());
                     ☃.scheduleUpdate(☃xx.toImmutable(), Blocks.FROSTED_ICE, MathHelper.getInt(☃.getRNG(), 60, 120));
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean canApplyTogether(Enchantment var1) {
      return super.canApplyTogether(☃) && ☃ != Enchantments.DEPTH_STRIDER;
   }
}
