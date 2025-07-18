package net.minecraft.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class RayTraceResult {
   private BlockPos blockPos;
   public RayTraceResult.Type typeOfHit;
   public EnumFacing sideHit;
   public Vec3d hitVec;
   public Entity entityHit;

   public RayTraceResult(Vec3d var1, EnumFacing var2, BlockPos var3) {
      this(RayTraceResult.Type.BLOCK, ☃, ☃, ☃);
   }

   public RayTraceResult(Vec3d var1, EnumFacing var2) {
      this(RayTraceResult.Type.BLOCK, ☃, ☃, BlockPos.ORIGIN);
   }

   public RayTraceResult(Entity var1) {
      this(☃, new Vec3d(☃.posX, ☃.posY, ☃.posZ));
   }

   public RayTraceResult(RayTraceResult.Type var1, Vec3d var2, EnumFacing var3, BlockPos var4) {
      this.typeOfHit = ☃;
      this.blockPos = ☃;
      this.sideHit = ☃;
      this.hitVec = new Vec3d(☃.x, ☃.y, ☃.z);
   }

   public RayTraceResult(Entity var1, Vec3d var2) {
      this.typeOfHit = RayTraceResult.Type.ENTITY;
      this.entityHit = ☃;
      this.hitVec = ☃;
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }

   @Override
   public String toString() {
      return "HitResult{type="
         + this.typeOfHit
         + ", blockpos="
         + this.blockPos
         + ", f="
         + this.sideHit
         + ", pos="
         + this.hitVec
         + ", entity="
         + this.entityHit
         + '}';
   }

   public static enum Type {
      MISS,
      BLOCK,
      ENTITY;
   }
}
