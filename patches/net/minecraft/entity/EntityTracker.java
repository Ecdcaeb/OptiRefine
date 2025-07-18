package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTracker {
   private static final Logger LOGGER = LogManager.getLogger();
   private final WorldServer world;
   private final Set<EntityTrackerEntry> entries = Sets.newHashSet();
   private final IntHashMap<EntityTrackerEntry> trackedEntityHashTable = new IntHashMap<>();
   private int maxTrackingDistanceThreshold;

   public EntityTracker(WorldServer var1) {
      this.world = ☃;
      this.maxTrackingDistanceThreshold = ☃.getMinecraftServer().getPlayerList().getEntityViewDistance();
   }

   public static long getPositionLong(double var0) {
      return MathHelper.lfloor(☃ * 4096.0);
   }

   public static void updateServerPosition(Entity var0, double var1, double var3, double var5) {
      ☃.serverPosX = getPositionLong(☃);
      ☃.serverPosY = getPositionLong(☃);
      ☃.serverPosZ = getPositionLong(☃);
   }

   public void track(Entity var1) {
      if (☃ instanceof EntityPlayerMP) {
         this.track(☃, 512, 2);
         EntityPlayerMP ☃ = (EntityPlayerMP)☃;

         for (EntityTrackerEntry ☃x : this.entries) {
            if (☃x.getTrackedEntity() != ☃) {
               ☃x.updatePlayerEntity(☃);
            }
         }
      } else if (☃ instanceof EntityFishHook) {
         this.track(☃, 64, 5, true);
      } else if (☃ instanceof EntityArrow) {
         this.track(☃, 64, 20, false);
      } else if (☃ instanceof EntitySmallFireball) {
         this.track(☃, 64, 10, false);
      } else if (☃ instanceof EntityFireball) {
         this.track(☃, 64, 10, true);
      } else if (☃ instanceof EntitySnowball) {
         this.track(☃, 64, 10, true);
      } else if (☃ instanceof EntityLlamaSpit) {
         this.track(☃, 64, 10, false);
      } else if (☃ instanceof EntityEnderPearl) {
         this.track(☃, 64, 10, true);
      } else if (☃ instanceof EntityEnderEye) {
         this.track(☃, 64, 4, true);
      } else if (☃ instanceof EntityEgg) {
         this.track(☃, 64, 10, true);
      } else if (☃ instanceof EntityPotion) {
         this.track(☃, 64, 10, true);
      } else if (☃ instanceof EntityExpBottle) {
         this.track(☃, 64, 10, true);
      } else if (☃ instanceof EntityFireworkRocket) {
         this.track(☃, 64, 10, true);
      } else if (☃ instanceof EntityItem) {
         this.track(☃, 64, 20, true);
      } else if (☃ instanceof EntityMinecart) {
         this.track(☃, 80, 3, true);
      } else if (☃ instanceof EntityBoat) {
         this.track(☃, 80, 3, true);
      } else if (☃ instanceof EntitySquid) {
         this.track(☃, 64, 3, true);
      } else if (☃ instanceof EntityWither) {
         this.track(☃, 80, 3, false);
      } else if (☃ instanceof EntityShulkerBullet) {
         this.track(☃, 80, 3, true);
      } else if (☃ instanceof EntityBat) {
         this.track(☃, 80, 3, false);
      } else if (☃ instanceof EntityDragon) {
         this.track(☃, 160, 3, true);
      } else if (☃ instanceof IAnimals) {
         this.track(☃, 80, 3, true);
      } else if (☃ instanceof EntityTNTPrimed) {
         this.track(☃, 160, 10, true);
      } else if (☃ instanceof EntityFallingBlock) {
         this.track(☃, 160, 20, true);
      } else if (☃ instanceof EntityHanging) {
         this.track(☃, 160, Integer.MAX_VALUE, false);
      } else if (☃ instanceof EntityArmorStand) {
         this.track(☃, 160, 3, true);
      } else if (☃ instanceof EntityXPOrb) {
         this.track(☃, 160, 20, true);
      } else if (☃ instanceof EntityAreaEffectCloud) {
         this.track(☃, 160, Integer.MAX_VALUE, true);
      } else if (☃ instanceof EntityEnderCrystal) {
         this.track(☃, 256, Integer.MAX_VALUE, false);
      } else if (☃ instanceof EntityEvokerFangs) {
         this.track(☃, 160, 2, false);
      }
   }

   public void track(Entity var1, int var2, int var3) {
      this.track(☃, ☃, ☃, false);
   }

   public void track(Entity var1, int var2, final int var3, boolean var4) {
      try {
         if (this.trackedEntityHashTable.containsItem(☃.getEntityId())) {
            throw new IllegalStateException("Entity is already tracked!");
         }

         EntityTrackerEntry ☃ = new EntityTrackerEntry(☃, ☃, this.maxTrackingDistanceThreshold, ☃, ☃);
         this.entries.add(☃);
         this.trackedEntityHashTable.addKey(☃.getEntityId(), ☃);
         ☃.updatePlayerEntities(this.world.playerEntities);
      } catch (Throwable var10) {
         CrashReport ☃ = CrashReport.makeCrashReport(var10, "Adding entity to track");
         CrashReportCategory ☃x = ☃.makeCategory("Entity To Track");
         ☃x.addCrashSection("Tracking range", ☃ + " blocks");
         ☃x.addDetail("Update interval", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               String ☃xx = "Once per " + ☃ + " ticks";
               if (☃ == Integer.MAX_VALUE) {
                  ☃xx = "Maximum (" + ☃xx + ")";
               }

               return ☃xx;
            }
         });
         ☃.addEntityCrashInfo(☃x);
         this.trackedEntityHashTable.lookup(☃.getEntityId()).getTrackedEntity().addEntityCrashInfo(☃.makeCategory("Entity That Is Already Tracked"));

         try {
            throw new ReportedException(☃);
         } catch (ReportedException var9) {
            LOGGER.error("\"Silently\" catching entity tracking error.", var9);
         }
      }
   }

   public void untrack(Entity var1) {
      if (☃ instanceof EntityPlayerMP) {
         EntityPlayerMP ☃ = (EntityPlayerMP)☃;

         for (EntityTrackerEntry ☃x : this.entries) {
            ☃x.removeFromTrackedPlayers(☃);
         }
      }

      EntityTrackerEntry ☃ = this.trackedEntityHashTable.removeObject(☃.getEntityId());
      if (☃ != null) {
         this.entries.remove(☃);
         ☃.sendDestroyEntityPacketToTrackedPlayers();
      }
   }

   public void tick() {
      List<EntityPlayerMP> ☃ = Lists.newArrayList();

      for (EntityTrackerEntry ☃x : this.entries) {
         ☃x.updatePlayerList(this.world.playerEntities);
         if (☃x.playerEntitiesUpdated) {
            Entity ☃xx = ☃x.getTrackedEntity();
            if (☃xx instanceof EntityPlayerMP) {
               ☃.add((EntityPlayerMP)☃xx);
            }
         }
      }

      for (int ☃xx = 0; ☃xx < ☃.size(); ☃xx++) {
         EntityPlayerMP ☃xxx = ☃.get(☃xx);

         for (EntityTrackerEntry ☃xxxx : this.entries) {
            if (☃xxxx.getTrackedEntity() != ☃xxx) {
               ☃xxxx.updatePlayerEntity(☃xxx);
            }
         }
      }
   }

   public void updateVisibility(EntityPlayerMP var1) {
      for (EntityTrackerEntry ☃ : this.entries) {
         if (☃.getTrackedEntity() == ☃) {
            ☃.updatePlayerEntities(this.world.playerEntities);
         } else {
            ☃.updatePlayerEntity(☃);
         }
      }
   }

   public void sendToTracking(Entity var1, Packet<?> var2) {
      EntityTrackerEntry ☃ = this.trackedEntityHashTable.lookup(☃.getEntityId());
      if (☃ != null) {
         ☃.sendPacketToTrackedPlayers(☃);
      }
   }

   public void sendToTrackingAndSelf(Entity var1, Packet<?> var2) {
      EntityTrackerEntry ☃ = this.trackedEntityHashTable.lookup(☃.getEntityId());
      if (☃ != null) {
         ☃.sendToTrackingAndSelf(☃);
      }
   }

   public void removePlayerFromTrackers(EntityPlayerMP var1) {
      for (EntityTrackerEntry ☃ : this.entries) {
         ☃.removeTrackedPlayerSymmetric(☃);
      }
   }

   public void sendLeashedEntitiesInChunk(EntityPlayerMP var1, Chunk var2) {
      List<Entity> ☃ = Lists.newArrayList();
      List<Entity> ☃x = Lists.newArrayList();

      for (EntityTrackerEntry ☃xx : this.entries) {
         Entity ☃xxx = ☃xx.getTrackedEntity();
         if (☃xxx != ☃ && ☃xxx.chunkCoordX == ☃.x && ☃xxx.chunkCoordZ == ☃.z) {
            ☃xx.updatePlayerEntity(☃);
            if (☃xxx instanceof EntityLiving && ((EntityLiving)☃xxx).getLeashHolder() != null) {
               ☃.add(☃xxx);
            }

            if (!☃xxx.getPassengers().isEmpty()) {
               ☃x.add(☃xxx);
            }
         }
      }

      if (!☃.isEmpty()) {
         for (Entity ☃xxx : ☃) {
            ☃.connection.sendPacket(new SPacketEntityAttach(☃xxx, ((EntityLiving)☃xxx).getLeashHolder()));
         }
      }

      if (!☃x.isEmpty()) {
         for (Entity ☃xxx : ☃x) {
            ☃.connection.sendPacket(new SPacketSetPassengers(☃xxx));
         }
      }
   }

   public void setViewDistance(int var1) {
      this.maxTrackingDistanceThreshold = (☃ - 1) * 16;

      for (EntityTrackerEntry ☃ : this.entries) {
         ☃.setMaxRange(this.maxTrackingDistanceThreshold);
      }
   }
}
