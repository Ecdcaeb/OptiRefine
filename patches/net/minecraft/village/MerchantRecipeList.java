package net.minecraft.village;

import java.io.IOException;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;

public class MerchantRecipeList extends ArrayList<MerchantRecipe> {
   public MerchantRecipeList() {
   }

   public MerchantRecipeList(NBTTagCompound var1) {
      this.readRecipiesFromTags(☃);
   }

   @Nullable
   public MerchantRecipe canRecipeBeUsed(ItemStack var1, ItemStack var2, int var3) {
      if (☃ > 0 && ☃ < this.size()) {
         MerchantRecipe ☃ = this.get(☃);
         return !this.areItemStacksExactlyEqual(☃, ☃.getItemToBuy())
               || (!☃.isEmpty() || ☃.hasSecondItemToBuy()) && (!☃.hasSecondItemToBuy() || !this.areItemStacksExactlyEqual(☃, ☃.getSecondItemToBuy()))
               || ☃.getCount() < ☃.getItemToBuy().getCount()
               || ☃.hasSecondItemToBuy() && ☃.getCount() < ☃.getSecondItemToBuy().getCount()
            ? null
            : ☃;
      } else {
         for (int ☃ = 0; ☃ < this.size(); ☃++) {
            MerchantRecipe ☃x = this.get(☃);
            if (this.areItemStacksExactlyEqual(☃, ☃x.getItemToBuy())
               && ☃.getCount() >= ☃x.getItemToBuy().getCount()
               && (
                  !☃x.hasSecondItemToBuy() && ☃.isEmpty()
                     || ☃x.hasSecondItemToBuy()
                        && this.areItemStacksExactlyEqual(☃, ☃x.getSecondItemToBuy())
                        && ☃.getCount() >= ☃x.getSecondItemToBuy().getCount()
               )) {
               return ☃x;
            }
         }

         return null;
      }
   }

   private boolean areItemStacksExactlyEqual(ItemStack var1, ItemStack var2) {
      return ItemStack.areItemsEqual(☃, ☃)
         && (!☃.hasTagCompound() || ☃.hasTagCompound() && NBTUtil.areNBTEquals(☃.getTagCompound(), ☃.getTagCompound(), false));
   }

   public void writeToBuf(PacketBuffer var1) {
      ☃.writeByte((byte)(this.size() & 0xFF));

      for (int ☃ = 0; ☃ < this.size(); ☃++) {
         MerchantRecipe ☃x = this.get(☃);
         ☃.writeItemStack(☃x.getItemToBuy());
         ☃.writeItemStack(☃x.getItemToSell());
         ItemStack ☃xx = ☃x.getSecondItemToBuy();
         ☃.writeBoolean(!☃xx.isEmpty());
         if (!☃xx.isEmpty()) {
            ☃.writeItemStack(☃xx);
         }

         ☃.writeBoolean(☃x.isRecipeDisabled());
         ☃.writeInt(☃x.getToolUses());
         ☃.writeInt(☃x.getMaxTradeUses());
      }
   }

   public static MerchantRecipeList readFromBuf(PacketBuffer var0) throws IOException {
      MerchantRecipeList ☃ = new MerchantRecipeList();
      int ☃x = ☃.readByte() & 255;

      for (int ☃xx = 0; ☃xx < ☃x; ☃xx++) {
         ItemStack ☃xxx = ☃.readItemStack();
         ItemStack ☃xxxx = ☃.readItemStack();
         ItemStack ☃xxxxx = ItemStack.EMPTY;
         if (☃.readBoolean()) {
            ☃xxxxx = ☃.readItemStack();
         }

         boolean ☃xxxxxx = ☃.readBoolean();
         int ☃xxxxxxx = ☃.readInt();
         int ☃xxxxxxxx = ☃.readInt();
         MerchantRecipe ☃xxxxxxxxx = new MerchantRecipe(☃xxx, ☃xxxxx, ☃xxxx, ☃xxxxxxx, ☃xxxxxxxx);
         if (☃xxxxxx) {
            ☃xxxxxxxxx.compensateToolUses();
         }

         ☃.add(☃xxxxxxxxx);
      }

      return ☃;
   }

   public void readRecipiesFromTags(NBTTagCompound var1) {
      NBTTagList ☃ = ☃.getTagList("Recipes", 10);

      for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
         NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
         this.add(new MerchantRecipe(☃xx));
      }
   }

   public NBTTagCompound getRecipiesAsTags() {
      NBTTagCompound ☃ = new NBTTagCompound();
      NBTTagList ☃x = new NBTTagList();

      for (int ☃xx = 0; ☃xx < this.size(); ☃xx++) {
         MerchantRecipe ☃xxx = this.get(☃xx);
         ☃x.appendTag(☃xxx.writeToTags());
      }

      ☃.setTag("Recipes", ☃x);
      return ☃;
   }
}
