package net.minecraft.tileentity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntityHopper extends TileEntityLockableLoot implements IHopper, ITickable {
   private NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);
   private int transferCooldown = -1;
   private long tickedGameTime;

   public static void registerFixesHopper(DataFixer var0) {
      ☃.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityHopper.class, "Items"));
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
      if (!this.checkLootAndRead(☃)) {
         ItemStackHelper.loadAllItems(☃, this.inventory);
      }

      if (☃.hasKey("CustomName", 8)) {
         this.customName = ☃.getString("CustomName");
      }

      this.transferCooldown = ☃.getInteger("TransferCooldown");
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      if (!this.checkLootAndWrite(☃)) {
         ItemStackHelper.saveAllItems(☃, this.inventory);
      }

      ☃.setInteger("TransferCooldown", this.transferCooldown);
      if (this.hasCustomName()) {
         ☃.setString("CustomName", this.customName);
      }

      return ☃;
   }

   @Override
   public int getSizeInventory() {
      return this.inventory.size();
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      this.fillWithLoot(null);
      return ItemStackHelper.getAndSplit(this.getItems(), ☃, ☃);
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.fillWithLoot(null);
      this.getItems().set(☃, ☃);
      if (☃.getCount() > this.getInventoryStackLimit()) {
         ☃.setCount(this.getInventoryStackLimit());
      }
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.customName : "container.hopper";
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public void update() {
      if (this.world != null && !this.world.isRemote) {
         this.transferCooldown--;
         this.tickedGameTime = this.world.getTotalWorldTime();
         if (!this.isOnTransferCooldown()) {
            this.setTransferCooldown(0);
            this.updateHopper();
         }
      }
   }

   private boolean updateHopper() {
      if (this.world != null && !this.world.isRemote) {
         if (!this.isOnTransferCooldown() && BlockHopper.isEnabled(this.getBlockMetadata())) {
            boolean ☃ = false;
            if (!this.isInventoryEmpty()) {
               ☃ = this.transferItemsOut();
            }

            if (!this.isFull()) {
               ☃ = pullItems(this) || ☃;
            }

            if (☃) {
               this.setTransferCooldown(8);
               this.markDirty();
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean isInventoryEmpty() {
      for (ItemStack ☃ : this.inventory) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean isEmpty() {
      return this.isInventoryEmpty();
   }

   private boolean isFull() {
      for (ItemStack ☃ : this.inventory) {
         if (☃.isEmpty() || ☃.getCount() != ☃.getMaxStackSize()) {
            return false;
         }
      }

      return true;
   }

   private boolean transferItemsOut() {
      IInventory ☃ = this.getInventoryForHopperTransfer();
      if (☃ == null) {
         return false;
      } else {
         EnumFacing ☃x = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();
         if (this.isInventoryFull(☃, ☃x)) {
            return false;
         } else {
            for (int ☃xx = 0; ☃xx < this.getSizeInventory(); ☃xx++) {
               if (!this.getStackInSlot(☃xx).isEmpty()) {
                  ItemStack ☃xxx = this.getStackInSlot(☃xx).copy();
                  ItemStack ☃xxxx = putStackInInventoryAllSlots(this, ☃, this.decrStackSize(☃xx, 1), ☃x);
                  if (☃xxxx.isEmpty()) {
                     ☃.markDirty();
                     return true;
                  }

                  this.setInventorySlotContents(☃xx, ☃xxx);
               }
            }

            return false;
         }
      }
   }

   private boolean isInventoryFull(IInventory var1, EnumFacing var2) {
      if (☃ instanceof ISidedInventory) {
         ISidedInventory ☃ = (ISidedInventory)☃;
         int[] ☃x = ☃.getSlotsForFace(☃);

         for (int ☃xx : ☃x) {
            ItemStack ☃xxx = ☃.getStackInSlot(☃xx);
            if (☃xxx.isEmpty() || ☃xxx.getCount() != ☃xxx.getMaxStackSize()) {
               return false;
            }
         }
      } else {
         int ☃ = ☃.getSizeInventory();

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            ItemStack ☃xxx = ☃.getStackInSlot(☃x);
            if (☃xxx.isEmpty() || ☃xxx.getCount() != ☃xxx.getMaxStackSize()) {
               return false;
            }
         }
      }

      return true;
   }

   private static boolean isInventoryEmpty(IInventory var0, EnumFacing var1) {
      if (☃ instanceof ISidedInventory) {
         ISidedInventory ☃ = (ISidedInventory)☃;
         int[] ☃x = ☃.getSlotsForFace(☃);

         for (int ☃xx : ☃x) {
            if (!☃.getStackInSlot(☃xx).isEmpty()) {
               return false;
            }
         }
      } else {
         int ☃ = ☃.getSizeInventory();

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            if (!☃.getStackInSlot(☃x).isEmpty()) {
               return false;
            }
         }
      }

      return true;
   }

   public static boolean pullItems(IHopper var0) {
      IInventory ☃ = getSourceInventory(☃);
      if (☃ != null) {
         EnumFacing ☃x = EnumFacing.DOWN;
         if (isInventoryEmpty(☃, ☃x)) {
            return false;
         }

         if (☃ instanceof ISidedInventory) {
            ISidedInventory ☃xx = (ISidedInventory)☃;
            int[] ☃xxx = ☃xx.getSlotsForFace(☃x);

            for (int ☃xxxx : ☃xxx) {
               if (pullItemFromSlot(☃, ☃, ☃xxxx, ☃x)) {
                  return true;
               }
            }
         } else {
            int ☃xx = ☃.getSizeInventory();

            for (int ☃xxx = 0; ☃xxx < ☃xx; ☃xxx++) {
               if (pullItemFromSlot(☃, ☃, ☃xxx, ☃x)) {
                  return true;
               }
            }
         }
      } else {
         for (EntityItem ☃xx : getCaptureItems(☃.getWorld(), ☃.getXPos(), ☃.getYPos(), ☃.getZPos())) {
            if (putDropInInventoryAllSlots(null, ☃, ☃xx)) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean pullItemFromSlot(IHopper var0, IInventory var1, int var2, EnumFacing var3) {
      ItemStack ☃ = ☃.getStackInSlot(☃);
      if (!☃.isEmpty() && canExtractItemFromSlot(☃, ☃, ☃, ☃)) {
         ItemStack ☃x = ☃.copy();
         ItemStack ☃xx = putStackInInventoryAllSlots(☃, ☃, ☃.decrStackSize(☃, 1), null);
         if (☃xx.isEmpty()) {
            ☃.markDirty();
            return true;
         }

         ☃.setInventorySlotContents(☃, ☃x);
      }

      return false;
   }

   public static boolean putDropInInventoryAllSlots(IInventory var0, IInventory var1, EntityItem var2) {
      boolean ☃ = false;
      if (☃ == null) {
         return false;
      } else {
         ItemStack ☃x = ☃.getItem().copy();
         ItemStack ☃xx = putStackInInventoryAllSlots(☃, ☃, ☃x, null);
         if (☃xx.isEmpty()) {
            ☃ = true;
            ☃.setDead();
         } else {
            ☃.setItem(☃xx);
         }

         return ☃;
      }
   }

   public static ItemStack putStackInInventoryAllSlots(IInventory var0, IInventory var1, ItemStack var2, @Nullable EnumFacing var3) {
      if (☃ instanceof ISidedInventory && ☃ != null) {
         ISidedInventory ☃ = (ISidedInventory)☃;
         int[] ☃x = ☃.getSlotsForFace(☃);

         for (int ☃xx = 0; ☃xx < ☃x.length && !☃.isEmpty(); ☃xx++) {
            ☃ = insertStack(☃, ☃, ☃, ☃x[☃xx], ☃);
         }
      } else {
         int ☃ = ☃.getSizeInventory();

         for (int ☃x = 0; ☃x < ☃ && !☃.isEmpty(); ☃x++) {
            ☃ = insertStack(☃, ☃, ☃, ☃x, ☃);
         }
      }

      return ☃;
   }

   private static boolean canInsertItemInSlot(IInventory var0, ItemStack var1, int var2, EnumFacing var3) {
      return !☃.isItemValidForSlot(☃, ☃) ? false : !(☃ instanceof ISidedInventory) || ((ISidedInventory)☃).canInsertItem(☃, ☃, ☃);
   }

   private static boolean canExtractItemFromSlot(IInventory var0, ItemStack var1, int var2, EnumFacing var3) {
      return !(☃ instanceof ISidedInventory) || ((ISidedInventory)☃).canExtractItem(☃, ☃, ☃);
   }

   private static ItemStack insertStack(IInventory var0, IInventory var1, ItemStack var2, int var3, EnumFacing var4) {
      ItemStack ☃ = ☃.getStackInSlot(☃);
      if (canInsertItemInSlot(☃, ☃, ☃, ☃)) {
         boolean ☃x = false;
         boolean ☃xx = ☃.isEmpty();
         if (☃.isEmpty()) {
            ☃.setInventorySlotContents(☃, ☃);
            ☃ = ItemStack.EMPTY;
            ☃x = true;
         } else if (canCombine(☃, ☃)) {
            int ☃xxx = ☃.getMaxStackSize() - ☃.getCount();
            int ☃xxxx = Math.min(☃.getCount(), ☃xxx);
            ☃.shrink(☃xxxx);
            ☃.grow(☃xxxx);
            ☃x = ☃xxxx > 0;
         }

         if (☃x) {
            if (☃xx && ☃ instanceof TileEntityHopper) {
               TileEntityHopper ☃xxx = (TileEntityHopper)☃;
               if (!☃xxx.mayTransfer()) {
                  int ☃xxxx = 0;
                  if (☃ != null && ☃ instanceof TileEntityHopper) {
                     TileEntityHopper ☃xxxxx = (TileEntityHopper)☃;
                     if (☃xxx.tickedGameTime >= ☃xxxxx.tickedGameTime) {
                        ☃xxxx = 1;
                     }
                  }

                  ☃xxx.setTransferCooldown(8 - ☃xxxx);
               }
            }

            ☃.markDirty();
         }
      }

      return ☃;
   }

   private IInventory getInventoryForHopperTransfer() {
      EnumFacing ☃ = BlockHopper.getFacing(this.getBlockMetadata());
      return getInventoryAtPosition(this.getWorld(), this.getXPos() + ☃.getXOffset(), this.getYPos() + ☃.getYOffset(), this.getZPos() + ☃.getZOffset());
   }

   public static IInventory getSourceInventory(IHopper var0) {
      return getInventoryAtPosition(☃.getWorld(), ☃.getXPos(), ☃.getYPos() + 1.0, ☃.getZPos());
   }

   public static List<EntityItem> getCaptureItems(World var0, double var1, double var3, double var5) {
      return ☃.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(☃ - 0.5, ☃, ☃ - 0.5, ☃ + 0.5, ☃ + 1.5, ☃ + 0.5), EntitySelectors.IS_ALIVE);
   }

   public static IInventory getInventoryAtPosition(World var0, double var1, double var3, double var5) {
      IInventory ☃ = null;
      int ☃x = MathHelper.floor(☃);
      int ☃xx = MathHelper.floor(☃);
      int ☃xxx = MathHelper.floor(☃);
      BlockPos ☃xxxx = new BlockPos(☃x, ☃xx, ☃xxx);
      Block ☃xxxxx = ☃.getBlockState(☃xxxx).getBlock();
      if (☃xxxxx.hasTileEntity()) {
         TileEntity ☃xxxxxx = ☃.getTileEntity(☃xxxx);
         if (☃xxxxxx instanceof IInventory) {
            ☃ = (IInventory)☃xxxxxx;
            if (☃ instanceof TileEntityChest && ☃xxxxx instanceof BlockChest) {
               ☃ = ((BlockChest)☃xxxxx).getContainer(☃, ☃xxxx, true);
            }
         }
      }

      if (☃ == null) {
         List<Entity> ☃xxxxxx = ☃.getEntitiesInAABBexcluding(
            null, new AxisAlignedBB(☃ - 0.5, ☃ - 0.5, ☃ - 0.5, ☃ + 0.5, ☃ + 0.5, ☃ + 0.5), EntitySelectors.HAS_INVENTORY
         );
         if (!☃xxxxxx.isEmpty()) {
            ☃ = (IInventory)☃xxxxxx.get(☃.rand.nextInt(☃xxxxxx.size()));
         }
      }

      return ☃;
   }

   private static boolean canCombine(ItemStack var0, ItemStack var1) {
      if (☃.getItem() != ☃.getItem()) {
         return false;
      } else if (☃.getMetadata() != ☃.getMetadata()) {
         return false;
      } else {
         return ☃.getCount() > ☃.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(☃, ☃);
      }
   }

   @Override
   public double getXPos() {
      return this.pos.getX() + 0.5;
   }

   @Override
   public double getYPos() {
      return this.pos.getY() + 0.5;
   }

   @Override
   public double getZPos() {
      return this.pos.getZ() + 0.5;
   }

   private void setTransferCooldown(int var1) {
      this.transferCooldown = ☃;
   }

   private boolean isOnTransferCooldown() {
      return this.transferCooldown > 0;
   }

   private boolean mayTransfer() {
      return this.transferCooldown > 8;
   }

   @Override
   public String getGuiID() {
      return "minecraft:hopper";
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      this.fillWithLoot(☃);
      return new ContainerHopper(☃, this, ☃);
   }

   @Override
   protected NonNullList<ItemStack> getItems() {
      return this.inventory;
   }
}
