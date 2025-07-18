package net.minecraft.inventory;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;

public class SlotCrafting extends Slot {
   private final InventoryCrafting craftMatrix;
   private final EntityPlayer player;
   private int amountCrafted;

   public SlotCrafting(EntityPlayer var1, InventoryCrafting var2, IInventory var3, int var4, int var5, int var6) {
      super(☃, ☃, ☃, ☃);
      this.player = ☃;
      this.craftMatrix = ☃;
   }

   @Override
   public boolean isItemValid(ItemStack var1) {
      return false;
   }

   @Override
   public ItemStack decrStackSize(int var1) {
      if (this.getHasStack()) {
         this.amountCrafted = this.amountCrafted + Math.min(☃, this.getStack().getCount());
      }

      return super.decrStackSize(☃);
   }

   @Override
   protected void onCrafting(ItemStack var1, int var2) {
      this.amountCrafted += ☃;
      this.onCrafting(☃);
   }

   @Override
   protected void onSwapCraft(int var1) {
      this.amountCrafted += ☃;
   }

   @Override
   protected void onCrafting(ItemStack var1) {
      if (this.amountCrafted > 0) {
         ☃.onCrafting(this.player.world, this.player, this.amountCrafted);
      }

      this.amountCrafted = 0;
      InventoryCraftResult ☃ = (InventoryCraftResult)this.inventory;
      IRecipe ☃x = ☃.getRecipeUsed();
      if (☃x != null && !☃x.isDynamic()) {
         this.player.unlockRecipes(Lists.newArrayList(new IRecipe[]{☃x}));
         ☃.setRecipeUsed(null);
      }
   }

   @Override
   public ItemStack onTake(EntityPlayer var1, ItemStack var2) {
      this.onCrafting(☃);
      NonNullList<ItemStack> ☃ = CraftingManager.getRemainingItems(this.craftMatrix, ☃.world);

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         ItemStack ☃xx = this.craftMatrix.getStackInSlot(☃x);
         ItemStack ☃xxx = ☃.get(☃x);
         if (!☃xx.isEmpty()) {
            this.craftMatrix.decrStackSize(☃x, 1);
            ☃xx = this.craftMatrix.getStackInSlot(☃x);
         }

         if (!☃xxx.isEmpty()) {
            if (☃xx.isEmpty()) {
               this.craftMatrix.setInventorySlotContents(☃x, ☃xxx);
            } else if (ItemStack.areItemsEqual(☃xx, ☃xxx) && ItemStack.areItemStackTagsEqual(☃xx, ☃xxx)) {
               ☃xxx.grow(☃xx.getCount());
               this.craftMatrix.setInventorySlotContents(☃x, ☃xxx);
            } else if (!this.player.inventory.addItemStackToInventory(☃xxx)) {
               this.player.dropItem(☃xxx, false);
            }
         }
      }

      return ☃;
   }
}
