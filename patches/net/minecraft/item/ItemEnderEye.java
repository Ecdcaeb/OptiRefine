package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemEnderEye extends Item {
   public ItemEnderEye() {
      this.setCreativeTab(CreativeTabs.MISC);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      IBlockState ☃ = ☃.getBlockState(☃);
      ItemStack ☃x = ☃.getHeldItem(☃);
      if (!☃.canPlayerEdit(☃.offset(☃), ☃, ☃x) || ☃.getBlock() != Blocks.END_PORTAL_FRAME || ☃.getValue(BlockEndPortalFrame.EYE)) {
         return EnumActionResult.FAIL;
      } else if (☃.isRemote) {
         return EnumActionResult.SUCCESS;
      } else {
         ☃.setBlockState(☃, ☃.withProperty(BlockEndPortalFrame.EYE, true), 2);
         ☃.updateComparatorOutputLevel(☃, Blocks.END_PORTAL_FRAME);
         ☃x.shrink(1);

         for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
            double ☃xxx = ☃.getX() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F;
            double ☃xxxx = ☃.getY() + 0.8125F;
            double ☃xxxxx = ☃.getZ() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F;
            double ☃xxxxxx = 0.0;
            double ☃xxxxxxx = 0.0;
            double ☃xxxxxxxx = 0.0;
            ☃.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃xxx, ☃xxxx, ☃xxxxx, 0.0, 0.0, 0.0);
         }

         ☃.playSound(null, ☃, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
         BlockPattern.PatternHelper ☃xx = BlockEndPortalFrame.getOrCreatePortalShape().match(☃, ☃);
         if (☃xx != null) {
            BlockPos ☃xxx = ☃xx.getFrontTopLeft().add(-3, 0, -3);

            for (int ☃xxxx = 0; ☃xxxx < 3; ☃xxxx++) {
               for (int ☃xxxxx = 0; ☃xxxxx < 3; ☃xxxxx++) {
                  ☃.setBlockState(☃xxx.add(☃xxxx, 0, ☃xxxxx), Blocks.END_PORTAL.getDefaultState(), 2);
               }
            }

            ☃.playBroadcastSound(1038, ☃xxx.add(1, 0, 1), 0);
         }

         return EnumActionResult.SUCCESS;
      }
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      RayTraceResult ☃x = this.rayTrace(☃, ☃, false);
      if (☃x != null && ☃x.typeOfHit == RayTraceResult.Type.BLOCK && ☃.getBlockState(☃x.getBlockPos()).getBlock() == Blocks.END_PORTAL_FRAME) {
         return new ActionResult<>(EnumActionResult.PASS, ☃);
      } else {
         ☃.setActiveHand(☃);
         if (!☃.isRemote) {
            BlockPos ☃xx = ((WorldServer)☃).getChunkProvider().getNearestStructurePos(☃, "Stronghold", new BlockPos(☃), false);
            if (☃xx != null) {
               EntityEnderEye ☃xxx = new EntityEnderEye(☃, ☃.posX, ☃.posY + ☃.height / 2.0F, ☃.posZ);
               ☃xxx.moveTowards(☃xx);
               ☃.spawnEntity(☃xxx);
               if (☃ instanceof EntityPlayerMP) {
                  CriteriaTriggers.USED_ENDER_EYE.trigger((EntityPlayerMP)☃, ☃xx);
               }

               ☃.playSound(
                  null, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F)
               );
               ☃.playEvent(null, 1003, new BlockPos(☃), 0);
               if (!☃.capabilities.isCreativeMode) {
                  ☃.shrink(1);
               }

               ☃.addStat(StatList.getObjectUseStats(this));
               return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
            }
         }

         return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
      }
   }
}
