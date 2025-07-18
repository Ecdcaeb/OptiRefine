package net.minecraft.inventory;

import java.util.Map;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerRepair extends Container {
   private static final Logger LOGGER = LogManager.getLogger();
   private final IInventory outputSlot = new InventoryCraftResult();
   private final IInventory inputSlots = new InventoryBasic("Repair", true, 2) {
      @Override
      public void markDirty() {
         super.markDirty();
         ContainerRepair.this.onCraftMatrixChanged(this);
      }
   };
   private final World world;
   private final BlockPos pos;
   public int maximumCost;
   private int materialCost;
   private String repairedItemName;
   private final EntityPlayer player;

   public ContainerRepair(InventoryPlayer var1, World var2, EntityPlayer var3) {
      this(☃, ☃, BlockPos.ORIGIN, ☃);
   }

   public ContainerRepair(InventoryPlayer var1, final World var2, final BlockPos var3, EntityPlayer var4) {
      this.pos = ☃;
      this.world = ☃;
      this.player = ☃;
      this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
      this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
      this.addSlotToContainer(
         new Slot(this.outputSlot, 2, 134, 47) {
            @Override
            public boolean isItemValid(ItemStack var1) {
               return false;
            }

            @Override
            public boolean canTakeStack(EntityPlayer var1) {
               return (☃.capabilities.isCreativeMode || ☃.experienceLevel >= ContainerRepair.this.maximumCost)
                  && ContainerRepair.this.maximumCost > 0
                  && this.getHasStack();
            }

            @Override
            public ItemStack onTake(EntityPlayer var1, ItemStack var2x) {
               if (!☃.capabilities.isCreativeMode) {
                  ☃.addExperienceLevel(-ContainerRepair.this.maximumCost);
               }

               ContainerRepair.this.inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);
               if (ContainerRepair.this.materialCost > 0) {
                  ItemStack ☃ = ContainerRepair.this.inputSlots.getStackInSlot(1);
                  if (!☃.isEmpty() && ☃.getCount() > ContainerRepair.this.materialCost) {
                     ☃.shrink(ContainerRepair.this.materialCost);
                     ContainerRepair.this.inputSlots.setInventorySlotContents(1, ☃);
                  } else {
                     ContainerRepair.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
                  }
               } else {
                  ContainerRepair.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
               }

               ContainerRepair.this.maximumCost = 0;
               IBlockState ☃ = ☃.getBlockState(☃);
               if (!☃.capabilities.isCreativeMode && !☃.isRemote && ☃.getBlock() == Blocks.ANVIL && ☃.getRNG().nextFloat() < 0.12F) {
                  int ☃x = ☃.getValue(BlockAnvil.DAMAGE);
                  if (++☃x > 2) {
                     ☃.setBlockToAir(☃);
                     ☃.playEvent(1029, ☃, 0);
                  } else {
                     ☃.setBlockState(☃, ☃.withProperty(BlockAnvil.DAMAGE, ☃x), 2);
                     ☃.playEvent(1030, ☃, 0);
                  }
               } else if (!☃.isRemote) {
                  ☃.playEvent(1030, ☃, 0);
               }

               return ☃;
            }
         }
      );

      for (int ☃ = 0; ☃ < 3; ☃++) {
         for (int ☃x = 0; ☃x < 9; ☃x++) {
            this.addSlotToContainer(new Slot(☃, ☃x + ☃ * 9 + 9, 8 + ☃x * 18, 84 + ☃ * 18));
         }
      }

      for (int ☃ = 0; ☃ < 9; ☃++) {
         this.addSlotToContainer(new Slot(☃, ☃, 8 + ☃ * 18, 142));
      }
   }

   @Override
   public void onCraftMatrixChanged(IInventory var1) {
      super.onCraftMatrixChanged(☃);
      if (☃ == this.inputSlots) {
         this.updateRepairOutput();
      }
   }

   public void updateRepairOutput() {
      ItemStack ☃ = this.inputSlots.getStackInSlot(0);
      this.maximumCost = 1;
      int ☃x = 0;
      int ☃xx = 0;
      int ☃xxx = 0;
      if (☃.isEmpty()) {
         this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
         this.maximumCost = 0;
      } else {
         ItemStack ☃xxxx = ☃.copy();
         ItemStack ☃xxxxx = this.inputSlots.getStackInSlot(1);
         Map<Enchantment, Integer> ☃xxxxxx = EnchantmentHelper.getEnchantments(☃xxxx);
         ☃xx += ☃.getRepairCost() + (☃xxxxx.isEmpty() ? 0 : ☃xxxxx.getRepairCost());
         this.materialCost = 0;
         if (!☃xxxxx.isEmpty()) {
            boolean ☃xxxxxxx = ☃xxxxx.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(☃xxxxx).isEmpty();
            if (☃xxxx.isItemStackDamageable() && ☃xxxx.getItem().getIsRepairable(☃, ☃xxxxx)) {
               int ☃xxxxxxxx = Math.min(☃xxxx.getItemDamage(), ☃xxxx.getMaxDamage() / 4);
               if (☃xxxxxxxx <= 0) {
                  this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                  this.maximumCost = 0;
                  return;
               }

               int ☃xxxxxxxxx;
               for (☃xxxxxxxxx = 0; ☃xxxxxxxx > 0 && ☃xxxxxxxxx < ☃xxxxx.getCount(); ☃xxxxxxxxx++) {
                  int ☃xxxxxxxxxx = ☃xxxx.getItemDamage() - ☃xxxxxxxx;
                  ☃xxxx.setItemDamage(☃xxxxxxxxxx);
                  ☃x++;
                  ☃xxxxxxxx = Math.min(☃xxxx.getItemDamage(), ☃xxxx.getMaxDamage() / 4);
               }

               this.materialCost = ☃xxxxxxxxx;
            } else {
               if (!☃xxxxxxx && (☃xxxx.getItem() != ☃xxxxx.getItem() || !☃xxxx.isItemStackDamageable())) {
                  this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                  this.maximumCost = 0;
                  return;
               }

               if (☃xxxx.isItemStackDamageable() && !☃xxxxxxx) {
                  int ☃xxxxxxxxx = ☃.getMaxDamage() - ☃.getItemDamage();
                  int ☃xxxxxxxxxx = ☃xxxxx.getMaxDamage() - ☃xxxxx.getItemDamage();
                  int ☃xxxxxxxxxxx = ☃xxxxxxxxxx + ☃xxxx.getMaxDamage() * 12 / 100;
                  int ☃xxxxxxxxxxxx = ☃xxxxxxxxx + ☃xxxxxxxxxxx;
                  int ☃xxxxxxxxxxxxx = ☃xxxx.getMaxDamage() - ☃xxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxx < 0) {
                     ☃xxxxxxxxxxxxx = 0;
                  }

                  if (☃xxxxxxxxxxxxx < ☃xxxx.getMetadata()) {
                     ☃xxxx.setItemDamage(☃xxxxxxxxxxxxx);
                     ☃x += 2;
                  }
               }

               Map<Enchantment, Integer> ☃xxxxxxxxxxxxxx = EnchantmentHelper.getEnchantments(☃xxxxx);
               boolean ☃xxxxxxxxxxxxxxx = false;
               boolean ☃xxxxxxxxxxxxxxxx = false;

               for (Enchantment ☃xxxxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxxx.keySet()) {
                  if (☃xxxxxxxxxxxxxxxxx != null) {
                     int ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxx.containsKey(☃xxxxxxxxxxxxxxxxx) ? ☃xxxxxx.get(☃xxxxxxxxxxxxxxxxx) : 0;
                     int ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxx);
                     ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxx == ☃xxxxxxxxxxxxxxxxxxx
                        ? ☃xxxxxxxxxxxxxxxxxxx + 1
                        : Math.max(☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx);
                     boolean ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx.canApply(☃);
                     if (this.player.capabilities.isCreativeMode || ☃.getItem() == Items.ENCHANTED_BOOK) {
                        ☃xxxxxxxxxxxxxxxxxxxx = true;
                     }

                     for (Enchantment ☃xxxxxxxxxxxxxxxxxxxxx : ☃xxxxxx.keySet()) {
                        if (☃xxxxxxxxxxxxxxxxxxxxx != ☃xxxxxxxxxxxxxxxxx && !☃xxxxxxxxxxxxxxxxx.isCompatibleWith(☃xxxxxxxxxxxxxxxxxxxxx)) {
                           ☃xxxxxxxxxxxxxxxxxxxx = false;
                           ☃x++;
                        }
                     }

                     if (!☃xxxxxxxxxxxxxxxxxxxx) {
                        ☃xxxxxxxxxxxxxxxx = true;
                     } else {
                        ☃xxxxxxxxxxxxxxx = true;
                        if (☃xxxxxxxxxxxxxxxxxxx > ☃xxxxxxxxxxxxxxxxx.getMaxLevel()) {
                           ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx.getMaxLevel();
                        }

                        ☃xxxxxx.put(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx);
                        int ☃xxxxxxxxxxxxxxxxxxxxxx = 0;
                        switch (☃xxxxxxxxxxxxxxxxx.getRarity()) {
                           case COMMON:
                              ☃xxxxxxxxxxxxxxxxxxxxxx = 1;
                              break;
                           case UNCOMMON:
                              ☃xxxxxxxxxxxxxxxxxxxxxx = 2;
                              break;
                           case RARE:
                              ☃xxxxxxxxxxxxxxxxxxxxxx = 4;
                              break;
                           case VERY_RARE:
                              ☃xxxxxxxxxxxxxxxxxxxxxx = 8;
                        }

                        if (☃xxxxxxx) {
                           ☃xxxxxxxxxxxxxxxxxxxxxx = Math.max(1, ☃xxxxxxxxxxxxxxxxxxxxxx / 2);
                        }

                        ☃x += ☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx;
                        if (☃.getCount() > 1) {
                           ☃x = 40;
                        }
                     }
                  }
               }

               if (☃xxxxxxxxxxxxxxxx && !☃xxxxxxxxxxxxxxx) {
                  this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                  this.maximumCost = 0;
                  return;
               }
            }
         }

         if (StringUtils.isBlank(this.repairedItemName)) {
            if (☃.hasDisplayName()) {
               ☃xxx = 1;
               ☃x += ☃xxx;
               ☃xxxx.clearCustomName();
            }
         } else if (!this.repairedItemName.equals(☃.getDisplayName())) {
            ☃xxx = 1;
            ☃x += ☃xxx;
            ☃xxxx.setStackDisplayName(this.repairedItemName);
         }

         this.maximumCost = ☃xx + ☃x;
         if (☃x <= 0) {
            ☃xxxx = ItemStack.EMPTY;
         }

         if (☃xxx == ☃x && ☃xxx > 0 && this.maximumCost >= 40) {
            this.maximumCost = 39;
         }

         if (this.maximumCost >= 40 && !this.player.capabilities.isCreativeMode) {
            ☃xxxx = ItemStack.EMPTY;
         }

         if (!☃xxxx.isEmpty()) {
            int ☃xxxxxxx = ☃xxxx.getRepairCost();
            if (!☃xxxxx.isEmpty() && ☃xxxxxxx < ☃xxxxx.getRepairCost()) {
               ☃xxxxxxx = ☃xxxxx.getRepairCost();
            }

            if (☃xxx != ☃x || ☃xxx == 0) {
               ☃xxxxxxx = ☃xxxxxxx * 2 + 1;
            }

            ☃xxxx.setRepairCost(☃xxxxxxx);
            EnchantmentHelper.setEnchantments(☃xxxxxx, ☃xxxx);
         }

         this.outputSlot.setInventorySlotContents(0, ☃xxxx);
         this.detectAndSendChanges();
      }
   }

   @Override
   public void addListener(IContainerListener var1) {
      super.addListener(☃);
      ☃.sendWindowProperty(this, 0, this.maximumCost);
   }

   @Override
   public void updateProgressBar(int var1, int var2) {
      if (☃ == 0) {
         this.maximumCost = ☃;
      }
   }

   @Override
   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(☃);
      if (!this.world.isRemote) {
         this.clearContainer(☃, this.world, this.inputSlots);
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.world.getBlockState(this.pos).getBlock() != Blocks.ANVIL
         ? false
         : ☃.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ == 2) {
            if (!this.mergeItemStack(☃xx, 3, 39, true)) {
               return ItemStack.EMPTY;
            }

            ☃x.onSlotChange(☃xx, ☃);
         } else if (☃ != 0 && ☃ != 1) {
            if (☃ >= 3 && ☃ < 39 && !this.mergeItemStack(☃xx, 0, 2, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(☃xx, 3, 39, false)) {
            return ItemStack.EMPTY;
         }

         if (☃xx.isEmpty()) {
            ☃x.putStack(ItemStack.EMPTY);
         } else {
            ☃x.onSlotChanged();
         }

         if (☃xx.getCount() == ☃.getCount()) {
            return ItemStack.EMPTY;
         }

         ☃x.onTake(☃, ☃xx);
      }

      return ☃;
   }

   public void updateItemName(String var1) {
      this.repairedItemName = ☃;
      if (this.getSlot(2).getHasStack()) {
         ItemStack ☃ = this.getSlot(2).getStack();
         if (StringUtils.isBlank(☃)) {
            ☃.clearCustomName();
         } else {
            ☃.setStackDisplayName(this.repairedItemName);
         }
      }

      this.updateRepairOutput();
   }
}
