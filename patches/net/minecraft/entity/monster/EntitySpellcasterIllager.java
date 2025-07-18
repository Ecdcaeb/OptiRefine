package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntitySpellcasterIllager extends AbstractIllager {
   private static final DataParameter<Byte> SPELL = EntityDataManager.createKey(EntitySpellcasterIllager.class, DataSerializers.BYTE);
   protected int spellTicks;
   private EntitySpellcasterIllager.SpellType activeSpell = EntitySpellcasterIllager.SpellType.NONE;

   public EntitySpellcasterIllager(World var1) {
      super(☃);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(SPELL, (byte)0);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.spellTicks = ☃.getInteger("SpellTicks");
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("SpellTicks", this.spellTicks);
   }

   @Override
   public AbstractIllager.IllagerArmPose getArmPose() {
      return this.isSpellcasting() ? AbstractIllager.IllagerArmPose.SPELLCASTING : AbstractIllager.IllagerArmPose.CROSSED;
   }

   public boolean isSpellcasting() {
      return this.world.isRemote ? this.dataManager.get(SPELL) > 0 : this.spellTicks > 0;
   }

   public void setSpellType(EntitySpellcasterIllager.SpellType var1) {
      this.activeSpell = ☃;
      this.dataManager.set(SPELL, (byte)☃.id);
   }

   protected EntitySpellcasterIllager.SpellType getSpellType() {
      return !this.world.isRemote ? this.activeSpell : EntitySpellcasterIllager.SpellType.getFromId(this.dataManager.get(SPELL));
   }

   @Override
   protected void updateAITasks() {
      super.updateAITasks();
      if (this.spellTicks > 0) {
         this.spellTicks--;
      }
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.world.isRemote && this.isSpellcasting()) {
         EntitySpellcasterIllager.SpellType ☃ = this.getSpellType();
         double ☃x = ☃.particleSpeed[0];
         double ☃xx = ☃.particleSpeed[1];
         double ☃xxx = ☃.particleSpeed[2];
         float ☃xxxx = this.renderYawOffset * (float) (Math.PI / 180.0) + MathHelper.cos(this.ticksExisted * 0.6662F) * 0.25F;
         float ☃xxxxx = MathHelper.cos(☃xxxx);
         float ☃xxxxxx = MathHelper.sin(☃xxxx);
         this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + ☃xxxxx * 0.6, this.posY + 1.8, this.posZ + ☃xxxxxx * 0.6, ☃x, ☃xx, ☃xxx);
         this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - ☃xxxxx * 0.6, this.posY + 1.8, this.posZ - ☃xxxxxx * 0.6, ☃x, ☃xx, ☃xxx);
      }
   }

   protected int getSpellTicks() {
      return this.spellTicks;
   }

   protected abstract SoundEvent getSpellSound();

   public class AICastingApell extends EntityAIBase {
      public AICastingApell() {
         this.setMutexBits(3);
      }

      @Override
      public boolean shouldExecute() {
         return EntitySpellcasterIllager.this.getSpellTicks() > 0;
      }

      @Override
      public void startExecuting() {
         super.startExecuting();
         EntitySpellcasterIllager.this.navigator.clearPath();
      }

      @Override
      public void resetTask() {
         super.resetTask();
         EntitySpellcasterIllager.this.setSpellType(EntitySpellcasterIllager.SpellType.NONE);
      }

      @Override
      public void updateTask() {
         if (EntitySpellcasterIllager.this.getAttackTarget() != null) {
            EntitySpellcasterIllager.this.getLookHelper()
               .setLookPositionWithEntity(
                  EntitySpellcasterIllager.this.getAttackTarget(),
                  EntitySpellcasterIllager.this.getHorizontalFaceSpeed(),
                  EntitySpellcasterIllager.this.getVerticalFaceSpeed()
               );
         }
      }
   }

   public abstract class AIUseSpell extends EntityAIBase {
      protected int spellWarmup;
      protected int spellCooldown;

      protected AIUseSpell() {
      }

      @Override
      public boolean shouldExecute() {
         if (EntitySpellcasterIllager.this.getAttackTarget() == null) {
            return false;
         } else {
            return EntitySpellcasterIllager.this.isSpellcasting() ? false : EntitySpellcasterIllager.this.ticksExisted >= this.spellCooldown;
         }
      }

      @Override
      public boolean shouldContinueExecuting() {
         return EntitySpellcasterIllager.this.getAttackTarget() != null && this.spellWarmup > 0;
      }

      @Override
      public void startExecuting() {
         this.spellWarmup = this.getCastWarmupTime();
         EntitySpellcasterIllager.this.spellTicks = this.getCastingTime();
         this.spellCooldown = EntitySpellcasterIllager.this.ticksExisted + this.getCastingInterval();
         SoundEvent ☃ = this.getSpellPrepareSound();
         if (☃ != null) {
            EntitySpellcasterIllager.this.playSound(☃, 1.0F, 1.0F);
         }

         EntitySpellcasterIllager.this.setSpellType(this.getSpellType());
      }

      @Override
      public void updateTask() {
         this.spellWarmup--;
         if (this.spellWarmup == 0) {
            this.castSpell();
            EntitySpellcasterIllager.this.playSound(EntitySpellcasterIllager.this.getSpellSound(), 1.0F, 1.0F);
         }
      }

      protected abstract void castSpell();

      protected int getCastWarmupTime() {
         return 20;
      }

      protected abstract int getCastingTime();

      protected abstract int getCastingInterval();

      @Nullable
      protected abstract SoundEvent getSpellPrepareSound();

      protected abstract EntitySpellcasterIllager.SpellType getSpellType();
   }

   public static enum SpellType {
      NONE(0, 0.0, 0.0, 0.0),
      SUMMON_VEX(1, 0.7, 0.7, 0.8),
      FANGS(2, 0.4, 0.3, 0.35),
      WOLOLO(3, 0.7, 0.5, 0.2),
      DISAPPEAR(4, 0.3, 0.3, 0.8),
      BLINDNESS(5, 0.1, 0.1, 0.2);

      private final int id;
      private final double[] particleSpeed;

      private SpellType(int var3, double var4, double var6, double var8) {
         this.id = ☃;
         this.particleSpeed = new double[]{☃, ☃, ☃};
      }

      public static EntitySpellcasterIllager.SpellType getFromId(int var0) {
         for (EntitySpellcasterIllager.SpellType ☃ : values()) {
            if (☃ == ☃.id) {
               return ☃;
            }
         }

         return NONE;
      }
   }
}
