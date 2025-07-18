package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.IChunkGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final ExtendedBlockStorage NULL_BLOCK_STORAGE = null;
   private final ExtendedBlockStorage[] storageArrays = new ExtendedBlockStorage[16];
   private final byte[] blockBiomeArray = new byte[256];
   private final int[] precipitationHeightMap = new int[256];
   private final boolean[] updateSkylightColumns = new boolean[256];
   private boolean loaded;
   private final World world;
   private final int[] heightMap;
   public final int x;
   public final int z;
   private boolean isGapLightingUpdated;
   private final Map<BlockPos, TileEntity> tileEntities = Maps.newHashMap();
   private final ClassInheritanceMultiMap<Entity>[] entityLists;
   private boolean isTerrainPopulated;
   private boolean isLightPopulated;
   private boolean ticked;
   private boolean dirty;
   private boolean hasEntities;
   private long lastSaveTime;
   private int heightMapMinimum;
   private long inhabitedTime;
   private int queuedLightChecks = 4096;
   private final ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue = Queues.newConcurrentLinkedQueue();
   public boolean unloadQueued;

   public Chunk(World var1, int var2, int var3) {
      this.entityLists = new ClassInheritanceMultiMap[16];
      this.world = ☃;
      this.x = ☃;
      this.z = ☃;
      this.heightMap = new int[256];

      for (int ☃ = 0; ☃ < this.entityLists.length; ☃++) {
         this.entityLists[☃] = new ClassInheritanceMultiMap<>(Entity.class);
      }

      Arrays.fill(this.precipitationHeightMap, -999);
      Arrays.fill(this.blockBiomeArray, (byte)-1);
   }

   public Chunk(World var1, ChunkPrimer var2, int var3, int var4) {
      this(☃, ☃, ☃);
      int ☃ = 256;
      boolean ☃x = ☃.provider.hasSkyLight();

      for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
         for (int ☃xxx = 0; ☃xxx < 16; ☃xxx++) {
            for (int ☃xxxx = 0; ☃xxxx < 256; ☃xxxx++) {
               IBlockState ☃xxxxx = ☃.getBlockState(☃xx, ☃xxxx, ☃xxx);
               if (☃xxxxx.getMaterial() != Material.AIR) {
                  int ☃xxxxxx = ☃xxxx >> 4;
                  if (this.storageArrays[☃xxxxxx] == NULL_BLOCK_STORAGE) {
                     this.storageArrays[☃xxxxxx] = new ExtendedBlockStorage(☃xxxxxx << 4, ☃x);
                  }

                  this.storageArrays[☃xxxxxx].set(☃xx, ☃xxxx & 15, ☃xxx, ☃xxxxx);
               }
            }
         }
      }
   }

   public boolean isAtLocation(int var1, int var2) {
      return ☃ == this.x && ☃ == this.z;
   }

   public int getHeight(BlockPos var1) {
      return this.getHeightValue(☃.getX() & 15, ☃.getZ() & 15);
   }

   public int getHeightValue(int var1, int var2) {
      return this.heightMap[☃ << 4 | ☃];
   }

   @Nullable
   private ExtendedBlockStorage getLastExtendedBlockStorage() {
      for (int ☃ = this.storageArrays.length - 1; ☃ >= 0; ☃--) {
         if (this.storageArrays[☃] != NULL_BLOCK_STORAGE) {
            return this.storageArrays[☃];
         }
      }

      return null;
   }

   public int getTopFilledSegment() {
      ExtendedBlockStorage ☃ = this.getLastExtendedBlockStorage();
      return ☃ == null ? 0 : ☃.getYLocation();
   }

   public ExtendedBlockStorage[] getBlockStorageArray() {
      return this.storageArrays;
   }

   protected void generateHeightMap() {
      int ☃ = this.getTopFilledSegment();
      this.heightMapMinimum = Integer.MAX_VALUE;

      for (int ☃x = 0; ☃x < 16; ☃x++) {
         for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
            this.precipitationHeightMap[☃x + (☃xx << 4)] = -999;

            for (int ☃xxx = ☃ + 16; ☃xxx > 0; ☃xxx--) {
               IBlockState ☃xxxx = this.getBlockState(☃x, ☃xxx - 1, ☃xx);
               if (☃xxxx.getLightOpacity() != 0) {
                  this.heightMap[☃xx << 4 | ☃x] = ☃xxx;
                  if (☃xxx < this.heightMapMinimum) {
                     this.heightMapMinimum = ☃xxx;
                  }
                  break;
               }
            }
         }
      }

      this.dirty = true;
   }

   public void generateSkylightMap() {
      int ☃ = this.getTopFilledSegment();
      this.heightMapMinimum = Integer.MAX_VALUE;

      for (int ☃x = 0; ☃x < 16; ☃x++) {
         for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
            this.precipitationHeightMap[☃x + (☃xx << 4)] = -999;

            for (int ☃xxx = ☃ + 16; ☃xxx > 0; ☃xxx--) {
               if (this.getBlockLightOpacity(☃x, ☃xxx - 1, ☃xx) != 0) {
                  this.heightMap[☃xx << 4 | ☃x] = ☃xxx;
                  if (☃xxx < this.heightMapMinimum) {
                     this.heightMapMinimum = ☃xxx;
                  }
                  break;
               }
            }

            if (this.world.provider.hasSkyLight()) {
               int ☃xxxx = 15;
               int ☃xxxxx = ☃ + 16 - 1;

               while (true) {
                  int ☃xxxxxx = this.getBlockLightOpacity(☃x, ☃xxxxx, ☃xx);
                  if (☃xxxxxx == 0 && ☃xxxx != 15) {
                     ☃xxxxxx = 1;
                  }

                  ☃xxxx -= ☃xxxxxx;
                  if (☃xxxx > 0) {
                     ExtendedBlockStorage ☃xxxxxxx = this.storageArrays[☃xxxxx >> 4];
                     if (☃xxxxxxx != NULL_BLOCK_STORAGE) {
                        ☃xxxxxxx.setSkyLight(☃x, ☃xxxxx & 15, ☃xx, ☃xxxx);
                        this.world.notifyLightSet(new BlockPos((this.x << 4) + ☃x, ☃xxxxx, (this.z << 4) + ☃xx));
                     }
                  }

                  if (--☃xxxxx <= 0 || ☃xxxx <= 0) {
                     break;
                  }
               }
            }
         }
      }

      this.dirty = true;
   }

   private void propagateSkylightOcclusion(int var1, int var2) {
      this.updateSkylightColumns[☃ + ☃ * 16] = true;
      this.isGapLightingUpdated = true;
   }

   private void recheckGaps(boolean var1) {
      this.world.profiler.startSection("recheckGaps");
      if (this.world.isAreaLoaded(new BlockPos(this.x * 16 + 8, 0, this.z * 16 + 8), 16)) {
         for (int ☃ = 0; ☃ < 16; ☃++) {
            for (int ☃x = 0; ☃x < 16; ☃x++) {
               if (this.updateSkylightColumns[☃ + ☃x * 16]) {
                  this.updateSkylightColumns[☃ + ☃x * 16] = false;
                  int ☃xx = this.getHeightValue(☃, ☃x);
                  int ☃xxx = this.x * 16 + ☃;
                  int ☃xxxx = this.z * 16 + ☃x;
                  int ☃xxxxx = Integer.MAX_VALUE;

                  for (EnumFacing ☃xxxxxx : EnumFacing.Plane.HORIZONTAL) {
                     ☃xxxxx = Math.min(☃xxxxx, this.world.getChunksLowestHorizon(☃xxx + ☃xxxxxx.getXOffset(), ☃xxxx + ☃xxxxxx.getZOffset()));
                  }

                  this.checkSkylightNeighborHeight(☃xxx, ☃xxxx, ☃xxxxx);

                  for (EnumFacing ☃xxxxxx : EnumFacing.Plane.HORIZONTAL) {
                     this.checkSkylightNeighborHeight(☃xxx + ☃xxxxxx.getXOffset(), ☃xxxx + ☃xxxxxx.getZOffset(), ☃xx);
                  }

                  if (☃) {
                     this.world.profiler.endSection();
                     return;
                  }
               }
            }
         }

         this.isGapLightingUpdated = false;
      }

      this.world.profiler.endSection();
   }

   private void checkSkylightNeighborHeight(int var1, int var2, int var3) {
      int ☃ = this.world.getHeight(new BlockPos(☃, 0, ☃)).getY();
      if (☃ > ☃) {
         this.updateSkylightNeighborHeight(☃, ☃, ☃, ☃ + 1);
      } else if (☃ < ☃) {
         this.updateSkylightNeighborHeight(☃, ☃, ☃, ☃ + 1);
      }
   }

   private void updateSkylightNeighborHeight(int var1, int var2, int var3, int var4) {
      if (☃ > ☃ && this.world.isAreaLoaded(new BlockPos(☃, 0, ☃), 16)) {
         for (int ☃ = ☃; ☃ < ☃; ☃++) {
            this.world.checkLightFor(EnumSkyBlock.SKY, new BlockPos(☃, ☃, ☃));
         }

         this.dirty = true;
      }
   }

   private void relightBlock(int var1, int var2, int var3) {
      int ☃ = this.heightMap[☃ << 4 | ☃] & 0xFF;
      int ☃x = ☃;
      if (☃ > ☃) {
         ☃x = ☃;
      }

      while (☃x > 0 && this.getBlockLightOpacity(☃, ☃x - 1, ☃) == 0) {
         ☃x--;
      }

      if (☃x != ☃) {
         this.world.markBlocksDirtyVertical(☃ + this.x * 16, ☃ + this.z * 16, ☃x, ☃);
         this.heightMap[☃ << 4 | ☃] = ☃x;
         int ☃xx = this.x * 16 + ☃;
         int ☃xxx = this.z * 16 + ☃;
         if (this.world.provider.hasSkyLight()) {
            if (☃x < ☃) {
               for (int ☃xxxx = ☃x; ☃xxxx < ☃; ☃xxxx++) {
                  ExtendedBlockStorage ☃xxxxx = this.storageArrays[☃xxxx >> 4];
                  if (☃xxxxx != NULL_BLOCK_STORAGE) {
                     ☃xxxxx.setSkyLight(☃, ☃xxxx & 15, ☃, 15);
                     this.world.notifyLightSet(new BlockPos((this.x << 4) + ☃, ☃xxxx, (this.z << 4) + ☃));
                  }
               }
            } else {
               for (int ☃xxxxx = ☃; ☃xxxxx < ☃x; ☃xxxxx++) {
                  ExtendedBlockStorage ☃xxxxxx = this.storageArrays[☃xxxxx >> 4];
                  if (☃xxxxxx != NULL_BLOCK_STORAGE) {
                     ☃xxxxxx.setSkyLight(☃, ☃xxxxx & 15, ☃, 0);
                     this.world.notifyLightSet(new BlockPos((this.x << 4) + ☃, ☃xxxxx, (this.z << 4) + ☃));
                  }
               }
            }

            int ☃xxxxxx = 15;

            while (☃x > 0 && ☃xxxxxx > 0) {
               int ☃xxxxxxx = this.getBlockLightOpacity(☃, --☃x, ☃);
               if (☃xxxxxxx == 0) {
                  ☃xxxxxxx = 1;
               }

               ☃xxxxxx -= ☃xxxxxxx;
               if (☃xxxxxx < 0) {
                  ☃xxxxxx = 0;
               }

               ExtendedBlockStorage ☃xxxxxxxx = this.storageArrays[☃x >> 4];
               if (☃xxxxxxxx != NULL_BLOCK_STORAGE) {
                  ☃xxxxxxxx.setSkyLight(☃, ☃x & 15, ☃, ☃xxxxxx);
               }
            }
         }

         int ☃xxxxxx = this.heightMap[☃ << 4 | ☃];
         int ☃xxxxxxxx = ☃;
         int ☃xxxxxxxxx = ☃xxxxxx;
         if (☃xxxxxx < ☃) {
            ☃xxxxxxxx = ☃xxxxxx;
            ☃xxxxxxxxx = ☃;
         }

         if (☃xxxxxx < this.heightMapMinimum) {
            this.heightMapMinimum = ☃xxxxxx;
         }

         if (this.world.provider.hasSkyLight()) {
            for (EnumFacing ☃xxxxxxxxxx : EnumFacing.Plane.HORIZONTAL) {
               this.updateSkylightNeighborHeight(☃xx + ☃xxxxxxxxxx.getXOffset(), ☃xxx + ☃xxxxxxxxxx.getZOffset(), ☃xxxxxxxx, ☃xxxxxxxxx);
            }

            this.updateSkylightNeighborHeight(☃xx, ☃xxx, ☃xxxxxxxx, ☃xxxxxxxxx);
         }

         this.dirty = true;
      }
   }

   public int getBlockLightOpacity(BlockPos var1) {
      return this.getBlockState(☃).getLightOpacity();
   }

   private int getBlockLightOpacity(int var1, int var2, int var3) {
      return this.getBlockState(☃, ☃, ☃).getLightOpacity();
   }

   public IBlockState getBlockState(BlockPos var1) {
      return this.getBlockState(☃.getX(), ☃.getY(), ☃.getZ());
   }

   public IBlockState getBlockState(final int var1, final int var2, final int var3) {
      if (this.world.getWorldType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         IBlockState ☃ = null;
         if (☃ == 60) {
            ☃ = Blocks.BARRIER.getDefaultState();
         }

         if (☃ == 70) {
            ☃ = ChunkGeneratorDebug.getBlockStateFor(☃, ☃);
         }

         return ☃ == null ? Blocks.AIR.getDefaultState() : ☃;
      } else {
         try {
            if (☃ >= 0 && ☃ >> 4 < this.storageArrays.length) {
               ExtendedBlockStorage ☃x = this.storageArrays[☃ >> 4];
               if (☃x != NULL_BLOCK_STORAGE) {
                  return ☃x.get(☃ & 15, ☃ & 15, ☃ & 15);
               }
            }

            return Blocks.AIR.getDefaultState();
         } catch (Throwable var7) {
            CrashReport ☃x = CrashReport.makeCrashReport(var7, "Getting block state");
            CrashReportCategory ☃xx = ☃x.makeCategory("Block being got");
            ☃xx.addDetail("Location", new ICrashReportDetail<String>() {
               public String call() throws Exception {
                  return CrashReportCategory.getCoordinateInfo(☃, ☃, ☃);
               }
            });
            throw new ReportedException(☃x);
         }
      }
   }

   @Nullable
   public IBlockState setBlockState(BlockPos var1, IBlockState var2) {
      int ☃ = ☃.getX() & 15;
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ() & 15;
      int ☃xxx = ☃xx << 4 | ☃;
      if (☃x >= this.precipitationHeightMap[☃xxx] - 1) {
         this.precipitationHeightMap[☃xxx] = -999;
      }

      int ☃xxxx = this.heightMap[☃xxx];
      IBlockState ☃xxxxx = this.getBlockState(☃);
      if (☃xxxxx == ☃) {
         return null;
      } else {
         Block ☃xxxxxx = ☃.getBlock();
         Block ☃xxxxxxx = ☃xxxxx.getBlock();
         ExtendedBlockStorage ☃xxxxxxxx = this.storageArrays[☃x >> 4];
         boolean ☃xxxxxxxxx = false;
         if (☃xxxxxxxx == NULL_BLOCK_STORAGE) {
            if (☃xxxxxx == Blocks.AIR) {
               return null;
            }

            ☃xxxxxxxx = new ExtendedBlockStorage(☃x >> 4 << 4, this.world.provider.hasSkyLight());
            this.storageArrays[☃x >> 4] = ☃xxxxxxxx;
            ☃xxxxxxxxx = ☃x >= ☃xxxx;
         }

         ☃xxxxxxxx.set(☃, ☃x & 15, ☃xx, ☃);
         if (☃xxxxxxx != ☃xxxxxx) {
            if (!this.world.isRemote) {
               ☃xxxxxxx.breakBlock(this.world, ☃, ☃xxxxx);
            } else if (☃xxxxxxx instanceof ITileEntityProvider) {
               this.world.removeTileEntity(☃);
            }
         }

         if (☃xxxxxxxx.get(☃, ☃x & 15, ☃xx).getBlock() != ☃xxxxxx) {
            return null;
         } else {
            if (☃xxxxxxxxx) {
               this.generateSkylightMap();
            } else {
               int ☃xxxxxxxxxx = ☃.getLightOpacity();
               int ☃xxxxxxxxxxx = ☃xxxxx.getLightOpacity();
               if (☃xxxxxxxxxx > 0) {
                  if (☃x >= ☃xxxx) {
                     this.relightBlock(☃, ☃x + 1, ☃xx);
                  }
               } else if (☃x == ☃xxxx - 1) {
                  this.relightBlock(☃, ☃x, ☃xx);
               }

               if (☃xxxxxxxxxx != ☃xxxxxxxxxxx
                  && (☃xxxxxxxxxx < ☃xxxxxxxxxxx || this.getLightFor(EnumSkyBlock.SKY, ☃) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, ☃) > 0)) {
                  this.propagateSkylightOcclusion(☃, ☃xx);
               }
            }

            if (☃xxxxxxx instanceof ITileEntityProvider) {
               TileEntity ☃xxxxxxxxxxxx = this.getTileEntity(☃, Chunk.EnumCreateEntityType.CHECK);
               if (☃xxxxxxxxxxxx != null) {
                  ☃xxxxxxxxxxxx.updateContainingBlockInfo();
               }
            }

            if (!this.world.isRemote && ☃xxxxxxx != ☃xxxxxx) {
               ☃xxxxxx.onBlockAdded(this.world, ☃, ☃);
            }

            if (☃xxxxxx instanceof ITileEntityProvider) {
               TileEntity ☃xxxxxxxxxxxx = this.getTileEntity(☃, Chunk.EnumCreateEntityType.CHECK);
               if (☃xxxxxxxxxxxx == null) {
                  ☃xxxxxxxxxxxx = ((ITileEntityProvider)☃xxxxxx).createNewTileEntity(this.world, ☃xxxxxx.getMetaFromState(☃));
                  this.world.setTileEntity(☃, ☃xxxxxxxxxxxx);
               }

               if (☃xxxxxxxxxxxx != null) {
                  ☃xxxxxxxxxxxx.updateContainingBlockInfo();
               }
            }

            this.dirty = true;
            return ☃xxxxx;
         }
      }
   }

   public int getLightFor(EnumSkyBlock var1, BlockPos var2) {
      int ☃ = ☃.getX() & 15;
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ() & 15;
      ExtendedBlockStorage ☃xxx = this.storageArrays[☃x >> 4];
      if (☃xxx == NULL_BLOCK_STORAGE) {
         return this.canSeeSky(☃) ? ☃.defaultLightValue : 0;
      } else if (☃ == EnumSkyBlock.SKY) {
         return !this.world.provider.hasSkyLight() ? 0 : ☃xxx.getSkyLight(☃, ☃x & 15, ☃xx);
      } else {
         return ☃ == EnumSkyBlock.BLOCK ? ☃xxx.getBlockLight(☃, ☃x & 15, ☃xx) : ☃.defaultLightValue;
      }
   }

   public void setLightFor(EnumSkyBlock var1, BlockPos var2, int var3) {
      int ☃ = ☃.getX() & 15;
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ() & 15;
      ExtendedBlockStorage ☃xxx = this.storageArrays[☃x >> 4];
      if (☃xxx == NULL_BLOCK_STORAGE) {
         ☃xxx = new ExtendedBlockStorage(☃x >> 4 << 4, this.world.provider.hasSkyLight());
         this.storageArrays[☃x >> 4] = ☃xxx;
         this.generateSkylightMap();
      }

      this.dirty = true;
      if (☃ == EnumSkyBlock.SKY) {
         if (this.world.provider.hasSkyLight()) {
            ☃xxx.setSkyLight(☃, ☃x & 15, ☃xx, ☃);
         }
      } else if (☃ == EnumSkyBlock.BLOCK) {
         ☃xxx.setBlockLight(☃, ☃x & 15, ☃xx, ☃);
      }
   }

   public int getLightSubtracted(BlockPos var1, int var2) {
      int ☃ = ☃.getX() & 15;
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ() & 15;
      ExtendedBlockStorage ☃xxx = this.storageArrays[☃x >> 4];
      if (☃xxx == NULL_BLOCK_STORAGE) {
         return this.world.provider.hasSkyLight() && ☃ < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - ☃ : 0;
      } else {
         int ☃xxxx = !this.world.provider.hasSkyLight() ? 0 : ☃xxx.getSkyLight(☃, ☃x & 15, ☃xx);
         ☃xxxx -= ☃;
         int ☃xxxxx = ☃xxx.getBlockLight(☃, ☃x & 15, ☃xx);
         if (☃xxxxx > ☃xxxx) {
            ☃xxxx = ☃xxxxx;
         }

         return ☃xxxx;
      }
   }

   public void addEntity(Entity var1) {
      this.hasEntities = true;
      int ☃ = MathHelper.floor(☃.posX / 16.0);
      int ☃x = MathHelper.floor(☃.posZ / 16.0);
      if (☃ != this.x || ☃x != this.z) {
         LOGGER.warn("Wrong location! ({}, {}) should be ({}, {}), {}", ☃, ☃x, this.x, this.z, ☃);
         ☃.setDead();
      }

      int ☃xx = MathHelper.floor(☃.posY / 16.0);
      if (☃xx < 0) {
         ☃xx = 0;
      }

      if (☃xx >= this.entityLists.length) {
         ☃xx = this.entityLists.length - 1;
      }

      ☃.addedToChunk = true;
      ☃.chunkCoordX = this.x;
      ☃.chunkCoordY = ☃xx;
      ☃.chunkCoordZ = this.z;
      this.entityLists[☃xx].add(☃);
   }

   public void removeEntity(Entity var1) {
      this.removeEntityAtIndex(☃, ☃.chunkCoordY);
   }

   public void removeEntityAtIndex(Entity var1, int var2) {
      if (☃ < 0) {
         ☃ = 0;
      }

      if (☃ >= this.entityLists.length) {
         ☃ = this.entityLists.length - 1;
      }

      this.entityLists[☃].remove(☃);
   }

   public boolean canSeeSky(BlockPos var1) {
      int ☃ = ☃.getX() & 15;
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ() & 15;
      return ☃x >= this.heightMap[☃xx << 4 | ☃];
   }

   @Nullable
   private TileEntity createNewTileEntity(BlockPos var1) {
      IBlockState ☃ = this.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      return !☃x.hasTileEntity() ? null : ((ITileEntityProvider)☃x).createNewTileEntity(this.world, ☃.getBlock().getMetaFromState(☃));
   }

   @Nullable
   public TileEntity getTileEntity(BlockPos var1, Chunk.EnumCreateEntityType var2) {
      TileEntity ☃ = this.tileEntities.get(☃);
      if (☃ == null) {
         if (☃ == Chunk.EnumCreateEntityType.IMMEDIATE) {
            ☃ = this.createNewTileEntity(☃);
            this.world.setTileEntity(☃, ☃);
         } else if (☃ == Chunk.EnumCreateEntityType.QUEUED) {
            this.tileEntityPosQueue.add(☃);
         }
      } else if (☃.isInvalid()) {
         this.tileEntities.remove(☃);
         return null;
      }

      return ☃;
   }

   public void addTileEntity(TileEntity var1) {
      this.addTileEntity(☃.getPos(), ☃);
      if (this.loaded) {
         this.world.addTileEntity(☃);
      }
   }

   public void addTileEntity(BlockPos var1, TileEntity var2) {
      ☃.setWorld(this.world);
      ☃.setPos(☃);
      if (this.getBlockState(☃).getBlock() instanceof ITileEntityProvider) {
         if (this.tileEntities.containsKey(☃)) {
            this.tileEntities.get(☃).invalidate();
         }

         ☃.validate();
         this.tileEntities.put(☃, ☃);
      }
   }

   public void removeTileEntity(BlockPos var1) {
      if (this.loaded) {
         TileEntity ☃ = this.tileEntities.remove(☃);
         if (☃ != null) {
            ☃.invalidate();
         }
      }
   }

   public void onLoad() {
      this.loaded = true;
      this.world.addTileEntities(this.tileEntities.values());

      for (ClassInheritanceMultiMap<Entity> ☃ : this.entityLists) {
         this.world.loadEntities(☃);
      }
   }

   public void onUnload() {
      this.loaded = false;

      for (TileEntity ☃ : this.tileEntities.values()) {
         this.world.markTileEntityForRemoval(☃);
      }

      for (ClassInheritanceMultiMap<Entity> ☃ : this.entityLists) {
         this.world.unloadEntities(☃);
      }
   }

   public void markDirty() {
      this.dirty = true;
   }

   public void getEntitiesWithinAABBForEntity(@Nullable Entity var1, AxisAlignedBB var2, List<Entity> var3, Predicate<? super Entity> var4) {
      int ☃ = MathHelper.floor((☃.minY - 2.0) / 16.0);
      int ☃x = MathHelper.floor((☃.maxY + 2.0) / 16.0);
      ☃ = MathHelper.clamp(☃, 0, this.entityLists.length - 1);
      ☃x = MathHelper.clamp(☃x, 0, this.entityLists.length - 1);

      for (int ☃xx = ☃; ☃xx <= ☃x; ☃xx++) {
         if (!this.entityLists[☃xx].isEmpty()) {
            for (Entity ☃xxx : this.entityLists[☃xx]) {
               if (☃xxx.getEntityBoundingBox().intersects(☃) && ☃xxx != ☃) {
                  if (☃ == null || ☃.apply(☃xxx)) {
                     ☃.add(☃xxx);
                  }

                  Entity[] ☃xxxx = ☃xxx.getParts();
                  if (☃xxxx != null) {
                     for (Entity ☃xxxxx : ☃xxxx) {
                        if (☃xxxxx != ☃ && ☃xxxxx.getEntityBoundingBox().intersects(☃) && (☃ == null || ☃.apply(☃xxxxx))) {
                           ☃.add(☃xxxxx);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public <T extends Entity> void getEntitiesOfTypeWithinAABB(Class<? extends T> var1, AxisAlignedBB var2, List<T> var3, Predicate<? super T> var4) {
      int ☃ = MathHelper.floor((☃.minY - 2.0) / 16.0);
      int ☃x = MathHelper.floor((☃.maxY + 2.0) / 16.0);
      ☃ = MathHelper.clamp(☃, 0, this.entityLists.length - 1);
      ☃x = MathHelper.clamp(☃x, 0, this.entityLists.length - 1);

      for (int ☃xx = ☃; ☃xx <= ☃x; ☃xx++) {
         for (T ☃xxx : this.entityLists[☃xx].getByClass(☃)) {
            if (☃xxx.getEntityBoundingBox().intersects(☃) && (☃ == null || ☃.apply(☃xxx))) {
               ☃.add(☃xxx);
            }
         }
      }
   }

   public boolean needsSaving(boolean var1) {
      if (☃) {
         if (this.hasEntities && this.world.getTotalWorldTime() != this.lastSaveTime || this.dirty) {
            return true;
         }
      } else if (this.hasEntities && this.world.getTotalWorldTime() >= this.lastSaveTime + 600L) {
         return true;
      }

      return this.dirty;
   }

   public Random getRandomWithSeed(long var1) {
      return new Random(this.world.getSeed() + this.x * this.x * 4987142 + this.x * 5947611 + this.z * this.z * 4392871L + this.z * 389711 ^ ☃);
   }

   public boolean isEmpty() {
      return false;
   }

   public void populate(IChunkProvider var1, IChunkGenerator var2) {
      Chunk ☃ = ☃.getLoadedChunk(this.x, this.z - 1);
      Chunk ☃x = ☃.getLoadedChunk(this.x + 1, this.z);
      Chunk ☃xx = ☃.getLoadedChunk(this.x, this.z + 1);
      Chunk ☃xxx = ☃.getLoadedChunk(this.x - 1, this.z);
      if (☃x != null && ☃xx != null && ☃.getLoadedChunk(this.x + 1, this.z + 1) != null) {
         this.populate(☃);
      }

      if (☃xxx != null && ☃xx != null && ☃.getLoadedChunk(this.x - 1, this.z + 1) != null) {
         ☃xxx.populate(☃);
      }

      if (☃ != null && ☃x != null && ☃.getLoadedChunk(this.x + 1, this.z - 1) != null) {
         ☃.populate(☃);
      }

      if (☃ != null && ☃xxx != null) {
         Chunk ☃xxxx = ☃.getLoadedChunk(this.x - 1, this.z - 1);
         if (☃xxxx != null) {
            ☃xxxx.populate(☃);
         }
      }
   }

   protected void populate(IChunkGenerator var1) {
      if (this.isTerrainPopulated()) {
         if (☃.generateStructures(this, this.x, this.z)) {
            this.markDirty();
         }
      } else {
         this.checkLight();
         ☃.populate(this.x, this.z);
         this.markDirty();
      }
   }

   public BlockPos getPrecipitationHeight(BlockPos var1) {
      int ☃ = ☃.getX() & 15;
      int ☃x = ☃.getZ() & 15;
      int ☃xx = ☃ | ☃x << 4;
      BlockPos ☃xxx = new BlockPos(☃.getX(), this.precipitationHeightMap[☃xx], ☃.getZ());
      if (☃xxx.getY() == -999) {
         int ☃xxxx = this.getTopFilledSegment() + 15;
         ☃xxx = new BlockPos(☃.getX(), ☃xxxx, ☃.getZ());
         int ☃xxxxx = -1;

         while (☃xxx.getY() > 0 && ☃xxxxx == -1) {
            IBlockState ☃xxxxxx = this.getBlockState(☃xxx);
            Material ☃xxxxxxx = ☃xxxxxx.getMaterial();
            if (!☃xxxxxxx.blocksMovement() && !☃xxxxxxx.isLiquid()) {
               ☃xxx = ☃xxx.down();
            } else {
               ☃xxxxx = ☃xxx.getY() + 1;
            }
         }

         this.precipitationHeightMap[☃xx] = ☃xxxxx;
      }

      return new BlockPos(☃.getX(), this.precipitationHeightMap[☃xx], ☃.getZ());
   }

   public void onTick(boolean var1) {
      if (this.isGapLightingUpdated && this.world.provider.hasSkyLight() && !☃) {
         this.recheckGaps(this.world.isRemote);
      }

      this.ticked = true;
      if (!this.isLightPopulated && this.isTerrainPopulated) {
         this.checkLight();
      }

      while (!this.tileEntityPosQueue.isEmpty()) {
         BlockPos ☃ = this.tileEntityPosQueue.poll();
         if (this.getTileEntity(☃, Chunk.EnumCreateEntityType.CHECK) == null && this.getBlockState(☃).getBlock().hasTileEntity()) {
            TileEntity ☃x = this.createNewTileEntity(☃);
            this.world.setTileEntity(☃, ☃x);
            this.world.markBlockRangeForRenderUpdate(☃, ☃);
         }
      }
   }

   public boolean isPopulated() {
      return this.ticked && this.isTerrainPopulated && this.isLightPopulated;
   }

   public boolean wasTicked() {
      return this.ticked;
   }

   public ChunkPos getPos() {
      return new ChunkPos(this.x, this.z);
   }

   public boolean isEmptyBetween(int var1, int var2) {
      if (☃ < 0) {
         ☃ = 0;
      }

      if (☃ >= 256) {
         ☃ = 255;
      }

      for (int ☃ = ☃; ☃ <= ☃; ☃ += 16) {
         ExtendedBlockStorage ☃x = this.storageArrays[☃ >> 4];
         if (☃x != NULL_BLOCK_STORAGE && !☃x.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public void setStorageArrays(ExtendedBlockStorage[] var1) {
      if (this.storageArrays.length != ☃.length) {
         LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", ☃.length, this.storageArrays.length);
      } else {
         System.arraycopy(☃, 0, this.storageArrays, 0, this.storageArrays.length);
      }
   }

   public void read(PacketBuffer var1, int var2, boolean var3) {
      boolean ☃ = this.world.provider.hasSkyLight();

      for (int ☃x = 0; ☃x < this.storageArrays.length; ☃x++) {
         ExtendedBlockStorage ☃xx = this.storageArrays[☃x];
         if ((☃ & 1 << ☃x) == 0) {
            if (☃ && ☃xx != NULL_BLOCK_STORAGE) {
               this.storageArrays[☃x] = NULL_BLOCK_STORAGE;
            }
         } else {
            if (☃xx == NULL_BLOCK_STORAGE) {
               ☃xx = new ExtendedBlockStorage(☃x << 4, ☃);
               this.storageArrays[☃x] = ☃xx;
            }

            ☃xx.getData().read(☃);
            ☃.readBytes(☃xx.getBlockLight().getData());
            if (☃) {
               ☃.readBytes(☃xx.getSkyLight().getData());
            }
         }
      }

      if (☃) {
         ☃.readBytes(this.blockBiomeArray);
      }

      for (int ☃xx = 0; ☃xx < this.storageArrays.length; ☃xx++) {
         if (this.storageArrays[☃xx] != NULL_BLOCK_STORAGE && (☃ & 1 << ☃xx) != 0) {
            this.storageArrays[☃xx].recalculateRefCounts();
         }
      }

      this.isLightPopulated = true;
      this.isTerrainPopulated = true;
      this.generateHeightMap();

      for (TileEntity ☃xxx : this.tileEntities.values()) {
         ☃xxx.updateContainingBlockInfo();
      }
   }

   public Biome getBiome(BlockPos var1, BiomeProvider var2) {
      int ☃ = ☃.getX() & 15;
      int ☃x = ☃.getZ() & 15;
      int ☃xx = this.blockBiomeArray[☃x << 4 | ☃] & 255;
      if (☃xx == 255) {
         Biome ☃xxx = ☃.getBiome(☃, Biomes.PLAINS);
         ☃xx = Biome.getIdForBiome(☃xxx);
         this.blockBiomeArray[☃x << 4 | ☃] = (byte)(☃xx & 0xFF);
      }

      Biome ☃xxx = Biome.getBiome(☃xx);
      return ☃xxx == null ? Biomes.PLAINS : ☃xxx;
   }

   public byte[] getBiomeArray() {
      return this.blockBiomeArray;
   }

   public void setBiomeArray(byte[] var1) {
      if (this.blockBiomeArray.length != ☃.length) {
         LOGGER.warn("Could not set level chunk biomes, array length is {} instead of {}", ☃.length, this.blockBiomeArray.length);
      } else {
         System.arraycopy(☃, 0, this.blockBiomeArray, 0, this.blockBiomeArray.length);
      }
   }

   public void resetRelightChecks() {
      this.queuedLightChecks = 0;
   }

   public void enqueueRelightChecks() {
      if (this.queuedLightChecks < 4096) {
         BlockPos ☃ = new BlockPos(this.x << 4, 0, this.z << 4);

         for (int ☃x = 0; ☃x < 8; ☃x++) {
            if (this.queuedLightChecks >= 4096) {
               return;
            }

            int ☃xx = this.queuedLightChecks % 16;
            int ☃xxx = this.queuedLightChecks / 16 % 16;
            int ☃xxxx = this.queuedLightChecks / 256;
            this.queuedLightChecks++;

            for (int ☃xxxxx = 0; ☃xxxxx < 16; ☃xxxxx++) {
               BlockPos ☃xxxxxx = ☃.add(☃xxx, (☃xx << 4) + ☃xxxxx, ☃xxxx);
               boolean ☃xxxxxxx = ☃xxxxx == 0 || ☃xxxxx == 15 || ☃xxx == 0 || ☃xxx == 15 || ☃xxxx == 0 || ☃xxxx == 15;
               if (this.storageArrays[☃xx] == NULL_BLOCK_STORAGE && ☃xxxxxxx
                  || this.storageArrays[☃xx] != NULL_BLOCK_STORAGE && this.storageArrays[☃xx].get(☃xxx, ☃xxxxx, ☃xxxx).getMaterial() == Material.AIR) {
                  for (EnumFacing ☃xxxxxxxx : EnumFacing.values()) {
                     BlockPos ☃xxxxxxxxx = ☃xxxxxx.offset(☃xxxxxxxx);
                     if (this.world.getBlockState(☃xxxxxxxxx).getLightValue() > 0) {
                        this.world.checkLight(☃xxxxxxxxx);
                     }
                  }

                  this.world.checkLight(☃xxxxxx);
               }
            }
         }
      }
   }

   public void checkLight() {
      this.isTerrainPopulated = true;
      this.isLightPopulated = true;
      BlockPos ☃ = new BlockPos(this.x << 4, 0, this.z << 4);
      if (this.world.provider.hasSkyLight()) {
         if (this.world.isAreaLoaded(☃.add(-1, 0, -1), ☃.add(16, this.world.getSeaLevel(), 16))) {
            label44:
            for (int ☃x = 0; ☃x < 16; ☃x++) {
               for (int ☃xx = 0; ☃xx < 16; ☃xx++) {
                  if (!this.checkLight(☃x, ☃xx)) {
                     this.isLightPopulated = false;
                     break label44;
                  }
               }
            }

            if (this.isLightPopulated) {
               for (EnumFacing ☃x : EnumFacing.Plane.HORIZONTAL) {
                  int ☃xxx = ☃x.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
                  this.world.getChunk(☃.offset(☃x, ☃xxx)).checkLightSide(☃x.getOpposite());
               }

               this.setSkylightUpdated();
            }
         } else {
            this.isLightPopulated = false;
         }
      }
   }

   private void setSkylightUpdated() {
      for (int ☃ = 0; ☃ < this.updateSkylightColumns.length; ☃++) {
         this.updateSkylightColumns[☃] = true;
      }

      this.recheckGaps(false);
   }

   private void checkLightSide(EnumFacing var1) {
      if (this.isTerrainPopulated) {
         if (☃ == EnumFacing.EAST) {
            for (int ☃ = 0; ☃ < 16; ☃++) {
               this.checkLight(15, ☃);
            }
         } else if (☃ == EnumFacing.WEST) {
            for (int ☃ = 0; ☃ < 16; ☃++) {
               this.checkLight(0, ☃);
            }
         } else if (☃ == EnumFacing.SOUTH) {
            for (int ☃ = 0; ☃ < 16; ☃++) {
               this.checkLight(☃, 15);
            }
         } else if (☃ == EnumFacing.NORTH) {
            for (int ☃ = 0; ☃ < 16; ☃++) {
               this.checkLight(☃, 0);
            }
         }
      }
   }

   private boolean checkLight(int var1, int var2) {
      int ☃ = this.getTopFilledSegment();
      boolean ☃x = false;
      boolean ☃xx = false;
      BlockPos.MutableBlockPos ☃xxx = new BlockPos.MutableBlockPos((this.x << 4) + ☃, 0, (this.z << 4) + ☃);

      for (int ☃xxxx = ☃ + 16 - 1; ☃xxxx > this.world.getSeaLevel() || ☃xxxx > 0 && !☃xx; ☃xxxx--) {
         ☃xxx.setPos(☃xxx.getX(), ☃xxxx, ☃xxx.getZ());
         int ☃xxxxx = this.getBlockLightOpacity(☃xxx);
         if (☃xxxxx == 255 && ☃xxx.getY() < this.world.getSeaLevel()) {
            ☃xx = true;
         }

         if (!☃x && ☃xxxxx > 0) {
            ☃x = true;
         } else if (☃x && ☃xxxxx == 0 && !this.world.checkLight(☃xxx)) {
            return false;
         }
      }

      for (int ☃xxxx = ☃xxx.getY(); ☃xxxx > 0; ☃xxxx--) {
         ☃xxx.setPos(☃xxx.getX(), ☃xxxx, ☃xxx.getZ());
         if (this.getBlockState(☃xxx).getLightValue() > 0) {
            this.world.checkLight(☃xxx);
         }
      }

      return true;
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public void markLoaded(boolean var1) {
      this.loaded = ☃;
   }

   public World getWorld() {
      return this.world;
   }

   public int[] getHeightMap() {
      return this.heightMap;
   }

   public void setHeightMap(int[] var1) {
      if (this.heightMap.length != ☃.length) {
         LOGGER.warn("Could not set level chunk heightmap, array length is {} instead of {}", ☃.length, this.heightMap.length);
      } else {
         System.arraycopy(☃, 0, this.heightMap, 0, this.heightMap.length);
      }
   }

   public Map<BlockPos, TileEntity> getTileEntityMap() {
      return this.tileEntities;
   }

   public ClassInheritanceMultiMap<Entity>[] getEntityLists() {
      return this.entityLists;
   }

   public boolean isTerrainPopulated() {
      return this.isTerrainPopulated;
   }

   public void setTerrainPopulated(boolean var1) {
      this.isTerrainPopulated = ☃;
   }

   public boolean isLightPopulated() {
      return this.isLightPopulated;
   }

   public void setLightPopulated(boolean var1) {
      this.isLightPopulated = ☃;
   }

   public void setModified(boolean var1) {
      this.dirty = ☃;
   }

   public void setHasEntities(boolean var1) {
      this.hasEntities = ☃;
   }

   public void setLastSaveTime(long var1) {
      this.lastSaveTime = ☃;
   }

   public int getLowestHeight() {
      return this.heightMapMinimum;
   }

   public long getInhabitedTime() {
      return this.inhabitedTime;
   }

   public void setInhabitedTime(long var1) {
      this.inhabitedTime = ☃;
   }

   public static enum EnumCreateEntityType {
      IMMEDIATE,
      QUEUED,
      CHECK;
   }
}
