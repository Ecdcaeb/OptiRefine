package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemBucket extends Item {
   private final Block containedBlock;

   public ItemBucket(Block var1) {
      this.maxStackSize = 1;
      this.containedBlock = ☃;
      this.setCreativeTab(CreativeTabs.MISC);
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      boolean ☃ = this.containedBlock == Blocks.AIR;
      ItemStack ☃x = ☃.getHeldItem(☃);
      RayTraceResult ☃xx = this.rayTrace(☃, ☃, ☃);
      if (☃xx == null) {
         return new ActionResult<>(EnumActionResult.PASS, ☃x);
      } else if (☃xx.typeOfHit != RayTraceResult.Type.BLOCK) {
         return new ActionResult<>(EnumActionResult.PASS, ☃x);
      } else {
         BlockPos ☃xxx = ☃xx.getBlockPos();
         if (!☃.isBlockModifiable(☃, ☃xxx)) {
            return new ActionResult<>(EnumActionResult.FAIL, ☃x);
         } else if (☃) {
            if (!☃.canPlayerEdit(☃xxx.offset(☃xx.sideHit), ☃xx.sideHit, ☃x)) {
               return new ActionResult<>(EnumActionResult.FAIL, ☃x);
            } else {
               IBlockState ☃xxxx = ☃.getBlockState(☃xxx);
               Material ☃xxxxx = ☃xxxx.getMaterial();
               if (☃xxxxx == Material.WATER && ☃xxxx.getValue(BlockLiquid.LEVEL) == 0) {
                  ☃.setBlockState(☃xxx, Blocks.AIR.getDefaultState(), 11);
                  ☃.addStat(StatList.getObjectUseStats(this));
                  ☃.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                  return new ActionResult<>(EnumActionResult.SUCCESS, this.fillBucket(☃x, ☃, Items.WATER_BUCKET));
               } else if (☃xxxxx == Material.LAVA && ☃xxxx.getValue(BlockLiquid.LEVEL) == 0) {
                  ☃.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
                  ☃.setBlockState(☃xxx, Blocks.AIR.getDefaultState(), 11);
                  ☃.addStat(StatList.getObjectUseStats(this));
                  return new ActionResult<>(EnumActionResult.SUCCESS, this.fillBucket(☃x, ☃, Items.LAVA_BUCKET));
               } else {
                  return new ActionResult<>(EnumActionResult.FAIL, ☃x);
               }
            }
         } else {
            boolean ☃xxxx = ☃.getBlockState(☃xxx).getBlock().isReplaceable(☃, ☃xxx);
            BlockPos ☃xxxxx = ☃xxxx && ☃xx.sideHit == EnumFacing.UP ? ☃xxx : ☃xxx.offset(☃xx.sideHit);
            if (!☃.canPlayerEdit(☃xxxxx, ☃xx.sideHit, ☃x)) {
               return new ActionResult<>(EnumActionResult.FAIL, ☃x);
            } else if (this.tryPlaceContainedLiquid(☃, ☃, ☃xxxxx)) {
               if (☃ instanceof EntityPlayerMP) {
                  CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)☃, ☃xxxxx, ☃x);
               }

               ☃.addStat(StatList.getObjectUseStats(this));
               return !☃.capabilities.isCreativeMode
                  ? new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET))
                  : new ActionResult<>(EnumActionResult.SUCCESS, ☃x);
            } else {
               return new ActionResult<>(EnumActionResult.FAIL, ☃x);
            }
         }
      }
   }

   private ItemStack fillBucket(ItemStack var1, EntityPlayer var2, Item var3) {
      if (☃.capabilities.isCreativeMode) {
         return ☃;
      } else {
         ☃.shrink(1);
         if (☃.isEmpty()) {
            return new ItemStack(☃);
         } else {
            if (!☃.inventory.addItemStackToInventory(new ItemStack(☃))) {
               ☃.dropItem(new ItemStack(☃), false);
            }

            return ☃;
         }
      }
   }

   public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer var1, World var2, BlockPos var3) {
      if (this.containedBlock == Blocks.AIR) {
         return false;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃);
         Material ☃x = ☃.getMaterial();
         boolean ☃xx = !☃x.isSolid();
         boolean ☃xxx = ☃.getBlock().isReplaceable(☃, ☃);
         if (!☃.isAirBlock(☃) && !☃xx && !☃xxx) {
            return false;
         } else {
            if (☃.provider.doesWaterVaporize() && this.containedBlock == Blocks.FLOWING_WATER) {
               int ☃xxxx = ☃.getX();
               int ☃xxxxx = ☃.getY();
               int ☃xxxxxx = ☃.getZ();
               ☃.playSound(☃, ☃, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (☃.rand.nextFloat() - ☃.rand.nextFloat()) * 0.8F);

               for (int ☃xxxxxxx = 0; ☃xxxxxxx < 8; ☃xxxxxxx++) {
                  ☃.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ☃xxxx + Math.random(), ☃xxxxx + Math.random(), ☃xxxxxx + Math.random(), 0.0, 0.0, 0.0);
               }
            } else {
               if (!☃.isRemote && (☃xx || ☃xxx) && !☃x.isLiquid()) {
                  ☃.destroyBlock(☃, true);
               }

               SoundEvent ☃xxxx = this.containedBlock == Blocks.FLOWING_LAVA ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
               ☃.playSound(☃, ☃, ☃xxxx, SoundCategory.BLOCKS, 1.0F, 1.0F);
               ☃.setBlockState(☃, this.containedBlock.getDefaultState(), 11);
            }

            return true;
         }
      }
   }
}
