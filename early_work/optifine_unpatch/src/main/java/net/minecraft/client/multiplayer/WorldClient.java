/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  java.lang.Deprecated
 *  java.lang.Math
 *  java.lang.Object
 *  java.util.Collection
 *  java.util.Random
 *  java.util.Set
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.MovingSoundMinecart
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.multiplayer.ChunkProviderClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleFirework$Starter
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityMinecart
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.Packet
 *  net.minecraft.profiler.Profiler
 *  net.minecraft.scoreboard.Scoreboard
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 *  net.minecraft.util.math.ChunkPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.world.EnumDifficulty
 *  net.minecraft.world.EnumSkyBlock
 *  net.minecraft.world.GameType
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldSettings
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraft.world.chunk.IChunkProvider
 *  net.minecraft.world.storage.ISaveHandler
 *  net.minecraft.world.storage.SaveDataMemoryStorage
 *  net.minecraft.world.storage.SaveHandlerMP
 *  net.minecraft.world.storage.WorldInfo
 *  net.minecraftforge.common.DimensionManager
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.multiplayer;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.Particle;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class WorldClient
extends World {
    private final NetHandlerPlayClient connection;
    private ChunkProviderClient clientChunkProvider;
    private final Set<Entity> entityList = Sets.newHashSet();
    private final Set<Entity> entitySpawnQueue = Sets.newHashSet();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set<ChunkPos> previousActiveChunkSet = Sets.newHashSet();
    private int ambienceTicks = this.rand.nextInt(12000);
    protected Set<ChunkPos> visibleChunks = Sets.newHashSet();

    public WorldClient(NetHandlerPlayClient netHandler, WorldSettings settings, int dimension, EnumDifficulty difficulty, Profiler profilerIn) {
        super((ISaveHandler)new SaveHandlerMP(), new WorldInfo(settings, "MpServer"), DimensionManager.createProviderFor((int)dimension), profilerIn, true);
        this.connection = netHandler;
        this.getWorldInfo().setDifficulty(difficulty);
        this.provider.setWorld((World)this);
        this.setSpawnPoint(new BlockPos(8, 64, 8));
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        this.initCapabilities();
        MinecraftForge.EVENT_BUS.post((Event)new WorldEvent.Load((World)this));
    }

    public void tick() {
        super.tick();
        this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.setWorldTime(this.getWorldTime() + 1L);
        }
        this.profiler.startSection("reEntryProcessing");
        for (int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); ++i) {
            Entity entity = (Entity)this.entitySpawnQueue.iterator().next();
            this.entitySpawnQueue.remove((Object)entity);
            if (this.loadedEntityList.contains((Object)entity)) continue;
            this.spawnEntity(entity);
        }
        this.profiler.endStartSection("chunkCache");
        this.clientChunkProvider.tick();
        this.profiler.endStartSection("blocks");
        this.updateBlocks();
        this.profiler.endSection();
    }

    public void invalidateBlockReceiveRegion(int x1, int y1, int z1, int x2, int y2, int z2) {
    }

    protected IChunkProvider createChunkProvider() {
        this.clientChunkProvider = new ChunkProviderClient((World)this);
        return this.clientChunkProvider;
    }

    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
        return allowEmpty || !this.getChunkProvider().provideChunk(x, z).isEmpty();
    }

    protected void refreshVisibleChunks() {
        this.visibleChunks.clear();
        int i = this.mc.gameSettings.renderDistanceChunks;
        this.profiler.startSection("buildList");
        int j = MathHelper.floor((double)(this.mc.player.p / 16.0));
        int k = MathHelper.floor((double)(this.mc.player.r / 16.0));
        for (int l = -i; l <= i; ++l) {
            for (int i1 = -i; i1 <= i; ++i1) {
                this.visibleChunks.add((Object)new ChunkPos(l + j, i1 + k));
            }
        }
        this.profiler.endSection();
    }

    protected void updateBlocks() {
        this.refreshVisibleChunks();
        if (this.ambienceTicks > 0) {
            --this.ambienceTicks;
        }
        this.previousActiveChunkSet.retainAll(this.visibleChunks);
        if (this.previousActiveChunkSet.size() == this.visibleChunks.size()) {
            this.previousActiveChunkSet.clear();
        }
        int i = 0;
        for (ChunkPos chunkpos : this.visibleChunks) {
            if (this.previousActiveChunkSet.contains((Object)chunkpos)) continue;
            int j = chunkpos.x * 16;
            int k = chunkpos.z * 16;
            this.profiler.startSection("getChunk");
            Chunk chunk = this.getChunk(chunkpos.x, chunkpos.z);
            this.playMoodSoundAndCheckLight(j, k, chunk);
            this.profiler.endSection();
            this.previousActiveChunkSet.add((Object)chunkpos);
            if (++i < 10) continue;
            return;
        }
    }

    public void doPreChunk(int chunkX, int chunkZ, boolean loadChunk) {
        if (loadChunk) {
            this.clientChunkProvider.loadChunk(chunkX, chunkZ);
        } else {
            this.clientChunkProvider.unloadChunk(chunkX, chunkZ);
            this.markBlockRangeForRenderUpdate(chunkX * 16, 0, chunkZ * 16, chunkX * 16 + 15, 256, chunkZ * 16 + 15);
        }
    }

    public boolean spawnEntity(Entity entityIn) {
        boolean flag = super.spawnEntity(entityIn);
        this.entityList.add((Object)entityIn);
        if (flag) {
            if (entityIn instanceof EntityMinecart) {
                this.mc.getSoundHandler().playSound((ISound)new MovingSoundMinecart((EntityMinecart)entityIn));
            }
        } else {
            this.entitySpawnQueue.add((Object)entityIn);
        }
        return flag;
    }

    public void removeEntity(Entity entityIn) {
        super.removeEntity(entityIn);
        this.entityList.remove((Object)entityIn);
    }

    public void onEntityAdded(Entity entityIn) {
        super.onEntityAdded(entityIn);
        if (this.entitySpawnQueue.contains((Object)entityIn)) {
            this.entitySpawnQueue.remove((Object)entityIn);
        }
    }

    public void onEntityRemoved(Entity entityIn) {
        super.onEntityRemoved(entityIn);
        if (this.entityList.contains((Object)entityIn)) {
            if (entityIn.isEntityAlive()) {
                this.entitySpawnQueue.add((Object)entityIn);
            } else {
                this.entityList.remove((Object)entityIn);
            }
        }
    }

    public void addEntityToWorld(int entityID, Entity entityToSpawn) {
        Entity entity = this.getEntityByID(entityID);
        if (entity != null) {
            this.removeEntity(entity);
        }
        this.entityList.add((Object)entityToSpawn);
        entityToSpawn.setEntityId(entityID);
        if (!this.spawnEntity(entityToSpawn)) {
            this.entitySpawnQueue.add((Object)entityToSpawn);
        }
        this.entitiesById.addKey(entityID, (Object)entityToSpawn);
    }

    @Nullable
    public Entity getEntityByID(int id) {
        return id == this.mc.player.S() ? this.mc.player : super.getEntityByID(id);
    }

    public Entity removeEntityFromWorld(int entityID) {
        Entity entity = (Entity)this.entitiesById.removeObject(entityID);
        if (entity != null) {
            this.entityList.remove((Object)entity);
            this.removeEntity(entity);
        }
        return entity;
    }

    @Deprecated
    public boolean invalidateRegionAndSetBlock(BlockPos pos, IBlockState state) {
        int i = pos.p();
        int j = pos.q();
        int k = pos.r();
        this.invalidateBlockReceiveRegion(i, j, k, i, j, k);
        return super.setBlockState(pos, state, 3);
    }

    public void sendQuittingDisconnectingPacket() {
        this.connection.getNetworkManager().closeChannel((ITextComponent)new TextComponentString("Quitting"));
    }

    protected void updateWeather() {
    }

    protected void playMoodSoundAndCheckLight(int x, int z, Chunk chunkIn) {
        super.playMoodSoundAndCheckLight(x, z, chunkIn);
        if (this.ambienceTicks == 0) {
            this.updateLCG = this.updateLCG * 3 + 1013904223;
            int i = this.updateLCG >> 2;
            int j = i & 0xF;
            int k = i >> 8 & 0xF;
            int l = i >> 16 & 0xFF;
            BlockPos blockpos = new BlockPos(j + x, l, k + z);
            IBlockState iblockstate = chunkIn.getBlockState(blockpos);
            j += x;
            k += z;
            if (iblockstate.a() == Material.AIR && this.getLight(blockpos) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, blockpos) <= 0) {
                double d0 = this.mc.player.d((double)j + 0.5, (double)l + 0.5, (double)k + 0.5);
                if (this.mc.player != null && d0 > 4.0 && d0 < 256.0) {
                    this.playSound((double)j + 0.5, (double)l + 0.5, (double)k + 0.5, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.7f, 0.8f + this.rand.nextFloat() * 0.2f, false);
                    this.ambienceTicks = this.rand.nextInt(12000) + 6000;
                }
            }
        }
    }

    public void doVoidFogParticles(int posX, int posY, int posZ) {
        int i = 32;
        Random random = new Random();
        ItemStack itemstack = this.mc.player.co();
        boolean flag = this.mc.playerController.getCurrentGameType() == GameType.CREATIVE && !itemstack.isEmpty() && itemstack.getItem() == Item.getItemFromBlock((Block)Blocks.BARRIER);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j = 0; j < 667; ++j) {
            this.showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
            this.showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
        }
    }

    public void showBarrierParticles(int x, int y, int z, int offset, Random random, boolean holdingBarrier, BlockPos.MutableBlockPos pos) {
        int i = x + this.rand.nextInt(offset) - this.rand.nextInt(offset);
        int j = y + this.rand.nextInt(offset) - this.rand.nextInt(offset);
        int k = z + this.rand.nextInt(offset) - this.rand.nextInt(offset);
        pos.setPos(i, j, k);
        IBlockState iblockstate = this.getBlockState((BlockPos)pos);
        iblockstate.getBlock().randomDisplayTick(iblockstate, (World)this, (BlockPos)pos, random);
        if (holdingBarrier && iblockstate.getBlock() == Blocks.BARRIER) {
            this.spawnParticle(EnumParticleTypes.BARRIER, (float)i + 0.5f, (float)j + 0.5f, (float)k + 0.5f, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    public void removeAllEntities() {
        this.loadedEntityList.removeAll((Collection)this.unloadedEntityList);
        for (int i = 0; i < this.unloadedEntityList.size(); ++i) {
            Entity entity = (Entity)this.unloadedEntityList.get(i);
            int j = entity.chunkCoordX;
            int k = entity.chunkCoordZ;
            if (!entity.addedToChunk || !this.isChunkLoaded(j, k, true)) continue;
            this.getChunk(j, k).removeEntity(entity);
        }
        for (int i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
            this.onEntityRemoved((Entity)this.unloadedEntityList.get(i1));
        }
        this.unloadedEntityList.clear();
        for (int j1 = 0; j1 < this.loadedEntityList.size(); ++j1) {
            Entity entity1 = (Entity)this.loadedEntityList.get(j1);
            Entity entity2 = entity1.getRidingEntity();
            if (entity2 != null) {
                if (!entity2.isDead && entity2.isPassenger(entity1)) continue;
                entity1.dismountRidingEntity();
            }
            if (!entity1.isDead) continue;
            int k1 = entity1.chunkCoordX;
            int l = entity1.chunkCoordZ;
            if (entity1.addedToChunk && this.isChunkLoaded(k1, l, true)) {
                this.getChunk(k1, l).removeEntity(entity1);
            }
            this.loadedEntityList.remove(j1--);
            this.onEntityRemoved(entity1);
        }
    }

    public CrashReportCategory addWorldInfoToCrashReport(CrashReport report) {
        CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
        crashreportcategory.addDetail("Forced entities", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        crashreportcategory.addDetail("Retry entities", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        crashreportcategory.addDetail("Server brand", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        crashreportcategory.addDetail("Server type", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        return crashreportcategory;
    }

    public void playSound(@Nullable EntityPlayer player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
        if (player == this.mc.player) {
            this.playSound(x, y, z, soundIn, category, volume, pitch, false);
        }
    }

    public void playSound(BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
        this.playSound((double)pos.p() + 0.5, (double)pos.q() + 0.5, (double)pos.r() + 0.5, soundIn, category, volume, pitch, distanceDelay);
    }

    public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
        double d0 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
        PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(soundIn, category, volume, pitch, (float)x, (float)y, (float)z);
        if (distanceDelay && d0 > 100.0) {
            double d1 = Math.sqrt((double)d0) / 40.0;
            this.mc.getSoundHandler().playDelayedSound((ISound)positionedsoundrecord, (int)(d1 * 20.0));
        } else {
            this.mc.getSoundHandler().playSound((ISound)positionedsoundrecord);
        }
    }

    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, @Nullable NBTTagCompound compound) {
        this.mc.effectRenderer.addEffect((Particle)new ParticleFirework.Starter((World)this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compound));
    }

    public void sendPacketToServer(Packet<?> packetIn) {
        this.connection.sendPacket(packetIn);
    }

    public void setWorldScoreboard(Scoreboard scoreboardIn) {
        this.worldScoreboard = scoreboardIn;
    }

    public void setWorldTime(long time) {
        if (time < 0L) {
            time = -time;
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
        } else {
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
        }
        super.setWorldTime(time);
    }

    public ChunkProviderClient getChunkProvider() {
        return (ChunkProviderClient)super.getChunkProvider();
    }
}
