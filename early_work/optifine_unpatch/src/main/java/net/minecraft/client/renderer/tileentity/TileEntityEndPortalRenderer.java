/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.nio.FloatBuffer
 *  java.util.Random
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.GlStateManager$TexGen
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.tileentity.TileEntityEndPortal
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.ResourceLocation
 */
package net.minecraft.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityEndPortalRenderer
extends TileEntitySpecialRenderer<TileEntityEndPortal> {
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer((int)16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer((int)16);
    private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer((int)16);

    public void render(TileEntityEndPortal tileEntityEndPortal, double d, double d2, double d3, float f, int n, float f2) {
        GlStateManager.disableLighting();
        RANDOM.setSeed(31100L);
        GlStateManager.getFloat((int)2982, (FloatBuffer)MODELVIEW);
        GlStateManager.getFloat((int)2983, (FloatBuffer)PROJECTION);
        double d4 = d * d + d2 * d2 + d3 * d3;
        int \u26032 = this.getPasses(d4);
        float \u26033 = this.getOffset();
        boolean \u26034 = false;
        for (int i = 0; i < \u26032; ++i) {
            GlStateManager.pushMatrix();
            float f3 = 2.0f / (float)(18 - i);
            if (i == 0) {
                this.bindTexture(END_SKY_TEXTURE);
                f3 = 0.15f;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            }
            if (i >= 1) {
                this.bindTexture(END_PORTAL_TEXTURE);
                \u26034 = true;
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            }
            if (i == 1) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
            }
            GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.S, (int)9216);
            GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.T, (int)9216);
            GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.R, (int)9216);
            GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.S, (int)9474, (FloatBuffer)this.getBuffer(1.0f, 0.0f, 0.0f, 0.0f));
            GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.T, (int)9474, (FloatBuffer)this.getBuffer(0.0f, 1.0f, 0.0f, 0.0f));
            GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.R, (int)9474, (FloatBuffer)this.getBuffer(0.0f, 0.0f, 1.0f, 0.0f));
            GlStateManager.enableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.S);
            GlStateManager.enableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.T);
            GlStateManager.enableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.R);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode((int)5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate((float)0.5f, (float)0.5f, (float)0.0f);
            GlStateManager.scale((float)0.5f, (float)0.5f, (float)1.0f);
            \u2603 = i + 1;
            GlStateManager.translate((float)(17.0f / \u2603), (float)((2.0f + \u2603 / 1.5f) * ((float)Minecraft.getSystemTime() % 800000.0f / 800000.0f)), (float)0.0f);
            GlStateManager.rotate((float)((\u2603 * \u2603 * 4321.0f + \u2603 * 9.0f) * 2.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.scale((float)(4.5f - \u2603 / 4.0f), (float)(4.5f - \u2603 / 4.0f), (float)1.0f);
            GlStateManager.multMatrix((FloatBuffer)PROJECTION);
            GlStateManager.multMatrix((FloatBuffer)MODELVIEW);
            Tessellator \u26035 = Tessellator.getInstance();
            BufferBuilder \u26036 = \u26035.getBuffer();
            \u26036.begin(7, DefaultVertexFormats.POSITION_COLOR);
            \u2603 = (RANDOM.nextFloat() * 0.5f + 0.1f) * f3;
            \u2603 = (RANDOM.nextFloat() * 0.5f + 0.4f) * f3;
            \u2603 = (RANDOM.nextFloat() * 0.5f + 0.5f) * f3;
            if (tileEntityEndPortal.shouldRenderFace(EnumFacing.SOUTH)) {
                \u26036.pos(d, d2, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2 + 1.0, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d, d2 + 1.0, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
            }
            if (tileEntityEndPortal.shouldRenderFace(EnumFacing.NORTH)) {
                \u26036.pos(d, d2 + 1.0, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2 + 1.0, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d, d2, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
            }
            if (tileEntityEndPortal.shouldRenderFace(EnumFacing.EAST)) {
                \u26036.pos(d + 1.0, d2 + 1.0, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2 + 1.0, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
            }
            if (tileEntityEndPortal.shouldRenderFace(EnumFacing.WEST)) {
                \u26036.pos(d, d2, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d, d2, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d, d2 + 1.0, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d, d2 + 1.0, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
            }
            if (tileEntityEndPortal.shouldRenderFace(EnumFacing.DOWN)) {
                \u26036.pos(d, d2, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d, d2, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
            }
            if (tileEntityEndPortal.shouldRenderFace(EnumFacing.UP)) {
                \u26036.pos(d, d2 + (double)\u26033, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2 + (double)\u26033, d3 + 1.0).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d + 1.0, d2 + (double)\u26033, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
                \u26036.pos(d, d2 + (double)\u26033, d3).color(\u2603, \u2603, \u2603, 1.0f).endVertex();
            }
            \u26035.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode((int)5888);
            this.bindTexture(END_SKY_TEXTURE);
        }
        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.R);
        GlStateManager.enableLighting();
        if (\u26034) {
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        }
    }

    protected int getPasses(double d) {
        int n = d > 36864.0 ? 1 : (d > 25600.0 ? 3 : (d > 16384.0 ? 5 : (d > 9216.0 ? 7 : (d > 4096.0 ? 9 : (d > 1024.0 ? 11 : (d > 576.0 ? 13 : (d > 256.0 ? 14 : 15)))))));
        return n;
    }

    protected float getOffset() {
        return 0.75f;
    }

    private FloatBuffer getBuffer(float f, float f2, float f3, float f4) {
        this.buffer.clear();
        this.buffer.put(f).put(f2).put(f3).put(f4);
        this.buffer.flip();
        return this.buffer;
    }
}
