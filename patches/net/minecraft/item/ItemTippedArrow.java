package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemTippedArrow extends ItemArrow {
   @Override
   public ItemStack getDefaultInstance() {
      return PotionUtils.addPotionToItemStack(super.getDefaultInstance(), PotionTypes.POISON);
   }

   @Override
   public EntityArrow createArrow(World var1, ItemStack var2, EntityLivingBase var3) {
      EntityTippedArrow ☃ = new EntityTippedArrow(☃, ☃);
      ☃.setPotionEffect(☃);
      return ☃;
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         for (PotionType ☃ : PotionType.REGISTRY) {
            if (!☃.getEffects().isEmpty()) {
               ☃.add(PotionUtils.addPotionToItemStack(new ItemStack(this), ☃));
            }
         }
      }
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      PotionUtils.addPotionTooltip(☃, ☃, 0.125F);
   }

   @Override
   public String getItemStackDisplayName(ItemStack var1) {
      return I18n.translateToLocal(PotionUtils.getPotionFromItem(☃).getNamePrefixed("tipped_arrow.effect."));
   }
}
