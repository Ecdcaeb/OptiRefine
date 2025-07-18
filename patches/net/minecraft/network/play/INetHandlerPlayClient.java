package net.minecraft.network.play;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;

public interface INetHandlerPlayClient extends INetHandler {
   void handleSpawnObject(SPacketSpawnObject var1);

   void handleSpawnExperienceOrb(SPacketSpawnExperienceOrb var1);

   void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity var1);

   void handleSpawnMob(SPacketSpawnMob var1);

   void handleScoreboardObjective(SPacketScoreboardObjective var1);

   void handleSpawnPainting(SPacketSpawnPainting var1);

   void handleSpawnPlayer(SPacketSpawnPlayer var1);

   void handleAnimation(SPacketAnimation var1);

   void handleStatistics(SPacketStatistics var1);

   void handleRecipeBook(SPacketRecipeBook var1);

   void handleBlockBreakAnim(SPacketBlockBreakAnim var1);

   void handleSignEditorOpen(SPacketSignEditorOpen var1);

   void handleUpdateTileEntity(SPacketUpdateTileEntity var1);

   void handleBlockAction(SPacketBlockAction var1);

   void handleBlockChange(SPacketBlockChange var1);

   void handleChat(SPacketChat var1);

   void handleTabComplete(SPacketTabComplete var1);

   void handleMultiBlockChange(SPacketMultiBlockChange var1);

   void handleMaps(SPacketMaps var1);

   void handleConfirmTransaction(SPacketConfirmTransaction var1);

   void handleCloseWindow(SPacketCloseWindow var1);

   void handleWindowItems(SPacketWindowItems var1);

   void handleOpenWindow(SPacketOpenWindow var1);

   void handleWindowProperty(SPacketWindowProperty var1);

   void handleSetSlot(SPacketSetSlot var1);

   void handleCustomPayload(SPacketCustomPayload var1);

   void handleDisconnect(SPacketDisconnect var1);

   void handleUseBed(SPacketUseBed var1);

   void handleEntityStatus(SPacketEntityStatus var1);

   void handleEntityAttach(SPacketEntityAttach var1);

   void handleSetPassengers(SPacketSetPassengers var1);

   void handleExplosion(SPacketExplosion var1);

   void handleChangeGameState(SPacketChangeGameState var1);

   void handleKeepAlive(SPacketKeepAlive var1);

   void handleChunkData(SPacketChunkData var1);

   void processChunkUnload(SPacketUnloadChunk var1);

   void handleEffect(SPacketEffect var1);

   void handleJoinGame(SPacketJoinGame var1);

   void handleEntityMovement(SPacketEntity var1);

   void handlePlayerPosLook(SPacketPlayerPosLook var1);

   void handleParticles(SPacketParticles var1);

   void handlePlayerAbilities(SPacketPlayerAbilities var1);

   void handlePlayerListItem(SPacketPlayerListItem var1);

   void handleDestroyEntities(SPacketDestroyEntities var1);

   void handleRemoveEntityEffect(SPacketRemoveEntityEffect var1);

   void handleRespawn(SPacketRespawn var1);

   void handleEntityHeadLook(SPacketEntityHeadLook var1);

   void handleHeldItemChange(SPacketHeldItemChange var1);

   void handleDisplayObjective(SPacketDisplayObjective var1);

   void handleEntityMetadata(SPacketEntityMetadata var1);

   void handleEntityVelocity(SPacketEntityVelocity var1);

   void handleEntityEquipment(SPacketEntityEquipment var1);

   void handleSetExperience(SPacketSetExperience var1);

   void handleUpdateHealth(SPacketUpdateHealth var1);

   void handleTeams(SPacketTeams var1);

   void handleUpdateScore(SPacketUpdateScore var1);

   void handleSpawnPosition(SPacketSpawnPosition var1);

   void handleTimeUpdate(SPacketTimeUpdate var1);

   void handleSoundEffect(SPacketSoundEffect var1);

   void handleCustomSound(SPacketCustomSound var1);

   void handleCollectItem(SPacketCollectItem var1);

   void handleEntityTeleport(SPacketEntityTeleport var1);

   void handleEntityProperties(SPacketEntityProperties var1);

   void handleEntityEffect(SPacketEntityEffect var1);

   void handleCombatEvent(SPacketCombatEvent var1);

   void handleServerDifficulty(SPacketServerDifficulty var1);

   void handleCamera(SPacketCamera var1);

   void handleWorldBorder(SPacketWorldBorder var1);

   void handleTitle(SPacketTitle var1);

   void handlePlayerListHeaderFooter(SPacketPlayerListHeaderFooter var1);

   void handleResourcePack(SPacketResourcePackSend var1);

   void handleUpdateBossInfo(SPacketUpdateBossInfo var1);

   void handleCooldown(SPacketCooldown var1);

   void handleMoveVehicle(SPacketMoveVehicle var1);

   void handleAdvancementInfo(SPacketAdvancementInfo var1);

   void handleSelectAdvancementsTab(SPacketSelectAdvancementsTab var1);

   void func_194307_a(SPacketPlaceGhostRecipe var1);
}
