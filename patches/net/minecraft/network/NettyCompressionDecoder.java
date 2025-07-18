package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import java.util.zip.Inflater;

public class NettyCompressionDecoder extends ByteToMessageDecoder {
   private final Inflater inflater;
   private int threshold;

   public NettyCompressionDecoder(int var1) {
      this.threshold = ☃;
      this.inflater = new Inflater();
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      if (☃.readableBytes() != 0) {
         PacketBuffer ☃ = new PacketBuffer(☃);
         int ☃x = ☃.readVarInt();
         if (☃x == 0) {
            ☃.add(☃.readBytes(☃.readableBytes()));
         } else {
            if (☃x < this.threshold) {
               throw new DecoderException("Badly compressed packet - size of " + ☃x + " is below server threshold of " + this.threshold);
            }

            if (☃x > 2097152) {
               throw new DecoderException("Badly compressed packet - size of " + ☃x + " is larger than protocol maximum of " + 2097152);
            }

            byte[] ☃xx = new byte[☃.readableBytes()];
            ☃.readBytes(☃xx);
            this.inflater.setInput(☃xx);
            byte[] ☃xxx = new byte[☃x];
            this.inflater.inflate(☃xxx);
            ☃.add(Unpooled.wrappedBuffer(☃xxx));
            this.inflater.reset();
         }
      }
   }

   public void setCompressionThreshold(int var1) {
      this.threshold = ☃;
   }
}
