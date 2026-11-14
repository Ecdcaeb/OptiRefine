package mods.Hileb.optirefine.mixin.defaults.minecraft.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.optifine.BlockPosM;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Mixin(WorldEntitySpawner.class)
public abstract class MixinWorldEntitySpawner {

    @Unique
    private Map<Class<?>, EntityLiving> optiRefine$sampleEntities;

    @Unique
    private ChunkPos[] optiRefine$eligibleChunkPositions;

    @Unique
    private World optiRefine$cacheWorld;

    @Unique
    private boolean optiRefine$cacheEligibleChunkPositions;

    @Unique
    private int optiRefine$playerChunkX;

    @Unique
    private int optiRefine$playerChunkZ;

    @Unique
    private BlockPosM optiRefine$randomChunkPosition;

    // Eligibility cannot be cached: borders, sent chunks and other mods' loading checks can change
    // while the player stays in one chunk. Only retain the 17x17 immutable candidate coordinates.
    @Inject(method = "findChunksForSpawning", at = @At(value = "INVOKE", target = "Ljava/util/Set;clear()V"))
    private void optiRefine$prepareEligibleChunkCache(WorldServer world, boolean hostile, boolean peaceful, boolean animals, CallbackInfoReturnable<Integer> cir) {
        if (this.optiRefine$cacheWorld != world) {
            this.optiRefine$sampleEntities.clear();
            this.optiRefine$eligibleChunkPositions = null;
            this.optiRefine$cacheWorld = world;
        }
        this.optiRefine$cacheEligibleChunkPositions = world.playerEntities.size() == 1;
        if (this.optiRefine$cacheEligibleChunkPositions) {
            EntityPlayer player = world.playerEntities.get(0);
            int chunkX = MathHelper.floor(player.posX / 16.0D);
            int chunkZ = MathHelper.floor(player.posZ / 16.0D);
            if (this.optiRefine$eligibleChunkPositions != null
                    && (chunkX != this.optiRefine$playerChunkX || chunkZ != this.optiRefine$playerChunkZ)) {
                Arrays.fill(this.optiRefine$eligibleChunkPositions, null);
            }
            this.optiRefine$playerChunkX = chunkX;
            this.optiRefine$playerChunkZ = chunkZ;
        } else {
            this.optiRefine$eligibleChunkPositions = null;
        }
    }

    @WrapOperation(method = "findChunksForSpawning", at = @At(value = "NEW", target = "(II)Lnet/minecraft/util/math/ChunkPos;"))
    private ChunkPos optiRefine$reuseEligibleChunkPosition(int x, int z, Operation<ChunkPos> original) {
        if (!this.optiRefine$cacheEligibleChunkPositions) {
            return original.call(x, z);
        }
        int offsetX = x - this.optiRefine$playerChunkX + 8;
        int offsetZ = z - this.optiRefine$playerChunkZ + 8;
        if (offsetX < 0 || offsetX >= 17 || offsetZ < 0 || offsetZ >= 17) {
            return original.call(x, z);
        }
        if (this.optiRefine$eligibleChunkPositions == null) {
            this.optiRefine$eligibleChunkPositions = new ChunkPos[17 * 17];
        }
        int index = offsetX * 17 + offsetZ;
        ChunkPos pos = this.optiRefine$eligibleChunkPositions[index];
        if (pos == null || pos.x != x || pos.z != z) {
            pos = original.call(x, z);
            this.optiRefine$eligibleChunkPositions[index] = pos;
        }
        return pos;
    }

    @WrapOperation(method = "findChunksForSpawning", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldEntitySpawner;getRandomChunkPosition(Lnet/minecraft/world/World;II)Lnet/minecraft/util/math/BlockPos;"))
    private BlockPos optiRefine$reuseRandomChunkPosition(World world, int chunkX, int chunkZ, Operation<BlockPos> original) {
        Chunk chunk = world.getChunk(chunkX, chunkZ);
        int x = chunkX * 16 + world.rand.nextInt(16);
        int z = chunkZ * 16 + world.rand.nextInt(16);
        int height = MathHelper.roundUp(chunk.getHeightValue(x & 15, z & 15) + 1, 16);
        int y = world.rand.nextInt(height > 0 ? height : chunk.getTopFilledSegment() + 16 - 1);
        this.optiRefine$randomChunkPosition.setXyz(x, y, z);
        return this.optiRefine$randomChunkPosition;
    }

    @WrapOperation(method = "findChunksForSpawning", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome$SpawnListEntry;newInstance(Lnet/minecraft/world/World;)Lnet/minecraft/entity/EntityLiving;", remap = false))
    private EntityLiving optiRefine$reuseSampleEntity(Biome.SpawnListEntry entry, World world, Operation<EntityLiving> original) {
        EntityLiving entity = this.optiRefine$sampleEntities.get(entry.entityClass);
        if (entity == null || entity.world != world || entity.isDead || entity.isAddedToWorld()) {
            entity = original.call(entry, world);
            this.optiRefine$sampleEntities.put(entry.entityClass, entity);
        }
        return entity;
    }

    // A sample stops being reusable before Forge initializes it, even if the later spawn fails.
    @Inject(method = "findChunksForSpawning", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;doSpecialSpawn(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/world/World;FFF)Z", remap = false))
    private void optiRefine$releaseSampleEntity(WorldServer world, boolean hostile, boolean peaceful, boolean animals, CallbackInfoReturnable<Integer> cir,
                                              @Local Biome.SpawnListEntry entry) {
        this.optiRefine$sampleEntities.remove(entry.entityClass);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initializeEntitySpawnCaches(CallbackInfo ci) {
        this.optiRefine$sampleEntities = new HashMap<>();
        this.optiRefine$randomChunkPosition = new BlockPosM(0, 0, 0);
    }
}
