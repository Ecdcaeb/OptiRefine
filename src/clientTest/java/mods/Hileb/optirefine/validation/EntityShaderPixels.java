package mods.Hileb.optirefine.validation;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.optifine.shaders.Program;
import net.optifine.shaders.Shaders;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

final class EntityShaderPixels {
    static final int SIZE = 48;

    private EntityShaderPixels() {}

    static int colorProgram(String source) {
        return ShaderLifecycleContracts.linkProgram("#version 130\nvoid main(){gl_Position=gl_ModelViewProjectionMatrix*gl_Vertex;}",
                "#version 130\n" + source);
    }

    static ByteBuffer draw(Program program, Runnable render) {
        int previousRead = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING);
        int previousDraw = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        int previousMatrix = GL11.glGetInteger(GL11.GL_MATRIX_MODE);
        Program previousProgram = Shaders.activeProgram;
        boolean renderingWorld = Shaders.isRenderingWorld, renderingDfb = Shaders.isRenderingDfb;
        boolean shadow = Shaders.isShadowPass, glowing = Shaders.isEntitiesGlowing;
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(-2, 2, -1, 3, -8, 8);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Framebuffer framebuffer = null;
        try {
            framebuffer = new Framebuffer(SIZE, SIZE, false);
            framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
            framebuffer.framebufferClear();
            framebuffer.bindFramebuffer(true);
            GlStateManager.disableBlend();
            GlStateManager.disableCull();
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            Shaders.isRenderingWorld = true;
            Shaders.isRenderingDfb = false;
            Shaders.isShadowPass = false;
            Shaders.isEntitiesGlowing = false;
            Shaders.useProgram(program);
            render.run();
            ByteBuffer result = BufferUtils.createByteBuffer(SIZE * SIZE * 4);
            GL11.glReadPixels(0, 0, SIZE, SIZE, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, result);
            return result;
        } finally {
            Shaders.isRenderingWorld = renderingWorld;
            Shaders.isRenderingDfb = renderingDfb;
            Shaders.isShadowPass = shadow;
            Shaders.isEntitiesGlowing = glowing;
            Shaders.useProgram(previousProgram);
            if (framebuffer != null) framebuffer.deleteFramebuffer();
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, previousRead);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, previousDraw);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glMatrixMode(previousMatrix);
            GL11.glPopAttrib();
        }
    }

    static int pixel(ByteBuffer pixels, int x, int y) {
        int offset = (y * SIZE + x) * 4;
        return (pixels.get(offset) & 255) << 16 | (pixels.get(offset + 1) & 255) << 8 | (pixels.get(offset + 2) & 255);
    }
}
