package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature {
   private static final DataParameter<Boolean> BABY = EntityDataManager.createKey(EntityAgeable.class, DataSerializers.BOOLEAN);
   protected int growingAge;
   protected int forcedAge;
   protected int forcedAgeTimer;
   private float ageWidth = -1.0F;
   private float ageHeight;

   public EntityAgeable(World var1) {
      super(☃);
   }

   @Nullable
   public abstract EntityAgeable createChild(EntityAgeable var1);

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.getItem() == Items.SPAWN_EGG) {
         if (!this.world.isRemote) {
            Class<? extends Entity> ☃x = EntityList.REGISTRY.getObject(ItemMonsterPlacer.getNamedIdFrom(☃));
            if (☃x != null && this.getClass() == ☃x) {
               EntityAgeable ☃xx = this.createChild(this);
               if (☃xx != null) {
                  ☃xx.setGrowingAge(-24000);
                  ☃xx.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                  this.world.spawnEntity(☃xx);
                  if (☃.hasDisplayName()) {
                     ☃xx.setCustomNameTag(☃.getDisplayName());
                  }

                  if (!☃.capabilities.isCreativeMode) {
                     ☃.shrink(1);
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected boolean holdingSpawnEggOfClass(ItemStack var1, Class<? extends Entity> var2) {
      if (☃.getItem() != Items.SPAWN_EGG) {
         return false;
      } else {
         Class<? extends Entity> ☃ = EntityList.REGISTRY.getObject(ItemMonsterPlacer.getNamedIdFrom(☃));
         return ☃ != null && ☃ == ☃;
      }
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(BABY, false);
   }

   public int getGrowingAge() {
      if (this.world.isRemote) {
         return this.dataManager.get(BABY) ? -1 : 1;
      } else {
         return this.growingAge;
      }
   }

   public void ageUp(int var1, boolean var2) {
      int ☃ = this.getGrowingAge();
      ☃ += ☃ * 20;
      if (☃ > 0) {
         ☃ = 0;
         if (☃ < 0) {
            this.onGrowingAdult();
         }
      }

      int ☃x = ☃ - ☃;
      this.setGrowingAge(☃);
      if (☃) {
         this.forcedAge += ☃x;
         if (this.forcedAgeTimer == 0) {
            this.forcedAgeTimer = 40;
         }
      }

      if (this.getGrowingAge() == 0) {
         this.setGrowingAge(this.forcedAge);
      }
   }

   public void addGrowth(int var1) {
      this.ageUp(☃, false);
   }

   public void setGrowingAge(int var1) {
      this.dataManager.set(BABY, ☃ < 0);
      this.growingAge = ☃;
      this.setScaleForAge(this.isChild());
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("Age", this.getGrowingAge());
      ☃.setInteger("ForcedAge", this.forcedAge);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setGrowingAge(☃.getInteger("Age"));
      this.forcedAge = ☃.getInteger("ForcedAge");
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (BABY.equals(☃)) {
         this.setScaleForAge(this.isChild());
      }

      super.notifyDataManagerChange(☃);
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.world.isRemote) {
         if (this.forcedAgeTimer > 0) {
            if (this.forcedAgeTimer % 4 == 0) {
               this.world
                  .spawnParticle(
                     EnumParticleTypes.VILLAGER_HAPPY,
                     this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
                     this.posY + 0.5 + this.rand.nextFloat() * this.height,
                     this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
                     0.0,
                     0.0,
                     0.0
                  );
            }

            this.forcedAgeTimer--;
         }
      } else {
         int ☃ = this.getGrowingAge();
         if (☃ < 0) {
            this.setGrowingAge(++☃);
            if (☃ == 0) {
               this.onGrowingAdult();
            }
         } else if (☃ > 0) {
            this.setGrowingAge(--☃);
         }
      }
   }

   protected void onGrowingAdult() {
   }

   @Override
   public boolean isChild() {
      return this.getGrowingAge() < 0;
   }

   public void setScaleForAge(boolean var1) {
      this.setScale(☃ ? 0.5F : 1.0F);
   }

   @Override
   protected final void setSize(float var1, float var2) {
      boolean ☃ = this.ageWidth > 0.0F;
      this.ageWidth = ☃;
      this.ageHeight = ☃;
      if (!☃) {
         this.setScale(1.0F);
      }
   }

   protected final void setScale(float var1) {
      super.setSize(this.ageWidth * ☃, this.ageHeight * ☃);
   }
}
