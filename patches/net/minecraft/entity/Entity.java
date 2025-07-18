package net.minecraft.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Entity implements ICommandSender {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final List<ItemStack> EMPTY_EQUIPMENT = Collections.emptyList();
   private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
   private static double renderDistanceWeight = 1.0;
   private static int nextEntityID;
   private int entityId;
   public boolean preventEntitySpawning;
   private final List<Entity> riddenByEntities;
   protected int rideCooldown;
   private Entity ridingEntity;
   public boolean forceSpawn;
   public World world;
   public double prevPosX;
   public double prevPosY;
   public double prevPosZ;
   public double posX;
   public double posY;
   public double posZ;
   public double motionX;
   public double motionY;
   public double motionZ;
   public float rotationYaw;
   public float rotationPitch;
   public float prevRotationYaw;
   public float prevRotationPitch;
   private AxisAlignedBB boundingBox;
   public boolean onGround;
   public boolean collidedHorizontally;
   public boolean collidedVertically;
   public boolean collided;
   public boolean velocityChanged;
   protected boolean isInWeb;
   private boolean isOutsideBorder;
   public boolean isDead;
   public float width;
   public float height;
   public float prevDistanceWalkedModified;
   public float distanceWalkedModified;
   public float distanceWalkedOnStepModified;
   public float fallDistance;
   private int nextStepDistance;
   private float nextFlap;
   public double lastTickPosX;
   public double lastTickPosY;
   public double lastTickPosZ;
   public float stepHeight;
   public boolean noClip;
   public float entityCollisionReduction;
   protected Random rand;
   public int ticksExisted;
   private int fire;
   protected boolean inWater;
   public int hurtResistantTime;
   protected boolean firstUpdate;
   protected boolean isImmuneToFire;
   protected EntityDataManager dataManager;
   protected static final DataParameter<Byte> FLAGS = EntityDataManager.createKey(Entity.class, DataSerializers.BYTE);
   private static final DataParameter<Integer> AIR = EntityDataManager.createKey(Entity.class, DataSerializers.VARINT);
   private static final DataParameter<String> CUSTOM_NAME = EntityDataManager.createKey(Entity.class, DataSerializers.STRING);
   private static final DataParameter<Boolean> CUSTOM_NAME_VISIBLE = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Boolean> SILENT = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Boolean> NO_GRAVITY = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
   public boolean addedToChunk;
   public int chunkCoordX;
   public int chunkCoordY;
   public int chunkCoordZ;
   public long serverPosX;
   public long serverPosY;
   public long serverPosZ;
   public boolean ignoreFrustumCheck;
   public boolean isAirBorne;
   public int timeUntilPortal;
   protected boolean inPortal;
   protected int portalCounter;
   public int dimension;
   protected BlockPos lastPortalPos;
   protected Vec3d lastPortalVec;
   protected EnumFacing teleportDirection;
   private boolean invulnerable;
   protected UUID entityUniqueID;
   protected String cachedUniqueIdString;
   private final CommandResultStats cmdResultStats;
   protected boolean glowing;
   private final Set<String> tags;
   private boolean isPositionDirty;
   private final double[] pistonDeltas;
   private long pistonDeltasGameTime;

   public Entity(World var1) {
      this.entityId = nextEntityID++;
      this.riddenByEntities = Lists.newArrayList();
      this.boundingBox = ZERO_AABB;
      this.width = 0.6F;
      this.height = 1.8F;
      this.nextStepDistance = 1;
      this.nextFlap = 1.0F;
      this.rand = new Random();
      this.fire = -this.getFireImmuneTicks();
      this.firstUpdate = true;
      this.entityUniqueID = MathHelper.getRandomUUID(this.rand);
      this.cachedUniqueIdString = this.entityUniqueID.toString();
      this.cmdResultStats = new CommandResultStats();
      this.tags = Sets.newHashSet();
      this.pistonDeltas = new double[]{0.0, 0.0, 0.0};
      this.world = ☃;
      this.setPosition(0.0, 0.0, 0.0);
      if (☃ != null) {
         this.dimension = ☃.provider.getDimensionType().getId();
      }

      this.dataManager = new EntityDataManager(this);
      this.dataManager.register(FLAGS, (byte)0);
      this.dataManager.register(AIR, 300);
      this.dataManager.register(CUSTOM_NAME_VISIBLE, false);
      this.dataManager.register(CUSTOM_NAME, "");
      this.dataManager.register(SILENT, false);
      this.dataManager.register(NO_GRAVITY, false);
      this.entityInit();
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int var1) {
      this.entityId = ☃;
   }

   public Set<String> getTags() {
      return this.tags;
   }

   public boolean addTag(String var1) {
      if (this.tags.size() >= 1024) {
         return false;
      } else {
         this.tags.add(☃);
         return true;
      }
   }

   public boolean removeTag(String var1) {
      return this.tags.remove(☃);
   }

   public void onKillCommand() {
      this.setDead();
   }

   protected abstract void entityInit();

   public EntityDataManager getDataManager() {
      return this.dataManager;
   }

   @Override
   public boolean equals(Object var1) {
      return ☃ instanceof Entity ? ((Entity)☃).entityId == this.entityId : false;
   }

   @Override
   public int hashCode() {
      return this.entityId;
   }

   protected void preparePlayerToSpawn() {
      if (this.world != null) {
         while (this.posY > 0.0 && this.posY < 256.0) {
            this.setPosition(this.posX, this.posY, this.posZ);
            if (this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
               break;
            }

            this.posY++;
         }

         this.motionX = 0.0;
         this.motionY = 0.0;
         this.motionZ = 0.0;
         this.rotationPitch = 0.0F;
      }
   }

   public void setDead() {
      this.isDead = true;
   }

   public void setDropItemsWhenDead(boolean var1) {
   }

   protected void setSize(float var1, float var2) {
      if (☃ != this.width || ☃ != this.height) {
         float ☃ = this.width;
         this.width = ☃;
         this.height = ☃;
         if (this.width < ☃) {
            double ☃x = ☃ / 2.0;
            this.setEntityBoundingBox(new AxisAlignedBB(this.posX - ☃x, this.posY, this.posZ - ☃x, this.posX + ☃x, this.posY + this.height, this.posZ + ☃x));
            return;
         }

         AxisAlignedBB ☃x = this.getEntityBoundingBox();
         this.setEntityBoundingBox(new AxisAlignedBB(☃x.minX, ☃x.minY, ☃x.minZ, ☃x.minX + this.width, ☃x.minY + this.height, ☃x.minZ + this.width));
         if (this.width > ☃ && !this.firstUpdate && !this.world.isRemote) {
            this.move(MoverType.SELF, ☃ - this.width, 0.0, ☃ - this.width);
         }
      }
   }

   protected void setRotation(float var1, float var2) {
      this.rotationYaw = ☃ % 360.0F;
      this.rotationPitch = ☃ % 360.0F;
   }

   public void setPosition(double var1, double var3, double var5) {
      this.posX = ☃;
      this.posY = ☃;
      this.posZ = ☃;
      float ☃ = this.width / 2.0F;
      float ☃x = this.height;
      this.setEntityBoundingBox(new AxisAlignedBB(☃ - ☃, ☃, ☃ - ☃, ☃ + ☃, ☃ + ☃x, ☃ + ☃));
   }

   public void turn(float var1, float var2) {
      float ☃ = this.rotationPitch;
      float ☃x = this.rotationYaw;
      this.rotationYaw = (float)(this.rotationYaw + ☃ * 0.15);
      this.rotationPitch = (float)(this.rotationPitch - ☃ * 0.15);
      this.rotationPitch = MathHelper.clamp(this.rotationPitch, -90.0F, 90.0F);
      this.prevRotationPitch = this.prevRotationPitch + (this.rotationPitch - ☃);
      this.prevRotationYaw = this.prevRotationYaw + (this.rotationYaw - ☃x);
      if (this.ridingEntity != null) {
         this.ridingEntity.applyOrientationToEntity(this);
      }
   }

   public void onUpdate() {
      if (!this.world.isRemote) {
         this.setFlag(6, this.isGlowing());
      }

      this.onEntityUpdate();
   }

   public void onEntityUpdate() {
      this.world.profiler.startSection("entityBaseTick");
      if (this.isRiding() && this.getRidingEntity().isDead) {
         this.dismountRidingEntity();
      }

      if (this.rideCooldown > 0) {
         this.rideCooldown--;
      }

      this.prevDistanceWalkedModified = this.distanceWalkedModified;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.prevRotationPitch = this.rotationPitch;
      this.prevRotationYaw = this.rotationYaw;
      if (!this.world.isRemote && this.world instanceof WorldServer) {
         this.world.profiler.startSection("portal");
         if (this.inPortal) {
            MinecraftServer ☃ = this.world.getMinecraftServer();
            if (☃.getAllowNether()) {
               if (!this.isRiding()) {
                  int ☃x = this.getMaxInPortalTime();
                  if (this.portalCounter++ >= ☃x) {
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

         this.decrementTimeUntilPortal();
         this.world.profiler.endSection();
      }

      this.spawnRunningParticles();
      this.handleWaterMovement();
      if (this.world.isRemote) {
         this.extinguish();
      } else if (this.fire > 0) {
         if (this.isImmuneToFire) {
            this.fire -= 4;
            if (this.fire < 0) {
               this.extinguish();
            }
         } else {
            if (this.fire % 20 == 0) {
               this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
            }

            this.fire--;
         }
      }

      if (this.isInLava()) {
         this.setOnFireFromLava();
         this.fallDistance *= 0.5F;
      }

      if (this.posY < -64.0) {
         this.outOfWorld();
      }

      if (!this.world.isRemote) {
         this.setFlag(0, this.fire > 0);
      }

      this.firstUpdate = false;
      this.world.profiler.endSection();
   }

   protected void decrementTimeUntilPortal() {
      if (this.timeUntilPortal > 0) {
         this.timeUntilPortal--;
      }
   }

   public int getMaxInPortalTime() {
      return 1;
   }

   protected void setOnFireFromLava() {
      if (!this.isImmuneToFire) {
         this.attackEntityFrom(DamageSource.LAVA, 4.0F);
         this.setFire(15);
      }
   }

   public void setFire(int var1) {
      int ☃ = ☃ * 20;
      if (this instanceof EntityLivingBase) {
         ☃ = EnchantmentProtection.getFireTimeForEntity((EntityLivingBase)this, ☃);
      }

      if (this.fire < ☃) {
         this.fire = ☃;
      }
   }

   public void extinguish() {
      this.fire = 0;
   }

   protected void outOfWorld() {
      this.setDead();
   }

   public boolean isOffsetPositionInLiquid(double var1, double var3, double var5) {
      AxisAlignedBB ☃ = this.getEntityBoundingBox().offset(☃, ☃, ☃);
      return this.isLiquidPresentInAABB(☃);
   }

   private boolean isLiquidPresentInAABB(AxisAlignedBB var1) {
      return this.world.getCollisionBoxes(this, ☃).isEmpty() && !this.world.containsAnyLiquid(☃);
   }

   public void move(MoverType var1, double var2, double var4, double var6) {
      if (this.noClip) {
         this.setEntityBoundingBox(this.getEntityBoundingBox().offset(☃, ☃, ☃));
         this.resetPositionToBB();
      } else {
         if (☃ == MoverType.PISTON) {
            long ☃ = this.world.getTotalWorldTime();
            if (☃ != this.pistonDeltasGameTime) {
               Arrays.fill(this.pistonDeltas, 0.0);
               this.pistonDeltasGameTime = ☃;
            }

            if (☃ != 0.0) {
               int ☃x = EnumFacing.Axis.X.ordinal();
               double ☃xx = MathHelper.clamp(☃ + this.pistonDeltas[☃x], -0.51, 0.51);
               ☃ = ☃xx - this.pistonDeltas[☃x];
               this.pistonDeltas[☃x] = ☃xx;
               if (Math.abs(☃) <= 1.0E-5F) {
                  return;
               }
            } else if (☃ != 0.0) {
               int ☃x = EnumFacing.Axis.Y.ordinal();
               double ☃xx = MathHelper.clamp(☃ + this.pistonDeltas[☃x], -0.51, 0.51);
               ☃ = ☃xx - this.pistonDeltas[☃x];
               this.pistonDeltas[☃x] = ☃xx;
               if (Math.abs(☃) <= 1.0E-5F) {
                  return;
               }
            } else {
               if (☃ == 0.0) {
                  return;
               }

               int ☃x = EnumFacing.Axis.Z.ordinal();
               double ☃xx = MathHelper.clamp(☃ + this.pistonDeltas[☃x], -0.51, 0.51);
               ☃ = ☃xx - this.pistonDeltas[☃x];
               this.pistonDeltas[☃x] = ☃xx;
               if (Math.abs(☃) <= 1.0E-5F) {
                  return;
               }
            }
         }

         this.world.profiler.startSection("move");
         double ☃x = this.posX;
         double ☃xx = this.posY;
         double ☃xxx = this.posZ;
         if (this.isInWeb) {
            this.isInWeb = false;
            ☃ *= 0.25;
            ☃ *= 0.05F;
            ☃ *= 0.25;
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
         }

         double ☃xxxx = ☃;
         double ☃xxxxx = ☃;
         double ☃xxxxxx = ☃;
         if ((☃ == MoverType.SELF || ☃ == MoverType.PLAYER) && this.onGround && this.isSneaking() && this instanceof EntityPlayer) {
            for (double ☃xxxxxxx = 0.05;
               ☃ != 0.0 && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(☃, -this.stepHeight, 0.0)).isEmpty();
               ☃xxxx = ☃
            ) {
               if (☃ < 0.05 && ☃ >= -0.05) {
                  ☃ = 0.0;
               } else if (☃ > 0.0) {
                  ☃ -= 0.05;
               } else {
                  ☃ += 0.05;
               }
            }

            for (; ☃ != 0.0 && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(0.0, -this.stepHeight, ☃)).isEmpty(); ☃xxxxxx = ☃) {
               if (☃ < 0.05 && ☃ >= -0.05) {
                  ☃ = 0.0;
               } else if (☃ > 0.0) {
                  ☃ -= 0.05;
               } else {
                  ☃ += 0.05;
               }
            }

            for (;
               ☃ != 0.0 && ☃ != 0.0 && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(☃, -this.stepHeight, ☃)).isEmpty();
               ☃xxxxxx = ☃
            ) {
               if (☃ < 0.05 && ☃ >= -0.05) {
                  ☃ = 0.0;
               } else if (☃ > 0.0) {
                  ☃ -= 0.05;
               } else {
                  ☃ += 0.05;
               }

               ☃xxxx = ☃;
               if (☃ < 0.05 && ☃ >= -0.05) {
                  ☃ = 0.0;
               } else if (☃ > 0.0) {
                  ☃ -= 0.05;
               } else {
                  ☃ += 0.05;
               }
            }
         }

         List<AxisAlignedBB> ☃xxxxxxxx = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().expand(☃, ☃, ☃));
         AxisAlignedBB ☃xxxxxxxxx = this.getEntityBoundingBox();
         if (☃ != 0.0) {
            int ☃xxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxx = ☃xxxxxxxx.size(); ☃xxxxxxxxxx < ☃xxxxxxxxxxx; ☃xxxxxxxxxx++) {
               ☃ = ☃xxxxxxxx.get(☃xxxxxxxxxx).calculateYOffset(this.getEntityBoundingBox(), ☃);
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, ☃, 0.0));
         }

         if (☃ != 0.0) {
            int ☃xxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxx = ☃xxxxxxxx.size(); ☃xxxxxxxxxx < ☃xxxxxxxxxxx; ☃xxxxxxxxxx++) {
               ☃ = ☃xxxxxxxx.get(☃xxxxxxxxxx).calculateXOffset(this.getEntityBoundingBox(), ☃);
            }

            if (☃ != 0.0) {
               this.setEntityBoundingBox(this.getEntityBoundingBox().offset(☃, 0.0, 0.0));
            }
         }

         if (☃ != 0.0) {
            int ☃xxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxx = ☃xxxxxxxx.size(); ☃xxxxxxxxxx < ☃xxxxxxxxxxx; ☃xxxxxxxxxx++) {
               ☃ = ☃xxxxxxxx.get(☃xxxxxxxxxx).calculateZOffset(this.getEntityBoundingBox(), ☃);
            }

            if (☃ != 0.0) {
               this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, ☃));
            }
         }

         boolean ☃xxxxxxxxxx = this.onGround || ☃xxxxx != ☃ && ☃xxxxx < 0.0;
         if (this.stepHeight > 0.0F && ☃xxxxxxxxxx && (☃xxxx != ☃ || ☃xxxxxx != ☃)) {
            double ☃xxxxxxxxxxx = ☃;
            double ☃xxxxxxxxxxxx = ☃;
            double ☃xxxxxxxxxxxxx = ☃;
            AxisAlignedBB ☃xxxxxxxxxxxxxx = this.getEntityBoundingBox();
            this.setEntityBoundingBox(☃xxxxxxxxx);
            ☃ = this.stepHeight;
            List<AxisAlignedBB> ☃xxxxxxxxxxxxxxx = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().expand(☃xxxx, ☃, ☃xxxxxx));
            AxisAlignedBB ☃xxxxxxxxxxxxxxxx = this.getEntityBoundingBox();
            AxisAlignedBB ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.expand(☃xxxx, 0.0, ☃xxxxxx);
            double ☃xxxxxxxxxxxxxxxxxx = ☃;
            int ☃xxxxxxxxxxxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.size(); ☃xxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxx++) {
               ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxx).calculateYOffset(☃xxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxx);
            }

            ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.offset(0.0, ☃xxxxxxxxxxxxxxxxxx, 0.0);
            double ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxx;
            int ☃xxxxxxxxxxxxxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.size(); ☃xxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxx++) {
               ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxx).calculateXOffset(☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx);
            }

            ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.offset(☃xxxxxxxxxxxxxxxxxxxx, 0.0, 0.0);
            double ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxx;
            int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.size(); ☃xxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxx; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
               ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxxx).calculateZOffset(☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx);
            }

            ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.offset(0.0, 0.0, ☃xxxxxxxxxxxxxxxxxxxxxx);
            AxisAlignedBB ☃xxxxxxxxxxxxxxxxxxxxxxxx = this.getEntityBoundingBox();
            double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃;
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.size();
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxx++
            ) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxxxxxx)
                  .calculateYOffset(☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxx);
            }

            AxisAlignedBB var88 = ☃xxxxxxxxxxxxxxxxxxxxxxxx.offset(0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxx, 0.0);
            double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxx;
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.size();
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx++
            ) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx).calculateXOffset(var88, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx);
            }

            AxisAlignedBB var89 = var88.offset(☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, 0.0, 0.0);
            double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxx;
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.size();
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++
            ) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx).calculateZOffset(var89, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
            }

            AxisAlignedBB var90 = var89.offset(0.0, 0.0, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
            double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxx;
            double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx
               + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
            if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx > ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) {
               ☃ = ☃xxxxxxxxxxxxxxxxxxxx;
               ☃ = ☃xxxxxxxxxxxxxxxxxxxxxx;
               ☃ = -☃xxxxxxxxxxxxxxxxxx;
               this.setEntityBoundingBox(☃xxxxxxxxxxxxxxxx);
            } else {
               ☃ = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃ = ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃ = -☃xxxxxxxxxxxxxxxxxxxxxxxxx;
               this.setEntityBoundingBox(var90);
            }

            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0;

            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.size();
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++
            ) {
               ☃ = ☃xxxxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx).calculateYOffset(this.getEntityBoundingBox(), ☃);
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, ☃, 0.0));
            if (☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxxx * ☃xxxxxxxxxxxxx >= ☃ * ☃ + ☃ * ☃) {
               ☃ = ☃xxxxxxxxxxx;
               ☃ = ☃xxxxxxxxxxxx;
               ☃ = ☃xxxxxxxxxxxxx;
               this.setEntityBoundingBox(☃xxxxxxxxxxxxxx);
            }
         }

         this.world.profiler.endSection();
         this.world.profiler.startSection("rest");
         this.resetPositionToBB();
         this.collidedHorizontally = ☃xxxx != ☃ || ☃xxxxxx != ☃;
         this.collidedVertically = ☃xxxxx != ☃;
         this.onGround = this.collidedVertically && ☃xxxxx < 0.0;
         this.collided = this.collidedHorizontally || this.collidedVertically;
         int ☃xxxxxxxxxxx = MathHelper.floor(this.posX);
         int ☃xxxxxxxxxxxx = MathHelper.floor(this.posY - 0.2F);
         int ☃xxxxxxxxxxxxx = MathHelper.floor(this.posZ);
         BlockPos ☃xxxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx);
         IBlockState ☃xxxxxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxxxxxx);
         if (☃xxxxxxxxxxxxxxx.getMaterial() == Material.AIR) {
            BlockPos ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.down();
            IBlockState ☃xxxxxxxxxxxxxxxxx = this.world.getBlockState(☃xxxxxxxxxxxxxxxx);
            Block ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx.getBlock();
            if (☃xxxxxxxxxxxxxxxxxx instanceof BlockFence || ☃xxxxxxxxxxxxxxxxxx instanceof BlockWall || ☃xxxxxxxxxxxxxxxxxx instanceof BlockFenceGate) {
               ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx;
            }
         }

         this.updateFallState(☃, this.onGround, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx);
         if (☃xxxx != ☃) {
            this.motionX = 0.0;
         }

         if (☃xxxxxx != ☃) {
            this.motionZ = 0.0;
         }

         Block ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.getBlock();
         if (☃xxxxx != ☃) {
            ☃xxxxxxxxxxxxxxxx.onLanded(this.world, this);
         }

         if (this.canTriggerWalking() && (!this.onGround || !this.isSneaking() || !(this instanceof EntityPlayer)) && !this.isRiding()) {
            double ☃xxxxxxxxxxxxxxxxx = this.posX - ☃x;
            double ☃xxxxxxxxxxxxxxxxxx = this.posY - ☃xx;
            double ☃xxxxxxxxxxxxxxxxxxx = this.posZ - ☃xxx;
            if (☃xxxxxxxxxxxxxxxx != Blocks.LADDER) {
               ☃xxxxxxxxxxxxxxxxxx = 0.0;
            }

            if (☃xxxxxxxxxxxxxxxx != null && this.onGround) {
               ☃xxxxxxxxxxxxxxxx.onEntityWalk(this.world, ☃xxxxxxxxxxxxxx, this);
            }

            this.distanceWalkedModified = (float)(
               this.distanceWalkedModified + MathHelper.sqrt(☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx) * 0.6
            );
            this.distanceWalkedOnStepModified = (float)(
               this.distanceWalkedOnStepModified
                  + MathHelper.sqrt(
                        ☃xxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx
                     )
                     * 0.6
            );
            if (this.distanceWalkedOnStepModified > this.nextStepDistance && ☃xxxxxxxxxxxxxxx.getMaterial() != Material.AIR) {
               this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
               if (this.isInWater()) {
                  Entity ☃xxxxxxxxxxxxxxxxxxxx = this.isBeingRidden() && this.getControllingPassenger() != null ? this.getControllingPassenger() : this;
                  float ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx == this ? 0.35F : 0.4F;
                  float ☃xxxxxxxxxxxxxxxxxxxxxx = MathHelper.sqrt(
                        ☃xxxxxxxxxxxxxxxxxxxx.motionX * ☃xxxxxxxxxxxxxxxxxxxx.motionX * 0.2F
                           + ☃xxxxxxxxxxxxxxxxxxxx.motionY * ☃xxxxxxxxxxxxxxxxxxxx.motionY
                           + ☃xxxxxxxxxxxxxxxxxxxx.motionZ * ☃xxxxxxxxxxxxxxxxxxxx.motionZ * 0.2F
                     )
                     * ☃xxxxxxxxxxxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxxxxxxxxxxx > 1.0F) {
                     ☃xxxxxxxxxxxxxxxxxxxxxx = 1.0F;
                  }

                  this.playSound(this.getSwimSound(), ☃xxxxxxxxxxxxxxxxxxxxxx, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
               } else {
                  this.playStepSound(☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx);
               }
            } else if (this.distanceWalkedOnStepModified > this.nextFlap && this.makeFlySound() && ☃xxxxxxxxxxxxxxx.getMaterial() == Material.AIR) {
               this.nextFlap = this.playFlySound(this.distanceWalkedOnStepModified);
            }
         }

         try {
            this.doBlockCollisions();
         } catch (Throwable var52) {
            CrashReport ☃xxxxxxxxxxxxxxxxxxxx = CrashReport.makeCrashReport(var52, "Checking entity block collision");
            CrashReportCategory ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxx.makeCategory("Entity being checked for collision");
            this.addEntityCrashInfo(☃xxxxxxxxxxxxxxxxxxxxx);
            throw new ReportedException(☃xxxxxxxxxxxxxxxxxxxx);
         }

         boolean ☃xxxxxxxxxxxxxxxxxxxx = this.isWet();
         if (this.world.isFlammableWithin(this.getEntityBoundingBox().shrink(0.001))) {
            this.dealFireDamage(1);
            if (!☃xxxxxxxxxxxxxxxxxxxx) {
               this.fire++;
               if (this.fire == 0) {
                  this.setFire(8);
               }
            }
         } else if (this.fire <= 0) {
            this.fire = -this.getFireImmuneTicks();
         }

         if (☃xxxxxxxxxxxxxxxxxxxx && this.isBurning()) {
            this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            this.fire = -this.getFireImmuneTicks();
         }

         this.world.profiler.endSection();
      }
   }

   public void resetPositionToBB() {
      AxisAlignedBB ☃ = this.getEntityBoundingBox();
      this.posX = (☃.minX + ☃.maxX) / 2.0;
      this.posY = ☃.minY;
      this.posZ = (☃.minZ + ☃.maxZ) / 2.0;
   }

   protected SoundEvent getSwimSound() {
      return SoundEvents.ENTITY_GENERIC_SWIM;
   }

   protected SoundEvent getSplashSound() {
      return SoundEvents.ENTITY_GENERIC_SPLASH;
   }

   protected void doBlockCollisions() {
      AxisAlignedBB ☃ = this.getEntityBoundingBox();
      BlockPos.PooledMutableBlockPos ☃x = BlockPos.PooledMutableBlockPos.retain(☃.minX + 0.001, ☃.minY + 0.001, ☃.minZ + 0.001);
      BlockPos.PooledMutableBlockPos ☃xx = BlockPos.PooledMutableBlockPos.retain(☃.maxX - 0.001, ☃.maxY - 0.001, ☃.maxZ - 0.001);
      BlockPos.PooledMutableBlockPos ☃xxx = BlockPos.PooledMutableBlockPos.retain();
      if (this.world.isAreaLoaded(☃x, ☃xx)) {
         for (int ☃xxxx = ☃x.getX(); ☃xxxx <= ☃xx.getX(); ☃xxxx++) {
            for (int ☃xxxxx = ☃x.getY(); ☃xxxxx <= ☃xx.getY(); ☃xxxxx++) {
               for (int ☃xxxxxx = ☃x.getZ(); ☃xxxxxx <= ☃xx.getZ(); ☃xxxxxx++) {
                  ☃xxx.setPos(☃xxxx, ☃xxxxx, ☃xxxxxx);
                  IBlockState ☃xxxxxxx = this.world.getBlockState(☃xxx);

                  try {
                     ☃xxxxxxx.getBlock().onEntityCollision(this.world, ☃xxx, ☃xxxxxxx, this);
                     this.onInsideBlock(☃xxxxxxx);
                  } catch (Throwable var12) {
                     CrashReport ☃xxxxxxxx = CrashReport.makeCrashReport(var12, "Colliding entity with block");
                     CrashReportCategory ☃xxxxxxxxx = ☃xxxxxxxx.makeCategory("Block being collided with");
                     CrashReportCategory.addBlockInfo(☃xxxxxxxxx, ☃xxx, ☃xxxxxxx);
                     throw new ReportedException(☃xxxxxxxx);
                  }
               }
            }
         }
      }

      ☃x.release();
      ☃xx.release();
      ☃xxx.release();
   }

   protected void onInsideBlock(IBlockState var1) {
   }

   protected void playStepSound(BlockPos var1, Block var2) {
      SoundType ☃ = ☃.getSoundType();
      if (this.world.getBlockState(☃.up()).getBlock() == Blocks.SNOW_LAYER) {
         ☃ = Blocks.SNOW_LAYER.getSoundType();
         this.playSound(☃.getStepSound(), ☃.getVolume() * 0.15F, ☃.getPitch());
      } else if (!☃.getDefaultState().getMaterial().isLiquid()) {
         this.playSound(☃.getStepSound(), ☃.getVolume() * 0.15F, ☃.getPitch());
      }
   }

   protected float playFlySound(float var1) {
      return 0.0F;
   }

   protected boolean makeFlySound() {
      return false;
   }

   public void playSound(SoundEvent var1, float var2, float var3) {
      if (!this.isSilent()) {
         this.world.playSound(null, this.posX, this.posY, this.posZ, ☃, this.getSoundCategory(), ☃, ☃);
      }
   }

   public boolean isSilent() {
      return this.dataManager.get(SILENT);
   }

   public void setSilent(boolean var1) {
      this.dataManager.set(SILENT, ☃);
   }

   public boolean hasNoGravity() {
      return this.dataManager.get(NO_GRAVITY);
   }

   public void setNoGravity(boolean var1) {
      this.dataManager.set(NO_GRAVITY, ☃);
   }

   protected boolean canTriggerWalking() {
      return true;
   }

   protected void updateFallState(double var1, boolean var3, IBlockState var4, BlockPos var5) {
      if (☃) {
         if (this.fallDistance > 0.0F) {
            ☃.getBlock().onFallenUpon(this.world, ☃, this, this.fallDistance);
         }

         this.fallDistance = 0.0F;
      } else if (☃ < 0.0) {
         this.fallDistance = (float)(this.fallDistance - ☃);
      }
   }

   @Nullable
   public AxisAlignedBB getCollisionBoundingBox() {
      return null;
   }

   protected void dealFireDamage(int var1) {
      if (!this.isImmuneToFire) {
         this.attackEntityFrom(DamageSource.IN_FIRE, ☃);
      }
   }

   public final boolean isImmuneToFire() {
      return this.isImmuneToFire;
   }

   public void fall(float var1, float var2) {
      if (this.isBeingRidden()) {
         for (Entity ☃ : this.getPassengers()) {
            ☃.fall(☃, ☃);
         }
      }
   }

   public boolean isWet() {
      if (this.inWater) {
         return true;
      } else {
         BlockPos.PooledMutableBlockPos ☃ = BlockPos.PooledMutableBlockPos.retain(this.posX, this.posY, this.posZ);
         if (!this.world.isRainingAt(☃) && !this.world.isRainingAt(☃.setPos(this.posX, this.posY + this.height, this.posZ))) {
            ☃.release();
            return false;
         } else {
            ☃.release();
            return true;
         }
      }
   }

   public boolean isInWater() {
      return this.inWater;
   }

   public boolean isOverWater() {
      return this.world.handleMaterialAcceleration(this.getEntityBoundingBox().grow(0.0, -20.0, 0.0).shrink(0.001), Material.WATER, this);
   }

   public boolean handleWaterMovement() {
      if (this.getRidingEntity() instanceof EntityBoat) {
         this.inWater = false;
      } else if (this.world.handleMaterialAcceleration(this.getEntityBoundingBox().grow(0.0, -0.4F, 0.0).shrink(0.001), Material.WATER, this)) {
         if (!this.inWater && !this.firstUpdate) {
            this.doWaterSplashEffect();
         }

         this.fallDistance = 0.0F;
         this.inWater = true;
         this.extinguish();
      } else {
         this.inWater = false;
      }

      return this.inWater;
   }

   protected void doWaterSplashEffect() {
      Entity ☃ = this.isBeingRidden() && this.getControllingPassenger() != null ? this.getControllingPassenger() : this;
      float ☃x = ☃ == this ? 0.2F : 0.9F;
      float ☃xx = MathHelper.sqrt(☃.motionX * ☃.motionX * 0.2F + ☃.motionY * ☃.motionY + ☃.motionZ * ☃.motionZ * 0.2F) * ☃x;
      if (☃xx > 1.0F) {
         ☃xx = 1.0F;
      }

      this.playSound(this.getSplashSound(), ☃xx, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
      float ☃xxx = MathHelper.floor(this.getEntityBoundingBox().minY);

      for (int ☃xxxx = 0; ☃xxxx < 1.0F + this.width * 20.0F; ☃xxxx++) {
         float ☃xxxxx = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
         float ☃xxxxxx = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
         this.world
            .spawnParticle(
               EnumParticleTypes.WATER_BUBBLE,
               this.posX + ☃xxxxx,
               ☃xxx + 1.0F,
               this.posZ + ☃xxxxxx,
               this.motionX,
               this.motionY - this.rand.nextFloat() * 0.2F,
               this.motionZ
            );
      }

      for (int ☃xxxx = 0; ☃xxxx < 1.0F + this.width * 20.0F; ☃xxxx++) {
         float ☃xxxxx = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
         float ☃xxxxxx = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
         this.world
            .spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + ☃xxxxx, ☃xxx + 1.0F, this.posZ + ☃xxxxxx, this.motionX, this.motionY, this.motionZ);
      }
   }

   public void spawnRunningParticles() {
      if (this.isSprinting() && !this.isInWater()) {
         this.createRunningParticles();
      }
   }

   protected void createRunningParticles() {
      int ☃ = MathHelper.floor(this.posX);
      int ☃x = MathHelper.floor(this.posY - 0.2F);
      int ☃xx = MathHelper.floor(this.posZ);
      BlockPos ☃xxx = new BlockPos(☃, ☃x, ☃xx);
      IBlockState ☃xxxx = this.world.getBlockState(☃xxx);
      if (☃xxxx.getRenderType() != EnumBlockRenderType.INVISIBLE) {
         this.world
            .spawnParticle(
               EnumParticleTypes.BLOCK_CRACK,
               this.posX + (this.rand.nextFloat() - 0.5) * this.width,
               this.getEntityBoundingBox().minY + 0.1,
               this.posZ + (this.rand.nextFloat() - 0.5) * this.width,
               -this.motionX * 4.0,
               1.5,
               -this.motionZ * 4.0,
               Block.getStateId(☃xxxx)
            );
      }
   }

   public boolean isInsideOfMaterial(Material var1) {
      if (this.getRidingEntity() instanceof EntityBoat) {
         return false;
      } else {
         double ☃ = this.posY + this.getEyeHeight();
         BlockPos ☃x = new BlockPos(this.posX, ☃, this.posZ);
         IBlockState ☃xx = this.world.getBlockState(☃x);
         if (☃xx.getMaterial() == ☃) {
            float ☃xxx = BlockLiquid.getLiquidHeightPercent(☃xx.getBlock().getMetaFromState(☃xx)) - 0.11111111F;
            float ☃xxxx = ☃x.getY() + 1 - ☃xxx;
            boolean ☃xxxxx = ☃ < ☃xxxx;
            return !☃xxxxx && this instanceof EntityPlayer ? false : ☃xxxxx;
         } else {
            return false;
         }
      }
   }

   public boolean isInLava() {
      return this.world.isMaterialInBB(this.getEntityBoundingBox().grow(-0.1F, -0.4F, -0.1F), Material.LAVA);
   }

   public void moveRelative(float var1, float var2, float var3, float var4) {
      float ☃ = ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
      if (!(☃ < 1.0E-4F)) {
         ☃ = MathHelper.sqrt(☃);
         if (☃ < 1.0F) {
            ☃ = 1.0F;
         }

         ☃ = ☃ / ☃;
         ☃ *= ☃;
         ☃ *= ☃;
         ☃ *= ☃;
         float ☃x = MathHelper.sin(this.rotationYaw * (float) (Math.PI / 180.0));
         float ☃xx = MathHelper.cos(this.rotationYaw * (float) (Math.PI / 180.0));
         this.motionX += ☃ * ☃xx - ☃ * ☃x;
         this.motionY += ☃;
         this.motionZ += ☃ * ☃xx + ☃ * ☃x;
      }
   }

   public int getBrightnessForRender() {
      BlockPos.MutableBlockPos ☃ = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
      if (this.world.isBlockLoaded(☃)) {
         ☃.setY(MathHelper.floor(this.posY + this.getEyeHeight()));
         return this.world.getCombinedLight(☃, 0);
      } else {
         return 0;
      }
   }

   public float getBrightness() {
      BlockPos.MutableBlockPos ☃ = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
      if (this.world.isBlockLoaded(☃)) {
         ☃.setY(MathHelper.floor(this.posY + this.getEyeHeight()));
         return this.world.getLightBrightness(☃);
      } else {
         return 0.0F;
      }
   }

   public void setWorld(World var1) {
      this.world = ☃;
   }

   public void setPositionAndRotation(double var1, double var3, double var5, float var7, float var8) {
      this.posX = MathHelper.clamp(☃, -3.0E7, 3.0E7);
      this.posY = ☃;
      this.posZ = MathHelper.clamp(☃, -3.0E7, 3.0E7);
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      ☃ = MathHelper.clamp(☃, -90.0F, 90.0F);
      this.rotationYaw = ☃;
      this.rotationPitch = ☃;
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
      double ☃ = this.prevRotationYaw - ☃;
      if (☃ < -180.0) {
         this.prevRotationYaw += 360.0F;
      }

      if (☃ >= 180.0) {
         this.prevRotationYaw -= 360.0F;
      }

      this.setPosition(this.posX, this.posY, this.posZ);
      this.setRotation(☃, ☃);
   }

   public void moveToBlockPosAndAngles(BlockPos var1, float var2, float var3) {
      this.setLocationAndAngles(☃.getX() + 0.5, ☃.getY(), ☃.getZ() + 0.5, ☃, ☃);
   }

   public void setLocationAndAngles(double var1, double var3, double var5, float var7, float var8) {
      this.posX = ☃;
      this.posY = ☃;
      this.posZ = ☃;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      this.rotationYaw = ☃;
      this.rotationPitch = ☃;
      this.setPosition(this.posX, this.posY, this.posZ);
   }

   public float getDistance(Entity var1) {
      float ☃ = (float)(this.posX - ☃.posX);
      float ☃x = (float)(this.posY - ☃.posY);
      float ☃xx = (float)(this.posZ - ☃.posZ);
      return MathHelper.sqrt(☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx);
   }

   public double getDistanceSq(double var1, double var3, double var5) {
      double ☃ = this.posX - ☃;
      double ☃x = this.posY - ☃;
      double ☃xx = this.posZ - ☃;
      return ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
   }

   public double getDistanceSq(BlockPos var1) {
      return ☃.distanceSq(this.posX, this.posY, this.posZ);
   }

   public double getDistanceSqToCenter(BlockPos var1) {
      return ☃.distanceSqToCenter(this.posX, this.posY, this.posZ);
   }

   public double getDistance(double var1, double var3, double var5) {
      double ☃ = this.posX - ☃;
      double ☃x = this.posY - ☃;
      double ☃xx = this.posZ - ☃;
      return MathHelper.sqrt(☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx);
   }

   public double getDistanceSq(Entity var1) {
      double ☃ = this.posX - ☃.posX;
      double ☃x = this.posY - ☃.posY;
      double ☃xx = this.posZ - ☃.posZ;
      return ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
   }

   public void onCollideWithPlayer(EntityPlayer var1) {
   }

   public void applyEntityCollision(Entity var1) {
      if (!this.isRidingSameEntity(☃)) {
         if (!☃.noClip && !this.noClip) {
            double ☃ = ☃.posX - this.posX;
            double ☃x = ☃.posZ - this.posZ;
            double ☃xx = MathHelper.absMax(☃, ☃x);
            if (☃xx >= 0.01F) {
               ☃xx = MathHelper.sqrt(☃xx);
               ☃ /= ☃xx;
               ☃x /= ☃xx;
               double ☃xxx = 1.0 / ☃xx;
               if (☃xxx > 1.0) {
                  ☃xxx = 1.0;
               }

               ☃ *= ☃xxx;
               ☃x *= ☃xxx;
               ☃ *= 0.05F;
               ☃x *= 0.05F;
               ☃ *= 1.0F - this.entityCollisionReduction;
               ☃x *= 1.0F - this.entityCollisionReduction;
               if (!this.isBeingRidden()) {
                  this.addVelocity(-☃, 0.0, -☃x);
               }

               if (!☃.isBeingRidden()) {
                  ☃.addVelocity(☃, 0.0, ☃x);
               }
            }
         }
      }
   }

   public void addVelocity(double var1, double var3, double var5) {
      this.motionX += ☃;
      this.motionY += ☃;
      this.motionZ += ☃;
      this.isAirBorne = true;
   }

   protected void markVelocityChanged() {
      this.velocityChanged = true;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         this.markVelocityChanged();
         return false;
      }
   }

   public Vec3d getLook(float var1) {
      if (☃ == 1.0F) {
         return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
      } else {
         float ☃ = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * ☃;
         float ☃x = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * ☃;
         return this.getVectorForRotation(☃, ☃x);
      }
   }

   protected final Vec3d getVectorForRotation(float var1, float var2) {
      float ☃ = MathHelper.cos(-☃ * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃x = MathHelper.sin(-☃ * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃xx = -MathHelper.cos(-☃ * (float) (Math.PI / 180.0));
      float ☃xxx = MathHelper.sin(-☃ * (float) (Math.PI / 180.0));
      return new Vec3d(☃x * ☃xx, ☃xxx, ☃ * ☃xx);
   }

   public Vec3d getPositionEyes(float var1) {
      if (☃ == 1.0F) {
         return new Vec3d(this.posX, this.posY + this.getEyeHeight(), this.posZ);
      } else {
         double ☃ = this.prevPosX + (this.posX - this.prevPosX) * ☃;
         double ☃x = this.prevPosY + (this.posY - this.prevPosY) * ☃ + this.getEyeHeight();
         double ☃xx = this.prevPosZ + (this.posZ - this.prevPosZ) * ☃;
         return new Vec3d(☃, ☃x, ☃xx);
      }
   }

   @Nullable
   public RayTraceResult rayTrace(double var1, float var3) {
      Vec3d ☃ = this.getPositionEyes(☃);
      Vec3d ☃x = this.getLook(☃);
      Vec3d ☃xx = ☃.add(☃x.x * ☃, ☃x.y * ☃, ☃x.z * ☃);
      return this.world.rayTraceBlocks(☃, ☃xx, false, false, true);
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public boolean canBePushed() {
      return false;
   }

   public void awardKillScore(Entity var1, int var2, DamageSource var3) {
      if (☃ instanceof EntityPlayerMP) {
         CriteriaTriggers.ENTITY_KILLED_PLAYER.trigger((EntityPlayerMP)☃, this, ☃);
      }
   }

   public boolean isInRangeToRender3d(double var1, double var3, double var5) {
      double ☃ = this.posX - ☃;
      double ☃x = this.posY - ☃;
      double ☃xx = this.posZ - ☃;
      double ☃xxx = ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
      return this.isInRangeToRenderDist(☃xxx);
   }

   public boolean isInRangeToRenderDist(double var1) {
      double ☃ = this.getEntityBoundingBox().getAverageEdgeLength();
      if (Double.isNaN(☃)) {
         ☃ = 1.0;
      }

      ☃ *= 64.0 * renderDistanceWeight;
      return ☃ < ☃ * ☃;
   }

   public boolean writeToNBTAtomically(NBTTagCompound var1) {
      String ☃ = this.getEntityString();
      if (!this.isDead && ☃ != null) {
         ☃.setString("id", ☃);
         this.writeToNBT(☃);
         return true;
      } else {
         return false;
      }
   }

   public boolean writeToNBTOptional(NBTTagCompound var1) {
      String ☃ = this.getEntityString();
      if (!this.isDead && ☃ != null && !this.isRiding()) {
         ☃.setString("id", ☃);
         this.writeToNBT(☃);
         return true;
      } else {
         return false;
      }
   }

   public static void registerFixes(DataFixer var0) {
      ☃.registerWalker(FixTypes.ENTITY, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            if (☃.hasKey("Passengers", 9)) {
               NBTTagList ☃ = ☃.getTagList("Passengers", 10);

               for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
                  ☃.set(☃x, ☃.process(FixTypes.ENTITY, ☃.getCompoundTagAt(☃x), ☃));
               }
            }

            return ☃;
         }
      });
   }

   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      try {
         ☃.setTag("Pos", this.newDoubleNBTList(this.posX, this.posY, this.posZ));
         ☃.setTag("Motion", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
         ☃.setTag("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
         ☃.setFloat("FallDistance", this.fallDistance);
         ☃.setShort("Fire", (short)this.fire);
         ☃.setShort("Air", (short)this.getAir());
         ☃.setBoolean("OnGround", this.onGround);
         ☃.setInteger("Dimension", this.dimension);
         ☃.setBoolean("Invulnerable", this.invulnerable);
         ☃.setInteger("PortalCooldown", this.timeUntilPortal);
         ☃.setUniqueId("UUID", this.getUniqueID());
         if (this.hasCustomName()) {
            ☃.setString("CustomName", this.getCustomNameTag());
         }

         if (this.getAlwaysRenderNameTag()) {
            ☃.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
         }

         this.cmdResultStats.writeStatsToNBT(☃);
         if (this.isSilent()) {
            ☃.setBoolean("Silent", this.isSilent());
         }

         if (this.hasNoGravity()) {
            ☃.setBoolean("NoGravity", this.hasNoGravity());
         }

         if (this.glowing) {
            ☃.setBoolean("Glowing", this.glowing);
         }

         if (!this.tags.isEmpty()) {
            NBTTagList ☃ = new NBTTagList();

            for (String ☃x : this.tags) {
               ☃.appendTag(new NBTTagString(☃x));
            }

            ☃.setTag("Tags", ☃);
         }

         this.writeEntityToNBT(☃);
         if (this.isBeingRidden()) {
            NBTTagList ☃ = new NBTTagList();

            for (Entity ☃x : this.getPassengers()) {
               NBTTagCompound ☃xx = new NBTTagCompound();
               if (☃x.writeToNBTAtomically(☃xx)) {
                  ☃.appendTag(☃xx);
               }
            }

            if (!☃.isEmpty()) {
               ☃.setTag("Passengers", ☃);
            }
         }

         return ☃;
      } catch (Throwable var6) {
         CrashReport ☃ = CrashReport.makeCrashReport(var6, "Saving entity NBT");
         CrashReportCategory ☃xx = ☃.makeCategory("Entity being saved");
         this.addEntityCrashInfo(☃xx);
         throw new ReportedException(☃);
      }
   }

   public void readFromNBT(NBTTagCompound var1) {
      try {
         NBTTagList ☃ = ☃.getTagList("Pos", 6);
         NBTTagList ☃x = ☃.getTagList("Motion", 6);
         NBTTagList ☃xx = ☃.getTagList("Rotation", 5);
         this.motionX = ☃x.getDoubleAt(0);
         this.motionY = ☃x.getDoubleAt(1);
         this.motionZ = ☃x.getDoubleAt(2);
         if (Math.abs(this.motionX) > 10.0) {
            this.motionX = 0.0;
         }

         if (Math.abs(this.motionY) > 10.0) {
            this.motionY = 0.0;
         }

         if (Math.abs(this.motionZ) > 10.0) {
            this.motionZ = 0.0;
         }

         this.posX = ☃.getDoubleAt(0);
         this.posY = ☃.getDoubleAt(1);
         this.posZ = ☃.getDoubleAt(2);
         this.lastTickPosX = this.posX;
         this.lastTickPosY = this.posY;
         this.lastTickPosZ = this.posZ;
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         this.rotationYaw = ☃xx.getFloatAt(0);
         this.rotationPitch = ☃xx.getFloatAt(1);
         this.prevRotationYaw = this.rotationYaw;
         this.prevRotationPitch = this.rotationPitch;
         this.setRotationYawHead(this.rotationYaw);
         this.setRenderYawOffset(this.rotationYaw);
         this.fallDistance = ☃.getFloat("FallDistance");
         this.fire = ☃.getShort("Fire");
         this.setAir(☃.getShort("Air"));
         this.onGround = ☃.getBoolean("OnGround");
         if (☃.hasKey("Dimension")) {
            this.dimension = ☃.getInteger("Dimension");
         }

         this.invulnerable = ☃.getBoolean("Invulnerable");
         this.timeUntilPortal = ☃.getInteger("PortalCooldown");
         if (☃.hasUniqueId("UUID")) {
            this.entityUniqueID = ☃.getUniqueId("UUID");
            this.cachedUniqueIdString = this.entityUniqueID.toString();
         }

         this.setPosition(this.posX, this.posY, this.posZ);
         this.setRotation(this.rotationYaw, this.rotationPitch);
         if (☃.hasKey("CustomName", 8)) {
            this.setCustomNameTag(☃.getString("CustomName"));
         }

         this.setAlwaysRenderNameTag(☃.getBoolean("CustomNameVisible"));
         this.cmdResultStats.readStatsFromNBT(☃);
         this.setSilent(☃.getBoolean("Silent"));
         this.setNoGravity(☃.getBoolean("NoGravity"));
         this.setGlowing(☃.getBoolean("Glowing"));
         if (☃.hasKey("Tags", 9)) {
            this.tags.clear();
            NBTTagList ☃xxx = ☃.getTagList("Tags", 8);
            int ☃xxxx = Math.min(☃xxx.tagCount(), 1024);

            for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx; ☃xxxxx++) {
               this.tags.add(☃xxx.getStringTagAt(☃xxxxx));
            }
         }

         this.readEntityFromNBT(☃);
         if (this.shouldSetPosAfterLoading()) {
            this.setPosition(this.posX, this.posY, this.posZ);
         }
      } catch (Throwable var8) {
         CrashReport ☃xxx = CrashReport.makeCrashReport(var8, "Loading entity NBT");
         CrashReportCategory ☃xxxx = ☃xxx.makeCategory("Entity being loaded");
         this.addEntityCrashInfo(☃xxxx);
         throw new ReportedException(☃xxx);
      }
   }

   protected boolean shouldSetPosAfterLoading() {
      return true;
   }

   @Nullable
   protected final String getEntityString() {
      ResourceLocation ☃ = EntityList.getKey(this);
      return ☃ == null ? null : ☃.toString();
   }

   protected abstract void readEntityFromNBT(NBTTagCompound var1);

   protected abstract void writeEntityToNBT(NBTTagCompound var1);

   protected NBTTagList newDoubleNBTList(double... var1) {
      NBTTagList ☃ = new NBTTagList();

      for (double ☃x : ☃) {
         ☃.appendTag(new NBTTagDouble(☃x));
      }

      return ☃;
   }

   protected NBTTagList newFloatNBTList(float... var1) {
      NBTTagList ☃ = new NBTTagList();

      for (float ☃x : ☃) {
         ☃.appendTag(new NBTTagFloat(☃x));
      }

      return ☃;
   }

   @Nullable
   public EntityItem dropItem(Item var1, int var2) {
      return this.dropItemWithOffset(☃, ☃, 0.0F);
   }

   @Nullable
   public EntityItem dropItemWithOffset(Item var1, int var2, float var3) {
      return this.entityDropItem(new ItemStack(☃, ☃, 0), ☃);
   }

   @Nullable
   public EntityItem entityDropItem(ItemStack var1, float var2) {
      if (☃.isEmpty()) {
         return null;
      } else {
         EntityItem ☃ = new EntityItem(this.world, this.posX, this.posY + ☃, this.posZ, ☃);
         ☃.setDefaultPickupDelay();
         this.world.spawnEntity(☃);
         return ☃;
      }
   }

   public boolean isEntityAlive() {
      return !this.isDead;
   }

   public boolean isEntityInsideOpaqueBlock() {
      if (this.noClip) {
         return false;
      } else {
         BlockPos.PooledMutableBlockPos ☃ = BlockPos.PooledMutableBlockPos.retain();

         for (int ☃x = 0; ☃x < 8; ☃x++) {
            int ☃xx = MathHelper.floor(this.posY + ((☃x >> 0) % 2 - 0.5F) * 0.1F + this.getEyeHeight());
            int ☃xxx = MathHelper.floor(this.posX + ((☃x >> 1) % 2 - 0.5F) * this.width * 0.8F);
            int ☃xxxx = MathHelper.floor(this.posZ + ((☃x >> 2) % 2 - 0.5F) * this.width * 0.8F);
            if (☃.getX() != ☃xxx || ☃.getY() != ☃xx || ☃.getZ() != ☃xxxx) {
               ☃.setPos(☃xxx, ☃xx, ☃xxxx);
               if (this.world.getBlockState(☃).causesSuffocation()) {
                  ☃.release();
                  return true;
               }
            }
         }

         ☃.release();
         return false;
      }
   }

   public boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      return false;
   }

   @Nullable
   public AxisAlignedBB getCollisionBox(Entity var1) {
      return null;
   }

   public void updateRidden() {
      Entity ☃ = this.getRidingEntity();
      if (this.isRiding() && ☃.isDead) {
         this.dismountRidingEntity();
      } else {
         this.motionX = 0.0;
         this.motionY = 0.0;
         this.motionZ = 0.0;
         this.onUpdate();
         if (this.isRiding()) {
            ☃.updatePassenger(this);
         }
      }
   }

   public void updatePassenger(Entity var1) {
      if (this.isPassenger(☃)) {
         ☃.setPosition(this.posX, this.posY + this.getMountedYOffset() + ☃.getYOffset(), this.posZ);
      }
   }

   public void applyOrientationToEntity(Entity var1) {
   }

   public double getYOffset() {
      return 0.0;
   }

   public double getMountedYOffset() {
      return this.height * 0.75;
   }

   public boolean startRiding(Entity var1) {
      return this.startRiding(☃, false);
   }

   public boolean startRiding(Entity var1, boolean var2) {
      for (Entity ☃ = ☃; ☃.ridingEntity != null; ☃ = ☃.ridingEntity) {
         if (☃.ridingEntity == this) {
            return false;
         }
      }

      if (☃ || this.canBeRidden(☃) && ☃.canFitPassenger(this)) {
         if (this.isRiding()) {
            this.dismountRidingEntity();
         }

         this.ridingEntity = ☃;
         this.ridingEntity.addPassenger(this);
         return true;
      } else {
         return false;
      }
   }

   protected boolean canBeRidden(Entity var1) {
      return this.rideCooldown <= 0;
   }

   public void removePassengers() {
      for (int ☃ = this.riddenByEntities.size() - 1; ☃ >= 0; ☃--) {
         this.riddenByEntities.get(☃).dismountRidingEntity();
      }
   }

   public void dismountRidingEntity() {
      if (this.ridingEntity != null) {
         Entity ☃ = this.ridingEntity;
         this.ridingEntity = null;
         ☃.removePassenger(this);
      }
   }

   protected void addPassenger(Entity var1) {
      if (☃.getRidingEntity() != this) {
         throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
      } else {
         if (!this.world.isRemote && ☃ instanceof EntityPlayer && !(this.getControllingPassenger() instanceof EntityPlayer)) {
            this.riddenByEntities.add(0, ☃);
         } else {
            this.riddenByEntities.add(☃);
         }
      }
   }

   protected void removePassenger(Entity var1) {
      if (☃.getRidingEntity() == this) {
         throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
      } else {
         this.riddenByEntities.remove(☃);
         ☃.rideCooldown = 60;
      }
   }

   protected boolean canFitPassenger(Entity var1) {
      return this.getPassengers().size() < 1;
   }

   public void setPositionAndRotationDirect(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.setPosition(☃, ☃, ☃);
      this.setRotation(☃, ☃);
   }

   public float getCollisionBorderSize() {
      return 0.0F;
   }

   public Vec3d getLookVec() {
      return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
   }

   public Vec2f getPitchYaw() {
      return new Vec2f(this.rotationPitch, this.rotationYaw);
   }

   public Vec3d getForward() {
      return Vec3d.fromPitchYaw(this.getPitchYaw());
   }

   public void setPortal(BlockPos var1) {
      if (this.timeUntilPortal > 0) {
         this.timeUntilPortal = this.getPortalCooldown();
      } else {
         if (!this.world.isRemote && !☃.equals(this.lastPortalPos)) {
            this.lastPortalPos = new BlockPos(☃);
            BlockPattern.PatternHelper ☃ = Blocks.PORTAL.createPatternHelper(this.world, this.lastPortalPos);
            double ☃x = ☃.getForwards().getAxis() == EnumFacing.Axis.X ? ☃.getFrontTopLeft().getZ() : ☃.getFrontTopLeft().getX();
            double ☃xx = ☃.getForwards().getAxis() == EnumFacing.Axis.X ? this.posZ : this.posX;
            ☃xx = Math.abs(
               MathHelper.pct(☃xx - (☃.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), ☃x, ☃x - ☃.getWidth())
            );
            double ☃xxx = MathHelper.pct(this.posY - 1.0, ☃.getFrontTopLeft().getY(), ☃.getFrontTopLeft().getY() - ☃.getHeight());
            this.lastPortalVec = new Vec3d(☃xx, ☃xxx, 0.0);
            this.teleportDirection = ☃.getForwards();
         }

         this.inPortal = true;
      }
   }

   public int getPortalCooldown() {
      return 300;
   }

   public void setVelocity(double var1, double var3, double var5) {
      this.motionX = ☃;
      this.motionY = ☃;
      this.motionZ = ☃;
   }

   public void handleStatusUpdate(byte var1) {
   }

   public void performHurtAnimation() {
   }

   public Iterable<ItemStack> getHeldEquipment() {
      return EMPTY_EQUIPMENT;
   }

   public Iterable<ItemStack> getArmorInventoryList() {
      return EMPTY_EQUIPMENT;
   }

   public Iterable<ItemStack> getEquipmentAndArmor() {
      return Iterables.concat(this.getHeldEquipment(), this.getArmorInventoryList());
   }

   public void setItemStackToSlot(EntityEquipmentSlot var1, ItemStack var2) {
   }

   public boolean isBurning() {
      boolean ☃ = this.world != null && this.world.isRemote;
      return !this.isImmuneToFire && (this.fire > 0 || ☃ && this.getFlag(0));
   }

   public boolean isRiding() {
      return this.getRidingEntity() != null;
   }

   public boolean isBeingRidden() {
      return !this.getPassengers().isEmpty();
   }

   public boolean isSneaking() {
      return this.getFlag(1);
   }

   public void setSneaking(boolean var1) {
      this.setFlag(1, ☃);
   }

   public boolean isSprinting() {
      return this.getFlag(3);
   }

   public void setSprinting(boolean var1) {
      this.setFlag(3, ☃);
   }

   public boolean isGlowing() {
      return this.glowing || this.world.isRemote && this.getFlag(6);
   }

   public void setGlowing(boolean var1) {
      this.glowing = ☃;
      if (!this.world.isRemote) {
         this.setFlag(6, this.glowing);
      }
   }

   public boolean isInvisible() {
      return this.getFlag(5);
   }

   public boolean isInvisibleToPlayer(EntityPlayer var1) {
      if (☃.isSpectator()) {
         return false;
      } else {
         Team ☃ = this.getTeam();
         return ☃ != null && ☃ != null && ☃.getTeam() == ☃ && ☃.getSeeFriendlyInvisiblesEnabled() ? false : this.isInvisible();
      }
   }

   @Nullable
   public Team getTeam() {
      return this.world.getScoreboard().getPlayersTeam(this.getCachedUniqueIdString());
   }

   public boolean isOnSameTeam(Entity var1) {
      return this.isOnScoreboardTeam(☃.getTeam());
   }

   public boolean isOnScoreboardTeam(Team var1) {
      return this.getTeam() != null ? this.getTeam().isSameTeam(☃) : false;
   }

   public void setInvisible(boolean var1) {
      this.setFlag(5, ☃);
   }

   protected boolean getFlag(int var1) {
      return (this.dataManager.get(FLAGS) & 1 << ☃) != 0;
   }

   protected void setFlag(int var1, boolean var2) {
      byte ☃ = this.dataManager.get(FLAGS);
      if (☃) {
         this.dataManager.set(FLAGS, (byte)(☃ | 1 << ☃));
      } else {
         this.dataManager.set(FLAGS, (byte)(☃ & ~(1 << ☃)));
      }
   }

   public int getAir() {
      return this.dataManager.get(AIR);
   }

   public void setAir(int var1) {
      this.dataManager.set(AIR, ☃);
   }

   public void onStruckByLightning(EntityLightningBolt var1) {
      this.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 5.0F);
      this.fire++;
      if (this.fire == 0) {
         this.setFire(8);
      }
   }

   public void onKillEntity(EntityLivingBase var1) {
   }

   protected boolean pushOutOfBlocks(double var1, double var3, double var5) {
      BlockPos ☃ = new BlockPos(☃, ☃, ☃);
      double ☃x = ☃ - ☃.getX();
      double ☃xx = ☃ - ☃.getY();
      double ☃xxx = ☃ - ☃.getZ();
      if (!this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
         return false;
      } else {
         EnumFacing ☃xxxx = EnumFacing.UP;
         double ☃xxxxx = Double.MAX_VALUE;
         if (!this.world.isBlockFullCube(☃.west()) && ☃x < ☃xxxxx) {
            ☃xxxxx = ☃x;
            ☃xxxx = EnumFacing.WEST;
         }

         if (!this.world.isBlockFullCube(☃.east()) && 1.0 - ☃x < ☃xxxxx) {
            ☃xxxxx = 1.0 - ☃x;
            ☃xxxx = EnumFacing.EAST;
         }

         if (!this.world.isBlockFullCube(☃.north()) && ☃xxx < ☃xxxxx) {
            ☃xxxxx = ☃xxx;
            ☃xxxx = EnumFacing.NORTH;
         }

         if (!this.world.isBlockFullCube(☃.south()) && 1.0 - ☃xxx < ☃xxxxx) {
            ☃xxxxx = 1.0 - ☃xxx;
            ☃xxxx = EnumFacing.SOUTH;
         }

         if (!this.world.isBlockFullCube(☃.up()) && 1.0 - ☃xx < ☃xxxxx) {
            ☃xxxxx = 1.0 - ☃xx;
            ☃xxxx = EnumFacing.UP;
         }

         float ☃xxxxxx = this.rand.nextFloat() * 0.2F + 0.1F;
         float ☃xxxxxxx = ☃xxxx.getAxisDirection().getOffset();
         if (☃xxxx.getAxis() == EnumFacing.Axis.X) {
            this.motionX = ☃xxxxxxx * ☃xxxxxx;
            this.motionY *= 0.75;
            this.motionZ *= 0.75;
         } else if (☃xxxx.getAxis() == EnumFacing.Axis.Y) {
            this.motionX *= 0.75;
            this.motionY = ☃xxxxxxx * ☃xxxxxx;
            this.motionZ *= 0.75;
         } else if (☃xxxx.getAxis() == EnumFacing.Axis.Z) {
            this.motionX *= 0.75;
            this.motionY *= 0.75;
            this.motionZ = ☃xxxxxxx * ☃xxxxxx;
         }

         return true;
      }
   }

   public void setInWeb() {
      this.isInWeb = true;
      this.fallDistance = 0.0F;
   }

   @Override
   public String getName() {
      if (this.hasCustomName()) {
         return this.getCustomNameTag();
      } else {
         String ☃ = EntityList.getEntityString(this);
         if (☃ == null) {
            ☃ = "generic";
         }

         return I18n.translateToLocal("entity." + ☃ + ".name");
      }
   }

   @Nullable
   public Entity[] getParts() {
      return null;
   }

   public boolean isEntityEqual(Entity var1) {
      return this == ☃;
   }

   public float getRotationYawHead() {
      return 0.0F;
   }

   public void setRotationYawHead(float var1) {
   }

   public void setRenderYawOffset(float var1) {
   }

   public boolean canBeAttackedWithItem() {
      return true;
   }

   public boolean hitByEntity(Entity var1) {
      return false;
   }

   @Override
   public String toString() {
      return String.format(
         "%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]",
         this.getClass().getSimpleName(),
         this.getName(),
         this.entityId,
         this.world == null ? "~NULL~" : this.world.getWorldInfo().getWorldName(),
         this.posX,
         this.posY,
         this.posZ
      );
   }

   public boolean isEntityInvulnerable(DamageSource var1) {
      return this.invulnerable && ☃ != DamageSource.OUT_OF_WORLD && !☃.isCreativePlayer();
   }

   public boolean getIsInvulnerable() {
      return this.invulnerable;
   }

   public void setEntityInvulnerable(boolean var1) {
      this.invulnerable = ☃;
   }

   public void copyLocationAndAnglesFrom(Entity var1) {
      this.setLocationAndAngles(☃.posX, ☃.posY, ☃.posZ, ☃.rotationYaw, ☃.rotationPitch);
   }

   private void copyDataFromOld(Entity var1) {
      NBTTagCompound ☃ = ☃.writeToNBT(new NBTTagCompound());
      ☃.removeTag("Dimension");
      this.readFromNBT(☃);
      this.timeUntilPortal = ☃.timeUntilPortal;
      this.lastPortalPos = ☃.lastPortalPos;
      this.lastPortalVec = ☃.lastPortalVec;
      this.teleportDirection = ☃.teleportDirection;
   }

   @Nullable
   public Entity changeDimension(int var1) {
      if (!this.world.isRemote && !this.isDead) {
         this.world.profiler.startSection("changeDimension");
         MinecraftServer ☃ = this.getServer();
         int ☃x = this.dimension;
         WorldServer ☃xx = ☃.getWorld(☃x);
         WorldServer ☃xxx = ☃.getWorld(☃);
         this.dimension = ☃;
         if (☃x == 1 && ☃ == 1) {
            ☃xxx = ☃.getWorld(0);
            this.dimension = 0;
         }

         this.world.removeEntity(this);
         this.isDead = false;
         this.world.profiler.startSection("reposition");
         BlockPos ☃xxxx;
         if (☃ == 1) {
            ☃xxxx = ☃xxx.getSpawnCoordinate();
         } else {
            double ☃xxxxx = this.posX;
            double ☃xxxxxx = this.posZ;
            double ☃xxxxxxx = 8.0;
            if (☃ == -1) {
               ☃xxxxx = MathHelper.clamp(☃xxxxx / 8.0, ☃xxx.getWorldBorder().minX() + 16.0, ☃xxx.getWorldBorder().maxX() - 16.0);
               ☃xxxxxx = MathHelper.clamp(☃xxxxxx / 8.0, ☃xxx.getWorldBorder().minZ() + 16.0, ☃xxx.getWorldBorder().maxZ() - 16.0);
            } else if (☃ == 0) {
               ☃xxxxx = MathHelper.clamp(☃xxxxx * 8.0, ☃xxx.getWorldBorder().minX() + 16.0, ☃xxx.getWorldBorder().maxX() - 16.0);
               ☃xxxxxx = MathHelper.clamp(☃xxxxxx * 8.0, ☃xxx.getWorldBorder().minZ() + 16.0, ☃xxx.getWorldBorder().maxZ() - 16.0);
            }

            ☃xxxxx = MathHelper.clamp((int)☃xxxxx, -29999872, 29999872);
            ☃xxxxxx = MathHelper.clamp((int)☃xxxxxx, -29999872, 29999872);
            float ☃xxxxxxxx = this.rotationYaw;
            this.setLocationAndAngles(☃xxxxx, this.posY, ☃xxxxxx, 90.0F, 0.0F);
            Teleporter ☃xxxxxxxxx = ☃xxx.getDefaultTeleporter();
            ☃xxxxxxxxx.placeInExistingPortal(this, ☃xxxxxxxx);
            ☃xxxx = new BlockPos(this);
         }

         ☃xx.updateEntityWithOptionalForce(this, false);
         this.world.profiler.endStartSection("reloading");
         Entity ☃xxxxx = EntityList.newEntity((Class<? extends Entity>)this.getClass(), ☃xxx);
         if (☃xxxxx != null) {
            ☃xxxxx.copyDataFromOld(this);
            if (☃x == 1 && ☃ == 1) {
               BlockPos ☃xxxxxx = ☃xxx.getTopSolidOrLiquidBlock(☃xxx.getSpawnPoint());
               ☃xxxxx.moveToBlockPosAndAngles(☃xxxxxx, ☃xxxxx.rotationYaw, ☃xxxxx.rotationPitch);
            } else {
               ☃xxxxx.moveToBlockPosAndAngles(☃xxxx, ☃xxxxx.rotationYaw, ☃xxxxx.rotationPitch);
            }

            boolean ☃xxxxxx = ☃xxxxx.forceSpawn;
            ☃xxxxx.forceSpawn = true;
            ☃xxx.spawnEntity(☃xxxxx);
            ☃xxxxx.forceSpawn = ☃xxxxxx;
            ☃xxx.updateEntityWithOptionalForce(☃xxxxx, false);
         }

         this.isDead = true;
         this.world.profiler.endSection();
         ☃xx.resetUpdateEntityTick();
         ☃xxx.resetUpdateEntityTick();
         this.world.profiler.endSection();
         return ☃xxxxx;
      } else {
         return null;
      }
   }

   public boolean isNonBoss() {
      return true;
   }

   public float getExplosionResistance(Explosion var1, World var2, BlockPos var3, IBlockState var4) {
      return ☃.getBlock().getExplosionResistance(this);
   }

   public boolean canExplosionDestroyBlock(Explosion var1, World var2, BlockPos var3, IBlockState var4, float var5) {
      return true;
   }

   public int getMaxFallHeight() {
      return 3;
   }

   public Vec3d getLastPortalVec() {
      return this.lastPortalVec;
   }

   public EnumFacing getTeleportDirection() {
      return this.teleportDirection;
   }

   public boolean doesEntityNotTriggerPressurePlate() {
      return false;
   }

   public void addEntityCrashInfo(CrashReportCategory var1) {
      ☃.addDetail("Entity Type", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return EntityList.getKey(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
         }
      });
      ☃.addCrashSection("Entity ID", this.entityId);
      ☃.addDetail("Entity Name", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return Entity.this.getName();
         }
      });
      ☃.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", this.posX, this.posY, this.posZ));
      ☃.addCrashSection(
         "Entity's Block location",
         CrashReportCategory.getCoordinateInfo(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ))
      );
      ☃.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", this.motionX, this.motionY, this.motionZ));
      ☃.addDetail("Entity's Passengers", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return Entity.this.getPassengers().toString();
         }
      });
      ☃.addDetail("Entity's Vehicle", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return Entity.this.getRidingEntity().toString();
         }
      });
   }

   public boolean canRenderOnFire() {
      return this.isBurning();
   }

   public void setUniqueId(UUID var1) {
      this.entityUniqueID = ☃;
      this.cachedUniqueIdString = this.entityUniqueID.toString();
   }

   public UUID getUniqueID() {
      return this.entityUniqueID;
   }

   public String getCachedUniqueIdString() {
      return this.cachedUniqueIdString;
   }

   public boolean isPushedByWater() {
      return true;
   }

   public static double getRenderDistanceWeight() {
      return renderDistanceWeight;
   }

   public static void setRenderDistanceWeight(double var0) {
      renderDistanceWeight = ☃;
   }

   @Override
   public ITextComponent getDisplayName() {
      TextComponentString ☃ = new TextComponentString(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
      ☃.getStyle().setHoverEvent(this.getHoverEvent());
      ☃.getStyle().setInsertion(this.getCachedUniqueIdString());
      return ☃;
   }

   public void setCustomNameTag(String var1) {
      this.dataManager.set(CUSTOM_NAME, ☃);
   }

   public String getCustomNameTag() {
      return this.dataManager.get(CUSTOM_NAME);
   }

   public boolean hasCustomName() {
      return !this.dataManager.get(CUSTOM_NAME).isEmpty();
   }

   public void setAlwaysRenderNameTag(boolean var1) {
      this.dataManager.set(CUSTOM_NAME_VISIBLE, ☃);
   }

   public boolean getAlwaysRenderNameTag() {
      return this.dataManager.get(CUSTOM_NAME_VISIBLE);
   }

   public void setPositionAndUpdate(double var1, double var3, double var5) {
      this.isPositionDirty = true;
      this.setLocationAndAngles(☃, ☃, ☃, this.rotationYaw, this.rotationPitch);
      this.world.updateEntityWithOptionalForce(this, false);
   }

   public boolean getAlwaysRenderNameTagForRender() {
      return this.getAlwaysRenderNameTag();
   }

   public void notifyDataManagerChange(DataParameter<?> var1) {
   }

   public EnumFacing getHorizontalFacing() {
      return EnumFacing.byHorizontalIndex(MathHelper.floor(this.rotationYaw * 4.0F / 360.0F + 0.5) & 3);
   }

   public EnumFacing getAdjustedHorizontalFacing() {
      return this.getHorizontalFacing();
   }

   protected HoverEvent getHoverEvent() {
      NBTTagCompound ☃ = new NBTTagCompound();
      ResourceLocation ☃x = EntityList.getKey(this);
      ☃.setString("id", this.getCachedUniqueIdString());
      if (☃x != null) {
         ☃.setString("type", ☃x.toString());
      }

      ☃.setString("name", this.getName());
      return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new TextComponentString(☃.toString()));
   }

   public boolean isSpectatedByPlayer(EntityPlayerMP var1) {
      return true;
   }

   public AxisAlignedBB getEntityBoundingBox() {
      return this.boundingBox;
   }

   public AxisAlignedBB getRenderBoundingBox() {
      return this.getEntityBoundingBox();
   }

   public void setEntityBoundingBox(AxisAlignedBB var1) {
      this.boundingBox = ☃;
   }

   public float getEyeHeight() {
      return this.height * 0.85F;
   }

   public boolean isOutsideBorder() {
      return this.isOutsideBorder;
   }

   public void setOutsideBorder(boolean var1) {
      this.isOutsideBorder = ☃;
   }

   public boolean replaceItemInInventory(int var1, ItemStack var2) {
      return false;
   }

   @Override
   public void sendMessage(ITextComponent var1) {
   }

   @Override
   public boolean canUseCommand(int var1, String var2) {
      return true;
   }

   @Override
   public BlockPos getPosition() {
      return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
   }

   @Override
   public Vec3d getPositionVector() {
      return new Vec3d(this.posX, this.posY, this.posZ);
   }

   @Override
   public World getEntityWorld() {
      return this.world;
   }

   @Override
   public Entity getCommandSenderEntity() {
      return this;
   }

   @Override
   public boolean sendCommandFeedback() {
      return false;
   }

   @Override
   public void setCommandStat(CommandResultStats.Type var1, int var2) {
      if (this.world != null && !this.world.isRemote) {
         this.cmdResultStats.setCommandStatForSender(this.world.getMinecraftServer(), this, ☃, ☃);
      }
   }

   @Nullable
   @Override
   public MinecraftServer getServer() {
      return this.world.getMinecraftServer();
   }

   public CommandResultStats getCommandStats() {
      return this.cmdResultStats;
   }

   public void setCommandStats(Entity var1) {
      this.cmdResultStats.addAllStats(☃.getCommandStats());
   }

   public EnumActionResult applyPlayerInteraction(EntityPlayer var1, Vec3d var2, EnumHand var3) {
      return EnumActionResult.PASS;
   }

   public boolean isImmuneToExplosions() {
      return false;
   }

   protected void applyEnchantments(EntityLivingBase var1, Entity var2) {
      if (☃ instanceof EntityLivingBase) {
         EnchantmentHelper.applyThornEnchantments((EntityLivingBase)☃, ☃);
      }

      EnchantmentHelper.applyArthropodEnchantments(☃, ☃);
   }

   public void addTrackingPlayer(EntityPlayerMP var1) {
   }

   public void removeTrackingPlayer(EntityPlayerMP var1) {
   }

   public float getRotatedYaw(Rotation var1) {
      float ☃ = MathHelper.wrapDegrees(this.rotationYaw);
      switch (☃) {
         case CLOCKWISE_180:
            return ☃ + 180.0F;
         case COUNTERCLOCKWISE_90:
            return ☃ + 270.0F;
         case CLOCKWISE_90:
            return ☃ + 90.0F;
         default:
            return ☃;
      }
   }

   public float getMirroredYaw(Mirror var1) {
      float ☃ = MathHelper.wrapDegrees(this.rotationYaw);
      switch (☃) {
         case LEFT_RIGHT:
            return -☃;
         case FRONT_BACK:
            return 180.0F - ☃;
         default:
            return ☃;
      }
   }

   public boolean ignoreItemEntityData() {
      return false;
   }

   public boolean setPositionNonDirty() {
      boolean ☃ = this.isPositionDirty;
      this.isPositionDirty = false;
      return ☃;
   }

   @Nullable
   public Entity getControllingPassenger() {
      return null;
   }

   public List<Entity> getPassengers() {
      return (List<Entity>)(this.riddenByEntities.isEmpty() ? Collections.emptyList() : Lists.newArrayList(this.riddenByEntities));
   }

   public boolean isPassenger(Entity var1) {
      for (Entity ☃ : this.getPassengers()) {
         if (☃.equals(☃)) {
            return true;
         }
      }

      return false;
   }

   public Collection<Entity> getRecursivePassengers() {
      Set<Entity> ☃ = Sets.newHashSet();
      this.getRecursivePassengersByType(Entity.class, ☃);
      return ☃;
   }

   public <T extends Entity> Collection<T> getRecursivePassengersByType(Class<T> var1) {
      Set<T> ☃ = Sets.newHashSet();
      this.getRecursivePassengersByType(☃, ☃);
      return ☃;
   }

   private <T extends Entity> void getRecursivePassengersByType(Class<T> var1, Set<T> var2) {
      for (Entity ☃ : this.getPassengers()) {
         if (☃.isAssignableFrom(☃.getClass())) {
            ☃.add((T)☃);
         }

         ☃.getRecursivePassengersByType(☃, ☃);
      }
   }

   public Entity getLowestRidingEntity() {
      Entity ☃ = this;

      while (☃.isRiding()) {
         ☃ = ☃.getRidingEntity();
      }

      return ☃;
   }

   public boolean isRidingSameEntity(Entity var1) {
      return this.getLowestRidingEntity() == ☃.getLowestRidingEntity();
   }

   public boolean isRidingOrBeingRiddenBy(Entity var1) {
      for (Entity ☃ : this.getPassengers()) {
         if (☃.equals(☃)) {
            return true;
         }

         if (☃.isRidingOrBeingRiddenBy(☃)) {
            return true;
         }
      }

      return false;
   }

   public boolean canPassengerSteer() {
      Entity ☃ = this.getControllingPassenger();
      return ☃ instanceof EntityPlayer ? ((EntityPlayer)☃).isUser() : !this.world.isRemote;
   }

   @Nullable
   public Entity getRidingEntity() {
      return this.ridingEntity;
   }

   public EnumPushReaction getPushReaction() {
      return EnumPushReaction.NORMAL;
   }

   public SoundCategory getSoundCategory() {
      return SoundCategory.NEUTRAL;
   }

   protected int getFireImmuneTicks() {
      return 1;
   }
}
