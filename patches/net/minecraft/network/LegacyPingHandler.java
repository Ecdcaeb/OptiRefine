package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LegacyPingHandler extends ChannelInboundHandlerAdapter {
   private static final Logger LOGGER = LogManager.getLogger();
   private final NetworkSystem networkSystem;

   public LegacyPingHandler(NetworkSystem var1) {
      this.networkSystem = ☃;
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      ByteBuf ☃ = (ByteBuf)☃;
      ☃.markReaderIndex();
      boolean ☃x = true;

      try {
         try {
            if (☃.readUnsignedByte() != 254) {
               return;
            }

            InetSocketAddress ☃xx = (InetSocketAddress)☃.channel().remoteAddress();
            MinecraftServer ☃xxx = this.networkSystem.getServer();
            int ☃xxxx = ☃.readableBytes();
            switch (☃xxxx) {
               case 0:
                  LOGGER.debug("Ping: (<1.3.x) from {}:{}", ☃xx.getAddress(), ☃xx.getPort());
                  String ☃xxxxx = String.format("%s§%d§%d", ☃xxx.getMOTD(), ☃xxx.getCurrentPlayerCount(), ☃xxx.getMaxPlayers());
                  this.writeAndFlush(☃, this.getStringBuffer(☃xxxxx));
                  break;
               case 1:
                  if (☃.readUnsignedByte() != 1) {
                     return;
                  }

                  LOGGER.debug("Ping: (1.4-1.5.x) from {}:{}", ☃xx.getAddress(), ☃xx.getPort());
                  String ☃xxxxxx = String.format(
                     "§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d",
                     127,
                     ☃xxx.getMinecraftVersion(),
                     ☃xxx.getMOTD(),
                     ☃xxx.getCurrentPlayerCount(),
                     ☃xxx.getMaxPlayers()
                  );
                  this.writeAndFlush(☃, this.getStringBuffer(☃xxxxxx));
                  break;
               default:
                  boolean ☃xxxxxx = ☃.readUnsignedByte() == 1;
                  ☃xxxxxx &= ☃.readUnsignedByte() == 250;
                  ☃xxxxxx &= "MC|PingHost".equals(new String(☃.readBytes(☃.readShort() * 2).array(), StandardCharsets.UTF_16BE));
                  int ☃xxxxxxx = ☃.readUnsignedShort();
                  ☃xxxxxx &= ☃.readUnsignedByte() >= 73;
                  ☃xxxxxx &= 3 + ☃.readBytes(☃.readShort() * 2).array().length + 4 == ☃xxxxxxx;
                  ☃xxxxxx &= ☃.readInt() <= 65535;
                  ☃xxxxxx &= ☃.readableBytes() == 0;
                  if (!☃xxxxxx) {
                     return;
                  }

                  LOGGER.debug("Ping: (1.6) from {}:{}", ☃xx.getAddress(), ☃xx.getPort());
                  String ☃xxxxxxxx = String.format(
                     "§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d",
                     127,
                     ☃xxx.getMinecraftVersion(),
                     ☃xxx.getMOTD(),
                     ☃xxx.getCurrentPlayerCount(),
                     ☃xxx.getMaxPlayers()
                  );
                  ByteBuf ☃xxxxxxxxx = this.getStringBuffer(☃xxxxxxxx);

                  try {
                     this.writeAndFlush(☃, ☃xxxxxxxxx);
                  } finally {
                     ☃xxxxxxxxx.release();
                  }
            }

            ☃.release();
            ☃x = false;
         } catch (RuntimeException var21) {
         }
      } finally {
         if (☃x) {
            ☃.resetReaderIndex();
            ☃.channel().pipeline().remove("legacy_query");
            ☃.fireChannelRead(☃);
         }
      }
   }

   private void writeAndFlush(ChannelHandlerContext var1, ByteBuf var2) {
      ☃.pipeline().firstContext().writeAndFlush(☃).addListener(ChannelFutureListener.CLOSE);
   }

   private ByteBuf getStringBuffer(String var1) {
      ByteBuf ☃ = Unpooled.buffer();
      ☃.writeByte(255);
      char[] ☃x = ☃.toCharArray();
      ☃.writeShort(☃x.length);

      for (char ☃xx : ☃x) {
         ☃.writeChar(☃xx);
      }

      return ☃;
   }
}
