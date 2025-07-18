package net.minecraft.entity;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Entity trackedEntity;
   private final int range;
   private int maxRange;
   private final int updateFrequency;
   private long encodedPosX;
   private long encodedPosY;
   private long encodedPosZ;
   private int encodedRotationYaw;
   private int encodedRotationPitch;
   private int lastHeadMotion;
   private double lastTrackedEntityMotionX;
   private double lastTrackedEntityMotionY;
   private double motionZ;
   public int updateCounter;
   private double lastTrackedEntityPosX;
   private double lastTrackedEntityPosY;
   private double lastTrackedEntityPosZ;
   private boolean updatedPlayerVisibility;
   private final boolean sendVelocityUpdates;
   private int ticksSinceLastForcedTeleport;
   private List<Entity> passengers = Collections.emptyList();
   private boolean ridingEntity;
   private boolean onGround;
   public boolean playerEntitiesUpdated;
   private final Set<EntityPlayerMP> trackingPlayers = Sets.newHashSet();

   public EntityTrackerEntry(Entity var1, int var2, int var3, int var4, boolean var5) {
      this.trackedEntity = ☃;
      this.range = ☃;
      this.maxRange = ☃;
      this.updateFrequency = ☃;
      this.sendVelocityUpdates = ☃;
      this.encodedPosX = EntityTracker.getPositionLong(☃.posX);
      this.encodedPosY = EntityTracker.getPositionLong(☃.posY);
      this.encodedPosZ = EntityTracker.getPositionLong(☃.posZ);
      this.encodedRotationYaw = MathHelper.floor(☃.rotationYaw * 256.0F / 360.0F);
      this.encodedRotationPitch = MathHelper.floor(☃.rotationPitch * 256.0F / 360.0F);
      this.lastHeadMotion = MathHelper.floor(☃.getRotationYawHead() * 256.0F / 360.0F);
      this.onGround = ☃.onGround;
   }

   @Override
   public boolean equals(Object var1) {
      return ☃ instanceof EntityTrackerEntry ? ((EntityTrackerEntry)☃).trackedEntity.getEntityId() == this.trackedEntity.getEntityId() : false;
   }

   @Override
   public int hashCode() {
      return this.trackedEntity.getEntityId();
   }

   public void updatePlayerList(List<EntityPlayer> var1) {
      this.playerEntitiesUpdated = false;
      if (!this.updatedPlayerVisibility
         || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0) {
         this.lastTrackedEntityPosX = this.trackedEntity.posX;
         this.lastTrackedEntityPosY = this.trackedEntity.posY;
         this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
         this.updatedPlayerVisibility = true;
         this.playerEntitiesUpdated = true;
         this.updatePlayerEntities(☃);
      }

      List<Entity> ☃ = this.trackedEntity.getPassengers();
      if (!☃.equals(this.passengers)) {
         this.passengers = ☃;
         this.sendPacketToTrackedPlayers(new SPacketSetPassengers(this.trackedEntity));
      }

      if (this.trackedEntity instanceof EntityItemFrame && this.updateCounter % 10 == 0) {
         EntityItemFrame ☃x = (EntityItemFrame)this.trackedEntity;
         ItemStack ☃xx = ☃x.getDisplayedItem();
         if (☃xx.getItem() instanceof ItemMap) {
            MapData ☃xxx = Items.FILLED_MAP.getMapData(☃xx, this.trackedEntity.world);

            for (EntityPlayer ☃xxxx : ☃) {
               EntityPlayerMP ☃xxxxx = (EntityPlayerMP)☃xxxx;
               ☃xxx.updateVisiblePlayers(☃xxxxx, ☃xx);
               Packet<?> ☃xxxxxx = Items.FILLED_MAP.createMapDataPacket(☃xx, this.trackedEntity.world, ☃xxxxx);
               if (☃xxxxxx != null) {
                  ☃xxxxx.connection.sendPacket(☃xxxxxx);
               }
            }
         }

         this.sendMetadata();
      }

      if (this.updateCounter % this.updateFrequency == 0 || this.trackedEntity.isAirBorne || this.trackedEntity.getDataManager().isDirty()) {
         if (this.trackedEntity.isRiding()) {
            int ☃x = MathHelper.floor(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
            int ☃xx = MathHelper.floor(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
            boolean ☃xxx = Math.abs(☃x - this.encodedRotationYaw) >= 1 || Math.abs(☃xx - this.encodedRotationPitch) >= 1;
            if (☃xxx) {
               this.sendPacketToTrackedPlayers(
                  new SPacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)☃x, (byte)☃xx, this.trackedEntity.onGround)
               );
               this.encodedRotationYaw = ☃x;
               this.encodedRotationPitch = ☃xx;
            }

            this.encodedPosX = EntityTracker.getPositionLong(this.trackedEntity.posX);
            this.encodedPosY = EntityTracker.getPositionLong(this.trackedEntity.posY);
            this.encodedPosZ = EntityTracker.getPositionLong(this.trackedEntity.posZ);
            this.sendMetadata();
            this.ridingEntity = true;
         } else {
            this.ticksSinceLastForcedTeleport++;
            long ☃x = EntityTracker.getPositionLong(this.trackedEntity.posX);
            long ☃xx = EntityTracker.getPositionLong(this.trackedEntity.posY);
            long ☃xxx = EntityTracker.getPositionLong(this.trackedEntity.posZ);
            int ☃xxxxx = MathHelper.floor(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
            int ☃xxxxxx = MathHelper.floor(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
            long ☃xxxxxxx = ☃x - this.encodedPosX;
            long ☃xxxxxxxx = ☃xx - this.encodedPosY;
            long ☃xxxxxxxxx = ☃xxx - this.encodedPosZ;
            Packet<?> ☃xxxxxxxxxx = null;
            boolean ☃xxxxxxxxxxx = ☃xxxxxxx * ☃xxxxxxx + ☃xxxxxxxx * ☃xxxxxxxx + ☃xxxxxxxxx * ☃xxxxxxxxx >= 128L || this.updateCounter % 60 == 0;
            boolean ☃xxxxxxxxxxxx = Math.abs(☃xxxxx - this.encodedRotationYaw) >= 1 || Math.abs(☃xxxxxx - this.encodedRotationPitch) >= 1;
            if (this.updateCounter > 0 || this.trackedEntity instanceof EntityArrow) {
               if (☃xxxxxxx >= -32768L
                  && ☃xxxxxxx < 32768L
                  && ☃xxxxxxxx >= -32768L
                  && ☃xxxxxxxx < 32768L
                  && ☃xxxxxxxxx >= -32768L
                  && ☃xxxxxxxxx < 32768L
                  && this.ticksSinceLastForcedTeleport <= 400
                  && !this.ridingEntity
                  && this.onGround == this.trackedEntity.onGround) {
                  if ((!☃xxxxxxxxxxx || !☃xxxxxxxxxxxx) && !(this.trackedEntity instanceof EntityArrow)) {
                     if (☃xxxxxxxxxxx) {
                        ☃xxxxxxxxxx = new SPacketEntity.S15PacketEntityRelMove(
                           this.trackedEntity.getEntityId(), ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx, this.trackedEntity.onGround
                        );
                     } else if (☃xxxxxxxxxxxx) {
                        ☃xxxxxxxxxx = new SPacketEntity.S16PacketEntityLook(
                           this.trackedEntity.getEntityId(), (byte)☃xxxxx, (byte)☃xxxxxx, this.trackedEntity.onGround
                        );
                     }
                  } else {
                     ☃xxxxxxxxxx = new SPacketEntity.S17PacketEntityLookMove(
                        this.trackedEntity.getEntityId(), ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx, (byte)☃xxxxx, (byte)☃xxxxxx, this.trackedEntity.onGround
                     );
                  }
               } else {
                  this.onGround = this.trackedEntity.onGround;
                  this.ticksSinceLastForcedTeleport = 0;
                  this.resetPlayerVisibility();
                  ☃xxxxxxxxxx = new SPacketEntityTeleport(this.trackedEntity);
               }
            }

            boolean ☃xxxxxxxxxxxxx = this.sendVelocityUpdates;
            if (this.trackedEntity instanceof EntityLivingBase && ((EntityLivingBase)this.trackedEntity).isElytraFlying()) {
               ☃xxxxxxxxxxxxx = true;
            }

            if (☃xxxxxxxxxxxxx && this.updateCounter > 0) {
               double ☃xxxxxxxxxxxxxx = this.trackedEntity.motionX - this.lastTrackedEntityMotionX;
               double ☃xxxxxxxxxxxxxxx = this.trackedEntity.motionY - this.lastTrackedEntityMotionY;
               double ☃xxxxxxxxxxxxxxxx = this.trackedEntity.motionZ - this.motionZ;
               double ☃xxxxxxxxxxxxxxxxx = 0.02;
               double ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxx;
               if (☃xxxxxxxxxxxxxxxxxx > 4.0E-4
                  || ☃xxxxxxxxxxxxxxxxxx > 0.0 && this.trackedEntity.motionX == 0.0 && this.trackedEntity.motionY == 0.0 && this.trackedEntity.motionZ == 0.0) {
                  this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                  this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                  this.motionZ = this.trackedEntity.motionZ;
                  this.sendPacketToTrackedPlayers(
                     new SPacketEntityVelocity(this.trackedEntity.getEntityId(), this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ)
                  );
               }
            }

            if (☃xxxxxxxxxx != null) {
               this.sendPacketToTrackedPlayers(☃xxxxxxxxxx);
            }

            this.sendMetadata();
            if (☃xxxxxxxxxxx) {
               this.encodedPosX = ☃x;
               this.encodedPosY = ☃xx;
               this.encodedPosZ = ☃xxx;
            }

            if (☃xxxxxxxxxxxx) {
               this.encodedRotationYaw = ☃xxxxx;
               this.encodedRotationPitch = ☃xxxxxx;
            }

            this.ridingEntity = false;
         }

         int ☃xxxxxxxxxxxxxx = MathHelper.floor(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
         if (Math.abs(☃xxxxxxxxxxxxxx - this.lastHeadMotion) >= 1) {
            this.sendPacketToTrackedPlayers(new SPacketEntityHeadLook(this.trackedEntity, (byte)☃xxxxxxxxxxxxxx));
            this.lastHeadMotion = ☃xxxxxxxxxxxxxx;
         }

         this.trackedEntity.isAirBorne = false;
      }

      this.updateCounter++;
      if (this.trackedEntity.velocityChanged) {
         this.sendToTrackingAndSelf(new SPacketEntityVelocity(this.trackedEntity));
         this.trackedEntity.velocityChanged = false;
      }
   }

   private void sendMetadata() {
      EntityDataManager ☃ = this.trackedEntity.getDataManager();
      if (☃.isDirty()) {
         this.sendToTrackingAndSelf(new SPacketEntityMetadata(this.trackedEntity.getEntityId(), ☃, false));
      }

      if (this.trackedEntity instanceof EntityLivingBase) {
         AttributeMap ☃x = (AttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
         Set<IAttributeInstance> ☃xx = ☃x.getDirtyInstances();
         if (!☃xx.isEmpty()) {
            this.sendToTrackingAndSelf(new SPacketEntityProperties(this.trackedEntity.getEntityId(), ☃xx));
         }

         ☃xx.clear();
      }
   }

   public void sendPacketToTrackedPlayers(Packet<?> var1) {
      for (EntityPlayerMP ☃ : this.trackingPlayers) {
         ☃.connection.sendPacket(☃);
      }
   }

   public void sendToTrackingAndSelf(Packet<?> var1) {
      this.sendPacketToTrackedPlayers(☃);
      if (this.trackedEntity instanceof EntityPlayerMP) {
         ((EntityPlayerMP)this.trackedEntity).connection.sendPacket(☃);
      }
   }

   public void sendDestroyEntityPacketToTrackedPlayers() {
      for (EntityPlayerMP ☃ : this.trackingPlayers) {
         this.trackedEntity.removeTrackingPlayer(☃);
         ☃.removeEntity(this.trackedEntity);
      }
   }

   public void removeFromTrackedPlayers(EntityPlayerMP var1) {
      if (this.trackingPlayers.contains(☃)) {
         this.trackedEntity.removeTrackingPlayer(☃);
         ☃.removeEntity(this.trackedEntity);
         this.trackingPlayers.remove(☃);
      }
   }

   public void updatePlayerEntity(EntityPlayerMP var1) {
      if (☃ != this.trackedEntity) {
         if (this.isVisibleTo(☃)) {
            if (!this.trackingPlayers.contains(☃) && (this.isPlayerWatchingThisChunk(☃) || this.trackedEntity.forceSpawn)) {
               this.trackingPlayers.add(☃);
               Packet<?> ☃ = this.createSpawnPacket();
               ☃.connection.sendPacket(☃);
               if (!this.trackedEntity.getDataManager().isEmpty()) {
                  ☃.connection.sendPacket(new SPacketEntityMetadata(this.trackedEntity.getEntityId(), this.trackedEntity.getDataManager(), true));
               }

               boolean ☃x = this.sendVelocityUpdates;
               if (this.trackedEntity instanceof EntityLivingBase) {
                  AttributeMap ☃xx = (AttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
                  Collection<IAttributeInstance> ☃xxx = ☃xx.getWatchedAttributes();
                  if (!☃xxx.isEmpty()) {
                     ☃.connection.sendPacket(new SPacketEntityProperties(this.trackedEntity.getEntityId(), ☃xxx));
                  }

                  if (((EntityLivingBase)this.trackedEntity).isElytraFlying()) {
                     ☃x = true;
                  }
               }

               this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
               this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
               this.motionZ = this.trackedEntity.motionZ;
               if (☃x && !(☃ instanceof SPacketSpawnMob)) {
                  ☃.connection
                     .sendPacket(
                        new SPacketEntityVelocity(
                           this.trackedEntity.getEntityId(), this.trackedEntity.motionX, this.trackedEntity.motionY, this.trackedEntity.motionZ
                        )
                     );
               }

               if (this.trackedEntity instanceof EntityLivingBase) {
                  for (EntityEquipmentSlot ☃xxxx : EntityEquipmentSlot.values()) {
                     ItemStack ☃xxxxx = ((EntityLivingBase)this.trackedEntity).getItemStackFromSlot(☃xxxx);
                     if (!☃xxxxx.isEmpty()) {
                        ☃.connection.sendPacket(new SPacketEntityEquipment(this.trackedEntity.getEntityId(), ☃xxxx, ☃xxxxx));
                     }
                  }
               }

               if (this.trackedEntity instanceof EntityPlayer) {
                  EntityPlayer ☃xxxxx = (EntityPlayer)this.trackedEntity;
                  if (☃xxxxx.isPlayerSleeping()) {
                     ☃.connection.sendPacket(new SPacketUseBed(☃xxxxx, new BlockPos(this.trackedEntity)));
                  }
               }

               if (this.trackedEntity instanceof EntityLivingBase) {
                  EntityLivingBase ☃xxxxx = (EntityLivingBase)this.trackedEntity;

                  for (PotionEffect ☃xxxxxx : ☃xxxxx.getActivePotionEffects()) {
                     ☃.connection.sendPacket(new SPacketEntityEffect(this.trackedEntity.getEntityId(), ☃xxxxxx));
                  }
               }

               if (!this.trackedEntity.getPassengers().isEmpty()) {
                  ☃.connection.sendPacket(new SPacketSetPassengers(this.trackedEntity));
               }

               if (this.trackedEntity.isRiding()) {
                  ☃.connection.sendPacket(new SPacketSetPassengers(this.trackedEntity.getRidingEntity()));
               }

               this.trackedEntity.addTrackingPlayer(☃);
               ☃.addEntity(this.trackedEntity);
            }
         } else if (this.trackingPlayers.contains(☃)) {
            this.trackingPlayers.remove(☃);
            this.trackedEntity.removeTrackingPlayer(☃);
            ☃.removeEntity(this.trackedEntity);
         }
      }
   }

   public boolean isVisibleTo(EntityPlayerMP var1) {
      double ☃ = ☃.posX - this.encodedPosX / 4096.0;
      double ☃x = ☃.posZ - this.encodedPosZ / 4096.0;
      int ☃xx = Math.min(this.range, this.maxRange);
      return ☃ >= -☃xx && ☃ <= ☃xx && ☃x >= -☃xx && ☃x <= ☃xx && this.trackedEntity.isSpectatedByPlayer(☃);
   }

   private boolean isPlayerWatchingThisChunk(EntityPlayerMP var1) {
      return ☃.getServerWorld().getPlayerChunkMap().isPlayerWatchingChunk(☃, this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
   }

   public void updatePlayerEntities(List<EntityPlayer> var1) {
      for (int ☃ = 0; ☃ < ☃.size(); ☃++) {
         this.updatePlayerEntity((EntityPlayerMP)☃.get(☃));
      }
   }

   private Packet<?> createSpawnPacket() {
      if (this.trackedEntity.isDead) {
         LOGGER.warn("Fetching addPacket for removed entity");
      }

      if (this.trackedEntity instanceof EntityPlayerMP) {
         return new SPacketSpawnPlayer((EntityPlayer)this.trackedEntity);
      } else if (this.trackedEntity instanceof IAnimals) {
         this.lastHeadMotion = MathHelper.floor(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
         return new SPacketSpawnMob((EntityLivingBase)this.trackedEntity);
      } else if (this.trackedEntity instanceof EntityPainting) {
         return new SPacketSpawnPainting((EntityPainting)this.trackedEntity);
      } else if (this.trackedEntity instanceof EntityItem) {
         return new SPacketSpawnObject(this.trackedEntity, 2, 1);
      } else if (this.trackedEntity instanceof EntityMinecart) {
         EntityMinecart ☃ = (EntityMinecart)this.trackedEntity;
         return new SPacketSpawnObject(this.trackedEntity, 10, ☃.getType().getId());
      } else if (this.trackedEntity instanceof EntityBoat) {
         return new SPacketSpawnObject(this.trackedEntity, 1);
      } else if (this.trackedEntity instanceof EntityXPOrb) {
         return new SPacketSpawnExperienceOrb((EntityXPOrb)this.trackedEntity);
      } else if (this.trackedEntity instanceof EntityFishHook) {
         Entity ☃ = ((EntityFishHook)this.trackedEntity).getAngler();
         return new SPacketSpawnObject(this.trackedEntity, 90, ☃ == null ? this.trackedEntity.getEntityId() : ☃.getEntityId());
      } else if (this.trackedEntity instanceof EntitySpectralArrow) {
         Entity ☃ = ((EntitySpectralArrow)this.trackedEntity).shootingEntity;
         return new SPacketSpawnObject(this.trackedEntity, 91, 1 + (☃ == null ? this.trackedEntity.getEntityId() : ☃.getEntityId()));
      } else if (this.trackedEntity instanceof EntityTippedArrow) {
         Entity ☃ = ((EntityArrow)this.trackedEntity).shootingEntity;
         return new SPacketSpawnObject(this.trackedEntity, 60, 1 + (☃ == null ? this.trackedEntity.getEntityId() : ☃.getEntityId()));
      } else if (this.trackedEntity instanceof EntitySnowball) {
         return new SPacketSpawnObject(this.trackedEntity, 61);
      } else if (this.trackedEntity instanceof EntityLlamaSpit) {
         return new SPacketSpawnObject(this.trackedEntity, 68);
      } else if (this.trackedEntity instanceof EntityPotion) {
         return new SPacketSpawnObject(this.trackedEntity, 73);
      } else if (this.trackedEntity instanceof EntityExpBottle) {
         return new SPacketSpawnObject(this.trackedEntity, 75);
      } else if (this.trackedEntity instanceof EntityEnderPearl) {
         return new SPacketSpawnObject(this.trackedEntity, 65);
      } else if (this.trackedEntity instanceof EntityEnderEye) {
         return new SPacketSpawnObject(this.trackedEntity, 72);
      } else if (this.trackedEntity instanceof EntityFireworkRocket) {
         return new SPacketSpawnObject(this.trackedEntity, 76);
      } else if (this.trackedEntity instanceof EntityFireball) {
         EntityFireball ☃ = (EntityFireball)this.trackedEntity;
         SPacketSpawnObject ☃x = null;
         int ☃xx = 63;
         if (this.trackedEntity instanceof EntitySmallFireball) {
            ☃xx = 64;
         } else if (this.trackedEntity instanceof EntityDragonFireball) {
            ☃xx = 93;
         } else if (this.trackedEntity instanceof EntityWitherSkull) {
            ☃xx = 66;
         }

         if (☃.shootingEntity != null) {
            ☃x = new SPacketSpawnObject(this.trackedEntity, ☃xx, ((EntityFireball)this.trackedEntity).shootingEntity.getEntityId());
         } else {
            ☃x = new SPacketSpawnObject(this.trackedEntity, ☃xx, 0);
         }

         ☃x.setSpeedX((int)(☃.accelerationX * 8000.0));
         ☃x.setSpeedY((int)(☃.accelerationY * 8000.0));
         ☃x.setSpeedZ((int)(☃.accelerationZ * 8000.0));
         return ☃x;
      } else if (this.trackedEntity instanceof EntityShulkerBullet) {
         SPacketSpawnObject ☃xxx = new SPacketSpawnObject(this.trackedEntity, 67, 0);
         ☃xxx.setSpeedX((int)(this.trackedEntity.motionX * 8000.0));
         ☃xxx.setSpeedY((int)(this.trackedEntity.motionY * 8000.0));
         ☃xxx.setSpeedZ((int)(this.trackedEntity.motionZ * 8000.0));
         return ☃xxx;
      } else if (this.trackedEntity instanceof EntityEgg) {
         return new SPacketSpawnObject(this.trackedEntity, 62);
      } else if (this.trackedEntity instanceof EntityEvokerFangs) {
         return new SPacketSpawnObject(this.trackedEntity, 79);
      } else if (this.trackedEntity instanceof EntityTNTPrimed) {
         return new SPacketSpawnObject(this.trackedEntity, 50);
      } else if (this.trackedEntity instanceof EntityEnderCrystal) {
         return new SPacketSpawnObject(this.trackedEntity, 51);
      } else if (this.trackedEntity instanceof EntityFallingBlock) {
         EntityFallingBlock ☃xxx = (EntityFallingBlock)this.trackedEntity;
         return new SPacketSpawnObject(this.trackedEntity, 70, Block.getStateId(☃xxx.getBlock()));
      } else if (this.trackedEntity instanceof EntityArmorStand) {
         return new SPacketSpawnObject(this.trackedEntity, 78);
      } else if (this.trackedEntity instanceof EntityItemFrame) {
         EntityItemFrame ☃xxx = (EntityItemFrame)this.trackedEntity;
         return new SPacketSpawnObject(this.trackedEntity, 71, ☃xxx.facingDirection.getHorizontalIndex(), ☃xxx.getHangingPosition());
      } else if (this.trackedEntity instanceof EntityLeashKnot) {
         EntityLeashKnot ☃xxx = (EntityLeashKnot)this.trackedEntity;
         return new SPacketSpawnObject(this.trackedEntity, 77, 0, ☃xxx.getHangingPosition());
      } else if (this.trackedEntity instanceof EntityAreaEffectCloud) {
         return new SPacketSpawnObject(this.trackedEntity, 3);
      } else {
         throw new IllegalArgumentException("Don't know how to add " + this.trackedEntity.getClass() + "!");
      }
   }

   public void removeTrackedPlayerSymmetric(EntityPlayerMP var1) {
      if (this.trackingPlayers.contains(☃)) {
         this.trackingPlayers.remove(☃);
         this.trackedEntity.removeTrackingPlayer(☃);
         ☃.removeEntity(this.trackedEntity);
      }
   }

   public Entity getTrackedEntity() {
      return this.trackedEntity;
   }

   public void setMaxRange(int var1) {
      this.maxRange = ☃;
   }

   public void resetPlayerVisibility() {
      this.updatedPlayerVisibility = false;
   }
}
