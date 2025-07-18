package net.minecraft.client.multiplayer;

import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;

public class WorldClient extends World {
   private final NetHandlerPlayClient connection;
   private ChunkProviderClient clientChunkProvider;
   private final Set<Entity> entityList = Sets.newHashSet();
   private final Set<Entity> entitySpawnQueue = Sets.newHashSet();
   private final Minecraft mc = Minecraft.getMinecraft();
   private final Set<ChunkPos> previousActiveChunkSet = Sets.newHashSet();
   private int ambienceTicks = this.rand.nextInt(12000);
   protected Set<ChunkPos> visibleChunks = Sets.newHashSet();

   public WorldClient(NetHandlerPlayClient var1, WorldSettings var2, int var3, EnumDifficulty var4, Profiler var5) {
      super(new SaveHandlerMP(), new WorldInfo(☃, "MpServer"), DimensionType.getById(☃).createDimension(), ☃, true);
      this.connection = ☃;
      this.getWorldInfo().setDifficulty(☃);
      this.setSpawnPoint(new BlockPos(8, 64, 8));
      this.provider.setWorld(this);
      this.chunkProvider = this.createChunkProvider();
      this.mapStorage = new SaveDataMemoryStorage();
      this.calculateInitialSkylight();
      this.calculateInitialWeather();
   }

   @Override
   public void tick() {
      super.tick();
      this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
      if (this.getGameRules().getBoolean("doDaylightCycle")) {
         this.setWorldTime(this.getWorldTime() + 1L);
      }

      this.profiler.startSection("reEntryProcessing");

      for (int ☃ = 0; ☃ < 10 && !this.entitySpawnQueue.isEmpty(); ☃++) {
         Entity ☃x = this.entitySpawnQueue.iterator().next();
         this.entitySpawnQueue.remove(☃x);
         if (!this.loadedEntityList.contains(☃x)) {
            this.spawnEntity(☃x);
         }
      }

      this.profiler.endStartSection("chunkCache");
      this.clientChunkProvider.tick();
      this.profiler.endStartSection("blocks");
      this.updateBlocks();
      this.profiler.endSection();
   }

   public void invalidateBlockReceiveRegion(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   @Override
   protected IChunkProvider createChunkProvider() {
      this.clientChunkProvider = new ChunkProviderClient(this);
      return this.clientChunkProvider;
   }

   @Override
   protected boolean isChunkLoaded(int var1, int var2, boolean var3) {
      return ☃ || !this.getChunkProvider().provideChunk(☃, ☃).isEmpty();
   }

   protected void refreshVisibleChunks() {
      this.visibleChunks.clear();
      int ☃ = this.mc.gameSettings.renderDistanceChunks;
      this.profiler.startSection("buildList");
      int ☃x = MathHelper.floor(this.mc.player.posX / 16.0);
      int ☃xx = MathHelper.floor(this.mc.player.posZ / 16.0);

      for (int ☃xxx = -☃; ☃xxx <= ☃; ☃xxx++) {
         for (int ☃xxxx = -☃; ☃xxxx <= ☃; ☃xxxx++) {
            this.visibleChunks.add(new ChunkPos(☃xxx + ☃x, ☃xxxx + ☃xx));
         }
      }

      this.profiler.endSection();
   }

   @Override
   protected void updateBlocks() {
      this.refreshVisibleChunks();
      if (this.ambienceTicks > 0) {
         this.ambienceTicks--;
      }

      this.previousActiveChunkSet.retainAll(this.visibleChunks);
      if (this.previousActiveChunkSet.size() == this.visibleChunks.size()) {
         this.previousActiveChunkSet.clear();
      }

      int ☃ = 0;

      for (ChunkPos ☃x : this.visibleChunks) {
         if (!this.previousActiveChunkSet.contains(☃x)) {
            int ☃xx = ☃x.x * 16;
            int ☃xxx = ☃x.z * 16;
            this.profiler.startSection("getChunk");
            Chunk ☃xxxx = this.getChunk(☃x.x, ☃x.z);
            this.playMoodSoundAndCheckLight(☃xx, ☃xxx, ☃xxxx);
            this.profiler.endSection();
            this.previousActiveChunkSet.add(☃x);
            if (++☃ >= 10) {
               return;
            }
         }
      }
   }

   public void doPreChunk(int var1, int var2, boolean var3) {
      if (☃) {
         this.clientChunkProvider.loadChunk(☃, ☃);
      } else {
         this.clientChunkProvider.unloadChunk(☃, ☃);
         this.markBlockRangeForRenderUpdate(☃ * 16, 0, ☃ * 16, ☃ * 16 + 15, 256, ☃ * 16 + 15);
      }
   }

   @Override
   public boolean spawnEntity(Entity var1) {
      boolean ☃ = super.spawnEntity(☃);
      this.entityList.add(☃);
      if (☃) {
         if (☃ instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)☃));
         }
      } else {
         this.entitySpawnQueue.add(☃);
      }

      return ☃;
   }

   @Override
   public void removeEntity(Entity var1) {
      super.removeEntity(☃);
      this.entityList.remove(☃);
   }

   @Override
   protected void onEntityAdded(Entity var1) {
      super.onEntityAdded(☃);
      if (this.entitySpawnQueue.contains(☃)) {
         this.entitySpawnQueue.remove(☃);
      }
   }

   @Override
   protected void onEntityRemoved(Entity var1) {
      super.onEntityRemoved(☃);
      if (this.entityList.contains(☃)) {
         if (☃.isEntityAlive()) {
            this.entitySpawnQueue.add(☃);
         } else {
            this.entityList.remove(☃);
         }
      }
   }

   public void addEntityToWorld(int var1, Entity var2) {
      Entity ☃ = this.getEntityByID(☃);
      if (☃ != null) {
         this.removeEntity(☃);
      }

      this.entityList.add(☃);
      ☃.setEntityId(☃);
      if (!this.spawnEntity(☃)) {
         this.entitySpawnQueue.add(☃);
      }

      this.entitiesById.addKey(☃, ☃);
   }

   @Nullable
   @Override
   public Entity getEntityByID(int var1) {
      return (Entity)(☃ == this.mc.player.getEntityId() ? this.mc.player : super.getEntityByID(☃));
   }

   public Entity removeEntityFromWorld(int var1) {
      Entity ☃ = this.entitiesById.removeObject(☃);
      if (☃ != null) {
         this.entityList.remove(☃);
         this.removeEntity(☃);
      }

      return ☃;
   }

   @Deprecated
   public boolean invalidateRegionAndSetBlock(BlockPos var1, IBlockState var2) {
      int ☃ = ☃.getX();
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ();
      this.invalidateBlockReceiveRegion(☃, ☃x, ☃xx, ☃, ☃x, ☃xx);
      return super.setBlockState(☃, ☃, 3);
   }

   @Override
   public void sendQuittingDisconnectingPacket() {
      this.connection.getNetworkManager().closeChannel(new TextComponentString("Quitting"));
   }

   @Override
   protected void updateWeather() {
   }

   @Override
   protected void playMoodSoundAndCheckLight(int var1, int var2, Chunk var3) {
      super.playMoodSoundAndCheckLight(☃, ☃, ☃);
      if (this.ambienceTicks == 0) {
         this.updateLCG = this.updateLCG * 3 + 1013904223;
         int ☃ = this.updateLCG >> 2;
         int ☃x = ☃ & 15;
         int ☃xx = ☃ >> 8 & 15;
         int ☃xxx = ☃ >> 16 & 0xFF;
         BlockPos ☃xxxx = new BlockPos(☃x + ☃, ☃xxx, ☃xx + ☃);
         IBlockState ☃xxxxx = ☃.getBlockState(☃xxxx);
         ☃x += ☃;
         ☃xx += ☃;
         if (☃xxxxx.getMaterial() == Material.AIR && this.getLight(☃xxxx) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, ☃xxxx) <= 0) {
            double ☃xxxxxx = this.mc.player.getDistanceSq(☃x + 0.5, ☃xxx + 0.5, ☃xx + 0.5);
            if (this.mc.player != null && ☃xxxxxx > 4.0 && ☃xxxxxx < 256.0) {
               this.playSound(
                  ☃x + 0.5, ☃xxx + 0.5, ☃xx + 0.5, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.7F, 0.8F + this.rand.nextFloat() * 0.2F, false
               );
               this.ambienceTicks = this.rand.nextInt(12000) + 6000;
            }
         }
      }
   }

   public void doVoidFogParticles(int var1, int var2, int var3) {
      int ☃ = 32;
      Random ☃x = new Random();
      ItemStack ☃xx = this.mc.player.getHeldItemMainhand();
      boolean ☃xxx = this.mc.playerController.getCurrentGameType() == GameType.CREATIVE
         && !☃xx.isEmpty()
         && ☃xx.getItem() == Item.getItemFromBlock(Blocks.BARRIER);
      BlockPos.MutableBlockPos ☃xxxx = new BlockPos.MutableBlockPos();

      for (int ☃xxxxx = 0; ☃xxxxx < 667; ☃xxxxx++) {
         this.showBarrierParticles(☃, ☃, ☃, 16, ☃x, ☃xxx, ☃xxxx);
         this.showBarrierParticles(☃, ☃, ☃, 32, ☃x, ☃xxx, ☃xxxx);
      }
   }

   public void showBarrierParticles(int var1, int var2, int var3, int var4, Random var5, boolean var6, BlockPos.MutableBlockPos var7) {
      int ☃ = ☃ + this.rand.nextInt(☃) - this.rand.nextInt(☃);
      int ☃x = ☃ + this.rand.nextInt(☃) - this.rand.nextInt(☃);
      int ☃xx = ☃ + this.rand.nextInt(☃) - this.rand.nextInt(☃);
      ☃.setPos(☃, ☃x, ☃xx);
      IBlockState ☃xxx = this.getBlockState(☃);
      ☃xxx.getBlock().randomDisplayTick(☃xxx, this, ☃, ☃);
      if (☃ && ☃xxx.getBlock() == Blocks.BARRIER) {
         this.spawnParticle(EnumParticleTypes.BARRIER, ☃ + 0.5F, ☃x + 0.5F, ☃xx + 0.5F, 0.0, 0.0, 0.0, new int[0]);
      }
   }

   public void removeAllEntities() {
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

      for (int ☃x = 0; ☃x < this.loadedEntityList.size(); ☃x++) {
         Entity ☃xx = this.loadedEntityList.get(☃x);
         Entity ☃xxx = ☃xx.getRidingEntity();
         if (☃xxx != null) {
            if (!☃xxx.isDead && ☃xxx.isPassenger(☃xx)) {
               continue;
            }

            ☃xx.dismountRidingEntity();
         }

         if (☃xx.isDead) {
            int ☃xxxx = ☃xx.chunkCoordX;
            int ☃xxxxx = ☃xx.chunkCoordZ;
            if (☃xx.addedToChunk && this.isChunkLoaded(☃xxxx, ☃xxxxx, true)) {
               this.getChunk(☃xxxx, ☃xxxxx).removeEntity(☃xx);
            }

            this.loadedEntityList.remove(☃x--);
            this.onEntityRemoved(☃xx);
         }
      }
   }

   @Override
   public CrashReportCategory addWorldInfoToCrashReport(CrashReport var1) {
      CrashReportCategory ☃ = super.addWorldInfoToCrashReport(☃);
      ☃.addDetail("Forced entities", new ICrashReportDetail<String>() {
         public String call() {
            return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList;
         }
      });
      ☃.addDetail("Retry entities", new ICrashReportDetail<String>() {
         public String call() {
            return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue;
         }
      });
      ☃.addDetail("Server brand", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return WorldClient.this.mc.player.getServerBrand();
         }
      });
      ☃.addDetail("Server type", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return WorldClient.this.mc.getIntegratedServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
         }
      });
      return ☃;
   }

   @Override
   public void playSound(@Nullable EntityPlayer var1, double var2, double var4, double var6, SoundEvent var8, SoundCategory var9, float var10, float var11) {
      if (☃ == this.mc.player) {
         this.playSound(☃, ☃, ☃, ☃, ☃, ☃, ☃, false);
      }
   }

   public void playSound(BlockPos var1, SoundEvent var2, SoundCategory var3, float var4, float var5, boolean var6) {
      this.playSound(☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5, ☃, ☃, ☃, ☃, ☃);
   }

   @Override
   public void playSound(double var1, double var3, double var5, SoundEvent var7, SoundCategory var8, float var9, float var10, boolean var11) {
      double ☃ = this.mc.getRenderViewEntity().getDistanceSq(☃, ☃, ☃);
      PositionedSoundRecord ☃x = new PositionedSoundRecord(☃, ☃, ☃, ☃, (float)☃, (float)☃, (float)☃);
      if (☃ && ☃ > 100.0) {
         double ☃xx = Math.sqrt(☃) / 40.0;
         this.mc.getSoundHandler().playDelayedSound(☃x, (int)(☃xx * 20.0));
      } else {
         this.mc.getSoundHandler().playSound(☃x);
      }
   }

   @Override
   public void makeFireworks(double var1, double var3, double var5, double var7, double var9, double var11, @Nullable NBTTagCompound var13) {
      this.mc.effectRenderer.addEffect(new ParticleFirework.Starter(this, ☃, ☃, ☃, ☃, ☃, ☃, this.mc.effectRenderer, ☃));
   }

   @Override
   public void sendPacketToServer(Packet<?> var1) {
      this.connection.sendPacket(☃);
   }

   public void setWorldScoreboard(Scoreboard var1) {
      this.worldScoreboard = ☃;
   }

   @Override
   public void setWorldTime(long var1) {
      if (☃ < 0L) {
         ☃ = -☃;
         this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
      } else {
         this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
      }

      super.setWorldTime(☃);
   }

   public ChunkProviderClient getChunkProvider() {
      return (ChunkProviderClient)super.getChunkProvider();
   }
}
