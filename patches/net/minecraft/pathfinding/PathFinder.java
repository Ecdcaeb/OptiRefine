package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class PathFinder {
   private final PathHeap path = new PathHeap();
   private final Set<PathPoint> closedSet = Sets.newHashSet();
   private final PathPoint[] pathOptions = new PathPoint[32];
   private final NodeProcessor nodeProcessor;

   public PathFinder(NodeProcessor var1) {
      this.nodeProcessor = ☃;
   }

   @Nullable
   public Path findPath(IBlockAccess var1, EntityLiving var2, Entity var3, float var4) {
      return this.findPath(☃, ☃, ☃.posX, ☃.getEntityBoundingBox().minY, ☃.posZ, ☃);
   }

   @Nullable
   public Path findPath(IBlockAccess var1, EntityLiving var2, BlockPos var3, float var4) {
      return this.findPath(☃, ☃, ☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F, ☃);
   }

   @Nullable
   private Path findPath(IBlockAccess var1, EntityLiving var2, double var3, double var5, double var7, float var9) {
      this.path.clearPath();
      this.nodeProcessor.init(☃, ☃);
      PathPoint ☃ = this.nodeProcessor.getStart();
      PathPoint ☃x = this.nodeProcessor.getPathPointToCoords(☃, ☃, ☃);
      Path ☃xx = this.findPath(☃, ☃x, ☃);
      this.nodeProcessor.postProcess();
      return ☃xx;
   }

   @Nullable
   private Path findPath(PathPoint var1, PathPoint var2, float var3) {
      ☃.totalPathDistance = 0.0F;
      ☃.distanceToNext = ☃.distanceManhattan(☃);
      ☃.distanceToTarget = ☃.distanceToNext;
      this.path.clearPath();
      this.closedSet.clear();
      this.path.addPoint(☃);
      PathPoint ☃ = ☃;
      int ☃x = 0;

      while (!this.path.isPathEmpty()) {
         if (++☃x >= 200) {
            break;
         }

         PathPoint ☃xx = this.path.dequeue();
         if (☃xx.equals(☃)) {
            ☃ = ☃;
            break;
         }

         if (☃xx.distanceManhattan(☃) < ☃.distanceManhattan(☃)) {
            ☃ = ☃xx;
         }

         ☃xx.visited = true;
         int ☃xxx = this.nodeProcessor.findPathOptions(this.pathOptions, ☃xx, ☃, ☃);

         for (int ☃xxxx = 0; ☃xxxx < ☃xxx; ☃xxxx++) {
            PathPoint ☃xxxxx = this.pathOptions[☃xxxx];
            float ☃xxxxxx = ☃xx.distanceManhattan(☃xxxxx);
            ☃xxxxx.distanceFromOrigin = ☃xx.distanceFromOrigin + ☃xxxxxx;
            ☃xxxxx.cost = ☃xxxxxx + ☃xxxxx.costMalus;
            float ☃xxxxxxx = ☃xx.totalPathDistance + ☃xxxxx.cost;
            if (☃xxxxx.distanceFromOrigin < ☃ && (!☃xxxxx.isAssigned() || ☃xxxxxxx < ☃xxxxx.totalPathDistance)) {
               ☃xxxxx.previous = ☃xx;
               ☃xxxxx.totalPathDistance = ☃xxxxxxx;
               ☃xxxxx.distanceToNext = ☃xxxxx.distanceManhattan(☃) + ☃xxxxx.costMalus;
               if (☃xxxxx.isAssigned()) {
                  this.path.changeDistance(☃xxxxx, ☃xxxxx.totalPathDistance + ☃xxxxx.distanceToNext);
               } else {
                  ☃xxxxx.distanceToTarget = ☃xxxxx.totalPathDistance + ☃xxxxx.distanceToNext;
                  this.path.addPoint(☃xxxxx);
               }
            }
         }
      }

      return ☃ == ☃ ? null : this.createPath(☃, ☃);
   }

   private Path createPath(PathPoint var1, PathPoint var2) {
      int ☃ = 1;

      for (PathPoint ☃x = ☃; ☃x.previous != null; ☃x = ☃x.previous) {
         ☃++;
      }

      PathPoint[] ☃x = new PathPoint[☃];
      PathPoint var7 = ☃;

      for (☃x[--☃] = ☃; var7.previous != null; ☃x[--☃] = var7) {
         var7 = var7.previous;
      }

      return new Path(☃x);
   }
}
