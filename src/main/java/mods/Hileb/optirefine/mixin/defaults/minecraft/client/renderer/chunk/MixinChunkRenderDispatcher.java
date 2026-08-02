package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;


import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
@Mixin(ChunkRenderDispatcher.class)
public abstract class MixinChunkRenderDispatcher {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    // [AUDIT-OK] OF-added field (OF ChunkRenderDispatcher:42)
    @Unique
    private List<RegionRenderCacheBuilder> listPausedBuilders = new ArrayList<>();

    @Shadow @Final
    // [AUDIT-OK] baseline members countRenderBuilders/queueFreeRenderBuilders/runChunkUploads (OF:33/37/82)
    private int countRenderBuilders;

    @Shadow
    public abstract boolean runChunkUploads(long finishTimeNano);

    @Shadow @Final
    private BlockingQueue<RegionRenderCacheBuilder> queueFreeRenderBuilders;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] body identical to OF:286-297 (OF addition)
    public void pauseChunkUpdates() {
        while (this.listPausedBuilders.size() != this.countRenderBuilders) {
            try {
                this.runChunkUploads(Long.MAX_VALUE);
                RegionRenderCacheBuilder builder = this.queueFreeRenderBuilders.poll(100L, TimeUnit.MILLISECONDS);
                if (builder != null) {
                    this.listPausedBuilders.add(builder);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] body identical to OF:299-302 (OF addition)
    public void resumeChunkUpdates() {
        this.queueFreeRenderBuilders.addAll(this.listPausedBuilders);
        this.listPausedBuilders.clear();
    }

}
