package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityItem extends Entity {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItem.class, DataSerializers.ITEM_STACK);
   private int age;
   private int pickupDelay;
   private int health = 5;
   private String thrower;
   private String owner;
   public float hoverStart = (float)(Math.random() * Math.PI * 2.0);

   public EntityItem(World var1, double var2, double var4, double var6) {
      super(☃);
      this.setSize(0.25F, 0.25F);
      this.setPosition(☃, ☃, ☃);
      this.rotationYaw = (float)(Math.random() * 360.0);
      this.motionX = (float)(Math.random() * 0.2F - 0.1F);
      this.motionY = 0.2F;
      this.motionZ = (float)(Math.random() * 0.2F - 0.1F);
   }

   public EntityItem(World var1, double var2, double var4, double var6, ItemStack var8) {
      this(☃, ☃, ☃, ☃);
      this.setItem(☃);
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   public EntityItem(World var1) {
      super(☃);
      this.setSize(0.25F, 0.25F);
      this.setItem(ItemStack.EMPTY);
   }

   @Override
   protected void entityInit() {
      this.getDataManager().register(ITEM, ItemStack.EMPTY);
   }

   @Override
   public void onUpdate() {
      if (this.getItem().isEmpty()) {
         this.setDead();
      } else {
         super.onUpdate();
         if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
            this.pickupDelay--;
         }

         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         double ☃ = this.motionX;
         double ☃x = this.motionY;
         double ☃xx = this.motionZ;
         if (!this.hasNoGravity()) {
            this.motionY -= 0.04F;
         }

         if (this.world.isRemote) {
            this.noClip = false;
         } else {
            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
         }

         this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
         boolean ☃xxx = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;
         if (☃xxx || this.ticksExisted % 25 == 0) {
            if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
               this.motionY = 0.2F;
               this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
               this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
               this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
            }

            if (!this.world.isRemote) {
               this.searchForOtherItemsNearby();
            }
         }

         float ☃xxxx = 0.98F;
         if (this.onGround) {
            ☃xxxx = this.world
                  .getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ)))
                  .getBlock()
                  .slipperiness
               * 0.98F;
         }

         this.motionX *= ☃xxxx;
         this.motionY *= 0.98F;
         this.motionZ *= ☃xxxx;
         if (this.onGround) {
            this.motionY *= -0.5;
         }

         if (this.age != -32768) {
            this.age++;
         }

         this.handleWaterMovement();
         if (!this.world.isRemote) {
            double ☃xxxxx = this.motionX - ☃;
            double ☃xxxxxx = this.motionY - ☃x;
            double ☃xxxxxxx = this.motionZ - ☃xx;
            double ☃xxxxxxxx = ☃xxxxx * ☃xxxxx + ☃xxxxxx * ☃xxxxxx + ☃xxxxxxx * ☃xxxxxxx;
            if (☃xxxxxxxx > 0.01) {
               this.isAirBorne = true;
            }
         }

         if (!this.world.isRemote && this.age >= 6000) {
            this.setDead();
         }
      }
   }

   private void searchForOtherItemsNearby() {
      for (EntityItem ☃ : this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(0.5, 0.0, 0.5))) {
         this.combineItems(☃);
      }
   }

   private boolean combineItems(EntityItem var1) {
      if (☃ == this) {
         return false;
      } else if (☃.isEntityAlive() && this.isEntityAlive()) {
         ItemStack ☃ = this.getItem();
         ItemStack ☃x = ☃.getItem();
         if (this.pickupDelay == 32767 || ☃.pickupDelay == 32767) {
            return false;
         } else if (this.age != -32768 && ☃.age != -32768) {
            if (☃x.getItem() != ☃.getItem()) {
               return false;
            } else if (☃x.hasTagCompound() ^ ☃.hasTagCompound()) {
               return false;
            } else if (☃x.hasTagCompound() && !☃x.getTagCompound().equals(☃.getTagCompound())) {
               return false;
            } else if (☃x.getItem() == null) {
               return false;
            } else if (☃x.getItem().getHasSubtypes() && ☃x.getMetadata() != ☃.getMetadata()) {
               return false;
            } else if (☃x.getCount() < ☃.getCount()) {
               return ☃.combineItems(this);
            } else if (☃x.getCount() + ☃.getCount() > ☃x.getMaxStackSize()) {
               return false;
            } else {
               ☃x.grow(☃.getCount());
               ☃.pickupDelay = Math.max(☃.pickupDelay, this.pickupDelay);
               ☃.age = Math.min(☃.age, this.age);
               ☃.setItem(☃x);
               this.setDead();
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void setAgeToCreativeDespawnTime() {
      this.age = 4800;
   }

   @Override
   public boolean handleWaterMovement() {
      if (this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this)) {
         if (!this.inWater && !this.firstUpdate) {
            this.doWaterSplashEffect();
         }

         this.inWater = true;
      } else {
         this.inWater = false;
      }

      return this.inWater;
   }

   @Override
   protected void dealFireDamage(int var1) {
      this.attackEntityFrom(DamageSource.IN_FIRE, ☃);
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else if (!this.getItem().isEmpty() && this.getItem().getItem() == Items.NETHER_STAR && ☃.isExplosion()) {
         return false;
      } else {
         this.markVelocityChanged();
         this.health = (int)(this.health - ☃);
         if (this.health <= 0) {
            this.setDead();
         }

         return false;
      }
   }

   public static void registerFixesItem(DataFixer var0) {
      ☃.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityItem.class, "Item"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setShort("Health", (short)this.health);
      ☃.setShort("Age", (short)this.age);
      ☃.setShort("PickupDelay", (short)this.pickupDelay);
      if (this.getThrower() != null) {
         ☃.setString("Thrower", this.thrower);
      }

      if (this.getOwner() != null) {
         ☃.setString("Owner", this.owner);
      }

      if (!this.getItem().isEmpty()) {
         ☃.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      this.health = ☃.getShort("Health");
      this.age = ☃.getShort("Age");
      if (☃.hasKey("PickupDelay")) {
         this.pickupDelay = ☃.getShort("PickupDelay");
      }

      if (☃.hasKey("Owner")) {
         this.owner = ☃.getString("Owner");
      }

      if (☃.hasKey("Thrower")) {
         this.thrower = ☃.getString("Thrower");
      }

      NBTTagCompound ☃ = ☃.getCompoundTag("Item");
      this.setItem(new ItemStack(☃));
      if (this.getItem().isEmpty()) {
         this.setDead();
      }
   }

   @Override
   public void onCollideWithPlayer(EntityPlayer var1) {
      if (!this.world.isRemote) {
         ItemStack ☃ = this.getItem();
         Item ☃x = ☃.getItem();
         int ☃xx = ☃.getCount();
         if (this.pickupDelay == 0
            && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(☃.getName()))
            && ☃.inventory.addItemStackToInventory(☃)) {
            ☃.onItemPickup(this, ☃xx);
            if (☃.isEmpty()) {
               this.setDead();
               ☃.setCount(☃xx);
            }

            ☃.addStat(StatList.getObjectsPickedUpStats(☃x), ☃xx);
         }
      }
   }

   @Override
   public String getName() {
      return this.hasCustomName() ? this.getCustomNameTag() : I18n.translateToLocal("item." + this.getItem().getTranslationKey());
   }

   @Override
   public boolean canBeAttackedWithItem() {
      return false;
   }

   @Nullable
   @Override
   public Entity changeDimension(int var1) {
      Entity ☃ = super.changeDimension(☃);
      if (!this.world.isRemote && ☃ instanceof EntityItem) {
         ((EntityItem)☃).searchForOtherItemsNearby();
      }

      return ☃;
   }

   public ItemStack getItem() {
      return this.getDataManager().get(ITEM);
   }

   public void setItem(ItemStack var1) {
      this.getDataManager().set(ITEM, ☃);
      this.getDataManager().setDirty(ITEM);
   }

   public String getOwner() {
      return this.owner;
   }

   public void setOwner(String var1) {
      this.owner = ☃;
   }

   public String getThrower() {
      return this.thrower;
   }

   public void setThrower(String var1) {
      this.thrower = ☃;
   }

   public int getAge() {
      return this.age;
   }

   public void setDefaultPickupDelay() {
      this.pickupDelay = 10;
   }

   public void setNoPickupDelay() {
      this.pickupDelay = 0;
   }

   public void setInfinitePickupDelay() {
      this.pickupDelay = 32767;
   }

   public void setPickupDelay(int var1) {
      this.pickupDelay = ☃;
   }

   public boolean cannotPickup() {
      return this.pickupDelay > 0;
   }

   public void setNoDespawn() {
      this.age = -6000;
   }

   public void makeFakeItem() {
      this.setInfinitePickupDelay();
      this.age = 5999;
   }
}
