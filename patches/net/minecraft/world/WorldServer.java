package net.minecraft.world;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.INpc;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataCallableSave;
import net.minecraft.world.storage.loot.LootTableManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldServer extends World implements IThreadListener {
   private static final Logger LOGGER = LogManager.getLogger();
   private final MinecraftServer server;
   private final EntityTracker entityTracker;
   private final PlayerChunkMap playerChunkMap;
   private final Set<NextTickListEntry> pendingTickListEntriesHashSet = Sets.newHashSet();
   private final TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet = new TreeSet<>();
   private final Map<UUID, Entity> entitiesByUuid = Maps.newHashMap();
   public boolean disableLevelSaving;
   private boolean allPlayersSleeping;
   private int updateEntityTick;
   private final Teleporter worldTeleporter;
   private final WorldEntitySpawner entitySpawner = new WorldEntitySpawner();
   protected final VillageSiege villageSiege = new VillageSiege(this);
   private final WorldServer.ServerBlockEventList[] blockEventQueue = new WorldServer.ServerBlockEventList[]{
      new WorldServer.ServerBlockEventList(), new WorldServer.ServerBlockEventList()
   };
   private int blockEventCacheIndex;
   private final List<NextTickListEntry> pendingTickListEntriesThisTick = Lists.newArrayList();

   public WorldServer(MinecraftServer var1, ISaveHandler var2, WorldInfo var3, int var4, Profiler var5) {
      super(☃, ☃, DimensionType.getById(☃).createDimension(), ☃, false);
      this.server = ☃;
      this.entityTracker = new EntityTracker(this);
      this.playerChunkMap = new PlayerChunkMap(this);
      this.provider.setWorld(this);
      this.chunkProvider = this.createChunkProvider();
      this.worldTeleporter = new Teleporter(this);
      this.calculateInitialSkylight();
      this.calculateInitialWeather();
      this.getWorldBorder().setSize(☃.getMaxWorldSize());
   }

   @Override
   public World init() {
      this.mapStorage = new MapStorage(this.saveHandler);
      String ☃ = VillageCollection.fileNameForProvider(this.provider);
      VillageCollection ☃x = (VillageCollection)this.mapStorage.getOrLoadData(VillageCollection.class, ☃);
      if (☃x == null) {
         this.villageCollection = new VillageCollection(this);
         this.mapStorage.setData(☃, this.villageCollection);
      } else {
         this.villageCollection = ☃x;
         this.villageCollection.setWorldsForAll(this);
      }

      this.worldScoreboard = new ServerScoreboard(this.server);
      ScoreboardSaveData ☃xx = (ScoreboardSaveData)this.mapStorage.getOrLoadData(ScoreboardSaveData.class, "scoreboard");
      if (☃xx == null) {
         ☃xx = new ScoreboardSaveData();
         this.mapStorage.setData("scoreboard", ☃xx);
      }

      ☃xx.setScoreboard(this.worldScoreboard);
      ((ServerScoreboard)this.worldScoreboard).addDirtyRunnable(new WorldSavedDataCallableSave(☃xx));
      this.lootTable = new LootTableManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "loot_tables"));
      this.advancementManager = new AdvancementManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "advancements"));
      this.functionManager = new FunctionManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "functions"), this.server);
      this.getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
      this.getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
      this.getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
      this.getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
      this.getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());
      if (this.worldInfo.getBorderLerpTime() > 0L) {
         this.getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderLerpTarget(), this.worldInfo.getBorderLerpTime());
      } else {
         this.getWorldBorder().setTransition(this.worldInfo.getBorderSize());
      }

      return this;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.getWorldInfo().isHardcoreModeEnabled() && this.getDifficulty() != EnumDifficulty.HARD) {
         this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
      }

      this.provider.getBiomeProvider().cleanupCache();
      if (this.areAllPlayersAsleep()) {
         if (this.getGameRules().getBoolean("doDaylightCycle")) {
            long ☃ = this.worldInfo.getWorldTime() + 24000L;
            this.worldInfo.setWorldTime(☃ - ☃ % 24000L);
         }

         this.wakeAllPlayers();
      }

      this.profiler.startSection("mobSpawner");
      if (this.getGameRules().getBoolean("doMobSpawning") && this.worldInfo.getTerrainType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
         this.entitySpawner.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, this.worldInfo.getWorldTotalTime() % 400L == 0L);
      }

      this.profiler.endStartSection("chunkSource");
      this.chunkProvider.tick();
      int ☃ = this.calculateSkylightSubtracted(1.0F);
      if (☃ != this.getSkylightSubtracted()) {
         this.setSkylightSubtracted(☃);
      }

      this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);
      if (this.getGameRules().getBoolean("doDaylightCycle")) {
         this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
      }

      this.profiler.endStartSection("tickPending");
      this.tickUpdates(false);
      this.profiler.endStartSection("tickBlocks");
      this.updateBlocks();
      this.profiler.endStartSection("chunkMap");
      this.playerChunkMap.tick();
      this.profiler.endStartSection("village");
      this.villageCollection.tick();
      this.villageSiege.tick();
      this.profiler.endStartSection("portalForcer");
      this.worldTeleporter.removeStalePortalLocations(this.getTotalWorldTime());
      this.profiler.endSection();
      this.sendQueuedBlockEvents();
   }

   @Nullable
   public Biome.SpawnListEntry getSpawnListEntryForTypeAt(EnumCreatureType var1, BlockPos var2) {
      List<Biome.SpawnListEntry> ☃ = this.getChunkProvider().getPossibleCreatures(☃, ☃);
      return ☃ != null && !☃.isEmpty() ? WeightedRandom.getRandomItem(this.rand, ☃) : null;
   }

   public boolean canCreatureTypeSpawnHere(EnumCreatureType var1, Biome.SpawnListEntry var2, BlockPos var3) {
      List<Biome.SpawnListEntry> ☃ = this.getChunkProvider().getPossibleCreatures(☃, ☃);
      return ☃ != null && !☃.isEmpty() ? ☃.contains(☃) : false;
   }

   @Override
   public void updateAllPlayersSleepingFlag() {
      this.allPlayersSleeping = false;
      if (!this.playerEntities.isEmpty()) {
         int ☃ = 0;
         int ☃x = 0;

         for (EntityPlayer ☃xx : this.playerEntities) {
            if (☃xx.isSpectator()) {
               ☃++;
            } else if (☃xx.isPlayerSleeping()) {
               ☃x++;
            }
         }

         this.allPlayersSleeping = ☃x > 0 && ☃x >= this.playerEntities.size() - ☃;
      }
   }

   protected void wakeAllPlayers() {
      this.allPlayersSleeping = false;

      for (EntityPlayer ☃ : this.playerEntities.stream().filter(EntityPlayer::isPlayerSleeping).collect(Collectors.toList())) {
         ☃.wakeUpPlayer(false, false, true);
      }

      if (this.getGameRules().getBoolean("doWeatherCycle")) {
         this.resetRainAndThunder();
      }
   }

   private void resetRainAndThunder() {
      this.worldInfo.setRainTime(0);
      this.worldInfo.setRaining(false);
      this.worldInfo.setThunderTime(0);
      this.worldInfo.setThundering(false);
   }

   public boolean areAllPlayersAsleep() {
      if (this.allPlayersSleeping && !this.isRemote) {
         for (EntityPlayer ☃ : this.playerEntities) {
            if (!☃.isSpectator() && !☃.isPlayerFullyAsleep()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void setInitialSpawnLocation() {
      if (this.worldInfo.getSpawnY() <= 0) {
         this.worldInfo.setSpawnY(this.getSeaLevel() + 1);
      }

      int ☃ = this.worldInfo.getSpawnX();
      int ☃x = this.worldInfo.getSpawnZ();
      int ☃xx = 0;

      while (this.getGroundAboveSeaLevel(new BlockPos(☃, 0, ☃x)).getMaterial() == Material.AIR) {
         ☃ += this.rand.nextInt(8) - this.rand.nextInt(8);
         ☃x += this.rand.nextInt(8) - this.rand.nextInt(8);
         if (++☃xx == 10000) {
            break;
         }
      }

      this.worldInfo.setSpawnX(☃);
      this.worldInfo.setSpawnZ(☃x);
   }

   @Override
   protected boolean isChunkLoaded(int var1, int var2, boolean var3) {
      return this.getChunkProvider().chunkExists(☃, ☃);
   }

   protected void playerCheckLight() {
      this.profiler.startSection("playerCheckLight");
      if (!this.playerEntities.isEmpty()) {
         int ☃ = this.rand.nextInt(this.playerEntities.size());
         EntityPlayer ☃x = this.playerEntities.get(☃);
         int ☃xx = MathHelper.floor(☃x.posX) + this.rand.nextInt(11) - 5;
         int ☃xxx = MathHelper.floor(☃x.posY) + this.rand.nextInt(11) - 5;
         int ☃xxxx = MathHelper.floor(☃x.posZ) + this.rand.nextInt(11) - 5;
         this.checkLight(new BlockPos(☃xx, ☃xxx, ☃xxxx));
      }

      this.profiler.endSection();
   }

   @Override
   protected void updateBlocks() {
      this.playerCheckLight();
      if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         Iterator<Chunk> ☃ = this.playerChunkMap.getChunkIterator();

         while (☃.hasNext()) {
            ☃.next().onTick(false);
         }
      } else {
         int ☃ = this.getGameRules().getInt("randomTickSpeed");
         boolean ☃x = this.isRaining();
         boolean ☃xx = this.isThundering();
         this.profiler.startSection("pollingChunks");

         for (Iterator<Chunk> ☃xxx = this.playerChunkMap.getChunkIterator(); ☃xxx.hasNext(); this.profiler.endSection()) {
            this.profiler.startSection("getChunk");
            Chunk ☃xxxx = ☃xxx.next();
            int ☃xxxxx = ☃xxxx.x * 16;
            int ☃xxxxxx = ☃xxxx.z * 16;
            this.profiler.endStartSection("checkNextLight");
            ☃xxxx.enqueueRelightChecks();
            this.profiler.endStartSection("tickChunk");
            ☃xxxx.onTick(false);
            this.profiler.endStartSection("thunder");
            if (☃x && ☃xx && this.rand.nextInt(100000) == 0) {
               this.updateLCG = this.updateLCG * 3 + 1013904223;
               int ☃xxxxxxx = this.updateLCG >> 2;
               BlockPos ☃xxxxxxxx = this.adjustPosToNearbyEntity(new BlockPos(☃xxxxx + (☃xxxxxxx & 15), 0, ☃xxxxxx + (☃xxxxxxx >> 8 & 15)));
               if (this.isRainingAt(☃xxxxxxxx)) {
                  DifficultyInstance ☃xxxxxxxxx = this.getDifficultyForLocation(☃xxxxxxxx);
                  if (this.getGameRules().getBoolean("doMobSpawning") && this.rand.nextDouble() < ☃xxxxxxxxx.getAdditionalDifficulty() * 0.01) {
                     EntitySkeletonHorse ☃xxxxxxxxxx = new EntitySkeletonHorse(this);
                     ☃xxxxxxxxxx.setTrap(true);
                     ☃xxxxxxxxxx.setGrowingAge(0);
                     ☃xxxxxxxxxx.setPosition(☃xxxxxxxx.getX(), ☃xxxxxxxx.getY(), ☃xxxxxxxx.getZ());
                     this.spawnEntity(☃xxxxxxxxxx);
                     this.addWeatherEffect(new EntityLightningBolt(this, ☃xxxxxxxx.getX(), ☃xxxxxxxx.getY(), ☃xxxxxxxx.getZ(), true));
                  } else {
                     this.addWeatherEffect(new EntityLightningBolt(this, ☃xxxxxxxx.getX(), ☃xxxxxxxx.getY(), ☃xxxxxxxx.getZ(), false));
                  }
               }
            }

            this.profiler.endStartSection("iceandsnow");
            if (this.rand.nextInt(16) == 0) {
               this.updateLCG = this.updateLCG * 3 + 1013904223;
               int ☃xxxxxxx = this.updateLCG >> 2;
               BlockPos ☃xxxxxxxx = this.getPrecipitationHeight(new BlockPos(☃xxxxx + (☃xxxxxxx & 15), 0, ☃xxxxxx + (☃xxxxxxx >> 8 & 15)));
               BlockPos ☃xxxxxxxxx = ☃xxxxxxxx.down();
               if (this.canBlockFreezeNoWater(☃xxxxxxxxx)) {
                  this.setBlockState(☃xxxxxxxxx, Blocks.ICE.getDefaultState());
               }

               if (☃x && this.canSnowAt(☃xxxxxxxx, true)) {
                  this.setBlockState(☃xxxxxxxx, Blocks.SNOW_LAYER.getDefaultState());
               }

               if (☃x && this.getBiome(☃xxxxxxxxx).canRain()) {
                  this.getBlockState(☃xxxxxxxxx).getBlock().fillWithRain(this, ☃xxxxxxxxx);
               }
            }

            this.profiler.endStartSection("tickBlocks");
            if (☃ > 0) {
               for (ExtendedBlockStorage ☃xxxxxxxxxx : ☃xxxx.getBlockStorageArray()) {
                  if (☃xxxxxxxxxx != Chunk.NULL_BLOCK_STORAGE && ☃xxxxxxxxxx.needsRandomTick()) {
                     for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx < ☃; ☃xxxxxxxxxxx++) {
                        this.updateLCG = this.updateLCG * 3 + 1013904223;
                        int ☃xxxxxxxxxxxx = this.updateLCG >> 2;
                        int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx & 15;
                        int ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxx >> 8 & 15;
                        int ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx >> 16 & 15;
                        IBlockState ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxx.get(☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx);
                        Block ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxx.getBlock();
                        this.profiler.startSection("randomTick");
                        if (☃xxxxxxxxxxxxxxxxx.getTickRandomly()) {
                           ☃xxxxxxxxxxxxxxxxx.randomTick(
                              this,
                              new BlockPos(☃xxxxxxxxxxxxx + ☃xxxxx, ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxx.getYLocation(), ☃xxxxxxxxxxxxxx + ☃xxxxxx),
                              ☃xxxxxxxxxxxxxxxx,
                              this.rand
                           );
                        }

                        this.profiler.endSection();
                     }
                  }
               }
            }
         }

         this.profiler.endSection();
      }
   }

   protected BlockPos adjustPosToNearbyEntity(BlockPos var1) {
      BlockPos ☃ = this.getPrecipitationHeight(☃);
      AxisAlignedBB ☃x = new AxisAlignedBB(☃, new BlockPos(☃.getX(), this.getHeight(), ☃.getZ())).grow(3.0);
      List<EntityLivingBase> ☃xx = this.getEntitiesWithinAABB(EntityLivingBase.class, ☃x, new Predicate<EntityLivingBase>() {
         public boolean apply(@Nullable EntityLivingBase var1) {
            return ☃ != null && ☃.isEntityAlive() && WorldServer.this.canSeeSky(☃.getPosition());
         }
      });
      if (!☃xx.isEmpty()) {
         return ☃xx.get(this.rand.nextInt(☃xx.size())).getPosition();
      } else {
         if (☃.getY() == -1) {
            ☃ = ☃.up(2);
         }

         return ☃;
      }
   }

   @Override
   public boolean isBlockTickPending(BlockPos var1, Block var2) {
      NextTickListEntry ☃ = new NextTickListEntry(☃, ☃);
      return this.pendingTickListEntriesThisTick.contains(☃);
   }

   @Override
   public boolean isUpdateScheduled(BlockPos var1, Block var2) {
      NextTickListEntry ☃ = new NextTickListEntry(☃, ☃);
      return this.pendingTickListEntriesHashSet.contains(☃);
   }

   @Override
   public void scheduleUpdate(BlockPos var1, Block var2, int var3) {
      this.updateBlockTick(☃, ☃, ☃, 0);
   }

   @Override
   public void updateBlockTick(BlockPos var1, Block var2, int var3, int var4) {
      Material ☃ = ☃.getDefaultState().getMaterial();
      if (this.scheduledUpdatesAreImmediate && ☃ != Material.AIR) {
         if (☃.requiresUpdates()) {
            if (this.isAreaLoaded(☃.add(-8, -8, -8), ☃.add(8, 8, 8))) {
               IBlockState ☃x = this.getBlockState(☃);
               if (☃x.getMaterial() != Material.AIR && ☃x.getBlock() == ☃) {
                  ☃x.getBlock().updateTick(this, ☃, ☃x, this.rand);
               }
            }

            return;
         }

         ☃ = 1;
      }

      NextTickListEntry ☃x = new NextTickListEntry(☃, ☃);
      if (this.isBlockLoaded(☃)) {
         if (☃ != Material.AIR) {
            ☃x.setScheduledTime(☃ + this.worldInfo.getWorldTotalTime());
            ☃x.setPriority(☃);
         }

         if (!this.pendingTickListEntriesHashSet.contains(☃x)) {
            this.pendingTickListEntriesHashSet.add(☃x);
            this.pendingTickListEntriesTreeSet.add(☃x);
         }
      }
   }

   @Override
   public void scheduleBlockUpdate(BlockPos var1, Block var2, int var3, int var4) {
      NextTickListEntry ☃ = new NextTickListEntry(☃, ☃);
      ☃.setPriority(☃);
      Material ☃x = ☃.getDefaultState().getMaterial();
      if (☃x != Material.AIR) {
         ☃.setScheduledTime(☃ + this.worldInfo.getWorldTotalTime());
      }

      if (!this.pendingTickListEntriesHashSet.contains(☃)) {
         this.pendingTickListEntriesHashSet.add(☃);
         this.pendingTickListEntriesTreeSet.add(☃);
      }
   }

   @Override
   public void updateEntities() {
      if (this.playerEntities.isEmpty()) {
         if (this.updateEntityTick++ >= 300) {
            return;
         }
      } else {
         this.resetUpdateEntityTick();
      }

      this.provider.onWorldUpdateEntities();
      super.updateEntities();
   }

   @Override
   protected void tickPlayers() {
      super.tickPlayers();
      this.profiler.endStartSection("players");

      for (int ☃ = 0; ☃ < this.playerEntities.size(); ☃++) {
         Entity ☃x = this.playerEntities.get(☃);
         Entity ☃xx = ☃x.getRidingEntity();
         if (☃xx != null) {
            if (!☃xx.isDead && ☃xx.isPassenger(☃x)) {
               continue;
            }

            ☃x.dismountRidingEntity();
         }

         this.profiler.startSection("tick");
         if (!☃x.isDead) {
            try {
               this.updateEntity(☃x);
            } catch (Throwable var7) {
               CrashReport ☃xxx = CrashReport.makeCrashReport(var7, "Ticking player");
               CrashReportCategory ☃xxxx = ☃xxx.makeCategory("Player being ticked");
               ☃x.addEntityCrashInfo(☃xxxx);
               throw new ReportedException(☃xxx);
            }
         }

         this.profiler.endSection();
         this.profiler.startSection("remove");
         if (☃x.isDead) {
            int ☃xxx = ☃x.chunkCoordX;
            int ☃xxxx = ☃x.chunkCoordZ;
            if (☃x.addedToChunk && this.isChunkLoaded(☃xxx, ☃xxxx, true)) {
               this.getChunk(☃xxx, ☃xxxx).removeEntity(☃x);
            }

            this.loadedEntityList.remove(☃x);
            this.onEntityRemoved(☃x);
         }

         this.profiler.endSection();
      }
   }

   public void resetUpdateEntityTick() {
      this.updateEntityTick = 0;
   }

   @Override
   public boolean tickUpdates(boolean var1) {
      if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         return false;
      } else {
         int ☃ = this.pendingTickListEntriesTreeSet.size();
         if (☃ != this.pendingTickListEntriesHashSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
         } else {
            if (☃ > 65536) {
               ☃ = 65536;
            }

            this.profiler.startSection("cleaning");

            for (int ☃x = 0; ☃x < ☃; ☃x++) {
               NextTickListEntry ☃xx = this.pendingTickListEntriesTreeSet.first();
               if (!☃ && ☃xx.scheduledTime > this.worldInfo.getWorldTotalTime()) {
                  break;
               }

               this.pendingTickListEntriesTreeSet.remove(☃xx);
               this.pendingTickListEntriesHashSet.remove(☃xx);
               this.pendingTickListEntriesThisTick.add(☃xx);
            }

            this.profiler.endSection();
            this.profiler.startSection("ticking");
            Iterator<NextTickListEntry> ☃x = this.pendingTickListEntriesThisTick.iterator();

            while (☃x.hasNext()) {
               NextTickListEntry ☃xx = ☃x.next();
               ☃x.remove();
               int ☃xxx = 0;
               if (this.isAreaLoaded(☃xx.position.add(0, 0, 0), ☃xx.position.add(0, 0, 0))) {
                  IBlockState ☃xxxx = this.getBlockState(☃xx.position);
                  if (☃xxxx.getMaterial() != Material.AIR && Block.isEqualTo(☃xxxx.getBlock(), ☃xx.getBlock())) {
                     try {
                        ☃xxxx.getBlock().updateTick(this, ☃xx.position, ☃xxxx, this.rand);
                     } catch (Throwable var10) {
                        CrashReport ☃xxxxx = CrashReport.makeCrashReport(var10, "Exception while ticking a block");
                        CrashReportCategory ☃xxxxxx = ☃xxxxx.makeCategory("Block being ticked");
                        CrashReportCategory.addBlockInfo(☃xxxxxx, ☃xx.position, ☃xxxx);
                        throw new ReportedException(☃xxxxx);
                     }
                  }
               } else {
                  this.scheduleUpdate(☃xx.position, ☃xx.getBlock(), 0);
               }
            }

            this.profiler.endSection();
            this.pendingTickListEntriesThisTick.clear();
            return !this.pendingTickListEntriesTreeSet.isEmpty();
         }
      }
   }

   @Nullable
   @Override
   public List<NextTickListEntry> getPendingBlockUpdates(Chunk var1, boolean var2) {
      ChunkPos ☃ = ☃.getPos();
      int ☃x = (☃.x << 4) - 2;
      int ☃xx = ☃x + 16 + 2;
      int ☃xxx = (☃.z << 4) - 2;
      int ☃xxxx = ☃xxx + 16 + 2;
      return this.getPendingBlockUpdates(new StructureBoundingBox(☃x, 0, ☃xxx, ☃xx, 256, ☃xxxx), ☃);
   }

   @Nullable
   @Override
   public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox var1, boolean var2) {
      List<NextTickListEntry> ☃ = null;

      for (int ☃x = 0; ☃x < 2; ☃x++) {
         Iterator<NextTickListEntry> ☃xx;
         if (☃x == 0) {
            ☃xx = this.pendingTickListEntriesTreeSet.iterator();
         } else {
            ☃xx = this.pendingTickListEntriesThisTick.iterator();
         }

         while (☃xx.hasNext()) {
            NextTickListEntry ☃xxx = ☃xx.next();
            BlockPos ☃xxxx = ☃xxx.position;
            if (☃xxxx.getX() >= ☃.minX && ☃xxxx.getX() < ☃.maxX && ☃xxxx.getZ() >= ☃.minZ && ☃xxxx.getZ() < ☃.maxZ) {
               if (☃) {
                  if (☃x == 0) {
                     this.pendingTickListEntriesHashSet.remove(☃xxx);
                  }

                  ☃xx.remove();
               }

               if (☃ == null) {
                  ☃ = Lists.newArrayList();
               }

               ☃.add(☃xxx);
            }
         }
      }

      return ☃;
   }

   @Override
   public void updateEntityWithOptionalForce(Entity var1, boolean var2) {
      if (!this.canSpawnAnimals() && (☃ instanceof EntityAnimal || ☃ instanceof EntityWaterMob)) {
         ☃.setDead();
      }

      if (!this.canSpawnNPCs() && ☃ instanceof INpc) {
         ☃.setDead();
      }

      super.updateEntityWithOptionalForce(☃, ☃);
   }

   private boolean canSpawnNPCs() {
      return this.server.getCanSpawnNPCs();
   }

   private boolean canSpawnAnimals() {
      return this.server.getCanSpawnAnimals();
   }

   @Override
   protected IChunkProvider createChunkProvider() {
      IChunkLoader ☃ = this.saveHandler.getChunkLoader(this.provider);
      return new ChunkProviderServer(this, ☃, this.provider.createChunkGenerator());
   }

   @Override
   public boolean isBlockModifiable(EntityPlayer var1, BlockPos var2) {
      return !this.server.isBlockProtected(this, ☃, ☃) && this.getWorldBorder().contains(☃);
   }

   @Override
   public void initialize(WorldSettings var1) {
      if (!this.worldInfo.isInitialized()) {
         try {
            this.createSpawnPosition(☃);
            if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
               this.setDebugWorldSettings();
            }

            super.initialize(☃);
         } catch (Throwable var6) {
            CrashReport ☃ = CrashReport.makeCrashReport(var6, "Exception initializing level");

            try {
               this.addWorldInfoToCrashReport(☃);
            } catch (Throwable var5) {
            }

            throw new ReportedException(☃);
         }

         this.worldInfo.setServerInitialized(true);
      }
   }

   private void setDebugWorldSettings() {
      this.worldInfo.setMapFeaturesEnabled(false);
      this.worldInfo.setAllowCommands(true);
      this.worldInfo.setRaining(false);
      this.worldInfo.setThundering(false);
      this.worldInfo.setCleanWeatherTime(1000000000);
      this.worldInfo.setWorldTime(6000L);
      this.worldInfo.setGameType(GameType.SPECTATOR);
      this.worldInfo.setHardcore(false);
      this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
      this.worldInfo.setDifficultyLocked(true);
      this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
   }

   private void createSpawnPosition(WorldSettings var1) {
      if (!this.provider.canRespawnHere()) {
         this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
      } else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
      } else {
         this.findingSpawnPoint = true;
         BiomeProvider ☃ = this.provider.getBiomeProvider();
         List<Biome> ☃x = ☃.getBiomesToSpawnIn();
         Random ☃xx = new Random(this.getSeed());
         BlockPos ☃xxx = ☃.findBiomePosition(0, 0, 256, ☃x, ☃xx);
         int ☃xxxx = 8;
         int ☃xxxxx = this.provider.getAverageGroundLevel();
         int ☃xxxxxx = 8;
         if (☃xxx != null) {
            ☃xxxx = ☃xxx.getX();
            ☃xxxxxx = ☃xxx.getZ();
         } else {
            LOGGER.warn("Unable to find spawn biome");
         }

         int ☃xxxxxxx = 0;

         while (!this.provider.canCoordinateBeSpawn(☃xxxx, ☃xxxxxx)) {
            ☃xxxx += ☃xx.nextInt(64) - ☃xx.nextInt(64);
            ☃xxxxxx += ☃xx.nextInt(64) - ☃xx.nextInt(64);
            if (++☃xxxxxxx == 1000) {
               break;
            }
         }

         this.worldInfo.setSpawn(new BlockPos(☃xxxx, ☃xxxxx, ☃xxxxxx));
         this.findingSpawnPoint = false;
         if (☃.isBonusChestEnabled()) {
            this.createBonusChest();
         }
      }
   }

   protected void createBonusChest() {
      WorldGeneratorBonusChest ☃ = new WorldGeneratorBonusChest();

      for (int ☃x = 0; ☃x < 10; ☃x++) {
         int ☃xx = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
         int ☃xxx = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
         BlockPos ☃xxxx = this.getTopSolidOrLiquidBlock(new BlockPos(☃xx, 0, ☃xxx)).up();
         if (☃.generate(this, this.rand, ☃xxxx)) {
            break;
         }
      }
   }

   @Nullable
   public BlockPos getSpawnCoordinate() {
      return this.provider.getSpawnCoordinate();
   }

   public void saveAllChunks(boolean var1, @Nullable IProgressUpdate var2) throws MinecraftException {
      ChunkProviderServer ☃ = this.getChunkProvider();
      if (☃.canSave()) {
         if (☃ != null) {
            ☃.displaySavingString("Saving level");
         }

         this.saveLevel();
         if (☃ != null) {
            ☃.displayLoadingString("Saving chunks");
         }

         ☃.saveChunks(☃);

         for (Chunk ☃x : Lists.newArrayList(☃.getLoadedChunks())) {
            if (☃x != null && !this.playerChunkMap.contains(☃x.x, ☃x.z)) {
               ☃.queueUnload(☃x);
            }
         }
      }
   }

   public void flushToDisk() {
      ChunkProviderServer ☃ = this.getChunkProvider();
      if (☃.canSave()) {
         ☃.flushToDisk();
      }
   }

   protected void saveLevel() throws MinecraftException {
      this.checkSessionLock();

      for (WorldServer ☃ : this.server.worlds) {
         if (☃ instanceof WorldServerMulti) {
            ((WorldServerMulti)☃).saveAdditionalData();
         }
      }

      this.worldInfo.setBorderSize(this.getWorldBorder().getDiameter());
      this.worldInfo.getBorderCenterX(this.getWorldBorder().getCenterX());
      this.worldInfo.getBorderCenterZ(this.getWorldBorder().getCenterZ());
      this.worldInfo.setBorderSafeZone(this.getWorldBorder().getDamageBuffer());
      this.worldInfo.setBorderDamagePerBlock(this.getWorldBorder().getDamageAmount());
      this.worldInfo.setBorderWarningDistance(this.getWorldBorder().getWarningDistance());
      this.worldInfo.setBorderWarningTime(this.getWorldBorder().getWarningTime());
      this.worldInfo.setBorderLerpTarget(this.getWorldBorder().getTargetSize());
      this.worldInfo.setBorderLerpTime(this.getWorldBorder().getTimeUntilTarget());
      this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.server.getPlayerList().getHostPlayerData());
      this.mapStorage.saveAllData();
   }

   @Override
   public boolean spawnEntity(Entity var1) {
      return this.canAddEntity(☃) ? super.spawnEntity(☃) : false;
   }

   @Override
   public void loadEntities(Collection<Entity> var1) {
      for (Entity ☃ : Lists.newArrayList(☃)) {
         if (this.canAddEntity(☃)) {
            this.loadedEntityList.add(☃);
            this.onEntityAdded(☃);
         }
      }
   }

   private boolean canAddEntity(Entity var1) {
      if (☃.isDead) {
         LOGGER.warn("Tried to add entity {} but it was marked as removed already", EntityList.getKey(☃));
         return false;
      } else {
         UUID ☃ = ☃.getUniqueID();
         if (this.entitiesByUuid.containsKey(☃)) {
            Entity ☃x = this.entitiesByUuid.get(☃);
            if (this.unloadedEntityList.contains(☃x)) {
               this.unloadedEntityList.remove(☃x);
            } else {
               if (!(☃ instanceof EntityPlayer)) {
                  LOGGER.warn("Keeping entity {} that already exists with UUID {}", EntityList.getKey(☃x), ☃.toString());
                  return false;
               }

               LOGGER.warn("Force-added player with duplicate UUID {}", ☃.toString());
            }

            this.removeEntityDangerously(☃x);
         }

         return true;
      }
   }

   @Override
   protected void onEntityAdded(Entity var1) {
      super.onEntityAdded(☃);
      this.entitiesById.addKey(☃.getEntityId(), ☃);
      this.entitiesByUuid.put(☃.getUniqueID(), ☃);
      Entity[] ☃ = ☃.getParts();
      if (☃ != null) {
         for (Entity ☃x : ☃) {
            this.entitiesById.addKey(☃x.getEntityId(), ☃x);
         }
      }
   }

   @Override
   protected void onEntityRemoved(Entity var1) {
      super.onEntityRemoved(☃);
      this.entitiesById.removeObject(☃.getEntityId());
      this.entitiesByUuid.remove(☃.getUniqueID());
      Entity[] ☃ = ☃.getParts();
      if (☃ != null) {
         for (Entity ☃x : ☃) {
            this.entitiesById.removeObject(☃x.getEntityId());
         }
      }
   }

   @Override
   public boolean addWeatherEffect(Entity var1) {
      if (super.addWeatherEffect(☃)) {
         this.server
            .getPlayerList()
            .sendToAllNearExcept(null, ☃.posX, ☃.posY, ☃.posZ, 512.0, this.provider.getDimensionType().getId(), new SPacketSpawnGlobalEntity(☃));
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void setEntityState(Entity var1, byte var2) {
      this.getEntityTracker().sendToTrackingAndSelf(☃, new SPacketEntityStatus(☃, ☃));
   }

   public ChunkProviderServer getChunkProvider() {
      return (ChunkProviderServer)super.getChunkProvider();
   }

   @Override
   public Explosion newExplosion(@Nullable Entity var1, double var2, double var4, double var6, float var8, boolean var9, boolean var10) {
      Explosion ☃ = new Explosion(this, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      ☃.doExplosionA();
      ☃.doExplosionB(false);
      if (!☃) {
         ☃.clearAffectedBlockPositions();
      }

      for (EntityPlayer ☃x : this.playerEntities) {
         if (☃x.getDistanceSq(☃, ☃, ☃) < 4096.0) {
            ((EntityPlayerMP)☃x).connection.sendPacket(new SPacketExplosion(☃, ☃, ☃, ☃, ☃.getAffectedBlockPositions(), ☃.getPlayerKnockbackMap().get(☃x)));
         }
      }

      return ☃;
   }

   @Override
   public void addBlockEvent(BlockPos var1, Block var2, int var3, int var4) {
      BlockEventData ☃ = new BlockEventData(☃, ☃, ☃, ☃);

      for (BlockEventData ☃x : this.blockEventQueue[this.blockEventCacheIndex]) {
         if (☃x.equals(☃)) {
            return;
         }
      }

      this.blockEventQueue[this.blockEventCacheIndex].add(☃);
   }

   private void sendQueuedBlockEvents() {
      while (!this.blockEventQueue[this.blockEventCacheIndex].isEmpty()) {
         int ☃ = this.blockEventCacheIndex;
         this.blockEventCacheIndex ^= 1;

         for (BlockEventData ☃x : this.blockEventQueue[☃]) {
            if (this.fireBlockEvent(☃x)) {
               this.server
                  .getPlayerList()
                  .sendToAllNearExcept(
                     null,
                     ☃x.getPosition().getX(),
                     ☃x.getPosition().getY(),
                     ☃x.getPosition().getZ(),
                     64.0,
                     this.provider.getDimensionType().getId(),
                     new SPacketBlockAction(☃x.getPosition(), ☃x.getBlock(), ☃x.getEventID(), ☃x.getEventParameter())
                  );
            }
         }

         this.blockEventQueue[☃].clear();
      }
   }

   private boolean fireBlockEvent(BlockEventData var1) {
      IBlockState ☃ = this.getBlockState(☃.getPosition());
      return ☃.getBlock() == ☃.getBlock() ? ☃.onBlockEventReceived(this, ☃.getPosition(), ☃.getEventID(), ☃.getEventParameter()) : false;
   }

   public void flush() {
      this.saveHandler.flush();
   }

   @Override
   protected void updateWeather() {
      boolean ☃ = this.isRaining();
      super.updateWeather();
      if (this.prevRainingStrength != this.rainingStrength) {
         this.server
            .getPlayerList()
            .sendPacketToAllPlayersInDimension(new SPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionType().getId());
      }

      if (this.prevThunderingStrength != this.thunderingStrength) {
         this.server
            .getPlayerList()
            .sendPacketToAllPlayersInDimension(new SPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionType().getId());
      }

      if (☃ != this.isRaining()) {
         if (☃) {
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(2, 0.0F));
         } else {
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(1, 0.0F));
         }

         this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(7, this.rainingStrength));
         this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(8, this.thunderingStrength));
      }
   }

   @Nullable
   @Override
   public MinecraftServer getMinecraftServer() {
      return this.server;
   }

   public EntityTracker getEntityTracker() {
      return this.entityTracker;
   }

   public PlayerChunkMap getPlayerChunkMap() {
      return this.playerChunkMap;
   }

   public Teleporter getDefaultTeleporter() {
      return this.worldTeleporter;
   }

   public TemplateManager getStructureTemplateManager() {
      return this.saveHandler.getStructureTemplateManager();
   }

   public void spawnParticle(
      EnumParticleTypes var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15, int... var17
   ) {
      this.spawnParticle(☃, false, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public void spawnParticle(
      EnumParticleTypes var1,
      boolean var2,
      double var3,
      double var5,
      double var7,
      int var9,
      double var10,
      double var12,
      double var14,
      double var16,
      int... var18
   ) {
      SPacketParticles ☃ = new SPacketParticles(☃, ☃, (float)☃, (float)☃, (float)☃, (float)☃, (float)☃, (float)☃, (float)☃, ☃, ☃);

      for (int ☃x = 0; ☃x < this.playerEntities.size(); ☃x++) {
         EntityPlayerMP ☃xx = (EntityPlayerMP)this.playerEntities.get(☃x);
         this.sendPacketWithinDistance(☃xx, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public void spawnParticle(
      EntityPlayerMP var1,
      EnumParticleTypes var2,
      boolean var3,
      double var4,
      double var6,
      double var8,
      int var10,
      double var11,
      double var13,
      double var15,
      double var17,
      int... var19
   ) {
      Packet<?> ☃ = new SPacketParticles(☃, ☃, (float)☃, (float)☃, (float)☃, (float)☃, (float)☃, (float)☃, (float)☃, ☃, ☃);
      this.sendPacketWithinDistance(☃, ☃, ☃, ☃, ☃, ☃);
   }

   private void sendPacketWithinDistance(EntityPlayerMP var1, boolean var2, double var3, double var5, double var7, Packet<?> var9) {
      BlockPos ☃ = ☃.getPosition();
      double ☃x = ☃.distanceSq(☃, ☃, ☃);
      if (☃x <= 1024.0 || ☃ && ☃x <= 262144.0) {
         ☃.connection.sendPacket(☃);
      }
   }

   @Nullable
   public Entity getEntityFromUuid(UUID var1) {
      return this.entitiesByUuid.get(☃);
   }

   @Override
   public ListenableFuture<Object> addScheduledTask(Runnable var1) {
      return this.server.addScheduledTask(☃);
   }

   @Override
   public boolean isCallingFromMinecraftThread() {
      return this.server.isCallingFromMinecraftThread();
   }

   @Nullable
   @Override
   public BlockPos findNearestStructure(String var1, BlockPos var2, boolean var3) {
      return this.getChunkProvider().getNearestStructurePos(this, ☃, ☃, ☃);
   }

   public AdvancementManager getAdvancementManager() {
      return this.advancementManager;
   }

   public FunctionManager getFunctionManager() {
      return this.functionManager;
   }

   static class ServerBlockEventList extends ArrayList<BlockEventData> {
      private ServerBlockEventList() {
      }
   }
}
