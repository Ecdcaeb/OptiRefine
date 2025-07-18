package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMule extends AbstractChestHorse {
   public EntityMule(World var1) {
      super(☃);
   }

   public static void registerFixesMule(DataFixer var0) {
      AbstractChestHorse.registerFixesAbstractChestHorse(☃, EntityMule.class);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_MULE;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      super.getAmbientSound();
      return SoundEvents.ENTITY_MULE_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      super.getDeathSound();
      return SoundEvents.ENTITY_MULE_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      super.getHurtSound(☃);
      return SoundEvents.ENTITY_MULE_HURT;
   }

   @Override
   protected void playChestEquipSound() {
      this.playSound(SoundEvents.ENTITY_MULE_CHEST, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
   }
}
