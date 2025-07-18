package net.minecraft.client.renderer.culling;

import net.minecraft.util.math.AxisAlignedBB;

public class Frustum implements ICamera {
   private final ClippingHelper clippingHelper;
   private double x;
   private double y;
   private double z;

   public Frustum() {
      this(ClippingHelperImpl.getInstance());
   }

   public Frustum(ClippingHelper var1) {
      this.clippingHelper = ☃;
   }

   @Override
   public void setPosition(double var1, double var3, double var5) {
      this.x = ☃;
      this.y = ☃;
      this.z = ☃;
   }

   public boolean isBoxInFrustum(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.clippingHelper.isBoxInFrustum(☃ - this.x, ☃ - this.y, ☃ - this.z, ☃ - this.x, ☃ - this.y, ☃ - this.z);
   }

   @Override
   public boolean isBoundingBoxInFrustum(AxisAlignedBB var1) {
      return this.isBoxInFrustum(☃.minX, ☃.minY, ☃.minZ, ☃.maxX, ☃.maxY, ☃.maxZ);
   }
}
