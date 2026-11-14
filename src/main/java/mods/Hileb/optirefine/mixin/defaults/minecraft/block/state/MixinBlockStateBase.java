package mods.Hileb.optirefine.mixin.defaults.minecraft.block.state;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(BlockStateBase.class)
public abstract class MixinBlockStateBase implements IBlockState {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field metadata (in OF, not in baseline; BlockStateBase itself does not implement IBlockState.getMetadata)
    @Unique
    private int metadata;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field blockLocation (in OF, not in baseline)
    @Unique
    private ResourceLocation blockLocation;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    // Forge remaps registry IDs when loading a world or joining a server; states outlive those remaps.
    public int getBlockId() {
        return Block.getIdFromBlock(this.getBlock());
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    // Only metadata is stable for the lifetime of an immutable state.
    public int getBlockStateId() {
        return this.getBlockId() + (this.getMetadata() << 12);
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

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initializeBlockStateCache(CallbackInfo ci) {
        this.metadata = -1;
        this.blockLocation = null;
    }
}
