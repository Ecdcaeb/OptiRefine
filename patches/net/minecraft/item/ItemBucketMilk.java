package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBucketMilk extends Item {
   public ItemBucketMilk() {
      this.setMaxStackSize(1);
      this.setCreativeTab(CreativeTabs.MISC);
   }

   @Override
   public ItemStack onItemUseFinish(ItemStack var1, World var2, EntityLivingBase var3) {
      if (☃ instanceof EntityPlayerMP) {
         EntityPlayerMP ☃ = (EntityPlayerMP)☃;
         CriteriaTriggers.CONSUME_ITEM.trigger(☃, ☃);
         ☃.addStat(StatList.getObjectUseStats(this));
      }

      if (☃ instanceof EntityPlayer && !((EntityPlayer)☃).capabilities.isCreativeMode) {
         ☃.shrink(1);
      }

      if (!☃.isRemote) {
         ☃.clearActivePotions();
      }

      return ☃.isEmpty() ? new ItemStack(Items.BUCKET) : ☃;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack var1) {
      return 32;
   }

   @Override
   public EnumAction getItemUseAction(ItemStack var1) {
      return EnumAction.DRINK;
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ☃.setActiveHand(☃);
      return new ActionResult<>(EnumActionResult.SUCCESS, ☃.getHeldItem(☃));
   }
}
