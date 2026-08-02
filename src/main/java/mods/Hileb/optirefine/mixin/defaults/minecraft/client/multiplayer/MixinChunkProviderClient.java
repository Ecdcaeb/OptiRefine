package mods.Hileb.optirefine.mixin.defaults.minecraft.client.multiplayer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(ChunkProviderClient.class)
public abstract class MixinChunkProviderClient {
// [AUDIT] 2026-08-03 — loadedChunks is vanilla private; MixinRenderGlobal.getCountLoadedChunks reads it cross-class

    @SuppressWarnings({"unused", "MissingUnique"})
    @Unique
    @AccessTransformer(name = "field_73236_b", deobf = true, access = Opcodes.ACC_PUBLIC)
    private Object acc_loadedChunks;
}
