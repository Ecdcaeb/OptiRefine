package mods.Hileb.optirefine.mixin.minecraft.world.chunk;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BitArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.BlockStatePaletteHashMap;
import net.minecraft.world.chunk.BlockStatePaletteLinear;
import net.minecraft.world.chunk.IBlockStatePalette;
import net.minecraftforge.registries.GameData;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("unused")
@Mixin(BlockStateContainer.class)
public abstract class MixinBlockStateContainer{
    @Final @Shadow
    private static IBlockStatePalette REGISTRY_BASED_PALETTE;

    @Shadow @Final
    protected static IBlockState AIR_BLOCK_STATE;
    @Shadow
    protected BitArray storage;
    @Shadow
    protected IBlockStatePalette palette;
    @Shadow
    private int bits;

    /**
     * @author Hileb
     * @reason cc
     */
    @Overwrite
    private void setBits(int bitsIn) {
        if (bitsIn != this.bits) {
            this.bits = bitsIn;
            if (this.bits <= 4) {
                this.bits = 4;
                this.palette = newBlockStatePaletteLinear(AccessibleOperation.Construction.construction(), this.bits, this);
            } else if (this.bits <= 8) {
                this.palette = newBlockStatePaletteHashMap(AccessibleOperation.Construction.construction(), this.bits, this);
            } else {
                this.palette = REGISTRY_BASED_PALETTE;
                this.bits = MathHelper.log2DeBruijn(GameData.getBlockStateIDMap().size());
            }
            this.palette.idFor(AIR_BLOCK_STATE);
            this.storage = new BitArray(this.bits, 4096);
        }
    }

    @SuppressWarnings({"MissingUnique", "AddedMixinMembersNamePattern"})
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net.minecraft.world.chunk.BlockStatePaletteHashMap (ILnet/minecraft/world/chunk/IBlockStatePaletteResizer;)V")
    private static BlockStatePaletteHashMap newBlockStatePaletteHashMap(AccessibleOperation.Construction construction, int i, Object o){
        throw new AbstractMethodError();
    }

    @SuppressWarnings({"MissingUnique", "AddedMixinMembersNamePattern"})
    @AccessibleOperation(opcode = Opcodes.NEW, desc = "net.minecraft.world.chunk.BlockStatePaletteLinear (ILnet/minecraft/world/chunk/IBlockStatePaletteResizer;)V")
    private static BlockStatePaletteLinear newBlockStatePaletteLinear(AccessibleOperation.Construction construction, int i, Object o){
        throw new AbstractMethodError();
    }
}
