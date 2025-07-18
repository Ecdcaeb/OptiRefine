package net.minecraft.item;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class ItemEndCrystal extends Item {
   public ItemEndCrystal() {
      this.setTranslationKey("end_crystal");
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      IBlockState ☃ = ☃.getBlockState(☃);
      if (☃.getBlock() != Blocks.OBSIDIAN && ☃.getBlock() != Blocks.BEDROCK) {
         return EnumActionResult.FAIL;
      } else {
         BlockPos ☃x = ☃.up();
         ItemStack ☃xx = ☃.getHeldItem(☃);
         if (!☃.canPlayerEdit(☃x, ☃, ☃xx)) {
            return EnumActionResult.FAIL;
         } else {
            BlockPos ☃xxx = ☃x.up();
            boolean ☃xxxx = !☃.isAirBlock(☃x) && !☃.getBlockState(☃x).getBlock().isReplaceable(☃, ☃x);
            ☃xxxx |= !☃.isAirBlock(☃xxx) && !☃.getBlockState(☃xxx).getBlock().isReplaceable(☃, ☃xxx);
            if (☃xxxx) {
               return EnumActionResult.FAIL;
            } else {
               double ☃xxxxx = ☃x.getX();
               double ☃xxxxxx = ☃x.getY();
               double ☃xxxxxxx = ☃x.getZ();
               List<Entity> ☃xxxxxxxx = ☃.getEntitiesWithinAABBExcludingEntity(
                  null, new AxisAlignedBB(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxx + 1.0, ☃xxxxxx + 2.0, ☃xxxxxxx + 1.0)
               );
               if (!☃xxxxxxxx.isEmpty()) {
                  return EnumActionResult.FAIL;
               } else {
                  if (!☃.isRemote) {
                     EntityEnderCrystal ☃xxxxxxxxx = new EntityEnderCrystal(☃, ☃.getX() + 0.5F, ☃.getY() + 1, ☃.getZ() + 0.5F);
                     ☃xxxxxxxxx.setShowBottom(false);
                     ☃.spawnEntity(☃xxxxxxxxx);
                     if (☃.provider instanceof WorldProviderEnd) {
                        DragonFightManager ☃xxxxxxxxxx = ((WorldProviderEnd)☃.provider).getDragonFightManager();
                        ☃xxxxxxxxxx.respawnDragon();
                     }
                  }

                  ☃xx.shrink(1);
                  return EnumActionResult.SUCCESS;
               }
            }
         }
      }
   }

   @Override
   public boolean hasEffect(ItemStack var1) {
      return true;
   }
}
