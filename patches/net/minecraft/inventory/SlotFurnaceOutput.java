package net.minecraft.inventory;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.MathHelper;

public class SlotFurnaceOutput extends Slot {
   private final EntityPlayer player;
   private int removeCount;

   public SlotFurnaceOutput(EntityPlayer var1, IInventory var2, int var3, int var4, int var5) {
      super(☃, ☃, ☃, ☃);
      this.player = ☃;
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
   public ItemStack onTake(EntityPlayer var1, ItemStack var2) {
      this.onCrafting(☃);
      super.onTake(☃, ☃);
      return ☃;
   }

   @Override
   protected void onCrafting(ItemStack var1, int var2) {
      this.removeCount += ☃;
      this.onCrafting(☃);
   }

   @Override
   protected void onCrafting(ItemStack var1) {
      ☃.onCrafting(this.player.world, this.player, this.removeCount);
      if (!this.player.world.isRemote) {
         int ☃ = this.removeCount;
         float ☃x = FurnaceRecipes.instance().getSmeltingExperience(☃);
         if (☃x == 0.0F) {
            ☃ = 0;
         } else if (☃x < 1.0F) {
            int ☃xx = MathHelper.floor(☃ * ☃x);
            if (☃xx < MathHelper.ceil(☃ * ☃x) && Math.random() < ☃ * ☃x - ☃xx) {
               ☃xx++;
            }

            ☃ = ☃xx;
         }

         while (☃ > 0) {
            int ☃xx = EntityXPOrb.getXPSplit(☃);
            ☃ -= ☃xx;
            this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5, this.player.posZ + 0.5, ☃xx));
         }
      }

      this.removeCount = 0;
   }
}
