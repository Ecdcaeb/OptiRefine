package net.minecraft.entity;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.CombatRules;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EntityLivingBase extends Entity {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final UUID SPRINTING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
   private static final AttributeModifier SPRINTING_SPEED_BOOST = new AttributeModifier(SPRINTING_SPEED_BOOST_ID, "Sprinting speed boost", 0.3F, 2)
      .setSaved(false);
   protected static final DataParameter<Byte> HAND_STATES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BYTE);
   private static final DataParameter<Float> HEALTH = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
   private static final DataParameter<Integer> POTION_EFFECTS = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
   private static final DataParameter<Boolean> HIDE_PARTICLES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> ARROW_COUNT_IN_ENTITY = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
   private AbstractAttributeMap attributeMap;
   private final CombatTracker combatTracker = new CombatTracker(this);
   private final Map<Potion, PotionEffect> activePotionsMap = Maps.newHashMap();
   private final NonNullList<ItemStack> handInventory = NonNullList.withSize(2, ItemStack.EMPTY);
   private final NonNullList<ItemStack> armorArray = NonNullList.withSize(4, ItemStack.EMPTY);
   public boolean isSwingInProgress;
   public EnumHand swingingHand;
   public int swingProgressInt;
   public int arrowHitTimer;
   public int hurtTime;
   public int maxHurtTime;
   public float attackedAtYaw;
   public int deathTime;
   public float prevSwingProgress;
   public float swingProgress;
   protected int ticksSinceLastSwing;
   public float prevLimbSwingAmount;
   public float limbSwingAmount;
   public float limbSwing;
   public int maxHurtResistantTime = 20;
   public float prevCameraPitch;
   public float cameraPitch;
   public float randomUnused2;
   public float randomUnused1;
   public float renderYawOffset;
   public float prevRenderYawOffset;
   public float rotationYawHead;
   public float prevRotationYawHead;
   public float jumpMovementFactor = 0.02F;
   protected EntityPlayer attackingPlayer;
   protected int recentlyHit;
   protected boolean dead;
   protected int idleTime;
   protected float prevOnGroundSpeedFactor;
   protected float onGroundSpeedFactor;
   protected float movedDistance;
   protected float prevMovedDistance;
   protected float unused180;
   protected int scoreValue;
   protected float lastDamage;
   protected boolean isJumping;
   public float moveStrafing;
   public float moveVertical;
   public float moveForward;
   public float randomYawVelocity;
   protected int newPosRotationIncrements;
   protected double interpTargetX;
   protected double interpTargetY;
   protected double interpTargetZ;
   protected double interpTargetYaw;
   protected double interpTargetPitch;
   private boolean potionsNeedUpdate = true;
   private EntityLivingBase revengeTarget;
   private int revengeTimer;
   private EntityLivingBase lastAttackedEntity;
   private int lastAttackedEntityTime;
   private float landMovementFactor;
   private int jumpTicks;
   private float absorptionAmount;
   protected ItemStack activeItemStack = ItemStack.EMPTY;
   protected int activeItemStackUseCount;
   protected int ticksElytraFlying;
   private BlockPos prevBlockpos;
   private DamageSource lastDamageSource;
   private long lastDamageStamp;

   @Override
   public void onKillCommand() {
      this.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
   }

   public EntityLivingBase(World var1) {
      super(☃);
      this.applyEntityAttributes();
      this.setHealth(this.getMaxHealth());
      this.preventEntitySpawning = true;
      this.randomUnused1 = (float)((Math.random() + 1.0) * 0.01F);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.randomUnused2 = (float)Math.random() * 12398.0F;
      this.rotationYaw = (float)(Math.random() * (float) (Math.PI * 2));
      this.rotationYawHead = this.rotationYaw;
      this.stepHeight = 0.6F;
   }

   @Override
   protected void entityInit() {
      this.dataManager.register(HAND_STATES, (byte)0);
      this.dataManager.register(POTION_EFFECTS, 0);
      this.dataManager.register(HIDE_PARTICLES, false);
      this.dataManager.register(ARROW_COUNT_IN_ENTITY, 0);
      this.dataManager.register(HEALTH, 1.0F);
   }

   protected void applyEntityAttributes() {
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
   }

   @Override
   protected void updateFallState(double var1, boolean var3, IBlockState var4, BlockPos var5) {
      if (!this.isInWater()) {
         this.handleWaterMovement();
      }

      if (!this.world.isRemote && this.fallDistance > 3.0F && ☃) {
         float ☃ = MathHelper.ceil(this.fallDistance - 3.0F);
         if (☃.getMaterial() != Material.AIR) {
            double ☃x = Math.min((double)(0.2F + ☃ / 15.0F), 2.5);
            int ☃xx = (int)(150.0 * ☃x);
            ((WorldServer)this.world)
               .spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, ☃xx, 0.0, 0.0, 0.0, 0.15F, Block.getStateId(☃));
         }
      }

      super.updateFallState(☃, ☃, ☃, ☃);
   }

   public boolean canBreatheUnderwater() {
      return false;
   }

   @Override
   public void onEntityUpdate() {
      this.prevSwingProgress = this.swingProgress;
      super.onEntityUpdate();
      this.world.profiler.startSection("livingEntityBaseTick");
      boolean ☃ = this instanceof EntityPlayer;
      if (this.isEntityAlive()) {
         if (this.isEntityInsideOpaqueBlock()) {
            this.attackEntityFrom(DamageSource.IN_WALL, 1.0F);
         } else if (☃ && !this.world.getWorldBorder().contains(this.getEntityBoundingBox())) {
            double ☃x = this.world.getWorldBorder().getClosestDistance(this) + this.world.getWorldBorder().getDamageBuffer();
            if (☃x < 0.0) {
               double ☃xx = this.world.getWorldBorder().getDamageAmount();
               if (☃xx > 0.0) {
                  this.attackEntityFrom(DamageSource.IN_WALL, Math.max(1, MathHelper.floor(-☃x * ☃xx)));
               }
            }
         }
      }

      if (this.isImmuneToFire() || this.world.isRemote) {
         this.extinguish();
      }

      boolean ☃x = ☃ && ((EntityPlayer)this).capabilities.disableDamage;
      if (this.isEntityAlive()) {
         if (!this.isInsideOfMaterial(Material.WATER)) {
            this.setAir(300);
         } else {
            if (!this.canBreatheUnderwater() && !this.isPotionActive(MobEffects.WATER_BREATHING) && !☃x) {
               this.setAir(this.decreaseAirSupply(this.getAir()));
               if (this.getAir() == -20) {
                  this.setAir(0);

                  for (int ☃xx = 0; ☃xx < 8; ☃xx++) {
                     float ☃xxx = this.rand.nextFloat() - this.rand.nextFloat();
                     float ☃xxxx = this.rand.nextFloat() - this.rand.nextFloat();
                     float ☃xxxxx = this.rand.nextFloat() - this.rand.nextFloat();
                     this.world
                        .spawnParticle(
                           EnumParticleTypes.WATER_BUBBLE, this.posX + ☃xxx, this.posY + ☃xxxx, this.posZ + ☃xxxxx, this.motionX, this.motionY, this.motionZ
                        );
                  }

                  this.attackEntityFrom(DamageSource.DROWN, 2.0F);
               }
            }

            if (!this.world.isRemote && this.isRiding() && this.getRidingEntity() instanceof EntityLivingBase) {
               this.dismountRidingEntity();
            }
         }

         if (!this.world.isRemote) {
            BlockPos ☃xx = new BlockPos(this);
            if (!Objects.equal(this.prevBlockpos, ☃xx)) {
               this.prevBlockpos = ☃xx;
               this.frostWalk(☃xx);
            }
         }
      }

      if (this.isEntityAlive() && this.isWet()) {
         this.extinguish();
      }

      this.prevCameraPitch = this.cameraPitch;
      if (this.hurtTime > 0) {
         this.hurtTime--;
      }

      if (this.hurtResistantTime > 0 && !(this instanceof EntityPlayerMP)) {
         this.hurtResistantTime--;
      }

      if (this.getHealth() <= 0.0F) {
         this.onDeathUpdate();
      }

      if (this.recentlyHit > 0) {
         this.recentlyHit--;
      } else {
         this.attackingPlayer = null;
      }

      if (this.lastAttackedEntity != null && !this.lastAttackedEntity.isEntityAlive()) {
         this.lastAttackedEntity = null;
      }

      if (this.revengeTarget != null) {
         if (!this.revengeTarget.isEntityAlive()) {
            this.setRevengeTarget(null);
         } else if (this.ticksExisted - this.revengeTimer > 100) {
            this.setRevengeTarget(null);
         }
      }

      this.updatePotionEffects();
      this.prevMovedDistance = this.movedDistance;
      this.prevRenderYawOffset = this.renderYawOffset;
      this.prevRotationYawHead = this.rotationYawHead;
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
      this.world.profiler.endSection();
   }

   protected void frostWalk(BlockPos var1) {
      int ☃ = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, this);
      if (☃ > 0) {
         EnchantmentFrostWalker.freezeNearby(this, this.world, ☃, ☃);
      }
   }

   public boolean isChild() {
      return false;
   }

   protected void onDeathUpdate() {
      this.deathTime++;
      if (this.deathTime == 20) {
         if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
            int ☃ = this.getExperiencePoints(this.attackingPlayer);

            while (☃ > 0) {
               int ☃x = EntityXPOrb.getXPSplit(☃);
               ☃ -= ☃x;
               this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, ☃x));
            }
         }

         this.setDead();

         for (int ☃ = 0; ☃ < 20; ☃++) {
            double ☃x = this.rand.nextGaussian() * 0.02;
            double ☃xx = this.rand.nextGaussian() * 0.02;
            double ☃xxx = this.rand.nextGaussian() * 0.02;
            this.world
               .spawnParticle(
                  EnumParticleTypes.EXPLOSION_NORMAL,
                  this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  this.posY + this.rand.nextFloat() * this.height,
                  this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  ☃x,
                  ☃xx,
                  ☃xxx
               );
         }
      }
   }

   protected boolean canDropLoot() {
      return !this.isChild();
   }

   protected int decreaseAirSupply(int var1) {
      int ☃ = EnchantmentHelper.getRespirationModifier(this);
      return ☃ > 0 && this.rand.nextInt(☃ + 1) > 0 ? ☃ : ☃ - 1;
   }

   protected int getExperiencePoints(EntityPlayer var1) {
      return 0;
   }

   protected boolean isPlayer() {
      return false;
   }

   public Random getRNG() {
      return this.rand;
   }

   @Nullable
   public EntityLivingBase getRevengeTarget() {
      return this.revengeTarget;
   }

   public int getRevengeTimer() {
      return this.revengeTimer;
   }

   public void setRevengeTarget(@Nullable EntityLivingBase var1) {
      this.revengeTarget = ☃;
      this.revengeTimer = this.ticksExisted;
   }

   public EntityLivingBase getLastAttackedEntity() {
      return this.lastAttackedEntity;
   }

   public int getLastAttackedEntityTime() {
      return this.lastAttackedEntityTime;
   }

   public void setLastAttackedEntity(Entity var1) {
      if (☃ instanceof EntityLivingBase) {
         this.lastAttackedEntity = (EntityLivingBase)☃;
      } else {
         this.lastAttackedEntity = null;
      }

      this.lastAttackedEntityTime = this.ticksExisted;
   }

   public int getIdleTime() {
      return this.idleTime;
   }

   protected void playEquipSound(ItemStack var1) {
      if (!☃.isEmpty()) {
         SoundEvent ☃ = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
         Item ☃x = ☃.getItem();
         if (☃x instanceof ItemArmor) {
            ☃ = ((ItemArmor)☃x).getArmorMaterial().getSoundEvent();
         } else if (☃x == Items.ELYTRA) {
            ☃ = SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA;
         }

         this.playSound(☃, 1.0F, 1.0F);
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setFloat("Health", this.getHealth());
      ☃.setShort("HurtTime", (short)this.hurtTime);
      ☃.setInteger("HurtByTimestamp", this.revengeTimer);
      ☃.setShort("DeathTime", (short)this.deathTime);
      ☃.setFloat("AbsorptionAmount", this.getAbsorptionAmount());

      for (EntityEquipmentSlot ☃ : EntityEquipmentSlot.values()) {
         ItemStack ☃x = this.getItemStackFromSlot(☃);
         if (!☃x.isEmpty()) {
            this.getAttributeMap().removeAttributeModifiers(☃x.getAttributeModifiers(☃));
         }
      }

      ☃.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));

      for (EntityEquipmentSlot ☃x : EntityEquipmentSlot.values()) {
         ItemStack ☃xx = this.getItemStackFromSlot(☃x);
         if (!☃xx.isEmpty()) {
            this.getAttributeMap().applyAttributeModifiers(☃xx.getAttributeModifiers(☃x));
         }
      }

      if (!this.activePotionsMap.isEmpty()) {
         NBTTagList ☃xx = new NBTTagList();

         for (PotionEffect ☃xxx : this.activePotionsMap.values()) {
            ☃xx.appendTag(☃xxx.writeCustomPotionEffectToNBT(new NBTTagCompound()));
         }

         ☃.setTag("ActiveEffects", ☃xx);
      }

      ☃.setBoolean("FallFlying", this.isElytraFlying());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      this.setAbsorptionAmount(☃.getFloat("AbsorptionAmount"));
      if (☃.hasKey("Attributes", 9) && this.world != null && !this.world.isRemote) {
         SharedMonsterAttributes.setAttributeModifiers(this.getAttributeMap(), ☃.getTagList("Attributes", 10));
      }

      if (☃.hasKey("ActiveEffects", 9)) {
         NBTTagList ☃ = ☃.getTagList("ActiveEffects", 10);

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
            PotionEffect ☃xxx = PotionEffect.readCustomPotionEffectFromNBT(☃xx);
            if (☃xxx != null) {
               this.activePotionsMap.put(☃xxx.getPotion(), ☃xxx);
            }
         }
      }

      if (☃.hasKey("Health", 99)) {
         this.setHealth(☃.getFloat("Health"));
      }

      this.hurtTime = ☃.getShort("HurtTime");
      this.deathTime = ☃.getShort("DeathTime");
      this.revengeTimer = ☃.getInteger("HurtByTimestamp");
      if (☃.hasKey("Team", 8)) {
         String ☃ = ☃.getString("Team");
         boolean ☃xx = this.world.getScoreboard().addPlayerToTeam(this.getCachedUniqueIdString(), ☃);
         if (!☃xx) {
            LOGGER.warn("Unable to add mob to team \"" + ☃ + "\" (that team probably doesn't exist)");
         }
      }

      if (☃.getBoolean("FallFlying")) {
         this.setFlag(7, true);
      }
   }

   protected void updatePotionEffects() {
      Iterator<Potion> ☃ = this.activePotionsMap.keySet().iterator();

      try {
         while (☃.hasNext()) {
            Potion ☃x = ☃.next();
            PotionEffect ☃xx = this.activePotionsMap.get(☃x);
            if (!☃xx.onUpdate(this)) {
               if (!this.world.isRemote) {
                  ☃.remove();
                  this.onFinishedPotionEffect(☃xx);
               }
            } else if (☃xx.getDuration() % 600 == 0) {
               this.onChangedPotionEffect(☃xx, false);
            }
         }
      } catch (ConcurrentModificationException var11) {
      }

      if (this.potionsNeedUpdate) {
         if (!this.world.isRemote) {
            this.updatePotionMetadata();
         }

         this.potionsNeedUpdate = false;
      }

      int ☃x = this.dataManager.get(POTION_EFFECTS);
      boolean ☃xx = this.dataManager.get(HIDE_PARTICLES);
      if (☃x > 0) {
         boolean ☃xxx;
         if (this.isInvisible()) {
            ☃xxx = this.rand.nextInt(15) == 0;
         } else {
            ☃xxx = this.rand.nextBoolean();
         }

         if (☃xx) {
            ☃xxx &= this.rand.nextInt(5) == 0;
         }

         if (☃xxx && ☃x > 0) {
            double ☃xxxx = (☃x >> 16 & 0xFF) / 255.0;
            double ☃xxxxx = (☃x >> 8 & 0xFF) / 255.0;
            double ☃xxxxxx = (☃x >> 0 & 0xFF) / 255.0;
            this.world
               .spawnParticle(
                  ☃xx ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB,
                  this.posX + (this.rand.nextDouble() - 0.5) * this.width,
                  this.posY + this.rand.nextDouble() * this.height,
                  this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
                  ☃xxxx,
                  ☃xxxxx,
                  ☃xxxxxx
               );
         }
      }
   }

   protected void updatePotionMetadata() {
      if (this.activePotionsMap.isEmpty()) {
         this.resetPotionEffectMetadata();
         this.setInvisible(false);
      } else {
         Collection<PotionEffect> ☃ = this.activePotionsMap.values();
         this.dataManager.set(HIDE_PARTICLES, areAllPotionsAmbient(☃));
         this.dataManager.set(POTION_EFFECTS, PotionUtils.getPotionColorFromEffectList(☃));
         this.setInvisible(this.isPotionActive(MobEffects.INVISIBILITY));
      }
   }

   public static boolean areAllPotionsAmbient(Collection<PotionEffect> var0) {
      for (PotionEffect ☃ : ☃) {
         if (!☃.getIsAmbient()) {
            return false;
         }
      }

      return true;
   }

   protected void resetPotionEffectMetadata() {
      this.dataManager.set(HIDE_PARTICLES, false);
      this.dataManager.set(POTION_EFFECTS, 0);
   }

   public void clearActivePotions() {
      if (!this.world.isRemote) {
         Iterator<PotionEffect> ☃ = this.activePotionsMap.values().iterator();

         while (☃.hasNext()) {
            this.onFinishedPotionEffect(☃.next());
            ☃.remove();
         }
      }
   }

   public Collection<PotionEffect> getActivePotionEffects() {
      return this.activePotionsMap.values();
   }

   public Map<Potion, PotionEffect> getActivePotionMap() {
      return this.activePotionsMap;
   }

   public boolean isPotionActive(Potion var1) {
      return this.activePotionsMap.containsKey(☃);
   }

   @Nullable
   public PotionEffect getActivePotionEffect(Potion var1) {
      return this.activePotionsMap.get(☃);
   }

   public void addPotionEffect(PotionEffect var1) {
      if (this.isPotionApplicable(☃)) {
         PotionEffect ☃ = this.activePotionsMap.get(☃.getPotion());
         if (☃ == null) {
            this.activePotionsMap.put(☃.getPotion(), ☃);
            this.onNewPotionEffect(☃);
         } else {
            ☃.combine(☃);
            this.onChangedPotionEffect(☃, true);
         }
      }
   }

   public boolean isPotionApplicable(PotionEffect var1) {
      if (this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
         Potion ☃ = ☃.getPotion();
         if (☃ == MobEffects.REGENERATION || ☃ == MobEffects.POISON) {
            return false;
         }
      }

      return true;
   }

   public boolean isEntityUndead() {
      return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
   }

   @Nullable
   public PotionEffect removeActivePotionEffect(@Nullable Potion var1) {
      return this.activePotionsMap.remove(☃);
   }

   public void removePotionEffect(Potion var1) {
      PotionEffect ☃ = this.removeActivePotionEffect(☃);
      if (☃ != null) {
         this.onFinishedPotionEffect(☃);
      }
   }

   protected void onNewPotionEffect(PotionEffect var1) {
      this.potionsNeedUpdate = true;
      if (!this.world.isRemote) {
         ☃.getPotion().applyAttributesModifiersToEntity(this, this.getAttributeMap(), ☃.getAmplifier());
      }
   }

   protected void onChangedPotionEffect(PotionEffect var1, boolean var2) {
      this.potionsNeedUpdate = true;
      if (☃ && !this.world.isRemote) {
         Potion ☃ = ☃.getPotion();
         ☃.removeAttributesModifiersFromEntity(this, this.getAttributeMap(), ☃.getAmplifier());
         ☃.applyAttributesModifiersToEntity(this, this.getAttributeMap(), ☃.getAmplifier());
      }
   }

   protected void onFinishedPotionEffect(PotionEffect var1) {
      this.potionsNeedUpdate = true;
      if (!this.world.isRemote) {
         ☃.getPotion().removeAttributesModifiersFromEntity(this, this.getAttributeMap(), ☃.getAmplifier());
      }
   }

   public void heal(float var1) {
      float ☃ = this.getHealth();
      if (☃ > 0.0F) {
         this.setHealth(☃ + ☃);
      }
   }

   public final float getHealth() {
      return this.dataManager.get(HEALTH);
   }

   public void setHealth(float var1) {
      this.dataManager.set(HEALTH, MathHelper.clamp(☃, 0.0F, this.getMaxHealth()));
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else if (this.world.isRemote) {
         return false;
      } else {
         this.idleTime = 0;
         if (this.getHealth() <= 0.0F) {
            return false;
         } else if (☃.isFireDamage() && this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
            return false;
         } else {
            float ☃ = ☃;
            if ((☃ == DamageSource.ANVIL || ☃ == DamageSource.FALLING_BLOCK) && !this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
               this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int)(☃ * 4.0F + this.rand.nextFloat() * ☃ * 2.0F), this);
               ☃ *= 0.75F;
            }

            boolean ☃x = false;
            if (☃ > 0.0F && this.canBlockDamageSource(☃)) {
               this.damageShield(☃);
               ☃ = 0.0F;
               if (!☃.isProjectile()) {
                  Entity ☃xx = ☃.getImmediateSource();
                  if (☃xx instanceof EntityLivingBase) {
                     this.blockUsingShield((EntityLivingBase)☃xx);
                  }
               }

               ☃x = true;
            }

            this.limbSwingAmount = 1.5F;
            boolean ☃xx = true;
            if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F) {
               if (☃ <= this.lastDamage) {
                  return false;
               }

               this.damageEntity(☃, ☃ - this.lastDamage);
               this.lastDamage = ☃;
               ☃xx = false;
            } else {
               this.lastDamage = ☃;
               this.hurtResistantTime = this.maxHurtResistantTime;
               this.damageEntity(☃, ☃);
               this.maxHurtTime = 10;
               this.hurtTime = this.maxHurtTime;
            }

            this.attackedAtYaw = 0.0F;
            Entity ☃xxx = ☃.getTrueSource();
            if (☃xxx != null) {
               if (☃xxx instanceof EntityLivingBase) {
                  this.setRevengeTarget((EntityLivingBase)☃xxx);
               }

               if (☃xxx instanceof EntityPlayer) {
                  this.recentlyHit = 100;
                  this.attackingPlayer = (EntityPlayer)☃xxx;
               } else if (☃xxx instanceof EntityWolf) {
                  EntityWolf ☃xxxx = (EntityWolf)☃xxx;
                  if (☃xxxx.isTamed()) {
                     this.recentlyHit = 100;
                     this.attackingPlayer = null;
                  }
               }
            }

            if (☃xx) {
               if (☃x) {
                  this.world.setEntityState(this, (byte)29);
               } else if (☃ instanceof EntityDamageSource && ((EntityDamageSource)☃).getIsThornsDamage()) {
                  this.world.setEntityState(this, (byte)33);
               } else {
                  byte ☃xxxx;
                  if (☃ == DamageSource.DROWN) {
                     ☃xxxx = 36;
                  } else if (☃.isFireDamage()) {
                     ☃xxxx = 37;
                  } else {
                     ☃xxxx = 2;
                  }

                  this.world.setEntityState(this, ☃xxxx);
               }

               if (☃ != DamageSource.DROWN && (!☃x || ☃ > 0.0F)) {
                  this.markVelocityChanged();
               }

               if (☃xxx != null) {
                  double ☃xxxx = ☃xxx.posX - this.posX;

                  double ☃xxxxx;
                  for (☃xxxxx = ☃xxx.posZ - this.posZ; ☃xxxx * ☃xxxx + ☃xxxxx * ☃xxxxx < 1.0E-4; ☃xxxxx = (Math.random() - Math.random()) * 0.01) {
                     ☃xxxx = (Math.random() - Math.random()) * 0.01;
                  }

                  this.attackedAtYaw = (float)(MathHelper.atan2(☃xxxxx, ☃xxxx) * 180.0F / (float)Math.PI - this.rotationYaw);
                  this.knockBack(☃xxx, 0.4F, ☃xxxx, ☃xxxxx);
               } else {
                  this.attackedAtYaw = (int)(Math.random() * 2.0) * 180;
               }
            }

            if (this.getHealth() <= 0.0F) {
               if (!this.checkTotemDeathProtection(☃)) {
                  SoundEvent ☃xxxx = this.getDeathSound();
                  if (☃xx && ☃xxxx != null) {
                     this.playSound(☃xxxx, this.getSoundVolume(), this.getSoundPitch());
                  }

                  this.onDeath(☃);
               }
            } else if (☃xx) {
               this.playHurtSound(☃);
            }

            boolean ☃xxxx = !☃x || ☃ > 0.0F;
            if (☃xxxx) {
               this.lastDamageSource = ☃;
               this.lastDamageStamp = this.world.getTotalWorldTime();
            }

            if (this instanceof EntityPlayerMP) {
               CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((EntityPlayerMP)this, ☃, ☃, ☃, ☃x);
            }

            if (☃xxx instanceof EntityPlayerMP) {
               CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((EntityPlayerMP)☃xxx, this, ☃, ☃, ☃, ☃x);
            }

            return ☃xxxx;
         }
      }
   }

   protected void blockUsingShield(EntityLivingBase var1) {
      ☃.knockBack(this, 0.5F, this.posX - ☃.posX, this.posZ - ☃.posZ);
   }

   private boolean checkTotemDeathProtection(DamageSource var1) {
      if (☃.canHarmInCreative()) {
         return false;
      } else {
         ItemStack ☃ = null;

         for (EnumHand ☃x : EnumHand.values()) {
            ItemStack ☃xx = this.getHeldItem(☃x);
            if (☃xx.getItem() == Items.TOTEM_OF_UNDYING) {
               ☃ = ☃xx.copy();
               ☃xx.shrink(1);
               break;
            }
         }

         if (☃ != null) {
            if (this instanceof EntityPlayerMP) {
               EntityPlayerMP ☃xx = (EntityPlayerMP)this;
               ☃xx.addStat(StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING));
               CriteriaTriggers.USED_TOTEM.trigger(☃xx, ☃);
            }

            this.setHealth(1.0F);
            this.clearActivePotions();
            this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
            this.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
            this.world.setEntityState(this, (byte)35);
         }

         return ☃ != null;
      }
   }

   @Nullable
   public DamageSource getLastDamageSource() {
      if (this.world.getTotalWorldTime() - this.lastDamageStamp > 40L) {
         this.lastDamageSource = null;
      }

      return this.lastDamageSource;
   }

   protected void playHurtSound(DamageSource var1) {
      SoundEvent ☃ = this.getHurtSound(☃);
      if (☃ != null) {
         this.playSound(☃, this.getSoundVolume(), this.getSoundPitch());
      }
   }

   private boolean canBlockDamageSource(DamageSource var1) {
      if (!☃.isUnblockable() && this.isActiveItemStackBlocking()) {
         Vec3d ☃ = ☃.getDamageLocation();
         if (☃ != null) {
            Vec3d ☃x = this.getLook(1.0F);
            Vec3d ☃xx = ☃.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
            ☃xx = new Vec3d(☃xx.x, 0.0, ☃xx.z);
            if (☃xx.dotProduct(☃x) < 0.0) {
               return true;
            }
         }
      }

      return false;
   }

   public void renderBrokenItemStack(ItemStack var1) {
      this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);

      for (int ☃ = 0; ☃ < 5; ☃++) {
         Vec3d ☃x = new Vec3d((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
         ☃x = ☃x.rotatePitch(-this.rotationPitch * (float) (Math.PI / 180.0));
         ☃x = ☃x.rotateYaw(-this.rotationYaw * (float) (Math.PI / 180.0));
         double ☃xx = -this.rand.nextFloat() * 0.6 - 0.3;
         Vec3d ☃xxx = new Vec3d((this.rand.nextFloat() - 0.5) * 0.3, ☃xx, 0.6);
         ☃xxx = ☃xxx.rotatePitch(-this.rotationPitch * (float) (Math.PI / 180.0));
         ☃xxx = ☃xxx.rotateYaw(-this.rotationYaw * (float) (Math.PI / 180.0));
         ☃xxx = ☃xxx.add(this.posX, this.posY + this.getEyeHeight(), this.posZ);
         this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, ☃xxx.x, ☃xxx.y, ☃xxx.z, ☃x.x, ☃x.y + 0.05, ☃x.z, Item.getIdFromItem(☃.getItem()));
      }
   }

   public void onDeath(DamageSource var1) {
      if (!this.dead) {
         Entity ☃ = ☃.getTrueSource();
         EntityLivingBase ☃x = this.getAttackingEntity();
         if (this.scoreValue >= 0 && ☃x != null) {
            ☃x.awardKillScore(this, this.scoreValue, ☃);
         }

         if (☃ != null) {
            ☃.onKillEntity(this);
         }

         this.dead = true;
         this.getCombatTracker().reset();
         if (!this.world.isRemote) {
            int ☃xx = 0;
            if (☃ instanceof EntityPlayer) {
               ☃xx = EnchantmentHelper.getLootingModifier((EntityLivingBase)☃);
            }

            if (this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")) {
               boolean ☃xxx = this.recentlyHit > 0;
               this.dropLoot(☃xxx, ☃xx, ☃);
            }
         }

         this.world.setEntityState(this, (byte)3);
      }
   }

   protected void dropLoot(boolean var1, int var2, DamageSource var3) {
      this.dropFewItems(☃, ☃);
      this.dropEquipment(☃, ☃);
   }

   protected void dropEquipment(boolean var1, int var2) {
   }

   public void knockBack(Entity var1, float var2, double var3, double var5) {
      if (!(this.rand.nextDouble() < this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue())) {
         this.isAirBorne = true;
         float ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃);
         this.motionX /= 2.0;
         this.motionZ /= 2.0;
         this.motionX -= ☃ / ☃ * ☃;
         this.motionZ -= ☃ / ☃ * ☃;
         if (this.onGround) {
            this.motionY /= 2.0;
            this.motionY += ☃;
            if (this.motionY > 0.4F) {
               this.motionY = 0.4F;
            }
         }
      }
   }

   @Nullable
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_GENERIC_HURT;
   }

   @Nullable
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_GENERIC_DEATH;
   }

   protected SoundEvent getFallSound(int var1) {
      return ☃ > 4 ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
   }

   protected void dropFewItems(boolean var1, int var2) {
   }

   public boolean isOnLadder() {
      int ☃ = MathHelper.floor(this.posX);
      int ☃x = MathHelper.floor(this.getEntityBoundingBox().minY);
      int ☃xx = MathHelper.floor(this.posZ);
      if (this instanceof EntityPlayer && ((EntityPlayer)this).isSpectator()) {
         return false;
      } else {
         BlockPos ☃xxx = new BlockPos(☃, ☃x, ☃xx);
         IBlockState ☃xxxx = this.world.getBlockState(☃xxx);
         Block ☃xxxxx = ☃xxxx.getBlock();
         return ☃xxxxx != Blocks.LADDER && ☃xxxxx != Blocks.VINE ? ☃xxxxx instanceof BlockTrapDoor && this.canGoThroughtTrapDoorOnLadder(☃xxx, ☃xxxx) : true;
      }
   }

   private boolean canGoThroughtTrapDoorOnLadder(BlockPos var1, IBlockState var2) {
      if (☃.getValue(BlockTrapDoor.OPEN)) {
         IBlockState ☃ = this.world.getBlockState(☃.down());
         if (☃.getBlock() == Blocks.LADDER && ☃.getValue(BlockLadder.FACING) == ☃.getValue(BlockTrapDoor.FACING)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean isEntityAlive() {
      return !this.isDead && this.getHealth() > 0.0F;
   }

   @Override
   public void fall(float var1, float var2) {
      super.fall(☃, ☃);
      PotionEffect ☃ = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
      float ☃x = ☃ == null ? 0.0F : ☃.getAmplifier() + 1;
      int ☃xx = MathHelper.ceil((☃ - 3.0F - ☃x) * ☃);
      if (☃xx > 0) {
         this.playSound(this.getFallSound(☃xx), 1.0F, 1.0F);
         this.attackEntityFrom(DamageSource.FALL, ☃xx);
         int ☃xxx = MathHelper.floor(this.posX);
         int ☃xxxx = MathHelper.floor(this.posY - 0.2F);
         int ☃xxxxx = MathHelper.floor(this.posZ);
         IBlockState ☃xxxxxx = this.world.getBlockState(new BlockPos(☃xxx, ☃xxxx, ☃xxxxx));
         if (☃xxxxxx.getMaterial() != Material.AIR) {
            SoundType ☃xxxxxxx = ☃xxxxxx.getBlock().getSoundType();
            this.playSound(☃xxxxxxx.getFallSound(), ☃xxxxxxx.getVolume() * 0.5F, ☃xxxxxxx.getPitch() * 0.75F);
         }
      }
   }

   @Override
   public void performHurtAnimation() {
      this.maxHurtTime = 10;
      this.hurtTime = this.maxHurtTime;
      this.attackedAtYaw = 0.0F;
   }

   public int getTotalArmorValue() {
      IAttributeInstance ☃ = this.getEntityAttribute(SharedMonsterAttributes.ARMOR);
      return MathHelper.floor(☃.getAttributeValue());
   }

   protected void damageArmor(float var1) {
   }

   protected void damageShield(float var1) {
   }

   protected float applyArmorCalculations(DamageSource var1, float var2) {
      if (!☃.isUnblockable()) {
         this.damageArmor(☃);
         ☃ = CombatRules.getDamageAfterAbsorb(
            ☃, this.getTotalArmorValue(), (float)this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()
         );
      }

      return ☃;
   }

   protected float applyPotionDamageCalculations(DamageSource var1, float var2) {
      if (☃.isDamageAbsolute()) {
         return ☃;
      } else {
         if (this.isPotionActive(MobEffects.RESISTANCE) && ☃ != DamageSource.OUT_OF_WORLD) {
            int ☃ = (this.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
            int ☃x = 25 - ☃;
            float ☃xx = ☃ * ☃x;
            ☃ = ☃xx / 25.0F;
         }

         if (☃ <= 0.0F) {
            return 0.0F;
         } else {
            int ☃ = EnchantmentHelper.getEnchantmentModifierDamage(this.getArmorInventoryList(), ☃);
            if (☃ > 0) {
               ☃ = CombatRules.getDamageAfterMagicAbsorb(☃, ☃);
            }

            return ☃;
         }
      }
   }

   protected void damageEntity(DamageSource var1, float var2) {
      if (!this.isEntityInvulnerable(☃)) {
         ☃ = this.applyArmorCalculations(☃, ☃);
         ☃ = this.applyPotionDamageCalculations(☃, ☃);
         float var7 = Math.max(☃ - this.getAbsorptionAmount(), 0.0F);
         this.setAbsorptionAmount(this.getAbsorptionAmount() - (☃ - var7));
         if (var7 != 0.0F) {
            float ☃ = this.getHealth();
            this.setHealth(☃ - var7);
            this.getCombatTracker().trackDamage(☃, ☃, var7);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - var7);
         }
      }
   }

   public CombatTracker getCombatTracker() {
      return this.combatTracker;
   }

   @Nullable
   public EntityLivingBase getAttackingEntity() {
      if (this.combatTracker.getBestAttacker() != null) {
         return this.combatTracker.getBestAttacker();
      } else if (this.attackingPlayer != null) {
         return this.attackingPlayer;
      } else {
         return this.revengeTarget != null ? this.revengeTarget : null;
      }
   }

   public final float getMaxHealth() {
      return (float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
   }

   public final int getArrowCountInEntity() {
      return this.dataManager.get(ARROW_COUNT_IN_ENTITY);
   }

   public final void setArrowCountInEntity(int var1) {
      this.dataManager.set(ARROW_COUNT_IN_ENTITY, ☃);
   }

   private int getArmSwingAnimationEnd() {
      if (this.isPotionActive(MobEffects.HASTE)) {
         return 6 - (1 + this.getActivePotionEffect(MobEffects.HASTE).getAmplifier());
      } else {
         return this.isPotionActive(MobEffects.MINING_FATIGUE) ? 6 + (1 + this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6;
      }
   }

   public void swingArm(EnumHand var1) {
      if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
         this.swingProgressInt = -1;
         this.isSwingInProgress = true;
         this.swingingHand = ☃;
         if (this.world instanceof WorldServer) {
            ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketAnimation(this, ☃ == EnumHand.MAIN_HAND ? 0 : 3));
         }
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      boolean ☃ = ☃ == 33;
      boolean ☃x = ☃ == 36;
      boolean ☃xx = ☃ == 37;
      if (☃ == 2 || ☃ || ☃x || ☃xx) {
         this.limbSwingAmount = 1.5F;
         this.hurtResistantTime = this.maxHurtResistantTime;
         this.maxHurtTime = 10;
         this.hurtTime = this.maxHurtTime;
         this.attackedAtYaw = 0.0F;
         if (☃) {
            this.playSound(SoundEvents.ENCHANT_THORNS_HIT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }

         DamageSource ☃xxx;
         if (☃xx) {
            ☃xxx = DamageSource.ON_FIRE;
         } else if (☃x) {
            ☃xxx = DamageSource.DROWN;
         } else {
            ☃xxx = DamageSource.GENERIC;
         }

         SoundEvent ☃xxxx = this.getHurtSound(☃xxx);
         if (☃xxxx != null) {
            this.playSound(☃xxxx, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }

         this.attackEntityFrom(DamageSource.GENERIC, 0.0F);
      } else if (☃ == 3) {
         SoundEvent ☃xxxx = this.getDeathSound();
         if (☃xxxx != null) {
            this.playSound(☃xxxx, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }

         this.setHealth(0.0F);
         this.onDeath(DamageSource.GENERIC);
      } else if (☃ == 30) {
         this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
      } else if (☃ == 29) {
         this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 0.8F + this.world.rand.nextFloat() * 0.4F);
      } else {
         super.handleStatusUpdate(☃);
      }
   }

   @Override
   protected void outOfWorld() {
      this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4.0F);
   }

   protected void updateArmSwingProgress() {
      int ☃ = this.getArmSwingAnimationEnd();
      if (this.isSwingInProgress) {
         this.swingProgressInt++;
         if (this.swingProgressInt >= ☃) {
            this.swingProgressInt = 0;
            this.isSwingInProgress = false;
         }
      } else {
         this.swingProgressInt = 0;
      }

      this.swingProgress = (float)this.swingProgressInt / ☃;
   }

   public IAttributeInstance getEntityAttribute(IAttribute var1) {
      return this.getAttributeMap().getAttributeInstance(☃);
   }

   public AbstractAttributeMap getAttributeMap() {
      if (this.attributeMap == null) {
         this.attributeMap = new AttributeMap();
      }

      return this.attributeMap;
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEFINED;
   }

   public ItemStack getHeldItemMainhand() {
      return this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
   }

   public ItemStack getHeldItemOffhand() {
      return this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
   }

   public ItemStack getHeldItem(EnumHand var1) {
      if (☃ == EnumHand.MAIN_HAND) {
         return this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
      } else if (☃ == EnumHand.OFF_HAND) {
         return this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
      } else {
         throw new IllegalArgumentException("Invalid hand " + ☃);
      }
   }

   public void setHeldItem(EnumHand var1, ItemStack var2) {
      if (☃ == EnumHand.MAIN_HAND) {
         this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ☃);
      } else {
         if (☃ != EnumHand.OFF_HAND) {
            throw new IllegalArgumentException("Invalid hand " + ☃);
         }

         this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ☃);
      }
   }

   public boolean hasItemInSlot(EntityEquipmentSlot var1) {
      return !this.getItemStackFromSlot(☃).isEmpty();
   }

   @Override
   public abstract Iterable<ItemStack> getArmorInventoryList();

   public abstract ItemStack getItemStackFromSlot(EntityEquipmentSlot var1);

   @Override
   public abstract void setItemStackToSlot(EntityEquipmentSlot var1, ItemStack var2);

   @Override
   public void setSprinting(boolean var1) {
      super.setSprinting(☃);
      IAttributeInstance ☃ = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
      if (☃.getModifier(SPRINTING_SPEED_BOOST_ID) != null) {
         ☃.removeModifier(SPRINTING_SPEED_BOOST);
      }

      if (☃) {
         ☃.applyModifier(SPRINTING_SPEED_BOOST);
      }
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   protected float getSoundPitch() {
      return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
   }

   protected boolean isMovementBlocked() {
      return this.getHealth() <= 0.0F;
   }

   public void dismountEntity(Entity var1) {
      if (!(☃ instanceof EntityBoat) && !(☃ instanceof AbstractHorse)) {
         double ☃ = ☃.posX;
         double ☃x = ☃.getEntityBoundingBox().minY + ☃.height;
         double ☃xx = ☃.posZ;
         EnumFacing ☃xxx = ☃.getAdjustedHorizontalFacing();
         if (☃xxx != null) {
            EnumFacing ☃xxxx = ☃xxx.rotateY();
            int[][] ☃xxxxx = new int[][]{{0, 1}, {0, -1}, {-1, 1}, {-1, -1}, {1, 1}, {1, -1}, {-1, 0}, {1, 0}, {0, 1}};
            double ☃xxxxxx = Math.floor(this.posX) + 0.5;
            double ☃xxxxxxx = Math.floor(this.posZ) + 0.5;
            double ☃xxxxxxxx = this.getEntityBoundingBox().maxX - this.getEntityBoundingBox().minX;
            double ☃xxxxxxxxx = this.getEntityBoundingBox().maxZ - this.getEntityBoundingBox().minZ;
            AxisAlignedBB ☃xxxxxxxxxx = new AxisAlignedBB(
               ☃xxxxxx - ☃xxxxxxxx / 2.0,
               ☃.getEntityBoundingBox().minY,
               ☃xxxxxxx - ☃xxxxxxxxx / 2.0,
               ☃xxxxxx + ☃xxxxxxxx / 2.0,
               Math.floor(☃.getEntityBoundingBox().minY) + this.height,
               ☃xxxxxxx + ☃xxxxxxxxx / 2.0
            );

            for (int[] ☃xxxxxxxxxxx : ☃xxxxx) {
               double ☃xxxxxxxxxxxx = ☃xxx.getXOffset() * ☃xxxxxxxxxxx[0] + ☃xxxx.getXOffset() * ☃xxxxxxxxxxx[1];
               double ☃xxxxxxxxxxxxx = ☃xxx.getZOffset() * ☃xxxxxxxxxxx[0] + ☃xxxx.getZOffset() * ☃xxxxxxxxxxx[1];
               double ☃xxxxxxxxxxxxxx = ☃xxxxxx + ☃xxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxx = ☃xxxxxxx + ☃xxxxxxxxxxxxx;
               AxisAlignedBB ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxx.offset(☃xxxxxxxxxxxx, 0.0, ☃xxxxxxxxxxxxx);
               if (!this.world.collidesWithAnyBlock(☃xxxxxxxxxxxxxxxx)) {
                  if (this.world.getBlockState(new BlockPos(☃xxxxxxxxxxxxxx, this.posY, ☃xxxxxxxxxxxxxxx)).isTopSolid()) {
                     this.setPositionAndUpdate(☃xxxxxxxxxxxxxx, this.posY + 1.0, ☃xxxxxxxxxxxxxxx);
                     return;
                  }

                  BlockPos ☃xxxxxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxxxxx, this.posY - 1.0, ☃xxxxxxxxxxxxxxx);
                  if (this.world.getBlockState(☃xxxxxxxxxxxxxxxxx).isTopSolid() || this.world.getBlockState(☃xxxxxxxxxxxxxxxxx).getMaterial() == Material.WATER
                     )
                   {
                     ☃ = ☃xxxxxxxxxxxxxx;
                     ☃x = this.posY + 1.0;
                     ☃xx = ☃xxxxxxxxxxxxxxx;
                  }
               } else if (!this.world.collidesWithAnyBlock(☃xxxxxxxxxxxxxxxx.offset(0.0, 1.0, 0.0))
                  && this.world.getBlockState(new BlockPos(☃xxxxxxxxxxxxxx, this.posY + 1.0, ☃xxxxxxxxxxxxxxx)).isTopSolid()) {
                  ☃ = ☃xxxxxxxxxxxxxx;
                  ☃x = this.posY + 2.0;
                  ☃xx = ☃xxxxxxxxxxxxxxx;
               }
            }
         }

         this.setPositionAndUpdate(☃, ☃x, ☃xx);
      } else {
         double ☃ = this.width / 2.0F + ☃.width / 2.0F + 0.4;
         float ☃x;
         if (☃ instanceof EntityBoat) {
            ☃x = 0.0F;
         } else {
            ☃x = (float) (Math.PI / 2) * (this.getPrimaryHand() == EnumHandSide.RIGHT ? -1 : 1);
         }

         float ☃xx = -MathHelper.sin(-this.rotationYaw * (float) (Math.PI / 180.0) - (float) Math.PI + ☃x);
         float ☃xxx = -MathHelper.cos(-this.rotationYaw * (float) (Math.PI / 180.0) - (float) Math.PI + ☃x);
         double ☃xxxx = Math.abs(☃xx) > Math.abs(☃xxx) ? ☃ / Math.abs(☃xx) : ☃ / Math.abs(☃xxx);
         double ☃xxxxx = this.posX + ☃xx * ☃xxxx;
         double ☃xxxxxx = this.posZ + ☃xxx * ☃xxxx;
         this.setPosition(☃xxxxx, ☃.posY + ☃.height + 0.001, ☃xxxxxx);
         if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
            this.setPosition(☃xxxxx, ☃.posY + ☃.height + 1.001, ☃xxxxxx);
            if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
               this.setPosition(☃.posX, ☃.posY + this.height + 0.001, ☃.posZ);
            }
         }
      }
   }

   @Override
   public boolean getAlwaysRenderNameTagForRender() {
      return this.getAlwaysRenderNameTag();
   }

   protected float getJumpUpwardsMotion() {
      return 0.42F;
   }

   protected void jump() {
      this.motionY = this.getJumpUpwardsMotion();
      if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
         this.motionY = this.motionY + (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
      }

      if (this.isSprinting()) {
         float ☃ = this.rotationYaw * (float) (Math.PI / 180.0);
         this.motionX = this.motionX - MathHelper.sin(☃) * 0.2F;
         this.motionZ = this.motionZ + MathHelper.cos(☃) * 0.2F;
      }

      this.isAirBorne = true;
   }

   protected void handleJumpWater() {
      this.motionY += 0.04F;
   }

   protected void handleJumpLava() {
      this.motionY += 0.04F;
   }

   protected float getWaterSlowDown() {
      return 0.8F;
   }

   public void travel(float var1, float var2, float var3) {
      if (this.isServerWorld() || this.canPassengerSteer()) {
         if (!this.isInWater() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying) {
            if (!this.isInLava() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying) {
               if (this.isElytraFlying()) {
                  if (this.motionY > -0.5) {
                     this.fallDistance = 1.0F;
                  }

                  Vec3d ☃ = this.getLookVec();
                  float ☃x = this.rotationPitch * (float) (Math.PI / 180.0);
                  double ☃xx = Math.sqrt(☃.x * ☃.x + ☃.z * ☃.z);
                  double ☃xxx = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                  double ☃xxxx = ☃.length();
                  float ☃xxxxx = MathHelper.cos(☃x);
                  ☃xxxxx = (float)(☃xxxxx * (☃xxxxx * Math.min(1.0, ☃xxxx / 0.4)));
                  this.motionY += -0.08 + ☃xxxxx * 0.06;
                  if (this.motionY < 0.0 && ☃xx > 0.0) {
                     double ☃xxxxxx = this.motionY * -0.1 * ☃xxxxx;
                     this.motionY += ☃xxxxxx;
                     this.motionX = this.motionX + ☃.x * ☃xxxxxx / ☃xx;
                     this.motionZ = this.motionZ + ☃.z * ☃xxxxxx / ☃xx;
                  }

                  if (☃x < 0.0F) {
                     double ☃xxxxxx = ☃xxx * -MathHelper.sin(☃x) * 0.04;
                     this.motionY += ☃xxxxxx * 3.2;
                     this.motionX = this.motionX - ☃.x * ☃xxxxxx / ☃xx;
                     this.motionZ = this.motionZ - ☃.z * ☃xxxxxx / ☃xx;
                  }

                  if (☃xx > 0.0) {
                     this.motionX = this.motionX + (☃.x / ☃xx * ☃xxx - this.motionX) * 0.1;
                     this.motionZ = this.motionZ + (☃.z / ☃xx * ☃xxx - this.motionZ) * 0.1;
                  }

                  this.motionX *= 0.99F;
                  this.motionY *= 0.98F;
                  this.motionZ *= 0.99F;
                  this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                  if (this.collidedHorizontally && !this.world.isRemote) {
                     double ☃xxxxxx = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                     double ☃xxxxxxx = ☃xxx - ☃xxxxxx;
                     float ☃xxxxxxxx = (float)(☃xxxxxxx * 10.0 - 3.0);
                     if (☃xxxxxxxx > 0.0F) {
                        this.playSound(this.getFallSound((int)☃xxxxxxxx), 1.0F, 1.0F);
                        this.attackEntityFrom(DamageSource.FLY_INTO_WALL, ☃xxxxxxxx);
                     }
                  }

                  if (this.onGround && !this.world.isRemote) {
                     this.setFlag(7, false);
                  }
               } else {
                  float ☃xxxxxx = 0.91F;
                  BlockPos.PooledMutableBlockPos ☃xxxxxxx = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY - 1.0, this.posZ);
                  if (this.onGround) {
                     ☃xxxxxx = this.world.getBlockState(☃xxxxxxx).getBlock().slipperiness * 0.91F;
                  }

                  float ☃xxxxxxxx = 0.16277136F / (☃xxxxxx * ☃xxxxxx * ☃xxxxxx);
                  float ☃xxxxxxxxx;
                  if (this.onGround) {
                     ☃xxxxxxxxx = this.getAIMoveSpeed() * ☃xxxxxxxx;
                  } else {
                     ☃xxxxxxxxx = this.jumpMovementFactor;
                  }

                  this.moveRelative(☃, ☃, ☃, ☃xxxxxxxxx);
                  ☃xxxxxx = 0.91F;
                  if (this.onGround) {
                     ☃xxxxxx = this.world.getBlockState(☃xxxxxxx.setPos(this.posX, this.getEntityBoundingBox().minY - 1.0, this.posZ)).getBlock().slipperiness
                        * 0.91F;
                  }

                  if (this.isOnLadder()) {
                     float ☃xxxxxxxxxx = 0.15F;
                     this.motionX = MathHelper.clamp(this.motionX, -0.15F, 0.15F);
                     this.motionZ = MathHelper.clamp(this.motionZ, -0.15F, 0.15F);
                     this.fallDistance = 0.0F;
                     if (this.motionY < -0.15) {
                        this.motionY = -0.15;
                     }

                     boolean ☃xxxxxxxxxxx = this.isSneaking() && this instanceof EntityPlayer;
                     if (☃xxxxxxxxxxx && this.motionY < 0.0) {
                        this.motionY = 0.0;
                     }
                  }

                  this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                  if (this.collidedHorizontally && this.isOnLadder()) {
                     this.motionY = 0.2;
                  }

                  if (this.isPotionActive(MobEffects.LEVITATION)) {
                     this.motionY = this.motionY + (0.05 * (this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2;
                  } else {
                     ☃xxxxxxx.setPos(this.posX, 0.0, this.posZ);
                     if (!this.world.isRemote || this.world.isBlockLoaded(☃xxxxxxx) && this.world.getChunk(☃xxxxxxx).isLoaded()) {
                        if (!this.hasNoGravity()) {
                           this.motionY -= 0.08;
                        }
                     } else if (this.posY > 0.0) {
                        this.motionY = -0.1;
                     } else {
                        this.motionY = 0.0;
                     }
                  }

                  this.motionY *= 0.98F;
                  this.motionX *= ☃xxxxxx;
                  this.motionZ *= ☃xxxxxx;
                  ☃xxxxxxx.release();
               }
            } else {
               double ☃xxxxxxxxxxx = this.posY;
               this.moveRelative(☃, ☃, ☃, 0.02F);
               this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
               this.motionX *= 0.5;
               this.motionY *= 0.5;
               this.motionZ *= 0.5;
               if (!this.hasNoGravity()) {
                  this.motionY -= 0.02;
               }

               if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6F - this.posY + ☃xxxxxxxxxxx, this.motionZ)) {
                  this.motionY = 0.3F;
               }
            }
         } else {
            double ☃xxxxxxxxxxxx = this.posY;
            float ☃xxxxxxxxxxxxx = this.getWaterSlowDown();
            float ☃xxxxxxxxxxxxxx = 0.02F;
            float ☃xxxxxxxxxxxxxxx = EnchantmentHelper.getDepthStriderModifier(this);
            if (☃xxxxxxxxxxxxxxx > 3.0F) {
               ☃xxxxxxxxxxxxxxx = 3.0F;
            }

            if (!this.onGround) {
               ☃xxxxxxxxxxxxxxx *= 0.5F;
            }

            if (☃xxxxxxxxxxxxxxx > 0.0F) {
               ☃xxxxxxxxxxxxx += (0.54600006F - ☃xxxxxxxxxxxxx) * ☃xxxxxxxxxxxxxxx / 3.0F;
               ☃xxxxxxxxxxxxxx += (this.getAIMoveSpeed() - ☃xxxxxxxxxxxxxx) * ☃xxxxxxxxxxxxxxx / 3.0F;
            }

            this.moveRelative(☃, ☃, ☃, ☃xxxxxxxxxxxxxx);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= ☃xxxxxxxxxxxxx;
            this.motionY *= 0.8F;
            this.motionZ *= ☃xxxxxxxxxxxxx;
            if (!this.hasNoGravity()) {
               this.motionY -= 0.02;
            }

            if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6F - this.posY + ☃xxxxxxxxxxxx, this.motionZ)) {
               this.motionY = 0.3F;
            }
         }
      }

      this.prevLimbSwingAmount = this.limbSwingAmount;
      double ☃xxxxxxxxxxxxxxxx = this.posX - this.prevPosX;
      double ☃xxxxxxxxxxxxxxxxx = this.posZ - this.prevPosZ;
      double ☃xxxxxxxxxxxxxxxxxx = this instanceof net.minecraft.entity.passive.EntityFlying ? this.posY - this.prevPosY : 0.0;
      float ☃xxxxxxxxxxxxxxxxxxx = MathHelper.sqrt(
            ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx
         )
         * 4.0F;
      if (☃xxxxxxxxxxxxxxxxxxx > 1.0F) {
         ☃xxxxxxxxxxxxxxxxxxx = 1.0F;
      }

      this.limbSwingAmount = this.limbSwingAmount + (☃xxxxxxxxxxxxxxxxxxx - this.limbSwingAmount) * 0.4F;
      this.limbSwing = this.limbSwing + this.limbSwingAmount;
   }

   public float getAIMoveSpeed() {
      return this.landMovementFactor;
   }

   public void setAIMoveSpeed(float var1) {
      this.landMovementFactor = ☃;
   }

   public boolean attackEntityAsMob(Entity var1) {
      this.setLastAttackedEntity(☃);
      return false;
   }

   public boolean isPlayerSleeping() {
      return false;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      this.updateActiveHand();
      if (!this.world.isRemote) {
         int ☃ = this.getArrowCountInEntity();
         if (☃ > 0) {
            if (this.arrowHitTimer <= 0) {
               this.arrowHitTimer = 20 * (30 - ☃);
            }

            this.arrowHitTimer--;
            if (this.arrowHitTimer <= 0) {
               this.setArrowCountInEntity(☃ - 1);
            }
         }

         for (EntityEquipmentSlot ☃x : EntityEquipmentSlot.values()) {
            ItemStack ☃xx;
            switch (☃x.getSlotType()) {
               case HAND:
                  ☃xx = this.handInventory.get(☃x.getIndex());
                  break;
               case ARMOR:
                  ☃xx = this.armorArray.get(☃x.getIndex());
                  break;
               default:
                  continue;
            }

            ItemStack ☃xx = this.getItemStackFromSlot(☃x);
            if (!ItemStack.areItemStacksEqual(☃xx, ☃xx)) {
               ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketEntityEquipment(this.getEntityId(), ☃x, ☃xx));
               if (!☃xx.isEmpty()) {
                  this.getAttributeMap().removeAttributeModifiers(☃xx.getAttributeModifiers(☃x));
               }

               if (!☃xx.isEmpty()) {
                  this.getAttributeMap().applyAttributeModifiers(☃xx.getAttributeModifiers(☃x));
               }

               switch (☃x.getSlotType()) {
                  case HAND:
                     this.handInventory.set(☃x.getIndex(), ☃xx.isEmpty() ? ItemStack.EMPTY : ☃xx.copy());
                     break;
                  case ARMOR:
                     this.armorArray.set(☃x.getIndex(), ☃xx.isEmpty() ? ItemStack.EMPTY : ☃xx.copy());
               }
            }
         }

         if (this.ticksExisted % 20 == 0) {
            this.getCombatTracker().reset();
         }

         if (!this.glowing) {
            boolean ☃x = this.isPotionActive(MobEffects.GLOWING);
            if (this.getFlag(6) != ☃x) {
               this.setFlag(6, ☃x);
            }
         }
      }

      this.onLivingUpdate();
      double ☃x = this.posX - this.prevPosX;
      double ☃xx = this.posZ - this.prevPosZ;
      float ☃xxx = (float)(☃x * ☃x + ☃xx * ☃xx);
      float ☃xxxx = this.renderYawOffset;
      float ☃xxxxx = 0.0F;
      this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
      float ☃xxxxxx = 0.0F;
      if (☃xxx > 0.0025000002F) {
         ☃xxxxxx = 1.0F;
         ☃xxxxx = (float)Math.sqrt(☃xxx) * 3.0F;
         float ☃xxxxxxx = (float)MathHelper.atan2(☃xx, ☃x) * (180.0F / (float)Math.PI) - 90.0F;
         float ☃xxxxxxxx = MathHelper.abs(MathHelper.wrapDegrees(this.rotationYaw) - ☃xxxxxxx);
         if (95.0F < ☃xxxxxxxx && ☃xxxxxxxx < 265.0F) {
            ☃xxxx = ☃xxxxxxx - 180.0F;
         } else {
            ☃xxxx = ☃xxxxxxx;
         }
      }

      if (this.swingProgress > 0.0F) {
         ☃xxxx = this.rotationYaw;
      }

      if (!this.onGround) {
         ☃xxxxxx = 0.0F;
      }

      this.onGroundSpeedFactor = this.onGroundSpeedFactor + (☃xxxxxx - this.onGroundSpeedFactor) * 0.3F;
      this.world.profiler.startSection("headTurn");
      ☃xxxxx = this.updateDistance(☃xxxx, ☃xxxxx);
      this.world.profiler.endSection();
      this.world.profiler.startSection("rangeChecks");

      while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
         this.prevRenderYawOffset -= 360.0F;
      }

      while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
         this.prevRenderYawOffset += 360.0F;
      }

      while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
         this.prevRotationPitch -= 360.0F;
      }

      while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while (this.rotationYawHead - this.prevRotationYawHead < -180.0F) {
         this.prevRotationYawHead -= 360.0F;
      }

      while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F) {
         this.prevRotationYawHead += 360.0F;
      }

      this.world.profiler.endSection();
      this.movedDistance += ☃xxxxx;
      if (this.isElytraFlying()) {
         this.ticksElytraFlying++;
      } else {
         this.ticksElytraFlying = 0;
      }
   }

   protected float updateDistance(float var1, float var2) {
      float ☃ = MathHelper.wrapDegrees(☃ - this.renderYawOffset);
      this.renderYawOffset += ☃ * 0.3F;
      float ☃x = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
      boolean ☃xx = ☃x < -90.0F || ☃x >= 90.0F;
      if (☃x < -75.0F) {
         ☃x = -75.0F;
      }

      if (☃x >= 75.0F) {
         ☃x = 75.0F;
      }

      this.renderYawOffset = this.rotationYaw - ☃x;
      if (☃x * ☃x > 2500.0F) {
         this.renderYawOffset += ☃x * 0.2F;
      }

      if (☃xx) {
         ☃ *= -1.0F;
      }

      return ☃;
   }

   public void onLivingUpdate() {
      if (this.jumpTicks > 0) {
         this.jumpTicks--;
      }

      if (this.newPosRotationIncrements > 0 && !this.canPassengerSteer()) {
         double ☃ = this.posX + (this.interpTargetX - this.posX) / this.newPosRotationIncrements;
         double ☃x = this.posY + (this.interpTargetY - this.posY) / this.newPosRotationIncrements;
         double ☃xx = this.posZ + (this.interpTargetZ - this.posZ) / this.newPosRotationIncrements;
         double ☃xxx = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
         this.rotationYaw = (float)(this.rotationYaw + ☃xxx / this.newPosRotationIncrements);
         this.rotationPitch = (float)(this.rotationPitch + (this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
         this.newPosRotationIncrements--;
         this.setPosition(☃, ☃x, ☃xx);
         this.setRotation(this.rotationYaw, this.rotationPitch);
      } else if (!this.isServerWorld()) {
         this.motionX *= 0.98;
         this.motionY *= 0.98;
         this.motionZ *= 0.98;
      }

      if (Math.abs(this.motionX) < 0.003) {
         this.motionX = 0.0;
      }

      if (Math.abs(this.motionY) < 0.003) {
         this.motionY = 0.0;
      }

      if (Math.abs(this.motionZ) < 0.003) {
         this.motionZ = 0.0;
      }

      this.world.profiler.startSection("ai");
      if (this.isMovementBlocked()) {
         this.isJumping = false;
         this.moveStrafing = 0.0F;
         this.moveForward = 0.0F;
         this.randomYawVelocity = 0.0F;
      } else if (this.isServerWorld()) {
         this.world.profiler.startSection("newAi");
         this.updateEntityActionState();
         this.world.profiler.endSection();
      }

      this.world.profiler.endSection();
      this.world.profiler.startSection("jump");
      if (this.isJumping) {
         if (this.isInWater()) {
            this.handleJumpWater();
         } else if (this.isInLava()) {
            this.handleJumpLava();
         } else if (this.onGround && this.jumpTicks == 0) {
            this.jump();
            this.jumpTicks = 10;
         }
      } else {
         this.jumpTicks = 0;
      }

      this.world.profiler.endSection();
      this.world.profiler.startSection("travel");
      this.moveStrafing *= 0.98F;
      this.moveForward *= 0.98F;
      this.randomYawVelocity *= 0.9F;
      this.updateElytra();
      this.travel(this.moveStrafing, this.moveVertical, this.moveForward);
      this.world.profiler.endSection();
      this.world.profiler.startSection("push");
      this.collideWithNearbyEntities();
      this.world.profiler.endSection();
   }

   private void updateElytra() {
      boolean ☃ = this.getFlag(7);
      if (☃ && !this.onGround && !this.isRiding()) {
         ItemStack ☃x = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
         if (☃x.getItem() == Items.ELYTRA && ItemElytra.isUsable(☃x)) {
            ☃ = true;
            if (!this.world.isRemote && (this.ticksElytraFlying + 1) % 20 == 0) {
               ☃x.damageItem(1, this);
            }
         } else {
            ☃ = false;
         }
      } else {
         ☃ = false;
      }

      if (!this.world.isRemote) {
         this.setFlag(7, ☃);
      }
   }

   protected void updateEntityActionState() {
   }

   protected void collideWithNearbyEntities() {
      List<Entity> ☃ = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), EntitySelectors.getTeamCollisionPredicate(this));
      if (!☃.isEmpty()) {
         int ☃x = this.world.getGameRules().getInt("maxEntityCramming");
         if (☃x > 0 && ☃.size() > ☃x - 1 && this.rand.nextInt(4) == 0) {
            int ☃xx = 0;

            for (int ☃xxx = 0; ☃xxx < ☃.size(); ☃xxx++) {
               if (!☃.get(☃xxx).isRiding()) {
                  ☃xx++;
               }
            }

            if (☃xx > ☃x - 1) {
               this.attackEntityFrom(DamageSource.CRAMMING, 6.0F);
            }
         }

         for (int ☃xx = 0; ☃xx < ☃.size(); ☃xx++) {
            Entity ☃xxxx = ☃.get(☃xx);
            this.collideWithEntity(☃xxxx);
         }
      }
   }

   protected void collideWithEntity(Entity var1) {
      ☃.applyEntityCollision(this);
   }

   @Override
   public void dismountRidingEntity() {
      Entity ☃ = this.getRidingEntity();
      super.dismountRidingEntity();
      if (☃ != null && ☃ != this.getRidingEntity() && !this.world.isRemote) {
         this.dismountEntity(☃);
      }
   }

   @Override
   public void updateRidden() {
      super.updateRidden();
      this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
      this.onGroundSpeedFactor = 0.0F;
      this.fallDistance = 0.0F;
   }

   @Override
   public void setPositionAndRotationDirect(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.interpTargetX = ☃;
      this.interpTargetY = ☃;
      this.interpTargetZ = ☃;
      this.interpTargetYaw = ☃;
      this.interpTargetPitch = ☃;
      this.newPosRotationIncrements = ☃;
   }

   public void setJumping(boolean var1) {
      this.isJumping = ☃;
   }

   public void onItemPickup(Entity var1, int var2) {
      if (!☃.isDead && !this.world.isRemote) {
         EntityTracker ☃ = ((WorldServer)this.world).getEntityTracker();
         if (☃ instanceof EntityItem || ☃ instanceof EntityArrow || ☃ instanceof EntityXPOrb) {
            ☃.sendToTracking(☃, new SPacketCollectItem(☃.getEntityId(), this.getEntityId(), ☃));
         }
      }
   }

   public boolean canEntityBeSeen(Entity var1) {
      return this.world
            .rayTraceBlocks(
               new Vec3d(this.posX, this.posY + this.getEyeHeight(), this.posZ), new Vec3d(☃.posX, ☃.posY + ☃.getEyeHeight(), ☃.posZ), false, true, false
            )
         == null;
   }

   @Override
   public Vec3d getLook(float var1) {
      if (☃ == 1.0F) {
         return this.getVectorForRotation(this.rotationPitch, this.rotationYawHead);
      } else {
         float ☃ = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * ☃;
         float ☃x = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * ☃;
         return this.getVectorForRotation(☃, ☃x);
      }
   }

   public float getSwingProgress(float var1) {
      float ☃ = this.swingProgress - this.prevSwingProgress;
      if (☃ < 0.0F) {
         ☃++;
      }

      return this.prevSwingProgress + ☃ * ☃;
   }

   public boolean isServerWorld() {
      return !this.world.isRemote;
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public boolean canBePushed() {
      return this.isEntityAlive() && !this.isOnLadder();
   }

   @Override
   protected void markVelocityChanged() {
      this.velocityChanged = this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
   }

   @Override
   public float getRotationYawHead() {
      return this.rotationYawHead;
   }

   @Override
   public void setRotationYawHead(float var1) {
      this.rotationYawHead = ☃;
   }

   @Override
   public void setRenderYawOffset(float var1) {
      this.renderYawOffset = ☃;
   }

   public float getAbsorptionAmount() {
      return this.absorptionAmount;
   }

   public void setAbsorptionAmount(float var1) {
      if (☃ < 0.0F) {
         ☃ = 0.0F;
      }

      this.absorptionAmount = ☃;
   }

   public void sendEnterCombat() {
   }

   public void sendEndCombat() {
   }

   protected void markPotionsDirty() {
      this.potionsNeedUpdate = true;
   }

   public abstract EnumHandSide getPrimaryHand();

   public boolean isHandActive() {
      return (this.dataManager.get(HAND_STATES) & 1) > 0;
   }

   public EnumHand getActiveHand() {
      return (this.dataManager.get(HAND_STATES) & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
   }

   protected void updateActiveHand() {
      if (this.isHandActive()) {
         ItemStack ☃ = this.getHeldItem(this.getActiveHand());
         if (☃ == this.activeItemStack) {
            if (this.getItemInUseCount() <= 25 && this.getItemInUseCount() % 4 == 0) {
               this.updateItemUse(this.activeItemStack, 5);
            }

            if (--this.activeItemStackUseCount == 0 && !this.world.isRemote) {
               this.onItemUseFinish();
            }
         } else {
            this.resetActiveHand();
         }
      }
   }

   public void setActiveHand(EnumHand var1) {
      ItemStack ☃ = this.getHeldItem(☃);
      if (!☃.isEmpty() && !this.isHandActive()) {
         this.activeItemStack = ☃;
         this.activeItemStackUseCount = ☃.getMaxItemUseDuration();
         if (!this.world.isRemote) {
            int ☃x = 1;
            if (☃ == EnumHand.OFF_HAND) {
               ☃x |= 2;
            }

            this.dataManager.set(HAND_STATES, (byte)☃x);
         }
      }
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      super.notifyDataManagerChange(☃);
      if (HAND_STATES.equals(☃) && this.world.isRemote) {
         if (this.isHandActive() && this.activeItemStack.isEmpty()) {
            this.activeItemStack = this.getHeldItem(this.getActiveHand());
            if (!this.activeItemStack.isEmpty()) {
               this.activeItemStackUseCount = this.activeItemStack.getMaxItemUseDuration();
            }
         } else if (!this.isHandActive() && !this.activeItemStack.isEmpty()) {
            this.activeItemStack = ItemStack.EMPTY;
            this.activeItemStackUseCount = 0;
         }
      }
   }

   protected void updateItemUse(ItemStack var1, int var2) {
      if (!☃.isEmpty() && this.isHandActive()) {
         if (☃.getItemUseAction() == EnumAction.DRINK) {
            this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
         }

         if (☃.getItemUseAction() == EnumAction.EAT) {
            for (int ☃ = 0; ☃ < ☃; ☃++) {
               Vec3d ☃x = new Vec3d((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
               ☃x = ☃x.rotatePitch(-this.rotationPitch * (float) (Math.PI / 180.0));
               ☃x = ☃x.rotateYaw(-this.rotationYaw * (float) (Math.PI / 180.0));
               double ☃xx = -this.rand.nextFloat() * 0.6 - 0.3;
               Vec3d ☃xxx = new Vec3d((this.rand.nextFloat() - 0.5) * 0.3, ☃xx, 0.6);
               ☃xxx = ☃xxx.rotatePitch(-this.rotationPitch * (float) (Math.PI / 180.0));
               ☃xxx = ☃xxx.rotateYaw(-this.rotationYaw * (float) (Math.PI / 180.0));
               ☃xxx = ☃xxx.add(this.posX, this.posY + this.getEyeHeight(), this.posZ);
               if (☃.getHasSubtypes()) {
                  this.world
                     .spawnParticle(
                        EnumParticleTypes.ITEM_CRACK, ☃xxx.x, ☃xxx.y, ☃xxx.z, ☃x.x, ☃x.y + 0.05, ☃x.z, Item.getIdFromItem(☃.getItem()), ☃.getMetadata()
                     );
               } else {
                  this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, ☃xxx.x, ☃xxx.y, ☃xxx.z, ☃x.x, ☃x.y + 0.05, ☃x.z, Item.getIdFromItem(☃.getItem()));
               }
            }

            this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }
      }
   }

   protected void onItemUseFinish() {
      if (!this.activeItemStack.isEmpty() && this.isHandActive()) {
         this.updateItemUse(this.activeItemStack, 16);
         this.setHeldItem(this.getActiveHand(), this.activeItemStack.onItemUseFinish(this.world, this));
         this.resetActiveHand();
      }
   }

   public ItemStack getActiveItemStack() {
      return this.activeItemStack;
   }

   public int getItemInUseCount() {
      return this.activeItemStackUseCount;
   }

   public int getItemInUseMaxCount() {
      return this.isHandActive() ? this.activeItemStack.getMaxItemUseDuration() - this.getItemInUseCount() : 0;
   }

   public void stopActiveHand() {
      if (!this.activeItemStack.isEmpty()) {
         this.activeItemStack.onPlayerStoppedUsing(this.world, this, this.getItemInUseCount());
      }

      this.resetActiveHand();
   }

   public void resetActiveHand() {
      if (!this.world.isRemote) {
         this.dataManager.set(HAND_STATES, (byte)0);
      }

      this.activeItemStack = ItemStack.EMPTY;
      this.activeItemStackUseCount = 0;
   }

   public boolean isActiveItemStackBlocking() {
      if (this.isHandActive() && !this.activeItemStack.isEmpty()) {
         Item ☃ = this.activeItemStack.getItem();
         return ☃.getItemUseAction(this.activeItemStack) != EnumAction.BLOCK
            ? false
            : ☃.getMaxItemUseDuration(this.activeItemStack) - this.activeItemStackUseCount >= 5;
      } else {
         return false;
      }
   }

   public boolean isElytraFlying() {
      return this.getFlag(7);
   }

   public int getTicksElytraFlying() {
      return this.ticksElytraFlying;
   }

   public boolean attemptTeleport(double var1, double var3, double var5) {
      double ☃ = this.posX;
      double ☃x = this.posY;
      double ☃xx = this.posZ;
      this.posX = ☃;
      this.posY = ☃;
      this.posZ = ☃;
      boolean ☃xxx = false;
      BlockPos ☃xxxx = new BlockPos(this);
      World ☃xxxxx = this.world;
      Random ☃xxxxxx = this.getRNG();
      if (☃xxxxx.isBlockLoaded(☃xxxx)) {
         boolean ☃xxxxxxx = false;

         while (!☃xxxxxxx && ☃xxxx.getY() > 0) {
            BlockPos ☃xxxxxxxx = ☃xxxx.down();
            IBlockState ☃xxxxxxxxx = ☃xxxxx.getBlockState(☃xxxxxxxx);
            if (☃xxxxxxxxx.getMaterial().blocksMovement()) {
               ☃xxxxxxx = true;
            } else {
               this.posY--;
               ☃xxxx = ☃xxxxxxxx;
            }
         }

         if (☃xxxxxxx) {
            this.setPositionAndUpdate(this.posX, this.posY, this.posZ);
            if (☃xxxxx.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !☃xxxxx.containsAnyLiquid(this.getEntityBoundingBox())) {
               ☃xxx = true;
            }
         }
      }

      if (!☃xxx) {
         this.setPositionAndUpdate(☃, ☃x, ☃xx);
         return false;
      } else {
         int ☃xxxxxxx = 128;

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 128; ☃xxxxxxxx++) {
            double ☃xxxxxxxxx = ☃xxxxxxxx / 127.0;
            float ☃xxxxxxxxxx = (☃xxxxxx.nextFloat() - 0.5F) * 0.2F;
            float ☃xxxxxxxxxxx = (☃xxxxxx.nextFloat() - 0.5F) * 0.2F;
            float ☃xxxxxxxxxxxx = (☃xxxxxx.nextFloat() - 0.5F) * 0.2F;
            double ☃xxxxxxxxxxxxx = ☃ + (this.posX - ☃) * ☃xxxxxxxxx + (☃xxxxxx.nextDouble() - 0.5) * this.width * 2.0;
            double ☃xxxxxxxxxxxxxx = ☃x + (this.posY - ☃x) * ☃xxxxxxxxx + ☃xxxxxx.nextDouble() * this.height;
            double ☃xxxxxxxxxxxxxxx = ☃xx + (this.posZ - ☃xx) * ☃xxxxxxxxx + (☃xxxxxx.nextDouble() - 0.5) * this.width * 2.0;
            ☃xxxxx.spawnParticle(EnumParticleTypes.PORTAL, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
         }

         if (this instanceof EntityCreature) {
            ((EntityCreature)this).getNavigator().clearPath();
         }

         return true;
      }
   }

   public boolean canBeHitWithPotion() {
      return true;
   }

   public boolean attackable() {
      return true;
   }

   public void setPartying(BlockPos var1, boolean var2) {
   }
}
