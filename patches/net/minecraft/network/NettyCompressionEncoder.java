package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class NettyCompressionEncoder extends MessageToByteEncoder<ByteBuf> {
   private final byte[] buffer = new byte[8192];
   private final Deflater deflater;
   private int threshold;

   public NettyCompressionEncoder(int var1) {
      this.threshold = ☃;
      this.deflater = new Deflater();
   }

   protected void encode(ChannelHandlerContext var1, ByteBuf var2, ByteBuf var3) throws Exception {
      int ☃ = ☃.readableBytes();
      PacketBuffer ☃x = new PacketBuffer(☃);
      if (☃ < this.threshold) {
         ☃x.writeVarInt(0);
         ☃x.writeBytes(☃);
      } else {
         byte[] ☃xx = new byte[☃];
         ☃.readBytes(☃xx);
         ☃x.writeVarInt(☃xx.length);
         this.deflater.setInput(☃xx, 0, ☃);
         this.deflater.finish();

         while (!this.deflater.finished()) {
            int ☃xxx = this.deflater.deflate(this.buffer);
            ☃x.writeBytes(this.buffer, 0, ☃xxx);
         }

         this.deflater.reset();
      }
   }

   public void setCompressionThreshold(int var1) {
      this.threshold = ☃;
   }
}
