package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemExpBottle extends Item {
   public ItemExpBottle() {
      this.setCreativeTab(CreativeTabs.MISC);
   }

   @Override
   public boolean hasEffect(ItemStack var1) {
      return true;
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.capabilities.isCreativeMode) {
         ☃.shrink(1);
      }

      ☃.playSound(
         null, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F)
      );
      if (!☃.isRemote) {
         EntityExpBottle ☃x = new EntityExpBottle(☃, ☃);
         ☃x.shoot(☃, ☃.rotationPitch, ☃.rotationYaw, -20.0F, 0.7F, 1.0F);
         ☃.spawnEntity(☃x);
      }

      ☃.addStat(StatList.getObjectUseStats(this));
      return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
   }
}
