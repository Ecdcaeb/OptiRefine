package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class EntityItemFrame extends EntityHanging {
   private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.ITEM_STACK);
   private static final DataParameter<Integer> ROTATION = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.VARINT);
   private float itemDropChance = 1.0F;

   public EntityItemFrame(World var1) {
      super(☃);
   }

   public EntityItemFrame(World var1, BlockPos var2, EnumFacing var3) {
      super(☃, ☃);
      this.updateFacingWithBoundingBox(☃);
   }

   @Override
   protected void entityInit() {
      this.getDataManager().register(ITEM, ItemStack.EMPTY);
      this.getDataManager().register(ROTATION, 0);
   }

   @Override
   public float getCollisionBorderSize() {
      return 0.0F;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else if (!☃.isExplosion() && !this.getDisplayedItem().isEmpty()) {
         if (!this.world.isRemote) {
            this.dropItemOrSelf(☃.getTrueSource(), false);
            this.playSound(SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, 1.0F, 1.0F);
            this.setDisplayedItem(ItemStack.EMPTY);
         }

         return true;
      } else {
         return super.attackEntityFrom(☃, ☃);
      }
   }

   @Override
   public int getWidthPixels() {
      return 12;
   }

   @Override
   public int getHeightPixels() {
      return 12;
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      double ☃ = 16.0;
      ☃ *= 64.0 * getRenderDistanceWeight();
      return ☃ < ☃ * ☃;
   }

   @Override
   public void onBroken(@Nullable Entity var1) {
      this.playSound(SoundEvents.ENTITY_ITEMFRAME_BREAK, 1.0F, 1.0F);
      this.dropItemOrSelf(☃, true);
   }

   @Override
   public void playPlaceSound() {
      this.playSound(SoundEvents.ENTITY_ITEMFRAME_PLACE, 1.0F, 1.0F);
   }

   public void dropItemOrSelf(@Nullable Entity var1, boolean var2) {
      if (this.world.getGameRules().getBoolean("doEntityDrops")) {
         ItemStack ☃ = this.getDisplayedItem();
         if (☃ instanceof EntityPlayer) {
            EntityPlayer ☃x = (EntityPlayer)☃;
            if (☃x.capabilities.isCreativeMode) {
               this.removeFrameFromMap(☃);
               return;
            }
         }

         if (☃) {
            this.entityDropItem(new ItemStack(Items.ITEM_FRAME), 0.0F);
         }

         if (!☃.isEmpty() && this.rand.nextFloat() < this.itemDropChance) {
            ☃ = ☃.copy();
            this.removeFrameFromMap(☃);
            this.entityDropItem(☃, 0.0F);
         }
      }
   }

   private void removeFrameFromMap(ItemStack var1) {
      if (!☃.isEmpty()) {
         if (☃.getItem() == Items.FILLED_MAP) {
            MapData ☃ = ((ItemMap)☃.getItem()).getMapData(☃, this.world);
            ☃.mapDecorations.remove("frame-" + this.getEntityId());
         }

         ☃.setItemFrame(null);
      }
   }

   public ItemStack getDisplayedItem() {
      return this.getDataManager().get(ITEM);
   }

   public void setDisplayedItem(ItemStack var1) {
      this.setDisplayedItemWithUpdate(☃, true);
   }

   private void setDisplayedItemWithUpdate(ItemStack var1, boolean var2) {
      if (!☃.isEmpty()) {
         ☃ = ☃.copy();
         ☃.setCount(1);
         ☃.setItemFrame(this);
      }

      this.getDataManager().set(ITEM, ☃);
      this.getDataManager().setDirty(ITEM);
      if (!☃.isEmpty()) {
         this.playSound(SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, 1.0F, 1.0F);
      }

      if (☃ && this.hangingPosition != null) {
         this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
      }
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (☃.equals(ITEM)) {
         ItemStack ☃ = this.getDisplayedItem();
         if (!☃.isEmpty() && ☃.getItemFrame() != this) {
            ☃.setItemFrame(this);
         }
      }
   }

   public int getRotation() {
      return this.getDataManager().get(ROTATION);
   }

   public void setItemRotation(int var1) {
      this.setRotation(☃, true);
   }

   private void setRotation(int var1, boolean var2) {
      this.getDataManager().set(ROTATION, ☃ % 8);
      if (☃ && this.hangingPosition != null) {
         this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
      }
   }

   public static void registerFixesItemFrame(DataFixer var0) {
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityItemFrame.class, "Item"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      if (!this.getDisplayedItem().isEmpty()) {
         ☃.setTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
         ☃.setByte("ItemRotation", (byte)this.getRotation());
         ☃.setFloat("ItemDropChance", this.itemDropChance);
      }

      super.writeEntityToNBT(☃);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      NBTTagCompound ☃ = ☃.getCompoundTag("Item");
      if (☃ != null && !☃.isEmpty()) {
         this.setDisplayedItemWithUpdate(new ItemStack(☃), false);
         this.setRotation(☃.getByte("ItemRotation"), false);
         if (☃.hasKey("ItemDropChance", 99)) {
            this.itemDropChance = ☃.getFloat("ItemDropChance");
         }
      }

      super.readEntityFromNBT(☃);
   }

   @Override
   public boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!this.world.isRemote) {
         if (this.getDisplayedItem().isEmpty()) {
            if (!☃.isEmpty()) {
               this.setDisplayedItem(☃);
               if (!☃.capabilities.isCreativeMode) {
                  ☃.shrink(1);
               }
            }
         } else {
            this.playSound(SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM, 1.0F, 1.0F);
            this.setItemRotation(this.getRotation() + 1);
         }
      }

      return true;
   }

   public int getAnalogOutput() {
      return this.getDisplayedItem().isEmpty() ? 0 : this.getRotation() % 8 + 1;
   }
}
