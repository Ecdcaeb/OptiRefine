package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

public class Path {
   private final PathPoint[] points;
   private PathPoint[] openSet = new PathPoint[0];
   private PathPoint[] closedSet = new PathPoint[0];
   private PathPoint target;
   private int currentPathIndex;
   private int pathLength;

   public Path(PathPoint[] var1) {
      this.points = ☃;
      this.pathLength = ☃.length;
   }

   public void incrementPathIndex() {
      this.currentPathIndex++;
   }

   public boolean isFinished() {
      return this.currentPathIndex >= this.pathLength;
   }

   @Nullable
   public PathPoint getFinalPathPoint() {
      return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
   }

   public PathPoint getPathPointFromIndex(int var1) {
      return this.points[☃];
   }

   public void setPoint(int var1, PathPoint var2) {
      this.points[☃] = ☃;
   }

   public int getCurrentPathLength() {
      return this.pathLength;
   }

   public void setCurrentPathLength(int var1) {
      this.pathLength = ☃;
   }

   public int getCurrentPathIndex() {
      return this.currentPathIndex;
   }

   public void setCurrentPathIndex(int var1) {
      this.currentPathIndex = ☃;
   }

   public Vec3d getVectorFromIndex(Entity var1, int var2) {
      double ☃ = this.points[☃].x + (int)(☃.width + 1.0F) * 0.5;
      double ☃x = this.points[☃].y;
      double ☃xx = this.points[☃].z + (int)(☃.width + 1.0F) * 0.5;
      return new Vec3d(☃, ☃x, ☃xx);
   }

   public Vec3d getPosition(Entity var1) {
      return this.getVectorFromIndex(☃, this.currentPathIndex);
   }

   public Vec3d getCurrentPos() {
      PathPoint ☃ = this.points[this.currentPathIndex];
      return new Vec3d(☃.x, ☃.y, ☃.z);
   }

   public boolean isSamePath(Path var1) {
      if (☃ == null) {
         return false;
      } else if (☃.points.length != this.points.length) {
         return false;
      } else {
         for (int ☃ = 0; ☃ < this.points.length; ☃++) {
            if (this.points[☃].x != ☃.points[☃].x || this.points[☃].y != ☃.points[☃].y || this.points[☃].z != ☃.points[☃].z) {
               return false;
            }
         }

         return true;
      }
   }

   public PathPoint[] getOpenSet() {
      return this.openSet;
   }

   public PathPoint[] getClosedSet() {
      return this.closedSet;
   }

   public PathPoint getTarget() {
      return this.target;
   }

   public static Path read(PacketBuffer var0) {
      int ☃ = ☃.readInt();
      PathPoint ☃x = PathPoint.createFromBuffer(☃);
      PathPoint[] ☃xx = new PathPoint[☃.readInt()];

      for (int ☃xxx = 0; ☃xxx < ☃xx.length; ☃xxx++) {
         ☃xx[☃xxx] = PathPoint.createFromBuffer(☃);
      }

      PathPoint[] ☃xxx = new PathPoint[☃.readInt()];

      for (int ☃xxxx = 0; ☃xxxx < ☃xxx.length; ☃xxxx++) {
         ☃xxx[☃xxxx] = PathPoint.createFromBuffer(☃);
      }

      PathPoint[] ☃xxxx = new PathPoint[☃.readInt()];

      for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.length; ☃xxxxx++) {
         ☃xxxx[☃xxxxx] = PathPoint.createFromBuffer(☃);
      }

      Path ☃xxxxx = new Path(☃xx);
      ☃xxxxx.openSet = ☃xxx;
      ☃xxxxx.closedSet = ☃xxxx;
      ☃xxxxx.target = ☃x;
      ☃xxxxx.currentPathIndex = ☃;
      return ☃xxxxx;
   }
}
