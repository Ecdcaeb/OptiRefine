package net.minecraft.entity.monster;

import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityCreeper extends EntityMob {
   private static final DataParameter<Integer> STATE = EntityDataManager.createKey(EntityCreeper.class, DataSerializers.VARINT);
   private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(EntityCreeper.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(EntityCreeper.class, DataSerializers.BOOLEAN);
   private int lastActiveTime;
   private int timeSinceIgnited;
   private int fuseTime = 30;
   private int explosionRadius = 3;
   private int droppedSkulls;

   public EntityCreeper(World var1) {
      super(☃);
      this.setSize(0.6F, 1.7F);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAICreeperSwell(this));
      this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityOcelot.class, 6.0F, 1.0, 1.2));
      this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, false));
      this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
      this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
   }

   @Override
   public int getMaxFallHeight() {
      return this.getAttackTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
   }

   @Override
   public void fall(float var1, float var2) {
      super.fall(☃, ☃);
      this.timeSinceIgnited = (int)(this.timeSinceIgnited + ☃ * 1.5F);
      if (this.timeSinceIgnited > this.fuseTime - 5) {
         this.timeSinceIgnited = this.fuseTime - 5;
      }
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(STATE, -1);
      this.dataManager.register(POWERED, false);
      this.dataManager.register(IGNITED, false);
   }

   public static void registerFixesCreeper(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityCreeper.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      if (this.dataManager.get(POWERED)) {
         ☃.setBoolean("powered", true);
      }

      ☃.setShort("Fuse", (short)this.fuseTime);
      ☃.setByte("ExplosionRadius", (byte)this.explosionRadius);
      ☃.setBoolean("ignited", this.hasIgnited());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.dataManager.set(POWERED, ☃.getBoolean("powered"));
      if (☃.hasKey("Fuse", 99)) {
         this.fuseTime = ☃.getShort("Fuse");
      }

      if (☃.hasKey("ExplosionRadius", 99)) {
         this.explosionRadius = ☃.getByte("ExplosionRadius");
      }

      if (☃.getBoolean("ignited")) {
         this.ignite();
      }
   }

   @Override
   public void onUpdate() {
      if (this.isEntityAlive()) {
         this.lastActiveTime = this.timeSinceIgnited;
         if (this.hasIgnited()) {
            this.setCreeperState(1);
         }

         int ☃ = this.getCreeperState();
         if (☃ > 0 && this.timeSinceIgnited == 0) {
            this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
         }

         this.timeSinceIgnited += ☃;
         if (this.timeSinceIgnited < 0) {
            this.timeSinceIgnited = 0;
         }

         if (this.timeSinceIgnited >= this.fuseTime) {
            this.timeSinceIgnited = this.fuseTime;
            this.explode();
         }
      }

      super.onUpdate();
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_CREEPER_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_CREEPER_DEATH;
   }

   @Override
   public void onDeath(DamageSource var1) {
      super.onDeath(☃);
      if (this.world.getGameRules().getBoolean("doMobLoot")) {
         if (☃.getTrueSource() instanceof EntitySkeleton) {
            int ☃ = Item.getIdFromItem(Items.RECORD_13);
            int ☃x = Item.getIdFromItem(Items.RECORD_WAIT);
            int ☃xx = ☃ + this.rand.nextInt(☃x - ☃ + 1);
            this.dropItem(Item.getItemById(☃xx), 1);
         } else if (☃.getTrueSource() instanceof EntityCreeper
            && ☃.getTrueSource() != this
            && ((EntityCreeper)☃.getTrueSource()).getPowered()
            && ((EntityCreeper)☃.getTrueSource()).ableToCauseSkullDrop()) {
            ((EntityCreeper)☃.getTrueSource()).incrementDroppedSkulls();
            this.entityDropItem(new ItemStack(Items.SKULL, 1, 4), 0.0F);
         }
      }
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      return true;
   }

   public boolean getPowered() {
      return this.dataManager.get(POWERED);
   }

   public float getCreeperFlashIntensity(float var1) {
      return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * ☃) / (this.fuseTime - 2);
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_CREEPER;
   }

   public int getCreeperState() {
      return this.dataManager.get(STATE);
   }

   public void setCreeperState(int var1) {
      this.dataManager.set(STATE, ☃);
   }

   @Override
   public void onStruckByLightning(EntityLightningBolt var1) {
      super.onStruckByLightning(☃);
      this.dataManager.set(POWERED, true);
   }

   @Override
   protected boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.getItem() == Items.FLINT_AND_STEEL) {
         this.world
            .playSound(
               ☃, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F
            );
         ☃.swingArm(☃);
         if (!this.world.isRemote) {
            this.ignite();
            ☃.damageItem(1, ☃);
            return true;
         }
      }

      return super.processInteract(☃, ☃);
   }

   private void explode() {
      if (!this.world.isRemote) {
         boolean ☃ = this.world.getGameRules().getBoolean("mobGriefing");
         float ☃x = this.getPowered() ? 2.0F : 1.0F;
         this.dead = true;
         this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * ☃x, ☃);
         this.setDead();
         this.spawnLingeringCloud();
      }
   }

   private void spawnLingeringCloud() {
      Collection<PotionEffect> ☃ = this.getActivePotionEffects();
      if (!☃.isEmpty()) {
         EntityAreaEffectCloud ☃x = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
         ☃x.setRadius(2.5F);
         ☃x.setRadiusOnUse(-0.5F);
         ☃x.setWaitTime(10);
         ☃x.setDuration(☃x.getDuration() / 2);
         ☃x.setRadiusPerTick(-☃x.getRadius() / ☃x.getDuration());

         for (PotionEffect ☃xx : ☃) {
            ☃x.addEffect(new PotionEffect(☃xx));
         }

         this.world.spawnEntity(☃x);
      }
   }

   public boolean hasIgnited() {
      return this.dataManager.get(IGNITED);
   }

   public void ignite() {
      this.dataManager.set(IGNITED, true);
   }

   public boolean ableToCauseSkullDrop() {
      return this.droppedSkulls < 1 && this.world.getGameRules().getBoolean("doMobLoot");
   }

   public void incrementDroppedSkulls() {
      this.droppedSkulls++;
   }
}
