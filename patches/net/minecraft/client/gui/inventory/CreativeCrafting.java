package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CreativeCrafting implements IContainerListener {
   private final Minecraft mc;

   public CreativeCrafting(Minecraft var1) {
      this.mc = ☃;
   }

   @Override
   public void sendAllContents(Container var1, NonNullList<ItemStack> var2) {
   }

   @Override
   public void sendSlotContents(Container var1, int var2, ItemStack var3) {
      this.mc.playerController.sendSlotPacket(☃, ☃);
   }

   @Override
   public void sendWindowProperty(Container var1, int var2, int var3) {
   }

   @Override
   public void sendAllWindowProperties(Container var1, IInventory var2) {
   }
}
