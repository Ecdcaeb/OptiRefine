package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public abstract class MobSpawnerBaseLogic {
   private int spawnDelay = 20;
   private final List<WeightedSpawnerEntity> potentialSpawns = Lists.newArrayList();
   private WeightedSpawnerEntity spawnData = new WeightedSpawnerEntity();
   private double mobRotation;
   private double prevMobRotation;
   private int minSpawnDelay = 200;
   private int maxSpawnDelay = 800;
   private int spawnCount = 4;
   private Entity cachedEntity;
   private int maxNearbyEntities = 6;
   private int activatingRangeFromPlayer = 16;
   private int spawnRange = 4;

   @Nullable
   private ResourceLocation getEntityId() {
      String ☃ = this.spawnData.getNbt().getString("id");
      return StringUtils.isNullOrEmpty(☃) ? null : new ResourceLocation(☃);
   }

   public void setEntityId(@Nullable ResourceLocation var1) {
      if (☃ != null) {
         this.spawnData.getNbt().setString("id", ☃.toString());
      }
   }

   private boolean isActivated() {
      BlockPos ☃ = this.getSpawnerPosition();
      return this.getSpawnerWorld().isAnyPlayerWithinRangeAt(☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5, this.activatingRangeFromPlayer);
   }

   public void updateSpawner() {
      if (!this.isActivated()) {
         this.prevMobRotation = this.mobRotation;
      } else {
         BlockPos ☃ = this.getSpawnerPosition();
         if (this.getSpawnerWorld().isRemote) {
            double ☃x = ☃.getX() + this.getSpawnerWorld().rand.nextFloat();
            double ☃xx = ☃.getY() + this.getSpawnerWorld().rand.nextFloat();
            double ☃xxx = ☃.getZ() + this.getSpawnerWorld().rand.nextFloat();
            this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
            this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
            if (this.spawnDelay > 0) {
               this.spawnDelay--;
            }

            this.prevMobRotation = this.mobRotation;
            this.mobRotation = (this.mobRotation + 1000.0F / (this.spawnDelay + 200.0F)) % 360.0;
         } else {
            if (this.spawnDelay == -1) {
               this.resetTimer();
            }

            if (this.spawnDelay > 0) {
               this.spawnDelay--;
               return;
            }

            boolean ☃x = false;

            for (int ☃xx = 0; ☃xx < this.spawnCount; ☃xx++) {
               NBTTagCompound ☃xxx = this.spawnData.getNbt();
               NBTTagList ☃xxxx = ☃xxx.getTagList("Pos", 6);
               World ☃xxxxx = this.getSpawnerWorld();
               int ☃xxxxxx = ☃xxxx.tagCount();
               double ☃xxxxxxx = ☃xxxxxx >= 1 ? ☃xxxx.getDoubleAt(0) : ☃.getX() + (☃xxxxx.rand.nextDouble() - ☃xxxxx.rand.nextDouble()) * this.spawnRange + 0.5;
               double ☃xxxxxxxx = ☃xxxxxx >= 2 ? ☃xxxx.getDoubleAt(1) : ☃.getY() + ☃xxxxx.rand.nextInt(3) - 1;
               double ☃xxxxxxxxx = ☃xxxxxx >= 3
                  ? ☃xxxx.getDoubleAt(2)
                  : ☃.getZ() + (☃xxxxx.rand.nextDouble() - ☃xxxxx.rand.nextDouble()) * this.spawnRange + 0.5;
               Entity ☃xxxxxxxxxx = AnvilChunkLoader.readWorldEntityPos(☃xxx, ☃xxxxx, ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx, false);
               if (☃xxxxxxxxxx == null) {
                  return;
               }

               int ☃xxxxxxxxxxx = ☃xxxxx.getEntitiesWithinAABB(
                     ☃xxxxxxxxxx.getClass(), new AxisAlignedBB(☃.getX(), ☃.getY(), ☃.getZ(), ☃.getX() + 1, ☃.getY() + 1, ☃.getZ() + 1).grow(this.spawnRange)
                  )
                  .size();
               if (☃xxxxxxxxxxx >= this.maxNearbyEntities) {
                  this.resetTimer();
                  return;
               }

               EntityLiving ☃xxxxxxxxxxxx = ☃xxxxxxxxxx instanceof EntityLiving ? (EntityLiving)☃xxxxxxxxxx : null;
               ☃xxxxxxxxxx.setLocationAndAngles(☃xxxxxxxxxx.posX, ☃xxxxxxxxxx.posY, ☃xxxxxxxxxx.posZ, ☃xxxxx.rand.nextFloat() * 360.0F, 0.0F);
               if (☃xxxxxxxxxxxx == null || ☃xxxxxxxxxxxx.getCanSpawnHere() && ☃xxxxxxxxxxxx.isNotColliding()) {
                  if (this.spawnData.getNbt().getSize() == 1 && this.spawnData.getNbt().hasKey("id", 8) && ☃xxxxxxxxxx instanceof EntityLiving) {
                     ((EntityLiving)☃xxxxxxxxxx).onInitialSpawn(☃xxxxx.getDifficultyForLocation(new BlockPos(☃xxxxxxxxxx)), null);
                  }

                  AnvilChunkLoader.spawnEntity(☃xxxxxxxxxx, ☃xxxxx);
                  ☃xxxxx.playEvent(2004, ☃, 0);
                  if (☃xxxxxxxxxxxx != null) {
                     ☃xxxxxxxxxxxx.spawnExplosionParticle();
                  }

                  ☃x = true;
               }
            }

            if (☃x) {
               this.resetTimer();
            }
         }
      }
   }

   private void resetTimer() {
      if (this.maxSpawnDelay <= this.minSpawnDelay) {
         this.spawnDelay = this.minSpawnDelay;
      } else {
         this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
      }

      if (!this.potentialSpawns.isEmpty()) {
         this.setNextSpawnData(WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.potentialSpawns));
      }

      this.broadcastEvent(1);
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.spawnDelay = ☃.getShort("Delay");
      this.potentialSpawns.clear();
      if (☃.hasKey("SpawnPotentials", 9)) {
         NBTTagList ☃ = ☃.getTagList("SpawnPotentials", 10);

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            this.potentialSpawns.add(new WeightedSpawnerEntity(☃.getCompoundTagAt(☃x)));
         }
      }

      if (☃.hasKey("SpawnData", 10)) {
         this.setNextSpawnData(new WeightedSpawnerEntity(1, ☃.getCompoundTag("SpawnData")));
      } else if (!this.potentialSpawns.isEmpty()) {
         this.setNextSpawnData(WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.potentialSpawns));
      }

      if (☃.hasKey("MinSpawnDelay", 99)) {
         this.minSpawnDelay = ☃.getShort("MinSpawnDelay");
         this.maxSpawnDelay = ☃.getShort("MaxSpawnDelay");
         this.spawnCount = ☃.getShort("SpawnCount");
      }

      if (☃.hasKey("MaxNearbyEntities", 99)) {
         this.maxNearbyEntities = ☃.getShort("MaxNearbyEntities");
         this.activatingRangeFromPlayer = ☃.getShort("RequiredPlayerRange");
      }

      if (☃.hasKey("SpawnRange", 99)) {
         this.spawnRange = ☃.getShort("SpawnRange");
      }

      if (this.getSpawnerWorld() != null) {
         this.cachedEntity = null;
      }
   }

   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      ResourceLocation ☃ = this.getEntityId();
      if (☃ == null) {
         return ☃;
      } else {
         ☃.setShort("Delay", (short)this.spawnDelay);
         ☃.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
         ☃.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
         ☃.setShort("SpawnCount", (short)this.spawnCount);
         ☃.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
         ☃.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
         ☃.setShort("SpawnRange", (short)this.spawnRange);
         ☃.setTag("SpawnData", this.spawnData.getNbt().copy());
         NBTTagList ☃x = new NBTTagList();
         if (this.potentialSpawns.isEmpty()) {
            ☃x.appendTag(this.spawnData.toCompoundTag());
         } else {
            for (WeightedSpawnerEntity ☃xx : this.potentialSpawns) {
               ☃x.appendTag(☃xx.toCompoundTag());
            }
         }

         ☃.setTag("SpawnPotentials", ☃x);
         return ☃;
      }
   }

   public Entity getCachedEntity() {
      if (this.cachedEntity == null) {
         this.cachedEntity = AnvilChunkLoader.readWorldEntity(this.spawnData.getNbt(), this.getSpawnerWorld(), false);
         if (this.spawnData.getNbt().getSize() == 1 && this.spawnData.getNbt().hasKey("id", 8) && this.cachedEntity instanceof EntityLiving) {
            ((EntityLiving)this.cachedEntity).onInitialSpawn(this.getSpawnerWorld().getDifficultyForLocation(new BlockPos(this.cachedEntity)), null);
         }
      }

      return this.cachedEntity;
   }

   public boolean setDelayToMin(int var1) {
      if (☃ == 1 && this.getSpawnerWorld().isRemote) {
         this.spawnDelay = this.minSpawnDelay;
         return true;
      } else {
         return false;
      }
   }

   public void setNextSpawnData(WeightedSpawnerEntity var1) {
      this.spawnData = ☃;
   }

   public abstract void broadcastEvent(int var1);

   public abstract World getSpawnerWorld();

   public abstract BlockPos getSpawnerPosition();

   public double getMobRotation() {
      return this.mobRotation;
   }

   public double getPrevMobRotation() {
      return this.prevMobRotation;
   }
}
