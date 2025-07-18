package net.minecraft.client.renderer.culling;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class ClippingHelperImpl extends ClippingHelper {
   private static final ClippingHelperImpl instance = new ClippingHelperImpl();
   private final FloatBuffer projectionMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer modelviewMatrixBuffer = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer floatBuffer16 = GLAllocation.createDirectFloatBuffer(16);

   public static ClippingHelper getInstance() {
      instance.init();
      return instance;
   }

   private void normalize(float[] var1) {
      float ☃ = MathHelper.sqrt(☃[0] * ☃[0] + ☃[1] * ☃[1] + ☃[2] * ☃[2]);
      ☃[0] /= ☃;
      ☃[1] /= ☃;
      ☃[2] /= ☃;
      ☃[3] /= ☃;
   }

   public void init() {
      ((Buffer)this.projectionMatrixBuffer).clear();
      ((Buffer)this.modelviewMatrixBuffer).clear();
      ((Buffer)this.floatBuffer16).clear();
      GlStateManager.getFloat(2983, this.projectionMatrixBuffer);
      GlStateManager.getFloat(2982, this.modelviewMatrixBuffer);
      float[] ☃ = this.projectionMatrix;
      float[] ☃x = this.modelviewMatrix;
      ((Buffer)this.projectionMatrixBuffer).flip().limit(16);
      this.projectionMatrixBuffer.get(☃);
      ((Buffer)this.modelviewMatrixBuffer).flip().limit(16);
      this.modelviewMatrixBuffer.get(☃x);
      this.clippingMatrix[0] = ☃x[0] * ☃[0] + ☃x[1] * ☃[4] + ☃x[2] * ☃[8] + ☃x[3] * ☃[12];
      this.clippingMatrix[1] = ☃x[0] * ☃[1] + ☃x[1] * ☃[5] + ☃x[2] * ☃[9] + ☃x[3] * ☃[13];
      this.clippingMatrix[2] = ☃x[0] * ☃[2] + ☃x[1] * ☃[6] + ☃x[2] * ☃[10] + ☃x[3] * ☃[14];
      this.clippingMatrix[3] = ☃x[0] * ☃[3] + ☃x[1] * ☃[7] + ☃x[2] * ☃[11] + ☃x[3] * ☃[15];
      this.clippingMatrix[4] = ☃x[4] * ☃[0] + ☃x[5] * ☃[4] + ☃x[6] * ☃[8] + ☃x[7] * ☃[12];
      this.clippingMatrix[5] = ☃x[4] * ☃[1] + ☃x[5] * ☃[5] + ☃x[6] * ☃[9] + ☃x[7] * ☃[13];
      this.clippingMatrix[6] = ☃x[4] * ☃[2] + ☃x[5] * ☃[6] + ☃x[6] * ☃[10] + ☃x[7] * ☃[14];
      this.clippingMatrix[7] = ☃x[4] * ☃[3] + ☃x[5] * ☃[7] + ☃x[6] * ☃[11] + ☃x[7] * ☃[15];
      this.clippingMatrix[8] = ☃x[8] * ☃[0] + ☃x[9] * ☃[4] + ☃x[10] * ☃[8] + ☃x[11] * ☃[12];
      this.clippingMatrix[9] = ☃x[8] * ☃[1] + ☃x[9] * ☃[5] + ☃x[10] * ☃[9] + ☃x[11] * ☃[13];
      this.clippingMatrix[10] = ☃x[8] * ☃[2] + ☃x[9] * ☃[6] + ☃x[10] * ☃[10] + ☃x[11] * ☃[14];
      this.clippingMatrix[11] = ☃x[8] * ☃[3] + ☃x[9] * ☃[7] + ☃x[10] * ☃[11] + ☃x[11] * ☃[15];
      this.clippingMatrix[12] = ☃x[12] * ☃[0] + ☃x[13] * ☃[4] + ☃x[14] * ☃[8] + ☃x[15] * ☃[12];
      this.clippingMatrix[13] = ☃x[12] * ☃[1] + ☃x[13] * ☃[5] + ☃x[14] * ☃[9] + ☃x[15] * ☃[13];
      this.clippingMatrix[14] = ☃x[12] * ☃[2] + ☃x[13] * ☃[6] + ☃x[14] * ☃[10] + ☃x[15] * ☃[14];
      this.clippingMatrix[15] = ☃x[12] * ☃[3] + ☃x[13] * ☃[7] + ☃x[14] * ☃[11] + ☃x[15] * ☃[15];
      float[] ☃xx = this.frustum[0];
      ☃xx[0] = this.clippingMatrix[3] - this.clippingMatrix[0];
      ☃xx[1] = this.clippingMatrix[7] - this.clippingMatrix[4];
      ☃xx[2] = this.clippingMatrix[11] - this.clippingMatrix[8];
      ☃xx[3] = this.clippingMatrix[15] - this.clippingMatrix[12];
      this.normalize(☃xx);
      float[] ☃xxx = this.frustum[1];
      ☃xxx[0] = this.clippingMatrix[3] + this.clippingMatrix[0];
      ☃xxx[1] = this.clippingMatrix[7] + this.clippingMatrix[4];
      ☃xxx[2] = this.clippingMatrix[11] + this.clippingMatrix[8];
      ☃xxx[3] = this.clippingMatrix[15] + this.clippingMatrix[12];
      this.normalize(☃xxx);
      float[] ☃xxxx = this.frustum[2];
      ☃xxxx[0] = this.clippingMatrix[3] + this.clippingMatrix[1];
      ☃xxxx[1] = this.clippingMatrix[7] + this.clippingMatrix[5];
      ☃xxxx[2] = this.clippingMatrix[11] + this.clippingMatrix[9];
      ☃xxxx[3] = this.clippingMatrix[15] + this.clippingMatrix[13];
      this.normalize(☃xxxx);
      float[] ☃xxxxx = this.frustum[3];
      ☃xxxxx[0] = this.clippingMatrix[3] - this.clippingMatrix[1];
      ☃xxxxx[1] = this.clippingMatrix[7] - this.clippingMatrix[5];
      ☃xxxxx[2] = this.clippingMatrix[11] - this.clippingMatrix[9];
      ☃xxxxx[3] = this.clippingMatrix[15] - this.clippingMatrix[13];
      this.normalize(☃xxxxx);
      float[] ☃xxxxxx = this.frustum[4];
      ☃xxxxxx[0] = this.clippingMatrix[3] - this.clippingMatrix[2];
      ☃xxxxxx[1] = this.clippingMatrix[7] - this.clippingMatrix[6];
      ☃xxxxxx[2] = this.clippingMatrix[11] - this.clippingMatrix[10];
      ☃xxxxxx[3] = this.clippingMatrix[15] - this.clippingMatrix[14];
      this.normalize(☃xxxxxx);
      float[] ☃xxxxxxx = this.frustum[5];
      ☃xxxxxxx[0] = this.clippingMatrix[3] + this.clippingMatrix[2];
      ☃xxxxxxx[1] = this.clippingMatrix[7] + this.clippingMatrix[6];
      ☃xxxxxxx[2] = this.clippingMatrix[11] + this.clippingMatrix[10];
      ☃xxxxxxx[3] = this.clippingMatrix[15] + this.clippingMatrix[14];
      this.normalize(☃xxxxxxx);
   }
}
