package net.minecraft.client.network;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerPinger {
   private static final Splitter PING_RESPONSE_SPLITTER = Splitter.on('\u0000').limit(6);
   private static final Logger LOGGER = LogManager.getLogger();
   private final List<NetworkManager> pingDestinations = Collections.synchronizedList(Lists.newArrayList());

   public void ping(final ServerData var1) throws UnknownHostException {
      ServerAddress ☃ = ServerAddress.fromString(☃.serverIP);
      final NetworkManager ☃x = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(☃.getIP()), ☃.getPort(), false);
      this.pingDestinations.add(☃x);
      ☃.serverMOTD = I18n.format("multiplayer.status.pinging");
      ☃.pingToServer = -1L;
      ☃.playerList = null;
      ☃x.setNetHandler(
         new INetHandlerStatusClient() {
            private boolean successful;
            private boolean receivedStatus;
            private long pingSentAt;

            @Override
            public void handleServerInfo(SPacketServerInfo var1x) {
               if (this.receivedStatus) {
                  ☃.closeChannel(new TextComponentTranslation("multiplayer.status.unrequested"));
               } else {
                  this.receivedStatus = true;
                  ServerStatusResponse ☃xx = ☃.getResponse();
                  if (☃xx.getServerDescription() != null) {
                     ☃.serverMOTD = ☃xx.getServerDescription().getFormattedText();
                  } else {
                     ☃.serverMOTD = "";
                  }

                  if (☃xx.getVersion() != null) {
                     ☃.gameVersion = ☃xx.getVersion().getName();
                     ☃.version = ☃xx.getVersion().getProtocol();
                  } else {
                     ☃.gameVersion = I18n.format("multiplayer.status.old");
                     ☃.version = 0;
                  }

                  if (☃xx.getPlayers() != null) {
                     ☃.populationInfo = TextFormatting.GRAY
                        + ""
                        + ☃xx.getPlayers().getOnlinePlayerCount()
                        + ""
                        + TextFormatting.DARK_GRAY
                        + "/"
                        + TextFormatting.GRAY
                        + ☃xx.getPlayers().getMaxPlayers();
                     if (ArrayUtils.isNotEmpty(☃xx.getPlayers().getPlayers())) {
                        StringBuilder ☃x = new StringBuilder();

                        for (GameProfile ☃xx : ☃xx.getPlayers().getPlayers()) {
                           if (☃x.length() > 0) {
                              ☃x.append("\n");
                           }

                           ☃x.append(☃xx.getName());
                        }

                        if (☃xx.getPlayers().getPlayers().length < ☃xx.getPlayers().getOnlinePlayerCount()) {
                           if (☃x.length() > 0) {
                              ☃x.append("\n");
                           }

                           ☃x.append(I18n.format("multiplayer.status.and_more", ☃xx.getPlayers().getOnlinePlayerCount() - ☃xx.getPlayers().getPlayers().length));
                        }

                        ☃.playerList = ☃x.toString();
                     }
                  } else {
                     ☃.populationInfo = TextFormatting.DARK_GRAY + I18n.format("multiplayer.status.unknown");
                  }

                  if (☃xx.getFavicon() != null) {
                     String ☃x = ☃xx.getFavicon();
                     if (☃x.startsWith("data:image/png;base64,")) {
                        ☃.setBase64EncodedIconData(☃x.substring("data:image/png;base64,".length()));
                     } else {
                        ServerPinger.LOGGER.error("Invalid server icon (unknown format)");
                     }
                  } else {
                     ☃.setBase64EncodedIconData(null);
                  }

                  this.pingSentAt = Minecraft.getSystemTime();
                  ☃.sendPacket(new CPacketPing(this.pingSentAt));
                  this.successful = true;
               }
            }

            @Override
            public void handlePong(SPacketPong var1x) {
               long ☃xx = this.pingSentAt;
               long ☃x = Minecraft.getSystemTime();
               ☃.pingToServer = ☃x - ☃xx;
               ☃.closeChannel(new TextComponentString("Finished"));
            }

            @Override
            public void onDisconnect(ITextComponent var1x) {
               if (!this.successful) {
                  ServerPinger.LOGGER.error("Can't ping {}: {}", ☃.serverIP, ☃.getUnformattedText());
                  ☃.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_connect");
                  ☃.populationInfo = "";
                  ServerPinger.this.tryCompatibilityPing(☃);
               }
            }
         }
      );

      try {
         ☃x.sendPacket(new C00Handshake(☃.getIP(), ☃.getPort(), EnumConnectionState.STATUS));
         ☃x.sendPacket(new CPacketServerQuery());
      } catch (Throwable var5) {
         LOGGER.error(var5);
      }
   }

   private void tryCompatibilityPing(final ServerData var1) {
      final ServerAddress ☃ = ServerAddress.fromString(☃.serverIP);
      ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)NetworkManager.CLIENT_NIO_EVENTLOOP.getValue()))
               .handler(new ChannelInitializer<Channel>() {
                  protected void initChannel(Channel var1x) throws Exception {
                     try {
                        ☃.config().setOption(ChannelOption.TCP_NODELAY, true);
                     } catch (ChannelException var3) {
                     }

                     ☃.pipeline().addLast(new ChannelHandler[]{new SimpleChannelInboundHandler<ByteBuf>() {
                        public void channelActive(ChannelHandlerContext var1x) throws Exception {
                           super.channelActive(☃);
                           ByteBuf ☃x = Unpooled.buffer();

                           try {
                              ☃x.writeByte(254);
                              ☃x.writeByte(1);
                              ☃x.writeByte(250);
                              char[] ☃x = "MC|PingHost".toCharArray();
                              ☃x.writeShort(☃x.length);

                              for (char ☃xx : ☃x) {
                                 ☃x.writeChar(☃xx);
                              }

                              ☃x.writeShort(7 + 2 * ☃.getIP().length());
                              ☃x.writeByte(127);
                              ☃x = ☃.getIP().toCharArray();
                              ☃x.writeShort(☃x.length);

                              for (char ☃xx : ☃x) {
                                 ☃x.writeChar(☃xx);
                              }

                              ☃x.writeInt(☃.getPort());
                              ☃.channel().writeAndFlush(☃x).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                           } finally {
                              ☃x.release();
                           }
                        }

                        protected void channelRead0(ChannelHandlerContext var1x, ByteBuf var2x) throws Exception {
                           short ☃x = ☃.readUnsignedByte();
                           if (☃x == 255) {
                              String ☃x = new String(☃.readBytes(☃.readShort() * 2).array(), StandardCharsets.UTF_16BE);
                              String[] ☃xx = (String[])Iterables.toArray(ServerPinger.PING_RESPONSE_SPLITTER.split(☃x), String.class);
                              if ("§1".equals(☃xx[0])) {
                                 int ☃xxx = MathHelper.getInt(☃xx[1], 0);
                                 String ☃xxxx = ☃xx[2];
                                 String ☃xxxxx = ☃xx[3];
                                 int ☃xxxxxx = MathHelper.getInt(☃xx[4], -1);
                                 int ☃xxxxxxx = MathHelper.getInt(☃xx[5], -1);
                                 ☃.version = -1;
                                 ☃.gameVersion = ☃xxxx;
                                 ☃.serverMOTD = ☃xxxxx;
                                 ☃.populationInfo = TextFormatting.GRAY + "" + ☃xxxxxx + "" + TextFormatting.DARK_GRAY + "/" + TextFormatting.GRAY + ☃xxxxxxx;
                              }
                           }

                           ☃.close();
                        }

                        public void exceptionCaught(ChannelHandlerContext var1x, Throwable var2x) throws Exception {
                           ☃.close();
                        }
                     }});
                  }
               }))
            .channel(NioSocketChannel.class))
         .connect(☃.getIP(), ☃.getPort());
   }

   public void pingPendingNetworks() {
      synchronized (this.pingDestinations) {
         Iterator<NetworkManager> ☃ = this.pingDestinations.iterator();

         while (☃.hasNext()) {
            NetworkManager ☃x = ☃.next();
            if (☃x.isChannelOpen()) {
               ☃x.processReceivedPackets();
            } else {
               ☃.remove();
               ☃x.handleDisconnection();
            }
         }
      }
   }

   public void clearPendingNetworks() {
      synchronized (this.pingDestinations) {
         Iterator<NetworkManager> ☃ = this.pingDestinations.iterator();

         while (☃.hasNext()) {
            NetworkManager ☃x = ☃.next();
            if (☃x.isChannelOpen()) {
               ☃.remove();
               ☃x.closeChannel(new TextComponentTranslation("multiplayer.status.cancelled"));
            }
         }
      }
   }
}
