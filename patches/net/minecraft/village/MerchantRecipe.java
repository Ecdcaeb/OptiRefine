package net.minecraft.village;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MerchantRecipe {
   private ItemStack itemToBuy = ItemStack.EMPTY;
   private ItemStack secondItemToBuy = ItemStack.EMPTY;
   private ItemStack itemToSell = ItemStack.EMPTY;
   private int toolUses;
   private int maxTradeUses;
   private boolean rewardsExp;

   public MerchantRecipe(NBTTagCompound var1) {
      this.readFromTags(☃);
   }

   public MerchantRecipe(ItemStack var1, ItemStack var2, ItemStack var3) {
      this(☃, ☃, ☃, 0, 7);
   }

   public MerchantRecipe(ItemStack var1, ItemStack var2, ItemStack var3, int var4, int var5) {
      this.itemToBuy = ☃;
      this.secondItemToBuy = ☃;
      this.itemToSell = ☃;
      this.toolUses = ☃;
      this.maxTradeUses = ☃;
      this.rewardsExp = true;
   }

   public MerchantRecipe(ItemStack var1, ItemStack var2) {
      this(☃, ItemStack.EMPTY, ☃);
   }

   public MerchantRecipe(ItemStack var1, Item var2) {
      this(☃, new ItemStack(☃));
   }

   public ItemStack getItemToBuy() {
      return this.itemToBuy;
   }

   public ItemStack getSecondItemToBuy() {
      return this.secondItemToBuy;
   }

   public boolean hasSecondItemToBuy() {
      return !this.secondItemToBuy.isEmpty();
   }

   public ItemStack getItemToSell() {
      return this.itemToSell;
   }

   public int getToolUses() {
      return this.toolUses;
   }

   public int getMaxTradeUses() {
      return this.maxTradeUses;
   }

   public void incrementToolUses() {
      this.toolUses++;
   }

   public void increaseMaxTradeUses(int var1) {
      this.maxTradeUses += ☃;
   }

   public boolean isRecipeDisabled() {
      return this.toolUses >= this.maxTradeUses;
   }

   public void compensateToolUses() {
      this.toolUses = this.maxTradeUses;
   }

   public boolean getRewardsExp() {
      return this.rewardsExp;
   }

   public void readFromTags(NBTTagCompound var1) {
      NBTTagCompound ☃ = ☃.getCompoundTag("buy");
      this.itemToBuy = new ItemStack(☃);
      NBTTagCompound ☃x = ☃.getCompoundTag("sell");
      this.itemToSell = new ItemStack(☃x);
      if (☃.hasKey("buyB", 10)) {
         this.secondItemToBuy = new ItemStack(☃.getCompoundTag("buyB"));
      }

      if (☃.hasKey("uses", 99)) {
         this.toolUses = ☃.getInteger("uses");
      }

      if (☃.hasKey("maxUses", 99)) {
         this.maxTradeUses = ☃.getInteger("maxUses");
      } else {
         this.maxTradeUses = 7;
      }

      if (☃.hasKey("rewardExp", 1)) {
         this.rewardsExp = ☃.getBoolean("rewardExp");
      } else {
         this.rewardsExp = true;
      }
   }

   public NBTTagCompound writeToTags() {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setTag("buy", this.itemToBuy.writeToNBT(new NBTTagCompound()));
      ☃.setTag("sell", this.itemToSell.writeToNBT(new NBTTagCompound()));
      if (!this.secondItemToBuy.isEmpty()) {
         ☃.setTag("buyB", this.secondItemToBuy.writeToNBT(new NBTTagCompound()));
      }

      ☃.setInteger("uses", this.toolUses);
      ☃.setInteger("maxUses", this.maxTradeUses);
      ☃.setBoolean("rewardExp", this.rewardsExp);
      return ☃;
   }
}
