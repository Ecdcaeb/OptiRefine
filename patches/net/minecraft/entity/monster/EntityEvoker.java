package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEvoker extends EntitySpellcasterIllager {
   private EntitySheep wololoTarget;

   public EntityEvoker(World var1) {
      super(☃);
      this.setSize(0.6F, 1.95F);
      this.experienceValue = 10;
   }

   @Override
   protected void initEntityAI() {
      super.initEntityAI();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityEvoker.AICastingSpell());
      this.tasks.addTask(2, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 8.0F, 0.6, 1.0));
      this.tasks.addTask(4, new EntityEvoker.AISummonSpell());
      this.tasks.addTask(5, new EntityEvoker.AIAttackSpell());
      this.tasks.addTask(6, new EntityEvoker.AIWololoSpell());
      this.tasks.addTask(8, new EntityAIWander(this, 0.6));
      this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityEvoker.class));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true).setUnseenMemoryTicks(300));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, false).setUnseenMemoryTicks(300));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, false));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0);
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
   }

   public static void registerFixesEvoker(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityEvoker.class);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
   }

   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_EVOCATION_ILLAGER;
   }

   @Override
   protected void updateAITasks() {
      super.updateAITasks();
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
   }

   @Override
   public boolean isOnSameTeam(Entity var1) {
      if (☃ == null) {
         return false;
      } else if (☃ == this) {
         return true;
      } else if (super.isOnSameTeam(☃)) {
         return true;
      } else if (☃ instanceof EntityVex) {
         return this.isOnSameTeam(((EntityVex)☃).getOwner());
      } else {
         return ☃ instanceof EntityLivingBase && ((EntityLivingBase)☃).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER
            ? this.getTeam() == null && ☃.getTeam() == null
            : false;
      }
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.EVOCATION_ILLAGER_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_EVOCATION_ILLAGER_HURT;
   }

   private void setWololoTarget(@Nullable EntitySheep var1) {
      this.wololoTarget = ☃;
   }

   @Nullable
   private EntitySheep getWololoTarget() {
      return this.wololoTarget;
   }

   @Override
   protected SoundEvent getSpellSound() {
      return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
   }

   class AIAttackSpell extends EntitySpellcasterIllager.AIUseSpell {
      private AIAttackSpell() {
      }

      @Override
      protected int getCastingTime() {
         return 40;
      }

      @Override
      protected int getCastingInterval() {
         return 100;
      }

      @Override
      protected void castSpell() {
         EntityLivingBase ☃ = EntityEvoker.this.getAttackTarget();
         double ☃x = Math.min(☃.posY, EntityEvoker.this.posY);
         double ☃xx = Math.max(☃.posY, EntityEvoker.this.posY) + 1.0;
         float ☃xxx = (float)MathHelper.atan2(☃.posZ - EntityEvoker.this.posZ, ☃.posX - EntityEvoker.this.posX);
         if (EntityEvoker.this.getDistanceSq(☃) < 9.0) {
            for (int ☃xxxx = 0; ☃xxxx < 5; ☃xxxx++) {
               float ☃xxxxx = ☃xxx + ☃xxxx * (float) Math.PI * 0.4F;
               this.spawnFangs(EntityEvoker.this.posX + MathHelper.cos(☃xxxxx) * 1.5, EntityEvoker.this.posZ + MathHelper.sin(☃xxxxx) * 1.5, ☃x, ☃xx, ☃xxxxx, 0);
            }

            for (int ☃xxxx = 0; ☃xxxx < 8; ☃xxxx++) {
               float ☃xxxxx = ☃xxx + ☃xxxx * (float) Math.PI * 2.0F / 8.0F + (float) (Math.PI * 2.0 / 5.0);
               this.spawnFangs(EntityEvoker.this.posX + MathHelper.cos(☃xxxxx) * 2.5, EntityEvoker.this.posZ + MathHelper.sin(☃xxxxx) * 2.5, ☃x, ☃xx, ☃xxxxx, 3);
            }
         } else {
            for (int ☃xxxx = 0; ☃xxxx < 16; ☃xxxx++) {
               double ☃xxxxx = 1.25 * (☃xxxx + 1);
               int ☃xxxxxx = 1 * ☃xxxx;
               this.spawnFangs(
                  EntityEvoker.this.posX + MathHelper.cos(☃xxx) * ☃xxxxx, EntityEvoker.this.posZ + MathHelper.sin(☃xxx) * ☃xxxxx, ☃x, ☃xx, ☃xxx, ☃xxxxxx
               );
            }
         }
      }

      private void spawnFangs(double var1, double var3, double var5, double var7, float var9, int var10) {
         BlockPos ☃ = new BlockPos(☃, ☃, ☃);
         boolean ☃x = false;
         double ☃xx = 0.0;

         do {
            if (!EntityEvoker.this.world.isBlockNormalCube(☃, true) && EntityEvoker.this.world.isBlockNormalCube(☃.down(), true)) {
               if (!EntityEvoker.this.world.isAirBlock(☃)) {
                  IBlockState ☃xxx = EntityEvoker.this.world.getBlockState(☃);
                  AxisAlignedBB ☃xxxx = ☃xxx.getCollisionBoundingBox(EntityEvoker.this.world, ☃);
                  if (☃xxxx != null) {
                     ☃xx = ☃xxxx.maxY;
                  }
               }

               ☃x = true;
               break;
            }

            ☃ = ☃.down();
         } while (☃.getY() >= MathHelper.floor(☃) - 1);

         if (☃x) {
            EntityEvokerFangs ☃xxx = new EntityEvokerFangs(EntityEvoker.this.world, ☃, ☃.getY() + ☃xx, ☃, ☃, ☃, EntityEvoker.this);
            EntityEvoker.this.world.spawnEntity(☃xxx);
         }
      }

      @Override
      protected SoundEvent getSpellPrepareSound() {
         return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
      }

      @Override
      protected EntitySpellcasterIllager.SpellType getSpellType() {
         return EntitySpellcasterIllager.SpellType.FANGS;
      }
   }

   class AICastingSpell extends EntitySpellcasterIllager.AICastingApell {
      private AICastingSpell() {
      }

      @Override
      public void updateTask() {
         if (EntityEvoker.this.getAttackTarget() != null) {
            EntityEvoker.this.getLookHelper()
               .setLookPositionWithEntity(
                  EntityEvoker.this.getAttackTarget(), EntityEvoker.this.getHorizontalFaceSpeed(), EntityEvoker.this.getVerticalFaceSpeed()
               );
         } else if (EntityEvoker.this.getWololoTarget() != null) {
            EntityEvoker.this.getLookHelper()
               .setLookPositionWithEntity(
                  EntityEvoker.this.getWololoTarget(), EntityEvoker.this.getHorizontalFaceSpeed(), EntityEvoker.this.getVerticalFaceSpeed()
               );
         }
      }
   }

   class AISummonSpell extends EntitySpellcasterIllager.AIUseSpell {
      private AISummonSpell() {
      }

      @Override
      public boolean shouldExecute() {
         if (!super.shouldExecute()) {
            return false;
         } else {
            int ☃ = EntityEvoker.this.world.getEntitiesWithinAABB(EntityVex.class, EntityEvoker.this.getEntityBoundingBox().grow(16.0)).size();
            return EntityEvoker.this.rand.nextInt(8) + 1 > ☃;
         }
      }

      @Override
      protected int getCastingTime() {
         return 100;
      }

      @Override
      protected int getCastingInterval() {
         return 340;
      }

      @Override
      protected void castSpell() {
         for (int ☃ = 0; ☃ < 3; ☃++) {
            BlockPos ☃x = new BlockPos(EntityEvoker.this).add(-2 + EntityEvoker.this.rand.nextInt(5), 1, -2 + EntityEvoker.this.rand.nextInt(5));
            EntityVex ☃xx = new EntityVex(EntityEvoker.this.world);
            ☃xx.moveToBlockPosAndAngles(☃x, 0.0F, 0.0F);
            ☃xx.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(☃x), null);
            ☃xx.setOwner(EntityEvoker.this);
            ☃xx.setBoundOrigin(☃x);
            ☃xx.setLimitedLife(20 * (30 + EntityEvoker.this.rand.nextInt(90)));
            EntityEvoker.this.world.spawnEntity(☃xx);
         }
      }

      @Override
      protected SoundEvent getSpellPrepareSound() {
         return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
      }

      @Override
      protected EntitySpellcasterIllager.SpellType getSpellType() {
         return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
      }
   }

   public class AIWololoSpell extends EntitySpellcasterIllager.AIUseSpell {
      final Predicate<EntitySheep> wololoSelector = new Predicate<EntitySheep>() {
         public boolean apply(EntitySheep var1) {
            return ☃.getFleeceColor() == EnumDyeColor.BLUE;
         }
      };

      @Override
      public boolean shouldExecute() {
         if (EntityEvoker.this.getAttackTarget() != null) {
            return false;
         } else if (EntityEvoker.this.isSpellcasting()) {
            return false;
         } else if (EntityEvoker.this.ticksExisted < this.spellCooldown) {
            return false;
         } else if (!EntityEvoker.this.world.getGameRules().getBoolean("mobGriefing")) {
            return false;
         } else {
            List<EntitySheep> ☃ = EntityEvoker.this.world
               .getEntitiesWithinAABB(EntitySheep.class, EntityEvoker.this.getEntityBoundingBox().grow(16.0, 4.0, 16.0), this.wololoSelector);
            if (☃.isEmpty()) {
               return false;
            } else {
               EntityEvoker.this.setWololoTarget(☃.get(EntityEvoker.this.rand.nextInt(☃.size())));
               return true;
            }
         }
      }

      @Override
      public boolean shouldContinueExecuting() {
         return EntityEvoker.this.getWololoTarget() != null && this.spellWarmup > 0;
      }

      @Override
      public void resetTask() {
         super.resetTask();
         EntityEvoker.this.setWololoTarget(null);
      }

      @Override
      protected void castSpell() {
         EntitySheep ☃ = EntityEvoker.this.getWololoTarget();
         if (☃ != null && ☃.isEntityAlive()) {
            ☃.setFleeceColor(EnumDyeColor.RED);
         }
      }

      @Override
      protected int getCastWarmupTime() {
         return 40;
      }

      @Override
      protected int getCastingTime() {
         return 60;
      }

      @Override
      protected int getCastingInterval() {
         return 140;
      }

      @Override
      protected SoundEvent getSpellPrepareSound() {
         return SoundEvents.EVOCATION_ILLAGER_PREPARE_WOLOLO;
      }

      @Override
      protected EntitySpellcasterIllager.SpellType getSpellType() {
         return EntitySpellcasterIllager.SpellType.WOLOLO;
      }
   }
}
