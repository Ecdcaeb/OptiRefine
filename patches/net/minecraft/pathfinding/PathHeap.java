package net.minecraft.pathfinding;

public class PathHeap {
   private PathPoint[] pathPoints = new PathPoint[128];
   private int count;

   public PathPoint addPoint(PathPoint var1) {
      if (☃.index >= 0) {
         throw new IllegalStateException("OW KNOWS!");
      } else {
         if (this.count == this.pathPoints.length) {
            PathPoint[] ☃ = new PathPoint[this.count << 1];
            System.arraycopy(this.pathPoints, 0, ☃, 0, this.count);
            this.pathPoints = ☃;
         }

         this.pathPoints[this.count] = ☃;
         ☃.index = this.count;
         this.sortBack(this.count++);
         return ☃;
      }
   }

   public void clearPath() {
      this.count = 0;
   }

   public PathPoint dequeue() {
      PathPoint ☃ = this.pathPoints[0];
      this.pathPoints[0] = this.pathPoints[--this.count];
      this.pathPoints[this.count] = null;
      if (this.count > 0) {
         this.sortForward(0);
      }

      ☃.index = -1;
      return ☃;
   }

   public void changeDistance(PathPoint var1, float var2) {
      float ☃ = ☃.distanceToTarget;
      ☃.distanceToTarget = ☃;
      if (☃ < ☃) {
         this.sortBack(☃.index);
      } else {
         this.sortForward(☃.index);
      }
   }

   private void sortBack(int var1) {
      PathPoint ☃ = this.pathPoints[☃];
      float ☃x = ☃.distanceToTarget;

      while (☃ > 0) {
         int ☃xx = ☃ - 1 >> 1;
         PathPoint ☃xxx = this.pathPoints[☃xx];
         if (!(☃x < ☃xxx.distanceToTarget)) {
            break;
         }

         this.pathPoints[☃] = ☃xxx;
         ☃xxx.index = ☃;
         ☃ = ☃xx;
      }

      this.pathPoints[☃] = ☃;
      ☃.index = ☃;
   }

   private void sortForward(int var1) {
      PathPoint ☃ = this.pathPoints[☃];
      float ☃x = ☃.distanceToTarget;

      while (true) {
         int ☃xx = 1 + (☃ << 1);
         int ☃xxx = ☃xx + 1;
         if (☃xx >= this.count) {
            break;
         }

         PathPoint ☃xxxx = this.pathPoints[☃xx];
         float ☃xxxxx = ☃xxxx.distanceToTarget;
         PathPoint ☃xxxxxx;
         float ☃xxxxxxx;
         if (☃xxx >= this.count) {
            ☃xxxxxx = null;
            ☃xxxxxxx = Float.POSITIVE_INFINITY;
         } else {
            ☃xxxxxx = this.pathPoints[☃xxx];
            ☃xxxxxxx = ☃xxxxxx.distanceToTarget;
         }

         if (☃xxxxx < ☃xxxxxxx) {
            if (!(☃xxxxx < ☃x)) {
               break;
            }

            this.pathPoints[☃] = ☃xxxx;
            ☃xxxx.index = ☃;
            ☃ = ☃xx;
         } else {
            if (!(☃xxxxxxx < ☃x)) {
               break;
            }

            this.pathPoints[☃] = ☃xxxxxx;
            ☃xxxxxx.index = ☃;
            ☃ = ☃xxx;
         }
      }

      this.pathPoints[☃] = ☃;
      ☃.index = ☃;
   }

   public boolean isPathEmpty() {
      return this.count == 0;
   }
}
