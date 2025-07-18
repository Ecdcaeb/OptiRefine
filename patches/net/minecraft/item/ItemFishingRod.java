package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFishingRod extends Item {
   public ItemFishingRod() {
      this.setMaxDamage(64);
      this.setMaxStackSize(1);
      this.setCreativeTab(CreativeTabs.TOOLS);
      this.addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter() {
         @Override
         public float apply(ItemStack var1, @Nullable World var2, @Nullable EntityLivingBase var3) {
            if (☃ == null) {
               return 0.0F;
            } else {
               boolean ☃ = ☃.getHeldItemMainhand() == ☃;
               boolean ☃x = ☃.getHeldItemOffhand() == ☃;
               if (☃.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
                  ☃x = false;
               }

               return (☃ || ☃x) && ☃ instanceof EntityPlayer && ((EntityPlayer)☃).fishEntity != null ? 1.0F : 0.0F;
            }
         }
      });
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
      if (☃.fishEntity != null) {
         int ☃x = ☃.fishEntity.handleHookRetraction();
         ☃.damageItem(☃x, ☃);
         ☃.swingArm(☃);
         ☃.playSound(null, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
      } else {
         ☃.playSound(null, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
         if (!☃.isRemote) {
            EntityFishHook ☃x = new EntityFishHook(☃, ☃);
            int ☃xx = EnchantmentHelper.getFishingSpeedBonus(☃);
            if (☃xx > 0) {
               ☃x.setLureSpeed(☃xx);
            }

            int ☃xxx = EnchantmentHelper.getFishingLuckBonus(☃);
            if (☃xxx > 0) {
               ☃x.setLuck(☃xxx);
            }

            ☃.spawnEntity(☃x);
         }

         ☃.swingArm(☃);
         ☃.addStat(StatList.getObjectUseStats(this));
      }

      return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
   }

   @Override
   public int getItemEnchantability() {
      return 1;
   }
}
