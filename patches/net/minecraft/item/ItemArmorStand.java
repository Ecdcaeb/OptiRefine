package net.minecraft.item;

import java.util.List;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.World;

public class ItemArmorStand extends Item {
   public ItemArmorStand() {
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      if (☃ == EnumFacing.DOWN) {
         return EnumActionResult.FAIL;
      } else {
         boolean ☃ = ☃.getBlockState(☃).getBlock().isReplaceable(☃, ☃);
         BlockPos ☃x = ☃ ? ☃ : ☃.offset(☃);
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
                     ☃.setBlockToAir(☃x);
                     ☃.setBlockToAir(☃xxx);
                     EntityArmorStand ☃xxxxxxxxx = new EntityArmorStand(☃, ☃xxxxx + 0.5, ☃xxxxxx, ☃xxxxxxx + 0.5);
                     float ☃xxxxxxxxxx = MathHelper.floor((MathHelper.wrapDegrees(☃.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                     ☃xxxxxxxxx.setLocationAndAngles(☃xxxxx + 0.5, ☃xxxxxx, ☃xxxxxxx + 0.5, ☃xxxxxxxxxx, 0.0F);
                     this.applyRandomRotations(☃xxxxxxxxx, ☃.rand);
                     ItemMonsterPlacer.applyItemEntityDataToEntity(☃, ☃, ☃xx, ☃xxxxxxxxx);
                     ☃.spawnEntity(☃xxxxxxxxx);
                     ☃.playSound(
                        null, ☃xxxxxxxxx.posX, ☃xxxxxxxxx.posY, ☃xxxxxxxxx.posZ, SoundEvents.ENTITY_ARMORSTAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F
                     );
                  }

                  ☃xx.shrink(1);
                  return EnumActionResult.SUCCESS;
               }
            }
         }
      }
   }

   private void applyRandomRotations(EntityArmorStand var1, Random var2) {
      Rotations ☃ = ☃.getHeadRotation();
      float ☃x = ☃.nextFloat() * 5.0F;
      float ☃xx = ☃.nextFloat() * 20.0F - 10.0F;
      Rotations ☃xxx = new Rotations(☃.getX() + ☃x, ☃.getY() + ☃xx, ☃.getZ());
      ☃.setHeadRotation(☃xxx);
      ☃ = ☃.getBodyRotation();
      ☃x = ☃.nextFloat() * 10.0F - 5.0F;
      ☃xxx = new Rotations(☃.getX(), ☃.getY() + ☃x, ☃.getZ());
      ☃.setBodyRotation(☃xxx);
   }
}
