/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$Profile
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

/*
 * Exception performing whole class analysis ignored.
 */
static final class GlStateManager.Profile.3
extends GlStateManager.Profile {
    GlStateManager.Profile.3() {
        super(string, n, null);
    }

    public void apply() {
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)0.15f);
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc((int)516, (float)0.003921569f);
    }

    public void clean() {
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        GlStateManager.depthMask((boolean)true);
    }
}
