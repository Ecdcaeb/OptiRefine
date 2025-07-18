package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemEgg extends Item {
   public ItemEgg() {
      this.maxStackSize = 16;
      this.setCreativeTab(CreativeTabs.MATERIALS);
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.capabilities.isCreativeMode) {
         ☃.shrink(1);
      }

      ☃.playSound(null, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
      if (!☃.isRemote) {
         EntityEgg ☃x = new EntityEgg(☃, ☃);
         ☃x.shoot(☃, ☃.rotationPitch, ☃.rotationYaw, 0.0F, 1.5F, 1.0F);
         ☃.spawnEntity(☃x);
      }

      ☃.addStat(StatList.getObjectUseStats(this));
      return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
   }
}
