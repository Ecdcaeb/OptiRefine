package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemBow extends Item {
   public ItemBow() {
      this.maxStackSize = 1;
      this.setMaxDamage(384);
      this.setCreativeTab(CreativeTabs.COMBAT);
      this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
         @Override
         public float apply(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
            if (☃ == null) {
               return 0.0F;
            } else {
               return ☃.getActiveItemStack().getItem() != Items.BOW ? 0.0F : (☃.getMaxItemUseDuration() - ☃.getItemInUseCount()) / 20.0F;
            }
         }
      });
      this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
         @Override
         public float apply(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
            return ☃ != null && ☃.isHandActive() && ☃.getActiveItemStack() == ☃ ? 1.0F : 0.0F;
         }
      });
   }

   private ItemStack findAmmo(EntityPlayer var1) {
      if (this.isArrow(☃.getHeldItem(EnumHand.OFF_HAND))) {
         return ☃.getHeldItem(EnumHand.OFF_HAND);
      } else if (this.isArrow(☃.getHeldItem(EnumHand.MAIN_HAND))) {
         return ☃.getHeldItem(EnumHand.MAIN_HAND);
      } else {
         for (int ☃ = 0; ☃ < ☃.inventory.getSizeInventory(); ☃++) {
            ItemStack ☃x = ☃.inventory.getStackInSlot(☃);
            if (this.isArrow(☃x)) {
               return ☃x;
            }
         }

         return ItemStack.EMPTY;
      }
   }

   protected boolean isArrow(ItemStack var1) {
      return ☃.getItem() instanceof ItemArrow;
   }

   @Override
   public void onPlayerStoppedUsing(ItemStack var1, World var2, EntityLivingBase var3, int var4) {
      if (☃ instanceof EntityPlayer) {
         EntityPlayer ☃ = (EntityPlayer)☃;
         boolean ☃x = ☃.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, ☃) > 0;
         ItemStack ☃xx = this.findAmmo(☃);
         if (!☃xx.isEmpty() || ☃x) {
            if (☃xx.isEmpty()) {
               ☃xx = new ItemStack(Items.ARROW);
            }

            int ☃xxx = this.getMaxItemUseDuration(☃) - ☃;
            float ☃xxxx = getArrowVelocity(☃xxx);
            if (!(☃xxxx < 0.1)) {
               boolean ☃xxxxx = ☃x && ☃xx.getItem() == Items.ARROW;
               if (!☃.isRemote) {
                  ItemArrow ☃xxxxxx = (ItemArrow)(☃xx.getItem() instanceof ItemArrow ? ☃xx.getItem() : Items.ARROW);
                  EntityArrow ☃xxxxxxx = ☃xxxxxx.createArrow(☃, ☃xx, ☃);
                  ☃xxxxxxx.shoot(☃, ☃.rotationPitch, ☃.rotationYaw, 0.0F, ☃xxxx * 3.0F, 1.0F);
                  if (☃xxxx == 1.0F) {
                     ☃xxxxxxx.setIsCritical(true);
                  }

                  int ☃xxxxxxxx = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, ☃);
                  if (☃xxxxxxxx > 0) {
                     ☃xxxxxxx.setDamage(☃xxxxxxx.getDamage() + ☃xxxxxxxx * 0.5 + 0.5);
                  }

                  int ☃xxxxxxxxx = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, ☃);
                  if (☃xxxxxxxxx > 0) {
                     ☃xxxxxxx.setKnockbackStrength(☃xxxxxxxxx);
                  }

                  if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, ☃) > 0) {
                     ☃xxxxxxx.setFire(100);
                  }

                  ☃.damageItem(1, ☃);
                  if (☃xxxxx || ☃.capabilities.isCreativeMode && (☃xx.getItem() == Items.SPECTRAL_ARROW || ☃xx.getItem() == Items.TIPPED_ARROW)) {
                     ☃xxxxxxx.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                  }

                  ☃.spawnEntity(☃xxxxxxx);
               }

               ☃.playSound(
                  null,
                  ☃.posX,
                  ☃.posY,
                  ☃.posZ,
                  SoundEvents.ENTITY_ARROW_SHOOT,
                  SoundCategory.PLAYERS,
                  1.0F,
                  1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + ☃xxxx * 0.5F
               );
               if (!☃xxxxx && !☃.capabilities.isCreativeMode) {
                  ☃xx.shrink(1);
                  if (☃xx.isEmpty()) {
                     ☃.inventory.deleteStack(☃xx);
                  }
               }

               ☃.addStat(StatList.getObjectUseStats(this));
            }
         }
      }
   }

   public static float getArrowVelocity(int var0) {
      float ☃ = ☃ / 20.0F;
      ☃ = (☃ * ☃ + ☃ * 2.0F) / 3.0F;
      if (☃ > 1.0F) {
         ☃ = 1.0F;
      }

      return ☃;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack var1) {
      return 72000;
   }

   @Override
   public EnumAction getItemUseAction(ItemStack var1) {
      return EnumAction.BOW;
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      boolean ☃x = !this.findAmmo(☃).isEmpty();
      if (☃.capabilities.isCreativeMode || ☃x) {
         ☃.setActiveHand(☃);
         return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
      } else {
         return ☃x ? new ActionResult<>(EnumActionResult.PASS, ☃) : new ActionResult<>(EnumActionResult.FAIL, ☃);
      }
   }

   @Override
   public int getItemEnchantability() {
      return 1;
   }
}
