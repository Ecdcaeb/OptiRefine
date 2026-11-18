package mods.Hileb.optirefine.mixin.defaults.optifine;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.entity.G5ShoulderParrots;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.optifine.RandomEntities;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RandomEntities.class, remap = false)
public abstract class MixinG5RandomEntitiesShoulder {
    @Inject(method = "checkEntityShoulder", at = @At("HEAD"), cancellable = true)
    private static void optiRefine$handoffShoulderParrot(EntityShoulderRiding entity, boolean unloading, CallbackInfo ci) {
        if (!(entity instanceof EntityParrot parrot)) return;
        // G5's hasKey("UUID") check misses the UUIDMost/UUIDLeast pair used by 1.12.2.
        if (unloading) {
            G5ShoulderParrots.untrack(parrot);
        } else {
            EntityParrot source = G5ShoulderParrots.getSpawnSource(parrot);
            if (source != null && source != parrot) {
                EntityDataManager from = source.getDataManager();
                EntityDataManager to = parrot.getDataManager();
                optiRefine$setSpawnPosition(to, optiRefine$getSpawnPosition(from));
                optiRefine$setSpawnBiome(to, optiRefine$getSpawnBiome(from));
            }
        }
        ci.cancel();
    }

    @Inject(method = "worldChanged", at = @At("HEAD"))
    private static void optiRefine$releaseOldShoulderParrots(World oldWorld, World newWorld, CallbackInfo ci) {
        G5ShoulderParrots.worldChanged(newWorld);
    }

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.network.datasync.EntityDataManager spawnPosition Lnet.minecraft.util.math.BlockPos;")
    private static native BlockPos optiRefine$getSpawnPosition(EntityDataManager data);

    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.network.datasync.EntityDataManager spawnPosition Lnet.minecraft.util.math.BlockPos;")
    private static native void optiRefine$setSpawnPosition(EntityDataManager data, BlockPos position);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.network.datasync.EntityDataManager spawnBiome Lnet.minecraft.world.biome.Biome;")
    private static native Biome optiRefine$getSpawnBiome(EntityDataManager data);

    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.network.datasync.EntityDataManager spawnBiome Lnet.minecraft.world.biome.Biome;")
    private static native void optiRefine$setSpawnBiome(EntityDataManager data, Biome biome);
}
