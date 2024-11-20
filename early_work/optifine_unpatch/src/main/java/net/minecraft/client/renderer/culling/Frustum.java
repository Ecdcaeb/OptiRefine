/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.culling.ClippingHelper
 *  net.minecraft.client.renderer.culling.ClippingHelperImpl
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.util.math.AxisAlignedBB
 */
package net.minecraft.client.renderer.culling;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.math.AxisAlignedBB;

public class Frustum
implements ICamera {
    private final ClippingHelper clippingHelper;
    private double x;
    private double y;
    private double z;

    public Frustum() {
        this(ClippingHelperImpl.getInstance());
    }

    public Frustum(ClippingHelper clippingHelper) {
        this.clippingHelper = clippingHelper;
    }

    public void setPosition(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public boolean isBoxInFrustum(double d, double d2, double d3, double d4, double d5, double d6) {
        return this.clippingHelper.isBoxInFrustum(d - this.x, d2 - this.y, d3 - this.z, d4 - this.x, d5 - this.y, d6 - this.z);
    }

    public boolean isBoundingBoxInFrustum(AxisAlignedBB axisAlignedBB) {
        return this.isBoxInFrustum(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    }
}
