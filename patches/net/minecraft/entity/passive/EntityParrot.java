package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAILandOnOwnersShoulder;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityParrot extends EntityShoulderRiding implements EntityFlying {
   private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityParrot.class, DataSerializers.VARINT);
   private static final Predicate<EntityLiving> CAN_MIMIC = new Predicate<EntityLiving>() {
      public boolean apply(@Nullable EntityLiving var1) {
         return ☃ != null && EntityParrot.IMITATION_SOUND_EVENTS.containsKey(EntityList.REGISTRY.getIDForObject((Class<? extends Entity>)☃.getClass()));
      }
   };
   private static final Item DEADLY_ITEM = Items.COOKIE;
   private static final Set<Item> TAME_ITEMS = Sets.newHashSet(new Item[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS});
   private static final Int2ObjectMap<SoundEvent> IMITATION_SOUND_EVENTS = new Int2ObjectOpenHashMap(32);
   public float flap;
   public float flapSpeed;
   public float oFlapSpeed;
   public float oFlap;
   public float flapping = 1.0F;
   private boolean partyParrot;
   private BlockPos jukeboxPosition;

   public EntityParrot(World var1) {
      super(☃);
      this.setSize(0.5F, 0.9F);
      this.moveHelper = new EntityFlyHelper(this);
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      this.setVariant(this.rand.nextInt(5));
      return super.onInitialSpawn(☃, ☃);
   }

   @Override
   protected void initEntityAI() {
      this.aiSit = new EntityAISit(this);
      this.tasks.addTask(0, new EntityAIPanic(this, 1.25));
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(2, this.aiSit);
      this.tasks.addTask(2, new EntityAIFollowOwnerFlying(this, 1.0, 5.0F, 1.0F));
      this.tasks.addTask(2, new EntityAIWanderAvoidWaterFlying(this, 1.0));
      this.tasks.addTask(3, new EntityAILandOnOwnersShoulder(this));
      this.tasks.addTask(3, new EntityAIFollow(this, 1.0, 3.0F, 7.0F));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0);
      this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4F);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2F);
   }

   @Override
   protected PathNavigate createNavigator(World var1) {
      PathNavigateFlying ☃ = new PathNavigateFlying(this, ☃);
      ☃.setCanOpenDoors(false);
      ☃.setCanFloat(true);
      ☃.setCanEnterDoors(true);
      return ☃;
   }

   @Override
   public float getEyeHeight() {
      return this.height * 0.6F;
   }

   @Override
   public void onLivingUpdate() {
      playMimicSound(this.world, this);
      if (this.jukeboxPosition == null
         || this.jukeboxPosition.distanceSq(this.posX, this.posY, this.posZ) > 12.0
         || this.world.getBlockState(this.jukeboxPosition).getBlock() != Blocks.JUKEBOX) {
         this.partyParrot = false;
         this.jukeboxPosition = null;
      }

      super.onLivingUpdate();
      this.calculateFlapping();
   }

   @Override
   public void setPartying(BlockPos var1, boolean var2) {
      this.jukeboxPosition = ☃;
      this.partyParrot = ☃;
   }

   public boolean isPartying() {
      return this.partyParrot;
   }

   private void calculateFlapping() {
      this.oFlap = this.flap;
      this.oFlapSpeed = this.flapSpeed;
      this.flapSpeed = (float)(this.flapSpeed + (this.onGround ? -1 : 4) * 0.3);
      this.flapSpeed = MathHelper.clamp(this.flapSpeed, 0.0F, 1.0F);
      if (!this.onGround && this.flapping < 1.0F) {
         this.flapping = 1.0F;
      }

      this.flapping = (float)(this.flapping * 0.9);
      if (!this.onGround && this.motionY < 0.0) {
         this.motionY *= 0.6;
      }

      this.flap = this.flap + this.flapping * 2.0F;
   }

   private static boolean playMimicSound(World var0, Entity var1) {
      if (!☃.isSilent() && ☃.rand.nextInt(50) == 0) {
         List<EntityLiving> ☃ = ☃.getEntitiesWithinAABB(EntityLiving.class, ☃.getEntityBoundingBox().grow(20.0), CAN_MIMIC);
         if (!☃.isEmpty()) {
            EntityLiving ☃x = ☃.get(☃.rand.nextInt(☃.size()));
            if (!☃x.isSilent()) {
               SoundEvent ☃xx = getImitatedSound(EntityList.REGISTRY.getIDForObject((Class<? extends Entity>)☃x.getClass()));
               ☃.playSound(null, ☃.posX, ☃.posY, ☃.posZ, ☃xx, ☃.getSoundCategory(), 0.7F, getPitch(☃.rand));
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   @Override
   public boolean processInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!this.isTamed() && TAME_ITEMS.contains(☃.getItem())) {
         if (!☃.capabilities.isCreativeMode) {
            ☃.shrink(1);
         }

         if (!this.isSilent()) {
            this.world
               .playSound(
                  null,
                  this.posX,
                  this.posY,
                  this.posZ,
                  SoundEvents.ENTITY_PARROT_EAT,
                  this.getSoundCategory(),
                  1.0F,
                  1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F
               );
         }

         if (!this.world.isRemote) {
            if (this.rand.nextInt(10) == 0) {
               this.setTamedBy(☃);
               this.playTameEffect(true);
               this.world.setEntityState(this, (byte)7);
            } else {
               this.playTameEffect(false);
               this.world.setEntityState(this, (byte)6);
            }
         }

         return true;
      } else if (☃.getItem() == DEADLY_ITEM) {
         if (!☃.capabilities.isCreativeMode) {
            ☃.shrink(1);
         }

         this.addPotionEffect(new PotionEffect(MobEffects.POISON, 900));
         if (☃.isCreative() || !this.getIsInvulnerable()) {
            this.attackEntityFrom(DamageSource.causePlayerDamage(☃), Float.MAX_VALUE);
         }

         return true;
      } else {
         if (!this.world.isRemote && !this.isFlying() && this.isTamed() && this.isOwner(☃)) {
            this.aiSit.setSitting(!this.isSitting());
         }

         return super.processInteract(☃, ☃);
      }
   }

   @Override
   public boolean isBreedingItem(ItemStack var1) {
      return false;
   }

   @Override
   public boolean getCanSpawnHere() {
      int ☃ = MathHelper.floor(this.posX);
      int ☃x = MathHelper.floor(this.getEntityBoundingBox().minY);
      int ☃xx = MathHelper.floor(this.posZ);
      BlockPos ☃xxx = new BlockPos(☃, ☃x, ☃xx);
      Block ☃xxxx = this.world.getBlockState(☃xxx.down()).getBlock();
      return ☃xxxx instanceof BlockLeaves
         || ☃xxxx == Blocks.GRASS
         || ☃xxxx instanceof BlockLog
         || ☃xxxx == Blocks.AIR && this.world.getLight(☃xxx) > 8 && super.getCanSpawnHere();
   }

   @Override
   public void fall(float var1, float var2) {
   }

   @Override
   protected void updateFallState(double var1, boolean var3, IBlockState var4, BlockPos var5) {
   }

   @Override
   public boolean canMateWith(EntityAnimal var1) {
      return false;
   }

   @Nullable
   @Override
   public EntityAgeable createChild(EntityAgeable var1) {
      return null;
   }

   public static void playAmbientSound(World var0, Entity var1) {
      if (!☃.isSilent() && !playMimicSound(☃, ☃) && ☃.rand.nextInt(200) == 0) {
         ☃.playSound(null, ☃.posX, ☃.posY, ☃.posZ, getAmbientSound(☃.rand), ☃.getSoundCategory(), 1.0F, getPitch(☃.rand));
      }
   }

   @Override
   public boolean attackEntityAsMob(Entity var1) {
      return ☃.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
   }

   @Nullable
   @Override
   public SoundEvent getAmbientSound() {
      return getAmbientSound(this.rand);
   }

   private static SoundEvent getAmbientSound(Random var0) {
      if (☃.nextInt(1000) == 0) {
         List<Integer> ☃ = new ArrayList<>(IMITATION_SOUND_EVENTS.keySet());
         return getImitatedSound(☃.get(☃.nextInt(☃.size())));
      } else {
         return SoundEvents.ENTITY_PARROT_AMBIENT;
      }
   }

   public static SoundEvent getImitatedSound(int var0) {
      return IMITATION_SOUND_EVENTS.containsKey(☃) ? (SoundEvent)IMITATION_SOUND_EVENTS.get(☃) : SoundEvents.ENTITY_PARROT_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_PARROT_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_PARROT_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos var1, Block var2) {
      this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 1.0F);
   }

   @Override
   protected float playFlySound(float var1) {
      this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15F, 1.0F);
      return ☃ + this.flapSpeed / 2.0F;
   }

   @Override
   protected boolean makeFlySound() {
      return true;
   }

   @Override
   protected float getSoundPitch() {
      return getPitch(this.rand);
   }

   private static float getPitch(Random var0) {
      return (☃.nextFloat() - ☃.nextFloat()) * 0.2F + 1.0F;
   }

   @Override
   public SoundCategory getSoundCategory() {
      return SoundCategory.NEUTRAL;
   }

   @Override
   public boolean canBePushed() {
      return true;
   }

   @Override
   protected void collideWithEntity(Entity var1) {
      if (!(☃ instanceof EntityPlayer)) {
         super.collideWithEntity(☃);
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         if (this.aiSit != null) {
            this.aiSit.setSitting(false);
         }

         return super.attackEntityFrom(☃, ☃);
      }
   }

   public int getVariant() {
      return MathHelper.clamp(this.dataManager.get(VARIANT), 0, 4);
   }

   public void setVariant(int var1) {
      this.dataManager.set(VARIANT, ☃);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(VARIANT, 0);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("Variant", this.getVariant());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.setVariant(☃.getInteger("Variant"));
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_PARROT;
   }

   public boolean isFlying() {
      return !this.onGround;
   }

   static {
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityBlaze.class), SoundEvents.E_PARROT_IM_BLAZE);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityCaveSpider.class), SoundEvents.E_PARROT_IM_SPIDER);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityCreeper.class), SoundEvents.E_PARROT_IM_CREEPER);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityElderGuardian.class), SoundEvents.E_PARROT_IM_ELDER_GUARDIAN);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityDragon.class), SoundEvents.E_PARROT_IM_ENDERDRAGON);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityEnderman.class), SoundEvents.E_PARROT_IM_ENDERMAN);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityEndermite.class), SoundEvents.E_PARROT_IM_ENDERMITE);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityEvoker.class), SoundEvents.E_PARROT_IM_EVOCATION_ILLAGER);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityGhast.class), SoundEvents.E_PARROT_IM_GHAST);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityHusk.class), SoundEvents.E_PARROT_IM_HUSK);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityIllusionIllager.class), SoundEvents.E_PARROT_IM_ILLUSION_ILLAGER);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityMagmaCube.class), SoundEvents.E_PARROT_IM_MAGMACUBE);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityPigZombie.class), SoundEvents.E_PARROT_IM_ZOMBIE_PIGMAN);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityPolarBear.class), SoundEvents.E_PARROT_IM_POLAR_BEAR);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityShulker.class), SoundEvents.E_PARROT_IM_SHULKER);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySilverfish.class), SoundEvents.E_PARROT_IM_SILVERFISH);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySkeleton.class), SoundEvents.E_PARROT_IM_SKELETON);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySlime.class), SoundEvents.E_PARROT_IM_SLIME);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySpider.class), SoundEvents.E_PARROT_IM_SPIDER);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityStray.class), SoundEvents.E_PARROT_IM_STRAY);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityVex.class), SoundEvents.E_PARROT_IM_VEX);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityVindicator.class), SoundEvents.E_PARROT_IM_VINDICATION_ILLAGER);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWitch.class), SoundEvents.E_PARROT_IM_WITCH);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWither.class), SoundEvents.E_PARROT_IM_WITHER);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWitherSkeleton.class), SoundEvents.E_PARROT_IM_WITHER_SKELETON);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWolf.class), SoundEvents.E_PARROT_IM_WOLF);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityZombie.class), SoundEvents.E_PARROT_IM_ZOMBIE);
      IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityZombieVillager.class), SoundEvents.E_PARROT_IM_ZOMBIE_VILLAGER);
   }
}
