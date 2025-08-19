package mods.Hileb.optirefine.mixin.defaults.minecraft.client.multiplayer;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.CustomGuis;
import net.optifine.DynamicLights;
import net.optifine.override.PlayerControllerOF;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import java.util.Set;

@SuppressWarnings("unused")
@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends World {

    @Shadow @Final
    private Minecraft mc;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    protected Set<ChunkPos> visibleChunks;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private int playerChunkX = Integer.MIN_VALUE;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private int playerChunkY = Integer.MIN_VALUE;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean playerUpdate = false;

    // Ignored
    protected MixinWorldClient(ISaveHandler p_i45749_1_, WorldInfo p_i45749_2_, WorldProvider p_i45749_3_, Profiler p_i45749_4_, boolean p_i45749_5_) {
        super(p_i45749_1_, p_i45749_2_, p_i45749_3_, p_i45749_4_, p_i45749_5_);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectInit(NetHandlerPlayClient netHandler, WorldSettings settings, int dimension, EnumDifficulty difficulty, Profiler profilerIn, CallbackInfo ci){
        if (this.mc.playerController != null && this.mc.playerController.getClass() == PlayerControllerMP.class) {
            this.mc.playerController = new PlayerControllerOF(this.mc, netHandler);
            CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.playerController);
        }
    }

    @WrapMethod(method = "refreshVisibleChunks")
    public void injectRefreshVisibleChunks(Operation<Void> original){
        int cx = MathHelper.floor(this.mc.player.posX / 16.0);
        int cy = MathHelper.floor(this.mc.player.posY / 16.0);
        if (cx != this.playerChunkX || cy != this.playerChunkY) {
            this.playerChunkX = cx;
            this.playerChunkY = cy;
            original.call();
        }
    }

    @Definition(id = "ambienceTicks", field = "Lnet/minecraft/client/multiplayer/WorldClient;ambienceTicks:I")
    @Expression("this.ambienceTicks == 0")
    @ModifyExpressionValue(method = "playMoodSoundAndCheckLight", at = @At("MIXINEXTRAS:EXPRESSION"))
    public boolean injectPlayMoodSoundAndCheckLight(boolean ambienceTicksIs0, @Local(argsOnly = true) Chunk chunkIn){
        if (ambienceTicksIs0) {
            EntityPlayerSP player = this.mc.player;
            return !((player == null) || (Math.abs(player.chunkCoordX - chunkIn.x) > 1 || Math.abs(player.chunkCoordZ - chunkIn.z) > 1));
        } else return false;
    }

    @Unique
    @Public
    @Override
    public int getCombinedLight(@Nonnull BlockPos pos, int lightValue) {
        int combinedLight = super.getCombinedLight(pos, lightValue);
        if (Config.isDynamicLights()) {
            combinedLight = DynamicLights.getCombinedLight(pos, combinedLight);
        }

        return combinedLight;
    }

    @Unique
    @Public
    @Override
    public boolean setBlockState(@Nonnull BlockPos pos, @Nonnull  IBlockState newState, int flags) {
        this.playerUpdate = this.isPlayerActing();
        boolean res = super.setBlockState(pos, newState, flags);
        this.playerUpdate = false;
        return res;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private boolean isPlayerActing() {
        if (this.mc.playerController instanceof PlayerControllerOF controlOF) {
            return controlOF.isActing();
        } else {
            return false;
        }
    }

    @Unique
    @Public
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }

}
/*
+++ net/minecraft/client/multiplayer/WorldClient.java	Tue Aug 19 14:59:58 2025
@@ -1,19 +1,21 @@
 package net.minecraft.client.multiplayer;

 import com.google.common.collect.Sets;
 import java.util.Random;
 import java.util.Set;
 import javax.annotation.Nullable;
+import net.minecraft.block.Block;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.audio.MovingSoundMinecart;
 import net.minecraft.client.audio.PositionedSoundRecord;
+import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.client.network.NetHandlerPlayClient;
-import net.minecraft.client.particle.ParticleFirework;
+import net.minecraft.client.particle.ParticleFirework.Starter;
 import net.minecraft.crash.CrashReport;
 import net.minecraft.crash.CrashReportCategory;
 import net.minecraft.crash.ICrashReportDetail;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.item.EntityMinecart;
 import net.minecraft.entity.player.EntityPlayer;
@@ -28,45 +30,68 @@
 import net.minecraft.util.EnumParticleTypes;
 import net.minecraft.util.SoundCategory;
 import net.minecraft.util.SoundEvent;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.ChunkPos;
 import net.minecraft.util.math.MathHelper;
+import net.minecraft.util.math.BlockPos.MutableBlockPos;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraft.world.DimensionType;
 import net.minecraft.world.EnumDifficulty;
 import net.minecraft.world.EnumSkyBlock;
 import net.minecraft.world.GameType;
 import net.minecraft.world.World;
+import net.minecraft.world.WorldProvider;
 import net.minecraft.world.WorldSettings;
 import net.minecraft.world.chunk.Chunk;
 import net.minecraft.world.chunk.IChunkProvider;
 import net.minecraft.world.storage.SaveDataMemoryStorage;
 import net.minecraft.world.storage.SaveHandlerMP;
 import net.minecraft.world.storage.WorldInfo;
+import net.optifine.CustomGuis;
+import net.optifine.DynamicLights;
+import net.optifine.override.PlayerControllerOF;
+import net.optifine.reflect.Reflector;

 public class WorldClient extends World {
    private final NetHandlerPlayClient connection;
    private ChunkProviderClient clientChunkProvider;
    private final Set<Entity> entityList = Sets.newHashSet();
    private final Set<Entity> entitySpawnQueue = Sets.newHashSet();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set<ChunkPos> previousActiveChunkSet = Sets.newHashSet();
-   private int ambienceTicks = this.rand.nextInt(12000);
-   protected Set<ChunkPos> visibleChunks = Sets.newHashSet();
+   private int ambienceTicks;
+   protected Set<ChunkPos> visibleChunks;
+   private int playerChunkX = Integer.MIN_VALUE;
+   private int playerChunkY = Integer.MIN_VALUE;
+   private boolean playerUpdate = false;

    public WorldClient(NetHandlerPlayClient var1, WorldSettings var2, int var3, EnumDifficulty var4, Profiler var5) {
-      super(new SaveHandlerMP(), new WorldInfo(var2, "MpServer"), DimensionType.getById(var3).createDimension(), var5, true);
+      super(new SaveHandlerMP(), new WorldInfo(var2, "MpServer"), makeWorldProvider(var3), var5, true);
+      this.ambienceTicks = this.rand.nextInt(12000);
+      this.visibleChunks = Sets.newHashSet();
       this.connection = var1;
       this.getWorldInfo().setDifficulty(var4);
-      this.setSpawnPoint(new BlockPos(8, 64, 8));
       this.provider.setWorld(this);
+      this.setSpawnPoint(new BlockPos(8, 64, 8));
       this.chunkProvider = this.createChunkProvider();
       this.mapStorage = new SaveDataMemoryStorage();
       this.calculateInitialSkylight();
       this.calculateInitialWeather();
+      Reflector.call(this, Reflector.ForgeWorld_initCapabilities, new Object[0]);
+      Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[]{this});
+      if (this.mc.playerController != null && this.mc.playerController.getClass() == PlayerControllerMP.class) {
+         this.mc.playerController = new PlayerControllerOF(this.mc, var1);
+         CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.playerController);
+      }
+   }
+
+   private static WorldProvider makeWorldProvider(int var0) {
+      return Reflector.DimensionManager_createProviderFor.exists()
+         ? (WorldProvider)Reflector.call(Reflector.DimensionManager_createProviderFor, new Object[]{var0})
+         : DimensionType.getById(var0).createDimension();
    }

    public void tick() {
       super.tick();
       this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
       if (this.getGameRules().getBoolean("doDaylightCycle")) {
@@ -100,25 +125,31 @@

    protected boolean isChunkLoaded(int var1, int var2, boolean var3) {
       return var3 || !this.getChunkProvider().provideChunk(var1, var2).isEmpty();
    }

    protected void refreshVisibleChunks() {
-      this.visibleChunks.clear();
-      int var1 = this.mc.gameSettings.renderDistanceChunks;
-      this.profiler.startSection("buildList");
-      int var2 = MathHelper.floor(this.mc.player.posX / 16.0);
-      int var3 = MathHelper.floor(this.mc.player.posZ / 16.0);
-
-      for (int var4 = -var1; var4 <= var1; var4++) {
-         for (int var5 = -var1; var5 <= var1; var5++) {
-            this.visibleChunks.add(new ChunkPos(var4 + var2, var5 + var3));
+      int var1 = MathHelper.floor(this.mc.player.posX / 16.0);
+      int var2 = MathHelper.floor(this.mc.player.posZ / 16.0);
+      if (var1 != this.playerChunkX || var2 != this.playerChunkY) {
+         this.playerChunkX = var1;
+         this.playerChunkY = var2;
+         this.visibleChunks.clear();
+         int var3 = this.mc.gameSettings.renderDistanceChunks;
+         this.profiler.startSection("buildList");
+         int var4 = MathHelper.floor(this.mc.player.posX / 16.0);
+         int var5 = MathHelper.floor(this.mc.player.posZ / 16.0);
+
+         for (int var6 = -var3; var6 <= var3; var6++) {
+            for (int var7 = -var3; var7 <= var3; var7++) {
+               this.visibleChunks.add(new ChunkPos(var6 + var4, var7 + var5));
+            }
          }
-      }

-      this.profiler.endSection();
+         this.profiler.endSection();
+      }
    }

    protected void updateBlocks() {
       this.refreshVisibleChunks();
       if (this.ambienceTicks > 0) {
          this.ambienceTicks--;
@@ -211,13 +242,13 @@
    @Nullable
    public Entity getEntityByID(int var1) {
       return (Entity)(var1 == this.mc.player.getEntityId() ? this.mc.player : super.getEntityByID(var1));
    }

    public Entity removeEntityFromWorld(int var1) {
-      Entity var2 = this.entitiesById.removeObject(var1);
+      Entity var2 = (Entity)this.entitiesById.removeObject(var1);
       if (var2 != null) {
          this.entityList.remove(var2);
          this.removeEntity(var2);
       }

       return var2;
@@ -239,49 +270,75 @@
    protected void updateWeather() {
    }

    protected void playMoodSoundAndCheckLight(int var1, int var2, Chunk var3) {
       super.playMoodSoundAndCheckLight(var1, var2, var3);
       if (this.ambienceTicks == 0) {
+         EntityPlayerSP var4 = this.mc.player;
+         if (var4 == null) {
+            return;
+         }
+
+         if (Math.abs(var4.chunkCoordX - var3.x) > 1 || Math.abs(var4.chunkCoordZ - var3.z) > 1) {
+            return;
+         }
+
          this.updateLCG = this.updateLCG * 3 + 1013904223;
-         int var4 = this.updateLCG >> 2;
-         int var5 = var4 & 15;
-         int var6 = var4 >> 8 & 15;
-         int var7 = var4 >> 16 & 0xFF;
-         BlockPos var8 = new BlockPos(var5 + var1, var7, var6 + var2);
-         IBlockState var9 = var3.getBlockState(var8);
-         var5 += var1;
-         var6 += var2;
-         if (var9.getMaterial() == Material.AIR && this.getLight(var8) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, var8) <= 0) {
-            double var10 = this.mc.player.getDistanceSq(var5 + 0.5, var7 + 0.5, var6 + 0.5);
-            if (this.mc.player != null && var10 > 4.0 && var10 < 256.0) {
-               this.playSound(
-                  var5 + 0.5, var7 + 0.5, var6 + 0.5, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.7F, 0.8F + this.rand.nextFloat() * 0.2F, false
-               );
-               this.ambienceTicks = this.rand.nextInt(12000) + 6000;
-            }
+         int var5 = this.updateLCG >> 2;
+         int var6 = var5 & 15;
+         int var7 = var5 >> 8 & 15;
+         int var8 = var5 >> 16 & 0xFF;
+         var8 /= 2;
+         if (var4.posY > 160.0) {
+            var8 += 128;
+         } else if (var4.posY > 96.0) {
+            var8 += 64;
+         }
+
+         BlockPos var9 = new BlockPos(var6 + var1, var8, var7 + var2);
+         IBlockState var10 = var3.getBlockState(var9);
+         var6 += var1;
+         var7 += var2;
+         double var11 = this.mc.player.getDistanceSq(var6 + 0.5, var8 + 0.5, var7 + 0.5);
+         if (var11 < 4.0) {
+            return;
+         }
+
+         if (var11 > 255.0) {
+            return;
+         }
+
+         if (var10.a() == Material.AIR && this.getLight(var9) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, var9) <= 0) {
+            this.playSound(
+               var6 + 0.5, var8 + 0.5, var7 + 0.5, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 0.7F, 0.8F + this.rand.nextFloat() * 0.2F, false
+            );
+            this.ambienceTicks = this.rand.nextInt(12000) + 6000;
          }
       }
    }

    public void doVoidFogParticles(int var1, int var2, int var3) {
       byte var4 = 32;
       Random var5 = new Random();
       ItemStack var6 = this.mc.player.getHeldItemMainhand();
+      if (var6 == null || Block.getBlockFromItem(var6.getItem()) != Blocks.BARRIER) {
+         var6 = this.mc.player.getHeldItemOffhand();
+      }
+
       boolean var7 = this.mc.playerController.getCurrentGameType() == GameType.CREATIVE
          && !var6.isEmpty()
          && var6.getItem() == Item.getItemFromBlock(Blocks.BARRIER);
-      BlockPos.MutableBlockPos var8 = new BlockPos.MutableBlockPos();
+      MutableBlockPos var8 = new MutableBlockPos();

       for (int var9 = 0; var9 < 667; var9++) {
          this.showBarrierParticles(var1, var2, var3, 16, var5, var7, var8);
          this.showBarrierParticles(var1, var2, var3, 32, var5, var7, var8);
       }
    }

-   public void showBarrierParticles(int var1, int var2, int var3, int var4, Random var5, boolean var6, BlockPos.MutableBlockPos var7) {
+   public void showBarrierParticles(int var1, int var2, int var3, int var4, Random var5, boolean var6, MutableBlockPos var7) {
       int var8 = var1 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
       int var9 = var2 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
       int var10 = var3 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
       var7.setPos(var8, var9, var10);
       IBlockState var11 = this.getBlockState(var7);
       var11.getBlock().randomDisplayTick(var11, this, var7, var5);
@@ -291,28 +348,28 @@
    }

    public void removeAllEntities() {
       this.loadedEntityList.removeAll(this.unloadedEntityList);

       for (int var1 = 0; var1 < this.unloadedEntityList.size(); var1++) {
-         Entity var2 = this.unloadedEntityList.get(var1);
+         Entity var2 = (Entity)this.unloadedEntityList.get(var1);
          int var3 = var2.chunkCoordX;
          int var4 = var2.chunkCoordZ;
          if (var2.addedToChunk && this.isChunkLoaded(var3, var4, true)) {
             this.getChunk(var3, var4).removeEntity(var2);
          }
       }

       for (int var6 = 0; var6 < this.unloadedEntityList.size(); var6++) {
-         this.onEntityRemoved(this.unloadedEntityList.get(var6));
+         this.onEntityRemoved((Entity)this.unloadedEntityList.get(var6));
       }

       this.unloadedEntityList.clear();

       for (int var7 = 0; var7 < this.loadedEntityList.size(); var7++) {
-         Entity var8 = this.loadedEntityList.get(var7);
+         Entity var8 = (Entity)this.loadedEntityList.get(var7);
          Entity var9 = var8.getRidingEntity();
          if (var9 != null) {
             if (!var9.isDead && var9.isPassenger(var8)) {
                continue;
             }

@@ -376,13 +433,13 @@
       } else {
          this.mc.getSoundHandler().playSound(var14);
       }
    }

    public void makeFireworks(double var1, double var3, double var5, double var7, double var9, double var11, @Nullable NBTTagCompound var13) {
-      this.mc.effectRenderer.addEffect(new ParticleFirework.Starter(this, var1, var3, var5, var7, var9, var11, this.mc.effectRenderer, var13));
+      this.mc.effectRenderer.addEffect(new Starter(this, var1, var3, var5, var7, var9, var11, this.mc.effectRenderer, var13));
    }

    public void sendPacketToServer(Packet<?> var1) {
       this.connection.sendPacket(var1);
    }

@@ -400,8 +457,37 @@

       super.setWorldTime(var1);
    }

    public ChunkProviderClient getChunkProvider() {
       return (ChunkProviderClient)super.getChunkProvider();
+   }
+
+   public int getCombinedLight(BlockPos var1, int var2) {
+      int var3 = super.getCombinedLight(var1, var2);
+      if (Config.isDynamicLights()) {
+         var3 = DynamicLights.getCombinedLight(var1, var3);
+      }
+
+      return var3;
+   }
+
+   public boolean setBlockState(BlockPos var1, IBlockState var2, int var3) {
+      this.playerUpdate = this.isPlayerActing();
+      boolean var4 = super.setBlockState(var1, var2, var3);
+      this.playerUpdate = false;
+      return var4;
+   }
+
+   private boolean isPlayerActing() {
+      if (this.mc.playerController instanceof PlayerControllerOF) {
+         PlayerControllerOF var1 = (PlayerControllerOF)this.mc.playerController;
+         return var1.isActing();
+      } else {
+         return false;
+      }
+   }
+
+   public boolean isPlayerUpdate() {
+      return this.playerUpdate;
    }
 }
 */
