package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;

import mods.Hileb.optirefine.library.common.utils.Checked;

import net.minecraft.util.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.BitSet;

@Checked
@Mixin(targets = "net.minecraft.client.renderer.chunk.CompiledChunk$1")
public abstract class MixinCompiledChunk_1 {
}
