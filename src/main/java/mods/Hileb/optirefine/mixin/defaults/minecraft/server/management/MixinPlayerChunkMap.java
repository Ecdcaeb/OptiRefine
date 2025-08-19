package mods.Hileb.optirefine.mixin.defaults.minecraft.server.management;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.optifine.ChunkPosComparator;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(PlayerChunkMap.class)
public abstract class MixinPlayerChunkMap {

    @Shadow @Final
    private WorldServer world;

    @Shadow
    private int playerViewRadius;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private final Map<EntityPlayerMP, Set<ChunkPos>> mapPlayerPendingEntries = new HashMap<>();

    @Shadow private native PlayerChunkMapEntry getOrCreateEntry(int chunkX, int chunkZ) ;


    @Inject(method = "tick", at = @At("HEAD"))
    public void inject_tick_(CallbackInfo ci){
        Set<Map.Entry<EntityPlayerMP, Set<ChunkPos>>> pairs = this.mapPlayerPendingEntries.entrySet();
        Iterator<Map.Entry<EntityPlayerMP, Set<ChunkPos>>> it = pairs.iterator();

        while (it.hasNext()) {
            Map.Entry<EntityPlayerMP, Set<ChunkPos>> entry = it.next();
            Set<ChunkPos> setPending = entry.getValue();
            if (!setPending.isEmpty()) {
                EntityPlayerMP player = entry.getKey();
                if (player.getServerWorld() != this.world) {
                    it.remove();
                } else {
                    int countUpdates = this.playerViewRadius / 3 + 1;
                    if (!Config.isLazyChunkLoading()) {
                        countUpdates = this.playerViewRadius * 2 + 1;
                    }

                    for (ChunkPos chunkPos : this.getNearest(setPending, player, countUpdates)) {
                        PlayerChunkMapEntry pcmr = this.getOrCreateEntry(chunkPos.x, chunkPos.z);
                        if (!pcmr.containsPlayer(player)) {
                            pcmr.addPlayer(player);
                        }

                        setPending.remove(chunkPos);
                    }
                }
            }
        }
    }

    @Definition(id = "players", field = "Lnet/minecraft/server/management/PlayerChunkMap;players:Ljava/util/List;")
    @Definition(id = "isEmpty", method = "Ljava/util/List;isEmpty()Z")
    @Expression("this.players.isEmpty()")
    @ModifyExpressionValue(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
    public boolean changeUnloadLogic(boolean original){
        return original && !this.world.provider.canRespawnHere();
    }



    @Inject(method = "addPlayer", at = @At("HEAD"))
    public void inject_addPlayer(EntityPlayerMP player, CallbackInfo ci,
                                 @Share("kMin") LocalIntRef kMin,
                                 @Share("kMax")LocalIntRef kMax,
                                 @Share("lMin")LocalIntRef lMin,
                                 @Share("lMax")LocalIntRef lMax,
                                 @Share("setPendingEntries") LocalRef<Set<ChunkPos>> setPendingEntries
                                 ){
        int i = (int)player.posX >> 4;
        int j = (int)player.posZ >> 4;
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;
        int loadRadius = Math.min(this.playerViewRadius, 8);
        kMin.set(i - loadRadius);
        kMax.set(i + loadRadius);
        lMin.set(j - loadRadius);
        lMax.set(j + loadRadius);
        setPendingEntries.set(this.getPendingEntriesSafe(player));
    }


    @Redirect(method = "addPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getOrCreateEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;"))
    public PlayerChunkMapEntry wrap_getOrCreateEntry(PlayerChunkMap instance, int k, int l,
                                                     @Share("kMin")LocalIntRef kMin,
                                                     @Share("kMax")LocalIntRef kMax,
                                                     @Share("lMin")LocalIntRef lMin,
                                                     @Share("lMax")LocalIntRef lMax,
                                                     @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries
    ){
        if (k < kMin.get() || k > kMax.get() || l < lMin.get() || l > lMax.get()) {
            setPendingEntries.get().add(new ChunkPos(k, l));
            return null;
        }
        else return this.getOrCreateEntry(k, l);
    }

    @Redirect(method = "addPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;addPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V"))
    public void wrap_addPlayer(PlayerChunkMapEntry instance, EntityPlayerMP p_187276_1_){
        if (instance != null) instance.addPlayer(p_187276_1_);
    }

    /*
         if (Config.isLazyChunkLoading()) {
              setPendingEntries.add(new ChunkPos(l1, i2));
         } else {
              this.getOrCreateEntry(l1, i2).addPlayer(player);
         }
    */
    @Inject(method = "updateMovingPlayer", at = @At("HEAD"))
    public void atHeadOfUpdateMovingPlayer(EntityPlayerMP p_72685_1_, CallbackInfo ci, @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries){
        setPendingEntries.set(this.getPendingEntriesSafe(p_72685_1_));
    }

    @Redirect(method = "updateMovingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getOrCreateEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;"))
    public PlayerChunkMapEntry wrap_getOrCreateEntry1(PlayerChunkMap instance, int p_187302_1_, int p_187302_2_, @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries){
        if (Config.isLazyChunkLoading()) {
            setPendingEntries.get().add(new ChunkPos(p_187302_1_, p_187302_2_));
            return null;
        } else return instance.getEntry(p_187302_1_, p_187302_2_);
    }

    @Redirect(method = "updateMovingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;addPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V"))
    public void wrap_addPlayer1(PlayerChunkMapEntry instance, EntityPlayerMP p_187276_1_){
        if (instance != null) instance.addPlayer(p_187276_1_);
    }

    @Redirect(method = "updateMovingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;"))
    public PlayerChunkMapEntry removeEntry(PlayerChunkMap instance, int p_187301_1_, int p_187301_2_, @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries){
        setPendingEntries.get().remove(new ChunkPos(p_187301_1_, p_187301_2_));
        return instance.getEntry(p_187301_1_, p_187301_2_);
    }


    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getOrCreateEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;"))
    public PlayerChunkMapEntry wrap_getOrCreateEntry2(PlayerChunkMap instance, int p_187302_1_, int p_187302_2_,
           @Share(value = "chunkX", namespace = "optirefine") LocalIntRef chunkX,
           @Share(value = "chunkZ", namespace = "optirefine") LocalIntRef chunkZ
    ){
        if (Config.isLazyChunkLoading()) {
            chunkX.set(p_187302_1_);
            chunkZ.set(p_187302_2_);
            return null;
        } else return instance.getEntry(p_187302_1_, p_187302_2_);
    }

    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;containsPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)Z"))
    public boolean wrap_getOrCreateEntry2(PlayerChunkMapEntry instance, EntityPlayerMP p_187275_1_,
                                          @Share(value = "chunkX", namespace = "optirefine") LocalIntRef chunkX,
                                          @Share(value = "chunkZ", namespace = "optirefine") LocalIntRef chunkZ
    ){
        if (instance == null) {
            this.getPendingEntriesSafe(p_187275_1_).add(new ChunkPos(chunkX.get(), chunkZ.get()));
            return false;
        } else return instance.containsPlayer(p_187275_1_);
    }

    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;removePlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V"))
    public void removeEntry2(PlayerChunkMapEntry instance, EntityPlayerMP p_187277_1_, @Local EntityPlayerMP playerMP,
                             @Share(value = "chunkX", namespace = "optirefine") LocalIntRef chunkX,
                             @Share(value = "chunkZ", namespace = "optirefine") LocalIntRef chunkZ
    ){
        if (instance == null) {
            this.getPendingEntriesSafe(playerMP).remove(new ChunkPos(chunkX.get(), chunkZ.get()));
        } else {
            instance.removePlayer(p_187277_1_);
        }

    }


    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private PriorityQueue<ChunkPos> getNearest(Set<ChunkPos> setPending, EntityPlayerMP player, int count) {
        float playerYaw = player.rotationYaw + 90.0F;

        while (playerYaw <= -180.0F) {
            playerYaw += 360.0F;
        }

        while (playerYaw > 180.0F) {
            playerYaw -= 360.0F;
        }

        double playerYawRad = playerYaw * (Math.PI / 180.0);
        double playerPitch = player.rotationPitch;
        double playerPitchRad = playerPitch * (Math.PI / 180.0);
        ChunkPosComparator comp = new ChunkPosComparator(player.chunkCoordX, player.chunkCoordZ, playerYawRad, playerPitchRad);
        Comparator<ChunkPos> compRev = Collections.reverseOrder(comp);
        PriorityQueue<ChunkPos> queue = new PriorityQueue<>(compRev);

        for (ChunkPos chunkPos : setPending) {
            if (queue.size() < count) {
                queue.add(chunkPos);
            } else {
                ChunkPos furthest = queue.peek();
                if (comp.compare(chunkPos, furthest) < 0) {
                    queue.remove();
                    queue.add(chunkPos);
                }
            }
        }

        return queue;
    }


    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private Set<ChunkPos> getPendingEntriesSafe(EntityPlayerMP player) {
        Set<ChunkPos> setPendingEntries = this.mapPlayerPendingEntries.get(player);
        if (setPendingEntries != null) {
            return setPendingEntries;
        } else {
            int loadRadius = Math.min(this.playerViewRadius, 8);
            int playerWidth = this.playerViewRadius * 2 + 1;
            int loadWidth = loadRadius * 2 + 1;
            int countLazyChunks = playerWidth * playerWidth - loadWidth * loadWidth;
            countLazyChunks = Math.max(countLazyChunks, 16);
            Set<ChunkPos> var7 = new HashSet<>(countLazyChunks);
            this.mapPlayerPendingEntries.put(player, var7);
            return var7;
        }
    }

}

/*
--- net/minecraft/server/management/PlayerChunkMap.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/server/management/PlayerChunkMap.java	Tue Aug 19 14:59:58 2025
@@ -6,23 +6,29 @@
 import com.google.common.collect.Lists;
 import com.google.common.collect.Sets;
 import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
 import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
 import java.util.Collections;
 import java.util.Comparator;
+import java.util.HashMap;
+import java.util.HashSet;
 import java.util.Iterator;
 import java.util.List;
+import java.util.Map;
+import java.util.PriorityQueue;
 import java.util.Set;
+import java.util.Map.Entry;
 import javax.annotation.Nullable;
 import net.minecraft.entity.player.EntityPlayerMP;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.ChunkPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.WorldProvider;
 import net.minecraft.world.WorldServer;
 import net.minecraft.world.chunk.Chunk;
+import net.optifine.ChunkPosComparator;

 public class PlayerChunkMap {
    private static final Predicate<EntityPlayerMP> NOT_SPECTATOR = new Predicate<EntityPlayerMP>() {
       public boolean apply(@Nullable EntityPlayerMP var1) {
          return var1 != null && !var1.isSpectator();
       }
@@ -40,12 +46,13 @@
    private final List<PlayerChunkMapEntry> entriesWithoutChunks = Lists.newLinkedList();
    private final List<PlayerChunkMapEntry> entries = Lists.newArrayList();
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private boolean sortMissingChunks = true;
    private boolean sortSendToPlayers = true;
+   private final Map<EntityPlayerMP, Set<ChunkPos>> mapPlayerPendingEntries = new HashMap<>();

    public PlayerChunkMap(WorldServer var1) {
       this.world = var1;
       this.setPlayerViewRadius(var1.getMinecraftServer().getPlayerList().getViewDistance());
    }

@@ -78,90 +85,118 @@
             return (Chunk)this.endOfData();
          }
       };
    }

    public void tick() {
-      long var1 = this.world.getTotalWorldTime();
-      if (var1 - this.previousTotalWorldTime > 8000L) {
-         this.previousTotalWorldTime = var1;
-
-         for (int var3 = 0; var3 < this.entries.size(); var3++) {
-            PlayerChunkMapEntry var4 = this.entries.get(var3);
-            var4.update();
-            var4.updateChunkInhabitedTime();
+      Set var1 = this.mapPlayerPendingEntries.entrySet();
+      Iterator var2 = var1.iterator();
+
+      while (var2.hasNext()) {
+         Entry var3 = (Entry)var2.next();
+         Set var4 = (Set)var3.getValue();
+         if (!var4.isEmpty()) {
+            EntityPlayerMP var5 = (EntityPlayerMP)var3.getKey();
+            if (var5.getServerWorld() != this.world) {
+               var2.remove();
+            } else {
+               int var6 = this.playerViewRadius / 3 + 1;
+               if (!Config.isLazyChunkLoading()) {
+                  var6 = this.playerViewRadius * 2 + 1;
+               }
+
+               for (ChunkPos var9 : this.getNearest(var4, var5, var6)) {
+                  PlayerChunkMapEntry var10 = this.getOrCreateEntry(var9.x, var9.z);
+                  if (!var10.containsPlayer(var5)) {
+                     var10.addPlayer(var5);
+                  }
+
+                  var4.remove(var9);
+               }
+            }
+         }
+      }
+
+      long var11 = this.world.getTotalWorldTime();
+      if (var11 - this.previousTotalWorldTime > 8000L) {
+         this.previousTotalWorldTime = var11;
+
+         for (int var12 = 0; var12 < this.entries.size(); var12++) {
+            PlayerChunkMapEntry var17 = this.entries.get(var12);
+            var17.update();
+            var17.updateChunkInhabitedTime();
          }
       }

       if (!this.dirtyEntries.isEmpty()) {
-         for (PlayerChunkMapEntry var13 : this.dirtyEntries) {
-            var13.update();
+         for (PlayerChunkMapEntry var18 : this.dirtyEntries) {
+            var18.update();
          }

          this.dirtyEntries.clear();
       }

-      if (this.sortMissingChunks && var1 % 4L == 0L) {
+      if (this.sortMissingChunks && var11 % 4L == 0L) {
          this.sortMissingChunks = false;
          Collections.sort(this.entriesWithoutChunks, new Comparator<PlayerChunkMapEntry>() {
-            public int compare(PlayerChunkMapEntry var1, PlayerChunkMapEntry var2) {
-               return ComparisonChain.start().compare(var1.getClosestPlayerDistance(), var2.getClosestPlayerDistance()).result();
+            public int compare(PlayerChunkMapEntry var1, PlayerChunkMapEntry var2x) {
+               return ComparisonChain.start().compare(var1.getClosestPlayerDistance(), var2x.getClosestPlayerDistance()).result();
             }
          });
       }

-      if (this.sortSendToPlayers && var1 % 4L == 2L) {
+      if (this.sortSendToPlayers && var11 % 4L == 2L) {
          this.sortSendToPlayers = false;
          Collections.sort(this.pendingSendToPlayers, new Comparator<PlayerChunkMapEntry>() {
             public int compare(PlayerChunkMapEntry var1, PlayerChunkMapEntry var2) {
                return ComparisonChain.start().compare(var1.getClosestPlayerDistance(), var2.getClosestPlayerDistance()).result();
             }
          });
       }

       if (!this.entriesWithoutChunks.isEmpty()) {
-         long var10 = System.nanoTime() + 50000000L;
-         int var5 = 49;
-         Iterator var6 = this.entriesWithoutChunks.iterator();
-
-         while (var6.hasNext()) {
-            PlayerChunkMapEntry var7 = (PlayerChunkMapEntry)var6.next();
-            if (var7.getChunk() == null) {
-               boolean var8 = var7.hasPlayerMatching(CAN_GENERATE_CHUNKS);
-               if (var7.providePlayerChunk(var8)) {
-                  var6.remove();
-                  if (var7.sendToPlayers()) {
-                     this.pendingSendToPlayers.remove(var7);
+         long var14 = System.nanoTime() + 50000000L;
+         int var20 = 49;
+         Iterator var22 = this.entriesWithoutChunks.iterator();
+
+         while (var22.hasNext()) {
+            PlayerChunkMapEntry var23 = (PlayerChunkMapEntry)var22.next();
+            if (var23.getChunk() == null) {
+               boolean var24 = var23.hasPlayerMatching(CAN_GENERATE_CHUNKS);
+               if (var23.providePlayerChunk(var24)) {
+                  var22.remove();
+                  if (var23.sendToPlayers()) {
+                     this.pendingSendToPlayers.remove(var23);
                   }

-                  if (--var5 < 0 || System.nanoTime() > var10) {
+                  if (--var20 < 0 || System.nanoTime() > var14) {
                      break;
                   }
                }
             }
          }
       }

       if (!this.pendingSendToPlayers.isEmpty()) {
-         int var11 = 81;
-         Iterator var14 = this.pendingSendToPlayers.iterator();
+         int var15 = 81;
+         Iterator var19 = this.pendingSendToPlayers.iterator();

-         while (var14.hasNext()) {
-            PlayerChunkMapEntry var15 = (PlayerChunkMapEntry)var14.next();
-            if (var15.sendToPlayers()) {
-               var14.remove();
-               if (--var11 < 0) {
+         while (var19.hasNext()) {
+            PlayerChunkMapEntry var21 = (PlayerChunkMapEntry)var19.next();
+            if (var21.sendToPlayers()) {
+               var19.remove();
+               if (--var15 < 0) {
                   break;
                }
             }
          }
       }

       if (this.players.isEmpty()) {
-         WorldProvider var12 = this.world.provider;
-         if (!var12.canRespawnHere()) {
+         WorldProvider var16 = this.world.provider;
+         if (!var16.canRespawnHere()) {
             this.world.getChunkProvider().queueUnloadAll();
          }
       }
    }

    public boolean contains(int var1, int var2) {
@@ -204,24 +239,35 @@

    public void addPlayer(EntityPlayerMP var1) {
       int var2 = (int)var1.posX >> 4;
       int var3 = (int)var1.posZ >> 4;
       var1.managedPosX = var1.posX;
       var1.managedPosZ = var1.posZ;
-
-      for (int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; var4++) {
-         for (int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; var5++) {
-            this.getOrCreateEntry(var4, var5).addPlayer(var1);
+      int var4 = Math.min(this.playerViewRadius, 8);
+      int var5 = var2 - var4;
+      int var6 = var2 + var4;
+      int var7 = var3 - var4;
+      int var8 = var3 + var4;
+      Set var9 = this.getPendingEntriesSafe(var1);
+
+      for (int var10 = var2 - this.playerViewRadius; var10 <= var2 + this.playerViewRadius; var10++) {
+         for (int var11 = var3 - this.playerViewRadius; var11 <= var3 + this.playerViewRadius; var11++) {
+            if (var10 >= var5 && var10 <= var6 && var11 >= var7 && var11 <= var8) {
+               this.getOrCreateEntry(var10, var11).addPlayer(var1);
+            } else {
+               var9.add(new ChunkPos(var10, var11));
+            }
          }
       }

       this.players.add(var1);
       this.markSortPending();
    }

    public void removePlayer(EntityPlayerMP var1) {
+      this.mapPlayerPendingEntries.remove(var1);
       int var2 = (int)var1.managedPosX >> 4;
       int var3 = (int)var1.managedPosZ >> 4;

       for (int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; var4++) {
          for (int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; var5++) {
             PlayerChunkMapEntry var6 = this.getEntry(var4, var5);
@@ -235,38 +281,45 @@
       this.markSortPending();
    }

    private boolean overlaps(int var1, int var2, int var3, int var4, int var5) {
       int var6 = var1 - var3;
       int var7 = var2 - var4;
-      return var6 < -var5 || var6 > var5 ? false : var7 >= -var5 && var7 <= var5;
+      return var6 >= -var5 && var6 <= var5 ? var7 >= -var5 && var7 <= var5 : false;
    }

    public void updateMovingPlayer(EntityPlayerMP var1) {
       int var2 = (int)var1.posX >> 4;
       int var3 = (int)var1.posZ >> 4;
       double var4 = var1.managedPosX - var1.posX;
       double var6 = var1.managedPosZ - var1.posZ;
       double var8 = var4 * var4 + var6 * var6;
-      if (!(var8 < 64.0)) {
+      if (var8 >= 64.0) {
          int var10 = (int)var1.managedPosX >> 4;
          int var11 = (int)var1.managedPosZ >> 4;
          int var12 = this.playerViewRadius;
          int var13 = var2 - var10;
          int var14 = var3 - var11;
          if (var13 != 0 || var14 != 0) {
-            for (int var15 = var2 - var12; var15 <= var2 + var12; var15++) {
-               for (int var16 = var3 - var12; var16 <= var3 + var12; var16++) {
-                  if (!this.overlaps(var15, var16, var10, var11, var12)) {
-                     this.getOrCreateEntry(var15, var16).addPlayer(var1);
+            Set var15 = this.getPendingEntriesSafe(var1);
+
+            for (int var16 = var2 - var12; var16 <= var2 + var12; var16++) {
+               for (int var17 = var3 - var12; var17 <= var3 + var12; var17++) {
+                  if (!this.overlaps(var16, var17, var10, var11, var12)) {
+                     if (Config.isLazyChunkLoading()) {
+                        var15.add(new ChunkPos(var16, var17));
+                     } else {
+                        this.getOrCreateEntry(var16, var17).addPlayer(var1);
+                     }
                   }

-                  if (!this.overlaps(var15 - var13, var16 - var14, var2, var3, var12)) {
-                     PlayerChunkMapEntry var17 = this.getEntry(var15 - var13, var16 - var14);
-                     if (var17 != null) {
-                        var17.removePlayer(var1);
+                  if (!this.overlaps(var16 - var13, var17 - var14, var2, var3, var12)) {
+                     var15.remove(new ChunkPos(var16 - var13, var17 - var14));
+                     PlayerChunkMapEntry var18 = this.getEntry(var16 - var13, var17 - var14);
+                     if (var18 != null) {
+                        var18.removePlayer(var1);
                      }
                   }
                }
             }

             var1.managedPosX = var1.posX;
@@ -279,33 +332,42 @@
    public boolean isPlayerWatchingChunk(EntityPlayerMP var1, int var2, int var3) {
       PlayerChunkMapEntry var4 = this.getEntry(var2, var3);
       return var4 != null && var4.containsPlayer(var1) && var4.isSentToPlayers();
    }

    public void setPlayerViewRadius(int var1) {
-      var1 = MathHelper.clamp(var1, 3, 32);
+      var1 = MathHelper.clamp(var1, 3, 64);
       if (var1 != this.playerViewRadius) {
          int var2 = var1 - this.playerViewRadius;

-         for (EntityPlayerMP var5 : Lists.newArrayList(this.players)) {
-            int var6 = (int)var5.posX >> 4;
-            int var7 = (int)var5.posZ >> 4;
+         for (EntityPlayerMP var4 : Lists.newArrayList(this.players)) {
+            int var5 = (int)var4.posX >> 4;
+            int var6 = (int)var4.posZ >> 4;
+            Set var7 = this.getPendingEntriesSafe(var4);
             if (var2 > 0) {
-               for (int var12 = var6 - var1; var12 <= var6 + var1; var12++) {
-                  for (int var13 = var7 - var1; var13 <= var7 + var1; var13++) {
-                     PlayerChunkMapEntry var10 = this.getOrCreateEntry(var12, var13);
-                     if (!var10.containsPlayer(var5)) {
-                        var10.addPlayer(var5);
+               for (int var12 = var5 - var1; var12 <= var5 + var1; var12++) {
+                  for (int var13 = var6 - var1; var13 <= var6 + var1; var13++) {
+                     if (Config.isLazyChunkLoading()) {
+                        var7.add(new ChunkPos(var12, var13));
+                     } else {
+                        PlayerChunkMapEntry var14 = this.getOrCreateEntry(var12, var13);
+                        if (!var14.containsPlayer(var4)) {
+                           var14.addPlayer(var4);
+                        }
                      }
                   }
                }
             } else {
-               for (int var8 = var6 - this.playerViewRadius; var8 <= var6 + this.playerViewRadius; var8++) {
-                  for (int var9 = var7 - this.playerViewRadius; var9 <= var7 + this.playerViewRadius; var9++) {
-                     if (!this.overlaps(var8, var9, var6, var7, var1)) {
-                        this.getOrCreateEntry(var8, var9).removePlayer(var5);
+               for (int var8 = var5 - this.playerViewRadius; var8 <= var5 + this.playerViewRadius; var8++) {
+                  for (int var9 = var6 - this.playerViewRadius; var9 <= var6 + this.playerViewRadius; var9++) {
+                     if (!this.overlaps(var8, var9, var5, var6, var1)) {
+                        var7.remove(new ChunkPos(var8, var9));
+                        PlayerChunkMapEntry var10 = this.getEntry(var8, var9);
+                        if (var10 != null) {
+                           var10.removePlayer(var4);
+                        }
                      }
                   }
                }
             }
          }

@@ -340,9 +402,58 @@
       this.dirtyEntries.remove(var1);
       this.pendingSendToPlayers.remove(var1);
       this.entriesWithoutChunks.remove(var1);
       Chunk var5 = var1.getChunk();
       if (var5 != null) {
          this.getWorldServer().getChunkProvider().queueUnload(var5);
+      }
+   }
+
+   private PriorityQueue<ChunkPos> getNearest(Set<ChunkPos> var1, EntityPlayerMP var2, int var3) {
+      float var4 = var2.rotationYaw + 90.0F;
+
+      while (var4 <= -180.0F) {
+         var4 += 360.0F;
+      }
+
+      while (var4 > 180.0F) {
+         var4 -= 360.0F;
+      }
+
+      double var5 = var4 * (Math.PI / 180.0);
+      double var7 = var2.rotationPitch;
+      double var9 = var7 * (Math.PI / 180.0);
+      ChunkPosComparator var11 = new ChunkPosComparator(var2.chunkCoordX, var2.chunkCoordZ, var5, var9);
+      Comparator var12 = Collections.reverseOrder(var11);
+      PriorityQueue var13 = new PriorityQueue(var12);
+
+      for (ChunkPos var15 : var1) {
+         if (var13.size() < var3) {
+            var13.add(var15);
+         } else {
+            ChunkPos var16 = (ChunkPos)var13.peek();
+            if (var11.compare(var15, var16) < 0) {
+               var13.remove();
+               var13.add(var15);
+            }
+         }
+      }
+
+      return var13;
+   }
+
+   private Set<ChunkPos> getPendingEntriesSafe(EntityPlayerMP var1) {
+      Set var2 = this.mapPlayerPendingEntries.get(var1);
+      if (var2 != null) {
+         return var2;
+      } else {
+         int var3 = Math.min(this.playerViewRadius, 8);
+         int var4 = this.playerViewRadius * 2 + 1;
+         int var5 = var3 * 2 + 1;
+         int var6 = var4 * var4 - var5 * var5;
+         var6 = Math.max(var6, 16);
+         HashSet var7 = new HashSet(var6);
+         this.mapPlayerPendingEntries.put(var1, var7);
+         return var7;
       }
    }
 }
 */
