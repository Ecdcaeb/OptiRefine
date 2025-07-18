package net.minecraft.entity.player;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class InventoryPlayer implements IInventory {
   public final NonNullList<ItemStack> mainInventory = NonNullList.withSize(36, ItemStack.EMPTY);
   public final NonNullList<ItemStack> armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
   public final NonNullList<ItemStack> offHandInventory = NonNullList.withSize(1, ItemStack.EMPTY);
   private final List<NonNullList<ItemStack>> allInventories = Arrays.asList(this.mainInventory, this.armorInventory, this.offHandInventory);
   public int currentItem;
   public EntityPlayer player;
   private ItemStack itemStack = ItemStack.EMPTY;
   private int timesChanged;

   public InventoryPlayer(EntityPlayer var1) {
      this.player = ☃;
   }

   public ItemStack getCurrentItem() {
      return isHotbar(this.currentItem) ? this.mainInventory.get(this.currentItem) : ItemStack.EMPTY;
   }

   public static int getHotbarSize() {
      return 9;
   }

   private boolean canMergeStacks(ItemStack var1, ItemStack var2) {
      return !☃.isEmpty()
         && this.stackEqualExact(☃, ☃)
         && ☃.isStackable()
         && ☃.getCount() < ☃.getMaxStackSize()
         && ☃.getCount() < this.getInventoryStackLimit();
   }

   private boolean stackEqualExact(ItemStack var1, ItemStack var2) {
      return ☃.getItem() == ☃.getItem() && (!☃.getHasSubtypes() || ☃.getMetadata() == ☃.getMetadata()) && ItemStack.areItemStackTagsEqual(☃, ☃);
   }

   public int getFirstEmptyStack() {
      for (int ☃ = 0; ☃ < this.mainInventory.size(); ☃++) {
         if (this.mainInventory.get(☃).isEmpty()) {
            return ☃;
         }
      }

      return -1;
   }

   public void setPickedItemStack(ItemStack var1) {
      int ☃ = this.getSlotFor(☃);
      if (isHotbar(☃)) {
         this.currentItem = ☃;
      } else {
         if (☃ == -1) {
            this.currentItem = this.getBestHotbarSlot();
            if (!this.mainInventory.get(this.currentItem).isEmpty()) {
               int ☃x = this.getFirstEmptyStack();
               if (☃x != -1) {
                  this.mainInventory.set(☃x, this.mainInventory.get(this.currentItem));
               }
            }

            this.mainInventory.set(this.currentItem, ☃);
         } else {
            this.pickItem(☃);
         }
      }
   }

   public void pickItem(int var1) {
      this.currentItem = this.getBestHotbarSlot();
      ItemStack ☃ = this.mainInventory.get(this.currentItem);
      this.mainInventory.set(this.currentItem, this.mainInventory.get(☃));
      this.mainInventory.set(☃, ☃);
   }

   public static boolean isHotbar(int var0) {
      return ☃ >= 0 && ☃ < 9;
   }

   public int getSlotFor(ItemStack var1) {
      for (int ☃ = 0; ☃ < this.mainInventory.size(); ☃++) {
         if (!this.mainInventory.get(☃).isEmpty() && this.stackEqualExact(☃, this.mainInventory.get(☃))) {
            return ☃;
         }
      }

      return -1;
   }

   public int findSlotMatchingUnusedItem(ItemStack var1) {
      for (int ☃ = 0; ☃ < this.mainInventory.size(); ☃++) {
         ItemStack ☃x = this.mainInventory.get(☃);
         if (!this.mainInventory.get(☃).isEmpty()
            && this.stackEqualExact(☃, this.mainInventory.get(☃))
            && !this.mainInventory.get(☃).isItemDamaged()
            && !☃x.isItemEnchanted()
            && !☃x.hasDisplayName()) {
            return ☃;
         }
      }

      return -1;
   }

   public int getBestHotbarSlot() {
      for (int ☃ = 0; ☃ < 9; ☃++) {
         int ☃x = (this.currentItem + ☃) % 9;
         if (this.mainInventory.get(☃x).isEmpty()) {
            return ☃x;
         }
      }

      for (int ☃x = 0; ☃x < 9; ☃x++) {
         int ☃xx = (this.currentItem + ☃x) % 9;
         if (!this.mainInventory.get(☃xx).isItemEnchanted()) {
            return ☃xx;
         }
      }

      return this.currentItem;
   }

   public void changeCurrentItem(int var1) {
      if (☃ > 0) {
         ☃ = 1;
      }

      if (☃ < 0) {
         ☃ = -1;
      }

      this.currentItem -= ☃;

      while (this.currentItem < 0) {
         this.currentItem += 9;
      }

      while (this.currentItem >= 9) {
         this.currentItem -= 9;
      }
   }

   public int clearMatchingItems(@Nullable Item var1, int var2, int var3, @Nullable NBTTagCompound var4) {
      int ☃ = 0;

      for (int ☃x = 0; ☃x < this.getSizeInventory(); ☃x++) {
         ItemStack ☃xx = this.getStackInSlot(☃x);
         if (!☃xx.isEmpty()
            && (☃ == null || ☃xx.getItem() == ☃)
            && (☃ <= -1 || ☃xx.getMetadata() == ☃)
            && (☃ == null || NBTUtil.areNBTEquals(☃, ☃xx.getTagCompound(), true))) {
            int ☃xxx = ☃ <= 0 ? ☃xx.getCount() : Math.min(☃ - ☃, ☃xx.getCount());
            ☃ += ☃xxx;
            if (☃ != 0) {
               ☃xx.shrink(☃xxx);
               if (☃xx.isEmpty()) {
                  this.setInventorySlotContents(☃x, ItemStack.EMPTY);
               }

               if (☃ > 0 && ☃ >= ☃) {
                  return ☃;
               }
            }
         }
      }

      if (!this.itemStack.isEmpty()) {
         if (☃ != null && this.itemStack.getItem() != ☃) {
            return ☃;
         }

         if (☃ > -1 && this.itemStack.getMetadata() != ☃) {
            return ☃;
         }

         if (☃ != null && !NBTUtil.areNBTEquals(☃, this.itemStack.getTagCompound(), true)) {
            return ☃;
         }

         int ☃xx = ☃ <= 0 ? this.itemStack.getCount() : Math.min(☃ - ☃, this.itemStack.getCount());
         ☃ += ☃xx;
         if (☃ != 0) {
            this.itemStack.shrink(☃xx);
            if (this.itemStack.isEmpty()) {
               this.itemStack = ItemStack.EMPTY;
            }

            if (☃ > 0 && ☃ >= ☃) {
               return ☃;
            }
         }
      }

      return ☃;
   }

   private int storePartialItemStack(ItemStack var1) {
      int ☃ = this.storeItemStack(☃);
      if (☃ == -1) {
         ☃ = this.getFirstEmptyStack();
      }

      return ☃ == -1 ? ☃.getCount() : this.addResource(☃, ☃);
   }

   private int addResource(int var1, ItemStack var2) {
      Item ☃ = ☃.getItem();
      int ☃x = ☃.getCount();
      ItemStack ☃xx = this.getStackInSlot(☃);
      if (☃xx.isEmpty()) {
         ☃xx = new ItemStack(☃, 0, ☃.getMetadata());
         if (☃.hasTagCompound()) {
            ☃xx.setTagCompound(☃.getTagCompound().copy());
         }

         this.setInventorySlotContents(☃, ☃xx);
      }

      int ☃xxx = ☃x;
      if (☃x > ☃xx.getMaxStackSize() - ☃xx.getCount()) {
         ☃xxx = ☃xx.getMaxStackSize() - ☃xx.getCount();
      }

      if (☃xxx > this.getInventoryStackLimit() - ☃xx.getCount()) {
         ☃xxx = this.getInventoryStackLimit() - ☃xx.getCount();
      }

      if (☃xxx == 0) {
         return ☃x;
      } else {
         ☃x -= ☃xxx;
         ☃xx.grow(☃xxx);
         ☃xx.setAnimationsToGo(5);
         return ☃x;
      }
   }

   public int storeItemStack(ItemStack var1) {
      if (this.canMergeStacks(this.getStackInSlot(this.currentItem), ☃)) {
         return this.currentItem;
      } else if (this.canMergeStacks(this.getStackInSlot(40), ☃)) {
         return 40;
      } else {
         for (int ☃ = 0; ☃ < this.mainInventory.size(); ☃++) {
            if (this.canMergeStacks(this.mainInventory.get(☃), ☃)) {
               return ☃;
            }
         }

         return -1;
      }
   }

   public void decrementAnimations() {
      for (NonNullList<ItemStack> ☃ : this.allInventories) {
         for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
            if (!☃.get(☃x).isEmpty()) {
               ☃.get(☃x).updateAnimation(this.player.world, this.player, ☃x, this.currentItem == ☃x);
            }
         }
      }
   }

   public boolean addItemStackToInventory(ItemStack var1) {
      return this.add(-1, ☃);
   }

   public boolean add(int var1, final ItemStack var2) {
      if (☃.isEmpty()) {
         return false;
      } else {
         try {
            if (☃.isItemDamaged()) {
               if (☃ == -1) {
                  ☃ = this.getFirstEmptyStack();
               }

               if (☃ >= 0) {
                  this.mainInventory.set(☃, ☃.copy());
                  this.mainInventory.get(☃).setAnimationsToGo(5);
                  ☃.setCount(0);
                  return true;
               } else if (this.player.capabilities.isCreativeMode) {
                  ☃.setCount(0);
                  return true;
               } else {
                  return false;
               }
            } else {
               int ☃;
               do {
                  ☃ = ☃.getCount();
                  if (☃ == -1) {
                     ☃.setCount(this.storePartialItemStack(☃));
                  } else {
                     ☃.setCount(this.addResource(☃, ☃));
                  }
               } while (!☃.isEmpty() && ☃.getCount() < ☃);

               if (☃.getCount() == ☃ && this.player.capabilities.isCreativeMode) {
                  ☃.setCount(0);
                  return true;
               } else {
                  return ☃.getCount() < ☃;
               }
            }
         } catch (Throwable var6) {
            CrashReport ☃x = CrashReport.makeCrashReport(var6, "Adding item to inventory");
            CrashReportCategory ☃xx = ☃x.makeCategory("Item being added");
            ☃xx.addCrashSection("Item ID", Item.getIdFromItem(☃.getItem()));
            ☃xx.addCrashSection("Item data", ☃.getMetadata());
            ☃xx.addDetail("Item name", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  return ☃.getDisplayName();
               }
            });
            throw new ReportedException(☃x);
         }
      }
   }

   public void placeItemBackInInventory(World var1, ItemStack var2) {
      if (!☃.isRemote) {
         while (!☃.isEmpty()) {
            int ☃ = this.storeItemStack(☃);
            if (☃ == -1) {
               ☃ = this.getFirstEmptyStack();
            }

            if (☃ == -1) {
               this.player.dropItem(☃, false);
               break;
            }

            int ☃x = ☃.getMaxStackSize() - this.getStackInSlot(☃).getCount();
            if (this.add(☃, ☃.splitStack(☃x))) {
               ((EntityPlayerMP)this.player).connection.sendPacket(new SPacketSetSlot(-2, ☃, this.getStackInSlot(☃)));
            }
         }
      }
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      List<ItemStack> ☃ = null;

      for (NonNullList<ItemStack> ☃x : this.allInventories) {
         if (☃ < ☃x.size()) {
            ☃ = ☃x;
            break;
         }

         ☃ -= ☃x.size();
      }

      return ☃ != null && !☃.get(☃).isEmpty() ? ItemStackHelper.getAndSplit(☃, ☃, ☃) : ItemStack.EMPTY;
   }

   public void deleteStack(ItemStack var1) {
      for (NonNullList<ItemStack> ☃ : this.allInventories) {
         for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
            if (☃.get(☃x) == ☃) {
               ☃.set(☃x, ItemStack.EMPTY);
               break;
            }
         }
      }
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      NonNullList<ItemStack> ☃ = null;

      for (NonNullList<ItemStack> ☃x : this.allInventories) {
         if (☃ < ☃x.size()) {
            ☃ = ☃x;
            break;
         }

         ☃ -= ☃x.size();
      }

      if (☃ != null && !☃.get(☃).isEmpty()) {
         ItemStack ☃x = ☃.get(☃);
         ☃.set(☃, ItemStack.EMPTY);
         return ☃x;
      } else {
         return ItemStack.EMPTY;
      }
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      NonNullList<ItemStack> ☃ = null;

      for (NonNullList<ItemStack> ☃x : this.allInventories) {
         if (☃ < ☃x.size()) {
            ☃ = ☃x;
            break;
         }

         ☃ -= ☃x.size();
      }

      if (☃ != null) {
         ☃.set(☃, ☃);
      }
   }

   public float getDestroySpeed(IBlockState var1) {
      float ☃ = 1.0F;
      if (!this.mainInventory.get(this.currentItem).isEmpty()) {
         ☃ *= this.mainInventory.get(this.currentItem).getDestroySpeed(☃);
      }

      return ☃;
   }

   public NBTTagList writeToNBT(NBTTagList var1) {
      for (int ☃ = 0; ☃ < this.mainInventory.size(); ☃++) {
         if (!this.mainInventory.get(☃).isEmpty()) {
            NBTTagCompound ☃x = new NBTTagCompound();
            ☃x.setByte("Slot", (byte)☃);
            this.mainInventory.get(☃).writeToNBT(☃x);
            ☃.appendTag(☃x);
         }
      }

      for (int ☃x = 0; ☃x < this.armorInventory.size(); ☃x++) {
         if (!this.armorInventory.get(☃x).isEmpty()) {
            NBTTagCompound ☃xx = new NBTTagCompound();
            ☃xx.setByte("Slot", (byte)(☃x + 100));
            this.armorInventory.get(☃x).writeToNBT(☃xx);
            ☃.appendTag(☃xx);
         }
      }

      for (int ☃xx = 0; ☃xx < this.offHandInventory.size(); ☃xx++) {
         if (!this.offHandInventory.get(☃xx).isEmpty()) {
            NBTTagCompound ☃xxx = new NBTTagCompound();
            ☃xxx.setByte("Slot", (byte)(☃xx + 150));
            this.offHandInventory.get(☃xx).writeToNBT(☃xxx);
            ☃.appendTag(☃xxx);
         }
      }

      return ☃;
   }

   public void readFromNBT(NBTTagList var1) {
      this.mainInventory.clear();
      this.armorInventory.clear();
      this.offHandInventory.clear();

      for (int ☃ = 0; ☃ < ☃.tagCount(); ☃++) {
         NBTTagCompound ☃x = ☃.getCompoundTagAt(☃);
         int ☃xx = ☃x.getByte("Slot") & 255;
         ItemStack ☃xxx = new ItemStack(☃x);
         if (!☃xxx.isEmpty()) {
            if (☃xx >= 0 && ☃xx < this.mainInventory.size()) {
               this.mainInventory.set(☃xx, ☃xxx);
            } else if (☃xx >= 100 && ☃xx < this.armorInventory.size() + 100) {
               this.armorInventory.set(☃xx - 100, ☃xxx);
            } else if (☃xx >= 150 && ☃xx < this.offHandInventory.size() + 150) {
               this.offHandInventory.set(☃xx - 150, ☃xxx);
            }
         }
      }
   }

   @Override
   public int getSizeInventory() {
      return this.mainInventory.size() + this.armorInventory.size() + this.offHandInventory.size();
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.mainInventory) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      for (ItemStack ☃x : this.armorInventory) {
         if (!☃x.isEmpty()) {
            return false;
         }
      }

      for (ItemStack ☃xx : this.offHandInventory) {
         if (!☃xx.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      List<ItemStack> ☃ = null;

      for (NonNullList<ItemStack> ☃x : this.allInventories) {
         if (☃ < ☃x.size()) {
            ☃ = ☃x;
            break;
         }

         ☃ -= ☃x.size();
      }

      return ☃ == null ? ItemStack.EMPTY : ☃.get(☃);
   }

   @Override
   public String getName() {
      return "container.inventory";
   }

   @Override
   public boolean hasCustomName() {
      return false;
   }

   @Override
   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName()));
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean canHarvestBlock(IBlockState var1) {
      if (☃.getMaterial().isToolNotRequired()) {
         return true;
      } else {
         ItemStack ☃ = this.getStackInSlot(this.currentItem);
         return !☃.isEmpty() ? ☃.canHarvestBlock(☃) : false;
      }
   }

   public ItemStack armorItemInSlot(int var1) {
      return this.armorInventory.get(☃);
   }

   public void damageArmor(float var1) {
      ☃ /= 4.0F;
      if (☃ < 1.0F) {
         ☃ = 1.0F;
      }

      for (int ☃ = 0; ☃ < this.armorInventory.size(); ☃++) {
         ItemStack ☃x = this.armorInventory.get(☃);
         if (☃x.getItem() instanceof ItemArmor) {
            ☃x.damageItem((int)☃, this.player);
         }
      }
   }

   public void dropAllItems() {
      for (List<ItemStack> ☃ : this.allInventories) {
         for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
            ItemStack ☃xx = ☃.get(☃x);
            if (!☃xx.isEmpty()) {
               this.player.dropItem(☃xx, true, false);
               ☃.set(☃x, ItemStack.EMPTY);
            }
         }
      }
   }

   @Override
   public void markDirty() {
      this.timesChanged++;
   }

   public int getTimesChanged() {
      return this.timesChanged;
   }

   public void setItemStack(ItemStack var1) {
      this.itemStack = ☃;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   @Override
   public boolean isUsableByPlayer(EntityPlayer var1) {
      return this.player.isDead ? false : !(☃.getDistanceSq(this.player) > 64.0);
   }

   public boolean hasItemStack(ItemStack var1) {
      for (List<ItemStack> ☃ : this.allInventories) {
         for (ItemStack ☃x : ☃) {
            if (!☃x.isEmpty() && ☃x.isItemEqual(☃)) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public void openInventory(EntityPlayer var1) {
   }

   @Override
   public void closeInventory(EntityPlayer var1) {
   }

   @Override
   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      return true;
   }

   public void copyInventory(InventoryPlayer var1) {
      for (int ☃ = 0; ☃ < this.getSizeInventory(); ☃++) {
         this.setInventorySlotContents(☃, ☃.getStackInSlot(☃));
      }

      this.currentItem = ☃.currentItem;
   }

   @Override
   public int getField(int var1) {
      return 0;
   }

   @Override
   public void setField(int var1, int var2) {
   }

   @Override
   public int getFieldCount() {
      return 0;
   }

   @Override
   public void clear() {
      for (List<ItemStack> ☃ : this.allInventories) {
         ☃.clear();
      }
   }

   public void fillStackedContents(RecipeItemHelper var1, boolean var2) {
      for (ItemStack ☃ : this.mainInventory) {
         ☃.accountStack(☃);
      }

      if (☃) {
         ☃.accountStack(this.offHandInventory.get(0));
      }
   }
}
