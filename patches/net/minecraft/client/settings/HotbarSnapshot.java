package net.minecraft.client.settings;

import java.util.ArrayList;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class HotbarSnapshot extends ArrayList<ItemStack> {
   public static final int HOTBAR_SIZE = InventoryPlayer.getHotbarSize();

   public HotbarSnapshot() {
      this.ensureCapacity(HOTBAR_SIZE);

      for (int ☃ = 0; ☃ < HOTBAR_SIZE; ☃++) {
         this.add(ItemStack.EMPTY);
      }
   }

   public NBTTagList createTag() {
      NBTTagList ☃ = new NBTTagList();

      for (int ☃x = 0; ☃x < HOTBAR_SIZE; ☃x++) {
         ☃.appendTag(this.get(☃x).writeToNBT(new NBTTagCompound()));
      }

      return ☃;
   }

   public void fromTag(NBTTagList var1) {
      for (int ☃ = 0; ☃ < HOTBAR_SIZE; ☃++) {
         this.set(☃, new ItemStack(☃.getCompoundTagAt(☃)));
      }
   }

   @Override
   public boolean isEmpty() {
      for (int ☃ = 0; ☃ < HOTBAR_SIZE; ☃++) {
         if (!this.get(☃).isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
