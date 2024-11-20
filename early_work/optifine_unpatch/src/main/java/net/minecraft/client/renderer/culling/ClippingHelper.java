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

    private double dot(float[] fArray, double d, double d2, double d3) {
        return (double)fArray[0] * d + (double)fArray[1] * d2 + (double)fArray[2] * d3 + (double)fArray[3];
    }

    public boolean isBoxInFrustum(double d, double d2, double d3, double d4, double d5, double d6) {
        for (int i = 0; i < 6; ++i) {
            float[] fArray = this.frustum[i];
            if (this.dot(fArray, d, d2, d3) > 0.0 || this.dot(fArray, d4, d2, d3) > 0.0 || this.dot(fArray, d, d5, d3) > 0.0 || this.dot(fArray, d4, d5, d3) > 0.0 || this.dot(fArray, d, d2, d6) > 0.0 || this.dot(fArray, d4, d2, d6) > 0.0 || this.dot(fArray, d, d5, d6) > 0.0 || this.dot(fArray, d4, d5, d6) > 0.0) continue;
            return false;
        }
        return true;
    }
}
