/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.nio.FloatBuffer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$Profile
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.GlStateManager$TexGen
 *  net.minecraft.client.renderer.RenderHelper
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL14
 *  org.lwjgl.opengl.GLContext
 */
package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

/*
 * Exception performing whole class analysis ignored.
 */
static final class GlStateManager.Profile.1
extends GlStateManager.Profile {
    GlStateManager.Profile.1() {
        super(string, n, null);
    }

    public void apply() {
        GlStateManager.disableAlpha();
        GlStateManager.alphaFunc((int)519, (float)0.0f);
        GlStateManager.disableLighting();
        GL11.glLightModel((int)2899, (FloatBuffer)RenderHelper.setColorBuffer((float)0.2f, (float)0.2f, (float)0.2f, (float)1.0f));
        for (int i = 0; i < 8; ++i) {
            GlStateManager.disableLight((int)i);
            GL11.glLight((int)(16384 + i), (int)4608, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f));
            GL11.glLight((int)(16384 + i), (int)4611, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)1.0f, (float)0.0f));
            if (i == 0) {
                GL11.glLight((int)(16384 + i), (int)4609, (FloatBuffer)RenderHelper.setColorBuffer((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f));
                GL11.glLight((int)(16384 + i), (int)4610, (FloatBuffer)RenderHelper.setColorBuffer((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f));
                continue;
            }
            GL11.glLight((int)(16384 + i), (int)4609, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f));
            GL11.glLight((int)(16384 + i), (int)4610, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f));
        }
        GlStateManager.disableColorMaterial();
        GlStateManager.colorMaterial((int)1032, (int)5634);
        GlStateManager.disableDepth();
        GlStateManager.depthFunc((int)513);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.disableBlend();
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GL14.glBlendEquation((int)32774);
        GlStateManager.disableFog();
        GL11.glFogi((int)2917, (int)2048);
        GlStateManager.setFogDensity((float)1.0f);
        GlStateManager.setFogStart((float)0.0f);
        GlStateManager.setFogEnd((float)1.0f);
        GL11.glFog((int)2918, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f));
        if (GLContext.getCapabilities().GL_NV_fog_distance) {
            GL11.glFogi((int)2917, (int)34140);
        }
        GlStateManager.doPolygonOffset((float)0.0f, (float)0.0f);
        GlStateManager.disableColorLogic();
        GlStateManager.colorLogicOp((int)5379);
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.S);
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.S, (int)9216);
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.S, (int)9474, (FloatBuffer)RenderHelper.setColorBuffer((float)1.0f, (float)0.0f, (float)0.0f, (float)0.0f));
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.S, (int)9217, (FloatBuffer)RenderHelper.setColorBuffer((float)1.0f, (float)0.0f, (float)0.0f, (float)0.0f));
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.T);
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.T, (int)9216);
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.T, (int)9474, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)1.0f, (float)0.0f, (float)0.0f));
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.T, (int)9217, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)1.0f, (float)0.0f, (float)0.0f));
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.R);
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.R, (int)9216);
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.R, (int)9474, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f));
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.R, (int)9217, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f));
        GlStateManager.disableTexGenCoord((GlStateManager.TexGen)GlStateManager.TexGen.Q);
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.Q, (int)9216);
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.Q, (int)9474, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f));
        GlStateManager.texGen((GlStateManager.TexGen)GlStateManager.TexGen.Q, (int)9217, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f));
        GlStateManager.setActiveTexture((int)0);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9986);
        GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
        GL11.glTexParameteri((int)3553, (int)10243, (int)10497);
        GL11.glTexParameteri((int)3553, (int)33085, (int)1000);
        GL11.glTexParameteri((int)3553, (int)33083, (int)1000);
        GL11.glTexParameteri((int)3553, (int)33082, (int)-1000);
        GL11.glTexParameterf((int)3553, (int)34049, (float)0.0f);
        GL11.glTexEnvi((int)8960, (int)8704, (int)8448);
        GL11.glTexEnv((int)8960, (int)8705, (FloatBuffer)RenderHelper.setColorBuffer((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f));
        GL11.glTexEnvi((int)8960, (int)34161, (int)8448);
        GL11.glTexEnvi((int)8960, (int)34162, (int)8448);
        GL11.glTexEnvi((int)8960, (int)34176, (int)5890);
        GL11.glTexEnvi((int)8960, (int)34177, (int)34168);
        GL11.glTexEnvi((int)8960, (int)34178, (int)34166);
        GL11.glTexEnvi((int)8960, (int)34184, (int)5890);
        GL11.glTexEnvi((int)8960, (int)34185, (int)34168);
        GL11.glTexEnvi((int)8960, (int)34186, (int)34166);
        GL11.glTexEnvi((int)8960, (int)34192, (int)768);
        GL11.glTexEnvi((int)8960, (int)34193, (int)768);
        GL11.glTexEnvi((int)8960, (int)34194, (int)770);
        GL11.glTexEnvi((int)8960, (int)34200, (int)770);
        GL11.glTexEnvi((int)8960, (int)34201, (int)770);
        GL11.glTexEnvi((int)8960, (int)34202, (int)770);
        GL11.glTexEnvf((int)8960, (int)34163, (float)1.0f);
        GL11.glTexEnvf((int)8960, (int)3356, (float)1.0f);
        GlStateManager.disableNormalize();
        GlStateManager.shadeModel((int)7425);
        GlStateManager.disableRescaleNormal();
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.clearDepth((double)1.0);
        GL11.glLineWidth((float)1.0f);
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glPolygonMode((int)1028, (int)6914);
        GL11.glPolygonMode((int)1029, (int)6914);
    }

    public void clean() {
    }
}
