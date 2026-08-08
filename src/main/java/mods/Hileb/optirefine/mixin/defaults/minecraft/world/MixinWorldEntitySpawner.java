package mods.Hileb.optirefine.mixin.defaults.minecraft.world;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.optifine.BlockPosM;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@Mixin(WorldEntitySpawner.class)
public abstract class MixinWorldEntitySpawner {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1 -> 0 (findChunksForSpawning OF cache re-enabled 2026-08-09)

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-REVIVED] OF-added field mapSampleEntitiesByClass (in OF WorldEntitySpawner, not in baseline); sample-entity reuse cache, init in <init>*
    private Map<Class<?>, EntityLiving> mapSampleEntitiesByClass;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-REVIVED] OF-added field lastPlayerChunkX (in OF, not in baseline); eligible-chunk cache key
    private int lastPlayerChunkX;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-REVIVED] OF-added field lastPlayerChunkZ (in OF, not in baseline); eligible-chunk cache key
    private int lastPlayerChunkZ;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
// [AUDIT-REVIVED] OF-added field countChunkPos (in OF, not in baseline); chunk-count cache for mob cap
    private int countChunkPos;

    @Shadow
// [AUDIT-OK] baseline member eligibleChunksForSpawning exists in target class
    private Set<ChunkPos> eligibleChunksForSpawning;

    @Shadow
// [AUDIT-OK] baseline static member MOB_COUNT_DIV exists in target class (non-final shadow, log-only warning)
    private static int MOB_COUNT_DIV;

    /**
     * @author Hileb
     * @reason OF eligible-chunk caching + sample-entity reuse layered on cleanroom's Forge spawn hooks
     */
    @Overwrite
// [AUDIT-REVIVED] findChunksForSpawning OF override re-enabled; cleanroom Forge hooks preserved via Reflector:
// countEntities(EnumCreatureType,true), shuffle, SpawnListEntry.newInstance, ForgeEventFactory.canEntitySpawn/doSpecialSpawn/getMaxSpawnPackSize
    public int findChunksForSpawning(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate) {
        if (!spawnHostileMobs && !spawnPeacefulMobs) {
            return 0;
        }
        boolean updateEligibleChunks = true;
        EntityPlayer player = null;
        if (worldServerIn.playerEntities.size() == 1) {
            player = worldServerIn.playerEntities.get(0);
            if (this.eligibleChunksForSpawning.size() > 0 && player != null && player.chunkCoordX == this.lastPlayerChunkX && player.chunkCoordZ == this.lastPlayerChunkZ) {
                updateEligibleChunks = false;
            }
        }
        if (updateEligibleChunks) {
            this.eligibleChunksForSpawning.clear();
            int i = 0;
            for (EntityPlayer entityplayer : worldServerIn.playerEntities) {
                if (entityplayer.isSpectator()) continue;
                int j = MathHelper.floor(entityplayer.posX / 16.0);
                int k = MathHelper.floor(entityplayer.posZ / 16.0);
                int l = 8;
                for (int i1 = -8; i1 <= 8; ++i1) {
                    for (int j1 = -8; j1 <= 8; ++j1) {
                        boolean flag = i1 == -8 || i1 == 8 || j1 == -8 || j1 == 8;
                        ChunkPos chunkpos = new ChunkPos(i1 + j, j1 + k);
                        if (this.eligibleChunksForSpawning.contains(chunkpos)) continue;
                        ++i;
                        if (flag || !worldServerIn.getWorldBorder().contains(chunkpos)) continue;
                        PlayerChunkMapEntry playerchunkmapentry = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.x, chunkpos.z);
                        if (playerchunkmapentry == null || !playerchunkmapentry.isSentToPlayers()) continue;
                        this.eligibleChunksForSpawning.add(chunkpos);
                    }
                }
            }
            this.countChunkPos = i;
            if (player != null) {
                this.lastPlayerChunkX = player.chunkCoordX;
                this.lastPlayerChunkZ = player.chunkCoordZ;
            }
        }
        int j4 = 0;
        BlockPos blockpos1 = worldServerIn.getSpawnPoint();
        BlockPosM blockPosM = new BlockPosM(0, 0, 0);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
            if ((!enumcreaturetype.getPeacefulCreature() || spawnPeacefulMobs) && (enumcreaturetype.getPeacefulCreature() || spawnHostileMobs) && (!enumcreaturetype.getAnimal() || spawnOnSetTickRate)) {
                int k4 = Reflector.ForgeWorld_countEntities.exists() ? Reflector.callInt(worldServerIn, Reflector.ForgeWorld_countEntities, enumcreaturetype, true) : worldServerIn.countEntities(enumcreaturetype.getCreatureClass());
                int l4 = enumcreaturetype.getMaxNumberOfCreature() * this.countChunkPos / MOB_COUNT_DIV;
                if (k4 <= l4) {
                    Collection<ChunkPos> chunksForSpawning = this.eligibleChunksForSpawning;
                    if (Reflector.ForgeHooksClient.exists()) {
                        ArrayList<ChunkPos> shuffled = Lists.newArrayList(chunksForSpawning);
                        Collections.shuffle(shuffled);
                        chunksForSpawning = shuffled;
                    }
                    chunkLoop:
                    for (ChunkPos chunkpos1 : chunksForSpawning) {
                        BlockPosM blockpos = getRandomChunkPosition(worldServerIn, chunkpos1.x, chunkpos1.z, blockPosM);
                        int k1 = blockpos.getX();
                        int l1 = blockpos.getY();
                        int i2 = blockpos.getZ();
                        IBlockState iblockstate = worldServerIn.getBlockState(blockpos);
                        if (iblockstate.isNormalCube()) continue;
                        int j2 = 0;
                        for (int k2 = 0; k2 < 3; ++k2) {
                            int l2 = k1;
                            int i3 = l1;
                            int j3 = i2;
                            Biome.SpawnListEntry biome$spawnlistentry = null;
                            IEntityLivingData ientitylivingdata = null;
                            int l3 = MathHelper.ceil(Math.random() * 4.0);
                            for (int i4 = 0; i4 < l3; ++i4) {
                                blockpos$mutableblockpos.setPos(l2 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6), i3 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1), j3 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6));
                                float f = (float) l2 + 0.5F;
                                float f1 = (float) j3 + 0.5F;
                                if (worldServerIn.isAnyPlayerWithinRangeAt(f, i3, f1, 24.0) || !(blockpos1.distanceSq(f, i3, f1) >= 576.0)) continue;
                                if (biome$spawnlistentry == null) {
                                    biome$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos$mutableblockpos);
                                    if (biome$spawnlistentry == null) {
                                        break;
                                    }
                                }
                                if (!worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biome$spawnlistentry, blockpos$mutableblockpos) || !WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biome$spawnlistentry.entityClass), worldServerIn, blockpos$mutableblockpos)) continue;
                                EntityLiving entityliving;
                                try {
                                    entityliving = this.mapSampleEntitiesByClass.get(biome$spawnlistentry.entityClass);
                                    if (entityliving == null) {
                                        entityliving = Reflector.ForgeBiomeSpawnListEntry_newInstance.exists() ? (EntityLiving) ((Object) Reflector.call(biome$spawnlistentry, Reflector.ForgeBiomeSpawnListEntry_newInstance, worldServerIn)) : (EntityLiving) ((Object) biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldServerIn));
                                        this.mapSampleEntitiesByClass.put(biome$spawnlistentry.entityClass, entityliving);
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                    return j4;
                                }
                                entityliving.setLocationAndAngles(f, i3, f1, worldServerIn.rand.nextFloat() * 360.0F, 0.0F);
                                boolean canSpawn = Reflector.ForgeEventFactory_canEntitySpawn.exists() ? ReflectorForge.canEntitySpawn(entityliving, worldServerIn, f, i3, f1) : entityliving.getCanSpawnHere() && entityliving.isNotColliding();
                                if (canSpawn) {
                                    this.mapSampleEntitiesByClass.remove(biome$spawnlistentry.entityClass);
                                    if (!ReflectorForge.doSpecialSpawn(entityliving, worldServerIn, f, i3, f1)) {
                                        ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                                    }
                                    if (entityliving.isNotColliding()) {
                                        ++j2;
                                        worldServerIn.spawnEntity(entityliving);
                                    } else {
                                        entityliving.setDead();
                                    }
                                    int maxSpawnedInChunk = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists() ? Reflector.callInt(Reflector.ForgeEventFactory_getMaxSpawnPackSize, entityliving) : entityliving.getMaxSpawnedInChunk();
                                    if (j2 >= maxSpawnedInChunk) continue chunkLoop;
                                }
                                j4 += j2;
                            }
                        }
                    }
                }
            }
        }
        return j4;
    }

    @Unique
// [AUDIT-OK] OF-added static helper getRandomChunkPosition(World,II,BlockPosM) (in OF, not in baseline), body matches OF (MathHelper.roundUp)
    private static BlockPosM getRandomChunkPosition(World var0, int var1, int var2, BlockPosM var3) {
        Chunk var4 = var0.getChunk(var1, var2);
        int var5 = var1 * 16 + var0.rand.nextInt(16);
        int var6 = var2 * 16 + var0.rand.nextInt(16);
        int var7 = MathHelper.roundUp(var4.getHeightValue(var5 & 15, var6 & 15) + 1, 16);
        int var8 = var0.rand.nextInt(var7 > 0 ? var7 : var4.getTopFilledSegment() + 16 - 1);
        var3.setXyz(var5, var8, var6);
        return var3;
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.mapSampleEntitiesByClass = new HashMap<>();
        this.lastPlayerChunkX = Integer.MAX_VALUE;
        this.lastPlayerChunkZ = Integer.MAX_VALUE;
    }
}
