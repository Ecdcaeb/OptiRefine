package net.minecraft.client.renderer.culling;

public class ClippingHelper {
   public float[][] frustum = new float[6][4];
   public float[] projectionMatrix = new float[16];
   public float[] modelviewMatrix = new float[16];
   public float[] clippingMatrix = new float[16];

   private double dot(float[] var1, double var2, double var4, double var6) {
      return ☃[0] * ☃ + ☃[1] * ☃ + ☃[2] * ☃ + ☃[3];
   }

   public boolean isBoxInFrustum(double var1, double var3, double var5, double var7, double var9, double var11) {
      for (int ☃ = 0; ☃ < 6; ☃++) {
         float[] ☃x = this.frustum[☃];
         if (!(this.dot(☃x, ☃, ☃, ☃) > 0.0)
            && !(this.dot(☃x, ☃, ☃, ☃) > 0.0)
            && !(this.dot(☃x, ☃, ☃, ☃) > 0.0)
            && !(this.dot(☃x, ☃, ☃, ☃) > 0.0)
            && !(this.dot(☃x, ☃, ☃, ☃) > 0.0)
            && !(this.dot(☃x, ☃, ☃, ☃) > 0.0)
            && !(this.dot(☃x, ☃, ☃, ☃) > 0.0)
            && !(this.dot(☃x, ☃, ☃, ☃) > 0.0)) {
            return false;
         }
      }

      return true;
   }
}
