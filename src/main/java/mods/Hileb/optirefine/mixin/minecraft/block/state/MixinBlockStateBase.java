package mods.Hileb.optirefine.mixin.minecraft.block.state;

import mods.Hileb.optirefine.mixinx.cursedmixinextensions.annotations.Public;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockStateBase.class)
public abstract class MixinBlockStateBase implements IBlockState {
    @Unique
    @SuppressWarnings("all")
    private int blockId = -1;
    @Unique
    @SuppressWarnings("all")
    private int blockStateId = -1;
    @Unique
    @SuppressWarnings("all")
    private int metadata = -1;
    @Unique
    @SuppressWarnings("all")
    private ResourceLocation blockLocation = null;

    @Unique
    @SuppressWarnings("all")
    @Public
    public int getBlockId() {
        if (this.blockId < 0) {
             this.blockId = Block.getIdFromBlock(this.getBlock());
         }
         return this.blockId;
    }

    @Unique
    @SuppressWarnings("all")
    @Public
    public int getBlockStateId() {
         if (this.blockStateId < 0) {
             this.blockStateId = Block.getStateId(this);
         }
         return this.blockStateId;
    }

    @Unique
    @SuppressWarnings("all")
    @Public
    public int getMetadata() {
         if (this.metadata < 0) {
             this.metadata = this.getBlock().getMetaFromState(this);
         }
         return this.metadata;
    }

    @Unique
    @SuppressWarnings("all")
    @Public
    public ResourceLocation getBlockLocation() {
        if (this.blockLocation == null) {
            this.blockLocation = Block.REGISTRY.getNameForObject(this.getBlock());
        }
        return this.blockLocation;
    }
}
