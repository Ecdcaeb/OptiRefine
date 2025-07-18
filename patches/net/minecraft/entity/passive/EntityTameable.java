package net.minecraft.entity.passive;

import com.google.common.base.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable {
   protected static final DataParameter<Byte> TAMED = EntityDataManager.createKey(EntityTameable.class, DataSerializers.BYTE);
   protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityTameable.class, DataSerializers.OPTIONAL_UNIQUE_ID);
   protected EntityAISit aiSit;

   public EntityTameable(World var1) {
      super(☃);
      this.setupTamedAI();
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(TAMED, (byte)0);
      this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      if (this.getOwnerId() == null) {
         ☃.setString("OwnerUUID", "");
      } else {
         ☃.setString("OwnerUUID", this.getOwnerId().toString());
      }

      ☃.setBoolean("Sitting", this.isSitting());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      String ☃;
      if (☃.hasKey("OwnerUUID", 8)) {
         ☃ = ☃.getString("OwnerUUID");
      } else {
         String ☃x = ☃.getString("Owner");
         ☃ = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), ☃x);
      }

      if (!☃.isEmpty()) {
         try {
            this.setOwnerId(UUID.fromString(☃));
            this.setTamed(true);
         } catch (Throwable var4) {
            this.setTamed(false);
         }
      }

      if (this.aiSit != null) {
         this.aiSit.setSitting(☃.getBoolean("Sitting"));
      }

      this.setSitting(☃.getBoolean("Sitting"));
   }

   @Override
   public boolean canBeLeashedTo(EntityPlayer var1) {
      return !this.getLeashed();
   }

   protected void playTameEffect(boolean var1) {
      EnumParticleTypes ☃ = EnumParticleTypes.HEART;
      if (!☃) {
         ☃ = EnumParticleTypes.SMOKE_NORMAL;
      }

      for (int ☃x = 0; ☃x < 7; ☃x++) {
         double ☃xx = this.rand.nextGaussian() * 0.02;
         double ☃xxx = this.rand.nextGaussian() * 0.02;
         double ☃xxxx = this.rand.nextGaussian() * 0.02;
         this.world
            .spawnParticle(
               ☃,
               this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
               this.posY + 0.5 + this.rand.nextFloat() * this.height,
               this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
               ☃xx,
               ☃xxx,
               ☃xxxx
            );
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 7) {
         this.playTameEffect(true);
      } else if (☃ == 6) {
         this.playTameEffect(false);
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   public boolean isTamed() {
      return (this.dataManager.get(TAMED) & 4) != 0;
   }

   public void setTamed(boolean var1) {
      byte ☃ = this.dataManager.get(TAMED);
      if (☃) {
         this.dataManager.set(TAMED, (byte)(☃ | 4));
      } else {
         this.dataManager.set(TAMED, (byte)(☃ & -5));
      }

      this.setupTamedAI();
   }

   protected void setupTamedAI() {
   }

   public boolean isSitting() {
      return (this.dataManager.get(TAMED) & 1) != 0;
   }

   public void setSitting(boolean var1) {
      byte ☃ = this.dataManager.get(TAMED);
      if (☃) {
         this.dataManager.set(TAMED, (byte)(☃ | 1));
      } else {
         this.dataManager.set(TAMED, (byte)(☃ & -2));
      }
   }

   @Nullable
   @Override
   public UUID getOwnerId() {
      return (UUID)this.dataManager.get(OWNER_UNIQUE_ID).orNull();
   }

   public void setOwnerId(@Nullable UUID var1) {
      this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(☃));
   }

   public void setTamedBy(EntityPlayer var1) {
      this.setTamed(true);
      this.setOwnerId(☃.getUniqueID());
      if (☃ instanceof EntityPlayerMP) {
         CriteriaTriggers.TAME_ANIMAL.trigger((EntityPlayerMP)☃, this);
      }
   }

   @Nullable
   public EntityLivingBase getOwner() {
      try {
         UUID ☃ = this.getOwnerId();
         return ☃ == null ? null : this.world.getPlayerEntityByUUID(☃);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public boolean isOwner(EntityLivingBase var1) {
      return ☃ == this.getOwner();
   }

   public EntityAISit getAISit() {
      return this.aiSit;
   }

   public boolean shouldAttackEntity(EntityLivingBase var1, EntityLivingBase var2) {
      return true;
   }

   @Override
   public Team getTeam() {
      if (this.isTamed()) {
         EntityLivingBase ☃ = this.getOwner();
         if (☃ != null) {
            return ☃.getTeam();
         }
      }

      return super.getTeam();
   }

   @Override
   public boolean isOnSameTeam(Entity var1) {
      if (this.isTamed()) {
         EntityLivingBase ☃ = this.getOwner();
         if (☃ == ☃) {
            return true;
         }

         if (☃ != null) {
            return ☃.isOnSameTeam(☃);
         }
      }

      return super.isOnSameTeam(☃);
   }

   @Override
   public void onDeath(DamageSource var1) {
      if (!this.world.isRemote && this.world.getGameRules().getBoolean("showDeathMessages") && this.getOwner() instanceof EntityPlayerMP) {
         this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage());
      }

      super.onDeath(☃);
   }
}
