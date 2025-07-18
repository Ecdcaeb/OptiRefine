package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class NpcMerchant implements IMerchant {
   private final InventoryMerchant merchantInventory;
   private final EntityPlayer customer;
   private MerchantRecipeList recipeList;
   private final ITextComponent name;

   public NpcMerchant(EntityPlayer var1, ITextComponent var2) {
      this.customer = ☃;
      this.name = ☃;
      this.merchantInventory = new InventoryMerchant(☃, this);
   }

   @Nullable
   @Override
   public EntityPlayer getCustomer() {
      return this.customer;
   }

   @Override
   public void setCustomer(@Nullable EntityPlayer var1) {
   }

   @Nullable
   @Override
   public MerchantRecipeList getRecipes(EntityPlayer var1) {
      return this.recipeList;
   }

   @Override
   public void setRecipes(@Nullable MerchantRecipeList var1) {
      this.recipeList = ☃;
   }

   @Override
   public void useRecipe(MerchantRecipe var1) {
      ☃.incrementToolUses();
   }

   @Override
   public void verifySellingItem(ItemStack var1) {
   }

   @Override
   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.name != null ? this.name : new TextComponentTranslation("entity.Villager.name"));
   }

   @Override
   public World getWorld() {
      return this.customer.world;
   }

   @Override
   public BlockPos getPos() {
      return new BlockPos(this.customer);
   }
}
