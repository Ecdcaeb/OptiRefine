package net.minecraft.inventory;

import javax.annotation.Nullable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ContainerPlayer extends Container {
   private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]{
      EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET
   };
   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
   public InventoryCraftResult craftResult = new InventoryCraftResult();
   public boolean isLocalWorld;
   private final EntityPlayer player;

   public ContainerPlayer(InventoryPlayer var1, boolean var2, EntityPlayer var3) {
      this.isLocalWorld = ☃;
      this.player = ☃;
      this.addSlotToContainer(new SlotCrafting(☃.player, this.craftMatrix, this.craftResult, 0, 154, 28));

      for (int ☃ = 0; ☃ < 2; ☃++) {
         for (int ☃x = 0; ☃x < 2; ☃x++) {
            this.addSlotToContainer(new Slot(this.craftMatrix, ☃x + ☃ * 2, 98 + ☃x * 18, 18 + ☃ * 18));
         }
      }

      for (int ☃ = 0; ☃ < 4; ☃++) {
         final EntityEquipmentSlot ☃x = VALID_EQUIPMENT_SLOTS[☃];
         this.addSlotToContainer(new Slot(☃, 36 + (3 - ☃), 8, 8 + ☃ * 18) {
            @Override
            public int getSlotStackLimit() {
               return 1;
            }

            @Override
            public boolean isItemValid(ItemStack var1) {
               return ☃ == EntityLiving.getSlotForItemStack(☃);
            }

            @Override
            public boolean canTakeStack(EntityPlayer var1) {
               ItemStack ☃xx = this.getStack();
               return !☃xx.isEmpty() && !☃.isCreative() && EnchantmentHelper.hasBindingCurse(☃xx) ? false : super.canTakeStack(☃);
            }

            @Nullable
            @Override
            public String getSlotTexture() {
               return ItemArmor.EMPTY_SLOT_NAMES[☃.getIndex()];
            }
         });
      }

      for (int ☃ = 0; ☃ < 3; ☃++) {
         for (int ☃x = 0; ☃x < 9; ☃x++) {
            this.addSlotToContainer(new Slot(☃, ☃x + (☃ + 1) * 9, 8 + ☃x * 18, 84 + ☃ * 18));
         }
      }

      for (int ☃ = 0; ☃ < 9; ☃++) {
         this.addSlotToContainer(new Slot(☃, ☃, 8 + ☃ * 18, 142));
      }

      this.addSlotToContainer(new Slot(☃, 40, 77, 62) {
         @Nullable
         @Override
         public String getSlotTexture() {
            return "minecraft:items/empty_armor_slot_shield";
         }
      });
   }

   @Override
   public void onCraftMatrixChanged(IInventory var1) {
      this.slotChangedCraftingGrid(this.player.world, this.player, this.craftMatrix, this.craftResult);
   }

   @Override
   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(☃);
      this.craftResult.clear();
      if (!☃.world.isRemote) {
         this.clearContainer(☃, ☃.world, this.craftMatrix);
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return true;
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         EntityEquipmentSlot ☃xxx = EntityLiving.getSlotForItemStack(☃);
         if (☃ == 0) {
            if (!this.mergeItemStack(☃xx, 9, 45, true)) {
               return ItemStack.EMPTY;
            }

            ☃x.onSlotChange(☃xx, ☃);
         } else if (☃ >= 1 && ☃ < 5) {
            if (!this.mergeItemStack(☃xx, 9, 45, false)) {
               return ItemStack.EMPTY;
            }
         } else if (☃ >= 5 && ☃ < 9) {
            if (!this.mergeItemStack(☃xx, 9, 45, false)) {
               return ItemStack.EMPTY;
            }
         } else if (☃xxx.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !this.inventorySlots.get(8 - ☃xxx.getIndex()).getHasStack()) {
            int ☃xxxx = 8 - ☃xxx.getIndex();
            if (!this.mergeItemStack(☃xx, ☃xxxx, ☃xxxx + 1, false)) {
               return ItemStack.EMPTY;
            }
         } else if (☃xxx == EntityEquipmentSlot.OFFHAND && !this.inventorySlots.get(45).getHasStack()) {
            if (!this.mergeItemStack(☃xx, 45, 46, false)) {
               return ItemStack.EMPTY;
            }
         } else if (☃ >= 9 && ☃ < 36) {
            if (!this.mergeItemStack(☃xx, 36, 45, false)) {
               return ItemStack.EMPTY;
            }
         } else if (☃ >= 36 && ☃ < 45) {
            if (!this.mergeItemStack(☃xx, 9, 36, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(☃xx, 9, 45, false)) {
            return ItemStack.EMPTY;
         }

         if (☃xx.isEmpty()) {
            ☃x.putStack(ItemStack.EMPTY);
         } else {
            ☃x.onSlotChanged();
         }

         if (☃xx.getCount() == ☃.getCount()) {
            return ItemStack.EMPTY;
         }

         ItemStack ☃xxxx = ☃x.onTake(☃, ☃xx);
         if (☃ == 0) {
            ☃.dropItem(☃xxxx, false);
         }
      }

      return ☃;
   }

   @Override
   public boolean canMergeSlot(ItemStack var1, Slot var2) {
      return ☃.inventory != this.craftResult && super.canMergeSlot(☃, ☃);
   }
}
