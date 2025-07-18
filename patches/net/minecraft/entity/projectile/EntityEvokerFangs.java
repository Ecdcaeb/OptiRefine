package net.minecraft.entity.projectile;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityEvokerFangs extends Entity {
   private int warmupDelayTicks;
   private boolean sentSpikeEvent;
   private int lifeTicks = 22;
   private boolean clientSideAttackStarted;
   private EntityLivingBase caster;
   private UUID casterUuid;

   public EntityEvokerFangs(World var1) {
      super(☃);
      this.setSize(0.5F, 0.8F);
   }

   public EntityEvokerFangs(World var1, double var2, double var4, double var6, float var8, int var9, EntityLivingBase var10) {
      this(☃);
      this.warmupDelayTicks = ☃;
      this.setCaster(☃);
      this.rotationYaw = ☃ * (180.0F / (float)Math.PI);
      this.setPosition(☃, ☃, ☃);
   }

   @Override
   protected void entityInit() {
   }

   public void setCaster(@Nullable EntityLivingBase var1) {
      this.caster = ☃;
      this.casterUuid = ☃ == null ? null : ☃.getUniqueID();
   }

   @Nullable
   public EntityLivingBase getCaster() {
      if (this.caster == null && this.casterUuid != null && this.world instanceof WorldServer) {
         Entity ☃ = ((WorldServer)this.world).getEntityFromUuid(this.casterUuid);
         if (☃ instanceof EntityLivingBase) {
            this.caster = (EntityLivingBase)☃;
         }
      }

      return this.caster;
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      this.warmupDelayTicks = ☃.getInteger("Warmup");
      this.casterUuid = ☃.getUniqueId("OwnerUUID");
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setInteger("Warmup", this.warmupDelayTicks);
      if (this.casterUuid != null) {
         ☃.setUniqueId("OwnerUUID", this.casterUuid);
      }
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.world.isRemote) {
         if (this.clientSideAttackStarted) {
            this.lifeTicks--;
            if (this.lifeTicks == 14) {
               for (int ☃ = 0; ☃ < 12; ☃++) {
                  double ☃x = this.posX + (this.rand.nextDouble() * 2.0 - 1.0) * this.width * 0.5;
                  double ☃xx = this.posY + 0.05 + this.rand.nextDouble() * 1.0;
                  double ☃xxx = this.posZ + (this.rand.nextDouble() * 2.0 - 1.0) * this.width * 0.5;
                  double ☃xxxx = (this.rand.nextDouble() * 2.0 - 1.0) * 0.3;
                  double ☃xxxxx = 0.3 + this.rand.nextDouble() * 0.3;
                  double ☃xxxxxx = (this.rand.nextDouble() * 2.0 - 1.0) * 0.3;
                  this.world.spawnParticle(EnumParticleTypes.CRIT, ☃x, ☃xx + 1.0, ☃xxx, ☃xxxx, ☃xxxxx, ☃xxxxxx);
               }
            }
         }
      } else if (--this.warmupDelayTicks < 0) {
         if (this.warmupDelayTicks == -8) {
            for (EntityLivingBase ☃ : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.2, 0.0, 0.2))) {
               this.damage(☃);
            }
         }

         if (!this.sentSpikeEvent) {
            this.world.setEntityState(this, (byte)4);
            this.sentSpikeEvent = true;
         }

         if (--this.lifeTicks < 0) {
            this.setDead();
         }
      }
   }

   private void damage(EntityLivingBase var1) {
      EntityLivingBase ☃ = this.getCaster();
      if (☃.isEntityAlive() && !☃.getIsInvulnerable() && ☃ != ☃) {
         if (☃ == null) {
            ☃.attackEntityFrom(DamageSource.MAGIC, 6.0F);
         } else {
            if (☃.isOnSameTeam(☃)) {
               return;
            }

            ☃.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, ☃), 6.0F);
         }
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      super.handleStatusUpdate(☃);
      if (☃ == 4) {
         this.clientSideAttackStarted = true;
         if (!this.isSilent()) {
            this.world
               .playSound(
                  this.posX,
                  this.posY,
                  this.posZ,
                  SoundEvents.EVOCATION_FANGS_ATTACK,
                  this.getSoundCategory(),
                  1.0F,
                  this.rand.nextFloat() * 0.2F + 0.85F,
                  false
               );
         }
      }
   }

   public float getAnimationProgress(float var1) {
      if (!this.clientSideAttackStarted) {
         return 0.0F;
      } else {
         int ☃ = this.lifeTicks - 2;
         return ☃ <= 0 ? 1.0F : 1.0F - (☃ - ☃) / 20.0F;
      }
   }
}
