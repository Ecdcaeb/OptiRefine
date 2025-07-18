package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCarrotOnAStick extends Item {
   public ItemCarrotOnAStick() {
      this.setCreativeTab(CreativeTabs.TRANSPORTATION);
      this.setMaxStackSize(1);
      this.setMaxDamage(25);
   }

   @Override
   public boolean isFull3D() {
      return true;
   }

   @Override
   public boolean shouldRotateAroundWhenRendering() {
      return true;
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.isRemote) {
         return new ActionResult<>(EnumActionResult.PASS, ☃);
      } else {
         if (☃.isRiding() && ☃.getRidingEntity() instanceof EntityPig) {
            EntityPig ☃x = (EntityPig)☃.getRidingEntity();
            if (☃.getMaxDamage() - ☃.getMetadata() >= 7 && ☃x.boost()) {
               ☃.damageItem(7, ☃);
               if (☃.isEmpty()) {
                  ItemStack ☃xx = new ItemStack(Items.FISHING_ROD);
                  ☃xx.setTagCompound(☃.getTagCompound());
                  return new ActionResult<>(EnumActionResult.SUCCESS, ☃xx);
               }

               return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
            }
         }

         ☃.addStat(StatList.getObjectUseStats(this));
         return new ActionResult<>(EnumActionResult.PASS, ☃);
      }
   }
}
