package net.minecraft.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class Container {
   public NonNullList<ItemStack> inventoryItemStacks = NonNullList.create();
   public List<Slot> inventorySlots = Lists.newArrayList();
   public int windowId;
   private short transactionID;
   private int dragMode = -1;
   private int dragEvent;
   private final Set<Slot> dragSlots = Sets.newHashSet();
   protected List<IContainerListener> listeners = Lists.newArrayList();
   private final Set<EntityPlayer> playerList = Sets.newHashSet();

   protected Slot addSlotToContainer(Slot var1) {
      ☃.slotNumber = this.inventorySlots.size();
      this.inventorySlots.add(☃);
      this.inventoryItemStacks.add(ItemStack.EMPTY);
      return ☃;
   }

   public void addListener(IContainerListener var1) {
      if (this.listeners.contains(☃)) {
         throw new IllegalArgumentException("Listener already listening");
      } else {
         this.listeners.add(☃);
         ☃.sendAllContents(this, this.getInventory());
         this.detectAndSendChanges();
      }
   }

   public void removeListener(IContainerListener var1) {
      this.listeners.remove(☃);
   }

   public NonNullList<ItemStack> getInventory() {
      NonNullList<ItemStack> ☃ = NonNullList.create();

      for (int ☃x = 0; ☃x < this.inventorySlots.size(); ☃x++) {
         ☃.add(this.inventorySlots.get(☃x).getStack());
      }

      return ☃;
   }

   public void detectAndSendChanges() {
      for (int ☃ = 0; ☃ < this.inventorySlots.size(); ☃++) {
         ItemStack ☃x = this.inventorySlots.get(☃).getStack();
         ItemStack ☃xx = this.inventoryItemStacks.get(☃);
         if (!ItemStack.areItemStacksEqual(☃xx, ☃x)) {
            ☃xx = ☃x.isEmpty() ? ItemStack.EMPTY : ☃x.copy();
            this.inventoryItemStacks.set(☃, ☃xx);

            for (int ☃xxx = 0; ☃xxx < this.listeners.size(); ☃xxx++) {
               this.listeners.get(☃xxx).sendSlotContents(this, ☃, ☃xx);
            }
         }
      }
   }

   public boolean enchantItem(EntityPlayer var1, int var2) {
      return false;
   }

   @Nullable
   public Slot getSlotFromInventory(IInventory var1, int var2) {
      for (int ☃ = 0; ☃ < this.inventorySlots.size(); ☃++) {
         Slot ☃x = this.inventorySlots.get(☃);
         if (☃x.isHere(☃, ☃)) {
            return ☃x;
         }
      }

      return null;
   }

   public Slot getSlot(int var1) {
      return this.inventorySlots.get(☃);
   }

   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      Slot ☃ = this.inventorySlots.get(☃);
      return ☃ != null ? ☃.getStack() : ItemStack.EMPTY;
   }

   public ItemStack slotClick(int var1, int var2, ClickType var3, EntityPlayer var4) {
      ItemStack ☃ = ItemStack.EMPTY;
      InventoryPlayer ☃x = ☃.inventory;
      if (☃ == ClickType.QUICK_CRAFT) {
         int ☃xx = this.dragEvent;
         this.dragEvent = getDragEvent(☃);
         if ((☃xx != 1 || this.dragEvent != 2) && ☃xx != this.dragEvent) {
            this.resetDrag();
         } else if (☃x.getItemStack().isEmpty()) {
            this.resetDrag();
         } else if (this.dragEvent == 0) {
            this.dragMode = extractDragMode(☃);
            if (isValidDragMode(this.dragMode, ☃)) {
               this.dragEvent = 1;
               this.dragSlots.clear();
            } else {
               this.resetDrag();
            }
         } else if (this.dragEvent == 1) {
            Slot ☃xxx = this.inventorySlots.get(☃);
            ItemStack ☃xxxx = ☃x.getItemStack();
            if (☃xxx != null
               && canAddItemToSlot(☃xxx, ☃xxxx, true)
               && ☃xxx.isItemValid(☃xxxx)
               && (this.dragMode == 2 || ☃xxxx.getCount() > this.dragSlots.size())
               && this.canDragIntoSlot(☃xxx)) {
               this.dragSlots.add(☃xxx);
            }
         } else if (this.dragEvent == 2) {
            if (!this.dragSlots.isEmpty()) {
               ItemStack ☃xxx = ☃x.getItemStack().copy();
               int ☃xxxx = ☃x.getItemStack().getCount();

               for (Slot ☃xxxxx : this.dragSlots) {
                  ItemStack ☃xxxxxx = ☃x.getItemStack();
                  if (☃xxxxx != null
                     && canAddItemToSlot(☃xxxxx, ☃xxxxxx, true)
                     && ☃xxxxx.isItemValid(☃xxxxxx)
                     && (this.dragMode == 2 || ☃xxxxxx.getCount() >= this.dragSlots.size())
                     && this.canDragIntoSlot(☃xxxxx)) {
                     ItemStack ☃xxxxxxx = ☃xxx.copy();
                     int ☃xxxxxxxx = ☃xxxxx.getHasStack() ? ☃xxxxx.getStack().getCount() : 0;
                     computeStackSize(this.dragSlots, this.dragMode, ☃xxxxxxx, ☃xxxxxxxx);
                     int ☃xxxxxxxxx = Math.min(☃xxxxxxx.getMaxStackSize(), ☃xxxxx.getItemStackLimit(☃xxxxxxx));
                     if (☃xxxxxxx.getCount() > ☃xxxxxxxxx) {
                        ☃xxxxxxx.setCount(☃xxxxxxxxx);
                     }

                     ☃xxxx -= ☃xxxxxxx.getCount() - ☃xxxxxxxx;
                     ☃xxxxx.putStack(☃xxxxxxx);
                  }
               }

               ☃xxx.setCount(☃xxxx);
               ☃x.setItemStack(☃xxx);
            }

            this.resetDrag();
         } else {
            this.resetDrag();
         }
      } else if (this.dragEvent != 0) {
         this.resetDrag();
      } else if ((☃ == ClickType.PICKUP || ☃ == ClickType.QUICK_MOVE) && (☃ == 0 || ☃ == 1)) {
         if (☃ == -999) {
            if (!☃x.getItemStack().isEmpty()) {
               if (☃ == 0) {
                  ☃.dropItem(☃x.getItemStack(), true);
                  ☃x.setItemStack(ItemStack.EMPTY);
               }

               if (☃ == 1) {
                  ☃.dropItem(☃x.getItemStack().splitStack(1), true);
               }
            }
         } else if (☃ == ClickType.QUICK_MOVE) {
            if (☃ < 0) {
               return ItemStack.EMPTY;
            }

            Slot ☃xx = this.inventorySlots.get(☃);
            if (☃xx == null || !☃xx.canTakeStack(☃)) {
               return ItemStack.EMPTY;
            }

            for (ItemStack ☃xxx = this.transferStackInSlot(☃, ☃);
               !☃xxx.isEmpty() && ItemStack.areItemsEqual(☃xx.getStack(), ☃xxx);
               ☃xxx = this.transferStackInSlot(☃, ☃)
            ) {
               ☃ = ☃xxx.copy();
            }
         } else {
            if (☃ < 0) {
               return ItemStack.EMPTY;
            }

            Slot ☃xx = this.inventorySlots.get(☃);
            if (☃xx != null) {
               ItemStack ☃xxx = ☃xx.getStack();
               ItemStack ☃xxxx = ☃x.getItemStack();
               if (!☃xxx.isEmpty()) {
                  ☃ = ☃xxx.copy();
               }

               if (☃xxx.isEmpty()) {
                  if (!☃xxxx.isEmpty() && ☃xx.isItemValid(☃xxxx)) {
                     int ☃xxxxxx = ☃ == 0 ? ☃xxxx.getCount() : 1;
                     if (☃xxxxxx > ☃xx.getItemStackLimit(☃xxxx)) {
                        ☃xxxxxx = ☃xx.getItemStackLimit(☃xxxx);
                     }

                     ☃xx.putStack(☃xxxx.splitStack(☃xxxxxx));
                  }
               } else if (☃xx.canTakeStack(☃)) {
                  if (☃xxxx.isEmpty()) {
                     if (☃xxx.isEmpty()) {
                        ☃xx.putStack(ItemStack.EMPTY);
                        ☃x.setItemStack(ItemStack.EMPTY);
                     } else {
                        int ☃xxxxxx = ☃ == 0 ? ☃xxx.getCount() : (☃xxx.getCount() + 1) / 2;
                        ☃x.setItemStack(☃xx.decrStackSize(☃xxxxxx));
                        if (☃xxx.isEmpty()) {
                           ☃xx.putStack(ItemStack.EMPTY);
                        }

                        ☃xx.onTake(☃, ☃x.getItemStack());
                     }
                  } else if (☃xx.isItemValid(☃xxxx)) {
                     if (☃xxx.getItem() == ☃xxxx.getItem() && ☃xxx.getMetadata() == ☃xxxx.getMetadata() && ItemStack.areItemStackTagsEqual(☃xxx, ☃xxxx)) {
                        int ☃xxxxxx = ☃ == 0 ? ☃xxxx.getCount() : 1;
                        if (☃xxxxxx > ☃xx.getItemStackLimit(☃xxxx) - ☃xxx.getCount()) {
                           ☃xxxxxx = ☃xx.getItemStackLimit(☃xxxx) - ☃xxx.getCount();
                        }

                        if (☃xxxxxx > ☃xxxx.getMaxStackSize() - ☃xxx.getCount()) {
                           ☃xxxxxx = ☃xxxx.getMaxStackSize() - ☃xxx.getCount();
                        }

                        ☃xxxx.shrink(☃xxxxxx);
                        ☃xxx.grow(☃xxxxxx);
                     } else if (☃xxxx.getCount() <= ☃xx.getItemStackLimit(☃xxxx)) {
                        ☃xx.putStack(☃xxxx);
                        ☃x.setItemStack(☃xxx);
                     }
                  } else if (☃xxx.getItem() == ☃xxxx.getItem()
                     && ☃xxxx.getMaxStackSize() > 1
                     && (!☃xxx.getHasSubtypes() || ☃xxx.getMetadata() == ☃xxxx.getMetadata())
                     && ItemStack.areItemStackTagsEqual(☃xxx, ☃xxxx)
                     && !☃xxx.isEmpty()) {
                     int ☃xxxxxxx = ☃xxx.getCount();
                     if (☃xxxxxxx + ☃xxxx.getCount() <= ☃xxxx.getMaxStackSize()) {
                        ☃xxxx.grow(☃xxxxxxx);
                        ☃xxx = ☃xx.decrStackSize(☃xxxxxxx);
                        if (☃xxx.isEmpty()) {
                           ☃xx.putStack(ItemStack.EMPTY);
                        }

                        ☃xx.onTake(☃, ☃x.getItemStack());
                     }
                  }
               }

               ☃xx.onSlotChanged();
            }
         }
      } else if (☃ == ClickType.SWAP && ☃ >= 0 && ☃ < 9) {
         Slot ☃xx = this.inventorySlots.get(☃);
         ItemStack ☃xxxxxxx = ☃x.getStackInSlot(☃);
         ItemStack ☃xxxxxxxx = ☃xx.getStack();
         if (!☃xxxxxxx.isEmpty() || !☃xxxxxxxx.isEmpty()) {
            if (☃xxxxxxx.isEmpty()) {
               if (☃xx.canTakeStack(☃)) {
                  ☃x.setInventorySlotContents(☃, ☃xxxxxxxx);
                  ☃xx.onSwapCraft(☃xxxxxxxx.getCount());
                  ☃xx.putStack(ItemStack.EMPTY);
                  ☃xx.onTake(☃, ☃xxxxxxxx);
               }
            } else if (☃xxxxxxxx.isEmpty()) {
               if (☃xx.isItemValid(☃xxxxxxx)) {
                  int ☃xxxxxxxxx = ☃xx.getItemStackLimit(☃xxxxxxx);
                  if (☃xxxxxxx.getCount() > ☃xxxxxxxxx) {
                     ☃xx.putStack(☃xxxxxxx.splitStack(☃xxxxxxxxx));
                  } else {
                     ☃xx.putStack(☃xxxxxxx);
                     ☃x.setInventorySlotContents(☃, ItemStack.EMPTY);
                  }
               }
            } else if (☃xx.canTakeStack(☃) && ☃xx.isItemValid(☃xxxxxxx)) {
               int ☃xxxxxxxxx = ☃xx.getItemStackLimit(☃xxxxxxx);
               if (☃xxxxxxx.getCount() > ☃xxxxxxxxx) {
                  ☃xx.putStack(☃xxxxxxx.splitStack(☃xxxxxxxxx));
                  ☃xx.onTake(☃, ☃xxxxxxxx);
                  if (!☃x.addItemStackToInventory(☃xxxxxxxx)) {
                     ☃.dropItem(☃xxxxxxxx, true);
                  }
               } else {
                  ☃xx.putStack(☃xxxxxxx);
                  ☃x.setInventorySlotContents(☃, ☃xxxxxxxx);
                  ☃xx.onTake(☃, ☃xxxxxxxx);
               }
            }
         }
      } else if (☃ == ClickType.CLONE && ☃.capabilities.isCreativeMode && ☃x.getItemStack().isEmpty() && ☃ >= 0) {
         Slot ☃xx = this.inventorySlots.get(☃);
         if (☃xx != null && ☃xx.getHasStack()) {
            ItemStack ☃xxxxxxx = ☃xx.getStack().copy();
            ☃xxxxxxx.setCount(☃xxxxxxx.getMaxStackSize());
            ☃x.setItemStack(☃xxxxxxx);
         }
      } else if (☃ == ClickType.THROW && ☃x.getItemStack().isEmpty() && ☃ >= 0) {
         Slot ☃xx = this.inventorySlots.get(☃);
         if (☃xx != null && ☃xx.getHasStack() && ☃xx.canTakeStack(☃)) {
            ItemStack ☃xxxxxxx = ☃xx.decrStackSize(☃ == 0 ? 1 : ☃xx.getStack().getCount());
            ☃xx.onTake(☃, ☃xxxxxxx);
            ☃.dropItem(☃xxxxxxx, true);
         }
      } else if (☃ == ClickType.PICKUP_ALL && ☃ >= 0) {
         Slot ☃xx = this.inventorySlots.get(☃);
         ItemStack ☃xxxxxxx = ☃x.getItemStack();
         if (!☃xxxxxxx.isEmpty() && (☃xx == null || !☃xx.getHasStack() || !☃xx.canTakeStack(☃))) {
            int ☃xxxxxxxx = ☃ == 0 ? 0 : this.inventorySlots.size() - 1;
            int ☃xxxxxxxxx = ☃ == 0 ? 1 : -1;

            for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx < 2; ☃xxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxx = ☃xxxxxxxx;
                  ☃xxxxxxxxxxx >= 0 && ☃xxxxxxxxxxx < this.inventorySlots.size() && ☃xxxxxxx.getCount() < ☃xxxxxxx.getMaxStackSize();
                  ☃xxxxxxxxxxx += ☃xxxxxxxxx
               ) {
                  Slot ☃xxxxxxxxxxxx = this.inventorySlots.get(☃xxxxxxxxxxx);
                  if (☃xxxxxxxxxxxx.getHasStack()
                     && canAddItemToSlot(☃xxxxxxxxxxxx, ☃xxxxxxx, true)
                     && ☃xxxxxxxxxxxx.canTakeStack(☃)
                     && this.canMergeSlot(☃xxxxxxx, ☃xxxxxxxxxxxx)) {
                     ItemStack ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getStack();
                     if (☃xxxxxxxxxx != 0 || ☃xxxxxxxxxxxxx.getCount() != ☃xxxxxxxxxxxxx.getMaxStackSize()) {
                        int ☃xxxxxxxxxxxxxx = Math.min(☃xxxxxxx.getMaxStackSize() - ☃xxxxxxx.getCount(), ☃xxxxxxxxxxxxx.getCount());
                        ItemStack ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx.decrStackSize(☃xxxxxxxxxxxxxx);
                        ☃xxxxxxx.grow(☃xxxxxxxxxxxxxx);
                        if (☃xxxxxxxxxxxxxxx.isEmpty()) {
                           ☃xxxxxxxxxxxx.putStack(ItemStack.EMPTY);
                        }

                        ☃xxxxxxxxxxxx.onTake(☃, ☃xxxxxxxxxxxxxxx);
                     }
                  }
               }
            }
         }

         this.detectAndSendChanges();
      }

      return ☃;
   }

   public boolean canMergeSlot(ItemStack var1, Slot var2) {
      return true;
   }

   public void onContainerClosed(EntityPlayer var1) {
      InventoryPlayer ☃ = ☃.inventory;
      if (!☃.getItemStack().isEmpty()) {
         ☃.dropItem(☃.getItemStack(), false);
         ☃.setItemStack(ItemStack.EMPTY);
      }
   }

   protected void clearContainer(EntityPlayer var1, World var2, IInventory var3) {
      if (!☃.isEntityAlive() || ☃ instanceof EntityPlayerMP && ((EntityPlayerMP)☃).hasDisconnected()) {
         for (int ☃ = 0; ☃ < ☃.getSizeInventory(); ☃++) {
            ☃.dropItem(☃.removeStackFromSlot(☃), false);
         }
      } else {
         for (int ☃ = 0; ☃ < ☃.getSizeInventory(); ☃++) {
            ☃.inventory.placeItemBackInInventory(☃, ☃.removeStackFromSlot(☃));
         }
      }
   }

   public void onCraftMatrixChanged(IInventory var1) {
      this.detectAndSendChanges();
   }

   public void putStackInSlot(int var1, ItemStack var2) {
      this.getSlot(☃).putStack(☃);
   }

   public void setAll(List<ItemStack> var1) {
      for (int ☃ = 0; ☃ < ☃.size(); ☃++) {
         this.getSlot(☃).putStack(☃.get(☃));
      }
   }

   public void updateProgressBar(int var1, int var2) {
   }

   public short getNextTransactionID(InventoryPlayer var1) {
      this.transactionID++;
      return this.transactionID;
   }

   public boolean getCanCraft(EntityPlayer var1) {
      return !this.playerList.contains(☃);
   }

   public void setCanCraft(EntityPlayer var1, boolean var2) {
      if (☃) {
         this.playerList.remove(☃);
      } else {
         this.playerList.add(☃);
      }
   }

   public abstract boolean canInteractWith(EntityPlayer var1);

   protected boolean mergeItemStack(ItemStack var1, int var2, int var3, boolean var4) {
      boolean ☃ = false;
      int ☃x = ☃;
      if (☃) {
         ☃x = ☃ - 1;
      }

      if (☃.isStackable()) {
         while (!☃.isEmpty() && (☃ ? ☃x >= ☃ : ☃x < ☃)) {
            Slot ☃xx = this.inventorySlots.get(☃x);
            ItemStack ☃xxx = ☃xx.getStack();
            if (!☃xxx.isEmpty()
               && ☃xxx.getItem() == ☃.getItem()
               && (!☃.getHasSubtypes() || ☃.getMetadata() == ☃xxx.getMetadata())
               && ItemStack.areItemStackTagsEqual(☃, ☃xxx)) {
               int ☃xxxx = ☃xxx.getCount() + ☃.getCount();
               if (☃xxxx <= ☃.getMaxStackSize()) {
                  ☃.setCount(0);
                  ☃xxx.setCount(☃xxxx);
                  ☃xx.onSlotChanged();
                  ☃ = true;
               } else if (☃xxx.getCount() < ☃.getMaxStackSize()) {
                  ☃.shrink(☃.getMaxStackSize() - ☃xxx.getCount());
                  ☃xxx.setCount(☃.getMaxStackSize());
                  ☃xx.onSlotChanged();
                  ☃ = true;
               }
            }

            if (☃) {
               ☃x--;
            } else {
               ☃x++;
            }
         }
      }

      if (!☃.isEmpty()) {
         if (☃) {
            ☃x = ☃ - 1;
         } else {
            ☃x = ☃;
         }

         while (☃ ? ☃x >= ☃ : ☃x < ☃) {
            Slot ☃xxxx = this.inventorySlots.get(☃x);
            ItemStack ☃xxxxx = ☃xxxx.getStack();
            if (☃xxxxx.isEmpty() && ☃xxxx.isItemValid(☃)) {
               if (☃.getCount() > ☃xxxx.getSlotStackLimit()) {
                  ☃xxxx.putStack(☃.splitStack(☃xxxx.getSlotStackLimit()));
               } else {
                  ☃xxxx.putStack(☃.splitStack(☃.getCount()));
               }

               ☃xxxx.onSlotChanged();
               ☃ = true;
               break;
            }

            if (☃) {
               ☃x--;
            } else {
               ☃x++;
            }
         }
      }

      return ☃;
   }

   public static int extractDragMode(int var0) {
      return ☃ >> 2 & 3;
   }

   public static int getDragEvent(int var0) {
      return ☃ & 3;
   }

   public static int getQuickcraftMask(int var0, int var1) {
      return ☃ & 3 | (☃ & 3) << 2;
   }

   public static boolean isValidDragMode(int var0, EntityPlayer var1) {
      if (☃ == 0) {
         return true;
      } else {
         return ☃ == 1 ? true : ☃ == 2 && ☃.capabilities.isCreativeMode;
      }
   }

   protected void resetDrag() {
      this.dragEvent = 0;
      this.dragSlots.clear();
   }

   public static boolean canAddItemToSlot(@Nullable Slot var0, ItemStack var1, boolean var2) {
      boolean ☃ = ☃ == null || !☃.getHasStack();
      return !☃ && ☃.isItemEqual(☃.getStack()) && ItemStack.areItemStackTagsEqual(☃.getStack(), ☃)
         ? ☃.getStack().getCount() + (☃ ? 0 : ☃.getCount()) <= ☃.getMaxStackSize()
         : ☃;
   }

   public static void computeStackSize(Set<Slot> var0, int var1, ItemStack var2, int var3) {
      switch (☃) {
         case 0:
            ☃.setCount(MathHelper.floor((float)☃.getCount() / ☃.size()));
            break;
         case 1:
            ☃.setCount(1);
            break;
         case 2:
            ☃.setCount(☃.getItem().getItemStackLimit());
      }

      ☃.grow(☃);
   }

   public boolean canDragIntoSlot(Slot var1) {
      return true;
   }

   public static int calcRedstone(@Nullable TileEntity var0) {
      return ☃ instanceof IInventory ? calcRedstoneFromInventory((IInventory)☃) : 0;
   }

   public static int calcRedstoneFromInventory(@Nullable IInventory var0) {
      if (☃ == null) {
         return 0;
      } else {
         int ☃ = 0;
         float ☃x = 0.0F;

         for (int ☃xx = 0; ☃xx < ☃.getSizeInventory(); ☃xx++) {
            ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
            if (!☃xxx.isEmpty()) {
               ☃x += (float)☃xxx.getCount() / Math.min(☃.getInventoryStackLimit(), ☃xxx.getMaxStackSize());
               ☃++;
            }
         }

         ☃x /= ☃.getSizeInventory();
         return MathHelper.floor(☃x * 14.0F) + (☃ > 0 ? 1 : 0);
      }
   }

   protected void slotChangedCraftingGrid(World var1, EntityPlayer var2, InventoryCrafting var3, InventoryCraftResult var4) {
      if (!☃.isRemote) {
         EntityPlayerMP ☃ = (EntityPlayerMP)☃;
         ItemStack ☃x = ItemStack.EMPTY;
         IRecipe ☃xx = CraftingManager.findMatchingRecipe(☃, ☃);
         if (☃xx != null && (☃xx.isDynamic() || !☃.getGameRules().getBoolean("doLimitedCrafting") || ☃.getRecipeBook().isUnlocked(☃xx))) {
            ☃.setRecipeUsed(☃xx);
            ☃x = ☃xx.getCraftingResult(☃);
         }

         ☃.setInventorySlotContents(0, ☃x);
         ☃.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, ☃x));
      }
   }
}
