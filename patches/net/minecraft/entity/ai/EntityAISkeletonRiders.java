package net.minecraft.entity.ai;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;

public class EntityAISkeletonRiders extends EntityAIBase {
   private final EntitySkeletonHorse horse;

   public EntityAISkeletonRiders(EntitySkeletonHorse var1) {
      this.horse = ☃;
   }

   @Override
   public boolean shouldExecute() {
      return this.horse.world.isAnyPlayerWithinRangeAt(this.horse.posX, this.horse.posY, this.horse.posZ, 10.0);
   }

   @Override
   public void updateTask() {
      DifficultyInstance ☃ = this.horse.world.getDifficultyForLocation(new BlockPos(this.horse));
      this.horse.setTrap(false);
      this.horse.setHorseTamed(true);
      this.horse.setGrowingAge(0);
      this.horse.world.addWeatherEffect(new EntityLightningBolt(this.horse.world, this.horse.posX, this.horse.posY, this.horse.posZ, true));
      EntitySkeleton ☃x = this.createSkeleton(☃, this.horse);
      ☃x.startRiding(this.horse);

      for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
         AbstractHorse ☃xxx = this.createHorse(☃);
         EntitySkeleton ☃xxxx = this.createSkeleton(☃, ☃xxx);
         ☃xxxx.startRiding(☃xxx);
         ☃xxx.addVelocity(this.horse.getRNG().nextGaussian() * 0.5, 0.0, this.horse.getRNG().nextGaussian() * 0.5);
      }
   }

   private AbstractHorse createHorse(DifficultyInstance var1) {
      EntitySkeletonHorse ☃ = new EntitySkeletonHorse(this.horse.world);
      ☃.onInitialSpawn(☃, null);
      ☃.setPosition(this.horse.posX, this.horse.posY, this.horse.posZ);
      ☃.hurtResistantTime = 60;
      ☃.enablePersistence();
      ☃.setHorseTamed(true);
      ☃.setGrowingAge(0);
      ☃.world.spawnEntity(☃);
      return ☃;
   }

   private EntitySkeleton createSkeleton(DifficultyInstance var1, AbstractHorse var2) {
      EntitySkeleton ☃ = new EntitySkeleton(☃.world);
      ☃.onInitialSpawn(☃, null);
      ☃.setPosition(☃.posX, ☃.posY, ☃.posZ);
      ☃.hurtResistantTime = 60;
      ☃.enablePersistence();
      if (☃.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
         ☃.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
      }

      ☃.setItemStackToSlot(
         EntityEquipmentSlot.MAINHAND,
         EnchantmentHelper.addRandomEnchantment(
            ☃.getRNG(), ☃.getHeldItemMainhand(), (int)(5.0F + ☃.getClampedAdditionalDifficulty() * ☃.getRNG().nextInt(18)), false
         )
      );
      ☃.setItemStackToSlot(
         EntityEquipmentSlot.HEAD,
         EnchantmentHelper.addRandomEnchantment(
            ☃.getRNG(), ☃.getItemStackFromSlot(EntityEquipmentSlot.HEAD), (int)(5.0F + ☃.getClampedAdditionalDifficulty() * ☃.getRNG().nextInt(18)), false
         )
      );
      ☃.world.spawnEntity(☃);
      return ☃;
   }
}
