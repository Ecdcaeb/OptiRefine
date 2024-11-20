/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.primitives.Doubles
 *  com.google.common.util.concurrent.ListenableFutureTask
 *  java.lang.Comparable
 *  java.lang.Object
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.primitives.Doubles;
import com.google.common.util.concurrent.ListenableFutureTask;

class ChunkRenderDispatcher.PendingUpload
implements Comparable<ChunkRenderDispatcher.PendingUpload> {
    private final ListenableFutureTask<Object> uploadTask;
    private final double distanceSq;

    public ChunkRenderDispatcher.PendingUpload(ListenableFutureTask<Object> uploadTaskIn, double distanceSqIn) {
        this.uploadTask = uploadTaskIn;
        this.distanceSq = distanceSqIn;
    }

    public int compareTo(ChunkRenderDispatcher.PendingUpload p_compareTo_1_) {
        return Doubles.compare((double)this.distanceSq, (double)p_compareTo_1_.distanceSq);
    }

    static /* synthetic */ ListenableFutureTask access$000(ChunkRenderDispatcher.PendingUpload x0) {
        return x0.uploadTask;
    }
}
