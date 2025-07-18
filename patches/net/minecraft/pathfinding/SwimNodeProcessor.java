package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class SwimNodeProcessor extends NodeProcessor {
   @Override
   public PathPoint getStart() {
      return this.openPoint(
         MathHelper.floor(this.entity.getEntityBoundingBox().minX),
         MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5),
         MathHelper.floor(this.entity.getEntityBoundingBox().minZ)
      );
   }

   @Override
   public PathPoint getPathPointToCoords(double var1, double var3, double var5) {
      return this.openPoint(MathHelper.floor(☃ - this.entity.width / 2.0F), MathHelper.floor(☃ + 0.5), MathHelper.floor(☃ - this.entity.width / 2.0F));
   }

   @Override
   public int findPathOptions(PathPoint[] var1, PathPoint var2, PathPoint var3, float var4) {
      int ☃ = 0;

      for (EnumFacing ☃x : EnumFacing.values()) {
         PathPoint ☃xx = this.getWaterNode(☃.x + ☃x.getXOffset(), ☃.y + ☃x.getYOffset(), ☃.z + ☃x.getZOffset());
         if (☃xx != null && !☃xx.visited && ☃xx.distanceTo(☃) < ☃) {
            ☃[☃++] = ☃xx;
         }
      }

      return ☃;
   }

   @Override
   public PathNodeType getPathNodeType(
      IBlockAccess var1, int var2, int var3, int var4, EntityLiving var5, int var6, int var7, int var8, boolean var9, boolean var10
   ) {
      return PathNodeType.WATER;
   }

   @Override
   public PathNodeType getPathNodeType(IBlockAccess var1, int var2, int var3, int var4) {
      return PathNodeType.WATER;
   }

   @Nullable
   private PathPoint getWaterNode(int var1, int var2, int var3) {
      PathNodeType ☃ = this.isFree(☃, ☃, ☃);
      return ☃ == PathNodeType.WATER ? this.openPoint(☃, ☃, ☃) : null;
   }

   private PathNodeType isFree(int var1, int var2, int var3) {
      BlockPos.MutableBlockPos ☃ = new BlockPos.MutableBlockPos();

      for (int ☃x = ☃; ☃x < ☃ + this.entitySizeX; ☃x++) {
         for (int ☃xx = ☃; ☃xx < ☃ + this.entitySizeY; ☃xx++) {
            for (int ☃xxx = ☃; ☃xxx < ☃ + this.entitySizeZ; ☃xxx++) {
               IBlockState ☃xxxx = this.blockaccess.getBlockState(☃.setPos(☃x, ☃xx, ☃xxx));
               if (☃xxxx.getMaterial() != Material.WATER) {
                  return PathNodeType.BLOCKED;
               }
            }
         }
      }

      return PathNodeType.WATER;
   }
}
