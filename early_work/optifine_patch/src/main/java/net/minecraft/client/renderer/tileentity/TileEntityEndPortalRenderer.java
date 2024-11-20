/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
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
 *  net.optifine.shaders.ShadersRender
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
import net.optifine.shaders.ShadersRender;

public class TileEntityEndPortalRenderer
extends TileEntitySpecialRenderer<TileEntityEndPortal> {
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer((int)16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer((int)16);
    private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer((int)16);

    public void render(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (Config.isShaders() && ShadersRender.renderEndPortal((TileEntityEndPortal)te, (double)x, (double)y, (double)z, (float)partialTicks, (int)destroyStage, (float)this.getOffset())) {
            return;
        }
        GlStateManager.disableLighting();
        RANDOM.setSeed(31100L);
        GlStateManager.getFloat((int)2982, (FloatBuffer)MODELVIEW);
        GlStateManager.getFloat((int)2983, (FloatBuffer)PROJECTION);
        double d0 = x * x + y * y + z * z;
        int i = this.getPasses(d0);
        float f = this.getOffset();
        boolean flag = false;
        for (int j = 0; j < i; ++j) {
            GlStateManager.pushMatrix();
            float f1 = 2.0f / (float)(18 - j);
            if (j == 0) {
                this.bindTexture(END_SKY_TEXTURE);
                f1 = 0.15f;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            }
            if (j >= 1) {
                this.bindTexture(END_PORTAL_TEXTURE);
                flag = true;
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            }
            if (j == 1) {
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
            float f2 = j + 1;
            GlStateManager.translate((float)(17.0f / f2), (float)((2.0f + f2 / 1.5f) * ((float)Minecraft.getSystemTime() % 800000.0f / 800000.0f)), (float)0.0f);
            GlStateManager.rotate((float)((f2 * f2 * 4321.0f + f2 * 9.0f) * 2.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.scale((float)(4.5f - f2 / 4.0f), (float)(4.5f - f2 / 4.0f), (float)1.0f);
            GlStateManager.multMatrix((FloatBuffer)PROJECTION);
            GlStateManager.multMatrix((FloatBuffer)MODELVIEW);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float f3 = (RANDOM.nextFloat() * 0.5f + 0.1f) * f1;
            float f4 = (RANDOM.nextFloat() * 0.5f + 0.4f) * f1;
            float f5 = (RANDOM.nextFloat() * 0.5f + 0.5f) * f1;
            if (te.shouldRenderFace(EnumFacing.SOUTH)) {
                bufferbuilder.pos(x, y, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y + 1.0, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x, y + 1.0, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
            }
            if (te.shouldRenderFace(EnumFacing.NORTH)) {
                bufferbuilder.pos(x, y + 1.0, z).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y + 1.0, z).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y, z).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x, y, z).color(f3, f4, f5, 1.0f).endVertex();
            }
            if (te.shouldRenderFace(EnumFacing.EAST)) {
                bufferbuilder.pos(x + 1.0, y + 1.0, z).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y + 1.0, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y, z).color(f3, f4, f5, 1.0f).endVertex();
            }
            if (te.shouldRenderFace(EnumFacing.WEST)) {
                bufferbuilder.pos(x, y, z).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x, y, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x, y + 1.0, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x, y + 1.0, z).color(f3, f4, f5, 1.0f).endVertex();
            }
            if (te.shouldRenderFace(EnumFacing.DOWN)) {
                bufferbuilder.pos(x, y, z).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y, z).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x, y, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
            }
            if (te.shouldRenderFace(EnumFacing.UP)) {
                bufferbuilder.pos(x, y + (double)f, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y + (double)f, z + 1.0).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x + 1.0, y + (double)f, z).color(f3, f4, f5, 1.0f).endVertex();
                bufferbuilder.pos(x, y + (double)f, z).color(f3, f4, f5, 1.0f).endVertex();
            }
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode((int)5888);
            this.bindTexture(END_SKY_TEXTURE);
        }
        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.R);
        GlStateManager.enableLighting();
        if (flag) {
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        }
    }

    protected int getPasses(double p_191286_1_) {
        int i = p_191286_1_ > 36864.0 ? 1 : (p_191286_1_ > 25600.0 ? 3 : (p_191286_1_ > 16384.0 ? 5 : (p_191286_1_ > 9216.0 ? 7 : (p_191286_1_ > 4096.0 ? 9 : (p_191286_1_ > 1024.0 ? 11 : (p_191286_1_ > 576.0 ? 13 : (p_191286_1_ > 256.0 ? 14 : 15)))))));
        return i;
    }

    protected float getOffset() {
        return 0.75f;
    }

    private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
        this.buffer.clear();
        this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.buffer.flip();
        return this.buffer;
    }
}
