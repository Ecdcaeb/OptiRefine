package mods.Hileb.optirefine.mixin.defaults.minecraft.network.datasync;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.init.Biomes;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(EntityDataManager.class)
public abstract class MixinEntityDataManager {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added fields spawnBiome/spawnPosition (in OF EntityDataManager, not in baseline), MCP names match (baseline has neither)
    public Biome spawnBiome = Biomes.PLAINS;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
// [AUDIT-OK] OF-added field spawnPosition (in OF, not in baseline), initializer matches OF (BlockPos.ORIGIN)
    @Unique
    public BlockPos spawnPosition = BlockPos.ORIGIN;
}
