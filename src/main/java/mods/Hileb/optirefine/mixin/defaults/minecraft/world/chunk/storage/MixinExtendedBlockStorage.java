package mods.Hileb.optirefine.mixin.defaults.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.*;
@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ExtendedBlockStorage.class)
public abstract class MixinExtendedBlockStorage {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


    @Shadow
// [AUDIT-OK] baseline member blockRefCount exists in target class
    private int blockRefCount;

    @SuppressWarnings("unused")
    @Shadow
// [AUDIT-OK] baseline member tickRefCount exists in target class
    private int tickRefCount;

    @Shadow
    public abstract IBlockState get(int x, int y, int z);


    /**
     * @author Hileb
     * @reason makeLocals
     */
    @Overwrite
    // Keep the baseline state-access hook and AIR-block semantics while accumulating locally.
    public void recalculateRefCounts() {
        int localBlockRefCount = 0;
        int localTickRefCount = 0;
        for (int y = 0; y < 16; ++y) {
            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    Block block = this.get(x, y, z).getBlock();
                    if (block == Blocks.AIR) continue;
                    ++localBlockRefCount;
                    if (!block.getTickRandomly()) continue;
                    ++localTickRefCount;
                }
            }
        }
        this.blockRefCount = localBlockRefCount;
        this.tickRefCount = localTickRefCount;
    }

    @SuppressWarnings("unused")
    @Unique
// [AUDIT-OK] OF-added member getBlockRefCount()I (in OF, not in baseline), MCP name matches
    public int getBlockRefCount() {
        return this.blockRefCount;
    }
}
