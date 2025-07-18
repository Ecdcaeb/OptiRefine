package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityDonkey extends AbstractChestHorse {
   public EntityDonkey(World var1) {
      super(☃);
   }

   public static void registerFixesDonkey(DataFixer var0) {
      AbstractChestHorse.registerFixesAbstractChestHorse(☃, EntityDonkey.class);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_DONKEY;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      super.getAmbientSound();
      return SoundEvents.ENTITY_DONKEY_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      super.getDeathSound();
      return SoundEvents.ENTITY_DONKEY_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      super.getHurtSound(☃);
      return SoundEvents.ENTITY_DONKEY_HURT;
   }

   @Override
   public boolean canMateWith(EntityAnimal var1) {
      if (☃ == this) {
         return false;
      } else {
         return !(☃ instanceof EntityDonkey) && !(☃ instanceof EntityHorse) ? false : this.canMate() && ((AbstractHorse)☃).canMate();
      }
   }

   @Override
   public EntityAgeable createChild(EntityAgeable var1) {
      AbstractHorse ☃ = (AbstractHorse)(☃ instanceof EntityHorse ? new EntityMule(this.world) : new EntityDonkey(this.world));
      this.setOffspringAttributes(☃, ☃);
      return ☃;
   }
}
