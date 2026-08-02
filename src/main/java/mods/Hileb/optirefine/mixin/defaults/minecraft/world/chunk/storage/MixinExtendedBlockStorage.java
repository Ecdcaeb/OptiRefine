package mods.Hileb.optirefine.mixin.defaults.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
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

    @Shadow @Final
// [AUDIT-OK] baseline member data exists in target class
    private BlockStateContainer data;

// [AUDIT-OK] mixin-private static constant (private static per rules), replicates OF "Blocks.AIR.getDefaultState()" compare value
    private static final IBlockState OPTIREFINE_STATE_AIR = Blocks.AIR.getDefaultState();


    /**
     * @author Hileb
     * @reason makeLocals
     */
    @Overwrite
// [AUDIT-OK] @Overwrite matches OF recalculateRefCounts (data.get loop, AIR compare, local counters)
    public void recalculateRefCounts() {
        int localBlockRefCount = 0;
        int localTickRefCount = 0;
        for (int y = 0; y < 16; ++y) {
            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    IBlockState bs = this.data.get(x, y, z);
                    if (bs == OPTIREFINE_STATE_AIR) continue;
                    ++localBlockRefCount;
                    Block block = bs.getBlock();
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
