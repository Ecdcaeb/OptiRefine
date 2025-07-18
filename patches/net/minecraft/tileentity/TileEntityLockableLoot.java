package net.minecraft.tileentity;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class TileEntityLockableLoot extends TileEntityLockable implements ILootContainer {
   protected ResourceLocation lootTable;
   protected long lootTableSeed;
   protected String customName;

   protected boolean checkLootAndRead(NBTTagCompound var1) {
      if (☃.hasKey("LootTable", 8)) {
         this.lootTable = new ResourceLocation(☃.getString("LootTable"));
         this.lootTableSeed = ☃.getLong("LootTableSeed");
         return true;
      } else {
         return false;
      }
   }

   protected boolean checkLootAndWrite(NBTTagCompound var1) {
      if (this.lootTable != null) {
         ☃.setString("LootTable", this.lootTable.toString());
         if (this.lootTableSeed != 0L) {
            ☃.setLong("LootTableSeed", this.lootTableSeed);
         }

         return true;
      } else {
         return false;
      }
   }

   public void fillWithLoot(@Nullable EntityPlayer var1) {
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
   public ResourceLocation getLootTable() {
      return this.lootTable;
   }

   public void setLootTable(ResourceLocation var1, long var2) {
      this.lootTable = ☃;
      this.lootTableSeed = ☃;
   }

   @Override
   public boolean hasCustomName() {
      return this.customName != null && !this.customName.isEmpty();
   }

   public void setCustomName(String var1) {
      this.customName = ☃;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      this.fillWithLoot(null);
      return this.getItems().get(☃);
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      this.fillWithLoot(null);
      ItemStack ☃ = ItemStackHelper.getAndSplit(this.getItems(), ☃, ☃);
      if (!☃.isEmpty()) {
         this.markDirty();
      }

      return ☃;
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      this.fillWithLoot(null);
      return ItemStackHelper.getAndRemove(this.getItems(), ☃);
   }

   @Override
   public void setInventorySlotContents(int var1, @Nullable ItemStack var2) {
      this.fillWithLoot(null);
      this.getItems().set(☃, ☃);
      if (☃.getCount() > this.getInventoryStackLimit()) {
         ☃.setCount(this.getInventoryStackLimit());
      }

      this.markDirty();
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
      return true;
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
      this.fillWithLoot(null);
      this.getItems().clear();
   }

   protected abstract NonNullList<ItemStack> getItems();
}
