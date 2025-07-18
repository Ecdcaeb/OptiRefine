package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import net.minecraft.util.CryptManager;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager extends SimpleChannelInboundHandler<Packet<?>> {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final Marker NETWORK_MARKER = MarkerManager.getMarker("NETWORK");
   public static final Marker NETWORK_PACKETS_MARKER = MarkerManager.getMarker("NETWORK_PACKETS", NETWORK_MARKER);
   public static final AttributeKey<EnumConnectionState> PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf("protocol");
   public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>() {
      protected NioEventLoopGroup load() {
         return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
      }
   };
   public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>() {
      protected EpollEventLoopGroup load() {
         return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
      }
   };
   public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>() {
      protected LocalEventLoopGroup load() {
         return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
      }
   };
   private final EnumPacketDirection direction;
   private final Queue<NetworkManager.InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
   private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
   private Channel channel;
   private SocketAddress socketAddress;
   private INetHandler packetListener;
   private ITextComponent terminationReason;
   private boolean isEncrypted;
   private boolean disconnected;

   public NetworkManager(EnumPacketDirection var1) {
      this.direction = ☃;
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
      super.channelActive(☃);
      this.channel = ☃.channel();
      this.socketAddress = this.channel.remoteAddress();

      try {
         this.setConnectionState(EnumConnectionState.HANDSHAKING);
      } catch (Throwable var3) {
         LOGGER.fatal(var3);
      }
   }

   public void setConnectionState(EnumConnectionState var1) {
      this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).set(☃);
      this.channel.config().setAutoRead(true);
      LOGGER.debug("Enabled auto read");
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.closeChannel(new TextComponentTranslation("disconnect.endOfStream"));
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      TextComponentTranslation ☃;
      if (☃ instanceof TimeoutException) {
         ☃ = new TextComponentTranslation("disconnect.timeout");
      } else {
         ☃ = new TextComponentTranslation("disconnect.genericReason", "Internal Exception: " + ☃);
      }

      LOGGER.debug(☃.getUnformattedText(), ☃);
      this.closeChannel(☃);
   }

   protected void channelRead0(ChannelHandlerContext var1, Packet<?> var2) throws Exception {
      if (this.channel.isOpen()) {
         try {
            ((Packet<INetHandler>)☃).processPacket(this.packetListener);
         } catch (ThreadQuickExitException var4) {
         }
      }
   }

   public void setNetHandler(INetHandler var1) {
      Validate.notNull(☃, "packetListener", new Object[0]);
      LOGGER.debug("Set listener of {} to {}", this, ☃);
      this.packetListener = ☃;
   }

   public void sendPacket(Packet<?> var1) {
      if (this.isChannelOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(☃, null);
      } else {
         this.readWriteLock.writeLock().lock();

         try {
            this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(☃));
         } finally {
            this.readWriteLock.writeLock().unlock();
         }
      }
   }

   public void sendPacket(
      Packet<?> var1, GenericFutureListener<? extends Future<? super Void>> var2, GenericFutureListener<? extends Future<? super Void>>... var3
   ) {
      if (this.isChannelOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(☃, (GenericFutureListener<? extends Future<? super Void>>[])ArrayUtils.add(☃, 0, ☃));
      } else {
         this.readWriteLock.writeLock().lock();

         try {
            this.outboundPacketsQueue
               .add(new NetworkManager.InboundHandlerTuplePacketListener(☃, (GenericFutureListener<? extends Future<? super Void>>[])ArrayUtils.add(☃, 0, ☃)));
         } finally {
            this.readWriteLock.writeLock().unlock();
         }
      }
   }

   private void dispatchPacket(final Packet<?> var1, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] var2) {
      final EnumConnectionState ☃ = EnumConnectionState.getFromPacket(☃);
      final EnumConnectionState ☃x = (EnumConnectionState)this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).get();
      if (☃x != ☃) {
         LOGGER.debug("Disabled auto read");
         this.channel.config().setAutoRead(false);
      }

      if (this.channel.eventLoop().inEventLoop()) {
         if (☃ != ☃x) {
            this.setConnectionState(☃);
         }

         ChannelFuture ☃xx = this.channel.writeAndFlush(☃);
         if (☃ != null) {
            ☃xx.addListeners(☃);
         }

         ☃xx.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
      } else {
         this.channel.eventLoop().execute(new Runnable() {
            @Override
            public void run() {
               if (☃ != ☃) {
                  NetworkManager.this.setConnectionState(☃);
               }

               ChannelFuture ☃xx = NetworkManager.this.channel.writeAndFlush(☃);
               if (☃ != null) {
                  ☃xx.addListeners(☃);
               }

               ☃xx.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
         });
      }
   }

   private void flushOutboundQueue() {
      if (this.channel != null && this.channel.isOpen()) {
         this.readWriteLock.readLock().lock();

         try {
            while (!this.outboundPacketsQueue.isEmpty()) {
               NetworkManager.InboundHandlerTuplePacketListener ☃ = this.outboundPacketsQueue.poll();
               this.dispatchPacket(☃.packet, ☃.futureListeners);
            }
         } finally {
            this.readWriteLock.readLock().unlock();
         }
      }
   }

   public void processReceivedPackets() {
      this.flushOutboundQueue();
      if (this.packetListener instanceof ITickable) {
         ((ITickable)this.packetListener).update();
      }

      if (this.channel != null) {
         this.channel.flush();
      }
   }

   public SocketAddress getRemoteAddress() {
      return this.socketAddress;
   }

   public void closeChannel(ITextComponent var1) {
      if (this.channel.isOpen()) {
         this.channel.close().awaitUninterruptibly();
         this.terminationReason = ☃;
      }
   }

   public boolean isLocalChannel() {
      return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
   }

   public static NetworkManager createNetworkManagerAndConnect(InetAddress var0, int var1, boolean var2) {
      final NetworkManager ☃ = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
      Class<? extends SocketChannel> ☃x;
      LazyLoadBase<? extends EventLoopGroup> ☃xx;
      if (Epoll.isAvailable() && ☃) {
         ☃x = EpollSocketChannel.class;
         ☃xx = CLIENT_EPOLL_EVENTLOOP;
      } else {
         ☃x = NioSocketChannel.class;
         ☃xx = CLIENT_NIO_EVENTLOOP;
      }

      ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group(☃xx.getValue()))
               .handler(
                  new ChannelInitializer<Channel>() {
                     protected void initChannel(Channel var1) throws Exception {
                        try {
                           ☃.config().setOption(ChannelOption.TCP_NODELAY, true);
                        } catch (ChannelException var3x) {
                        }

                        ☃.pipeline()
                           .addLast("timeout", new ReadTimeoutHandler(30))
                           .addLast("splitter", new NettyVarint21FrameDecoder())
                           .addLast("decoder", new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND))
                           .addLast("prepender", new NettyVarint21FrameEncoder())
                           .addLast("encoder", new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND))
                           .addLast("packet_handler", ☃);
                     }
                  }
               ))
            .channel(☃x))
         .connect(☃, ☃)
         .syncUninterruptibly();
      return ☃;
   }

   public static NetworkManager provideLocalClient(SocketAddress var0) {
      final NetworkManager ☃ = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
      ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)CLIENT_LOCAL_EVENTLOOP.getValue())).handler(new ChannelInitializer<Channel>() {
         protected void initChannel(Channel var1x) throws Exception {
            ☃.pipeline().addLast("packet_handler", ☃);
         }
      })).channel(LocalChannel.class)).connect(☃).syncUninterruptibly();
      return ☃;
   }

   public void enableEncryption(SecretKey var1) {
      this.isEncrypted = true;
      this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, ☃)));
      this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, ☃)));
   }

   public boolean isEncrypted() {
      return this.isEncrypted;
   }

   public boolean isChannelOpen() {
      return this.channel != null && this.channel.isOpen();
   }

   public boolean hasNoChannel() {
      return this.channel == null;
   }

   public INetHandler getNetHandler() {
      return this.packetListener;
   }

   public ITextComponent getExitMessage() {
      return this.terminationReason;
   }

   public void disableAutoRead() {
      this.channel.config().setAutoRead(false);
   }

   public void setCompressionThreshold(int var1) {
      if (☃ >= 0) {
         if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionThreshold(☃);
         } else {
            this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(☃));
         }

         if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            ((NettyCompressionEncoder)this.channel.pipeline().get("compress")).setCompressionThreshold(☃);
         } else {
            this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(☃));
         }
      } else {
         if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            this.channel.pipeline().remove("decompress");
         }

         if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            this.channel.pipeline().remove("compress");
         }
      }
   }

   public void handleDisconnection() {
      if (this.channel != null && !this.channel.isOpen()) {
         if (this.disconnected) {
            LOGGER.warn("handleDisconnection() called twice");
         } else {
            this.disconnected = true;
            if (this.getExitMessage() != null) {
               this.getNetHandler().onDisconnect(this.getExitMessage());
            } else if (this.getNetHandler() != null) {
               this.getNetHandler().onDisconnect(new TextComponentTranslation("multiplayer.disconnect.generic"));
            }
         }
      }
   }

   static class InboundHandlerTuplePacketListener {
      private final Packet<?> packet;
      private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;

      public InboundHandlerTuplePacketListener(Packet<?> var1, GenericFutureListener<? extends Future<? super Void>>... var2) {
         this.packet = ☃;
         this.futureListeners = ☃;
      }
   }
}
