package net.minecraft.world;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.pathfinding.PathWorldListener;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.loot.LootTableManager;

public abstract class World implements IBlockAccess {
   private int seaLevel = 63;
   protected boolean scheduledUpdatesAreImmediate;
   public final List<Entity> loadedEntityList = Lists.newArrayList();
   protected final List<Entity> unloadedEntityList = Lists.newArrayList();
   public final List<TileEntity> loadedTileEntityList = Lists.newArrayList();
   public final List<TileEntity> tickableTileEntities = Lists.newArrayList();
   private final List<TileEntity> addedTileEntityList = Lists.newArrayList();
   private final List<TileEntity> tileEntitiesToBeRemoved = Lists.newArrayList();
   public final List<EntityPlayer> playerEntities = Lists.newArrayList();
   public final List<Entity> weatherEffects = Lists.newArrayList();
   protected final IntHashMap<Entity> entitiesById = new IntHashMap<>();
   private final long cloudColour = 16777215L;
   private int skylightSubtracted;
   protected int updateLCG = new Random().nextInt();
   protected final int DIST_HASH_MAGIC = 1013904223;
   protected float prevRainingStrength;
   protected float rainingStrength;
   protected float prevThunderingStrength;
   protected float thunderingStrength;
   private int lastLightningBolt;
   public final Random rand = new Random();
   public final WorldProvider provider;
   protected PathWorldListener pathListener = new PathWorldListener();
   protected List<IWorldEventListener> eventListeners = Lists.newArrayList(new IWorldEventListener[]{this.pathListener});
   protected IChunkProvider chunkProvider;
   protected final ISaveHandler saveHandler;
   protected WorldInfo worldInfo;
   protected boolean findingSpawnPoint;
   protected MapStorage mapStorage;
   protected VillageCollection villageCollection;
   protected LootTableManager lootTable;
   protected AdvancementManager advancementManager;
   protected FunctionManager functionManager;
   public final Profiler profiler;
   private final Calendar calendar = Calendar.getInstance();
   protected Scoreboard worldScoreboard = new Scoreboard();
   public final boolean isRemote;
   protected boolean spawnHostileMobs = true;
   protected boolean spawnPeacefulMobs = true;
   private boolean processingLoadedTiles;
   private final WorldBorder worldBorder;
   int[] lightUpdateBlockList = new int[32768];

   protected World(ISaveHandler var1, WorldInfo var2, WorldProvider var3, Profiler var4, boolean var5) {
      this.saveHandler = ☃;
      this.profiler = ☃;
      this.worldInfo = ☃;
      this.provider = ☃;
      this.isRemote = ☃;
      this.worldBorder = ☃.createWorldBorder();
   }

   public World init() {
      return this;
   }

   @Override
   public Biome getBiome(final BlockPos var1) {
      if (this.isBlockLoaded(☃)) {
         Chunk ☃ = this.getChunk(☃);

         try {
            return ☃.getBiome(☃, this.provider.getBiomeProvider());
         } catch (Throwable var6) {
            CrashReport ☃x = CrashReport.makeCrashReport(var6, "Getting biome");
            CrashReportCategory ☃xx = ☃x.makeCategory("Coordinates of biome request");
            ☃xx.addDetail("Location", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  return CrashReportCategory.getCoordinateInfo(☃);
               }
            });
            throw new ReportedException(☃x);
         }
      } else {
         return this.provider.getBiomeProvider().getBiome(☃, Biomes.PLAINS);
      }
   }

   public BiomeProvider getBiomeProvider() {
      return this.provider.getBiomeProvider();
   }

   protected abstract IChunkProvider createChunkProvider();

   public void initialize(WorldSettings var1) {
      this.worldInfo.setServerInitialized(true);
   }

   @Nullable
   public MinecraftServer getMinecraftServer() {
      return null;
   }

   public void setInitialSpawnLocation() {
      this.setSpawnPoint(new BlockPos(8, 64, 8));
   }

   public IBlockState getGroundAboveSeaLevel(BlockPos var1) {
      BlockPos ☃ = new BlockPos(☃.getX(), this.getSeaLevel(), ☃.getZ());

      while (!this.isAirBlock(☃.up())) {
         ☃ = ☃.up();
      }

      return this.getBlockState(☃);
   }

   private boolean isValid(BlockPos var1) {
      return !this.isOutsideBuildHeight(☃) && ☃.getX() >= -30000000 && ☃.getZ() >= -30000000 && ☃.getX() < 30000000 && ☃.getZ() < 30000000;
   }

   private boolean isOutsideBuildHeight(BlockPos var1) {
      return ☃.getY() < 0 || ☃.getY() >= 256;
   }

   @Override
   public boolean isAirBlock(BlockPos var1) {
      return this.getBlockState(☃).getMaterial() == Material.AIR;
   }

   public boolean isBlockLoaded(BlockPos var1) {
      return this.isBlockLoaded(☃, true);
   }

   public boolean isBlockLoaded(BlockPos var1, boolean var2) {
      return this.isChunkLoaded(☃.getX() >> 4, ☃.getZ() >> 4, ☃);
   }

   public boolean isAreaLoaded(BlockPos var1, int var2) {
      return this.isAreaLoaded(☃, ☃, true);
   }

   public boolean isAreaLoaded(BlockPos var1, int var2, boolean var3) {
      return this.isAreaLoaded(☃.getX() - ☃, ☃.getY() - ☃, ☃.getZ() - ☃, ☃.getX() + ☃, ☃.getY() + ☃, ☃.getZ() + ☃, ☃);
   }

   public boolean isAreaLoaded(BlockPos var1, BlockPos var2) {
      return this.isAreaLoaded(☃, ☃, true);
   }

   public boolean isAreaLoaded(BlockPos var1, BlockPos var2, boolean var3) {
      return this.isAreaLoaded(☃.getX(), ☃.getY(), ☃.getZ(), ☃.getX(), ☃.getY(), ☃.getZ(), ☃);
   }

   public boolean isAreaLoaded(StructureBoundingBox var1) {
      return this.isAreaLoaded(☃, true);
   }

   public boolean isAreaLoaded(StructureBoundingBox var1, boolean var2) {
      return this.isAreaLoaded(☃.minX, ☃.minY, ☃.minZ, ☃.maxX, ☃.maxY, ☃.maxZ, ☃);
   }

   private boolean isAreaLoaded(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      if (☃ >= 0 && ☃ < 256) {
         ☃ >>= 4;
         ☃ >>= 4;
         ☃ >>= 4;
         ☃ >>= 4;

         for (int ☃ = ☃; ☃ <= ☃; ☃++) {
            for (int ☃x = ☃; ☃x <= ☃; ☃x++) {
               if (!this.isChunkLoaded(☃, ☃x, ☃)) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected abstract boolean isChunkLoaded(int var1, int var2, boolean var3);

   public Chunk getChunk(BlockPos var1) {
      return this.getChunk(☃.getX() >> 4, ☃.getZ() >> 4);
   }

   public Chunk getChunk(int var1, int var2) {
      return this.chunkProvider.provideChunk(☃, ☃);
   }

   public boolean isChunkGeneratedAt(int var1, int var2) {
      return this.isChunkLoaded(☃, ☃, false) ? true : this.chunkProvider.isChunkGeneratedAt(☃, ☃);
   }

   public boolean setBlockState(BlockPos var1, IBlockState var2, int var3) {
      if (this.isOutsideBuildHeight(☃)) {
         return false;
      } else if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         return false;
      } else {
         Chunk ☃ = this.getChunk(☃);
         Block ☃x = ☃.getBlock();
         IBlockState ☃xx = ☃.setBlockState(☃, ☃);
         if (☃xx == null) {
            return false;
         } else {
            if (☃.getLightOpacity() != ☃xx.getLightOpacity() || ☃.getLightValue() != ☃xx.getLightValue()) {
               this.profiler.startSection("checkLight");
               this.checkLight(☃);
               this.profiler.endSection();
            }

            if ((☃ & 2) != 0 && (!this.isRemote || (☃ & 4) == 0) && ☃.isPopulated()) {
               this.notifyBlockUpdate(☃, ☃xx, ☃, ☃);
            }

            if (!this.isRemote && (☃ & 1) != 0) {
               this.notifyNeighborsRespectDebug(☃, ☃xx.getBlock(), true);
               if (☃.hasComparatorInputOverride()) {
                  this.updateComparatorOutputLevel(☃, ☃x);
               }
            } else if (!this.isRemote && (☃ & 16) == 0) {
               this.updateObservingBlocksAt(☃, ☃x);
            }

            return true;
         }
      }
   }

   public boolean setBlockToAir(BlockPos var1) {
      return this.setBlockState(☃, Blocks.AIR.getDefaultState(), 3);
   }

   public boolean destroyBlock(BlockPos var1, boolean var2) {
      IBlockState ☃ = this.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      if (☃.getMaterial() == Material.AIR) {
         return false;
      } else {
         this.playEvent(2001, ☃, Block.getStateId(☃));
         if (☃) {
            ☃x.dropBlockAsItem(this, ☃, ☃, 0);
         }

         return this.setBlockState(☃, Blocks.AIR.getDefaultState(), 3);
      }
   }

   public boolean setBlockState(BlockPos var1, IBlockState var2) {
      return this.setBlockState(☃, ☃, 3);
   }

   public void notifyBlockUpdate(BlockPos var1, IBlockState var2, IBlockState var3, int var4) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).notifyBlockUpdate(this, ☃, ☃, ☃, ☃);
      }
   }

   public void notifyNeighborsRespectDebug(BlockPos var1, Block var2, boolean var3) {
      if (this.worldInfo.getTerrainType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
         this.notifyNeighborsOfStateChange(☃, ☃, ☃);
      }
   }

   public void markBlocksDirtyVertical(int var1, int var2, int var3, int var4) {
      if (☃ > ☃) {
         int ☃ = ☃;
         ☃ = ☃;
         ☃ = ☃;
      }

      if (this.provider.hasSkyLight()) {
         for (int ☃ = ☃; ☃ <= ☃; ☃++) {
            this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(☃, ☃, ☃));
         }
      }

      this.markBlockRangeForRenderUpdate(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public void markBlockRangeForRenderUpdate(BlockPos var1, BlockPos var2) {
      this.markBlockRangeForRenderUpdate(☃.getX(), ☃.getY(), ☃.getZ(), ☃.getX(), ☃.getY(), ☃.getZ());
   }

   public void markBlockRangeForRenderUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).markBlockRangeForRenderUpdate(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public void updateObservingBlocksAt(BlockPos var1, Block var2) {
      this.observedNeighborChanged(☃.west(), ☃, ☃);
      this.observedNeighborChanged(☃.east(), ☃, ☃);
      this.observedNeighborChanged(☃.down(), ☃, ☃);
      this.observedNeighborChanged(☃.up(), ☃, ☃);
      this.observedNeighborChanged(☃.north(), ☃, ☃);
      this.observedNeighborChanged(☃.south(), ☃, ☃);
   }

   public void notifyNeighborsOfStateChange(BlockPos var1, Block var2, boolean var3) {
      this.neighborChanged(☃.west(), ☃, ☃);
      this.neighborChanged(☃.east(), ☃, ☃);
      this.neighborChanged(☃.down(), ☃, ☃);
      this.neighborChanged(☃.up(), ☃, ☃);
      this.neighborChanged(☃.north(), ☃, ☃);
      this.neighborChanged(☃.south(), ☃, ☃);
      if (☃) {
         this.updateObservingBlocksAt(☃, ☃);
      }
   }

   public void notifyNeighborsOfStateExcept(BlockPos var1, Block var2, EnumFacing var3) {
      if (☃ != EnumFacing.WEST) {
         this.neighborChanged(☃.west(), ☃, ☃);
      }

      if (☃ != EnumFacing.EAST) {
         this.neighborChanged(☃.east(), ☃, ☃);
      }

      if (☃ != EnumFacing.DOWN) {
         this.neighborChanged(☃.down(), ☃, ☃);
      }

      if (☃ != EnumFacing.UP) {
         this.neighborChanged(☃.up(), ☃, ☃);
      }

      if (☃ != EnumFacing.NORTH) {
         this.neighborChanged(☃.north(), ☃, ☃);
      }

      if (☃ != EnumFacing.SOUTH) {
         this.neighborChanged(☃.south(), ☃, ☃);
      }
   }

   public void neighborChanged(BlockPos var1, final Block var2, BlockPos var3) {
      if (!this.isRemote) {
         IBlockState ☃ = this.getBlockState(☃);

         try {
            ☃.neighborChanged(this, ☃, ☃, ☃);
         } catch (Throwable var8) {
            CrashReport ☃x = CrashReport.makeCrashReport(var8, "Exception while updating neighbours");
            CrashReportCategory ☃xx = ☃x.makeCategory("Block being updated");
            ☃xx.addDetail("Source block type", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  try {
                     return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(☃), ☃.getTranslationKey(), ☃.getClass().getCanonicalName());
                  } catch (Throwable var2x) {
                     return "ID #" + Block.getIdFromBlock(☃);
                  }
               }
            });
            CrashReportCategory.addBlockInfo(☃xx, ☃, ☃);
            throw new ReportedException(☃x);
         }
      }
   }

   public void observedNeighborChanged(BlockPos var1, final Block var2, BlockPos var3) {
      if (!this.isRemote) {
         IBlockState ☃ = this.getBlockState(☃);
         if (☃.getBlock() == Blocks.OBSERVER) {
            try {
               ((BlockObserver)☃.getBlock()).observedNeighborChanged(☃, this, ☃, ☃, ☃);
            } catch (Throwable var8) {
               CrashReport ☃x = CrashReport.makeCrashReport(var8, "Exception while updating neighbours");
               CrashReportCategory ☃xx = ☃x.makeCategory("Block being updated");
               ☃xx.addDetail("Source block type", new ICrashReportDetail<String>() {
                  public String call() throws Exception {
                     try {
                        return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(☃), ☃.getTranslationKey(), ☃.getClass().getCanonicalName());
                     } catch (Throwable var2x) {
                        return "ID #" + Block.getIdFromBlock(☃);
                     }
                  }
               });
               CrashReportCategory.addBlockInfo(☃xx, ☃, ☃);
               throw new ReportedException(☃x);
            }
         }
      }
   }

   public boolean isBlockTickPending(BlockPos var1, Block var2) {
      return false;
   }

   public boolean canSeeSky(BlockPos var1) {
      return this.getChunk(☃).canSeeSky(☃);
   }

   public boolean canBlockSeeSky(BlockPos var1) {
      if (☃.getY() >= this.getSeaLevel()) {
         return this.canSeeSky(☃);
      } else {
         BlockPos ☃ = new BlockPos(☃.getX(), this.getSeaLevel(), ☃.getZ());
         if (!this.canSeeSky(☃)) {
            return false;
         } else {
            for (BlockPos var4 = ☃.down(); var4.getY() > ☃.getY(); var4 = var4.down()) {
               IBlockState ☃x = this.getBlockState(var4);
               if (☃x.getLightOpacity() > 0 && !☃x.getMaterial().isLiquid()) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int getLight(BlockPos var1) {
      if (☃.getY() < 0) {
         return 0;
      } else {
         if (☃.getY() >= 256) {
            ☃ = new BlockPos(☃.getX(), 255, ☃.getZ());
         }

         return this.getChunk(☃).getLightSubtracted(☃, 0);
      }
   }

   public int getLightFromNeighbors(BlockPos var1) {
      return this.getLight(☃, true);
   }

   public int getLight(BlockPos var1, boolean var2) {
      if (☃.getX() < -30000000 || ☃.getZ() < -30000000 || ☃.getX() >= 30000000 || ☃.getZ() >= 30000000) {
         return 15;
      } else if (☃ && this.getBlockState(☃).useNeighborBrightness()) {
         int ☃ = this.getLight(☃.up(), false);
         int ☃x = this.getLight(☃.east(), false);
         int ☃xx = this.getLight(☃.west(), false);
         int ☃xxx = this.getLight(☃.south(), false);
         int ☃xxxx = this.getLight(☃.north(), false);
         if (☃x > ☃) {
            ☃ = ☃x;
         }

         if (☃xx > ☃) {
            ☃ = ☃xx;
         }

         if (☃xxx > ☃) {
            ☃ = ☃xxx;
         }

         if (☃xxxx > ☃) {
            ☃ = ☃xxxx;
         }

         return ☃;
      } else if (☃.getY() < 0) {
         return 0;
      } else {
         if (☃.getY() >= 256) {
            ☃ = new BlockPos(☃.getX(), 255, ☃.getZ());
         }

         Chunk ☃xxxxx = this.getChunk(☃);
         return ☃xxxxx.getLightSubtracted(☃, this.skylightSubtracted);
      }
   }

   public BlockPos getHeight(BlockPos var1) {
      return new BlockPos(☃.getX(), this.getHeight(☃.getX(), ☃.getZ()), ☃.getZ());
   }

   public int getHeight(int var1, int var2) {
      int ☃;
      if (☃ >= -30000000 && ☃ >= -30000000 && ☃ < 30000000 && ☃ < 30000000) {
         if (this.isChunkLoaded(☃ >> 4, ☃ >> 4, true)) {
            ☃ = this.getChunk(☃ >> 4, ☃ >> 4).getHeightValue(☃ & 15, ☃ & 15);
         } else {
            ☃ = 0;
         }
      } else {
         ☃ = this.getSeaLevel() + 1;
      }

      return ☃;
   }

   @Deprecated
   public int getChunksLowestHorizon(int var1, int var2) {
      if (☃ >= -30000000 && ☃ >= -30000000 && ☃ < 30000000 && ☃ < 30000000) {
         if (!this.isChunkLoaded(☃ >> 4, ☃ >> 4, true)) {
            return 0;
         } else {
            Chunk ☃ = this.getChunk(☃ >> 4, ☃ >> 4);
            return ☃.getLowestHeight();
         }
      } else {
         return this.getSeaLevel() + 1;
      }
   }

   public int getLightFromNeighborsFor(EnumSkyBlock var1, BlockPos var2) {
      if (!this.provider.hasSkyLight() && ☃ == EnumSkyBlock.SKY) {
         return 0;
      } else {
         if (☃.getY() < 0) {
            ☃ = new BlockPos(☃.getX(), 0, ☃.getZ());
         }

         if (!this.isValid(☃)) {
            return ☃.defaultLightValue;
         } else if (!this.isBlockLoaded(☃)) {
            return ☃.defaultLightValue;
         } else if (this.getBlockState(☃).useNeighborBrightness()) {
            int ☃ = this.getLightFor(☃, ☃.up());
            int ☃x = this.getLightFor(☃, ☃.east());
            int ☃xx = this.getLightFor(☃, ☃.west());
            int ☃xxx = this.getLightFor(☃, ☃.south());
            int ☃xxxx = this.getLightFor(☃, ☃.north());
            if (☃x > ☃) {
               ☃ = ☃x;
            }

            if (☃xx > ☃) {
               ☃ = ☃xx;
            }

            if (☃xxx > ☃) {
               ☃ = ☃xxx;
            }

            if (☃xxxx > ☃) {
               ☃ = ☃xxxx;
            }

            return ☃;
         } else {
            Chunk ☃xxxxx = this.getChunk(☃);
            return ☃xxxxx.getLightFor(☃, ☃);
         }
      }
   }

   public int getLightFor(EnumSkyBlock var1, BlockPos var2) {
      if (☃.getY() < 0) {
         ☃ = new BlockPos(☃.getX(), 0, ☃.getZ());
      }

      if (!this.isValid(☃)) {
         return ☃.defaultLightValue;
      } else if (!this.isBlockLoaded(☃)) {
         return ☃.defaultLightValue;
      } else {
         Chunk ☃ = this.getChunk(☃);
         return ☃.getLightFor(☃, ☃);
      }
   }

   public void setLightFor(EnumSkyBlock var1, BlockPos var2, int var3) {
      if (this.isValid(☃)) {
         if (this.isBlockLoaded(☃)) {
            Chunk ☃ = this.getChunk(☃);
            ☃.setLightFor(☃, ☃, ☃);
            this.notifyLightSet(☃);
         }
      }
   }

   public void notifyLightSet(BlockPos var1) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).notifyLightSet(☃);
      }
   }

   @Override
   public int getCombinedLight(BlockPos var1, int var2) {
      int ☃ = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, ☃);
      int ☃x = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, ☃);
      if (☃x < ☃) {
         ☃x = ☃;
      }

      return ☃ << 20 | ☃x << 4;
   }

   public float getLightBrightness(BlockPos var1) {
      return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(☃)];
   }

   @Override
   public IBlockState getBlockState(BlockPos var1) {
      if (this.isOutsideBuildHeight(☃)) {
         return Blocks.AIR.getDefaultState();
      } else {
         Chunk ☃ = this.getChunk(☃);
         return ☃.getBlockState(☃);
      }
   }

   public boolean isDaytime() {
      return this.skylightSubtracted < 4;
   }

   @Nullable
   public RayTraceResult rayTraceBlocks(Vec3d var1, Vec3d var2) {
      return this.rayTraceBlocks(☃, ☃, false, false, false);
   }

   @Nullable
   public RayTraceResult rayTraceBlocks(Vec3d var1, Vec3d var2, boolean var3) {
      return this.rayTraceBlocks(☃, ☃, ☃, false, false);
   }

   @Nullable
   public RayTraceResult rayTraceBlocks(Vec3d var1, Vec3d var2, boolean var3, boolean var4, boolean var5) {
      if (Double.isNaN(☃.x) || Double.isNaN(☃.y) || Double.isNaN(☃.z)) {
         return null;
      } else if (!Double.isNaN(☃.x) && !Double.isNaN(☃.y) && !Double.isNaN(☃.z)) {
         int ☃ = MathHelper.floor(☃.x);
         int ☃x = MathHelper.floor(☃.y);
         int ☃xx = MathHelper.floor(☃.z);
         int ☃xxx = MathHelper.floor(☃.x);
         int ☃xxxx = MathHelper.floor(☃.y);
         int ☃xxxxx = MathHelper.floor(☃.z);
         BlockPos ☃xxxxxx = new BlockPos(☃xxx, ☃xxxx, ☃xxxxx);
         IBlockState ☃xxxxxxx = this.getBlockState(☃xxxxxx);
         Block ☃xxxxxxxx = ☃xxxxxxx.getBlock();
         if ((!☃ || ☃xxxxxxx.getCollisionBoundingBox(this, ☃xxxxxx) != Block.NULL_AABB) && ☃xxxxxxxx.canCollideCheck(☃xxxxxxx, ☃)) {
            RayTraceResult ☃xxxxxxxxx = ☃xxxxxxx.collisionRayTrace(this, ☃xxxxxx, ☃, ☃);
            if (☃xxxxxxxxx != null) {
               return ☃xxxxxxxxx;
            }
         }

         RayTraceResult ☃xxxxxxxxx = null;
         int ☃xxxxxxxxxx = 200;

         while (☃xxxxxxxxxx-- >= 0) {
            if (Double.isNaN(☃.x) || Double.isNaN(☃.y) || Double.isNaN(☃.z)) {
               return null;
            }

            if (☃xxx == ☃ && ☃xxxx == ☃x && ☃xxxxx == ☃xx) {
               return ☃ ? ☃xxxxxxxxx : null;
            }

            boolean ☃xxxxxxxxxxx = true;
            boolean ☃xxxxxxxxxxxx = true;
            boolean ☃xxxxxxxxxxxxx = true;
            double ☃xxxxxxxxxxxxxx = 999.0;
            double ☃xxxxxxxxxxxxxxx = 999.0;
            double ☃xxxxxxxxxxxxxxxx = 999.0;
            if (☃ > ☃xxx) {
               ☃xxxxxxxxxxxxxx = ☃xxx + 1.0;
            } else if (☃ < ☃xxx) {
               ☃xxxxxxxxxxxxxx = ☃xxx + 0.0;
            } else {
               ☃xxxxxxxxxxx = false;
            }

            if (☃x > ☃xxxx) {
               ☃xxxxxxxxxxxxxxx = ☃xxxx + 1.0;
            } else if (☃x < ☃xxxx) {
               ☃xxxxxxxxxxxxxxx = ☃xxxx + 0.0;
            } else {
               ☃xxxxxxxxxxxx = false;
            }

            if (☃xx > ☃xxxxx) {
               ☃xxxxxxxxxxxxxxxx = ☃xxxxx + 1.0;
            } else if (☃xx < ☃xxxxx) {
               ☃xxxxxxxxxxxxxxxx = ☃xxxxx + 0.0;
            } else {
               ☃xxxxxxxxxxxxx = false;
            }

            double ☃xxxxxxxxxxxxxxxxx = 999.0;
            double ☃xxxxxxxxxxxxxxxxxx = 999.0;
            double ☃xxxxxxxxxxxxxxxxxxx = 999.0;
            double ☃xxxxxxxxxxxxxxxxxxxx = ☃.x - ☃.x;
            double ☃xxxxxxxxxxxxxxxxxxxxx = ☃.y - ☃.y;
            double ☃xxxxxxxxxxxxxxxxxxxxxx = ☃.z - ☃.z;
            if (☃xxxxxxxxxxx) {
               ☃xxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxx - ☃.x) / ☃xxxxxxxxxxxxxxxxxxxx;
            }

            if (☃xxxxxxxxxxxx) {
               ☃xxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxx - ☃.y) / ☃xxxxxxxxxxxxxxxxxxxxx;
            }

            if (☃xxxxxxxxxxxxx) {
               ☃xxxxxxxxxxxxxxxxxxx = (☃xxxxxxxxxxxxxxxx - ☃.z) / ☃xxxxxxxxxxxxxxxxxxxxxx;
            }

            if (☃xxxxxxxxxxxxxxxxx == -0.0) {
               ☃xxxxxxxxxxxxxxxxx = -1.0E-4;
            }

            if (☃xxxxxxxxxxxxxxxxxx == -0.0) {
               ☃xxxxxxxxxxxxxxxxxx = -1.0E-4;
            }

            if (☃xxxxxxxxxxxxxxxxxxx == -0.0) {
               ☃xxxxxxxxxxxxxxxxxxx = -1.0E-4;
            }

            EnumFacing ☃xxxxxxxxxxxxxxxxxxxxxxx;
            if (☃xxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxx && ☃xxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxx) {
               ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃ > ☃xxx ? EnumFacing.WEST : EnumFacing.EAST;
               ☃ = new Vec3d(☃xxxxxxxxxxxxxx, ☃.y + ☃xxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx, ☃.z + ☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxx);
            } else if (☃xxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxx) {
               ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃x > ☃xxxx ? EnumFacing.DOWN : EnumFacing.UP;
               ☃ = new Vec3d(☃.x + ☃xxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃.z + ☃xxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxx);
            } else {
               ☃xxxxxxxxxxxxxxxxxxxxxxx = ☃xx > ☃xxxxx ? EnumFacing.NORTH : EnumFacing.SOUTH;
               ☃ = new Vec3d(☃.x + ☃xxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx, ☃.y + ☃xxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx);
            }

            ☃xxx = MathHelper.floor(☃.x) - (☃xxxxxxxxxxxxxxxxxxxxxxx == EnumFacing.EAST ? 1 : 0);
            ☃xxxx = MathHelper.floor(☃.y) - (☃xxxxxxxxxxxxxxxxxxxxxxx == EnumFacing.UP ? 1 : 0);
            ☃xxxxx = MathHelper.floor(☃.z) - (☃xxxxxxxxxxxxxxxxxxxxxxx == EnumFacing.SOUTH ? 1 : 0);
            ☃xxxxxx = new BlockPos(☃xxx, ☃xxxx, ☃xxxxx);
            IBlockState ☃xxxxxxxxxxxxxxxxxxxxxxxx = this.getBlockState(☃xxxxxx);
            Block ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxx.getBlock();
            if (!☃
               || ☃xxxxxxxxxxxxxxxxxxxxxxxx.getMaterial() == Material.PORTAL
               || ☃xxxxxxxxxxxxxxxxxxxxxxxx.getCollisionBoundingBox(this, ☃xxxxxx) != Block.NULL_AABB) {
               if (☃xxxxxxxxxxxxxxxxxxxxxxxxx.canCollideCheck(☃xxxxxxxxxxxxxxxxxxxxxxxx, ☃)) {
                  RayTraceResult ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxxxxx.collisionRayTrace(this, ☃xxxxxx, ☃, ☃);
                  if (☃xxxxxxxxxxxxxxxxxxxxxxxxxx != null) {
                     return ☃xxxxxxxxxxxxxxxxxxxxxxxxxx;
                  }
               } else {
                  ☃xxxxxxxxx = new RayTraceResult(RayTraceResult.Type.MISS, ☃, ☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxx);
               }
            }
         }

         return ☃ ? ☃xxxxxxxxx : null;
      } else {
         return null;
      }
   }

   public void playSound(@Nullable EntityPlayer var1, BlockPos var2, SoundEvent var3, SoundCategory var4, float var5, float var6) {
      this.playSound(☃, ☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5, ☃, ☃, ☃, ☃);
   }

   public void playSound(@Nullable EntityPlayer var1, double var2, double var4, double var6, SoundEvent var8, SoundCategory var9, float var10, float var11) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).playSoundToAllNearExcept(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public void playSound(double var1, double var3, double var5, SoundEvent var7, SoundCategory var8, float var9, float var10, boolean var11) {
   }

   public void playRecord(BlockPos var1, @Nullable SoundEvent var2) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).playRecord(☃, ☃);
      }
   }

   public void spawnParticle(EnumParticleTypes var1, double var2, double var4, double var6, double var8, double var10, double var12, int... var14) {
      this.spawnParticle(☃.getParticleID(), ☃.getShouldIgnoreRange(), ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   public void spawnAlwaysVisibleParticle(int var1, double var2, double var4, double var6, double var8, double var10, double var12, int... var14) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).spawnParticle(☃, false, true, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public void spawnParticle(EnumParticleTypes var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
      this.spawnParticle(☃.getParticleID(), ☃.getShouldIgnoreRange() || ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
   }

   private void spawnParticle(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).spawnParticle(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   public boolean addWeatherEffect(Entity var1) {
      this.weatherEffects.add(☃);
      return true;
   }

   public boolean spawnEntity(Entity var1) {
      int ☃ = MathHelper.floor(☃.posX / 16.0);
      int ☃x = MathHelper.floor(☃.posZ / 16.0);
      boolean ☃xx = ☃.forceSpawn;
      if (☃ instanceof EntityPlayer) {
         ☃xx = true;
      }

      if (!☃xx && !this.isChunkLoaded(☃, ☃x, false)) {
         return false;
      } else {
         if (☃ instanceof EntityPlayer) {
            EntityPlayer ☃xxx = (EntityPlayer)☃;
            this.playerEntities.add(☃xxx);
            this.updateAllPlayersSleepingFlag();
         }

         this.getChunk(☃, ☃x).addEntity(☃);
         this.loadedEntityList.add(☃);
         this.onEntityAdded(☃);
         return true;
      }
   }

   protected void onEntityAdded(Entity var1) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).onEntityAdded(☃);
      }
   }

   protected void onEntityRemoved(Entity var1) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).onEntityRemoved(☃);
      }
   }

   public void removeEntity(Entity var1) {
      if (☃.isBeingRidden()) {
         ☃.removePassengers();
      }

      if (☃.isRiding()) {
         ☃.dismountRidingEntity();
      }

      ☃.setDead();
      if (☃ instanceof EntityPlayer) {
         this.playerEntities.remove(☃);
         this.updateAllPlayersSleepingFlag();
         this.onEntityRemoved(☃);
      }
   }

   public void removeEntityDangerously(Entity var1) {
      ☃.setDropItemsWhenDead(false);
      ☃.setDead();
      if (☃ instanceof EntityPlayer) {
         this.playerEntities.remove(☃);
         this.updateAllPlayersSleepingFlag();
      }

      int ☃ = ☃.chunkCoordX;
      int ☃x = ☃.chunkCoordZ;
      if (☃.addedToChunk && this.isChunkLoaded(☃, ☃x, true)) {
         this.getChunk(☃, ☃x).removeEntity(☃);
      }

      this.loadedEntityList.remove(☃);
      this.onEntityRemoved(☃);
   }

   public void addEventListener(IWorldEventListener var1) {
      this.eventListeners.add(☃);
   }

   public void removeEventListener(IWorldEventListener var1) {
      this.eventListeners.remove(☃);
   }

   private boolean getCollisionBoxes(@Nullable Entity var1, AxisAlignedBB var2, boolean var3, @Nullable List<AxisAlignedBB> var4) {
      int ☃ = MathHelper.floor(☃.minX) - 1;
      int ☃x = MathHelper.ceil(☃.maxX) + 1;
      int ☃xx = MathHelper.floor(☃.minY) - 1;
      int ☃xxx = MathHelper.ceil(☃.maxY) + 1;
      int ☃xxxx = MathHelper.floor(☃.minZ) - 1;
      int ☃xxxxx = MathHelper.ceil(☃.maxZ) + 1;
      WorldBorder ☃xxxxxx = this.getWorldBorder();
      boolean ☃xxxxxxx = ☃ != null && ☃.isOutsideBorder();
      boolean ☃xxxxxxxx = ☃ != null && this.isInsideWorldBorder(☃);
      IBlockState ☃xxxxxxxxx = Blocks.STONE.getDefaultState();
      BlockPos.PooledMutableBlockPos ☃xxxxxxxxxx = BlockPos.PooledMutableBlockPos.retain();

      try {
         for (int ☃xxxxxxxxxxx = ☃; ☃xxxxxxxxxxx < ☃x; ☃xxxxxxxxxxx++) {
            for (int ☃xxxxxxxxxxxx = ☃xxxx; ☃xxxxxxxxxxxx < ☃xxxxx; ☃xxxxxxxxxxxx++) {
               boolean ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx == ☃ || ☃xxxxxxxxxxx == ☃x - 1;
               boolean ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxxxx == ☃xxxx || ☃xxxxxxxxxxxx == ☃xxxxx - 1;
               if ((!☃xxxxxxxxxxxxx || !☃xxxxxxxxxxxxxx) && this.isBlockLoaded(☃xxxxxxxxxx.setPos(☃xxxxxxxxxxx, 64, ☃xxxxxxxxxxxx))) {
                  for (int ☃xxxxxxxxxxxxxxx = ☃xx; ☃xxxxxxxxxxxxxxx < ☃xxx; ☃xxxxxxxxxxxxxxx++) {
                     if (!☃xxxxxxxxxxxxx && !☃xxxxxxxxxxxxxx || ☃xxxxxxxxxxxxxxx != ☃xxx - 1) {
                        if (☃) {
                           if (☃xxxxxxxxxxx < -30000000 || ☃xxxxxxxxxxx >= 30000000 || ☃xxxxxxxxxxxx < -30000000 || ☃xxxxxxxxxxxx >= 30000000) {
                              return true;
                           }
                        } else if (☃ != null && ☃xxxxxxx == ☃xxxxxxxx) {
                           ☃.setOutsideBorder(!☃xxxxxxxx);
                        }

                        ☃xxxxxxxxxx.setPos(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxxx);
                        IBlockState ☃xxxxxxxxxxxxxxxx;
                        if (!☃ && !☃xxxxxx.contains(☃xxxxxxxxxx) && ☃xxxxxxxx) {
                           ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxx;
                        } else {
                           ☃xxxxxxxxxxxxxxxx = this.getBlockState(☃xxxxxxxxxx);
                        }

                        ☃xxxxxxxxxxxxxxxx.addCollisionBoxToList(this, ☃xxxxxxxxxx, ☃, ☃, ☃, false);
                        if (☃ && !☃.isEmpty()) {
                           return true;
                        }
                     }
                  }
               }
            }
         }
      } finally {
         ☃xxxxxxxxxx.release();
      }

      return !☃.isEmpty();
   }

   public List<AxisAlignedBB> getCollisionBoxes(@Nullable Entity var1, AxisAlignedBB var2) {
      List<AxisAlignedBB> ☃ = Lists.newArrayList();
      this.getCollisionBoxes(☃, ☃, false, ☃);
      if (☃ != null) {
         List<Entity> ☃x = this.getEntitiesWithinAABBExcludingEntity(☃, ☃.grow(0.25));

         for (int ☃xx = 0; ☃xx < ☃x.size(); ☃xx++) {
            Entity ☃xxx = ☃x.get(☃xx);
            if (!☃.isRidingSameEntity(☃xxx)) {
               AxisAlignedBB ☃xxxx = ☃xxx.getCollisionBoundingBox();
               if (☃xxxx != null && ☃xxxx.intersects(☃)) {
                  ☃.add(☃xxxx);
               }

               ☃xxxx = ☃.getCollisionBox(☃xxx);
               if (☃xxxx != null && ☃xxxx.intersects(☃)) {
                  ☃.add(☃xxxx);
               }
            }
         }
      }

      return ☃;
   }

   public boolean isInsideWorldBorder(Entity var1) {
      double ☃ = this.worldBorder.minX();
      double ☃x = this.worldBorder.minZ();
      double ☃xx = this.worldBorder.maxX();
      double ☃xxx = this.worldBorder.maxZ();
      if (☃.isOutsideBorder()) {
         ☃++;
         ☃x++;
         ☃xx--;
         ☃xxx--;
      } else {
         ☃--;
         ☃x--;
         ☃xx++;
         ☃xxx++;
      }

      return ☃.posX > ☃ && ☃.posX < ☃xx && ☃.posZ > ☃x && ☃.posZ < ☃xxx;
   }

   public boolean collidesWithAnyBlock(AxisAlignedBB var1) {
      return this.getCollisionBoxes(null, ☃, true, Lists.newArrayList());
   }

   public int calculateSkylightSubtracted(float var1) {
      float ☃ = this.getCelestialAngle(☃);
      float ☃x = 1.0F - (MathHelper.cos(☃ * (float) (Math.PI * 2)) * 2.0F + 0.5F);
      ☃x = MathHelper.clamp(☃x, 0.0F, 1.0F);
      ☃x = 1.0F - ☃x;
      ☃x = (float)(☃x * (1.0 - this.getRainStrength(☃) * 5.0F / 16.0));
      ☃x = (float)(☃x * (1.0 - this.getThunderStrength(☃) * 5.0F / 16.0));
      ☃x = 1.0F - ☃x;
      return (int)(☃x * 11.0F);
   }

   public float getSunBrightness(float var1) {
      float ☃ = this.getCelestialAngle(☃);
      float ☃x = 1.0F - (MathHelper.cos(☃ * (float) (Math.PI * 2)) * 2.0F + 0.2F);
      ☃x = MathHelper.clamp(☃x, 0.0F, 1.0F);
      ☃x = 1.0F - ☃x;
      ☃x = (float)(☃x * (1.0 - this.getRainStrength(☃) * 5.0F / 16.0));
      ☃x = (float)(☃x * (1.0 - this.getThunderStrength(☃) * 5.0F / 16.0));
      return ☃x * 0.8F + 0.2F;
   }

   public Vec3d getSkyColor(Entity var1, float var2) {
      float ☃ = this.getCelestialAngle(☃);
      float ☃x = MathHelper.cos(☃ * (float) (Math.PI * 2)) * 2.0F + 0.5F;
      ☃x = MathHelper.clamp(☃x, 0.0F, 1.0F);
      int ☃xx = MathHelper.floor(☃.posX);
      int ☃xxx = MathHelper.floor(☃.posY);
      int ☃xxxx = MathHelper.floor(☃.posZ);
      BlockPos ☃xxxxx = new BlockPos(☃xx, ☃xxx, ☃xxxx);
      Biome ☃xxxxxx = this.getBiome(☃xxxxx);
      float ☃xxxxxxx = ☃xxxxxx.getTemperature(☃xxxxx);
      int ☃xxxxxxxx = ☃xxxxxx.getSkyColorByTemp(☃xxxxxxx);
      float ☃xxxxxxxxx = (☃xxxxxxxx >> 16 & 0xFF) / 255.0F;
      float ☃xxxxxxxxxx = (☃xxxxxxxx >> 8 & 0xFF) / 255.0F;
      float ☃xxxxxxxxxxx = (☃xxxxxxxx & 0xFF) / 255.0F;
      ☃xxxxxxxxx *= ☃x;
      ☃xxxxxxxxxx *= ☃x;
      ☃xxxxxxxxxxx *= ☃x;
      float ☃xxxxxxxxxxxx = this.getRainStrength(☃);
      if (☃xxxxxxxxxxxx > 0.0F) {
         float ☃xxxxxxxxxxxxx = (☃xxxxxxxxx * 0.3F + ☃xxxxxxxxxx * 0.59F + ☃xxxxxxxxxxx * 0.11F) * 0.6F;
         float ☃xxxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxxx * 0.75F;
         ☃xxxxxxxxx = ☃xxxxxxxxx * ☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxx);
         ☃xxxxxxxxxx = ☃xxxxxxxxxx * ☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxx);
         ☃xxxxxxxxxxx = ☃xxxxxxxxxxx * ☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxx);
      }

      float ☃xxxxxxxxxxxxx = this.getThunderStrength(☃);
      if (☃xxxxxxxxxxxxx > 0.0F) {
         float ☃xxxxxxxxxxxxxx = (☃xxxxxxxxx * 0.3F + ☃xxxxxxxxxx * 0.59F + ☃xxxxxxxxxxx * 0.11F) * 0.2F;
         float ☃xxxxxxxxxxxxxxx = 1.0F - ☃xxxxxxxxxxxxx * 0.75F;
         ☃xxxxxxxxx = ☃xxxxxxxxx * ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxxx);
         ☃xxxxxxxxxx = ☃xxxxxxxxxx * ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxxx);
         ☃xxxxxxxxxxx = ☃xxxxxxxxxxx * ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxxx);
      }

      if (this.lastLightningBolt > 0) {
         float ☃xxxxxxxxxxxxxx = this.lastLightningBolt - ☃;
         if (☃xxxxxxxxxxxxxx > 1.0F) {
            ☃xxxxxxxxxxxxxx = 1.0F;
         }

         ☃xxxxxxxxxxxxxx *= 0.45F;
         ☃xxxxxxxxx = ☃xxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxx) + 0.8F * ☃xxxxxxxxxxxxxx;
         ☃xxxxxxxxxx = ☃xxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxx) + 0.8F * ☃xxxxxxxxxxxxxx;
         ☃xxxxxxxxxxx = ☃xxxxxxxxxxx * (1.0F - ☃xxxxxxxxxxxxxx) + 1.0F * ☃xxxxxxxxxxxxxx;
      }

      return new Vec3d(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx);
   }

   public float getCelestialAngle(float var1) {
      return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), ☃);
   }

   public int getMoonPhase() {
      return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
   }

   public float getCurrentMoonPhaseFactor() {
      return WorldProvider.MOON_PHASE_FACTORS[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
   }

   public float getCelestialAngleRadians(float var1) {
      float ☃ = this.getCelestialAngle(☃);
      return ☃ * (float) (Math.PI * 2);
   }

   public Vec3d getCloudColour(float var1) {
      float ☃ = this.getCelestialAngle(☃);
      float ☃x = MathHelper.cos(☃ * (float) (Math.PI * 2)) * 2.0F + 0.5F;
      ☃x = MathHelper.clamp(☃x, 0.0F, 1.0F);
      float ☃xx = 1.0F;
      float ☃xxx = 1.0F;
      float ☃xxxx = 1.0F;
      float ☃xxxxx = this.getRainStrength(☃);
      if (☃xxxxx > 0.0F) {
         float ☃xxxxxx = (☃xx * 0.3F + ☃xxx * 0.59F + ☃xxxx * 0.11F) * 0.6F;
         float ☃xxxxxxx = 1.0F - ☃xxxxx * 0.95F;
         ☃xx = ☃xx * ☃xxxxxxx + ☃xxxxxx * (1.0F - ☃xxxxxxx);
         ☃xxx = ☃xxx * ☃xxxxxxx + ☃xxxxxx * (1.0F - ☃xxxxxxx);
         ☃xxxx = ☃xxxx * ☃xxxxxxx + ☃xxxxxx * (1.0F - ☃xxxxxxx);
      }

      ☃xx *= ☃x * 0.9F + 0.1F;
      ☃xxx *= ☃x * 0.9F + 0.1F;
      ☃xxxx *= ☃x * 0.85F + 0.15F;
      float ☃xxxxxx = this.getThunderStrength(☃);
      if (☃xxxxxx > 0.0F) {
         float ☃xxxxxxx = (☃xx * 0.3F + ☃xxx * 0.59F + ☃xxxx * 0.11F) * 0.2F;
         float ☃xxxxxxxx = 1.0F - ☃xxxxxx * 0.95F;
         ☃xx = ☃xx * ☃xxxxxxxx + ☃xxxxxxx * (1.0F - ☃xxxxxxxx);
         ☃xxx = ☃xxx * ☃xxxxxxxx + ☃xxxxxxx * (1.0F - ☃xxxxxxxx);
         ☃xxxx = ☃xxxx * ☃xxxxxxxx + ☃xxxxxxx * (1.0F - ☃xxxxxxxx);
      }

      return new Vec3d(☃xx, ☃xxx, ☃xxxx);
   }

   public Vec3d getFogColor(float var1) {
      float ☃ = this.getCelestialAngle(☃);
      return this.provider.getFogColor(☃, ☃);
   }

   public BlockPos getPrecipitationHeight(BlockPos var1) {
      return this.getChunk(☃).getPrecipitationHeight(☃);
   }

   public BlockPos getTopSolidOrLiquidBlock(BlockPos var1) {
      Chunk ☃ = this.getChunk(☃);
      BlockPos ☃x = new BlockPos(☃.getX(), ☃.getTopFilledSegment() + 16, ☃.getZ());

      while (☃x.getY() >= 0) {
         BlockPos ☃xx = ☃x.down();
         Material ☃xxx = ☃.getBlockState(☃xx).getMaterial();
         if (☃xxx.blocksMovement() && ☃xxx != Material.LEAVES) {
            break;
         }

         ☃x = ☃xx;
      }

      return ☃x;
   }

   public float getStarBrightness(float var1) {
      float ☃ = this.getCelestialAngle(☃);
      float ☃x = 1.0F - (MathHelper.cos(☃ * (float) (Math.PI * 2)) * 2.0F + 0.25F);
      ☃x = MathHelper.clamp(☃x, 0.0F, 1.0F);
      return ☃x * ☃x * 0.5F;
   }

   public boolean isUpdateScheduled(BlockPos var1, Block var2) {
      return true;
   }

   public void scheduleUpdate(BlockPos var1, Block var2, int var3) {
   }

   public void updateBlockTick(BlockPos var1, Block var2, int var3, int var4) {
   }

   public void scheduleBlockUpdate(BlockPos var1, Block var2, int var3, int var4) {
   }

   public void updateEntities() {
      this.profiler.startSection("entities");
      this.profiler.startSection("global");

      for (int ☃ = 0; ☃ < this.weatherEffects.size(); ☃++) {
         Entity ☃x = this.weatherEffects.get(☃);

         try {
            ☃x.ticksExisted++;
            ☃x.onUpdate();
         } catch (Throwable var9) {
            CrashReport ☃xx = CrashReport.makeCrashReport(var9, "Ticking entity");
            CrashReportCategory ☃xxx = ☃xx.makeCategory("Entity being ticked");
            if (☃x == null) {
               ☃xxx.addCrashSection("Entity", "~~NULL~~");
            } else {
               ☃x.addEntityCrashInfo(☃xxx);
            }

            throw new ReportedException(☃xx);
         }

         if (☃x.isDead) {
            this.weatherEffects.remove(☃--);
         }
      }

      this.profiler.endStartSection("remove");
      this.loadedEntityList.removeAll(this.unloadedEntityList);

      for (int ☃ = 0; ☃ < this.unloadedEntityList.size(); ☃++) {
         Entity ☃x = this.unloadedEntityList.get(☃);
         int ☃xx = ☃x.chunkCoordX;
         int ☃xxx = ☃x.chunkCoordZ;
         if (☃x.addedToChunk && this.isChunkLoaded(☃xx, ☃xxx, true)) {
            this.getChunk(☃xx, ☃xxx).removeEntity(☃x);
         }
      }

      for (int ☃x = 0; ☃x < this.unloadedEntityList.size(); ☃x++) {
         this.onEntityRemoved(this.unloadedEntityList.get(☃x));
      }

      this.unloadedEntityList.clear();
      this.tickPlayers();
      this.profiler.endStartSection("regular");

      for (int ☃x = 0; ☃x < this.loadedEntityList.size(); ☃x++) {
         Entity ☃xx = this.loadedEntityList.get(☃x);
         Entity ☃xxx = ☃xx.getRidingEntity();
         if (☃xxx != null) {
            if (!☃xxx.isDead && ☃xxx.isPassenger(☃xx)) {
               continue;
            }

            ☃xx.dismountRidingEntity();
         }

         this.profiler.startSection("tick");
         if (!☃xx.isDead && !(☃xx instanceof EntityPlayerMP)) {
            try {
               this.updateEntity(☃xx);
            } catch (Throwable var8) {
               CrashReport ☃xxxx = CrashReport.makeCrashReport(var8, "Ticking entity");
               CrashReportCategory ☃xxxxx = ☃xxxx.makeCategory("Entity being ticked");
               ☃xx.addEntityCrashInfo(☃xxxxx);
               throw new ReportedException(☃xxxx);
            }
         }

         this.profiler.endSection();
         this.profiler.startSection("remove");
         if (☃xx.isDead) {
            int ☃xxxx = ☃xx.chunkCoordX;
            int ☃xxxxx = ☃xx.chunkCoordZ;
            if (☃xx.addedToChunk && this.isChunkLoaded(☃xxxx, ☃xxxxx, true)) {
               this.getChunk(☃xxxx, ☃xxxxx).removeEntity(☃xx);
            }

            this.loadedEntityList.remove(☃x--);
            this.onEntityRemoved(☃xx);
         }

         this.profiler.endSection();
      }

      this.profiler.endStartSection("blockEntities");
      if (!this.tileEntitiesToBeRemoved.isEmpty()) {
         this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
         this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
         this.tileEntitiesToBeRemoved.clear();
      }

      this.processingLoadedTiles = true;
      Iterator<TileEntity> ☃x = this.tickableTileEntities.iterator();

      while (☃x.hasNext()) {
         TileEntity ☃xxxx = ☃x.next();
         if (!☃xxxx.isInvalid() && ☃xxxx.hasWorld()) {
            BlockPos ☃xxxxx = ☃xxxx.getPos();
            if (this.isBlockLoaded(☃xxxxx) && this.worldBorder.contains(☃xxxxx)) {
               try {
                  this.profiler.func_194340_a(() -> String.valueOf(TileEntity.getKey((Class<? extends TileEntity>)☃.getClass())));
                  ((ITickable)☃xxxx).update();
                  this.profiler.endSection();
               } catch (Throwable var7) {
                  CrashReport ☃xxxxxx = CrashReport.makeCrashReport(var7, "Ticking block entity");
                  CrashReportCategory ☃xxxxxxx = ☃xxxxxx.makeCategory("Block entity being ticked");
                  ☃xxxx.addInfoToCrashReport(☃xxxxxxx);
                  throw new ReportedException(☃xxxxxx);
               }
            }
         }

         if (☃xxxx.isInvalid()) {
            ☃x.remove();
            this.loadedTileEntityList.remove(☃xxxx);
            if (this.isBlockLoaded(☃xxxx.getPos())) {
               this.getChunk(☃xxxx.getPos()).removeTileEntity(☃xxxx.getPos());
            }
         }
      }

      this.processingLoadedTiles = false;
      this.profiler.endStartSection("pendingBlockEntities");
      if (!this.addedTileEntityList.isEmpty()) {
         for (int ☃xxxxx = 0; ☃xxxxx < this.addedTileEntityList.size(); ☃xxxxx++) {
            TileEntity ☃xxxxxx = this.addedTileEntityList.get(☃xxxxx);
            if (!☃xxxxxx.isInvalid()) {
               if (!this.loadedTileEntityList.contains(☃xxxxxx)) {
                  this.addTileEntity(☃xxxxxx);
               }

               if (this.isBlockLoaded(☃xxxxxx.getPos())) {
                  Chunk ☃xxxxxxx = this.getChunk(☃xxxxxx.getPos());
                  IBlockState ☃xxxxxxxx = ☃xxxxxxx.getBlockState(☃xxxxxx.getPos());
                  ☃xxxxxxx.addTileEntity(☃xxxxxx.getPos(), ☃xxxxxx);
                  this.notifyBlockUpdate(☃xxxxxx.getPos(), ☃xxxxxxxx, ☃xxxxxxxx, 3);
               }
            }
         }

         this.addedTileEntityList.clear();
      }

      this.profiler.endSection();
      this.profiler.endSection();
   }

   protected void tickPlayers() {
   }

   public boolean addTileEntity(TileEntity var1) {
      boolean ☃ = this.loadedTileEntityList.add(☃);
      if (☃ && ☃ instanceof ITickable) {
         this.tickableTileEntities.add(☃);
      }

      if (this.isRemote) {
         BlockPos ☃x = ☃.getPos();
         IBlockState ☃xx = this.getBlockState(☃x);
         this.notifyBlockUpdate(☃x, ☃xx, ☃xx, 2);
      }

      return ☃;
   }

   public void addTileEntities(Collection<TileEntity> var1) {
      if (this.processingLoadedTiles) {
         this.addedTileEntityList.addAll(☃);
      } else {
         for (TileEntity ☃ : ☃) {
            this.addTileEntity(☃);
         }
      }
   }

   public void updateEntity(Entity var1) {
      this.updateEntityWithOptionalForce(☃, true);
   }

   public void updateEntityWithOptionalForce(Entity var1, boolean var2) {
      if (!(☃ instanceof EntityPlayer)) {
         int ☃ = MathHelper.floor(☃.posX);
         int ☃x = MathHelper.floor(☃.posZ);
         int ☃xx = 32;
         if (☃ && !this.isAreaLoaded(☃ - 32, 0, ☃x - 32, ☃ + 32, 0, ☃x + 32, true)) {
            return;
         }
      }

      ☃.lastTickPosX = ☃.posX;
      ☃.lastTickPosY = ☃.posY;
      ☃.lastTickPosZ = ☃.posZ;
      ☃.prevRotationYaw = ☃.rotationYaw;
      ☃.prevRotationPitch = ☃.rotationPitch;
      if (☃ && ☃.addedToChunk) {
         ☃.ticksExisted++;
         if (☃.isRiding()) {
            ☃.updateRidden();
         } else {
            ☃.onUpdate();
         }
      }

      this.profiler.startSection("chunkCheck");
      if (Double.isNaN(☃.posX) || Double.isInfinite(☃.posX)) {
         ☃.posX = ☃.lastTickPosX;
      }

      if (Double.isNaN(☃.posY) || Double.isInfinite(☃.posY)) {
         ☃.posY = ☃.lastTickPosY;
      }

      if (Double.isNaN(☃.posZ) || Double.isInfinite(☃.posZ)) {
         ☃.posZ = ☃.lastTickPosZ;
      }

      if (Double.isNaN(☃.rotationPitch) || Double.isInfinite(☃.rotationPitch)) {
         ☃.rotationPitch = ☃.prevRotationPitch;
      }

      if (Double.isNaN(☃.rotationYaw) || Double.isInfinite(☃.rotationYaw)) {
         ☃.rotationYaw = ☃.prevRotationYaw;
      }

      int ☃ = MathHelper.floor(☃.posX / 16.0);
      int ☃x = MathHelper.floor(☃.posY / 16.0);
      int ☃xx = MathHelper.floor(☃.posZ / 16.0);
      if (!☃.addedToChunk || ☃.chunkCoordX != ☃ || ☃.chunkCoordY != ☃x || ☃.chunkCoordZ != ☃xx) {
         if (☃.addedToChunk && this.isChunkLoaded(☃.chunkCoordX, ☃.chunkCoordZ, true)) {
            this.getChunk(☃.chunkCoordX, ☃.chunkCoordZ).removeEntityAtIndex(☃, ☃.chunkCoordY);
         }

         if (!☃.setPositionNonDirty() && !this.isChunkLoaded(☃, ☃xx, true)) {
            ☃.addedToChunk = false;
         } else {
            this.getChunk(☃, ☃xx).addEntity(☃);
         }
      }

      this.profiler.endSection();
      if (☃ && ☃.addedToChunk) {
         for (Entity ☃xxx : ☃.getPassengers()) {
            if (!☃xxx.isDead && ☃xxx.getRidingEntity() == ☃) {
               this.updateEntity(☃xxx);
            } else {
               ☃xxx.dismountRidingEntity();
            }
         }
      }
   }

   public boolean checkNoEntityCollision(AxisAlignedBB var1) {
      return this.checkNoEntityCollision(☃, null);
   }

   public boolean checkNoEntityCollision(AxisAlignedBB var1, @Nullable Entity var2) {
      List<Entity> ☃ = this.getEntitiesWithinAABBExcludingEntity(null, ☃);

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         Entity ☃xx = ☃.get(☃x);
         if (!☃xx.isDead && ☃xx.preventEntitySpawning && ☃xx != ☃ && (☃ == null || ☃xx.isRidingSameEntity(☃))) {
            return false;
         }
      }

      return true;
   }

   public boolean checkBlockCollision(AxisAlignedBB var1) {
      int ☃ = MathHelper.floor(☃.minX);
      int ☃x = MathHelper.ceil(☃.maxX);
      int ☃xx = MathHelper.floor(☃.minY);
      int ☃xxx = MathHelper.ceil(☃.maxY);
      int ☃xxxx = MathHelper.floor(☃.minZ);
      int ☃xxxxx = MathHelper.ceil(☃.maxZ);
      BlockPos.PooledMutableBlockPos ☃xxxxxx = BlockPos.PooledMutableBlockPos.retain();

      for (int ☃xxxxxxx = ☃; ☃xxxxxxx < ☃x; ☃xxxxxxx++) {
         for (int ☃xxxxxxxx = ☃xx; ☃xxxxxxxx < ☃xxx; ☃xxxxxxxx++) {
            for (int ☃xxxxxxxxx = ☃xxxx; ☃xxxxxxxxx < ☃xxxxx; ☃xxxxxxxxx++) {
               IBlockState ☃xxxxxxxxxx = this.getBlockState(☃xxxxxx.setPos(☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx));
               if (☃xxxxxxxxxx.getMaterial() != Material.AIR) {
                  ☃xxxxxx.release();
                  return true;
               }
            }
         }
      }

      ☃xxxxxx.release();
      return false;
   }

   public boolean containsAnyLiquid(AxisAlignedBB var1) {
      int ☃ = MathHelper.floor(☃.minX);
      int ☃x = MathHelper.ceil(☃.maxX);
      int ☃xx = MathHelper.floor(☃.minY);
      int ☃xxx = MathHelper.ceil(☃.maxY);
      int ☃xxxx = MathHelper.floor(☃.minZ);
      int ☃xxxxx = MathHelper.ceil(☃.maxZ);
      BlockPos.PooledMutableBlockPos ☃xxxxxx = BlockPos.PooledMutableBlockPos.retain();

      for (int ☃xxxxxxx = ☃; ☃xxxxxxx < ☃x; ☃xxxxxxx++) {
         for (int ☃xxxxxxxx = ☃xx; ☃xxxxxxxx < ☃xxx; ☃xxxxxxxx++) {
            for (int ☃xxxxxxxxx = ☃xxxx; ☃xxxxxxxxx < ☃xxxxx; ☃xxxxxxxxx++) {
               IBlockState ☃xxxxxxxxxx = this.getBlockState(☃xxxxxx.setPos(☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx));
               if (☃xxxxxxxxxx.getMaterial().isLiquid()) {
                  ☃xxxxxx.release();
                  return true;
               }
            }
         }
      }

      ☃xxxxxx.release();
      return false;
   }

   public boolean isFlammableWithin(AxisAlignedBB var1) {
      int ☃ = MathHelper.floor(☃.minX);
      int ☃x = MathHelper.ceil(☃.maxX);
      int ☃xx = MathHelper.floor(☃.minY);
      int ☃xxx = MathHelper.ceil(☃.maxY);
      int ☃xxxx = MathHelper.floor(☃.minZ);
      int ☃xxxxx = MathHelper.ceil(☃.maxZ);
      if (this.isAreaLoaded(☃, ☃xx, ☃xxxx, ☃x, ☃xxx, ☃xxxxx, true)) {
         BlockPos.PooledMutableBlockPos ☃xxxxxx = BlockPos.PooledMutableBlockPos.retain();

         for (int ☃xxxxxxx = ☃; ☃xxxxxxx < ☃x; ☃xxxxxxx++) {
            for (int ☃xxxxxxxx = ☃xx; ☃xxxxxxxx < ☃xxx; ☃xxxxxxxx++) {
               for (int ☃xxxxxxxxx = ☃xxxx; ☃xxxxxxxxx < ☃xxxxx; ☃xxxxxxxxx++) {
                  Block ☃xxxxxxxxxx = this.getBlockState(☃xxxxxx.setPos(☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx)).getBlock();
                  if (☃xxxxxxxxxx == Blocks.FIRE || ☃xxxxxxxxxx == Blocks.FLOWING_LAVA || ☃xxxxxxxxxx == Blocks.LAVA) {
                     ☃xxxxxx.release();
                     return true;
                  }
               }
            }
         }

         ☃xxxxxx.release();
      }

      return false;
   }

   public boolean handleMaterialAcceleration(AxisAlignedBB var1, Material var2, Entity var3) {
      int ☃ = MathHelper.floor(☃.minX);
      int ☃x = MathHelper.ceil(☃.maxX);
      int ☃xx = MathHelper.floor(☃.minY);
      int ☃xxx = MathHelper.ceil(☃.maxY);
      int ☃xxxx = MathHelper.floor(☃.minZ);
      int ☃xxxxx = MathHelper.ceil(☃.maxZ);
      if (!this.isAreaLoaded(☃, ☃xx, ☃xxxx, ☃x, ☃xxx, ☃xxxxx, true)) {
         return false;
      } else {
         boolean ☃xxxxxx = false;
         Vec3d ☃xxxxxxx = Vec3d.ZERO;
         BlockPos.PooledMutableBlockPos ☃xxxxxxxx = BlockPos.PooledMutableBlockPos.retain();

         for (int ☃xxxxxxxxx = ☃; ☃xxxxxxxxx < ☃x; ☃xxxxxxxxx++) {
            for (int ☃xxxxxxxxxx = ☃xx; ☃xxxxxxxxxx < ☃xxx; ☃xxxxxxxxxx++) {
               for (int ☃xxxxxxxxxxx = ☃xxxx; ☃xxxxxxxxxxx < ☃xxxxx; ☃xxxxxxxxxxx++) {
                  ☃xxxxxxxx.setPos(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx);
                  IBlockState ☃xxxxxxxxxxxx = this.getBlockState(☃xxxxxxxx);
                  Block ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getBlock();
                  if (☃xxxxxxxxxxxx.getMaterial() == ☃) {
                     double ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxx + 1 - BlockLiquid.getLiquidHeightPercent(☃xxxxxxxxxxxx.getValue(BlockLiquid.LEVEL));
                     if (☃xxx >= ☃xxxxxxxxxxxxxx) {
                        ☃xxxxxx = true;
                        ☃xxxxxxx = ☃xxxxxxxxxxxxx.modifyAcceleration(this, ☃xxxxxxxx, ☃, ☃xxxxxxx);
                     }
                  }
               }
            }
         }

         ☃xxxxxxxx.release();
         if (☃xxxxxxx.length() > 0.0 && ☃.isPushedByWater()) {
            ☃xxxxxxx = ☃xxxxxxx.normalize();
            double ☃xxxxxxxxx = 0.014;
            ☃.motionX = ☃.motionX + ☃xxxxxxx.x * 0.014;
            ☃.motionY = ☃.motionY + ☃xxxxxxx.y * 0.014;
            ☃.motionZ = ☃.motionZ + ☃xxxxxxx.z * 0.014;
         }

         return ☃xxxxxx;
      }
   }

   public boolean isMaterialInBB(AxisAlignedBB var1, Material var2) {
      int ☃ = MathHelper.floor(☃.minX);
      int ☃x = MathHelper.ceil(☃.maxX);
      int ☃xx = MathHelper.floor(☃.minY);
      int ☃xxx = MathHelper.ceil(☃.maxY);
      int ☃xxxx = MathHelper.floor(☃.minZ);
      int ☃xxxxx = MathHelper.ceil(☃.maxZ);
      BlockPos.PooledMutableBlockPos ☃xxxxxx = BlockPos.PooledMutableBlockPos.retain();

      for (int ☃xxxxxxx = ☃; ☃xxxxxxx < ☃x; ☃xxxxxxx++) {
         for (int ☃xxxxxxxx = ☃xx; ☃xxxxxxxx < ☃xxx; ☃xxxxxxxx++) {
            for (int ☃xxxxxxxxx = ☃xxxx; ☃xxxxxxxxx < ☃xxxxx; ☃xxxxxxxxx++) {
               if (this.getBlockState(☃xxxxxx.setPos(☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx)).getMaterial() == ☃) {
                  ☃xxxxxx.release();
                  return true;
               }
            }
         }
      }

      ☃xxxxxx.release();
      return false;
   }

   public Explosion createExplosion(@Nullable Entity var1, double var2, double var4, double var6, float var8, boolean var9) {
      return this.newExplosion(☃, ☃, ☃, ☃, ☃, false, ☃);
   }

   public Explosion newExplosion(@Nullable Entity var1, double var2, double var4, double var6, float var8, boolean var9, boolean var10) {
      Explosion ☃ = new Explosion(this, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      ☃.doExplosionA();
      ☃.doExplosionB(true);
      return ☃;
   }

   public float getBlockDensity(Vec3d var1, AxisAlignedBB var2) {
      double ☃ = 1.0 / ((☃.maxX - ☃.minX) * 2.0 + 1.0);
      double ☃x = 1.0 / ((☃.maxY - ☃.minY) * 2.0 + 1.0);
      double ☃xx = 1.0 / ((☃.maxZ - ☃.minZ) * 2.0 + 1.0);
      double ☃xxx = (1.0 - Math.floor(1.0 / ☃) * ☃) / 2.0;
      double ☃xxxx = (1.0 - Math.floor(1.0 / ☃xx) * ☃xx) / 2.0;
      if (!(☃ < 0.0) && !(☃x < 0.0) && !(☃xx < 0.0)) {
         int ☃xxxxx = 0;
         int ☃xxxxxx = 0;

         for (float ☃xxxxxxx = 0.0F; ☃xxxxxxx <= 1.0F; ☃xxxxxxx = (float)(☃xxxxxxx + ☃)) {
            for (float ☃xxxxxxxx = 0.0F; ☃xxxxxxxx <= 1.0F; ☃xxxxxxxx = (float)(☃xxxxxxxx + ☃x)) {
               for (float ☃xxxxxxxxx = 0.0F; ☃xxxxxxxxx <= 1.0F; ☃xxxxxxxxx = (float)(☃xxxxxxxxx + ☃xx)) {
                  double ☃xxxxxxxxxx = ☃.minX + (☃.maxX - ☃.minX) * ☃xxxxxxx;
                  double ☃xxxxxxxxxxx = ☃.minY + (☃.maxY - ☃.minY) * ☃xxxxxxxx;
                  double ☃xxxxxxxxxxxx = ☃.minZ + (☃.maxZ - ☃.minZ) * ☃xxxxxxxxx;
                  if (this.rayTraceBlocks(new Vec3d(☃xxxxxxxxxx + ☃xxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx + ☃xxxx), ☃) == null) {
                     ☃xxxxx++;
                  }

                  ☃xxxxxx++;
               }
            }
         }

         return (float)☃xxxxx / ☃xxxxxx;
      } else {
         return 0.0F;
      }
   }

   public boolean extinguishFire(@Nullable EntityPlayer var1, BlockPos var2, EnumFacing var3) {
      ☃ = ☃.offset(☃);
      if (this.getBlockState(☃).getBlock() == Blocks.FIRE) {
         this.playEvent(☃, 1009, ☃, 0);
         this.setBlockToAir(☃);
         return true;
      } else {
         return false;
      }
   }

   public String getDebugLoadedEntities() {
      return "All: " + this.loadedEntityList.size();
   }

   public String getProviderName() {
      return this.chunkProvider.makeString();
   }

   @Nullable
   @Override
   public TileEntity getTileEntity(BlockPos var1) {
      if (this.isOutsideBuildHeight(☃)) {
         return null;
      } else {
         TileEntity ☃ = null;
         if (this.processingLoadedTiles) {
            ☃ = this.getPendingTileEntityAt(☃);
         }

         if (☃ == null) {
            ☃ = this.getChunk(☃).getTileEntity(☃, Chunk.EnumCreateEntityType.IMMEDIATE);
         }

         if (☃ == null) {
            ☃ = this.getPendingTileEntityAt(☃);
         }

         return ☃;
      }
   }

   @Nullable
   private TileEntity getPendingTileEntityAt(BlockPos var1) {
      for (int ☃ = 0; ☃ < this.addedTileEntityList.size(); ☃++) {
         TileEntity ☃x = this.addedTileEntityList.get(☃);
         if (!☃x.isInvalid() && ☃x.getPos().equals(☃)) {
            return ☃x;
         }
      }

      return null;
   }

   public void setTileEntity(BlockPos var1, @Nullable TileEntity var2) {
      if (!this.isOutsideBuildHeight(☃)) {
         if (☃ != null && !☃.isInvalid()) {
            if (this.processingLoadedTiles) {
               ☃.setPos(☃);
               Iterator<TileEntity> ☃ = this.addedTileEntityList.iterator();

               while (☃.hasNext()) {
                  TileEntity ☃x = ☃.next();
                  if (☃x.getPos().equals(☃)) {
                     ☃x.invalidate();
                     ☃.remove();
                  }
               }

               this.addedTileEntityList.add(☃);
            } else {
               this.getChunk(☃).addTileEntity(☃, ☃);
               this.addTileEntity(☃);
            }
         }
      }
   }

   public void removeTileEntity(BlockPos var1) {
      TileEntity ☃ = this.getTileEntity(☃);
      if (☃ != null && this.processingLoadedTiles) {
         ☃.invalidate();
         this.addedTileEntityList.remove(☃);
      } else {
         if (☃ != null) {
            this.addedTileEntityList.remove(☃);
            this.loadedTileEntityList.remove(☃);
            this.tickableTileEntities.remove(☃);
         }

         this.getChunk(☃).removeTileEntity(☃);
      }
   }

   public void markTileEntityForRemoval(TileEntity var1) {
      this.tileEntitiesToBeRemoved.add(☃);
   }

   public boolean isBlockFullCube(BlockPos var1) {
      AxisAlignedBB ☃ = this.getBlockState(☃).getCollisionBoundingBox(this, ☃);
      return ☃ != Block.NULL_AABB && ☃.getAverageEdgeLength() >= 1.0;
   }

   public boolean isBlockNormalCube(BlockPos var1, boolean var2) {
      if (this.isOutsideBuildHeight(☃)) {
         return false;
      } else {
         Chunk ☃ = this.chunkProvider.getLoadedChunk(☃.getX() >> 4, ☃.getZ() >> 4);
         if (☃ != null && !☃.isEmpty()) {
            IBlockState ☃x = this.getBlockState(☃);
            return ☃x.getMaterial().isOpaque() && ☃x.isFullCube();
         } else {
            return ☃;
         }
      }
   }

   public void calculateInitialSkylight() {
      int ☃ = this.calculateSkylightSubtracted(1.0F);
      if (☃ != this.skylightSubtracted) {
         this.skylightSubtracted = ☃;
      }
   }

   public void setAllowedSpawnTypes(boolean var1, boolean var2) {
      this.spawnHostileMobs = ☃;
      this.spawnPeacefulMobs = ☃;
   }

   public void tick() {
      this.updateWeather();
   }

   protected void calculateInitialWeather() {
      if (this.worldInfo.isRaining()) {
         this.rainingStrength = 1.0F;
         if (this.worldInfo.isThundering()) {
            this.thunderingStrength = 1.0F;
         }
      }
   }

   protected void updateWeather() {
      if (this.provider.hasSkyLight()) {
         if (!this.isRemote) {
            boolean ☃ = this.getGameRules().getBoolean("doWeatherCycle");
            if (☃) {
               int ☃x = this.worldInfo.getCleanWeatherTime();
               if (☃x > 0) {
                  this.worldInfo.setCleanWeatherTime(--☃x);
                  this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
                  this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
               }

               int ☃xx = this.worldInfo.getThunderTime();
               if (☃xx <= 0) {
                  if (this.worldInfo.isThundering()) {
                     this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                  } else {
                     this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                  }
               } else {
                  this.worldInfo.setThunderTime(--☃xx);
                  if (☃xx <= 0) {
                     this.worldInfo.setThundering(!this.worldInfo.isThundering());
                  }
               }

               int ☃xxx = this.worldInfo.getRainTime();
               if (☃xxx <= 0) {
                  if (this.worldInfo.isRaining()) {
                     this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                  } else {
                     this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                  }
               } else {
                  this.worldInfo.setRainTime(--☃xxx);
                  if (☃xxx <= 0) {
                     this.worldInfo.setRaining(!this.worldInfo.isRaining());
                  }
               }
            }

            this.prevThunderingStrength = this.thunderingStrength;
            if (this.worldInfo.isThundering()) {
               this.thunderingStrength = (float)(this.thunderingStrength + 0.01);
            } else {
               this.thunderingStrength = (float)(this.thunderingStrength - 0.01);
            }

            this.thunderingStrength = MathHelper.clamp(this.thunderingStrength, 0.0F, 1.0F);
            this.prevRainingStrength = this.rainingStrength;
            if (this.worldInfo.isRaining()) {
               this.rainingStrength = (float)(this.rainingStrength + 0.01);
            } else {
               this.rainingStrength = (float)(this.rainingStrength - 0.01);
            }

            this.rainingStrength = MathHelper.clamp(this.rainingStrength, 0.0F, 1.0F);
         }
      }
   }

   protected void playMoodSoundAndCheckLight(int var1, int var2, Chunk var3) {
      ☃.enqueueRelightChecks();
   }

   protected void updateBlocks() {
   }

   public void immediateBlockTick(BlockPos var1, IBlockState var2, Random var3) {
      this.scheduledUpdatesAreImmediate = true;
      ☃.getBlock().updateTick(this, ☃, ☃, ☃);
      this.scheduledUpdatesAreImmediate = false;
   }

   public boolean canBlockFreezeWater(BlockPos var1) {
      return this.canBlockFreeze(☃, false);
   }

   public boolean canBlockFreezeNoWater(BlockPos var1) {
      return this.canBlockFreeze(☃, true);
   }

   public boolean canBlockFreeze(BlockPos var1, boolean var2) {
      Biome ☃ = this.getBiome(☃);
      float ☃x = ☃.getTemperature(☃);
      if (☃x >= 0.15F) {
         return false;
      } else {
         if (☃.getY() >= 0 && ☃.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, ☃) < 10) {
            IBlockState ☃xx = this.getBlockState(☃);
            Block ☃xxx = ☃xx.getBlock();
            if ((☃xxx == Blocks.WATER || ☃xxx == Blocks.FLOWING_WATER) && ☃xx.getValue(BlockLiquid.LEVEL) == 0) {
               if (!☃) {
                  return true;
               }

               boolean ☃xxxx = this.isWater(☃.west()) && this.isWater(☃.east()) && this.isWater(☃.north()) && this.isWater(☃.south());
               if (!☃xxxx) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean isWater(BlockPos var1) {
      return this.getBlockState(☃).getMaterial() == Material.WATER;
   }

   public boolean canSnowAt(BlockPos var1, boolean var2) {
      Biome ☃ = this.getBiome(☃);
      float ☃x = ☃.getTemperature(☃);
      if (☃x >= 0.15F) {
         return false;
      } else if (!☃) {
         return true;
      } else {
         if (☃.getY() >= 0 && ☃.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, ☃) < 10) {
            IBlockState ☃xx = this.getBlockState(☃);
            if (☃xx.getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlaceBlockAt(this, ☃)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean checkLight(BlockPos var1) {
      boolean ☃ = false;
      if (this.provider.hasSkyLight()) {
         ☃ |= this.checkLightFor(EnumSkyBlock.SKY, ☃);
      }

      return ☃ | this.checkLightFor(EnumSkyBlock.BLOCK, ☃);
   }

   private int getRawLight(BlockPos var1, EnumSkyBlock var2) {
      if (☃ == EnumSkyBlock.SKY && this.canSeeSky(☃)) {
         return 15;
      } else {
         IBlockState ☃ = this.getBlockState(☃);
         int ☃x = ☃ == EnumSkyBlock.SKY ? 0 : ☃.getLightValue();
         int ☃xx = ☃.getLightOpacity();
         if (☃xx >= 15 && ☃.getLightValue() > 0) {
            ☃xx = 1;
         }

         if (☃xx < 1) {
            ☃xx = 1;
         }

         if (☃xx >= 15) {
            return 0;
         } else if (☃x >= 14) {
            return ☃x;
         } else {
            BlockPos.PooledMutableBlockPos ☃xxx = BlockPos.PooledMutableBlockPos.retain();

            try {
               for (EnumFacing ☃xxxx : EnumFacing.values()) {
                  ☃xxx.setPos(☃).move(☃xxxx);
                  int ☃xxxxx = this.getLightFor(☃, ☃xxx) - ☃xx;
                  if (☃xxxxx > ☃x) {
                     ☃x = ☃xxxxx;
                  }

                  if (☃x >= 14) {
                     return ☃x;
                  }
               }

               return ☃x;
            } finally {
               ☃xxx.release();
            }
         }
      }
   }

   public boolean checkLightFor(EnumSkyBlock var1, BlockPos var2) {
      if (!this.isAreaLoaded(☃, 17, false)) {
         return false;
      } else {
         int ☃ = 0;
         int ☃x = 0;
         this.profiler.startSection("getBrightness");
         int ☃xx = this.getLightFor(☃, ☃);
         int ☃xxx = this.getRawLight(☃, ☃);
         int ☃xxxx = ☃.getX();
         int ☃xxxxx = ☃.getY();
         int ☃xxxxxx = ☃.getZ();
         if (☃xxx > ☃xx) {
            this.lightUpdateBlockList[☃x++] = 133152;
         } else if (☃xxx < ☃xx) {
            this.lightUpdateBlockList[☃x++] = 133152 | ☃xx << 18;

            while (☃ < ☃x) {
               int ☃xxxxxxx = this.lightUpdateBlockList[☃++];
               int ☃xxxxxxxx = (☃xxxxxxx & 63) - 32 + ☃xxxx;
               int ☃xxxxxxxxx = (☃xxxxxxx >> 6 & 63) - 32 + ☃xxxxx;
               int ☃xxxxxxxxxx = (☃xxxxxxx >> 12 & 63) - 32 + ☃xxxxxx;
               int ☃xxxxxxxxxxx = ☃xxxxxxx >> 18 & 15;
               BlockPos ☃xxxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx);
               int ☃xxxxxxxxxxxxx = this.getLightFor(☃, ☃xxxxxxxxxxxx);
               if (☃xxxxxxxxxxxxx == ☃xxxxxxxxxxx) {
                  this.setLightFor(☃, ☃xxxxxxxxxxxx, 0);
                  if (☃xxxxxxxxxxx > 0) {
                     int ☃xxxxxxxxxxxxxx = MathHelper.abs(☃xxxxxxxx - ☃xxxx);
                     int ☃xxxxxxxxxxxxxxx = MathHelper.abs(☃xxxxxxxxx - ☃xxxxx);
                     int ☃xxxxxxxxxxxxxxxx = MathHelper.abs(☃xxxxxxxxxx - ☃xxxxxx);
                     if (☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxx < 17) {
                        BlockPos.PooledMutableBlockPos ☃xxxxxxxxxxxxxxxxx = BlockPos.PooledMutableBlockPos.retain();

                        for (EnumFacing ☃xxxxxxxxxxxxxxxxxx : EnumFacing.values()) {
                           int ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxx + ☃xxxxxxxxxxxxxxxxxx.getXOffset();
                           int ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx.getYOffset();
                           int ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxx.getZOffset();
                           ☃xxxxxxxxxxxxxxxxx.setPos(☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxx);
                           int ☃xxxxxxxxxxxxxxxxxxxxxx = Math.max(1, this.getBlockState(☃xxxxxxxxxxxxxxxxx).getLightOpacity());
                           ☃xxxxxxxxxxxxx = this.getLightFor(☃, ☃xxxxxxxxxxxxxxxxx);
                           if (☃xxxxxxxxxxxxx == ☃xxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxx && ☃x < this.lightUpdateBlockList.length) {
                              this.lightUpdateBlockList[☃x++] = ☃xxxxxxxxxxxxxxxxxxx - ☃xxxx + 32
                                 | ☃xxxxxxxxxxxxxxxxxxxx - ☃xxxxx + 32 << 6
                                 | ☃xxxxxxxxxxxxxxxxxxxxx - ☃xxxxxx + 32 << 12
                                 | ☃xxxxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxx << 18;
                           }
                        }

                        ☃xxxxxxxxxxxxxxxxx.release();
                     }
                  }
               }
            }

            ☃ = 0;
         }

         this.profiler.endSection();
         this.profiler.startSection("checkedPosition < toCheckCount");

         while (☃ < ☃x) {
            int ☃xxxxxxx = this.lightUpdateBlockList[☃++];
            int ☃xxxxxxxx = (☃xxxxxxx & 63) - 32 + ☃xxxx;
            int ☃xxxxxxxxx = (☃xxxxxxx >> 6 & 63) - 32 + ☃xxxxx;
            int ☃xxxxxxxxxx = (☃xxxxxxx >> 12 & 63) - 32 + ☃xxxxxx;
            BlockPos ☃xxxxxxxxxxx = new BlockPos(☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx);
            int ☃xxxxxxxxxxxx = this.getLightFor(☃, ☃xxxxxxxxxxx);
            int ☃xxxxxxxxxxxxx = this.getRawLight(☃xxxxxxxxxxx, ☃);
            if (☃xxxxxxxxxxxxx != ☃xxxxxxxxxxxx) {
               this.setLightFor(☃, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxxx);
               if (☃xxxxxxxxxxxxx > ☃xxxxxxxxxxxx) {
                  int ☃xxxxxxxxxxxxxx = Math.abs(☃xxxxxxxx - ☃xxxx);
                  int ☃xxxxxxxxxxxxxxx = Math.abs(☃xxxxxxxxx - ☃xxxxx);
                  int ☃xxxxxxxxxxxxxxxx = Math.abs(☃xxxxxxxxxx - ☃xxxxxx);
                  boolean ☃xxxxxxxxxxxxxxxxx = ☃x < this.lightUpdateBlockList.length - 6;
                  if (☃xxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxx < 17 && ☃xxxxxxxxxxxxxxxxx) {
                     if (this.getLightFor(☃, ☃xxxxxxxxxxx.west()) < ☃xxxxxxxxxxxxx) {
                        this.lightUpdateBlockList[☃x++] = ☃xxxxxxxx - 1 - ☃xxxx + 32 + (☃xxxxxxxxx - ☃xxxxx + 32 << 6) + (☃xxxxxxxxxx - ☃xxxxxx + 32 << 12);
                     }

                     if (this.getLightFor(☃, ☃xxxxxxxxxxx.east()) < ☃xxxxxxxxxxxxx) {
                        this.lightUpdateBlockList[☃x++] = ☃xxxxxxxx + 1 - ☃xxxx + 32 + (☃xxxxxxxxx - ☃xxxxx + 32 << 6) + (☃xxxxxxxxxx - ☃xxxxxx + 32 << 12);
                     }

                     if (this.getLightFor(☃, ☃xxxxxxxxxxx.down()) < ☃xxxxxxxxxxxxx) {
                        this.lightUpdateBlockList[☃x++] = ☃xxxxxxxx - ☃xxxx + 32 + (☃xxxxxxxxx - 1 - ☃xxxxx + 32 << 6) + (☃xxxxxxxxxx - ☃xxxxxx + 32 << 12);
                     }

                     if (this.getLightFor(☃, ☃xxxxxxxxxxx.up()) < ☃xxxxxxxxxxxxx) {
                        this.lightUpdateBlockList[☃x++] = ☃xxxxxxxx - ☃xxxx + 32 + (☃xxxxxxxxx + 1 - ☃xxxxx + 32 << 6) + (☃xxxxxxxxxx - ☃xxxxxx + 32 << 12);
                     }

                     if (this.getLightFor(☃, ☃xxxxxxxxxxx.north()) < ☃xxxxxxxxxxxxx) {
                        this.lightUpdateBlockList[☃x++] = ☃xxxxxxxx - ☃xxxx + 32 + (☃xxxxxxxxx - ☃xxxxx + 32 << 6) + (☃xxxxxxxxxx - 1 - ☃xxxxxx + 32 << 12);
                     }

                     if (this.getLightFor(☃, ☃xxxxxxxxxxx.south()) < ☃xxxxxxxxxxxxx) {
                        this.lightUpdateBlockList[☃x++] = ☃xxxxxxxx - ☃xxxx + 32 + (☃xxxxxxxxx - ☃xxxxx + 32 << 6) + (☃xxxxxxxxxx + 1 - ☃xxxxxx + 32 << 12);
                     }
                  }
               }
            }
         }

         this.profiler.endSection();
         return true;
      }
   }

   public boolean tickUpdates(boolean var1) {
      return false;
   }

   @Nullable
   public List<NextTickListEntry> getPendingBlockUpdates(Chunk var1, boolean var2) {
      return null;
   }

   @Nullable
   public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox var1, boolean var2) {
      return null;
   }

   public List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable Entity var1, AxisAlignedBB var2) {
      return this.getEntitiesInAABBexcluding(☃, ☃, EntitySelectors.NOT_SPECTATING);
   }

   public List<Entity> getEntitiesInAABBexcluding(@Nullable Entity var1, AxisAlignedBB var2, @Nullable Predicate<? super Entity> var3) {
      List<Entity> ☃ = Lists.newArrayList();
      int ☃x = MathHelper.floor((☃.minX - 2.0) / 16.0);
      int ☃xx = MathHelper.floor((☃.maxX + 2.0) / 16.0);
      int ☃xxx = MathHelper.floor((☃.minZ - 2.0) / 16.0);
      int ☃xxxx = MathHelper.floor((☃.maxZ + 2.0) / 16.0);

      for (int ☃xxxxx = ☃x; ☃xxxxx <= ☃xx; ☃xxxxx++) {
         for (int ☃xxxxxx = ☃xxx; ☃xxxxxx <= ☃xxxx; ☃xxxxxx++) {
            if (this.isChunkLoaded(☃xxxxx, ☃xxxxxx, true)) {
               this.getChunk(☃xxxxx, ☃xxxxxx).getEntitiesWithinAABBForEntity(☃, ☃, ☃, ☃);
            }
         }
      }

      return ☃;
   }

   public <T extends Entity> List<T> getEntities(Class<? extends T> var1, Predicate<? super T> var2) {
      List<T> ☃ = Lists.newArrayList();

      for (Entity ☃x : this.loadedEntityList) {
         if (☃.isAssignableFrom(☃x.getClass()) && ☃.apply(☃x)) {
            ☃.add((T)☃x);
         }
      }

      return ☃;
   }

   public <T extends Entity> List<T> getPlayers(Class<? extends T> var1, Predicate<? super T> var2) {
      List<T> ☃ = Lists.newArrayList();

      for (Entity ☃x : this.playerEntities) {
         if (☃.isAssignableFrom(☃x.getClass()) && ☃.apply(☃x)) {
            ☃.add((T)☃x);
         }
      }

      return ☃;
   }

   public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> var1, AxisAlignedBB var2) {
      return this.getEntitiesWithinAABB(☃, ☃, EntitySelectors.NOT_SPECTATING);
   }

   public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> var1, AxisAlignedBB var2, @Nullable Predicate<? super T> var3) {
      int ☃ = MathHelper.floor((☃.minX - 2.0) / 16.0);
      int ☃x = MathHelper.ceil((☃.maxX + 2.0) / 16.0);
      int ☃xx = MathHelper.floor((☃.minZ - 2.0) / 16.0);
      int ☃xxx = MathHelper.ceil((☃.maxZ + 2.0) / 16.0);
      List<T> ☃xxxx = Lists.newArrayList();

      for (int ☃xxxxx = ☃; ☃xxxxx < ☃x; ☃xxxxx++) {
         for (int ☃xxxxxx = ☃xx; ☃xxxxxx < ☃xxx; ☃xxxxxx++) {
            if (this.isChunkLoaded(☃xxxxx, ☃xxxxxx, true)) {
               this.getChunk(☃xxxxx, ☃xxxxxx).getEntitiesOfTypeWithinAABB(☃, ☃, ☃xxxx, ☃);
            }
         }
      }

      return ☃xxxx;
   }

   @Nullable
   public <T extends Entity> T findNearestEntityWithinAABB(Class<? extends T> var1, AxisAlignedBB var2, T var3) {
      List<T> ☃ = this.getEntitiesWithinAABB(☃, ☃);
      T ☃x = null;
      double ☃xx = Double.MAX_VALUE;

      for (int ☃xxx = 0; ☃xxx < ☃.size(); ☃xxx++) {
         T ☃xxxx = (T)☃.get(☃xxx);
         if (☃xxxx != ☃ && EntitySelectors.NOT_SPECTATING.apply(☃xxxx)) {
            double ☃xxxxx = ☃.getDistanceSq(☃xxxx);
            if (!(☃xxxxx > ☃xx)) {
               ☃x = ☃xxxx;
               ☃xx = ☃xxxxx;
            }
         }
      }

      return ☃x;
   }

   @Nullable
   public Entity getEntityByID(int var1) {
      return this.entitiesById.lookup(☃);
   }

   public List<Entity> getLoadedEntityList() {
      return this.loadedEntityList;
   }

   public void markChunkDirty(BlockPos var1, TileEntity var2) {
      if (this.isBlockLoaded(☃)) {
         this.getChunk(☃).markDirty();
      }
   }

   public int countEntities(Class<?> var1) {
      int ☃ = 0;

      for (Entity ☃x : this.loadedEntityList) {
         if ((!(☃x instanceof EntityLiving) || !((EntityLiving)☃x).isNoDespawnRequired()) && ☃.isAssignableFrom(☃x.getClass())) {
            ☃++;
         }
      }

      return ☃;
   }

   public void loadEntities(Collection<Entity> var1) {
      this.loadedEntityList.addAll(☃);

      for (Entity ☃ : ☃) {
         this.onEntityAdded(☃);
      }
   }

   public void unloadEntities(Collection<Entity> var1) {
      this.unloadedEntityList.addAll(☃);
   }

   public boolean mayPlace(Block var1, BlockPos var2, boolean var3, EnumFacing var4, @Nullable Entity var5) {
      IBlockState ☃ = this.getBlockState(☃);
      AxisAlignedBB ☃x = ☃ ? null : ☃.getDefaultState().getCollisionBoundingBox(this, ☃);
      if (☃x != Block.NULL_AABB && !this.checkNoEntityCollision(☃x.offset(☃), ☃)) {
         return false;
      } else {
         return ☃.getMaterial() == Material.CIRCUITS && ☃ == Blocks.ANVIL ? true : ☃.getMaterial().isReplaceable() && ☃.canPlaceBlockOnSide(this, ☃, ☃);
      }
   }

   public int getSeaLevel() {
      return this.seaLevel;
   }

   public void setSeaLevel(int var1) {
      this.seaLevel = ☃;
   }

   @Override
   public int getStrongPower(BlockPos var1, EnumFacing var2) {
      return this.getBlockState(☃).getStrongPower(this, ☃, ☃);
   }

   @Override
   public WorldType getWorldType() {
      return this.worldInfo.getTerrainType();
   }

   public int getStrongPower(BlockPos var1) {
      int ☃ = 0;
      ☃ = Math.max(☃, this.getStrongPower(☃.down(), EnumFacing.DOWN));
      if (☃ >= 15) {
         return ☃;
      } else {
         ☃ = Math.max(☃, this.getStrongPower(☃.up(), EnumFacing.UP));
         if (☃ >= 15) {
            return ☃;
         } else {
            ☃ = Math.max(☃, this.getStrongPower(☃.north(), EnumFacing.NORTH));
            if (☃ >= 15) {
               return ☃;
            } else {
               ☃ = Math.max(☃, this.getStrongPower(☃.south(), EnumFacing.SOUTH));
               if (☃ >= 15) {
                  return ☃;
               } else {
                  ☃ = Math.max(☃, this.getStrongPower(☃.west(), EnumFacing.WEST));
                  if (☃ >= 15) {
                     return ☃;
                  } else {
                     ☃ = Math.max(☃, this.getStrongPower(☃.east(), EnumFacing.EAST));
                     return ☃ >= 15 ? ☃ : ☃;
                  }
               }
            }
         }
      }
   }

   public boolean isSidePowered(BlockPos var1, EnumFacing var2) {
      return this.getRedstonePower(☃, ☃) > 0;
   }

   public int getRedstonePower(BlockPos var1, EnumFacing var2) {
      IBlockState ☃ = this.getBlockState(☃);
      return ☃.isNormalCube() ? this.getStrongPower(☃) : ☃.getWeakPower(this, ☃, ☃);
   }

   public boolean isBlockPowered(BlockPos var1) {
      if (this.getRedstonePower(☃.down(), EnumFacing.DOWN) > 0) {
         return true;
      } else if (this.getRedstonePower(☃.up(), EnumFacing.UP) > 0) {
         return true;
      } else if (this.getRedstonePower(☃.north(), EnumFacing.NORTH) > 0) {
         return true;
      } else if (this.getRedstonePower(☃.south(), EnumFacing.SOUTH) > 0) {
         return true;
      } else {
         return this.getRedstonePower(☃.west(), EnumFacing.WEST) > 0 ? true : this.getRedstonePower(☃.east(), EnumFacing.EAST) > 0;
      }
   }

   public int getRedstonePowerFromNeighbors(BlockPos var1) {
      int ☃ = 0;

      for (EnumFacing ☃x : EnumFacing.values()) {
         int ☃xx = this.getRedstonePower(☃.offset(☃x), ☃x);
         if (☃xx >= 15) {
            return 15;
         }

         if (☃xx > ☃) {
            ☃ = ☃xx;
         }
      }

      return ☃;
   }

   @Nullable
   public EntityPlayer getClosestPlayerToEntity(Entity var1, double var2) {
      return this.getClosestPlayer(☃.posX, ☃.posY, ☃.posZ, ☃, false);
   }

   @Nullable
   public EntityPlayer getNearestPlayerNotCreative(Entity var1, double var2) {
      return this.getClosestPlayer(☃.posX, ☃.posY, ☃.posZ, ☃, true);
   }

   @Nullable
   public EntityPlayer getClosestPlayer(double var1, double var3, double var5, double var7, boolean var9) {
      Predicate<Entity> ☃ = ☃ ? EntitySelectors.CAN_AI_TARGET : EntitySelectors.NOT_SPECTATING;
      return this.getClosestPlayer(☃, ☃, ☃, ☃, ☃);
   }

   @Nullable
   public EntityPlayer getClosestPlayer(double var1, double var3, double var5, double var7, Predicate<Entity> var9) {
      double ☃ = -1.0;
      EntityPlayer ☃x = null;

      for (int ☃xx = 0; ☃xx < this.playerEntities.size(); ☃xx++) {
         EntityPlayer ☃xxx = this.playerEntities.get(☃xx);
         if (☃.apply(☃xxx)) {
            double ☃xxxx = ☃xxx.getDistanceSq(☃, ☃, ☃);
            if ((☃ < 0.0 || ☃xxxx < ☃ * ☃) && (☃ == -1.0 || ☃xxxx < ☃)) {
               ☃ = ☃xxxx;
               ☃x = ☃xxx;
            }
         }
      }

      return ☃x;
   }

   public boolean isAnyPlayerWithinRangeAt(double var1, double var3, double var5, double var7) {
      for (int ☃ = 0; ☃ < this.playerEntities.size(); ☃++) {
         EntityPlayer ☃x = this.playerEntities.get(☃);
         if (EntitySelectors.NOT_SPECTATING.apply(☃x)) {
            double ☃xx = ☃x.getDistanceSq(☃, ☃, ☃);
            if (☃ < 0.0 || ☃xx < ☃ * ☃) {
               return true;
            }
         }
      }

      return false;
   }

   @Nullable
   public EntityPlayer getNearestAttackablePlayer(Entity var1, double var2, double var4) {
      return this.getNearestAttackablePlayer(☃.posX, ☃.posY, ☃.posZ, ☃, ☃, null, null);
   }

   @Nullable
   public EntityPlayer getNearestAttackablePlayer(BlockPos var1, double var2, double var4) {
      return this.getNearestAttackablePlayer(☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F, ☃, ☃, null, null);
   }

   @Nullable
   public EntityPlayer getNearestAttackablePlayer(
      double var1, double var3, double var5, double var7, double var9, @Nullable Function<EntityPlayer, Double> var11, @Nullable Predicate<EntityPlayer> var12
   ) {
      double ☃ = -1.0;
      EntityPlayer ☃x = null;

      for (int ☃xx = 0; ☃xx < this.playerEntities.size(); ☃xx++) {
         EntityPlayer ☃xxx = this.playerEntities.get(☃xx);
         if (!☃xxx.capabilities.disableDamage && ☃xxx.isEntityAlive() && !☃xxx.isSpectator() && (☃ == null || ☃.apply(☃xxx))) {
            double ☃xxxx = ☃xxx.getDistanceSq(☃, ☃xxx.posY, ☃);
            double ☃xxxxx = ☃;
            if (☃xxx.isSneaking()) {
               ☃xxxxx = ☃ * 0.8F;
            }

            if (☃xxx.isInvisible()) {
               float ☃xxxxxx = ☃xxx.getArmorVisibility();
               if (☃xxxxxx < 0.1F) {
                  ☃xxxxxx = 0.1F;
               }

               ☃xxxxx *= 0.7F * ☃xxxxxx;
            }

            if (☃ != null) {
               ☃xxxxx *= MoreObjects.firstNonNull(☃.apply(☃xxx), 1.0);
            }

            if ((☃ < 0.0 || Math.abs(☃xxx.posY - ☃) < ☃ * ☃) && (☃ < 0.0 || ☃xxxx < ☃xxxxx * ☃xxxxx) && (☃ == -1.0 || ☃xxxx < ☃)) {
               ☃ = ☃xxxx;
               ☃x = ☃xxx;
            }
         }
      }

      return ☃x;
   }

   @Nullable
   public EntityPlayer getPlayerEntityByName(String var1) {
      for (int ☃ = 0; ☃ < this.playerEntities.size(); ☃++) {
         EntityPlayer ☃x = this.playerEntities.get(☃);
         if (☃.equals(☃x.getName())) {
            return ☃x;
         }
      }

      return null;
   }

   @Nullable
   public EntityPlayer getPlayerEntityByUUID(UUID var1) {
      for (int ☃ = 0; ☃ < this.playerEntities.size(); ☃++) {
         EntityPlayer ☃x = this.playerEntities.get(☃);
         if (☃.equals(☃x.getUniqueID())) {
            return ☃x;
         }
      }

      return null;
   }

   public void sendQuittingDisconnectingPacket() {
   }

   public void checkSessionLock() throws MinecraftException {
      this.saveHandler.checkSessionLock();
   }

   public void setTotalWorldTime(long var1) {
      this.worldInfo.setWorldTotalTime(☃);
   }

   public long getSeed() {
      return this.worldInfo.getSeed();
   }

   public long getTotalWorldTime() {
      return this.worldInfo.getWorldTotalTime();
   }

   public long getWorldTime() {
      return this.worldInfo.getWorldTime();
   }

   public void setWorldTime(long var1) {
      this.worldInfo.setWorldTime(☃);
   }

   public BlockPos getSpawnPoint() {
      BlockPos ☃ = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
      if (!this.getWorldBorder().contains(☃)) {
         ☃ = this.getHeight(new BlockPos(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
      }

      return ☃;
   }

   public void setSpawnPoint(BlockPos var1) {
      this.worldInfo.setSpawn(☃);
   }

   public void joinEntityInSurroundings(Entity var1) {
      int ☃ = MathHelper.floor(☃.posX / 16.0);
      int ☃x = MathHelper.floor(☃.posZ / 16.0);
      int ☃xx = 2;

      for (int ☃xxx = -2; ☃xxx <= 2; ☃xxx++) {
         for (int ☃xxxx = -2; ☃xxxx <= 2; ☃xxxx++) {
            this.getChunk(☃ + ☃xxx, ☃x + ☃xxxx);
         }
      }

      if (!this.loadedEntityList.contains(☃)) {
         this.loadedEntityList.add(☃);
      }
   }

   public boolean isBlockModifiable(EntityPlayer var1, BlockPos var2) {
      return true;
   }

   public void setEntityState(Entity var1, byte var2) {
   }

   public IChunkProvider getChunkProvider() {
      return this.chunkProvider;
   }

   public void addBlockEvent(BlockPos var1, Block var2, int var3, int var4) {
      this.getBlockState(☃).onBlockEventReceived(this, ☃, ☃, ☃);
   }

   public ISaveHandler getSaveHandler() {
      return this.saveHandler;
   }

   public WorldInfo getWorldInfo() {
      return this.worldInfo;
   }

   public GameRules getGameRules() {
      return this.worldInfo.getGameRulesInstance();
   }

   public void updateAllPlayersSleepingFlag() {
   }

   public float getThunderStrength(float var1) {
      return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * ☃) * this.getRainStrength(☃);
   }

   public void setThunderStrength(float var1) {
      this.prevThunderingStrength = ☃;
      this.thunderingStrength = ☃;
   }

   public float getRainStrength(float var1) {
      return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * ☃;
   }

   public void setRainStrength(float var1) {
      this.prevRainingStrength = ☃;
      this.rainingStrength = ☃;
   }

   public boolean isThundering() {
      return this.getThunderStrength(1.0F) > 0.9;
   }

   public boolean isRaining() {
      return this.getRainStrength(1.0F) > 0.2;
   }

   public boolean isRainingAt(BlockPos var1) {
      if (!this.isRaining()) {
         return false;
      } else if (!this.canSeeSky(☃)) {
         return false;
      } else if (this.getPrecipitationHeight(☃).getY() > ☃.getY()) {
         return false;
      } else {
         Biome ☃ = this.getBiome(☃);
         if (☃.getEnableSnow()) {
            return false;
         } else {
            return this.canSnowAt(☃, false) ? false : ☃.canRain();
         }
      }
   }

   public boolean isBlockinHighHumidity(BlockPos var1) {
      Biome ☃ = this.getBiome(☃);
      return ☃.isHighHumidity();
   }

   @Nullable
   public MapStorage getMapStorage() {
      return this.mapStorage;
   }

   public void setData(String var1, WorldSavedData var2) {
      this.mapStorage.setData(☃, ☃);
   }

   @Nullable
   public WorldSavedData loadData(Class<? extends WorldSavedData> var1, String var2) {
      return this.mapStorage.getOrLoadData(☃, ☃);
   }

   public int getUniqueDataId(String var1) {
      return this.mapStorage.getUniqueDataId(☃);
   }

   public void playBroadcastSound(int var1, BlockPos var2, int var3) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         this.eventListeners.get(☃).broadcastSound(☃, ☃, ☃);
      }
   }

   public void playEvent(int var1, BlockPos var2, int var3) {
      this.playEvent(null, ☃, ☃, ☃);
   }

   public void playEvent(@Nullable EntityPlayer var1, int var2, BlockPos var3, int var4) {
      try {
         for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
            this.eventListeners.get(☃).playEvent(☃, ☃, ☃, ☃);
         }
      } catch (Throwable var8) {
         CrashReport ☃ = CrashReport.makeCrashReport(var8, "Playing level event");
         CrashReportCategory ☃x = ☃.makeCategory("Level event being played");
         ☃x.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(☃));
         ☃x.addCrashSection("Event source", ☃);
         ☃x.addCrashSection("Event type", ☃);
         ☃x.addCrashSection("Event data", ☃);
         throw new ReportedException(☃);
      }
   }

   public int getHeight() {
      return 256;
   }

   public int getActualHeight() {
      return this.provider.isNether() ? 128 : 256;
   }

   public Random setRandomSeed(int var1, int var2, int var3) {
      long ☃ = ☃ * 341873128712L + ☃ * 132897987541L + this.getWorldInfo().getSeed() + ☃;
      this.rand.setSeed(☃);
      return this.rand;
   }

   public double getHorizon() {
      return this.worldInfo.getTerrainType() == WorldType.FLAT ? 0.0 : 63.0;
   }

   public CrashReportCategory addWorldInfoToCrashReport(CrashReport var1) {
      CrashReportCategory ☃ = ☃.makeCategoryDepth("Affected level", 1);
      ☃.addCrashSection("Level name", this.worldInfo == null ? "????" : this.worldInfo.getWorldName());
      ☃.addDetail("All players", new ICrashReportDetail<String>() {
         public String call() {
            return World.this.playerEntities.size() + " total; " + World.this.playerEntities;
         }
      });
      ☃.addDetail("Chunk stats", new ICrashReportDetail<String>() {
         public String call() {
            return World.this.chunkProvider.makeString();
         }
      });

      try {
         this.worldInfo.addToCrashReport(☃);
      } catch (Throwable var4) {
         ☃.addCrashSectionThrowable("Level Data Unobtainable", var4);
      }

      return ☃;
   }

   public void sendBlockBreakProgress(int var1, BlockPos var2, int var3) {
      for (int ☃ = 0; ☃ < this.eventListeners.size(); ☃++) {
         IWorldEventListener ☃x = this.eventListeners.get(☃);
         ☃x.sendBlockBreakProgress(☃, ☃, ☃);
      }
   }

   public Calendar getCurrentDate() {
      if (this.getTotalWorldTime() % 600L == 0L) {
         this.calendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
      }

      return this.calendar;
   }

   public void makeFireworks(double var1, double var3, double var5, double var7, double var9, double var11, @Nullable NBTTagCompound var13) {
   }

   public Scoreboard getScoreboard() {
      return this.worldScoreboard;
   }

   public void updateComparatorOutputLevel(BlockPos var1, Block var2) {
      for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
         BlockPos ☃x = ☃.offset(☃);
         if (this.isBlockLoaded(☃x)) {
            IBlockState ☃xx = this.getBlockState(☃x);
            if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(☃xx)) {
               ☃xx.neighborChanged(this, ☃x, ☃, ☃);
            } else if (☃xx.isNormalCube()) {
               ☃x = ☃x.offset(☃);
               ☃xx = this.getBlockState(☃x);
               if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(☃xx)) {
                  ☃xx.neighborChanged(this, ☃x, ☃, ☃);
               }
            }
         }
      }
   }

   public DifficultyInstance getDifficultyForLocation(BlockPos var1) {
      long ☃ = 0L;
      float ☃x = 0.0F;
      if (this.isBlockLoaded(☃)) {
         ☃x = this.getCurrentMoonPhaseFactor();
         ☃ = this.getChunk(☃).getInhabitedTime();
      }

      return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), ☃, ☃x);
   }

   public EnumDifficulty getDifficulty() {
      return this.getWorldInfo().getDifficulty();
   }

   public int getSkylightSubtracted() {
      return this.skylightSubtracted;
   }

   public void setSkylightSubtracted(int var1) {
      this.skylightSubtracted = ☃;
   }

   public int getLastLightningBolt() {
      return this.lastLightningBolt;
   }

   public void setLastLightningBolt(int var1) {
      this.lastLightningBolt = ☃;
   }

   public VillageCollection getVillageCollection() {
      return this.villageCollection;
   }

   public WorldBorder getWorldBorder() {
      return this.worldBorder;
   }

   public boolean isSpawnChunk(int var1, int var2) {
      BlockPos ☃ = this.getSpawnPoint();
      int ☃x = ☃ * 16 + 8 - ☃.getX();
      int ☃xx = ☃ * 16 + 8 - ☃.getZ();
      int ☃xxx = 128;
      return ☃x >= -128 && ☃x <= 128 && ☃xx >= -128 && ☃xx <= 128;
   }

   public void sendPacketToServer(Packet<?> var1) {
      throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
   }

   public LootTableManager getLootTableManager() {
      return this.lootTable;
   }

   @Nullable
   public BlockPos findNearestStructure(String var1, BlockPos var2, boolean var3) {
      return null;
   }
}
