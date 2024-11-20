/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumFacing$Axis
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;

static class FaceBakery.5 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$util$EnumFacing;
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$util$EnumFacing$Axis;

    static {
        $SwitchMap$net$minecraft$util$EnumFacing$Axis = new int[EnumFacing.Axis.values().length];
        try {
            FaceBakery.5.$SwitchMap$net$minecraft$util$EnumFacing$Axis[EnumFacing.Axis.X.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FaceBakery.5.$SwitchMap$net$minecraft$util$EnumFacing$Axis[EnumFacing.Axis.Y.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FaceBakery.5.$SwitchMap$net$minecraft$util$EnumFacing$Axis[EnumFacing.Axis.Z.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$net$minecraft$util$EnumFacing = new int[EnumFacing.values().length];
        try {
            FaceBakery.5.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.DOWN.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FaceBakery.5.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.UP.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FaceBakery.5.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.NORTH.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FaceBakery.5.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.SOUTH.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FaceBakery.5.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.WEST.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FaceBakery.5.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.EAST.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
