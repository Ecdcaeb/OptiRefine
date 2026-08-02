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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 3


    @Shadow @Final
// [AUDIT-OK] baseline member world exists in target class
    private WorldServer world;

    @Shadow
// [AUDIT-OK] baseline member playerViewRadius exists in target class
    private int playerViewRadius;

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field mapPlayerPendingEntries (in OF, not in baseline), MCP name matches
    private final Map<EntityPlayerMP, Set<ChunkPos>> mapPlayerPendingEntries = new HashMap<>();

// [AUDIT-OK] baseline member getOrCreateEntry(II) exists in target class (odd "native" modifier on shadow is inert)
    @Shadow public native PlayerChunkMapEntry getOrCreateEntry(int chunkX, int chunkZ) ;


    @Inject(method = "tick", at = @At("HEAD"))
// [AUDIT-OK] tick HEAD pending-chunk loop matches OF tick start (getNearest + getOrCreateEntry/containsPlayer/addPlayer)
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
// [AUDIT-ISSUE] expression change is a behavioral no-op: baseline AND OF tick already guard the unload with !provider.canRespawnHere() inside the players.isEmpty() block; outer AND just duplicates the inner check
    public boolean changeUnloadLogic(boolean original){
        return original && !this.world.provider.canRespawnHere();
    }



    @Inject(method = "addPlayer", at = @At("HEAD"))
// [AUDIT-OK] addPlayer HEAD: load radius bounds (min(viewRadius,8)) + pending set share match OF addPlayer
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
// [AUDIT-OK] addPlayer getOrCreateEntry redirect matches OF (inside radius -> create+add, outside -> pending set)
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
// [AUDIT-OK] null-safe addPlayer matches OF lazy path
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
// [AUDIT-OK] updateMovingPlayer HEAD: pending-set share matches OF lazy handling
    public void atHeadOfUpdateMovingPlayer(EntityPlayerMP p_72685_1_, CallbackInfo ci, @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries){
        setPendingEntries.set(this.getPendingEntriesSafe(p_72685_1_));
    }

    @Redirect(method = "updateMovingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getOrCreateEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;"))
// [AUDIT-ISSUE] non-lazy branch returns instance.getEntry(...) instead of getOrCreateEntry(...) (OF/vanilla create the entry) - chunks at the leading edge of player movement are silently dropped in non-lazy mode; the comment block above even documents getOrCreateEntry. Fix: call getOrCreateEntry.
    public PlayerChunkMapEntry wrap_getOrCreateEntry1(PlayerChunkMap instance, int p_187302_1_, int p_187302_2_, @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries){
        if (Config.isLazyChunkLoading()) {
            setPendingEntries.get().add(new ChunkPos(p_187302_1_, p_187302_2_));
            return null;
        } else return this.getOrCreateEntry(p_187302_1_, p_187302_2_);
    }

    @Redirect(method = "updateMovingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;addPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V"))
// [AUDIT-OK] null-safe addPlayer matches OF lazy path
    public void wrap_addPlayer1(PlayerChunkMapEntry instance, EntityPlayerMP p_187276_1_){
        if (instance != null) instance.addPlayer(p_187276_1_);
    }

    @Redirect(method = "updateMovingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;"))
// [AUDIT-OK] getEntry redirect does pending-set removal before getEntry, matching OF updateMovingPlayer second branch
    public PlayerChunkMapEntry removeEntry(PlayerChunkMap instance, int p_187301_1_, int p_187301_2_, @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries){
        setPendingEntries.get().remove(new ChunkPos(p_187301_1_, p_187301_2_));
        return this.getOrCreateEntry(p_187301_1_, p_187301_2_);
    }


    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getOrCreateEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;"))
// [AUDIT-ISSUE] non-lazy branch returns instance.getEntry(...) instead of getOrCreateEntry(...) - on view-radius increase new-ring chunks often have no entry yet; the following containsPlayer redirect then adds to pending and returns false, and entry.addPlayer(player) runs on null -> NPE in non-lazy mode. Fix: return getOrCreateEntry.
    public PlayerChunkMapEntry wrap_getOrCreateEntry2(PlayerChunkMap instance, int p_187302_1_, int p_187302_2_,
           @Share(value = "chunkX", namespace = "optirefine") LocalIntRef chunkX,
           @Share(value = "chunkZ", namespace = "optirefine") LocalIntRef chunkZ
    ){
        if (Config.isLazyChunkLoading()) {
            chunkX.set(p_187302_1_);
            chunkZ.set(p_187302_2_);
            return null;
        } else return this.getOrCreateEntry(p_187302_1_, p_187302_2_);
    }

    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;containsPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)Z"))
// [AUDIT-OK] null-entry -> pending set + false, matches OF lazy path
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
// [AUDIT-OK] null-entry -> pending remove, matches OF setPlayerViewRadius shrink path
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
// [AUDIT-OK] OF-added private method getNearest, body matches OF (ChunkPosComparator, reverse-order priority queue)
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
// [AUDIT-OK] OF-added private method getPendingEntriesSafe, body matches OF
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
