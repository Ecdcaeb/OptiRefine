package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemChorusFruit extends ItemFood {
   public ItemChorusFruit(int var1, float var2) {
      super(☃, ☃, false);
   }

   @Override
   public ItemStack onItemUseFinish(ItemStack var1, World var2, EntityLivingBase var3) {
      ItemStack ☃ = super.onItemUseFinish(☃, ☃, ☃);
      if (!☃.isRemote) {
         double ☃x = ☃.posX;
         double ☃xx = ☃.posY;
         double ☃xxx = ☃.posZ;

         for (int ☃xxxx = 0; ☃xxxx < 16; ☃xxxx++) {
            double ☃xxxxx = ☃.posX + (☃.getRNG().nextDouble() - 0.5) * 16.0;
            double ☃xxxxxx = MathHelper.clamp(☃.posY + (☃.getRNG().nextInt(16) - 8), 0.0, (double)(☃.getActualHeight() - 1));
            double ☃xxxxxxx = ☃.posZ + (☃.getRNG().nextDouble() - 0.5) * 16.0;
            if (☃.isRiding()) {
               ☃.dismountRidingEntity();
            }

            if (☃.attemptTeleport(☃xxxxx, ☃xxxxxx, ☃xxxxxxx)) {
               ☃.playSound(null, ☃x, ☃xx, ☃xxx, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
               ☃.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
               break;
            }
         }

         if (☃ instanceof EntityPlayer) {
            ((EntityPlayer)☃).getCooldownTracker().setCooldown(this, 20);
         }
      }

      return ☃;
   }
}
