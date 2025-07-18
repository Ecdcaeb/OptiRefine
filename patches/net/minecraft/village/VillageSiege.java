package net.minecraft.village;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;

public class VillageSiege {
   private final World world;
   private boolean hasSetupSiege;
   private int siegeState = -1;
   private int siegeCount;
   private int nextSpawnTime;
   private Village village;
   private int spawnX;
   private int spawnY;
   private int spawnZ;

   public VillageSiege(World var1) {
      this.world = ☃;
   }

   public void tick() {
      if (this.world.isDaytime()) {
         this.siegeState = 0;
      } else if (this.siegeState != 2) {
         if (this.siegeState == 0) {
            float ☃ = this.world.getCelestialAngle(0.0F);
            if (☃ < 0.5 || ☃ > 0.501) {
               return;
            }

            this.siegeState = this.world.rand.nextInt(10) == 0 ? 1 : 2;
            this.hasSetupSiege = false;
            if (this.siegeState == 2) {
               return;
            }
         }

         if (this.siegeState != -1) {
            if (!this.hasSetupSiege) {
               if (!this.trySetupSiege()) {
                  return;
               }

               this.hasSetupSiege = true;
            }

            if (this.nextSpawnTime > 0) {
               this.nextSpawnTime--;
            } else {
               this.nextSpawnTime = 2;
               if (this.siegeCount > 0) {
                  this.spawnZombie();
                  this.siegeCount--;
               } else {
                  this.siegeState = 2;
               }
            }
         }
      }
   }

   private boolean trySetupSiege() {
      for (EntityPlayer ☃ : this.world.playerEntities) {
         if (!☃.isSpectator()) {
            this.village = this.world.getVillageCollection().getNearestVillage(new BlockPos(☃), 1);
            if (this.village != null
               && this.village.getNumVillageDoors() >= 10
               && this.village.getTicksSinceLastDoorAdding() >= 20
               && this.village.getNumVillagers() >= 20) {
               BlockPos ☃x = this.village.getCenter();
               float ☃xx = this.village.getVillageRadius();
               boolean ☃xxx = false;

               for (int ☃xxxx = 0; ☃xxxx < 10; ☃xxxx++) {
                  float ☃xxxxx = this.world.rand.nextFloat() * (float) (Math.PI * 2);
                  this.spawnX = ☃x.getX() + (int)(MathHelper.cos(☃xxxxx) * ☃xx * 0.9);
                  this.spawnY = ☃x.getY();
                  this.spawnZ = ☃x.getZ() + (int)(MathHelper.sin(☃xxxxx) * ☃xx * 0.9);
                  ☃xxx = false;

                  for (Village ☃xxxxxx : this.world.getVillageCollection().getVillageList()) {
                     if (☃xxxxxx != this.village && ☃xxxxxx.isBlockPosWithinSqVillageRadius(new BlockPos(this.spawnX, this.spawnY, this.spawnZ))) {
                        ☃xxx = true;
                        break;
                     }
                  }

                  if (!☃xxx) {
                     break;
                  }
               }

               if (☃xxx) {
                  return false;
               }

               Vec3d ☃xxxx = this.findRandomSpawnPos(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
               if (☃xxxx != null) {
                  this.nextSpawnTime = 0;
                  this.siegeCount = 20;
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean spawnZombie() {
      Vec3d ☃ = this.findRandomSpawnPos(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
      if (☃ == null) {
         return false;
      } else {
         EntityZombie ☃x;
         try {
            ☃x = new EntityZombie(this.world);
            ☃x.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(☃x)), null);
         } catch (Exception var4) {
            var4.printStackTrace();
            return false;
         }

         ☃x.setLocationAndAngles(☃.x, ☃.y, ☃.z, this.world.rand.nextFloat() * 360.0F, 0.0F);
         this.world.spawnEntity(☃x);
         BlockPos ☃xx = this.village.getCenter();
         ☃x.setHomePosAndDistance(☃xx, this.village.getVillageRadius());
         return true;
      }
   }

   @Nullable
   private Vec3d findRandomSpawnPos(BlockPos var1) {
      for (int ☃ = 0; ☃ < 10; ☃++) {
         BlockPos ☃x = ☃.add(this.world.rand.nextInt(16) - 8, this.world.rand.nextInt(6) - 3, this.world.rand.nextInt(16) - 8);
         if (this.village.isBlockPosWithinSqVillageRadius(☃x)
            && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, this.world, ☃x)) {
            return new Vec3d(☃x.getX(), ☃x.getY(), ☃x.getZ());
         }
      }

      return null;
   }
}
