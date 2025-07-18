package net.minecraft.tileentity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityShulkerBox extends TileEntityLockableLoot implements ITickable, ISidedInventory {
   private static final int[] SLOTS = new int[27];
   private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
   private boolean hasBeenCleared;
   private int openCount;
   private TileEntityShulkerBox.AnimationStatus animationStatus = TileEntityShulkerBox.AnimationStatus.CLOSED;
   private float progress;
   private float progressOld;
   private EnumDyeColor color;
   private boolean destroyedByCreativePlayer;

   public TileEntityShulkerBox() {
      this(null);
   }

   public TileEntityShulkerBox(@Nullable EnumDyeColor var1) {
      this.color = ☃;
   }

   @Override
   public void update() {
      this.updateAnimation();
      if (this.animationStatus == TileEntityShulkerBox.AnimationStatus.OPENING || this.animationStatus == TileEntityShulkerBox.AnimationStatus.CLOSING) {
         this.moveCollidedEntities();
      }
   }

   protected void updateAnimation() {
      this.progressOld = this.progress;
      switch (this.animationStatus) {
         case CLOSED:
            this.progress = 0.0F;
            break;
         case OPENING:
            this.progress += 0.1F;
            if (this.progress >= 1.0F) {
               this.moveCollidedEntities();
               this.animationStatus = TileEntityShulkerBox.AnimationStatus.OPENED;
               this.progress = 1.0F;
            }
            break;
         case CLOSING:
            this.progress -= 0.1F;
            if (this.progress <= 0.0F) {
               this.animationStatus = TileEntityShulkerBox.AnimationStatus.CLOSED;
               this.progress = 0.0F;
            }
            break;
         case OPENED:
            this.progress = 1.0F;
      }
   }

   public TileEntityShulkerBox.AnimationStatus getAnimationStatus() {
      return this.animationStatus;
   }

   public AxisAlignedBB getBoundingBox(IBlockState var1) {
      return this.getBoundingBox(☃.getValue(BlockShulkerBox.FACING));
   }

   public AxisAlignedBB getBoundingBox(EnumFacing var1) {
      return Block.FULL_BLOCK_AABB
         .expand(0.5F * this.getProgress(1.0F) * ☃.getXOffset(), 0.5F * this.getProgress(1.0F) * ☃.getYOffset(), 0.5F * this.getProgress(1.0F) * ☃.getZOffset());
   }

   private AxisAlignedBB getTopBoundingBox(EnumFacing var1) {
      EnumFacing ☃ = ☃.getOpposite();
      return this.getBoundingBox(☃).contract(☃.getXOffset(), ☃.getYOffset(), ☃.getZOffset());
   }

   private void moveCollidedEntities() {
      IBlockState ☃ = this.world.getBlockState(this.getPos());
      if (☃.getBlock() instanceof BlockShulkerBox) {
         EnumFacing ☃x = ☃.getValue(BlockShulkerBox.FACING);
         AxisAlignedBB ☃xx = this.getTopBoundingBox(☃x).offset(this.pos);
         List<Entity> ☃xxx = this.world.getEntitiesWithinAABBExcludingEntity(null, ☃xx);
         if (!☃xxx.isEmpty()) {
            for (int ☃xxxx = 0; ☃xxxx < ☃xxx.size(); ☃xxxx++) {
               Entity ☃xxxxx = ☃xxx.get(☃xxxx);
               if (☃xxxxx.getPushReaction() != EnumPushReaction.IGNORE) {
                  double ☃xxxxxx = 0.0;
                  double ☃xxxxxxx = 0.0;
                  double ☃xxxxxxxx = 0.0;
                  AxisAlignedBB ☃xxxxxxxxx = ☃xxxxx.getEntityBoundingBox();
                  switch (☃x.getAxis()) {
                     case X:
                        if (☃x.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                           ☃xxxxxx = ☃xx.maxX - ☃xxxxxxxxx.minX;
                        } else {
                           ☃xxxxxx = ☃xxxxxxxxx.maxX - ☃xx.minX;
                        }

                        ☃xxxxxx += 0.01;
                        break;
                     case Y:
                        if (☃x.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                           ☃xxxxxxx = ☃xx.maxY - ☃xxxxxxxxx.minY;
                        } else {
                           ☃xxxxxxx = ☃xxxxxxxxx.maxY - ☃xx.minY;
                        }

                        ☃xxxxxxx += 0.01;
                        break;
                     case Z:
                        if (☃x.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                           ☃xxxxxxxx = ☃xx.maxZ - ☃xxxxxxxxx.minZ;
                        } else {
                           ☃xxxxxxxx = ☃xxxxxxxxx.maxZ - ☃xx.minZ;
                        }

                        ☃xxxxxxxx += 0.01;
                  }

                  ☃xxxxx.move(MoverType.SHULKER_BOX, ☃xxxxxx * ☃x.getXOffset(), ☃xxxxxxx * ☃x.getYOffset(), ☃xxxxxxxx * ☃x.getZOffset());
               }
            }
         }
      }
   }

   @Override
   public int getSizeInventory() {
      return this.items.size();
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public boolean receiveClientEvent(int var1, int var2) {
      if (☃ == 1) {
         this.openCount = ☃;
         if (☃ == 0) {
            this.animationStatus = TileEntityShulkerBox.AnimationStatus.CLOSING;
         }

         if (☃ == 1) {
            this.animationStatus = TileEntityShulkerBox.AnimationStatus.OPENING;
         }

         return true;
      } else {
         return super.receiveClientEvent(☃, ☃);
      }
   }

   @Override
   public void openInventory(EntityPlayer var1) {
      if (!☃.isSpectator()) {
         if (this.openCount < 0) {
            this.openCount = 0;
         }

         this.openCount++;
         this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.openCount);
         if (this.openCount == 1) {
            this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
         }
      }
   }

   @Override
   public void closeInventory(EntityPlayer var1) {
      if (!☃.isSpectator()) {
         this.openCount--;
         this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.openCount);
         if (this.openCount <= 0) {
            this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
         }
      }
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerShulkerBox(☃, this, ☃);
   }

   @Override
   public String getGuiID() {
      return "minecraft:shulker_box";
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.customName : "container.shulkerBox";
   }

   public static void registerFixesShulkerBox(DataFixer var0) {
      ☃.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityShulkerBox.class, "Items"));
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.loadFromNbt(☃);
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      return this.saveToNbt(☃);
   }

   public void loadFromNbt(NBTTagCompound var1) {
      this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
      if (!this.checkLootAndRead(☃) && ☃.hasKey("Items", 9)) {
         ItemStackHelper.loadAllItems(☃, this.items);
      }

      if (☃.hasKey("CustomName", 8)) {
         this.customName = ☃.getString("CustomName");
      }
   }

   public NBTTagCompound saveToNbt(NBTTagCompound var1) {
      if (!this.checkLootAndWrite(☃)) {
         ItemStackHelper.saveAllItems(☃, this.items, false);
      }

      if (this.hasCustomName()) {
         ☃.setString("CustomName", this.customName);
      }

      if (!☃.hasKey("Lock") && this.isLocked()) {
         this.getLockCode().toNBT(☃);
      }

      return ☃;
   }

   @Override
   protected NonNullList<ItemStack> getItems() {
      return this.items;
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack ☃ : this.items) {
         if (!☃.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public int[] getSlotsForFace(EnumFacing var1) {
      return SLOTS;
   }

   @Override
   public boolean canInsertItem(int var1, ItemStack var2, EnumFacing var3) {
      return !(Block.getBlockFromItem(☃.getItem()) instanceof BlockShulkerBox);
   }

   @Override
   public boolean canExtractItem(int var1, ItemStack var2, EnumFacing var3) {
      return true;
   }

   @Override
   public void clear() {
      this.hasBeenCleared = true;
      super.clear();
   }

   public boolean isCleared() {
      return this.hasBeenCleared;
   }

   public float getProgress(float var1) {
      return this.progressOld + (this.progress - this.progressOld) * ☃;
   }

   public EnumDyeColor getColor() {
      if (this.color == null) {
         this.color = BlockShulkerBox.getColorFromBlock(this.getBlockType());
      }

      return this.color;
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 10, this.getUpdateTag());
   }

   public boolean isDestroyedByCreativePlayer() {
      return this.destroyedByCreativePlayer;
   }

   public void setDestroyedByCreativePlayer(boolean var1) {
      this.destroyedByCreativePlayer = ☃;
   }

   public boolean shouldDrop() {
      return !this.isDestroyedByCreativePlayer() || !this.isEmpty() || this.hasCustomName() || this.lootTable != null;
   }

   static {
      int ☃ = 0;

      while (☃ < SLOTS.length) {
         SLOTS[☃] = ☃++;
      }
   }

   public static enum AnimationStatus {
      CLOSED,
      OPENING,
      OPENED,
      CLOSING;
   }
}
