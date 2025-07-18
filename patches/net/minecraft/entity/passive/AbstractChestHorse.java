package net.minecraft.entity.passive;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.World;

public abstract class AbstractChestHorse extends AbstractHorse {
   private static final DataParameter<Boolean> DATA_ID_CHEST = EntityDataManager.createKey(AbstractChestHorse.class, DataSerializers.BOOLEAN);

   public AbstractChestHorse(World var1) {
      super(☃);
      this.canGallop = false;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(DATA_ID_CHEST, false);
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getModifiedMaxHealth());
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.175F);
      this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(0.5);
   }

   public boolean hasChest() {
      return this.dataManager.get(DATA_ID_CHEST);
   }

   public void setChested(boolean var1) {
      this.dataManager.set(DATA_ID_CHEST, ☃);
   }

   @Override
   protected int getInventorySize() {
      return this.hasChest() ? 17 : super.getInventorySize();
   }

   @Override
   public double getMountedYOffset() {
      return super.getMountedYOffset() - 0.25;
   }

   @Override
   protected SoundEvent getAngrySound() {
      super.getAngrySound();
      return SoundEvents.ENTITY_DONKEY_ANGRY;
   }

   @Override
   public void onDeath(DamageSource var1) {
      super.onDeath(☃);
      if (this.hasChest()) {
         if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(Blocks.CHEST), 1);
         }

         this.setChested(false);
      }
   }

   public static void registerFixesAbstractChestHorse(DataFixer var0, Class<?> var1) {
      AbstractHorse.registerFixesAbstractHorse(☃, ☃);
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(☃, "Items"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("ChestedHorse", this.hasChest());
      if (this.hasChest()) {
         NBTTagList ☃ = new NBTTagList();

         for (int ☃x = 2; ☃x < this.horseChest.getSizeInventory(); ☃x++) {
            ItemStack ☃xx = this.horseChest.getStackInSlot(☃x);
            if (!☃xx.isEmpty()) {
               NBTTagCompound ☃xxx = new NBTTagCompound();
               ☃xxx.setByte("Slot", (byte)☃x);
               ☃xx.writeToNBT(☃xxx);
               ☃.appendTag(☃xxx);
            }
         }

         ☃.setTag("Items", ☃);
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setChested(☃.getBoolean("ChestedHorse"));
      if (this.hasChest()) {
         NBTTagList ☃ = ☃.getTagList("Items", 10);
         this.initHorseChest();

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
            int ☃xxx = ☃xx.getByte("Slot") & 255;
            if (☃xxx >= 2 && ☃xxx < this.horseChest.getSizeInventory()) {
               this.horseChest.setInventorySlotContents(☃xxx, new ItemStack(☃xx));
            }
         }
      }

      this.updateHorseSlots();
   }

   @Override
   public boolean replaceItemInInventory(int var1, ItemStack var2) {
      if (☃ == 499) {
         if (this.hasChest() && ☃.isEmpty()) {
            this.setChested(false);
            this.initHorseChest();
            return true;
         }

         if (!this.hasChest() && ☃.getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
            this.setChested(true);
            this.initHorseChest();
            return true;
         }
      }

      return super.replaceItemInInventory(☃, ☃);
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.getItem() == Items.SPAWN_EGG) {
         return super.processInteract(☃, ☃);
      } else {
         if (!this.isChild()) {
            if (this.isTame() && ☃.isSneaking()) {
               this.openGUI(☃);
               return true;
            }

            if (this.isBeingRidden()) {
               return super.processInteract(☃, ☃);
            }
         }

         if (!☃.isEmpty()) {
            boolean ☃x = this.handleEating(☃, ☃);
            if (!☃x && !this.isTame()) {
               if (☃.interactWithEntity(☃, this, ☃)) {
                  return true;
               }

               this.makeMad();
               return true;
            }

            if (!☃x && !this.hasChest() && ☃.getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
               this.setChested(true);
               this.playChestEquipSound();
               ☃x = true;
               this.initHorseChest();
            }

            if (!☃x && !this.isChild() && !this.isHorseSaddled() && ☃.getItem() == Items.SADDLE) {
               this.openGUI(☃);
               return true;
            }

            if (☃x) {
               if (!☃.capabilities.isCreativeMode) {
                  ☃.shrink(1);
               }

               return true;
            }
         }

         if (this.isChild()) {
            return super.processInteract(☃, ☃);
         } else if (☃.interactWithEntity(☃, this, ☃)) {
            return true;
         } else {
            this.mountTo(☃);
            return true;
         }
      }
   }

   protected void playChestEquipSound() {
      this.playSound(SoundEvents.ENTITY_DONKEY_CHEST, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
   }

   public int getInventoryColumns() {
      return 5;
   }
}
