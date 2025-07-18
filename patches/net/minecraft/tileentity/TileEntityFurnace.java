package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;

public class TileEntityFurnace extends TileEntityLockable implements ITickable, ISidedInventory {
   private static final int[] SLOTS_TOP = new int[]{0};
   private static final int[] SLOTS_BOTTOM = new int[]{2, 1};
   private static final int[] SLOTS_SIDES = new int[]{1};
   private NonNullList<ItemStack> furnaceItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
   private int furnaceBurnTime;
   private int currentItemBurnTime;
   private int cookTime;
   private int totalCookTime;
   private String furnaceCustomName;

   @Override
   public int getSizeInventory() {
      return this.furnaceItemStacks.size();
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.furnaceItemStacks) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return this.furnaceItemStacks.get(☃);
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      return ItemStackHelper.getAndSplit(this.furnaceItemStacks, ☃, ☃);
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      return ItemStackHelper.getAndRemove(this.furnaceItemStacks, ☃);
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      ItemStack ☃ = this.furnaceItemStacks.get(☃);
      boolean ☃x = !☃.isEmpty() && ☃.isItemEqual(☃) && ItemStack.areItemStackTagsEqual(☃, ☃);
      this.furnaceItemStacks.set(☃, ☃);
      if (☃.getCount() > this.getInventoryStackLimit()) {
         ☃.setCount(this.getInventoryStackLimit());
      }

      if (☃ == 0 && !☃x) {
         this.totalCookTime = this.getCookTime(☃);
         this.cookTime = 0;
         this.markDirty();
      }
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
   }

   @Override
   public boolean hasCustomName() {
      return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
   }

   public void setCustomInventoryName(String var1) {
      this.furnaceCustomName = ☃;
   }

   public static void registerFixesFurnace(DataFixer var0) {
      ☃.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityFurnace.class, "Items"));
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.furnaceItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
      ItemStackHelper.loadAllItems(☃, this.furnaceItemStacks);
      this.furnaceBurnTime = ☃.getShort("BurnTime");
      this.cookTime = ☃.getShort("CookTime");
      this.totalCookTime = ☃.getShort("CookTimeTotal");
      this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks.get(1));
      if (☃.hasKey("CustomName", 8)) {
         this.furnaceCustomName = ☃.getString("CustomName");
      }
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setShort("BurnTime", (short)this.furnaceBurnTime);
      ☃.setShort("CookTime", (short)this.cookTime);
      ☃.setShort("CookTimeTotal", (short)this.totalCookTime);
      ItemStackHelper.saveAllItems(☃, this.furnaceItemStacks);
      if (this.hasCustomName()) {
         ☃.setString("CustomName", this.furnaceCustomName);
      }

      return ☃;
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isBurning() {
      return this.furnaceBurnTime > 0;
   }

   public static boolean isBurning(IInventory var0) {
      return ☃.getField(0) > 0;
   }

   @Override
   public void update() {
      boolean ☃ = this.isBurning();
      boolean ☃x = false;
      if (this.isBurning()) {
         this.furnaceBurnTime--;
      }

      if (!this.world.isRemote) {
         ItemStack ☃xx = this.furnaceItemStacks.get(1);
         if (this.isBurning() || !☃xx.isEmpty() && !this.furnaceItemStacks.get(0).isEmpty()) {
            if (!this.isBurning() && this.canSmelt()) {
               this.furnaceBurnTime = getItemBurnTime(☃xx);
               this.currentItemBurnTime = this.furnaceBurnTime;
               if (this.isBurning()) {
                  ☃x = true;
                  if (!☃xx.isEmpty()) {
                     Item ☃xxx = ☃xx.getItem();
                     ☃xx.shrink(1);
                     if (☃xx.isEmpty()) {
                        Item ☃xxxx = ☃xxx.getContainerItem();
                        this.furnaceItemStacks.set(1, ☃xxxx == null ? ItemStack.EMPTY : new ItemStack(☃xxxx));
                     }
                  }
               }
            }

            if (this.isBurning() && this.canSmelt()) {
               this.cookTime++;
               if (this.cookTime == this.totalCookTime) {
                  this.cookTime = 0;
                  this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
                  this.smeltItem();
                  ☃x = true;
               }
            } else {
               this.cookTime = 0;
            }
         } else if (!this.isBurning() && this.cookTime > 0) {
            this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
         }

         if (☃ != this.isBurning()) {
            ☃x = true;
            BlockFurnace.setState(this.isBurning(), this.world, this.pos);
         }
      }

      if (☃x) {
         this.markDirty();
      }
   }

   public int getCookTime(ItemStack var1) {
      return 200;
   }

   private boolean canSmelt() {
      if (this.furnaceItemStacks.get(0).isEmpty()) {
         return false;
      } else {
         ItemStack ☃ = FurnaceRecipes.instance().getSmeltingResult(this.furnaceItemStacks.get(0));
         if (☃.isEmpty()) {
            return false;
         } else {
            ItemStack ☃x = this.furnaceItemStacks.get(2);
            if (☃x.isEmpty()) {
               return true;
            } else if (!☃x.isItemEqual(☃)) {
               return false;
            } else {
               return ☃x.getCount() < this.getInventoryStackLimit() && ☃x.getCount() < ☃x.getMaxStackSize() ? true : ☃x.getCount() < ☃.getMaxStackSize();
            }
         }
      }
   }

   public void smeltItem() {
      if (this.canSmelt()) {
         ItemStack ☃ = this.furnaceItemStacks.get(0);
         ItemStack ☃x = FurnaceRecipes.instance().getSmeltingResult(☃);
         ItemStack ☃xx = this.furnaceItemStacks.get(2);
         if (☃xx.isEmpty()) {
            this.furnaceItemStacks.set(2, ☃x.copy());
         } else if (☃xx.getItem() == ☃x.getItem()) {
            ☃xx.grow(1);
         }

         if (☃.getItem() == Item.getItemFromBlock(Blocks.SPONGE)
            && ☃.getMetadata() == 1
            && !this.furnaceItemStacks.get(1).isEmpty()
            && this.furnaceItemStacks.get(1).getItem() == Items.BUCKET) {
            this.furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
         }

         ☃.shrink(1);
      }
   }

   public static int getItemBurnTime(ItemStack var0) {
      if (☃.isEmpty()) {
         return 0;
      } else {
         Item ☃ = ☃.getItem();
         if (☃ == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) {
            return 150;
         } else if (☃ == Item.getItemFromBlock(Blocks.WOOL)) {
            return 100;
         } else if (☃ == Item.getItemFromBlock(Blocks.CARPET)) {
            return 67;
         } else if (☃ == Item.getItemFromBlock(Blocks.LADDER)) {
            return 300;
         } else if (☃ == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) {
            return 100;
         } else if (Block.getBlockFromItem(☃).getDefaultState().getMaterial() == Material.WOOD) {
            return 300;
         } else if (☃ == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
            return 16000;
         } else if (☃ instanceof ItemTool && "WOOD".equals(((ItemTool)☃).getToolMaterialName())) {
            return 200;
         } else if (☃ instanceof ItemSword && "WOOD".equals(((ItemSword)☃).getToolMaterialName())) {
            return 200;
         } else if (☃ instanceof ItemHoe && "WOOD".equals(((ItemHoe)☃).getMaterialName())) {
            return 200;
         } else if (☃ == Items.STICK) {
            return 100;
         } else if (☃ == Items.BOW || ☃ == Items.FISHING_ROD) {
            return 300;
         } else if (☃ == Items.SIGN) {
            return 200;
         } else if (☃ == Items.COAL) {
            return 1600;
         } else if (☃ == Items.LAVA_BUCKET) {
            return 20000;
         } else if (☃ == Item.getItemFromBlock(Blocks.SAPLING) || ☃ == Items.BOWL) {
            return 100;
         } else if (☃ == Items.BLAZE_ROD) {
            return 2400;
         } else if (☃ instanceof ItemDoor && ☃ != Items.IRON_DOOR) {
            return 200;
         } else {
            return ☃ instanceof ItemBoat ? 400 : 0;
         }
      }
   }

   public static boolean isItemFuel(ItemStack var0) {
      return getItemBurnTime(☃) > 0;
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
      if (☃ == 2) {
         return false;
      } else if (☃ != 1) {
         return true;
      } else {
         ItemStack ☃ = this.furnaceItemStacks.get(1);
         return isItemFuel(☃) || SlotFurnaceFuel.isBucket(☃) && ☃.getItem() != Items.BUCKET;
      }
   }

   @Override
   public int[] getSlotsForFace(EnumFacing var1) {
      if (☃ == EnumFacing.DOWN) {
         return SLOTS_BOTTOM;
      } else {
         return ☃ == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
      }
   }

   @Override
   public boolean canInsertItem(int var1, ItemStack var2, EnumFacing var3) {
      return this.isItemValidForSlot(☃, ☃);
   }

   @Override
   public boolean canExtractItem(int var1, ItemStack var2, EnumFacing var3) {
      if (☃ == EnumFacing.DOWN && ☃ == 1) {
         Item ☃ = ☃.getItem();
         if (☃ != Items.WATER_BUCKET && ☃ != Items.BUCKET) {
            return false;
         }
      }

      return true;
   }

   @Override
   public String getGuiID() {
      return "minecraft:furnace";
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerFurnace(☃, this);
   }

   @Override
   public int getField(int var1) {
      switch (☃) {
         case 0:
            return this.furnaceBurnTime;
         case 1:
            return this.currentItemBurnTime;
         case 2:
            return this.cookTime;
         case 3:
            return this.totalCookTime;
         default:
            return 0;
      }
   }

   @Override
   public void setField(int var1, int var2) {
      switch (☃) {
         case 0:
            this.furnaceBurnTime = ☃;
            break;
         case 1:
            this.currentItemBurnTime = ☃;
            break;
         case 2:
            this.cookTime = ☃;
            break;
         case 3:
            this.totalCookTime = ☃;
      }
   }

   @Override
   public int getFieldCount() {
      return 4;
   }

   @Override
   public void clear() {
      this.furnaceItemStacks.clear();
   }
}
