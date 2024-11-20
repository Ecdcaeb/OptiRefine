/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  net.minecraft.client.renderer.OpenGlHelper$FboMode
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.OpenGlHelper;

/*
 * Exception performing whole class analysis ignored.
 */
static class OpenGlHelper.1 {
    static final /* synthetic */ int[] field_188784_a;

    static {
        field_188784_a = new int[OpenGlHelper.FboMode.values().length];
        try {
            OpenGlHelper.1.field_188784_a[OpenGlHelper.FboMode.BASE.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            OpenGlHelper.1.field_188784_a[OpenGlHelper.FboMode.ARB.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            OpenGlHelper.1.field_188784_a[OpenGlHelper.FboMode.EXT.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
