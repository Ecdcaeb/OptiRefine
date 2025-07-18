package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySkeleton extends AbstractSkeleton {
   public EntitySkeleton(World var1) {
      super(☃);
   }

   public static void registerFixesSkeleton(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntitySkeleton.class);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_SKELETON;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_SKELETON_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_SKELETON_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_SKELETON_DEATH;
   }

   @Override
   SoundEvent getStepSound() {
      return SoundEvents.ENTITY_SKELETON_STEP;
   }

   @Override
   public void onDeath(DamageSource var1) {
      super.onDeath(☃);
      if (☃.getTrueSource() instanceof EntityCreeper) {
         EntityCreeper ☃ = (EntityCreeper)☃.getTrueSource();
         if (☃.getPowered() && ☃.ableToCauseSkullDrop()) {
            ☃.incrementDroppedSkulls();
            this.entityDropItem(new ItemStack(Items.SKULL, 1, 0), 0.0F);
         }
      }
   }

   @Override
   protected EntityArrow getArrow(float var1) {
      ItemStack ☃ = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
      if (☃.getItem() == Items.SPECTRAL_ARROW) {
         EntitySpectralArrow ☃x = new EntitySpectralArrow(this.world, this);
         ☃x.setEnchantmentEffectsFromEntity(this, ☃);
         return ☃x;
      } else {
         EntityArrow ☃x = super.getArrow(☃);
         if (☃.getItem() == Items.TIPPED_ARROW && ☃x instanceof EntityTippedArrow) {
            ((EntityTippedArrow)☃x).setPotionEffect(☃);
         }

         return ☃x;
      }
   }
}
