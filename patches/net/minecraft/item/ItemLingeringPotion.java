package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemLingeringPotion extends ItemPotion {
   @Override
   public String getItemStackDisplayName(ItemStack var1) {
      return I18n.translateToLocal(PotionUtils.getPotionFromItem(☃).getNamePrefixed("lingering_potion.effect."));
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      PotionUtils.addPotionTooltip(☃, ☃, 0.25F);
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      ItemStack ☃x = ☃.capabilities.isCreativeMode ? ☃.copy() : ☃.splitStack(1);
      ☃.playSound(
         null, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ENTITY_LINGERINGPOTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F)
      );
      if (!☃.isRemote) {
         EntityPotion ☃xx = new EntityPotion(☃, ☃, ☃x);
         ☃xx.shoot(☃, ☃.rotationPitch, ☃.rotationYaw, -20.0F, 0.5F, 1.0F);
         ☃.spawnEntity(☃xx);
      }

      ☃.addStat(StatList.getObjectUseStats(this));
      return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
   }
}
