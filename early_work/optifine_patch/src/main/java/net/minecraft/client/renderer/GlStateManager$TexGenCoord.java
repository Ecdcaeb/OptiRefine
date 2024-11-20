/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager$BooleanState
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

static class GlStateManager.TexGenCoord {
    public GlStateManager.BooleanState textureGen;
    public int coord;
    public int param = -1;

    public GlStateManager.TexGenCoord(int coordIn, int capabilityIn) {
        this.coord = coordIn;
        this.textureGen = new GlStateManager.BooleanState(capabilityIn);
    }
}
