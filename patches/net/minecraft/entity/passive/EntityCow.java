package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityCow extends EntityAnimal {
   public EntityCow(World var1) {
      super(☃);
      this.setSize(0.9F, 1.4F);
   }

   public static void registerFixesCow(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityCow.class);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 2.0));
      this.tasks.addTask(2, new EntityAIMate(this, 1.0));
      this.tasks.addTask(3, new EntityAITempt(this, 1.25, Items.WHEAT, false));
      this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25));
      this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2F);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_COW_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_COW_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_COW_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_COW;
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.getItem() == Items.BUCKET && !☃.capabilities.isCreativeMode && !this.isChild()) {
         ☃.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
         ☃.shrink(1);
         if (☃.isEmpty()) {
            ☃.setHeldItem(☃, new ItemStack(Items.MILK_BUCKET));
         } else if (!☃.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) {
            ☃.dropItem(new ItemStack(Items.MILK_BUCKET), false);
         }

         return true;
      } else {
         return super.processInteract(☃, ☃);
      }
   }

   public EntityCow createChild(EntityAgeable var1) {
      return new EntityCow(this.world);
   }

   @Override
   public float getEyeHeight() {
      return this.isChild() ? this.height : 1.3F;
   }
}
