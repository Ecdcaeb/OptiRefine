package net.minecraft.world.border;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class WorldBorder {
   private final List<IBorderListener> listeners = Lists.newArrayList();
   private double centerX;
   private double centerZ;
   private double startDiameter = 6.0E7;
   private double endDiameter = this.startDiameter;
   private long endTime;
   private long startTime;
   private int worldSize = 29999984;
   private double damageAmount = 0.2;
   private double damageBuffer = 5.0;
   private int warningTime = 15;
   private int warningDistance = 5;

   public boolean contains(BlockPos var1) {
      return ☃.getX() + 1 > this.minX() && ☃.getX() < this.maxX() && ☃.getZ() + 1 > this.minZ() && ☃.getZ() < this.maxZ();
   }

   public boolean contains(ChunkPos var1) {
      return ☃.getXEnd() > this.minX() && ☃.getXStart() < this.maxX() && ☃.getZEnd() > this.minZ() && ☃.getZStart() < this.maxZ();
   }

   public boolean contains(AxisAlignedBB var1) {
      return ☃.maxX > this.minX() && ☃.minX < this.maxX() && ☃.maxZ > this.minZ() && ☃.minZ < this.maxZ();
   }

   public double getClosestDistance(Entity var1) {
      return this.getClosestDistance(☃.posX, ☃.posZ);
   }

   public double getClosestDistance(double var1, double var3) {
      double ☃ = ☃ - this.minZ();
      double ☃x = this.maxZ() - ☃;
      double ☃xx = ☃ - this.minX();
      double ☃xxx = this.maxX() - ☃;
      double ☃xxxx = Math.min(☃xx, ☃xxx);
      ☃xxxx = Math.min(☃xxxx, ☃);
      return Math.min(☃xxxx, ☃x);
   }

   public EnumBorderStatus getStatus() {
      if (this.endDiameter < this.startDiameter) {
         return EnumBorderStatus.SHRINKING;
      } else {
         return this.endDiameter > this.startDiameter ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY;
      }
   }

   public double minX() {
      double ☃ = this.getCenterX() - this.getDiameter() / 2.0;
      if (☃ < -this.worldSize) {
         ☃ = -this.worldSize;
      }

      return ☃;
   }

   public double minZ() {
      double ☃ = this.getCenterZ() - this.getDiameter() / 2.0;
      if (☃ < -this.worldSize) {
         ☃ = -this.worldSize;
      }

      return ☃;
   }

   public double maxX() {
      double ☃ = this.getCenterX() + this.getDiameter() / 2.0;
      if (☃ > this.worldSize) {
         ☃ = this.worldSize;
      }

      return ☃;
   }

   public double maxZ() {
      double ☃ = this.getCenterZ() + this.getDiameter() / 2.0;
      if (☃ > this.worldSize) {
         ☃ = this.worldSize;
      }

      return ☃;
   }

   public double getCenterX() {
      return this.centerX;
   }

   public double getCenterZ() {
      return this.centerZ;
   }

   public void setCenter(double var1, double var3) {
      this.centerX = ☃;
      this.centerZ = ☃;

      for (IBorderListener ☃ : this.getListeners()) {
         ☃.onCenterChanged(this, ☃, ☃);
      }
   }

   public double getDiameter() {
      if (this.getStatus() != EnumBorderStatus.STATIONARY) {
         double ☃ = (float)(System.currentTimeMillis() - this.startTime) / (float)(this.endTime - this.startTime);
         if (!(☃ >= 1.0)) {
            return this.startDiameter + (this.endDiameter - this.startDiameter) * ☃;
         }

         this.setTransition(this.endDiameter);
      }

      return this.startDiameter;
   }

   public long getTimeUntilTarget() {
      return this.getStatus() == EnumBorderStatus.STATIONARY ? 0L : this.endTime - System.currentTimeMillis();
   }

   public double getTargetSize() {
      return this.endDiameter;
   }

   public void setTransition(double var1) {
      this.startDiameter = ☃;
      this.endDiameter = ☃;
      this.endTime = System.currentTimeMillis();
      this.startTime = this.endTime;

      for (IBorderListener ☃ : this.getListeners()) {
         ☃.onSizeChanged(this, ☃);
      }
   }

   public void setTransition(double var1, double var3, long var5) {
      this.startDiameter = ☃;
      this.endDiameter = ☃;
      this.startTime = System.currentTimeMillis();
      this.endTime = this.startTime + ☃;

      for (IBorderListener ☃ : this.getListeners()) {
         ☃.onTransitionStarted(this, ☃, ☃, ☃);
      }
   }

   protected List<IBorderListener> getListeners() {
      return Lists.newArrayList(this.listeners);
   }

   public void addListener(IBorderListener var1) {
      this.listeners.add(☃);
   }

   public void setSize(int var1) {
      this.worldSize = ☃;
   }

   public int getSize() {
      return this.worldSize;
   }

   public double getDamageBuffer() {
      return this.damageBuffer;
   }

   public void setDamageBuffer(double var1) {
      this.damageBuffer = ☃;

      for (IBorderListener ☃ : this.getListeners()) {
         ☃.onDamageBufferChanged(this, ☃);
      }
   }

   public double getDamageAmount() {
      return this.damageAmount;
   }

   public void setDamageAmount(double var1) {
      this.damageAmount = ☃;

      for (IBorderListener ☃ : this.getListeners()) {
         ☃.onDamageAmountChanged(this, ☃);
      }
   }

   public double getResizeSpeed() {
      return this.endTime == this.startTime ? 0.0 : Math.abs(this.startDiameter - this.endDiameter) / (this.endTime - this.startTime);
   }

   public int getWarningTime() {
      return this.warningTime;
   }

   public void setWarningTime(int var1) {
      this.warningTime = ☃;

      for (IBorderListener ☃ : this.getListeners()) {
         ☃.onWarningTimeChanged(this, ☃);
      }
   }

   public int getWarningDistance() {
      return this.warningDistance;
   }

   public void setWarningDistance(int var1) {
      this.warningDistance = ☃;

      for (IBorderListener ☃ : this.getListeners()) {
         ☃.onWarningDistanceChanged(this, ☃);
      }
   }
}
