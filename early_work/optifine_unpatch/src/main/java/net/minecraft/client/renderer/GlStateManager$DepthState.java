/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager$BooleanState
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.DepthState {
    public GlStateManager.BooleanState depthTest = new GlStateManager.BooleanState(2929);
    public boolean maskEnabled = true;
    public int depthFunc = 513;

    private GlStateManager.DepthState() {
    }
}
