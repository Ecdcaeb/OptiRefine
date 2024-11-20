/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.nio.ByteBuffer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.vertex.VertexFormat
 */
package net.minecraft.client.renderer.vertex;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class VertexBuffer {
    private int glBufferId;
    private final VertexFormat vertexFormat;
    private int count;

    public VertexBuffer(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
        this.glBufferId = OpenGlHelper.glGenBuffers();
    }

    public void bindBuffer() {
        OpenGlHelper.glBindBuffer((int)OpenGlHelper.GL_ARRAY_BUFFER, (int)this.glBufferId);
    }

    public void bufferData(ByteBuffer byteBuffer) {
        this.bindBuffer();
        OpenGlHelper.glBufferData((int)OpenGlHelper.GL_ARRAY_BUFFER, (ByteBuffer)byteBuffer, (int)35044);
        this.unbindBuffer();
        this.count = byteBuffer.limit() / this.vertexFormat.getSize();
    }

    public void drawArrays(int n) {
        GlStateManager.glDrawArrays((int)n, (int)0, (int)this.count);
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
}
