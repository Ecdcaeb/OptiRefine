/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager$BooleanState
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.PolygonOffsetState {
    public GlStateManager.BooleanState polygonOffsetFill = new GlStateManager.BooleanState(32823);
    public GlStateManager.BooleanState polygonOffsetLine = new GlStateManager.BooleanState(10754);
    public float factor;
    public float units;

    private GlStateManager.PolygonOffsetState() {
    }
}
