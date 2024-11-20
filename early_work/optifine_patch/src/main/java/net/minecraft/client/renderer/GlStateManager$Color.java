/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package net.minecraft.client.renderer;

static class GlStateManager.Color {
    public float red = 1.0f;
    public float green = 1.0f;
    public float blue = 1.0f;
    public float alpha = 1.0f;

    public GlStateManager.Color() {
        this(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public GlStateManager.Color(float redIn, float greenIn, float blueIn, float alphaIn) {
        this.red = redIn;
        this.green = greenIn;
        this.blue = blueIn;
        this.alpha = alphaIn;
    }
}
