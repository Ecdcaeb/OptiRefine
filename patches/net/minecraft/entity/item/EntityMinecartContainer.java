package net.minecraft.entity.item;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer, ILootContainer {
   private NonNullList<ItemStack> minecartContainerItems = NonNullList.withSize(36, ItemStack.EMPTY);
   private boolean dropContentsWhenDead = true;
   private ResourceLocation lootTable;
   private long lootTableSeed;

   public EntityMinecartContainer(World var1) {
      super(☃);
   }

   public EntityMinecartContainer(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   @Override
   public void killMinecart(DamageSource var1) {
      super.killMinecart(☃);
      if (this.world.getGameRules().getBoolean("doEntityDrops")) {
         InventoryHelper.dropInventoryItems(this.world, this, this);
      }
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.minecartContainerItems) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      this.addLoot(null);
      return this.minecartContainerItems.get(☃);
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      this.addLoot(null);
      return ItemStackHelper.getAndSplit(this.minecartContainerItems, ☃, ☃);
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      this.addLoot(null);
      ItemStack ☃ = this.minecartContainerItems.get(☃);
      if (☃.isEmpty()) {
         return ItemStack.EMPTY;
      } else {
         this.minecartContainerItems.set(☃, ItemStack.EMPTY);
         return ☃;
      }
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.addLoot(null);
      this.minecartContainerItems.set(☃, ☃);
      if (!☃.isEmpty() && ☃.getCount() > this.getInventoryStackLimit()) {
         ☃.setCount(this.getInventoryStackLimit());
      }
   }

   @Override
   public void markDirty() {
   }

   @Override
   public boolean isUsableByPlayer(EntityPlayer var1) {
      return this.isDead ? false : !(☃.getDistanceSq(this) > 64.0);
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

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Nullable
   @Override
   public Entity changeDimension(int var1) {
      this.dropContentsWhenDead = false;
      return super.changeDimension(☃);
   }

   @Override
   public void setDead() {
      if (this.dropContentsWhenDead) {
         InventoryHelper.dropInventoryItems(this.world, this, this);
      }

      super.setDead();
   }

   @Override
   public void setDropItemsWhenDead(boolean var1) {
      this.dropContentsWhenDead = ☃;
   }

   public static void addDataFixers(DataFixer var0, Class<?> var1) {
      EntityMinecart.registerFixesMinecart(☃, ☃);
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(☃, "Items"));
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      if (this.lootTable != null) {
         ☃.setString("LootTable", this.lootTable.toString());
         if (this.lootTableSeed != 0L) {
            ☃.setLong("LootTableSeed", this.lootTableSeed);
         }
      } else {
         ItemStackHelper.saveAllItems(☃, this.minecartContainerItems);
      }
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.minecartContainerItems = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
      if (☃.hasKey("LootTable", 8)) {
         this.lootTable = new ResourceLocation(☃.getString("LootTable"));
         this.lootTableSeed = ☃.getLong("LootTableSeed");
      } else {
         ItemStackHelper.loadAllItems(☃, this.minecartContainerItems);
      }
   }

   @Override
   public boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      if (!this.world.isRemote) {
         ☃.displayGUIChest(this);
      }

      return true;
   }

   @Override
   protected void applyDrag() {
      float ☃ = 0.98F;
      if (this.lootTable == null) {
         int ☃x = 15 - Container.calcRedstoneFromInventory(this);
         ☃ += ☃x * 0.001F;
      }

      this.motionX *= ☃;
      this.motionY *= 0.0;
      this.motionZ *= ☃;
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
   public boolean isLocked() {
      return false;
   }

   @Override
   public void setLockCode(LockCode var1) {
   }

   @Override
   public LockCode getLockCode() {
      return LockCode.EMPTY_CODE;
   }

   public void addLoot(@Nullable EntityPlayer var1) {
      if (this.lootTable != null) {
         LootTable ☃ = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
         this.lootTable = null;
         Random ☃x;
         if (this.lootTableSeed == 0L) {
            ☃x = new Random();
         } else {
            ☃x = new Random(this.lootTableSeed);
         }

         LootContext.Builder ☃xx = new LootContext.Builder((WorldServer)this.world);
         if (☃ != null) {
            ☃xx.withLuck(☃.getLuck());
         }

         ☃.fillInventory(this, ☃x, ☃xx.build());
      }
   }

   @Override
   public void clear() {
      this.addLoot(null);
      this.minecartContainerItems.clear();
   }

   public void setLootTable(ResourceLocation var1, long var2) {
      this.lootTable = ☃;
      this.lootTableSeed = ☃;
   }

   @Override
   public ResourceLocation getLootTable() {
      return this.lootTable;
   }
}
