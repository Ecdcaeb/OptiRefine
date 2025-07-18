package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;

public class SlotMerchantResult extends Slot {
   private final InventoryMerchant merchantInventory;
   private final EntityPlayer player;
   private int removeCount;
   private final IMerchant merchant;

   public SlotMerchantResult(EntityPlayer var1, IMerchant var2, InventoryMerchant var3, int var4, int var5, int var6) {
      super(☃, ☃, ☃, ☃);
      this.player = ☃;
      this.merchant = ☃;
      this.merchantInventory = ☃;
   }

   @Override
   public boolean isItemValid(ItemStack var1) {
      return false;
   }

   @Override
   public ItemStack decrStackSize(int var1) {
      if (this.getHasStack()) {
         this.removeCount = this.removeCount + Math.min(☃, this.getStack().getCount());
      }

      return super.decrStackSize(☃);
   }

   @Override
   protected void onCrafting(ItemStack var1, int var2) {
      this.removeCount += ☃;
      this.onCrafting(☃);
   }

   @Override
   protected void onCrafting(ItemStack var1) {
      ☃.onCrafting(this.player.world, this.player, this.removeCount);
      this.removeCount = 0;
   }

   @Override
   public ItemStack onTake(EntityPlayer var1, ItemStack var2) {
      this.onCrafting(☃);
      MerchantRecipe ☃ = this.merchantInventory.getCurrentRecipe();
      if (☃ != null) {
         ItemStack ☃x = this.merchantInventory.getStackInSlot(0);
         ItemStack ☃xx = this.merchantInventory.getStackInSlot(1);
         if (this.doTrade(☃, ☃x, ☃xx) || this.doTrade(☃, ☃xx, ☃x)) {
            this.merchant.useRecipe(☃);
            ☃.addStat(StatList.TRADED_WITH_VILLAGER);
            this.merchantInventory.setInventorySlotContents(0, ☃x);
            this.merchantInventory.setInventorySlotContents(1, ☃xx);
         }
      }

      return ☃;
   }

   private boolean doTrade(MerchantRecipe var1, ItemStack var2, ItemStack var3) {
      ItemStack ☃ = ☃.getItemToBuy();
      ItemStack ☃x = ☃.getSecondItemToBuy();
      if (☃.getItem() == ☃.getItem() && ☃.getCount() >= ☃.getCount()) {
         if (!☃x.isEmpty() && !☃.isEmpty() && ☃x.getItem() == ☃.getItem() && ☃.getCount() >= ☃x.getCount()) {
            ☃.shrink(☃.getCount());
            ☃.shrink(☃x.getCount());
            return true;
         }

         if (☃x.isEmpty() && ☃.isEmpty()) {
            ☃.shrink(☃.getCount());
            return true;
         }
      }

      return false;
   }
}
