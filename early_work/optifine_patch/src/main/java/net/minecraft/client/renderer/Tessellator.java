/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.BitSet
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.WorldVertexBufferUploader
 *  net.optifine.SmartAnimations
 */
package net.minecraft.client.renderer;

import java.util.BitSet;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.optifine.SmartAnimations;

public class Tessellator {
    private final BufferBuilder buffer;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
    private static final Tessellator INSTANCE = new Tessellator(0x200000);

    public static Tessellator getInstance() {
        return INSTANCE;
    }

    public Tessellator(int bufferSize) {
        this.buffer = new BufferBuilder(bufferSize);
    }

    public void draw() {
        if (this.buffer.animatedSprites != null) {
            SmartAnimations.spritesRendered((BitSet)this.buffer.animatedSprites);
        }
        this.buffer.finishDrawing();
        this.vboUploader.draw(this.buffer);
    }

    public BufferBuilder getBuffer() {
        return this.buffer;
    }
}
