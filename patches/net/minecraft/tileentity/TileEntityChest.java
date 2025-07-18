package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityChest extends TileEntityLockableLoot implements ITickable {
   private NonNullList<ItemStack> chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
   public boolean adjacentChestChecked;
   public TileEntityChest adjacentChestZNeg;
   public TileEntityChest adjacentChestXPos;
   public TileEntityChest adjacentChestXNeg;
   public TileEntityChest adjacentChestZPos;
   public float lidAngle;
   public float prevLidAngle;
   public int numPlayersUsing;
   private int ticksSinceSync;
   private BlockChest.Type cachedChestType;

   public TileEntityChest() {
   }

   public TileEntityChest(BlockChest.Type var1) {
      this.cachedChestType = ☃;
   }

   @Override
   public int getSizeInventory() {
      return 27;
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.chestContents) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.customName : "container.chest";
   }

   public static void registerFixesChest(DataFixer var0) {
      ☃.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityChest.class, "Items"));
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.chestContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
      if (!this.checkLootAndRead(☃)) {
         ItemStackHelper.loadAllItems(☃, this.chestContents);
      }

      if (☃.hasKey("CustomName", 8)) {
         this.customName = ☃.getString("CustomName");
      }
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      if (!this.checkLootAndWrite(☃)) {
         ItemStackHelper.saveAllItems(☃, this.chestContents);
      }

      if (this.hasCustomName()) {
         ☃.setString("CustomName", this.customName);
      }

      return ☃;
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public void updateContainingBlockInfo() {
      super.updateContainingBlockInfo();
      this.adjacentChestChecked = false;
   }

   private void setNeighbor(TileEntityChest var1, EnumFacing var2) {
      if (☃.isInvalid()) {
         this.adjacentChestChecked = false;
      } else if (this.adjacentChestChecked) {
         switch (☃) {
            case NORTH:
               if (this.adjacentChestZNeg != ☃) {
                  this.adjacentChestChecked = false;
               }
               break;
            case SOUTH:
               if (this.adjacentChestZPos != ☃) {
                  this.adjacentChestChecked = false;
               }
               break;
            case EAST:
               if (this.adjacentChestXPos != ☃) {
                  this.adjacentChestChecked = false;
               }
               break;
            case WEST:
               if (this.adjacentChestXNeg != ☃) {
                  this.adjacentChestChecked = false;
               }
         }
      }
   }

   public void checkForAdjacentChests() {
      if (!this.adjacentChestChecked) {
         this.adjacentChestChecked = true;
         this.adjacentChestXNeg = this.getAdjacentChest(EnumFacing.WEST);
         this.adjacentChestXPos = this.getAdjacentChest(EnumFacing.EAST);
         this.adjacentChestZNeg = this.getAdjacentChest(EnumFacing.NORTH);
         this.adjacentChestZPos = this.getAdjacentChest(EnumFacing.SOUTH);
      }
   }

   @Nullable
   protected TileEntityChest getAdjacentChest(EnumFacing var1) {
      BlockPos ☃ = this.pos.offset(☃);
      if (this.isChestAt(☃)) {
         TileEntity ☃x = this.world.getTileEntity(☃);
         if (☃x instanceof TileEntityChest) {
            TileEntityChest ☃xx = (TileEntityChest)☃x;
            ☃xx.setNeighbor(this, ☃.getOpposite());
            return ☃xx;
         }
      }

      return null;
   }

   private boolean isChestAt(BlockPos var1) {
      if (this.world == null) {
         return false;
      } else {
         Block ☃ = this.world.getBlockState(☃).getBlock();
         return ☃ instanceof BlockChest && ((BlockChest)☃).chestType == this.getChestType();
      }
   }

   @Override
   public void update() {
      this.checkForAdjacentChests();
      int ☃ = this.pos.getX();
      int ☃x = this.pos.getY();
      int ☃xx = this.pos.getZ();
      this.ticksSinceSync++;
      if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + ☃ + ☃x + ☃xx) % 200 == 0) {
         this.numPlayersUsing = 0;
         float ☃xxx = 5.0F;

         for (EntityPlayer ☃xxxx : this.world
            .getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(☃ - 5.0F, ☃x - 5.0F, ☃xx - 5.0F, ☃ + 1 + 5.0F, ☃x + 1 + 5.0F, ☃xx + 1 + 5.0F))) {
            if (☃xxxx.openContainer instanceof ContainerChest) {
               IInventory ☃xxxxx = ((ContainerChest)☃xxxx.openContainer).getLowerChestInventory();
               if (☃xxxxx == this || ☃xxxxx instanceof InventoryLargeChest && ((InventoryLargeChest)☃xxxxx).isPartOfLargeChest(this)) {
                  this.numPlayersUsing++;
               }
            }
         }
      }

      this.prevLidAngle = this.lidAngle;
      float ☃xxx = 0.1F;
      if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
         double ☃xxxxx = ☃ + 0.5;
         double ☃xxxxxx = ☃xx + 0.5;
         if (this.adjacentChestZPos != null) {
            ☃xxxxxx += 0.5;
         }

         if (this.adjacentChestXPos != null) {
            ☃xxxxx += 0.5;
         }

         this.world
            .playSound(null, ☃xxxxx, ☃x + 0.5, ☃xxxxxx, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
      }

      if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
         float ☃xxxxxxx = this.lidAngle;
         if (this.numPlayersUsing > 0) {
            this.lidAngle += 0.1F;
         } else {
            this.lidAngle -= 0.1F;
         }

         if (this.lidAngle > 1.0F) {
            this.lidAngle = 1.0F;
         }

         float ☃xxxxxxxx = 0.5F;
         if (this.lidAngle < 0.5F && ☃xxxxxxx >= 0.5F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
            double ☃xxxxxxxxx = ☃ + 0.5;
            double ☃xxxxxxxxxx = ☃xx + 0.5;
            if (this.adjacentChestZPos != null) {
               ☃xxxxxxxxxx += 0.5;
            }

            if (this.adjacentChestXPos != null) {
               ☃xxxxxxxxx += 0.5;
            }

            this.world
               .playSound(
                  null, ☃xxxxxxxxx, ☃x + 0.5, ☃xxxxxxxxxx, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F
               );
         }

         if (this.lidAngle < 0.0F) {
            this.lidAngle = 0.0F;
         }
      }
   }

   @Override
   public boolean receiveClientEvent(int var1, int var2) {
      if (☃ == 1) {
         this.numPlayersUsing = ☃;
         return true;
      } else {
         return super.receiveClientEvent(☃, ☃);
      }
   }

   @Override
   public void openInventory(EntityPlayer var1) {
      if (!☃.isSpectator()) {
         if (this.numPlayersUsing < 0) {
            this.numPlayersUsing = 0;
         }

         this.numPlayersUsing++;
         this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
         this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
         if (this.getChestType() == BlockChest.Type.TRAP) {
            this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
         }
      }
   }

   @Override
   public void closeInventory(EntityPlayer var1) {
      if (!☃.isSpectator() && this.getBlockType() instanceof BlockChest) {
         this.numPlayersUsing--;
         this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
         this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
         if (this.getChestType() == BlockChest.Type.TRAP) {
            this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
         }
      }
   }

   @Override
   public void invalidate() {
      super.invalidate();
      this.updateContainingBlockInfo();
      this.checkForAdjacentChests();
   }

   public BlockChest.Type getChestType() {
      if (this.cachedChestType == null) {
         if (this.world == null || !(this.getBlockType() instanceof BlockChest)) {
            return BlockChest.Type.BASIC;
         }

         this.cachedChestType = ((BlockChest)this.getBlockType()).chestType;
      }

      return this.cachedChestType;
   }

   @Override
   public String getGuiID() {
      return "minecraft:chest";
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      this.fillWithLoot(☃);
      return new ContainerChest(☃, this, ☃);
   }

   @Override
   protected NonNullList<ItemStack> getItems() {
      return this.chestContents;
   }
}
