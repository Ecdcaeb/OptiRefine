package net.minecraft.entity.monster;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEnderman extends EntityMob {
   private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
   private static final AttributeModifier ATTACKING_SPEED_BOOST = new AttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.15F, 0)
      .setSaved(false);
   private static final Set<Block> CARRIABLE_BLOCKS = Sets.newIdentityHashSet();
   private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK = EntityDataManager.createKey(
      EntityEnderman.class, DataSerializers.OPTIONAL_BLOCK_STATE
   );
   private static final DataParameter<Boolean> SCREAMING = EntityDataManager.createKey(EntityEnderman.class, DataSerializers.BOOLEAN);
   private int lastCreepySound;
   private int targetChangeTime;

   public EntityEnderman(World var1) {
      super(☃);
      this.setSize(0.6F, 2.9F);
      this.stepHeight = 1.0F;
      this.setPathPriority(PathNodeType.WATER, -1.0F);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0, false));
      this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0, 0.0F));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.tasks.addTask(10, new EntityEnderman.AIPlaceBlock(this));
      this.tasks.addTask(11, new EntityEnderman.AITakeBlock(this));
      this.targetTasks.addTask(1, new EntityEnderman.AIFindPlayer(this));
      this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityEndermite.class, 10, true, false, new Predicate<EntityEndermite>() {
         public boolean apply(@Nullable EntityEndermite var1) {
            return ☃.isSpawnedByPlayer();
         }
      }));
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3F);
      this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0);
      this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0);
   }

   @Override
   public void setAttackTarget(@Nullable EntityLivingBase var1) {
      super.setAttackTarget(☃);
      IAttributeInstance ☃ = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
      if (☃ == null) {
         this.targetChangeTime = 0;
         this.dataManager.set(SCREAMING, false);
         ☃.removeModifier(ATTACKING_SPEED_BOOST);
      } else {
         this.targetChangeTime = this.ticksExisted;
         this.dataManager.set(SCREAMING, true);
         if (!☃.hasModifier(ATTACKING_SPEED_BOOST)) {
            ☃.applyModifier(ATTACKING_SPEED_BOOST);
         }
      }
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(CARRIED_BLOCK, Optional.absent());
      this.dataManager.register(SCREAMING, false);
   }

   public void playEndermanSound() {
      if (this.ticksExisted >= this.lastCreepySound + 400) {
         this.lastCreepySound = this.ticksExisted;
         if (!this.isSilent()) {
            this.world
               .playSound(this.posX, this.posY + this.getEyeHeight(), this.posZ, SoundEvents.ENTITY_ENDERMEN_STARE, this.getSoundCategory(), 2.5F, 1.0F, false);
         }
      }
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (SCREAMING.equals(☃) && this.isScreaming() && this.world.isRemote) {
         this.playEndermanSound();
      }

      super.notifyDataManagerChange(☃);
   }

   public static void registerFixesEnderman(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityEnderman.class);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      IBlockState ☃ = this.getHeldBlockState();
      if (☃ != null) {
         ☃.setShort("carried", (short)Block.getIdFromBlock(☃.getBlock()));
         ☃.setShort("carriedData", (short)☃.getBlock().getMetaFromState(☃));
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      IBlockState ☃;
      if (☃.hasKey("carried", 8)) {
         ☃ = Block.getBlockFromName(☃.getString("carried")).getStateFromMeta(☃.getShort("carriedData") & '\uffff');
      } else {
         ☃ = Block.getBlockById(☃.getShort("carried")).getStateFromMeta(☃.getShort("carriedData") & '\uffff');
      }

      if (☃ == null || ☃.getBlock() == null || ☃.getMaterial() == Material.AIR) {
         ☃ = null;
      }

      this.setHeldBlockState(☃);
   }

   private boolean shouldAttackPlayer(EntityPlayer var1) {
      ItemStack ☃ = ☃.inventory.armorInventory.get(3);
      if (☃.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
         return false;
      } else {
         Vec3d ☃x = ☃.getLook(1.0F).normalize();
         Vec3d ☃xx = new Vec3d(this.posX - ☃.posX, this.getEntityBoundingBox().minY + this.getEyeHeight() - (☃.posY + ☃.getEyeHeight()), this.posZ - ☃.posZ);
         double ☃xxx = ☃xx.length();
         ☃xx = ☃xx.normalize();
         double ☃xxxx = ☃x.dotProduct(☃xx);
         return ☃xxxx > 1.0 - 0.025 / ☃xxx ? ☃.canEntityBeSeen(this) : false;
      }
   }

   @Override
   public float getEyeHeight() {
      return 2.55F;
   }

   @Override
   public void onLivingUpdate() {
      if (this.world.isRemote) {
         for (int ☃ = 0; ☃ < 2; ☃++) {
            this.world
               .spawnParticle(
                  EnumParticleTypes.PORTAL,
                  this.posX + (this.rand.nextDouble() - 0.5) * this.width,
                  this.posY + this.rand.nextDouble() * this.height - 0.25,
                  this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
                  (this.rand.nextDouble() - 0.5) * 2.0,
                  -this.rand.nextDouble(),
                  (this.rand.nextDouble() - 0.5) * 2.0
               );
         }
      }

      this.isJumping = false;
      super.onLivingUpdate();
   }

   @Override
   protected void updateAITasks() {
      if (this.isWet()) {
         this.attackEntityFrom(DamageSource.DROWN, 1.0F);
      }

      if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600) {
         float ☃ = this.getBrightness();
         if (☃ > 0.5F && this.world.canSeeSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0F < (☃ - 0.4F) * 2.0F) {
            this.setAttackTarget(null);
            this.teleportRandomly();
         }
      }

      super.updateAITasks();
   }

   protected boolean teleportRandomly() {
      double ☃ = this.posX + (this.rand.nextDouble() - 0.5) * 64.0;
      double ☃x = this.posY + (this.rand.nextInt(64) - 32);
      double ☃xx = this.posZ + (this.rand.nextDouble() - 0.5) * 64.0;
      return this.teleportTo(☃, ☃x, ☃xx);
   }

   protected boolean teleportToEntity(Entity var1) {
      Vec3d ☃ = new Vec3d(this.posX - ☃.posX, this.getEntityBoundingBox().minY + this.height / 2.0F - ☃.posY + ☃.getEyeHeight(), this.posZ - ☃.posZ);
      ☃ = ☃.normalize();
      double ☃x = 16.0;
      double ☃xx = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - ☃.x * 16.0;
      double ☃xxx = this.posY + (this.rand.nextInt(16) - 8) - ☃.y * 16.0;
      double ☃xxxx = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - ☃.z * 16.0;
      return this.teleportTo(☃xx, ☃xxx, ☃xxxx);
   }

   private boolean teleportTo(double var1, double var3, double var5) {
      boolean ☃ = this.attemptTeleport(☃, ☃, ☃);
      if (☃) {
         this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
         this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
      }

      return ☃;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return SoundEvents.ENTITY_ENDERMEN_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_ENDERMEN_DEATH;
   }

   @Override
   protected void dropEquipment(boolean var1, int var2) {
      super.dropEquipment(☃, ☃);
      IBlockState ☃ = this.getHeldBlockState();
      if (☃ != null) {
         Item ☃x = Item.getItemFromBlock(☃.getBlock());
         int ☃xx = ☃x.getHasSubtypes() ? ☃.getBlock().getMetaFromState(☃) : 0;
         this.entityDropItem(new ItemStack(☃x, 1, ☃xx), 0.0F);
      }
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_ENDERMAN;
   }

   public void setHeldBlockState(@Nullable IBlockState var1) {
      this.dataManager.set(CARRIED_BLOCK, Optional.fromNullable(☃));
   }

   @Nullable
   public IBlockState getHeldBlockState() {
      return (IBlockState)this.dataManager.get(CARRIED_BLOCK).orNull();
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else if (☃ instanceof EntityDamageSourceIndirect) {
         for (int ☃ = 0; ☃ < 64; ☃++) {
            if (this.teleportRandomly()) {
               return true;
            }
         }

         return false;
      } else {
         boolean ☃x = super.attackEntityFrom(☃, ☃);
         if (☃.isUnblockable() && this.rand.nextInt(10) != 0) {
            this.teleportRandomly();
         }

         return ☃x;
      }
   }

   public boolean isScreaming() {
      return this.dataManager.get(SCREAMING);
   }

   static {
      CARRIABLE_BLOCKS.add(Blocks.GRASS);
      CARRIABLE_BLOCKS.add(Blocks.DIRT);
      CARRIABLE_BLOCKS.add(Blocks.SAND);
      CARRIABLE_BLOCKS.add(Blocks.GRAVEL);
      CARRIABLE_BLOCKS.add(Blocks.YELLOW_FLOWER);
      CARRIABLE_BLOCKS.add(Blocks.RED_FLOWER);
      CARRIABLE_BLOCKS.add(Blocks.BROWN_MUSHROOM);
      CARRIABLE_BLOCKS.add(Blocks.RED_MUSHROOM);
      CARRIABLE_BLOCKS.add(Blocks.TNT);
      CARRIABLE_BLOCKS.add(Blocks.CACTUS);
      CARRIABLE_BLOCKS.add(Blocks.CLAY);
      CARRIABLE_BLOCKS.add(Blocks.PUMPKIN);
      CARRIABLE_BLOCKS.add(Blocks.MELON_BLOCK);
      CARRIABLE_BLOCKS.add(Blocks.MYCELIUM);
      CARRIABLE_BLOCKS.add(Blocks.NETHERRACK);
   }

   static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {
      private final EntityEnderman enderman;
      private EntityPlayer player;
      private int aggroTime;
      private int teleportTime;

      public AIFindPlayer(EntityEnderman var1) {
         super(☃, EntityPlayer.class, false);
         this.enderman = ☃;
      }

      @Override
      public boolean shouldExecute() {
         double ☃ = this.getTargetDistance();
         this.player = this.enderman
            .world
            .getNearestAttackablePlayer(this.enderman.posX, this.enderman.posY, this.enderman.posZ, ☃, ☃, null, new Predicate<EntityPlayer>() {
               public boolean apply(@Nullable EntityPlayer var1) {
                  return ☃ != null && AIFindPlayer.this.enderman.shouldAttackPlayer(☃);
               }
            });
         return this.player != null;
      }

      @Override
      public void startExecuting() {
         this.aggroTime = 5;
         this.teleportTime = 0;
      }

      @Override
      public void resetTask() {
         this.player = null;
         super.resetTask();
      }

      @Override
      public boolean shouldContinueExecuting() {
         if (this.player != null) {
            if (!this.enderman.shouldAttackPlayer(this.player)) {
               return false;
            } else {
               this.enderman.faceEntity(this.player, 10.0F, 10.0F);
               return true;
            }
         } else {
            return this.targetEntity != null && this.targetEntity.isEntityAlive() ? true : super.shouldContinueExecuting();
         }
      }

      @Override
      public void updateTask() {
         if (this.player != null) {
            if (--this.aggroTime <= 0) {
               this.targetEntity = this.player;
               this.player = null;
               super.startExecuting();
            }
         } else {
            if (this.targetEntity != null) {
               if (this.enderman.shouldAttackPlayer(this.targetEntity)) {
                  if (this.targetEntity.getDistanceSq(this.enderman) < 16.0) {
                     this.enderman.teleportRandomly();
                  }

                  this.teleportTime = 0;
               } else if (this.targetEntity.getDistanceSq(this.enderman) > 256.0
                  && this.teleportTime++ >= 30
                  && this.enderman.teleportToEntity(this.targetEntity)) {
                  this.teleportTime = 0;
               }
            }

            super.updateTask();
         }
      }
   }

   static class AIPlaceBlock extends EntityAIBase {
      private final EntityEnderman enderman;

      public AIPlaceBlock(EntityEnderman var1) {
         this.enderman = ☃;
      }

      @Override
      public boolean shouldExecute() {
         if (this.enderman.getHeldBlockState() == null) {
            return false;
         } else {
            return !this.enderman.world.getGameRules().getBoolean("mobGriefing") ? false : this.enderman.getRNG().nextInt(2000) == 0;
         }
      }

      @Override
      public void updateTask() {
         Random ☃ = this.enderman.getRNG();
         World ☃x = this.enderman.world;
         int ☃xx = MathHelper.floor(this.enderman.posX - 1.0 + ☃.nextDouble() * 2.0);
         int ☃xxx = MathHelper.floor(this.enderman.posY + ☃.nextDouble() * 2.0);
         int ☃xxxx = MathHelper.floor(this.enderman.posZ - 1.0 + ☃.nextDouble() * 2.0);
         BlockPos ☃xxxxx = new BlockPos(☃xx, ☃xxx, ☃xxxx);
         IBlockState ☃xxxxxx = ☃x.getBlockState(☃xxxxx);
         IBlockState ☃xxxxxxx = ☃x.getBlockState(☃xxxxx.down());
         IBlockState ☃xxxxxxxx = this.enderman.getHeldBlockState();
         if (☃xxxxxxxx != null && this.canPlaceBlock(☃x, ☃xxxxx, ☃xxxxxxxx.getBlock(), ☃xxxxxx, ☃xxxxxxx)) {
            ☃x.setBlockState(☃xxxxx, ☃xxxxxxxx, 3);
            this.enderman.setHeldBlockState(null);
         }
      }

      private boolean canPlaceBlock(World var1, BlockPos var2, Block var3, IBlockState var4, IBlockState var5) {
         if (!☃.canPlaceBlockAt(☃, ☃)) {
            return false;
         } else if (☃.getMaterial() != Material.AIR) {
            return false;
         } else {
            return ☃.getMaterial() == Material.AIR ? false : ☃.isFullCube();
         }
      }
   }

   static class AITakeBlock extends EntityAIBase {
      private final EntityEnderman enderman;

      public AITakeBlock(EntityEnderman var1) {
         this.enderman = ☃;
      }

      @Override
      public boolean shouldExecute() {
         if (this.enderman.getHeldBlockState() != null) {
            return false;
         } else {
            return !this.enderman.world.getGameRules().getBoolean("mobGriefing") ? false : this.enderman.getRNG().nextInt(20) == 0;
         }
      }

      @Override
      public void updateTask() {
         Random ☃ = this.enderman.getRNG();
         World ☃x = this.enderman.world;
         int ☃xx = MathHelper.floor(this.enderman.posX - 2.0 + ☃.nextDouble() * 4.0);
         int ☃xxx = MathHelper.floor(this.enderman.posY + ☃.nextDouble() * 3.0);
         int ☃xxxx = MathHelper.floor(this.enderman.posZ - 2.0 + ☃.nextDouble() * 4.0);
         BlockPos ☃xxxxx = new BlockPos(☃xx, ☃xxx, ☃xxxx);
         IBlockState ☃xxxxxx = ☃x.getBlockState(☃xxxxx);
         Block ☃xxxxxxx = ☃xxxxxx.getBlock();
         RayTraceResult ☃xxxxxxxx = ☃x.rayTraceBlocks(
            new Vec3d(MathHelper.floor(this.enderman.posX) + 0.5F, ☃xxx + 0.5F, MathHelper.floor(this.enderman.posZ) + 0.5F),
            new Vec3d(☃xx + 0.5F, ☃xxx + 0.5F, ☃xxxx + 0.5F),
            false,
            true,
            false
         );
         boolean ☃xxxxxxxxx = ☃xxxxxxxx != null && ☃xxxxxxxx.getBlockPos().equals(☃xxxxx);
         if (EntityEnderman.CARRIABLE_BLOCKS.contains(☃xxxxxxx) && ☃xxxxxxxxx) {
            this.enderman.setHeldBlockState(☃xxxxxx);
            ☃x.setBlockToAir(☃xxxxx);
         }
      }
   }
}
