package net.minecraft.network;

import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {
   public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> var0, final T var1, IThreadListener var2) throws ThreadQuickExitException {
      if (!☃.isCallingFromMinecraftThread()) {
         ☃.addScheduledTask(new Runnable() {
            @Override
            public void run() {
               ☃.processPacket(☃);
            }
         });
         throw ThreadQuickExitException.INSTANCE;
      }
   }
}
