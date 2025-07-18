package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class NettyVarint21FrameDecoder extends ByteToMessageDecoder {
   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      ☃.markReaderIndex();
      byte[] ☃ = new byte[3];

      for (int ☃x = 0; ☃x < ☃.length; ☃x++) {
         if (!☃.isReadable()) {
            ☃.resetReaderIndex();
            return;
         }

         ☃[☃x] = ☃.readByte();
         if (☃[☃x] >= 0) {
            PacketBuffer ☃xx = new PacketBuffer(Unpooled.wrappedBuffer(☃));

            try {
               int ☃xxx = ☃xx.readVarInt();
               if (☃.readableBytes() >= ☃xxx) {
                  ☃.add(☃.readBytes(☃xxx));
                  return;
               }

               ☃.resetReaderIndex();
            } finally {
               ☃xx.release();
            }

            return;
         }
      }

      throw new CorruptedFrameException("length wider than 21-bit");
   }
}
