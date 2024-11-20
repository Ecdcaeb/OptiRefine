/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.vertex.VertexFormat
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class BufferBuilder.State {
    private final int[] stateRawBuffer;
    private final VertexFormat stateVertexFormat;
    private TextureAtlasSprite[] stateQuadSprites;

    public BufferBuilder.State(int[] buffer, VertexFormat format, TextureAtlasSprite[] quadSprites) {
        this.stateRawBuffer = buffer;
        this.stateVertexFormat = format;
        this.stateQuadSprites = quadSprites;
    }

    public BufferBuilder.State(int[] buffer, VertexFormat format) {
        this.stateRawBuffer = buffer;
        this.stateVertexFormat = format;
    }

    public int[] getRawBuffer() {
        return this.stateRawBuffer;
    }

    public int getVertexCount() {
        return this.stateRawBuffer.length / this.stateVertexFormat.getIntegerSize();
    }

    public VertexFormat getVertexFormat() {
        return this.stateVertexFormat;
    }

    static /* synthetic */ TextureAtlasSprite[] access$000(BufferBuilder.State x0) {
        return x0.stateQuadSprites;
    }
}
