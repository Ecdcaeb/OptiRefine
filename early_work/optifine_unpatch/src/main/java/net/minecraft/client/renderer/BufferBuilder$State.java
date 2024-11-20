/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BufferBuilder.State {
    private final int[] stateRawBuffer;
    private final VertexFormat stateVertexFormat;

    public BufferBuilder.State(BufferBuilder this$0, int[] buffer, VertexFormat format) {
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
}
