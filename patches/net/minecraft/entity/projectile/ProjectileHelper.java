package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class ProjectileHelper {
   public static RayTraceResult forwardsRaycast(Entity var0, boolean var1, boolean var2, Entity var3) {
      double ☃ = ☃.posX;
      double ☃x = ☃.posY;
      double ☃xx = ☃.posZ;
      double ☃xxx = ☃.motionX;
      double ☃xxxx = ☃.motionY;
      double ☃xxxxx = ☃.motionZ;
      World ☃xxxxxx = ☃.world;
      Vec3d ☃xxxxxxx = new Vec3d(☃, ☃x, ☃xx);
      Vec3d ☃xxxxxxxx = new Vec3d(☃ + ☃xxx, ☃x + ☃xxxx, ☃xx + ☃xxxxx);
      RayTraceResult ☃xxxxxxxxx = ☃xxxxxx.rayTraceBlocks(☃xxxxxxx, ☃xxxxxxxx, false, true, false);
      if (☃) {
         if (☃xxxxxxxxx != null) {
            ☃xxxxxxxx = new Vec3d(☃xxxxxxxxx.hitVec.x, ☃xxxxxxxxx.hitVec.y, ☃xxxxxxxxx.hitVec.z);
         }

         Entity ☃xxxxxxxxxx = null;
         List<Entity> ☃xxxxxxxxxxx = ☃xxxxxx.getEntitiesWithinAABBExcludingEntity(☃, ☃.getEntityBoundingBox().expand(☃xxx, ☃xxxx, ☃xxxxx).grow(1.0));
         double ☃xxxxxxxxxxxx = 0.0;

         for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < ☃xxxxxxxxxxx.size(); ☃xxxxxxxxxxxxx++) {
            Entity ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxx.get(☃xxxxxxxxxxxxx);
            if (☃xxxxxxxxxxxxxx.canBeCollidedWith() && (☃ || !☃xxxxxxxxxxxxxx.isEntityEqual(☃)) && !☃xxxxxxxxxxxxxx.noClip) {
               AxisAlignedBB ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.getEntityBoundingBox().grow(0.3F);
               RayTraceResult ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.calculateIntercept(☃xxxxxxx, ☃xxxxxxxx);
               if (☃xxxxxxxxxxxxxxxx != null) {
                  double ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxx.squareDistanceTo(☃xxxxxxxxxxxxxxxx.hitVec);
                  if (☃xxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxx || ☃xxxxxxxxxxxx == 0.0) {
                     ☃xxxxxxxxxx = ☃xxxxxxxxxxxxxx;
                     ☃xxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxx;
                  }
               }
            }
         }

         if (☃xxxxxxxxxx != null) {
            ☃xxxxxxxxx = new RayTraceResult(☃xxxxxxxxxx);
         }
      }

      return ☃xxxxxxxxx;
   }

   public static final void rotateTowardsMovement(Entity var0, float var1) {
      double ☃ = ☃.motionX;
      double ☃x = ☃.motionY;
      double ☃xx = ☃.motionZ;
      float ☃xxx = MathHelper.sqrt(☃ * ☃ + ☃xx * ☃xx);
      ☃.rotationYaw = (float)(MathHelper.atan2(☃xx, ☃) * 180.0F / (float)Math.PI) + 90.0F;
      ☃.rotationPitch = (float)(MathHelper.atan2(☃xxx, ☃x) * 180.0F / (float)Math.PI) - 90.0F;

      while (☃.rotationPitch - ☃.prevRotationPitch < -180.0F) {
         ☃.prevRotationPitch -= 360.0F;
      }

      while (☃.rotationPitch - ☃.prevRotationPitch >= 180.0F) {
         ☃.prevRotationPitch += 360.0F;
      }

      while (☃.rotationYaw - ☃.prevRotationYaw < -180.0F) {
         ☃.prevRotationYaw -= 360.0F;
      }

      while (☃.rotationYaw - ☃.prevRotationYaw >= 180.0F) {
         ☃.prevRotationYaw += 360.0F;
      }

      ☃.rotationPitch = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
      ☃.rotationYaw = ☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃;
   }
}
