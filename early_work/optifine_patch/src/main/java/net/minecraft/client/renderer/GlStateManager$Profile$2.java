/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$Profile
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

/*
 * Exception performing whole class analysis ignored.
 */
static final class GlStateManager.Profile.2
extends GlStateManager.Profile {
    GlStateManager.Profile.2() {
        super(string, n, null);
    }

    public void apply() {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
    }

    public void clean() {
        GlStateManager.disableBlend();
    }
}
