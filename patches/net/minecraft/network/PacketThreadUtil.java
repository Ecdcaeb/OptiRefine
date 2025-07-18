package net.minecraft.network;

import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {
   public static int lastDimensionId = Integer.MIN_VALUE;

   public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> packetIn, final T processor, IThreadListener scheduler) throws ThreadQuickExitException {
      if (!scheduler.isCallingFromMinecraftThread()) {
         scheduler.addScheduledTask(new Runnable() {
            @Override
            public void run() {
               PacketThreadUtil.clientPreProcessPacket(packetIn);
               packetIn.processPacket(processor);
            }
         });
         throw ThreadQuickExitException.INSTANCE;
      } else {
         clientPreProcessPacket(packetIn);
      }
   }

   protected static void clientPreProcessPacket(Packet packetIn) {
      if (packetIn instanceof SPacketPlayerPosLook) {
         Config.getRenderGlobal().onPlayerPositionSet();
      }

      if (packetIn instanceof SPacketRespawn) {
         SPacketRespawn respawn = (SPacketRespawn)packetIn;
         lastDimensionId = respawn.getDimensionID();
      } else if (packetIn instanceof SPacketJoinGame) {
         SPacketJoinGame joinGame = (SPacketJoinGame)packetIn;
         lastDimensionId = joinGame.getDimension();
      } else {
         lastDimensionId = Integer.MIN_VALUE;
      }
   }
}
