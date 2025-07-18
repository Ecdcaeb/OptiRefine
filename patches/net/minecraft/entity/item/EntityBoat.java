package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBoat extends Entity {
   private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
   private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.createKey(EntityBoat.class, DataSerializers.FLOAT);
   private static final DataParameter<Integer> BOAT_TYPE = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
   private static final DataParameter<Boolean>[] DATA_ID_PADDLE = new DataParameter[]{
      EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN), EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN)
   };
   private final float[] paddlePositions = new float[2];
   private float momentum;
   private float outOfControlTicks;
   private float deltaRotation;
   private int lerpSteps;
   private double lerpX;
   private double lerpY;
   private double lerpZ;
   private double lerpYaw;
   private double lerpPitch;
   private boolean leftInputDown;
   private boolean rightInputDown;
   private boolean forwardInputDown;
   private boolean backInputDown;
   private double waterLevel;
   private float boatGlide;
   private EntityBoat.Status status;
   private EntityBoat.Status previousStatus;
   private double lastYd;

   public EntityBoat(World var1) {
      super(☃);
      this.preventEntitySpawning = true;
      this.setSize(1.375F, 0.5625F);
   }

   public EntityBoat(World var1, double var2, double var4, double var6) {
      this(☃);
      this.setPosition(☃, ☃, ☃);
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.prevPosX = ☃;
      this.prevPosY = ☃;
      this.prevPosZ = ☃;
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void entityInit() {
      this.dataManager.register(TIME_SINCE_HIT, 0);
      this.dataManager.register(FORWARD_DIRECTION, 1);
      this.dataManager.register(DAMAGE_TAKEN, 0.0F);
      this.dataManager.register(BOAT_TYPE, EntityBoat.Type.OAK.ordinal());

      for (DataParameter<Boolean> ☃ : DATA_ID_PADDLE) {
         this.dataManager.register(☃, false);
      }
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBox(Entity var1) {
      return ☃.canBePushed() ? ☃.getEntityBoundingBox() : null;
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox() {
      return this.getEntityBoundingBox();
   }

   @Override
   public boolean canBePushed() {
      return true;
   }

   @Override
   public double getMountedYOffset() {
      return -0.1;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else if (this.world.isRemote || this.isDead) {
         return true;
      } else if (☃ instanceof EntityDamageSourceIndirect && ☃.getTrueSource() != null && this.isPassenger(☃.getTrueSource())) {
         return false;
      } else {
         this.setForwardDirection(-this.getForwardDirection());
         this.setTimeSinceHit(10);
         this.setDamageTaken(this.getDamageTaken() + ☃ * 10.0F);
         this.markVelocityChanged();
         boolean ☃ = ☃.getTrueSource() instanceof EntityPlayer && ((EntityPlayer)☃.getTrueSource()).capabilities.isCreativeMode;
         if (☃ || this.getDamageTaken() > 40.0F) {
            if (!☃ && this.world.getGameRules().getBoolean("doEntityDrops")) {
               this.dropItemWithOffset(this.getItemBoat(), 1, 0.0F);
            }

            this.setDead();
         }

         return true;
      }
   }

   @Override
   public void applyEntityCollision(Entity var1) {
      if (☃ instanceof EntityBoat) {
         if (☃.getEntityBoundingBox().minY < this.getEntityBoundingBox().maxY) {
            super.applyEntityCollision(☃);
         }
      } else if (☃.getEntityBoundingBox().minY <= this.getEntityBoundingBox().minY) {
         super.applyEntityCollision(☃);
      }
   }

   public Item getItemBoat() {
      switch (this.getBoatType()) {
         case OAK:
         default:
            return Items.BOAT;
         case SPRUCE:
            return Items.SPRUCE_BOAT;
         case BIRCH:
            return Items.BIRCH_BOAT;
         case JUNGLE:
            return Items.JUNGLE_BOAT;
         case ACACIA:
            return Items.ACACIA_BOAT;
         case DARK_OAK:
            return Items.DARK_OAK_BOAT;
      }
   }

   @Override
   public void performHurtAnimation() {
      this.setForwardDirection(-this.getForwardDirection());
      this.setTimeSinceHit(10);
      this.setDamageTaken(this.getDamageTaken() * 11.0F);
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public void setPositionAndRotationDirect(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.lerpX = ☃;
      this.lerpY = ☃;
      this.lerpZ = ☃;
      this.lerpYaw = ☃;
      this.lerpPitch = ☃;
      this.lerpSteps = 10;
   }

   @Override
   public EnumFacing getAdjustedHorizontalFacing() {
      return this.getHorizontalFacing().rotateY();
   }

   @Override
   public void onUpdate() {
      this.previousStatus = this.status;
      this.status = this.getBoatStatus();
      if (this.status != EntityBoat.Status.UNDER_WATER && this.status != EntityBoat.Status.UNDER_FLOWING_WATER) {
         this.outOfControlTicks = 0.0F;
      } else {
         this.outOfControlTicks++;
      }

      if (!this.world.isRemote && this.outOfControlTicks >= 60.0F) {
         this.removePassengers();
      }

      if (this.getTimeSinceHit() > 0) {
         this.setTimeSinceHit(this.getTimeSinceHit() - 1);
      }

      if (this.getDamageTaken() > 0.0F) {
         this.setDamageTaken(this.getDamageTaken() - 1.0F);
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      super.onUpdate();
      this.tickLerp();
      if (this.canPassengerSteer()) {
         if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof EntityPlayer)) {
            this.setPaddleState(false, false);
         }

         this.updateMotion();
         if (this.world.isRemote) {
            this.controlBoat();
            this.world.sendPacketToServer(new CPacketSteerBoat(this.getPaddleState(0), this.getPaddleState(1)));
         }

         this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
      } else {
         this.motionX = 0.0;
         this.motionY = 0.0;
         this.motionZ = 0.0;
      }

      for (int ☃ = 0; ☃ <= 1; ☃++) {
         if (this.getPaddleState(☃)) {
            if (!this.isSilent()
               && this.paddlePositions[☃] % (float) (Math.PI * 2) <= (float) (Math.PI / 4)
               && (this.paddlePositions[☃] + (float) (Math.PI / 8)) % (float) (Math.PI * 2) >= (float) (Math.PI / 4)) {
               SoundEvent ☃x = this.getPaddleSound();
               if (☃x != null) {
                  Vec3d ☃xx = this.getLook(1.0F);
                  double ☃xxx = ☃ == 1 ? -☃xx.z : ☃xx.z;
                  double ☃xxxx = ☃ == 1 ? ☃xx.x : -☃xx.x;
                  this.world
                     .playSound(null, this.posX + ☃xxx, this.posY, this.posZ + ☃xxxx, ☃x, this.getSoundCategory(), 1.0F, 0.8F + 0.4F * this.rand.nextFloat());
               }
            }

            this.paddlePositions[☃] = (float)(this.paddlePositions[☃] + (float) (Math.PI / 8));
         } else {
            this.paddlePositions[☃] = 0.0F;
         }
      }

      this.doBlockCollisions();
      List<Entity> ☃x = this.world
         .getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.2F, -0.01F, 0.2F), EntitySelectors.getTeamCollisionPredicate(this));
      if (!☃x.isEmpty()) {
         boolean ☃xx = !this.world.isRemote && !(this.getControllingPassenger() instanceof EntityPlayer);

         for (int ☃xxx = 0; ☃xxx < ☃x.size(); ☃xxx++) {
            Entity ☃xxxx = ☃x.get(☃xxx);
            if (!☃xxxx.isPassenger(this)) {
               if (☃xx
                  && this.getPassengers().size() < 2
                  && !☃xxxx.isRiding()
                  && ☃xxxx.width < this.width
                  && ☃xxxx instanceof EntityLivingBase
                  && !(☃xxxx instanceof EntityWaterMob)
                  && !(☃xxxx instanceof EntityPlayer)) {
                  ☃xxxx.startRiding(this);
               } else {
                  this.applyEntityCollision(☃xxxx);
               }
            }
         }
      }
   }

   @Nullable
   protected SoundEvent getPaddleSound() {
      switch (this.getBoatStatus()) {
         case IN_WATER:
         case UNDER_WATER:
         case UNDER_FLOWING_WATER:
            return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
         case ON_LAND:
            return SoundEvents.ENTITY_BOAT_PADDLE_LAND;
         case IN_AIR:
         default:
            return null;
      }
   }

   private void tickLerp() {
      if (this.lerpSteps > 0 && !this.canPassengerSteer()) {
         double ☃ = this.posX + (this.lerpX - this.posX) / this.lerpSteps;
         double ☃x = this.posY + (this.lerpY - this.posY) / this.lerpSteps;
         double ☃xx = this.posZ + (this.lerpZ - this.posZ) / this.lerpSteps;
         double ☃xxx = MathHelper.wrapDegrees(this.lerpYaw - this.rotationYaw);
         this.rotationYaw = (float)(this.rotationYaw + ☃xxx / this.lerpSteps);
         this.rotationPitch = (float)(this.rotationPitch + (this.lerpPitch - this.rotationPitch) / this.lerpSteps);
         this.lerpSteps--;
         this.setPosition(☃, ☃x, ☃xx);
         this.setRotation(this.rotationYaw, this.rotationPitch);
      }
   }

   public void setPaddleState(boolean var1, boolean var2) {
      this.dataManager.set(DATA_ID_PADDLE[0], ☃);
      this.dataManager.set(DATA_ID_PADDLE[1], ☃);
   }

   public float getRowingTime(int var1, float var2) {
      return this.getPaddleState(☃) ? (float)MathHelper.clampedLerp(this.paddlePositions[☃] - (float) (Math.PI / 8), this.paddlePositions[☃], ☃) : 0.0F;
   }

   private EntityBoat.Status getBoatStatus() {
      EntityBoat.Status ☃ = this.getUnderwaterStatus();
      if (☃ != null) {
         this.waterLevel = this.getEntityBoundingBox().maxY;
         return ☃;
      } else if (this.checkInWater()) {
         return EntityBoat.Status.IN_WATER;
      } else {
         float ☃x = this.getBoatGlide();
         if (☃x > 0.0F) {
            this.boatGlide = ☃x;
            return EntityBoat.Status.ON_LAND;
         } else {
            return EntityBoat.Status.IN_AIR;
         }
      }
   }

   public float getWaterLevelAbove() {
      AxisAlignedBB ☃ = this.getEntityBoundingBox();
      int ☃x = MathHelper.floor(☃.minX);
      int ☃xx = MathHelper.ceil(☃.maxX);
      int ☃xxx = MathHelper.floor(☃.maxY);
      int ☃xxxx = MathHelper.ceil(☃.maxY - this.lastYd);
      int ☃xxxxx = MathHelper.floor(☃.minZ);
      int ☃xxxxxx = MathHelper.ceil(☃.maxZ);
      BlockPos.PooledMutableBlockPos ☃xxxxxxx = BlockPos.PooledMutableBlockPos.retain();

      try {
         label87:
         for (int ☃xxxxxxxx = ☃xxx; ☃xxxxxxxx < ☃xxxx; ☃xxxxxxxx++) {
            float ☃xxxxxxxxx = 0.0F;
            int ☃xxxxxxxxxx = ☃x;

            while (true) {
               if (☃xxxxxxxxxx < ☃xx) {
                  for (int ☃xxxxxxxxxxx = ☃xxxxx; ☃xxxxxxxxxxx < ☃xxxxxx; ☃xxxxxxxxxxx++) {
                     ☃xxxxxxx.setPos(☃xxxxxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxxx);
                     IBlockState ☃xxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxx);
                     if (☃xxxxxxxxxxxx.getMaterial() == Material.WATER) {
                        ☃xxxxxxxxx = Math.max(☃xxxxxxxxx, BlockLiquid.getBlockLiquidHeight(☃xxxxxxxxxxxx, this.world, ☃xxxxxxx));
                     }

                     if (☃xxxxxxxxx >= 1.0F) {
                        continue label87;
                     }
                  }

                  ☃xxxxxxxxxx++;
               } else {
                  if (☃xxxxxxxxx < 1.0F) {
                     return ☃xxxxxxx.getY() + ☃xxxxxxxxx;
                  }
                  break;
               }
            }
         }

         return ☃xxxx + 1;
      } finally {
         ☃xxxxxxx.release();
      }
   }

   public float getBoatGlide() {
      AxisAlignedBB ☃ = this.getEntityBoundingBox();
      AxisAlignedBB ☃x = new AxisAlignedBB(☃.minX, ☃.minY - 0.001, ☃.minZ, ☃.maxX, ☃.minY, ☃.maxZ);
      int ☃xx = MathHelper.floor(☃x.minX) - 1;
      int ☃xxx = MathHelper.ceil(☃x.maxX) + 1;
      int ☃xxxx = MathHelper.floor(☃x.minY) - 1;
      int ☃xxxxx = MathHelper.ceil(☃x.maxY) + 1;
      int ☃xxxxxx = MathHelper.floor(☃x.minZ) - 1;
      int ☃xxxxxxx = MathHelper.ceil(☃x.maxZ) + 1;
      List<AxisAlignedBB> ☃xxxxxxxx = Lists.newArrayList();
      float ☃xxxxxxxxx = 0.0F;
      int ☃xxxxxxxxxx = 0;
      BlockPos.PooledMutableBlockPos ☃xxxxxxxxxxx = BlockPos.PooledMutableBlockPos.retain();

      try {
         for (int ☃xxxxxxxxxxxx = ☃xx; ☃xxxxxxxxxxxx < ☃xxx; ☃xxxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxxx = ☃xxxxxx; ☃xxxxxxxxxxxxx < ☃xxxxxxx; ☃xxxxxxxxxxxxx++) {
               int ☃xxxxxxxxxxxxxx = (☃xxxxxxxxxxxx != ☃xx && ☃xxxxxxxxxxxx != ☃xxx - 1 ? 0 : 1)
                  + (☃xxxxxxxxxxxxx != ☃xxxxxx && ☃xxxxxxxxxxxxx != ☃xxxxxxx - 1 ? 0 : 1);
               if (☃xxxxxxxxxxxxxx != 2) {
                  for (int ☃xxxxxxxxxxxxxxx = ☃xxxx; ☃xxxxxxxxxxxxxxx < ☃xxxxx; ☃xxxxxxxxxxxxxxx++) {
                     if (☃xxxxxxxxxxxxxx <= 0 || ☃xxxxxxxxxxxxxxx != ☃xxxx && ☃xxxxxxxxxxxxxxx != ☃xxxxx - 1) {
                        ☃xxxxxxxxxxx.setPos(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx);
                        IBlockState ☃xxxxxxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxxx);
                        ☃xxxxxxxxxxxxxxxx.addCollisionBoxToList(this.world, ☃xxxxxxxxxxx, ☃x, ☃xxxxxxxx, this, false);
                        if (!☃xxxxxxxx.isEmpty()) {
                           ☃xxxxxxxxx += ☃xxxxxxxxxxxxxxxx.getBlock().slipperiness;
                           ☃xxxxxxxxxx++;
                        }

                        ☃xxxxxxxx.clear();
                     }
                  }
               }
            }
         }
      } finally {
         ☃xxxxxxxxxxx.release();
      }

      return ☃xxxxxxxxx / ☃xxxxxxxxxx;
   }

   private boolean checkInWater() {
      AxisAlignedBB ☃ = this.getEntityBoundingBox();
      int ☃x = MathHelper.floor(☃.minX);
      int ☃xx = MathHelper.ceil(☃.maxX);
      int ☃xxx = MathHelper.floor(☃.minY);
      int ☃xxxx = MathHelper.ceil(☃.minY + 0.001);
      int ☃xxxxx = MathHelper.floor(☃.minZ);
      int ☃xxxxxx = MathHelper.ceil(☃.maxZ);
      boolean ☃xxxxxxx = false;
      this.waterLevel = Double.MIN_VALUE;
      BlockPos.PooledMutableBlockPos ☃xxxxxxxx = BlockPos.PooledMutableBlockPos.retain();

      try {
         for (int ☃xxxxxxxxx = ☃x; ☃xxxxxxxxx < ☃xx; ☃xxxxxxxxx++) {
            for (int ☃xxxxxxxxxx = ☃xxx; ☃xxxxxxxxxx < ☃xxxx; ☃xxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxx = ☃xxxxx; ☃xxxxxxxxxxx < ☃xxxxxx; ☃xxxxxxxxxxx++) {
                  ☃xxxxxxxx.setPos(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx);
                  IBlockState ☃xxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxx);
                  if (☃xxxxxxxxxxxx.getMaterial() == Material.WATER) {
                     float ☃xxxxxxxxxxxxx = BlockLiquid.getLiquidHeight(☃xxxxxxxxxxxx, this.world, ☃xxxxxxxx);
                     this.waterLevel = Math.max((double)☃xxxxxxxxxxxxx, this.waterLevel);
                     ☃xxxxxxx |= ☃.minY < ☃xxxxxxxxxxxxx;
                  }
               }
            }
         }
      } finally {
         ☃xxxxxxxx.release();
      }

      return ☃xxxxxxx;
   }

   @Nullable
   private EntityBoat.Status getUnderwaterStatus() {
      AxisAlignedBB ☃ = this.getEntityBoundingBox();
      double ☃x = ☃.maxY + 0.001;
      int ☃xx = MathHelper.floor(☃.minX);
      int ☃xxx = MathHelper.ceil(☃.maxX);
      int ☃xxxx = MathHelper.floor(☃.maxY);
      int ☃xxxxx = MathHelper.ceil(☃x);
      int ☃xxxxxx = MathHelper.floor(☃.minZ);
      int ☃xxxxxxx = MathHelper.ceil(☃.maxZ);
      boolean ☃xxxxxxxx = false;
      BlockPos.PooledMutableBlockPos ☃xxxxxxxxx = BlockPos.PooledMutableBlockPos.retain();

      try {
         for (int ☃xxxxxxxxxx = ☃xx; ☃xxxxxxxxxx < ☃xxx; ☃xxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxx = ☃xxxx; ☃xxxxxxxxxxx < ☃xxxxx; ☃xxxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxxx = ☃xxxxxx; ☃xxxxxxxxxxxx < ☃xxxxxxx; ☃xxxxxxxxxxxx++) {
                  ☃xxxxxxxxx.setPos(☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
                  IBlockState ☃xxxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxx);
                  if (☃xxxxxxxxxxxxx.getMaterial() == Material.WATER && ☃x < BlockLiquid.getLiquidHeight(☃xxxxxxxxxxxxx, this.world, ☃xxxxxxxxx)) {
                     if (☃xxxxxxxxxxxxx.getValue(BlockLiquid.LEVEL) != 0) {
                        return EntityBoat.Status.UNDER_FLOWING_WATER;
                     }

                     ☃xxxxxxxx = true;
                  }
               }
            }
         }
      } finally {
         ☃xxxxxxxxx.release();
      }

      return ☃xxxxxxxx ? EntityBoat.Status.UNDER_WATER : null;
   }

   private void updateMotion() {
      double ☃ = -0.04F;
      double ☃x = this.hasNoGravity() ? 0.0 : -0.04F;
      double ☃xx = 0.0;
      this.momentum = 0.05F;
      if (this.previousStatus == EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.ON_LAND) {
         this.waterLevel = this.getEntityBoundingBox().minY + this.height;
         this.setPosition(this.posX, this.getWaterLevelAbove() - this.height + 0.101, this.posZ);
         this.motionY = 0.0;
         this.lastYd = 0.0;
         this.status = EntityBoat.Status.IN_WATER;
      } else {
         if (this.status == EntityBoat.Status.IN_WATER) {
            ☃xx = (this.waterLevel - this.getEntityBoundingBox().minY) / this.height;
            this.momentum = 0.9F;
         } else if (this.status == EntityBoat.Status.UNDER_FLOWING_WATER) {
            ☃x = -7.0E-4;
            this.momentum = 0.9F;
         } else if (this.status == EntityBoat.Status.UNDER_WATER) {
            ☃xx = 0.01F;
            this.momentum = 0.45F;
         } else if (this.status == EntityBoat.Status.IN_AIR) {
            this.momentum = 0.9F;
         } else if (this.status == EntityBoat.Status.ON_LAND) {
            this.momentum = this.boatGlide;
            if (this.getControllingPassenger() instanceof EntityPlayer) {
               this.boatGlide /= 2.0F;
            }
         }

         this.motionX = this.motionX * this.momentum;
         this.motionZ = this.motionZ * this.momentum;
         this.deltaRotation = this.deltaRotation * this.momentum;
         this.motionY += ☃x;
         if (☃xx > 0.0) {
            double ☃xxx = 0.65;
            this.motionY += ☃xx * 0.06153846016296973;
            double ☃xxxx = 0.75;
            this.motionY *= 0.75;
         }
      }
   }

   private void controlBoat() {
      if (this.isBeingRidden()) {
         float ☃ = 0.0F;
         if (this.leftInputDown) {
            this.deltaRotation += -1.0F;
         }

         if (this.rightInputDown) {
            this.deltaRotation++;
         }

         if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
            ☃ += 0.005F;
         }

         this.rotationYaw = this.rotationYaw + this.deltaRotation;
         if (this.forwardInputDown) {
            ☃ += 0.04F;
         }

         if (this.backInputDown) {
            ☃ -= 0.005F;
         }

         this.motionX = this.motionX + MathHelper.sin(-this.rotationYaw * (float) (Math.PI / 180.0)) * ☃;
         this.motionZ = this.motionZ + MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0)) * ☃;
         this.setPaddleState(
            this.rightInputDown && !this.leftInputDown || this.forwardInputDown, this.leftInputDown && !this.rightInputDown || this.forwardInputDown
         );
      }
   }

   @Override
   public void updatePassenger(Entity var1) {
      if (this.isPassenger(☃)) {
         float ☃ = 0.0F;
         float ☃x = (float)((this.isDead ? 0.01F : this.getMountedYOffset()) + ☃.getYOffset());
         if (this.getPassengers().size() > 1) {
            int ☃xx = this.getPassengers().indexOf(☃);
            if (☃xx == 0) {
               ☃ = 0.2F;
            } else {
               ☃ = -0.6F;
            }

            if (☃ instanceof EntityAnimal) {
               ☃ = (float)(☃ + 0.2);
            }
         }

         Vec3d ☃xxx = new Vec3d(☃, 0.0, 0.0).rotateYaw(-this.rotationYaw * (float) (Math.PI / 180.0) - (float) (Math.PI / 2));
         ☃.setPosition(this.posX + ☃xxx.x, this.posY + ☃x, this.posZ + ☃xxx.z);
         ☃.rotationYaw = ☃.rotationYaw + this.deltaRotation;
         ☃.setRotationYawHead(☃.getRotationYawHead() + this.deltaRotation);
         this.applyYawToEntity(☃);
         if (☃ instanceof EntityAnimal && this.getPassengers().size() > 1) {
            int ☃xxxx = ☃.getEntityId() % 2 == 0 ? 90 : 270;
            ☃.setRenderYawOffset(((EntityAnimal)☃).renderYawOffset + ☃xxxx);
            ☃.setRotationYawHead(☃.getRotationYawHead() + ☃xxxx);
         }
      }
   }

   protected void applyYawToEntity(Entity var1) {
      ☃.setRenderYawOffset(this.rotationYaw);
      float ☃ = MathHelper.wrapDegrees(☃.rotationYaw - this.rotationYaw);
      float ☃x = MathHelper.clamp(☃, -105.0F, 105.0F);
      ☃.prevRotationYaw += ☃x - ☃;
      ☃.rotationYaw += ☃x - ☃;
      ☃.setRotationYawHead(☃.rotationYaw);
   }

   @Override
   public void applyOrientationToEntity(Entity var1) {
      this.applyYawToEntity(☃);
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      ☃.setString("Type", this.getBoatType().getName());
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      if (☃.hasKey("Type", 8)) {
         this.setBoatType(EntityBoat.Type.getTypeFromString(☃.getString("Type")));
      }
   }

   @Override
   public boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      if (☃.isSneaking()) {
         return false;
      } else {
         if (!this.world.isRemote && this.outOfControlTicks < 60.0F) {
            ☃.startRiding(this);
         }

         return true;
      }
   }

   @Override
   protected void updateFallState(double var1, boolean var3, IBlockState var4, BlockPos var5) {
      this.lastYd = this.motionY;
      if (!this.isRiding()) {
         if (☃) {
            if (this.fallDistance > 3.0F) {
               if (this.status != EntityBoat.Status.ON_LAND) {
                  this.fallDistance = 0.0F;
                  return;
               }

               this.fall(this.fallDistance, 1.0F);
               if (!this.world.isRemote && !this.isDead) {
                  this.setDead();
                  if (this.world.getGameRules().getBoolean("doEntityDrops")) {
                     for (int ☃ = 0; ☃ < 3; ☃++) {
                        this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, this.getBoatType().getMetadata()), 0.0F);
                     }

                     for (int ☃ = 0; ☃ < 2; ☃++) {
                        this.dropItemWithOffset(Items.STICK, 1, 0.0F);
                     }
                  }
               }
            }

            this.fallDistance = 0.0F;
         } else if (this.world.getBlockState(new BlockPos(this).down()).getMaterial() != Material.WATER && ☃ < 0.0) {
            this.fallDistance = (float)(this.fallDistance - ☃);
         }
      }
   }

   public boolean getPaddleState(int var1) {
      return this.dataManager.get(DATA_ID_PADDLE[☃]) && this.getControllingPassenger() != null;
   }

   public void setDamageTaken(float var1) {
      this.dataManager.set(DAMAGE_TAKEN, ☃);
   }

   public float getDamageTaken() {
      return this.dataManager.get(DAMAGE_TAKEN);
   }

   public void setTimeSinceHit(int var1) {
      this.dataManager.set(TIME_SINCE_HIT, ☃);
   }

   public int getTimeSinceHit() {
      return this.dataManager.get(TIME_SINCE_HIT);
   }

   public void setForwardDirection(int var1) {
      this.dataManager.set(FORWARD_DIRECTION, ☃);
   }

   public int getForwardDirection() {
      return this.dataManager.get(FORWARD_DIRECTION);
   }

   public void setBoatType(EntityBoat.Type var1) {
      this.dataManager.set(BOAT_TYPE, ☃.ordinal());
   }

   public EntityBoat.Type getBoatType() {
      return EntityBoat.Type.byId(this.dataManager.get(BOAT_TYPE));
   }

   @Override
   protected boolean canFitPassenger(Entity var1) {
      return this.getPassengers().size() < 2;
   }

   @Nullable
   @Override
   public Entity getControllingPassenger() {
      List<Entity> ☃ = this.getPassengers();
      return ☃.isEmpty() ? null : ☃.get(0);
   }

   public void updateInputs(boolean var1, boolean var2, boolean var3, boolean var4) {
      this.leftInputDown = ☃;
      this.rightInputDown = ☃;
      this.forwardInputDown = ☃;
      this.backInputDown = ☃;
   }

   public static enum Status {
      IN_WATER,
      UNDER_WATER,
      UNDER_FLOWING_WATER,
      ON_LAND,
      IN_AIR;
   }

   public static enum Type {
      OAK(BlockPlanks.EnumType.OAK.getMetadata(), "oak"),
      SPRUCE(BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce"),
      BIRCH(BlockPlanks.EnumType.BIRCH.getMetadata(), "birch"),
      JUNGLE(BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle"),
      ACACIA(BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia"),
      DARK_OAK(BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak");

      private final String name;
      private final int metadata;

      private Type(int var3, String var4) {
         this.name = ☃;
         this.metadata = ☃;
      }

      public String getName() {
         return this.name;
      }

      public int getMetadata() {
         return this.metadata;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static EntityBoat.Type byId(int var0) {
         if (☃ < 0 || ☃ >= values().length) {
            ☃ = 0;
         }

         return values()[☃];
      }

      public static EntityBoat.Type getTypeFromString(String var0) {
         for (int ☃ = 0; ☃ < values().length; ☃++) {
            if (values()[☃].getName().equals(☃)) {
               return values()[☃];
            }
         }

         return values()[0];
      }
   }
}
