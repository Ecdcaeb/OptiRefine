/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.debug.DebugRenderer$IDebugRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 */
package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class DebugRendererChunkBorder
implements DebugRenderer.IDebugRenderer {
    private final Minecraft minecraft;

    public DebugRendererChunkBorder(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void render(float f, long l) {
        int n;
        EntityPlayerSP entityPlayerSP = this.minecraft.player;
        Tessellator \u26032 = Tessellator.getInstance();
        BufferBuilder \u26033 = \u26032.getBuffer();
        double \u26034 = entityPlayerSP.M + (entityPlayerSP.p - entityPlayerSP.M) * (double)f;
        double \u26035 = entityPlayerSP.N + (entityPlayerSP.q - entityPlayerSP.N) * (double)f;
        double \u26036 = entityPlayerSP.O + (entityPlayerSP.r - entityPlayerSP.O) * (double)f;
        double \u26037 = 0.0 - \u26035;
        double \u26038 = 256.0 - \u26035;
        GlStateManager.disableTexture2D();
        GlStateManager.disableBlend();
        double \u26039 = (double)(entityPlayerSP.ab << 4) - \u26034;
        double \u260310 = (double)(entityPlayerSP.ad << 4) - \u26036;
        GlStateManager.glLineWidth((float)1.0f);
        \u26033.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for (n = -16; n <= 32; n += 16) {
            for (\u2603 = -16; \u2603 <= 32; \u2603 += 16) {
                \u26033.pos(\u26039 + (double)n, \u26037, \u260310 + (double)\u2603).color(1.0f, 0.0f, 0.0f, 0.0f).endVertex();
                \u26033.pos(\u26039 + (double)n, \u26037, \u260310 + (double)\u2603).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                \u26033.pos(\u26039 + (double)n, \u26038, \u260310 + (double)\u2603).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                \u26033.pos(\u26039 + (double)n, \u26038, \u260310 + (double)\u2603).color(1.0f, 0.0f, 0.0f, 0.0f).endVertex();
            }
        }
        for (n = 2; n < 16; n += 2) {
            \u26033.pos(\u26039 + (double)n, \u26037, \u260310).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            \u26033.pos(\u26039 + (double)n, \u26037, \u260310).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + (double)n, \u26038, \u260310).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + (double)n, \u26038, \u260310).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            \u26033.pos(\u26039 + (double)n, \u26037, \u260310 + 16.0).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            \u26033.pos(\u26039 + (double)n, \u26037, \u260310 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + (double)n, \u26038, \u260310 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + (double)n, \u26038, \u260310 + 16.0).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
        }
        for (n = 2; n < 16; n += 2) {
            \u26033.pos(\u26039, \u26037, \u260310 + (double)n).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            \u26033.pos(\u26039, \u26037, \u260310 + (double)n).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039, \u26038, \u260310 + (double)n).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039, \u26038, \u260310 + (double)n).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            \u26033.pos(\u26039 + 16.0, \u26037, \u260310 + (double)n).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            \u26033.pos(\u26039 + 16.0, \u26037, \u260310 + (double)n).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + 16.0, \u26038, \u260310 + (double)n).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + 16.0, \u26038, \u260310 + (double)n).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
        }
        for (n = 0; n <= 256; n += 2) {
            double d = (double)n - \u26035;
            \u26033.pos(\u26039, d, \u260310).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            \u26033.pos(\u26039, d, \u260310).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039, d, \u260310 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + 16.0, d, \u260310 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + 16.0, d, \u260310).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039, d, \u260310).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            \u26033.pos(\u26039, d, \u260310).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
        }
        \u26032.draw();
        GlStateManager.glLineWidth((float)2.0f);
        \u26033.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for (n = 0; n <= 16; n += 16) {
            for (\u2603 = 0; \u2603 <= 16; \u2603 += 16) {
                \u26033.pos(\u26039 + (double)n, \u26037, \u260310 + (double)\u2603).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
                \u26033.pos(\u26039 + (double)n, \u26037, \u260310 + (double)\u2603).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                \u26033.pos(\u26039 + (double)n, \u26038, \u260310 + (double)\u2603).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                \u26033.pos(\u26039 + (double)n, \u26038, \u260310 + (double)\u2603).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
            }
        }
        for (n = 0; n <= 256; n += 16) {
            double d = (double)n - \u26035;
            \u26033.pos(\u26039, d, \u260310).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
            \u26033.pos(\u26039, d, \u260310).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            \u26033.pos(\u26039, d, \u260310 + 16.0).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + 16.0, d, \u260310 + 16.0).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            \u26033.pos(\u26039 + 16.0, d, \u260310).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            \u26033.pos(\u26039, d, \u260310).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            \u26033.pos(\u26039, d, \u260310).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
        }
        \u26032.draw();
        GlStateManager.glLineWidth((float)1.0f);
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
    }
}
