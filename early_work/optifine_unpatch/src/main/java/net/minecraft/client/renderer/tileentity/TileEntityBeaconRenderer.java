/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.util.List
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.tileentity.TileEntityBeacon
 *  net.minecraft.tileentity.TileEntityBeacon$BeamSegment
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 */
package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityBeaconRenderer
extends TileEntitySpecialRenderer<TileEntityBeacon> {
    public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    public void render(TileEntityBeacon tileEntityBeacon, double d, double d2, double d3, float f, int n, float f2) {
        this.renderBeacon(d, d2, d3, f, tileEntityBeacon.shouldBeamRender(), (List<TileEntityBeacon.BeamSegment>)tileEntityBeacon.getBeamSegments(), tileEntityBeacon.D().getTotalWorldTime());
    }

    public void renderBeacon(double d, double d2, double d3, double d4, double d5, List<TileEntityBeacon.BeamSegment> list, double d6) {
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        this.bindTexture(TEXTURE_BEACON_BEAM);
        if (d5 > 0.0) {
            GlStateManager.disableFog();
            int n = 0;
            for (\u2603 = 0; \u2603 < list.size(); ++\u2603) {
                TileEntityBeacon.BeamSegment beamSegment = (TileEntityBeacon.BeamSegment)list.get(\u2603);
                TileEntityBeaconRenderer.renderBeamSegment(d, d2, d3, d4, d5, d6, n, beamSegment.getHeight(), beamSegment.getColors());
                n += beamSegment.getHeight();
            }
            GlStateManager.enableFog();
        }
    }

    public static void renderBeamSegment(double d, double d2, double d3, double d4, double d5, double d6, int n, int n2, float[] fArray) {
        TileEntityBeaconRenderer.renderBeamSegment(d, d2, d3, d4, d5, d6, n, n2, fArray, 0.2, 0.25);
    }

    public static void renderBeamSegment(double d, double d2, double d3, double d4, double d5, double d6, int n, int n2, float[] fArray, double d7, double d8) {
        int n3 = n + n2;
        GlStateManager.glTexParameteri((int)3553, (int)10242, (int)10497);
        GlStateManager.glTexParameteri((int)3553, (int)10243, (int)10497);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        Tessellator \u26032 = Tessellator.getInstance();
        BufferBuilder \u26033 = \u26032.getBuffer();
        double \u26034 = d6 + d4;
        double \u26035 = n2 < 0 ? \u26034 : -\u26034;
        double \u26036 = MathHelper.frac((double)(\u26035 * 0.2 - (double)MathHelper.floor((double)(\u26035 * 0.1))));
        float \u26037 = fArray[0];
        float \u26038 = fArray[1];
        float \u26039 = fArray[2];
        double \u260310 = \u26034 * 0.025 * -1.5;
        double \u260311 = 0.5 + Math.cos((double)(\u260310 + 2.356194490192345)) * d7;
        double \u260312 = 0.5 + Math.sin((double)(\u260310 + 2.356194490192345)) * d7;
        double \u260313 = 0.5 + Math.cos((double)(\u260310 + 0.7853981633974483)) * d7;
        double \u260314 = 0.5 + Math.sin((double)(\u260310 + 0.7853981633974483)) * d7;
        double \u260315 = 0.5 + Math.cos((double)(\u260310 + 3.9269908169872414)) * d7;
        double \u260316 = 0.5 + Math.sin((double)(\u260310 + 3.9269908169872414)) * d7;
        double \u260317 = 0.5 + Math.cos((double)(\u260310 + 5.497787143782138)) * d7;
        double \u260318 = 0.5 + Math.sin((double)(\u260310 + 5.497787143782138)) * d7;
        double \u260319 = 0.0;
        double \u260320 = 1.0;
        double \u260321 = -1.0 + \u26036;
        double \u260322 = (double)n2 * d5 * (0.5 / d7) + \u260321;
        \u26033.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        \u26033.pos(d + \u260311, d2 + (double)n3, d3 + \u260312).tex(1.0, \u260322).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260311, d2 + (double)n, d3 + \u260312).tex(1.0, \u260321).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260313, d2 + (double)n, d3 + \u260314).tex(0.0, \u260321).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260313, d2 + (double)n3, d3 + \u260314).tex(0.0, \u260322).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260317, d2 + (double)n3, d3 + \u260318).tex(1.0, \u260322).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260317, d2 + (double)n, d3 + \u260318).tex(1.0, \u260321).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260315, d2 + (double)n, d3 + \u260316).tex(0.0, \u260321).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260315, d2 + (double)n3, d3 + \u260316).tex(0.0, \u260322).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260313, d2 + (double)n3, d3 + \u260314).tex(1.0, \u260322).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260313, d2 + (double)n, d3 + \u260314).tex(1.0, \u260321).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260317, d2 + (double)n, d3 + \u260318).tex(0.0, \u260321).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260317, d2 + (double)n3, d3 + \u260318).tex(0.0, \u260322).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260315, d2 + (double)n3, d3 + \u260316).tex(1.0, \u260322).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260315, d2 + (double)n, d3 + \u260316).tex(1.0, \u260321).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260311, d2 + (double)n, d3 + \u260312).tex(0.0, \u260321).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26033.pos(d + \u260311, d2 + (double)n3, d3 + \u260312).tex(0.0, \u260322).color(\u26037, \u26038, \u26039, 1.0f).endVertex();
        \u26032.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask((boolean)false);
        \u260310 = 0.5 - d8;
        \u260311 = 0.5 - d8;
        \u260312 = 0.5 + d8;
        \u260313 = 0.5 - d8;
        \u260314 = 0.5 - d8;
        \u260315 = 0.5 + d8;
        \u260316 = 0.5 + d8;
        \u260317 = 0.5 + d8;
        \u260318 = 0.0;
        \u260319 = 1.0;
        \u260320 = -1.0 + \u26036;
        \u260321 = (double)n2 * d5 + \u260320;
        \u26033.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        \u26033.pos(d + \u260310, d2 + (double)n3, d3 + \u260311).tex(1.0, \u260321).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260310, d2 + (double)n, d3 + \u260311).tex(1.0, \u260320).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260312, d2 + (double)n, d3 + \u260313).tex(0.0, \u260320).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260312, d2 + (double)n3, d3 + \u260313).tex(0.0, \u260321).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260316, d2 + (double)n3, d3 + \u260317).tex(1.0, \u260321).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260316, d2 + (double)n, d3 + \u260317).tex(1.0, \u260320).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260314, d2 + (double)n, d3 + \u260315).tex(0.0, \u260320).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260314, d2 + (double)n3, d3 + \u260315).tex(0.0, \u260321).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260312, d2 + (double)n3, d3 + \u260313).tex(1.0, \u260321).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260312, d2 + (double)n, d3 + \u260313).tex(1.0, \u260320).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260316, d2 + (double)n, d3 + \u260317).tex(0.0, \u260320).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260316, d2 + (double)n3, d3 + \u260317).tex(0.0, \u260321).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260314, d2 + (double)n3, d3 + \u260315).tex(1.0, \u260321).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260314, d2 + (double)n, d3 + \u260315).tex(1.0, \u260320).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260310, d2 + (double)n, d3 + \u260311).tex(0.0, \u260320).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26033.pos(d + \u260310, d2 + (double)n3, d3 + \u260311).tex(0.0, \u260321).color(\u26037, \u26038, \u26039, 0.125f).endVertex();
        \u26032.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask((boolean)true);
    }

    public boolean isGlobalRenderer(TileEntityBeacon tileEntityBeacon) {
        return true;
    }
}
