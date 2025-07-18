package net.minecraft.entity.ai;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RandomPositionGenerator {
   private static Vec3d staticVector = Vec3d.ZERO;

   @Nullable
   public static Vec3d findRandomTarget(EntityCreature var0, int var1, int var2) {
      return findRandomTargetBlock(☃, ☃, ☃, null);
   }

   @Nullable
   public static Vec3d getLandPos(EntityCreature var0, int var1, int var2) {
      return generateRandomPos(☃, ☃, ☃, null, false);
   }

   @Nullable
   public static Vec3d findRandomTargetBlockTowards(EntityCreature var0, int var1, int var2, Vec3d var3) {
      staticVector = ☃.subtract(☃.posX, ☃.posY, ☃.posZ);
      return findRandomTargetBlock(☃, ☃, ☃, staticVector);
   }

   @Nullable
   public static Vec3d findRandomTargetBlockAwayFrom(EntityCreature var0, int var1, int var2, Vec3d var3) {
      staticVector = new Vec3d(☃.posX, ☃.posY, ☃.posZ).subtract(☃);
      return findRandomTargetBlock(☃, ☃, ☃, staticVector);
   }

   @Nullable
   private static Vec3d findRandomTargetBlock(EntityCreature var0, int var1, int var2, @Nullable Vec3d var3) {
      return generateRandomPos(☃, ☃, ☃, ☃, true);
   }

   @Nullable
   private static Vec3d generateRandomPos(EntityCreature var0, int var1, int var2, @Nullable Vec3d var3, boolean var4) {
      PathNavigate ☃ = ☃.getNavigator();
      Random ☃x = ☃.getRNG();
      boolean ☃xx;
      if (☃.hasHome()) {
         double ☃xxx = ☃.getHomePosition().distanceSq(MathHelper.floor(☃.posX), MathHelper.floor(☃.posY), MathHelper.floor(☃.posZ)) + 4.0;
         double ☃xxxx = ☃.getMaximumHomeDistance() + ☃;
         ☃xx = ☃xxx < ☃xxxx * ☃xxxx;
      } else {
         ☃xx = false;
      }

      boolean ☃xxx = false;
      float ☃xxxx = -99999.0F;
      int ☃xxxxx = 0;
      int ☃xxxxxx = 0;
      int ☃xxxxxxx = 0;

      for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < 10; ☃xxxxxxxx++) {
         int ☃xxxxxxxxx = ☃x.nextInt(2 * ☃ + 1) - ☃;
         int ☃xxxxxxxxxx = ☃x.nextInt(2 * ☃ + 1) - ☃;
         int ☃xxxxxxxxxxx = ☃x.nextInt(2 * ☃ + 1) - ☃;
         if (☃ == null || !(☃xxxxxxxxx * ☃.x + ☃xxxxxxxxxxx * ☃.z < 0.0)) {
            if (☃.hasHome() && ☃ > 1) {
               BlockPos ☃xxxxxxxxxxxx = ☃.getHomePosition();
               if (☃.posX > ☃xxxxxxxxxxxx.getX()) {
                  ☃xxxxxxxxx -= ☃x.nextInt(☃ / 2);
               } else {
                  ☃xxxxxxxxx += ☃x.nextInt(☃ / 2);
               }

               if (☃.posZ > ☃xxxxxxxxxxxx.getZ()) {
                  ☃xxxxxxxxxxx -= ☃x.nextInt(☃ / 2);
               } else {
                  ☃xxxxxxxxxxx += ☃x.nextInt(☃ / 2);
               }
            }

            BlockPos ☃xxxxxxxxxxxxx = new BlockPos(☃xxxxxxxxx + ☃.posX, ☃xxxxxxxxxx + ☃.posY, ☃xxxxxxxxxxx + ☃.posZ);
            if ((!☃xx || ☃.isWithinHomeDistanceFromPosition(☃xxxxxxxxxxxxx)) && ☃.canEntityStandOnPos(☃xxxxxxxxxxxxx)) {
               if (!☃) {
                  ☃xxxxxxxxxxxxx = moveAboveSolid(☃xxxxxxxxxxxxx, ☃);
                  if (isWaterDestination(☃xxxxxxxxxxxxx, ☃)) {
                     continue;
                  }
               }

               float ☃xxxxxxxxxxxxxx = ☃.getBlockPathWeight(☃xxxxxxxxxxxxx);
               if (☃xxxxxxxxxxxxxx > ☃xxxx) {
                  ☃xxxx = ☃xxxxxxxxxxxxxx;
                  ☃xxxxx = ☃xxxxxxxxx;
                  ☃xxxxxx = ☃xxxxxxxxxx;
                  ☃xxxxxxx = ☃xxxxxxxxxxx;
                  ☃xxx = true;
               }
            }
         }
      }

      return ☃xxx ? new Vec3d(☃xxxxx + ☃.posX, ☃xxxxxx + ☃.posY, ☃xxxxxxx + ☃.posZ) : null;
   }

   private static BlockPos moveAboveSolid(BlockPos var0, EntityCreature var1) {
      if (!☃.world.getBlockState(☃).getMaterial().isSolid()) {
         return ☃;
      } else {
         BlockPos ☃ = ☃.up();

         while (☃.getY() < ☃.world.getHeight() && ☃.world.getBlockState(☃).getMaterial().isSolid()) {
            ☃ = ☃.up();
         }

         return ☃;
      }
   }

   private static boolean isWaterDestination(BlockPos var0, EntityCreature var1) {
      return ☃.world.getBlockState(☃).getMaterial() == Material.WATER;
   }
}
