/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.WorldVertexBufferUploader
 */
package net.minecraft.client.renderer;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.WorldVertexBufferUploader;

public class Tessellator {
    private final BufferBuilder buffer;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
    private static final Tessellator INSTANCE = new Tessellator(0x200000);

    public static Tessellator getInstance() {
        return INSTANCE;
    }

    public Tessellator(int n) {
        this.buffer = new BufferBuilder(n);
    }

    public void draw() {
        this.buffer.finishDrawing();
        this.vboUploader.draw(this.buffer);
    }

    public BufferBuilder getBuffer() {
        return this.buffer;
    }
}
