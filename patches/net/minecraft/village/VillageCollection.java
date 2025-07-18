package net.minecraft.village;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldSavedData;

public class VillageCollection extends WorldSavedData {
   private World world;
   private final List<BlockPos> villagerPositionsList = Lists.newArrayList();
   private final List<VillageDoorInfo> newDoors = Lists.newArrayList();
   private final List<Village> villageList = Lists.newArrayList();
   private int tickCounter;

   public VillageCollection(String var1) {
      super(☃);
   }

   public VillageCollection(World var1) {
      super(fileNameForProvider(☃.provider));
      this.world = ☃;
      this.markDirty();
   }

   public void setWorldsForAll(World var1) {
      this.world = ☃;

      for (Village ☃ : this.villageList) {
         ☃.setWorld(☃);
      }
   }

   public void addToVillagerPositionList(BlockPos var1) {
      if (this.villagerPositionsList.size() <= 64) {
         if (!this.positionInList(☃)) {
            this.villagerPositionsList.add(☃);
         }
      }
   }

   public void tick() {
      this.tickCounter++;

      for (Village ☃ : this.villageList) {
         ☃.tick(this.tickCounter);
      }

      this.removeAnnihilatedVillages();
      this.dropOldestVillagerPosition();
      this.addNewDoorsToVillageOrCreateVillage();
      if (this.tickCounter % 400 == 0) {
         this.markDirty();
      }
   }

   private void removeAnnihilatedVillages() {
      Iterator<Village> ☃ = this.villageList.iterator();

      while (☃.hasNext()) {
         Village ☃x = ☃.next();
         if (☃x.isAnnihilated()) {
            ☃.remove();
            this.markDirty();
         }
      }
   }

   public List<Village> getVillageList() {
      return this.villageList;
   }

   public Village getNearestVillage(BlockPos var1, int var2) {
      Village ☃ = null;
      double ☃x = Float.MAX_VALUE;

      for (Village ☃xx : this.villageList) {
         double ☃xxx = ☃xx.getCenter().distanceSq(☃);
         if (!(☃xxx >= ☃x)) {
            float ☃xxxx = ☃ + ☃xx.getVillageRadius();
            if (!(☃xxx > ☃xxxx * ☃xxxx)) {
               ☃ = ☃xx;
               ☃x = ☃xxx;
            }
         }
      }

      return ☃;
   }

   private void dropOldestVillagerPosition() {
      if (!this.villagerPositionsList.isEmpty()) {
         this.addDoorsAround(this.villagerPositionsList.remove(0));
      }
   }

   private void addNewDoorsToVillageOrCreateVillage() {
      for (int ☃ = 0; ☃ < this.newDoors.size(); ☃++) {
         VillageDoorInfo ☃x = this.newDoors.get(☃);
         Village ☃xx = this.getNearestVillage(☃x.getDoorBlockPos(), 32);
         if (☃xx == null) {
            ☃xx = new Village(this.world);
            this.villageList.add(☃xx);
            this.markDirty();
         }

         ☃xx.addVillageDoorInfo(☃x);
      }

      this.newDoors.clear();
   }

   private void addDoorsAround(BlockPos var1) {
      int ☃ = 16;
      int ☃x = 4;
      int ☃xx = 16;

      for (int ☃xxx = -16; ☃xxx < 16; ☃xxx++) {
         for (int ☃xxxx = -4; ☃xxxx < 4; ☃xxxx++) {
            for (int ☃xxxxx = -16; ☃xxxxx < 16; ☃xxxxx++) {
               BlockPos ☃xxxxxx = ☃.add(☃xxx, ☃xxxx, ☃xxxxx);
               if (this.isWoodDoor(☃xxxxxx)) {
                  VillageDoorInfo ☃xxxxxxx = this.checkDoorExistence(☃xxxxxx);
                  if (☃xxxxxxx == null) {
                     this.addToNewDoorsList(☃xxxxxx);
                  } else {
                     ☃xxxxxxx.setLastActivityTimestamp(this.tickCounter);
                  }
               }
            }
         }
      }
   }

   @Nullable
   private VillageDoorInfo checkDoorExistence(BlockPos var1) {
      for (VillageDoorInfo ☃ : this.newDoors) {
         if (☃.getDoorBlockPos().getX() == ☃.getX() && ☃.getDoorBlockPos().getZ() == ☃.getZ() && Math.abs(☃.getDoorBlockPos().getY() - ☃.getY()) <= 1) {
            return ☃;
         }
      }

      for (Village ☃x : this.villageList) {
         VillageDoorInfo ☃xx = ☃x.getExistedDoor(☃);
         if (☃xx != null) {
            return ☃xx;
         }
      }

      return null;
   }

   private void addToNewDoorsList(BlockPos var1) {
      EnumFacing ☃ = BlockDoor.getFacing(this.world, ☃);
      EnumFacing ☃x = ☃.getOpposite();
      int ☃xx = this.countBlocksCanSeeSky(☃, ☃, 5);
      int ☃xxx = this.countBlocksCanSeeSky(☃, ☃x, ☃xx + 1);
      if (☃xx != ☃xxx) {
         this.newDoors.add(new VillageDoorInfo(☃, ☃xx < ☃xxx ? ☃ : ☃x, this.tickCounter));
      }
   }

   private int countBlocksCanSeeSky(BlockPos var1, EnumFacing var2, int var3) {
      int ☃ = 0;

      for (int ☃x = 1; ☃x <= 5; ☃x++) {
         if (this.world.canSeeSky(☃.offset(☃, ☃x))) {
            if (++☃ >= ☃) {
               return ☃;
            }
         }
      }

      return ☃;
   }

   private boolean positionInList(BlockPos var1) {
      for (BlockPos ☃ : this.villagerPositionsList) {
         if (☃.equals(☃)) {
            return true;
         }
      }

      return false;
   }

   private boolean isWoodDoor(BlockPos var1) {
      IBlockState ☃ = this.world.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      return ☃x instanceof BlockDoor ? ☃.getMaterial() == Material.WOOD : false;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      this.tickCounter = ☃.getInteger("Tick");
      NBTTagList ☃ = ☃.getTagList("Villages", 10);

      for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
         NBTTagCompound ☃xx = ☃.getCompoundTagAt(☃x);
         Village ☃xxx = new Village();
         ☃xxx.readVillageDataFromNBT(☃xx);
         this.villageList.add(☃xxx);
      }
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      ☃.setInteger("Tick", this.tickCounter);
      NBTTagList ☃ = new NBTTagList();

      for (Village ☃x : this.villageList) {
         NBTTagCompound ☃xx = new NBTTagCompound();
         ☃x.writeVillageDataToNBT(☃xx);
         ☃.appendTag(☃xx);
      }

      ☃.setTag("Villages", ☃);
      return ☃;
   }

   public static String fileNameForProvider(WorldProvider var0) {
      return "villages" + ☃.getDimensionType().getSuffix();
   }
}
