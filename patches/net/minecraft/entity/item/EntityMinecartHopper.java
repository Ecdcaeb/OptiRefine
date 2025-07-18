package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper {
   private boolean isBlocked = true;
   private int transferTicker = -1;
   private final BlockPos lastPosition = BlockPos.ORIGIN;

   public EntityMinecartHopper(World var1) {
      super(☃);
   }

   public EntityMinecartHopper(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   @Override
   public EntityMinecart.Type getType() {
      return EntityMinecart.Type.HOPPER;
   }

   @Override
   public IBlockState getDefaultDisplayTile() {
      return Blocks.HOPPER.getDefaultState();
   }

   @Override
   public int getDefaultDisplayTileOffset() {
      return 1;
   }

   @Override
   public int getSizeInventory() {
      return 5;
   }

   @Override
   public boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      if (!this.world.isRemote) {
         ☃.displayGUIChest(this);
      }

      return true;
   }

   @Override
   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
      boolean ☃ = !☃;
      if (☃ != this.getBlocked()) {
         this.setBlocked(☃);
      }
   }

   public boolean getBlocked() {
      return this.isBlocked;
   }

   public void setBlocked(boolean var1) {
      this.isBlocked = ☃;
   }

   @Override
   public World getWorld() {
      return this.world;
   }

   @Override
   public double getXPos() {
      return this.posX;
   }

   @Override
   public double getYPos() {
      return this.posY + 0.5;
   }

   @Override
   public double getZPos() {
      return this.posZ;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (!this.world.isRemote && this.isEntityAlive() && this.getBlocked()) {
         BlockPos ☃ = new BlockPos(this);
         if (☃.equals(this.lastPosition)) {
            this.transferTicker--;
         } else {
            this.setTransferTicker(0);
         }

         if (!this.canTransfer()) {
            this.setTransferTicker(0);
            if (this.captureDroppedItems()) {
               this.setTransferTicker(4);
               this.markDirty();
            }
         }
      }
   }

   public boolean captureDroppedItems() {
      if (TileEntityHopper.pullItems(this)) {
         return true;
      } else {
         List<EntityItem> ☃ = this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(0.25, 0.0, 0.25), EntitySelectors.IS_ALIVE);
         if (!☃.isEmpty()) {
            TileEntityHopper.putDropInInventoryAllSlots(null, this, ☃.get(0));
         }

         return false;
      }
   }

   @Override
   public void killMinecart(DamageSource var1) {
      super.killMinecart(☃);
      if (this.world.getGameRules().getBoolean("doEntityDrops")) {
         this.dropItemWithOffset(Item.getItemFromBlock(Blocks.HOPPER), 1, 0.0F);
      }
   }

   public static void registerFixesMinecartHopper(DataFixer var0) {
      EntityMinecartContainer.addDataFixers(☃, EntityMinecartHopper.class);
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("TransferCooldown", this.transferTicker);
      ☃.setBoolean("Enabled", this.isBlocked);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.transferTicker = ☃.getInteger("TransferCooldown");
      this.isBlocked = ☃.hasKey("Enabled") ? ☃.getBoolean("Enabled") : true;
   }

   public void setTransferTicker(int var1) {
      this.transferTicker = ☃;
   }

   public boolean canTransfer() {
      return this.transferTicker > 0;
   }

   @Override
   public String getGuiID() {
      return "minecraft:hopper";
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerHopper(☃, this, ☃);
   }
}
