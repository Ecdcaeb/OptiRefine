package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class NettyVarint21FrameEncoder extends MessageToByteEncoder<ByteBuf> {
   protected void encode(ChannelHandlerContext var1, ByteBuf var2, ByteBuf var3) throws Exception {
      int ☃ = ☃.readableBytes();
      int ☃x = PacketBuffer.getVarIntSize(☃);
      if (☃x > 3) {
         throw new IllegalArgumentException("unable to fit " + ☃ + " into " + 3);
      } else {
         PacketBuffer ☃xx = new PacketBuffer(☃);
         ☃xx.ensureWritable(☃x + ☃);
         ☃xx.writeVarInt(☃);
         ☃xx.writeBytes(☃, ☃.readerIndex(), ☃);
      }
   }
}
