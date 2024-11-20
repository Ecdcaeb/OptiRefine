/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package net.minecraft.client.renderer;

public static enum GlStateManager.FogMode {
    LINEAR(9729),
    EXP(2048),
    EXP2(2049);

    public final int capabilityId;

    private GlStateManager.FogMode(int capabilityIn) {
        this.capabilityId = capabilityIn;
    }
}
