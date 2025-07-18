package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetHandlerHandshakeMemory;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkSystem {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final LazyLoadBase<NioEventLoopGroup> SERVER_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>() {
      protected NioEventLoopGroup load() {
         return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build());
      }
   };
   public static final LazyLoadBase<EpollEventLoopGroup> SERVER_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>() {
      protected EpollEventLoopGroup load() {
         return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
      }
   };
   public static final LazyLoadBase<LocalEventLoopGroup> SERVER_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>() {
      protected LocalEventLoopGroup load() {
         return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
      }
   };
   private final MinecraftServer server;
   public volatile boolean isAlive;
   private final List<ChannelFuture> endpoints = Collections.synchronizedList(Lists.newArrayList());
   private final List<NetworkManager> networkManagers = Collections.synchronizedList(Lists.newArrayList());

   public NetworkSystem(MinecraftServer var1) {
      this.server = ☃;
      this.isAlive = true;
   }

   public void addEndpoint(InetAddress var1, int var2) throws IOException {
      synchronized (this.endpoints) {
         Class<? extends ServerSocketChannel> ☃;
         LazyLoadBase<? extends EventLoopGroup> ☃x;
         if (Epoll.isAvailable() && this.server.shouldUseNativeTransport()) {
            ☃ = EpollServerSocketChannel.class;
            ☃x = SERVER_EPOLL_EVENTLOOP;
            LOGGER.info("Using epoll channel type");
         } else {
            ☃ = NioServerSocketChannel.class;
            ☃x = SERVER_NIO_EVENTLOOP;
            LOGGER.info("Using default channel type");
         }

         this.endpoints
            .add(
               ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(☃))
                     .childHandler(
                        new ChannelInitializer<Channel>() {
                           protected void initChannel(Channel var1) throws Exception {
                              try {
                                 ☃.config().setOption(ChannelOption.TCP_NODELAY, true);
                              } catch (ChannelException var3) {
                              }

                              ☃.pipeline()
                                 .addLast("timeout", new ReadTimeoutHandler(30))
                                 .addLast("legacy_query", new LegacyPingHandler(NetworkSystem.this))
                                 .addLast("splitter", new NettyVarint21FrameDecoder())
                                 .addLast("decoder", new NettyPacketDecoder(EnumPacketDirection.SERVERBOUND))
                                 .addLast("prepender", new NettyVarint21FrameEncoder())
                                 .addLast("encoder", new NettyPacketEncoder(EnumPacketDirection.CLIENTBOUND));
                              NetworkManager ☃xx = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                              NetworkSystem.this.networkManagers.add(☃xx);
                              ☃.pipeline().addLast("packet_handler", ☃xx);
                              ☃xx.setNetHandler(new NetHandlerHandshakeTCP(NetworkSystem.this.server, ☃xx));
                           }
                        }
                     )
                     .group(☃x.getValue())
                     .localAddress(☃, ☃))
                  .bind()
                  .syncUninterruptibly()
            );
      }
   }

   public SocketAddress addLocalEndpoint() {
      ChannelFuture ☃;
      synchronized (this.endpoints) {
         ☃ = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(LocalServerChannel.class)).childHandler(new ChannelInitializer<Channel>() {
            protected void initChannel(Channel var1) throws Exception {
               NetworkManager ☃x = new NetworkManager(EnumPacketDirection.SERVERBOUND);
               ☃x.setNetHandler(new NetHandlerHandshakeMemory(NetworkSystem.this.server, ☃x));
               NetworkSystem.this.networkManagers.add(☃x);
               ☃.pipeline().addLast("packet_handler", ☃x);
            }
         }).group((EventLoopGroup)SERVER_NIO_EVENTLOOP.getValue()).localAddress(LocalAddress.ANY)).bind().syncUninterruptibly();
         this.endpoints.add(☃);
      }

      return ☃.channel().localAddress();
   }

   public void terminateEndpoints() {
      this.isAlive = false;

      for (ChannelFuture ☃ : this.endpoints) {
         try {
            ☃.channel().close().sync();
         } catch (InterruptedException var4) {
            LOGGER.error("Interrupted whilst closing channel");
         }
      }
   }

   public void networkTick() {
      synchronized (this.networkManagers) {
         Iterator<NetworkManager> ☃ = this.networkManagers.iterator();

         while (☃.hasNext()) {
            final NetworkManager ☃x = ☃.next();
            if (!☃x.hasNoChannel()) {
               if (☃x.isChannelOpen()) {
                  try {
                     ☃x.processReceivedPackets();
                  } catch (Exception var8) {
                     if (☃x.isLocalChannel()) {
                        CrashReport ☃xx = CrashReport.makeCrashReport(var8, "Ticking memory connection");
                        CrashReportCategory ☃xxx = ☃xx.makeCategory("Ticking connection");
                        ☃xxx.addDetail("Connection", new ICrashReportDetail<String>() {
                           public String call() throws Exception {
                              return ☃.toString();
                           }
                        });
                        throw new ReportedException(☃xx);
                     }

                     LOGGER.warn("Failed to handle packet for {}", ☃x.getRemoteAddress(), var8);
                     final TextComponentString ☃xx = new TextComponentString("Internal server error");
                     ☃x.sendPacket(new SPacketDisconnect(☃xx), new GenericFutureListener<Future<? super Void>>() {
                        public void operationComplete(Future<? super Void> var1) throws Exception {
                           ☃.closeChannel(☃);
                        }
                     });
                     ☃x.disableAutoRead();
                  }
               } else {
                  ☃.remove();
                  ☃x.handleDisconnection();
               }
            }
         }
      }
   }

   public MinecraftServer getServer() {
      return this.server;
   }
}
