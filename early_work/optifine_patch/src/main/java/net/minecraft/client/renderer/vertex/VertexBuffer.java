/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.nio.ByteBuffer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.optifine.render.VboRange
 *  net.optifine.render.VboRegion
 */
package net.minecraft.client.renderer.vertex;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.optifine.render.VboRange;
import net.optifine.render.VboRegion;

public class VertexBuffer {
    private int glBufferId;
    private final VertexFormat vertexFormat;
    private int count;
    private VboRegion vboRegion;
    private VboRange vboRange;
    private int drawMode;

    public VertexBuffer(VertexFormat vertexFormatIn) {
        this.vertexFormat = vertexFormatIn;
        this.glBufferId = OpenGlHelper.glGenBuffers();
    }

    public void bindBuffer() {
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_ARRAY_BUFFER, (int)this.glBufferId);
    }

    public void bufferData(ByteBuffer data) {
        if (this.vboRegion != null) {
            this.vboRegion.bufferData(data, this.vboRange);
            return;
        }
        this.bindBuffer();
        OpenGlHelper.glBufferData((int)OpenGlHelper.GL_ARRAY_BUFFER, (ByteBuffer)data, (int)35044);
        this.unbindBuffer();
        this.count = data.limit() / this.vertexFormat.getSize();
    }

    public void drawArrays(int mode) {
        if (this.drawMode > 0) {
            mode = this.drawMode;
        }
        if (this.vboRegion != null) {
            this.vboRegion.drawArrays(mode, this.vboRange);
        } else {
            GlStateManager.glDrawArrays((int)mode, (int)0, (int)this.count);
        }
    }

    public void unbindBuffer() {
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_ARRAY_BUFFER, (int)0);
    }

    public void deleteGlBuffers() {
        if (this.glBufferId >= 0) {
            OpenGlHelper.glDeleteBuffers((int)this.glBufferId);
            this.glBufferId = -1;
        }
    }

    public void setVboRegion(VboRegion vboRegion) {
        if (vboRegion == null) {
            return;
        }
        this.deleteGlBuffers();
        this.vboRegion = vboRegion;
        this.vboRange = new VboRange();
    }

    public VboRegion getVboRegion() {
        return this.vboRegion;
    }

    public VboRange getVboRange() {
        return this.vboRange;
    }

    public int getDrawMode() {
        return this.drawMode;
    }

    public void setDrawMode(int drawMode) {
        this.drawMode = drawMode;
    }
}
