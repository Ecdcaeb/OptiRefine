/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager$BooleanState
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.BlendState {
    public GlStateManager.BooleanState blend = new GlStateManager.BooleanState(3042);
    public int srcFactor = 1;
    public int dstFactor = 0;
    public int srcFactorAlpha = 1;
    public int dstFactorAlpha = 0;

    private GlStateManager.BlendState() {
    }
}
