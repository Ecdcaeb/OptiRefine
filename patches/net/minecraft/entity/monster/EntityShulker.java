package net.minecraft.entity.monster;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityShulker extends EntityGolem implements IMob {
   private static final UUID COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
   private static final AttributeModifier COVERED_ARMOR_BONUS_MODIFIER = new AttributeModifier(COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0, 0)
      .setSaved(false);
   protected static final DataParameter<EnumFacing> ATTACHED_FACE = EntityDataManager.createKey(EntityShulker.class, DataSerializers.FACING);
   protected static final DataParameter<Optional<BlockPos>> ATTACHED_BLOCK_POS = EntityDataManager.createKey(
      EntityShulker.class, DataSerializers.OPTIONAL_BLOCK_POS
   );
   protected static final DataParameter<Byte> PEEK_TICK = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
   protected static final DataParameter<Byte> COLOR = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
   public static final EnumDyeColor DEFAULT_COLOR = EnumDyeColor.PURPLE;
   private float prevPeekAmount;
   private float peekAmount;
   private BlockPos currentAttachmentPosition;
   private int clientSideTeleportInterpolation;

   public EntityShulker(World var1) {
      super(☃);
      this.setSize(1.0F, 1.0F);
      this.prevRenderYawOffset = 180.0F;
      this.renderYawOffset = 180.0F;
      this.isImmuneToFire = true;
      this.currentAttachmentPosition = null;
      this.experienceValue = 5;
   }

   @Nullable
   @Override
   public IEntityLivingData onInitialSpawn(DifficultyInstance var1, @Nullable IEntityLivingData var2) {
      this.renderYawOffset = 180.0F;
      this.prevRenderYawOffset = 180.0F;
      this.rotationYaw = 180.0F;
      this.prevRotationYaw = 180.0F;
      this.rotationYawHead = 180.0F;
      this.prevRotationYawHead = 180.0F;
      return super.onInitialSpawn(☃, ☃);
   }

   @Override
   protected void initEntityAI() {
      this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(4, new EntityShulker.AIAttack());
      this.tasks.addTask(7, new EntityShulker.AIPeek());
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(2, new EntityShulker.AIAttackNearest(this));
      this.targetTasks.addTask(3, new EntityShulker.AIDefenseAttack(this));
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   public SoundCategory getSoundCategory() {
      return SoundCategory.HOSTILE;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_SHULKER_AMBIENT;
   }

   @Override
   public void playLivingSound() {
      if (!this.isClosed()) {
         super.playLivingSound();
      }
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_SHULKER_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource var1) {
      return this.isClosed() ? SoundEvents.ENTITY_SHULKER_HURT_CLOSED : SoundEvents.ENTITY_SHULKER_HURT;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(ATTACHED_FACE, EnumFacing.DOWN);
      this.dataManager.register(ATTACHED_BLOCK_POS, Optional.absent());
      this.dataManager.register(PEEK_TICK, (byte)0);
      this.dataManager.register(COLOR, (byte)DEFAULT_COLOR.getMetadata());
   }

   @Override
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
   }

   @Override
   protected EntityBodyHelper createBodyHelper() {
      return new EntityShulker.BodyHelper(this);
   }

   public static void registerFixesShulker(DataFixer var0) {
      EntityLiving.registerFixesMob(☃, EntityShulker.class);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.dataManager.set(ATTACHED_FACE, EnumFacing.byIndex(☃.getByte("AttachFace")));
      this.dataManager.set(PEEK_TICK, ☃.getByte("Peek"));
      this.dataManager.set(COLOR, ☃.getByte("Color"));
      if (☃.hasKey("APX")) {
         int ☃ = ☃.getInteger("APX");
         int ☃x = ☃.getInteger("APY");
         int ☃xx = ☃.getInteger("APZ");
         this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(new BlockPos(☃, ☃x, ☃xx)));
      } else {
         this.dataManager.set(ATTACHED_BLOCK_POS, Optional.absent());
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setByte("AttachFace", (byte)this.dataManager.get(ATTACHED_FACE).getIndex());
      ☃.setByte("Peek", this.dataManager.get(PEEK_TICK));
      ☃.setByte("Color", this.dataManager.get(COLOR));
      BlockPos ☃ = this.getAttachmentPos();
      if (☃ != null) {
         ☃.setInteger("APX", ☃.getX());
         ☃.setInteger("APY", ☃.getY());
         ☃.setInteger("APZ", ☃.getZ());
      }
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      BlockPos ☃ = (BlockPos)this.dataManager.get(ATTACHED_BLOCK_POS).orNull();
      if (☃ == null && !this.world.isRemote) {
         ☃ = new BlockPos(this);
         this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(☃));
      }

      if (this.isRiding()) {
         ☃ = null;
         float ☃x = this.getRidingEntity().rotationYaw;
         this.rotationYaw = ☃x;
         this.renderYawOffset = ☃x;
         this.prevRenderYawOffset = ☃x;
         this.clientSideTeleportInterpolation = 0;
      } else if (!this.world.isRemote) {
         IBlockState ☃x = this.world.getBlockState(☃);
         if (☃x.getMaterial() != Material.AIR) {
            if (☃x.getBlock() == Blocks.PISTON_EXTENSION) {
               EnumFacing ☃xx = ☃x.getValue(BlockPistonBase.FACING);
               if (this.world.isAirBlock(☃.offset(☃xx))) {
                  ☃ = ☃.offset(☃xx);
                  this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(☃));
               } else {
                  this.tryTeleportToNewPosition();
               }
            } else if (☃x.getBlock() == Blocks.PISTON_HEAD) {
               EnumFacing ☃xx = ☃x.getValue(BlockPistonExtension.FACING);
               if (this.world.isAirBlock(☃.offset(☃xx))) {
                  ☃ = ☃.offset(☃xx);
                  this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(☃));
               } else {
                  this.tryTeleportToNewPosition();
               }
            } else {
               this.tryTeleportToNewPosition();
            }
         }

         BlockPos ☃xx = ☃.offset(this.getAttachmentFacing());
         if (!this.world.isBlockNormalCube(☃xx, false)) {
            boolean ☃xxx = false;

            for (EnumFacing ☃xxxx : EnumFacing.values()) {
               ☃xx = ☃.offset(☃xxxx);
               if (this.world.isBlockNormalCube(☃xx, false)) {
                  this.dataManager.set(ATTACHED_FACE, ☃xxxx);
                  ☃xxx = true;
                  break;
               }
            }

            if (!☃xxx) {
               this.tryTeleportToNewPosition();
            }
         }

         BlockPos ☃xxx = ☃.offset(this.getAttachmentFacing().getOpposite());
         if (this.world.isBlockNormalCube(☃xxx, false)) {
            this.tryTeleportToNewPosition();
         }
      }

      float ☃xxx = this.getPeekTick() * 0.01F;
      this.prevPeekAmount = this.peekAmount;
      if (this.peekAmount > ☃xxx) {
         this.peekAmount = MathHelper.clamp(this.peekAmount - 0.05F, ☃xxx, 1.0F);
      } else if (this.peekAmount < ☃xxx) {
         this.peekAmount = MathHelper.clamp(this.peekAmount + 0.05F, 0.0F, ☃xxx);
      }

      if (☃ != null) {
         if (this.world.isRemote) {
            if (this.clientSideTeleportInterpolation > 0 && this.currentAttachmentPosition != null) {
               this.clientSideTeleportInterpolation--;
            } else {
               this.currentAttachmentPosition = ☃;
            }
         }

         this.posX = ☃.getX() + 0.5;
         this.posY = ☃.getY();
         this.posZ = ☃.getZ() + 0.5;
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         this.lastTickPosX = this.posX;
         this.lastTickPosY = this.posY;
         this.lastTickPosZ = this.posZ;
         double ☃xxxxx = 0.5 - MathHelper.sin((0.5F + this.peekAmount) * (float) Math.PI) * 0.5;
         double ☃xxxxxx = 0.5 - MathHelper.sin((0.5F + this.prevPeekAmount) * (float) Math.PI) * 0.5;
         double ☃xxxxxxx = ☃xxxxx - ☃xxxxxx;
         double ☃xxxxxxxx = 0.0;
         double ☃xxxxxxxxx = 0.0;
         double ☃xxxxxxxxxx = 0.0;
         EnumFacing ☃xxxxxxxxxxx = this.getAttachmentFacing();
         switch (☃xxxxxxxxxxx) {
            case DOWN:
               this.setEntityBoundingBox(
                  new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0 + ☃xxxxx, this.posZ + 0.5)
               );
               ☃xxxxxxxxx = ☃xxxxxxx;
               break;
            case UP:
               this.setEntityBoundingBox(
                  new AxisAlignedBB(this.posX - 0.5, this.posY - ☃xxxxx, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5)
               );
               ☃xxxxxxxxx = -☃xxxxxxx;
               break;
            case NORTH:
               this.setEntityBoundingBox(
                  new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5 + ☃xxxxx)
               );
               ☃xxxxxxxxxx = ☃xxxxxxx;
               break;
            case SOUTH:
               this.setEntityBoundingBox(
                  new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5 - ☃xxxxx, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5)
               );
               ☃xxxxxxxxxx = -☃xxxxxxx;
               break;
            case WEST:
               this.setEntityBoundingBox(
                  new AxisAlignedBB(this.posX - 0.5, this.posY, this.posZ - 0.5, this.posX + 0.5 + ☃xxxxx, this.posY + 1.0, this.posZ + 0.5)
               );
               ☃xxxxxxxx = ☃xxxxxxx;
               break;
            case EAST:
               this.setEntityBoundingBox(
                  new AxisAlignedBB(this.posX - 0.5 - ☃xxxxx, this.posY, this.posZ - 0.5, this.posX + 0.5, this.posY + 1.0, this.posZ + 0.5)
               );
               ☃xxxxxxxx = -☃xxxxxxx;
         }

         if (☃xxxxxxx > 0.0) {
            List<Entity> ☃xxxxx = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
            if (!☃xxxxx.isEmpty()) {
               for (Entity ☃xxxxxx : ☃xxxxx) {
                  if (!(☃xxxxxx instanceof EntityShulker) && !☃xxxxxx.noClip) {
                     ☃xxxxxx.move(MoverType.SHULKER, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx);
                  }
               }
            }
         }
      }
   }

   @Override
   public void move(MoverType var1, double var2, double var4, double var6) {
      if (☃ == MoverType.SHULKER_BOX) {
         this.tryTeleportToNewPosition();
      } else {
         super.move(☃, ☃, ☃, ☃);
      }
   }

   @Override
   public void setPosition(double var1, double var3, double var5) {
      super.setPosition(☃, ☃, ☃);
      if (this.dataManager != null && this.ticksExisted != 0) {
         Optional<BlockPos> ☃ = this.dataManager.get(ATTACHED_BLOCK_POS);
         Optional<BlockPos> ☃x = Optional.of(new BlockPos(☃, ☃, ☃));
         if (!☃x.equals(☃)) {
            this.dataManager.set(ATTACHED_BLOCK_POS, ☃x);
            this.dataManager.set(PEEK_TICK, (byte)0);
            this.isAirBorne = true;
         }
      }
   }

   protected boolean tryTeleportToNewPosition() {
      if (!this.isAIDisabled() && this.isEntityAlive()) {
         BlockPos ☃ = new BlockPos(this);

         for (int ☃x = 0; ☃x < 5; ☃x++) {
            BlockPos ☃xx = ☃.add(8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17));
            if (☃xx.getY() > 0
               && this.world.isAirBlock(☃xx)
               && this.world.isInsideWorldBorder(this)
               && this.world.getCollisionBoxes(this, new AxisAlignedBB(☃xx)).isEmpty()) {
               boolean ☃xxx = false;

               for (EnumFacing ☃xxxx : EnumFacing.values()) {
                  if (this.world.isBlockNormalCube(☃xx.offset(☃xxxx), false)) {
                     this.dataManager.set(ATTACHED_FACE, ☃xxxx);
                     ☃xxx = true;
                     break;
                  }
               }

               if (☃xxx) {
                  this.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0F, 1.0F);
                  this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(☃xx));
                  this.dataManager.set(PEEK_TICK, (byte)0);
                  this.setAttackTarget(null);
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.prevRenderYawOffset = 180.0F;
      this.renderYawOffset = 180.0F;
      this.rotationYaw = 180.0F;
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (ATTACHED_BLOCK_POS.equals(☃) && this.world.isRemote && !this.isRiding()) {
         BlockPos ☃ = this.getAttachmentPos();
         if (☃ != null) {
            if (this.currentAttachmentPosition == null) {
               this.currentAttachmentPosition = ☃;
            } else {
               this.clientSideTeleportInterpolation = 6;
            }

            this.posX = ☃.getX() + 0.5;
            this.posY = ☃.getY();
            this.posZ = ☃.getZ() + 0.5;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
         }
      }

      super.notifyDataManagerChange(☃);
   }

   @Override
   public void setPositionAndRotationDirect(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.newPosRotationIncrements = 0;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isClosed()) {
         Entity ☃ = ☃.getImmediateSource();
         if (☃ instanceof EntityArrow) {
            return false;
         }
      }

      if (super.attackEntityFrom(☃, ☃)) {
         if (this.getHealth() < this.getMaxHealth() * 0.5 && this.rand.nextInt(4) == 0) {
            this.tryTeleportToNewPosition();
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isClosed() {
      return this.getPeekTick() == 0;
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox() {
      return this.isEntityAlive() ? this.getEntityBoundingBox() : null;
   }

   public EnumFacing getAttachmentFacing() {
      return this.dataManager.get(ATTACHED_FACE);
   }

   @Nullable
   public BlockPos getAttachmentPos() {
      return (BlockPos)this.dataManager.get(ATTACHED_BLOCK_POS).orNull();
   }

   public void setAttachmentPos(@Nullable BlockPos var1) {
      this.dataManager.set(ATTACHED_BLOCK_POS, Optional.fromNullable(☃));
   }

   public int getPeekTick() {
      return this.dataManager.get(PEEK_TICK);
   }

   public void updateArmorModifier(int var1) {
      if (!this.world.isRemote) {
         this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(COVERED_ARMOR_BONUS_MODIFIER);
         if (☃ == 0) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(COVERED_ARMOR_BONUS_MODIFIER);
            this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0F, 1.0F);
         } else {
            this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0F, 1.0F);
         }
      }

      this.dataManager.set(PEEK_TICK, (byte)☃);
   }

   public float getClientPeekAmount(float var1) {
      return this.prevPeekAmount + (this.peekAmount - this.prevPeekAmount) * ☃;
   }

   public int getClientTeleportInterp() {
      return this.clientSideTeleportInterpolation;
   }

   public BlockPos getOldAttachPos() {
      return this.currentAttachmentPosition;
   }

   @Override
   public float getEyeHeight() {
      return 0.5F;
   }

   @Override
   public int getVerticalFaceSpeed() {
      return 180;
   }

   @Override
   public int getHorizontalFaceSpeed() {
      return 180;
   }

   @Override
   public void applyEntityCollision(Entity var1) {
   }

   @Override
   public float getCollisionBorderSize() {
      return 0.0F;
   }

   public boolean isAttachedToBlock() {
      return this.currentAttachmentPosition != null && this.getAttachmentPos() != null;
   }

   @Nullable
   @Override
   protected ResourceLocation getLootTable() {
      return LootTableList.ENTITIES_SHULKER;
   }

   public EnumDyeColor getColor() {
      return EnumDyeColor.byMetadata(this.dataManager.get(COLOR));
   }

   class AIAttack extends EntityAIBase {
      private int attackTime;

      public AIAttack() {
         this.setMutexBits(3);
      }

      @Override
      public boolean shouldExecute() {
         EntityLivingBase ☃ = EntityShulker.this.getAttackTarget();
         return ☃ != null && ☃.isEntityAlive() ? EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL : false;
      }

      @Override
      public void startExecuting() {
         this.attackTime = 20;
         EntityShulker.this.updateArmorModifier(100);
      }

      @Override
      public void resetTask() {
         EntityShulker.this.updateArmorModifier(0);
      }

      @Override
      public void updateTask() {
         if (EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            this.attackTime--;
            EntityLivingBase ☃ = EntityShulker.this.getAttackTarget();
            EntityShulker.this.getLookHelper().setLookPositionWithEntity(☃, 180.0F, 180.0F);
            double ☃x = EntityShulker.this.getDistanceSq(☃);
            if (☃x < 400.0) {
               if (this.attackTime <= 0) {
                  this.attackTime = 20 + EntityShulker.this.rand.nextInt(10) * 20 / 2;
                  EntityShulkerBullet ☃xx = new EntityShulkerBullet(
                     EntityShulker.this.world, EntityShulker.this, ☃, EntityShulker.this.getAttachmentFacing().getAxis()
                  );
                  EntityShulker.this.world.spawnEntity(☃xx);
                  EntityShulker.this.playSound(
                     SoundEvents.ENTITY_SHULKER_SHOOT, 2.0F, (EntityShulker.this.rand.nextFloat() - EntityShulker.this.rand.nextFloat()) * 0.2F + 1.0F
                  );
               }
            } else {
               EntityShulker.this.setAttackTarget(null);
            }

            super.updateTask();
         }
      }
   }

   class AIAttackNearest extends EntityAINearestAttackableTarget<EntityPlayer> {
      public AIAttackNearest(EntityShulker var2) {
         super(☃, EntityPlayer.class, true);
      }

      @Override
      public boolean shouldExecute() {
         return EntityShulker.this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? false : super.shouldExecute();
      }

      @Override
      protected AxisAlignedBB getTargetableArea(double var1) {
         EnumFacing ☃ = ((EntityShulker)this.taskOwner).getAttachmentFacing();
         if (☃.getAxis() == EnumFacing.Axis.X) {
            return this.taskOwner.getEntityBoundingBox().grow(4.0, ☃, ☃);
         } else {
            return ☃.getAxis() == EnumFacing.Axis.Z
               ? this.taskOwner.getEntityBoundingBox().grow(☃, ☃, 4.0)
               : this.taskOwner.getEntityBoundingBox().grow(☃, 4.0, ☃);
         }
      }
   }

   static class AIDefenseAttack extends EntityAINearestAttackableTarget<EntityLivingBase> {
      public AIDefenseAttack(EntityShulker var1) {
         super(☃, EntityLivingBase.class, 10, true, false, new Predicate<EntityLivingBase>() {
            public boolean apply(@Nullable EntityLivingBase var1) {
               return ☃ instanceof IMob;
            }
         });
      }

      @Override
      public boolean shouldExecute() {
         return this.taskOwner.getTeam() == null ? false : super.shouldExecute();
      }

      @Override
      protected AxisAlignedBB getTargetableArea(double var1) {
         EnumFacing ☃ = ((EntityShulker)this.taskOwner).getAttachmentFacing();
         if (☃.getAxis() == EnumFacing.Axis.X) {
            return this.taskOwner.getEntityBoundingBox().grow(4.0, ☃, ☃);
         } else {
            return ☃.getAxis() == EnumFacing.Axis.Z
               ? this.taskOwner.getEntityBoundingBox().grow(☃, ☃, 4.0)
               : this.taskOwner.getEntityBoundingBox().grow(☃, 4.0, ☃);
         }
      }
   }

   class AIPeek extends EntityAIBase {
      private int peekTime;

      private AIPeek() {
      }

      @Override
      public boolean shouldExecute() {
         return EntityShulker.this.getAttackTarget() == null && EntityShulker.this.rand.nextInt(40) == 0;
      }

      @Override
      public boolean shouldContinueExecuting() {
         return EntityShulker.this.getAttackTarget() == null && this.peekTime > 0;
      }

      @Override
      public void startExecuting() {
         this.peekTime = 20 * (1 + EntityShulker.this.rand.nextInt(3));
         EntityShulker.this.updateArmorModifier(30);
      }

      @Override
      public void resetTask() {
         if (EntityShulker.this.getAttackTarget() == null) {
            EntityShulker.this.updateArmorModifier(0);
         }
      }

      @Override
      public void updateTask() {
         this.peekTime--;
      }
   }

   class BodyHelper extends EntityBodyHelper {
      public BodyHelper(EntityLivingBase var2) {
         super(☃);
      }

      @Override
      public void updateRenderAngles() {
      }
   }
}
