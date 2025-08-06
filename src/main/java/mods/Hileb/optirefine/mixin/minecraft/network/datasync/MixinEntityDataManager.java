package mods.Hileb.optirefine.mixin.minecraft.network.datasync;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.init.Biomes;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityDataManager.class)
public abstract class MixinEntityDataManager {
    @SuppressWarnings("unused")
    @Unique @Public
    public Biome spawnBiome = Biomes.PLAINS;
    @SuppressWarnings("unused")
    @Unique @Public
    public BlockPos spawnPosition = BlockPos.ORIGIN;
}
