package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class ItemNameTag extends Item {
   public ItemNameTag() {
      this.setCreativeTab(CreativeTabs.TOOLS);
   }

   @Override
   public boolean itemInteractionForEntity(ItemStack var1, EntityPlayer var2, EntityLivingBase var3, EnumHand var4) {
      if (☃.hasDisplayName() && !(☃ instanceof EntityPlayer)) {
         ☃.setCustomNameTag(☃.getDisplayName());
         if (☃ instanceof EntityLiving) {
            ((EntityLiving)☃).enablePersistence();
         }

         ☃.shrink(1);
         return true;
      } else {
         return false;
      }
   }
}
