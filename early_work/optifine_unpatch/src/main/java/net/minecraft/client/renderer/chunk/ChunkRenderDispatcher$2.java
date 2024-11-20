/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.Runnable
 *  net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator
 */
package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;

class ChunkRenderDispatcher.2
implements Runnable {
    final /* synthetic */ ChunkCompileTaskGenerator val$chunkcompiletaskgenerator;

    ChunkRenderDispatcher.2() {
        this.val$chunkcompiletaskgenerator = chunkCompileTaskGenerator;
    }

    public void run() {
        ChunkRenderDispatcher.this.queueChunkUpdates.remove((Object)this.val$chunkcompiletaskgenerator);
    }
}
