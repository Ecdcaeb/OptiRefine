/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager$StencilFunc
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.StencilState {
    public GlStateManager.StencilFunc func = new GlStateManager.StencilFunc(null);
    public int mask = -1;
    public int fail = 7680;
    public int zfail = 7680;
    public int zpass = 7680;

    private GlStateManager.StencilState() {
    }
}
