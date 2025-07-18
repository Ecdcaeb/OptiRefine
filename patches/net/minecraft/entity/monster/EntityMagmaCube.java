package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMagmaCube extends EntitySlime {
   public EntityMagmaCube(World var1) {
      super(☃);
      this.isImmuneToFire = true;
   }

   public static void registerFixesMagmaCube(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityMagmaCube.class);
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2F);
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   @Override
   public boolean isNotColliding() {
      return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this)
         && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
         && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
   }

   @Override
   protected void setSlimeSize(int var1, boolean var2) {
      super.setSlimeSize(☃, ☃);
      this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(☃ * 3);
   }

   @Override
   public int getBrightnessForRender() {
      return 15728880;
   }

   @Override
   public float getBrightness() {
      return 1.0F;
   }

   @Override
   protected EnumParticleTypes getParticleType() {
      return EnumParticleTypes.FLAME;
   }

   @Override
   protected EntitySlime createInstance() {
      return new EntityMagmaCube(this.world);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return this.isSmallSlime() ? LootTableList.EMPTY : LootTableList.ENTITIES_MAGMA_CUBE;
   }

   @Override
   public boolean isBurning() {
      return false;
   }

   @Override
   protected int getJumpDelay() {
      return super.getJumpDelay() * 4;
   }

   @Override
   protected void alterSquishAmount() {
      this.squishAmount *= 0.9F;
   }

   @Override
   protected void jump() {
      this.motionY = 0.42F + this.getSlimeSize() * 0.1F;
      this.isAirBorne = true;
   }

   @Override
   protected void handleJumpLava() {
      this.motionY = 0.22F + this.getSlimeSize() * 0.05F;
      this.isAirBorne = true;
   }

   @Override
   public void fall(float var1, float var2) {
   }

   @Override
   protected boolean canDamagePlayer() {
      return true;
   }

   @Override
   protected int getAttackStrength() {
      return super.getAttackStrength() + 2;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_HURT : SoundEvents.ENTITY_MAGMACUBE_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_DEATH : SoundEvents.ENTITY_MAGMACUBE_DEATH;
   }

   @Override
   protected SoundEvent getSquishSound() {
      return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_MAGMACUBE_SQUISH : SoundEvents.ENTITY_MAGMACUBE_SQUISH;
   }

   @Override
   protected SoundEvent getJumpSound() {
      return SoundEvents.ENTITY_MAGMACUBE_JUMP;
   }
}
