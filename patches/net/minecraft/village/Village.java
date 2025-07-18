package net.minecraft.village;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Village {
   private World world;
   private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList();
   private BlockPos centerHelper = BlockPos.ORIGIN;
   private BlockPos center = BlockPos.ORIGIN;
   private int villageRadius;
   private int lastAddDoorTimestamp;
   private int tickCounter;
   private int numVillagers;
   private int noBreedTicks;
   private final Map<String, Integer> playerReputation = Maps.newHashMap();
   private final List<Village.VillageAggressor> villageAgressors = Lists.newArrayList();
   private int numIronGolems;

   public Village() {
   }

   public Village(World var1) {
      this.world = ☃;
   }

   public void setWorld(World var1) {
      this.world = ☃;
   }

   public void tick(int var1) {
      this.tickCounter = ☃;
      this.removeDeadAndOutOfRangeDoors();
      this.removeDeadAndOldAgressors();
      if (☃ % 20 == 0) {
         this.updateNumVillagers();
      }

      if (☃ % 30 == 0) {
         this.updateNumIronGolems();
      }

      int ☃ = this.numVillagers / 10;
      if (this.numIronGolems < ☃ && this.villageDoorInfoList.size() > 20 && this.world.rand.nextInt(7000) == 0) {
         Vec3d ☃x = this.findRandomSpawnPos(this.center, 2, 4, 2);
         if (☃x != null) {
            EntityIronGolem ☃xx = new EntityIronGolem(this.world);
            ☃xx.setPosition(☃x.x, ☃x.y, ☃x.z);
            this.world.spawnEntity(☃xx);
            this.numIronGolems++;
         }
      }
   }

   private Vec3d findRandomSpawnPos(BlockPos var1, int var2, int var3, int var4) {
      for (int ☃ = 0; ☃ < 10; ☃++) {
         BlockPos ☃x = ☃.add(this.world.rand.nextInt(16) - 8, this.world.rand.nextInt(6) - 3, this.world.rand.nextInt(16) - 8);
         if (this.isBlockPosWithinSqVillageRadius(☃x) && this.isAreaClearAround(new BlockPos(☃, ☃, ☃), ☃x)) {
            return new Vec3d(☃x.getX(), ☃x.getY(), ☃x.getZ());
         }
      }

      return null;
   }

   private boolean isAreaClearAround(BlockPos var1, BlockPos var2) {
      if (!this.world.getBlockState(☃.down()).isTopSolid()) {
         return false;
      } else {
         int ☃ = ☃.getX() - ☃.getX() / 2;
         int ☃x = ☃.getZ() - ☃.getZ() / 2;

         for (int ☃xx = ☃; ☃xx < ☃ + ☃.getX(); ☃xx++) {
            for (int ☃xxx = ☃.getY(); ☃xxx < ☃.getY() + ☃.getY(); ☃xxx++) {
               for (int ☃xxxx = ☃x; ☃xxxx < ☃x + ☃.getZ(); ☃xxxx++) {
                  if (this.world.getBlockState(new BlockPos(☃xx, ☃xxx, ☃xxxx)).isNormalCube()) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private void updateNumIronGolems() {
      List<EntityIronGolem> ☃ = this.world
         .getEntitiesWithinAABB(
            EntityIronGolem.class,
            new AxisAlignedBB(
               this.center.getX() - this.villageRadius,
               this.center.getY() - 4,
               this.center.getZ() - this.villageRadius,
               this.center.getX() + this.villageRadius,
               this.center.getY() + 4,
               this.center.getZ() + this.villageRadius
            )
         );
      this.numIronGolems = ☃.size();
   }

   private void updateNumVillagers() {
      List<EntityVillager> ☃ = this.world
         .getEntitiesWithinAABB(
            EntityVillager.class,
            new AxisAlignedBB(
               this.center.getX() - this.villageRadius,
               this.center.getY() - 4,
               this.center.getZ() - this.villageRadius,
               this.center.getX() + this.villageRadius,
               this.center.getY() + 4,
               this.center.getZ() + this.villageRadius
            )
         );
      this.numVillagers = ☃.size();
      if (this.numVillagers == 0) {
         this.playerReputation.clear();
      }
   }

   public BlockPos getCenter() {
      return this.center;
   }

   public int getVillageRadius() {
      return this.villageRadius;
   }

   public int getNumVillageDoors() {
      return this.villageDoorInfoList.size();
   }

   public int getTicksSinceLastDoorAdding() {
      return this.tickCounter - this.lastAddDoorTimestamp;
   }

   public int getNumVillagers() {
      return this.numVillagers;
   }

   public boolean isBlockPosWithinSqVillageRadius(BlockPos var1) {
      return this.center.distanceSq(☃) < this.villageRadius * this.villageRadius;
   }

   public List<VillageDoorInfo> getVillageDoorInfoList() {
      return this.villageDoorInfoList;
   }

   public VillageDoorInfo getNearestDoor(BlockPos var1) {
      VillageDoorInfo ☃ = null;
      int ☃x = Integer.MAX_VALUE;

      for (VillageDoorInfo ☃xx : this.villageDoorInfoList) {
         int ☃xxx = ☃xx.getDistanceToDoorBlockSq(☃);
         if (☃xxx < ☃x) {
            ☃ = ☃xx;
            ☃x = ☃xxx;
         }
      }

      return ☃;
   }

   public VillageDoorInfo getDoorInfo(BlockPos var1) {
      VillageDoorInfo ☃ = null;
      int ☃x = Integer.MAX_VALUE;

      for (VillageDoorInfo ☃xx : this.villageDoorInfoList) {
         int ☃xxx = ☃xx.getDistanceToDoorBlockSq(☃);
         if (☃xxx > 256) {
            ☃xxx *= 1000;
         } else {
            ☃xxx = ☃xx.getDoorOpeningRestrictionCounter();
         }

         if (☃xxx < ☃x) {
            BlockPos ☃xxxx = ☃xx.getDoorBlockPos();
            EnumFacing ☃xxxxx = ☃xx.getInsideDirection();
            if (this.world.getBlockState(☃xxxx.offset(☃xxxxx, 1)).getBlock().isPassable(this.world, ☃xxxx.offset(☃xxxxx, 1))
               && this.world.getBlockState(☃xxxx.offset(☃xxxxx, -1)).getBlock().isPassable(this.world, ☃xxxx.offset(☃xxxxx, -1))
               && this.world.getBlockState(☃xxxx.up().offset(☃xxxxx, 1)).getBlock().isPassable(this.world, ☃xxxx.up().offset(☃xxxxx, 1))
               && this.world.getBlockState(☃xxxx.up().offset(☃xxxxx, -1)).getBlock().isPassable(this.world, ☃xxxx.up().offset(☃xxxxx, -1))) {
               ☃ = ☃xx;
               ☃x = ☃xxx;
            }
         }
      }

      return ☃;
   }

   @Nullable
   public VillageDoorInfo getExistedDoor(BlockPos var1) {
      if (this.center.distanceSq(☃) > this.villageRadius * this.villageRadius) {
         return null;
      } else {
         for (VillageDoorInfo ☃ : this.villageDoorInfoList) {
            if (☃.getDoorBlockPos().getX() == ☃.getX() && ☃.getDoorBlockPos().getZ() == ☃.getZ() && Math.abs(☃.getDoorBlockPos().getY() - ☃.getY()) <= 1) {
               return ☃;
            }
         }

         return null;
      }
   }

   public void addVillageDoorInfo(VillageDoorInfo var1) {
      this.villageDoorInfoList.add(☃);
      this.centerHelper = this.centerHelper.add(☃.getDoorBlockPos());
      this.updateVillageRadiusAndCenter();
      this.lastAddDoorTimestamp = ☃.getLastActivityTimestamp();
   }

   public boolean isAnnihilated() {
      return this.villageDoorInfoList.isEmpty();
   }

   public void addOrRenewAgressor(EntityLivingBase var1) {
      for (Village.VillageAggressor ☃ : this.villageAgressors) {
         if (☃.agressor == ☃) {
            ☃.agressionTime = this.tickCounter;
            return;
         }
      }

      this.villageAgressors.add(new Village.VillageAggressor(☃, this.tickCounter));
   }

   @Nullable
   public EntityLivingBase findNearestVillageAggressor(EntityLivingBase var1) {
      double ☃ = Double.MAX_VALUE;
      Village.VillageAggressor ☃x = null;

      for (int ☃xx = 0; ☃xx < this.villageAgressors.size(); ☃xx++) {
         Village.VillageAggressor ☃xxx = this.villageAgressors.get(☃xx);
         double ☃xxxx = ☃xxx.agressor.getDistanceSq(☃);
         if (!(☃xxxx > ☃)) {
            ☃x = ☃xxx;
            ☃ = ☃xxxx;
         }
      }

      return ☃x == null ? null : ☃x.agressor;
   }

   public EntityPlayer getNearestTargetPlayer(EntityLivingBase var1) {
      double ☃ = Double.MAX_VALUE;
      EntityPlayer ☃x = null;

      for (String ☃xx : this.playerReputation.keySet()) {
         if (this.isPlayerReputationTooLow(☃xx)) {
            EntityPlayer ☃xxx = this.world.getPlayerEntityByName(☃xx);
            if (☃xxx != null) {
               double ☃xxxx = ☃xxx.getDistanceSq(☃);
               if (!(☃xxxx > ☃)) {
                  ☃x = ☃xxx;
                  ☃ = ☃xxxx;
               }
            }
         }
      }

      return ☃x;
   }

   private void removeDeadAndOldAgressors() {
      Iterator<Village.VillageAggressor> ☃ = this.villageAgressors.iterator();

      while (☃.hasNext()) {
         Village.VillageAggressor ☃x = ☃.next();
         if (!☃x.agressor.isEntityAlive() || Math.abs(this.tickCounter - ☃x.agressionTime) > 300) {
            ☃.remove();
         }
      }
   }

   private void removeDeadAndOutOfRangeDoors() {
      boolean ☃ = false;
      boolean ☃x = this.world.rand.nextInt(50) == 0;
      Iterator<VillageDoorInfo> ☃xx = this.villageDoorInfoList.iterator();

      while (☃xx.hasNext()) {
         VillageDoorInfo ☃xxx = ☃xx.next();
         if (☃x) {
            ☃xxx.resetDoorOpeningRestrictionCounter();
         }

         if (!this.isWoodDoor(☃xxx.getDoorBlockPos()) || Math.abs(this.tickCounter - ☃xxx.getLastActivityTimestamp()) > 1200) {
            this.centerHelper = this.centerHelper.subtract(☃xxx.getDoorBlockPos());
            ☃ = true;
            ☃xxx.setIsDetachedFromVillageFlag(true);
            ☃xx.remove();
         }
      }

      if (☃) {
         this.updateVillageRadiusAndCenter();
      }
   }

   private boolean isWoodDoor(BlockPos var1) {
      IBlockState ☃ = this.world.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      return ☃x instanceof BlockDoor ? ☃.getMaterial() == Material.WOOD : false;
   }

   private void updateVillageRadiusAndCenter() {
      int ☃ = this.villageDoorInfoList.size();
      if (☃ == 0) {
         this.center = BlockPos.ORIGIN;
         this.villageRadius = 0;
      } else {
         this.center = new BlockPos(this.centerHelper.getX() / ☃, this.centerHelper.getY() / ☃, this.centerHelper.getZ() / ☃);
         int ☃x = 0;

         for (VillageDoorInfo ☃xx : this.villageDoorInfoList) {
            ☃x = Math.max(☃xx.getDistanceToDoorBlockSq(this.center), ☃x);
         }

         this.villageRadius = Math.max(32, (int)Math.sqrt(☃x) + 1);
      }
   }

   public int getPlayerReputation(String var1) {
      Integer ☃ = this.playerReputation.get(☃);
      return ☃ == null ? 0 : ☃;
   }

   public int modifyPlayerReputation(String var1, int var2) {
      int ☃ = this.getPlayerReputation(☃);
      int ☃x = MathHelper.clamp(☃ + ☃, -30, 10);
      this.playerReputation.put(☃, ☃x);
      return ☃x;
   }

   public boolean isPlayerReputationTooLow(String var1) {
      return this.getPlayerReputation(☃) <= -15;
   }

   public void readVillageDataFromNBT(NBTTagCompound var1) {
      this.numVillagers = ☃.getInteger("PopSize");
      this.villageRadius = ☃.getInteger("Radius");
      this.numIronGolems = ☃.getInteger("Golems");
      this.lastAddDoorTimestamp = ☃.getInteger("Stable");
      this.tickCounter = ☃.getInteger("Tick");
      this.noBreedTicks = ☃.getInteger("MTick");
      this.center = new BlockPos(☃.getInteger("CX"), ☃.getInteger("CY"), ☃.getInteger("CZ"));
      this.centerHelper = new BlockPos(☃.getInteger("ACX"), ☃.getInteger("ACY"), ☃.getInteger("ACZ"));
      NBTTagList ☃ = ☃.getTagList("Doors", 10);

      for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
         NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
         VillageDoorInfo ☃xxx = new VillageDoorInfo(
            new BlockPos(☃xx.getInteger("X"), ☃xx.getInteger("Y"), ☃xx.getInteger("Z")), ☃xx.getInteger("IDX"), ☃xx.getInteger("IDZ"), ☃xx.getInteger("TS")
         );
         this.villageDoorInfoList.add(☃xxx);
      }

      NBTTagList ☃x = ☃.getTagList("Players", 10);

      for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
         NBTTagCompound ☃xxx = ☃x.getCompoundTagAt(☃xx);
         if (☃xxx.hasKey("UUID") && this.world != null && this.world.getMinecraftServer() != null) {
            PlayerProfileCache ☃xxxx = this.world.getMinecraftServer().getPlayerProfileCache();
            GameProfile ☃xxxxx = ☃xxxx.getProfileByUUID(UUID.fromString(☃xxx.getString("UUID")));
            if (☃xxxxx != null) {
               this.playerReputation.put(☃xxxxx.getName(), ☃xxx.getInteger("S"));
            }
         } else {
            this.playerReputation.put(☃xxx.getString("Name"), ☃xxx.getInteger("S"));
         }
      }
   }

   public void writeVillageDataToNBT(NBTTagCompound var1) {
      ☃.setInteger("PopSize", this.numVillagers);
      ☃.setInteger("Radius", this.villageRadius);
      ☃.setInteger("Golems", this.numIronGolems);
      ☃.setInteger("Stable", this.lastAddDoorTimestamp);
      ☃.setInteger("Tick", this.tickCounter);
      ☃.setInteger("MTick", this.noBreedTicks);
      ☃.setInteger("CX", this.center.getX());
      ☃.setInteger("CY", this.center.getY());
      ☃.setInteger("CZ", this.center.getZ());
      ☃.setInteger("ACX", this.centerHelper.getX());
      ☃.setInteger("ACY", this.centerHelper.getY());
      ☃.setInteger("ACZ", this.centerHelper.getZ());
      NBTTagList ☃ = new NBTTagList();

      for (VillageDoorInfo ☃x : this.villageDoorInfoList) {
         NBTTagCompound ☃xx = new NBTTagCompound();
         ☃xx.setInteger("X", ☃x.getDoorBlockPos().getX());
         ☃xx.setInteger("Y", ☃x.getDoorBlockPos().getY());
         ☃xx.setInteger("Z", ☃x.getDoorBlockPos().getZ());
         ☃xx.setInteger("IDX", ☃x.getInsideOffsetX());
         ☃xx.setInteger("IDZ", ☃x.getInsideOffsetZ());
         ☃xx.setInteger("TS", ☃x.getLastActivityTimestamp());
         ☃.appendTag(☃xx);
      }

      ☃.setTag("Doors", ☃);
      NBTTagList ☃x = new NBTTagList();

      for (String ☃xx : this.playerReputation.keySet()) {
         NBTTagCompound ☃xxx = new NBTTagCompound();
         PlayerProfileCache ☃xxxx = this.world.getMinecraftServer().getPlayerProfileCache();

         try {
            GameProfile ☃xxxxx = ☃xxxx.getGameProfileForUsername(☃xx);
            if (☃xxxxx != null) {
               ☃xxx.setString("UUID", ☃xxxxx.getId().toString());
               ☃xxx.setInteger("S", this.playerReputation.get(☃xx));
               ☃x.appendTag(☃xxx);
            }
         } catch (RuntimeException var9) {
         }
      }

      ☃.setTag("Players", ☃x);
   }

   public void endMatingSeason() {
      this.noBreedTicks = this.tickCounter;
   }

   public boolean isMatingSeason() {
      return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
   }

   public void setDefaultPlayerReputation(int var1) {
      for (String ☃ : this.playerReputation.keySet()) {
         this.modifyPlayerReputation(☃, ☃);
      }
   }

   class VillageAggressor {
      public EntityLivingBase agressor;
      public int agressionTime;

      VillageAggressor(EntityLivingBase var2, int var3) {
         this.agressor = ☃;
         this.agressionTime = ☃;
      }
   }
}
