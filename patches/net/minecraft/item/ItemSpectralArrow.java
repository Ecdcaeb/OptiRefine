package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.world.World;

public class ItemSpectralArrow extends ItemArrow {
   @Override
   public EntityArrow createArrow(World var1, ItemStack var2, EntityLivingBase var3) {
      return new EntitySpectralArrow(☃, ☃);
   }
}
