package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityElderGuardian extends EntityGuardian {
   public EntityElderGuardian(World var1) {
      super(☃);
      this.setSize(this.width * 2.35F, this.height * 2.35F);
      this.enablePersistence();
      if (this.wander != null) {
         this.wander.setExecutionChance(400);
      }
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3F);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0);
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0);
   }

   public static void registerFixesElderGuardian(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityElderGuardian.class);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_ELDER_GUARDIAN;
   }

   @Override
   public int getAttackDuration() {
      return 60;
   }

   public void setGhost() {
      this.clientSideSpikesAnimation = 1.0F;
      this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT : SoundEvents.ENTITY_ELDERGUARDIAN_AMBIENTLAND;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_HURT : SoundEvents.ENTITY_ELDER_GUARDIAN_HURT_LAND;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH : SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
   }

   @Override
   protected SoundEvent getFlopSound() {
      return SoundEvents.ENTITY_ELDER_GUARDIAN_FLOP;
   }

   @Override
   protected void updateAITasks() {
      super.updateAITasks();
      int ☃ = 1200;
      if ((this.ticksExisted + this.getEntityId()) % 1200 == 0) {
         Potion ☃x = MobEffects.MINING_FATIGUE;
         List<EntityPlayerMP> ☃xx = this.world.getPlayers(EntityPlayerMP.class, new Predicate<EntityPlayerMP>() {
            public boolean apply(@Nullable EntityPlayerMP var1) {
               return EntityElderGuardian.this.getDistanceSq(☃) < 2500.0 && ☃.interactionManager.survivalOrAdventure();
            }
         });
         int ☃xxx = 2;
         int ☃xxxx = 6000;
         int ☃xxxxx = 1200;

         for (EntityPlayerMP ☃xxxxxx : ☃xx) {
            if (!☃xxxxxx.isPotionActive(☃x) || ☃xxxxxx.getActivePotionEffect(☃x).getAmplifier() < 2 || ☃xxxxxx.getActivePotionEffect(☃x).getDuration() < 1200) {
               ☃xxxxxx.connection.sendPacket(new SPacketChangeGameState(10, 0.0F));
               ☃xxxxxx.addPotionEffect(new PotionEffect(☃x, 6000, 2));
            }
         }
      }

      if (!this.hasHome()) {
         this.setHomePosAndDistance(new BlockPos(this), 16);
      }
   }
}
