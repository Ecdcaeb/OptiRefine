package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.world.World;

public class ItemArrow extends Item {
   public ItemArrow() {
      this.setCreativeTab(CreativeTabs.COMBAT);
   }

   public EntityArrow createArrow(World var1, ItemStack var2, EntityLivingBase var3) {
      EntityTippedArrow ☃ = new EntityTippedArrow(☃, ☃);
      ☃.setPotionEffect(☃);
      return ☃;
   }
}
