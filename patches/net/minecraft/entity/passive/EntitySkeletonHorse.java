package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySkeletonHorse extends AbstractHorse {
   private final EntityAISkeletonRiders skeletonTrapAI = new EntityAISkeletonRiders(this);
   private boolean skeletonTrap;
   private int skeletonTrapTime;

   public EntitySkeletonHorse(World var1) {
      super(☃);
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2F);
      this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
   }

   @Override
   protected SoundEvent getAmbientSound() {
      super.getAmbientSound();
      return SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      super.getDeathSound();
      return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      super.getHurtSound(☃);
      return SoundEvents.ENTITY_SKELETON_HORSE_HURT;
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   @Override
   public double getMountedYOffset() {
      return super.getMountedYOffset() - 0.1875;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_SKELETON_HORSE;
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.isTrap() && this.skeletonTrapTime++ >= 18000) {
         this.setDead();
      }
   }

   public static void registerFixesSkeletonHorse(DataFixer var0) {
      AbstractHorse.registerFixesAbstractHorse(☃, EntitySkeletonHorse.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setBoolean("SkeletonTrap", this.isTrap());
      ☃.setInteger("SkeletonTrapTime", this.skeletonTrapTime);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setTrap(☃.getBoolean("SkeletonTrap"));
      this.skeletonTrapTime = ☃.getInteger("SkeletonTrapTime");
   }

   public boolean isTrap() {
      return this.skeletonTrap;
   }

   public void setTrap(boolean var1) {
      if (☃ != this.skeletonTrap) {
         this.skeletonTrap = ☃;
         if (☃) {
            this.tasks.addTask(1, this.skeletonTrapAI);
         } else {
            this.tasks.removeTask(this.skeletonTrapAI);
         }
      }
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      boolean ☃x = !☃.isEmpty();
      if (☃x && ☃.getItem() == Items.SPAWN_EGG) {
         return super.processInteract(☃, ☃);
      } else if (!this.isTame()) {
         return false;
      } else if (this.isChild()) {
         return super.processInteract(☃, ☃);
      } else if (☃.isSneaking()) {
         this.openGUI(☃);
         return true;
      } else if (this.isBeingRidden()) {
         return super.processInteract(☃, ☃);
      } else {
         if (☃x) {
            if (☃.getItem() == Items.SADDLE && !this.isHorseSaddled()) {
               this.openGUI(☃);
               return true;
            }

            if (☃.interactWithEntity(☃, this, ☃)) {
               return true;
            }
         }

         this.mountTo(☃);
         return true;
      }
   }
}
