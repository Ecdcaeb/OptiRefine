package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemPotion extends Item {
   public ItemPotion() {
      this.setMaxStackSize(1);
      this.setCreativeTab(CreativeTabs.BREWING);
   }

   @Override
   public ItemStack getDefaultInstance() {
      return PotionUtils.addPotionToItemStack(super.getDefaultInstance(), PotionTypes.WATER);
   }

   @Override
   public ItemStack onItemUseFinish(ItemStack var1, World var2, EntityLivingBase var3) {
      EntityPlayer ☃ = ☃ instanceof EntityPlayer ? (EntityPlayer)☃ : null;
      if (☃ == null || !☃.capabilities.isCreativeMode) {
         ☃.shrink(1);
      }

      if (☃ instanceof EntityPlayerMP) {
         CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)☃, ☃);
      }

      if (!☃.isRemote) {
         for (PotionEffect ☃x : PotionUtils.getEffectsFromStack(☃)) {
            if (☃x.getPotion().isInstant()) {
               ☃x.getPotion().affectEntity(☃, ☃, ☃, ☃x.getAmplifier(), 1.0);
            } else {
               ☃.addPotionEffect(new PotionEffect(☃x));
            }
         }
      }

      if (☃ != null) {
         ☃.addStat(StatList.getObjectUseStats(this));
      }

      if (☃ == null || !☃.capabilities.isCreativeMode) {
         if (☃.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
         }

         if (☃ != null) {
            ☃.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
         }
      }

      return ☃;
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

   @Override
   public String getItemStackDisplayName(ItemStack var1) {
      return I18n.translateToLocal(PotionUtils.getPotionFromItem(☃).getNamePrefixed("potion.effect."));
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      PotionUtils.addPotionTooltip(☃, ☃, 1.0F);
   }

   @Override
   public boolean hasEffect(ItemStack var1) {
      return super.hasEffect(☃) || !PotionUtils.getEffectsFromStack(☃).isEmpty();
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         for (PotionType ☃ : PotionType.REGISTRY) {
            if (☃ != PotionTypes.EMPTY) {
               ☃.add(PotionUtils.addPotionToItemStack(new ItemStack(this), ☃));
            }
         }
      }
   }
}
