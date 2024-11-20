/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package net.minecraft.client.renderer;

public static enum GlStateManager.DestFactor {
    CONSTANT_ALPHA(32771),
    CONSTANT_COLOR(32769),
    DST_ALPHA(772),
    DST_COLOR(774),
    ONE(1),
    ONE_MINUS_CONSTANT_ALPHA(32772),
    ONE_MINUS_CONSTANT_COLOR(32770),
    ONE_MINUS_DST_ALPHA(773),
    ONE_MINUS_DST_COLOR(775),
    ONE_MINUS_SRC_ALPHA(771),
    ONE_MINUS_SRC_COLOR(769),
    SRC_ALPHA(770),
    SRC_COLOR(768),
    ZERO(0);

    public final int factor;

    private GlStateManager.DestFactor(int factorIn) {
        this.factor = factorIn;
    }
}
