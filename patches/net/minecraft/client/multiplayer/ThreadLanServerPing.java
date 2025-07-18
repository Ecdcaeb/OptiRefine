package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadLanServerPing extends Thread {
   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
   private static final Logger LOGGER = LogManager.getLogger();
   private final String motd;
   private final DatagramSocket socket;
   private boolean isStopping = true;
   private final String address;

   public ThreadLanServerPing(String var1, String var2) throws IOException {
      super("LanServerPinger #" + UNIQUE_THREAD_ID.incrementAndGet());
      this.motd = ☃;
      this.address = ☃;
      this.setDaemon(true);
      this.socket = new DatagramSocket();
   }

   @Override
   public void run() {
      String ☃ = getPingResponse(this.motd, this.address);
      byte[] ☃x = ☃.getBytes(StandardCharsets.UTF_8);

      while (!this.isInterrupted() && this.isStopping) {
         try {
            InetAddress ☃xx = InetAddress.getByName("224.0.2.60");
            DatagramPacket ☃xxx = new DatagramPacket(☃x, ☃x.length, ☃xx, 4445);
            this.socket.send(☃xxx);
         } catch (IOException var6) {
            LOGGER.warn("LanServerPinger: {}", var6.getMessage());
            break;
         }

         try {
            sleep(1500L);
         } catch (InterruptedException var5) {
         }
      }
   }

   @Override
   public void interrupt() {
      super.interrupt();
      this.isStopping = false;
   }

   public static String getPingResponse(String var0, String var1) {
      return "[MOTD]" + ☃ + "[/MOTD][AD]" + ☃ + "[/AD]";
   }

   public static String getMotdFromPingResponse(String var0) {
      int ☃ = ☃.indexOf("[MOTD]");
      if (☃ < 0) {
         return "missing no";
      } else {
         int ☃x = ☃.indexOf("[/MOTD]", ☃ + "[MOTD]".length());
         return ☃x < ☃ ? "missing no" : ☃.substring(☃ + "[MOTD]".length(), ☃x);
      }
   }

   public static String getAdFromPingResponse(String var0) {
      int ☃ = ☃.indexOf("[/MOTD]");
      if (☃ < 0) {
         return null;
      } else {
         int ☃x = ☃.indexOf("[/MOTD]", ☃ + "[/MOTD]".length());
         if (☃x >= 0) {
            return null;
         } else {
            int ☃xx = ☃.indexOf("[AD]", ☃ + "[/MOTD]".length());
            if (☃xx < 0) {
               return null;
            } else {
               int ☃xxx = ☃.indexOf("[/AD]", ☃xx + "[AD]".length());
               return ☃xxx < ☃xx ? null : ☃.substring(☃xx + "[AD]".length(), ☃xxx);
            }
         }
      }
   }
}
