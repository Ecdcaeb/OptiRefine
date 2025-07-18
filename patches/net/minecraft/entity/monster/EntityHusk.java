package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityHusk extends EntityZombie {
   public EntityHusk(World var1) {
      super(☃);
   }

   public static void registerFixesHusk(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityHusk.class);
   }

   @Override
   public boolean getCanSpawnHere() {
      return super.getCanSpawnHere() && this.world.canSeeSky(new BlockPos(this));
   }

   @Override
   protected boolean shouldBurnInDay() {
      return false;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_HUSK_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_HUSK_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_HUSK_DEATH;
   }

   @Override
   protected SoundEvent getStepSound() {
      return SoundEvents.ENTITY_HUSK_STEP;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_HUSK;
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      boolean ☃ = super.attackEntityAsMob(☃);
      if (☃ && this.getHeldItemMainhand().isEmpty() && ☃ instanceof EntityLivingBase) {
         float ☃x = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
         ((EntityLivingBase)☃).addPotionEffect(new PotionEffect(MobEffects.HUNGER, 140 * (int)☃x));
      }

      return ☃;
   }

   @Override
   protected ItemStack getSkullDrop() {
      return ItemStack.EMPTY;
   }
}
