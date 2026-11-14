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
import net.minecraft.util.math.MathHelper;
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
    @Unique
    // [AUDIT-FIXED] three-way audit (P25): cleanmix does not inject @Unique instance-field
    // initializers -> init moved to <init>* RETURN; dropped final to allow assignment there.
    private Map<EntityPlayerMP, Set<ChunkPos>> mapPlayerPendingEntries;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void optiRefine$initializePendingChunkEntries(CallbackInfo ci) {
        this.mapPlayerPendingEntries = new HashMap<>();
    }

// [AUDIT-OK] baseline member getOrCreateEntry(II) exists in target class (odd "native" modifier on shadow is inert)
    @Shadow public native PlayerChunkMapEntry getOrCreateEntry(int chunkX, int chunkZ) ;
    @Shadow public native PlayerChunkMapEntry getEntry(int chunkX, int chunkZ) ;


    @Inject(method = "tick", at = @At("HEAD"))
// [AUDIT-OK] tick HEAD pending-chunk loop matches OF tick start (getNearest + getOrCreateEntry/containsPlayer/addPlayer)
    public void optiRefine$loadPendingChunks(CallbackInfo ci){
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

    @Inject(method = "removePlayer", at = @At("HEAD"))
// [AUDIT-FIXED] OF:266-267 removes the player's pending lazy-chunk entries before the vanilla body runs
    public void optiRefine$discardPlayerPendingChunks(EntityPlayerMP player, CallbackInfo ci) {
        this.mapPlayerPendingEntries.remove(player);
    }





    @Inject(method = "addPlayer", at = @At("HEAD"))
// [AUDIT-OK] addPlayer HEAD: load radius bounds (min(viewRadius,8)) + pending set share match OF addPlayer
    public void optiRefine$prepareInitialChunkLoad(EntityPlayerMP player, CallbackInfo ci,
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
    public PlayerChunkMapEntry optiRefine$deferDistantInitialChunk(PlayerChunkMap chunkMap, int chunkX, int chunkZ,
                                                     @Share("kMin")LocalIntRef kMin,
                                                     @Share("kMax")LocalIntRef kMax,
                                                     @Share("lMin")LocalIntRef lMin,
                                                     @Share("lMax")LocalIntRef lMax,
                                                     @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries
    ){
        if (chunkX < kMin.get() || chunkX > kMax.get() || chunkZ < lMin.get() || chunkZ > lMax.get()) {
            setPendingEntries.get().add(new ChunkPos(chunkX, chunkZ));
            return null;
        }
        else return this.getOrCreateEntry(chunkX, chunkZ);
    }

    @Redirect(method = "addPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;addPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V"))
// [AUDIT-OK] null-safe addPlayer matches OF lazy path
    public void optiRefine$addPlayerToInitialChunk(PlayerChunkMapEntry entry, EntityPlayerMP player){
        if (entry != null) entry.addPlayer(player);
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
    public void optiRefine$prepareMovingPlayerPendingChunks(EntityPlayerMP player, CallbackInfo ci, @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries){
        setPendingEntries.set(this.getPendingEntriesSafe(player));
    }

    @Redirect(method = "updateMovingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getOrCreateEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;"))
// [AUDIT-OK] non-lazy branch returns this.getOrCreateEntry (entry created, OF/vanilla semantics; verified 2026-08-05)
    public PlayerChunkMapEntry optiRefine$deferMovingPlayerChunk(PlayerChunkMap chunkMap, int chunkXPosition, int chunkZPosition, @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries){
        if (Config.isLazyChunkLoading()) {
            setPendingEntries.get().add(new ChunkPos(chunkXPosition, chunkZPosition));
            return null;
        } else return this.getOrCreateEntry(chunkXPosition, chunkZPosition);
    }

    @Redirect(method = "updateMovingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;addPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V"))
// [AUDIT-OK] null-safe addPlayer matches OF lazy path
    public void optiRefine$addMovingPlayerToLoadedChunk(PlayerChunkMapEntry entry, EntityPlayerMP player){
        if (entry != null) entry.addPlayer(player);
    }

    @Redirect(method = "updateMovingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;"))
// [AUDIT-OK] getEntry redirect does pending-set removal before getEntry, matching OF updateMovingPlayer second branch
    public PlayerChunkMapEntry optiRefine$removeMovingPlayerPendingChunk(PlayerChunkMap chunkMap, int chunkXPosition, int chunkZPosition, @Share("setPendingEntries")LocalRef<Set<ChunkPos>> setPendingEntries){
        // [AUDIT-FIXED] three-way audit (P25): OF:358-363 removes then getEntry (no creation);
        // getOrCreateEntry leaked an empty entry which tick would then provide chunks for.
        setPendingEntries.get().remove(new ChunkPos(chunkXPosition, chunkZPosition));
        return this.getEntry(chunkXPosition, chunkZPosition);
    }


    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    // [AUDIT-FIXED] three-way audit (P25): OF:382 clamps view radius to (3,64); baseline caps at 32.
    public int optiRefine$clampExtendedViewRadius(int value, int min, int max) {
        return MathHelper.clamp(value, 3, 64);
    }

    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getOrCreateEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;", ordinal = 0))
    // Only defer additions; shrinking must still remove players from existing entries.
    public PlayerChunkMapEntry optiRefine$deferExpandedRadiusChunk(PlayerChunkMap chunkMap, int chunkXPosition, int chunkZPosition,
           @Share(value = "chunkX", namespace = "optirefine") LocalIntRef chunkX,
           @Share(value = "chunkZ", namespace = "optirefine") LocalIntRef chunkZ
    ){
        if (Config.isLazyChunkLoading()) {
            chunkX.set(chunkXPosition);
            chunkZ.set(chunkZPosition);
            return null;
        } else return this.getOrCreateEntry(chunkXPosition, chunkZPosition);
    }

    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;addPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V"))
    // [AUDIT-FIXED] three-way audit (P25): lazy mode redirects getOrCreateEntry to null; the baseline
    // body then runs entry.addPlayer(player) unconditionally -> NPE. OF's lazy branch (OF:397-407)
    // never touches the entry; guard null here.
    public void optiRefine$addPlayerToExpandedRadiusChunk(PlayerChunkMapEntry entry, EntityPlayerMP player){
        if (entry != null) {
            entry.addPlayer(player);
        }
    }

    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;containsPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)Z"))
// [AUDIT-OK] null-entry -> pending set + false, matches OF lazy path
    public boolean optiRefine$trackExpandedRadiusPendingChunk(PlayerChunkMapEntry entry, EntityPlayerMP player,
                                          @Share(value = "chunkX", namespace = "optirefine") LocalIntRef chunkX,
                                          @Share(value = "chunkZ", namespace = "optirefine") LocalIntRef chunkZ
    ){
        if (entry == null) {
            this.getPendingEntriesSafe(player).add(new ChunkPos(chunkX.get(), chunkZ.get()));
            return false;
        } else return entry.containsPlayer(player);
    }

    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMap;getOrCreateEntry(II)Lnet/minecraft/server/management/PlayerChunkMapEntry;", ordinal = 1))
    private PlayerChunkMapEntry optiRefine$getEntryForRadiusShrink(PlayerChunkMap chunkMap, int chunkX, int chunkZ, @Local EntityPlayerMP player) {
        this.getPendingEntriesSafe(player).remove(new ChunkPos(chunkX, chunkZ));
        return this.getEntry(chunkX, chunkZ);
    }

    @Redirect(method = "setPlayerViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerChunkMapEntry;removePlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V"))
    public void optiRefine$removePlayerFromShrunkRadiusChunk(PlayerChunkMapEntry entry, EntityPlayerMP player) {
        if (entry != null) {
            entry.removePlayer(player);
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
