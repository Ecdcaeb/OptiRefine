package mods.Hileb.optirefine.mixin.defaults.minecraft.network.datasync;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.init.Biomes;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(EntityDataManager.class)
public abstract class MixinEntityDataManager {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added fields spawnBiome/spawnPosition (in OF EntityDataManager, not in baseline), MCP names match (baseline has neither)
    @Unique
    private Biome spawnBiome;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added field spawnPosition (in OF, not in baseline), initializer matches OF (BlockPos.ORIGIN)
    @Unique
    private BlockPos spawnPosition;

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.spawnBiome = Biomes.PLAINS;
        this.spawnPosition = BlockPos.ORIGIN;
    }
}
