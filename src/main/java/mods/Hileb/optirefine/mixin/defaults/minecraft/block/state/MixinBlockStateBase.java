package mods.Hileb.optirefine.mixin.defaults.minecraft.block.state;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(BlockStateBase.class)
public abstract class MixinBlockStateBase implements IBlockState {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field blockId (in OF BlockStateBase, not in baseline), MCP name matches
    @Unique
    private int blockId = -1;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field blockStateId (in OF, not in baseline)
    @Unique
    private int blockStateId = -1;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field metadata (in OF, not in baseline; BlockStateBase itself does not implement IBlockState.getMetadata)
    @Unique
    private int metadata = -1;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field blockLocation (in OF, not in baseline)
    @Unique
    private ResourceLocation blockLocation = null;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added method getBlockId, body matches OF
    public int getBlockId() {
        if (this.blockId < 0) {
             this.blockId = Block.getIdFromBlock(this.getBlock());
         }
         return this.blockId;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added method getBlockStateId, body matches OF
    public int getBlockStateId() {
         if (this.blockStateId < 0) {
             this.blockStateId = Block.getStateId(this);
         }
         return this.blockStateId;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added method getMetadata, body matches OF
    public int getMetadata() {
         if (this.metadata < 0) {
             this.metadata = this.getBlock().getMetaFromState(this);
         }
         return this.metadata;
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added method getBlockLocation, body matches OF
    public ResourceLocation getBlockLocation() {
        if (this.blockLocation == null) {
            this.blockLocation = Block.REGISTRY.getNameForObject(this.getBlock());
        }
        return this.blockLocation;
    }
}
