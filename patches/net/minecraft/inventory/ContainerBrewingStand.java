package net.minecraft.inventory;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

public class ContainerBrewingStand extends Container {
   private final IInventory tileBrewingStand;
   private final Slot slot;
   private int prevBrewTime;
   private int prevFuel;

   public ContainerBrewingStand(InventoryPlayer var1, IInventory var2) {
      this.tileBrewingStand = ☃;
      this.addSlotToContainer(new ContainerBrewingStand.Potion(☃, 0, 56, 51));
      this.addSlotToContainer(new ContainerBrewingStand.Potion(☃, 1, 79, 58));
      this.addSlotToContainer(new ContainerBrewingStand.Potion(☃, 2, 102, 51));
      this.slot = this.addSlotToContainer(new ContainerBrewingStand.Ingredient(☃, 3, 79, 17));
      this.addSlotToContainer(new ContainerBrewingStand.Fuel(☃, 4, 17, 17));

      for (int ☃ = 0; ☃ < 3; ☃++) {
         for (int ☃x = 0; ☃x < 9; ☃x++) {
            this.addSlotToContainer(new Slot(☃, ☃x + ☃ * 9 + 9, 8 + ☃x * 18, 84 + ☃ * 18));
         }
      }

      for (int ☃ = 0; ☃ < 9; ☃++) {
         this.addSlotToContainer(new Slot(☃, ☃, 8 + ☃ * 18, 142));
      }
   }

   @Override
   public void addListener(IContainerListener var1) {
      super.addListener(☃);
      ☃.sendAllWindowProperties(this, this.tileBrewingStand);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (int ☃ = 0; ☃ < this.listeners.size(); ☃++) {
         IContainerListener ☃x = this.listeners.get(☃);
         if (this.prevBrewTime != this.tileBrewingStand.getField(0)) {
            ☃x.sendWindowProperty(this, 0, this.tileBrewingStand.getField(0));
         }

         if (this.prevFuel != this.tileBrewingStand.getField(1)) {
            ☃x.sendWindowProperty(this, 1, this.tileBrewingStand.getField(1));
         }
      }

      this.prevBrewTime = this.tileBrewingStand.getField(0);
      this.prevFuel = this.tileBrewingStand.getField(1);
   }

   @Override
   public void updateProgressBar(int var1, int var2) {
      this.tileBrewingStand.setField(☃, ☃);
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.tileBrewingStand.isUsableByPlayer(☃);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if ((☃ < 0 || ☃ > 2) && ☃ != 3 && ☃ != 4) {
            if (this.slot.isItemValid(☃xx)) {
               if (!this.mergeItemStack(☃xx, 3, 4, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (ContainerBrewingStand.Potion.canHoldPotion(☃) && ☃.getCount() == 1) {
               if (!this.mergeItemStack(☃xx, 0, 3, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (ContainerBrewingStand.Fuel.isValidBrewingFuel(☃)) {
               if (!this.mergeItemStack(☃xx, 4, 5, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (☃ >= 5 && ☃ < 32) {
               if (!this.mergeItemStack(☃xx, 32, 41, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (☃ >= 32 && ☃ < 41) {
               if (!this.mergeItemStack(☃xx, 5, 32, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (!this.mergeItemStack(☃xx, 5, 41, false)) {
               return ItemStack.EMPTY;
            }
         } else {
            if (!this.mergeItemStack(☃xx, 5, 41, true)) {
               return ItemStack.EMPTY;
            }

            ☃x.onSlotChange(☃xx, ☃);
         }

         if (☃xx.isEmpty()) {
            ☃x.putStack(ItemStack.EMPTY);
         } else {
            ☃x.onSlotChanged();
         }

         if (☃xx.getCount() == ☃.getCount()) {
            return ItemStack.EMPTY;
         }

         ☃x.onTake(☃, ☃xx);
      }

      return ☃;
   }

   static class Fuel extends Slot {
      public Fuel(IInventory var1, int var2, int var3, int var4) {
         super(☃, ☃, ☃, ☃);
      }

      @Override
      public boolean isItemValid(ItemStack var1) {
         return isValidBrewingFuel(☃);
      }

      public static boolean isValidBrewingFuel(ItemStack var0) {
         return ☃.getItem() == Items.BLAZE_POWDER;
      }

      @Override
      public int getSlotStackLimit() {
         return 64;
      }
   }

   static class Ingredient extends Slot {
      public Ingredient(IInventory var1, int var2, int var3, int var4) {
         super(☃, ☃, ☃, ☃);
      }

      @Override
      public boolean isItemValid(ItemStack var1) {
         return PotionHelper.isReagent(☃);
      }

      @Override
      public int getSlotStackLimit() {
         return 64;
      }
   }

   static class Potion extends Slot {
      public Potion(IInventory var1, int var2, int var3, int var4) {
         super(☃, ☃, ☃, ☃);
      }

      @Override
      public boolean isItemValid(ItemStack var1) {
         return canHoldPotion(☃);
      }

      @Override
      public int getSlotStackLimit() {
         return 1;
      }

      @Override
      public ItemStack onTake(EntityPlayer var1, ItemStack var2) {
         PotionType ☃ = PotionUtils.getPotionFromItem(☃);
         if (☃ instanceof EntityPlayerMP) {
            CriteriaTriggers.BREWED_POTION.trigger((EntityPlayerMP)☃, ☃);
         }

         super.onTake(☃, ☃);
         return ☃;
      }

      public static boolean canHoldPotion(ItemStack var0) {
         Item ☃ = ☃.getItem();
         return ☃ == Items.POTIONITEM || ☃ == Items.SPLASH_POTION || ☃ == Items.LINGERING_POTION || ☃ == Items.GLASS_BOTTLE;
      }
   }
}
