/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumFacing$Axis
 *  net.minecraft.util.EnumFacing$Plane
 */
package net.minecraft.util;

import net.minecraft.util.EnumFacing;

/*
 * Exception performing whole class analysis ignored.
 */
static class EnumFacing.1 {
    static final /* synthetic */ int[] field_179515_a;
    static final /* synthetic */ int[] field_179513_b;
    static final /* synthetic */ int[] field_179514_c;

    static {
        field_179514_c = new int[EnumFacing.Plane.values().length];
        try {
            EnumFacing.1.field_179514_c[EnumFacing.Plane.HORIZONTAL.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EnumFacing.1.field_179514_c[EnumFacing.Plane.VERTICAL.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        field_179513_b = new int[EnumFacing.values().length];
        try {
            EnumFacing.1.field_179513_b[EnumFacing.NORTH.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EnumFacing.1.field_179513_b[EnumFacing.EAST.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EnumFacing.1.field_179513_b[EnumFacing.SOUTH.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EnumFacing.1.field_179513_b[EnumFacing.WEST.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EnumFacing.1.field_179513_b[EnumFacing.UP.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EnumFacing.1.field_179513_b[EnumFacing.DOWN.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        field_179515_a = new int[EnumFacing.Axis.values().length];
        try {
            EnumFacing.1.field_179515_a[EnumFacing.Axis.X.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EnumFacing.1.field_179515_a[EnumFacing.Axis.Y.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            EnumFacing.1.field_179515_a[EnumFacing.Axis.Z.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
