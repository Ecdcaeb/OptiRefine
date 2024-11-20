/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;

static class GlStateManager.BooleanState {
    private final int capability;
    private boolean currentState;

    public GlStateManager.BooleanState(int n) {
        this.capability = n;
    }

    public void setDisabled() {
        this.setState(false);
    }

    public void setEnabled() {
        this.setState(true);
    }

    public void setState(boolean bl) {
        if (bl != this.currentState) {
            this.currentState = bl;
            if (bl) {
                GL11.glEnable((int)this.capability);
            } else {
                GL11.glDisable((int)this.capability);
            }
        }
    }
}
