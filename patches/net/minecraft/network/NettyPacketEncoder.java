package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NettyPacketEncoder extends MessageToByteEncoder<Packet<?>> {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.NETWORK_PACKETS_MARKER);
   private final EnumPacketDirection direction;

   public NettyPacketEncoder(EnumPacketDirection var1) {
      this.direction = ☃;
   }

   protected void encode(ChannelHandlerContext var1, Packet<?> var2, ByteBuf var3) throws Exception {
      EnumConnectionState ☃ = (EnumConnectionState)☃.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get();
      if (☃ == null) {
         throw new RuntimeException("ConnectionProtocol unknown: " + ☃.toString());
      } else {
         Integer ☃x = ☃.getPacketId(this.direction, ☃);
         if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(RECEIVED_PACKET_MARKER, "OUT: [{}:{}] {}", ☃.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get(), ☃x, ☃.getClass().getName());
         }

         if (☃x == null) {
            throw new IOException("Can't serialize unregistered packet");
         } else {
            PacketBuffer ☃xx = new PacketBuffer(☃);
            ☃xx.writeVarInt(☃x);

            try {
               ☃.writePacketData(☃xx);
            } catch (Throwable var8) {
               LOGGER.error(var8);
            }
         }
      }
   }
}
