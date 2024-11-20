/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  net.minecraft.util.EnumFacing
 */
package net.minecraft.client.renderer.chunk;

import net.minecraft.util.EnumFacing;

static class VisGraph.1 {
    static final /* synthetic */ int[] field_178617_a;

    static {
        field_178617_a = new int[EnumFacing.values().length];
        try {
            VisGraph.1.field_178617_a[EnumFacing.DOWN.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            VisGraph.1.field_178617_a[EnumFacing.UP.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            VisGraph.1.field_178617_a[EnumFacing.NORTH.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            VisGraph.1.field_178617_a[EnumFacing.SOUTH.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            VisGraph.1.field_178617_a[EnumFacing.WEST.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            VisGraph.1.field_178617_a[EnumFacing.EAST.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
