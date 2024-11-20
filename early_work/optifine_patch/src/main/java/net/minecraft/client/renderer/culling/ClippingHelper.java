/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package net.minecraft.client.renderer.culling;

public class ClippingHelper {
    public float[][] frustum = new float[6][4];
    public float[] projectionMatrix = new float[16];
    public float[] modelviewMatrix = new float[16];
    public float[] clippingMatrix = new float[16];
    public boolean disabled = false;

    private float dot(float[] p_178624_1_, float p_178624_2_, float p_178624_4_, float p_178624_6_) {
        return p_178624_1_[0] * p_178624_2_ + p_178624_1_[1] * p_178624_4_ + p_178624_1_[2] * p_178624_6_ + p_178624_1_[3];
    }

    public boolean isBoxInFrustum(double p_78553_1_, double p_78553_3_, double p_78553_5_, double p_78553_7_, double p_78553_9_, double p_78553_11_) {
        if (this.disabled) {
            return true;
        }
        float minXf = (float)p_78553_1_;
        float minYf = (float)p_78553_3_;
        float minZf = (float)p_78553_5_;
        float maxXf = (float)p_78553_7_;
        float maxYf = (float)p_78553_9_;
        float maxZf = (float)p_78553_11_;
        for (int var13 = 0; var13 < 6; ++var13) {
            float[] frustumi = this.frustum[var13];
            float frustumi0 = frustumi[0];
            float frustumi1 = frustumi[1];
            float frustumi2 = frustumi[2];
            float frustumi3 = frustumi[3];
            if (!(frustumi0 * minXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0f) || !(frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0f) || !(frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0f) || !(frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0f) || !(frustumi0 * minXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0f) || !(frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0f) || !(frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0f) || !(frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0f)) continue;
            return false;
        }
        return true;
    }

    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (this.disabled) {
            return true;
        }
        float minXf = (float)minX;
        float minYf = (float)minY;
        float minZf = (float)minZ;
        float maxXf = (float)maxX;
        float maxYf = (float)maxY;
        float maxZf = (float)maxZ;
        for (int i = 0; i < 6; ++i) {
            float[] frustumi = this.frustum[i];
            float frustumi0 = frustumi[0];
            float frustumi1 = frustumi[1];
            float frustumi2 = frustumi[2];
            float frustumi3 = frustumi[3];
            if (!(i < 4 ? frustumi0 * minXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0f || frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0f || frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0f || frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0f || frustumi0 * minXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0f || frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0f || frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0f || frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0f : frustumi0 * minXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0f && frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * minZf + frustumi3 <= 0.0f && frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0f && frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * minZf + frustumi3 <= 0.0f && frustumi0 * minXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0f && frustumi0 * maxXf + frustumi1 * minYf + frustumi2 * maxZf + frustumi3 <= 0.0f && frustumi0 * minXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0f && frustumi0 * maxXf + frustumi1 * maxYf + frustumi2 * maxZf + frustumi3 <= 0.0f)) continue;
            return false;
        }
        return true;
    }
}
