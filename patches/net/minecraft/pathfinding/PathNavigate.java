package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public abstract class PathNavigate {
   protected EntityLiving entity;
   protected World world;
   @Nullable
   protected Path currentPath;
   protected double speed;
   private final IAttributeInstance pathSearchRange;
   protected int totalTicks;
   private int ticksAtLastPos;
   private Vec3d lastPosCheck = Vec3d.ZERO;
   private Vec3d timeoutCachedNode = Vec3d.ZERO;
   private long timeoutTimer;
   private long lastTimeoutCheck;
   private double timeoutLimit;
   protected float maxDistanceToWaypoint = 0.5F;
   protected boolean tryUpdatePath;
   private long lastTimeUpdated;
   protected NodeProcessor nodeProcessor;
   private BlockPos targetPos;
   private final PathFinder pathFinder;

   public PathNavigate(EntityLiving var1, World var2) {
      this.entity = ☃;
      this.world = ☃;
      this.pathSearchRange = ☃.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
      this.pathFinder = this.getPathFinder();
   }

   protected abstract PathFinder getPathFinder();

   public void setSpeed(double var1) {
      this.speed = ☃;
   }

   public float getPathSearchRange() {
      return (float)this.pathSearchRange.getAttributeValue();
   }

   public boolean canUpdatePathOnTimeout() {
      return this.tryUpdatePath;
   }

   public void updatePath() {
      if (this.world.getTotalWorldTime() - this.lastTimeUpdated > 20L) {
         if (this.targetPos != null) {
            this.currentPath = null;
            this.currentPath = this.getPathToPos(this.targetPos);
            this.lastTimeUpdated = this.world.getTotalWorldTime();
            this.tryUpdatePath = false;
         }
      } else {
         this.tryUpdatePath = true;
      }
   }

   @Nullable
   public final Path getPathToXYZ(double var1, double var3, double var5) {
      return this.getPathToPos(new BlockPos(☃, ☃, ☃));
   }

   @Nullable
   public Path getPathToPos(BlockPos var1) {
      if (!this.canNavigate()) {
         return null;
      } else if (this.currentPath != null && !this.currentPath.isFinished() && ☃.equals(this.targetPos)) {
         return this.currentPath;
      } else {
         this.targetPos = ☃;
         float ☃ = this.getPathSearchRange();
         this.world.profiler.startSection("pathfind");
         BlockPos ☃x = new BlockPos(this.entity);
         int ☃xx = (int)(☃ + 8.0F);
         ChunkCache ☃xxx = new ChunkCache(this.world, ☃x.add(-☃xx, -☃xx, -☃xx), ☃x.add(☃xx, ☃xx, ☃xx), 0);
         Path ☃xxxx = this.pathFinder.findPath(☃xxx, this.entity, this.targetPos, ☃);
         this.world.profiler.endSection();
         return ☃xxxx;
      }
   }

   @Nullable
   public Path getPathToEntityLiving(Entity var1) {
      if (!this.canNavigate()) {
         return null;
      } else {
         BlockPos ☃ = new BlockPos(☃);
         if (this.currentPath != null && !this.currentPath.isFinished() && ☃.equals(this.targetPos)) {
            return this.currentPath;
         } else {
            this.targetPos = ☃;
            float ☃x = this.getPathSearchRange();
            this.world.profiler.startSection("pathfind");
            BlockPos ☃xx = new BlockPos(this.entity).up();
            int ☃xxx = (int)(☃x + 16.0F);
            ChunkCache ☃xxxx = new ChunkCache(this.world, ☃xx.add(-☃xxx, -☃xxx, -☃xxx), ☃xx.add(☃xxx, ☃xxx, ☃xxx), 0);
            Path ☃xxxxx = this.pathFinder.findPath(☃xxxx, this.entity, ☃, ☃x);
            this.world.profiler.endSection();
            return ☃xxxxx;
         }
      }
   }

   public boolean tryMoveToXYZ(double var1, double var3, double var5, double var7) {
      return this.setPath(this.getPathToXYZ(☃, ☃, ☃), ☃);
   }

   public boolean tryMoveToEntityLiving(Entity var1, double var2) {
      Path ☃ = this.getPathToEntityLiving(☃);
      return ☃ != null && this.setPath(☃, ☃);
   }

   public boolean setPath(@Nullable Path var1, double var2) {
      if (☃ == null) {
         this.currentPath = null;
         return false;
      } else {
         if (!☃.isSamePath(this.currentPath)) {
            this.currentPath = ☃;
         }

         this.removeSunnyPath();
         if (this.currentPath.getCurrentPathLength() <= 0) {
            return false;
         } else {
            this.speed = ☃;
            Vec3d ☃ = this.getEntityPosition();
            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck = ☃;
            return true;
         }
      }
   }

   @Nullable
   public Path getPath() {
      return this.currentPath;
   }

   public void onUpdateNavigation() {
      this.totalTicks++;
      if (this.tryUpdatePath) {
         this.updatePath();
      }

      if (!this.noPath()) {
         if (this.canNavigate()) {
            this.pathFollow();
         } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
            Vec3d ☃ = this.getEntityPosition();
            Vec3d ☃x = this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex());
            if (☃.y > ☃x.y && !this.entity.onGround && MathHelper.floor(☃.x) == MathHelper.floor(☃x.x) && MathHelper.floor(☃.z) == MathHelper.floor(☃x.z)) {
               this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
            }
         }

         this.debugPathFinding();
         if (!this.noPath()) {
            Vec3d ☃ = this.currentPath.getPosition(this.entity);
            BlockPos ☃x = new BlockPos(☃).down();
            AxisAlignedBB ☃xx = this.world.getBlockState(☃x).getBoundingBox(this.world, ☃x);
            ☃ = ☃.subtract(0.0, 1.0 - ☃xx.maxY, 0.0);
            this.entity.getMoveHelper().setMoveTo(☃.x, ☃.y, ☃.z, this.speed);
         }
      }
   }

   protected void debugPathFinding() {
   }

   protected void pathFollow() {
      Vec3d ☃ = this.getEntityPosition();
      int ☃x = this.currentPath.getCurrentPathLength();

      for (int ☃xx = this.currentPath.getCurrentPathIndex(); ☃xx < this.currentPath.getCurrentPathLength(); ☃xx++) {
         if (this.currentPath.getPathPointFromIndex(☃xx).y != Math.floor(☃.y)) {
            ☃x = ☃xx;
            break;
         }
      }

      this.maxDistanceToWaypoint = this.entity.width > 0.75F ? this.entity.width / 2.0F : 0.75F - this.entity.width / 2.0F;
      Vec3d ☃xxx = this.currentPath.getCurrentPos();
      if (MathHelper.abs((float)(this.entity.posX - (☃xxx.x + 0.5))) < this.maxDistanceToWaypoint
         && MathHelper.abs((float)(this.entity.posZ - (☃xxx.z + 0.5))) < this.maxDistanceToWaypoint
         && Math.abs(this.entity.posY - ☃xxx.y) < 1.0) {
         this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
      }

      int ☃xxxx = MathHelper.ceil(this.entity.width);
      int ☃xxxxx = MathHelper.ceil(this.entity.height);
      int ☃xxxxxx = ☃xxxx;

      for (int ☃xxxxxxx = ☃x - 1; ☃xxxxxxx >= this.currentPath.getCurrentPathIndex(); ☃xxxxxxx--) {
         if (this.isDirectPathBetweenPoints(☃, this.currentPath.getVectorFromIndex(this.entity, ☃xxxxxxx), ☃xxxx, ☃xxxxx, ☃xxxxxx)) {
            this.currentPath.setCurrentPathIndex(☃xxxxxxx);
            break;
         }
      }

      this.checkForStuck(☃);
   }

   protected void checkForStuck(Vec3d var1) {
      if (this.totalTicks - this.ticksAtLastPos > 100) {
         if (☃.squareDistanceTo(this.lastPosCheck) < 2.25) {
            this.clearPath();
         }

         this.ticksAtLastPos = this.totalTicks;
         this.lastPosCheck = ☃;
      }

      if (this.currentPath != null && !this.currentPath.isFinished()) {
         Vec3d ☃ = this.currentPath.getCurrentPos();
         if (☃.equals(this.timeoutCachedNode)) {
            this.timeoutTimer = this.timeoutTimer + (System.currentTimeMillis() - this.lastTimeoutCheck);
         } else {
            this.timeoutCachedNode = ☃;
            double ☃x = ☃.distanceTo(this.timeoutCachedNode);
            this.timeoutLimit = this.entity.getAIMoveSpeed() > 0.0F ? ☃x / this.entity.getAIMoveSpeed() * 1000.0 : 0.0;
         }

         if (this.timeoutLimit > 0.0 && this.timeoutTimer > this.timeoutLimit * 3.0) {
            this.timeoutCachedNode = Vec3d.ZERO;
            this.timeoutTimer = 0L;
            this.timeoutLimit = 0.0;
            this.clearPath();
         }

         this.lastTimeoutCheck = System.currentTimeMillis();
      }
   }

   public boolean noPath() {
      return this.currentPath == null || this.currentPath.isFinished();
   }

   public void clearPath() {
      this.currentPath = null;
   }

   protected abstract Vec3d getEntityPosition();

   protected abstract boolean canNavigate();

   protected boolean isInLiquid() {
      return this.entity.isInWater() || this.entity.isInLava();
   }

   protected void removeSunnyPath() {
      if (this.currentPath != null) {
         for (int ☃ = 0; ☃ < this.currentPath.getCurrentPathLength(); ☃++) {
            PathPoint ☃x = this.currentPath.getPathPointFromIndex(☃);
            PathPoint ☃xx = ☃ + 1 < this.currentPath.getCurrentPathLength() ? this.currentPath.getPathPointFromIndex(☃ + 1) : null;
            IBlockState ☃xxx = this.world.getBlockState(new BlockPos(☃x.x, ☃x.y, ☃x.z));
            Block ☃xxxx = ☃xxx.getBlock();
            if (☃xxxx == Blocks.CAULDRON) {
               this.currentPath.setPoint(☃, ☃x.cloneMove(☃x.x, ☃x.y + 1, ☃x.z));
               if (☃xx != null && ☃x.y >= ☃xx.y) {
                  this.currentPath.setPoint(☃ + 1, ☃xx.cloneMove(☃xx.x, ☃x.y + 1, ☃xx.z));
               }
            }
         }
      }
   }

   protected abstract boolean isDirectPathBetweenPoints(Vec3d var1, Vec3d var2, int var3, int var4, int var5);

   public boolean canEntityStandOnPos(BlockPos var1) {
      return this.world.getBlockState(☃.down()).isFullBlock();
   }

   public NodeProcessor getNodeProcessor() {
      return this.nodeProcessor;
   }
}
