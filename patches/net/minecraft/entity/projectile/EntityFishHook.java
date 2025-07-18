package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
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
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityFishHook extends Entity {
   private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager.createKey(EntityFishHook.class, DataSerializers.VARINT);
   private boolean inGround;
   private int ticksInGround;
   private EntityPlayer angler;
   private int ticksInAir;
   private int ticksCatchable;
   private int ticksCaughtDelay;
   private int ticksCatchableDelay;
   private float fishApproachAngle;
   public Entity caughtEntity;
   private EntityFishHook.State currentState = EntityFishHook.State.FLYING;
   private int luck;
   private int lureSpeed;

   public EntityFishHook(World var1, EntityPlayer var2, double var3, double var5, double var7) {
      super(☃);
      this.init(☃);
      this.setPosition(☃, ☃, ☃);
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
   }

   public EntityFishHook(World var1, EntityPlayer var2) {
      super(☃);
      this.init(☃);
      this.shoot();
   }

   private void init(EntityPlayer var1) {
      this.setSize(0.25F, 0.25F);
      this.ignoreFrustumCheck = true;
      this.angler = ☃;
      this.angler.fishEntity = this;
   }

   public void setLureSpeed(int var1) {
      this.lureSpeed = ☃;
   }

   public void setLuck(int var1) {
      this.luck = ☃;
   }

   private void shoot() {
      float ☃ = this.angler.prevRotationPitch + (this.angler.rotationPitch - this.angler.prevRotationPitch);
      float ☃x = this.angler.prevRotationYaw + (this.angler.rotationYaw - this.angler.prevRotationYaw);
      float ☃xx = MathHelper.cos(-☃x * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃xxx = MathHelper.sin(-☃x * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃xxxx = -MathHelper.cos(-☃ * (float) (Math.PI / 180.0));
      float ☃xxxxx = MathHelper.sin(-☃ * (float) (Math.PI / 180.0));
      double ☃xxxxxx = this.angler.prevPosX + (this.angler.posX - this.angler.prevPosX) - ☃xxx * 0.3;
      double ☃xxxxxxx = this.angler.prevPosY + (this.angler.posY - this.angler.prevPosY) + this.angler.getEyeHeight();
      double ☃xxxxxxxx = this.angler.prevPosZ + (this.angler.posZ - this.angler.prevPosZ) - ☃xx * 0.3;
      this.setLocationAndAngles(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, ☃x, ☃);
      this.motionX = -☃xxx;
      this.motionY = MathHelper.clamp(-(☃xxxxx / ☃xxxx), -5.0F, 5.0F);
      this.motionZ = -☃xx;
      float ☃xxxxxxxxx = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
      this.motionX = this.motionX * (0.6 / ☃xxxxxxxxx + 0.5 + this.rand.nextGaussian() * 0.0045);
      this.motionY = this.motionY * (0.6 / ☃xxxxxxxxx + 0.5 + this.rand.nextGaussian() * 0.0045);
      this.motionZ = this.motionZ * (0.6 / ☃xxxxxxxxx + 0.5 + this.rand.nextGaussian() * 0.0045);
      float ☃xxxxxxxxxx = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0F / (float)Math.PI);
      this.rotationPitch = (float)(MathHelper.atan2(this.motionY, ☃xxxxxxxxxx) * 180.0F / (float)Math.PI);
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
   }

   @Override
   protected void entityInit() {
      this.getDataManager().register(DATA_HOOKED_ENTITY, 0);
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> var1) {
      if (DATA_HOOKED_ENTITY.equals(☃)) {
         int ☃ = this.getDataManager().get(DATA_HOOKED_ENTITY);
         this.caughtEntity = ☃ > 0 ? this.world.getEntityByID(☃ - 1) : null;
      }

      super.notifyDataManagerChange(☃);
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      double ☃ = 64.0;
      return ☃ < 4096.0;
   }

   @Override
   public void setPositionAndRotationDirect(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.angler == null) {
         this.setDead();
      } else if (this.world.isRemote || !this.shouldStopFishing()) {
         if (this.inGround) {
            this.ticksInGround++;
            if (this.ticksInGround >= 1200) {
               this.setDead();
               return;
            }
         }

         float ☃ = 0.0F;
         BlockPos ☃x = new BlockPos(this);
         IBlockState ☃xx = this.world.getBlockState(☃x);
         if (☃xx.getMaterial() == Material.WATER) {
            ☃ = BlockLiquid.getBlockLiquidHeight(☃xx, this.world, ☃x);
         }

         if (this.currentState == EntityFishHook.State.FLYING) {
            if (this.caughtEntity != null) {
               this.motionX = 0.0;
               this.motionY = 0.0;
               this.motionZ = 0.0;
               this.currentState = EntityFishHook.State.HOOKED_IN_ENTITY;
               return;
            }

            if (☃ > 0.0F) {
               this.motionX *= 0.3;
               this.motionY *= 0.2;
               this.motionZ *= 0.3;
               this.currentState = EntityFishHook.State.BOBBING;
               return;
            }

            if (!this.world.isRemote) {
               this.checkCollision();
            }

            if (!this.inGround && !this.onGround && !this.collidedHorizontally) {
               this.ticksInAir++;
            } else {
               this.ticksInAir = 0;
               this.motionX = 0.0;
               this.motionY = 0.0;
               this.motionZ = 0.0;
            }
         } else {
            if (this.currentState == EntityFishHook.State.HOOKED_IN_ENTITY) {
               if (this.caughtEntity != null) {
                  if (this.caughtEntity.isDead) {
                     this.caughtEntity = null;
                     this.currentState = EntityFishHook.State.FLYING;
                  } else {
                     this.posX = this.caughtEntity.posX;
                     double var10002 = this.caughtEntity.height;
                     this.posY = this.caughtEntity.getEntityBoundingBox().minY + var10002 * 0.8;
                     this.posZ = this.caughtEntity.posZ;
                     this.setPosition(this.posX, this.posY, this.posZ);
                  }
               }

               return;
            }

            if (this.currentState == EntityFishHook.State.BOBBING) {
               this.motionX *= 0.9;
               this.motionZ *= 0.9;
               double ☃xxx = this.posY + this.motionY - ☃x.getY() - ☃;
               if (Math.abs(☃xxx) < 0.01) {
                  ☃xxx += Math.signum(☃xxx) * 0.1;
               }

               this.motionY = this.motionY - ☃xxx * this.rand.nextFloat() * 0.2;
               if (!this.world.isRemote && ☃ > 0.0F) {
                  this.catchingFish(☃x);
               }
            }
         }

         if (☃xx.getMaterial() != Material.WATER) {
            this.motionY -= 0.03;
         }

         this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
         this.updateRotation();
         double ☃xxxx = 0.92;
         this.motionX *= 0.92;
         this.motionY *= 0.92;
         this.motionZ *= 0.92;
         this.setPosition(this.posX, this.posY, this.posZ);
      }
   }

   private boolean shouldStopFishing() {
      ItemStack ☃ = this.angler.getHeldItemMainhand();
      ItemStack ☃x = this.angler.getHeldItemOffhand();
      boolean ☃xx = ☃.getItem() == Items.FISHING_ROD;
      boolean ☃xxx = ☃x.getItem() == Items.FISHING_ROD;
      if (!this.angler.isDead && this.angler.isEntityAlive() && (☃xx || ☃xxx) && !(this.getDistanceSq(this.angler) > 1024.0)) {
         return false;
      } else {
         this.setDead();
         return true;
      }
   }

   private void updateRotation() {
      float ☃ = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0F / (float)Math.PI);
      this.rotationPitch = (float)(MathHelper.atan2(this.motionY, ☃) * 180.0F / (float)Math.PI);

      while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
         this.prevRotationPitch -= 360.0F;
      }

      while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
   }

   private void checkCollision() {
      Vec3d ☃ = new Vec3d(this.posX, this.posY, this.posZ);
      Vec3d ☃x = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      RayTraceResult ☃xx = this.world.rayTraceBlocks(☃, ☃x, false, true, false);
      ☃ = new Vec3d(this.posX, this.posY, this.posZ);
      ☃x = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      if (☃xx != null) {
         ☃x = new Vec3d(☃xx.hitVec.x, ☃xx.hitVec.y, ☃xx.hitVec.z);
      }

      Entity ☃xxx = null;
      List<Entity> ☃xxxx = this.world
         .getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0));
      double ☃xxxxx = 0.0;

      for (Entity ☃xxxxxx : ☃xxxx) {
         if (this.canBeHooked(☃xxxxxx) && (☃xxxxxx != this.angler || this.ticksInAir >= 5)) {
            AxisAlignedBB ☃xxxxxxx = ☃xxxxxx.getEntityBoundingBox().grow(0.3F);
            RayTraceResult ☃xxxxxxxx = ☃xxxxxxx.calculateIntercept(☃, ☃x);
            if (☃xxxxxxxx != null) {
               double ☃xxxxxxxxx = ☃.squareDistanceTo(☃xxxxxxxx.hitVec);
               if (☃xxxxxxxxx < ☃xxxxx || ☃xxxxx == 0.0) {
                  ☃xxx = ☃xxxxxx;
                  ☃xxxxx = ☃xxxxxxxxx;
               }
            }
         }
      }

      if (☃xxx != null) {
         ☃xx = new RayTraceResult(☃xxx);
      }

      if (☃xx != null && ☃xx.typeOfHit != RayTraceResult.Type.MISS) {
         if (☃xx.typeOfHit == RayTraceResult.Type.ENTITY) {
            this.caughtEntity = ☃xx.entityHit;
            this.setHookedEntity();
         } else {
            this.inGround = true;
         }
      }
   }

   private void setHookedEntity() {
      this.getDataManager().set(DATA_HOOKED_ENTITY, this.caughtEntity.getEntityId() + 1);
   }

   private void catchingFish(BlockPos var1) {
      WorldServer ☃ = (WorldServer)this.world;
      int ☃x = 1;
      BlockPos ☃xx = ☃.up();
      if (this.rand.nextFloat() < 0.25F && this.world.isRainingAt(☃xx)) {
         ☃x++;
      }

      if (this.rand.nextFloat() < 0.5F && !this.world.canSeeSky(☃xx)) {
         ☃x--;
      }

      if (this.ticksCatchable > 0) {
         this.ticksCatchable--;
         if (this.ticksCatchable <= 0) {
            this.ticksCaughtDelay = 0;
            this.ticksCatchableDelay = 0;
         } else {
            this.motionY = this.motionY - 0.2 * this.rand.nextFloat() * this.rand.nextFloat();
         }
      } else if (this.ticksCatchableDelay > 0) {
         this.ticksCatchableDelay -= ☃x;
         if (this.ticksCatchableDelay > 0) {
            this.fishApproachAngle = (float)(this.fishApproachAngle + this.rand.nextGaussian() * 4.0);
            float ☃xxx = this.fishApproachAngle * (float) (Math.PI / 180.0);
            float ☃xxxx = MathHelper.sin(☃xxx);
            float ☃xxxxx = MathHelper.cos(☃xxx);
            double ☃xxxxxx = this.posX + ☃xxxx * this.ticksCatchableDelay * 0.1F;
            double ☃xxxxxxx = MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F;
            double ☃xxxxxxxx = this.posZ + ☃xxxxx * this.ticksCatchableDelay * 0.1F;
            Block ☃xxxxxxxxx = ☃.getBlockState(new BlockPos(☃xxxxxx, ☃xxxxxxx - 1.0, ☃xxxxxxxx)).getBlock();
            if (☃xxxxxxxxx == Blocks.WATER || ☃xxxxxxxxx == Blocks.FLOWING_WATER) {
               if (this.rand.nextFloat() < 0.15F) {
                  ☃.spawnParticle(EnumParticleTypes.WATER_BUBBLE, ☃xxxxxx, ☃xxxxxxx - 0.1F, ☃xxxxxxxx, 1, ☃xxxx, 0.1, ☃xxxxx, 0.0);
               }

               float ☃xxxxxxxxxx = ☃xxxx * 0.04F;
               float ☃xxxxxxxxxxx = ☃xxxxx * 0.04F;
               ☃.spawnParticle(EnumParticleTypes.WATER_WAKE, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0, ☃xxxxxxxxxxx, 0.01, -☃xxxxxxxxxx, 1.0);
               ☃.spawnParticle(EnumParticleTypes.WATER_WAKE, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0, -☃xxxxxxxxxxx, 0.01, ☃xxxxxxxxxx, 1.0);
            }
         } else {
            this.motionY = -0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F);
            this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            double ☃xxx = this.getEntityBoundingBox().minY + 0.5;
            ☃.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, ☃xxx, this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0, this.width, 0.2F);
            ☃.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, ☃xxx, this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0, this.width, 0.2F);
            this.ticksCatchable = MathHelper.getInt(this.rand, 20, 40);
         }
      } else if (this.ticksCaughtDelay > 0) {
         this.ticksCaughtDelay -= ☃x;
         float ☃xxx = 0.15F;
         if (this.ticksCaughtDelay < 20) {
            ☃xxx = (float)(☃xxx + (20 - this.ticksCaughtDelay) * 0.05);
         } else if (this.ticksCaughtDelay < 40) {
            ☃xxx = (float)(☃xxx + (40 - this.ticksCaughtDelay) * 0.02);
         } else if (this.ticksCaughtDelay < 60) {
            ☃xxx = (float)(☃xxx + (60 - this.ticksCaughtDelay) * 0.01);
         }

         if (this.rand.nextFloat() < ☃xxx) {
            float ☃xxxx = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * (float) (Math.PI / 180.0);
            float ☃xxxxx = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
            double ☃xxxxxx = this.posX + MathHelper.sin(☃xxxx) * ☃xxxxx * 0.1F;
            double ☃xxxxxxx = MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F;
            double ☃xxxxxxxx = this.posZ + MathHelper.cos(☃xxxx) * ☃xxxxx * 0.1F;
            Block ☃xxxxxxxxx = ☃.getBlockState(new BlockPos((int)☃xxxxxx, (int)☃xxxxxxx - 1, (int)☃xxxxxxxx)).getBlock();
            if (☃xxxxxxxxx == Blocks.WATER || ☃xxxxxxxxx == Blocks.FLOWING_WATER) {
               ☃.spawnParticle(EnumParticleTypes.WATER_SPLASH, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 2 + this.rand.nextInt(2), 0.1F, 0.0, 0.1F, 0.0);
            }
         }

         if (this.ticksCaughtDelay <= 0) {
            this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0F, 360.0F);
            this.ticksCatchableDelay = MathHelper.getInt(this.rand, 20, 80);
         }
      } else {
         this.ticksCaughtDelay = MathHelper.getInt(this.rand, 100, 600);
         this.ticksCaughtDelay = this.ticksCaughtDelay - this.lureSpeed * 20 * 5;
      }
   }

   protected boolean canBeHooked(Entity var1) {
      return ☃.canBeCollidedWith() || ☃ instanceof EntityItem;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
   }

   public int handleHookRetraction() {
      if (!this.world.isRemote && this.angler != null) {
         int ☃ = 0;
         if (this.caughtEntity != null) {
            this.bringInHookedEntity();
            this.world.setEntityState(this, (byte)31);
            ☃ = this.caughtEntity instanceof EntityItem ? 3 : 5;
         } else if (this.ticksCatchable > 0) {
            LootContext.Builder ☃x = new LootContext.Builder((WorldServer)this.world);
            ☃x.withLuck(this.luck + this.angler.getLuck());

            for (ItemStack ☃xx : this.world
               .getLootTableManager()
               .getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING)
               .generateLootForPools(this.rand, ☃x.build())) {
               EntityItem ☃xxx = new EntityItem(this.world, this.posX, this.posY, this.posZ, ☃xx);
               double ☃xxxx = this.angler.posX - this.posX;
               double ☃xxxxx = this.angler.posY - this.posY;
               double ☃xxxxxx = this.angler.posZ - this.posZ;
               double ☃xxxxxxx = MathHelper.sqrt(☃xxxx * ☃xxxx + ☃xxxxx * ☃xxxxx + ☃xxxxxx * ☃xxxxxx);
               double ☃xxxxxxxx = 0.1;
               ☃xxx.motionX = ☃xxxx * 0.1;
               ☃xxx.motionY = ☃xxxxx * 0.1 + MathHelper.sqrt(☃xxxxxxx) * 0.08;
               ☃xxx.motionZ = ☃xxxxxx * 0.1;
               this.world.spawnEntity(☃xxx);
               this.angler
                  .world
                  .spawnEntity(new EntityXPOrb(this.angler.world, this.angler.posX, this.angler.posY + 0.5, this.angler.posZ + 0.5, this.rand.nextInt(6) + 1));
               Item ☃xxxxxxxxx = ☃xx.getItem();
               if (☃xxxxxxxxx == Items.FISH || ☃xxxxxxxxx == Items.COOKED_FISH) {
                  this.angler.addStat(StatList.FISH_CAUGHT, 1);
               }
            }

            ☃ = 1;
         }

         if (this.inGround) {
            ☃ = 2;
         }

         this.setDead();
         return ☃;
      } else {
         return 0;
      }
   }

   @Override
   public void handleStatusUpdate(byte var1) {
      if (☃ == 31 && this.world.isRemote && this.caughtEntity instanceof EntityPlayer && ((EntityPlayer)this.caughtEntity).isUser()) {
         this.bringInHookedEntity();
      }

      super.handleStatusUpdate(☃);
   }

   protected void bringInHookedEntity() {
      if (this.angler != null) {
         double ☃ = this.angler.posX - this.posX;
         double ☃x = this.angler.posY - this.posY;
         double ☃xx = this.angler.posZ - this.posZ;
         double ☃xxx = 0.1;
         this.caughtEntity.motionX += ☃ * 0.1;
         this.caughtEntity.motionY += ☃x * 0.1;
         this.caughtEntity.motionZ += ☃xx * 0.1;
      }
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   public void setDead() {
      super.setDead();
      if (this.angler != null) {
         this.angler.fishEntity = null;
      }
   }

   public EntityPlayer getAngler() {
      return this.angler;
   }

   static enum State {
      FLYING,
      HOOKED_IN_ENTITY,
      BOBBING;
   }
}
