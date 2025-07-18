package net.minecraft.inventory;

import java.util.List;
import java.util.Random;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerEnchantment extends Container {
   public IInventory tableInventory = new InventoryBasic("Enchant", true, 2) {
      @Override
      public int getInventoryStackLimit() {
         return 64;
      }

      @Override
      public void markDirty() {
         super.markDirty();
         ContainerEnchantment.this.onCraftMatrixChanged(this);
      }
   };
   private final World world;
   private final BlockPos position;
   private final Random rand = new Random();
   public int xpSeed;
   public int[] enchantLevels = new int[3];
   public int[] enchantClue = new int[]{-1, -1, -1};
   public int[] worldClue = new int[]{-1, -1, -1};

   public ContainerEnchantment(InventoryPlayer var1, World var2) {
      this(☃, ☃, BlockPos.ORIGIN);
   }

   public ContainerEnchantment(InventoryPlayer var1, World var2, BlockPos var3) {
      this.world = ☃;
      this.position = ☃;
      this.xpSeed = ☃.player.getXPSeed();
      this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47) {
         @Override
         public boolean isItemValid(ItemStack var1) {
            return true;
         }

         @Override
         public int getSlotStackLimit() {
            return 1;
         }
      });
      this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47) {
         @Override
         public boolean isItemValid(ItemStack var1) {
            return ☃.getItem() == Items.DYE && EnumDyeColor.byDyeDamage(☃.getMetadata()) == EnumDyeColor.BLUE;
         }
      });

      for (int ☃ = 0; ☃ < 3; ☃++) {
         for (int ☃x = 0; ☃x < 9; ☃x++) {
            this.addSlotToContainer(new Slot(☃, ☃x + ☃ * 9 + 9, 8 + ☃x * 18, 84 + ☃ * 18));
         }
      }

      for (int ☃ = 0; ☃ < 9; ☃++) {
         this.addSlotToContainer(new Slot(☃, ☃, 8 + ☃ * 18, 142));
      }
   }

   protected void broadcastData(IContainerListener var1) {
      ☃.sendWindowProperty(this, 0, this.enchantLevels[0]);
      ☃.sendWindowProperty(this, 1, this.enchantLevels[1]);
      ☃.sendWindowProperty(this, 2, this.enchantLevels[2]);
      ☃.sendWindowProperty(this, 3, this.xpSeed & -16);
      ☃.sendWindowProperty(this, 4, this.enchantClue[0]);
      ☃.sendWindowProperty(this, 5, this.enchantClue[1]);
      ☃.sendWindowProperty(this, 6, this.enchantClue[2]);
      ☃.sendWindowProperty(this, 7, this.worldClue[0]);
      ☃.sendWindowProperty(this, 8, this.worldClue[1]);
      ☃.sendWindowProperty(this, 9, this.worldClue[2]);
   }

   @Override
   public void addListener(IContainerListener var1) {
      super.addListener(☃);
      this.broadcastData(☃);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (int ☃ = 0; ☃ < this.listeners.size(); ☃++) {
         IContainerListener ☃x = this.listeners.get(☃);
         this.broadcastData(☃x);
      }
   }

   @Override
   public void updateProgressBar(int var1, int var2) {
      if (☃ >= 0 && ☃ <= 2) {
         this.enchantLevels[☃] = ☃;
      } else if (☃ == 3) {
         this.xpSeed = ☃;
      } else if (☃ >= 4 && ☃ <= 6) {
         this.enchantClue[☃ - 4] = ☃;
      } else if (☃ >= 7 && ☃ <= 9) {
         this.worldClue[☃ - 7] = ☃;
      } else {
         super.updateProgressBar(☃, ☃);
      }
   }

   @Override
   public void onCraftMatrixChanged(IInventory var1) {
      if (☃ == this.tableInventory) {
         ItemStack ☃ = ☃.getStackInSlot(0);
         if (!☃.isEmpty() && ☃.isItemEnchantable()) {
            if (!this.world.isRemote) {
               int ☃x = 0;

               for (int ☃xx = -1; ☃xx <= 1; ☃xx++) {
                  for (int ☃xxx = -1; ☃xxx <= 1; ☃xxx++) {
                     if ((☃xx != 0 || ☃xxx != 0)
                        && this.world.isAirBlock(this.position.add(☃xxx, 0, ☃xx))
                        && this.world.isAirBlock(this.position.add(☃xxx, 1, ☃xx))) {
                        if (this.world.getBlockState(this.position.add(☃xxx * 2, 0, ☃xx * 2)).getBlock() == Blocks.BOOKSHELF) {
                           ☃x++;
                        }

                        if (this.world.getBlockState(this.position.add(☃xxx * 2, 1, ☃xx * 2)).getBlock() == Blocks.BOOKSHELF) {
                           ☃x++;
                        }

                        if (☃xxx != 0 && ☃xx != 0) {
                           if (this.world.getBlockState(this.position.add(☃xxx * 2, 0, ☃xx)).getBlock() == Blocks.BOOKSHELF) {
                              ☃x++;
                           }

                           if (this.world.getBlockState(this.position.add(☃xxx * 2, 1, ☃xx)).getBlock() == Blocks.BOOKSHELF) {
                              ☃x++;
                           }

                           if (this.world.getBlockState(this.position.add(☃xxx, 0, ☃xx * 2)).getBlock() == Blocks.BOOKSHELF) {
                              ☃x++;
                           }

                           if (this.world.getBlockState(this.position.add(☃xxx, 1, ☃xx * 2)).getBlock() == Blocks.BOOKSHELF) {
                              ☃x++;
                           }
                        }
                     }
                  }
               }

               this.rand.setSeed(this.xpSeed);

               for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
                  this.enchantLevels[☃xx] = EnchantmentHelper.calcItemStackEnchantability(this.rand, ☃xx, ☃x, ☃);
                  this.enchantClue[☃xx] = -1;
                  this.worldClue[☃xx] = -1;
                  if (this.enchantLevels[☃xx] < ☃xx + 1) {
                     this.enchantLevels[☃xx] = 0;
                  }
               }

               for (int ☃xxxx = 0; ☃xxxx < 3; ☃xxxx++) {
                  if (this.enchantLevels[☃xxxx] > 0) {
                     List<EnchantmentData> ☃xxxxx = this.getEnchantmentList(☃, ☃xxxx, this.enchantLevels[☃xxxx]);
                     if (☃xxxxx != null && !☃xxxxx.isEmpty()) {
                        EnchantmentData ☃xxxxxx = ☃xxxxx.get(this.rand.nextInt(☃xxxxx.size()));
                        this.enchantClue[☃xxxx] = Enchantment.getEnchantmentID(☃xxxxxx.enchantment);
                        this.worldClue[☃xxxx] = ☃xxxxxx.enchantmentLevel;
                     }
                  }
               }

               this.detectAndSendChanges();
            }
         } else {
            for (int ☃x = 0; ☃x < 3; ☃x++) {
               this.enchantLevels[☃x] = 0;
               this.enchantClue[☃x] = -1;
               this.worldClue[☃x] = -1;
            }
         }
      }
   }

   @Override
   public boolean enchantItem(EntityPlayer var1, int var2) {
      ItemStack ☃ = this.tableInventory.getStackInSlot(0);
      ItemStack ☃x = this.tableInventory.getStackInSlot(1);
      int ☃xx = ☃ + 1;
      if ((☃x.isEmpty() || ☃x.getCount() < ☃xx) && !☃.capabilities.isCreativeMode) {
         return false;
      } else if (this.enchantLevels[☃] > 0
         && !☃.isEmpty()
         && (☃.experienceLevel >= ☃xx && ☃.experienceLevel >= this.enchantLevels[☃] || ☃.capabilities.isCreativeMode)) {
         if (!this.world.isRemote) {
            List<EnchantmentData> ☃xxx = this.getEnchantmentList(☃, ☃, this.enchantLevels[☃]);
            if (!☃xxx.isEmpty()) {
               ☃.onEnchant(☃, ☃xx);
               boolean ☃xxxx = ☃.getItem() == Items.BOOK;
               if (☃xxxx) {
                  ☃ = new ItemStack(Items.ENCHANTED_BOOK);
                  this.tableInventory.setInventorySlotContents(0, ☃);
               }

               for (int ☃xxxxx = 0; ☃xxxxx < ☃xxx.size(); ☃xxxxx++) {
                  EnchantmentData ☃xxxxxx = ☃xxx.get(☃xxxxx);
                  if (☃xxxx) {
                     ItemEnchantedBook.addEnchantment(☃, ☃xxxxxx);
                  } else {
                     ☃.addEnchantment(☃xxxxxx.enchantment, ☃xxxxxx.enchantmentLevel);
                  }
               }

               if (!☃.capabilities.isCreativeMode) {
                  ☃x.shrink(☃xx);
                  if (☃x.isEmpty()) {
                     this.tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
                  }
               }

               ☃.addStat(StatList.ITEM_ENCHANTED);
               if (☃ instanceof EntityPlayerMP) {
                  CriteriaTriggers.ENCHANTED_ITEM.trigger((EntityPlayerMP)☃, ☃, ☃xx);
               }

               this.tableInventory.markDirty();
               this.xpSeed = ☃.getXPSeed();
               this.onCraftMatrixChanged(this.tableInventory);
               this.world
                  .playSound(
                     null, this.position, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F
                  );
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private List<EnchantmentData> getEnchantmentList(ItemStack var1, int var2, int var3) {
      this.rand.setSeed(this.xpSeed + ☃);
      List<EnchantmentData> ☃ = EnchantmentHelper.buildEnchantmentList(this.rand, ☃, ☃, false);
      if (☃.getItem() == Items.BOOK && ☃.size() > 1) {
         ☃.remove(this.rand.nextInt(☃.size()));
      }

      return ☃;
   }

   public int getLapisAmount() {
      ItemStack ☃ = this.tableInventory.getStackInSlot(1);
      return ☃.isEmpty() ? 0 : ☃.getCount();
   }

   @Override
   public void onContainerClosed(EntityPlayer var1) {
      super.onContainerClosed(☃);
      if (!this.world.isRemote) {
         this.clearContainer(☃, ☃.world, this.tableInventory);
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.world.getBlockState(this.position).getBlock() != Blocks.ENCHANTING_TABLE
         ? false
         : !(☃.getDistanceSq(this.position.getX() + 0.5, this.position.getY() + 0.5, this.position.getZ() + 0.5) > 64.0);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack ☃ = ItemStack.EMPTY;
      Slot ☃x = this.inventorySlots.get(☃);
      if (☃x != null && ☃x.getHasStack()) {
         ItemStack ☃xx = ☃x.getStack();
         ☃ = ☃xx.copy();
         if (☃ == 0) {
            if (!this.mergeItemStack(☃xx, 2, 38, true)) {
               return ItemStack.EMPTY;
            }
         } else if (☃ == 1) {
            if (!this.mergeItemStack(☃xx, 2, 38, true)) {
               return ItemStack.EMPTY;
            }
         } else if (☃xx.getItem() == Items.DYE && EnumDyeColor.byDyeDamage(☃xx.getMetadata()) == EnumDyeColor.BLUE) {
            if (!this.mergeItemStack(☃xx, 1, 2, true)) {
               return ItemStack.EMPTY;
            }
         } else {
            if (this.inventorySlots.get(0).getHasStack() || !this.inventorySlots.get(0).isItemValid(☃xx)) {
               return ItemStack.EMPTY;
            }

            if (☃xx.hasTagCompound() && ☃xx.getCount() == 1) {
               this.inventorySlots.get(0).putStack(☃xx.copy());
               ☃xx.setCount(0);
            } else if (!☃xx.isEmpty()) {
               this.inventorySlots.get(0).putStack(new ItemStack(☃xx.getItem(), 1, ☃xx.getMetadata()));
               ☃xx.shrink(1);
            }
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
}
