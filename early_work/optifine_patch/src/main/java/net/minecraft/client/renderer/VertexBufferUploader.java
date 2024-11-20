/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Object
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.WorldVertexBufferUploader
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader
extends WorldVertexBufferUploader {
    private VertexBuffer vertexBuffer;

    public void draw(BufferBuilder vertexBufferIn) {
        if (vertexBufferIn.getDrawMode() == 7 && Config.isQuadsToTriangles()) {
            vertexBufferIn.quadsToTriangles();
            this.vertexBuffer.setDrawMode(vertexBufferIn.getDrawMode());
        }
        this.vertexBuffer.bufferData(vertexBufferIn.getByteBuffer());
        vertexBufferIn.reset();
    }

    public void setVertexBuffer(VertexBuffer vertexBufferIn) {
        this.vertexBuffer = vertexBufferIn;
    }
}
