package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NettyPacketDecoder extends ByteToMessageDecoder {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.NETWORK_PACKETS_MARKER);
   private final EnumPacketDirection direction;

   public NettyPacketDecoder(EnumPacketDirection var1) {
      this.direction = ☃;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      if (☃.readableBytes() != 0) {
         PacketBuffer ☃ = new PacketBuffer(☃);
         int ☃x = ☃.readVarInt();
         Packet<?> ☃xx = ((EnumConnectionState)☃.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get()).getPacket(this.direction, ☃x);
         if (☃xx == null) {
            throw new IOException("Bad packet id " + ☃x);
         } else {
            ☃xx.readPacketData(☃);
            if (☃.readableBytes() > 0) {
               throw new IOException(
                  "Packet "
                     + ((EnumConnectionState)☃.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get()).getId()
                     + "/"
                     + ☃x
                     + " ("
                     + ☃xx.getClass().getSimpleName()
                     + ") was larger than I expected, found "
                     + ☃.readableBytes()
                     + " bytes extra whilst reading packet "
                     + ☃x
               );
            } else {
               ☃.add(☃xx);
               if (LOGGER.isDebugEnabled()) {
                  LOGGER.debug(
                     RECEIVED_PACKET_MARKER, " IN: [{}:{}] {}", ☃.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get(), ☃x, ☃xx.getClass().getName()
                  );
               }
            }
         }
      }
   }
}
