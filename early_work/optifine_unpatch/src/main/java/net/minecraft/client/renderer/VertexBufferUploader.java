/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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

    public void draw(BufferBuilder bufferBuilder) {
        bufferBuilder.reset();
        this.vertexBuffer.bufferData(bufferBuilder.getByteBuffer());
    }

    public void setVertexBuffer(VertexBuffer vertexBuffer) {
        this.vertexBuffer = vertexBuffer;
    }
}
