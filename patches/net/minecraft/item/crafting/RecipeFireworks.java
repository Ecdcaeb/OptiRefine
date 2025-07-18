package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeFireworks implements IRecipe {
   private ItemStack resultItem = ItemStack.EMPTY;

   @Override
   public boolean matches(InventoryCrafting var1, World var2) {
      this.resultItem = ItemStack.EMPTY;
      int ☃ = 0;
      int ☃x = 0;
      int ☃xx = 0;
      int ☃xxx = 0;
      int ☃xxxx = 0;
      int ☃xxxxx = 0;

      for (int ☃xxxxxx = 0; ☃xxxxxx < ☃.getSizeInventory(); ☃xxxxxx++) {
         ItemStack ☃xxxxxxx = ☃.getStackInSlot(☃xxxxxx);
         if (!☃xxxxxxx.isEmpty()) {
            if (☃xxxxxxx.getItem() == Items.GUNPOWDER) {
               ☃x++;
            } else if (☃xxxxxxx.getItem() == Items.FIREWORK_CHARGE) {
               ☃xxx++;
            } else if (☃xxxxxxx.getItem() == Items.DYE) {
               ☃xx++;
            } else if (☃xxxxxxx.getItem() == Items.PAPER) {
               ☃++;
            } else if (☃xxxxxxx.getItem() == Items.GLOWSTONE_DUST) {
               ☃xxxx++;
            } else if (☃xxxxxxx.getItem() == Items.DIAMOND) {
               ☃xxxx++;
            } else if (☃xxxxxxx.getItem() == Items.FIRE_CHARGE) {
               ☃xxxxx++;
            } else if (☃xxxxxxx.getItem() == Items.FEATHER) {
               ☃xxxxx++;
            } else if (☃xxxxxxx.getItem() == Items.GOLD_NUGGET) {
               ☃xxxxx++;
            } else {
               if (☃xxxxxxx.getItem() != Items.SKULL) {
                  return false;
               }

               ☃xxxxx++;
            }
         }
      }

      ☃xxxx += ☃xx + ☃xxxxx;
      if (☃x > 3 || ☃ > 1) {
         return false;
      } else if (☃x >= 1 && ☃ == 1 && ☃xxxx == 0) {
         this.resultItem = new ItemStack(Items.FIREWORKS, 3);
         NBTTagCompound ☃xxxxxxx = new NBTTagCompound();
         if (☃xxx > 0) {
            NBTTagList ☃xxxxxxxx = new NBTTagList();

            for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃.getSizeInventory(); ☃xxxxxxxxx++) {
               ItemStack ☃xxxxxxxxxx = ☃.getStackInSlot(☃xxxxxxxxx);
               if (☃xxxxxxxxxx.getItem() == Items.FIREWORK_CHARGE && ☃xxxxxxxxxx.hasTagCompound() && ☃xxxxxxxxxx.getTagCompound().hasKey("Explosion", 10)) {
                  ☃xxxxxxxx.appendTag(☃xxxxxxxxxx.getTagCompound().getCompoundTag("Explosion"));
               }
            }

            ☃xxxxxxx.setTag("Explosions", ☃xxxxxxxx);
         }

         ☃xxxxxxx.setByte("Flight", (byte)☃x);
         NBTTagCompound ☃xxxxxxxx = new NBTTagCompound();
         ☃xxxxxxxx.setTag("Fireworks", ☃xxxxxxx);
         this.resultItem.setTagCompound(☃xxxxxxxx);
         return true;
      } else if (☃x == 1 && ☃ == 0 && ☃xxx == 0 && ☃xx > 0 && ☃xxxxx <= 1) {
         this.resultItem = new ItemStack(Items.FIREWORK_CHARGE);
         NBTTagCompound ☃xxxxxxx = new NBTTagCompound();
         NBTTagCompound ☃xxxxxxxx = new NBTTagCompound();
         byte ☃xxxxxxxxxx = 0;
         List<Integer> ☃xxxxxxxxxxx = Lists.newArrayList();

         for (int ☃xxxxxxxxxxxx = 0; ☃xxxxxxxxxxxx < ☃.getSizeInventory(); ☃xxxxxxxxxxxx++) {
            ItemStack ☃xxxxxxxxxxxxx = ☃.getStackInSlot(☃xxxxxxxxxxxx);
            if (!☃xxxxxxxxxxxxx.isEmpty()) {
               if (☃xxxxxxxxxxxxx.getItem() == Items.DYE) {
                  ☃xxxxxxxxxxx.add(ItemDye.DYE_COLORS[☃xxxxxxxxxxxxx.getMetadata() & 15]);
               } else if (☃xxxxxxxxxxxxx.getItem() == Items.GLOWSTONE_DUST) {
                  ☃xxxxxxxx.setBoolean("Flicker", true);
               } else if (☃xxxxxxxxxxxxx.getItem() == Items.DIAMOND) {
                  ☃xxxxxxxx.setBoolean("Trail", true);
               } else if (☃xxxxxxxxxxxxx.getItem() == Items.FIRE_CHARGE) {
                  ☃xxxxxxxxxx = 1;
               } else if (☃xxxxxxxxxxxxx.getItem() == Items.FEATHER) {
                  ☃xxxxxxxxxx = 4;
               } else if (☃xxxxxxxxxxxxx.getItem() == Items.GOLD_NUGGET) {
                  ☃xxxxxxxxxx = 2;
               } else if (☃xxxxxxxxxxxxx.getItem() == Items.SKULL) {
                  ☃xxxxxxxxxx = 3;
               }
            }
         }

         int[] ☃xxxxxxxxxxxxx = new int[☃xxxxxxxxxxx.size()];

         for (int ☃xxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxx < ☃xxxxxxxxxxxxx.length; ☃xxxxxxxxxxxxxx++) {
            ☃xxxxxxxxxxxxx[☃xxxxxxxxxxxxxx] = ☃xxxxxxxxxxx.get(☃xxxxxxxxxxxxxx);
         }

         ☃xxxxxxxx.setIntArray("Colors", ☃xxxxxxxxxxxxx);
         ☃xxxxxxxx.setByte("Type", ☃xxxxxxxxxx);
         ☃xxxxxxx.setTag("Explosion", ☃xxxxxxxx);
         this.resultItem.setTagCompound(☃xxxxxxx);
         return true;
      } else if (☃x == 0 && ☃ == 0 && ☃xxx == 1 && ☃xx > 0 && ☃xx == ☃xxxx) {
         List<Integer> ☃xxxxxxx = Lists.newArrayList();

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃.getSizeInventory(); ☃xxxxxxxx++) {
            ItemStack ☃xxxxxxxxxx = ☃.getStackInSlot(☃xxxxxxxx);
            if (!☃xxxxxxxxxx.isEmpty()) {
               if (☃xxxxxxxxxx.getItem() == Items.DYE) {
                  ☃xxxxxxx.add(ItemDye.DYE_COLORS[☃xxxxxxxxxx.getMetadata() & 15]);
               } else if (☃xxxxxxxxxx.getItem() == Items.FIREWORK_CHARGE) {
                  this.resultItem = ☃xxxxxxxxxx.copy();
                  this.resultItem.setCount(1);
               }
            }
         }

         int[] ☃xxxxxxxxxx = new int[☃xxxxxxx.size()];

         for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < ☃xxxxxxxxxx.length; ☃xxxxxxxxxxx++) {
            ☃xxxxxxxxxx[☃xxxxxxxxxxx] = ☃xxxxxxx.get(☃xxxxxxxxxxx);
         }

         if (!this.resultItem.isEmpty() && this.resultItem.hasTagCompound()) {
            NBTTagCompound ☃xxxxxxxxxxx = this.resultItem.getTagCompound().getCompoundTag("Explosion");
            if (☃xxxxxxxxxxx == null) {
               return false;
            } else {
               ☃xxxxxxxxxxx.setIntArray("FadeColors", ☃xxxxxxxxxx);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting var1) {
      return this.resultItem.copy();
   }

   @Override
   public ItemStack getRecipeOutput() {
      return this.resultItem;
   }

   @Override
   public NonNullList<ItemStack> getRemainingItems(InventoryCrafting var1) {
      NonNullList<ItemStack> ☃ = NonNullList.withSize(☃.getSizeInventory(), ItemStack.EMPTY);

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         ItemStack ☃xx = ☃.getStackInSlot(☃x);
         if (☃xx.getItem().hasContainerItem()) {
            ☃.set(☃x, new ItemStack(☃xx.getItem().getContainerItem()));
         }
      }

      return ☃;
   }

   @Override
   public boolean isDynamic() {
      return true;
   }

   @Override
   public boolean canFit(int var1, int var2) {
      return ☃ * ☃ >= 1;
   }
}
