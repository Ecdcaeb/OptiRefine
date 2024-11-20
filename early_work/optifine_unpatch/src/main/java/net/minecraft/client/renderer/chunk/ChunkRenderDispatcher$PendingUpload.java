/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.primitives.Doubles
 *  com.google.common.util.concurrent.ListenableFutureTask
 *  java.lang.Comparable
 *  java.lang.Object
 *  net.minecraft.client.renderer.chunk.ChunkRenderDispatcher
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.primitives.Doubles;
import com.google.common.util.concurrent.ListenableFutureTask;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
class ChunkRenderDispatcher.PendingUpload
implements Comparable<ChunkRenderDispatcher.PendingUpload> {
    private final ListenableFutureTask<Object> uploadTask;
    private final double distanceSq;

    public ChunkRenderDispatcher.PendingUpload(ChunkRenderDispatcher this$0, ListenableFutureTask<Object> uploadTaskIn, double distanceSqIn) {
        this.uploadTask = uploadTaskIn;
        this.distanceSq = distanceSqIn;
    }

    public int compareTo(ChunkRenderDispatcher.PendingUpload p_compareTo_1_) {
        return Doubles.compare((double)this.distanceSq, (double)p_compareTo_1_.distanceSq);
    }
}
