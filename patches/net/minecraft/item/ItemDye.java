package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDye extends Item {
   public static final int[] DYE_COLORS = new int[]{
      1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320
   };

   public ItemDye() {
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(CreativeTabs.MATERIALS);
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      int ☃ = ☃.getMetadata();
      return super.getTranslationKey() + "." + EnumDyeColor.byDyeDamage(☃).getTranslationKey();
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.canPlayerEdit(☃.offset(☃), ☃, ☃)) {
         return EnumActionResult.FAIL;
      } else {
         EnumDyeColor ☃x = EnumDyeColor.byDyeDamage(☃.getMetadata());
         if (☃x == EnumDyeColor.WHITE) {
            if (applyBonemeal(☃, ☃, ☃)) {
               if (!☃.isRemote) {
                  ☃.playEvent(2005, ☃, 0);
               }

               return EnumActionResult.SUCCESS;
            }
         } else if (☃x == EnumDyeColor.BROWN) {
            IBlockState ☃xx = ☃.getBlockState(☃);
            Block ☃xxx = ☃xx.getBlock();
            if (☃xxx == Blocks.LOG && ☃xx.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
               if (☃ == EnumFacing.DOWN || ☃ == EnumFacing.UP) {
                  return EnumActionResult.FAIL;
               }

               ☃ = ☃.offset(☃);
               if (☃.isAirBlock(☃)) {
                  IBlockState ☃xxxx = Blocks.COCOA.getStateForPlacement(☃, ☃, ☃, ☃, ☃, ☃, 0, ☃);
                  ☃.setBlockState(☃, ☃xxxx, 10);
                  if (!☃.capabilities.isCreativeMode) {
                     ☃.shrink(1);
                  }

                  return EnumActionResult.SUCCESS;
               }
            }

            return EnumActionResult.FAIL;
         }

         return EnumActionResult.PASS;
      }
   }

   public static boolean applyBonemeal(ItemStack var0, World var1, BlockPos var2) {
      IBlockState ☃ = ☃.getBlockState(☃);
      if (☃.getBlock() instanceof IGrowable) {
         IGrowable ☃x = (IGrowable)☃.getBlock();
         if (☃x.canGrow(☃, ☃, ☃, ☃.isRemote)) {
            if (!☃.isRemote) {
               if (☃x.canUseBonemeal(☃, ☃.rand, ☃, ☃)) {
                  ☃x.grow(☃, ☃.rand, ☃, ☃);
               }

               ☃.shrink(1);
            }

            return true;
         }
      }

      return false;
   }

   public static void spawnBonemealParticles(World var0, BlockPos var1, int var2) {
      if (☃ == 0) {
         ☃ = 15;
      }

      IBlockState ☃ = ☃.getBlockState(☃);
      if (☃.getMaterial() != Material.AIR) {
         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            double ☃xx = itemRand.nextGaussian() * 0.02;
            double ☃xxx = itemRand.nextGaussian() * 0.02;
            double ☃xxxx = itemRand.nextGaussian() * 0.02;
            ☃.spawnParticle(
               EnumParticleTypes.VILLAGER_HAPPY,
               ☃.getX() + itemRand.nextFloat(),
               ☃.getY() + itemRand.nextFloat() * ☃.getBoundingBox(☃, ☃).maxY,
               ☃.getZ() + itemRand.nextFloat(),
               ☃xx,
               ☃xxx,
               ☃xxxx
            );
         }
      }
   }

   @Override
   public boolean itemInteractionForEntity(ItemStack var1, EntityPlayer var2, EntityLivingBase var3, EnumHand var4) {
      if (☃ instanceof EntitySheep) {
         EntitySheep ☃ = (EntitySheep)☃;
         EnumDyeColor ☃x = EnumDyeColor.byDyeDamage(☃.getMetadata());
         if (!☃.getSheared() && ☃.getFleeceColor() != ☃x) {
            ☃.setFleeceColor(☃x);
            ☃.shrink(1);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         for (int ☃ = 0; ☃ < 16; ☃++) {
            ☃.add(new ItemStack(this, 1, ☃));
         }
      }
   }
}
