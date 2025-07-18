package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySilverfish extends EntityMob {
   private EntitySilverfish.AISummonSilverfish summonSilverfish;

   public EntitySilverfish(World var1) {
      super(☃);
      this.setSize(0.4F, 0.3F);
   }

   public static void registerFixesSilverfish(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntitySilverfish.class);
   }

   @Override
   protected void initEntityAI() {
      this.summonSilverfish = new EntitySilverfish.AISummonSilverfish(this);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(3, this.summonSilverfish);
      this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, false));
      this.tasks.addTask(5, new EntitySilverfish.AIHideInStone(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
   }

   @Override
   public double getYOffset() {
      return 0.1;
   }

   @Override
   public float getEyeHeight() {
      return 0.1F;
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0);
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_SILVERFISH_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_SILVERFISH_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         if ((☃ instanceof EntityDamageSource || ☃ == DamageSource.MAGIC) && this.summonSilverfish != null) {
            this.summonSilverfish.notifyHurt();
         }

         return super.attackEntityFrom(☃, ☃);
      }
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_SILVERFISH;
   }

   @Override
   public void onUpdate() {
      this.renderYawOffset = this.rotationYaw;
      super.onUpdate();
   }

   @Override
   public void setRenderYawOffset(float var1) {
      this.rotationYaw = ☃;
      super.setRenderYawOffset(☃);
   }

   @Override
   public float getBlockPathWeight(BlockPos var1) {
      return this.world.getBlockState(☃.down()).getBlock() == Blocks.STONE ? 10.0F : super.getBlockPathWeight(☃);
   }

   @Override
   protected boolean isValidLightLevel() {
      return true;
   }

   @Override
   public boolean getCanSpawnHere() {
      if (super.getCanSpawnHere()) {
         EntityPlayer ☃ = this.world.getNearestPlayerNotCreative(this, 5.0);
         return ☃ == null;
      } else {
         return false;
      }
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   static class AIHideInStone extends EntityAIWander {
      private EnumFacing facing;
      private boolean doMerge;

      public AIHideInStone(EntitySilverfish var1) {
         super(☃, 1.0, 10);
         this.setMutexBits(1);
      }

      @Override
      public boolean shouldExecute() {
         if (this.entity.getAttackTarget() != null) {
            return false;
         } else if (!this.entity.getNavigator().noPath()) {
            return false;
         } else {
            Random ☃ = this.entity.getRNG();
            if (this.entity.world.getGameRules().getBoolean("mobGriefing") && ☃.nextInt(10) == 0) {
               this.facing = EnumFacing.random(☃);
               BlockPos ☃x = new BlockPos(this.entity.posX, this.entity.posY + 0.5, this.entity.posZ).offset(this.facing);
               IBlockState ☃xx = this.entity.world.getBlockState(☃x);
               if (BlockSilverfish.canContainSilverfish(☃xx)) {
                  this.doMerge = true;
                  return true;
               }
            }

            this.doMerge = false;
            return super.shouldExecute();
         }
      }

      @Override
      public boolean shouldContinueExecuting() {
         return this.doMerge ? false : super.shouldContinueExecuting();
      }

      @Override
      public void startExecuting() {
         if (!this.doMerge) {
            super.startExecuting();
         } else {
            World ☃ = this.entity.world;
            BlockPos ☃x = new BlockPos(this.entity.posX, this.entity.posY + 0.5, this.entity.posZ).offset(this.facing);
            IBlockState ☃xx = ☃.getBlockState(☃x);
            if (BlockSilverfish.canContainSilverfish(☃xx)) {
               ☃.setBlockState(☃x, Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(☃xx)), 3);
               this.entity.spawnExplosionParticle();
               this.entity.setDead();
            }
         }
      }
   }

   static class AISummonSilverfish extends EntityAIBase {
      private final EntitySilverfish silverfish;
      private int lookForFriends;

      public AISummonSilverfish(EntitySilverfish var1) {
         this.silverfish = ☃;
      }

      public void notifyHurt() {
         if (this.lookForFriends == 0) {
            this.lookForFriends = 20;
         }
      }

      @Override
      public boolean shouldExecute() {
         return this.lookForFriends > 0;
      }

      @Override
      public void updateTask() {
         this.lookForFriends--;
         if (this.lookForFriends <= 0) {
            World ☃ = this.silverfish.world;
            Random ☃x = this.silverfish.getRNG();
            BlockPos ☃xx = new BlockPos(this.silverfish);

            for (int ☃xxx = 0; ☃xxx <= 5 && ☃xxx >= -5; ☃xxx = (☃xxx <= 0 ? 1 : 0) - ☃xxx) {
               for (int ☃xxxx = 0; ☃xxxx <= 10 && ☃xxxx >= -10; ☃xxxx = (☃xxxx <= 0 ? 1 : 0) - ☃xxxx) {
                  for (int ☃xxxxx = 0; ☃xxxxx <= 10 && ☃xxxxx >= -10; ☃xxxxx = (☃xxxxx <= 0 ? 1 : 0) - ☃xxxxx) {
                     BlockPos ☃xxxxxx = ☃xx.add(☃xxxx, ☃xxx, ☃xxxxx);
                     IBlockState ☃xxxxxxx = ☃.getBlockState(☃xxxxxx);
                     if (☃xxxxxxx.getBlock() == Blocks.MONSTER_EGG) {
                        if (☃.getGameRules().getBoolean("mobGriefing")) {
                           ☃.destroyBlock(☃xxxxxx, true);
                        } else {
                           ☃.setBlockState(☃xxxxxx, ☃xxxxxxx.getValue(BlockSilverfish.VARIANT).getModelBlock(), 3);
                        }

                        if (☃x.nextBoolean()) {
                           return;
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
