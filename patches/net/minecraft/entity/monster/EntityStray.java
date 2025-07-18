package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityStray extends AbstractSkeleton {
   public EntityStray(World var1) {
      super(☃);
   }

   public static void registerFixesStray(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityStray.class);
   }

   @Override
   public boolean getCanSpawnHere() {
      return super.getCanSpawnHere() && this.world.canSeeSky(new BlockPos(this));
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_STRAY;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_STRAY_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_STRAY_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_STRAY_DEATH;
   }

   @Override
   SoundEvent getStepSound() {
      return SoundEvents.ENTITY_STRAY_STEP;
   }

   @Override
   protected EntityArrow getArrow(float var1) {
      EntityArrow ☃ = super.getArrow(☃);
      if (☃ instanceof EntityTippedArrow) {
         ((EntityTippedArrow)☃).addEffect(new PotionEffect(MobEffects.SLOWNESS, 600));
      }

      return ☃;
   }
}
