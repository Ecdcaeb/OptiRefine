/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
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
 *  net.optifine.shaders.Shaders
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
import net.optifine.shaders.Shaders;

public class TileEntityBeaconRenderer
extends TileEntitySpecialRenderer<TileEntityBeacon> {
    public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    public void render(TileEntityBeacon te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        this.renderBeacon(x, y, z, partialTicks, te.shouldBeamRender(), (List<TileEntityBeacon.BeamSegment>)te.getBeamSegments(), te.D().getTotalWorldTime());
    }

    public void renderBeacon(double x, double y, double z, double partialTicks, double textureScale, List<TileEntityBeacon.BeamSegment> beamSegments, double totalWorldTime) {
        if (textureScale <= 0.0 || beamSegments.size() <= 0) {
            return;
        }
        if (Config.isShaders()) {
            Shaders.beginBeacon();
        }
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        this.bindTexture(TEXTURE_BEACON_BEAM);
        if (textureScale > 0.0) {
            GlStateManager.disableFog();
            int i = 0;
            for (int j = 0; j < beamSegments.size(); ++j) {
                TileEntityBeacon.BeamSegment tileentitybeacon$beamsegment = (TileEntityBeacon.BeamSegment)beamSegments.get(j);
                TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, textureScale, totalWorldTime, i, tileentitybeacon$beamsegment.getHeight(), tileentitybeacon$beamsegment.getColors());
                i += tileentitybeacon$beamsegment.getHeight();
            }
            GlStateManager.enableFog();
        }
        if (Config.isShaders()) {
            Shaders.endBeacon();
        }
    }

    public static void renderBeamSegment(double x, double y, double z, double partialTicks, double textureScale, double totalWorldTime, int yOffset, int height, float[] colors) {
        TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, textureScale, totalWorldTime, yOffset, height, colors, 0.2, 0.25);
    }

    public static void renderBeamSegment(double x, double y, double z, double partialTicks, double textureScale, double totalWorldTime, int yOffset, int height, float[] colors, double beamRadius, double glowRadius) {
        int i = yOffset + height;
        GlStateManager.glTexParameteri((int)3553, (int)10242, (int)10497);
        GlStateManager.glTexParameteri((int)3553, (int)10243, (int)10497);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        double d0 = totalWorldTime + partialTicks;
        double d1 = height < 0 ? d0 : -d0;
        double d2 = MathHelper.frac((double)(d1 * 0.2 - (double)MathHelper.floor((double)(d1 * 0.1))));
        float f = colors[0];
        float f1 = colors[1];
        float f2 = colors[2];
        double d3 = d0 * 0.025 * -1.5;
        double d4 = 0.5 + Math.cos((double)(d3 + 2.356194490192345)) * beamRadius;
        double d5 = 0.5 + Math.sin((double)(d3 + 2.356194490192345)) * beamRadius;
        double d6 = 0.5 + Math.cos((double)(d3 + 0.7853981633974483)) * beamRadius;
        double d7 = 0.5 + Math.sin((double)(d3 + 0.7853981633974483)) * beamRadius;
        double d8 = 0.5 + Math.cos((double)(d3 + 3.9269908169872414)) * beamRadius;
        double d9 = 0.5 + Math.sin((double)(d3 + 3.9269908169872414)) * beamRadius;
        double d10 = 0.5 + Math.cos((double)(d3 + 5.497787143782138)) * beamRadius;
        double d11 = 0.5 + Math.sin((double)(d3 + 5.497787143782138)) * beamRadius;
        double d12 = 0.0;
        double d13 = 1.0;
        double d14 = -1.0 + d2;
        double d15 = (double)height * textureScale * (0.5 / beamRadius) + d14;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(x + d4, y + (double)i, z + d5).tex(1.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d4, y + (double)yOffset, z + d5).tex(1.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d6, y + (double)yOffset, z + d7).tex(0.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d6, y + (double)i, z + d7).tex(0.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d10, y + (double)i, z + d11).tex(1.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d10, y + (double)yOffset, z + d11).tex(1.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d8, y + (double)yOffset, z + d9).tex(0.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d8, y + (double)i, z + d9).tex(0.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d6, y + (double)i, z + d7).tex(1.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d6, y + (double)yOffset, z + d7).tex(1.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d10, y + (double)yOffset, z + d11).tex(0.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d10, y + (double)i, z + d11).tex(0.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d8, y + (double)i, z + d9).tex(1.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d8, y + (double)yOffset, z + d9).tex(1.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d4, y + (double)yOffset, z + d5).tex(0.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d4, y + (double)i, z + d5).tex(0.0, d15).color(f, f1, f2, 1.0f).endVertex();
        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask((boolean)false);
        if (Config.isShaders()) {
            GlStateManager.depthMask((boolean)Shaders.isBeaconBeamDepth());
        }
        d3 = 0.5 - glowRadius;
        d4 = 0.5 - glowRadius;
        d5 = 0.5 + glowRadius;
        d6 = 0.5 - glowRadius;
        d7 = 0.5 - glowRadius;
        d8 = 0.5 + glowRadius;
        d9 = 0.5 + glowRadius;
        d10 = 0.5 + glowRadius;
        d11 = 0.0;
        d12 = 1.0;
        d13 = -1.0 + d2;
        d14 = (double)height * textureScale + d13;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(x + d3, y + (double)i, z + d4).tex(1.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d3, y + (double)yOffset, z + d4).tex(1.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d5, y + (double)yOffset, z + d6).tex(0.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d5, y + (double)i, z + d6).tex(0.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d9, y + (double)i, z + d10).tex(1.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d9, y + (double)yOffset, z + d10).tex(1.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d7, y + (double)yOffset, z + d8).tex(0.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d7, y + (double)i, z + d8).tex(0.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d5, y + (double)i, z + d6).tex(1.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d5, y + (double)yOffset, z + d6).tex(1.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d9, y + (double)yOffset, z + d10).tex(0.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d9, y + (double)i, z + d10).tex(0.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d7, y + (double)i, z + d8).tex(1.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d7, y + (double)yOffset, z + d8).tex(1.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d3, y + (double)yOffset, z + d4).tex(0.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d3, y + (double)i, z + d4).tex(0.0, d14).color(f, f1, f2, 0.125f).endVertex();
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask((boolean)true);
    }

    public boolean isGlobalRenderer(TileEntityBeacon te) {
        return true;
    }
}
