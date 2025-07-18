package net.minecraft.tileentity;

import java.util.Arrays;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;

public class TileEntityBrewingStand extends TileEntityLockable implements ITickable, ISidedInventory {
   private static final int[] SLOTS_FOR_UP = new int[]{3};
   private static final int[] SLOTS_FOR_DOWN = new int[]{0, 1, 2, 3};
   private static final int[] OUTPUT_SLOTS = new int[]{0, 1, 2, 4};
   private NonNullList<ItemStack> brewingItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);
   private int brewTime;
   private boolean[] filledSlots;
   private Item ingredientID;
   private String customName;
   private int fuel;

   @Override
   public String getName() {
      return this.hasCustomName() ? this.customName : "container.brewing";
   }

   @Override
   public boolean hasCustomName() {
      return this.customName != null && !this.customName.isEmpty();
   }

   public void setName(String var1) {
      this.customName = ☃;
   }

   @Override
   public int getSizeInventory() {
      return this.brewingItemStacks.size();
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.brewingItemStacks) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public void update() {
      ItemStack ☃ = this.brewingItemStacks.get(4);
      if (this.fuel <= 0 && ☃.getItem() == Items.BLAZE_POWDER) {
         this.fuel = 20;
         ☃.shrink(1);
         this.markDirty();
      }

      boolean ☃x = this.canBrew();
      boolean ☃xx = this.brewTime > 0;
      ItemStack ☃xxx = this.brewingItemStacks.get(3);
      if (☃xx) {
         this.brewTime--;
         boolean ☃xxxx = this.brewTime == 0;
         if (☃xxxx && ☃x) {
            this.brewPotions();
            this.markDirty();
         } else if (!☃x) {
            this.brewTime = 0;
            this.markDirty();
         } else if (this.ingredientID != ☃xxx.getItem()) {
            this.brewTime = 0;
            this.markDirty();
         }
      } else if (☃x && this.fuel > 0) {
         this.fuel--;
         this.brewTime = 400;
         this.ingredientID = ☃xxx.getItem();
         this.markDirty();
      }

      if (!this.world.isRemote) {
         boolean[] ☃xxxx = this.createFilledSlotsArray();
         if (!Arrays.equals(☃xxxx, this.filledSlots)) {
            this.filledSlots = ☃xxxx;
            IBlockState ☃xxxxx = this.world.getBlockState(this.getPos());
            if (!(☃xxxxx.getBlock() instanceof BlockBrewingStand)) {
               return;
            }

            for (int ☃xxxxxx = 0; ☃xxxxxx < BlockBrewingStand.HAS_BOTTLE.length; ☃xxxxxx++) {
               ☃xxxxx = ☃xxxxx.withProperty(BlockBrewingStand.HAS_BOTTLE[☃xxxxxx], ☃xxxx[☃xxxxxx]);
            }

            this.world.setBlockState(this.pos, ☃xxxxx, 2);
         }
      }
   }

   public boolean[] createFilledSlotsArray() {
      boolean[] ☃ = new boolean[3];

      for (int ☃x = 0; ☃x < 3; ☃x++) {
         if (!this.brewingItemStacks.get(☃x).isEmpty()) {
            ☃[☃x] = true;
         }
      }

      return ☃;
   }

   private boolean canBrew() {
      ItemStack ☃ = this.brewingItemStacks.get(3);
      if (☃.isEmpty()) {
         return false;
      } else if (!PotionHelper.isReagent(☃)) {
         return false;
      } else {
         for (int ☃x = 0; ☃x < 3; ☃x++) {
            ItemStack ☃xx = this.brewingItemStacks.get(☃x);
            if (!☃xx.isEmpty() && PotionHelper.hasConversions(☃xx, ☃)) {
               return true;
            }
         }

         return false;
      }
   }

   private void brewPotions() {
      ItemStack ☃ = this.brewingItemStacks.get(3);

      for (int ☃x = 0; ☃x < 3; ☃x++) {
         this.brewingItemStacks.set(☃x, PotionHelper.doReaction(☃, this.brewingItemStacks.get(☃x)));
      }

      ☃.shrink(1);
      BlockPos ☃x = this.getPos();
      if (☃.getItem().hasContainerItem()) {
         ItemStack ☃xx = new ItemStack(☃.getItem().getContainerItem());
         if (☃.isEmpty()) {
            ☃ = ☃xx;
         } else {
            InventoryHelper.spawnItemStack(this.world, ☃x.getX(), ☃x.getY(), ☃x.getZ(), ☃xx);
         }
      }

      this.brewingItemStacks.set(3, ☃);
      this.world.playEvent(1035, ☃x, 0);
   }

   public static void registerFixesBrewingStand(DataFixer var0) {
      ☃.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityBrewingStand.class, "Items"));
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.brewingItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
      ItemStackHelper.loadAllItems(☃, this.brewingItemStacks);
      this.brewTime = ☃.getShort("BrewTime");
      if (☃.hasKey("CustomName", 8)) {
         this.customName = ☃.getString("CustomName");
      }

      this.fuel = ☃.getByte("Fuel");
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setShort("BrewTime", (short)this.brewTime);
      ItemStackHelper.saveAllItems(☃, this.brewingItemStacks);
      if (this.hasCustomName()) {
         ☃.setString("CustomName", this.customName);
      }

      ☃.setByte("Fuel", (byte)this.fuel);
      return ☃;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return ☃ >= 0 && ☃ < this.brewingItemStacks.size() ? this.brewingItemStacks.get(☃) : ItemStack.EMPTY;
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      return ItemStackHelper.getAndSplit(this.brewingItemStacks, ☃, ☃);
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      return ItemStackHelper.getAndRemove(this.brewingItemStacks, ☃);
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      if (☃ >= 0 && ☃ < this.brewingItemStacks.size()) {
         this.brewingItemStacks.set(☃, ☃);
      }
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public boolean isUsableByPlayer(EntityPlayer var1) {
      return this.world.getTileEntity(this.pos) != this
         ? false
         : !(☃.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) > 64.0);
   }

   @Override
   public void openInventory(EntityPlayer var1) {
   }

   @Override
   public void closeInventory(EntityPlayer var1) {
   }

   @Override
   public boolean isItemValidForSlot(int var1, ItemStack var2) {
      if (☃ == 3) {
         return PotionHelper.isReagent(☃);
      } else {
         Item ☃ = ☃.getItem();
         return ☃ == 4
            ? ☃ == Items.BLAZE_POWDER
            : (☃ == Items.POTIONITEM || ☃ == Items.SPLASH_POTION || ☃ == Items.LINGERING_POTION || ☃ == Items.GLASS_BOTTLE) && this.getStackInSlot(☃).isEmpty();
      }
   }

   @Override
   public int[] getSlotsForFace(EnumFacing var1) {
      if (☃ == EnumFacing.UP) {
         return SLOTS_FOR_UP;
      } else {
         return ☃ == EnumFacing.DOWN ? SLOTS_FOR_DOWN : OUTPUT_SLOTS;
      }
   }

   @Override
   public boolean canInsertItem(int var1, ItemStack var2, EnumFacing var3) {
      return this.isItemValidForSlot(☃, ☃);
   }

   @Override
   public boolean canExtractItem(int var1, ItemStack var2, EnumFacing var3) {
      return ☃ == 3 ? ☃.getItem() == Items.GLASS_BOTTLE : true;
   }

   @Override
   public String getGuiID() {
      return "minecraft:brewing_stand";
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerBrewingStand(☃, this);
   }

   @Override
   public int getField(int var1) {
      switch (☃) {
         case 0:
            return this.brewTime;
         case 1:
            return this.fuel;
         default:
            return 0;
      }
   }

   @Override
   public void setField(int var1, int var2) {
      switch (☃) {
         case 0:
            this.brewTime = ☃;
            break;
         case 1:
            this.fuel = ☃;
      }
   }

   @Override
   public int getFieldCount() {
      return 2;
   }

   @Override
   public void clear() {
      this.brewingItemStacks.clear();
   }
}
