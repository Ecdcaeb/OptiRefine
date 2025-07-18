package net.minecraft.village;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class VillageDoorInfo {
   private final BlockPos doorBlockPos;
   private final BlockPos insideBlock;
   private final EnumFacing insideDirection;
   private int lastActivityTimestamp;
   private boolean isDetachedFromVillageFlag;
   private int doorOpeningRestrictionCounter;

   public VillageDoorInfo(BlockPos var1, int var2, int var3, int var4) {
      this(☃, getFaceDirection(☃, ☃), ☃);
   }

   private static EnumFacing getFaceDirection(int var0, int var1) {
      if (☃ < 0) {
         return EnumFacing.WEST;
      } else if (☃ > 0) {
         return EnumFacing.EAST;
      } else {
         return ☃ < 0 ? EnumFacing.NORTH : EnumFacing.SOUTH;
      }
   }

   public VillageDoorInfo(BlockPos var1, EnumFacing var2, int var3) {
      this.doorBlockPos = ☃;
      this.insideDirection = ☃;
      this.insideBlock = ☃.offset(☃, 2);
      this.lastActivityTimestamp = ☃;
   }

   public int getDistanceSquared(int var1, int var2, int var3) {
      return (int)this.doorBlockPos.distanceSq(☃, ☃, ☃);
   }

   public int getDistanceToDoorBlockSq(BlockPos var1) {
      return (int)☃.distanceSq(this.getDoorBlockPos());
   }

   public int getDistanceToInsideBlockSq(BlockPos var1) {
      return (int)this.insideBlock.distanceSq(☃);
   }

   public boolean isInsideSide(BlockPos var1) {
      int ☃ = ☃.getX() - this.doorBlockPos.getX();
      int ☃x = ☃.getZ() - this.doorBlockPos.getY();
      return ☃ * this.insideDirection.getXOffset() + ☃x * this.insideDirection.getZOffset() >= 0;
   }

   public void resetDoorOpeningRestrictionCounter() {
      this.doorOpeningRestrictionCounter = 0;
   }

   public void incrementDoorOpeningRestrictionCounter() {
      this.doorOpeningRestrictionCounter++;
   }

   public int getDoorOpeningRestrictionCounter() {
      return this.doorOpeningRestrictionCounter;
   }

   public BlockPos getDoorBlockPos() {
      return this.doorBlockPos;
   }

   public BlockPos getInsideBlockPos() {
      return this.insideBlock;
   }

   public int getInsideOffsetX() {
      return this.insideDirection.getXOffset() * 2;
   }

   public int getInsideOffsetZ() {
      return this.insideDirection.getZOffset() * 2;
   }

   public int getLastActivityTimestamp() {
      return this.lastActivityTimestamp;
   }

   public void setLastActivityTimestamp(int var1) {
      this.lastActivityTimestamp = ☃;
   }

   public boolean getIsDetachedFromVillageFlag() {
      return this.isDetachedFromVillageFlag;
   }

   public void setIsDetachedFromVillageFlag(boolean var1) {
      this.isDetachedFromVillageFlag = ☃;
   }

   public EnumFacing getInsideDirection() {
      return this.insideDirection;
   }
}
