package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemAppleGold extends ItemFood {
   public ItemAppleGold(int var1, float var2, boolean var3) {
      super(☃, ☃, ☃);
      this.setHasSubtypes(true);
   }

   @Override
   public boolean hasEffect(ItemStack var1) {
      return super.hasEffect(☃) || ☃.getMetadata() > 0;
   }

   @Override
   public EnumRarity getRarity(ItemStack var1) {
      return ☃.getMetadata() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
   }

   @Override
   protected void onFoodEaten(ItemStack var1, World var2, EntityPlayer var3) {
      if (!☃.isRemote) {
         if (☃.getMetadata() > 0) {
            ☃.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
            ☃.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
            ☃.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
            ☃.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
         } else {
            ☃.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
            ☃.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
         }
      }
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         ☃.add(new ItemStack(this));
         ☃.add(new ItemStack(this, 1, 1));
      }
   }
}
