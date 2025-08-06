package mods.Hileb.optirefine.mixin.minecraft.world;

import net.minecraft.world.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldEntitySpawner.class)
public abstract class MixinWorldEntitySpawner {
//    @Unique
//    private final Map<Class<?>, EntityLiving> mapSampleEntitiesByClass = new HashMap<>();
//    @Unique
//    private int lastPlayerChunkX = Integer.MAX_VALUE;
//    @Unique
//    private int lastPlayerChunkZ = Integer.MAX_VALUE;
//    @Unique
//    private int countChunkPos;
//
//
//    /**
//     * @author Hileb
//     * @reason TODO
//     */
//    @Overwrite
//    public int findChunksForSpawning(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate) {
//        if (!spawnHostileMobs && !spawnPeacefulMobs) {
//            return 0;
//        }
//        boolean updateEligibleChunks = true;
//        EntityPlayer player = null;
//        if (worldServerIn.playerEntities.size() == 1) {
//            player = worldServerIn.playerEntities.get(0);
//            if (this.eligibleChunksForSpawning.size() > 0 && player != null && player.chunkCoordX == this.lastPlayerChunkX && player.chunkCoordZ == this.lastPlayerChunkZ) {
//                updateEligibleChunks = false;
//            }
//        }
//        if (updateEligibleChunks) {
//            this.eligibleChunksForSpawning.clear();
//            int i = 0;
//            for (EntityPlayer entityplayer : worldServerIn.playerEntities) {
//                if (entityplayer.isSpectator()) continue;
//                int j = MathHelper.floor(entityplayer.posX / 16.0);
//                int k = MathHelper.floor(entityplayer.posZ / 16.0);
//                int l = 8;
//                for (int i1 = -8; i1 <= 8; ++i1) {
//                    for (int j1 = -8; j1 <= 8; ++j1) {
//                        PlayerChunkMapEntry playerchunkmapentry;
//                        boolean flag = i1 == -8 || i1 == 8 || j1 == -8 || j1 == 8;
//                        ChunkPos chunkpos = new ChunkPos(i1 + j, j1 + k);
//                        if (this.eligibleChunksForSpawning.contains(chunkpos)) continue;
//                        ++i;
//                        if (flag || !worldServerIn.getWorldBorder().contains(chunkpos) || (playerchunkmapentry = worldServerIn.getPlayerChunkMap().getEntry(chunkpos.x, chunkpos.z)) == null || !playerchunkmapentry.isSentToPlayers()) continue;
//                        this.eligibleChunksForSpawning.add(chunkpos);
//                    }
//                }
//            }
//            this.countChunkPos = i;
//            if (player != null) {
//                this.lastPlayerChunkX = player.chunkCoordX;
//                this.lastPlayerChunkZ = player.chunkCoordZ;
//            }
//        }
//        int j4 = 0;
//        BlockPos blockpos1 = worldServerIn.getSpawnPoint();
//        BlockPosM blockPosM = new BlockPosM(0, 0, 0);
//        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
//        for (EnumCreatureType enumcreaturetype : EnumCreatureType.values()) {
//            int l4;
//            int k4;
//            if (enumcreaturetype.getPeacefulCreature() && !spawnPeacefulMobs || !enumcreaturetype.getPeacefulCreature() && !spawnHostileMobs || enumcreaturetype.getAnimal() && !spawnOnSetTickRate || (k4 = Reflector.ForgeWorld_countEntities.exists() ? Reflector.callInt(worldServerIn, Reflector.ForgeWorld_countEntities, enumcreaturetype, true) : worldServerIn.countEntities(enumcreaturetype.getCreatureClass())) > (l4 = enumcreaturetype.getMaxNumberOfCreature() * this.countChunkPos / MOB_COUNT_DIV)) continue;
//            Collection<ChunkPos> chunksForSpawning = this.eligibleChunksForSpawning;
//            if (Reflector.ForgeHooksClient.exists()) {
//                ArrayList<ChunkPos> shuffled = Lists.newArrayList(chunksForSpawning);
//                Collections.shuffle(shuffled);
//                chunksForSpawning = shuffled;
//            }
//            for (ChunkPos chunkpos1 : chunksForSpawning) {
//                BlockPosM blockpos = WorldEntitySpawner.getRandomChunkPosition((World)worldServerIn, chunkpos1.x, chunkpos1.z, blockPosM);
//                int k1 = blockpos.getX();
//                int l1 = blockpos.getY();
//                int i2 = blockpos.getZ();
//                IBlockState iblockstate = worldServerIn.getBlockState(blockpos);
//                if (iblockstate.l()) continue;
//                int j2 = 0;
//                for (int k2 = 0; k2 < 3; ++k2) {
//                    int l2 = k1;
//                    int i3 = l1;
//                    int j3 = i2;
//                    int k3 = 6;
//                    Biome.SpawnListEntry biome$spawnlistentry = null;
//                    IEntityLivingData ientitylivingdata = null;
//                    int l3 = MathHelper.ceil(Math.random() * 4.0);
//                    for (int i4 = 0; i4 < l3; ++i4) {
//                        boolean canSpawn;
//                        EntityLiving entityliving;
//                        blockpos$mutableblockpos.setPos(l2 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6), i3 += worldServerIn.rand.nextInt(1) - worldServerIn.rand.nextInt(1), j3 += worldServerIn.rand.nextInt(6) - worldServerIn.rand.nextInt(6));
//                        float f = (float)l2 + 0.5f;
//                        float f1 = (float)j3 + 0.5f;
//                        if (worldServerIn.isAnyPlayerWithinRangeAt(f, i3, f1, 24.0) || !(blockpos1.distanceSq(f, i3, f1) >= 576.0)) continue;
//                        if (biome$spawnlistentry == null && (biome$spawnlistentry = worldServerIn.getSpawnListEntryForTypeAt(enumcreaturetype, blockpos$mutableblockpos)) == null) continue block7;
//                        if (!worldServerIn.canCreatureTypeSpawnHere(enumcreaturetype, biome$spawnlistentry, (BlockPos)blockpos$mutableblockpos) || !WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity((Class)biome$spawnlistentry.entityClass), (World)worldServerIn, (BlockPos)blockpos$mutableblockpos)) continue;
//                        try {
//                            entityliving = this.mapSampleEntitiesByClass.get(biome$spawnlistentry.entityClass);
//                            if (entityliving == null) {
//                                entityliving = Reflector.ForgeBiomeSpawnListEntry_newInstance.exists() ? (EntityLiving)((Object)Reflector.call(biome$spawnlistentry, Reflector.ForgeBiomeSpawnListEntry_newInstance, worldServerIn)) : (EntityLiving)((Object)biome$spawnlistentry.entityClass.getConstructor(World.class).newInstance(worldServerIn));
//                                this.mapSampleEntitiesByClass.put(biome$spawnlistentry.entityClass, entityliving);
//                            }
//                        }
//                        catch (Exception exception) {
//                            exception.printStackTrace();
//                            return j4;
//                        }
//                        entityliving.setLocationAndAngles(f, i3, f1, worldServerIn.rand.nextFloat() * 360.0f, 0.0f);
//                        boolean bl = Reflector.ForgeEventFactory_canEntitySpawn.exists() ? ReflectorForge.canEntitySpawn(entityliving, (World)worldServerIn, f, i3, f1) : (canSpawn = entityliving.getCanSpawnHere() && entityliving.isNotColliding());
//                        if (canSpawn) {
//                            int maxSpawnedInChunk;
//                            this.mapSampleEntitiesByClass.remove(biome$spawnlistentry.entityClass);
//                            if (!ReflectorForge.doSpecialSpawn(entityliving, (World)worldServerIn, f, i3, f1)) {
//                                ientitylivingdata = entityliving.onInitialSpawn(worldServerIn.getDifficultyForLocation(new BlockPos((Entity)entityliving)), ientitylivingdata);
//                            }
//                            if (entityliving.isNotColliding()) {
//                                ++j2;
//                                worldServerIn.spawnEntity(entityliving);
//                            } else {
//                                entityliving.setDead();
//                            }
//                            int n = maxSpawnedInChunk = Reflector.ForgeEventFactory_getMaxSpawnPackSize.exists() ? Reflector.callInt(Reflector.ForgeEventFactory_getMaxSpawnPackSize, new Object[]{entityliving}) : entityliving.getMaxSpawnedInChunk();
//                            if (j2 >= maxSpawnedInChunk) continue block6;
//                        }
//                        j4 += j2;
//                    }
//                }
//            }
//        }
//        return j4;
//    }
//
//    @Unique
//    private static BlockPosM getRandomChunkPosition(World worldIn, int x, int z, BlockPosM blockPosM) {
//        Chunk chunk = worldIn.getChunk(x, z);
//        int px = x * 16 + worldIn.rand.nextInt(16);
//        int pz = z * 16 + worldIn.rand.nextInt(16);
//        int k = MathHelper.roundUp(chunk.getHeightValue(px & 0xF, pz & 0xF) + 1, 16);
//        int py = worldIn.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
//        blockPosM.setXyz(px, py, pz);
//        return blockPosM;
//    }
}
