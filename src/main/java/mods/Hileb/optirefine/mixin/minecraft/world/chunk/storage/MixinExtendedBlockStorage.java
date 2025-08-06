package mods.Hileb.optirefine.mixin.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.*;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ExtendedBlockStorage.class)
public abstract class MixinExtendedBlockStorage {

    @Shadow
    private int blockRefCount;

    @SuppressWarnings("unused")
    @Shadow
    private int tickRefCount;

    @Shadow @Final
    private BlockStateContainer data;

    @Unique
    private static final IBlockState OPTIREFINE_STATE_AIR = Blocks.AIR.getDefaultState();


    /**
     * @author Hileb
     * @reason makeLocals
     */
    @Overwrite
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
    public int getBlockRefCount() {
        return this.blockRefCount;
    }
}
