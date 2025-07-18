package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWitherSkeleton extends AbstractSkeleton {
   public EntityWitherSkeleton(World var1) {
      super(☃);
      this.setSize(0.7F, 2.4F);
      this.isImmuneToFire = true;
   }

   public static void registerFixesWitherSkeleton(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityWitherSkeleton.class);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_WITHER_SKELETON;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
   }

   @Override
   SoundEvent getStepSound() {
      return SoundEvents.ENTITY_WITHER_SKELETON_STEP;
   }

   @Override
   public void onDeath(DamageSource var1) {
      super.onDeath(☃);
      if (☃.getTrueSource() instanceof EntityCreeper) {
         EntityCreeper ☃ = (EntityCreeper)☃.getTrueSource();
         if (☃.getPowered() && ☃.ableToCauseSkullDrop()) {
            ☃.incrementDroppedSkulls();
            this.entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
         }
      }
   }

   @Override
   protected void setEquipmentBasedOnDifficulty(DifficultyInstance var1) {
      this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
   }

   @Override
   protected void setEnchantmentBasedOnDifficulty(DifficultyInstance var1) {
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      IEntityLivingData ☃ = super.onInitialSpawn(☃, ☃);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
      this.setCombatTask();
      return ☃;
   }

   @Override
   public float getEyeHeight() {
      return 2.1F;
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      if (!super.attackEntityAsMob(☃)) {
         return false;
      } else {
         if (☃ instanceof EntityLivingBase) {
            ((EntityLivingBase)☃).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
         }

         return true;
      }
   }

   @Override
   protected EntityArrow getArrow(float var1) {
      EntityArrow ☃ = super.getArrow(☃);
      ☃.setFire(100);
      return ☃;
   }
}
