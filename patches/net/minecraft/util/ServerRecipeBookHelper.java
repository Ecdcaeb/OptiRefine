package net.minecraft.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerRecipeBookHelper {
   private final Logger field_194330_a = LogManager.getLogger();
   private final RecipeItemHelper field_194331_b = new RecipeItemHelper();
   private EntityPlayerMP field_194332_c;
   private IRecipe field_194333_d;
   private boolean field_194334_e;
   private InventoryCraftResult field_194335_f;
   private InventoryCrafting field_194336_g;
   private List<Slot> field_194337_h;

   public void func_194327_a(EntityPlayerMP var1, @Nullable IRecipe var2, boolean var3) {
      if (☃ != null && ☃.getRecipeBook().isUnlocked(☃)) {
         this.field_194332_c = ☃;
         this.field_194333_d = ☃;
         this.field_194334_e = ☃;
         this.field_194337_h = ☃.openContainer.inventorySlots;
         Container ☃ = ☃.openContainer;
         this.field_194335_f = null;
         this.field_194336_g = null;
         if (☃ instanceof ContainerWorkbench) {
            this.field_194335_f = ((ContainerWorkbench)☃).craftResult;
            this.field_194336_g = ((ContainerWorkbench)☃).craftMatrix;
         } else if (☃ instanceof ContainerPlayer) {
            this.field_194335_f = ((ContainerPlayer)☃).craftResult;
            this.field_194336_g = ((ContainerPlayer)☃).craftMatrix;
         }

         if (this.field_194335_f != null && this.field_194336_g != null) {
            if (this.func_194328_c() || ☃.isCreative()) {
               this.field_194331_b.clear();
               ☃.inventory.fillStackedContents(this.field_194331_b, false);
               this.field_194336_g.fillStackedContents(this.field_194331_b);
               if (this.field_194331_b.canCraft(☃, null)) {
                  this.func_194329_b();
               } else {
                  this.func_194326_a();
                  ☃.connection.sendPacket(new SPacketPlaceGhostRecipe(☃.openContainer.windowId, ☃));
               }

               ☃.inventory.markDirty();
            }
         }
      }
   }

   private void func_194326_a() {
      InventoryPlayer ☃ = this.field_194332_c.inventory;

      for (int ☃x = 0; ☃x < this.field_194336_g.getSizeInventory(); ☃x++) {
         ItemStack ☃xx = this.field_194336_g.getStackInSlot(☃x);
         if (!☃xx.isEmpty()) {
            while (☃xx.getCount() > 0) {
               int ☃xxx = ☃.storeItemStack(☃xx);
               if (☃xxx == -1) {
                  ☃xxx = ☃.getFirstEmptyStack();
               }

               ItemStack ☃xxxx = ☃xx.copy();
               ☃xxxx.setCount(1);
               ☃.add(☃xxx, ☃xxxx);
               this.field_194336_g.decrStackSize(☃x, 1);
            }
         }
      }

      this.field_194336_g.clear();
      this.field_194335_f.clear();
   }

   private void func_194329_b() {
      boolean ☃ = this.field_194333_d.matches(this.field_194336_g, this.field_194332_c.world);
      int ☃x = this.field_194331_b.getBiggestCraftableStack(this.field_194333_d, null);
      if (☃) {
         boolean ☃xx = true;

         for (int ☃xxx = 0; ☃xxx < this.field_194336_g.getSizeInventory(); ☃xxx++) {
            ItemStack ☃xxxx = this.field_194336_g.getStackInSlot(☃xxx);
            if (!☃xxxx.isEmpty() && Math.min(☃x, ☃xxxx.getMaxStackSize()) > ☃xxxx.getCount()) {
               ☃xx = false;
            }
         }

         if (☃xx) {
            return;
         }
      }

      int ☃xx = this.func_194324_a(☃x, ☃);
      IntList ☃xxxx = new IntArrayList();
      if (this.field_194331_b.canCraft(this.field_194333_d, ☃xxxx, ☃xx)) {
         int ☃xxxxx = ☃xx;
         IntListIterator var6 = ☃xxxx.iterator();

         while (var6.hasNext()) {
            int ☃xxxxxx = (Integer)var6.next();
            int ☃xxxxxxx = RecipeItemHelper.unpack(☃xxxxxx).getMaxStackSize();
            if (☃xxxxxxx < ☃xxxxx) {
               ☃xxxxx = ☃xxxxxxx;
            }
         }

         if (this.field_194331_b.canCraft(this.field_194333_d, ☃xxxx, ☃xxxxx)) {
            this.func_194326_a();
            this.func_194323_a(☃xxxxx, ☃xxxx);
         }
      }
   }

   private int func_194324_a(int var1, boolean var2) {
      int ☃ = 1;
      if (this.field_194334_e) {
         ☃ = ☃;
      } else if (☃) {
         ☃ = 64;

         for (int ☃x = 0; ☃x < this.field_194336_g.getSizeInventory(); ☃x++) {
            ItemStack ☃xx = this.field_194336_g.getStackInSlot(☃x);
            if (!☃xx.isEmpty() && ☃ > ☃xx.getCount()) {
               ☃ = ☃xx.getCount();
            }
         }

         if (☃ < 64) {
            ☃++;
         }
      }

      return ☃;
   }

   private void func_194323_a(int var1, IntList var2) {
      int ☃ = this.field_194336_g.getWidth();
      int ☃x = this.field_194336_g.getHeight();
      if (this.field_194333_d instanceof ShapedRecipes) {
         ShapedRecipes ☃xx = (ShapedRecipes)this.field_194333_d;
         ☃ = ☃xx.getWidth();
         ☃x = ☃xx.getHeight();
      }

      int ☃xx = 1;
      Iterator<Integer> ☃xxx = ☃.iterator();

      for (int ☃xxxx = 0; ☃xxxx < this.field_194336_g.getWidth() && ☃x != ☃xxxx; ☃xxxx++) {
         for (int ☃xxxxx = 0; ☃xxxxx < this.field_194336_g.getHeight(); ☃xxxxx++) {
            if (☃ == ☃xxxxx || !☃xxx.hasNext()) {
               ☃xx += this.field_194336_g.getWidth() - ☃xxxxx;
               break;
            }

            Slot ☃xxxxxx = this.field_194337_h.get(☃xx);
            ItemStack ☃xxxxxxx = RecipeItemHelper.unpack(☃xxx.next());
            if (☃xxxxxxx.isEmpty()) {
               ☃xx++;
            } else {
               for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃; ☃xxxxxxxx++) {
                  this.func_194325_a(☃xxxxxx, ☃xxxxxxx);
               }

               ☃xx++;
            }
         }

         if (!☃xxx.hasNext()) {
            break;
         }
      }
   }

   private void func_194325_a(Slot var1, ItemStack var2) {
      InventoryPlayer ☃ = this.field_194332_c.inventory;
      int ☃x = ☃.findSlotMatchingUnusedItem(☃);
      if (☃x != -1) {
         ItemStack ☃xx = ☃.getStackInSlot(☃x).copy();
         if (!☃xx.isEmpty()) {
            if (☃xx.getCount() > 1) {
               ☃.decrStackSize(☃x, 1);
            } else {
               ☃.removeStackFromSlot(☃x);
            }

            ☃xx.setCount(1);
            if (☃.getStack().isEmpty()) {
               ☃.putStack(☃xx);
            } else {
               ☃.getStack().grow(1);
            }
         }
      }
   }

   private boolean func_194328_c() {
      InventoryPlayer ☃ = this.field_194332_c.inventory;

      for (int ☃x = 0; ☃x < this.field_194336_g.getSizeInventory(); ☃x++) {
         ItemStack ☃xx = this.field_194336_g.getStackInSlot(☃x);
         if (!☃xx.isEmpty()) {
            int ☃xxx = ☃.storeItemStack(☃xx);
            if (☃xxx == -1) {
               ☃xxx = ☃.getFirstEmptyStack();
            }

            if (☃xxx == -1) {
               return false;
            }
         }
      }

      return true;
   }
}
