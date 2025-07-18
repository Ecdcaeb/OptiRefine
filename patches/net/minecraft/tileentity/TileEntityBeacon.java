package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityBeacon extends TileEntityLockable implements ITickable, ISidedInventory {
   public static final Potion[][] EFFECTS_LIST = new Potion[][]{
      {MobEffects.SPEED, MobEffects.HASTE}, {MobEffects.RESISTANCE, MobEffects.JUMP_BOOST}, {MobEffects.STRENGTH}, {MobEffects.REGENERATION}
   };
   private static final Set<Potion> VALID_EFFECTS = Sets.newHashSet();
   private final List<TileEntityBeacon.BeamSegment> beamSegments = Lists.newArrayList();
   private long beamRenderCounter;
   private float beamRenderScale;
   private boolean isComplete;
   private int levels = -1;
   @Nullable
   private Potion primaryEffect;
   @Nullable
   private Potion secondaryEffect;
   private ItemStack payment = ItemStack.EMPTY;
   private String customName;

   @Override
   public void update() {
      if (this.world.getTotalWorldTime() % 80L == 0L) {
         this.updateBeacon();
      }
   }

   public void updateBeacon() {
      if (this.world != null) {
         this.updateSegmentColors();
         this.addEffectsToPlayers();
      }
   }

   private void addEffectsToPlayers() {
      if (this.isComplete && this.levels > 0 && !this.world.isRemote && this.primaryEffect != null) {
         double ☃ = this.levels * 10 + 10;
         int ☃x = 0;
         if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
            ☃x = 1;
         }

         int ☃xx = (9 + this.levels * 2) * 20;
         int ☃xxx = this.pos.getX();
         int ☃xxxx = this.pos.getY();
         int ☃xxxxx = this.pos.getZ();
         AxisAlignedBB ☃xxxxxx = new AxisAlignedBB(☃xxx, ☃xxxx, ☃xxxxx, ☃xxx + 1, ☃xxxx + 1, ☃xxxxx + 1).grow(☃).expand(0.0, this.world.getHeight(), 0.0);
         List<EntityPlayer> ☃xxxxxxx = this.world.getEntitiesWithinAABB(EntityPlayer.class, ☃xxxxxx);

         for (EntityPlayer ☃xxxxxxxx : ☃xxxxxxx) {
            ☃xxxxxxxx.addPotionEffect(new PotionEffect(this.primaryEffect, ☃xx, ☃x, true, true));
         }

         if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect != null) {
            for (EntityPlayer ☃xxxxxxxx : ☃xxxxxxx) {
               ☃xxxxxxxx.addPotionEffect(new PotionEffect(this.secondaryEffect, ☃xx, 0, true, true));
            }
         }
      }
   }

   private void updateSegmentColors() {
      int ☃ = this.pos.getX();
      int ☃x = this.pos.getY();
      int ☃xx = this.pos.getZ();
      int ☃xxx = this.levels;
      this.levels = 0;
      this.beamSegments.clear();
      this.isComplete = true;
      TileEntityBeacon.BeamSegment ☃xxxx = new TileEntityBeacon.BeamSegment(EnumDyeColor.WHITE.getColorComponentValues());
      this.beamSegments.add(☃xxxx);
      boolean ☃xxxxx = true;
      BlockPos.MutableBlockPos ☃xxxxxx = new BlockPos.MutableBlockPos();

      for (int ☃xxxxxxx = ☃x + 1; ☃xxxxxxx < 256; ☃xxxxxxx++) {
         IBlockState ☃xxxxxxxx = this.world.getBlockState(☃xxxxxx.setPos(☃, ☃xxxxxxx, ☃xx));
         float[] ☃xxxxxxxxx;
         if (☃xxxxxxxx.getBlock() == Blocks.STAINED_GLASS) {
            ☃xxxxxxxxx = ☃xxxxxxxx.getValue(BlockStainedGlass.COLOR).getColorComponentValues();
         } else {
            if (☃xxxxxxxx.getBlock() != Blocks.STAINED_GLASS_PANE) {
               if (☃xxxxxxxx.getLightOpacity() >= 15 && ☃xxxxxxxx.getBlock() != Blocks.BEDROCK) {
                  this.isComplete = false;
                  this.beamSegments.clear();
                  break;
               }

               ☃xxxx.incrementHeight();
               continue;
            }

            ☃xxxxxxxxx = ☃xxxxxxxx.getValue(BlockStainedGlassPane.COLOR).getColorComponentValues();
         }

         if (!☃xxxxx) {
            ☃xxxxxxxxx = new float[]{
               (☃xxxx.getColors()[0] + ☃xxxxxxxxx[0]) / 2.0F, (☃xxxx.getColors()[1] + ☃xxxxxxxxx[1]) / 2.0F, (☃xxxx.getColors()[2] + ☃xxxxxxxxx[2]) / 2.0F
            };
         }

         if (Arrays.equals(☃xxxxxxxxx, ☃xxxx.getColors())) {
            ☃xxxx.incrementHeight();
         } else {
            ☃xxxx = new TileEntityBeacon.BeamSegment(☃xxxxxxxxx);
            this.beamSegments.add(☃xxxx);
         }

         ☃xxxxx = false;
      }

      if (this.isComplete) {
         for (int ☃xxxxxxx = 1; ☃xxxxxxx <= 4; this.levels = ☃xxxxxxx++) {
            int ☃xxxxxxxxxx = ☃x - ☃xxxxxxx;
            if (☃xxxxxxxxxx < 0) {
               break;
            }

            boolean ☃xxxxxxxxxxx = true;

            for (int ☃xxxxxxxxxxxx = ☃ - ☃xxxxxxx; ☃xxxxxxxxxxxx <= ☃ + ☃xxxxxxx && ☃xxxxxxxxxxx; ☃xxxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxxx = ☃xx - ☃xxxxxxx; ☃xxxxxxxxxxxxx <= ☃xx + ☃xxxxxxx; ☃xxxxxxxxxxxxx++) {
                  Block ☃xxxxxxxxxxxxxx = this.world.getBlockState(new BlockPos(☃xxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxx)).getBlock();
                  if (☃xxxxxxxxxxxxxx != Blocks.EMERALD_BLOCK
                     && ☃xxxxxxxxxxxxxx != Blocks.GOLD_BLOCK
                     && ☃xxxxxxxxxxxxxx != Blocks.DIAMOND_BLOCK
                     && ☃xxxxxxxxxxxxxx != Blocks.IRON_BLOCK) {
                     ☃xxxxxxxxxxx = false;
                     break;
                  }
               }
            }

            if (!☃xxxxxxxxxxx) {
               break;
            }
         }

         if (this.levels == 0) {
            this.isComplete = false;
         }
      }

      if (!this.world.isRemote && ☃xxx < this.levels) {
         for (EntityPlayerMP ☃xxxxxxx : this.world
            .getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(☃, ☃x, ☃xx, ☃, ☃x - 4, ☃xx).grow(10.0, 5.0, 10.0))) {
            CriteriaTriggers.CONSTRUCT_BEACON.trigger(☃xxxxxxx, this);
         }
      }
   }

   public List<TileEntityBeacon.BeamSegment> getBeamSegments() {
      return this.beamSegments;
   }

   public float shouldBeamRender() {
      if (!this.isComplete) {
         return 0.0F;
      } else {
         int ☃ = (int)(this.world.getTotalWorldTime() - this.beamRenderCounter);
         this.beamRenderCounter = this.world.getTotalWorldTime();
         if (☃ > 1) {
            this.beamRenderScale -= ☃ / 40.0F;
            if (this.beamRenderScale < 0.0F) {
               this.beamRenderScale = 0.0F;
            }
         }

         this.beamRenderScale += 0.025F;
         if (this.beamRenderScale > 1.0F) {
            this.beamRenderScale = 1.0F;
         }

         return this.beamRenderScale;
      }
   }

   public int getLevels() {
      return this.levels;
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
   }

   @Override
   public double getMaxRenderDistanceSquared() {
      return 65536.0;
   }

   @Nullable
   private static Potion isBeaconEffect(int var0) {
      Potion ☃ = Potion.getPotionById(☃);
      return VALID_EFFECTS.contains(☃) ? ☃ : null;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.primaryEffect = isBeaconEffect(☃.getInteger("Primary"));
      this.secondaryEffect = isBeaconEffect(☃.getInteger("Secondary"));
      this.levels = ☃.getInteger("Levels");
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setInteger("Primary", Potion.getIdFromPotion(this.primaryEffect));
      ☃.setInteger("Secondary", Potion.getIdFromPotion(this.secondaryEffect));
      ☃.setInteger("Levels", this.levels);
      return ☃;
   }

   @Override
   public int getSizeInventory() {
      return 1;
   }

   @Override
   public boolean isEmpty() {
      return this.payment.isEmpty();
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return ☃ == 0 ? this.payment : ItemStack.EMPTY;
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      if (☃ != 0 || this.payment.isEmpty()) {
         return ItemStack.EMPTY;
      } else if (☃ >= this.payment.getCount()) {
         ItemStack ☃ = this.payment;
         this.payment = ItemStack.EMPTY;
         return ☃;
      } else {
         return this.payment.splitStack(☃);
      }
   }

   @Override
   public ItemStack removeStackFromSlot(int var1) {
      if (☃ == 0) {
         ItemStack ☃ = this.payment;
         this.payment = ItemStack.EMPTY;
         return ☃;
      } else {
         return ItemStack.EMPTY;
      }
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      if (☃ == 0) {
         this.payment = ☃;
      }
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.customName : "container.beacon";
   }

   @Override
   public boolean hasCustomName() {
      return this.customName != null && !this.customName.isEmpty();
   }

   public void setName(String var1) {
      this.customName = ☃;
   }

   @Override
   public int getInventoryStackLimit() {
      return 1;
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
      return ☃.getItem() == Items.EMERALD || ☃.getItem() == Items.DIAMOND || ☃.getItem() == Items.GOLD_INGOT || ☃.getItem() == Items.IRON_INGOT;
   }

   @Override
   public String getGuiID() {
      return "minecraft:beacon";
   }

   @Override
   public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
      return new ContainerBeacon(☃, this);
   }

   @Override
   public int getField(int var1) {
      switch (☃) {
         case 0:
            return this.levels;
         case 1:
            return Potion.getIdFromPotion(this.primaryEffect);
         case 2:
            return Potion.getIdFromPotion(this.secondaryEffect);
         default:
            return 0;
      }
   }

   @Override
   public void setField(int var1, int var2) {
      switch (☃) {
         case 0:
            this.levels = ☃;
            break;
         case 1:
            this.primaryEffect = isBeaconEffect(☃);
            break;
         case 2:
            this.secondaryEffect = isBeaconEffect(☃);
      }
   }

   @Override
   public int getFieldCount() {
      return 3;
   }

   @Override
   public void clear() {
      this.payment = ItemStack.EMPTY;
   }

   @Override
   public boolean receiveClientEvent(int var1, int var2) {
      if (☃ == 1) {
         this.updateBeacon();
         return true;
      } else {
         return super.receiveClientEvent(☃, ☃);
      }
   }

   @Override
   public int[] getSlotsForFace(EnumFacing var1) {
      return new int[0];
   }

   @Override
   public boolean canInsertItem(int var1, ItemStack var2, EnumFacing var3) {
      return false;
   }

   @Override
   public boolean canExtractItem(int var1, ItemStack var2, EnumFacing var3) {
      return false;
   }

   static {
      for (Potion[] ☃ : EFFECTS_LIST) {
         Collections.addAll(VALID_EFFECTS, ☃);
      }
   }

   public static class BeamSegment {
      private final float[] colors;
      private int height;

      public BeamSegment(float[] var1) {
         this.colors = ☃;
         this.height = 1;
      }

      protected void incrementHeight() {
         this.height++;
      }

      public float[] getColors() {
         return this.colors;
      }

      public int getHeight() {
         return this.height;
      }
   }
}
