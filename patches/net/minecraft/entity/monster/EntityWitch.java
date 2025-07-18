package net.minecraft.entity.monster;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWitch extends EntityMob implements IRangedAttackMob {
   private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
   private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25, 0).setSaved(false);
   private static final DataParameter<Boolean> IS_DRINKING = EntityDataManager.createKey(EntityWitch.class, DataSerializers.BOOLEAN);
   private int potionUseTimer;

   public EntityWitch(World var1) {
      super(☃);
      this.setSize(0.6F, 1.95F);
   }

   public static void registerFixesWitch(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityWitch.class);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0, 60, 10.0F));
      this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0));
      this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(3, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.getDataManager().register(IS_DRINKING, false);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_WITCH_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_WITCH_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_WITCH_DEATH;
   }

   public void setDrinkingPotion(boolean var1) {
      this.getDataManager().set(IS_DRINKING, ☃);
   }

   public boolean isDrinkingPotion() {
      return this.getDataManager().get(IS_DRINKING);
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
   }

   @Override
   public void onLivingUpdate() {
      if (!this.world.isRemote) {
         if (this.isDrinkingPotion()) {
            if (this.potionUseTimer-- <= 0) {
               this.setDrinkingPotion(false);
               ItemStack ☃ = this.getHeldItemMainhand();
               this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
               if (☃.getItem() == Items.POTIONITEM) {
                  List<PotionEffect> ☃x = PotionUtils.getEffectsFromStack(☃);
                  if (☃x != null) {
                     for (PotionEffect ☃xx : ☃x) {
                        this.addPotionEffect(new PotionEffect(☃xx));
                     }
                  }
               }

               this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
            }
         } else {
            PotionType ☃ = null;
            if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING)) {
               ☃ = PotionTypes.WATER_BREATHING;
            } else if (this.rand.nextFloat() < 0.15F
               && (this.isBurning() || this.getLastDamageSource() != null && this.getLastDamageSource().isFireDamage())
               && !this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
               ☃ = PotionTypes.FIRE_RESISTANCE;
            } else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
               ☃ = PotionTypes.HEALING;
            } else if (this.rand.nextFloat() < 0.5F
               && this.getAttackTarget() != null
               && !this.isPotionActive(MobEffects.SPEED)
               && this.getAttackTarget().getDistanceSq(this) > 121.0) {
               ☃ = PotionTypes.SWIFTNESS;
            }

            if (☃ != null) {
               this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), ☃));
               this.potionUseTimer = this.getHeldItemMainhand().getMaxItemUseDuration();
               this.setDrinkingPotion(true);
               this.world
                  .playSound(
                     null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F
                  );
               IAttributeInstance ☃x = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
               ☃x.removeModifier(MODIFIER);
               ☃x.applyModifier(MODIFIER);
            }
         }

         if (this.rand.nextFloat() < 7.5E-4F) {
            this.world.setEntityState(this, (byte)15);
         }
      }

      super.onLivingUpdate();
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 15) {
         for (int ☃ = 0; ☃ < this.rand.nextInt(35) + 10; ☃++) {
            this.world
               .spawnParticle(
                  EnumParticleTypes.SPELL_WITCH,
                  this.posX + this.rand.nextGaussian() * 0.13F,
                  this.getEntityBoundingBox().maxY + 0.5 + this.rand.nextGaussian() * 0.13F,
                  this.posZ + this.rand.nextGaussian() * 0.13F,
                  0.0,
                  0.0,
                  0.0
               );
         }
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   @Override
   protected float applyPotionDamageCalculations(DamageSource var1, float var2) {
      ☃ = super.applyPotionDamageCalculations(☃, ☃);
      if (☃.getTrueSource() == this) {
         ☃ = 0.0F;
      }

      if (☃.isMagicDamage()) {
         ☃ = (float)(☃ * 0.15);
      }

      return ☃;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_WITCH;
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLivingBase var1, float var2) {
      if (!this.isDrinkingPotion()) {
         double ☃ = ☃.posY + ☃.getEyeHeight() - 1.1F;
         double ☃x = ☃.posX + ☃.motionX - this.posX;
         double ☃xx = ☃ - this.posY;
         double ☃xxx = ☃.posZ + ☃.motionZ - this.posZ;
         float ☃xxxx = MathHelper.sqrt(☃x * ☃x + ☃xxx * ☃xxx);
         PotionType ☃xxxxx = PotionTypes.HARMING;
         if (☃xxxx >= 8.0F && !☃.isPotionActive(MobEffects.SLOWNESS)) {
            ☃xxxxx = PotionTypes.SLOWNESS;
         } else if (☃.getHealth() >= 8.0F && !☃.isPotionActive(MobEffects.POISON)) {
            ☃xxxxx = PotionTypes.POISON;
         } else if (☃xxxx <= 3.0F && !☃.isPotionActive(MobEffects.WEAKNESS) && this.rand.nextFloat() < 0.25F) {
            ☃xxxxx = PotionTypes.WEAKNESS;
         }

         EntityPotion ☃xxxxxx = new EntityPotion(this.world, this, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), ☃xxxxx));
         ☃xxxxxx.rotationPitch -= -20.0F;
         ☃xxxxxx.shoot(☃x, ☃xx + ☃xxxx * 0.2F, ☃xxx, 0.75F, 8.0F);
         this.world
            .playSound(
               null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F
            );
         this.world.spawnEntity(☃xxxxxx);
      }
   }

   @Override
   public float getEyeHeight() {
      return 1.62F;
   }

   @Override
   public void setSwingingArms(boolean var1) {
   }
}
