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

    public Frustum(ClippingHelper clippingHelperIn) {
        this.clippingHelper = clippingHelperIn;
    }

    public void setPosition(double xIn, double yIn, double zIn) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }

    public boolean isBoxInFrustum(double p_78548_1_, double p_78548_3_, double p_78548_5_, double p_78548_7_, double p_78548_9_, double p_78548_11_) {
        return this.clippingHelper.isBoxInFrustum(p_78548_1_ - this.x, p_78548_3_ - this.y, p_78548_5_ - this.z, p_78548_7_ - this.x, p_78548_9_ - this.y, p_78548_11_ - this.z);
    }

    public boolean isBoundingBoxInFrustum(AxisAlignedBB p_78546_1_) {
        return this.isBoxInFrustum(p_78546_1_.minX, p_78546_1_.minY, p_78546_1_.minZ, p_78546_1_.maxX, p_78546_1_.maxY, p_78546_1_.maxZ);
    }

    public boolean isBoxInFrustumFully(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.clippingHelper.isBoxInFrustumFully(minX - this.x, minY - this.y, minZ - this.z, maxX - this.x, maxY - this.y, maxZ - this.z);
    }
}
