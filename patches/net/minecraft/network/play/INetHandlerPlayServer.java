package net.minecraft.network.play;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;

public interface INetHandlerPlayServer extends INetHandler {
   void handleAnimation(CPacketAnimation var1);

   void processChatMessage(CPacketChatMessage var1);

   void processTabComplete(CPacketTabComplete var1);

   void processClientStatus(CPacketClientStatus var1);

   void processClientSettings(CPacketClientSettings var1);

   void processConfirmTransaction(CPacketConfirmTransaction var1);

   void processEnchantItem(CPacketEnchantItem var1);

   void processClickWindow(CPacketClickWindow var1);

   void func_194308_a(CPacketPlaceRecipe var1);

   void processCloseWindow(CPacketCloseWindow var1);

   void processCustomPayload(CPacketCustomPayload var1);

   void processUseEntity(CPacketUseEntity var1);

   void processKeepAlive(CPacketKeepAlive var1);

   void processPlayer(CPacketPlayer var1);

   void processPlayerAbilities(CPacketPlayerAbilities var1);

   void processPlayerDigging(CPacketPlayerDigging var1);

   void processEntityAction(CPacketEntityAction var1);

   void processInput(CPacketInput var1);

   void processHeldItemChange(CPacketHeldItemChange var1);

   void processCreativeInventoryAction(CPacketCreativeInventoryAction var1);

   void processUpdateSign(CPacketUpdateSign var1);

   void processTryUseItemOnBlock(CPacketPlayerTryUseItemOnBlock var1);

   void processTryUseItem(CPacketPlayerTryUseItem var1);

   void handleSpectate(CPacketSpectate var1);

   void handleResourcePackStatus(CPacketResourcePackStatus var1);

   void processSteerBoat(CPacketSteerBoat var1);

   void processVehicleMove(CPacketVehicleMove var1);

   void processConfirmTeleport(CPacketConfirmTeleport var1);

   void handleRecipeBookUpdate(CPacketRecipeInfo var1);

   void handleSeenAdvancements(CPacketSeenAdvancements var1);
}
