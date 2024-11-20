/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package net.minecraft.client.renderer;

public static enum GlStateManager.CullFace {
    FRONT(1028),
    BACK(1029),
    FRONT_AND_BACK(1032);

    public final int mode;

    private GlStateManager.CullFace(int n2) {
        this.mode = n2;
    }
}
