package net.minecraft.item.crafting;

import javax.annotation.Nullable;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipesBanners {
   public static class RecipeAddPattern implements IRecipe {
      @Override
      public boolean matches(InventoryCrafting var1, World var2) {
         boolean ☃ = false;

         for (int ☃x = 0; ☃x < ☃.getSizeInventory(); ☃x++) {
            ItemStack ☃xx = ☃.getStackInSlot(☃x);
            if (☃xx.getItem() == Items.BANNER) {
               if (☃) {
                  return false;
               }

               if (TileEntityBanner.getPatterns(☃xx) >= 6) {
                  return false;
               }

               ☃ = true;
            }
         }

         return !☃ ? false : this.matchPatterns(☃) != null;
      }

      @Override
      public ItemStack getCraftingResult(InventoryCrafting var1) {
         ItemStack ☃ = ItemStack.EMPTY;

         for (int ☃x = 0; ☃x < ☃.getSizeInventory(); ☃x++) {
            ItemStack ☃xx = ☃.getStackInSlot(☃x);
            if (!☃xx.isEmpty() && ☃xx.getItem() == Items.BANNER) {
               ☃ = ☃xx.copy();
               ☃.setCount(1);
               break;
            }
         }

         BannerPattern ☃xx = this.matchPatterns(☃);
         if (☃xx != null) {
            int ☃xxx = 0;

            for (int ☃xxxx = 0; ☃xxxx < ☃.getSizeInventory(); ☃xxxx++) {
               ItemStack ☃xxxxx = ☃.getStackInSlot(☃xxxx);
               if (☃xxxxx.getItem() == Items.DYE) {
                  ☃xxx = ☃xxxxx.getMetadata();
                  break;
               }
            }

            NBTTagCompound ☃xxxxx = ☃.getOrCreateSubCompound("BlockEntityTag");
            NBTTagList ☃xxxxxx;
            if (☃xxxxx.hasKey("Patterns", 9)) {
               ☃xxxxxx = ☃xxxxx.getTagList("Patterns", 10);
            } else {
               ☃xxxxxx = new NBTTagList();
               ☃xxxxx.setTag("Patterns", ☃xxxxxx);
            }

            NBTTagCompound ☃xxxxxxx = new NBTTagCompound();
            ☃xxxxxxx.setString("Pattern", ☃xx.getHashname());
            ☃xxxxxxx.setInteger("Color", ☃xxx);
            ☃xxxxxx.appendTag(☃xxxxxxx);
         }

         return ☃;
      }

      @Override
      public ItemStack getRecipeOutput() {
         return ItemStack.EMPTY;
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

      @Nullable
      private BannerPattern matchPatterns(InventoryCrafting var1) {
         for (BannerPattern ☃ : BannerPattern.values()) {
            if (☃.hasPattern()) {
               boolean ☃x = true;
               if (☃.hasPatternItem()) {
                  boolean ☃xx = false;
                  boolean ☃xxx = false;

                  for (int ☃xxxx = 0; ☃xxxx < ☃.getSizeInventory() && ☃x; ☃xxxx++) {
                     ItemStack ☃xxxxx = ☃.getStackInSlot(☃xxxx);
                     if (!☃xxxxx.isEmpty() && ☃xxxxx.getItem() != Items.BANNER) {
                        if (☃xxxxx.getItem() == Items.DYE) {
                           if (☃xxx) {
                              ☃x = false;
                              break;
                           }

                           ☃xxx = true;
                        } else {
                           if (☃xx || !☃xxxxx.isItemEqual(☃.getPatternItem())) {
                              ☃x = false;
                              break;
                           }

                           ☃xx = true;
                        }
                     }
                  }

                  if (!☃xx || !☃xxx) {
                     ☃x = false;
                  }
               } else if (☃.getSizeInventory() == ☃.getPatterns().length * ☃.getPatterns()[0].length()) {
                  int ☃xx = -1;

                  for (int ☃xxx = 0; ☃xxx < ☃.getSizeInventory() && ☃x; ☃xxx++) {
                     int ☃xxxxx = ☃xxx / 3;
                     int ☃xxxxxx = ☃xxx % 3;
                     ItemStack ☃xxxxxxx = ☃.getStackInSlot(☃xxx);
                     if (!☃xxxxxxx.isEmpty() && ☃xxxxxxx.getItem() != Items.BANNER) {
                        if (☃xxxxxxx.getItem() != Items.DYE) {
                           ☃x = false;
                           break;
                        }

                        if (☃xx != -1 && ☃xx != ☃xxxxxxx.getMetadata()) {
                           ☃x = false;
                           break;
                        }

                        if (☃.getPatterns()[☃xxxxx].charAt(☃xxxxxx) == ' ') {
                           ☃x = false;
                           break;
                        }

                        ☃xx = ☃xxxxxxx.getMetadata();
                     } else if (☃.getPatterns()[☃xxxxx].charAt(☃xxxxxx) != ' ') {
                        ☃x = false;
                        break;
                     }
                  }
               } else {
                  ☃x = false;
               }

               if (☃x) {
                  return ☃;
               }
            }
         }

         return null;
      }

      @Override
      public boolean isDynamic() {
         return true;
      }

      @Override
      public boolean canFit(int var1, int var2) {
         return ☃ >= 3 && ☃ >= 3;
      }
   }

   public static class RecipeDuplicatePattern implements IRecipe {
      @Override
      public boolean matches(InventoryCrafting var1, World var2) {
         ItemStack ☃ = ItemStack.EMPTY;
         ItemStack ☃x = ItemStack.EMPTY;

         for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
            ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
            if (!☃xxx.isEmpty()) {
               if (☃xxx.getItem() != Items.BANNER) {
                  return false;
               }

               if (!☃.isEmpty() && !☃x.isEmpty()) {
                  return false;
               }

               EnumDyeColor ☃xxxx = ItemBanner.getBaseColor(☃xxx);
               boolean ☃xxxxx = TileEntityBanner.getPatterns(☃xxx) > 0;
               if (!☃.isEmpty()) {
                  if (☃xxxxx) {
                     return false;
                  }

                  if (☃xxxx != ItemBanner.getBaseColor(☃)) {
                     return false;
                  }

                  ☃x = ☃xxx;
               } else if (!☃x.isEmpty()) {
                  if (!☃xxxxx) {
                     return false;
                  }

                  if (☃xxxx != ItemBanner.getBaseColor(☃x)) {
                     return false;
                  }

                  ☃ = ☃xxx;
               } else if (☃xxxxx) {
                  ☃ = ☃xxx;
               } else {
                  ☃x = ☃xxx;
               }
            }
         }

         return !☃.isEmpty() && !☃x.isEmpty();
      }

      @Override
      public ItemStack getCraftingResult(InventoryCrafting var1) {
         for (int ☃ = 0; ☃ < ☃.getSizeInventory(); ☃++) {
            ItemStack ☃x = ☃.getStackInSlot(☃);
            if (!☃x.isEmpty() && TileEntityBanner.getPatterns(☃x) > 0) {
               ItemStack ☃xx = ☃x.copy();
               ☃xx.setCount(1);
               return ☃xx;
            }
         }

         return ItemStack.EMPTY;
      }

      @Override
      public ItemStack getRecipeOutput() {
         return ItemStack.EMPTY;
      }

      @Override
      public NonNullList<ItemStack> getRemainingItems(InventoryCrafting var1) {
         NonNullList<ItemStack> ☃ = NonNullList.withSize(☃.getSizeInventory(), ItemStack.EMPTY);

         for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
            ItemStack ☃xx = ☃.getStackInSlot(☃x);
            if (!☃xx.isEmpty()) {
               if (☃xx.getItem().hasContainerItem()) {
                  ☃.set(☃x, new ItemStack(☃xx.getItem().getContainerItem()));
               } else if (☃xx.hasTagCompound() && TileEntityBanner.getPatterns(☃xx) > 0) {
                  ItemStack ☃xxx = ☃xx.copy();
                  ☃xxx.setCount(1);
                  ☃.set(☃x, ☃xxx);
               }
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
         return ☃ * ☃ >= 2;
      }
   }
}
