package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhaseStrafePlayer extends PhaseBase {
   private static final Logger LOGGER = LogManager.getLogger();
   private int fireballCharge;
   private Path currentPath;
   private Vec3d targetLocation;
   private EntityLivingBase attackTarget;
   private boolean holdingPatternClockwise;

   public PhaseStrafePlayer(EntityDragon var1) {
      super(☃);
   }

   @Override
   public void doLocalUpdate() {
      if (this.attackTarget == null) {
         LOGGER.warn("Skipping player strafe phase because no player was found");
         this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
      } else {
         if (this.currentPath != null && this.currentPath.isFinished()) {
            double ☃ = this.attackTarget.posX;
            double ☃x = this.attackTarget.posZ;
            double ☃xx = ☃ - this.dragon.posX;
            double ☃xxx = ☃x - this.dragon.posZ;
            double ☃xxxx = MathHelper.sqrt(☃xx * ☃xx + ☃xxx * ☃xxx);
            double ☃xxxxx = Math.min(0.4F + ☃xxxx / 80.0 - 1.0, 10.0);
            this.targetLocation = new Vec3d(☃, this.attackTarget.posY + ☃xxxxx, ☃x);
         }

         double ☃ = this.targetLocation == null ? 0.0 : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
         if (☃ < 100.0 || ☃ > 22500.0) {
            this.findNewTarget();
         }

         double ☃x = 64.0;
         if (this.attackTarget.getDistanceSq(this.dragon) < 4096.0) {
            if (this.dragon.canEntityBeSeen(this.attackTarget)) {
               this.fireballCharge++;
               Vec3d ☃xx = new Vec3d(this.attackTarget.posX - this.dragon.posX, 0.0, this.attackTarget.posZ - this.dragon.posZ).normalize();
               Vec3d ☃xxx = new Vec3d(
                     MathHelper.sin(this.dragon.rotationYaw * (float) (Math.PI / 180.0)),
                     0.0,
                     -MathHelper.cos(this.dragon.rotationYaw * (float) (Math.PI / 180.0))
                  )
                  .normalize();
               float ☃xxxx = (float)☃xxx.dotProduct(☃xx);
               float ☃xxxxx = (float)(Math.acos(☃xxxx) * 180.0F / (float)Math.PI);
               ☃xxxxx += 0.5F;
               if (this.fireballCharge >= 5 && ☃xxxxx >= 0.0F && ☃xxxxx < 10.0F) {
                  double ☃xxxxxx = 1.0;
                  Vec3d ☃xxxxxxx = this.dragon.getLook(1.0F);
                  double ☃xxxxxxxx = this.dragon.dragonPartHead.posX - ☃xxxxxxx.x * 1.0;
                  double ☃xxxxxxxxx = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0F + 0.5;
                  double ☃xxxxxxxxxx = this.dragon.dragonPartHead.posZ - ☃xxxxxxx.z * 1.0;
                  double ☃xxxxxxxxxxx = this.attackTarget.posX - ☃xxxxxxxx;
                  double ☃xxxxxxxxxxxx = this.attackTarget.posY + this.attackTarget.height / 2.0F - (☃xxxxxxxxx + this.dragon.dragonPartHead.height / 2.0F);
                  double ☃xxxxxxxxxxxxx = this.attackTarget.posZ - ☃xxxxxxxxxx;
                  this.dragon.world.playEvent(null, 1017, new BlockPos(this.dragon), 0);
                  EntityDragonFireball ☃xxxxxxxxxxxxxx = new EntityDragonFireball(this.dragon.world, this.dragon, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx);
                  ☃xxxxxxxxxxxxxx.setLocationAndAngles(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx, 0.0F, 0.0F);
                  this.dragon.world.spawnEntity(☃xxxxxxxxxxxxxx);
                  this.fireballCharge = 0;
                  if (this.currentPath != null) {
                     while (!this.currentPath.isFinished()) {
                        this.currentPath.incrementPathIndex();
                     }
                  }

                  this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
               }
            } else if (this.fireballCharge > 0) {
               this.fireballCharge--;
            }
         } else if (this.fireballCharge > 0) {
            this.fireballCharge--;
         }
      }
   }

   private void findNewTarget() {
      if (this.currentPath == null || this.currentPath.isFinished()) {
         int ☃ = this.dragon.initPathPoints();
         int ☃x = ☃;
         if (this.dragon.getRNG().nextInt(8) == 0) {
            this.holdingPatternClockwise = !this.holdingPatternClockwise;
            ☃x = ☃ + 6;
         }

         if (this.holdingPatternClockwise) {
            ☃x++;
         } else {
            ☃x--;
         }

         if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() > 0) {
            ☃x %= 12;
            if (☃x < 0) {
               ☃x += 12;
            }
         } else {
            ☃x -= 12;
            ☃x &= 7;
            ☃x += 12;
         }

         this.currentPath = this.dragon.findPath(☃, ☃x, null);
         if (this.currentPath != null) {
            this.currentPath.incrementPathIndex();
         }
      }

      this.navigateToNextPathNode();
   }

   private void navigateToNextPathNode() {
      if (this.currentPath != null && !this.currentPath.isFinished()) {
         Vec3d ☃ = this.currentPath.getCurrentPos();
         this.currentPath.incrementPathIndex();
         double ☃x = ☃.x;
         double ☃xx = ☃.z;

         double ☃xxx;
         do {
            ☃xxx = ☃.y + this.dragon.getRNG().nextFloat() * 20.0F;
         } while (☃xxx < ☃.y);

         this.targetLocation = new Vec3d(☃x, ☃xxx, ☃xx);
      }
   }

   @Override
   public void initPhase() {
      this.fireballCharge = 0;
      this.targetLocation = null;
      this.currentPath = null;
      this.attackTarget = null;
   }

   public void setTarget(EntityLivingBase var1) {
      this.attackTarget = ☃;
      int ☃ = this.dragon.initPathPoints();
      int ☃x = this.dragon.getNearestPpIdx(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ);
      int ☃xx = MathHelper.floor(this.attackTarget.posX);
      int ☃xxx = MathHelper.floor(this.attackTarget.posZ);
      double ☃xxxx = ☃xx - this.dragon.posX;
      double ☃xxxxx = ☃xxx - this.dragon.posZ;
      double ☃xxxxxx = MathHelper.sqrt(☃xxxx * ☃xxxx + ☃xxxxx * ☃xxxxx);
      double ☃xxxxxxx = Math.min(0.4F + ☃xxxxxx / 80.0 - 1.0, 10.0);
      int ☃xxxxxxxx = MathHelper.floor(this.attackTarget.posY + ☃xxxxxxx);
      PathPoint ☃xxxxxxxxx = new PathPoint(☃xx, ☃xxxxxxxx, ☃xxx);
      this.currentPath = this.dragon.findPath(☃, ☃x, ☃xxxxxxxxx);
      if (this.currentPath != null) {
         this.currentPath.incrementPathIndex();
         this.navigateToNextPathNode();
      }
   }

   @Nullable
   @Override
   public Vec3d getTargetLocation() {
      return this.targetLocation;
   }

   @Override
   public PhaseList<PhaseStrafePlayer> getType() {
      return PhaseList.STRAFE_PLAYER;
   }
}
