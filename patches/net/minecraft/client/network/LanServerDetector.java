package net.minecraft.client.network;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanServerDetector {
   private static final AtomicInteger ATOMIC_COUNTER = new AtomicInteger(0);
   private static final Logger LOGGER = LogManager.getLogger();

   public static class LanServerList {
      private final List<LanServerInfo> listOfLanServers = Lists.newArrayList();
      boolean wasUpdated;

      public synchronized boolean getWasUpdated() {
         return this.wasUpdated;
      }

      public synchronized void setWasNotUpdated() {
         this.wasUpdated = false;
      }

      public synchronized List<LanServerInfo> getLanServers() {
         return Collections.unmodifiableList(this.listOfLanServers);
      }

      public synchronized void addServer(String var1, InetAddress var2) {
         String ☃ = ThreadLanServerPing.getMotdFromPingResponse(☃);
         String ☃x = ThreadLanServerPing.getAdFromPingResponse(☃);
         if (☃x != null) {
            ☃x = ☃.getHostAddress() + ":" + ☃x;
            boolean ☃xx = false;

            for (LanServerInfo ☃xxx : this.listOfLanServers) {
               if (☃xxx.getServerIpPort().equals(☃x)) {
                  ☃xxx.updateLastSeen();
                  ☃xx = true;
                  break;
               }
            }

            if (!☃xx) {
               this.listOfLanServers.add(new LanServerInfo(☃, ☃x));
               this.wasUpdated = true;
            }
         }
      }
   }

   public static class ThreadLanServerFind extends Thread {
      private final LanServerDetector.LanServerList localServerList;
      private final InetAddress broadcastAddress;
      private final MulticastSocket socket;

      public ThreadLanServerFind(LanServerDetector.LanServerList var1) throws IOException {
         super("LanServerDetector #" + LanServerDetector.ATOMIC_COUNTER.incrementAndGet());
         this.localServerList = ☃;
         this.setDaemon(true);
         this.socket = new MulticastSocket(4445);
         this.broadcastAddress = InetAddress.getByName("224.0.2.60");
         this.socket.setSoTimeout(5000);
         this.socket.joinGroup(this.broadcastAddress);
      }

      @Override
      public void run() {
         byte[] ☃ = new byte[1024];

         while (!this.isInterrupted()) {
            DatagramPacket ☃x = new DatagramPacket(☃, ☃.length);

            try {
               this.socket.receive(☃x);
            } catch (SocketTimeoutException var5) {
               continue;
            } catch (IOException var6) {
               LanServerDetector.LOGGER.error("Couldn't ping server", var6);
               break;
            }

            String ☃xx = new String(☃x.getData(), ☃x.getOffset(), ☃x.getLength(), StandardCharsets.UTF_8);
            LanServerDetector.LOGGER.debug("{}: {}", ☃x.getAddress(), ☃xx);
            this.localServerList.addServer(☃xx, ☃x.getAddress());
         }

         try {
            this.socket.leaveGroup(this.broadcastAddress);
         } catch (IOException var4) {
         }

         this.socket.close();
      }
   }
}
