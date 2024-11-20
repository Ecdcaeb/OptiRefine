/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager$BooleanState
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.FogState {
    public GlStateManager.BooleanState fog = new GlStateManager.BooleanState(2912);
    public int mode = 2048;
    public float density = 1.0f;
    public float start;
    public float end = 1.0f;

    private GlStateManager.FogState() {
    }
}
