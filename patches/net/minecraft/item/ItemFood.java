package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFood extends Item {
   public final int itemUseDuration = 32;
   private final int healAmount;
   private final float saturationModifier;
   private final boolean isWolfsFavoriteMeat;
   private boolean alwaysEdible;
   private PotionEffect potionId;
   private float potionEffectProbability;

   public ItemFood(int var1, float var2, boolean var3) {
      this.healAmount = ☃;
      this.isWolfsFavoriteMeat = ☃;
      this.saturationModifier = ☃;
      this.setCreativeTab(CreativeTabs.FOOD);
   }

   public ItemFood(int var1, boolean var2) {
      this(☃, 0.6F, ☃);
   }

   @Override
   public ItemStack onItemUseFinish(ItemStack var1, World var2, EntityLivingBase var3) {
      if (☃ instanceof EntityPlayer) {
         EntityPlayer ☃ = (EntityPlayer)☃;
         ☃.getFoodStats().addStats(this, ☃);
         ☃.playSound(null, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, ☃.rand.nextFloat() * 0.1F + 0.9F);
         this.onFoodEaten(☃, ☃, ☃);
         ☃.addStat(StatList.getObjectUseStats(this));
         if (☃ instanceof EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)☃, ☃);
         }
      }

      ☃.shrink(1);
      return ☃;
   }

   protected void onFoodEaten(ItemStack var1, World var2, EntityPlayer var3) {
      if (!☃.isRemote && this.potionId != null && ☃.rand.nextFloat() < this.potionEffectProbability) {
         ☃.addPotionEffect(new PotionEffect(this.potionId));
      }
   }

   @Override
   public int getMaxItemUseDuration(ItemStack var1) {
      return 32;
   }

   @Override
   public EnumAction getItemUseAction(ItemStack var1) {
      return EnumAction.EAT;
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.canEat(this.alwaysEdible)) {
         ☃.setActiveHand(☃);
         return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
      } else {
         return new ActionResult<>(EnumActionResult.FAIL, ☃);
      }
   }

   public int getHealAmount(ItemStack var1) {
      return this.healAmount;
   }

   public float getSaturationModifier(ItemStack var1) {
      return this.saturationModifier;
   }

   public boolean isWolfsFavoriteMeat() {
      return this.isWolfsFavoriteMeat;
   }

   public ItemFood setPotionEffect(PotionEffect var1, float var2) {
      this.potionId = ☃;
      this.potionEffectProbability = ☃;
      return this;
   }

   public ItemFood setAlwaysEdible() {
      this.alwaysEdible = true;
      return this;
   }
}
