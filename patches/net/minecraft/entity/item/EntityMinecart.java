package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityMinecart extends Entity implements IWorldNameable {
   private static final DataParameter<Integer> ROLLING_AMPLITUDE = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> ROLLING_DIRECTION = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
   private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.FLOAT);
   private static final DataParameter<Integer> DISPLAY_TILE = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
   private static final DataParameter<Integer> DISPLAY_TILE_OFFSET = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
   private static final DataParameter<Boolean> SHOW_BLOCK = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.BOOLEAN);
   private boolean isInReverse;
   private static final int[][][] MATRIX = new int[][][]{
      {{0, 0, -1}, {0, 0, 1}},
      {{-1, 0, 0}, {1, 0, 0}},
      {{-1, -1, 0}, {1, 0, 0}},
      {{-1, 0, 0}, {1, -1, 0}},
      {{0, 0, -1}, {0, -1, 1}},
      {{0, -1, -1}, {0, 0, 1}},
      {{0, 0, 1}, {1, 0, 0}},
      {{0, 0, 1}, {-1, 0, 0}},
      {{0, 0, -1}, {-1, 0, 0}},
      {{0, 0, -1}, {1, 0, 0}}
   };
   private int turnProgress;
   private double minecartX;
   private double minecartY;
   private double minecartZ;
   private double minecartYaw;
   private double minecartPitch;
   private double velocityX;
   private double velocityY;
   private double velocityZ;

   public EntityMinecart(World var1) {
      super(☃);
      this.preventEntitySpawning = true;
      this.setSize(0.98F, 0.7F);
   }

   public static EntityMinecart create(World var0, double var1, double var3, double var5, EntityMinecart.Type var7) {
      switch (☃) {
         case CHEST:
            return new EntityMinecartChest(☃, ☃, ☃, ☃);
         case FURNACE:
            return new EntityMinecartFurnace(☃, ☃, ☃, ☃);
         case TNT:
            return new EntityMinecartTNT(☃, ☃, ☃, ☃);
         case SPAWNER:
            return new EntityMinecartMobSpawner(☃, ☃, ☃, ☃);
         case HOPPER:
            return new EntityMinecartHopper(☃, ☃, ☃, ☃);
         case COMMAND_BLOCK:
            return new EntityMinecartCommandBlock(☃, ☃, ☃, ☃);
         default:
            return new EntityMinecartEmpty(☃, ☃, ☃, ☃);
      }
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void entityInit() {
      this.dataManager.register(ROLLING_AMPLITUDE, 0);
      this.dataManager.register(ROLLING_DIRECTION, 1);
      this.dataManager.register(DAMAGE, 0.0F);
      this.dataManager.register(DISPLAY_TILE, 0);
      this.dataManager.register(DISPLAY_TILE_OFFSET, 6);
      this.dataManager.register(SHOW_BLOCK, false);
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBox(Entity var1) {
      return ☃.canBePushed() ? ☃.getEntityBoundingBox() : null;
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox() {
      return null;
   }

   @Override
   public boolean canBePushed() {
      return true;
   }

   public EntityMinecart(World var1, double var2, double var4, double var6) {
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
   public double getMountedYOffset() {
      return 0.0;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.world.isRemote || this.isDead) {
         return true;
      } else if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         this.setRollingDirection(-this.getRollingDirection());
         this.setRollingAmplitude(10);
         this.markVelocityChanged();
         this.setDamage(this.getDamage() + ☃ * 10.0F);
         boolean ☃ = ☃.getTrueSource() instanceof EntityPlayer && ((EntityPlayer)☃.getTrueSource()).capabilities.isCreativeMode;
         if (☃ || this.getDamage() > 40.0F) {
            this.removePassengers();
            if (☃ && !this.hasCustomName()) {
               this.setDead();
            } else {
               this.killMinecart(☃);
            }
         }

         return true;
      }
   }

   public void killMinecart(DamageSource var1) {
      this.setDead();
      if (this.world.getGameRules().getBoolean("doEntityDrops")) {
         ItemStack ☃ = new ItemStack(Items.MINECART, 1);
         if (this.hasCustomName()) {
            ☃.setStackDisplayName(this.getCustomNameTag());
         }

         this.entityDropItem(☃, 0.0F);
      }
   }

   @Override
   public void performHurtAnimation() {
      this.setRollingDirection(-this.getRollingDirection());
      this.setRollingAmplitude(10);
      this.setDamage(this.getDamage() + this.getDamage() * 10.0F);
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public EnumFacing getAdjustedHorizontalFacing() {
      return this.isInReverse ? this.getHorizontalFacing().getOpposite().rotateY() : this.getHorizontalFacing().rotateY();
   }

   @Override
   public void onUpdate() {
      if (this.getRollingAmplitude() > 0) {
         this.setRollingAmplitude(this.getRollingAmplitude() - 1);
      }

      if (this.getDamage() > 0.0F) {
         this.setDamage(this.getDamage() - 1.0F);
      }

      if (this.posY < -64.0) {
         this.outOfWorld();
      }

      if (!this.world.isRemote && this.world instanceof WorldServer) {
         this.world.profiler.startSection("portal");
         MinecraftServer ☃ = this.world.getMinecraftServer();
         int ☃x = this.getMaxInPortalTime();
         if (this.inPortal) {
            if (☃.getAllowNether()) {
               if (!this.isRiding() && this.portalCounter++ >= ☃x) {
                  this.portalCounter = ☃x;
                  this.timeUntilPortal = this.getPortalCooldown();
                  int ☃xx;
                  if (this.world.provider.getDimensionType().getId() == -1) {
                     ☃xx = 0;
                  } else {
                     ☃xx = -1;
                  }

                  this.changeDimension(☃xx);
               }

               this.inPortal = false;
            }
         } else {
            if (this.portalCounter > 0) {
               this.portalCounter -= 4;
            }

            if (this.portalCounter < 0) {
               this.portalCounter = 0;
            }
         }

         if (this.timeUntilPortal > 0) {
            this.timeUntilPortal--;
         }

         this.world.profiler.endSection();
      }

      if (this.world.isRemote) {
         if (this.turnProgress > 0) {
            double ☃xx = this.posX + (this.minecartX - this.posX) / this.turnProgress;
            double ☃xxx = this.posY + (this.minecartY - this.posY) / this.turnProgress;
            double ☃xxxx = this.posZ + (this.minecartZ - this.posZ) / this.turnProgress;
            double ☃xxxxx = MathHelper.wrapDegrees(this.minecartYaw - this.rotationYaw);
            this.rotationYaw = (float)(this.rotationYaw + ☃xxxxx / this.turnProgress);
            this.rotationPitch = (float)(this.rotationPitch + (this.minecartPitch - this.rotationPitch) / this.turnProgress);
            this.turnProgress--;
            this.setPosition(☃xx, ☃xxx, ☃xxxx);
            this.setRotation(this.rotationYaw, this.rotationPitch);
         } else {
            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
         }
      } else {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         if (!this.hasNoGravity()) {
            this.motionY -= 0.04F;
         }

         int ☃xx = MathHelper.floor(this.posX);
         int ☃xxx = MathHelper.floor(this.posY);
         int ☃xxxx = MathHelper.floor(this.posZ);
         if (BlockRailBase.isRailBlock(this.world, new BlockPos(☃xx, ☃xxx - 1, ☃xxxx))) {
            ☃xxx--;
         }

         BlockPos ☃xxxxx = new BlockPos(☃xx, ☃xxx, ☃xxxx);
         IBlockState ☃xxxxxx = this.world.getBlockState(☃xxxxx);
         if (BlockRailBase.isRailBlock(☃xxxxxx)) {
            this.moveAlongTrack(☃xxxxx, ☃xxxxxx);
            if (☃xxxxxx.getBlock() == Blocks.ACTIVATOR_RAIL) {
               this.onActivatorRailPass(☃xx, ☃xxx, ☃xxxx, ☃xxxxxx.getValue(BlockRailPowered.POWERED));
            }
         } else {
            this.moveDerailedMinecart();
         }

         this.doBlockCollisions();
         this.rotationPitch = 0.0F;
         double ☃xxxxxxx = this.prevPosX - this.posX;
         double ☃xxxxxxxx = this.prevPosZ - this.posZ;
         if (☃xxxxxxx * ☃xxxxxxx + ☃xxxxxxxx * ☃xxxxxxxx > 0.001) {
            this.rotationYaw = (float)(MathHelper.atan2(☃xxxxxxxx, ☃xxxxxxx) * 180.0 / Math.PI);
            if (this.isInReverse) {
               this.rotationYaw += 180.0F;
            }
         }

         double ☃xxxxxxxxx = MathHelper.wrapDegrees(this.rotationYaw - this.prevRotationYaw);
         if (☃xxxxxxxxx < -170.0 || ☃xxxxxxxxx >= 170.0) {
            this.rotationYaw += 180.0F;
            this.isInReverse = !this.isInReverse;
         }

         this.setRotation(this.rotationYaw, this.rotationPitch);
         if (this.getType() == EntityMinecart.Type.RIDEABLE && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01) {
            List<Entity> ☃xxxxxxxxxx = this.world
               .getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.2F, 0.0, 0.2F), EntitySelectors.getTeamCollisionPredicate(this));
            if (!☃xxxxxxxxxx.isEmpty()) {
               for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < ☃xxxxxxxxxx.size(); ☃xxxxxxxxxxx++) {
                  Entity ☃xxxxxxxxxxxx = ☃xxxxxxxxxx.get(☃xxxxxxxxxxx);
                  if (!(☃xxxxxxxxxxxx instanceof EntityPlayer)
                     && !(☃xxxxxxxxxxxx instanceof EntityIronGolem)
                     && !(☃xxxxxxxxxxxx instanceof EntityMinecart)
                     && !this.isBeingRidden()
                     && !☃xxxxxxxxxxxx.isRiding()) {
                     ☃xxxxxxxxxxxx.startRiding(this);
                  } else {
                     ☃xxxxxxxxxxxx.applyEntityCollision(this);
                  }
               }
            }
         } else {
            for (Entity ☃xxxxxxxxxx : this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(0.2F, 0.0, 0.2F))) {
               if (!this.isPassenger(☃xxxxxxxxxx) && ☃xxxxxxxxxx.canBePushed() && ☃xxxxxxxxxx instanceof EntityMinecart) {
                  ☃xxxxxxxxxx.applyEntityCollision(this);
               }
            }
         }

         this.handleWaterMovement();
      }
   }

   protected double getMaximumSpeed() {
      return 0.4;
   }

   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
   }

   protected void moveDerailedMinecart() {
      double ☃ = this.getMaximumSpeed();
      this.motionX = MathHelper.clamp(this.motionX, -☃, ☃);
      this.motionZ = MathHelper.clamp(this.motionZ, -☃, ☃);
      if (this.onGround) {
         this.motionX *= 0.5;
         this.motionY *= 0.5;
         this.motionZ *= 0.5;
      }

      this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
      if (!this.onGround) {
         this.motionX *= 0.95F;
         this.motionY *= 0.95F;
         this.motionZ *= 0.95F;
      }
   }

   protected void moveAlongTrack(BlockPos var1, IBlockState var2) {
      this.fallDistance = 0.0F;
      Vec3d ☃ = this.getPos(this.posX, this.posY, this.posZ);
      this.posY = ☃.getY();
      boolean ☃x = false;
      boolean ☃xx = false;
      BlockRailBase ☃xxx = (BlockRailBase)☃.getBlock();
      if (☃xxx == Blocks.GOLDEN_RAIL) {
         ☃x = ☃.getValue(BlockRailPowered.POWERED);
         ☃xx = !☃x;
      }

      double ☃xxxx = 0.0078125;
      BlockRailBase.EnumRailDirection ☃xxxxx = ☃.getValue(☃xxx.getShapeProperty());
      switch (☃xxxxx) {
         case ASCENDING_EAST:
            this.motionX -= 0.0078125;
            this.posY++;
            break;
         case ASCENDING_WEST:
            this.motionX += 0.0078125;
            this.posY++;
            break;
         case ASCENDING_NORTH:
            this.motionZ += 0.0078125;
            this.posY++;
            break;
         case ASCENDING_SOUTH:
            this.motionZ -= 0.0078125;
            this.posY++;
      }

      int[][] ☃xxxx = MATRIX[☃xxxxx.getMetadata()];
      double ☃xxxxx = ☃xxxx[1][0] - ☃xxxx[0][0];
      double ☃xxxxxx = ☃xxxx[1][2] - ☃xxxx[0][2];
      double ☃xxxxxxx = Math.sqrt(☃xxxxx * ☃xxxxx + ☃xxxxxx * ☃xxxxxx);
      double ☃xxxxxxxx = this.motionX * ☃xxxxx + this.motionZ * ☃xxxxxx;
      if (☃xxxxxxxx < 0.0) {
         ☃xxxxx = -☃xxxxx;
         ☃xxxxxx = -☃xxxxxx;
      }

      double ☃xxxxxxxxx = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      if (☃xxxxxxxxx > 2.0) {
         ☃xxxxxxxxx = 2.0;
      }

      this.motionX = ☃xxxxxxxxx * ☃xxxxx / ☃xxxxxxx;
      this.motionZ = ☃xxxxxxxxx * ☃xxxxxx / ☃xxxxxxx;
      Entity ☃xxxxxxxxxx = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
      if (☃xxxxxxxxxx instanceof EntityLivingBase) {
         double ☃xxxxxxxxxxx = ((EntityLivingBase)☃xxxxxxxxxx).moveForward;
         if (☃xxxxxxxxxxx > 0.0) {
            double ☃xxxxxxxxxxxx = -Math.sin(☃xxxxxxxxxx.rotationYaw * (float) (Math.PI / 180.0));
            double ☃xxxxxxxxxxxxx = Math.cos(☃xxxxxxxxxx.rotationYaw * (float) (Math.PI / 180.0));
            double ☃xxxxxxxxxxxxxx = this.motionX * this.motionX + this.motionZ * this.motionZ;
            if (☃xxxxxxxxxxxxxx < 0.01) {
               this.motionX += ☃xxxxxxxxxxxx * 0.1;
               this.motionZ += ☃xxxxxxxxxxxxx * 0.1;
               ☃xx = false;
            }
         }
      }

      if (☃xx) {
         double ☃xxxxxxxxxxx = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (☃xxxxxxxxxxx < 0.03) {
            this.motionX *= 0.0;
            this.motionY *= 0.0;
            this.motionZ *= 0.0;
         } else {
            this.motionX *= 0.5;
            this.motionY *= 0.0;
            this.motionZ *= 0.5;
         }
      }

      double ☃xxxxxxxxxxx = ☃.getX() + 0.5 + ☃xxxx[0][0] * 0.5;
      double ☃xxxxxxxxxxxx = ☃.getZ() + 0.5 + ☃xxxx[0][2] * 0.5;
      double ☃xxxxxxxxxxxxx = ☃.getX() + 0.5 + ☃xxxx[1][0] * 0.5;
      double ☃xxxxxxxxxxxxxx = ☃.getZ() + 0.5 + ☃xxxx[1][2] * 0.5;
      ☃xxxxx = ☃xxxxxxxxxxxxx - ☃xxxxxxxxxxx;
      ☃xxxxxx = ☃xxxxxxxxxxxxxx - ☃xxxxxxxxxxxx;
      double ☃xxxxxxxxxxxxxxx;
      if (☃xxxxx == 0.0) {
         this.posX = ☃.getX() + 0.5;
         ☃xxxxxxxxxxxxxxx = this.posZ - ☃.getZ();
      } else if (☃xxxxxx == 0.0) {
         this.posZ = ☃.getZ() + 0.5;
         ☃xxxxxxxxxxxxxxx = this.posX - ☃.getX();
      } else {
         double ☃xxxxxxxxxxxxxxxx = this.posX - ☃xxxxxxxxxxx;
         double ☃xxxxxxxxxxxxxxxxx = this.posZ - ☃xxxxxxxxxxxx;
         ☃xxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxx * ☃xxxxx + ☃xxxxxxxxxxxxxxxxx * ☃xxxxxx) * 2.0;
      }

      this.posX = ☃xxxxxxxxxxx + ☃xxxxx * ☃xxxxxxxxxxxxxxx;
      this.posZ = ☃xxxxxxxxxxxx + ☃xxxxxx * ☃xxxxxxxxxxxxxxx;
      this.setPosition(this.posX, this.posY, this.posZ);
      double ☃xxxxxxxxxxxxxxxx = this.motionX;
      double ☃xxxxxxxxxxxxxxxxx = this.motionZ;
      if (this.isBeingRidden()) {
         ☃xxxxxxxxxxxxxxxx *= 0.75;
         ☃xxxxxxxxxxxxxxxxx *= 0.75;
      }

      double ☃xxxxxxxxxxxxxxxxxx = this.getMaximumSpeed();
      ☃xxxxxxxxxxxxxxxx = MathHelper.clamp(☃xxxxxxxxxxxxxxxx, -☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx);
      ☃xxxxxxxxxxxxxxxxx = MathHelper.clamp(☃xxxxxxxxxxxxxxxxx, -☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx);
      this.move(MoverType.SELF, ☃xxxxxxxxxxxxxxxx, 0.0, ☃xxxxxxxxxxxxxxxxx);
      if (☃xxxx[0][1] != 0 && MathHelper.floor(this.posX) - ☃.getX() == ☃xxxx[0][0] && MathHelper.floor(this.posZ) - ☃.getZ() == ☃xxxx[0][2]) {
         this.setPosition(this.posX, this.posY + ☃xxxx[0][1], this.posZ);
      } else if (☃xxxx[1][1] != 0 && MathHelper.floor(this.posX) - ☃.getX() == ☃xxxx[1][0] && MathHelper.floor(this.posZ) - ☃.getZ() == ☃xxxx[1][2]) {
         this.setPosition(this.posX, this.posY + ☃xxxx[1][1], this.posZ);
      }

      this.applyDrag();
      Vec3d ☃xxxxxxxxxxxxxxxxxxx = this.getPos(this.posX, this.posY, this.posZ);
      if (☃xxxxxxxxxxxxxxxxxxx != null && ☃ != null) {
         double ☃xxxxxxxxxxxxxxxxxxxx = (☃.y - ☃xxxxxxxxxxxxxxxxxxx.y) * 0.05;
         ☃xxxxxxxxx = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (☃xxxxxxxxx > 0.0) {
            this.motionX = this.motionX / ☃xxxxxxxxx * (☃xxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx);
            this.motionZ = this.motionZ / ☃xxxxxxxxx * (☃xxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxx);
         }

         this.setPosition(this.posX, ☃xxxxxxxxxxxxxxxxxxx.y, this.posZ);
      }

      int ☃xxxxxxxxxxxxxxxxxxxx = MathHelper.floor(this.posX);
      int ☃xxxxxxxxxxxxxxxxxxxxx = MathHelper.floor(this.posZ);
      if (☃xxxxxxxxxxxxxxxxxxxx != ☃.getX() || ☃xxxxxxxxxxxxxxxxxxxxx != ☃.getZ()) {
         ☃xxxxxxxxx = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.motionX = ☃xxxxxxxxx * (☃xxxxxxxxxxxxxxxxxxxx - ☃.getX());
         this.motionZ = ☃xxxxxxxxx * (☃xxxxxxxxxxxxxxxxxxxxx - ☃.getZ());
      }

      if (☃x) {
         double ☃xxxxxxxxxxxxxxxxxxxxxx = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (☃xxxxxxxxxxxxxxxxxxxxxx > 0.01) {
            double ☃xxxxxxxxxxxxxxxxxxxxxxx = 0.06;
            this.motionX = this.motionX + this.motionX / ☃xxxxxxxxxxxxxxxxxxxxxx * 0.06;
            this.motionZ = this.motionZ + this.motionZ / ☃xxxxxxxxxxxxxxxxxxxxxx * 0.06;
         } else if (☃xxxxx == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (this.world.getBlockState(☃.west()).isNormalCube()) {
               this.motionX = 0.02;
            } else if (this.world.getBlockState(☃.east()).isNormalCube()) {
               this.motionX = -0.02;
            }
         } else if (☃xxxxx == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (this.world.getBlockState(☃.north()).isNormalCube()) {
               this.motionZ = 0.02;
            } else if (this.world.getBlockState(☃.south()).isNormalCube()) {
               this.motionZ = -0.02;
            }
         }
      }
   }

   protected void applyDrag() {
      if (this.isBeingRidden()) {
         this.motionX *= 0.997F;
         this.motionY *= 0.0;
         this.motionZ *= 0.997F;
      } else {
         this.motionX *= 0.96F;
         this.motionY *= 0.0;
         this.motionZ *= 0.96F;
      }
   }

   @Override
   public void setPosition(double var1, double var3, double var5) {
      this.posX = ☃;
      this.posY = ☃;
      this.posZ = ☃;
      float ☃ = this.width / 2.0F;
      float ☃x = this.height;
      this.setEntityBoundingBox(new AxisAlignedBB(☃ - ☃, ☃, ☃ - ☃, ☃ + ☃, ☃ + ☃x, ☃ + ☃));
   }

   @Nullable
   public Vec3d getPosOffset(double var1, double var3, double var5, double var7) {
      int ☃ = MathHelper.floor(☃);
      int ☃x = MathHelper.floor(☃);
      int ☃xx = MathHelper.floor(☃);
      if (BlockRailBase.isRailBlock(this.world, new BlockPos(☃, ☃x - 1, ☃xx))) {
         ☃x--;
      }

      IBlockState ☃xxx = this.world.getBlockState(new BlockPos(☃, ☃x, ☃xx));
      if (BlockRailBase.isRailBlock(☃xxx)) {
         BlockRailBase.EnumRailDirection ☃xxxx = ☃xxx.getValue(((BlockRailBase)☃xxx.getBlock()).getShapeProperty());
         ☃ = ☃x;
         if (☃xxxx.isAscending()) {
            ☃ = ☃x + 1;
         }

         int[][] ☃xxxxx = MATRIX[☃xxxx.getMetadata()];
         double ☃xxxxxx = ☃xxxxx[1][0] - ☃xxxxx[0][0];
         double ☃xxxxxxx = ☃xxxxx[1][2] - ☃xxxxx[0][2];
         double ☃xxxxxxxx = Math.sqrt(☃xxxxxx * ☃xxxxxx + ☃xxxxxxx * ☃xxxxxxx);
         ☃xxxxxx /= ☃xxxxxxxx;
         ☃xxxxxxx /= ☃xxxxxxxx;
         ☃ += ☃xxxxxx * ☃;
         ☃ += ☃xxxxxxx * ☃;
         if (☃xxxxx[0][1] != 0 && MathHelper.floor(☃) - ☃ == ☃xxxxx[0][0] && MathHelper.floor(☃) - ☃xx == ☃xxxxx[0][2]) {
            ☃ += ☃xxxxx[0][1];
         } else if (☃xxxxx[1][1] != 0 && MathHelper.floor(☃) - ☃ == ☃xxxxx[1][0] && MathHelper.floor(☃) - ☃xx == ☃xxxxx[1][2]) {
            ☃ += ☃xxxxx[1][1];
         }

         return this.getPos(☃, ☃, ☃);
      } else {
         return null;
      }
   }

   @Nullable
   public Vec3d getPos(double var1, double var3, double var5) {
      int ☃ = MathHelper.floor(☃);
      int ☃x = MathHelper.floor(☃);
      int ☃xx = MathHelper.floor(☃);
      if (BlockRailBase.isRailBlock(this.world, new BlockPos(☃, ☃x - 1, ☃xx))) {
         ☃x--;
      }

      IBlockState ☃xxx = this.world.getBlockState(new BlockPos(☃, ☃x, ☃xx));
      if (BlockRailBase.isRailBlock(☃xxx)) {
         BlockRailBase.EnumRailDirection ☃xxxx = ☃xxx.getValue(((BlockRailBase)☃xxx.getBlock()).getShapeProperty());
         int[][] ☃xxxxx = MATRIX[☃xxxx.getMetadata()];
         double ☃xxxxxx = ☃ + 0.5 + ☃xxxxx[0][0] * 0.5;
         double ☃xxxxxxx = ☃x + 0.0625 + ☃xxxxx[0][1] * 0.5;
         double ☃xxxxxxxx = ☃xx + 0.5 + ☃xxxxx[0][2] * 0.5;
         double ☃xxxxxxxxx = ☃ + 0.5 + ☃xxxxx[1][0] * 0.5;
         double ☃xxxxxxxxxx = ☃x + 0.0625 + ☃xxxxx[1][1] * 0.5;
         double ☃xxxxxxxxxxx = ☃xx + 0.5 + ☃xxxxx[1][2] * 0.5;
         double ☃xxxxxxxxxxxx = ☃xxxxxxxxx - ☃xxxxxx;
         double ☃xxxxxxxxxxxxx = (☃xxxxxxxxxx - ☃xxxxxxx) * 2.0;
         double ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxx - ☃xxxxxxxx;
         double ☃xxxxxxxxxxxxxxx;
         if (☃xxxxxxxxxxxx == 0.0) {
            ☃xxxxxxxxxxxxxxx = ☃ - ☃xx;
         } else if (☃xxxxxxxxxxxxxx == 0.0) {
            ☃xxxxxxxxxxxxxxx = ☃ - ☃;
         } else {
            double ☃xxxxxxxxxxxxxxxx = ☃ - ☃xxxxxx;
            double ☃xxxxxxxxxxxxxxxxx = ☃ - ☃xxxxxxxx;
            ☃xxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx) * 2.0;
         }

         ☃ = ☃xxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx;
         ☃ = ☃xxxxxxx + ☃xxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx;
         ☃ = ☃xxxxxxxx + ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx;
         if (☃xxxxxxxxxxxxx < 0.0) {
            ☃++;
         }

         if (☃xxxxxxxxxxxxx > 0.0) {
            ☃ += 0.5;
         }

         return new Vec3d(☃, ☃, ☃);
      } else {
         return null;
      }
   }

   @Override
   public AxisAlignedBB getRenderBoundingBox() {
      AxisAlignedBB ☃ = this.getEntityBoundingBox();
      return this.hasDisplayTile() ? ☃.grow(Math.abs(this.getDisplayTileOffset()) / 16.0) : ☃;
   }

   public static void registerFixesMinecart(DataFixer var0, Class<?> var1) {
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      if (☃.getBoolean("CustomDisplayTile")) {
         Block ☃;
         if (☃.hasKey("DisplayTile", 8)) {
            ☃ = Block.getBlockFromName(☃.getString("DisplayTile"));
         } else {
            ☃ = Block.getBlockById(☃.getInteger("DisplayTile"));
         }

         int ☃x = ☃.getInteger("DisplayData");
         this.setDisplayTile(☃ == null ? Blocks.AIR.getDefaultState() : ☃.getStateFromMeta(☃x));
         this.setDisplayTileOffset(☃.getInteger("DisplayOffset"));
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      if (this.hasDisplayTile()) {
         ☃.setBoolean("CustomDisplayTile", true);
         IBlockState ☃ = this.getDisplayTile();
         ResourceLocation ☃x = Block.REGISTRY.getNameForObject(☃.getBlock());
         ☃.setString("DisplayTile", ☃x == null ? "" : ☃x.toString());
         ☃.setInteger("DisplayData", ☃.getBlock().getMetaFromState(☃));
         ☃.setInteger("DisplayOffset", this.getDisplayTileOffset());
      }
   }

   @Override
   public void applyEntityCollision(Entity var1) {
      if (!this.world.isRemote) {
         if (!☃.noClip && !this.noClip) {
            if (!this.isPassenger(☃)) {
               double ☃ = ☃.posX - this.posX;
               double ☃x = ☃.posZ - this.posZ;
               double ☃xx = ☃ * ☃ + ☃x * ☃x;
               if (☃xx >= 1.0E-4F) {
                  ☃xx = MathHelper.sqrt(☃xx);
                  ☃ /= ☃xx;
                  ☃x /= ☃xx;
                  double ☃xxx = 1.0 / ☃xx;
                  if (☃xxx > 1.0) {
                     ☃xxx = 1.0;
                  }

                  ☃ *= ☃xxx;
                  ☃x *= ☃xxx;
                  ☃ *= 0.1F;
                  ☃x *= 0.1F;
                  ☃ *= 1.0F - this.entityCollisionReduction;
                  ☃x *= 1.0F - this.entityCollisionReduction;
                  ☃ *= 0.5;
                  ☃x *= 0.5;
                  if (☃ instanceof EntityMinecart) {
                     double ☃xxxx = ☃.posX - this.posX;
                     double ☃xxxxx = ☃.posZ - this.posZ;
                     Vec3d ☃xxxxxx = new Vec3d(☃xxxx, 0.0, ☃xxxxx).normalize();
                     Vec3d ☃xxxxxxx = new Vec3d(
                           MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0)), 0.0, MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0))
                        )
                        .normalize();
                     double ☃xxxxxxxx = Math.abs(☃xxxxxx.dotProduct(☃xxxxxxx));
                     if (☃xxxxxxxx < 0.8F) {
                        return;
                     }

                     double ☃xxxxxxxxx = ☃.motionX + this.motionX;
                     double ☃xxxxxxxxxx = ☃.motionZ + this.motionZ;
                     if (((EntityMinecart)☃).getType() == EntityMinecart.Type.FURNACE && this.getType() != EntityMinecart.Type.FURNACE) {
                        this.motionX *= 0.2F;
                        this.motionZ *= 0.2F;
                        this.addVelocity(☃.motionX - ☃, 0.0, ☃.motionZ - ☃x);
                        ☃.motionX *= 0.95F;
                        ☃.motionZ *= 0.95F;
                     } else if (((EntityMinecart)☃).getType() != EntityMinecart.Type.FURNACE && this.getType() == EntityMinecart.Type.FURNACE) {
                        ☃.motionX *= 0.2F;
                        ☃.motionZ *= 0.2F;
                        ☃.addVelocity(this.motionX + ☃, 0.0, this.motionZ + ☃x);
                        this.motionX *= 0.95F;
                        this.motionZ *= 0.95F;
                     } else {
                        ☃xxxxxxxxx /= 2.0;
                        ☃xxxxxxxxxx /= 2.0;
                        this.motionX *= 0.2F;
                        this.motionZ *= 0.2F;
                        this.addVelocity(☃xxxxxxxxx - ☃, 0.0, ☃xxxxxxxxxx - ☃x);
                        ☃.motionX *= 0.2F;
                        ☃.motionZ *= 0.2F;
                        ☃.addVelocity(☃xxxxxxxxx + ☃, 0.0, ☃xxxxxxxxxx + ☃x);
                     }
                  } else {
                     this.addVelocity(-☃, 0.0, -☃x);
                     ☃.addVelocity(☃ / 4.0, 0.0, ☃x / 4.0);
                  }
               }
            }
         }
      }
   }

   @Override
   public void setPositionAndRotationDirect(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.minecartX = ☃;
      this.minecartY = ☃;
      this.minecartZ = ☃;
      this.minecartYaw = ☃;
      this.minecartPitch = ☃;
      this.turnProgress = ☃ + 2;
      this.motionX = this.velocityX;
      this.motionY = this.velocityY;
      this.motionZ = this.velocityZ;
   }

   @Override
   public void setVelocity(double var1, double var3, double var5) {
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
      this.velocityX = this.motionX;
      this.velocityY = this.motionY;
      this.velocityZ = this.motionZ;
   }

   public void setDamage(float var1) {
      this.dataManager.set(DAMAGE, ☃);
   }

   public float getDamage() {
      return this.dataManager.get(DAMAGE);
   }

   public void setRollingAmplitude(int var1) {
      this.dataManager.set(ROLLING_AMPLITUDE, ☃);
   }

   public int getRollingAmplitude() {
      return this.dataManager.get(ROLLING_AMPLITUDE);
   }

   public void setRollingDirection(int var1) {
      this.dataManager.set(ROLLING_DIRECTION, ☃);
   }

   public int getRollingDirection() {
      return this.dataManager.get(ROLLING_DIRECTION);
   }

   public abstract EntityMinecart.Type getType();

   public IBlockState getDisplayTile() {
      return !this.hasDisplayTile() ? this.getDefaultDisplayTile() : Block.getStateById(this.getDataManager().get(DISPLAY_TILE));
   }

   public IBlockState getDefaultDisplayTile() {
      return Blocks.AIR.getDefaultState();
   }

   public int getDisplayTileOffset() {
      return !this.hasDisplayTile() ? this.getDefaultDisplayTileOffset() : this.getDataManager().get(DISPLAY_TILE_OFFSET);
   }

   public int getDefaultDisplayTileOffset() {
      return 6;
   }

   public void setDisplayTile(IBlockState var1) {
      this.getDataManager().set(DISPLAY_TILE, Block.getStateId(☃));
      this.setHasDisplayTile(true);
   }

   public void setDisplayTileOffset(int var1) {
      this.getDataManager().set(DISPLAY_TILE_OFFSET, ☃);
      this.setHasDisplayTile(true);
   }

   public boolean hasDisplayTile() {
      return this.getDataManager().get(SHOW_BLOCK);
   }

   public void setHasDisplayTile(boolean var1) {
      this.getDataManager().set(SHOW_BLOCK, ☃);
   }

   public static enum Type {
      RIDEABLE(0, "MinecartRideable"),
      CHEST(1, "MinecartChest"),
      FURNACE(2, "MinecartFurnace"),
      TNT(3, "MinecartTNT"),
      SPAWNER(4, "MinecartSpawner"),
      HOPPER(5, "MinecartHopper"),
      COMMAND_BLOCK(6, "MinecartCommandBlock");

      private static final Map<Integer, EntityMinecart.Type> BY_ID = Maps.newHashMap();
      private final int id;
      private final String name;

      private Type(int var3, String var4) {
         this.id = ☃;
         this.name = ☃;
      }

      public int getId() {
         return this.id;
      }

      public String getName() {
         return this.name;
      }

      public static EntityMinecart.Type getById(int var0) {
         EntityMinecart.Type ☃ = BY_ID.get(☃);
         return ☃ == null ? RIDEABLE : ☃;
      }

      static {
         for (EntityMinecart.Type ☃ : values()) {
            BY_ID.put(☃.getId(), ☃);
         }
      }
   }
}
