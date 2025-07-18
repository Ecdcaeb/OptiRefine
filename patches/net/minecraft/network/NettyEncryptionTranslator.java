package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptionTranslator {
   private final Cipher cipher;
   private byte[] inputBuffer = new byte[0];
   private byte[] outputBuffer = new byte[0];

   protected NettyEncryptionTranslator(Cipher var1) {
      this.cipher = ☃;
   }

   private byte[] bufToBytes(ByteBuf var1) {
      int ☃ = ☃.readableBytes();
      if (this.inputBuffer.length < ☃) {
         this.inputBuffer = new byte[☃];
      }

      ☃.readBytes(this.inputBuffer, 0, ☃);
      return this.inputBuffer;
   }

   protected ByteBuf decipher(ChannelHandlerContext var1, ByteBuf var2) throws ShortBufferException {
      int ☃ = ☃.readableBytes();
      byte[] ☃x = this.bufToBytes(☃);
      ByteBuf ☃xx = ☃.alloc().heapBuffer(this.cipher.getOutputSize(☃));
      ☃xx.writerIndex(this.cipher.update(☃x, 0, ☃, ☃xx.array(), ☃xx.arrayOffset()));
      return ☃xx;
   }

   protected void cipher(ByteBuf var1, ByteBuf var2) throws ShortBufferException {
      int ☃ = ☃.readableBytes();
      byte[] ☃x = this.bufToBytes(☃);
      int ☃xx = this.cipher.getOutputSize(☃);
      if (this.outputBuffer.length < ☃xx) {
         this.outputBuffer = new byte[☃xx];
      }

      ☃.writeBytes(this.outputBuffer, 0, this.cipher.update(☃x, 0, ☃, this.outputBuffer));
   }
}
